package com.bedrock.padder.model;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.bedrock.padder.helper.OnSwipeTouchListener;

public class Pad {

    protected Sound normal = null;

    protected View view = null;

    protected int color = 0;

    protected int colorDef = 0;

    protected Handler handler;

    protected Activity activity = null;

    public Pad(Sound normal, View view, int color, int colorDef, Activity activity) {
        this.normal = normal;
        this.view = view;
        this.color = color;
        this.colorDef = colorDef;
        this.activity = activity;
        handler = new Handler(Looper.getMainLooper());
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

    public void setDefColor(int colorDef) {
        this.colorDef = colorDef;
    }

    void setPadColor(int color) {
        try {
            view.setBackgroundColor(activity.getResources().getColor(color));
        } catch (Resources.NotFoundException e) {
            // no res value
            view.setBackgroundColor(color);
        }
        Log.d("setPadColor", view.toString() + " color set to " + color);
    }

    void setPadColor() {
        setPadColor(color);
    }

    void setPadColorToDefault() {
        if (normal != null && !normal.isLooping && view != null) {
            // while not looping
            setPadColor(colorDef);
        }
    }

    void setPadColorToDefault(boolean isForced) {
        if (isForced && view != null) {
            // forced change
            setPadColor(colorDef);
        } else {
            if (normal != null && !normal.isLooping) {
                // while not looping
                setPadColor(colorDef);
            }
        }
    }

    void setPadColorToDefault(int delay) {
        if (normal != null && !normal.isLooping && view != null) {
            // while not looping
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setPadColorToDefault();
                }
            }, delay);
        }
    }

    void setPad() {
        if (view != null && color != 0 && colorDef != 0 && activity != null) {
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
        if (getNormal() != null) {
            getNormal().unload();
        }
        setPadColorToDefault();
    }

    public void stop() {
        if (getNormal() != null) {
            getNormal().stop();
        }
        setPadColorToDefault();
    }
}
