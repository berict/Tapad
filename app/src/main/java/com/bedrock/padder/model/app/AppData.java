package com.bedrock.padder.model.app;

import com.google.gson.annotations.SerializedName;

public class AppData {

    @SerializedName("version_code")
    private Integer versionCode;

    public Integer getVersionCode() {
        return versionCode;
    }
}
