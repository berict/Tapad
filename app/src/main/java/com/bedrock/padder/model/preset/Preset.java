package com.bedrock.padder.model.preset;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import com.bedrock.padder.helper.FirebaseHelper;
import com.bedrock.padder.helper.SoundHelper;
import com.bedrock.padder.model.about.About;
import com.google.gson.annotations.SerializedName;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.PRESET_KEY;
import static com.bedrock.padder.activity.MainActivity.isPresetChanged;
import static com.bedrock.padder.helper.FirebaseHelper.PROJECT_LOCATION_PRESETS;
import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class Preset {

    @SerializedName("firebase_location")
    private String firebaseLocation;

    @SerializedName("music")
    private Music music;

    @SerializedName("about")
    private About about;

    public Preset(String firebaseLocation, Music music, About about) {
        this.firebaseLocation = firebaseLocation;
        this.music = music;
        this.about = about;
    }

    public String getFirebaseLocation() {
        return firebaseLocation;
    }

    public Music getMusic() {
        return music;
    }

    public About getAbout() {
        return about;
    }

    public String getSound(int deckId, int padId, int gestureId) {
        String fileName = PROJECT_LOCATION_PRESETS + "/"
                + firebaseLocation
                + "/sounds/sound"
                + "_" + (deckId + 1)
                + "_" + getPadStringFromId(padId);
        if (gestureId > 0) {
            fileName += "_" + gestureId;
        }
        File sound = new File(fileName);
        if (sound.exists()) {
            return fileName;
        } else {
            return null;
        }
    }

    private String getPadStringFromId(int padId) {
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

    public void setLoadPreset(Activity activity) {
        isPresetChanged = true;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        sharedPreferences.edit().putString(PRESET_KEY, firebaseLocation).apply();
    }

    public void loadPreset(Activity activity) {
        SoundHelper sound = new SoundHelper();
        sound.loadSound(this, activity);
    }

    public void downloadPreset(View parentView, Activity activity, Runnable onFinish) {
        // download the preset from firebase
        FirebaseHelper firebase = new FirebaseHelper();
        firebase.downloadFirebasePreset(firebaseLocation, about.getTitle(), parentView, activity, onFinish);
    }

    public void removePreset(Runnable onFinish, Activity activity) {
        // reset the savedPreset
        isPresetChanged = true;
        SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        prefs.edit().putString(PRESET_KEY, null).apply();
        // remove the preset folder
        FirebaseHelper firebase = new FirebaseHelper();
        firebase.removeLocalPreset(firebaseLocation, onFinish, null);
    }

    public void repairPreset(final View parentView, final Activity activity, final Runnable onFinish) {
        // remove and download the preset again
        final FirebaseHelper firebase = new FirebaseHelper();
        firebase.removeLocalPreset(firebaseLocation, new Runnable() {
            @Override
            public void run() {
                firebase.downloadFirebasePreset(firebaseLocation,
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
