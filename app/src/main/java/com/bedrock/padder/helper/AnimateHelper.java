package com.bedrock.padder.helper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
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

import static android.content.Context.POWER_SERVICE;

@TargetApi(14)
public class AnimateHelper {
    private WindowHelper window = new WindowHelper();

    // Fade animations

    public static String getViewId(View view) {
        String name = view.toString();
        return name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("}"));
    }

    public void fade(final int id, final float startAlpha, final float endAlpha, final int delay, final long duration, String handlerName, Activity activity) {
        final AlphaAnimation fade = new AlphaAnimation(startAlpha, endAlpha);
        final View view = activity.findViewById(id);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Map<String, Handler> handlerCreator = new HashMap<>();
                handlerCreator.put(handlerName, new Handler());

                handlerCreator.get(handlerName).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setAlpha(endAlpha);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT [GONE] effect for 0ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                view.setAlpha(endAlpha);
                Log.i("AnimateHelper",
                        getViewId(view) + " fade effect to " +
                                String.valueOf(endAlpha) + "f for 0ms with no delay");
            }
        } else {
            // normal fade out
            if (duration > 0) {
                // delay, needs an handler
                Map<String, Handler> handlerCreator = new HashMap<>();
                handlerCreator.put(handlerName, new Handler());

                handlerCreator.get(handlerName).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fade.setDuration(duration);
                        view.startAnimation(fade);
                        view.setVisibility(View.VISIBLE);

                        Log.i("AnimateHelper",
                                getViewId(view) + " fade effect from " +
                                        String.valueOf(startAlpha) + "f to " +
                                        String.valueOf(endAlpha) + "f for " +
                                        String.valueOf(duration) + "ms with " +
                                        String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                fade.setDuration(duration);
                view.startAnimation(fade);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimateHelper",
                        getViewId(view) + " fade effect from " +
                                String.valueOf(startAlpha) + "f to " +
                                String.valueOf(endAlpha) + "f for " +
                                String.valueOf(duration) + "ms with no delay");
            }

            fade.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view.setAlpha(startAlpha);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setAlpha(endAlpha);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void fadeIn(final int id, final int delay, final long duration, String handlerName, Activity activity) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        final View view = activity.findViewById(id);
        view.setVisibility(View.INVISIBLE);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Map<String, Handler> handlerCreator = new HashMap<>();
                handlerCreator.put(handlerName, new Handler());

                handlerCreator.get(handlerName).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.VISIBLE);

                        Log.i("AnimateHelper", getViewId(view) + " fade IN [VISIBLE] effect for 0ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                view.setVisibility(View.VISIBLE);
                Log.i("AnimateHelper",
                        getViewId(view) + " fade IN effect to 1.0f for 0ms with no delay");
            }
        } else {
            // normal fadeIn IN
            if (duration > 0) {
                // delay, needs an handler
                Map<String, Handler> handlerCreator = new HashMap<>();
                handlerCreator.put(handlerName, new Handler());

                handlerCreator.get(handlerName).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeIn.setDuration(duration);
                        view.startAnimation(fadeIn);
                        view.setVisibility(View.VISIBLE);

                        Log.i("AnimateHelper",
                                getViewId(view) + " fade IN effect for " +
                                        String.valueOf(duration) + "ms with " +
                                        String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                fadeIn.setDuration(duration);
                view.startAnimation(fadeIn);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimateHelper",
                        getViewId(view) + " fade IN effect for " +
                                String.valueOf(duration) + "ms with no delay");
            }
        }
    }

    public void fadeIn(final View view, final int delay, final long duration, String handlerName, Activity activity) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        view.setVisibility(View.INVISIBLE);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Map<String, Handler> handlerCreator = new HashMap<>();
                handlerCreator.put(handlerName, new Handler());

                handlerCreator.get(handlerName).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.VISIBLE);

                        Log.i("AnimateHelper", getViewId(view) + " fade IN [VISIBLE] effect for 0ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                view.setVisibility(View.VISIBLE);
                Log.i("AnimateHelper",
                        view.getId() + " fade IN effect to 1.0f for 0ms with no delay");
            }
        } else {
            // normal fadeIn IN
            if (duration > 0) {
                // delay, needs an handler
                Map<String, Handler> handlerCreator = new HashMap<>();
                handlerCreator.put(handlerName, new Handler());

                handlerCreator.get(handlerName).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeIn.setDuration(duration);
                        view.startAnimation(fadeIn);
                        view.setVisibility(View.VISIBLE);

                        Log.i("AnimateHelper",
                                getViewId(view) + " fade IN effect for " +
                                        String.valueOf(duration) + "ms with " +
                                        String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                fadeIn.setDuration(duration);
                view.startAnimation(fadeIn);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimateHelper",
                        getViewId(view) + " fade IN effect for " +
                                String.valueOf(duration) + "ms with no delay");
            }
        }
    }

    public void fadeIn(final int id, final int delay, final long duration, String handlerName, View parentView, Activity activity) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        final View view = parentView.findViewById(id);
        view.setVisibility(View.INVISIBLE);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Map<String, Handler> handlerCreator = new HashMap<>();
                handlerCreator.put(handlerName, new Handler());

                handlerCreator.get(handlerName).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.VISIBLE);

                        Log.i("AnimateHelper", getViewId(view) + " fade IN [VISIBLE] effect for 0ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                view.setVisibility(View.VISIBLE);
                Log.i("AnimateHelper",
                        getViewId(view) + " fade IN effect to 1.0f for 0ms with no delay");
            }
        } else {
            // normal fadeIn IN
            if (duration > 0) {
                // delay, needs an handler
                Map<String, Handler> handlerCreator = new HashMap<>();
                handlerCreator.put(handlerName, new Handler());

                handlerCreator.get(handlerName).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeIn.setDuration(duration);
                        view.startAnimation(fadeIn);
                        view.setVisibility(View.VISIBLE);

                        Log.i("AnimateHelper",
                                getViewId(view) + " fade IN effect for " +
                                        String.valueOf(duration) + "ms with " +
                                        String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                fadeIn.setDuration(duration);
                view.startAnimation(fadeIn);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimateHelper",
                        getViewId(view) + " fade IN effect for " +
                                String.valueOf(duration) + "ms with no delay");
            }
        }
    }

    public void fadeOut(final int id, final int delay, final long duration, Activity activity) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        final View view = activity.findViewById(id);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT [GONE] effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                }, delay + 10);
            } else {
                view.setVisibility(View.GONE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT [GONE] effect for " + String.valueOf(duration) + "ms with no delay");
            }
        } else {
            // normal fade out
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeOut.setDuration(duration);
                        view.startAnimation(fadeOut);
                        view.setVisibility(View.GONE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                }, delay + 10);
            } else {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);
                view.setVisibility(View.GONE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with no delay");
            }
        }
    }

    public void fadeOut(final View view, final int delay, final long duration, Activity activity) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT [GONE] effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                }, delay + 10);
            } else {
                view.setVisibility(View.GONE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT [GONE] effect for " + String.valueOf(duration) + "ms with no delay");
            }
        } else {
            // normal fade out
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeOut.setDuration(duration);
                        view.startAnimation(fadeOut);
                        view.setVisibility(View.GONE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                }, delay + 10);
            } else {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);
                view.setVisibility(View.GONE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with no delay");
            }
        }
    }

    public void fadeOut(final int id, final int delay, final long duration, View parentView, Activity activity) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        final View view = parentView.findViewById(id);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT [GONE] effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                }, delay + 10);
            } else {
                view.setVisibility(View.GONE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT [GONE] effect for " + String.valueOf(duration) + "ms with no delay");
            }
        } else {
            // normal fade out
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeOut.setDuration(duration);
                        view.startAnimation(fadeOut);
                        view.setVisibility(View.GONE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                }, delay + 10);
            } else {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);
                view.setVisibility(View.GONE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with no delay");
            }
        }
    }

    public void fadeOutInvisible(final int id, final int delay, final long duration, Activity activity) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        final View view = activity.findViewById(id);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.INVISIBLE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT [INVISIBLE] effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                view.setVisibility(View.INVISIBLE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT [INVISIBLE] effect for " + String.valueOf(duration) + "ms with no delay");
            }
        } else {
            // normal fade out
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeOut.setDuration(duration);
                        view.startAnimation(fadeOut);
                        view.setVisibility(View.INVISIBLE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);
                view.setVisibility(View.INVISIBLE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with no delay");
            }
        }
    }

    public void fadeOutInvisible(final int id, final int delay, final long duration, View parentView, Activity activity) {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        final View view = parentView.findViewById(id);
        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode()) {
            // power save mode on
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.INVISIBLE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT [INVISIBLE] effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                view.setVisibility(View.INVISIBLE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT [INVISIBLE] effect for " + String.valueOf(duration) + "ms with no delay");
            }
        } else {
            // normal fade out
            if (duration > 0) {
                // delay, needs an handler
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeOut.setDuration(duration);
                        view.startAnimation(fadeOut);
                        view.setVisibility(View.INVISIBLE);

                        Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                    }
                }, delay);
            } else {
                fadeOut.setDuration(duration);
                view.startAnimation(fadeOut);
                view.setVisibility(View.INVISIBLE);
                Log.i("AnimateHelper", getViewId(view) + " fade OUT effect for " + String.valueOf(duration) + "ms with no delay");
            }
        }
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

                Log.i("AnimateHelper", "slide IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    // Scale Animations

    public void scaleOut(final View view, final int delay, final long duration, String handlerName) {
        final ScaleAnimation scaleOut = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        if (delay > 0) {
            handlerCreator.get(handlerName).postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                    scaleOut.setDuration(duration);
                    view.startAnimation(scaleOut);

                    Log.i("AnimateHelper", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                }
            }, delay);
        } else {
            view.setVisibility(View.VISIBLE);
            scaleOut.setDuration(duration);
            view.startAnimation(scaleOut);

            Log.i("AnimateHelper", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
        }

        handlerCreator.get(handlerName).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, delay + 10);
    }

    public void scaleOut(final int id, final int delay, final long duration, String handlerName, Activity activity) {
        final View view = activity.findViewById(id);
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

                Log.i("AnimateHelper", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
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

                Log.i("AnimateHelper", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
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
        final View view = activity.findViewById(id);
        float x = touchX / window.getWindowWidthPx(activity);
        float y = touchY / window.getWindowHeightPx(activity);
        final ScaleAnimation scaleOut = new ScaleAnimation(1, 0, 1, 0, Animation.ABSOLUTE, x, Animation.ABSOLUTE, y);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        if (delay > 0) {
            handlerCreator.get(handlerName).postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                    scaleOut.setDuration(duration);
                    view.startAnimation(scaleOut);

                    Log.i("AnimateHelper", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                }
            }, delay);
        } else {
            view.setVisibility(View.VISIBLE);
            scaleOut.setDuration(duration);
            view.startAnimation(scaleOut);

            Log.i("AnimateHelper", "scale OUT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
        }

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

        if (delay > 0) {
            Map<String, Handler> handlerCreator = new HashMap<>();
            handlerCreator.put(handlerName, new Handler());

            handlerCreator.get(handlerName).postDelayed(new Runnable() {
                @Override
                public void run() {
                    scaleIn.setDuration(duration);
                    view.startAnimation(scaleIn);
                    view.setVisibility(View.VISIBLE);

                    Log.i("AnimateHelper", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                }
            }, delay);
        } else {
            scaleIn.setDuration(duration);
            view.startAnimation(scaleIn);
            view.setVisibility(View.VISIBLE);

            Log.i("AnimateHelper", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
        }
    }

    public void scaleIn(final int id, final int delay, final long duration, String handlerName, Activity activity) {
        final View view = activity.findViewById(id);
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

                Log.i("AnimateHelper", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
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

                Log.i("AnimateHelper", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
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
        final View view = activity.findViewById(id);
        float x = touchX / window.getWindowWidthPx(activity);
        float y = touchY / window.getWindowHeightPx(activity);
        final ScaleAnimation scaleOut = new ScaleAnimation(0, 1, 0, 1, Animation.ABSOLUTE, x, Animation.ABSOLUTE, y);

        scaleOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Map<String, Handler> handlerCreator = new HashMap<>();
        handlerCreator.put(handlerName, new Handler());

        if (delay > 0) {
            handlerCreator.get(handlerName).postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                    scaleOut.setDuration(duration);
                    view.startAnimation(scaleOut);

                    Log.i("AnimateHelper", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
                }
            }, delay);
        } else {
            view.setVisibility(View.VISIBLE);
            scaleOut.setDuration(duration);
            view.startAnimation(scaleOut);

            Log.i("AnimateHelper", "scale IN effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
        }

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

                Log.i("AnimateHelper", "scale IN OVERSHOOT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
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
                View view = activity.findViewById(view_id);
                scaleInOverShoot.setDuration(duration);
                view.startAnimation(scaleInOverShoot);
                view.setVisibility(View.VISIBLE);

                Log.i("AnimateHelper", "scale IN OVERSHOOT effect for " + String.valueOf(duration) + "ms with " + String.valueOf(delay) + "ms delay");
            }
        }, delay);
    }

    // Object Animations

    public void moveXYinPX(View view,
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

    public void moveXYinPX(int view,
                           int fromX, int toX,
                           int durationX, int delayX, TimeInterpolator intX,
                           int fromY, int toY,
                           int durationY, int delayY, TimeInterpolator intY,
                           int duration, int delay, Activity activity) {

        if (Build.VERSION.SDK_INT >= 11) {
            View object = activity.findViewById(view);

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

    public void moveXYinDP(View view,
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

    public void moveXYinDP(int view,
                           int fromX, int toX,
                           int durationX, int delayX, TimeInterpolator intX,
                           int fromY, int toY,
                           int durationY, int delayY, TimeInterpolator intY,
                           int duration, int delay, Activity activity) {

        if (Build.VERSION.SDK_INT >= 11) {
            View object = activity.findViewById(view);

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

    public void circularRevealInPx(int view,
                                   int centerX, int centerY,
                                   int startRad, int endRad, TimeInterpolator interpolator,
                                   int duration, int delay, Activity activity) {

        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
        final View object = activity.findViewById(view);

        if (Build.VERSION.SDK_INT >= 21 && powerManager.isPowerSaveMode() == false) {
            Animator animator = ViewAnimationUtils.createCircularReveal(object, centerX, centerY, startRad, endRad);
            animator.setDuration(duration);
            animator.setStartDelay(delay);
            animator.setInterpolator(interpolator);
            animator.start();

            object.setVisibility(View.VISIBLE);

            if (startRad > endRad) {
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

        final View object = activity.findViewById(view);

        if (Build.VERSION.SDK_INT >= 21) {
            Animator animator = ViewAnimationUtils.createCircularReveal(object, centerX, centerY, startRad, endRad);
            animator.setDuration(duration);
            animator.setStartDelay(delay);
            animator.setInterpolator(interpolator);
            animator.start();

            object.setVisibility(View.VISIBLE);

            if (startRad > endRad) {
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

    public void circularRevealinDP(int view,
                                   int centerX, int centerY,
                                   int startRad, int endRad, TimeInterpolator interpolator,
                                   int duration, int delay, Activity activity) {

        final View object = activity.findViewById(view);

        float scale = activity.getResources().getDisplayMetrics().density;

        if (Build.VERSION.SDK_INT >= 21) {
            Animator animator = ViewAnimationUtils.createCircularReveal(object, centerX * (int) scale, centerY * (int) scale, startRad * (int) scale, endRad * (int) scale);
            animator.setDuration(duration);
            animator.setStartDelay(delay);
            animator.setInterpolator(interpolator);
            animator.start();

            object.setVisibility(View.VISIBLE);

            if (startRad > endRad) {
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
                                    final int duration, final int delay, final Activity activity) {
        View touch = activity.findViewById(touch_view);
        final int coordinate[] = {0, 0};

        touch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinate[0] = (int) event.getRawX();
                coordinate[1] = (int) event.getRawY();

                return false;
            }
        });

        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularRevealInPx(reveal_view,
                        coordinate[0], coordinate[1],
                        0, (int) Math.hypot(window.getWindowWidthPx(activity), window.getWindowHeightPx(activity)) + 200, interpolator,
                        duration, delay, activity);
                onClick.run();
            }
        });
    }

    public void circularRevealTouch(View touch_view, final int reveal_view,
                                    final TimeInterpolator interpolator, final Runnable onClick,
                                    final int duration, final int delay, final Activity activity) {
        final int coordinate[] = {0, 0};

        touch_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinate[0] = (int) event.getRawX();
                coordinate[1] = (int) event.getRawY();

                return false;
            }
        });

        touch_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularRevealInPx(reveal_view,
                        coordinate[0], coordinate[1],
                        0, (int) Math.hypot(window.getWindowWidthPx(activity), window.getWindowHeightPx(activity)) + 200, interpolator,
                        duration, delay, activity);
                onClick.run();
            }
        });
    }
}
