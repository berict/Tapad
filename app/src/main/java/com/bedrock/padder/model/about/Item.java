package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("text_id")
    private String   textId;

    @SerializedName("hint_id")
    private String   hintId;

    @SerializedName("hint_is_visible")
    private Boolean hintIsVisible;

    @SerializedName("image_id")
    private String   imageId;
    
    @SerializedName("runnable")
    private Runnable runnable;

    public Item (String textId, String hintId, String imageId) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = true;
        this.imageId = imageId;
        this.runnable = null;
    }

    public Item (String textId, String hintId, Boolean hintIsVisible, String imageId) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = hintIsVisible;
        this.imageId = imageId;
        this.runnable = null;
    }

    public Item (String textId, String hintId, String imageId, Runnable runnable) {
        this.textId = textId;
        this.hintId = hintId;
        this.imageId = imageId;
        this.runnable = runnable;
    }

    public Item (String textId, String hintId) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = true;
        this.imageId = "about_detail_" + textId;
        this.runnable = null;
    }

    public Item (String textId, String hintId, Runnable runnable) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = true;
        this.imageId = "about_detail_" + textId;
        this.runnable = runnable;
    }

    public Item (String textId, String hintId, Boolean hintIsVisible) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = hintIsVisible;
        this.imageId = "about_detail_" + textId;
        this.runnable = null;
    }

    public Item (String textId, String hintId, Boolean hintIsVisible, Runnable runnable) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = hintIsVisible;
        this.imageId = "about_detail_" + textId;
        this.runnable = runnable;
    }

    public String getTextId() {
        return textId;
    }

    public String getHintId() {
        return hintId;
    }

    public String getImageId() {
        return imageId;
    }

    public Boolean getHintIsVisible() {
        return hintIsVisible;
    }

    public Runnable getRunnable() { return runnable; }
}
