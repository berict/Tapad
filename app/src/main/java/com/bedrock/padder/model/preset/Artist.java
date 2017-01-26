package com.bedrock.padder.model.preset;

import com.bedrock.padder.model.about.About;
import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("name_id")
    private String nameId;

    @SerializedName("about")
    private About about;

    public Artist(String nameId, About about) {
        this.nameId = nameId;
        this.about = about;
    }

    public String getNameId() {
        return nameId;
    }

    public About getAbout() {
        return about;
    }
}
