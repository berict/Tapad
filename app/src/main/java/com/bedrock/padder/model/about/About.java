package com.bedrock.padder.model.about;

import android.app.Activity;

import com.bedrock.padder.helper.WindowService;
import com.google.gson.annotations.SerializedName;

public class About {

    @SerializedName("title_id")
    private String titleId;

    @SerializedName("image_id")
    private String imageId;

    @SerializedName("statusbar_color_id")
    private String statusbarColorId;

    @SerializedName("actionbar_color_id")
    private String actionbarColorId;
    // used as actionbar / taskDesc

    @SerializedName("bio")
    private Bio bio;

    @SerializedName("details")
    private Detail[] details;

    public About (String titleId, String imageId,
                  Bio bio, Detail[] details,
                  String statusbarColorId, String actionbarColorId) {
        this.titleId = titleId;
        this.imageId = imageId;
        this.statusbarColorId = statusbarColorId;
        this.actionbarColorId = actionbarColorId;
        this.bio = bio;
        this.details = details;
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

    public String getStatusbarColorId() {
        return statusbarColorId;
    }

    public String getActionbarColorId() {
        return actionbarColorId;
    }

    public Bio getBio() {
        return bio;
    }

    public Detail[] getDetails() {
        return details;
    }

    public Detail getDetail(Integer index) {
        return details[index];
    }
}
