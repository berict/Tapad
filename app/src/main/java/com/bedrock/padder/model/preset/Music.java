package com.bedrock.padder.model.preset;

public class Music {

    private Integer id;
    private Integer nameResId;
    private Integer imgResId;
    private Deck[] decks;

    public Music(Integer id, Integer nameResId, Integer imgResId, Deck[] decks) {
        this.id = id;
        this.nameResId = nameResId;
        this.imgResId = imgResId;
        this.decks = decks;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNameResId() {
        return nameResId;
    }

    public Integer getImgResId() {
        return imgResId;
    }

    public Deck[] getDecks() {
        return decks;
    }

    public Deck getDeck(Integer index) {
        return decks[index];
    }

    public Integer getDecksCount() {
        return decks.length;
    }
}
