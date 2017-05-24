package com.bedrock.padder.model.preset;

import com.bedrock.padder.model.about.About;
import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("name")
    private String name;

    @SerializedName("about")
    private About about;

    public Artist(String name, About about) {
        this.name = name;
        this.about = about;
    }

    public String getName() {
        return name;
    }

    public About getAbout() {
        return about;
    }
}
