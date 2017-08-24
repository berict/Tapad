package com.bedrock.padder.model;

import com.bedrock.padder.model.preset.PresetSchema;

public class Schema {

    private PresetSchema[] presets;

    private Integer versionCode;

    public Schema(PresetSchema[] presets, Integer versionCode) {
        this.presets = presets;
        this.versionCode = versionCode;
        if (presets != null) {
            for (PresetSchema preset : this.presets) {
                // null values to shorten the json
                preset.getPreset().getAbout().setBio(null);
                preset.getPreset().getAbout().setDetails(null);
            }
        }
    }

    public PresetSchema[] getPresets() {
        return presets;
    }

    public PresetSchema getPreset(int position) {
        return presets[position];
    }

    public Integer getVersionCode() {
        return versionCode;
    }
}
