package com.marsool.firetool.utils.networking;

import android.os.AsyncTask;
import android.os.SystemClock;

import java.util.HashMap;

public class ApiCall extends AsyncTask<Void, HttpResponse, HttpResponse> {
    private boolean disclosed = false;

    private String baseUrl;
    private HashMap<String, String> params;
    private Handler handler;

    public ApiCall(String baseUrl, Handler handler, Param... prms) {
        this.baseUrl = baseUrl;
        params = new HashMap<String, String>();
        for (Param p : prms) {
            params.put(p.getKey(), p.getValue());
        }
        this.handler = handler;
    }

    @Override
    protected HttpResponse doInBackground(Void... voids) {
        Thread observer = new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(8000);
                if (!disclosed) {
                    disclosed = true;
                    handler.handleError("Connection timed out!");
                }
            }
        };
        try {
            long start = System.currentTimeMillis();
            HttpResponse res = new RequestHandler().sendPostRequest(baseUrl, params);
            long took = System.currentTimeMillis() - start;
            SystemClock.sleep(Math.max(1500 - took,0));
            return res;
        } catch (Exception x) {
            if(!disclosed) {
                disclosed = true;
                handler.handleError(x.getClass().getName());
                x.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(HttpResponse s) {
        if(!disclosed) {
            disclosed = true;
            handler.handleResponse(s);
        }
    }
}
