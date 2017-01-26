package com.bedrock.padder.model.preset;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Deck {

    @SerializedName("pad")
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