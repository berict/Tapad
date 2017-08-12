package com.bedrock.padder.model.tutorial;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.bedrock.padder.helper.AnimateHelper;

public class GesturePadTiming extends PadTiming {

    String TAG = "GesturePadTiming";

    private Timing up = null;

    private Timing right = null;

    private Timing down = null;

    private Timing left = null;

    private ScaleAnimation animationUp = null;

    private ScaleAnimation animationRight = null;

    private ScaleAnimation animationDown = null;

    private ScaleAnimation animationLeft = null;

    private AsyncTask taskUp = null;

    private AsyncTask taskRight = null;

    private AsyncTask taskDown = null;

    private AsyncTask taskLeft = null;

    public GesturePadTiming(Timing normal, View tutorialView, ScaleAnimation animationNormal, AnimateHelper anim, Activity activity,
                            Timing up, Timing right, Timing down, Timing left,
                            ScaleAnimation animationUp, ScaleAnimation animationRight, ScaleAnimation animationDown, ScaleAnimation animationLeft) {
        super(normal, tutorialView, animationNormal, anim, activity);
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;
        this.animationUp = animationUp;
        this.animationRight = animationRight;
        this.animationDown = animationDown;
        this.animationLeft = animationLeft;
    }

    public GesturePadTiming(Timing timings[], View tutorialView, ScaleAnimation animations[], AnimateHelper anim, Activity activity) {
        super(timings[0], tutorialView, animations[0], anim, activity);
        this.up = timings[1];
        this.right = timings[2];
        this.down = timings[3];
        this.left = timings[4];
        this.animationUp = animations[1];
        this.animationRight = animations[2];
        this.animationDown = animations[3];
        this.animationLeft = animations[4];
    }

    @Override
    public void start() {
        super.start();
        taskUp = new TimingTaskUp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        taskRight = new TimingTaskRight().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        taskDown = new TimingTaskDown().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        taskLeft = new TimingTaskLeft().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void stop() {
        super.stop();
        if (taskUp != null) {
            taskUp.cancel(true);
        }
        if (taskRight != null) {
            taskRight.cancel(true);
        }
        if (taskDown != null) {
            taskDown.cancel(true);
        }
        if (taskLeft != null) {
            taskLeft.cancel(true);
        }
    }

    private class TimingTaskUp extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            tutorialView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int delay = up.getDelay();
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
            tutorialView.startAnimation(animationUp);
            tutorialView.setVisibility(View.VISIBLE);
            animationUp.setAnimationListener(new Animation.AnimationListener() {
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

    private class TimingTaskRight extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            tutorialView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int delay = right.getDelay();
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
            tutorialView.startAnimation(animationRight);
            tutorialView.setVisibility(View.VISIBLE);
            animationRight.setAnimationListener(new Animation.AnimationListener() {
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

    private class TimingTaskDown extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            tutorialView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int delay = down.getDelay();
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
            tutorialView.startAnimation(animationDown);
            tutorialView.setVisibility(View.VISIBLE);
            animationDown.setAnimationListener(new Animation.AnimationListener() {
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

    private class TimingTaskLeft extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            tutorialView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int delay = left.getDelay();
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
            tutorialView.startAnimation(animationLeft);
            tutorialView.setVisibility(View.VISIBLE);
            animationLeft.setAnimationListener(new Animation.AnimationListener() {
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
