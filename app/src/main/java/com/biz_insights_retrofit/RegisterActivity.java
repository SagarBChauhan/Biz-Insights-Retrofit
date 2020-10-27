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

import com.biz_insights_retrofit.utility.FormValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.tv_login, R.id.btn_register})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_register:
                progressBar.setVisibility(View.VISIBLE);
                btn_register.setVisibility(View.GONE);
                if (isFormValidate()) {
                    //perform action
                    toggleProgressVisibility(progressBar, btn_register);
                } else {
                    Toast.makeText(this, getString(R.string.msg_registration_failed), Toast.LENGTH_SHORT).show();
                    toggleProgressVisibility(progressBar, btn_register);
                }
                break;
        }
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