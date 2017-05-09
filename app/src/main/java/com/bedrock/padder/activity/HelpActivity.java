package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.ThemeService;
import com.bedrock.padder.helper.ToolbarService;
import com.bedrock.padder.helper.WindowService;

public class HelpActivity extends AppCompatActivity {

    private Activity activity = this;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private SharedPreferences prefs;

    private WindowService window = new WindowService();
    private AnimService anim = new AnimService();
    private ThemeService theme = new ThemeService();
    private ToolbarService toolbar = new ToolbarService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        toolbar.setActionBar(this);
        toolbar.setActionBarColor(R.color.colorAccent, this);
        toolbar.setActionBarDisplayHomeAsUp(true, this);
        toolbar.setActionBarTitle(R.string.help_title, this);
        toolbar.setActionBarPadding(this);
        toolbar.setStatusBarTint(this);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        window.setNavigationBar(R.color.transparent, activity);

        window.setMarginRelativePX(R.id.layout_relative, 0, window.getStatusBarFromPrefs(activity), 0, 0, activity);
        window.getView(R.id.layout_margin, activity).getLayoutParams().height = window.getNavigationBarFromPrefs(activity) + window.convertDPtoPX(10, activity);

        enterAnim();
        setUi();
    }

    private void setUi() {
        // action bar
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorAccent));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorAccent));
        collapsingToolbarLayout.setTitle(window.getStringFromId("help_title", activity));

        // set taskDesc
        window.setRecentColor(window.getStringFromId("help_title", activity), R.color.colorAccent, activity);
    }

    @Override
    public void onBackPressed() {
        anim.fadeOut(R.id.layout_text, 0, 200, activity);
        anim.fadeOut(R.id.layout_detail, 0, 200, activity);
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                HelpActivity.super.onBackPressed();
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
            window.setRecentColor(window.getStringFromId("help_title", activity), R.color.colorAccent, activity);
        }
    }

    void enterAnim() {
        anim.fadeIn(R.id.layout_text, 400, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_help_layout, 500, 200, "helpIn", activity);
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }
}
