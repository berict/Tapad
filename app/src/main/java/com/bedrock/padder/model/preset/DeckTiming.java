package com.bedrock.padder.model.preset;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class DeckTiming {

    @SerializedName("deckTiming")
    private Integer[][] deckTiming;

    @SerializedName("deckStartingId")
    private Integer deckStartingId;

    @SerializedName("deckStartTiming")
    private Integer deckStartTiming;

    public DeckTiming(Integer[][] deckTiming, Integer deckStartingId, Integer deckStartTiming) {
        if (deckTiming.length == 85) {
            this.deckTiming = deckTiming;
        } else {
            Log.d("DeckTiming", "Need 85 pads");
        }
        this.deckStartingId = deckStartingId;
        this.deckStartTiming = deckStartTiming;
    }

    public Integer[][] getDeckTiming() {
        return deckTiming;
    }

    public Integer getDeckStartingId() {
        return deckStartingId;
    }

    public Integer getDeckStartTiming() {
        return deckStartTiming;
    }
}
