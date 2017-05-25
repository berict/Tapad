package com.bedrock.padder.model.about;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

public class About {

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("tutorial_link")
    private String tutorialLink;

    @SerializedName("preset_creator")
    private String presetCreator;

    @SerializedName("actionbar_color")
    private String actionbarColor;
    // used as actionbar / taskDesc
    // formatted in #000000

    @SerializedName("bio")
    private Bio bio;

    @SerializedName("details")
    private Detail[] details;

    public About(String title, String image, String tutorialLink, String presetCreator, String actionbarColor, Bio bio, Detail[] details) {
        this.title = title;
        this.image = image;
        this.tutorialLink = tutorialLink;
        this.presetCreator = presetCreator;
        this.actionbarColor = actionbarColor;
        this.bio = bio;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
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
}
