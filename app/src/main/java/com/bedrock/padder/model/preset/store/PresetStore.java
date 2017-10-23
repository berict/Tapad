package com.bedrock.padder.model.preset.store;

import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.PresetSchema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class PresetStore {

    Item selected = null;

    ArrayList<Item> available;

    public PresetStore() {
        this.available = null;
    }

    public PresetStore(Item[] available) {
        this.available = new ArrayList<>(Arrays.asList(available));
    }

    public PresetStore(Item selected, Item[] available) {
        this.selected = selected;
        this.available = new ArrayList<>(Arrays.asList(available));

        for (Item item : this.available) {
            if (item.equals(selected)) {
                this.available.remove(item);
            }
        }
    }

    public PresetStore(PresetSchema selected, PresetSchema[] available) {
        this.selected = new Item(selected);
        this.available = new ArrayList<>();

        for (int i = 0; i < available.length; i++) {
            PresetSchema presetSchema = available[i];
            if (!selected.equals(presetSchema)) {
                this.available.add(new Item(presetSchema, i));
            } else {
                this.selected.setIndex(i);
            }
        }
    }

    public PresetStore(PresetSchema[] available, Preferences preferences) {
        PresetSchema lastPlayed = preferences.getLastPlayedPreset();
        if (lastPlayed != null) {
            this.selected = new Item(lastPlayed);
        } else {
            this.selected = null;
        }

        this.available = new ArrayList<>();

        for (int i = 0; i < available.length; i++) {
            PresetSchema presetSchema = available[i];
            if (!presetSchema.equals(lastPlayed)) {
                this.available.add(new Item(presetSchema, i));
            } else {
                this.selected.setIndex(i);
            }
        }
    }

    public Item getSelected() {
        return selected;
    }

    public Item getItem(int index) {
        if (available != null) {
            return available.get(index);
        } else {
            return null;
        }
    }

    public int getLength() {
        if (available != null) {
            return available.size();
        } else {
            return -1;
        }
    }

    public void select(int position) {
        if (selected == null) {
            // there was no previously selected presets
            // move to selected from the list
            this.selected = available.get(position);
            available.remove(position);
        } else {
            // there was already selected preset
            // swap between selected and the list
            available.add(selected);
            // sort the new list
            Collections.sort(available, new Comparator<Item>() {
                @Override
                public int compare(Item t1, Item t2) {
                    // If index of first object is greater than the second, we are returning positive value
                    // If index of first object is less than the second object, we are returning negative value
                    // If index of both objects are equal, we are returning 0
                    return t1.getIndex() - t2.getIndex();
                }
            });
        }
    }

    public void remove(int position) {
        available.remove(position);
    }
}
