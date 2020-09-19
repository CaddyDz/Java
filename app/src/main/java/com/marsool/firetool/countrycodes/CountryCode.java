package com.marsool.firetool.countrycodes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryCode implements Comparable<CountryCode>{
    private String name;
    private String dialPrefix;
    private String ISOCode;
    private Bitmap flag;

    public CountryCode(String name, String dialPrefix, String ISOCode) {
        this.name = name;
        this.dialPrefix = dialPrefix;
        this.ISOCode = ISOCode;
    }

    private static Bitmap downloadFlag(String code) {
        try {
            URL url = new URL("https://www.countryflags.io/"+code.toLowerCase()+"/flat/32.png");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();
            Bitmap res = BitmapFactory.decodeStream(is);
            return res;
        }catch(Exception x) {
            x.printStackTrace();
            return null;
        }
    }

    public Bitmap getFlag() {
        if(flag == null) {
            flag = downloadFlag(getISOCode());
        }
        return flag;
    }

    public String getName() {
        return name;
    }

    public String getDialPrefix() {
        return "+" + dialPrefix.split(",")[0].trim();
    }

    public String getISOCode() {
        return ISOCode.split(" /")[0].toLowerCase();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("dialPrefix", dialPrefix);
        obj.put("ISOCode", ISOCode);
        return obj;
    }

    @Override
    public int compareTo(CountryCode o) {
        return name.compareTo(o.getName());
    }
}
