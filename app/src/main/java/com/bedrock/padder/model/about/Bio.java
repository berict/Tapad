package com.bedrock.padder.model.about;

import android.content.Context;

import com.bedrock.padder.model.preset.Preset;

import static com.bedrock.padder.helper.FileHelper.PROJECT_LOCATION_PRESETS;
import static com.bedrock.padder.helper.WindowHelper.getStringFromIdWithFallback;

public class Bio {

    private String title;

    private String name;

    private String text;

    private String source;

    public Bio(String title, String name, String text, String source) {
        this.title = title;
        this.name = name;
        this.text = text;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle(Context context) {
        return getStringFromIdWithFallback(getTitle(), context);
    }

    public String getImage(Preset preset) {
        String tag = preset.getTag();
        if (tag == null || tag.equals("about_bio_tapad")) {
            return tag;
        } else {
            return PROJECT_LOCATION_PRESETS + "/" + tag + "/about/artist_image";
        }
    }

    public String getName() {
        return name;
    }

    public String getName(Context context) {
        return getStringFromIdWithFallback(getName(), context);
    }

    public String getText() {
        return text;
    }

    public String getText(Context context) {
        return getStringFromIdWithFallback(getText(), context);
    }

    public String getSource() {
        return source;
    }

    public String getSource(Context context) {
        return getStringFromIdWithFallback(getSource(), context);
    }
}
