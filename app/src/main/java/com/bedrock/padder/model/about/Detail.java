package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("title_id")
    private String titleId;

    @SerializedName("items")
    private Item[] items;

    public Detail (String titleId, Item[] items) {
        this.titleId = titleId;
        this.items = items;
    }

    public String getTitle() {
        return titleId;
    }

    public Item[] getItems() {
        return items;
    }

    public Item getItem(Integer index) {
        return items[index];
    }
}
