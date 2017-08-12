package com.bedrock.padder.model.tutorial;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.bedrock.padder.helper.AnimateHelper;

public class PadTiming {

    protected View tutorialView;
    protected AnimateHelper anim;
    protected Activity activity = null;
    String TAG = "PadTiming";
    private Timing normal;
    private ScaleAnimation animationNormal;
    private AsyncTask taskNormal = null;

    public PadTiming(Timing normal, View tutorialView, ScaleAnimation animationNormal, AnimateHelper anim, Activity activity) {
        this.normal = normal;
        this.tutorialView = tutorialView;
        this.animationNormal = animationNormal;
        this.anim = anim;
        this.activity = activity;
    }

    public void start() {
        // execute the AsyncTask
        taskNormal = new TimingTaskNormal().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void stop() {
        // cancel
        if (taskNormal != null) {
            taskNormal.cancel(true);
        }
    }

    private class TimingTaskNormal extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            tutorialView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int delay = normal.getDelay();
            if (delay >= 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Sleep interrupted");
                }
                animate();
                // recursive call
                doInBackground();
            } else {
                // ended
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "tutorial ended");
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "tutorial cancelled");
        }

        void animate() {
            // start animation
            tutorialView.startAnimation(animationNormal);
            tutorialView.setVisibility(View.VISIBLE);
            animationNormal.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    anim.fadeOut(tutorialView, 0, 100, activity);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }
}
