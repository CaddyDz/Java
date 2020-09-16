package com.marsool.firetool.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

import com.marsool.firetool.R;
import com.marsool.firetool.SharedPrefManager;

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

                            String pending_logout = SharedPrefManager.getInstance(ctx).getPendingLogout();
                            if (pending_logout != null) {
                                ApiCall logout = new ApiCall(ctx.getString(R.string.api_base) + "logout",
                                        new Handler() {
                                            @Override
                                            public void handleResponse(HttpResponse text) {
                                                SharedPrefManager.getInstance(ctx).deletePendingLogout();
                                                new android.os.Handler((Looper.getMainLooper())).post(onSuccess);
                                            }

                                            @Override
                                            public void handleError(Exception x) {

                                            }
                                        });
                                logout.addHeader("Authorization", "Bearer " + pending_logout);
                                logout.execute();
                            }else{
                                new android.os.Handler((Looper.getMainLooper())).post(onSuccess);
                            }
                        }

                        @Override
                        public void handleError(Exception x) {
                            new android.os.Handler((Looper.getMainLooper())).post(onFail);
                        }
                    });
                    test.setTimeout(4000);
                    test.execute();
                } else {
                    new android.os.Handler((Looper.getMainLooper())).post(onFail);
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
