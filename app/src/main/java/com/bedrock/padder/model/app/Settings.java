package com.bedrock.padder.model.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class Settings {

    private double marginScale = 0.8;

    private String startPage = "main";

    private boolean singleTouchForStopLoop = false;

    private Activity activity = null;

    private SharedPreferences prefs = null;

    String TAG = "Settings";

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setPrefs() {
        if (activity != null) {
            this.prefs = activity.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
        } else {
            Log.e(TAG, "Activity null");
        }
    }

    public void setPrefs(Activity activity) {
        this.activity = activity;
        if (activity != null) {
            this.prefs = activity.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
        } else {
            Log.e(TAG, "Activity null");
        }
    }

    public double getMarginScale() {
        return marginScale;
    }

    public String getStartPage() {
        return startPage;
    }

    public boolean isSingleTouchForStopLoop() {
        return singleTouchForStopLoop;
    }

    // TODO add actually committing the change of the prefs

    public void setMarginScale(double marginScale) {
        if (prefs != null) {
            this.marginScale = marginScale;
        } else {
            Log.e(TAG, "Activity null");
        }
    }

    public void setStartPage(String startPage) {
        if (prefs != null) {
            this.startPage = startPage;
        } else {
            Log.e(TAG, "Activity null");
        }
    }

    public void setSingleTouchForStopLoop(boolean singleTouchForStopLoop) {
        if (prefs != null) {
            this.singleTouchForStopLoop = singleTouchForStopLoop;
        } else {
            Log.e(TAG, "Activity null");
        }
    }
}
