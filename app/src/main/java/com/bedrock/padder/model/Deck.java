package com.bedrock.padder.model;

import android.util.Log;

public class Deck {

    private Pad pad[];

    public Deck (Pad[] pad) {
        if(pad.length == 21) {
            this.pad = pad;
        } else {
            Log.e("Deck", "Not enough pads");
            this.pad = null;
        }
    }

    public Pad[] getPads() {
        return pad;
    }

    public Pad getPad(int index) {
        return pad[index];
    }
}