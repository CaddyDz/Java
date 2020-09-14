package com.marsool.firetool.utils.assets;

import android.content.Context;

import com.marsool.firetool.utils.countrycodes.CountryCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AssetLoader {
    public static ArrayList<CountryCode> loadCountryCodes(Context context) {
        ArrayList<CountryCode> res = new ArrayList<CountryCode>();

        String jsonString = readStringFromAsset("countryCodes.json",context);
        try {
            JSONArray array = new JSONArray(jsonString);
            for(int i = 0;i<array.length();i++) {
                JSONObject obj = array.getJSONObject(i);
                res.add(new CountryCode(obj.getString("name"),obj.getString("dialPrefix"),obj.getString("ISOCode")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    private static String readStringFromAsset(String assetName, Context context) {
        String res = null;
        try {
            InputStream is = context.getAssets().open(assetName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            res = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return res;
    }
}
