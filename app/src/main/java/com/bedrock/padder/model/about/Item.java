package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("text_id")
    private String   textId;

    @SerializedName("hint_id")
    private String   hintId;

    @SerializedName("image_id")
    private String   imageId;

    public Item (String textId, String hintId, String imageId) {
        this.textId = textId;
        this.hintId = hintId;
        this.imageId = imageId;
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
}
