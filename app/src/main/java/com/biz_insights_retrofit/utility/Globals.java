package com.biz_insights_retrofit.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import com.biz_insights_retrofit.models.LoginDataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;

public class Globals extends MultiDexApplication {
    @SuppressLint("StaticFieldLeak")
    static Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public static Context getContext() {
        return context;
    }

    public static String userDatatoJson(LoginDataModel userDetails) {
        if (userDetails == null) {
            return null;
        }
        Type mapType = new TypeToken<LoginDataModel>() {
        }.getType();
        Gson gson = new Gson();
        return gson.toJson(userDetails, mapType);
    }

    public static LoginDataModel toUserData(String params) {
        if (params == null)
            return null;

        Type mapType = new TypeToken<LoginDataModel>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(params, mapType);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public SharedPreferences getSharedPref() {
        return sp = (sp == null) ? getSharedPreferences(Constants.secrets, Context.MODE_PRIVATE) : sp;
    }

    public SharedPreferences.Editor getEditor() {
        return editor = (editor == null) ? getSharedPref().edit() : editor;
    }

    //New Method
    public LoginDataModel getLoginData() {
        return toUserData(getSharedPref().getString(Constants.USER_MAP, null));
    }

    //New Method
    public void setLoginData(LoginDataModel userData) {
        getEditor().putString(Constants.USER_MAP, userDatatoJson(userData));
        getEditor().commit();
    }
}

