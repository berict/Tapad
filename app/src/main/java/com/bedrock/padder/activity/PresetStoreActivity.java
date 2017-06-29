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
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.fragment.PresetStoreInstalledFragment;
import com.bedrock.padder.fragment.PresetStoreOnlineFragment;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.FileHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.google.firebase.FirebaseApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.bedrock.padder.activity.MainActivity.isDeckShouldCleared;
import static com.bedrock.padder.activity.MainActivity.isPresetVisible;

public class PresetStoreActivity extends AppCompatActivity {

    Activity activity = this;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();
    private IntentHelper intent = new IntentHelper();
    private FileHelper fileHelper = new FileHelper();

    private int themeColor;
    private String themeTitle;
    private String TAG = "PresetStore";

    public static boolean isPresetDownloading = false;

    private static final int REQUEST_WRITE_STORAGE = 112;

    private boolean hasPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_store);

        isPresetVisible = true;

        themeColor = getResources().getColor(R.color.colorPresetStore);
        themeTitle = getResources().getString(R.string.preset_store);

        // initialize firebase
        FirebaseApp.initializeApp(activity);

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
        setDirectory();

        isDeckShouldCleared = true;

        hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // reload my activity with permission granted or use the features what required the permission
                    // TODO refresh fragments, check working
                    setViewPager();
                } else {
                    // show dialog to grant access
                    new MaterialDialog.Builder(activity)
                            .title(R.string.preset_store_permission_dialog_title)
                            .titleColorRes(R.color.dark_primary)
                            .content(R.string.preset_store_permission_dialog_text)
                            .contentColorRes(R.color.dark_action)
                            .positiveText(R.string.preset_store_permission_dialog_positive)
                            .positiveColorRes(R.color.dark_primary)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // go to settings
                                    intent.intentAppDetailSettings(activity, 0);
                                }
                            })
                            .negativeText(R.string.preset_store_permission_dialog_negative)
                            .negativeColorRes(R.color.red_400)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // close current activity
                                    finish(100);
                                }
                            })
                            .show();
                    Log.e(TAG, "Permission error");
                }
        }
    }

    private void finish(int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, delay);
    }

    String tapadFolderPath = Environment.getExternalStorageDirectory().getPath() + "/Tapad";

    private void setDirectory() {
        // loading start
        Log.d(TAG, "setDirectory");
        if (!hasPermission) {
            // no permission granted
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            // got the permission
            // Make sdcard/Tapad folder
            File folder = new File(tapadFolderPath);
            if (folder.mkdirs()) {
                Log.i(TAG, "folder successfully created");
            } else {
                // folder exists
                Log.e(TAG, "folder already exists");
            }

            // Make sdcard/Tapad/presets folder
            File presets = new File(tapadFolderPath + "/presets");
            if (presets.mkdirs()) {
                Log.i(TAG, "folder successfully created");
            } else {
                // folder exists
                Log.e(TAG, "folder already exists");
            }
        }
    }

    ViewPager viewPager;

    private void setViewPager() {
        if (hasPermission) {
            viewPager = (ViewPager) findViewById(R.id.layout_viewpager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new PresetStoreInstalledFragment(), window.getStringFromId(R.string.tab_1, activity));
            viewPagerAdapter.addFragment(new PresetStoreOnlineFragment(), window.getStringFromId(R.string.tab_2, activity));
            viewPager.setAdapter(viewPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.layout_tab_layout);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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

        // viewpager
        setViewPager();
    }

    @Override
    protected void onDestroy() {
        // remove all callbacks
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        anim.fadeOut(R.id.layout_preset_store, 0, 200, activity);
        anim.fadeOut(R.id.layout_text, 100, 200, activity);
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                PresetStoreActivity.super.onBackPressed();
            }
        }, 300);
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
            window.setRecentColor(themeTitle, themeColor, activity);
        }
    }

    void enterAnim() {
        anim.fadeIn(R.id.layout_text, 100, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_preset_store, 200, 200, "presetIn", activity);
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }
}