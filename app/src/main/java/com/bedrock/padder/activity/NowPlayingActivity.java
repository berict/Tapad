package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.bedrock.padder.R;
import com.bedrock.padder.adapter.DetailAdapter;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.ThemeService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.about.About;

import static com.bedrock.padder.helper.SoundService.currentPreset;

public class NowPlayingActivity extends AppCompatActivity {

    Activity activity = this;
    About about;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    SharedPreferences prefs;

    WindowService window = new WindowService();
    AnimService anim = new AnimService();
    ThemeService theme = new ThemeService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        about = currentPreset.getAbout();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        prefs = getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        window.setNavigationBar(R.color.transparent, activity);

        View statusbar = findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT < 21) {
            statusbar.setVisibility(View.GONE);
        } else {
            try {
                statusbar.getLayoutParams().height = prefs.getInt("statBarPX", 0);
            } catch (NullPointerException e) {
                Log.d("NullExp", e.getMessage());
                statusbar.setVisibility(View.GONE);
            }
        }

        window.setMarginRelativePX(R.id.layout_relative, 0, prefs.getInt("statBarPX", 0), 0, 0, activity);
        window.getView(R.id.layout_margin, activity).getLayoutParams().height = prefs.getInt("navBarPX", 0) + window.convertDPtoPX(10, activity);

        setUi();
        enterAnim();
    }

    private void setUi() {
        // status bar
        window.getView(R.id.statusbar, activity).setBackgroundColor(getResources().getColor(window.getColorId(about.getStatusbarColorId())));

        // action bar
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(window.getColorId(about.getActionbarColorId())));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(window.getColorId(about.getActionbarColorId())));
        collapsingToolbarLayout.setTitle(about.getTitle(activity));

        // set taskDesc
        window.setRecentColor(about.getTitle(activity), window.getColorId(about.getActionbarColorId()), activity);

        // title image / text
        window.getImageView(R.id.layout_image, activity).setImageResource(window.getDrawableId(about.getImageId()));
        //window.getTextView(R.id.layout_text, activity).setText(about.getTitle());

        // bio
        window.getTextView(R.id.layout_bio_title, activity).setText(about.getBio().getTitle(activity));
        window.getTextView(R.id.layout_bio_title, activity).setTextColor(getResources().getColor(window.getColorId(about.getActionbarColorId())));
        if(about.getBio().getImageId() == null) {
            // no bio image exception
            window.getImageView(R.id.layout_bio_image, activity).setVisibility(View.GONE);
            window.getView(R.id.layout_bio_image_divider, activity).setVisibility(View.GONE);
        } else {
            window.getImageView(R.id.layout_bio_image, activity).setImageResource(window.getDrawableId(about.getBio().getImageId()));
        }
        window.getTextView(R.id.layout_bio_name, activity).setText(about.getBio().getName(activity));
        window.getTextView(R.id.layout_bio_text, activity).setText(about.getBio().getText(activity));
        window.getTextView(R.id.layout_bio_source, activity).setText(about.getBio().getSource(activity));

        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        window.getRecyclerView(R.id.layout_detail_recyclerview, activity).setLayoutManager(layoutManager);
        window.getRecyclerView(R.id.layout_detail_recyclerview, activity).setNestedScrollingEnabled(false);
        window.getRecyclerView(R.id.layout_detail_recyclerview, activity).setAdapter(new DetailAdapter(about, R.layout.adapter_details, getApplicationContext(), activity));
    }

    @Override
    public void onBackPressed() {
        anim.fadeOut(R.id.layout_text, 0, 200, activity);
        anim.fadeOut(R.id.layout_detail, 0, 200, activity);
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                NowPlayingActivity.super.onBackPressed();
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
            window.setRecentColor(about.getTitle(activity), window.getColorId(about.getActionbarColorId()), activity);
        }
    }

    void enterAnim() {
        anim.fadeIn(R.id.layout_text, 400, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_detail_bio, 500, 200, "bioIn", activity);
        anim.fadeIn(R.id.layout_detail_recyclerview, 600, 200, "aboutIn", activity);
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }
}
