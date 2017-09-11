package com.bedrock.padder.model;

import com.bedrock.padder.model.preset.PresetSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Schema {

    private PresetSchema[] presets;

    private Integer version;

    public Schema(PresetSchema[] presets, Integer version) {
        this.presets = presets;
        this.version = version;
        if (presets != null) {
            for (PresetSchema preset : this.presets) {
                // null values to shorten the json
                preset.getPreset().getAbout().setBio(null);
                preset.getPreset().getAbout().setDetails(null);
            }
        }
    }

    public void setPresets(PresetSchema[] presets) {
        this.presets = presets;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public PresetSchema[] getPresets() {
        return presets;
    }

    public PresetSchema getPreset(int position) {
        return presets[position];
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        // JSON output
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this, Schema.class);
    }
}
