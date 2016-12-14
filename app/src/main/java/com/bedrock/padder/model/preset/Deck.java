package com.bedrock.padder.model.preset;

import android.util.Log;

public class Deck {
    private Pad pad[];

    public Deck (Pad[] pad) {
        if(pad.length == 17) {
            this.pad = pad;
        } else {
            Log.e("Deck", "Not enough pads");
        }
    }

    public Pad[] getPads() {
        return pad;
    }

    public Pad getPad(int index) {
        return pad[index];
    }
}
