package com.bedrock.padder.model.preset;

import com.google.gson.annotations.SerializedName;

public class Music {

    @SerializedName("name")
    private String name;

    @SerializedName("file_name")
    private String fileName;

    @SerializedName("is_gesture")
    private Boolean isGesture;

    @SerializedName("sound_count")
    private Integer soundCount;

    @SerializedName("bpm")
    private Integer bpm;

    @SerializedName("deck_timings")
    private DeckTiming[] deckTimings;

    public Music(String name, String fileName, Boolean isGesture, Integer soundCount, Integer bpm, DeckTiming[] deckTimings) {
        this.name = name;
        this.fileName = fileName;
        this.isGesture = isGesture;
        this.soundCount = soundCount;
        this.bpm = bpm;
        this.deckTimings = deckTimings;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public Boolean getGesture() {
        return isGesture;
    }

    public Integer getSoundCount() {
        return soundCount;
    }

    public Integer getBpm() {
        return bpm;
    }

    public DeckTiming[] getDeckTimings() {
        return deckTimings;
    }

    public void setDeckTimings(DeckTiming[] deckTimings) {
        this.deckTimings = deckTimings;
    }
}