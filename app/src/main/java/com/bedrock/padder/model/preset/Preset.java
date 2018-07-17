package com.bedrock.padder.model.preset;

import android.app.Activity;

import com.bedrock.padder.helper.SoundHelper;
import com.bedrock.padder.model.about.About;
import com.bedrock.padder.model.preferences.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import static com.bedrock.padder.activity.MainActivity.isPresetChanged;
import static com.bedrock.padder.helper.FileHelper.PROJECT_LOCATION_PRESETS;

public class Preset {

    private String tag;

    private About about;

    private Boolean isGesture;

    private Integer soundCount;

    private Integer bpm;

    public Preset(String tag, About about, Boolean isGesture, Integer soundCount, Integer bpm) {
        this.tag = tag;
        this.about = about;
        this.isGesture = isGesture;
        this.soundCount = soundCount;
        this.bpm = bpm;
    }

    public String getTag() {
        return tag;
    }

    public Boolean isGesture() {
        return isGesture;
    }

    public Integer getSoundCount() {
        return soundCount;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public About getAbout() {
        return about;
    }

    public String getSound(int deckIndex, int padIndex, int gestureIndex) {
        String fileName = PROJECT_LOCATION_PRESETS + "/"
                + tag
                + "/sounds/sound"
                + "_" + (deckIndex + 1)
                + "_" + getPadStringFromIndex(padIndex);
        if (gestureIndex > 0) {
            fileName += "_" + gestureIndex;
        }
        File sound = new File(fileName);
        if (sound.exists()) {
            return fileName;
        } else {
            return null;
        }
    }

    public String getSound(int deckIndex, int padIndex) {
        String fileName = PROJECT_LOCATION_PRESETS + "/"
                + tag
                + "/sounds/sound"
                + "_" + (deckIndex + 1)
                + "_" + getPadStringFromIndex(padIndex);
        File sound = new File(fileName);
        if (sound.exists()) {
            return fileName;
        } else {
            return null;
        }
    }

    public String getSound(int deckIndex, String padIndex) {
        String fileName = PROJECT_LOCATION_PRESETS + "/"
                + tag
                + "/sounds/sound"
                + "_" + (deckIndex + 1)
                + "_" + padIndex;
        File sound = new File(fileName);
        if (sound.exists()) {
            return fileName;
        } else {
            return null;
        }
    }

    private String getPadStringFromIndex(int padIndex) {
        switch (padIndex) {
            case 0:
                return "00";
            case 1:
                return "11";
            case 2:
                return "12";
            case 3:
                return "13";
            case 4:
                return "14";
            case 5:
                return "21";
            case 6:
                return "22";
            case 7:
                return "23";
            case 8:
                return "24";
            case 9:
                return "31";
            case 10:
                return "32";
            case 11:
                return "33";
            case 12:
                return "34";
            case 13:
                return "41";
            case 14:
                return "42";
            case 15:
                return "43";
            case 16:
                return "44";
            default:
                return null;
        }
    }

    public Boolean getInAppTutorialAvailable() {
        return new File(PROJECT_LOCATION_PRESETS + "/" + getTag() + "/timing/timing.tpt").exists();
    }

    public void loadPreset(Activity activity) {
        isPresetChanged = true;
        new Preferences(activity).setLastPlayed(tag);
    }

    public void load(int color, int colorDef, Activity activity) {
        SoundHelper sound = new SoundHelper();
        sound.load(this, color, colorDef, activity);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }
}
