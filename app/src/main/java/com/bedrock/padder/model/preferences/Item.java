package com.bedrock.padder.model.preferences;

import android.util.Log;

public class Item {

    private final String key;

    private Object value = null;

    public Item(String key) {
        this.key = key;
    }

    public Item(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getValue() {
        if (value == null) {
            Log.e("Item", "returned null value");
        }
        return value;
    }

    public Integer getIntegerValue() {
        return (Integer) getValue();
    }

    public String getStringValue() {
        return (String) getValue();
    }

    public Double getDoubleValue() {
        return (Double) getValue();
    }

    public Boolean getBooleanValue() {
        return (Boolean) getValue();
    }

    public Long getLongValue() {
        return (Long) getValue();
    }

    public ItemColor getColorValue() {
        return (ItemColor) getValue();
    }

    public boolean contains() {
        return !(value == null);
    }

    public String getKey() {
        return key;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "[" + key + ", " + value.toString() + "]";
    }
}
