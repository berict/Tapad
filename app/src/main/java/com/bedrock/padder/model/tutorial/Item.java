package com.bedrock.padder.model.tutorial;

public class Item {

    int deck;
    int pad = -1;
    int gesture = -1;

    public Item(int deck) {
        if (deck > 0 && deck <= 4) {
            this.deck = deck;
        } else {
            throw new IllegalArgumentException("not matching deck > 0 && deck <= 4");
        }
    }

    public Item(int deck, int pad) {
        if (deck > 0 && deck <= 4) {
            this.deck = deck;
        } else {
            throw new IllegalArgumentException("not matching deck > 0 && deck <= 4");
        }

        if (pad >= 0 && pad <= 44) {
            this.pad = pad;
        } else {
            throw new IllegalArgumentException("not matching pad >= 0 && pad <= 44");
        }
    }

    public Item(int deck, int pad, int gesture) {
        this.gesture = gesture;
        if (deck > 0 && deck <= 4) {
            this.deck = deck;
        } else {
            throw new IllegalArgumentException("not matching deck > 0 && deck <= 4");
        }
        if (pad >= 0 && pad <= 44) {
            this.pad = pad;
        } else {
            throw new IllegalArgumentException("not matching pad >= 0 && pad <= 44");
        }
        if (gesture > 0 && gesture <= 4) {
        } else {
            throw new IllegalArgumentException("not matching gesture > 0 && gesture <= 4");
        }
    }

    public Item setDeck(int deck) {
        if (deck > 0 && deck <= 4) {
            this.deck = deck;
        } else {
            throw new IllegalArgumentException("not matching deck > 0 && deck <= 4");
        }
        return this;
    }

    public Item setPad(int pad) {
        if (pad >= 0 && pad <= 44) {
            this.pad = pad;
        } else {
            throw new IllegalArgumentException("not matching pad >= 0 && pad <= 44");
        }
        return this;
    }

    public Item setGesture(int gesture) {
        if (gesture > 0 && gesture <= 4) {
            this.gesture = gesture;
        } else {
            throw new IllegalArgumentException("not matching gesture > 0 && gesture <= 4");
        }
        return this;
    }

    @Override
    public String toString() {
        return "Item{" +
                "deck=" + deck +
                ", pad=" + pad +
                ", gesture=" + gesture +
                '}';
    }
}
