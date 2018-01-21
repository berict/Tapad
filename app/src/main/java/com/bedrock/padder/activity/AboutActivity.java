package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bedrock.padder.R;
import com.bedrock.padder.adapter.DetailAdapter;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.about.About;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.helper.ApiHelper.logJson;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class AboutActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    private String currentAbout;
    private Activity activity = this;
    private About about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Intent intent = getIntent();
        currentAbout = intent.getStringExtra("about");

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
                Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        logJson(about);

        toolbar.setActionBar(this);
        toolbar.setActionBarDisplayHomeAsUp(true);
        toolbar.setStatusBarTint(this);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
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
        activity.findViewById(R.id.layout_margin).getLayoutParams().height = window.getNavigationBarFromPrefs(activity) + window.convertDPtoPX(10, activity);

        enterAnim();
        setUi();
    }

    private void setUi() {
        // status bar
        activity.findViewById(R.id.status_bar).setBackgroundColor(about.getColor());

        if (about == null) {
            Log.e("About", "Exploded");
        }
        // action bar
        collapsingToolbarLayout.setContentScrimColor(about.getColor());
        collapsingToolbarLayout.setStatusBarScrimColor(about.getColor());

        // set taskDesc, title image / text
        if (currentAbout.equals("now_playing")) {
            collapsingToolbarLayout.setTitle(about.getTitle());
            window.setRecentColor(about.getTitle(), about.getColor(), activity);
            // storage
            Picasso.with(activity)
                    .load("file:" + about.getImage(currentPreset))
                    .placeholder(R.drawable.ic_image_album_placeholder)
                    .error(R.drawable.ic_image_album_error)
                    .into(((ImageView) activity.findViewById(R.id.layout_image)));
        } else {
            collapsingToolbarLayout.setTitle(about.getSongName());
            window.setRecentColor(about.getSongName(), about.getColor(), activity);
            // res
            ((ImageView) activity.findViewById(R.id.layout_image)).setImageResource(
                    window.getDrawableId(about.getSongArtist())
            );
        }

        // bio
        ((TextView) activity.findViewById(R.id.layout_bio_title)).setText(about.getBio().getTitle());
        ((TextView) activity.findViewById(R.id.layout_bio_title)).setTextColor(about.getColor());

        ImageView imageView = ((ImageView) activity.findViewById(R.id.layout_bio_image));
        switch (currentAbout) {
            case "now_playing":
                // storage
                Picasso.with(activity)
                        .load("file:" + about.getBio().getImage(currentPreset))
                        .placeholder(R.drawable.ic_image_album_placeholder)
                        .error(R.drawable.ic_image_album_error)
                        .into(imageView);
                break;
            case "tapad":
                // res
                imageView.setImageResource(window.getDrawableId("about_bio_tapad"));
                break;
            default:
                // no bio image exception
                ((ImageView) activity.findViewById(R.id.layout_bio_image)).setVisibility(View.GONE);
                activity.findViewById(R.id.layout_bio_image_divider).setVisibility(View.GONE);
                break;
        }

        ((TextView) activity.findViewById(R.id.layout_bio_name)).setText(about.getBio().getName());
        ((TextView) activity.findViewById(R.id.layout_bio_text)).setText(about.getBio().getText());
        ((TextView) activity.findViewById(R.id.layout_bio_source)).setText(about.getBio().getSource());
        if (about.getPresetArtist() != null) {
            ((TextView) activity.findViewById(R.id.layout_bio_preset_creator)).setText(getStringFromId("about_bio_preset_by", activity) + " " + about.getPresetArtist());
        } else {
            window.setGone(R.id.layout_bio_preset_creator, 0, activity);
            window.setGone(R.id.layout_bio_text_divider, 0, activity);
        }

        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) activity.findViewById(R.id.layout_detail_recycler_view)).setLayoutManager(layoutManager);
        ((RecyclerView) activity.findViewById(R.id.layout_detail_recycler_view)).setNestedScrollingEnabled(false);
        ((RecyclerView) activity.findViewById(R.id.layout_detail_recycler_view)).setAdapter(new DetailAdapter(about, R.layout.adapter_details, getApplicationContext(), activity));
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
            if (currentAbout.equals("now_playing")) {
                window.setRecentColor(about.getTitle(), about.getColor(), activity);
            } else {
                window.setRecentColor(about.getSongName(), about.getColor(), activity);
            }
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
