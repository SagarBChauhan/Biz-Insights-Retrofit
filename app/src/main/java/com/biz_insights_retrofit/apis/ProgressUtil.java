package com.biz_insights_retrofit.apis;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.kaopiz.kprogresshud.KProgressHUD;

public class ProgressUtil {
    public static com.biz_insights_retrofit.apis.ProgressUtil mInstance = null;

    public static com.biz_insights_retrofit.apis.ProgressUtil getInstance() {
        if (mInstance == null) {
            mInstance = new com.biz_insights_retrofit.apis.ProgressUtil();
        }
        return mInstance;
    }

    public static KProgressHUD initProgressBar(Context context) {
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
                .show();
    }

    public void showDialog(KProgressHUD dialog, ProgressBar pb, boolean isLoaderRequired) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        } else if (pb != null && isLoaderRequired) {
            pb.setVisibility(View.VISIBLE);
        }
    }

    public void hideDialog(KProgressHUD dialog, ProgressBar pb) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else if (pb != null) {
            pb.setVisibility(View.GONE);
        }
    }
}