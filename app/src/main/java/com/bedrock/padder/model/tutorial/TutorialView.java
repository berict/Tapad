package com.bedrock.padder.model.tutorial;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import static com.bedrock.padder.model.tutorial.TimingListener.listen;
import static com.bedrock.padder.model.tutorial.Tutorial.ANIMATION_FADE;

public class TutorialView {

    private View view;
    private Timing timing;

    private Animation animation = null;
    private Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (view != null) {
                Animation hide = ANIMATION_FADE;
                hide.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(hide);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
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
