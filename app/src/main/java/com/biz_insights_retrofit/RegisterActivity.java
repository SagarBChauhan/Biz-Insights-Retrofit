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
import com.biz_insights_retrofit.apis.ConnectionDetector;
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
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_first_name)
    AppCompatEditText et_first_name;
    @BindView(R.id.et_last_name)
    AppCompatEditText et_last_name;
    @BindView(R.id.et_address)
    AppCompatEditText et_address;
    @BindView(R.id.et_email)
    AppCompatEditText et_email;
    @BindView(R.id.et_mobile)
    AppCompatEditText et_mobile;
    @BindView(R.id.et_password)
    AppCompatEditText et_password;
    @BindView(R.id.et_confirm_password)
    AppCompatEditText et_confirm_password;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_register)
    AppCompatButton btn_register;

    LoginDataModel loginDataModel;
    Globals globals;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        ButterKnife.bind(this);

        loginDataModel = new LoginDataModel();
        globals = (Globals) getApplicationContext();
        gson = new GsonBuilder().create();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.tv_login, R.id.btn_register})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_register:
                toggleProgressVisibility(progressBar, btn_register);
                if (isFormValidate()) {
                    doRequestForRegister();
                } else {
                    Toast.makeText(this, getString(R.string.msg_registration_failed), Toast.LENGTH_SHORT).show();
                }
                toggleProgressVisibility(progressBar, btn_register);
                break;
        }
    }

    private void doRequestForRegister() {
        if (!ConnectionDetector.internetCheck(RegisterActivity.this, true))
            return;

        APIService apiService = RetrofitClient.getClient(getString(R.string.base_url)).create(APIService.class);
        JSONObject postData = new JSONObject();
        try {
            postData.put(KeyConstants.First_name, et_first_name.getText().toString().trim());
            postData.put(KeyConstants.Last_name, et_last_name.getText().toString().trim());
            postData.put(KeyConstants.Address, et_address.getText().toString().trim());
            postData.put(KeyConstants.Email_id, et_email.getText().toString().trim());
            postData.put(KeyConstants.Mobile_no, et_mobile.getText().toString().trim());
            postData.put(KeyConstants.Password, et_password.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> call = apiService.post(getString(R.string.register), RequestBody.create(MediaType.parse("Content-Type:application/jason;"), (postData).toString()));
        KProgressHUD progressFlower = ProgressUtil.initProgressBar(RegisterActivity.this);
        progressFlower.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                progressFlower.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String data = response.body().string();
                        Logger.json(data);
                        JSONObject object = new JSONObject(data);
                        loginDataModel = gson.fromJson(object.toString(), LoginDataModel.class);

                        if ("Registered successfully.".equals(loginDataModel.msg)) {
                            Toast.makeText(RegisterActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, loginDataModel.msg, Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                progressFlower.dismiss();
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Logger.e(t.getMessage());
            }
        });
    }

    private void toggleProgressVisibility(ProgressBar progressBar, AppCompatButton btn_register) {
        if (progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
        else progressBar.setVisibility(View.VISIBLE);

        if (btn_register.getVisibility() == View.VISIBLE) btn_register.setVisibility(View.GONE);
        else btn_register.setVisibility(View.VISIBLE);
    }

    private boolean isFormValidate() {
        if (new FormValidation().checkEmptyEditText(et_first_name)) {
            et_first_name.setError(getString(R.string.msg_first_name_empty));
            et_first_name.requestFocus();
            return false;
        }
        if (new FormValidation().checkEmptyEditText(et_last_name)) {
            et_last_name.setError(getString(R.string.msg_last_name_empty));
            et_last_name.requestFocus();
            return false;
        }
        if (new FormValidation().checkEmptyEditText(et_address)) {
            et_address.setError(getString(R.string.msg_address_empty));
            et_address.requestFocus();
            return false;
        }
        if (new FormValidation().checkEmptyEditText(et_email)) {
            et_email.setError(getString(R.string.msg_email_empty));
            et_email.requestFocus();
            return false;
        } else {
            if (!new FormValidation().checkEmail(et_email)) {
                et_email.setError(getString(R.string.msg_email_invalid));
                et_email.requestFocus();
                return false;
            }
        }
        if (new FormValidation().checkEmptyEditText(et_mobile)) {
            et_mobile.setError(getString(R.string.msg_mobile_no_empty));
            et_mobile.requestFocus();
            return false;
        } else {
            if (!new FormValidation().checkMobileNumber(et_mobile)) {
                et_mobile.setError(getString(R.string.msg_mobile_no_invalid));
                et_mobile.requestFocus();
                return false;
            }
        }
        if (new FormValidation().checkEmptyEditText(et_password)) {
            et_password.setError(getString(R.string.msg_password_empty));
            et_password.requestFocus();
            return false;
        } else {
            if (!new FormValidation().checkPassword(et_password)) {
                et_password.setError(getString(R.string.msg_password_invalid));
                et_password.requestFocus();
                return false;
            }
        }
        if (new FormValidation().checkEmptyEditText(et_confirm_password)) {
            et_confirm_password.setError(getString(R.string.msg_confirm_password_empty));
            et_confirm_password.requestFocus();
            return false;
        } else {
            if (!new FormValidation().checkPassword(et_confirm_password)) {
                et_confirm_password.setError(getString(R.string.msg_confirm_password_invalid));
                et_confirm_password.requestFocus();
                return false;
            }
        }
        if (!new FormValidation().checkConfirmPassword(et_password, et_confirm_password)) {
            et_confirm_password.setError(getString(R.string.msg_password_not_matched));
            et_confirm_password.requestFocus();
            return false;
        }
        return true;
    }

}