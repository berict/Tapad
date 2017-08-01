package com.bedrock.padder.model.about;

import static com.bedrock.padder.helper.FirebaseHelper.PROJECT_LOCATION_PRESETS;

public class Bio {

    private String title;

    // also used as imageResId
    private String presetName;

    private String name;

    private String text;

    private String source;

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
        if (presetName == null || presetName.equals("about_bio_tapad")) {
            return presetName;
        } else {
            return PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/artist_image";
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
