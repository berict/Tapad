package com.bedrock.padder.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.File;
import java.io.FileOutputStream;

public class PresetStoreActivity extends AppCompatActivity {

    Activity activity = this;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private WindowService window = new WindowService();
    private AnimService anim = new AnimService();
    private ThemeService theme = new ThemeService();
    private ToolbarService toolbar = new ToolbarService();

    private int themeColor;
    private String themeTitle;
    private String TAG = "PresetStore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_store);

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

        themeColor = activity.getResources().getColor(R.color.amber);
        themeTitle = activity.getResources().getString(R.string.preset_store);

        window.setOnClick(R.id.do_sth, new Runnable() {
            @Override
            public void run() {
                doSth();
            }
        }, activity);

        enterAnim();
        setUi();
    }

    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                    doSth();
                } else {
                    Log.e(TAG, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission");
                }
        }
    }

    private void doSth() {
        // TODO this is a dev only function

        boolean hasPermission =
                ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        String string = "{\"about\":{\"actionbar_color\":\"#00D3BE\",\"bio\":{\"image\":\"about_bio_faded\",\"name\":\"Alan Olav Walker\",\"source\":\"Powered by Wikipedia X last.fm\",\"text\":\"Alan Walker (Alan Olav Walker) is a British-Norwegian record producer who was born in Northampton, England. He recorded electronic dance music single \\\"Faded\\\" and his song released on NoCopyrightSounds, \\\"Fade\\\".\",\"title\":\"Alan Walker\\u0027s biography\"},\"details\":[{\"items\":[{\"hint\":\"https://facebook.com/alanwalkermusic\",\"hint_is_visible\":true,\"image_id\":\"ic_logo_facebook\",\"runnable_is_with_anim\":false,\"text_id\":\"facebook\"},{\"hint\":\"https://twitter.com/IAmAlanWalker\",\"hint_is_visible\":true,\"image_id\":\"ic_logo_twitter\",\"runnable_is_with_anim\":false,\"text_id\":\"twitter\"},{\"hint\":\"https://soundcloud.com/alanwalker\",\"hint_is_visible\":true,\"image_id\":\"ic_logo_soundcloud\",\"runnable_is_with_anim\":false,\"text_id\":\"soundcloud\"},{\"hint\":\"https://instagram.com/alanwalkermusic\",\"hint_is_visible\":true,\"image_id\":\"ic_logo_instagram\",\"runnable_is_with_anim\":false,\"text_id\":\"instagram\"},{\"hint\":\"https://plus.google.com/u/0/+Alanwalkermusic\",\"hint_is_visible\":true,\"image_id\":\"ic_logo_google_plus\",\"runnable_is_with_anim\":false,\"text_id\":\"google_plus\"},{\"hint\":\"https://youtube.com/user/DjWalkzz\",\"hint_is_visible\":true,\"image_id\":\"ic_logo_youtube\",\"runnable_is_with_anim\":false,\"text_id\":\"youtube\"},{\"hint\":\"http://alanwalkermusic.no\",\"hint_is_visible\":true,\"image_id\":\"ic_logo_web\",\"runnable_is_with_anim\":false,\"text_id\":\"web\"}],\"title\":\"About Alan Walker\"},{\"items\":[{\"hint\":\"https://soundcloud.com/alanwalker/faded-1\",\"hint_is_visible\":false,\"image_id\":\"ic_logo_soundcloud\",\"runnable_is_with_anim\":false,\"text_id\":\"soundcloud\"},{\"hint\":\"https://youtu.be/60ItHLz5WEA\",\"hint_is_visible\":false,\"image_id\":\"ic_logo_youtube\",\"runnable_is_with_anim\":false,\"text_id\":\"youtube\"},{\"hint\":\"https://open.spotify.com/track/1brwdYwjltrJo7WHpIvbYt\",\"hint_is_visible\":false,\"image_id\":\"ic_logo_spotify\",\"runnable_is_with_anim\":false,\"text_id\":\"spotify\"},{\"hint\":\"https://play.google.com/store/music/album/Alan_Walker_Faded?id\\u003dBgdyyljvf7b624pbv5ylcrfevte\",\"hint_is_visible\":false,\"image_id\":\"ic_logo_google_play_music\",\"runnable_is_with_anim\":false,\"text_id\":\"google_play_music\"},{\"hint\":\"https://itunes.apple.com/us/album/faded/id1196294554?i\\u003d1196294581\",\"hint_is_visible\":false,\"image_id\":\"ic_logo_apple\",\"runnable_is_with_anim\":false,\"text_id\":\"apple\"},{\"hint\":\"https://amazon.com/Faded/dp/B01NBYNKWJ\",\"hint_is_visible\":false,\"image_id\":\"ic_logo_amazon\",\"runnable_is_with_anim\":false,\"text_id\":\"amazon\"},{\"hint\":\"https://pandora.com/alan-walker/faded-single/faded\",\"hint_is_visible\":false,\"image_id\":\"ic_logo_pandora\",\"runnable_is_with_anim\":false,\"text_id\":\"pandora\"}],\"title\":\"About this track\"}],\"image\":\"about_album_faded\",\"preset_creator\":\"Studio Berict\",\"title\":\"Alan Walker - Faded\",\"tutorial_link\":\"null\"},\"id\":2,\"music\":{\"bpm\":90,\"file_name\":\"alan_walker_faded\",\"is_gesture\":true,\"name\":\"preset_faded\",\"sound_count\":246}}";

        String sdcardPath = Environment.getExternalStorageDirectory().getPath() + "/";
        Log.d(TAG, sdcardPath);
//        FileInputStream inputStream;
//
//        try {
//            inputStream = openFileInput("test.txt");
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line).append("\n");
//            }
//            Log.i(TAG, stringBuilder.toString());
//            inputStream.close();
//            inputStreamReader.close();
//            bufferedReader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // make a dir in external sdcard
        File folder = new File(sdcardPath + "Tapad");
        if (folder.mkdirs() == false) {
            // folder exists, wtf?
            Log.e(TAG, "folder already exists");
        } else {
            Log.i(TAG, "folder successfully created");
        }

        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(new File(sdcardPath + "Tapad/" + "json.txt"));
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


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
