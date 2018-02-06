package com.bedrock.padder.model.sound;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bedrock.padder.helper.OnSwipeTouchListener;

import static com.bedrock.padder.activity.MainActivity.isStopLoopOnSingle;
import static com.bedrock.padder.model.tutorial.TimingListener.broadcast;

public class GesturePad extends Pad {

    private Sound up = null;

    private Sound right = null;

    private Sound down = null;

    private Sound left = null;

    private int threadCount = 0;

    public GesturePad(Sound normal,
                      Sound up, Sound right, Sound down, Sound left,
                      int deck, View view, int color, int colorDef, Activity activity) {
        super(normal, deck, view, color, colorDef, activity, false);
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;

        initListener();
    }

    public GesturePad(Sound sounds[],
                      int deck, View view, int color, int colorDef, Activity activity) {
        super(sounds[0], deck, view, color, colorDef, activity, false);
        this.up = sounds[1];
        this.right = sounds[2];
        this.down = sounds[3];
        this.left = sounds[4];

        initListener();
    }

    private void initListener() {
        normal.setSoundListener(new Sound.SoundListener() {
            @Override
            public void onSoundStart(Sound sound, int playingThreadCount) {
                threadCount++;
                if (threadCount == 1 && !padColorOnPlay) {
                    setPadColor(colorDef);
                }
                broadcast(deck, column * 10 + row);
            }

            @Override
            public void onSoundEnd(Sound sound, int playingThreadCount) {
                threadCount--;
                if (threadCount == 0 && padColorOnPlay) {
                    setPadColor(colorDef);
                }
            }

            @Override
            public void onSoundStop(Sound sound, int position, float completion) {
            }

            @Override
            public void onSoundLoop(Sound sound) {
            }
        });

        up.setSoundListener(new Sound.SoundListener() {
            @Override
            public void onSoundStart(Sound sound, int playingThreadCount) {
                threadCount++;
                if (threadCount == 1 && !padColorOnPlay) {
                    setPadColor(colorDef);
                }
                broadcast(deck, column * 10 + row, 1);
            }

            @Override
            public void onSoundEnd(Sound sound, int playingThreadCount) {
                threadCount--;
                if (threadCount == 0 && padColorOnPlay) {
                    setPadColor(colorDef);
                }
            }

            @Override
            public void onSoundStop(Sound sound, int position, float completion) {
            }

            @Override
            public void onSoundLoop(Sound sound) {
            }
        });

        right.setSoundListener(new Sound.SoundListener() {
            @Override
            public void onSoundStart(Sound sound, int playingThreadCount) {
                threadCount++;
                if (threadCount == 1 && !padColorOnPlay) {
                    setPadColor(colorDef);
                }
                broadcast(deck, column * 10 + row, 2);
            }

            @Override
            public void onSoundEnd(Sound sound, int playingThreadCount) {
                threadCount--;
                if (threadCount == 0 && padColorOnPlay) {
                    setPadColor(colorDef);
                }
            }

            @Override
            public void onSoundStop(Sound sound, int position, float completion) {
            }

            @Override
            public void onSoundLoop(Sound sound) {
            }
        });

        down.setSoundListener(new Sound.SoundListener() {
            @Override
            public void onSoundStart(Sound sound, int playingThreadCount) {
                threadCount++;
                if (threadCount == 1 && !padColorOnPlay) {
                    setPadColor(colorDef);
                }
                broadcast(deck, column * 10 + row, 3);
            }

            @Override
            public void onSoundEnd(Sound sound, int playingThreadCount) {
                threadCount--;
                if (threadCount == 0 && padColorOnPlay) {
                    setPadColor(colorDef);
                }
            }

            @Override
            public void onSoundStop(Sound sound, int position, float completion) {
            }

            @Override
            public void onSoundLoop(Sound sound) {
            }
        });

        left.setSoundListener(new Sound.SoundListener() {
            @Override
            public void onSoundStart(Sound sound, int playingThreadCount) {
                threadCount++;
                if (threadCount == 1 && !padColorOnPlay) {
                    setPadColor(colorDef);
                }
                broadcast(deck, column * 10 + row, 4);
            }

            @Override
            public void onSoundEnd(Sound sound, int playingThreadCount) {
                threadCount--;
                if (threadCount == 0 && padColorOnPlay) {
                    setPadColor(colorDef);
                }
            }

            @Override
            public void onSoundStop(Sound sound, int position, float completion) {
            }

            @Override
            public void onSoundLoop(Sound sound) {
            }
        });
    }

    public Sound getUp() {
        if (up.getDuration() > 0) {
            return up;
        } else {
            return normal;
        }
    }

    public Sound getRight() {
        if (right.getDuration() > 0) {
            return right;
        } else {
            return normal;
        }
    }

    public Sound getDown() {
        if (down.getDuration() > 0) {
            return down;
        } else {
            return normal;
        }
    }

    public Sound getLeft() {
        if (left.getDuration() > 0) {
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
                    onClick();
                }

                @Override
                public void onLongClick() {
                    loopNormal();
                }

                @Override
                public void onSwipeUp() {
                    playUp();
                }

                @Override
                public void onSwipeLeft() {
                    playLeft();
                }

                @Override
                public void onSwipeDown() {
                    playDown();
                }

                @Override
                public void onSwipeRight() {
                    playRight();
                }
            });
            Log.d("Pad", "setOnTouchListener [Gesture] on view " + view.toString());
        }
    }

    private void playUp() {
        getUp().play();
    }

    private void playRight() {
        getRight().play();
    }

    private void playDown() {
        getDown().play();
    }

    private void playLeft() {
        getLeft().play();
    }

    @Override
    void unload() {
        Sound sounds[] = {
                up, right, down, left
        };
        for (Sound sound : sounds) {
            if (sound != null) {
                sound.unload();
            }
        }

        super.unload();
    }

    @Override
    public void stop() {
        Sound sounds[] = {
                up, right, down, left
        };
        for (Sound sound : sounds) {
            if (sound != null) {
                sound.stop();
            }
        }

        super.stop();
    }
}
