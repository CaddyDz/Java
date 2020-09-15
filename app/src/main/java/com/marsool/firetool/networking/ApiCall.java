package com.marsool.firetool.networking;

import android.os.AsyncTask;
import android.os.SystemClock;

import java.net.UnknownHostException;
import java.util.HashMap;

public class ApiCall extends AsyncTask<Void, HttpResponse, HttpResponse> {
    private boolean disclosed = false;

    private String baseUrl;
    private HashMap<String, String> header;
    private HashMap<String, String> params;
    private Handler handler;

    public ApiCall(String baseUrl, Handler handler, Param... prms) {
        this.baseUrl = baseUrl;
        params = new HashMap<String, String>();
        for (Param p : prms) {
            params.put(p.getKey(), p.getValue());
        }
        header = new HashMap<String, String>();
        this.handler = handler;
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    @Override
    protected HttpResponse doInBackground(Void... voids) {
        Thread observer = new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(8000);
                if (!disclosed) {
                    disclosed = true;
                    handler.handleError(new UnknownHostException());
                }
            }
        };
        observer.start();
        try {
            long start = System.currentTimeMillis();
            HttpResponse res = new RequestHandler().sendPostRequest(baseUrl, header, params);
            long took = System.currentTimeMillis() - start;
            SystemClock.sleep(Math.max(1500 - took,0));
            return res;
        } catch (Exception x) {
            if(!disclosed) {
                disclosed = true;
                handler.handleError(x);
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
