package com.bedrock.padder.model.about;

import android.app.Activity;

import com.bedrock.padder.helper.WindowService;
import com.google.gson.annotations.SerializedName;

public class Bio {

    @SerializedName("title_id")
    private String   titleId;

    @SerializedName("image_id")
    private String   imageId;

    @SerializedName("name_id")
    private String   nameId;

    @SerializedName("text_id")
    private String   textId;

    @SerializedName("source_id")
    private String   sourceId;

    public Bio(String titleId, String imageId, String nameId, String textId, String sourceId) {
        this.titleId = titleId;
        this.imageId = imageId;
        this.nameId = nameId;
        this.textId = textId;
        this.sourceId = sourceId;
    }

    public String getTitleId() {
        return titleId;
    }

    public String getTitle(Activity activity) {
        WindowService window = new WindowService();
        return window.getStringFromId(titleId, activity);
    }

    public String getImageId() {
        return imageId;
    }

    public String getNameId() {
        return nameId;
    }

    public String getName(Activity activity) {
        WindowService window = new WindowService();
        return window.getStringFromId(nameId, activity);
    }

    public String getTextId() {
        return textId;
    }

    public String getText(Activity activity) {
        WindowService window = new WindowService();
        return window.getStringFromId(textId, activity);
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSource(Activity activity) {
        WindowService window = new WindowService();
        return window.getStringFromId(sourceId, activity);
    }
}
