package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.ThemeService;
import com.bedrock.padder.helper.ToolbarService;
import com.bedrock.padder.helper.WindowService;

public class PresetStoreActivity extends AppCompatActivity {

    Activity activity = this;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    SharedPreferences prefs;

    private WindowService window = new WindowService();
    private AnimService anim = new AnimService();
    private ThemeService theme = new ThemeService();
    private ToolbarService toolbar = new ToolbarService();

    private int themeColor;
    private String themeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_store);

        toolbar.setActionBar(this);
        toolbar.setActionBarDisplayHomeAsUp(true, this);
        toolbar.setStatusBarTint(this);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        window.setNavigationBar(R.color.transparent, activity);

        View statusbar = findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT < 21) {
            statusbar.setVisibility(View.GONE);
        } else {
            try {
                statusbar.getLayoutParams().height = window.getStatusBarFromPrefs(activity);
            } catch (NullPointerException e) {
                Log.d("NullExp", e.getMessage());
                statusbar.setVisibility(View.GONE);
            }
        }

        window.setMarginRelativePX(R.id.layout_relative, 0, window.getStatusBarFromPrefs(activity), 0, 0, activity);
        window.getView(R.id.layout_margin, activity).getLayoutParams().height = window.getNavigationBarFromPrefs(activity) + window.convertDPtoPX(10, activity);

        themeColor = activity.getResources().getColor(R.color.amber);
        themeTitle = activity.getResources().getString(R.string.preset_store);

        enterAnim();
        setUi();
    }

    private void setUi() {
        // status bar
        window.getView(R.id.statusbar, activity).setBackgroundColor(themeColor);

        // action bar
        collapsingToolbarLayout.setContentScrimColor(themeColor);
        collapsingToolbarLayout.setStatusBarScrimColor(themeColor);
        collapsingToolbarLayout.setTitle(themeTitle);

        // set taskDesc
        window.setRecentColor(themeTitle, themeColor, activity);

        // title image / text
        window.getImageView(R.id.layout_image, activity).setImageResource(R.drawable.about_image_preset_store);
    }

    @Override
    public void onBackPressed() {
        anim.fadeOut(R.id.layout_text, 0, 200, activity);
        anim.fadeOut(R.id.layout_detail, 0, 200, activity);
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                PresetStoreActivity.super.onBackPressed();
            }
        }, 200);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        pressBack();
        return true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            theme.setGone(R.id.layout_placeholder, 0, activity);
            // reset taskDesc
            window.setRecentColor(themeTitle, themeColor, activity);
        }
    }

    void enterAnim() {
        anim.fadeIn(R.id.layout_text, 400, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_detail_recyclerview, 500, 200, "aboutIn", activity);
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }
}
