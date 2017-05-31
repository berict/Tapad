package com.bedrock.padder.helper;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bedrock.padder.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ToolbarService {

    public static int TOOLBAR = R.id.actionbar_toolbar;
    public static int IMAGE = R.id.actionbar_image;

    public void setActionBar(AppCompatActivity appCompatActivity) {
        appCompatActivity.setSupportActionBar((Toolbar)appCompatActivity.findViewById(TOOLBAR));
    }

    public void setActionBar(AppCompatActivity appCompatActivity, View view) {
        appCompatActivity.setSupportActionBar((Toolbar)view.findViewById(TOOLBAR));
    }

    public void setActionBarTitle(int titleResId, AppCompatActivity appCompatActivity) {
        if (titleResId == 0) {
            // no title, remove the title
            appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);
            appCompatActivity.getSupportActionBar().setTitle(titleResId);
        }
    }

    public void setActionBarColor(int color, AppCompatActivity appCompatActivity) {
        try {
            try {
                appCompatActivity.getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable(ContextCompat.getColor(appCompatActivity, color))
                );
            } catch (Exception e) {
                appCompatActivity.getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable(color)
                );
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("setActionBar", "Toolbar is not initialized");
        }
    }

    public void setActionBarImage(int imageResId, AppCompatActivity appCompatActivity) {
        WindowService window = new WindowService();
        window.getImageView(IMAGE, appCompatActivity).setImageResource(imageResId);
        window.getImageView(IMAGE, appCompatActivity).setVisibility(View.VISIBLE);
    }

    public void setActionBarDisplayHomeAsUp(boolean displayHomeAsUp, AppCompatActivity appCompatActivity) {
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUp);
    }

    public void setActionBarDisplayHomeAsUpIcon(int iconId, AppCompatActivity appCompatActivity) {
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(iconId);
    }

    public ActionBar getActionBar(AppCompatActivity appCompatActivity) {
        return appCompatActivity.getSupportActionBar();
    }

    public void setActionBarPadding(AppCompatActivity appCompatActivity) {
        WindowService window = new WindowService();
        // set the top padding to the status bar size
        Toolbar toolbar = (Toolbar)appCompatActivity.findViewById(TOOLBAR);
        toolbar.setPadding(0, window.getStatusBarFromPrefs(appCompatActivity), 0, 0);
    }

    public void setActionBarPadding(AppCompatActivity appCompatActivity, View view) {
        WindowService window = new WindowService();
        // set the top padding to the status bar size
        Toolbar toolbar = (Toolbar)view.findViewById(TOOLBAR);
        toolbar.setPadding(0, window.getStatusBarFromPrefs(appCompatActivity), 0, 0);
        Log.d("TAG", String.valueOf(window.getStatusBarFromPrefs(appCompatActivity)));
    }

    public void setStatusBarTint(AppCompatActivity appCompatActivity) {
        SystemBarTintManager tintManager = new SystemBarTintManager(appCompatActivity);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set the transparent color of the status bar, 20% darker
        tintManager.setTintColor(appCompatActivity.getResources().getColor(R.color.dark_status_bar));
    }
}
