package com.bedrock.padder.model.preset;

import com.bedrock.padder.model.about.About;
import com.google.gson.annotations.SerializedName;

public class Preset {

    @SerializedName("id")
    private Integer id;

    @SerializedName("music")
    private Music music;

    @SerializedName("about")
    private About about;

    public Preset(Integer id, Music music, About about) {
        this.id = id;
        this.music = music;
        this.about = about;
    }

    public Integer getId() {
        return id;
    }

    public Music getMusic() {
        return music;
    }

    public About getAbout() {
        return about;
    }
}
