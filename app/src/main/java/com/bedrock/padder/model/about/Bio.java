package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

import static com.bedrock.padder.helper.FirebaseService.PROJECT_LOCATION_PRESETS;

public class Bio {

    @SerializedName("title")
    private String   title;

    // also used as imageResId
    @SerializedName("preset_name")
    private String   presetName;

    @SerializedName("name")
    private String   name;

    @SerializedName("text")
    private String   text;

    @SerializedName("source")
    private String   source;

    public Bio(String title, String presetName, String name, String text, String source) {
        this.title = title;
        this.presetName = presetName;
        this.name = name;
        this.text = text;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        if (presetName.equals("about_bio_tapad") || presetName == null) {
            return presetName;
        } else {
            return PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/artist_image.jpg";
        }
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
