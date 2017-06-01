package com.bedrock.padder.model.preset;

import android.app.Activity;
import android.view.View;

import com.bedrock.padder.helper.FirebaseService;
import com.bedrock.padder.helper.SoundService;
import com.bedrock.padder.model.about.About;
import com.google.gson.annotations.SerializedName;

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

    public void loadPreset(Activity activity) {
        SoundService sound = new SoundService();
        sound.loadSchemeSound(this, activity);
    }

    public void downloadPreset(View parentView, Activity activity, Runnable onFinish) {
        // download the preset from firebase
        FirebaseService firebase = new FirebaseService();
        firebase.downloadFirebasePreset(firebaseLocation, parentView, activity, onFinish);
    }

    public void removePreset(Runnable onFinish) {
        // remove the preset folder
        FirebaseService firebase = new FirebaseService();
        firebase.removeLocalPreset(firebaseLocation, onFinish, null);
    }

    public void repairPreset(final View parentView, final Activity activity, final Runnable onFinish) {
        // remove and download the preset again
        final FirebaseService firebase = new FirebaseService();
        firebase.removeLocalPreset(firebaseLocation, new Runnable() {
            @Override
            public void run() {
                firebase.downloadFirebasePreset(firebaseLocation,
                        parentView,
                        activity,
                        onFinish
                );
            }
        }, null);
    }
}
