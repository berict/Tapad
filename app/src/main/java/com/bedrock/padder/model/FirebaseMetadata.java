package com.bedrock.padder.model;

import com.bedrock.padder.model.preset.Preset;

public class FirebaseMetadata {

    private Preset[] presets;

    private Integer versionCode;

    public FirebaseMetadata(Preset[] presets, Integer versionCode) {
        this.presets = presets;
        this.versionCode = versionCode;
        if (presets != null) {
            for (Preset preset : this.presets) {
                // null values to shorten the json
                preset.getAbout().setBio(null);
                preset.getAbout().setDetails(null);
            }
        }
    }

    public Preset[] getPresets() {
        return presets;
    }

    public Preset getPreset(int position) {
        return presets[position];
    }

    public Integer getVersionCode() {
        return versionCode;
    }
}
