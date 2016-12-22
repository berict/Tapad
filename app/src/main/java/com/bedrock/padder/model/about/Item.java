package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("text")
    private String   text;

    @SerializedName("hint")
    private String   hint;

    @SerializedName("image_id")
    private String   imageId;

    public Item (String text, String hint, String imageId) {
        this.text = text;
        this.hint = hint;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public String getHint() {
        return hint;
    }

    public String getImageId() {
        return imageId;
    }
}
