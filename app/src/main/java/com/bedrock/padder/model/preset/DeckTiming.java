package com.bedrock.padder.model.preset;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class DeckTiming {

    @SerializedName("deckTiming")
    private Integer[][] deckTiming;

    @SerializedName("deckStartTiming")
    private Integer deckStartTiming;

    public DeckTiming(Integer[][] deckTiming, Integer deckStartTiming) {
        if (deckTiming.length == 85) {
            this.deckTiming = deckTiming;
        } else {
            Log.d("DeckTiming", "Need 85 pads");
        }
        this.deckStartTiming = deckStartTiming;
    }

    public Integer[][] getDeckTiming() {
        return deckTiming;
    }

    public Integer getDeckStartTiming() {
        return deckStartTiming;
    }
}
