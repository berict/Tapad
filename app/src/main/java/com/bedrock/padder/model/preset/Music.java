package com.bedrock.padder.model.preset;

import android.util.Log;

import com.bedrock.padder.R;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;

public class Music {

    @SerializedName("name")
    private String name;

    @SerializedName("file_name")
    private String fileName;

    @SerializedName("is_gesture")
    private Boolean isGesture;

    @SerializedName("sound_count")
    private Integer soundCount;

    @SerializedName("bpm")
    private Integer bpm;

    @SerializedName("deck_timings")
    private DeckTiming[] deckTimings;

    public Music(String name, String fileName, Boolean isGesture, Integer soundCount, Integer bpm, DeckTiming[] deckTimings) {
        this.name = name;
        this.fileName = fileName;
        this.isGesture = isGesture;
        this.soundCount = soundCount;
        this.bpm = bpm;
        this.deckTimings = deckTimings;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public Boolean getGesture() {
        return isGesture;
    }

    public Integer getSoundCount() {
        return soundCount;
    }

    public Integer getBpm() {
        return bpm;
    }

    public DeckTiming[] getDeckTimings() {
        return deckTimings;
    }

    public String getSound(int deckId, int padId, int gestureId) {
        String fileName = this.fileName + "_" + (deckId + 1) + "_" + getPadStringFromId(padId);
        if (gestureId > 0) {
            fileName += "_" + gestureId;
        }
        try {
            Class res = R.raw.class;
            Field field = res.getField(fileName);
            // legit
            if (field != null) {
                return fileName;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("getSound", "Failure to get raw id.", e);
            // fail
            return null;
        }
    }

    String getPadStringFromId(int padId) {
        switch (padId) {
            case 0:
                return "00";
            case 1:
                return "01";
            case 2:
                return "02";
            case 3:
                return "03";
            case 4:
                return "04";
            case 5:
                return "11";
            case 6:
                return "12";
            case 7:
                return "13";
            case 8:
                return "14";
            case 9:
                return "21";
            case 10:
                return "22";
            case 11:
                return "23";
            case 12:
                return "24";
            case 13:
                return "31";
            case 14:
                return "32";
            case 15:
                return "33";
            case 16:
                return "34";
            case 17:
                return "41";
            case 18:
                return "42";
            case 19:
                return "43";
            case 20:
                return "44";
            default:
                return null;
        }
    }

    public void setDeckTimings(DeckTiming[] deckTimings) {
        this.deckTimings = deckTimings;
    }
}