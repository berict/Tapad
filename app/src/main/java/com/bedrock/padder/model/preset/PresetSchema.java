package com.bedrock.padder.model.preset;

public class PresetSchema {
    // mongoose schema wrapper

    private Preset preset;

    // enum [Drum and Bass, Hardcore, House, Trap, Techno, Trance, Pop, /custom]
    private String genre;

    // should add censoring
    private String description = null;

    // enum [1, 2, 3, 4, 5], defaults for 3
    private Integer difficulty = 3;

    // starts from 1
    private Integer version = 1;

    private Review reviews[] = null;

    public PresetSchema(Preset preset, String genre, String description, Integer difficulty, Integer version, Review[] reviews) {
        this.preset = preset;
        this.genre = genre;
        this.description = description;
        this.difficulty = difficulty;
        this.version = version;
        this.reviews = reviews;
    }

    public PresetSchema(Preset preset, String genre, Integer difficulty, Integer version) {
        this.preset = preset;
        this.genre = genre;
        this.difficulty = difficulty;
        this.version = version;
    }

    public PresetSchema(Preset preset, String genre) {
        this.preset = preset;
        this.genre = genre;
    }

    public Preset getPreset() {
        return preset;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public Integer getVersion() {
        return version;
    }

    public Review[] getReviews() {
        return reviews;
    }
}
