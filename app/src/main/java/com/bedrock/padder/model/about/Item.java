package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("string_id")
    private String stringId;

    @SerializedName("image_id")
    private String imageId;
    
    public Item (String stringId, String imageId) {
        this.stringId = stringId;
        this.imageId = imageId;
    }
    
    public String getStringId() {
        return stringId;
    }
    
    public String getImageId() {
        return imageId;
    }
}
