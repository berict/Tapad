package com.bedrock.padder.model.preset.store;

import android.util.Log;

import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.PresetSchema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PresetStore {

    Item selected;

    ArrayList<Item> items;

    public PresetStore() {
        this.items = null;
    }

    public PresetStore(Item[] items) {
        this.items = new ArrayList<>(Arrays.asList(items));
    }

    public PresetStore(PresetSchema[] presetItems, Preferences preferences) {
        items = new ArrayList<>();
        String prefsSelected = preferences.getLastPlayed();

        for (int i = 0; i < presetItems.length; i++) {
            items.add(new Item(presetItems[i], i));
            if (presetItems[i].getPreset().getTag().equals(prefsSelected)) {
                // selected preset
                setSelected(items.get(i));
                items.remove(i);
            }
        }
    }

    public List<Item> get() {
        if (selected != null) {
            ArrayList<Item> all = (ArrayList<Item>) items.clone();
            all.add(0, selected);
            return all;
        } else {
            return items;
        }
    }

    public Item getSelected() {
        return selected;
    }

    public void setSelected(Item selected) {
        this.selected = selected;
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
            if (selected != null) {
                return items.size() + 1;
            } else {
                return items.size();
            }
        } else {
            return -1;
        }
    }

    public void select(String tag) {
        int position = search(tag);
        if (position >= 0 && position < items.size()) {
            Item selected = getSelected();
            setSelected(items.get(position));
            items.remove(position);
            if (selected != null) {
                items.add(position, selected);
            }
        } else {
            Log.e("PresetStore", "Position [" + position + "] is out of bound of " + items.size());
        }
    }

    public void unselect() {
        setSelected(null);
    }

    public void remove(int position) {
        // TODO check if this is needed
        // seems this method doesn't need to work
        //items.remove(position);
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
}
