package com.bedrock.padder.model;

import android.util.Log;

public class Deck {

    private Pad pads[];

    public Deck (Pad[] pads) {
        if(pads.length == 21) {
            this.pads = pads;
        } else {
            Log.e("Deck", "Not enough padss");
            this.pads = null;
        }
    }

    public Pad[] getPads() {
        return pads;
    }

    public Pad getPad(int index) {
        return pads[index];
    }

    public void setPad(Pad pad, int index) {
        pads[index] = pad;
    }

    public void pause() {
        if (pads != null && pads.length >= 21) {
            for (Pad pad : pads) {
                pad.stop();
            }
        }
    }

    public void unload() {
        if (pads != null && pads.length >= 21) {
            for (Pad pad : pads) {
                pad.unload();
            }
        }
    }
}