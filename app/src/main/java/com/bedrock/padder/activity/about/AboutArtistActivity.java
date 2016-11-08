package com.bedrock.padder.activity.about;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bedrock.padder.R;
import com.bedrock.padder.service.AnimService;
import com.bedrock.padder.service.IntentService;
import com.bedrock.padder.service.ThemeService;
import com.bedrock.padder.service.WindowService;

public class AboutArtistActivity extends AppCompatActivity {

    private WindowService window = new WindowService();
    private AnimService anim = new AnimService();
    private IntentService intent = new IntentService();
    private ThemeService theme = new ThemeService();

    Activity activity;

    SharedPreferences prefs;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_artist);

        activity = this;
        prefs = getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        window.setNavigationBar(R.color.transparent, activity);

        View statusbar = (View) findViewById(R.id.statusbar);
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

        window.setMarginRelativePX(R.id.layout_artist_relative, 0, prefs.getInt("statBarPX", 0), 0, 0, activity);
        window.getView(R.id.layout_artist_margin, activity).getLayoutParams().height = prefs.getInt("navBarPX", 0) + window.convertDPtoPX(10, activity);

        enterAnim();
        //setLink();
        setSchemeInfo();
    }

    @Override
    public void onBackPressed() {
        anim.fadeOut(R.id.layout_artist_text, 0, 200, activity);
        anim.fadeOut(R.id.layout_artist_detail, 0, 200, activity);
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                AboutArtistActivity.super.onBackPressed();
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
            theme.setGone(R.id.layout_artist_placeholder, 0, activity);
            setSchemeInfo();
        }
    }

    void enterAnim() {
        anim.fadeIn(R.id.layout_artist_text, 400, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_artist_detail_bio, 500, 200, "bioIn", activity);
        anim.fadeIn(R.id.layout_artist_detail_about, 600, 200, "aboutIn", activity);
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }

    void setSchemeInfo() {
        final int scheme = prefs.getInt("scheme", 1) - 1;
        Log.d("scheme", String.valueOf(scheme));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        int textRes[][][] = {
                {
                        {R.id.statusbar, R.color.hello_dark},
                        {R.id.layout_artist_image, R.drawable.cardview_background_artist_hello},
                        {R.id.layout_artist_bio_image, R.drawable.about_bio_hello},
                        {R.id.layout_artist_bio_title, R.color.hello},
                        {R.string.hello_artist, R.color.hello},
                        {R.string.hello_artist},
                        {R.color.hello},
                        {R.id.layout_artist_bio_title, R.string.hello_bio},
                        {R.id.layout_artist_bio_name, R.string.hello_bio_name},
                        {R.id.layout_artist_bio_text, R.string.hello_bio_full},
                        {R.id.layout_artist_bio_source, R.string.hello_bio_source},
                        {R.id.layout_artist_about_title, R.string.hello_about},
                        {R.id.layout_artist_web_link, R.string.hello_web},
                        {R.id.layout_artist_facebook_link, R.string.hello_facebook},
                        {R.id.layout_artist_twitter_link, R.string.hello_twitter},
                        {R.id.layout_artist_youtube_link, R.string.hello_youtube},
                        {R.id.layout_artist_soundcloud_link, R.string.hello_soundcloud}
                }, {
                {R.id.statusbar, R.color.roses_dark},
                {R.id.layout_artist_image, R.drawable.cardview_background_artist_roses},
                {R.id.layout_artist_bio_image, R.drawable.about_bio_roses},
                {R.id.layout_artist_bio_title, R.color.roses},
                {R.string.roses_artist, R.color.roses},
                {R.string.roses_artist},
                {R.color.roses},
                {R.id.layout_artist_bio_title, R.string.roses_bio},
                {R.id.layout_artist_bio_name, R.string.roses_bio_name},
                {R.id.layout_artist_bio_text, R.string.roses_bio_full},
                {R.id.layout_artist_bio_source, R.string.roses_bio_source},
                {R.id.layout_artist_about_title, R.string.roses_about},
                {R.id.layout_artist_web_link, R.string.roses_web},
                {R.id.layout_artist_facebook_link, R.string.roses_facebook},
                {R.id.layout_artist_twitter_link, R.string.roses_twitter},
                {R.id.layout_artist_youtube_link, R.string.roses_youtube},
                {R.id.layout_artist_soundcloud_link, R.string.roses_soundcloud}
        }, {
                {R.id.statusbar, R.color.faded_dark},
                {R.id.layout_artist_image, R.drawable.cardview_background_artist_faded},
                {R.id.layout_artist_bio_image, R.drawable.about_bio_faded},
                {R.id.layout_artist_bio_title, R.color.faded},
                {R.string.faded_artist, R.color.faded},
                {R.string.faded_artist},
                {R.color.faded},
                {R.id.layout_artist_bio_title, R.string.faded_bio},
                {R.id.layout_artist_bio_name, R.string.faded_bio_name},
                {R.id.layout_artist_bio_text, R.string.faded_bio_full},
                {R.id.layout_artist_bio_source, R.string.faded_bio_source},
                {R.id.layout_artist_about_title, R.string.faded_about},
                {R.id.layout_artist_web_link, R.string.faded_web},
                {R.id.layout_artist_facebook_link, R.string.faded_facebook},
                {R.id.layout_artist_twitter_link, R.string.faded_twitter},
                {R.id.layout_artist_youtube_link, R.string.faded_youtube},
                {R.id.layout_artist_soundcloud_link, R.string.faded_soundcloud}
        }
        };

        for (int i = 0; i < 17; i++) {
            switch (i) {
                case 0:
                    window.getView(textRes[scheme][i][0], activity).setBackgroundColor(getResources().getColor(textRes[scheme][i][1]));
                    break;
                case 1:
                case 2:
                    window.getImageView(textRes[scheme][i][0], activity).setImageResource(textRes[scheme][i][1]);
                    break;
                case 3:
                    window.getTextView(textRes[scheme][i][0], activity).setTextColor(getResources().getColor(textRes[scheme][i][1]));
                    break;
                case 4:
                    window.setRecentColor(textRes[scheme][i][0], 0, textRes[scheme][i][1], activity);
                    break;
                case 5:
                    collapsingToolbarLayout.setTitle(getResources().getString(textRes[scheme][i][0]));
                    break;
                case 6:
                    collapsingToolbarLayout.setContentScrimColor(getResources().getColor(textRes[scheme][i][0]));
                    collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(textRes[scheme][i][0]));
                    break;
                default:
                    window.getTextView(textRes[scheme][i][0], activity).setText(textRes[scheme][i][1]);
                    break;
            }
        }

        final int linkRes[][][] = {
                {
                        {R.id.layout_artist_web, R.string.hello_web, R.string.web},
                        {R.id.layout_artist_facebook, R.string.hello_facebook, R.string.facebook},
                        {R.id.layout_artist_twitter, R.string.hello_twitter, R.string.twitter},
                        {R.id.layout_artist_youtube, R.string.hello_youtube, R.string.youtube},
                        {R.id.layout_artist_soundcloud, R.string.hello_soundcloud, R.string.soundcloud}
                }, {
                {R.id.layout_artist_web, R.string.roses_web, R.string.web},
                {R.id.layout_artist_facebook, R.string.roses_facebook, R.string.facebook},
                {R.id.layout_artist_twitter, R.string.roses_twitter, R.string.twitter},
                {R.id.layout_artist_youtube, R.string.roses_youtube, R.string.youtube},
                {R.id.layout_artist_soundcloud, R.string.roses_soundcloud, R.string.soundcloud}
        }, {
                {R.id.layout_artist_web, R.string.faded_web, R.string.web},
                {R.id.layout_artist_facebook, R.string.faded_facebook, R.string.facebook},
                {R.id.layout_artist_twitter, R.string.faded_twitter, R.string.twitter},
                {R.id.layout_artist_youtube, R.string.faded_youtube, R.string.youtube},
                {R.id.layout_artist_soundcloud, R.string.faded_soundcloud, R.string.soundcloud}
        }
        };

        for (final int j[] = {0}; j[0] < 5; j[0]++) {
            final int tmp[] = {linkRes[scheme][j[0]][1], linkRes[scheme][j[0]][2]};
            anim.circularRevealTouch(linkRes[scheme][j[0]][0], R.id.layout_artist_placeholder,
                    new AccelerateDecelerateInterpolator(), new Runnable() {
                        @Override
                        public void run() {
                            intent.intentLink(activity, getResources().getString(tmp[0]), 400);
                            window.setRecentColor(tmp[1], 0, R.color.colorAccent, activity);
                        }
                    }, 400, 0, activity);
        }
    }
}
