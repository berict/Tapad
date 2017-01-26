package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("text_id")
    private String   textId;

    @SerializedName("hint_id")
    private String   hintId;

    @SerializedName("image_id")
    private String   imageId;
    
    @SerializedName("runnable")
    private Runnable runnable;

    public Item (String textId, String hintId, String imageId) {
        this.textId = textId;
        this.hintId = hintId;
        this.imageId = imageId;
        this.runnable = null;
    }

    public Item (String textId, String hintId, String imageId, Runnable runnable) {
        this.textId = textId;
        this.hintId = hintId;
        this.imageId = imageId;
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

    public Runnable getRunnable() { return runnable; }
}
