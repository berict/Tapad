package com.bedrock.padder.model.tutorial;

import java.util.ArrayList;
import java.util.Arrays;

public class Sync {

    int start;

    ArrayList<Item> items = new ArrayList<>();

    public Sync() {
        this.start = -1;
    }

    public Sync(int start) {
        this.start = start;
    }

    public Sync(int start, Item[] items) {
        this.start = start;
        this.items.addAll(Arrays.asList(items));
    }

    public Sync(int start, Item item) {
        this.start = start;
        this.items.add(item);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void add(Item item) {
        items.add(item);
    }

    public void addAll(Item items[]) {
        this.items.addAll(Arrays.asList(items));
    }
}
