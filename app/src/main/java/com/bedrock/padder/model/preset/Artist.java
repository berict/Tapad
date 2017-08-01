package com.bedrock.padder.model.preset;

import com.bedrock.padder.model.about.About;

public class Artist {

    private String name;

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
