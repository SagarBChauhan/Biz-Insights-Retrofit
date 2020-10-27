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

import com.biz_insights_retrofit.models.LoginDataModel;
import com.biz_insights_retrofit.utility.FormValidation;
import com.biz_insights_retrofit.utility.Globals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                    //preform action
                    toggleProgressVisibility(progressBar, btn_login);
                } else {
                    toggleProgressVisibility(progressBar, btn_login);
                    Toast.makeText(this, getString(R.string.msg_login_failed), Toast.LENGTH_SHORT).show();
                }
                break;
        }
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