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
import static com.bedrock.padder.model.tutorial.TimingListener.broadcast;

public class Pad {

    protected Sound normal = null;

    protected View view = null;

    protected int color = 0;
    protected int colorDef = 0;

    protected Handler handler;

    protected Activity activity = null;

    protected int deck;
    protected int column = -1;
    protected int row = -1;

    // TODO change to preferences
    boolean padColorOnPlay = true;

    public Pad(Sound normal, final int deck, View view, int color, int def, Activity activity) {
        this.normal = normal;
        this.deck = deck;
        this.view = view;
        this.color = color;
        this.colorDef = def;
        this.activity = activity;
        handler = new Handler(Looper.getMainLooper());

        // pad patterns
        column = Integer.parseInt(String.valueOf(view.getTag().toString().charAt(0)));
        row = Integer.parseInt(String.valueOf(view.getTag().toString().charAt(1)));

        normal.setSoundListener(new Sound.SoundListener() {
            @Override
            public void onSoundStart(Sound sound, int playingThreadCount) {
                if (playingThreadCount == 1 && !padColorOnPlay) {
                    setPadColor(colorDef);
                }
                broadcast(deck, column * 10 + row);
            }

            @Override
            public void onSoundEnd(Sound sound, int playingThreadCount) {
                if (playingThreadCount == 0 && padColorOnPlay) {
                    setPadColor(colorDef);
                }
            }

            @Override
            public void onSoundStop(Sound sound, int position, float completion) {
                Log.i("listener", "Sound stopped at " + position + ", completion of " + completion);
            }

            @Override
            public void onSoundLoop(Sound sound) {
                Log.i("listener", "Sound looped");
            }
        });
    }

    public Pad(Sound normal, int deck, View view, int color, int def, Activity activity, boolean withListener) {
        this.normal = normal;
        this.deck = deck;
        this.view = view;
        this.color = color;
        this.colorDef = def;
        this.activity = activity;
        handler = new Handler(Looper.getMainLooper());

        // pad patterns
        column = Integer.parseInt(String.valueOf(view.getTag().toString().charAt(0)));
        row = Integer.parseInt(String.valueOf(view.getTag().toString().charAt(1)));

        if (withListener) {
            normal.setSoundListener(new Sound.SoundListener() {
                @Override
                public void onSoundStart(Sound sound, int playingThreadCount) {
                    if (playingThreadCount == 1 && !padColorOnPlay) {
                        setPadColor(colorDef);
                    }
                }

                @Override
                public void onSoundEnd(Sound sound, int playingThreadCount) {
                    if (playingThreadCount == 0 && padColorOnPlay) {
                        setPadColor(colorDef);
                    }
                }

                @Override
                public void onSoundStop(Sound sound, int position, float completion) {
                    Log.i("listener", "Sound stopped at " + position + ", completion of " + completion);
                }

                @Override
                public void onSoundLoop(Sound sound) {
                    Log.i("listener", "Sound looped");
                }
            });
        }
    }

    public Sound getNormal() {
        if (normal.getDuration() > 0) {
            return normal;
        } else {
            return null;
        }
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
                for (int i = 1; i <= 4; i++) {
                    if (i != column) {
                        int dy = Math.abs(column - i);
                        setPadColor(row, i, getBlendColor(color, colorDef, 0.3f * (1 / (float) dy), activity));
                    }
                }
                break;
            case 3:
                // horizontal fade
                for (int i = 1; i <= 4; i++) {
                    if (i != row) {
                        int dx = Math.abs(row - i);
                        setPadColor(i, column, getBlendColor(color, colorDef, 0.3f * (1 / (float) dx), activity));
                    }
                }
                break;
            case 4:
                // vertical-horizontal fade
                for (int i = 1; i <= 4; i++) {
                    if (i != column) {
                        int dy = Math.abs(column - i);
                        setPadColor(row, i, getBlendColor(color, colorDef, 0.3f * (1 / (float) dy), activity));
                    }
                }
                for (int i = 1; i <= 4; i++) {
                    if (i != row) {
                        int dx = Math.abs(row - i);
                        setPadColor(i, column, getBlendColor(color, colorDef, 0.3f * (1 / (float) dx), activity));
                    }
                }
                break;
        }
    }

    void setPadColor(int row, int column, final int colorNew) {
        if (row * column != 0) {
            final View pad = getViewFromId("btn" + column + row, activity);
            if (getBackgroundColor(pad) != getColor(colorNew, activity) && getBackgroundColor(pad) != getColor(color, activity)) {
                // was not pressed
                pad.setBackgroundColor(colorNew);
            }
        }
    }

    void setPadColor(int row, int column, final int color, int duration) {
        if (row * column != 0) {
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
    }

    void setPadColor(int row, int column, final int color, int duration, int delay) {
        if (row * column != 0) {
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
                for (int i = 1; i <= 4; i++) {
                    if (i != column) {
                        int dy = Math.abs(column - i);
                        setPadColorToDefault(row, i, getBlendColor(color, colorDef, 0.3f * (1 / (float) dy), activity));
                    }
                }
                break;
            case 3:
                // horizontal fade
                for (int i = 1; i <= 4; i++) {
                    if (i != row) {
                        int dx = Math.abs(row - i);
                        setPadColorToDefault(i, column, getBlendColor(color, colorDef, 0.3f * (1 / (float) dx), activity));
                    }
                }
                break;
            case 4:
                // vertical-horizontal fade
                for (int i = 1; i <= 4; i++) {
                    if (i != column) {
                        int dy = Math.abs(column - i);
                        setPadColorToDefault(row, i, getBlendColor(color, colorDef, 0.3f * (1 / (float) dy), activity));
                    }
                }
                for (int i = 1; i <= 4; i++) {
                    if (i != row) {
                        int dx = Math.abs(row - i);
                        setPadColorToDefault(i, column, getBlendColor(color, colorDef, 0.3f * (1 / (float) dx), activity));
                    }
                }
                break;
        }
    }

    void setPadColorToDefault(int row, int column, int color) {
        if (row * column != 0) {
            final View pad = getViewFromId("btn" + column + row, activity);
            if (getBackgroundColor(pad) == getColor(color, activity)) {
                // was pressed
                pad.setBackgroundColor(getColor(colorDef, activity));
            }
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
                    if (getNormal() != null) {
                        if (getNormal().isLooping && isStopLoopOnSingle) {
                            setPadColorToDefault(true);
                            getNormal().loop(false);
                        } else {
                            playNormal();
                        }
                        setPadColorToDefault();
                    } else {
                        Log.w("Pad", "Pad attempted to play a null sound");
                        setPadColorToDefault(true);
                    }
                }

                @Override
                public void onSwipeRight() {
                    onClick();
                }

                @Override
                public void onSwipeLeft() {
                    onClick();
                }

                @Override
                public void onSwipeUp() {
                    onClick();
                }

                @Override
                public void onSwipeDown() {
                    onClick();
                }

                @Override
                public void onDoubleClick() {
                    onClick();
                }

                @Override
                public void onLongClick() {
                    setPadColor();
                    loopNormal();
                }
            });
            Log.d("Pad", "setOnTouchListener [Normal] on view " + view.toString());
        }
    }

    void loopNormal() {
        if (getNormal() != null) {
            if (getNormal().isLooping && normal != null && view != null) {
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
