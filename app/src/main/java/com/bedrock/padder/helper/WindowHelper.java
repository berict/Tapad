package com.bedrock.padder.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class WindowHelper {

    public static final String APPLICATION_ID = "com.bedrock.padder";

    public void setNavigationBar(int colorId, Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            if (colorId == R.color.transparent) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Window w = activity.getWindow();
                    w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                }
                Log.i("WindowHelper", "Transparent navigation bar color applied.");
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setNavigationBarColor(activity.getResources().getColor(colorId));
                    Log.i("WindowHelper", "Navigation bar color applied.");
                }
            }
        } else {
            Log.i("WindowHelper", "API doesn't match requirement. (API >= 16)");
        }
    }

    public int getNavigationBar(final int id, final Activity activity) {
        /* Must be a parent view */

        final SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
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
                Log.i("SharedPrefs", "navBarPX = " + String.valueOf(prefs.getInt("navBarPX", 144)));
            }
        }, 100);

        return navBarHeight[0];
    }

    public int getNavigationBarFromPrefs(Activity activity) {
        int navigationHeight;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        navigationHeight = sharedPreferences.getInt("navBarPX", 144);
        
        if (navigationHeight >= 540 || navigationHeight < 0) {
            // something gone wrong
            navigationHeight = 144;
        }
        
        return navigationHeight;
    }

    public void setStatusBar(int colorId, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, colorId));

            Log.i("WindowHelper", "Status bar color applied.");
        } else {
            Log.i("WindowHelper", "API doesn't match requirement. (API >= 21)");
        }
    }

    public int getStatusBar(final int id, final Activity activity) {
        /* Must be a parent view */

        final SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        final int[] statBarHeight = {-1};

        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statBarHeight[0] = activity.getResources().getDimensionPixelSize(resourceId);
        }

        prefs.edit().putInt("statBarPX", statBarHeight[0]).apply();
        Log.i("SharedPrefs", "statBarPX = " + String.valueOf(prefs.getInt("statBarPX", 72)));

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                Rect rectangle = new Rect();
////                Window window = activity.getWindow();
////                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
////                int statusBar = rectangle.top;
////                Log.i("Status Bar Height", String.valueOf(statusBar));
////                statBarHeight[0] = statusBar;
//
////                View view = activity.findViewById(id);
////                Rect rect = new Rect();
////                view.getGlobalVisibleRect(rect);
////
////                Log.i("Status Bar Height", String.valueOf(rect.top));
////                statBarHeight[0] = rect.top;
//            }
//        }, 10);

        return statBarHeight[0];
    }

    public int getStatusBarFromPrefs(Activity activity) {
        int statusHeight;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        statusHeight = sharedPreferences.getInt("statBarPX", 72);

        if (statusHeight >= 240 || statusHeight <= 0) {
            // something gone wrong
            statusHeight = 72;
        }

        return statusHeight;
    }

    public void setVisible(final int viewId, final int delay, final Activity activity){
        final View view = activity.findViewById(viewId);
        if (delay > 0) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                }
            }, delay);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void setInvisible(final int viewId, final int delay, final Activity activity){
        final View view = activity.findViewById(viewId);
        if (delay > 0) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.INVISIBLE);
                }
            }, delay);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void setGone(final int viewId, final int delay, final Activity activity){
        final View view = activity.findViewById(viewId);
        if (delay > 0) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, delay);
        } else {
            view.setVisibility(View.GONE);
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

            Log.i("WindowHelper", "TaskDescription applied.");
        } else {
            Log.i("WindowHelper", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(int titleId, int color, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (titleId == 0) {
                // Default app name
                titleId = R.string.app_name;
            }

            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);

            // color id - color
            try {
                ActivityManager.TaskDescription taskDesc =
                        new ActivityManager.TaskDescription(
                                activity.getResources().getString(titleId),
                                icon,
                                activity.getResources().getColor(color));
                activity.setTaskDescription(taskDesc);
            } catch (Resources.NotFoundException e) {
                Log.i("NotFoundException", "Handling with normal value");
                ActivityManager.TaskDescription taskDesc =
                        new ActivityManager.TaskDescription(
                                activity.getResources().getString(titleId),
                                icon,
                                color);
                activity.setTaskDescription(taskDesc);
            }

            Log.i("WindowHelper", "TaskDescription applied.");
        } else {
            Log.i("WindowHelper", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(int titleId, int iconId, int color, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (titleId == 0) {
                // Default app name
                titleId = R.string.app_name;
            }

            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), iconId);

            // color id - color
            try {
                ActivityManager.TaskDescription taskDesc =
                        new ActivityManager.TaskDescription(
                                activity.getResources().getString(titleId),
                                icon,
                                activity.getResources().getColor(color));
                activity.setTaskDescription(taskDesc);
            } catch (Resources.NotFoundException e) {
                Log.i("NotFoundException", "Handling with normal value");
                ActivityManager.TaskDescription taskDesc =
                        new ActivityManager.TaskDescription(
                                activity.getResources().getString(titleId),
                                icon,
                                color);
                activity.setTaskDescription(taskDesc);
            }

            Log.i("WindowHelper", "TaskDescription applied.");
        } else {
            Log.i("WindowHelper", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(String title, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (title == null) {
                // Default app name
                title = activity.getResources().getString(R.string.app_name);
            }

            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);

            ActivityManager.TaskDescription taskDesc =
                    new ActivityManager.TaskDescription(
                            title,
                            icon,
                            activity.getResources().getColor(R.color.colorPrimary));
            activity.setTaskDescription(taskDesc);

            Log.i("WindowHelper", "TaskDescription applied.");
        } else {
            Log.i("WindowHelper", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(String title, int color, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (title == null) {
                // Default app name
                title = activity.getResources().getString(R.string.app_name);
            }

            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);

            // color id - color
            try {
                ActivityManager.TaskDescription taskDesc =
                        new ActivityManager.TaskDescription(
                                title,
                                icon,
                                activity.getResources().getColor(color));
                activity.setTaskDescription(taskDesc);
            } catch (Resources.NotFoundException e) {
                Log.i("NotFoundException", "Handling with normal value");
                ActivityManager.TaskDescription taskDesc =
                        new ActivityManager.TaskDescription(
                                title,
                                icon,
                                color);
                activity.setTaskDescription(taskDesc);
            }

            Log.i("WindowHelper", "TaskDescription applied.");
        } else {
            Log.i("WindowHelper", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setRecentColor(String title, int iconId, int color, Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (title == null) {
                // Default app name
                title = activity.getResources().getString(R.string.app_name);
            }

            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), iconId);

            // color id - color
            try {
                ActivityManager.TaskDescription taskDesc =
                        new ActivityManager.TaskDescription(title, icon, activity.getResources().getColor(color));
                activity.setTaskDescription(taskDesc);
            } catch (Resources.NotFoundException e) {
                Log.i("NotFoundException", "Handling with normal value");
                ActivityManager.TaskDescription taskDesc =
                        new ActivityManager.TaskDescription(title, icon, color);
                activity.setTaskDescription(taskDesc);
            }

            Log.i("WindowHelper", "TaskDescription applied.");
        } else {
            Log.i("WindowHelper", "API doesn't match requirement. (API >= 21)");
        }
    }

    public void setViewBackgroundDrawable (int viewId, int drawableId, Activity activity){
        View view = activity.findViewById(viewId);

        Drawable drawable;

        if (Build.VERSION.SDK_INT >= 21) {
            drawable = activity.getResources().getDrawable(drawableId, activity.getTheme());
        } else {
            drawable = activity.getResources().getDrawable(drawableId);
        }

        if (Build.VERSION.SDK_INT >= 16){
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }

        /** Dammit google, stop deprecating methods! */
    }

    public void setViewBackgroundColor(int viewId, int colorId, Activity activity) {
        try {
            getView(viewId, activity).setBackgroundColor(activity.getResources().getColor(colorId));
        } catch (Resources.NotFoundException e) {
            Log.i("NotFoundException", "Handling with normal value");
            getView(viewId, activity).setBackgroundColor(colorId);
        }
    }

    public void setViewBackgroundColor(int viewId, int colorId, Activity activity, View view) {
        try {
            getView(viewId, view).setBackgroundColor(activity.getResources().getColor(colorId));
        } catch (Resources.NotFoundException e) {
            Log.i("NotFoundException", "Handling with normal value");
            getView(viewId, view).setBackgroundColor(colorId);
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
        View view = activity.findViewById(id);

        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        return rect;
    }

    public View getView(int id, Activity activity) {
        return activity.findViewById(id);
    }

    public View getView(int id, View view) {
        return view.findViewById(id);
    }

    public ProgressBar getProgressBar(int id, Activity activity) {
        return (ProgressBar) activity.findViewById(id);
    }

    public Button getButton(int id, Activity activity) {
        return (Button) activity.findViewById(id);
    }

    public Toolbar getToolbar(int id, Activity activity) {
        return (Toolbar) activity.findViewById(id);
    }

    public Toolbar getToolbar(int id, View view) {
        return (Toolbar) view.findViewById(id);
    }

    public ToggleButton getToggleButton(int id, Activity activity) {
        return (ToggleButton) activity.findViewById(id);
    }

    public SwitchCompat getSwitchCompat(int id, Activity activity) {
        return (SwitchCompat) activity.findViewById(id);
    }

    public SwitchCompat getSwitchCompat(int id, View view) {
        return (SwitchCompat) view.findViewById(id);
    }

    public ImageView getImageView(int id, Activity activity) {
        return (ImageView) activity.findViewById(id);
    }

    public ImageView getImageView(int id, View view) {
        return (ImageView) view.findViewById(id);
    }

    public TextView getTextView(int id, Activity activity) {
        return (TextView) activity.findViewById(id);
    }

    public TextView getTextView(int id, View view) {
        return (TextView) view.findViewById(id);
    }

    public AdView getAdView(int id, Activity activity) {
        return (AdView) activity.findViewById(id);
    }

    public NativeExpressAdView getNativeAdView(int id, Activity activity) {
        return (NativeExpressAdView) activity.findViewById(id);
    }

    public RelativeLayout getRelativeLayout(int id, Activity activity) {
        return (RelativeLayout) activity.findViewById(id);
    }

    public LinearLayout getLinearLayout(int id, Activity activity) {
        return (LinearLayout) activity.findViewById(id);
    }

    public CardView getCardView(int id, Activity activity) {
        return (CardView) activity.findViewById(id);
    }

    public VideoView getVideoView(int id, Activity activity) {
        return (VideoView) activity.findViewById(id);
    }

    public View getViewDialog(int id, Dialog dialog) {
        return dialog.findViewById(id);
    }

    public RecyclerView getRecyclerView(int id, Activity activity) {
        return (RecyclerView) activity.findViewById(id);
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

    public int getColorFromString(String colorString) {
        return Color.parseColor(colorString);
    }

    public int getStringId(String id) {
        try {
            Class res = R.string.class;
            Field field = res.getField(id);
            return field.getInt(null);
        } catch (Exception e) {
            Log.e("getStringId", "Failure to get string id.", e);
            return -1;
        }
        //from : https://daniel-codes.blogspot.com/2009/12/dynamically-retrieving-resources-in.html
    }

    public String getStringFromId(String id, Activity activity) {
        try {
            Class res = R.string.class;
            Field field = res.getField(id);
            return activity.getResources().getString(field.getInt(null));
        } catch (Exception e) {
            Log.e("getStringFromId", "Failure to get string id.", e);
            return null;
        }
        //from : https://daniel-codes.blogspot.com/2009/12/dynamically-retrieving-resources-in.html
    }

    public String getStringFromId(int resId, Activity activity) {
        return activity.getResources().getString(resId);
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

    public void setOnTouch(int id, final Runnable touchDown, final Runnable touchUp, Activity activity) {
        View view = activity.findViewById(id);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchDown.run();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    touchUp.run();
                }
                return false;
            }
        });
    }

    public void setOnTouchColor(int id, final int colorDown, final int colorUp, final Activity activity) {
        final View view = activity.findViewById(id);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        view.setBackgroundColor(activity.getResources().getColor(colorDown));
                    } catch (Resources.NotFoundException e) {
                        Log.i("NotFoundException", "Handling with normal value");
                        view.setBackgroundColor(colorDown);
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(activity.getResources().getColor(colorUp));
                }
                return false;
            }
        });
    }

    public void setOnTouchSoundPattern(int id, final int pattern[][],
                                       final int colorDown, final int colorUp,
                                       final SoundPool sp, final int soundId[],
                                       final int length, final Activity activity) {
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
                        view.setBackgroundColor(activity.getResources().getColor(colorDown));
                    } catch (Resources.NotFoundException e) {
                        Log.i("NotFoundException", "Handling with normal value");
                        view.setBackgroundColor(colorDown);
                    }
                    for (int i = 0; i < pattern[idnum[0]].length; i++) {
                        try {
                            getView(pattern[idnum[0]][i], activity).setBackgroundColor(activity.getResources().getColor(colorDown));
                        } catch (Resources.NotFoundException e) {
                            Log.i("NotFoundException", "Handling with normal value");
                            getView(pattern[idnum[0]][i], activity).setBackgroundColor(colorDown);
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
                    view.setBackgroundColor(activity.getResources().getColor(colorUp));
                    for (int i = 0; i < pattern[idnum[0]].length; i++) {
                        getView(pattern[idnum[0]][i], activity).setBackgroundColor(activity.getResources().getColor(colorUp));
                    }
                }
                return false;
            }
        });
    }

    private ArrayList<Integer> loopStreamIds = new ArrayList<>();

    private void addLoopStreamId(Integer id) {
        Log.d("addLoopStreamId", "added " + id);
        loopStreamIds.add(id);
    }

    private void removeLoopStreamId(Integer id) {
        Log.d("removeLoopStreamId", "removed " + id);
        loopStreamIds.remove(id);
    }

    public void clearLoopStreamId() {
        Log.d("clearLoopStreamId", "cleared");
        loopStreamIds.clear();
    }

    public Integer[] getLoopStreamIds() {
        Log.d("getLoopStreamIds", "length = " + loopStreamIds.size());
        return loopStreamIds.toArray(new Integer[loopStreamIds.size()]);
    }

    public void setPadColor(View pad, int color, Activity activity) {
        try {
            pad.setBackgroundColor(activity.getResources().getColor(color));
        } catch (Resources.NotFoundException e) {
            Log.i("NotFoundException", "Handling with normal value");
            pad.setBackgroundColor(color);
        }
    }

    public void setPadColor(int padId, int color, Activity activity) {
        View pad = getView(padId, activity);
        try {
            pad.setBackgroundColor(activity.getResources().getColor(color));
        } catch (Resources.NotFoundException e) {
            Log.i("NotFoundException", "Handling with normal value");
            pad.setBackgroundColor(color);
        }
    }
    
    private int getButtonId(int padId) {
        switch (padId) {
            case R.id.btn00:
                return -1;
            case R.id.btn11:
                return 0;
            case R.id.btn12:
                return 1;
            case R.id.btn13:
                return 2;
            case R.id.btn14:
                return 3;
            case R.id.btn21:
                return 4;
            case R.id.btn22:
                return 5;
            case R.id.btn23:
                return 6;
            case R.id.btn24:
                return 7;
            case R.id.btn31:
                return 8;
            case R.id.btn32:
                return 9;
            case R.id.btn33:
                return 10;
            case R.id.btn34:
                return 11;
            case R.id.btn41:
                return 12;
            case R.id.btn42:
                return 13;
            case R.id.btn43:
                return 14;
            case R.id.btn44:
                return 15;
            default:
                return -1;
        }
    }

    public void setOnTouchSound(final int padId,
                                final int colorDown, final int colorUp,
                                final SoundPool sp, final int spid[],
                                final Activity activity) {
        // Normal
        final View pad = activity.findViewById(padId);
        final int streamId[] = {0};

        pad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    streamId[0] = sp.play(spid[0], 1, 1, 1, -1, 1);
                    setPadColor(pad, colorDown, activity);
                    Log.d("TouchListener", "TouchDown");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    sp.stop(streamId[0]);
                    setPadColor(pad, colorUp, activity);
                    Log.d("TouchListener", "TouchUp");
                }
                return false;
            }
        });
    }

    public void setOnTouchSound(final int padId,
                                final int colorDown, final int colorUp,
                                final SoundPool sp, final int spid[],
                                final int patternPreset[][][], final Activity activity) {
        // Normal Pattern
        final int btnId[] = {getButtonId(padId)};

        final View pad = activity.findViewById(padId);
        final int streamId[] = {0};

        pad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    streamId[0] = sp.play(spid[0], 1, 1, 1, -1, 1);
                    setPadColor(pad, colorDown, activity);
                    setButtonPattern(patternPreset, btnId[0], colorDown, colorUp, activity);
                    Log.d("TouchListener", "TouchDown");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    sp.stop(streamId[0]);
                    setPadColor(pad, colorUp, activity);
                    setButtonPatternDefault(patternPreset, btnId[0], colorUp, activity);
                    Log.d("TouchListener", "TouchUp");
                }
                return false;
            }
        });
    }

    public void setOnGestureSound(final int padId,
                                  final int colorDown, final int colorUp,
                                  final SoundPool sp, final int spid[],
                                  final Activity activity) {
        // Gesture
        final boolean isLoopEnabled[] = {false};
        final View pad = activity.findViewById(padId);
        final int streamId[] = {0};

        final Handler buttonDelay = new Handler();
        final Runnable clearPad = new Runnable() {
            @Override
            public void run() {
                if (getBackgroundColor(padId, activity) != colorDown) {
                    setPadColor(pad, colorUp, activity);
                }
            }
        };

        pad.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onTouch() {
                setPadColor(pad, colorDown, activity);
                if (isLoopEnabled[0] == false) {
                    buttonDelay.postDelayed(clearPad, 500);
                }
            }

            @Override
            public void onClick() {
                sp.play(spid[0], 1, 1, 1, 0, 1);
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                }
                Log.d("TouchListener", "Click");
            }

            @Override
            public void onDoubleClick() {
                sp.play(spid[0], 1, 1, 1, 0, 1);
                Handler backgroundChangeHandler = new Handler();
                backgroundChangeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLoopEnabled[0] == false) {
                            setPadColor(pad, colorUp, activity);
                            buttonDelay.postDelayed(clearPad, 500);
                        }
                    }
                }, 10);
                Log.d("TouchListener", "Double Click");
            }

            @Override
            public void onSingleClickConfirmed() {
                if (isLoopEnabled[0] == false) {
                    pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                }
                Log.d("TouchListener", "SingleClickConfirmed");
            }

            @Override
            public void onSwipeUp() {
                if (spid[1] != 0) {
                    sp.play(spid[1], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                }
                Log.d("TouchListener", "SwipeUp");
            }

            @Override
            public void onSwipeRight() {
                if (spid[2] != 0) {
                    sp.play(spid[2], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                }
                Log.d("TouchListener", "SwipeRight");
            }

            @Override
            public void onSwipeDown() {
                if (spid[3] != 0) {
                    sp.play(spid[3], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                }
                Log.d("TouchListener", "SwipeDown");
            }

            @Override
            public void onSwipeLeft() {
                if (spid[4] != 0) {
                    sp.play(spid[4], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                }
                Log.d("TouchListener", "SwipeLeft");
            }

            @Override
            public void onLongClick() {
                if (isLoopEnabled[0] == false) {
                    streamId[0] = sp.play(spid[0], 1, 1, 1, -1, 1);
                    // add item to arraylist to reset loop sounds on deck change
                    addLoopStreamId(streamId[0]);
                    isLoopEnabled[0] = true;
                    setPadColor(pad, colorDown, activity);
                    Log.d("TouchListener", "LongClick, loop on");
                } else {
                    sp.stop(streamId[0]);
                    removeLoopStreamId(streamId[0]);
                    isLoopEnabled[0] = false;
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                    Log.d("TouchListener", "LongClick, loop off");
                }
            }
        });
    }

    public void setOnGestureSound(final int padId,
                                  final int colorDown, final int colorUp,
                                  final SoundPool sp, final int spid[],
                                  final int patternPreset[][][], final Activity activity) {
        // Gesture Pattern
        final int btnId[] = {getButtonId(padId)};
        final boolean isLoopEnabled[] = {false};

        final View pad = activity.findViewById(padId);
        final int streamId[] = {0};

        final Handler buttonDelay = new Handler();
        final Runnable clearPad = new Runnable() {
            @Override
            public void run() {
                if (getBackgroundColor(padId, activity) != colorDown) {
                    setPadColor(pad, colorUp, activity);
                }
            }
        };

        pad.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onTouch() {
                setPadColor(pad, colorDown, activity);
                setButtonPattern(patternPreset, btnId[0], colorDown, colorUp, activity);
                if (isLoopEnabled[0] == false) {
                    buttonDelay.postDelayed(clearPad, 500);
                }
            }

            @Override
            public void onClick() {
                sp.play(spid[0], 1, 1, 1, 0, 1);
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                }
                setButtonPatternDefault(patternPreset, btnId[0], colorUp, activity);
                Log.d("TouchListener", "Click");
            }

            @Override
            public void onDoubleClick() {
                sp.play(spid[0], 1, 1, 1, 0, 1);
                Handler backgroundChangeHandler = new Handler();
                backgroundChangeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLoopEnabled[0] == false) {
                            setPadColor(pad, colorUp, activity);
                            buttonDelay.postDelayed(clearPad, 500);
                        }
                        setButtonPatternDefault(patternPreset, btnId[0], colorUp, activity);
                    }
                }, 10);
                Log.d("TouchListener", "Double Click");
            }

            @Override
            public void onSwipeUp() {
                if (spid[1] != 0) {
                    sp.play(spid[1], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                }
                setButtonPatternDefault(patternPreset, btnId[0], colorUp, activity);
                Log.d("TouchListener", "SwipeUp");
            }

            @Override
            public void onSwipeRight() {
                if (spid[2] != 0) {
                    sp.play(spid[2], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                }
                setButtonPatternDefault(patternPreset, btnId[0], colorUp, activity);
                Log.d("TouchListener", "SwipeRight");
            }

            @Override
            public void onSwipeDown() {
                if (spid[3] != 0) {
                    sp.play(spid[3], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                }
                setButtonPatternDefault(patternPreset, btnId[0], colorUp, activity);
                Log.d("TouchListener", "SwipeDown");
            }

            @Override
            public void onSwipeLeft() {
                if (spid[4] != 0) {
                    sp.play(spid[4], 1, 1, 1, 0, 1);
                } else {
                    sp.play(spid[0], 1, 1, 1, 0, 1);
                }
                if (isLoopEnabled[0] == false) {
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                }
                setButtonPatternDefault(patternPreset, btnId[0], colorUp, activity);
                Log.d("TouchListener", "SwipeLeft");
            }

            @Override
            public void onLongClick() {
                //pad.setBackgroundColor(activity.getResources().getColor(colorUp));
                buttonDelay.removeCallbacks(clearPad);
                if (isLoopEnabled[0] == false) {
                    streamId[0] = sp.play(spid[0], 1, 1, 1, -1, 1);
                    // add item to arraylist to reset loop sounds on deck change
                    addLoopStreamId(streamId[0]);
                    isLoopEnabled[0] = true;
                    setPadColor(pad, colorDown, activity);
                    Log.d("TouchListener", "LongClick, loop on");
                } else {
                    sp.stop(streamId[0]);
                    removeLoopStreamId(streamId[0]);
                    isLoopEnabled[0] = false;
                    setPadColor(pad, colorUp, activity);
                    buttonDelay.postDelayed(clearPad, 500);
                    Log.d("TouchListener", "LongClick, loop off");
                }
                setButtonPatternDefault(patternPreset, btnId[0], colorUp, activity);
            }
        });
    }

    // TODO NEXT UPDATE check algorithm
