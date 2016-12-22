package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class About {

    @SerializedName("title")
    private String title;

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

    public About (String title, String imageId,
                  Bio bio, Detail[] details,
                  String statusbarColorId, String actionbarColorId) {
        this.title = title;
        this.imageId = imageId;
        this.statusbarColorId = statusbarColorId;
        this.actionbarColorId = actionbarColorId;
        this.bio = bio;
        this.details = details;
    }

    public String getTitle() {
        return title;
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
