package com.bedrock.padder.model;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import com.bedrock.padder.helper.OnSwipeTouchListener;

public class Pad {

    protected Sound normal = null;

    protected View view = null;

    protected int color = 0;

    protected int defColor = 0;

    protected Activity activity = null;

    public Pad(Sound normal, View view, int color, int defColor, Activity activity) {
        this.normal = normal;
        this.view = view;
        this.color = color;
        this.defColor = defColor;
        this.activity = activity;
    }

    public Sound getNormal() {
        return normal;
    }

    public void setNormal(Sound normal) {
        this.normal = normal;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDefColor(int defColor) {
        this.defColor = defColor;
    }

    void setPadColor(int color) {
        try {
            view.setBackgroundColor(activity.getResources().getColor(color));
        } catch (Resources.NotFoundException e) {
            // no res value
            view.setBackgroundColor(color);
        }
    }

    void setPadColor() {
        setPadColor(color);
    }

    void setPadColorToDefault() {
        if (!normal.isLooping) {
            // while not looping
            setPadColor(defColor);
        }
    }

    void setPad() {
        if (view != null && color != 0 && defColor != 0 && activity != null) {
            // initialized check
            view.setOnTouchListener(new OnSwipeTouchListener(activity) {
                @Override
                public void onTouch() {
                    setPadColor();
                }

                @Override
                public void onClick() {
                    getNormal().play();
                    setPadColorToDefault();
                }

                @Override
                public void onDoubleClick() {
                    getNormal().play();
                    setPadColorToDefault();
                }

                @Override
                public void onLongClick() {
                    getNormal().loop();
                }
            });
        }
    }

    void unload() {
        getNormal().unload();
    }

    void stop() {
        getNormal().stop();
    }
}
