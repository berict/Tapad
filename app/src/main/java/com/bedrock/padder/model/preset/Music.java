package com.bedrock.padder.model.preset;

import com.google.gson.annotations.SerializedName;

public class Music {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("decks")
    private Deck[] decks;

    public Music(Integer id, String name, Deck[] decks) {
        this.id = id;
        this.name = name;
        this.decks = decks;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Deck[] getDecks() {
        return decks;
    }
}
