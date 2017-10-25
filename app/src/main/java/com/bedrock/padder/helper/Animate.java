package com.bedrock.padder.helper;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class Animate {


    public class ViewAnimation {

        Animation animation = null;

        View view = null;

        int duration = 200;

        int delay = 0;

        Context context = null;

        public ViewAnimation(Animation animation, View view) {
            this.animation = animation;
            this.view = view;
        }

        public ViewAnimation(Animation animation, View view, Animation.AnimationListener listener) {
            this.animation = animation;
            this.view = view;

            animation.setAnimationListener(listener);
        }

        public Animation getAnimation() {
            return animation;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public void animate() {
            animation.setDuration(duration);
            if (delay > 0) {
                view.startAnimation(animation);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.startAnimation(animation);
                    }
                }, delay);
            }
        }
    }

    public class Animations {

        public Animation FadeIn = new AlphaAnimation(0, 1);

        public Animation FadeOut = new AlphaAnimation(1, 0);
    }
}
