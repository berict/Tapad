package com.bedrock.padder.helper;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bedrock.padder.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ToolbarHelper {

    public static int TOOLBAR = R.id.actionbar_toolbar;
    public static int IMAGE = R.id.actionbar_image;
    
    private ActionBar actionBar;

    public void setActionBar(AppCompatActivity appCompatActivity) {
        appCompatActivity.setSupportActionBar((Toolbar)appCompatActivity.findViewById(TOOLBAR));
        actionBar = appCompatActivity.getSupportActionBar();
    }

    public void setActionBar(AppCompatActivity appCompatActivity, View view) {
        appCompatActivity.setSupportActionBar((Toolbar)view.findViewById(TOOLBAR));
        actionBar = appCompatActivity.getSupportActionBar();
    }

    public void setActionBarTitle(int titleResId) {
        if (titleResId == 0) {
            // no title, remove the title
            actionBar.setDisplayShowTitleEnabled(false);
        } else {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(titleResId);
        }
    }

    public void setActionBarTitle(String title) {
        if (title == null) {
            // no title, remove the title
            actionBar.setDisplayShowTitleEnabled(false);
        } else {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(title);
        }
    }

    public void setActionBarColor(int color, Activity activity) {
        try {
            try {
                actionBar.setBackgroundDrawable(
                        new ColorDrawable(ContextCompat.getColor(activity, color))
                );
            } catch (Exception e) {
                actionBar.setBackgroundDrawable(
                        new ColorDrawable(color)
                );
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("setActionBar", "Toolbar is not initialized");
        }
    }

    public void setActionBarImage(int imageResId, Activity activity) {
        WindowHelper window = new WindowHelper();
        if (imageResId != 0) {
            ((ImageView) activity.findViewById(IMAGE)).setImageResource(imageResId);
            ((ImageView) activity.findViewById(IMAGE)).setVisibility(View.VISIBLE);
        } else {
            ((ImageView) activity.findViewById(IMAGE)).setVisibility(View.GONE);
        }
    }

    public void setActionBarImage(String imageLocation, Activity activity) {
        WindowHelper window = new WindowHelper();
        ImageView imageView = ((ImageView) activity.findViewById(IMAGE));
        if (imageLocation != null) {
            Picasso.with(activity).load(new File(imageLocation)).into(imageView);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public void setActionBarDisplayHomeAsUp(boolean displayHomeAsUp) {
        actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUp);
    }

    public void setActionBarDisplayHomeAsUpIcon(int iconId) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(iconId);
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    public void setActionBarPadding(Activity activity) {
        WindowHelper window = new WindowHelper();
        // set the top padding to the status bar size
        Toolbar toolbar = (Toolbar)activity.findViewById(TOOLBAR);
        toolbar.setPadding(0, window.getStatusBarFromPrefs(activity), 0, 0);
    }

    public void setActionBarPadding(Activity activity, View view) {
        WindowHelper window = new WindowHelper();
        // set the top padding to the status bar size
        Toolbar toolbar = (Toolbar)view.findViewById(TOOLBAR);
        toolbar.setPadding(0, window.getStatusBarFromPrefs(activity), 0, 0);
    }

    public void setStatusBarTint(Activity activity) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set the transparent color of the status bar, 20% darker
        tintManager.setTintColor(activity.getResources().getColor(R.color.dark_status_bar));
    }
}
