package com.bedrock.padder.model;

import com.bedrock.padder.model.preset.Preset;
import com.google.gson.annotations.SerializedName;

public class FirebaseMetadata {

    @SerializedName("presets")
    private Preset[] presets;

    @SerializedName("version_code")
    private Integer versionCode;

    public FirebaseMetadata(Preset[] presets, Integer versionCode) {
        this.presets = presets;
        this.versionCode = versionCode;
        for (Preset preset : this.presets) {
            // null values to shorten the json
            preset.getMusic().setDeckTimings(null);
            preset.getAbout().setBio(null);
            preset.getAbout().setDetails(null);
            preset.getAbout().setImage(null);
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
