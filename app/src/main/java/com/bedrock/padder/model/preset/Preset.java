package com.bedrock.padder.model.preset;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import com.bedrock.padder.helper.FirebaseHelper;
import com.bedrock.padder.helper.SoundHelper;
import com.bedrock.padder.model.about.About;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.PRESET_KEY;
import static com.bedrock.padder.activity.MainActivity.isPresetChanged;
import static com.bedrock.padder.helper.FirebaseHelper.PROJECT_LOCATION_PRESETS;
import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

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

    public void setLoadPreset(Activity activity) {
        isPresetChanged = true;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        sharedPreferences.edit().putString(PRESET_KEY, tag).apply();
    }

    public void load(int color, int colorDef, Activity activity) {
        SoundHelper sound = new SoundHelper();
        sound.load(this, color, colorDef, activity);
    }

    public void downloadPreset(View parentView, Activity activity, Runnable onFinish) {
        // download the preset from firebase
        FirebaseHelper firebase = new FirebaseHelper();
        firebase.downloadFirebasePreset(tag, about.getTitle(), parentView, activity, onFinish);
    }

    public void removePreset(Runnable onFinish, Activity activity) {
        // reset the savedPreset
        isPresetChanged = true;
        SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        prefs.edit().putString(PRESET_KEY, null).apply();
        // remove the preset folder
        FirebaseHelper firebase = new FirebaseHelper();
        firebase.removeLocalPreset(tag, onFinish, null);
    }

    public void repairPreset(final View parentView, final Activity activity, final Runnable onFinish) {
        // remove and download the preset again
        final FirebaseHelper firebase = new FirebaseHelper();
        firebase.removeLocalPreset(tag, new Runnable() {
            @Override
            public void run() {
                firebase.downloadFirebasePreset(tag,
                        about.getTitle(),
                        parentView,
                        activity,
                        onFinish
                );
            }
        }, null);
        // reset the savedPreset
        isPresetChanged = true;
        SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        prefs.edit().putString(PRESET_KEY, null).apply();
    }
}
