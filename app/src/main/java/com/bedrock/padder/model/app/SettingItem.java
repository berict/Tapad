package com.bedrock.padder.model.app;

import android.content.SharedPreferences;

public class SettingItem<T> {

    private T item;

    private final Class<T> type;

    public SettingItem(T item, Class<T> type) {
        this.item = item;
        this.type = type;
    }

    public void setItem(SharedPreferences prefs) {
        if (prefs != null) {

        } else {

        }
    }

    public Class<T> getType() {
        return this.type;
    }
}
