package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("title_id")
    private String titleId;

    @SerializedName("title_color_id")
    private String titleColorId;

    @SerializedName("items")
    private Item[] items;

    public Detail (String titleId, String titleColorId, Item[] items) {
        this.titleId = titleId;
        this.titleColorId = titleColorId;
        this.items = items;
    }

    public String getTitleId() {
        return titleId;
    }

    public String getTitleColorId() {
        return titleColorId;
    }

    public Item[] getItems() {
        return items;
    }

    public Item getItem(Integer index) {
        return items[index];
    }
}
