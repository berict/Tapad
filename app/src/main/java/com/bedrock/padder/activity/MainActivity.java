package com.bedrock.padder.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.gson.Gson;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

@TargetApi(9)
@SuppressWarnings("deprecation")

public class MainActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    private AnimService anim = new AnimService();
    private ThemeService t = new ThemeService();
    private SoundService sound = new SoundService();
    private IntentService intent = new IntentService();
    private WindowService w = new WindowService();
    private FabService fab = new FabService();
    private AppbarService ab = new AppbarService();
    private TutorialService tut = new TutorialService();

    float volume;

    final Activity a = this;
    final String qs = "quickstart";
    SharedPreferences prefs = null;
    int currentVersionCode;
    int themeColor = R.color.hello;
    int color = R.color.red;

    private boolean isToolbarVisible = false;
    private boolean isAboutVisible = false;
    private boolean isPresetVisible = false;
    public boolean isPresetLoading = false;
    private boolean isSettingVisible = false;
    private boolean isTutorialVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        // for test
        //prefs.edit().putInt(qs, 0).apply();

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

        //Set transparent nav bar
        w.setStatusBar(R.color.transparent, a);
        w.setNavigationBar(R.color.transparent, a);

        w.setMarginRelativePX(R.id.fab, 0, 0, w.convertDPtoPX(20, a), prefs.getInt("navBarPX", 0) + w.convertDPtoPX(20, a), a);
        w.setMarginRelativePX(R.id.toolbar, 0, 0, 0, prefs.getInt("navBarPX", 0), a);
        //ab.setStatusHeight(a);

        color = prefs.getInt("color", R.color.red);
        sound.setButton(R.color.grey_dark, a);

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

        //TODO Remove this
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
//        Gson gson = new Gson();
//        String json = gson.toJson(about, About.class);
//
//        Log.d("JSON TEST", json);
    }

    void enterAnim() {
        anim.fadeIn(R.id.actionbar_layout, 0, 200, "background", a);
        anim.fadeIn(R.id.actionbar_image, 200, 200, "image", a);
        //TODO: Remove this to load preset
        //loadPreset(400);
        isPresetLoading = true;
    }

    boolean doubleBackToExitPressedOnce = false;

    private int circularRevealDuration = 400;
    private int fadeAnimDuration = 200;

    @Override
    public void onBackPressed() {
        if (isToolbarVisible == true) {
            if ((prefs.getInt(qs, 0) != -1) && isAboutVisible == false) {
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
        if (isTutorialVisible == true) {
            tut.tutorialStop(a);
        }
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

    private MaterialTapTargetPrompt promptToggle;   // 1
    private MaterialTapTargetPrompt promptButton;   // 2
    private MaterialTapTargetPrompt promptPattern;  // 3
    private MaterialTapTargetPrompt promptFab;      // 4
    private MaterialTapTargetPrompt promptInfo;     // 5
    private MaterialTapTargetPrompt promptPreset;   // 6
    private MaterialTapTargetPrompt promptTutorial; // 7

    MaterialDialog ChangelogDialog;
    MaterialDialog QuickstartDialog;
    MaterialDialog PresetDialog;

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

    // Used for circularReveal
    // End two is for settings coordination
    private int coord[] = {0, 0, 0, 0};

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
                //TODO: REMOVE THIS, THIS IS A TEST

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

        w.getView(R.id.cardview_artist_change, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPreset();
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
                if (w.getView(R.id.progress_bar_layout, a).getVisibility() == View.GONE) {
                    toggleTutorial();
                } else {
                    Toast.makeText(a, R.string.tutorial_loading, Toast.LENGTH_LONG).show();
                }
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
        w.getView(R.id.cardview_artist, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_artist_image, "transition",
                        "json", getResources().getStringArray(R.array.json_about)[getScheme() - 1],0);
            }
        });

        w.getView(R.id.cardview_artist_explore, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.AboutArtistActivity", R.id.cardview_artist_image, "artist", 0);
            }
        });

        w.getView(R.id.cardview_about, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.about.AboutTapadActivity", R.id.cardview_about_image, "tapad", 0);
            }
        });

        w.getView(R.id.cardview_about_explore, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.about.AboutTapadActivity", R.id.cardview_about_image, "tapad", 0);
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

        w.getView(R.id.cardview_dev, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.about.AboutDevActivity", R.id.cardview_dev_image, "dev", 0);
            }
        });

        w.getView(R.id.cardview_dev_explore, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.about.AboutDevActivity", R.id.cardview_dev_image, "dev", 0);
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
        if (isTutorialVisible == false) {
            if (w.getDeviceRam() > 2048) {
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
                                tut.tutorialStart(a);
                                w.getImageView(R.id.toolbar_tutorial_icon, a).setImageResource(R.drawable.icon_tutorial_quit);
                                w.getImageView(R.id.layout_settings_tutorial_icon, a).setImageResource(R.drawable.settings_tutorial_quit);
                                isPresetLoading = true;
                                isTutorialVisible = true;
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        })
                        .show();
            } else {
                new MaterialDialog.Builder(a)
                        .title(R.string.dialog_tutorial_warning_title)
                        .content(R.string.dialog_tutorial_warning_text_low_ram)
                        .positiveText(R.string.dialog_tutorial_warning_positive)
                        .positiveColorRes(R.color.red_500)
                        .negativeText(R.string.dialog_tutorial_warning_negative)
                        .negativeColorRes(R.color.dark_secondary)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                tut.tutorialStart(a);
                                w.getImageView(R.id.toolbar_tutorial_icon, a).setImageResource(R.drawable.icon_tutorial_quit);
                                w.getImageView(R.id.layout_settings_tutorial_icon, a).setImageResource(R.drawable.settings_tutorial_quit);
                                isPresetLoading = true;
                                isTutorialVisible = true;
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        })
                        .show();
            }
        } else {
            tut.tutorialStop(a);
            w.getImageView(R.id.toolbar_tutorial_icon, a).setImageResource(R.drawable.icon_tutorial);
            w.getImageView(R.id.layout_settings_tutorial_icon, a).setImageResource(R.drawable.settings_tutorial);
            isTutorialVisible = false;
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

        //final SwitchCompat switchCompat = (SwitchCompat)findViewById(R.id.layout_settings_tutorial_switch);

        w.getView(R.id.layout_settings_tutorial, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTutorial();
                closeSettings();
            }
        });

        w.getView(R.id.layout_settings_about_tapad, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intent(a, "activity.about.AboutTapadActivity", 0);
            }
        });

        w.getView(R.id.layout_settings_about_dev, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intent(a, "activity.about.AboutDevActivity", 0);
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

    boolean isSchemeChanged = false;

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
            default:
                color = R.color.hello;
                break;
        }

        if (isPresetVisible == true) {
            anim.fade(R.id.placeholder, 1.0f, 0.5f, 0, 200, "phIN", a);
        }

        PresetDialog = new MaterialDialog.Builder(this)
                .title(R.string.dialog_preset_title)
                .items(R.array.indigo) //TODO edit this
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
            // Tutorial removed
            /*
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
                                prefs.edit().putInt(qs, 7).apply();
                                Log.i("sharedPrefs", "quickstart edited to 7");
                            }
                        }

                        @Override
                        public void onHidePromptComplete() {}
                    })
                    .show();
            */
            prefs.edit().putInt(qs, -1).apply();
            Log.i("sharedPrefs", "quickstart edited to -1, completed");
        }
    }

    void loadPreset(int delay) {
        //sound.setSchemeSoundAsync(delay, progressBar, a);
        Handler preset = new Handler();
        preset.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (w.getView(R.id.progress_bar_layout, a).getVisibility() == View.GONE) {
                    anim.fadeIn(R.id.progress_bar_layout, 0, 400, "progressIn", a);
                }
            }
        }, delay);

        preset.postDelayed(new Runnable() {
            @Override
            public void run() {
                sound.loadSchemeSound(a);
            }
        }, delay + 400);
    }

    public boolean tgl1 = false;
    public boolean tgl2 = false;
    public boolean tgl3 = false;
    public boolean tgl4 = false;
    public boolean tgl5 = false;
    public boolean tgl6 = false;
    public boolean tgl7 = false;
    public boolean tgl8 = false;

    int toggleSoundId = 0;
    int togglePatternId = 0;

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
            w.getTextView(R.id.cardview_artist_song, a).setText(getResources().getString(R.string.hello));
            w.getTextView(R.id.cardview_artist_explore, a).setTextColor(getResources().getColor(themeColor));
            w.getTextView(R.id.cardview_artist_change, a).setTextColor(getResources().getColor(themeColor));

            w.getTextView(R.id.layout_settings_preset_hint, a).setText(getResources().getString(R.string.hello));
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
            w.getTextView(R.id.cardview_artist_song, a).setText(getResources().getString(R.string.roses));
            w.getTextView(R.id.cardview_artist_explore, a).setTextColor(getResources().getColor(themeColor));
            w.getTextView(R.id.cardview_artist_change, a).setTextColor(getResources().getColor(themeColor));

            w.getTextView(R.id.layout_settings_preset_hint, a).setText(getResources().getString(R.string.roses));
        } else if (scheme == 3) {
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
            w.getTextView(R.id.cardview_artist_song, a).setText(getResources().getString(R.string.faded));
            w.getTextView(R.id.cardview_artist_explore, a).setTextColor(getResources().getColor(themeColor));
            w.getTextView(R.id.cardview_artist_change, a).setTextColor(getResources().getColor(themeColor));

            w.getTextView(R.id.layout_settings_preset_hint, a).setText(getResources().getString(R.string.faded));
        }
    }

    int getScheme() {
        return prefs.getInt("scheme", 1);
    }

    void setScheme(int scheme) {
        prefs.edit().putInt("scheme", scheme).apply();
    }
}