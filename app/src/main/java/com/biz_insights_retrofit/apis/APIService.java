package com.biz_insights_retrofit.apis;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIService {
    @Headers("Content-Type:application/json")
    @POST
    Call<ResponseBody> post(@Url String url, @Body RequestBody requestBody);
}
