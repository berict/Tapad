package com.bedrock.padder.model.preset;

import com.google.gson.annotations.SerializedName;

public class Music {

    @SerializedName("name_id")
    private String nameId;

    @SerializedName("decks")
    private Deck[] decks;

    @SerializedName("deckTimings")
    private DeckTiming[] deckTimings;

    public Music(String nameId, Deck[] decks, DeckTiming[] deckTimings) {
        this.nameId = nameId;
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
}