package com.bedrock.padder.model.app;

import android.content.SharedPreferences;
import android.util.Log;

public class SettingItem {

    private Object item;

    private String TAG = "SettingItem";

    public SettingItem(Object item) {
        this.item = item;
    }

    public void setItem(SharedPreferences prefs, String key, Object value) {
        if (prefs != null) {
            if (item instanceof Boolean) {
                // boolean variable
                item = value;
                prefs.edit().putBoolean(key, (boolean) value).apply();
            } else if (item instanceof Float) {
                // float variable
                item = value;
                prefs.edit().putFloat(key, (float) value).apply();
            } else if (item instanceof String) {
                // string variable
                item = value;
                prefs.edit().putString(key, (String) value).apply();
            }
        } else {
            Log.d(TAG, "Prefs null");
        }
    }

    public Object getItem() {
        return item;
    }

    public void fetchItem(SharedPreferences prefs, String key, Object defValue) {
        if (item instanceof Boolean) {
            // boolean variable
            item = prefs.getBoolean(key, (boolean) defValue);
        } else if (item instanceof Float) {
            // float variable
            item = prefs.getFloat(key, (float) defValue);
        } else if (item instanceof String) {
            // string variable
            item = prefs.getString(key, (String) defValue);
        }
    }
}
