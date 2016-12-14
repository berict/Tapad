package com.bedrock.padder.model.preset;

public class Preset {
    
    private Integer id;
    private Music music;
    private Artist artist;

    public Preset(Integer id, Music music, Artist artist) {
        this.id = id;
        this.music = music;
        this.artist = artist;
    }

    public Integer getId() {
        return id;
    }

    public Music getMusic() {
        return music;
    }

    public Artist getArtist() {
        return artist;
    }
}
