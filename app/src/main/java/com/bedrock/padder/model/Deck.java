package com.bedrock.padder.model;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

public class Deck {

    private Pad pads[];

    private Sound sound;

    private View view = null;

    private int color = 0;

    private int colorDef = 0;

    private boolean isSelected = false;

    Activity activity = null;

    public Deck(Pad[] pads, Sound sound, View view, int color, int colorDef, Activity activity) {
        if (pads.length == 21) {
            this.pads = pads;
        } else {
            Log.e("Deck", "Not enough pads");
            this.pads = null;
        }
        this.sound = sound;
        this.view = view;
        this.color = color;
        this.colorDef = colorDef;
        this.activity = activity;
    }

    public Pad[] getPads() {
        return pads;
    }

    public Pad getPad(int index) {
        return pads[index];
    }

    public void setPad(Pad pad, int index) {
        pads[index] = pad;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void playSound() {
        if (sound != null) {
            sound.play();
        }
    }

    void setDeckColor(int color) {
        try {
            view.setBackgroundColor(activity.getResources().getColor(color));
        } catch (Resources.NotFoundException e) {
            // no res value
            view.setBackgroundColor(color);
        }
    }

    void setDeckColor() {
        setDeckColor(color);
    }

    void setDeckColorToDefault() {
        setDeckColor(colorDef);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        setDeck();
    }

    public void toggleSelected() {
        this.isSelected = !isSelected;
        setDeck();
    }

    void setDeck() {
        if (isSelected) {
            // selected
            setDeckColor();
            playSound();
        } else {
            // unselected
            setDeckColorToDefault();
        }
    }

    public void stop() {
        if (pads != null && pads.length >= 21) {
            for (Pad pad : pads) {
                if (pad != null) {
                    pad.stop();
                }
            }
        }
    }

    public void unload() {
        if (pads != null && pads.length >= 21) {
            for (Pad pad : pads) {
                pad.unload();
            }
        }
    }
}