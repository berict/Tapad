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

        boolean isOnAnimationEndCalled = false;
        boolean isAnimationCalled = false;

        @Override
        public void onAnimationStart(final Animation anim) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
                Log.d("TutorialView", "animation.onAnimationStart " + view.getTag());
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
            if (!isOnAnimationEndCalled) {
                Log.d("TutorialView", "animation.onAnimationEnd " + view.getTag());
                if (view != null) {

                    Animation hide = ANIMATION_FADE;
                    hide.setAnimationListener(new Animation.AnimationListener() {

                        boolean isOnAnimationEndCalled = false;

                        @Override
                        public void onAnimationStart(final Animation anim) {
                            Log.d("TutorialView", "hide.onAnimationStart " + view.getTag());
                            isAnimationCalled = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onAnimationEnd(anim);
                                }
                            }, anim.getDuration());
                        }

                        @Override
                        public void onAnimationEnd(Animation anim) {
                            if (!isOnAnimationEndCalled) {
                                view.setVisibility(View.INVISIBLE);
                                Log.d("TutorialView", "hide.onAnimationEnd " + view.getTag());
                            }
                            isOnAnimationEndCalled = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation anim) {
                        }
                    });
                    hide.setDuration(50);

                    if (view != null) {
                        Log.d("TutorialView", "view.startAnimation " + view.getTag());
                        Log.d("TutorialView", "view.getVisibility " + view.getVisibility());
                    }
                    view.startAnimation(hide);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isAnimationCalled) {
                                Log.w("run", "View " + view.getTag() + ".getVisibility " + view.getVisibility());
                                view.setVisibility(View.INVISIBLE);
                            }
                        }
                    }, 50 + 10);

                    if (animation.getDuration() != TUTORIAL_ANIMATION_DURATION) {
                        // Return to default duration
                        animation.setDuration(TUTORIAL_ANIMATION_DURATION);
                    }
                } else {
                    Log.e("onAnimationEnd", "View is null");
                }
            }
            isOnAnimationEndCalled = true;
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
