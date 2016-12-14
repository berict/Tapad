package com.bedrock.padder.model.preset;

public class Artist {

    private Integer name;
    private Integer bio;
    private Integer social;

    public Artist (Integer name, Integer bio, Integer social) {
        this.name = name;
        this.bio = bio;
        this.social = social;
    }

    public Integer getName() {
        return name;
    }

    public Integer getBio() {
        return bio;
    }

    public Integer getSocial() {
        return social;
    }
}
