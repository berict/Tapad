package com.bedrock.padder.model.tutorial;

public class Sync {

    int start;

    Item items[];

    public Sync(int start, Item[] items) {
        this.start = start;
        this.items = items;
    }

    public Sync(int start, Item item) {
        this.start = start;
        this.items = new Item[]{item};
    }

}
