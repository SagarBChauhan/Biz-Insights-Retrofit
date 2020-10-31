package com.biz_insights_retrofit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.biz_insights_retrofit.adapters.UserListAdapter;
import com.biz_insights_retrofit.apis.APIService;
import com.biz_insights_retrofit.apis.ConnectionDetector;
import com.biz_insights_retrofit.apis.ProgressUtil;
import com.biz_insights_retrofit.apis.RetrofitClient;
import com.biz_insights_retrofit.models.UserDataModel;
import com.biz_insights_retrofit.utility.Globals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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
public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.iv_logout)
    AppCompatImageView iv_logout;
    @BindView(R.id.iv_back)
    AppCompatImageView iv_back;
    UserListAdapter userListAdapter;
    Gson gson;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setupToolbar();
        gson = new GsonBuilder().create();
        globals = (Globals) getApplicationContext();
        getDataResponse();
    }

    @OnClick({R.id.iv_logout})
    public void onClickAction(View view) {
        if (view.getId() == R.id.iv_logout) {
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure want logout?")
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(getString(R.string.logout), (dialog, which) -> {
                        Globals globals = (Globals) getApplicationContext();
                        globals.setLoginData(null);
                        if (globals.getLoginData() == null) {
                            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ConnectionDetector.internetCheck(DashboardActivity.this, true))
            return;
    }

    private void getDataResponse() {
        if (!ConnectionDetector.internetCheck(DashboardActivity.this, true))
            return;

        APIService apiService = RetrofitClient.getClient(getString(R.string.base_url)).create(APIService.class);
        JSONObject params = new JSONObject();
        try {
            params.put("page_no", 1);
            params.put("page_record", 50);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> call = apiService.post(getString(R.string.get_users_list), RequestBody.create(MediaType.parse("application/json;"), (params).toString()));
        KProgressHUD progressFlower = ProgressUtil.initProgressBar(DashboardActivity.this);
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
                        UserDataModel dataModel = gson.fromJson(String.valueOf(object), UserDataModel.class);
                        userListAdapter = new UserListAdapter(dataModel.data.rows);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                        recyclerView.setAdapter(userListAdapter);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                progressFlower.dismiss();
                Logger.e(Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        iv_logout.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.text_dashbord));
    }
}