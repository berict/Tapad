package com.bedrock.padder.service;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bedrock.padder.R;

public class AppbarService {
    public static int LAYOUT        = R.id.actionbar_layout;
    public static int STATUS        = R.id.statusbar;
    public static int NAV_LAYOUT    = R.id.actionbar_nav_layout;
    public static int NAV           = R.id.actionbar_nav;
    public static int NAV_BACK      = R.id.actionbar_nav_icon_back;
    public static int NAV_NAV       = R.id.actionbar_nav_icon_nav;
    public static int NAV_CLOSE     = R.id.actionbar_nav_icon_close;
    public static int IMAGE         = R.id.actionbar_image;
    public static int TITLE         = R.id.actionbar_title;
    public static int ACTION        = R.id.actionbar_action_layout;
    public static int MENU          = R.id.actionbar_menu;
    public static int MENU_ICON     = R.id.actionbar_menu_icon;
    public static int ACTION_1      = R.id.actionbar_action_1;
    public static int ACTION_1_ICON = R.id.actionbar_action_1_icon;
    public static int ACTION_2      = R.id.actionbar_action_2;
    public static int ACTION_2_ICON = R.id.actionbar_action_2_icon;
    public static int ACTION_TEXT   = R.id.actionbar_action_text;

    WindowService w = new WindowService();
    ThemeService t = new ThemeService();

    public void setTitle(int resid, Activity activity) {
        w.getTextView(TITLE, activity).setText(resid);
        w.getView(TITLE, activity).setVisibility(View.VISIBLE);
        w.getView(IMAGE, activity).setVisibility(View.GONE);
        Log.d("Appbar", "Title set");
    }

    public void setTitleColor(int resid, Activity activity) {
        w.getTextView(TITLE, activity).setTextColor(resid);
        Log.d("Appbar", "Title color set");
    }

    public void setColor(int resid, Activity activity) {
        w.setViewBackgroundColor(LAYOUT, resid, activity);
        w.setViewBackgroundColor(STATUS, resid, activity);
        Log.d("Appbar", "Color set");
    }

    public void setStatusTheme(int mode, Activity activity) {
        /* 0: dark, 1: light */
        switch (mode) {
            case 0:
                // DARK
                w.setViewBackgroundColor(STATUS, R.color.dark_status_bar, activity);
                break;
            case 1:
                // LIGHT
                w.setViewBackgroundColor(STATUS, R.color.light_status_bar, activity);
                break;
            default:
                // DARK
                w.setViewBackgroundColor(STATUS, R.color.dark_status_bar, activity);
                break;
        }
        Log.d("Appbar", "Status theme set");
    }

