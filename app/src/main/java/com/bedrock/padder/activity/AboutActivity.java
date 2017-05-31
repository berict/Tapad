package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.bedrock.padder.R;
import com.bedrock.padder.adapter.DetailAdapter;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.ThemeService;
import com.bedrock.padder.helper.ToolbarService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.about.About;
import com.google.gson.Gson;

import static com.bedrock.padder.activity.MainActivity.currentPreset;

public class AboutActivity extends AppCompatActivity {

    Activity activity = this;
    About about;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private WindowService window = new WindowService();
    private AnimService anim = new AnimService();
    private ThemeService theme = new ThemeService();
    private ToolbarService toolbar = new ToolbarService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Gson gson = new Gson();

        Intent intent = getIntent();
        String currentAbout = intent.getStringExtra("about");

        switch (currentAbout) {
            case "now_playing":
                about = currentPreset.getAbout();
                break;
            case "tapad":
                about = gson.fromJson(getResources().getString(R.string.json_about_tapad), About.class);
                break;
            case "dev":
                about = gson.fromJson(getResources().getString(R.string.json_about_dev  ), About.class);
                break;
            default:
                Log.e("Error", "currentAbout is not specified");
                break;
        }

        toolbar.setActionBar(this);
        toolbar.setActionBarDisplayHomeAsUp(true);
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

        enterAnim();
        setUi();
    }

    private void setUi() {
        // status bar
        window.getView(R.id.statusbar, activity).setBackgroundColor(about.getActionbarColor());

        // action bar
        collapsingToolbarLayout.setContentScrimColor(about.getActionbarColor());
        collapsingToolbarLayout.setStatusBarScrimColor(about.getActionbarColor());
        collapsingToolbarLayout.setTitle(about.getTitle());

        // set taskDesc
        window.setRecentColor(about.getTitle(), about.getActionbarColor(), activity);

        // title image / text
        window.getImageView(R.id.layout_image, activity).setImageResource(window.getDrawableId(about.getImage()));

        // bio
        window.getTextView(R.id.layout_bio_title, activity).setText(about.getBio().getTitle());
        window.getTextView(R.id.layout_bio_title, activity).setTextColor(about.getActionbarColor());
        if(about.getBio().getImage() != null) {
            window.getImageView(R.id.layout_bio_image, activity).setImageResource(window.getDrawableId(about.getBio().getImage()));
        } else {
            // no bio image exception
            window.getImageView(R.id.layout_bio_image, activity).setVisibility(View.GONE);
            window.getView(R.id.layout_bio_image_divider, activity).setVisibility(View.GONE);
        }
        window.getTextView(R.id.layout_bio_name, activity).setText(about.getBio().getName());
        window.getTextView(R.id.layout_bio_text, activity).setText(about.getBio().getText());
        window.getTextView(R.id.layout_bio_source, activity).setText(about.getBio().getSource());
        if (about.getPresetCreator() != null) {
            window.getTextView(R.id.layout_bio_preset_creator, activity).setText(window.getStringFromId("about_bio_preset_by", activity) + " " + about.getPresetCreator());
        } else {
            window.setGone(R.id.layout_bio_preset_creator, 0, activity);
            window.setGone(R.id.layout_bio_text_divider, 0, activity);
        }

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
                AboutActivity.super.onBackPressed();
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
            window.setRecentColor(about.getTitle(), about.getActionbarColor(), activity);
        }
    }

    private void enterAnim() {
        anim.fadeIn(R.id.layout_text, 400, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_detail_bio, 500, 200, "bioIn", activity);
        anim.fadeIn(R.id.layout_detail_recyclerview, 600, 200, "aboutIn", activity);
    }

    private void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }
}
