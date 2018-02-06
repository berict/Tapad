package com.bedrock.padder.model.tutorial;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class Sync {

    int start;

    ArrayList<Item> items = new ArrayList<>();

    public Sync() {
        this.start = -1;
    }

    public Sync(int start) {
        this.start = start;
    }

    public Sync(int start, Item[] items) {
        this.start = start;
        this.items.addAll(Arrays.asList(items));
    }

    public Sync(int start, Item item) {
        this.start = start;
        this.items.add(item);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        Log.d("TUTORIAL", "start set to : " + start);
        this.start = start;
    }

    public void add(Item item) {
        items.add(item);
    }

    public void addAll(Item items[]) {
        this.items.addAll(Arrays.asList(items));
    }

    public static class Item {

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
                this.gesture = gesture;
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
}
