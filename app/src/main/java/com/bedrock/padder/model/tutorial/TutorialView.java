package com.bedrock.padder.model.tutorial;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import static com.bedrock.padder.model.tutorial.TimingListener.listen;
import static com.bedrock.padder.model.tutorial.Tutorial.ANIMATION_FADE;
import static com.bedrock.padder.model.tutorial.Tutorial.TUTORIAL_ANIMATION_DURATION;

public class TutorialView {

    private View view;
    private Timing timing;

    private Animation animation = null;
    private Animation.AnimationListener listener = new Animation.AnimationListener() {

        boolean isOnAnimationCalled = false;

        @Override
        public void onAnimationStart(final Animation anim) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
                Log.d("TutorialView", "animation.onAnimationStart");
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onAnimationEnd(anim);
                }
            }, anim.getDuration());
        }

        @Override
        public void onAnimationEnd(Animation anim) {
            if (!isOnAnimationCalled) {
                Log.d("TutorialView", "animation.onAnimationEnd");
                if (view != null) {
                    Animation hide = ANIMATION_FADE;
                    hide.setAnimationListener(new Animation.AnimationListener() {

                        boolean isOnAnimationCalled = false;

                        @Override
                        public void onAnimationStart(final Animation anim) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onAnimationEnd(anim);
                                }
                            }, anim.getDuration());
                        }

                        @Override
                        public void onAnimationEnd(Animation anim) {
                            if (!isOnAnimationCalled) {
                                view.setVisibility(View.INVISIBLE);
                                Log.d("TutorialView", "hide.onAnimationEnd");
                            }
                            isOnAnimationCalled = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation anim) {
                        }
                    });
                    view.startAnimation(hide);
                    if (animation.getDuration() != TUTORIAL_ANIMATION_DURATION) {
                        // Return to default duration
                        animation.setDuration(TUTORIAL_ANIMATION_DURATION);
                    }
                }
            }
            isOnAnimationCalled = true;
        }

        @Override
        public void onAnimationRepeat(Animation anim) {
        }
    };

    TutorialView(View view, Timing timing) {
        this.view = view;
        this.timing = timing;
    }

    void setAnimation(Animation animation) {
        this.animation = animation;
        animation.setAnimationListener(listener);
    }

    public void setDuration(int duration) {
        animation.setDuration(duration);
    }

    public void start() {
        if (view == null) {
            Log.e("TutorialView", "View is null");
            return;
        }
        if (animation == null) {
            Log.e("TutorialView", "Animation is not initialized");
            return;
        }
        listen(timing);
        timing.setListener(new Timing.BroadcastListener() {
            @Override
            public void onBroadcast(Timing timing, int delay) {
                Log.i("broadcast", String.valueOf(delay) + " at " + timing.toString());
            }
        });
        view.startAnimation(animation);
    }
}
