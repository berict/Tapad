package com.bedrock.padder.model.app;

import android.content.SharedPreferences;
import android.util.Log;

public class SettingItem {

    private SharedPreferences prefs = null;

    private String key = null;

    private Object item = null;

    private String TAG = "SettingItem";

    public SettingItem(SharedPreferences prefs, String key, Object defValue) {
        this.prefs = prefs;
        this.key = key;
        this.item = defValue;
        fetch();
    }

    public void set(Object value) {
        if (prefs != null) {
            item = value;
            if (item instanceof Boolean) {
                // boolean variable
                prefs.edit().putBoolean(key, (boolean) value).apply();
            } else if (item instanceof Float) {
                // float variable
                prefs.edit().putFloat(key, (float) value).apply();
            } else if (item instanceof String) {
                // string variable
                prefs.edit().putString(key, (String) value).apply();
            }
        } else {
            Log.d(TAG, "Prefs null");
        }
    }

    public Object get() {
        if (item != null) {
            fetch();
            return item;
        } else {
            Log.e(TAG, "Item null");
            return null;
        }
    }

    public void fetch() {
        if (prefs != null) {
            if (item instanceof Boolean) {
                // boolean variable
                item = prefs.getBoolean(key, (boolean) item);
            } else if (item instanceof Float) {
                // float variable
                item = prefs.getFloat(key, (float) item);
            } else if (item instanceof String) {
                // string variable
                item = prefs.getString(key, (String) item);
            }
        } else {
            Log.d(TAG, "Prefs null");
        }
    }
}
