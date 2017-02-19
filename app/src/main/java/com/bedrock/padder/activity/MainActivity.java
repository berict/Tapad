package com.bedrock.padder.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.FabService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.SoundService;
import com.bedrock.padder.helper.ThemeService;
import com.bedrock.padder.helper.TutorialService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.about.About;
import com.bedrock.padder.model.about.Bio;
import com.bedrock.padder.model.about.Detail;
import com.bedrock.padder.model.about.Item;
import com.bedrock.padder.model.preset.Deck;
import com.bedrock.padder.model.preset.DeckTiming;
import com.bedrock.padder.model.preset.Music;
import com.bedrock.padder.model.preset.Pad;
import com.bedrock.padder.model.preset.Preset;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

@TargetApi(9)
@SuppressWarnings("deprecation")

public class MainActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    final Activity a = this;
    final String qs = "quickstart";
    final String TAG = "MainActivity";
    public boolean isPresetLoading = false;
    public boolean tgl1 = false;
    public boolean tgl2 = false;
    public boolean tgl3 = false;
    public boolean tgl4 = false;
    public boolean tgl5 = false;
    public boolean tgl6 = false;
    public boolean tgl7 = false;
    public boolean tgl8 = false;
    float volume;
    SharedPreferences prefs = null;
    int currentVersionCode;
    int themeColor = R.color.hello;
    int color = R.color.red;
    boolean doubleBackToExitPressedOnce = false;
    MaterialDialog ChangelogDialog;
    MaterialDialog QuickstartDialog;
    MaterialDialog PresetDialog;
    boolean isSchemeChanged = false;
    int toggleSoundId = 0;
    int togglePatternId = 0;
    private AnimService anim = new AnimService();
    private ThemeService t = new ThemeService();
    private SoundService sound = new SoundService();
    private IntentService intent = new IntentService();
    private WindowService w = new WindowService();
    private FabService fab = new FabService();
    private AppbarService ab = new AppbarService();
    private TutorialService tut = new TutorialService();
    private boolean isToolbarVisible = false;
    private boolean isAboutVisible = false;
    private boolean isPresetVisible = false;
    private boolean isSettingVisible = false;
    private boolean isTutorialVisible = false;
    private int circularRevealDuration = 400;
    private int fadeAnimDuration = 200;
    private MaterialTapTargetPrompt promptToggle;   // 1
    private MaterialTapTargetPrompt promptButton;   // 2
    private MaterialTapTargetPrompt promptPattern;  // 3
    private MaterialTapTargetPrompt promptFab;      // 4
    private MaterialTapTargetPrompt promptInfo;     // 5
    private MaterialTapTargetPrompt promptPreset;   // 6
    private MaterialTapTargetPrompt promptTutorial; // 7
    // Used for circularReveal
    // End two is for settings coordination
    private int coord[] = {0, 0, 0, 0};

    public static void largeLog(String tag, String content) {
        if (content.length() > 4000) {
            Log.d(tag, content.substring(0, 4000));
            largeLog(tag, content.substring(4000));
        } else {
            Log.d(tag, content);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Sharedprefs initialized");
        prefs = this.getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        // for test
        //prefs.edit().putInt(qs, 0).apply();

        w.getView(R.id.progress_bar_layout, a).setVisibility(View.GONE);

        volume = 1.0f;

        a.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        clearToggleButton();

        if (prefs.getBoolean("welcome", true)) {
            prefs.edit().putBoolean("welcome", false).apply();
        }

        setFab();
        setToolbar();
        setAbout();
        setSettings();
        setSchemeInfo();
        setToggleButton(R.color.colorAccent);
        enterAnim();
        // TODO new
        setButtonLayout();

        //Set transparent nav bar
        w.setStatusBar(R.color.transparent, a);
        w.setNavigationBar(R.color.transparent, a);

        w.setMarginRelativePX(R.id.fab, 0, 0, w.convertDPtoPX(20, a), prefs.getInt("navBarPX", 0) + w.convertDPtoPX(20, a), a);
        w.setMarginRelativePX(R.id.toolbar, 0, 0, 0, prefs.getInt("navBarPX", 0), a);
        ab.setStatusHeight(a);

        color = prefs.getInt("color", R.color.red);
        sound.setButton(R.color.grey_dark, a);

        //TODO EDIT
        makeJson();
    }

    void enterAnim() {
        anim.fadeIn(R.id.actionbar_layout, 0, 200, "background", a);
        anim.fadeIn(R.id.actionbar_image, 200, 200, "image", a);
        //TODO: Remove this to load preset
        loadPreset(400);
        isPresetLoading = true;
    }

    void setButtonLayout() {
        int screenWidthPx = w.getWindowWidthPx(a) - ((w.convertDPtoPX(36, a)) * 2);
        int marginPx = w.convertDPtoPX(2, a);
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
                        w.getTextView(buttons[i][j], a).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(newWidthPx / 3));
                    }
                } else {
                    newWidthPx = (screenWidthPx / 9) * 2;
                    resizeView(tutorialButtons[i][j], newWidthPx, newHeightPx);
                    resizeView(buttons[i][j], newWidthPx - (marginPx * 2), newHeightPx - (marginPx * 2));
                    w.setMarginLinearPX(buttons[i][j], marginPx, marginPx, marginPx, marginPx, a);
                    if(i == 0) {
                        w.getTextView(buttons[i][j], a).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(newHeightPx / 3));
                    }
                }
            }
        }
    }

    private void resizeView(int viewId, int newWidth, int newHeight) {
        View view = a.findViewById(viewId);
        Log.d("resizeView", "width " + newWidth + " X height " + newHeight);
        if (newHeight > 0) {
            view.getLayoutParams().height = newHeight;
        }
        if (newWidth > 0) {
            view.getLayoutParams().width = newWidth;
        }
        view.setLayoutParams(view.getLayoutParams());
    }

    @Override
    public void onBackPressed() {
        if (isToolbarVisible == true) {
            if ((prefs.getInt(qs, 0) != -1) && isAboutVisible == false && isSettingVisible == false) {
                Log.i("BackPressed", "Tutorial prompt is visible, backpress ignored.");
            } else {
                if (isAboutVisible == true) {
                    if (isSettingVisible == true) {
                        closeSettings();
                    } else {
                        closeAbout();
                    }
                } else if (isSettingVisible == true) {
                    closeSettings();
                } else {
                    closeToolbar();
                }
            }
        } else {
            if (prefs.getInt(qs, 0) != -1) {
                Log.i("BackPressed", "Tutorial prompt is visible, backpress ignored.");
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
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i("MainActivity", "onWindowFocusChanged");
        sound.soundAllStop();

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
            t.setInvisible(tutorial[i], 10, a);
        }
        //Log.d("MainActivity", "tutorial setVisible");
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (isTutorialVisible == true) {
            tut.tutorialStop(a);
        }

        Log.d("MainActivity", "onResume");
        sound.soundAllStop();
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
            t.setInvisible(tutorial[i], 10, a);
        }
        //Log.d("MainActivity", "Placeholder setVisible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");

        sound.soundAllStop();
        sound.cancelLoading();
    }

    public void setQuickstart(final Activity activity) {
        final SharedPreferences pref = activity.getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);
        try {
            currentVersionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
            Log.i("versionCode", "versionCode retrieved = " + String.valueOf(currentVersionCode));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            currentVersionCode = -1;
            Log.e("NameNotFound", "NNFE, currentVersionCode = -1");
        }

        try {
            Log.d("VersionCode", "sharedPrefs versionCode = " + String.valueOf(pref.getInt("versionCode", -1))
                    + " , currentVersionCode = " + String.valueOf(currentVersionCode));
            if (currentVersionCode > pref.getInt("versionCode", -1)) {
                // Updated
                Log.d("VersionCode", "Updated, show changelog");
                ChangelogDialog = new MaterialDialog.Builder(activity)
                        .title(R.string.dialog_changelog_title)
                        .content(R.string.dialog_changelog_text)
                        .contentColorRes(R.color.dark_primary)
                        .positiveText(R.string.dialog_changelog_positive)
                        .positiveColorRes(R.color.colorAccent)
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                // Dialog
                                if (pref.getInt(qs, 0) == 0) {
                                    QuickstartDialog = new MaterialDialog.Builder(activity)
                                            .title(R.string.dialog_quickstart_welcome_title)
                                            .content(R.string.dialog_quickstart_welcome_text)
                                            .positiveText(R.string.dialog_quickstart_welcome_positive)
                                            .positiveColorRes(R.color.colorAccent)
                                            .negativeText(R.string.dialog_quickstart_welcome_negative)
                                            .negativeColorRes(R.color.dark_secondary)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    pref.edit().putInt(qs, 0).apply();
                                                    Log.i("sharedPrefs", "quickstart edited to 0");
                                                }
                                            })
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    pref.edit().putInt(qs, -1).apply();
                                                    Log.i("sharedPrefs", "quickstart edited to -1");
                                                }
                                            })
                                            .dismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    if (pref.getInt(qs, 0) == 0) {
                                                        Log.i("setQuickstart", "Quickstart started");
                                                        if (promptFab != null) {
                                                            return;
                                                        }
                                                        promptToggle = new MaterialTapTargetPrompt.Builder(activity)
                                                                .setTarget(activity.findViewById(R.id.tgl1))
                                                                .setPrimaryText(R.string.dialog_tap_target_toggle_primary)
                                                                .setSecondaryText(R.string.dialog_tap_target_toggle_secondary)
                                                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                                                .setAutoDismiss(false)
                                                                .setAutoFinish(false)
                                                                .setFocalColourFromRes(R.color.white)
                                                                .setCaptureTouchEventOutsidePrompt(true)
                                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                    @Override
                                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                        if (tappedTarget) {
                                                                            promptToggle.finish();
                                                                            promptToggle = null;
                                                                            pref.edit().putInt(qs, 1).apply();
                                                                            Log.i("sharedPrefs", "quickstart edited to 1");
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onHidePromptComplete() {
                                                                        promptButton = new MaterialTapTargetPrompt.Builder(activity)
                                                                                .setTarget(activity.findViewById(R.id.btn31))
                                                                                .setPrimaryText(R.string.dialog_tap_target_button_primary)
                                                                                .setSecondaryText(R.string.dialog_tap_target_button_secondary)
                                                                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                                                                .setAutoDismiss(false)
                                                                                .setAutoFinish(false)
                                                                                .setFocalColourFromRes(R.color.white)
                                                                                .setFocalRadius((float) w.convertDPtoPX(80, activity))
                                                                                .setCaptureTouchEventOutsidePrompt(true)
                                                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                                    @Override
                                                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                                        if (tappedTarget) {
                                                                                            promptButton.finish();
                                                                                            promptButton = null;
                                                                                            pref.edit().putInt(qs, 2).apply();
                                                                                            Log.i("sharedPrefs", "quickstart edited to 2");
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onHidePromptComplete() {
                                                                                        promptPattern = new MaterialTapTargetPrompt.Builder(activity)
                                                                                                .setTarget(activity.findViewById(R.id.tgl7))
                                                                                                .setPrimaryText(R.string.dialog_tap_target_pattern_primary)
                                                                                                .setSecondaryText(R.string.dialog_tap_target_pattern_secondary)
                                                                                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                                                                                .setAutoDismiss(false)
                                                                                                .setAutoFinish(false)
                                                                                                .setFocalColourFromRes(R.color.white)
                                                                                                .setCaptureTouchEventOutsidePrompt(true)
                                                                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                                                    @Override
                                                                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                                                        if (tappedTarget) {
                                                                                                            promptPattern.finish();
                                                                                                            promptPattern = null;
                                                                                                            pref.edit().putInt(qs, 3).apply();
                                                                                                            Log.i("sharedPrefs", "quickstart edited to 3");
                                                                                                        }
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onHidePromptComplete() {
                                                                                                        promptFab = new MaterialTapTargetPrompt.Builder(activity)
                                                                                                                .setTarget(activity.findViewById(R.id.fab))
                                                                                                                .setPrimaryText(R.string.dialog_tap_target_fab_primary)
                                                                                                                .setSecondaryText(R.string.dialog_tap_target_fab_secondary)
                                                                                                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                                                                                                .setAutoDismiss(false)
                                                                                                                .setAutoFinish(false)
                                                                                                                .setFocalColourFromRes(R.color.white)
                                                                                                                .setCaptureTouchEventOutsidePrompt(true)
                                                                                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                                                                    @Override
                                                                                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                                                                        if (tappedTarget) {
                                                                                                                            promptFab.finish();
                                                                                                                            promptFab = null;
                                                                                                                            pref.edit().putInt(qs, 4).apply();
                                                                                                                            Log.i("sharedPrefs", "quickstart edited to 4");
                                                                                                                        }
                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onHidePromptComplete() {
                                                                                                                    }
                                                                                                                })
                                                                                                                .show();
                                                                                                    }
                                                                                                })
                                                                                                .show();
                                                                                    }
                                                                                })
                                                                                .show();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        Log.i("setQuickstart", "Quickstart canceled");
                                                    }
                                                }
                                            })
                                            .show();
                                }
                                pref.edit().putInt("versionCode", currentVersionCode).apply(); // Change this
                                Log.d("VersionCode", "putInt " + String.valueOf(pref.getInt("versionCode", -1)));
                            }
                        })
                        .show();
            } else {
                if (pref.getInt(qs, 0) == 0) {
                    QuickstartDialog = new MaterialDialog.Builder(activity)
                            .title(R.string.dialog_quickstart_welcome_title)
                            .content(R.string.dialog_quickstart_welcome_text)
                            .positiveText(R.string.dialog_quickstart_welcome_positive)
                            .positiveColorRes(R.color.colorAccent)
                            .negativeText(R.string.dialog_quickstart_welcome_negative)
                            .negativeColorRes(R.color.dark_secondary)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    pref.edit().putInt(qs, 0).apply();
                                    Log.i("sharedPrefs", "quickstart edited to 0");
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    pref.edit().putInt(qs, -1).apply();
                                    Log.i("sharedPrefs", "quickstart edited to -1");
                                }
                            })
                            .dismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    if (pref.getInt(qs, 0) == 0) {
                                        Log.i("setQuickstart", "Quickstart started");
                                        if (promptFab != null) {
                                            return;
                                        }
                                        promptToggle = new MaterialTapTargetPrompt.Builder(activity)
                                                .setTarget(activity.findViewById(R.id.tgl1))
                                                .setPrimaryText(R.string.dialog_tap_target_toggle_primary)
                                                .setSecondaryText(R.string.dialog_tap_target_toggle_secondary)
                                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                                .setAutoDismiss(false)
                                                .setAutoFinish(false)
                                                .setFocalColourFromRes(R.color.white)
                                                .setCaptureTouchEventOutsidePrompt(true)
                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                    @Override
                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                        if (tappedTarget) {
                                                            promptToggle.finish();
                                                            promptToggle = null;
                                                            pref.edit().putInt(qs, 1).apply();
                                                            Log.i("sharedPrefs", "quickstart edited to 1");
                                                        }
                                                    }

                                                    @Override
                                                    public void onHidePromptComplete() {
                                                        promptButton = new MaterialTapTargetPrompt.Builder(activity)
                                                                .setTarget(activity.findViewById(R.id.btn31))
                                                                .setPrimaryText(R.string.dialog_tap_target_button_primary)
                                                                .setSecondaryText(R.string.dialog_tap_target_button_secondary)
                                                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                                                .setAutoDismiss(false)
                                                                .setAutoFinish(false)
                                                                .setFocalColourFromRes(R.color.white)
                                                                .setFocalRadius((float) w.convertDPtoPX(80, activity))
                                                                .setCaptureTouchEventOutsidePrompt(true)
                                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                    @Override
                                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                        if (tappedTarget) {
                                                                            promptButton.finish();
                                                                            promptButton = null;
                                                                            pref.edit().putInt(qs, 2).apply();
                                                                            Log.i("sharedPrefs", "quickstart edited to 2");
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onHidePromptComplete() {
                                                                        promptPattern = new MaterialTapTargetPrompt.Builder(activity)
                                                                                .setTarget(activity.findViewById(R.id.tgl7))
                                                                                .setPrimaryText(R.string.dialog_tap_target_pattern_primary)
                                                                                .setSecondaryText(R.string.dialog_tap_target_pattern_secondary)
                                                                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                                                                .setAutoDismiss(false)
                                                                                .setAutoFinish(false)
                                                                                .setFocalColourFromRes(R.color.white)
                                                                                .setCaptureTouchEventOutsidePrompt(true)
                                                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                                    @Override
                                                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                                        if (tappedTarget) {
                                                                                            promptPattern.finish();
                                                                                            promptPattern = null;
                                                                                            pref.edit().putInt(qs, 3).apply();
                                                                                            Log.i("sharedPrefs", "quickstart edited to 3");
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onHidePromptComplete() {
                                                                                        promptFab = new MaterialTapTargetPrompt.Builder(activity)
                                                                                                .setTarget(activity.findViewById(R.id.fab))
                                                                                                .setPrimaryText(R.string.dialog_tap_target_fab_primary)
                                                                                                .setSecondaryText(R.string.dialog_tap_target_fab_secondary)
                                                                                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                                                                                .setAutoDismiss(false)
                                                                                                .setAutoFinish(false)
                                                                                                .setFocalColourFromRes(R.color.white)
                                                                                                .setCaptureTouchEventOutsidePrompt(true)
                                                                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                                                    @Override
                                                                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                                                        if (tappedTarget) {
                                                                                                            promptFab.finish();
                                                                                                            promptFab = null;
                                                                                                            pref.edit().putInt(qs, 4).apply();
                                                                                                            Log.i("sharedPrefs", "quickstart edited to 4");
                                                                                                        }
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onHidePromptComplete() {
                                                                                                    }
                                                                                                })
                                                                                                .show();
                                                                                    }
                                                                                })
                                                                                .show();
                                                                    }
                                                                })
                                                                .show();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        Log.i("setQuickstart", "Quickstart canceled");
                                    }
                                }
                            })
                            .show();
                }
            }
        } catch (Exception e) {
            Log.e("QuickstartException", e.getMessage());
        }
    }

    void setFab() {
        fab.setFab(a);
        fab.show();
        fab.onClick(new Runnable() {
            @Override
            public void run() {
                if (isToolbarVisible == false) {
                    fab.hide(0, 200);
                    anim.fadeIn(R.id.toolbar, 200, 100, "toolbarIn", a);
                    if (prefs.getInt(qs, 0) == 4) {
                        Log.i("setQuickstart", "Quickstart started");
                        if (promptInfo != null) {
                            return;
                        }
                        promptInfo = new MaterialTapTargetPrompt.Builder(a)
                                .setTarget(a.findViewById(R.id.toolbar_info))
                                .setPrimaryText(R.string.dialog_tap_target_info_primary)
                                .setSecondaryText(R.string.dialog_tap_target_info_secondary)
                                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                                .setFocalColourFromRes(R.color.blue_500)
                                .setAutoDismiss(false)
                                .setAutoFinish(false)
                                .setCaptureTouchEventOutsidePrompt(true)
                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                    @Override
                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                        if (tappedTarget) {
                                            promptInfo.finish();
                                            promptInfo = null;
                                            prefs.edit().putInt(qs, 5).apply();
                                            Log.i("sharedPrefs", "quickstart edited to 5");
                                        }
                                    }

                                    @Override
                                    public void onHidePromptComplete() {
                                    }
                                })
                                .show();
                    }
                    isToolbarVisible = true;
                }
            }
        });
    }

    void setToolbar() {
        View info = findViewById(R.id.toolbar_info);
        View tutorial = findViewById(R.id.toolbar_tutorial);
        View preset = findViewById(R.id.toolbar_preset);
        View settings = findViewById(R.id.toolbar_settings);

        assert info != null;
        info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[0] = (int) event.getRawX();
                coord[1] = (int) event.getRawY();

                return false;
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim.circularRevealinpx(R.id.placeholder,
                        coord[0], coord[1],
                        0, (int) Math.hypot(coord[0], coord[1]) + 200, new AccelerateDecelerateInterpolator(),
                        circularRevealDuration, 0, a);

                Handler about = new Handler();
                about.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        w.getView(R.id.layout_about, a).setVisibility(View.VISIBLE);
                        w.setRecentColor(R.string.about, 0, themeColor, a);
                        ab.setNav(1, null, a);
                        ab.setTitle(R.string.about, a);
                    }
                }, circularRevealDuration);

                setSettings();
                anim.fadeOut(R.id.placeholder, circularRevealDuration, fadeAnimDuration, a);

                isAboutVisible = true;
            }
        });

        assert preset != null;
        preset.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[0] = (int) event.getRawX();
                coord[1] = (int) event.getRawY();

                return false;
            }
        });

        preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim.circularRevealinpx(R.id.placeholder,
                        coord[0], coord[1],
                        0, (int) Math.hypot(coord[0], coord[1]) + 200, new AccelerateDecelerateInterpolator(),
                        circularRevealDuration, 0, a);

                Handler preset = new Handler();
                preset.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showDialogPreset();
                    }
                }, circularRevealDuration);

                isPresetVisible = true;
            }
        });

        assert tutorial != null;
        tutorial.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[0] = (int) event.getRawX();
                coord[1] = (int) event.getRawY();

                return false;
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTutorial();
            }
        });

        assert settings != null;
        settings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[2] = (int) event.getRawX();
                coord[3] = (int) event.getRawY();

                return false;
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w.setRecentColor(R.string.settings, 0, R.color.colorAccent, a);
                anim.circularRevealinpx(R.id.placeholder,
                        coord[2], coord[3],
                        0, (int) Math.hypot(coord[2], coord[3]) + 200, new AccelerateDecelerateInterpolator(),
                        circularRevealDuration, 0, a);

                Handler about = new Handler();
                about.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        w.getView(R.id.layout_settings, a).setVisibility(View.VISIBLE);
                        ab.setNav(1, null, a);
                        ab.setTitle(R.string.settings, a);
                        ab.setColor(R.color.colorAccent, a);
                    }
                }, circularRevealDuration);

                anim.fadeOut(R.id.placeholder, circularRevealDuration, fadeAnimDuration, a);

                isSettingVisible = true;
            }
        });
    }

    void closeToolbar() {
        anim.fadeOut(R.id.toolbar, 0, 100, a);
        fab.show(100, 200);
        isToolbarVisible = false;
    }

    void setAbout() {
        // artist
        w.getView(R.id.cardview_artist, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.AboutActivity",
                        R.id.cardview_artist_image, "transition", 0);
            }
        });

        w.getView(R.id.cardview_artist_explore, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.AboutActivity",
                        R.id.cardview_artist_image, "transition", 0);
            }
        });

        w.getView(R.id.cardview_artist_change, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPreset();
            }
        });

        // tapad
        w.getView(R.id.cardview_about, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.AboutActivity",
                        R.id.cardview_about_image, "transition", 0);
            }
        });

        w.getView(R.id.cardview_about_explore, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.AboutActivity",
                        R.id.cardview_about_image, "transition", 0);
            }
        });
        w.getView(R.id.cardview_about_settings, a).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[2] = (int) event.getRawX();
                coord[3] = (int) event.getRawY();

                return false;
            }
        });

        w.getView(R.id.cardview_about_settings, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w.setRecentColor(R.string.settings, 0, R.color.colorAccent, a);
                anim.circularRevealinpx(R.id.placeholder,
                        coord[2], coord[3],
                        0, (int) Math.hypot(coord[2], coord[3]) + 200, new AccelerateDecelerateInterpolator(),
                        circularRevealDuration, 0, a);

                Handler about = new Handler();
                about.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        w.getView(R.id.layout_settings, a).setVisibility(View.VISIBLE);
                        ab.setNav(1, null, a);
                        ab.setTitle(R.string.settings, a);
                        ab.setColor(R.color.colorAccent, a);
                    }
                }, circularRevealDuration);

                anim.fadeOut(R.id.placeholder, circularRevealDuration, fadeAnimDuration, a);

                isSettingVisible = true;
            }
        });

        // developer
        w.getView(R.id.cardview_dev, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.AboutActivity",
                        R.id.cardview_dev_image, "transition", 0);
            }
        });

        w.getView(R.id.cardview_dev_explore, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.AboutActivity",
                        R.id.cardview_dev_image, "transition", 0);
            }
        });
    }

    void closeAbout() {
        anim.circularRevealinpx(R.id.placeholder,
                coord[0], coord[1],
                (int) Math.hypot(coord[0], coord[1]) + 200, 0, new AccelerateDecelerateInterpolator(),
                circularRevealDuration, fadeAnimDuration, a);

        anim.fadeIn(R.id.placeholder, 0, fadeAnimDuration, "aboutOut", a);
        isAboutVisible = false;

        Handler closeAbout = new Handler();
        closeAbout.postDelayed(new Runnable() {
            @Override
            public void run() {
                setSchemeInfo();
                w.getView(R.id.layout_about, a).setVisibility(View.GONE);
            }
        }, 200);

        if (prefs.getInt(qs, 0) == 5) {
            promptPreset = new MaterialTapTargetPrompt.Builder(a)
                    .setTarget(a.findViewById(R.id.toolbar_preset))
                    .setPrimaryText(R.string.dialog_tap_target_preset_primary)
                    .setSecondaryText(R.string.dialog_tap_target_preset_secondary)
                    .setAnimationInterpolator(new FastOutSlowInInterpolator())
                    .setFocalColourFromRes(R.color.blue_500)
                    .setAutoDismiss(false)
                    .setAutoFinish(false)
                    .setCaptureTouchEventOutsidePrompt(true)
                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                        @Override
                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            if (tappedTarget) {
                                promptPreset.finish();
                                promptPreset = null;
                                prefs.edit().putInt(qs, 6).apply();
                                Log.i("sharedPrefs", "quickstart edited to 6");
                            }
                        }

                        @Override
                        public void onHidePromptComplete() {
                        }
                    })
                    .show();
        }
    }

    void showDialogColor() {
        // Color palette
        new ColorChooserDialog.Builder(this, R.string.dialog_color)
                .accentMode(false)
                .titleSub(R.string.dialog_color)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .dynamicButtonColor(true)
                .show();
    }

    void toggleTutorial() {
        // TODO add 2gb ram limit if statement
        if (w.getView(R.id.progress_bar_layout, a).getVisibility() == View.GONE) {
            // on loading finished
            if (isTutorialVisible == false) {
                new MaterialDialog.Builder(a)
                        .title(R.string.dialog_tutorial_warning_title)
                        .content(R.string.dialog_tutorial_warning_text)
                        .positiveText(R.string.dialog_tutorial_warning_positive)
                        .positiveColorRes(R.color.red_500)
                        .negativeText(R.string.dialog_tutorial_warning_negative)
                        .negativeColorRes(R.color.dark_secondary)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // TODO TUTORIAL
                                //tut.tutorialStart(a);
                                tut.initCurrentTiming();
                                tut.startTutorial(tut.getCurrentTutorialDeckId(), a);
                                isTutorialVisible = true;
                                setTutorialUI();
                                if (isSettingVisible == true) {
                                    closeSettings();
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                isTutorialVisible = false;
                                setTutorialUI();
                            }
                        })
                        .show();
            } else {
                tut.tutorialStop(a);
                isTutorialVisible = false;
                setTutorialUI();
            }
        } else {
            // still loading preset
            Toast.makeText(a, R.string.tutorial_loading, Toast.LENGTH_LONG).show();
        }
    }

    void setTutorialUI() {
        if (isTutorialVisible == true) {
            w.getImageView(R.id.toolbar_tutorial_icon, a).setImageResource(R.drawable.icon_tutorial_quit);
            w.getImageView(R.id.layout_settings_tutorial_icon, a).setImageResource(R.drawable.settings_tutorial_quit);
            w.getSwitchCompat(R.id.layout_settings_tutorial_switch, a).setChecked(true);
        } else {
            w.getImageView(R.id.toolbar_tutorial_icon, a).setImageResource(R.drawable.icon_tutorial);
            w.getImageView(R.id.layout_settings_tutorial_icon, a).setImageResource(R.drawable.settings_tutorial);
            w.getSwitchCompat(R.id.layout_settings_tutorial_switch, a).setChecked(false);
        }
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int colorInt) {
        prefs.edit().putInt("color", colorInt).apply();
        color = prefs.getInt("color", colorInt);
        setSettings();
        clearToggleButton();
    }

    void setSettings() {
        // Set circle color
        Drawable circleBackground = w.getView(R.id.layout_settings_color_circle, a).getBackground();
        ((GradientDrawable) circleBackground).setColor(prefs.getInt("color", R.color.red));

        w.getView(R.id.layout_settings_preset, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPreset();
                w.setRecentColor(R.string.task_presets, R.color.colorAccent, a);
            }
        });

        w.getView(R.id.layout_settings_color, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogColor();
            }
        });

        w.getView(R.id.layout_settings_custom_touch, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_custom_touch_error, Toast.LENGTH_SHORT).show();
            }
        });

        w.getView(R.id.layout_settings_custom_sound, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_custom_sound_error, Toast.LENGTH_SHORT).show();
            }
        });

        w.getView(R.id.layout_settings_layout, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_layout_error, Toast.LENGTH_SHORT).show();
            }
        });

        w.getView(R.id.layout_settings_tutorial, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTutorial();
            }
        });

        w.getSwitchCompat(R.id.layout_settings_tutorial_switch, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (w.getView(R.id.progress_bar_layout, a).getVisibility() == View.GONE) {
                    toggleTutorial();
                } else {
                    // still loading preset
                    Toast.makeText(a, R.string.tutorial_loading, Toast.LENGTH_LONG).show();
                    w.getSwitchCompat(R.id.layout_settings_tutorial_switch, a).toggle();
                }
            }
        });

        w.getView(R.id.layout_settings_about_tapad, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentWithExtra(a, "activity.AboutActivity",
                        "json", getResources().getString(R.string.json_about_tapad), 0);
            }
        });

        w.getView(R.id.layout_settings_about_dev, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentWithExtra(a, "activity.AboutActivity",
                        "json", getResources().getString(R.string.json_about_dev), 0);
            }
        });
    }

    void closeSettings() {
        Log.d("closeSettings", "triggered");
        anim.circularRevealinpx(R.id.placeholder,
                coord[2], coord[3],
                (int) Math.hypot(coord[2], coord[3]) + 200, 0, new AccelerateDecelerateInterpolator(),
                circularRevealDuration, fadeAnimDuration, a);

        anim.fadeIn(R.id.placeholder, 0, fadeAnimDuration, "settingOut", a);
        isSettingVisible = false;

        Handler closeSettings = new Handler();
        closeSettings.postDelayed(new Runnable() {
            @Override
            public void run() {
                setSchemeInfo();
                w.getView(R.id.layout_settings, a).setVisibility(View.GONE);
            }
        }, fadeAnimDuration);
    }

    void showDialogPreset() {
        tut.tutorialStop(a);
        sound.soundAllStop();

        int color;

        final int defaultScheme = prefs.getInt("scheme", 0);
        switch (defaultScheme) {
            case 1:
                color = R.color.hello;
                break;
            case 2:
                color = R.color.roses;
                break;
            case 3:
                color = R.color.faded;
                break;
            //TODO GESTURE
            case 4:
                color = R.color.faded;
                break;
            default:
                color = R.color.hello;
                break;
        }

        if (isPresetVisible == true) {
            anim.fade(R.id.placeholder, 1.0f, 0.5f, 0, 200, "phIN", a);
        }

        PresetDialog = new MaterialDialog.Builder(this)
                .title(R.string.dialog_preset_title)
                .items(R.array.presets)
                .autoDismiss(false)
                .itemsCallbackSingleChoice(defaultScheme - 1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                setScheme(which + 1);
                                PresetDialog.getBuilder()
                                        .widgetColorRes(R.color.hello)
                                        .positiveColorRes(R.color.hello);
                                setSchemeInfo();
                                break;
                            case 1:
                                setScheme(which + 1);
                                PresetDialog.getBuilder()
                                        .widgetColorRes(R.color.roses)
                                        .positiveColorRes(R.color.roses);
                                setSchemeInfo();
                                break;
                            case 2:
                                setScheme(which + 1);
                                PresetDialog.getBuilder()
                                        .widgetColorRes(R.color.faded)
                                        .positiveColorRes(R.color.faded);
                                setSchemeInfo();
                                break;
                            //TODO GESTURE
                            case 3:
                                setScheme(which + 1);
                                PresetDialog.getBuilder()
                                        .widgetColorRes(R.color.faded)
                                        .positiveColorRes(R.color.faded);
                                setSchemeInfo();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                })
                .alwaysCallSingleChoiceCallback()
                .widgetColorRes(color)
                .positiveText(R.string.dialog_preset_positive)
                .positiveColorRes(color)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PresetDialog.dismiss();
                        isSchemeChanged = true;
                    }
                })
                .negativeText(R.string.dialog_preset_negative)
                .negativeColorRes(R.color.dark_secondary)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PresetDialog.dismiss();
                        isSchemeChanged = false;
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (isSchemeChanged == false) {
                            setScheme(defaultScheme);
                            setSchemeInfo();
                        } else {
                            loadPreset(circularRevealDuration);
                            clearToggleButton();
                        }
                        setSchemeInfo();
                        if (isPresetVisible == true) {
                            anim.fade(R.id.placeholder, 0.5f, 1.0f, 0, 200, "phOUT", a);
                            closeDialogPreset();
                        }
                    }
                })
                .show();
    }

    void closeDialogPreset() {
        anim.circularRevealinpx(R.id.placeholder,
                coord[0], coord[1],
                (int) Math.hypot(coord[0], coord[1]) + 200, 0, new AccelerateDecelerateInterpolator(),
                circularRevealDuration, 200, a);

        isPresetVisible = false;

        if (prefs.getInt(qs, 0) == 6) {
            promptTutorial = new MaterialTapTargetPrompt.Builder(a)
                    .setTarget(a.findViewById(R.id.toolbar_tutorial))
                    .setPrimaryText(R.string.dialog_tap_target_tutorial_primary)
                    .setSecondaryText(R.string.dialog_tap_target_tutorial_secondary)
                    .setAnimationInterpolator(new FastOutSlowInInterpolator())
                    .setFocalColourFromRes(R.color.blue_500)
                    .setAutoDismiss(false)
                    .setAutoFinish(false)
                    .setCaptureTouchEventOutsidePrompt(true)
                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                        @Override
                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            if (tappedTarget) {
                                promptTutorial.finish();
                                promptTutorial = null;
                                // Removed because it disables toolbar
                                //prefs.edit().putInt(qs, 7).apply();
                                //Log.i("sharedPrefs", "quickstart edited to 7");
                                prefs.edit().putInt(qs, -1).apply();
                                Log.i("sharedPrefs", "quickstart edited to -1, completed");
                            }
                        }

                        @Override
                        public void onHidePromptComplete() {
                        }
                    })
                    .show();

        }
    }

    void loadPreset(int delay) {
        Gson gson = new Gson();
        final Preset presets[] = {
                gson.fromJson(getResources().getString(R.string.json_hello), Preset.class),
                gson.fromJson(getResources().getString(R.string.json_roses), Preset.class),
                gson.fromJson(getResources().getString(R.string.json_faded), Preset.class),
                //TODO GESTURE
                gson.fromJson(getResources().getString(R.string.json_faded_gesture), Preset.class)
        };

        //w.getProgressBar(R.id.progress_bar, a).setMax(PRESET_SOUND_COUNTS[getScheme() - 1]);

        Handler preset = new Handler();
        preset.postDelayed(new Runnable() {
            @Override
            public void run() {
                sound.loadSchemeSound(presets[getScheme() - 1], a);
            }
        }, delay);
    }

    void setToggleButton(final int color_id) {
        // 1 - 4
        w.setOnTouch(R.id.tgl1, new Runnable() {
            @Override
            public void run() {
                if (tgl1 == false) {
                    toggleSoundId = 1;
                    if (tgl5 || tgl6 || tgl7 || tgl8) {
                        sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
                    } else {
                        sound.setButtonToggle(toggleSoundId, color, a);
                    }
                    w.setViewBackgroundColor(R.id.tgl1, color_id, a);
                    w.setViewBackgroundColor(R.id.tgl2, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl3, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl4, R.color.grey, a);
                    if (tgl2 || tgl3 || tgl4) {
                        sound.playToggleButtonSound(1);
                    }
                } else {
                    w.setViewBackgroundColor(R.id.tgl1, R.color.grey, a);
                    toggleSoundId = 0;
                    sound.setButton(R.color.grey_dark, a);
                    sound.soundAllStop();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (tgl1 == false) {
                    tgl1 = true;
                    tgl2 = false;
                    tgl3 = false;
                    tgl4 = false;
                } else {
                    tgl1 = false;
                }
            }
        }, a);

        w.setOnTouch(R.id.tgl2, new Runnable() {
            @Override
            public void run() {
                if (tgl2 == false) {
                    toggleSoundId = 2;
                    if (tgl5 || tgl6 || tgl7 || tgl8) {
                        sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
                    } else {
                        sound.setButtonToggle(toggleSoundId, color, a);
                    }
                    w.setViewBackgroundColor(R.id.tgl2, color_id, a);
                    w.setViewBackgroundColor(R.id.tgl1, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl3, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl4, R.color.grey, a);
                    sound.playToggleButtonSound(2);
                } else {
                    w.setViewBackgroundColor(R.id.tgl2, R.color.grey, a);
                    toggleSoundId = 0;
                    sound.setButton(R.color.grey_dark, a);
                    sound.soundAllStop();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (tgl2 == false) {
                    tgl2 = true;
                    tgl1 = false;
                    tgl3 = false;
                    tgl4 = false;
                } else {
                    tgl2 = false;
                }
            }
        }, a);

        w.setOnTouch(R.id.tgl3, new Runnable() {
            @Override
            public void run() {
                if (tgl3 == false) {
                    toggleSoundId = 3;
                    if (tgl5 || tgl6 || tgl7 || tgl8) {
                        sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
                    } else {
                        sound.setButtonToggle(toggleSoundId, color, a);
                    }
                    w.setViewBackgroundColor(R.id.tgl3, color_id, a);
                    w.setViewBackgroundColor(R.id.tgl2, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl1, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl4, R.color.grey, a);
                    sound.playToggleButtonSound(3);
                } else {
                    w.setViewBackgroundColor(R.id.tgl3, R.color.grey, a);
                    toggleSoundId = 0;
                    sound.setButton(R.color.grey_dark, a);
                    sound.soundAllStop();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (tgl3 == false) {
                    tgl3 = true;
                    tgl2 = false;
                    tgl1 = false;
                    tgl4 = false;
                } else {
                    tgl3 = false;
                }
            }
        }, a);

        w.setOnTouch(R.id.tgl4, new Runnable() {
            @Override
            public void run() {
                if (tgl4 == false) {
                    toggleSoundId = 4;
                    if (tgl5 || tgl6 || tgl7 || tgl8) {
                        sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
                    } else {
                        sound.setButtonToggle(toggleSoundId, color, a);
                    }
                    w.setViewBackgroundColor(R.id.tgl4, color_id, a);
                    w.setViewBackgroundColor(R.id.tgl2, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl3, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl1, R.color.grey, a);
                    sound.playToggleButtonSound(4);
                } else {
                    w.setViewBackgroundColor(R.id.tgl4, R.color.grey, a);
                    toggleSoundId = 0;
                    sound.setButton(R.color.grey_dark, a);
                    sound.soundAllStop();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (tgl4 == false) {
                    tgl4 = true;
                    tgl2 = false;
                    tgl3 = false;
                    tgl1 = false;
                } else {
                    tgl4 = false;
                }
            }
        }, a);

        // 5 - 8

        w.setOnTouch(R.id.tgl5, new Runnable() {
            @Override
            public void run() {
                if (tgl5 == false) {
                    togglePatternId = 1;
                    if (tgl1 || tgl2 || tgl3 || tgl4) {
                        sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
                    }
                    w.setViewBackgroundColor(R.id.tgl5, color_id, a);
                    w.setViewBackgroundColor(R.id.tgl6, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl7, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl8, R.color.grey, a);
                } else {
                    w.setViewBackgroundColor(R.id.tgl5, R.color.grey, a);
                    togglePatternId = 0;
                    if (tgl1 || tgl2 || tgl3 || tgl4) {
                        sound.setButtonToggle(toggleSoundId, color, a);
                    }
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (tgl5 == false) {
                    tgl5 = true;
                    tgl6 = false;
                    tgl7 = false;
                    tgl8 = false;
                } else {
                    tgl5 = false;
                }
            }
        }, a);

        w.setOnTouch(R.id.tgl6, new Runnable() {
            @Override
            public void run() {
                if (tgl6 == false) {
                    togglePatternId = 2;
                    if (tgl1 || tgl2 || tgl3 || tgl4) {
                        sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
                    }
                    w.setViewBackgroundColor(R.id.tgl6, color_id, a);
                    w.setViewBackgroundColor(R.id.tgl5, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl7, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl8, R.color.grey, a);
                } else {
                    w.setViewBackgroundColor(R.id.tgl6, R.color.grey, a);
                    togglePatternId = 0;
                    if (tgl1 || tgl2 || tgl3 || tgl4) {
                        sound.setButtonToggle(toggleSoundId, color, a);
                    }
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (tgl6 == false) {
                    tgl6 = true;
                    tgl5 = false;
                    tgl7 = false;
                    tgl8 = false;
                } else {
                    tgl6 = false;
                }
            }
        }, a);

        w.setOnTouch(R.id.tgl7, new Runnable() {
            @Override
            public void run() {
                if (tgl7 == false) {
                    togglePatternId = 3;
                    if (tgl1 || tgl2 || tgl3 || tgl4) {
                        sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
                    }
                    w.setViewBackgroundColor(R.id.tgl7, color_id, a);
                    w.setViewBackgroundColor(R.id.tgl6, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl5, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl8, R.color.grey, a);
                } else {
                    w.setViewBackgroundColor(R.id.tgl7, R.color.grey, a);
                    togglePatternId = 0;
                    if (tgl1 || tgl2 || tgl3 || tgl4) {
                        sound.setButtonToggle(toggleSoundId, color, a);
                    }
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (tgl7 == false) {
                    tgl7 = true;
                    tgl6 = false;
                    tgl5 = false;
                    tgl8 = false;
                } else {
                    tgl7 = false;
                }
            }
        }, a);

        w.setOnTouch(R.id.tgl8, new Runnable() {
            @Override
            public void run() {
                if (tgl8 == false) {
                    togglePatternId = 4;
                    if (tgl1 || tgl2 || tgl3 || tgl4) {
                        sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
                    }
                    w.setViewBackgroundColor(R.id.tgl8, color_id, a);
                    w.setViewBackgroundColor(R.id.tgl6, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl7, R.color.grey, a);
                    w.setViewBackgroundColor(R.id.tgl5, R.color.grey, a);
                } else {
                    w.setViewBackgroundColor(R.id.tgl8, R.color.grey, a);
                    togglePatternId = 0;
                    if (tgl1 || tgl2 || tgl3 || tgl4) {
                        sound.setButtonToggle(toggleSoundId, color, a);
                    }
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (tgl8 == false) {
                    tgl8 = true;
                    tgl6 = false;
                    tgl7 = false;
                    tgl5 = false;
                } else {
                    tgl8 = false;
                }
            }
        }, a);
    }

    void clearToggleButton() {
        w.setViewBackgroundColor(R.id.tgl1, R.color.grey, a);
        w.setViewBackgroundColor(R.id.tgl2, R.color.grey, a);
        w.setViewBackgroundColor(R.id.tgl3, R.color.grey, a);
        w.setViewBackgroundColor(R.id.tgl4, R.color.grey, a);
        w.setViewBackgroundColor(R.id.tgl5, R.color.grey, a);
        w.setViewBackgroundColor(R.id.tgl6, R.color.grey, a);
        w.setViewBackgroundColor(R.id.tgl7, R.color.grey, a);
        w.setViewBackgroundColor(R.id.tgl8, R.color.grey, a);

        tgl1 = false;
        tgl2 = false;
        tgl3 = false;
        tgl4 = false;
        tgl5 = false;
        tgl6 = false;
        tgl7 = false;
        tgl8 = false;

        sound.setButton(R.color.grey_dark, a);
    }

    void setSchemeInfo() {
        int scheme = getScheme();
        ab.setNav(0, null, a);
        if (scheme == 1) {
            themeColor = R.color.hello;
            w.setRecentColor(0, 0, themeColor, a);

            if (isSettingVisible == false) {
                ab.setColor(themeColor, a);
                ab.setImage(R.drawable.logo_hello, a);
            } else {
                ab.setNav(1, null, a);
                ab.setTitle(R.string.settings, a);
                ab.setColor(R.color.colorAccent, a);
            }

            // Cardview
            w.getImageView(R.id.cardview_artist_image, a).setImageResource(R.drawable.cardview_background_artist_hello);
            w.getTextView(R.id.cardview_artist_song, a).setText(getResources().getString(R.string.preset_hello_full));
            w.getTextView(R.id.cardview_artist_explore, a).setTextColor(getResources().getColor(themeColor));
            w.getTextView(R.id.cardview_artist_change, a).setTextColor(getResources().getColor(themeColor));

            w.getTextView(R.id.layout_settings_preset_hint, a).setText(getResources().getString(R.string.preset_hello_full));
        } else if (scheme == 2) {
            themeColor = R.color.roses;
            w.setRecentColor(0, 0, themeColor, a);

            if (isSettingVisible == false) {
                ab.setColor(themeColor, a);
                ab.setImage(R.drawable.logo_roses, a);
            } else {
                ab.setNav(1, null, a);
                ab.setTitle(R.string.settings, a);
                ab.setColor(R.color.colorAccent, a);
            }

            // Cardview
            w.getImageView(R.id.cardview_artist_image, a).setImageResource(R.drawable.cardview_background_artist_roses);
            w.getTextView(R.id.cardview_artist_song, a).setText(getResources().getString(R.string.preset_roses_full));
            w.getTextView(R.id.cardview_artist_explore, a).setTextColor(getResources().getColor(themeColor));
            w.getTextView(R.id.cardview_artist_change, a).setTextColor(getResources().getColor(themeColor));

            w.getTextView(R.id.layout_settings_preset_hint, a).setText(getResources().getString(R.string.preset_roses_full));
        } else if (scheme == 3 || scheme == 4) { //TODO GESTURE
            themeColor = R.color.faded;
            w.setRecentColor(0, 0, themeColor, a);

            if (isSettingVisible == false) {
                ab.setColor(themeColor, a);
                ab.setImage(R.drawable.logo_faded, a);
            } else {
                ab.setNav(1, null, a);
                ab.setTitle(R.string.settings, a);
                ab.setColor(R.color.colorAccent, a);
            }

            // Cardview
            w.getImageView(R.id.cardview_artist_image, a).setImageResource(R.drawable.cardview_background_artist_faded);
            w.getTextView(R.id.cardview_artist_song, a).setText(getResources().getString(R.string.preset_faded_full));
            w.getTextView(R.id.cardview_artist_explore, a).setTextColor(getResources().getColor(themeColor));
            w.getTextView(R.id.cardview_artist_change, a).setTextColor(getResources().getColor(themeColor));

            w.getTextView(R.id.layout_settings_preset_hint, a).setText(getResources().getString(R.string.preset_faded_full));
        }
    }

    int getScheme() {
        return prefs.getInt("scheme", 1);
    }

    void setScheme(int scheme) {
        prefs.edit().putInt("scheme", scheme).apply();
    }

    void makeJson() {
//        //Default json creator
//        Item items[] = {
//                new Item("item1Text", "item1Hint", "item1ImageResId"),
//                new Item("item2Text", "item2Hint", "item2ImageResId")
//        };
//
//        Detail details[] = {
//                new Detail("detail1Title", items),
//                new Detail("detail2Title", items)
//        };
//
//        About about = new About("title", "imageResId",
//                new Bio("bioTitle", "bioImageResId", "bioName", "bioText", "bioSource"), details,
//                "statusColorResId", "actionColorResId");

        //TODO use this for new presets
//        String[] social = getResources().getStringArray(R.array.roses_social);
//        String[] bio = getResources().getStringArray(R.array.roses_bio);
//
//        Item items[] = {
//                new Item("Facebook"  , social[0], "about_facebook"),
//                new Item("Twitter"   , social[1], "about_twitter"),
//                new Item("SoundCloud", social[2], "about_soundcloud"),
//                new Item("YouTube"   , social[3], "about_youtube"),
//                new Item("Webpage"   , social[4], "about_web")
//        };
//
//        Detail details[] = {
//                new Detail("About " + bio[0], items)
//        };
//
//        About about = new About(bio[0], "cardview_background_artist_roses",
//                new Bio(bio[2], "about_bio_roses", bio[3], bio[4], bio[5]), details,
//                "roses_dark", "roses");
//
//        Log.d("JSON TEST", json);
        Item fadedItems[] = {
                new Item("facebook", "preset_faded_detail_facebook"),
                new Item("twitter", "preset_faded_detail_twitter"),
                new Item("soundcloud", "preset_faded_detail_soundcloud"),
                new Item("youtube", "preset_faded_detail_youtube"),
                new Item("web", "preset_faded_detail_web"),
        };

        Detail fadedDetail = new Detail("preset_faded_detail_title", fadedItems);

        Item fadedSongItems[] = {
                new Item("soundcloud", "preset_faded_song_detail_soundcloud", false),
                new Item("youtube", "preset_faded_song_detail_youtube", false),
                new Item("spotify", "preset_faded_song_detail_spotify", false),
                new Item("google_play_music", "preset_faded_song_detail_google_play_music", false),
                new Item("apple", "preset_faded_song_detail_apple", false),
                new Item("amazon", "preset_faded_song_detail_amazon", false),
        };

        Detail fadedSongDetail = new Detail("preset_faded_song_detail_title", fadedSongItems);

        Bio fadedBio = new Bio(
                "preset_faded_bio_title",
                "about_bio_faded",
                "preset_faded_bio_name",
                "preset_faded_bio_text",
                "preset_faded_bio_source"
        );

        Detail fadedDetails[] = {
                fadedDetail,
                fadedSongDetail
        };

        About fadedAbout = new About(
                "preset_faded_title", "about_album_faded",
                fadedBio, fadedDetails,
                "preset_faded_color_dark", "preset_faded_color"
        );

//        Pad part1[] = {
//                new Pad("ft1_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("ft1_11", "ft1_11_1", "ft1_11_2", "ft1_11_3", "ft1_11_4"),
//                new Pad("ft1_12", "ft1_12_1", "ft1_12_2", "ft1_12_3", "ft1_12_4"),
//                new Pad("ft1_13", "ft1_13_1", "ft1_13_2", "ft1_13_3", "ft1_13_4"),
//                new Pad("ft1_14", "ft1_14_1", "ft1_14_2", "ft1_14_3", "ft1_14_4"),
//                new Pad("ft1_21", "ft1_21_1", "ft1_21_2", "ft1_21_3", "ft1_21_4"),
//                new Pad("ft1_22", "ft1_22_1", "ft1_22_2", "ft1_22_3", "ft1_22_4"),
//                new Pad("ft1_23", "ft1_23_1", "ft1_23_2", "ft1_23_3", "ft1_23_4"),
//                new Pad("ft1_24", "ft1_24_1", "ft1_24_2", "ft1_24_3", "ft1_24_4"),
//                new Pad("ft1_31", "ft1_31_1", "ft1_31_2", "ft1_31_3", "ft1_31_4"),
//                new Pad("ft1_32", "ft1_32_1", "ft1_32_2", "ft1_32_3", "ft1_32_4"),
//                new Pad("ft1_33", "ft1_33_1", "ft1_33_2", "ft1_33_3", "ft1_33_4"),
//                new Pad("ft1_34", "ft1_34_1", "ft1_34_2", "ft1_34_3", "ft1_34_4"),
//                new Pad("ft1_41"),
//                new Pad("ft1_42", "ft1_42_1", "ft1_42_2", "ft1_42_3", "ft1_42_4"),
//                new Pad("ft1_43", "ft1_43_1", "ft1_43_2", "ft1_43_3"),
//                new Pad("ft1_44")
//        };
//
//        Pad part2[] = {
//                new Pad("ft2_00", "ft2_00_1", "ft2_00_3"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("ft2_11",
//                        "ft2_11_1", "ft2_11_2", "ft2_11_3", "ft2_11_4"),
//                new Pad("ft2_12", "ft2_12_1",
//                        "ft2_12_2",
//                        "ft2_12_3",
//                        "ft2_12_4"),
//                new Pad("ft2_13",
//                        "ft2_13_1",
//                        "ft2_13_2",
//                        "ft2_13_3",
//                        "ft2_13_4"),
//                new Pad("ft2_14",
//                        "ft2_14_1",
//                        "ft2_14_2",
//                        "ft2_14_3",
//                        "ft2_14_4"),
//                new Pad("ft2_21",
//                        "ft2_21_1",
//                        "ft2_21_2",
//                        "ft2_21_3",
//                        "ft2_21_4"),
//                new Pad("ft2_22",
//                        "ft2_22_1",
//                        "ft2_22_2",
//                        "ft2_22_3",
//                        "ft2_22_4"),
//                new Pad("ft2_23",
//                        "ft2_23_1",
//                        "ft2_23_2",
//                        "ft2_23_3",
//                        "ft2_23_4"),
//                new Pad("ft2_24",
//                        "ft2_24_1",
//                        "ft2_24_2",
//                        "ft2_24_3",
//                        "ft2_24_4"),
//                new Pad("ft2_31",
//                        "ft2_31_1",
//                        "ft2_31_2",
//                        "ft2_31_3",
//                        "ft2_31_4"),
//                new Pad("ft2_32",
//                        "ft2_32_1",
//                        "ft2_32_2",
//                        "ft2_32_3",
//                        "ft2_32_4"),
//                new Pad("ft2_33",
//                        "ft2_33_1",
//                        "ft2_33_2",
//                        "ft2_33_3",
//                        "ft2_33_4"),
//                new Pad("ft2_34",
//                        "ft2_34_1",
//                        "ft2_34_2",
//                        "ft2_34_3",
//                        "ft2_34_4"),
//                new Pad("ft2_41",
//                        "ft2_41_1",
//                        "ft2_41_2",
//                        "ft2_41_3",
//                        "ft2_41_4"),
//                new Pad("ft2_42",
//                        "ft2_42_1",
//                        "ft2_42_2",
//                        "ft2_42_3",
//                        "ft2_42_4"),
//                new Pad("ft2_43",
//                        "ft2_43_1",
//                        "ft2_43_2",
//                        "ft2_43_3",
//                        "ft2_43_4"),
//                new Pad("ft2_44",
//                        "ft2_44_1",
//                        "ft2_44_2",
//                        "ft2_44_3",
//                        "ft2_44_4")
//        };
//
//        Pad part3[] = {
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("ft3_11",
//                        "ft3_11_1",
//                        "ft3_11_2",
//                        "ft3_11_3",
//                        "ft3_11_4"),
//                new Pad("ft3_12",
//                        "ft3_12_1",
//                        "ft3_12_2",
//                        "ft3_12_3",
//                        "ft3_12_4"),
//                new Pad("ft3_13",
//                        "ft3_13_1",
//                        "ft3_13_2",
//                        "ft3_13_3",
//                        "ft3_13_4"),
//                new Pad("ft3_14",
//                        "ft3_14_1",
//                        "ft3_14_2",
//                        "ft3_14_3",
//                        "ft3_14_4"),
//                new Pad("ft3_21",
//                        "ft3_21_1",
//                        "ft3_21_2",
//                        "ft3_21_3",
//                        "ft3_21_4"),
//                new Pad("ft3_22",
//                        "ft3_22_1",
//                        "ft3_22_2",
//                        "ft3_22_3",
//                        "ft3_22_4"),
//                new Pad("ft3_23",
//                        "ft3_23_1",
//                        "ft3_23_2",
//                        "ft3_23_3",
//                        "ft3_23_4"),
//                new Pad("ft3_24",
//                        "ft3_24_1",
//                        "ft3_24_2",
//                        "ft3_24_3",
//                        "ft3_24_4"),
//                new Pad("ft3_31"),
//                new Pad("ft3_32",
//                        "ft3_32_1",
//                        "ft3_32_2",
//                        "ft3_32_3",
//                        "ft3_32_4"),
//                new Pad("ft3_33",
//                        "ft3_33_1",
//                        "ft3_33_2",
//                        "ft3_33_3",
//                        "ft3_33_4"),
//                new Pad("ft3_34",
//                        "ft3_34_1",
//                        "ft3_34_2",
//                        "ft3_34_3",
//                        "ft3_34_4"),
//                new Pad("ft3_41",
//                        "ft3_41_1",
//                        "ft3_41_3"),
//                new Pad("ft3_42",
//                        "ft3_42_1",
//                        "ft3_42_2",
//                        "ft3_42_3",
//                        "ft3_42_4"),
//                new Pad("ft3_43",
//                        "ft3_43_1",
//                        "ft3_43_2",
//                        "ft3_43_3",
//                        "ft3_43_4"),
//                new Pad("ft3_44",
//                        "ft3_44_1",
//                        "ft3_44_2",
//                        "ft3_44_3",
//                        "ft3_44_4")
//        };
//
//        Pad part4[] = {
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("a0_00"),
//                new Pad("ft4_11"),
//                new Pad("ft4_12"),
//                new Pad("ft4_13"),
//                new Pad("ft4_14"),
//                new Pad("ft4_21"),
//                new Pad("ft4_22"),
//                new Pad("ft4_23"),
//                new Pad("ft4_24"),
//                new Pad("ft4_31"),
//                new Pad("ft4_32"),
//                new Pad("ft4_33"),
//                new Pad("ft4_34"),
//                new Pad("ft4_41"),
//                new Pad("ft4_42"),
//                new Pad("ft4_43"),
//                new Pad("ft4_44")
//        };

        // Timings
        Integer pt1[][] = {
                {42660},
                {null},
                {null},
                {null},
                {null},
                {26658},
                {0},
                {1333},
                {2666},
                {3998},
                {27991},
                {10664, 21327},
                {11996, 22660},
                {13329, 23993},
                {14662, 25326},
                {29324},
                {15995},
                {17328},
                {18660},
                {19993},
                {30657},
                {5331},
                {6664},
                {7997},
                {9330},
                {42660},
                {34656},
                {37322},
                {45320},
                {47986},
                {47986},
                {35989},
                {38655},
                {46653},
                {49318},
                {50651},
                {36655},
                {39321},
                {47319},
                {49984},
                {45320},
                {35322},
                {37988},
                {45986},
                {48652},
                {31991},
                {31991},
                {39988},
                {42660},
                {50651},
                {37322},
                {33324},
                {41320},
                {43987},
                {51984},
                {39988},
                {33990},
                {41987},
                {44653},
                {52650},
                {34656},
                {32657},
                {40654},
                {43320},
                {51317},
                {43987},
                {null},
                {null},
                {null},
                {null},
                {10664},
                {10664},
                {15995},
                {21327},
                {26658},
                {34656},
                {34656},
                {39988},
                {45320},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
        };

        Integer pt2[][] = {
                {54660},
                {53318},
                {null},
                {75993},
                {null},
                {75993},
                {70660},
                {54660},
                {59993},
                {65326},
                {78659},
                {71326},
                {55327},
                {60660},
                {65993},
                {81325},
                {71993},
                {55994},
                {61327},
                {66660},
                {83992},
                {72660},
                {56660},
                {61993},
                {67326},
                {86659},
                {73326},
                {57327},
                {62660},
                {67993},
                {89325},
                {73993},
                {57993},
                {63327},
                {68660},
                {91992},
                {74660},
                {58660},
                {63993},
                {69326},
                {94658},
                {75326},
                {59324},
                {64659},
                {69991},
                {87992},
                {57662},
                {73660},
                {68329},
                {62994},
                {90658},
                {58329},
                {74327},
                {68995},
                {63660},
                {93325},
                {58995},
                {74996},
                {69661},
                {64330},
                {95992},
                {59662},
                {75663},
                {70328},
                {64997},
                {77326},
                {54997},
                {70994},
                {65663},
                {60328},
                {79993},
                {55664},
                {71661},
                {66330},
                {60995},
                {82659},
                {56330},
                {72327},
                {66996},
                {61661},
                {85326},
                {56996},
                {72994},
                {67663},
                {62328}
        };

        Integer pt3[][] = {
                {null},
                {null},
                {null},
                {null},
                {null},
                {139993},
                {97325},
                {107994},
                {118660},
                {129327},
                {142659},
                {99991},
                {110660},
                {121326},
                {131993},
                {143992},
                {101325},
                {111994},
                {122660},
                {133326},
                {141326},
                {98658},
                {109327},
                {119993},
                {130659},
                {145326},
                {102658},
                {113327},
                {123993},
                {134659},
                {147992},
                {105325},
                {115994},
                {126660},
                {137326},
                {149326},
                {106658},
                {117327},
                {127993},
                {138659},
                {146659},
                {103991},
                {114660},
                {125326},
                {135993},
                {null},
                {null},
                {null},
                {null},
                {null},
                {139993},
                {97325},
                {107994},
                {118660},
                {129327},
                {142659},
                {99991},
                {110660},
                {121326},
                {131993},
                {97325},
                {102658},
                {107994},
                {113327},
                {118660},
                {129327, 130657, 131997, 133327, 134657, 135987, 137327, 138657},
                {139997, 141327, 142667, 143987},
                {145327, 146657, 147997, 149327},
                {null},
                {null},
                {145326},
                {102658},
                {113327},
                {123993},
                {134659},
                {147992},
                {105325},
                {115994},
                {126660},
                {137326},
                {123993},
                {129327},
                {134659},
                {139993},
                {145326}
        };

        Integer pt4[][] = {
                {null},
                {150650},
                {null},
                {173325},
                {null},
                {173325},
                {167992},
                {151992},
                {157325},
                {162658},
                {175991},
                {168658},
                {152659},
                {157992},
                {163325},
                {178658},
                {169325},
                {153326},
                {158659},
                {163992},
                {181324},
                {169992},
                {153992},
                {159326},
                {164658},
                {183991},
                {170658},
                {154659},
                {159992},
                {165325},
                {186657},
                {171325},
                {155326},
                {160659},
                {165992},
                {189324},
                {171992},
                {155992},
                {161326},
                {166658},
                {191990},
                {172658},
                {156657},
                {161992},
                {167323},
                {185324},
                {154994},
                {170992},
                {165661},
                {160326},
                {187990},
                {155661},
                {171659},
                {166327},
                {160992},
                {190657},
                {156327},
                {172328},
                {166993},
                {161662},
                {193324},
                {156994},
                {172995},
                {167660},
                {162329},
                {174658},
                {152329},
                {168326},
                {162995},
                {157660},
                {177325},
                {152996},
                {168993},
                {163662},
                {158327},
                {179991},
                {153662},
                {169659},
                {164328},
                {158993},
                {182658},
                {154328},
                {170326},
                {164995},
                {159660}
        };

        Integer pt5[][] = {
                {null},
                {null},
                {null},
                {null},
                {null},
                {194637},
                {null},
                {null},
                {null},
                {null},
                {195993},
                {null},
                {null},
                {null},
                {null},
                {195326},
                {null},
                {null},
                {null},
                {null},
                {196660},
                {null},
                {null},
                {null},
                {null},
                {197326},
                {null},
                {null},
                {null},
                {null},
                {198660},
                {null},
                {null},
                {null},
                {null},
                {197993},
                {null},
                {null},
                {null},
                {null},
                {199324},
                {null},
                {null},
                {null},
                {null},
                {199993},
                {null},
                {null},
                {null},
                {null},
                {201340},
                {null},
                {null},
                {null},
                {null},
                {200682},
                {null},
                {null},
                {null},
                {null},
                {202128},
                {null},
                {null},
                {null},
                {null},
                {203147},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
        };

        DeckTiming deckTiming[] = {
                new DeckTiming(
                        pt1, 1, 0
                ),
                new DeckTiming(
                        pt2, 2, 52949
                ),
                new DeckTiming(
                        pt3, 3, 96379
                ),
                new DeckTiming(
                        pt4, 2, 149947
                ),
                new DeckTiming(
                        pt5, 4, 193772
                )
        };

        //TODO EDIT
        Log.d("Array", Arrays.deepToString(deckTiming[0].getDeckTiming()));

        Music fadedMusic = new Music(
                "preset_faded",
                getDeckFromFileName(fileTag),
                deckTiming
        );

        Preset fadedPreset = new Preset(4, fadedMusic, fadedAbout);

        //String json = gson.toJson(preset, Preset.class);
        Gson gson = new Gson();
        largeLog("JSON", gson.toJson(fadedPreset));
    }

    String fileTag = "ft";

    Deck[] getDeckFromFileName(String fileTag) {
        Pad part1[] = {
                getPadsFromFile(fileTag, 0, 0),
                getPadsFromFile(fileTag, 0, 1),
                getPadsFromFile(fileTag, 0, 2),
                getPadsFromFile(fileTag, 0, 3),
                getPadsFromFile(fileTag, 0, 4),
                getPadsFromFile(fileTag, 0, 5),
                getPadsFromFile(fileTag, 0, 6),
                getPadsFromFile(fileTag, 0, 7),
                getPadsFromFile(fileTag, 0, 8),
                getPadsFromFile(fileTag, 0, 9),
                getPadsFromFile(fileTag, 0, 10),
                getPadsFromFile(fileTag, 0, 11),
                getPadsFromFile(fileTag, 0, 12),
                getPadsFromFile(fileTag, 0, 13),
                getPadsFromFile(fileTag, 0, 14),
                getPadsFromFile(fileTag, 0, 15),
                getPadsFromFile(fileTag, 0, 16),
                getPadsFromFile(fileTag, 0, 17),
                getPadsFromFile(fileTag, 0, 18),
                getPadsFromFile(fileTag, 0, 19),
                getPadsFromFile(fileTag, 0, 20)
        };
        Pad part2[] = {
                getPadsFromFile(fileTag, 1, 0),
                getPadsFromFile(fileTag, 1, 1),
                getPadsFromFile(fileTag, 1, 2),
                getPadsFromFile(fileTag, 1, 3),
                getPadsFromFile(fileTag, 1, 4),
                getPadsFromFile(fileTag, 1, 5),
                getPadsFromFile(fileTag, 1, 6),
                getPadsFromFile(fileTag, 1, 7),
                getPadsFromFile(fileTag, 1, 8),
                getPadsFromFile(fileTag, 1, 9),
                getPadsFromFile(fileTag, 1, 10),
                getPadsFromFile(fileTag, 1, 11),
                getPadsFromFile(fileTag, 1, 12),
                getPadsFromFile(fileTag, 1, 13),
                getPadsFromFile(fileTag, 1, 14),
                getPadsFromFile(fileTag, 1, 15),
                getPadsFromFile(fileTag, 1, 16),
                getPadsFromFile(fileTag, 1, 17),
                getPadsFromFile(fileTag, 1, 18),
                getPadsFromFile(fileTag, 1, 19),
                getPadsFromFile(fileTag, 1, 20)
        };
        Pad part3[] = {
                getPadsFromFile(fileTag, 2, 0),
                getPadsFromFile(fileTag, 2, 1),
                getPadsFromFile(fileTag, 2, 2),
                getPadsFromFile(fileTag, 2, 3),
                getPadsFromFile(fileTag, 2, 4),
                getPadsFromFile(fileTag, 2, 5),
                getPadsFromFile(fileTag, 2, 6),
                getPadsFromFile(fileTag, 2, 7),
                getPadsFromFile(fileTag, 2, 8),
                getPadsFromFile(fileTag, 2, 9),
                getPadsFromFile(fileTag, 2, 10),
                getPadsFromFile(fileTag, 2, 11),
                getPadsFromFile(fileTag, 2, 12),
                getPadsFromFile(fileTag, 2, 13),
                getPadsFromFile(fileTag, 2, 14),
                getPadsFromFile(fileTag, 2, 15),
                getPadsFromFile(fileTag, 2, 16),
                getPadsFromFile(fileTag, 2, 17),
                getPadsFromFile(fileTag, 2, 18),
                getPadsFromFile(fileTag, 2, 19),
                getPadsFromFile(fileTag, 2, 20)
        };
        Pad part4[] = {
                getPadsFromFile(fileTag, 3, 0),
                getPadsFromFile(fileTag, 3, 1),
                getPadsFromFile(fileTag, 3, 2),
                getPadsFromFile(fileTag, 3, 3),
                getPadsFromFile(fileTag, 3, 4),
                getPadsFromFile(fileTag, 3, 5),
                getPadsFromFile(fileTag, 3, 6),
                getPadsFromFile(fileTag, 3, 7),
                getPadsFromFile(fileTag, 3, 8),
                getPadsFromFile(fileTag, 3, 9),
                getPadsFromFile(fileTag, 3, 10),
                getPadsFromFile(fileTag, 3, 11),
                getPadsFromFile(fileTag, 3, 12),
                getPadsFromFile(fileTag, 3, 13),
                getPadsFromFile(fileTag, 3, 14),
                getPadsFromFile(fileTag, 3, 15),
                getPadsFromFile(fileTag, 3, 16),
                getPadsFromFile(fileTag, 3, 17),
                getPadsFromFile(fileTag, 3, 18),
                getPadsFromFile(fileTag, 3, 19),
                getPadsFromFile(fileTag, 3, 20)
        };

        return new Deck[]{new Deck(part1), new Deck(part2), new Deck(part3), new Deck(part4)};
    }

    String getPadStringFromId(int padId) {
        switch (padId) {
            case 0:
                return "00";
            case 1:
                return "01";
            case 2:
                return "02";
            case 3:
                return "03";
            case 4:
                return "04";
            case 5:
                return "11";
            case 6:
                return "12";
            case 7:
                return "13";
            case 8:
                return "14";
            case 9:
                return "21";
            case 10:
                return "22";
            case 11:
                return "23";
            case 12:
                return "24";
            case 13:
                return "31";
            case 14:
                return "32";
            case 15:
                return "33";
            case 16:
                return "34";
            case 17:
                return "41";
            case 18:
                return "42";
            case 19:
                return "43";
            case 20:
                return "44";
            default:
                return null;
        }
    }

    Pad getPadsFromFile(String fileTag, int deck, int pad) {
        if (validateFileName(
                fileTag,
                Integer.toString(deck + 1),
                getPadStringFromId(pad),
                Integer.toString(0)
        ) == null) {
            // the pad is empty from the first gesture == empty
            return new Pad("a0_00");
        } else {
            String fileNameArray[] = {
                    validateFileName(
                            fileTag,
                            Integer.toString(deck + 1),
                            getPadStringFromId(pad),
                            Integer.toString(0)
                    ),
                    validateFileName(
                            fileTag,
                            Integer.toString(deck + 1),
                            getPadStringFromId(pad),
                            Integer.toString(1)
                    ),
                    validateFileName(
                            fileTag,
                            Integer.toString(deck + 1),
                            getPadStringFromId(pad),
                            Integer.toString(2)
                    ),
                    validateFileName(
                            fileTag,
                            Integer.toString(deck + 1),
                            getPadStringFromId(pad),
                            Integer.toString(3)
                    ),
                    validateFileName(
                            fileTag,
                            Integer.toString(deck + 1),
                            getPadStringFromId(pad),
                            Integer.toString(4)
                    )
            };
            return getPadFromStringArray(fileNameArray);
        }
    }

    Pad getPadFromStringArray(String fileName[]) {
        ArrayList<String> stringArray = new ArrayList<>();
        for (int i = 0; i < fileName.length; i++) {
            if (fileName[i] != null) {
                stringArray.add(fileName[i]);
            }
        }

        String padStringArray[] = stringArray.toArray(new String[stringArray.size()]);

        switch (padStringArray.length) {
            case 1:
                return new Pad(padStringArray[0]);
            case 2:
                return new Pad(padStringArray[0], padStringArray[1]);
            case 3:
                return new Pad(padStringArray[0], padStringArray[1], padStringArray[2]);
            case 4:
                return new Pad(padStringArray[0], padStringArray[1], padStringArray[2], padStringArray[3]);
            case 5:
                return new Pad(padStringArray[0], padStringArray[1], padStringArray[2], padStringArray[3], padStringArray[4]);
            default:
                Log.d(TAG, "getPadFromStringArray : null array");
                return null;
        }
    }

    String validateFileName(String fileTag, String realPart, String realPad, String realGesture) {
        String fileName;
        if (realGesture.equals("0")) {
            fileName = fileTag + realPart + "_" + realPad;
        } else {
            fileName = fileTag + realPart + "_" + realPad + "_" + realGesture;
        }
        try {
            Class res = R.raw.class;
            Field field = res.getField(fileName);
            // legit
            if (field != null) {
                return fileName;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("getColorId", "Failure to get raw id.", e);
            // fail
            return null;
        }
    }
}