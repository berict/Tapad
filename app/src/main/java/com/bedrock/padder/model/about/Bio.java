package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Bio {

    @SerializedName("title_id")
    private String   titleId;

    @SerializedName("image_id")
    private String   imageId;

    @SerializedName("text_id")
    private String   textId;

    @SerializedName("source_id")
    private String   sourceId;

    public Bio(String titleId, String imageId, String textId, String sourceId) {
        this.titleId = titleId;
        this.imageId = imageId;
        this.textId = textId;
        this.sourceId = sourceId;
    }

    public String getTitleId() {
        return titleId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getTextId() {
        return textId;
    }

    public String getSourceId() {
        return sourceId;
    }
}
