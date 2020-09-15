package com.marsool.firetool.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Belal on 9/5/2017.
 */

public class RequestHandler {

    //this method will send a post request to the specified url
    //in this app we are using only post request
    //in the hashmap we have the data to be sent to the server in keyvalue pairs
    public HttpResponse sendPostRequest(String requestURL, HashMap<String, String> header, HashMap<String, String> postDataParams) throws IOException {
        URL url;
        StringBuilder sb = new StringBuilder();
        url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        for (String key : header.keySet()) {
            conn.setRequestProperty(key, header.get(key));
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(postDataParams));

        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();

        sb = new StringBuilder();
        InputStream ris = null;
        if (responseCode == 200) {
            ris = conn.getInputStream();
        } else if (responseCode == 403 || responseCode == 422) {
            ris = conn.getErrorStream();
        }
        if(ris != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(ris));
            String response;
            while ((response = br.readLine()) != null) {
                sb.append(response);
            }
        }
        return new HttpResponse(responseCode, sb.toString());
    }


    //this method is converting keyvalue pairs data into a query string as needed to send to the server
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
