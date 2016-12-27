package com.bedrock.padder.helper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import java.util.HashMap;
import java.util.Map;

@TargetApi(9)
public class AnimService {
    WindowService window = new WindowService();

    // Fade animations

    public void fade(final int id, final float startAlpha, final float endAlpha, final int delay, final long duration, String handlerName, Activity activity) {
        final AlphaAnimation fade = new AlphaAnimation(startAlpha, endAlpha);
        final View view = (View) activity.findViewById(id);

        //fadeIn.setInterpolator();

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                fade.setDuration(duration);
                view.startAnimation(fade);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimService", String.valueOf(id) + " fade effect from " + String.valueOf(startAlpha) + "f to " + String.valueOf(endAlpha) + "f for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);

        if(Build.VERSION.SDK_INT >= 11) {
            fade.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (Build.VERSION.SDK_INT >= 11) {
                        view.setAlpha(startAlpha);
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (Build.VERSION.SDK_INT >= 11) {
                        view.setAlpha(endAlpha);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void fadeIn(final View view, final int delay, final long duration, String handlerName) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        view.setVisibility(View.INVISIBLE);

        //fadeIn.setInterpolator();

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                fadeIn.setDuration(duration);
                view.startAnimation(fadeIn);

                Log.i("AnimService", "fade IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    public void fadeIn(final int id, final int delay, final long duration, String handlerName, Activity activity) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        final View view = (View) activity.findViewById(id);
        view.setVisibility(View.INVISIBLE);

        //fadeIn.setInterpolator();

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeIn.setDuration(duration);
                view.startAnimation(fadeIn);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimService", String.valueOf(id) + " fade IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    public void fadeInDelay(final int id, final int delay, final int fadeDelay, final long duration, String handlerName, Activity activity) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        final View view = (View) activity.findViewById(id);
        view.setVisibility(View.INVISIBLE);

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                Handler fade = new Handler();
                fade.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("AnimService", "Visibility changed to View.VISIBLE");
                        view.setVisibility(View.VISIBLE);
                    }
                }, fadeDelay);

                fadeIn.setDuration(duration);
                view.startAnimation(fadeIn);

                Log.i("AnimService", String.valueOf(id) + " fade IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    public void fadeInDelay(final View view, final int delay, final int fadeDelay, final long duration, String handlerName) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        view.setVisibility(View.INVISIBLE);

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                Handler fade = new Handler();
                fade.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("AnimService", "Visibility changed to View.VISIBLE");
                        view.setVisibility(View.VISIBLE);
                    }
                }, fadeDelay);

                fadeIn.setDuration(duration);
                view.startAnimation(fadeIn);

                Log.i("AnimService", "fade IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    public void fadeOut(final int id, final int delay, final long duration, Activity activity) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        final View view = (View) activity.findViewById(id);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);
                view.setVisibility(View.GONE);

                Log.i("AnimService", String.valueOf(id) + " fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);

            }
        }, delay + 10);
    }

    public void fadeOut(final View view, final int delay, final long duration) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        //view.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);

                Log.i("AnimService", "fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);

            }
        }, duration + delay + 10);
    }

    public void fadeOutInvisible(final View view, final int delay, final long duration) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);
                view.setVisibility(View.INVISIBLE);

                Log.i("AnimService", "fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    public void fadeOutInvisible(final int id, final int delay, final long duration, Activity activity) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        final View view = (View) activity.findViewById(id);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);
                view.setVisibility(View.INVISIBLE);

                Log.i("AnimService", String.valueOf(id) + " fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    // Slide Animations

    public void slideIn(final View view, final int delay, final long duration, String handlerName) {
        final TranslateAnimation slideIn = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        view.setVisibility(View.INVISIBLE);

        slideIn.setInterpolator(new DecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                slideIn.setDuration(duration);
                view.startAnimation(slideIn);

                Log.i("AnimService", "slide IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    // Scale Animations

    public void scaleOut(final View view, final int delay, final long duration, String handlerName) {
        final ScaleAnimation scaleOut = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                scaleOut.setDuration(duration);
                view.startAnimation(scaleOut);

                Log.i("AnimService", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, delay + 10);
    }

    public void scaleOut(final int id, final int delay, final long duration, String handlerName, Activity activity) {
        final View view = (View)activity.findViewById(id);
        final ScaleAnimation scaleOut = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                scaleOut.setDuration(duration);
                view.startAnimation(scaleOut);

                Log.i("AnimService", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, delay + 10);
    }

    public void scaleOut(final View view, int touchX, int touchY, final int delay, final long duration, String handlerName, Activity activity) {
        float x = touchX / window.getWindowWidthPx(activity);
        float y = touchY / window.getWindowHeightPx(activity);
        final ScaleAnimation scaleOut = new ScaleAnimation(1, 0, 1, 0, Animation.ABSOLUTE, x, Animation.ABSOLUTE, y);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                scaleOut.setDuration(duration);
                view.startAnimation(scaleOut);

                Log.i("AnimService", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, delay + 10);
    }

    public void scaleOut(final int id, int touchX, int touchY, final int delay, final long duration, String handlerName, Activity activity) {
        final View view = (View)activity.findViewById(id);
        float x = touchX / window.getWindowWidthPx(activity);
        float y = touchY / window.getWindowHeightPx(activity);
        final ScaleAnimation scaleOut = new ScaleAnimation(1, 0, 1, 0, Animation.ABSOLUTE, x, Animation.ABSOLUTE, y);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                scaleOut.setDuration(duration);
                view.startAnimation(scaleOut);

                Log.i("AnimService", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, delay + 10);
    }

    public void scaleIn(final View view, final int delay, final long duration, String handlerName) {
        final ScaleAnimation scaleIn = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        view.setVisibility(View.INVISIBLE);

        scaleIn.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                scaleIn.setDuration(duration);
                view.startAnimation(scaleIn);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimService", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    public void scaleIn(final int id, final int delay, final long duration, String handlerName, Activity activity) {
        final View view = (View)activity.findViewById(id);
        final ScaleAnimation scaleIn = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        view.setVisibility(View.INVISIBLE);

        scaleIn.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                scaleIn.setDuration(duration);
                view.startAnimation(scaleIn);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimService", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    public void scaleIn(final View view, int touchX, int touchY, final int delay, final long duration, String handlerName, Activity activity) {
        float x = touchX / window.getWindowWidthPx(activity);
        float y = touchY / window.getWindowHeightPx(activity);
        final ScaleAnimation scaleOut = new ScaleAnimation(0, 1, 0, 1, Animation.ABSOLUTE, x, Animation.ABSOLUTE, y);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                scaleOut.setDuration(duration);
                view.startAnimation(scaleOut);

                Log.i("AnimService", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, delay + 10);
    }

    public void scaleIn(final int id, int touchX, int touchY, final int delay, final long duration, String handlerName, Activity activity) {
        final View view = (View)activity.findViewById(id);
        float x = touchX / window.getWindowWidthPx(activity);
        float y = touchY / window.getWindowHeightPx(activity);
        final ScaleAnimation scaleOut = new ScaleAnimation(0, 1, 0, 1, Animation.ABSOLUTE, x, Animation.ABSOLUTE, y);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                scaleOut.setDuration(duration);
                view.startAnimation(scaleOut);

                Log.i("AnimService", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, delay + 10);
    }

    public void scaleInOverShoot(final View view, final int delay, final long duration, String handlerName) {
        final ScaleAnimation scaleInOverShoot = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        view.setVisibility(View.INVISIBLE);

        scaleInOverShoot.setInterpolator(new OvershootInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                scaleInOverShoot.setDuration(duration);
                view.startAnimation(scaleInOverShoot);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimService", "scale IN OVERSHOOT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    public void scaleInOverShoot(final int view_id, final int delay, final long duration, String handlerName, final Activity activity) {
        final ScaleAnimation scaleInOverShoot = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleInOverShoot.setInterpolator(new OvershootInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = (View) activity.findViewById(view_id);
                scaleInOverShoot.setDuration(duration);
                view.startAnimation(scaleInOverShoot);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimService", "scale IN OVERSHOOT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    // Object Animations

    public void moveXYinpx(View view,
                           int fromX, int toX,
                           int durationX, int delayX, TimeInterpolator intX,
                           int fromY, int toY,
                           int durationY, int delayY, TimeInterpolator intY,
                           int duration, int delay) {
        if (Build.VERSION.SDK_INT >= 11) {
            Animator animSet = new AnimatorSet();

            ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", fromX, toX);
            animX.setDuration(durationX);
            animX.setStartDelay(delayX);
            animX.setInterpolator(intX);

            ObjectAnimator animY = ObjectAnimator.ofFloat(view, "y", fromY, toY);
            animY.setDuration(durationY);
            animY.setStartDelay(delayY);
            animY.setInterpolator(intY);

            animSet.setDuration(duration);
            animSet.setStartDelay(delay);
            animSet.start();
        } else {
            Log.i("ObjectAnimator", "API < 11, can't play animation");
        }
    }

    public void moveXYinpx(int view,
                           int fromX, int toX,
                           int durationX, int delayX, TimeInterpolator intX,
                           int fromY, int toY,
                           int durationY, int delayY, TimeInterpolator intY,
                           int duration, int delay, Activity activity) {

        if (Build.VERSION.SDK_INT >= 11) {
            View object = (View) activity.findViewById(view);

            Animator animSet = new AnimatorSet();

            ObjectAnimator animX = ObjectAnimator.ofFloat(object, "x", fromX, toX);
            animX.setDuration(durationX);
            animX.setStartDelay(delayX);
            animX.setInterpolator(intX);

            ObjectAnimator animY = ObjectAnimator.ofFloat(object, "y", fromY, toY);
            animY.setDuration(durationY);
            animY.setStartDelay(delayY);
            animY.setInterpolator(intY);

            animSet.setDuration(duration);
            animSet.setStartDelay(delay);
            animSet.start();
        } else {
            Log.i("ObjectAnimator", "API < 11, can't play animation");
        }
    }

    public void moveXYindp(View view,
                           int fromX, int toX,
                           int durationX, int delayX, TimeInterpolator intX,
                           int fromY, int toY,
                           int durationY, int delayY, TimeInterpolator intY,
                           int duration, int delay, Activity activity) {

        if (Build.VERSION.SDK_INT >= 11) {
            float scale = activity.getResources().getDisplayMetrics().density;

            Animator animSet = new AnimatorSet();

            ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", fromX * scale, toX * scale);
            animX.setDuration(durationX);
            animX.setStartDelay(delayX);
            animX.setInterpolator(intX);

            ObjectAnimator animY = ObjectAnimator.ofFloat(view, "y", fromY * scale, toY * scale);
            animY.setDuration(durationY);
            animY.setStartDelay(delayY);
            animY.setInterpolator(intY);

            animSet.setDuration(duration);
            animSet.setStartDelay(delay);
            animSet.start();
        } else {
            Log.i("ObjectAnimator", "API < 11, can't play animation");
        }
    }

    public void moveXYindp(int view,
                           int fromX, int toX,
                           int durationX, int delayX, TimeInterpolator intX,
                           int fromY, int toY,
                           int durationY, int delayY, TimeInterpolator intY,
                           int duration, int delay, Activity activity) {

        if (Build.VERSION.SDK_INT >= 11) {
            View object = (View) activity.findViewById(view);

            float scale = activity.getResources().getDisplayMetrics().density;

            Animator animSet = new AnimatorSet();

            ObjectAnimator animX = ObjectAnimator.ofFloat(object, "x", fromX * scale, toX * scale);
            animX.setDuration(durationX);
            animX.setStartDelay(delayX);
            animX.setInterpolator(intX);

            ObjectAnimator animY = ObjectAnimator.ofFloat(object, "y", fromY * scale, toY * scale);
            animY.setDuration(durationY);
            animY.setStartDelay(delayY);
            animY.setInterpolator(intY);

            animSet.setDuration(duration);
            animSet.setStartDelay(delay);
            animSet.start();
        } else {
            Log.i("ObjectAnimator", "API < 11, can't play animation");
        }
    }

    // CircularReveal Animation

    public void circularRevealinpx(int view,
                                   int centerX, int centerY,
                                   int startRad, int endRad, TimeInterpolator interpolator,
                                   int duration, int delay, Activity activity) {

        final View object = (View) activity.findViewById(view);

        if (Build.VERSION.SDK_INT >= 21) {
            Animator animator = ViewAnimationUtils.createCircularReveal(object, centerX, centerY, startRad, endRad);
            animator.setDuration(duration);
            animator.setStartDelay(delay);
            animator.setInterpolator(interpolator);
            animator.start();

            object.setVisibility(View.VISIBLE);

            if(startRad > endRad){
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        object.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        } else {
            Log.i("CircularReveal", "API < 21, No circular reveal used, fade effect used instead.");
            if (startRad < endRad) {
                //this.scaleIn(view, centerX, centerY, delay, duration, "scaleIn", activity);
                fadeIn(view, duration, delay, "circularIn", activity);
            } else {
                //this.scaleOut(view, centerX, centerY, delay, duration, "scaleIn", activity);
                fadeOut(view, duration, delay, activity);
            }
        }
    }

    public Animator circularRevealAnimator(int view,
                                   int centerX, int centerY,
                                   int startRad, int endRad, TimeInterpolator interpolator,
                                   int duration, int delay, Activity activity) {

        final View object = (View) activity.findViewById(view);

        if (Build.VERSION.SDK_INT >= 21) {
            Animator animator = ViewAnimationUtils.createCircularReveal(object, centerX, centerY, startRad, endRad);
            animator.setDuration(duration);
            animator.setStartDelay(delay);
            animator.setInterpolator(interpolator);
            animator.start();

            object.setVisibility(View.VISIBLE);

            if(startRad > endRad){
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        object.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            return animator;
        } else {
            Log.i("CircularReveal", "API < 21, No circular reveal used, scale effect used instead.");
            if (startRad < endRad) {
                this.scaleIn(view, centerX, centerY, delay, duration, "scaleIn", activity);
            } else {
                this.scaleOut(view, centerX, centerY, delay, duration, "scaleIn", activity);
            }

            return null;
        }
    }

    public void circularRevealindp(int view,
                                   int centerX, int centerY,
                                   int startRad, int endRad, TimeInterpolator interpolator,
                                   int duration, int delay, Activity activity) {

        final View object = (View) activity.findViewById(view);

        float scale = activity.getResources().getDisplayMetrics().density;

        if (Build.VERSION.SDK_INT >= 21) {
            Animator animator = ViewAnimationUtils.createCircularReveal(object, centerX * (int) scale, centerY * (int) scale, startRad * (int) scale, endRad * (int) scale);
            animator.setDuration(duration);
            animator.setStartDelay(delay);
            animator.setInterpolator(interpolator);
            animator.start();

            object.setVisibility(View.VISIBLE);

            if(startRad > endRad){
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        object.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        } else {
            Log.i("CircularReveal", "API < 21, No circular reveal used, scale effect used instead.");
            if (startRad < endRad) {
                this.scaleIn(view, window.convertDPtoPX(centerX, activity), window.convertDPtoPX(centerY, activity), delay, duration, "scaleIn", activity);
            } else {
                this.scaleOut(view, window.convertDPtoPX(centerX, activity), window.convertDPtoPX(centerY, activity), delay, duration, "scaleIn", activity);
            }
        }
    }

    public void circularRevealTouch(int touch_view, final int reveal_view,
                                    final TimeInterpolator interpolator, final Runnable onClick,
                                    final int duration, final int delay, final Activity activity){
        View touch = activity.findViewById(touch_view);
        final int coord[] = {0, 0};

        touch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[0] = (int)event.getRawX();
                coord[1] = (int)event.getRawY();

                return false;
            }
        });

        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularRevealinpx(reveal_view,
                        coord[0], coord[1],
                        0, (int)Math.hypot(window.getWindowWidthPx(activity), window.getWindowHeightPx(activity)) + 200, interpolator,
                        duration, delay, activity);
                onClick.run();
            }
        });
    }

    public void circularRevealTouch(View touch_view, final int reveal_view,
                                    final TimeInterpolator interpolator, final Runnable onClick,
                                    final int duration, final int delay, final Activity activity){
        final int coord[] = {0, 0};

        touch_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[0] = (int)event.getRawX();
                coord[1] = (int)event.getRawY();

                return false;
            }
        });

        touch_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularRevealinpx(reveal_view,
                        coord[0], coord[1],
                        0, (int)Math.hypot(window.getWindowWidthPx(activity), window.getWindowHeightPx(activity)) + 200, interpolator,
                        duration, delay, activity);
                onClick.run();
            }
        });
    }
}