    public void setNav(int mode, final Runnable onClick, final Activity activity) {
        /* 0: gone, 1: back, 2: nav, 3: close */
        switch (mode) {
            case 0: w.getView(NAV, activity).setVisibility(View.GONE); break;
            case 1:
                // BACK
                w.getView(NAV, activity).setVisibility(View.VISIBLE);
                w.getView(NAV_BACK, activity).setVisibility(View.VISIBLE);
                w.getView(NAV_NAV, activity).setVisibility(View.GONE);
                w.getView(NAV_CLOSE, activity).setVisibility(View.GONE);
                w.getView(NAV_BACK, activity).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
                        activity.dispatchKeyEvent(kDown);
                        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
                        activity.dispatchKeyEvent(kUp);
                    }
                });
                break;
            case 2:
                // NAV
                w.getView(NAV, activity).setVisibility(View.VISIBLE);
                w.getView(NAV_NAV, activity).setVisibility(View.VISIBLE);
                w.getView(NAV_BACK, activity).setVisibility(View.GONE);
                w.getView(NAV_CLOSE, activity).setVisibility(View.GONE);
                w.getView(NAV_NAV, activity).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onClick != null) {
                            onClick.run();
                        }
                    }
                });
                break;
            case 3:
                // CLOSE
                w.getView(NAV, activity).setVisibility(View.VISIBLE);
                w.getView(NAV_NAV, activity).setVisibility(View.GONE);
                w.getView(NAV_BACK, activity).setVisibility(View.GONE);
                w.getView(NAV_CLOSE, activity).setVisibility(View.VISIBLE);
                w.getView(NAV_NAV, activity).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
                        activity.dispatchKeyEvent(kDown);
                        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
                        activity.dispatchKeyEvent(kUp);
                    }
                });
                break;
            default:
                w.getView(NAV, activity).setVisibility(View.GONE);
                Log.e("Appbar", "Wrong mode value, set to no nav button."); break;
        }
        Log.d("Appbar", "Nav set");
    }

    public void setNavCustom(int resid, final Runnable onClick, Activity activity) {
        w.getView(NAV, activity).setVisibility(View.VISIBLE);
        w.getView(NAV_BACK, activity).setVisibility(View.VISIBLE);
        w.getView(NAV_NAV, activity).setVisibility(View.GONE);
        w.getImageView(NAV_BACK, activity).setImageResource(resid);

        w.getView(NAV, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.run();
            }
        });
    }

    public void setImage(int resid, Activity activity) {
        w.getView(TITLE, activity).setVisibility(View.GONE);
        w.getView(ACTION_1, activity).setVisibility(View.GONE);
        w.getView(ACTION_2, activity).setVisibility(View.GONE);
        w.getView(IMAGE, activity).setVisibility(View.VISIBLE);
        w.getImageView(IMAGE, activity).setImageResource(resid);

        Log.d("Appbar", "Image set");
    }

    public void setStatusHeight(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("com.bedrock.padder", activity.MODE_PRIVATE);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) w.getRelativeLayout(STATUS, activity).getLayoutParams();
        params.height = prefs.getInt("statBarPX", 0);
        w.getRelativeLayout(STATUS, activity).setLayoutParams(params);
        Log.d("Appbar", "Status bar height set");
    }

    public void setStatusHeightCustom(int heightPX, Activity activity){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) w.getRelativeLayout(STATUS, activity).getLayoutParams();
        params.height = heightPX;
        w.getRelativeLayout(STATUS, activity).setLayoutParams(params);
        Log.d("Appbar", "Status bar height set in " + String.valueOf(heightPX) + "px");
    }

    public void setAction(int resid, final Runnable onClick, Activity activity){
        w.getView(ACTION, activity).setVisibility(View.VISIBLE);
        w.getView(ACTION_1, activity).setVisibility(View.VISIBLE);
        w.getView(ACTION_2, activity).setVisibility(View.GONE);
        w.getView(MENU, activity).setVisibility(View.GONE);
        w.getView(ACTION_TEXT, activity).setVisibility(View.GONE);

        w.getImageView(ACTION_1, activity).setImageResource(resid);
        w.getView(ACTION_1, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.run();
            }
        });
    }

    public void setAction(int resid1, int resid2, final Runnable onClick1, final Runnable onClick2, Activity activity){
        w.getView(ACTION, activity).setVisibility(View.VISIBLE);
        w.getView(ACTION_1, activity).setVisibility(View.VISIBLE);
        w.getView(ACTION_2, activity).setVisibility(View.VISIBLE);
        w.getView(MENU, activity).setVisibility(View.GONE);
        w.getView(ACTION_TEXT, activity).setVisibility(View.GONE);

        w.getImageView(ACTION_1, activity).setImageResource(resid1);
        w.getImageView(ACTION_2, activity).setImageResource(resid2);

        w.getView(ACTION_1, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick1.run();
            }
        });
        w.getView(ACTION_2, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick2.run();
            }
        });
    }

    public void setMenu(final Runnable onClick, Activity activity){
        w.getView(ACTION, activity).setVisibility(View.VISIBLE);
        w.getView(MENU, activity).setVisibility(View.VISIBLE);
        w.getView(ACTION_TEXT, activity).setVisibility(View.GONE);

        w.getView(MENU, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.run();
            }
        });
    }

    public void setActionText(int resid, final Runnable onClick, Activity activity){
        w.getView(ACTION, activity).setVisibility(View.VISIBLE);
        w.getView(ACTION_1, activity).setVisibility(View.GONE);
        w.getView(ACTION_2, activity).setVisibility(View.GONE);
        w.getView(MENU, activity).setVisibility(View.GONE);
        w.getView(ACTION_TEXT, activity).setVisibility(View.VISIBLE);

        w.getTextView(ACTION_TEXT, activity).setText(resid);
        w.getView(ACTION_TEXT, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.run();
            }
        });
    }
}