//    void setButtonPattern(int patternPreset[][], int btnId, int colorDown, int colorUp, int mode, Activity activity) {
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
//            for (int i = 0; i < patternPreset[btnId].length; i++) {
//                try {
//                    getView(patternPreset[btnId][i], activity).setBackgroundColor(activity.getResources().getColor(colorDown));
//                } catch (Resources.NotFoundException e) {
//                    Log.i("NotFoundException", "Handling with normal value");
//                    getView(patternPreset[btnId][i], activity).setBackgroundColor(colorDown);
//                }
//            }
//        } else if (mode == 0) {
//            // button clicked (up)
//            for (int i = 0; i < patternPreset[btnId].length; i++) {
//                if (wasAlreadyOn[btnId] == false) {
//                    try {
//                        getView(patternPreset[btnId][i], activity).setBackgroundColor(activity.getResources().getColor(colorUp));
//                    } catch (Resources.NotFoundException e) {
//                        Log.i("NotFoundException", "Handling with normal value");
//                        getView(patternPreset[btnId][i], activity).setBackgroundColor(colorUp);
//                    }
//                    wasAlreadyOn[btnId] = true;
//                }
//            }
//        } else {
//            Log.d("NotFoundException", "Wrong touch mode");
//        }
//    }

    private void setButtonPattern(int patternPreset[][][], int btnId, int colorDown, int colorUp, Activity activity) {
        if (btnId >= 0) {
            for (int i = 0; i < patternPreset[btnId].length; i++) {
                for (int j = 0; j < patternPreset[btnId][i].length; j++) {
                    try {
                        getView(patternPreset[btnId][i][j], activity).setBackgroundColor(
                                getBlendColor(
                                        activity.getResources().getColor(colorDown),
                                        activity.getResources().getColor(colorUp),
                                        (0.8f - (0.3f * i))));
                    } catch (Resources.NotFoundException e) {
                        Log.i("NotFoundException", "Handling with normal value");
                        getView(patternPreset[btnId][i][j], activity).setBackgroundColor(
                                getBlendColor(
                                        colorDown,
                                        activity.getResources().getColor(colorUp),
                                        (0.8f - (0.3f * i))
                                )
                        );
                    }
                }
            }
        }
    }

    private void setButtonPatternDefault(int patternPreset[][][], int btnId, int colorUp, Activity activity) {
        if (btnId >= 0) {
            for (int i = 0; i < patternPreset[btnId].length; i++) {
                for (int j = 0; j < patternPreset[btnId][i].length; j++) {
                    try {
                        getView(patternPreset[btnId][i][j], activity).setBackgroundColor(activity.getResources().getColor(colorUp));
                    } catch (Resources.NotFoundException e) {
                        Log.i("NotFoundException", "Handling with normal value");
                        getView(patternPreset[btnId][i][j], activity).setBackgroundColor(colorUp);
                    }
                }
            }
        }
    }

    public int getBlendColor(int color0, int color1, float blendPercent) {
        String colorString0 = String.format("#%06X", (0xFFFFFF & color0));
        //Log.d("color0", colorString0);
        String colorString1 = String.format("#%06X", (0xFFFFFF & color1));
        //Log.d("color1", colorString1);

        int r0 = Integer.parseInt(colorString0.substring(1, 3), 16);
        int g0 = Integer.parseInt(colorString0.substring(3, 5), 16);
        int b0 = Integer.parseInt(colorString0.substring(5, 7), 16);

        int r1 = Integer.parseInt(colorString1.substring(1, 3), 16);
        int g1 = Integer.parseInt(colorString1.substring(3, 5), 16);
        int b1 = Integer.parseInt(colorString1.substring(5, 7), 16);

        String blendColorHex = "#" +
                        getTwoDigitHexString(averageIntegerWithPercent(r0, r1, blendPercent)) +
                        getTwoDigitHexString(averageIntegerWithPercent(g0, g1, blendPercent)) +
                        getTwoDigitHexString(averageIntegerWithPercent(b0, b1, blendPercent));

        return Color.parseColor(blendColorHex);
    }

    private int averageIntegerWithPercent(int num1, int num2, float percent) {
        return Math.round((num1 - num2) * percent) + num2;
    }

    private String getTwoDigitHexString(int hexValue) {
        String hexString = Integer.toHexString(hexValue);
        //Log.d("hexStringCheck", hexString);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    private void setSoundPoolDelay(final SoundPool sp, final int soundId[], final int count, final int delay) {
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("SoundPoolDelay", "SoundPool played on " + soundId[count] + " with " + delay + "ms delay");
                sp.play(soundId[count], 1, 1, 1, 0, 1f);
            }
        }, delay);
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
