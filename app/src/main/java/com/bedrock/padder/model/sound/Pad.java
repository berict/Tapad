package com.bedrock.padder.model.sound;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.bedrock.padder.helper.OnSwipeTouchListener;

import static com.bedrock.padder.activity.MainActivity.PAD_PATTERN;
import static com.bedrock.padder.activity.MainActivity.isStopLoopOnSingle;
import static com.bedrock.padder.helper.WindowHelper.getBackgroundColor;
import static com.bedrock.padder.helper.WindowHelper.getBlendColor;
import static com.bedrock.padder.helper.WindowHelper.getColor;
import static com.bedrock.padder.helper.WindowHelper.getViewFromId;

public class Pad {

    protected Sound normal = null;

    protected View view = null;

    protected int color = 0;

    protected int colorDef = 0;

    protected Handler handler;

    protected Activity activity = null;

    protected int column;
    protected int row;

    public Pad(Sound normal, View view, int color, int colorDef, Activity activity) {
        this.normal = normal;
        this.view = view;
        this.color = color;
        this.colorDef = colorDef;
        this.activity = activity;
        handler = new Handler(Looper.getMainLooper());

        // pad patterns
        column = Integer.parseInt(String.valueOf(view.getTag().toString().charAt(0)));
        row = Integer.parseInt(String.valueOf(view.getTag().toString().charAt(1)));
    }

    public Sound getNormal() {
        return normal;
    }

    public void setNormal(Sound normal) {
        this.normal = normal;
    }

    public void setDefColor(int colorDef) {
        this.colorDef = colorDef;
    }

    public void setColor(int color) {
        this.color = color;
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
        // TODO add equivalent to GesturePad

        switch (PAD_PATTERN) {
            case 1:
                // 4 side
                if (row - 1 > 0) {
                    // left available
                    setPadColor(row - 1, column, getBlendColor(color, colorDef, 0.3f, activity));
                }
                if (row + 1 <= 4) {
                    // right available
                    setPadColor(row + 1, column, getBlendColor(color, colorDef, 0.3f, activity));
                }
                if (column - 1 > 0) {
                    // up available
                    setPadColor(row, column - 1, getBlendColor(color, colorDef, 0.3f, activity));
                }
                if (column + 1 <= 4) {
                    // up available
                    setPadColor(row, column + 1, getBlendColor(color, colorDef, 0.3f, activity));
                }
                break;
            case 2:
                // vertical fade
                break;
            case 3:
                // horizontal fade
                break;
            case 4:
                // vertical-horizontal fade
                break;
        }
    }

    void setPadColor(int row, int column, final int colorNew) {
        final View pad = getViewFromId("btn" + column + row, activity);
        if (getBackgroundColor(pad) != getColor(colorNew, activity) && getBackgroundColor(pad) != getColor(color, activity)) {
            // was not pressed
            pad.setBackgroundColor(colorNew);
        }
    }

    void setPadColor(int row, int column, final int color, int duration) {
        final View pad = getViewFromId("btn" + column + row, activity);
        if (getBackgroundColor(pad) != getColor(color, activity) && getBackgroundColor(pad) != getColor(color, activity)) {
            // was not pressed
            pad.setBackgroundColor(color);

            if (duration > 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pad.setBackgroundColor(getColor(colorDef, activity));
                    }
                }, duration);
            }
        }
    }

    void setPadColor(int row, int column, final int color, int duration, int delay) {
        final View pad = getViewFromId("btn" + column + row, activity);
        if (getBackgroundColor(pad) != getColor(color, activity) && getBackgroundColor(pad) != getColor(color, activity)) {
            // was not pressed
            if (delay > 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pad.setBackgroundColor(color);
                    }
                }, delay);

                if (duration > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pad.setBackgroundColor(getColor(colorDef, activity));
                        }
                    }, delay + duration);
                }
            }
        }
    }

    public void update() {
        try {
            if (normal != null && normal.isLooping) {
                setPadColor();
            } else {
                setPadColorToDefault(true);
            }
        } catch (Exception e) {
            Log.e("Pad", "Failed to update pad color : " + e.getMessage());
        }
    }

    void setPadColorToDefault() {
        if (normal != null && !normal.isLooping && view != null) {
            // while not looping
            setPadColor(colorDef);
        }

        // TODO add equivalent to GesturePad

        switch (PAD_PATTERN) {
            case 1:
                // 4 side
                if (row - 1 > 0) {
                    // left available
                    setPadColorToDefault(row - 1, column, getBlendColor(color, colorDef, 0.3f, activity));
                }
                if (row + 1 <= 4) {
                    // right available
                    setPadColorToDefault(row + 1, column, getBlendColor(color, colorDef, 0.3f, activity));
                }
                if (column - 1 > 0) {
                    // up available
                    setPadColorToDefault(row, column - 1, getBlendColor(color, colorDef, 0.3f, activity));
                }
                if (column + 1 <= 4) {
                    // up available
                    setPadColorToDefault(row, column + 1, getBlendColor(color, colorDef, 0.3f, activity));
                }
                break;
            case 2:
                // vertical fade
                break;
            case 3:
                // horizontal fade
                break;
            case 4:
                // vertical-horizontal fade
                break;
        }
    }

    void setPadColorToDefault(int row, int column, int color) {
        final View pad = getViewFromId("btn" + column + row, activity);
        if (getBackgroundColor(pad) == getColor(color, activity)) {
            // was pressed
            pad.setBackgroundColor(getColor(colorDef, activity));
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
        // set the onTouchListener
        if (view != null && color != 0 && colorDef != 0 && activity != null) {
            // initialized check
            view.setOnTouchListener(new OnSwipeTouchListener(activity) {
                @Override
                public void onTouch() {
                    setPadColor();
                    Log.d("Pad [Normal]", "onTouch");
                }

                @Override
                public void onClick() {
                    if (getNormal().isLooping && isStopLoopOnSingle) {
                        setPadColorToDefault(true);
                        getNormal().loop(false);
                    } else {
                        playNormal();
                    }
                    setPadColorToDefault();
                }

                @Override
                public void onDoubleClick() {
                    playNormal();
                    setPadColorToDefault();
                }

                @Override
                public void onLongClick() {
                    loopNormal();
                }
            });
            Log.d("Pad", "setOnTouchListener [Normal] on view " + view.toString());
        }
    }

    void loopNormal() {
        if (getNormal() != null) {
            if (getNormal().isLooping) {
                setPadColorToDefault(true);
            }
            getNormal().loop();
        } else {
            Log.d("Pad [Gesture]", "Sound is null, can't loop.");
        }
    }

    void playNormal() {
        if (getNormal() != null) {
            getNormal().play();
        } else {
            Log.d("Pad [Normal]", "Sound is null, can't play.");
        }
    }

    void resetPad() {
        // removes the allocated onTouchListener
        if (view != null && color != 0 && colorDef != 0 && activity != null) {
            // initialized check
            view.setOnTouchListener(null);
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
