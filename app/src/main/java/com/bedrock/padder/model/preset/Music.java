package com.bedrock.padder.model.preset;

import com.google.gson.annotations.SerializedName;

public class Music {

    @SerializedName("name_id")
    private String nameId;

    @SerializedName("sound_count")
    private Integer soundCount;

    @SerializedName("bpm")
    private Integer bpm;

    @SerializedName("decks")
    private Deck[] decks;

    @SerializedName("deckTimings")
    private DeckTiming[] deckTimings;

    public Music(String nameId, Integer soundCount, Integer bpm, Deck[] decks, DeckTiming[] deckTimings) {
        this.nameId = nameId;
        this.soundCount = soundCount;
        this.bpm = bpm;
        this.decks = decks;
        this.deckTimings = deckTimings;
    }

    public String getNameId() {
        return nameId;
    }

    public Deck[] getDecks() {
        return decks;
    }

    public DeckTiming[] getDeckTimings() {
        return deckTimings;
    }

    public Integer getSoundCount() {
        return soundCount;
    }

    public Integer getBpm() {
        return bpm;
    }
}