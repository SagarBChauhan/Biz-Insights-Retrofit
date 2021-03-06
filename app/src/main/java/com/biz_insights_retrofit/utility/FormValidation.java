package com.biz_insights_retrofit.utility;

import android.util.Patterns;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidation {
    public boolean checkEmptyEditText(AppCompatEditText editText) {
        return Objects.requireNonNull(editText.getText()).toString().trim().isEmpty();
    }

    public boolean checkMobileNumber(AppCompatEditText editText) {
        if (Objects.requireNonNull(editText.getText()).toString().length() > 10) {
            return false;
        } else if (editText.getText().toString().length() < 10) {
            return false;
        } else return Patterns.PHONE.matcher(editText.getText().toString().trim()).matches();
    }

    public boolean checkEmail(AppCompatEditText editText) {
        return Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(editText.getText()).toString().trim()).matches();
    }

    public boolean checkPassword(AppCompatEditText editText) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(Objects.requireNonNull(editText.getText()).toString().trim());
        return matcher.matches();
    }

    public boolean checkConfirmPassword(AppCompatEditText editTextPassword, AppCompatEditText editTextConfirmPassword) {
        return Objects.requireNonNull(editTextPassword.getText()).toString().equals(Objects.requireNonNull(editTextConfirmPassword.getText()).toString());
    }
}
