package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.fragment.AboutFragment;
import com.bedrock.padder.helper.AdMobHelper;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.FabHelper;
import com.bedrock.padder.helper.FileHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.SoundHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.Preset;
import com.bedrock.padder.model.preset.PresetSchema;
import com.bedrock.padder.model.preset.store.PresetStore;
import com.bedrock.padder.model.tutorial.Tutorial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import static com.bedrock.padder.helper.FileHelper.PROJECT_LOCATION_PRESETS;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class MainActivity
        extends AppCompatActivity
        implements AboutFragment.OnFragmentInteractionListener {

    public static final String TAG = "MainActivity";

    public static boolean isPresetLoading = false;
    public static boolean isPresetVisible = false;
    public static boolean isPresetDownloading = false;
    public static boolean isPresetChanged = false;
    public static boolean isTutorialVisible = false;
    public static boolean isAboutVisible = false;
    public static boolean isSettingVisible = false;
    public static boolean isDeckShouldCleared = false;
    public static boolean isStopLoopOnSingle = false;

    public static Preset currentPreset = null;
    private Tutorial tutorial = null;

    public static PresetStore installed = null;
    public static PresetStore online = null;

    public Preferences preferences = null;

    // Used for circularReveal
    // End two is for settings coordinate animation
    public static int coordinate[] = {0, 0, 0, 0};

    final AppCompatActivity a = this;

    public static int PAD_PATTERN = 0;

    int currentVersionCode;
    int themeColor = R.color.hello;
    int color = R.color.cyan_400;
    int colorDef = R.color.grey;

    private AnimateHelper anim = new AnimateHelper();
    private SoundHelper sound = new SoundHelper();
    private WindowHelper w = new WindowHelper();
    private FabHelper fab = new FabHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();
    private IntentHelper intent = new IntentHelper();
    private AdMobHelper ad = new AdMobHelper();
    private FileHelper file = new FileHelper();

    private boolean doubleBackToExitPressedOnce = false;
    private boolean isToolbarVisible = false;
    private int circularRevealDuration = 400;
    private int fadeAnimDuration = 200;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void setSettingVisible(boolean isVisible) {
        isSettingVisible = isVisible;
        Log.d("SettingVisible", String.valueOf(isSettingVisible));
    }

    public static void setAboutVisible(boolean isVisible) {
        isAboutVisible = isVisible;
        Log.d("AboutVisible", String.valueOf(isAboutVisible));
    }

    public static void setPadPattern(int id) {
        /*
         *  1: 4 side
         *  2: vertical fade
         *  3: horizontal fade
         *  4: vertical-horizontal fade
         */

        PAD_PATTERN = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize Preferences
        preferences = new Preferences(this);

        checkVersion();

        // set color from Preferences
        setColor();
        toolbar.setActionBar(this);
        toolbar.setStatusBarTint(this);

        a.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Set UI
        setFab();
        setToolbar();

        String shortcut = getIntent().getStringExtra("shortcut");

        if (shortcut != null) {
            Log.i(TAG, "onCreate: shortcuts used " + shortcut);
            sound.cancelLoad();

            File folder = new File(PROJECT_LOCATION_PRESETS + "/" + shortcut);
            if (folder.isDirectory() && folder.exists()) {
                try {
                    currentPreset = gson.fromJson(file.getStringFromFile(PROJECT_LOCATION_PRESETS + "/" + shortcut + "/about/json"), PresetSchema.class).getPreset();
                    if (!file.isPresetAvailable(currentPreset)) {
                        // preset corrupted or doesn't exist
                        currentPreset = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    currentPreset = null;
                    Toast.makeText(a, R.string.preset_shortcut_error, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            switch (preferences.getStartPage()) {
                case "recent":
                    // load latest played preset
                    if (preferences.getLastPlayed() != null) {
                        try {
                            currentPreset = gson.fromJson(file.getStringFromFile(getCurrentPresetLocation() + "/about/json"), PresetSchema.class).getPreset();
                            if (!file.isPresetAvailable(currentPreset)) {
                                // preset corrupted or doesn't exist
                                currentPreset = null;
                            }
                        } catch (Exception e) {
                            // corrupted preset
                            // e.printStackTrace();
                            currentPreset = null;
                        }
                    }
                    break;
                case "about":
                    // load latest played preset and show about
                    setToolbarVisible();
                    if (preferences.getLastPlayed() != null) {
                        try {
                            currentPreset = gson.fromJson(file.getStringFromFile(getCurrentPresetLocation() + "/about/json"), PresetSchema.class).getPreset();
                            if (!file.isPresetAvailable(currentPreset)) {
                                // preset corrupted or doesn't exist
                                currentPreset = null;
                            }
                        } catch (Exception e) {
                            // corrupted preset
                            // e.printStackTrace();
                            currentPreset = null;
                        }
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coordinate[0] = w.getRect(R.id.toolbar_info, a).centerX();
                            coordinate[1] = w.getRect(R.id.toolbar_info, a).centerY();
                            showAboutFragment();
                        }
                    }, 200);
                    break;
                case "preset_store":
                    // set null for selecting presets
                    preferences.setLastPlayed(null);
                    setToolbarVisible();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coordinate[0] = w.getRect(R.id.toolbar_preset, a).centerX();
                            coordinate[1] = w.getRect(R.id.toolbar_preset, a).centerY();
                            intent.intent(a, "activity.PresetStoreActivity", 0);
                        }
                    }, 200);
                    break;
                default:
                    // load latest played preset
                    if (preferences.getLastPlayed() != null) {
                        try {
                            currentPreset = gson.fromJson(file.getStringFromFile(getCurrentPresetLocation() + "/about/json"), PresetSchema.class).getPreset();
                            if (!file.isPresetAvailable(currentPreset)) {
                                // preset corrupted or doesn't exist
                                currentPreset = null;
                            }
                        } catch (Exception e) {
                            // corrupted preset
                            // e.printStackTrace();
                            currentPreset = null;
                        }
                    }
                    break;
            }
        }

        // load preset info / sound
        setPresetInfo();
        sound.setDecks(R.color.colorAccent, R.color.grey, a);

        anim.fadeIn(R.id.actionbar_layout, 0, 200, "background", a);
        anim.fadeIn(R.id.actionbar_image, 200, 200, "image", a);

        isPresetLoading = true;
        isStopLoopOnSingle = preferences.getStopLoopOnSingle();

        loadPreset(400);
        setButtonLayout();

        // Set transparent nav bar
        w.setStatusBar(R.color.transparent, a);
        w.setNavigationBar(R.color.transparent, a);

        //ab.setStatusHeight(a);
        sound.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                intent.intentWithExtra(a, "activity.AboutActivity", "about", "tapad", 0);
                break;
            case R.id.action_settings:
                intent.intent(a, "activity.SettingsActivity");
                break;
            case R.id.action_help:
                intent.intent(a, "activity.HelpActivity");
                break;
            case R.id.action_help_developer:
                intent.intent(this, "activity.VideoAdActivity");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // leave it empty
        // used for fragments
    }

    @Override
    public void onBackPressed() {
        Log.i("BackPressed", "isAboutVisible " + String.valueOf(isAboutVisible));
        if (isToolbarVisible == true) {
            if (isAboutVisible) {
                // About visible alone
                closeAbout();
            } else {
                // Toolbar visible alone
                closeToolbar(a);
            }
        } else {
            Log.d("BackPressed", "Down");
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
            }

            doubleBackToExitPressedOnce = true;

            Toast.makeText(this, R.string.confirm_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i("MainActivity", "onWindowFocusChanged");

        if (preferences.getStopOnFocusLoss()) {
            sound.stopAll();
        }

        if (isDeckShouldCleared) {
            // get changed color values
            setColor();
            sound.loadColor(color);
            setButtonLayout();

            final View buttonViews[] = {
                    a.findViewById(R.id.btn00),
                    a.findViewById(R.id.tgl1),
                    a.findViewById(R.id.tgl2),
                    a.findViewById(R.id.tgl3),
                    a.findViewById(R.id.tgl4),
                    a.findViewById(R.id.tgl5),
                    a.findViewById(R.id.tgl6),
                    a.findViewById(R.id.tgl7),
                    a.findViewById(R.id.tgl8),
                    a.findViewById(R.id.btn11),
                    a.findViewById(R.id.btn12),
                    a.findViewById(R.id.btn13),
                    a.findViewById(R.id.btn14),
                    a.findViewById(R.id.btn21),
                    a.findViewById(R.id.btn22),
                    a.findViewById(R.id.btn23),
                    a.findViewById(R.id.btn24),
                    a.findViewById(R.id.btn31),
                    a.findViewById(R.id.btn32),
                    a.findViewById(R.id.btn33),
                    a.findViewById(R.id.btn34),
                    a.findViewById(R.id.btn41),
                    a.findViewById(R.id.btn42),
                    a.findViewById(R.id.btn43),
                    a.findViewById(R.id.btn44)
            };

            for (View view : buttonViews) {
                view.setVisibility(View.INVISIBLE);
            }

            sound.revealButtonWithAnimation();
        }

        if (isPresetVisible) {
            if (isToolbarVisible) {
                // preset store visible
                closePresetStore();
            }
            isPresetVisible = false;
        }

        if (isSettingVisible) {
            closeSettings();
            setSettingVisible(false);
        }

        if (isPresetChanged) {
            tutorial = null;
            currentPreset = null;
            if (preferences.getLastPlayed() != null) {
                // preset loaded
                Log.d(TAG, "changed");
                PresetSchema preset = gson.fromJson(file.getStringFromFile(getCurrentPresetLocation() + "/about/json"), PresetSchema.class);
                if (preset != null) {
                    currentPreset = preset.getPreset();
                    loadPreset(0);
                }
            } else {
                Log.d(TAG, "removed");
            }
            setPresetInfo();
            isPresetChanged = false;
        }

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onPause() {
        ad.pauseAdView(R.id.adView_main, a);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("MainActivity", "onResume");
        sound.stop();
        int tutorial[] = {
                R.id.btn00_tutorial,
                R.id.tgl1_tutorial,
                R.id.tgl2_tutorial,
                R.id.tgl3_tutorial,
                R.id.tgl4_tutorial,
                R.id.tgl5_tutorial,
                R.id.tgl6_tutorial,
                R.id.tgl7_tutorial,
                R.id.tgl8_tutorial,
                R.id.btn11_tutorial,
                R.id.btn12_tutorial,
                R.id.btn13_tutorial,
                R.id.btn14_tutorial,
                R.id.btn21_tutorial,
                R.id.btn22_tutorial,
                R.id.btn23_tutorial,
                R.id.btn24_tutorial,
                R.id.btn31_tutorial,
                R.id.btn32_tutorial,
                R.id.btn33_tutorial,
                R.id.btn34_tutorial,
                R.id.btn41_tutorial,
                R.id.btn42_tutorial,
                R.id.btn43_tutorial,
                R.id.btn44_tutorial};

        for (int i = 0; i < tutorial.length; i++) {
            w.setInvisible(tutorial[i], 10, a);
        }

        ad.resumeAdView(R.id.adView_main, a);

        if (currentPreset != null && !file.isPresetAvailable(currentPreset)) {
            currentPreset = null;
        }

        setPresetInfo();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");

        sound.stop();
        sound.cancelLoad();

        ad.destroyAdView(R.id.adView_main, a);

        super.onDestroy();
    }

    private void checkVersion() {
        try {
            currentVersionCode = a.getPackageManager().getPackageInfo(a.getPackageName(), 0).versionCode;
            Log.i("versionCode", "versionCode retrieved = " + String.valueOf(currentVersionCode));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            currentVersionCode = -1;
            Log.e("NameNotFound", "NNFE, currentVersionCode = -1");
        }

        // version checks
        Intent launcherIntent = getIntent();
        if (launcherIntent != null &&
                launcherIntent.getStringExtra("version") != null) {
            String version = launcherIntent.getStringExtra("version");
            if (version.equals("new")) {
                // new install, show intro
                intent.intent(a, "activity.MainIntroActivity");
                preferences.setVersionCode(currentVersionCode);
            } else if (version.equals("updated")) {
                // updated, show changelog
                new MaterialDialog.Builder(a)
                        .title(w.getStringId("info_tapad_info_changelog"))
                        .content(w.getStringId("info_tapad_info_changelog_text"))
                        .contentColorRes(R.color.dark_primary)
                        .positiveText(R.string.dialog_close)
                        .positiveColorRes(R.color.colorAccent)
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                preferences.setVersionCode(currentVersionCode);
                                // show Studio Berict social links
                                new MaterialDialog.Builder(a)
                                        .title(w.getStringId("info_tapad_info_social"))
                                        .content(w.getStringId("info_tapad_info_social_text"))
                                        .contentColorRes(R.color.dark_primary)
                                        .positiveText(R.string.dialog_learn_more)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                intent.intentWithExtra(a, "activity.AboutActivity", "about", "dev", 0);
                                            }
                                        })
                                        .negativeText(R.string.close)
                                        .show();
                            }
                        })
                        .show();
            }
        }
    }

    private void showAboutFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_about_container, new AboutFragment())
                .commit();
        WindowHelper w = new WindowHelper();
        (a.findViewById(R.id.fragment_about_container)).setVisibility(View.VISIBLE);
        setAboutVisible(true);
        w.setRecentColor(R.string.about, 0, themeColor, a);
    }

    private void setButtonLayout() {
        int screenWidthPx = (int) (w.getWindowWidthPx(a) * preferences.getDeckMargin());
        int marginPx = w.getWindowWidthPx(a) / 160;
        int newWidthPx;
        int newHeightPx;
        int buttons[][] = {
                // first row is root view
                {R.id.ver0, R.id.tgl1, R.id.tgl2, R.id.tgl3, R.id.tgl4, R.id.btn00},
                {R.id.ver1, R.id.btn11, R.id.btn12, R.id.btn13, R.id.btn14, R.id.tgl5},
                {R.id.ver2, R.id.btn21, R.id.btn22, R.id.btn23, R.id.btn24, R.id.tgl6},
                {R.id.ver3, R.id.btn31, R.id.btn32, R.id.btn33, R.id.btn34, R.id.tgl7},
                {R.id.ver4, R.id.btn41, R.id.btn42, R.id.btn43, R.id.btn44, R.id.tgl8},
        };

        int tutorialButtons[][] = {
                // first row is root view
                {R.id.ver0_tutorial, R.id.tgl1_tutorial, R.id.tgl2_tutorial, R.id.tgl3_tutorial, R.id.tgl4_tutorial, R.id.btn00_tutorial},
                {R.id.ver1_tutorial, R.id.btn11_tutorial, R.id.btn12_tutorial, R.id.btn13_tutorial, R.id.btn14_tutorial, R.id.tgl5_tutorial},
                {R.id.ver2_tutorial, R.id.btn21_tutorial, R.id.btn22_tutorial, R.id.btn23_tutorial, R.id.btn24_tutorial, R.id.tgl6_tutorial},
                {R.id.ver3_tutorial, R.id.btn31_tutorial, R.id.btn32_tutorial, R.id.btn33_tutorial, R.id.btn34_tutorial, R.id.tgl7_tutorial},
                {R.id.ver4_tutorial, R.id.btn41_tutorial, R.id.btn42_tutorial, R.id.btn43_tutorial, R.id.btn44_tutorial, R.id.tgl8_tutorial},
        };

        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                newHeightPx = screenWidthPx / 9;
            } else {
                newHeightPx = (screenWidthPx / 9) * 2;
            }
            for (int j = 0; j < 6; j++) {
                if (j == 0) {
                    resizeView(tutorialButtons[i][j], 0, newHeightPx);
                    resizeView(buttons[i][j], 0, newHeightPx);
                } else if (j == 5) {
                    newWidthPx = screenWidthPx / 9;
                    resizeView(tutorialButtons[i][j], newWidthPx, newHeightPx);
                    resizeView(buttons[i][j], newWidthPx - (marginPx * 2), newHeightPx - (marginPx * 2));
                    w.setMarginLinearPX(buttons[i][j], marginPx, marginPx, marginPx, marginPx, a);
                    if (i != 0) {
                        ((TextView) a.findViewById(buttons[i][j])).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (newWidthPx / 3));
                    }
                } else {
                    newWidthPx = (screenWidthPx / 9) * 2;
                    resizeView(tutorialButtons[i][j], newWidthPx, newHeightPx);
                    resizeView(buttons[i][j], newWidthPx - (marginPx * 2), newHeightPx - (marginPx * 2));
                    w.setMarginLinearPX(buttons[i][j], marginPx, marginPx, marginPx, marginPx, a);
                    if (i == 0) {
                        ((TextView) a.findViewById(buttons[i][j])).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (newHeightPx / 3));
                    }
                }
            }

            Log.i(TAG, "Button layout reset");
        }
    }

    private void resizeView(int viewId, int newWidth, int newHeight) {
        View view = a.findViewById(viewId);
        //Log.d("resizeView", "width " + newWidth + " X height " + newHeight);
        if (newHeight > 0) {
            view.getLayoutParams().height = newHeight;
        }
        if (newWidth > 0) {
            view.getLayoutParams().width = newWidth;
        }
        view.setLayoutParams(view.getLayoutParams());
    }

    private void setFab() {
        fab.set(a);
        fab.setIcon(R.drawable.ic_info_white, a);
        fab.show();
        fab.setOnClickListener(new Runnable() {
            @Override
            public void run() {
                if (isToolbarVisible == false) {
                    // remove this for test
                    fab.hide(0, 200);
                    anim.fadeIn(R.id.toolbar, 200, 100, "toolbarIn", a);
                    isToolbarVisible = true;
                }
            }
        });

        // set bottom margin
        w.setMarginRelativePX(R.id.fab, 0, 0, w.convertDPtoPX(20, a), w.getNavigationBarFromPrefs(a) + w.convertDPtoPX(20, a), a);
    }

    private void setToolbarVisible() {
        fab.hide(0, 0);
        anim.fadeIn(R.id.toolbar, 0, 0, "toolbarShow", a);
        isToolbarVisible = true;
    }

    private void setToolbar() {
        // set bottom margin
        w.setMarginRelativePX(R.id.toolbar, 0, 0, 0, w.getNavigationBarFromPrefs(a), a);

        View info = findViewById(R.id.toolbar_info);
        View tutorial = findViewById(R.id.toolbar_tutorial);
        View preset = findViewById(R.id.toolbar_preset);
        View settings = findViewById(R.id.toolbar_settings);

        assert info != null;
        info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinate[0] = (int) event.getRawX();
                coordinate[1] = (int) event.getRawY();

                return false;
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAboutVisible == false) {
                    anim.circularRevealInPx(R.id.placeholder,
                            coordinate[0], coordinate[1],
                            0, (int) Math.hypot(coordinate[0], coordinate[1]) + 200, new AccelerateDecelerateInterpolator(),
                            circularRevealDuration, 0, a);

                    Handler about = new Handler();
                    about.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showAboutFragment();
                        }
                    }, circularRevealDuration);

                    anim.fadeOut(R.id.placeholder, circularRevealDuration, fadeAnimDuration, a);

                    isAboutVisible = true;
                }
            }
        });

        assert preset != null;
        preset.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinate[0] = (int) event.getRawX();
                coordinate[1] = (int) event.getRawY();

                return false;
            }
        });

        preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPresetVisible == false) {
                    anim.circularRevealInPx(R.id.placeholder,
                            coordinate[0], coordinate[1],
                            0, (int) Math.hypot(coordinate[0], coordinate[1]) + 200, new AccelerateDecelerateInterpolator(),
                            circularRevealDuration, 0, a);

                    intent.intent(a, "activity.PresetStoreActivity", circularRevealDuration);
                }
            }
        });

        assert tutorial != null;
        tutorial.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinate[0] = (int) event.getRawX();
                coordinate[1] = (int) event.getRawY();

                return false;
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTutorial();
            }
        });

        assert settings != null;
        settings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinate[2] = (int) event.getRawX();
                coordinate[3] = (int) event.getRawY();

                return false;
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim.circularRevealInPx(R.id.placeholder,
                        coordinate[2], coordinate[3],
                        0, (int) Math.hypot(coordinate[2], coordinate[3]) + 200, new AccelerateDecelerateInterpolator(),
                        circularRevealDuration, 0, a);

                intent.intent(a, "activity.SettingsActivity", circularRevealDuration);
            }
        });
    }

    private void closeToolbar(Activity activity) {
        anim.fadeOut(R.id.toolbar, 0, 100, activity);
        fab.show(100, 200);
        isToolbarVisible = false;
    }

    private void closeAbout() {
        Log.d("closeAbout", "triggered");
        anim.circularRevealInPx(R.id.placeholder,
                coordinate[0], coordinate[1],
                (int) Math.hypot(coordinate[0], coordinate[1]) + 200, 0, new AccelerateDecelerateInterpolator(),
                circularRevealDuration, fadeAnimDuration, a);

        anim.fadeIn(R.id.placeholder, 0, fadeAnimDuration, "aboutOut", a);

        setAboutVisible(false);

        Handler closeAbout = new Handler();
        closeAbout.postDelayed(new Runnable() {
            @Override
            public void run() {
                setPresetInfo();
                (a.findViewById(R.id.fragment_about_container)).setVisibility(View.GONE);
            }
        }, fadeAnimDuration);

        // reset the touch coordinates
        coordinate[0] = 0;
        coordinate[1] = 0;
    }

    private void showTutorial() {
        final ImageView tutorialToolbar = findViewById(R.id.toolbar_tutorial_icon);

        if (isTutorialVisible == false) {
            isTutorialVisible = true;
            if (currentPreset != null) {
                if (!isPresetLoading) {
                    if (currentPreset.getInAppTutorialAvailable()) {
                        if (tutorial == null) {
                            tutorial = new Tutorial(currentPreset.getTag(), a);
                            tutorial.setTutorialListener(new Tutorial.TutorialListener() {
                                @Override
                                public void onLoadStart(Tutorial tutorial) {
                                }

                                @Override
                                public void onLoadFinish(Tutorial t) {
                                    tutorial.start(0);
                                }

                                @Override
                                public void onStart(Tutorial tutorial) {
                                    tutorialToolbar.setImageResource(R.drawable.ic_tutorial_quit_white);
                                }

                                @Override
                                public void onPause(Tutorial tutorial, int syncIndex) {
                                    // TODO add pause UI changes
                                }

                                @Override
                                public void onFinish(Tutorial tutorial) {
                                    tutorialToolbar.setImageResource(R.drawable.ic_tutorial_white);
                                    isTutorialVisible = false;
                                }
                            });
                            tutorial.parse();
                        } else {
                            tutorial.start(0);
                        }
                    } else if (currentPreset.getAbout().getTutorialAvailable()) {
                        String tutorialText = currentPreset.getAbout().getTutorialVideoLink();
                        if (tutorialText == null || tutorialText.equals("null")) {
                            tutorialText = getStringFromId("dialog_tutorial_text_error", a);
                        }

                        // TODO update tutorial dialog
                        new MaterialDialog.Builder(a)
                                .title(R.string.dialog_tutorial_title)
                                .content(tutorialText)
                                .neutralText(R.string.dialog_close)
                                .dismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        isTutorialVisible = false;
                                    }
                                })
                                .show();
                    }
                } else {
                    // still loading preset
                    Toast.makeText(a, R.string.tutorial_loading, Toast.LENGTH_LONG).show();
                    isTutorialVisible = false;
                }
            } else {
                // no selected preset
                Toast.makeText(a, R.string.tutorial_no_preset, Toast.LENGTH_LONG).show();
                isTutorialVisible = false;
            }
        } else {
            // playing tutorial
            if (tutorial != null) {
                tutorial.stop();
            }
            isTutorialVisible = false;
        }
    }

    private void closeSettings() {
        setPresetInfo();

        Log.d(TAG, coordinate[2] + ", " + coordinate[3]);

        if (coordinate[2] > 0 && coordinate[3] > 0) {
            w.setVisible(R.id.placeholder, 0, a);
            anim.circularRevealInPx(R.id.placeholder,
                    coordinate[2], coordinate[3],
                    (int) Math.hypot(coordinate[2], coordinate[3]) + 200, 0, new AccelerateDecelerateInterpolator(),
                    circularRevealDuration, 200, a);
        }
    }

    private void closePresetStore() {
        setPresetInfo();

        if (!isAboutVisible) {
            if (coordinate[0] > 0 && coordinate[1] > 0) {
                w.setVisible(R.id.placeholder, 0, a);
                anim.circularRevealInPx(R.id.placeholder,
                        coordinate[0], coordinate[1],
                        (int) Math.hypot(coordinate[0], coordinate[1]) + 200, 0, new AccelerateDecelerateInterpolator(),
                        circularRevealDuration, 200, a);
                Log.i(TAG, "Animated");
            }
        }
    }

    private void loadPreset(int delay) {
        if (currentPreset != null) {
            Handler preset = new Handler();
            preset.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sound.load(currentPreset, preferences.getColor(), colorDef, a);
                    new Preferences(a).setLastPlayed(currentPreset.getTag());
                }
            }, delay);
        }
    }

    private void setPresetInfo() {
        if (isAboutVisible == false && currentPreset != null) {
            themeColor = currentPreset.getAbout().getColor();
            toolbar.setActionBarTitle(0);
            toolbar.setActionBarColor(themeColor, a);
            toolbar.setActionBarPadding(a);
            toolbar.setActionBarImage(
                    PROJECT_LOCATION_PRESETS + "/" + currentPreset.getTag() + "/about/artist_icon",
                    this);
            w.setRecentColor(0, 0, themeColor, a);
            w.setVisible(R.id.base, 0, a);
            w.setGone(R.id.main_cardview_preset_store, 0, a);
        } else if (currentPreset == null || preferences.getLastPlayed() == null) {
            toolbar.setActionBarTitle(R.string.app_name);
            toolbar.setActionBarColor(R.color.colorPrimary, a);
            toolbar.setActionBarPadding(a);
            toolbar.setActionBarImage(0, this);
            w.setRecentColor(0, 0, R.color.colorPrimary, a);
            (a.findViewById(R.id.main_cardview_preset_store)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.intent(a, "activity.PresetStoreActivity");
                }
            });
            (a.findViewById(R.id.main_cardview_preset_store_download)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.intent(a, "activity.PresetStoreActivity");
                }
            });
            w.setVisible(R.id.main_cardview_preset_store, 0, a);
            w.setGone(R.id.base, 0, a);
        }
    }

    public String getCurrentPresetLocation() {
        if (preferences.getLastPlayed() != null && preferences.getLastPlayed().length() > 0) {
            return PROJECT_LOCATION_PRESETS + "/" + preferences.getLastPlayed();
        } else {
            return null;
        }
    }

    private void setColor() {
        color = preferences.getColor();
    }
}