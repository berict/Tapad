package com.bedrock.padder.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.bedrock.padder.R;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WindowService {

    public void setNavigationBar(int color_id, Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            if (color_id == R.color.transparent) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Window w = activity.getWindow();
                    w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                }
                Log.i("WindowService", "Transparent navigation bar color applied.");
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setNavigationBarColor(activity.getResources().getColor(color_id));
                    Log.i("WindowService", "Navigation bar color applied.");
                }
            }
        } else {
            Log.i("WindowService", "API doesn't match requirement. (API >= 16)");
        }
    }

    public int getNavigationBar(final int id, final Activity activity) {
        /** Must be a parent view */

        final SharedPreferences prefs = activity.getSharedPreferences("com.bedrock.padder", activity.MODE_PRIVATE);
        final int[] navBarHeight = {-1};

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = activity.findViewById(id);
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);

                Log.i("Navigation Bar Height", String.valueOf(getWindowHeightPx(activity) + " - " + String.valueOf(rect.bottom) +
                        " = " + String.valueOf(getWindowHeightPx(activity) - rect.bottom)));
                navBarHeight[0] = getWindowHeightPx(activity) - rect.bottom;

                prefs.edit().putInt("navBarPX", navBarHeight[0]).apply();
                Log.i("SharedPrefs", "navBarPX = " + String.valueOf(prefs.getInt("navBarPX", 0)));
            }
        }, 100);

        return navBarHeight[0];
    }

    public void setStatusBar(int color_id, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(color_id));

            Log.i("WindowService", "Status bar color applied.");
        } else {
            Log.i("WindowService", "API doesn't match requirement. (API >= 21)");
        }
    }

    public int getStatusBar(final int id, final Activity activity) {
        /** Must be a parent view */

        final SharedPreferences prefs = activity.getSharedPreferences("com.bedrock.padder", activity.MODE_PRIVATE);
        final int[] statBarHeight = {-1};

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = activity.findViewById(id);
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);

                Log.i("Status Bar Height", String.valueOf(rect.top));
                statBarHeight[0] = rect.top;

                prefs.edit().putInt("statBarPX", statBarHeight[0]).apply();
                Log.i("SharedPrefs", "statBarPX = " + String.valueOf(prefs.getInt("statBarPX", 0)));
            }
        }, 100);

        return statBarHeight[0];
    }

    public void setActionBarBack(final boolean backEnable, final Runnable back, final Activity activity) {
        View backLayout = activity.findViewById(R.id.actionbar_back_layout);
        View backButton = activity.findViewById(R.id.actionbar_back);
        if (backEnable == true) {
            backLayout.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back.run();
                }
            });
        } else {
            backLayout.setVisibility(View.GONE);
        }
    }

    public void setRecentColor(int titleId, int icon_id, int color_id, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (titleId == 0) {
                // Default app name
                titleId = R.string.app_name;
            }
            Bitmap icon;
            if (icon_id == 0) {
                // Default app icon
                icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);
            } else {
                icon = BitmapFactory.decodeResource(activity.getResources(), icon_id);
            }
            if (color_id == 0) {
                color_id = R.color.colorPrimary;
            }
            ActivityManager.TaskDescription taskDesc =
                    new ActivityManager.TaskDescription(
                            activity.getResources().getString(titleId),
                            icon,
                            activity.getResources().getColor(color_id));
            activity.setTaskDescription(taskDesc);

            Log.i("WindowService", "TaskDescription applied.");
        } else {
            Log.i("WindowService", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(int titleId, int color_id, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (titleId == 0) {
                // Default app name
                titleId = R.string.app_name;
            }

            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);

            if (color_id == 0) {
                color_id = R.color.colorPrimary;
            }
            ActivityManager.TaskDescription taskDesc =
                    new ActivityManager.TaskDescription(
                            activity.getResources().getString(titleId),
                            icon,
                            activity.getResources().getColor(color_id));
            activity.setTaskDescription(taskDesc);

            Log.i("WindowService", "TaskDescription applied.");
        } else {
            Log.i("WindowService", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(int titleId, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (titleId == 0) {
                // Default app name
                titleId = R.string.app_name;
            }

            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);

            ActivityManager.TaskDescription taskDesc =
                    new ActivityManager.TaskDescription(
                            activity.getResources().getString(titleId),
                            icon,
                            activity.getResources().getColor(R.color.colorPrimary));
            activity.setTaskDescription(taskDesc);

            Log.i("WindowService", "TaskDescription applied.");
        } else {
            Log.i("WindowService", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(String titleId, int icon_id, int color_id, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (titleId == null) {
                // Default app name
                titleId = activity.getResources().getString(R.string.app_name);
            }
            Bitmap icon;
            if (icon_id == 0) {
                // Default app icon
                icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);
            } else {
                icon = BitmapFactory.decodeResource(activity.getResources(), icon_id);
            }
            if (color_id == 0) {
                color_id = R.color.colorPrimary;
            }
            ActivityManager.TaskDescription taskDesc =
                    new ActivityManager.TaskDescription(
                            titleId,
                            icon,
                            activity.getResources().getColor(color_id));
            activity.setTaskDescription(taskDesc);

            Log.i("WindowService", "TaskDescription applied.");
        } else {
            Log.i("WindowService", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(String titleId, int color_id, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (titleId == null) {
                // Default app name
                titleId = activity.getResources().getString(R.string.app_name);
            }

            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);

            if (color_id == 0) {
                color_id = R.color.colorPrimary;
            }
            ActivityManager.TaskDescription taskDesc =
                    new ActivityManager.TaskDescription(
                            titleId,
                            icon,
                            activity.getResources().getColor(color_id));
            activity.setTaskDescription(taskDesc);

            Log.i("WindowService", "TaskDescription applied.");
        } else {
            Log.i("WindowService", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setViewBackgroundColor(int view_id, int color_id, Activity activity) {
        try {
            getView(view_id, activity).setBackgroundColor(activity.getResources().getColor(color_id));
        } catch (Resources.NotFoundException e) {
            Log.i("NotFoundException", "Handling with normal value");
            getView(view_id, activity).setBackgroundColor(color_id);
        }
    }

    public void hideKeyboard(Activity activity) {
        Log.i("HideKeyboard", "Called");
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            Log.i("HideKeyboard", "NullPointer");
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public int getWindowHeightPx(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int realHeight;

        if (Build.VERSION.SDK_INT >= 17) {
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realHeight = realMetrics.heightPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                realHeight = display.getHeight();
                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            realHeight = display.getHeight();
        }
        return realHeight;
    }

    public int getWindowWidthPx(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int realWidth;

        if (Build.VERSION.SDK_INT >= 17) {
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realWidth = realMetrics.widthPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                realWidth = display.getWidth();
                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            realWidth = display.getWidth();
        }
        return realWidth;
    }

    public int getWindowHypot(Activity activity) {
        int hypot = (int) Math.hypot(getWindowWidthPx(activity), getWindowHeightPx(activity)) + 200;
        return hypot;
    }

    public int convertPXtoDP(int px, Activity activity) {
        float scale = activity.getResources().getDisplayMetrics().density;

        return (int) (px / scale);
    }

    public int convertDPtoPX(int dp, Activity activity) {
        float scale = activity.getResources().getDisplayMetrics().density;

        return (int) (dp * scale);
    }

    public Rect getRect(int id, Activity activity) {
        View view = (View) activity.findViewById(id);

        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        return rect;
    }

    public View getView(int id, Activity activity) {
        View view = (View) activity.findViewById(id);
        return view;
    }

    public ProgressBar getProgressBar(int id, Activity activity) {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(id);
        return progressBar;
    }

    public Button getButton(int id, Activity activity) {
        Button button = (Button) activity.findViewById(id);
        return button;
    }

    public ToggleButton getToggleButton(int id, Activity activity) {
        ToggleButton toggle = (ToggleButton) activity.findViewById(id);
        return toggle;
    }

    public SwitchCompat getSwitchCompat(int id, Activity activity) {
        SwitchCompat view = (SwitchCompat) activity.findViewById(id);
        return view;
    }

    public ImageView getImageView(int id, Activity activity) {
        ImageView imageview = (ImageView) activity.findViewById(id);
        return imageview;
    }

    public TextView getTextView(int id, Activity activity) {
        TextView textview = (TextView) activity.findViewById(id);
        return textview;
    }

    public RelativeLayout getRelativeLayout(int id, Activity activity) {
        RelativeLayout relative = (RelativeLayout) activity.findViewById(id);
        return relative;
    }

    public LinearLayout getLinearLayout(int id, Activity activity) {
        LinearLayout linear = (LinearLayout) activity.findViewById(id);
        return linear;
    }

    public CardView getCardView(int id, Activity activity) {
        CardView card = (CardView) activity.findViewById(id);
        return card;
    }

    public VideoView getVideoView(int id, Activity activity) {
        VideoView video = (VideoView) activity.findViewById(id);
        return video;
    }

    public View getViewDialog(int id, Dialog dialog) {
        View view = (View) dialog.findViewById(id);
        return view;
    }

    public RecyclerView getRecyclerView(int id, Activity activity) {
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(id);
        return recyclerView;
    }

    public int getBackgroundColor(int id, Activity activity) {
        View view = getView(id, activity);
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            if (Build.VERSION.SDK_INT >= 11) {
                return colorDrawable.getColor();
            }
            try {
                Field field = colorDrawable.getClass().getDeclaredField("mState");
                field.setAccessible(true);
                Object object = field.get(colorDrawable);
                field = object.getClass().getDeclaredField("mUseColor");
                field.setAccessible(true);
                return field.getInt(object);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int getId(String id) {
        try {
            Class res = R.id.class;
            Field field = res.getField(id);
            return field.getInt(null);
        } catch (Exception e) {
            Log.e("getId", "Failure to get id.", e);
            return -1;
        }
        //from : https://daniel-codes.blogspot.com/2009/12/dynamically-retrieving-resources-in.html
    }

    public int getColorId(String id) {
        try {
            Class res = R.color.class;
            Field field = res.getField(id);
            return field.getInt(null);
        } catch (Exception e) {
            Log.e("getColorId", "Failure to get color id.", e);
            return -1;
        }
        //from : https://daniel-codes.blogspot.com/2009/12/dynamically-retrieving-resources-in.html
    }

    public int getDrawableId(String id) {
        try {
            Class res = R.drawable.class;
            Field field = res.getField(id);
            return field.getInt(null);
        } catch (Exception e) {
            Log.e("getDrawableId", "Failure to get drawable id.", e);
            return -1;
        }
        //from : https://daniel-codes.blogspot.com/2009/12/dynamically-retrieving-resources-in.html
    }

    public int getRawId(String id, Activity activity) {
        return activity.getResources().getIdentifier(id, "raw", activity.getPackageName());
    }

    // public Z getZ(int id, Activity activity) {
    //     Z z = (Z) activity.findViewById(id);
    //     return z;
    // }

    public void setMarginRelativePX(int id, int left, int top, int right, int bottom, Activity activity) {
        View v = activity.findViewById(id);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        v.setLayoutParams(params);
    }

    public void setMarginRelativeDP(int id, int left, int top, int right, int bottom, Activity activity) {
        View v = activity.findViewById(id);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(
                convertDPtoPX(left, activity),
                convertDPtoPX(top, activity),
                convertDPtoPX(right, activity),
                convertDPtoPX(bottom, activity));
        v.setLayoutParams(params);
    }

    public void setMarginLinearPX(int id, int left, int top, int right, int bottom, Activity activity) {
        View v = activity.findViewById(id);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        v.setLayoutParams(params);
    }

    public void setMarginLinearDP(int id, int left, int top, int right, int bottom, Activity activity) {
        View v = activity.findViewById(id);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(
                convertDPtoPX(left, activity),
                convertDPtoPX(top, activity),
                convertDPtoPX(right, activity),
                convertDPtoPX(bottom, activity));
        v.setLayoutParams(params);
    }

    public void setOnTouch(int id, final Runnable touch_down, final Runnable touch_up, Activity activity) {
        View view = activity.findViewById(id);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touch_down.run();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    touch_up.run();
                }
                return false;
            }
        });
    }

    public void setOnTouchColor(int id, final int color_down, final int color_up, final Activity activity) {
        final View view = activity.findViewById(id);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        view.setBackgroundColor(activity.getResources().getColor(color_down));
                    } catch (Resources.NotFoundException e) {
                        Log.i("NotFoundException", "Handling with normal value");
                        view.setBackgroundColor(color_down);
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(activity.getResources().getColor(color_up));
                }
                return false;
            }
        });
    }

    public void setOnTouchSound(int id, final int color_down, final int color_up, final SoundPool sp, final int soundId[], final int length, final Activity activity) {
        final View view = activity.findViewById(id);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        view.setBackgroundColor(activity.getResources().getColor(color_down));
                    } catch (Resources.NotFoundException e) {
                        Log.i("NotFoundException", "Handling with normal value");
                        view.setBackgroundColor(color_down);
                    }
                    if (soundId.length == 1) {
                        sp.play(soundId[0], 1, 1, 1, 0, 1f);
                    } else {
                        final int i[] = {0};
                        Handler repeat = new Handler();
                        Runnable play = new Runnable() {
                            @Override
                            public void run() {
                                sp.play(soundId[i[0]], 1, 1, 1, 0, 1f);
                            }
                        };

                        for (i[0] = 0; i[0] < length; i[0]++) {
                            if (i[0] == 0) {
                                play.run();
                            } else {
                                repeat.postDelayed(play, 500 * i[0]);
                            }
                        }
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(activity.getResources().getColor(color_up));
                }
                return false;
            }
        });
    }

    public void setOnTouchSoundPattern(int id, final int pattern[][], final int color_down, final int color_up, final SoundPool sp, final int soundId[], final int length, final Activity activity) {
        final View view = activity.findViewById(id);

        final int idnum[] = {0};
        switch (id) {
            case R.id.btn11:
                idnum[0] = 0;
                break;
            case R.id.btn12:
                idnum[0] = 1;
                break;
            case R.id.btn13:
                idnum[0] = 2;
                break;
            case R.id.btn14:
                idnum[0] = 3;
                break;
            case R.id.btn21:
                idnum[0] = 4;
                break;
            case R.id.btn22:
                idnum[0] = 5;
                break;
            case R.id.btn23:
                idnum[0] = 6;
                break;
            case R.id.btn24:
                idnum[0] = 7;
                break;
            case R.id.btn31:
                idnum[0] = 8;
                break;
            case R.id.btn32:
                idnum[0] = 9;
                break;
            case R.id.btn33:
                idnum[0] = 10;
                break;
            case R.id.btn34:
                idnum[0] = 11;
                break;
            case R.id.btn41:
                idnum[0] = 12;
                break;
            case R.id.btn42:
                idnum[0] = 13;
                break;
            case R.id.btn43:
                idnum[0] = 14;
                break;
            case R.id.btn44:
                idnum[0] = 15;
                break;
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        view.setBackgroundColor(activity.getResources().getColor(color_down));
                    } catch (Resources.NotFoundException e) {
                        Log.i("NotFoundException", "Handling with normal value");
                        view.setBackgroundColor(color_down);
                    }
                    for (int i = 0; i < pattern[idnum[0]].length; i++) {
                        try {
                            getView(pattern[idnum[0]][i], activity).setBackgroundColor(activity.getResources().getColor(color_down));
                        } catch (Resources.NotFoundException e) {
                            Log.i("NotFoundException", "Handling with normal value");
                            getView(pattern[idnum[0]][i], activity).setBackgroundColor(color_down);
                        }
                    }
                    if (soundId.length == 1) {
                        sp.play(soundId[0], 1, 1, 1, 0, 1f);
                    } else {
                        for (int i = 0; i < length; i++) {
                            setSoundPoolDelay(sp, soundId, i, i * 5000);
                        }
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(activity.getResources().getColor(color_up));
                    for (int i = 0; i < pattern[idnum[0]].length; i++) {
                        getView(pattern[idnum[0]][i], activity).setBackgroundColor(activity.getResources().getColor(color_up));
                    }
                }
                return false;
            }
        });
    }

    int buttonIds[] = {
            R.id.btn11,
            R.id.btn12,
            R.id.btn13,
            R.id.btn14,
            R.id.btn21,
            R.id.btn22,
            R.id.btn23,
            R.id.btn24,
            R.id.btn31,
            R.id.btn32,
            R.id.btn33,
            R.id.btn34,
            R.id.btn41,
            R.id.btn42,
            R.id.btn43,
            R.id.btn44
    };

    public void setOnGestureSoundPattern(int padId, final int patternScheme[][], final int colorDown, final int colorUp, final SoundPool sp, final int spid[], final Activity activity) {
        final int btnId[] = {0};
        final boolean isLoopEnabled[] = {false};
        switch (padId) {
            case R.id.btn11:
                btnId[0] = 0;
                break;
            case R.id.btn12:
                btnId[0] = 1;
                break;
            case R.id.btn13:
                btnId[0] = 2;
                break;
            case R.id.btn14:
                btnId[0] = 3;
                break;
            case R.id.btn21:
                btnId[0] = 4;
                break;
            case R.id.btn22:
                btnId[0] = 5;
                break;
            case R.id.btn23:
                btnId[0] = 6;
                break;
            case R.id.btn24:
                btnId[0] = 7;
                break;
            case R.id.btn31:
                btnId[0] = 8;
                break;
            case R.id.btn32:
                btnId[0] = 9;
                break;
            case R.id.btn33:
                btnId[0] = 10;
                break;
            case R.id.btn34:
                btnId[0] = 11;
                break;
            case R.id.btn41:
                btnId[0] = 12;
                break;
            case R.id.btn42:
                btnId[0] = 13;
                break;
            case R.id.btn43:
                btnId[0] = 14;
                break;
            case R.id.btn44:
                btnId[0] = 15;
                break;
        }
        final View pad = activity.findViewById(padId);
        final int streamId[] = {0};
        pad.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onTouch() {
                try {
                    pad.setBackgroundColor(activity.getResources().getColor(colorDown));
                    setButtonPattern(patternScheme, btnId[0], colorDown, activity);
                } catch (Resources.NotFoundException e) {
                    Log.i("NotFoundException", "Handling with normal value");
                    pad.setBackgroundColor(colorDown);
                    setButtonPattern(patternScheme, btnId[0], colorDown, activity);
                }
            }

            @Override
            public void onDoubleClick() {
                sp.play(spid[0], 1, 1, 1, 0, 1);
                Handler backgroundChangeHandler = new Handler();
                backgroundChangeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLoopEnabled[0] == false) {
                            pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                        }
                        setButtonPattern(patternScheme, btnId[0], colorUp, activity);
                    }
                }, 10);
                Log.d("TouchListener", "Double Click");
            }

            @Override
            public void onClick() {
                sp.play(spid[0], 1, 1, 1, 0, 1);
                if (isLoopEnabled[0] == false) {
                    pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                }
                setButtonPattern(patternScheme, btnId[0], colorUp, activity);
                Log.d("TouchListener", "Click");
            }

            @Override
            public void onSwipeUp() {
                if (spid[1] != 0) {
                    sp.play(spid[1], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                setButtonPattern(patternScheme, btnId[0], colorUp, activity);
                Log.d("TouchListener", "SwipeUp");
            }

            @Override
            public void onSwipeRight() {
                if (spid[2] != 0) {
                    sp.play(spid[2], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                setButtonPattern(patternScheme, btnId[0], colorUp, activity);
                Log.d("TouchListener", "SwipeRight");
            }

            @Override
            public void onSwipeDown() {
                if (spid[3] != 0) {
                    sp.play(spid[3], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                setButtonPattern(patternScheme, btnId[0], colorUp, activity);
                Log.d("TouchListener", "SwipeDown");
            }

            @Override
            public void onSwipeLeft() {
                if (spid[4] != 0) {
                    sp.play(spid[4], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                setButtonPattern(patternScheme, btnId[0], colorUp, activity);
                Log.d("TouchListener", "SwipeLeft");
            }

            @Override
            public void onLongClick() {
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                if (isLoopEnabled[0] == false) {
                    streamId[0] = sp.play(spid[0], 1, 1, 1, -1, 1);
                    isLoopEnabled[0] = true;
                    pad.setBackgroundColor(activity.getResources().getColor(colorDown));
                    Log.d("TouchListener", "LongClick, loop on");
                } else {
                    sp.stop(streamId[0]);
                    isLoopEnabled[0] = false;
                    pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                    Log.d("TouchListener", "LongClick, loop off");
                }
                setButtonPattern(patternScheme, btnId[0], colorUp, activity);
            }
        });
    }

    // TODO NEXT UPDATE on check algorithm, use on next update
//    void setButtonPattern(int patternScheme[][], int btnId, int colorDown, int colorUp, int mode, Activity activity) {
//        boolean wasAlreadyOn[] = {
//                false, false, false, false,
//                false, false, false, false,
//                false, false, false, false,
//                false, false, false, false
//        };
//        if (mode == 1) {
//            // button pressed (down)
//            for (int i = 0; i < buttonIds.length; i++) {
//                if (getBackgroundColor(buttonIds[btnId], activity) == colorDown) {
//                    wasAlreadyOn[btnId] = true;
//                }
//            }
//            for (int i = 0; i < patternScheme[btnId].length; i++) {
//                try {
//                    getView(patternScheme[btnId][i], activity).setBackgroundColor(activity.getResources().getColor(colorDown));
//                } catch (Resources.NotFoundException e) {
//                    Log.i("NotFoundException", "Handling with normal value");
//                    getView(patternScheme[btnId][i], activity).setBackgroundColor(colorDown);
//                }
//            }
//        } else if (mode == 0) {
//            // button clicked (up)
//            for (int i = 0; i < patternScheme[btnId].length; i++) {
//                if (wasAlreadyOn[btnId] == false) {
//                    try {
//                        getView(patternScheme[btnId][i], activity).setBackgroundColor(activity.getResources().getColor(colorUp));
//                    } catch (Resources.NotFoundException e) {
//                        Log.i("NotFoundException", "Handling with normal value");
//                        getView(patternScheme[btnId][i], activity).setBackgroundColor(colorUp);
//                    }
//                    wasAlreadyOn[btnId] = true;
//                }
//            }
//        } else {
//            Log.d("NotFoundException", "Wrong touch mode");
//        }
//    }

    void setButtonPattern(int patternScheme[][], int btnId, int color, Activity activity) {
        for (int i = 0; i < patternScheme[btnId].length; i++) {
            try {
                getView(patternScheme[btnId][i], activity).setBackgroundColor(activity.getResources().getColor(color));
            } catch (Resources.NotFoundException e) {
                Log.i("NotFoundException", "Handling with normal value");
                getView(patternScheme[btnId][i], activity).setBackgroundColor(color);
            }
        }
    }

    void setSoundPoolDelay(final SoundPool sp, final int soundId[], final int count, final int delay) {
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("SoundPoolDelay", "SoundPool played on " + soundId[count] + " with " + delay + "ms delay");
                sp.play(soundId[count], 1, 1, 1, 0, 1f);
            }
        }, delay);
    }

    public void setOnGestureSound(int padId, final int colorDown, final int colorUp, final SoundPool sp, final int spid[], final Activity activity) {
        final boolean isLoopEnabled[] = {false};
        final View pad = activity.findViewById(padId);
        final int streamId[] = {0};
        pad.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onTouch() {
                try {
                    pad.setBackgroundColor(activity.getResources().getColor(colorDown));
                } catch (Resources.NotFoundException e) {
                    Log.i("NotFoundException", "Handling with normal value");
                    pad.setBackgroundColor(colorDown);
                }
            }

            @Override
            public void onDoubleClick() {
                sp.play(spid[0], 1, 1, 1, 0, 1);
                Handler backgroundChangeHandler = new Handler();
                backgroundChangeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLoopEnabled[0] == false) {
                            pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                        }
                    }
                }, 10);
                Log.d("TouchListener", "Double Click");
            }

            @Override
            public void onClick() {
                sp.play(spid[0], 1, 1, 1, 0, 1);
                if (isLoopEnabled[0] == false) {
                    pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                }
                Log.d("TouchListener", "Click");
            }

            @Override
            public void onSwipeUp() {
                if (spid[1] != 0) {
                    sp.play(spid[1], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                Log.d("TouchListener", "SwipeUp");
            }

            @Override
            public void onSwipeRight() {
                if (spid[2] != 0) {
                    sp.play(spid[2], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                Log.d("TouchListener", "SwipeRight");
            }

            @Override
            public void onSwipeDown() {
                if (spid[3] != 0) {
                    sp.play(spid[3], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                Log.d("TouchListener", "SwipeDown");
            }

            @Override
            public void onSwipeLeft() {
                if (spid[4] != 0) {
                    sp.play(spid[4], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                Log.d("TouchListener", "SwipeLeft");
            }

            @Override
            public void onLongClick() {
                if (isLoopEnabled[0] == false) {
                    streamId[0] = sp.play(spid[0], 1, 1, 1, -1, 1);
                    isLoopEnabled[0] = true;
                    Log.d("TouchListener", "LongClick, loop on");
                } else {
                    sp.stop(streamId[0]);
                    isLoopEnabled[0] = false;
                    pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                    Log.d("TouchListener", "LongClick, loop off");
                }
            }
        });
    }

    public void setOnTouchSound(int id, final SoundPool sp, final int soundId, final Activity activity) {
        final View view = activity.findViewById(id);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sp.play(soundId, 1, 1, 1, 0, 1f);
                }
                return false;
            }
        });
    }

    public void setOnTouch(int id, final Runnable touch, Activity activity) {
        View view = activity.findViewById(id);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch.run();
                return false;
            }
        });
    }

    public void setOnClick(int id, final Runnable click, Activity activity) {
        View view = activity.findViewById(id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.run();
            }
        });
    }

    public int getDeviceRam() {
        RandomAccessFile reader = null;
        String load = null;
        double totRam = 0;
        double totRamMb = 0;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            totRamMb = totRam / 1024.0;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        Log.i("getDeviceRam", "Device Ram requested = returned " + String.valueOf(totRamMb));

        return (int) totRamMb;
    }
}
