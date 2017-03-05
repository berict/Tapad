package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("text_id")
    private String   textId;

    @SerializedName("hint_id")
    private String   hintId;

    @SerializedName("hint_is_visible")
    private Boolean  hintIsVisible;

    @SerializedName("image_id")
    private String   imageId;

    @SerializedName("runnable_is_with_anim")
    private Boolean  runnableIsWithAnim;

    public Item (String textId, String hintId) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = true;
        this.imageId = "about_detail_" + textId;
        this.runnableIsWithAnim = false;
    }

    public Item (String textId, String hintId, String imageId) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = true;
        this.imageId = imageId;
        this.runnableIsWithAnim = false;
    }

    public Item (String textId, String hintId, Boolean hintIsVisible) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = hintIsVisible;
        this.imageId = "about_detail_" + textId;
        this.runnableIsWithAnim = false;
    }

    public Item (String textId, String hintId, Boolean hintIsVisible, String imageId) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = hintIsVisible;
        this.imageId = imageId;
        this.runnableIsWithAnim = false;
    }

    public Item (String textId, String hintId, String imageId, Boolean runnableIsWithAnim) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = true;
        this.imageId = imageId;
        this.runnableIsWithAnim = runnableIsWithAnim;
    }

    public Item (String textId, String hintId, Boolean hintIsVisible, Boolean runnableIsWithAnim) {
        this.textId = textId;
        this.hintId = hintId;
        this.hintIsVisible = hintIsVisible;
        this.imageId = "about_detail_" + textId;
        this.runnableIsWithAnim = runnableIsWithAnim;
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

    public Boolean getRunnableIsWithAnim() {
        return runnableIsWithAnim;
    }
}
