package com.bedrock.padder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bedrock.padder.model.app.SettingItem;

import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class SettingsHelper {

    private SettingItem startPage; // default "main"

    private SettingItem marginScale; // default 0.8f

    private SettingItem singleTouchForStopLoop; // default false

    private SettingItem stopSoundOnFocusLoss; // default false

    private SettingItem settings[] = null;

    private Context context = null;

    private SharedPreferences prefs = null;

    String TAG = "SettingsHelper";

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPrefs(Context context) {
        this.context = context;
        if (context != null) {
            this.prefs = context.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);

            // initialize the setting items
            startPage = new SettingItem(prefs, "startPage", "main");
            marginScale = new SettingItem(prefs, "marginScale", 0.8f);
            singleTouchForStopLoop = new SettingItem(prefs, "singleTouchForStopLoop", false);
            stopSoundOnFocusLoss = new SettingItem(prefs, "stopSoundOnFocusLoss", false);

            // settings array
            settings = new SettingItem[]{
                    startPage,
                    marginScale,
                    singleTouchForStopLoop,
                    stopSoundOnFocusLoss
            };
        } else {
            Log.e(TAG, "Context null");
        }
    }

    public void fetchSettings() {
        if (prefs != null) {
            if (settings != null) {
                for (SettingItem item : settings) {
                    item.fetchItem();
                }
            } else {
                Log.e(TAG, "SettingsHelper null");
            }
        } else {
            Log.e(TAG, "Prefs null");
        }
    }

    public float getMarginScale() {
        return (float) marginScale.getItem();
    }

    public String getStartPage() {
        return (String) startPage.getItem();
    }

    public boolean getSingleTouchForStopLoop() {
        return (boolean) singleTouchForStopLoop.getItem();
    }

    public boolean getStopSoundOnFocusLoss() {
        return (boolean) stopSoundOnFocusLoss.getItem();
    }

    public void setMarginScale(float scale) {
        marginScale.setItem(scale);
    }

    public void setStartPage(String page) {
        startPage.setItem(page);
    }

    public void setSingleTouchForStopLoop(boolean touch) {
        singleTouchForStopLoop.setItem(touch);
    }

    public void setStopSoundOnFocusLoss(boolean stop) {
        stopSoundOnFocusLoss.setItem(stop);
    }
}
