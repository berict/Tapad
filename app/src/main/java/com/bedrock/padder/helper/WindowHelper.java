package com.bedrock.padder.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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

        if (statBarHeight[0] <= 0) {
            // error with above method
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Rect rectangle = new Rect();
                    Window window = activity.getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                    int statusBar = rectangle.top;
                    Log.i("Status Bar Height", String.valueOf(statusBar));
                    statBarHeight[0] = statusBar;

                    View view = activity.findViewById(id);
                    Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);

                    Log.i("Status Bar Height", String.valueOf(rect.top));
                    statBarHeight[0] = rect.top;
                }
            }, 10);
        }

        prefs.edit().putInt("statBarPX", statBarHeight[0]).apply();
        Log.i("SharedPrefs", "statBarPX = " + String.valueOf(prefs.getInt("statBarPX", 72)));

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

    public void setVisible(final int viewId, final int delay, final Activity activity) {
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

    public void setInvisible(final int viewId, final int delay, final Activity activity) {
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

    public void setGone(final int viewId, final int delay, final Activity activity) {
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

    public void setViewBackgroundDrawable(int viewId, int drawableId, Activity activity) {
        View view = activity.findViewById(viewId);

        Drawable drawable;

        if (Build.VERSION.SDK_INT >= 21) {
            drawable = activity.getResources().getDrawable(drawableId, activity.getTheme());
        } else {
            drawable = activity.getResources().getDrawable(drawableId);
        }

        if (Build.VERSION.SDK_INT >= 16) {
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

    public SeekBar getSeekBar(int id, View view) {
        return (SeekBar) view.findViewById(id);
    }

    public EditText getEditText(int id, View view) {
        return (EditText) view.findViewById(id);
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

    public RecyclerView getRecyclerView(int id, View view) {
        return (RecyclerView) view.findViewById(id);
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
            Log.e("getStringId", "Failure to get string from id [" + id + "]");
            return -1;
        }
        //from : https://daniel-codes.blogspot.com/2009/12/dynamically-retrieving-resources-in.html
    }

    public String getStringFromId(String id, Context context) {
        try {
            Class res = R.string.class;
            Field field = res.getField(id);
            return context.getResources().getString(field.getInt(null));
        } catch (Exception e) {
            Log.e("getStringFromId", "Failure to get string from id [" + id + "]");
            return null;
        }
        //from : https://daniel-codes.blogspot.com/2009/12/dynamically-retrieving-resources-in.html
    }

    public String getStringFromId(int resId, Context context) {
        return context.getResources().getString(resId);
    }

    // public Z getZ(int id, Activity activity) {
    //     Z z = (Z) activity.findViewById(id);
    //     return z;
    // }

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
        }

        Log.i("getDeviceRam", "Device Ram requested = returned " + String.valueOf(totRamMb));

        return (int) totRamMb;
    }
}
