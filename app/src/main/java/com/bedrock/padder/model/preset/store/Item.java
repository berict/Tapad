package com.bedrock.padder.model.preset.store;

import android.util.Log;

import com.bedrock.padder.model.preset.Preset;
import com.bedrock.padder.model.preset.PresetSchema;

/*
* Wrapper class for saving download status
* */

public class Item {

    private PresetSchema presetSchema;

    private long bytesTransferred = -1;

    private long totalByteCount = -1;

    private int index = -1;

    public Item(PresetSchema presetSchema, int index) {
        this.presetSchema = presetSchema;
        this.index = index;
    }

    public Item(PresetSchema presetSchema) {
        // for selected
        this.presetSchema = presetSchema;
    }

    public Preset getPreset() {
        return presetSchema.getPreset();
    }

    public PresetSchema getPresetSchema() {
        return presetSchema;
    }

    public boolean isDownloading() {
        return bytesTransferred != -1 && totalByteCount != -1;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object item) {
        try {
            return getPresetSchema().equals(((Item) item).getPresetSchema());
        } catch (Exception e) {
            Log.e("Store.Item", "equals(), cannot compare with another object");
            return false;
        }
    }

    public long getBytesTransferred() {
        return bytesTransferred;
    }

    public long getTotalByteCount() {
        return totalByteCount;
    }

    public void setBytesTransferred(long bytesTransferred) {
        this.bytesTransferred = bytesTransferred;
    }

    public void setTotalByteCount(long totalByteCount) {
        this.totalByteCount = totalByteCount;
    }
}
