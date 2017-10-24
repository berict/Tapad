package com.bedrock.padder.model.preset.store;

import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.PresetSchema;

import java.util.ArrayList;
import java.util.Arrays;

public class PresetStore {

    // current selected item comes to 0
    ArrayList<Item> items;

    public PresetStore() {
        this.items = null;
    }

    public PresetStore(Item[] items) {
        this.items = new ArrayList<>(Arrays.asList(items));
    }

    public PresetStore(PresetSchema[] items, Preferences preferences) {
        this.items = new ArrayList<>();
        int selectedPosition = -1;

        for (int i = 0; i < items.length; i++) {
            this.items.add(new Item(items[i], i));
            if (items[i].getPreset().getTag().equals(preferences.getLastPlayed())) {
                // selected preset
                selectedPosition = i;
            }
        }

        if (selectedPosition != -1) {
            select(selectedPosition);
        }
    }

    public Item getSelected() {
        return getItem(0);
    }

    public Item getItem(int index) {
        if (items != null) {
            return items.get(index);
        } else {
            return null;
        }
    }

    public int getLength() {
        if (items != null) {
            return items.size();
        } else {
            return -1;
        }
    }

    public void select(int position) {
        Item selected = items.get(position);

        for (int i = position; i > 0; i--) {
            items.set(i, items.get(i - 1));
        }

        items.set(0, selected);
    }

    public void remove(int position) {
        items.remove(position);
    }

    public void add(Item item) {
        items.add(item);
    }

    public int search(String tag) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getPreset().getTag().equals(tag)) {
                return i;
            }
        }
        return -1;
    }

    public void merge(PresetStore presetStore) {
        if (this.getLength() > presetStore.getLength()) {
            for (Item item : items) {
                if (this.search(item.getPreset().getTag()) < 0) {
                    // no match found, add
                    this.add(item);
                }
            }
        }
        // TODO add else
    }
}
