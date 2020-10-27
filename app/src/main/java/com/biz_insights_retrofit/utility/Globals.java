package com.biz_insights_retrofit.utility;

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
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    //GET Login data
    public LoginDataModel getLoginData() {
        return toUserData(getSharedPref().getString(Constants.USER_MAP, null));
    }

    //SET Login data
    public void setLoginData(LoginDataModel userData) {
        getEditor().putString(Constants.USER_MAP, userDataToJson(userData));
    }

    private SharedPreferences getSharedPref() {
        return sharedPreferences = (sharedPreferences == null) ? getSharedPreferences(Constants.secrets, Context.MODE_PRIVATE) : sharedPreferences;
    }

    private LoginDataModel toUserData(String params) {
        if (params == null) {
            return null;
        }
        Type mapType = new TypeToken<LoginDataModel>() {
        }.getType();
        return new Gson().fromJson(params, mapType);
    }

    private SharedPreferences.Editor getEditor() {
        return editor = (editor == null) ? getSharedPref().edit() : editor;
    }

    private String userDataToJson(LoginDataModel userData) {
        if (userData == null) {
            return null;
        }
        Type mapType = new TypeToken<LoginDataModel>() {
        }.getType();
        return new Gson().toJson(userData, mapType);
    }
}
