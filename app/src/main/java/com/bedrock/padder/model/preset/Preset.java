package com.bedrock.padder.model.preset;

import android.app.Activity;

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
}
