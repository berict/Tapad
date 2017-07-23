package com.bedrock.padder.model.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class Settings {

    private float marginScale = 0.8f;

    private String startPage = "main";

    private boolean singleTouchForStopLoop = false;

    private boolean stopSoundOnFocusLoss = false;

    private Context context = null;

    private SharedPreferences prefs = null;

    String TAG = "Settings";

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPrefs() {
        if (context != null) {
            this.prefs = context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
        } else {
            Log.e(TAG, "Context null");
        }
    }

    public void setPrefs(Context context) {
        this.context = context;
        if (context != null) {
            this.prefs = context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
        } else {
            Log.e(TAG, "Context null");
        }
    }

    public float getMarginScale() {
        return marginScale;
    }

    public String getStartPage() {
        return startPage;
    }

    public boolean isSingleTouchForStopLoop() {
        return singleTouchForStopLoop;
    }

    // TODO add actually committing the change of the prefs

    public void setMarginScale(float marginScale) {
        if (prefs != null) {
            this.marginScale = marginScale;
            prefs.edit().putFloat("marginScale", marginScale).apply();
        } else {
            Log.e(TAG, "Prefs null");
        }
    }

    public void setStartPage(String startPage) {
        if (prefs != null) {
            this.startPage = startPage;
        } else {
            Log.e(TAG, "Prefs null");
        }
    }

    public void setSingleTouchForStopLoop(boolean singleTouchForStopLoop) {
        if (prefs != null) {
            this.singleTouchForStopLoop = singleTouchForStopLoop;
        } else {
            Log.e(TAG, "Prefs null");
        }
    }

    public void fetchAllFromPrefs() {
        if (prefs != null) {
            this.singleTouchForStopLoop = prefs.getBoolean("singleTouchForStopLoop", false);
            this.marginScale = prefs.getFloat("marginScale", 0.8f);
            this.startPage = prefs.getString("startPage", "main");
        } else {
            Log.e(TAG, "Prefs null");
        }
    }
}
