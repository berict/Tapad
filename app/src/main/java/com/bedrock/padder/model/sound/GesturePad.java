package com.bedrock.padder.model.sound;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bedrock.padder.helper.OnSwipeTouchListener;

import static com.bedrock.padder.activity.MainActivity.isStopLoopOnSingle;

public class GesturePad extends Pad {

    private Sound up = null;

    private Sound right = null;

    private Sound down = null;

    private Sound left = null;

    public GesturePad(Sound normal,
                      Sound up, Sound right, Sound down, Sound left,
                      View view, int color, int colorDef, Activity activity) {
        super(normal, view, color, colorDef, activity);
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;
    }

    public GesturePad(Sound sounds[],
                      View view, int color, int colorDef, Activity activity) {
        super(sounds[0], view, color, colorDef, activity);
        this.up = sounds[1];
        this.right = sounds[2];
        this.down = sounds[3];
        this.left = sounds[4];
    }

    public Sound getUp() {
        if (up != null) {
            return up;
        } else {
            return normal;
        }
    }

    public Sound getRight() {
        if (right != null) {
            return right;
        } else {
            return normal;
        }
    }

    public Sound getDown() {
        if (down != null) {
            return down;
        } else {
            return normal;
        }
    }

    public Sound getLeft() {
        if (left != null) {
            return left;
        } else {
            return normal;
        }
    }

    @Override
    public Sound getNormal() {
        return super.getNormal();
    }

    @Override
    void setPad() {
        if (normal != null && view != null && color != 0 && colorDef != 0 && activity != null) {
            // initialized check, normal should be not null
            view.setOnTouchListener(new OnSwipeTouchListener(activity) {
                @Override
                public void onTouch() {
                    setPadColor();
                    Log.d("Pad [Gesture]", "onTouch");
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

                @Override
                public void onSwipeUp() {
                    playUp();
                    setPadColorToDefault();
                }

                @Override
                public void onSwipeLeft() {
                    playLeft();
                    setPadColorToDefault();
                }

                @Override
                public void onSwipeDown() {
                    playDown();
                    setPadColorToDefault();
                }

                @Override
                public void onSwipeRight() {
                    playRight();
                    setPadColorToDefault();
                }
            });
            Log.d("Pad", "setOnTouchListener [Gesture] on view " + view.toString());
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
            Log.d("Pad [Gesture]", "Sound is null, can't play.");
        }
    }

    void playUp() {
        if (getUp() != null) {
            getUp().play();
        } else {
            Log.d("Pad [Gesture]", "Sound is null, can't play.");
        }
    }

    void playRight() {
        if (getRight() != null) {
            getRight().play();
        } else {
            Log.d("Pad [Gesture]", "Sound is null, can't play.");
        }
    }

    void playDown() {
        if (getDown() != null) {
            getDown().play();
        } else {
            Log.d("Pad [Gesture]", "Sound is null, can't play.");
        }
    }

    void playLeft() {
        if (getLeft() != null) {
            getLeft().play();
        } else {
            Log.d("Pad [Gesture]", "Sound is null, can't play.");
        }
    }

    @Override
    void resetPad() {
        super.resetPad();
    }

    @Override
    void unload() {
        super.unload();

        Sound sounds[] = {
                up, right, down, left
        };
        for (Sound sound : sounds) {
            if (sound != null) {
                sound.unload();
            }
        }
    }

    @Override
    public void stop() {
        super.stop();

        Sound sounds[] = {
                up, right, down, left
        };
        for (Sound sound : sounds) {
            if (sound != null) {
                sound.stop();
            }
        }
    }
}
