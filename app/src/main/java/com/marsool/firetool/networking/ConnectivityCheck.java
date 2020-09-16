package com.marsool.firetool.networking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.marsool.firetool.MainActivity;
import com.marsool.firetool.NoInternet;

public class ConnectivityCheck {
    private Context ctx;

    private Runnable onSuccess;
    private Runnable onFail;
    private ProgressListener onProgress;

    public ConnectivityCheck(Context ctx, ProgressListener onProgress, Runnable onSuccess, Runnable onFail) {
        this.ctx = ctx;
        this.onProgress = onProgress;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }

    public void execute() {
        new Thread() {
            public void run() {
                if (isNetworkConnected()) {
                    onProgress.onProgess(40);
                    ApiCall test = new ApiCall("https://google.com", new Handler() {
                        @Override
                        public void handleResponse(HttpResponse text) {
                            onProgress.onProgess(100);
                            onSuccess.run();
                        }
                        @Override
                        public void handleError(Exception x) {
                            onFail.run();
                        }
                    });
                    test.setTimeout(4000);
                    test.execute();
                } else {
                    onFail.run();
                }
            }
        }.start();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
