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
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.fragment.PresetStoreInstalledFragment;
import com.bedrock.padder.fragment.PresetStoreOnlineFragment;
import com.bedrock.padder.fragment.Refreshable;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.FabHelper;
import com.bedrock.padder.helper.FileHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.bedrock.padder.activity.MainActivity.isPresetVisible;
import static com.bedrock.padder.helper.FileHelper.PROJECT_LOCATION_PRESETS;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class PresetStoreActivity extends AppCompatActivity implements FileChooserDialog.FileCallback {

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();
    private IntentHelper intent = new IntentHelper();
    private FabHelper fab = new FabHelper();
    private FileHelper fileHelper = new FileHelper();

    private static final int REQUEST_WRITE_STORAGE = 112;
    private boolean hasPermission;

    public static PresetStoreInstalledFragment installedFragment;
    public static PresetStoreOnlineFragment onlineFragment;

    Activity activity = this;

    private String tapadFolderPath = Environment.getExternalStorageDirectory().getPath() + "/Tapad";
    private String themeTitle;
    private String TAG = "PresetStore";

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private int themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_store);

        themeColor = getResources().getColor(R.color.colorPresetStore);
        themeTitle = getResources().getString(R.string.preset_store);

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

        hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        enterAnim();
        setUi();
        setDirectory();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // reload my activity with permission granted or use the features what required the permission
                    hasPermission = true;
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

    private void openPreset() {
        new FileChooserDialog.Builder(this)
                .initialPath(Environment.getExternalStorageDirectory().getPath())
                .mimeType("application/zip")
                .tag("optional-identifier")
                .show(this);
        Toast.makeText(activity, R.string.preset_store_open_preset, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFileSelection(@NonNull FileChooserDialog dialog, @NonNull File file) {
        Log.d(TAG, file.getAbsolutePath());
        final String presetName = getCustomFolderName();

        // install zip
        fileHelper.unzip(file.getAbsolutePath(),
                PROJECT_LOCATION_PRESETS,
                presetName,
                activity,
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "installed " + presetName);
                        setViewPager();
                    }
                }
        );
    }

    @Override
    public void onFileChooserDismissed(@NonNull FileChooserDialog dialog) {
    }

    String getCustomFolderName() {
        File files[] = new File(PROJECT_LOCATION_PRESETS).listFiles();
        String folderName = getResources().getString(R.string.custom_preset_folder_name);
        int fileNameCount = 0;
        for (File folder : files) {
            if (folder.getName().startsWith(folderName)) {
                int num = Integer.parseInt(folder.getName().replace(folderName, ""));
                if (fileNameCount < num) {
                    fileNameCount = num;
                }
            }
        }
        // make directory which name is custom-preset-{maxInt + 1}
        File customFolder = new File(PROJECT_LOCATION_PRESETS + "/" + folderName + ++fileNameCount + "/");
        if (customFolder.mkdirs()) {
            Log.d(TAG, "folder created at " + customFolder.getAbsolutePath());
        } else {
            Log.e(TAG, "folder failed to create at " + customFolder.getAbsolutePath());
        }
        return (folderName + fileNameCount);
    }

    private void setViewPager() {
        if (hasPermission) {
            Log.d(TAG, "setViewPager");

            installedFragment = new PresetStoreInstalledFragment();
            onlineFragment = new PresetStoreOnlineFragment();

            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(installedFragment, getStringFromId(R.string.tab_1, activity));
            viewPagerAdapter.addFragment(onlineFragment, getStringFromId(R.string.tab_2, activity));

            viewPager = findViewById(R.id.layout_viewpager);
            viewPager.setAdapter(viewPagerAdapter);

            final PresetStoreOnlineFragment onlineFragment = (PresetStoreOnlineFragment) viewPagerAdapter.instantiateItem(viewPager, 1);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                int position = 0;

                // from: https://stackoverflow.com/questions/31319758/how-can-i-animate-the-new-floating-action-button-between-tab-fragment-transition
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position != 1) {
                        if (positionOffset < 0.5) {
                            if (!fab.isVisible()) {
                                fab.setVisibility(true);
                            }
                            fab.setScale((0.5 - positionOffset) * 2);
                        } else if (positionOffset >= 0.5) {
                            fab.setVisibility(false);
                        }
                    } else {
                        fab.setVisibility(false);
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    this.position = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // from: https://stackoverflow.com/questions/20412379/viewpager-update-fragment-on-swipe
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        final Fragment fragment = (Fragment) viewPagerAdapter.instantiateItem(viewPager, position);
                        if (fragment != null) {
                            if (fragment instanceof Refreshable) {
                                ((Refreshable) fragment).refresh();
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                    }
//                                }, 2000);
                            } else {
                                Log.d(TAG, "Not an instance of any sub fragment");
                            }
                        }
                    } else {
                        onlineFragment.setLoadingFinished(false);
                    }
                }
            });

            TabLayout tabLayout = findViewById(R.id.layout_tab_layout);
            tabLayout.setupWithViewPager(viewPager);
//            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                @Override
//                public void onTabSelected(TabLayout.Tab tab) {
//                    if (tab.getPosition() == 0) {
//                        // show open preset fab
//                        if (!fab.isVisible()) {
//                            Log.d(TAG, "show from tabLayout");
//                            fab.show();
//                        }
//                    } else {
//                        if (onlineFragment != null) {
//                            ((PresetStoreOnlineFragment) onlineFragment).clear();
//                        }
//                        if (fab.isVisible()) {
//                            fab.hide();
//                        }
//                    }
//                }
//
//                @Override
//                public void onTabUnselected(TabLayout.Tab tab) {}
//
//                @Override
//                public void onTabReselected(TabLayout.Tab tab) {}
//            });
        }
    }

    private void setFab() {
        fab.set(activity);
        fab.setIcon(R.drawable.ic_search_white, activity);
        fab.setOnClickListener(new Runnable() {
            @Override
            public void run() {
                openPreset();
            }
        });

        // set bottom margin
        window.setMarginRelativePX(R.id.fab, 0, 0,
                window.convertDPtoPX(20, activity),
                window.getNavigationBarFromPrefs(activity) + window.convertDPtoPX(20, activity),
                activity);
    }

    private void setUi() {
        // status bar
        activity.findViewById(R.id.status_bar).setBackgroundColor(themeColor);

        // action bar
        collapsingToolbarLayout.setContentScrimColor(themeColor);
        collapsingToolbarLayout.setStatusBarScrimColor(themeColor);
        collapsingToolbarLayout.setTitle(themeTitle);

        // set taskDesc
        window.setRecentColor(themeTitle, themeColor, activity);

        // title image / text
        ((ImageView) activity.findViewById(R.id.layout_image)).setImageResource(R.drawable.about_image_preset_store);

        // fab
        setFab();

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
        isPresetVisible = true;
        anim.fadeOut(R.id.layout_nested_scrollview, 0, 200, activity);
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}