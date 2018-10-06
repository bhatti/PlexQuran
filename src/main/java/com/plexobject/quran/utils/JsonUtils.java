package com.plexobject.quran.utils;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonUtils {
    public static String getJsonString(final JSONObject json, final String name)
            throws JSONException {
        String value = json.has(name) ? json.getString(name) : null;
        if (value != null) {
            value = value.trim();
            if (value.equalsIgnoreCase("null") || value.equalsIgnoreCase("nil")) {
                value = null;
            }
        }
        return value;
    }

    public static boolean getJsonBoolean(JSONObject json, String name)
            throws JSONException {
        String value = json.has(name) ? json.getString(name) : null;
        if (value != null) {
            value = value.trim();
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) {
                return true;
            }
        }
        return false;
    }

    public static int getJsonInt(JSONObject json, String name)
            throws JSONException {
        String value = json.has(name) ? json.getString(name) : null;
        if (value != null) {
            value = value.trim();
            return Integer.parseInt(value);
        }
        return -1;
    }

    public static long getJsonLong(JSONObject json, String name)
            throws JSONException {
        String value = json.has(name) ? json.getString(name) : null;
        if (value != null) {
            value = value.trim();
            return Long.parseLong(value);
        }
        return -1;
    }
}
