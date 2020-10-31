package com.biz_insights_retrofit.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDataModel implements Serializable {

    @SerializedName("data")
    public Data data;
    @SerializedName("msg")
    public String msg;
    @SerializedName("res")
    public String res;

    public static class Data {
        @SerializedName("rows")
        public ArrayList<Rows> rows;
        @SerializedName("count")
        public int count;
    }

    public static class Rows {
        @SerializedName("password")
        public String password;
        @SerializedName("address")
        public String address;
        @SerializedName("mobile_no")
        public String mobile_no;
        @SerializedName("email_id")
        public String email_id;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("user_id")
        public int user_id;
    }
}
