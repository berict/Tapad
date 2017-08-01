package com.bedrock.padder.model.about;

import android.graphics.Color;

import static com.bedrock.padder.helper.FirebaseHelper.PROJECT_LOCATION_PRESETS;

public class About {

    private String title;

    // also used as imageResId
    private String presetName;

    private String tutorialLink;

    private String presetCreator;

    // used as actionbar / taskDesc
    // formatted in #000000
    private String actionbarColor;

    private Bio bio;

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

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }

    public void setDetails(Detail[] details) {
        this.details = details;
    }
}
