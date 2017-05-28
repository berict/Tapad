package com.bedrock.padder.model.about;

import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("title")
    private String title;

    @SerializedName("items")
    private Item[] items;

    public Detail (String title, Item[] items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public Item[] getItems() {
        return items;
    }

    public Item getItem(Integer index) {
        return items[index];
    }
}
