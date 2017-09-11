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
import android.widget.ImageView;

import com.bedrock.padder.R;
import com.bedrock.padder.adapter.DetailAdapter;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.PresetStoreHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.about.About;
import com.bedrock.padder.model.preset.Preset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class AboutActivity extends AppCompatActivity {

    Activity activity = this;
    About about;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();
    private PresetStoreHelper firebase = new PresetStoreHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Intent intent = getIntent();
        String currentAbout = intent.getStringExtra("about");

        Log.d("AA", currentAbout);
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
                String presetJson = firebase.getPresetJson(currentAbout);
                if (presetJson != null) {
                    about = gson.fromJson(firebase.getPresetJson(currentAbout), Preset.class).getAbout();
                } else {
                    Log.d("About", "currentAbout wasn't defined");
                }
                break;
        }

        toolbar.setActionBar(this);
        toolbar.setActionBarDisplayHomeAsUp(true);
        toolbar.setStatusBarTint(this);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedAppBar);

        window.setNavigationBar(R.color.transparent, activity);

        View statusBar = findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT < 21) {
            statusBar.setVisibility(View.GONE);
        } else {
            try {
                statusBar.getLayoutParams().height = window.getStatusBarFromPrefs(activity);
            } catch (NullPointerException e) {
                Log.d("NullExp", e.getMessage());
                statusBar.setVisibility(View.GONE);
            }
        }

        window.setMarginRelativePX(R.id.layout_relative, 0, window.getStatusBarFromPrefs(activity), 0, 0, activity);
        window.getView(R.id.layout_margin, activity).getLayoutParams().height = window.getNavigationBarFromPrefs(activity) + window.convertDPtoPX(10, activity);

        enterAnim();
        setUi();
    }

    private void setUi() {
        // status bar
        window.getView(R.id.status_bar, activity).setBackgroundColor(about.getColor());

        // action bar
        collapsingToolbarLayout.setContentScrimColor(about.getColor());
        collapsingToolbarLayout.setStatusBarScrimColor(about.getColor());
        collapsingToolbarLayout.setTitle(about.getTitle());

        // set taskDesc
        window.setRecentColor(about.getTitle(), about.getColor(), activity);

        // title image / text
        if (about.getImage(currentPreset).endsWith("album_art")) {
            // storage
            Picasso.with(activity)
                    .load("file:" + about.getImage(currentPreset))
                    .placeholder(R.drawable.ic_image_album_placeholder)
                    .error(R.drawable.ic_image_album_error)
                    .into(window.getImageView(R.id.layout_image, activity));
        } else {
            // res
            window.getImageView(R.id.layout_image, activity).setImageResource(window.getDrawableId(about.getImage(currentPreset)));
        }

        // bio
        window.getTextView(R.id.layout_bio_title, activity).setText(about.getBio().getTitle());
        window.getTextView(R.id.layout_bio_title, activity).setTextColor(about.getColor());
        if (about.getBio().getImage(currentPreset) != null) {
            ImageView imageView = window.getImageView(R.id.layout_bio_image, activity);
            if (about.getBio().getImage(currentPreset).endsWith("artist_image")) {
                // storage
                Picasso.with(activity)
                        .load("file:" + about.getBio().getImage(currentPreset))
                        .placeholder(R.drawable.ic_image_album_placeholder)
                        .error(R.drawable.ic_image_album_error)
                        .into(imageView);
            } else {
                // res
                imageView.setImageResource(window.getDrawableId(about.getBio().getImage(currentPreset)));
            }
        } else {
            // no bio image exception
            window.getImageView(R.id.layout_bio_image, activity).setVisibility(View.GONE);
            window.getView(R.id.layout_bio_image_divider, activity).setVisibility(View.GONE);
        }
        window.getTextView(R.id.layout_bio_name, activity).setText(about.getBio().getName());
        window.getTextView(R.id.layout_bio_text, activity).setText(about.getBio().getText());
        window.getTextView(R.id.layout_bio_source, activity).setText(about.getBio().getSource());
        if (about.getPresetArtist() != null) {
            window.getTextView(R.id.layout_bio_preset_creator, activity).setText(getStringFromId("about_bio_preset_by", activity) + " " + about.getPresetArtist());
        } else {
            window.setGone(R.id.layout_bio_preset_creator, 0, activity);
            window.setGone(R.id.layout_bio_text_divider, 0, activity);
        }

        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        window.getRecyclerView(R.id.layout_detail_recycler_view, activity).setLayoutManager(layoutManager);
        window.getRecyclerView(R.id.layout_detail_recycler_view, activity).setNestedScrollingEnabled(false);
        window.getRecyclerView(R.id.layout_detail_recycler_view, activity).setAdapter(new DetailAdapter(about, R.layout.adapter_details, getApplicationContext(), activity));
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
            window.setGone(R.id.layout_placeholder, 0, activity);
            // reset taskDesc
            window.setRecentColor(about.getTitle(), about.getColor(), activity);
        }
    }

    private void enterAnim() {
        anim.fadeIn(R.id.layout_text, 400, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_detail_bio, 500, 200, "bioIn", activity);
        anim.fadeIn(R.id.layout_detail_recycler_view, 600, 200, "aboutIn", activity);
    }

    private void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }
}
