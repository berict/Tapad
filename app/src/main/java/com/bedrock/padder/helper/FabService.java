package com.bedrock.padder.helper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bedrock.padder.R;

public class FabService {
    ImageView image;
    RelativeLayout layout;
    RelativeLayout button;

    AnimService anim = new AnimService();
    WindowService window = new WindowService();
    ThemeService theme = new ThemeService();

    Activity activity;

    public void setFab(Activity a) {
        try {
            activity = a;

            image = (ImageView) activity.findViewById(R.id.fab_icon);
            layout = (RelativeLayout) activity.findViewById(R.id.fab);
            button = (RelativeLayout) activity.findViewById(R.id.fab_button);
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout.");
        }
    }

    public void setFab(Activity a, int image_id) {
        try {
            activity = a;

            image = (ImageView) activity.findViewById(R.id.fab_icon);
            layout = (RelativeLayout) activity.findViewById(R.id.fab);

            image.setImageResource(image_id);
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout.");
        }
    }

    public void show() {
        try {
            anim.scaleIn(layout, 0, 400, "fabIn");
            anim.scaleIn(image, 300, 200, "fabImageIn");
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void show(int delay) {
        try {
            if (delay < 0) {
                Log.i("Fab", "Animation delay negative ( < 0 ), reset to 0ms.");
                delay = 0;
            }

            anim.scaleIn(layout, delay, 400, "fabIn");
            anim.scaleIn(image, delay + 100, 200, "fabImageIn");
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void show(int delay, int duration) {
        try {
            if (duration < 200) {
                Log.i("Fab", "Animation duration so short ( < 200 ), reset to 400ms.");
                duration = 400;
            }

            if (delay < 0) {
                Log.i("Fab", "Animation delay negative ( < 0 ), reset to 0ms.");
                delay = 0;
            }

            anim.scaleIn(layout, delay, duration, "fabIn");
            anim.scaleIn(image, delay + 100, duration - 200, "fabImageIn");
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void hide() {
        try {
            anim.scaleOut(layout, 0, 400, "fabOut");
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void hide(int delay) {
        try {
            if (delay < 0) {
                Log.i("Fab", "Animation delay negative ( < 0 ), reset to 0ms.");
                delay = 0;
            }

            anim.scaleOut(layout, delay, 400, "fabOut");
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void hide(int delay, int duration) {
        try {
            if (duration < 200) {
                Log.i("Fab", "Animation duration so short ( < 200 ), reset to 400ms.");
                duration = 400;
            }

            if (delay < 0) {
                Log.i("Fab", "Animation delay negative ( < 0 ), reset to 0ms.");
                delay = 0;
            }

            anim.scaleOut(layout, delay, duration, "fabOut");
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void onClick(final Runnable runnable) {
        try {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runnable.run();
                }
            });
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void onTouch(final Runnable touchDown) {
        try {
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        touchDown.run();

                        return true;
                    }
                    return false;
                }
            });
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void onTouch(final Runnable touchDown, final Runnable touchUp) {
        try {
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        touchDown.run();

                        return true;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        touchUp.run();

                        return true;
                    }
                    return false;
                }
            });
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void onTouch(final Runnable onClick,
                        final Runnable onDoubleClick,
                        final Runnable onLongClick,
                        final Runnable onSwipeUp,
                        final Runnable onSwipeDown,
                        final Runnable onSwipeLeft,
                        final Runnable onSwipeRight) {
        try {
            button.setOnTouchListener(new OnSwipeTouchListener(activity) {

                @Override
                public void onClick() {
                    super.onClick();
                    // your on click here

                    onClick.run();
                }

                @Override
                public void onDoubleClick() {
                    super.onDoubleClick();
                    // your on onDoubleClick here

                    onDoubleClick.run();
                }

                @Override
                public void onLongClick() {
                    super.onLongClick();
                    // your on onLongClick here

                    onLongClick.run();
                }

                @Override
                public void onSwipeUp() {
                    super.onSwipeUp();
                    // your swipe up here

                    onSwipeUp.run();
                }

                @Override
                public void onSwipeDown() {
                    super.onSwipeDown();
                    // your swipe down here.

                    onSwipeDown.run();
                }

                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    // your swipe left here.

                    onSwipeLeft.run();
                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
                    // your swipe right here.

                    onSwipeRight.run();
                }
            });
        } catch (NullPointerException e) {
            Log.i("Fab", e.getMessage() + "No matching view found on current activity layout. Make sure to call setFab.");
        }
    }

    public void move() {
        if (Build.VERSION.SDK_INT >= 11) {
            float scale = activity.getResources().getDisplayMetrics().density;
            ObjectAnimator animX = ObjectAnimator.ofFloat(layout, "x", 284f * scale, 152f * scale);
            ObjectAnimator animY = ObjectAnimator.ofFloat(layout, "y", 516f * scale, 528f * scale);

            AnimatorSet animSet = new AnimatorSet();

            animX.setDuration(200);
            animX.setInterpolator(new AccelerateDecelerateInterpolator());
            animY.setDuration(150);
            animY.setInterpolator(new DecelerateInterpolator());

            animSet.play(animX).with(animY).after(0);
            animSet.setDuration(200);
            animSet.start();
        }
    }

    LinearLayout toolbarLayout;

    int originX, originY;

    public void showToolBar(int x, int y) {
        // if (Build.VERSION.SDK_INT >= 11) {
        //     // To be used in the next update

        //     anim.moveXYindp(R.id.fab,
        //             window.convertPXtoDP(window.getRect(R.id.fab, activity).left, activity) + 28, 188, 200, 0, new AccelerateInterpolator(),
        //             window.convertPXtoDP(window.getRect(R.id.fab, activity).top, activity), window.getWindowHeightdp(activity) - 28, 100, 100, new DecelerateInterpolator(),
        //             200, 0, activity);

        //     anim.circularRevealindp(R.id.toolbar, 188, window.getWindowHeightdp(activity) - 28, 50, 2000,
        //             new AccelerateDecelerateInterpolator(), 200, 250, activity);

        // } else {
        //     toolbarLayout.setVisibility(View.VISIBLE);
        // }
        //anim.scaleIn(R.id.toolbar, 0, 200, "toolbarIn", activity);
        anim.scaleIn(R.id.toolbar, x, y, 0, 200, "toolbarIn", activity);
    }

    public void hideToolBar(int x, int y) {
        anim.scaleOut(R.id.toolbar, x, y, 0, 200, "toolbarIn", activity);
    }
}
