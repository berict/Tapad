package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Bio {

    @SerializedName("title")
    private String   title;

    @SerializedName("image")
    private String   image;

    @SerializedName("name")
    private String   name;

    @SerializedName("text")
    private String   text;

    @SerializedName("source")
    private String   source;

    public Bio(String title, String image, String name, String text, String source) {
        this.title = title;
        this.image = image;
        this.name = name;
        this.text = text;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
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
