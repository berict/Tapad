package com.bedrock.padder.model.about;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

import static com.bedrock.padder.helper.FirebaseHelper.PROJECT_LOCATION_PRESETS;

public class About {

    @SerializedName("title")
    private String title;

    // also used as imageResId
    @SerializedName("preset_name")
    private String presetName;

    @SerializedName("tutorial_link")
    private String tutorialLink;

    @SerializedName("preset_creator")
    private String presetCreator;

    // used as actionbar / taskDesc
    // formatted in #000000
    @SerializedName("actionbar_color")
    private String actionbarColor;

    @SerializedName("bio")
    private Bio bio;

    @SerializedName("details")
    private Detail[] details;

    public About(String title,
                 String presetName,
                 String tutorialLink,
                 String presetCreator,
                 String actionbarColor,
                 Bio bio,
                 Detail[] details) {
        this.title = title;
        this.presetName = presetName;
        this.tutorialLink = tutorialLink;
        this.presetCreator = presetCreator;
        this.actionbarColor = actionbarColor;
        this.bio = bio;
        this.details = details;
    }

    public About(String title,
                 String presetName,
                 String actionbarColor,
                 Bio bio,
                 Detail[] details) {
        this.title = title;
        this.presetName = presetName;
        this.tutorialLink = null;
        this.presetCreator = null;
        this.actionbarColor = actionbarColor;
        this.bio = bio;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        if (presetCreator != null) {
            // normal preset
            return PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/album_art";
        } else {
            // in-app about
            return presetName;
        }
    }

    public String getActionbarColorString() {
        return actionbarColor;
    }

    public int getActionbarColor() {
        return Color.parseColor(actionbarColor);
    }

    public Bio getBio() {
        return bio;
    }

    public Detail[] getDetails() {
        return details;
    }

    public Detail getDetail(Integer index) {
        return details[index];
    }

    public String getTutorialLink() {
        return tutorialLink;
    }

    public String getPresetCreator() {
        return presetCreator;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }

    public void setDetails(Detail[] details) {
        this.details = details;
    }
}
