package com.biz_insights_retrofit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.biz_insights_retrofit.apis.APIService;
import com.biz_insights_retrofit.apis.KeyConstants;
import com.biz_insights_retrofit.apis.ProgressUtil;
import com.biz_insights_retrofit.apis.RetrofitClient;
import com.biz_insights_retrofit.models.LoginDataModel;
import com.biz_insights_retrofit.utility.FormValidation;
import com.biz_insights_retrofit.utility.Globals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.et_username)
    AppCompatEditText et_username;
    @BindView(R.id.et_password)
    AppCompatEditText et_password;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_login)
    AppCompatButton btn_login;

    LoginDataModel loginDataModel;
    Gson gson;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        ButterKnife.bind(this);

        loginDataModel = new LoginDataModel();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        globals = (Globals) getApplicationContext();
    }

    @OnClick({R.id.tv_register, R.id.btn_login})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_login:
                toggleProgressVisibility(progressBar, btn_login);
                if (isFormValidate()) {
                    doRequestForLogin();
                } else {
                    toggleProgressVisibility(progressBar, btn_login);
                    Toast.makeText(this, getString(R.string.msg_login_failed), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void doRequestForLogin() {
        APIService apiService = RetrofitClient.getClient(getString(R.string.base_url)).create(APIService.class);
        JSONObject postData = new JSONObject();
        try {
            postData.put(KeyConstants.Username, et_username.getText().toString().trim());
            postData.put(KeyConstants.Password, et_password.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> call = apiService.post(getString(R.string.login), RequestBody.create(MediaType.parse("application/json;"), (postData).toString()));
        Logger.d("CALL", "" + call.request().toString());
        KProgressHUD progressFlower = ProgressUtil.initProgressBar(LoginActivity.this);
        progressFlower.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                progressFlower.dismiss();
                toggleProgressVisibility(progressBar, btn_login);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String data = response.body().string();
                        Logger.json(data);
                        JSONObject object = new JSONObject(data);
                        loginDataModel = gson.fromJson(String.valueOf(object), LoginDataModel.class);

                        if (loginDataModel.data != null) {
                            globals.setLoginData(loginDataModel);
                            Toast.makeText(LoginActivity.this, loginDataModel.msg, Toast.LENGTH_SHORT).show();
//                            start Next Activity
//                            finish();
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                progressFlower.dismiss();
                toggleProgressVisibility(progressBar, btn_login);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Logger.e(t.getMessage());
            }
        });
    }

    private void toggleProgressVisibility(ProgressBar progressBar, AppCompatButton btn_login) {
        if (progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
        else progressBar.setVisibility(View.VISIBLE);

        if (btn_login.getVisibility() == View.VISIBLE) btn_login.setVisibility(View.GONE);
        else btn_login.setVisibility(View.VISIBLE);
    }

    private boolean isFormValidate() {
        if (new FormValidation().checkEmptyEditText(et_username)) {
            et_username.requestFocus();
            et_username.setError(getString(R.string.msg_username_empty));
            return false;
        }
        if (new FormValidation().checkEmptyEditText(et_password)) {
            et_password.requestFocus();
            et_password.setError(getString(R.string.msg_password_empty));
            return false;
        } else {
            if (!new FormValidation().checkPassword(et_password)) {
                et_password.requestFocus();
                et_password.setError(getString(R.string.password_requirement));
                return false;
            }
        }
        return true;
    }
}