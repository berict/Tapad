package com.bedrock.padder.model;

public class Preset {

    private Integer id;
    private String name;
    private Deck[] decks;

    public Preset (Integer id, String name, Deck[] decks) {
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

    public Deck getDeck(Integer index) {
        return decks[index];
    }

    public Integer getDecksLength() {
        return decks.length;
    }
}
