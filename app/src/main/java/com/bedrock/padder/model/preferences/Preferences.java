package com.bedrock.padder.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public interface Preferences {

    Item items[] = null;

    Item[] getAll();

    Item get(String key);

    int size();

    int search(String key);

    void apply(Context context);

    boolean commit(Context context);

    String toString(Context context);

    class Editor implements Preferences {

        private List<Item> items = new ArrayList<>();

        private String key = null;

        private Context context = null;

        public void set(Item[] items) {
            this.items = new ArrayList<>(Arrays.asList(items));
        }

        @Override
        public Item[] getAll() {
            return items.toArray(new Item[items.size()]);
        }

        @Override
        public Item get(String key) {
            int index = search(key);
            if (index != -1 && index < items.size()) {
                return items.get(index);
            } else {
                // no item found
                Log.e(".Editor", "No item was found with key " + key);
                return null;
            }
        }

        @Override
        public int size() {
            if (items != null) {
                return items.size();
            } else {
                return -1;
            }
        }

        @Override
        public int search(String key) {
            if (items != null && items.size() > 0) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getKey().equals(key)) {
                        return i;
                    }
                }
            }
            // no match found / error
            Log.e(".Editor", "List item is empty / null");
            return -1;
        }

        /*
        * Actions
        * Returns the object itself (Builder)
        * */

        public Editor set(String key, Object value) {
            int index = search(key);
            if (index != -1 && index < items.size()) {
                items.get(index).setValue(value);
            } else {
                // no item found
                Log.e("Preferences", "No preferences matching key " + key + " was found");
            }
            return this;
        }

        public Editor put(String key, Object value) {
            if (items != null) {
                items.add(new Item(key, value));
            }
            return this;
        }

        /*
        * Builder actions
        * */

        public Editor create(Context context) {
            if (context != null) {
                this.context = context;
                SharedPreferences preferences = context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
                String prefsJson = preferences.getString("prefs", null);
                if (prefsJson != null) {
                    // prefs exists
                    Gson gson = new Gson();
                    // parse prefsJson to the items
                    items = new ArrayList<>(Arrays.asList(gson.fromJson(prefsJson, Preferences.Wrapper.class).getAll()));
                }
            }
            return this;
        }

        public Editor create(String prefs[]) {
            // initialize the objects
            items = new ArrayList<>();
            for (int i = 0; i < prefs.length; i++) {
                items.add(new Item(prefs[i]));
            }
            return this;
        }

        public Editor setContext(Context context) {
            this.context = context;
            return this;
        }

        public Editor setKey(String key) {
            this.key = key;
            return this;
        }

        public Editor setValue(Object object) {
            int index = search(key);
            if (key != null && index >= 0 && items.size() > index) {
                items.get(index).setValue(object);
            } else {
                Log.e(".Editor", "Error setting value. There is no value matching the key.");
            }
            return this;
        }

        public Editor apply() {
            if (context != null) {
                apply(this.context);
            } else {
                Log.e(".Editor", "Error applying value. The context is null.");
            }
            return this;
        }

        public boolean commit() {
            if (context != null) {
                return commit(this.context);
            } else {
                Log.e(".Editor", "Error applying value. The context is null.");
                return false;
            }
        }

        @Override
        public void apply(Context context) {
            String prefsJson = new Gson().toJson(get());
            Log.i(".Editor", "Added [" + prefsJson + "] to SharedPreferences key [prefs]");
            context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE).edit().putString("prefs", prefsJson).apply();
        }

        @Override
        public boolean commit(Context context) {
            String prefsJson = new Gson().toJson(this);
            return context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE).edit().putString("prefs", prefsJson).commit();
        }

        @Override
        public String toString(Context context) {
            SharedPreferences preferences = context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            String prefsJson = preferences.getString("prefs", null);
            if (prefsJson != null) {
                // prefs exists
                return prefsJson;
            } else {
                Log.e(".Editor", "Json is null");
                return null;
            }
        }

        @Override
        public String toString() {
            if (context != null) {
                return toString(context);
            } else {
                Log.e(".Editor", "The context is null.");
                return null;
            }
        }

        public Preferences get() {
            return new Preferences.Wrapper(items.toArray(new Item[items.size()]));
        }
    }

    class Wrapper implements Preferences {

        private Item items[] = null;

        public Wrapper(Context context) {
            if (context != null) {
                SharedPreferences preferences = context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
                String prefsJson = preferences.getString("prefs", null);
                if (prefsJson != null) {
                    // prefs exists
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    items = gson.fromJson(prefsJson, Preferences.class).getAll();
                }
            }
        }

        public Wrapper(String prefs[]) {
            // initialize the objects
            items = new Item[prefs.length];
            for (int i = 0; i < prefs.length; i++) {
                items[i] = new Item(prefs[i]);
            }
        }

        public Wrapper(Item[] items) {
            this.items = items;
        }

        @Override
        public Item[] getAll() {
            return items;
        }

        @Override
        public Item get(String key) {
            int index = search(key);
            if (index != -1 && index < items.length) {
                return items[index];
            } else {
                // no item found
                return null;
            }
        }

        @Override
        public int size() {
            if (items != null) {
                return items.length;
            } else {
                return -1;
            }
        }

        public void set(Item[] items) {
            this.items = items;
        }

        public void set(String key, Object value) {
            int index = search(key);
            if (index != -1 && index < items.length) {
                items[index].setValue(value);
            } else {
                // no item found
                Log.e("Preferences", "No preferences matching key " + key + " was found");
            }
        }

        public void put(String key, Object value) {
            if (items != null) {
                items = new Item[items.length + 1];
                items[items.length] = new Item(key, value);
            } else {
                items = new Item[1];
                items[0] = new Item(key, value);
            }
        }

        @Override
        public int search(String key) {
            if (items != null && items.length > 0) {
                for (int i = 0; i < items.length; i++) {
                    if (items[i].getKey() == key) {
                        return i;
                    }
                }
            }
            // no match found / error
            return -1;
        }

        @Override
        public void apply(Context context) {
            String prefsJson = new Gson().toJson(this);
            context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE).edit().putString("prefs", prefsJson).apply();
        }

        @Override
        public boolean commit(Context context) {
            String prefsJson = new Gson().toJson(this);
            return context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE).edit().putString("prefs", prefsJson).commit();
        }

        @Override
        public String toString(Context context) {
            SharedPreferences preferences = context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            String prefsJson = preferences.getString("prefs", null);
            if (prefsJson != null) {
                // prefs exists
                return prefsJson;
            } else {
                return null;
            }
        }
    }
}
