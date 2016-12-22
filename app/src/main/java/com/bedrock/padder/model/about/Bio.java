package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Bio {

    @SerializedName("title")
    private String   title;

    @SerializedName("image_id")
    private String   imageId;

    @SerializedName("name")
    private String   name;

    @SerializedName("text")
    private String   text;

    @SerializedName("source")
    private String   source;

    public Bio(String title, String imageId, String name, String text, String source) {
        this.title = title;
        this.imageId = imageId;
        this.name = name;
        this.text = text;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getSource() {
        return source;
    }
}
