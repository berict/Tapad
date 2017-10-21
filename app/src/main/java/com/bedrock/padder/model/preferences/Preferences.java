package com.bedrock.padder.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bedrock.padder.R;
import com.google.gson.Gson;

public class Preferences {

    /*
    * This is a wrapper class for settings in this app
    * Not a good design, but it works
    * Which is not a good mind to work with.
    * */

    public static final String APPLICATION_ID = "com.bedrock.padder";

    private SharedPreferences prefs = null;

    public Preferences(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public Preferences(Context context) {
        this.prefs = context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
    }

    /* Getters */

    public Float getDeckMargin(Context... context) {
        if (this.prefs != null) {
            return prefs.getFloat(Keys.DECK_MARGIN, Defaults.DECK_MARGIN);
        } else if (context != null && context.length > 0) {
            this.prefs = context[0].getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            return prefs.getFloat(Keys.DECK_MARGIN, Defaults.DECK_MARGIN);
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
            return null;
        }
    }

    public Boolean getStopOnFocusLoss(Context... context) {
        if (this.prefs != null) {
            return prefs.getBoolean(Keys.STOP_ON_FOCUS_LOSS, Defaults.STOP_ON_FOCUS_LOSS);
        } else if (context != null && context.length > 0) {
            this.prefs = context[0].getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            return prefs.getBoolean(Keys.STOP_ON_FOCUS_LOSS, Defaults.STOP_ON_FOCUS_LOSS);
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
            return null;
        }
    }

    public Boolean getStopLoopOnSingle(Context... context) {
        if (this.prefs != null) {
            return prefs.getBoolean(Keys.STOP_LOOP_ON_SINGLE, Defaults.STOP_LOOP_ON_SINGLE);
        } else if (context != null && context.length > 0) {
            this.prefs = context[0].getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            return prefs.getBoolean(Keys.STOP_LOOP_ON_SINGLE, Defaults.STOP_LOOP_ON_SINGLE);
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
            return null;
        }
    }

    public String getStartPage(Context... context) {
        if (this.prefs != null) {
            return prefs.getString(Keys.START_PAGE, Defaults.START_PAGE);
        } else if (context != null && context.length > 0) {
            this.prefs = context[0].getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            return prefs.getString(Keys.START_PAGE, Defaults.START_PAGE);
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
            return null;
        }
    }

    public Integer getColor(Context... context) {
        if (this.prefs != null) {
            return prefs.getInt(Keys.COLOR_CURRENT, Defaults.COLOR_CURRENT);
        } else if (context != null && context.length > 0) {
            this.prefs = context[0].getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            return prefs.getInt(Keys.COLOR_CURRENT, Defaults.COLOR_CURRENT);
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
            return null;
        }
    }

    public ItemColor getRecentColor(Context... context) {
        if (this.prefs != null) {
            return new Gson().fromJson(prefs.getString(Keys.COLOR_RECENT, Defaults.COLOR_RECENT), ItemColor.class);
        } else if (context != null && context.length > 0) {
            this.prefs = context[0].getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            return new Gson().fromJson(prefs.getString(Keys.COLOR_RECENT, Defaults.COLOR_RECENT), ItemColor.class);
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
            return null;
        }
    }

    public Integer getVersionCode(Context... context) {
        if (this.prefs != null) {
            return prefs.getInt(Keys.VERSION_CODE, Defaults.VERSION_CODE);
        } else if (context != null && context.length > 0) {
            this.prefs = context[0].getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            return prefs.getInt(Keys.VERSION_CODE, Defaults.VERSION_CODE);
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
            return null;
        }
    }

    public String getLastPlayed(Context... context) {
        if (this.prefs != null) {
            return prefs.getString(Keys.LAST_PLAYED, Defaults.LAST_PLAYED);
        } else if (context != null && context.length > 0) {
            this.prefs = context[0].getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
            return prefs.getString(Keys.LAST_PLAYED, Defaults.LAST_PLAYED);
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
            return null;
        }
    }
    
    /* Setters */

    public void setDeckMargin(Float value) {
        if (this.prefs != null) {
            if (prefs.edit().putFloat(Keys.DECK_MARGIN, value).commit()) {
                Log.i("Preferences", "Added value [" + value + "] to key [" + Keys.DECK_MARGIN + "]");
            } else {
                Log.i("Preferences", "Failed to add value [" + value + "] to key [" + Keys.DECK_MARGIN + "]");
            }
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
        }
    }

    public void setStopOnFocusLoss(Boolean value) {
        if (this.prefs != null) {
            if (prefs.edit().putBoolean(Keys.STOP_ON_FOCUS_LOSS, value).commit()) {
                Log.i("Preferences", "Added value [" + value + "] to key [" + Keys.STOP_ON_FOCUS_LOSS + "]");
            } else {
                Log.i("Preferences", "Failed to add value [" + value + "] to key [" + Keys.STOP_ON_FOCUS_LOSS + "]");
            }
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
        }
    }

    public void setStopLoopOnSingle(Boolean value) {
        if (this.prefs != null) {
            if (prefs.edit().putBoolean(Keys.STOP_LOOP_ON_SINGLE, value).commit()) {
                Log.i("Preferences", "Added value [" + value + "] to key [" + Keys.STOP_LOOP_ON_SINGLE + "]");
            } else {
                Log.i("Preferences", "Failed to add value [" + value + "] to key [" + Keys.STOP_LOOP_ON_SINGLE + "]");
            }
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
        }
    }

    public void setStartPage(String value) {
        if (this.prefs != null) {
            if (prefs.edit().putString(Keys.START_PAGE, value).commit()) {
                Log.i("Preferences", "Added value [" + value + "] to key [" + Keys.START_PAGE + "]");
            } else {
                Log.i("Preferences", "Failed to add value [" + value + "] to key [" + Keys.START_PAGE + "]");
            }
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
        }
    }

    public void setColor(Integer value) {
        if (this.prefs != null) {
            if (prefs.edit().putInt(Keys.COLOR_CURRENT, value).commit()) {
                Log.i("Preferences", "Added value [" + value + "] to key [" + Keys.COLOR_CURRENT + "]");
            } else {
                Log.i("Preferences", "Failed to add value [" + value + "] to key [" + Keys.COLOR_CURRENT + "]");
            }
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
        }
    }

    public void setRecentColor(ItemColor value) {
        if (this.prefs != null) {
            if (prefs.edit().putString(Keys.COLOR_RECENT, new Gson().toJson(value)).commit()) {
                Log.i("Preferences", "Added value [" + value + "] to key [" + Keys.COLOR_RECENT + "]");
            } else {
                Log.i("Preferences", "Failed to add value [" + value + "] to key [" + Keys.COLOR_RECENT + "]");
            }
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
        }
    }

    public void setVersionCode(Integer value) {
        if (this.prefs != null) {
            if (prefs.edit().putInt(Keys.VERSION_CODE, value).commit()) {
                Log.i("Preferences", "Added value [" + value + "] to key [" + Keys.VERSION_CODE + "]");
            } else {
                Log.i("Preferences", "Failed to add value [" + value + "] to key [" + Keys.VERSION_CODE + "]");
            }
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
        }
    }

    public void setLastPlayed(String value) {
        if (this.prefs != null) {
            if (prefs.edit().putString(Keys.LAST_PLAYED, value).commit()) {
                Log.i("Preferences", "Added value [" + value + "] to key [" + Keys.LAST_PLAYED + "]");
            } else {
                Log.i("Preferences", "Failed to add value [" + value + "] to key [" + Keys.LAST_PLAYED + "]");
            }
        } else {
            Log.e("Preferences", "The SharedPreferences was not initialized");
        }
    }

    // Keys:
    // DECK_MARGIN
    // STOP_ON_FOCUS_LOSS
    // STOP_LOOP_ON_SINGLE
    // START_PAGE
    // COLOR_CURRENT
    // COLOR_RECENT
    // VERSION_CODE
    // LAST_PLAYED

    class Keys {

        public static final String DECK_MARGIN = "deckMargin";

        public static final String STOP_ON_FOCUS_LOSS = "stopOnFocusLoss";

        public static final String STOP_LOOP_ON_SINGLE = "stopLoopOnSingle";

        public static final String START_PAGE = "startPage";

        public static final String COLOR_CURRENT = "color";

        public static final String COLOR_RECENT = "itemColor";

        public static final String VERSION_CODE = "versionCode";

        public static final String LAST_PLAYED = "savedPreset";

    }

    // Values data type:
    // Float
    // Boolean
    // Boolean
    // String
    // Int
    // String
    // Int
    // String

    class Defaults {

        public static final float DECK_MARGIN = 0.8f;

        public static final boolean STOP_ON_FOCUS_LOSS = true;

        public static final boolean STOP_LOOP_ON_SINGLE = false;

        public static final String START_PAGE = "recent";

        public static final int COLOR_CURRENT = R.color.cyan_400;

        public static final String COLOR_RECENT = "{\"colorButton\": 2131492969, \"colorButtonRecent\": []}";

        public static final int VERSION_CODE = 0;

        public static final String LAST_PLAYED = "";

    }
}
