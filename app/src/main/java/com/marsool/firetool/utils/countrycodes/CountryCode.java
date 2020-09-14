package com.marsool.firetool.utils.countrycodes;

import org.json.JSONException;
import org.json.JSONObject;

public class CountryCode implements Comparable<CountryCode>{
    private String name;
    private String dialPrefix;
    private String ISOCode;

    public CountryCode(String name, String dialPrefix, String ISOCode) {
        this.name = name;
        this.dialPrefix = dialPrefix;
        this.ISOCode = ISOCode;
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
