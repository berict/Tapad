package com.bedrock.padder.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
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
import com.bedrock.padder.model.preset.Pad;
import com.bedrock.padder.model.preset.Preset;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
    // TODO SET ON INTENT
    public About currentAbout = null;
    Gson gson = new Gson();
    Preset presets[];
    About abouts[];

    // TODO iap launch
    //IabHelper mHelper;
    //IabBroadcastReceiver mBroadcastReceiver;

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

        //String base64EncodePublicKey = constructBase64Key();

        //mHelper = new IabHelper(this, base64EncodePublicKey);
        //mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        //Log.d(TAG, "Starting setup.");
        //mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
        //    public void onIabSetupFinished(IabResult result) {
        //        Log.d(TAG, "Setup finished.");

        //        if (!result.isSuccess()) {
        //            // Oh noes, there was a problem.
        //            complain("Problem setting up in-app billing: " + result);
        //            return;
        //        }

        //        // Have we been disposed of in the meantime? If so, quit.
        //        if (mHelper == null) return;

        //        // Important: Dynamically register for broadcast messages about updated purchases.
        //        // We register the receiver here instead of as a <receiver> in the Manifest
        //        // because we always call getPurchases() at startup, so therefore we can ignore
        //        // any broadcasts sent while the app isn't running.
        //        // Note: registering this listener in an Activity is a bad idea, but is done here
        //        // because this is a SAMPLE. Regardless, the receiver must be registered after
        //        // IabHelper is setup, but before first call to getPurchases().
        //        mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
        //        IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
        //        registerReceiver(mBroadcastReceiver, broadcastFilter);

        //        // IAB is fully set up. Now, let's get an inventory of stuff we own.
        //        //Log.d(TAG, "Setup successful. Querying inventory.");
        //        //try {
        //        //    mHelper.queryInventoryAsync(mGotInventoryListener);
        //        //} catch (IabAsyncInProgressException e) {
        //        //    complain("Error querying inventory. Another async operation in progress.");
        //        //}
        //    }
        //});

        presets = new Preset[] {
                gson.fromJson(getResources().getString(R.string.json_hello), Preset.class),
                gson.fromJson(getResources().getString(R.string.json_roses), Preset.class),
                gson.fromJson(getResources().getString(R.string.json_faded), Preset.class)
        };

        abouts = new About[] {

        };

        Log.d(TAG, "Sharedprefs initialized");
        prefs = this.getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

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
        setButtonLayout();

        //Set transparent nav bar
        w.setStatusBar(R.color.transparent, a);
        w.setNavigationBar(R.color.transparent, a);

        w.setMarginRelativePX(R.id.fab, 0, 0, w.convertDPtoPX(20, a), prefs.getInt("navBarPX", 0) + w.convertDPtoPX(20, a), a);
        w.setMarginRelativePX(R.id.toolbar, 0, 0, 0, prefs.getInt("navBarPX", 0), a);
        ab.setStatusHeight(a);

        color = prefs.getInt("color", R.color.red);
        clearDeck();

        //TODO EDIT
        //makeJson();
    }

// TODO iap launch
//    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//            Log.d(TAG, "Query inventory finished.");
//
//            // Have we been disposed of in the meantime? If so, quit.
//            if (mHelper == null) return;
//
//            // Is it a failure?
//            if (result.isFailure()) {
//                complain("Failed to query inventory: " + result);
//                return;
//            }
//
//            Log.d(TAG, "Query inventory was successful.");
//            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
//        }
//    };
//
//    @NonNull
//    private String constructBase64Key() {
//        // TODO work on iap processes
//        String encodedString = getResources().getString(R.string.base64_rsa_key);
//        int base64Length = encodedString.length();
//        char[] encodedStringArray = encodedString.toCharArray();
//        char temp;
//
//        for(int i = 0; i < base64Length / 2; i++) {
//            if (i % 2 == 0) {
//                // ******   E P I C   D E C O D I N G   M E C H A N I S M   ****** //
//                temp = encodedStringArray[i];
//                encodedStringArray[i] = encodedStringArray[base64Length - 1 - i];
//                encodedStringArray[base64Length - 1 - i] = temp;
//            }
//        }
//
//        return String.valueOf(encodedStringArray);
//    }
//
//    void complain(String message) {
//        Log.e(TAG, "**** Purchase Error: " + message);
//        alert("Error: " + message);
//    }
//
//    void alert(String message) {
//        AlertDialog.Builder bld = new AlertDialog.Builder(this);
//        bld.setMessage(message);
//        bld.setNeutralButton("OK", null);
//        Log.d(TAG, "Showing alert dialog: " + message);
//        bld.create().show();
//    }

    void enterAnim() {
        anim.fadeIn(R.id.actionbar_layout, 0, 200, "background", a);
        anim.fadeIn(R.id.actionbar_image, 200, 200, "image", a);
        //TODO: Remove this to not load preset
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
                        w.getTextView(buttons[i][j], a).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (newWidthPx / 3));
                    }
                } else {
                    newWidthPx = (screenWidthPx / 9) * 2;
                    resizeView(tutorialButtons[i][j], newWidthPx, newHeightPx);
                    resizeView(buttons[i][j], newWidthPx - (marginPx * 2), newHeightPx - (marginPx * 2));
                    w.setMarginLinearPX(buttons[i][j], marginPx, marginPx, marginPx, marginPx, a);
                    if (i == 0) {
                        w.getTextView(buttons[i][j], a).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (newHeightPx / 3));
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
                        R.id.cardview_music_image, "transition", 0);
            }
        });

        w.getView(R.id.cardview_music_explore, a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElement(a, "activity.AboutActivity",
                        R.id.cardview_music_image, "transition", 0);
            }
        });

        w.getView(R.id.cardview_music_change, a).setOnClickListener(new View.OnClickListener() {
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
                .itemsCallbackSingleChoice(defaultScheme, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                setScheme(which);
                                PresetDialog.getBuilder()
                                        .widgetColorRes(R.color.hello)
                                        .positiveColorRes(R.color.hello);
                                setSchemeInfo();
                                break;
                            case 1:
                                setScheme(which);
                                PresetDialog.getBuilder()
                                        .widgetColorRes(R.color.roses)
                                        .positiveColorRes(R.color.roses);
                                setSchemeInfo();
                                break;
                            case 2:
                                setScheme(which);
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
        Handler preset = new Handler();
        preset.postDelayed(new Runnable() {
            @Override
            public void run() {
                sound.loadSchemeSound(presets[getScheme()], a);
            }
        }, delay);
    }

    void setToggleButton(final int color_id) {
        final int toggleButtonIds[] = {
                R.id.tgl1,
                R.id.tgl2,
                R.id.tgl3,
                R.id.tgl4,
                R.id.tgl5,
                R.id.tgl6,
                R.id.tgl7,
                R.id.tgl8
        };

        final boolean toggleButtonBool[] = {
                tgl1,
                tgl2,
                tgl3,
                tgl4,
                tgl5,
                tgl6,
                tgl7,
                tgl8
        };

        // TODO REKT
//        for (final int i[] = {0}; i[0] < 7; i[0]++) {
//            if (i[0] < 4) {
//                w.setOnTouch(toggleButtonIds[i[0]], new Runnable() {
//                    @Override
//                    public void run() {
//                        clearDeck();
//                        if (toggleButtonBool[i[0]] == false) {
//                            toggleSoundId = i[0] + 1;
//
//                            if (tgl5 || tgl6 || tgl7 || tgl8) {
//                                // TODO CHANGE THIS METHOD
//                                sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
//                            } else {
//                                sound.setButtonToggle(toggleSoundId, color, a);
//                            }
//
//                            for (int j = 0; j < 4; j++) {
//                                if (i[0] == j) {
//                                    // highlight current deck
//                                    w.setViewBackgroundColor(toggleButtonIds[j], color_id, a);
//                                } else {
//                                    // disable all other decks
//                                    w.setViewBackgroundColor(toggleButtonIds[j], R.color.grey, a);
//                                }
//                            }
//
//                            for (int j = 0; j < 4; j++) {
//                                if (i[0] != j) {
//                                    // except current deck number
//                                    if (toggleButtonBool[j] == true) {
//                                        // if other deck was selected before, play deck sound
//                                        sound.playToggleButtonSound(i[0] + 1);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            w.setPadColor(toggleButtonIds[i[0]], R.color.grey, a);
//                            toggleSoundId = 0;
//                            clearDeck();
//                            sound.soundAllStop();
//                        }
//                    }
//                }, new Runnable() {
//                    @Override
//                    public void run() {
//                        if (toggleButtonBool[i[0]] == false) {
//                            for (int j = 0; j < 4; j++) {
//                                if (j != i[0]) {
//                                    toggleButtonBool[j] = false;
//                                } else {
//                                    toggleButtonBool[j] = true;
//                                }
//                            }
//                        } else {
//                            toggleButtonBool[i[0]] = false;
//                        }
//                    }
//                }, a);
//            } else {
//                w.setOnTouch(toggleButtonIds[i[0]], new Runnable() {
//                    @Override
//                    public void run() {
//                        if (toggleButtonBool[i[0]] == false) {
//                            togglePatternId = i[0] - 4 + 1;
//                            if (tgl1 || tgl2 || tgl3 || tgl4) {
//                                sound.setButtonTogglePattern(toggleSoundId, color, togglePatternId, a);
//                            }
//
//                            for (int j = 4; j < 8; j++) {
//                                if (i[0] == j) {
//                                    // highlight current deck
//                                    w.setViewBackgroundColor(toggleButtonIds[j], color_id, a);
//                                } else {
//                                    // disable all other decks
//                                    w.setViewBackgroundColor(toggleButtonIds[j], R.color.grey, a);
//                                }
//                            }
//                        } else {
//                            w.setViewBackgroundColor(toggleButtonIds[i[0]], R.color.grey, a);
//                            togglePatternId = 0;
//                            if (tgl1 || tgl2 || tgl3 || tgl4) {
//                                sound.setButtonToggle(toggleSoundId, color, a);
//                            }
//                        }
//                    }
//                }, new Runnable() {
//                    @Override
//                    public void run() {
//                        if (toggleButtonBool[i[0]] == false) {
//                            for (int j = 4; j < 8; j++) {
//                                if (j != i[0]) {
//                                    toggleButtonBool[j] = false;
//                                } else {
//                                    toggleButtonBool[j] = true;
//                                }
//                            }
//                        } else {
//                            toggleButtonBool[i[0]] = false;
//                        }
//                    }
//                }, a);
//            }
//        }

        // 1 - 4
        w.setOnTouch(R.id.tgl1, new Runnable() {
            @Override
            public void run() {
                clearDeck();
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
                clearDeck();
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
                clearDeck();
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
                clearDeck();
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

    void clearDeck() {
        // clear button colors
        int buttonIds[] = {
                R.id.btn00,
                R.id.btn11,
                R.id.btn12,
                R.id.btn13,
                R.id.btn14,
                R.id.btn21,
                R.id.btn22,
                R.id.btn23,
                R.id.btn24,
                R.id.btn31,
                R.id.btn32,
                R.id.btn33,
                R.id.btn34,
                R.id.btn41,
                R.id.btn42,
                R.id.btn43,
                R.id.btn44
        };
        for (int buttonId : buttonIds) {
            View pad = findViewById(buttonId);
            pad.setBackgroundColor(a.getResources().getColor(R.color.grey));
        }

        // stop all looping sounds
        Integer streamIds[] = w.getLoopStreamIds();
        SoundPool soundPool = sound.getSoundPool();
        try {
            for (Integer streamId : streamIds) {
                soundPool.stop(streamId);
            }
        } finally {
            w.clearLoopStreamId();
        }
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
        ab.setNav(0, null, a);
        Preset currentPreset = presets[getScheme()];
        themeColor = currentPreset.getAbout().getActionbarColor();
        w.setRecentColor(0, 0, themeColor, a);

        if (isSettingVisible == false) {
            ab.setColor(themeColor, a);
            ab.setImage(w.getDrawableId("logo_" + currentPreset.getMusic().getNameId().replace("preset_", "")), a);
        } else {
            ab.setNav(1, null, a);
            ab.setTitle(R.string.settings, a);
            ab.setColor(R.color.colorAccent, a);
        }

        // Cardview
        w.getImageView(R.id.cardview_music_image, a).setImageResource(w.getDrawableId("about_album_" + currentPreset.getMusic().getNameId().replace("preset_", "")));
        w.getTextView(R.id.cardview_music_song, a).setText(w.getStringId(currentPreset.getMusic().getNameId() + "_full"));
        w.getTextView(R.id.cardview_music_explore, a).setTextColor(getResources().getColor(themeColor));
        w.getTextView(R.id.cardview_music_change, a).setTextColor(getResources().getColor(themeColor));

        w.getTextView(R.id.layout_settings_preset_hint, a).setText(w.getStringId(currentPreset.getMusic().getNameId() + "_full"));
    }

    int getScheme() {
        return prefs.getInt("scheme", 0);
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
//        About about = new About(bio[0], "cardview_background_music_roses",
//                new Bio(bio[2], "about_bio_roses", bio[3], bio[4], bio[5]), details,
//                "roses_dark", "roses");
//
//        Log.d("JSON TEST", json);
//        Item fadedItems[] = {
//                new Item("facebook", "preset_faded_detail_facebook"),
//                new Item("twitter", "preset_faded_detail_twitter"),
//                new Item("soundcloud", "preset_faded_detail_soundcloud"),
//                new Item("instagram", "preset_faded_detail_instagram"),
//                new Item("google_plus", "preset_faded_detail_google_plus"),
//                new Item("youtube", "preset_faded_detail_youtube"),
//                new Item("twitch", "preset_faded_detail_twitch"), // only faded
//                new Item("web", "preset_faded_detail_web")
//        };
//
//        Detail fadedDetail = new Detail("preset_faded_detail_title", fadedItems);
//
//        Item fadedSongItems[] = {
//                new Item("soundcloud", "preset_faded_song_detail_soundcloud", false),
//                new Item("youtube", "preset_faded_song_detail_youtube", false),
//                new Item("spotify", "preset_faded_song_detail_spotify", false),
//                new Item("google_play_music", "preset_faded_song_detail_google_play_music", false),
//                new Item("apple", "preset_faded_song_detail_apple", false),
//                new Item("amazon", "preset_faded_song_detail_amazon", false),
//                new Item("pandora", "preset_faded_song_detail_pandora", false)
//        };
//
//        Detail fadedSongDetail = new Detail("preset_faded_song_detail_title", fadedSongItems);
//
//        Bio fadedBio = new Bio(
//                "preset_faded_bio_title",
//                "about_bio_faded",
//                "preset_faded_bio_name",
//                "preset_faded_bio_text",
//                "preset_faded_bio_source"
//        );
//
//        Detail fadedDetails[] = {
//                fadedDetail,
//                fadedSongDetail
//        };
//
//        About fadedAbout = new About(
//                "preset_faded_title", "about_album_faded",
//                fadedBio, fadedDetails,
//                "preset_faded_color_dark", "preset_faded_color"
//        );
//
//        // Timings
////        Integer pt1[][] = {
////                {42660},
////                {null},
////                {null},
////                {null},
////                {null},
////                {26658},
////                {0},
////                {1333},
////                {2666},
////                {3998},
////                {27991},
////                {10664, 21327},
////                {11996, 22660},
////                {13329, 23993},
////                {14662, 25326},
////                {29324},
////                {15995},
////                {17328},
////                {18660},
////                {19993},
////                {30657},
////                {5331},
////                {6664},
////                {7997},
////                {9330},
////                {42660},
////                {34656},
////                {37322},
////                {45320},
////                {47986},
////                {47986},
////                {35989},
////                {38655},
////                {46653},
////                {49318},
////                {50651},
////                {36655},
////                {39321},
////                {47319},
////                {49984},
////                {45320},
////                {35322},
////                {37988},
////                {45986},
////                {48652},
////                {31991},
////                {31991},
////                {39988},
////                {42660},
////                {50651},
////                {37322},
////                {33324},
////                {41320},
////                {43987},
////                {51984},
////                {39988},
////                {33990},
////                {41987},
////                {44653},
////                {52650},
////                {34656},
////                {32657},
////                {40654},
////                {43320},
////                {51317},
////                {43987},
////                {null},
////                {null},
////                {null},
////                {null},
////                {10664},
////                {10664},
////                {15995},
////                {21327},
////                {26658},
////                {34656},
////                {34656},
////                {39988},
////                {45320},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null}
////        };
////
////        Integer pt2[][] = {
////                {54660},
////                {53318},
////                {null},
////                {75993},
////                {null},
////                {75993},
////                {70660},
////                {54660},
////                {59993},
////                {65326},
////                {78659},
////                {71326},
////                {55327},
////                {60660},
////                {65993},
////                {81325},
////                {71993},
////                {55994},
////                {61327},
////                {66660},
////                {83992},
////                {72660},
////                {56660},
////                {61993},
////                {67326},
////                {86659},
////                {73326},
////                {57327},
////                {62660},
////                {67993},
////                {89325},
////                {73993},
////                {57993},
////                {63327},
////                {68660},
////                {91992},
////                {74660},
////                {58660},
////                {63993},
////                {69326},
////                {94658},
////                {75326},
////                {59324},
////                {64659},
////                {69991},
////                {87992},
////                {57662},
////                {73660},
////                {68329},
////                {62994},
////                {90658},
////                {58329},
////                {74327},
////                {68995},
////                {63660},
////                {93325},
////                {58995},
////                {74996},
////                {69661},
////                {64330},
////                {95992},
////                {59662},
////                {75663},
////                {70328},
////                {64997},
////                {77326},
////                {54997},
////                {70994},
////                {65663},
////                {60328},
////                {79993},
////                {55664},
////                {71661},
////                {66330},
////                {60995},
////                {82659},
////                {56330},
////                {72327},
////                {66996},
////                {61661},
////                {85326},
////                {56996},
////                {72994},
////                {67663},
////                {62328}
////        };
////
////        Integer pt3[][] = {
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {139993},
////                {97325},
////                {107994},
////                {118660},
////                {129327},
////                {142659},
////                {99991},
////                {110660},
////                {121326},
////                {131993},
////                {143992},
////                {101325},
////                {111994},
////                {122660},
////                {133326},
////                {141326},
////                {98658},
////                {109327},
////                {119993},
////                {130659},
////                {145326},
////                {102658},
////                {113327},
////                {123993},
////                {134659},
////                {147992},
////                {105325},
////                {115994},
////                {126660},
////                {137326},
////                {149326},
////                {106658},
////                {117327},
////                {127993},
////                {138659},
////                {146659},
////                {103991},
////                {114660},
////                {125326},
////                {135993},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {139993},
////                {97325},
////                {107994},
////                {118660},
////                {129327},
////                {142659},
////                {99991},
////                {110660},
////                {121326},
////                {131993},
////                {97325},
////                {102658},
////                {107994},
////                {113327},
////                {118660},
////                {129327, 130657, 131997, 133327, 134657, 135987, 137327, 138657},
////                {139997, 141327, 142667, 143987},
////                {145327, 146657, 147997, 149327},
////                {null},
////                {null},
////                {145326},
////                {102658},
////                {113327},
////                {123993},
////                {134659},
////                {147992},
////                {105325},
////                {115994},
////                {126660},
////                {137326},
////                {123993},
////                {129327},
////                {134659},
////                {139993},
////                {145326}
////        };
////
////        Integer pt4[][] = {
////                {null},
////                {150650},
////                {null},
////                {173325},
////                {null},
////                {173325},
////                {167992},
////                {151992},
////                {157325},
////                {162658},
////                {175991},
////                {168658},
////                {152659},
////                {157992},
////                {163325},
////                {178658},
////                {169325},
////                {153326},
////                {158659},
////                {163992},
////                {181324},
////                {169992},
////                {153992},
////                {159326},
////                {164658},
////                {183991},
////                {170658},
////                {154659},
////                {159992},
////                {165325},
////                {186657},
////                {171325},
////                {155326},
////                {160659},
////                {165992},
////                {189324},
////                {171992},
////                {155992},
////                {161326},
////                {166658},
////                {191990},
////                {172658},
////                {156657},
////                {161992},
////                {167323},
////                {185324},
////                {154994},
////                {170992},
////                {165661},
////                {160326},
////                {187990},
////                {155661},
////                {171659},
////                {166327},
////                {160992},
////                {190657},
////                {156327},
////                {172328},
////                {166993},
////                {161662},
////                {193324},
////                {156994},
////                {172995},
////                {167660},
////                {162329},
////                {174658},
////                {152329},
////                {168326},
////                {162995},
////                {157660},
////                {177325},
////                {152996},
////                {168993},
////                {163662},
////                {158327},
////                {179991},
////                {153662},
////                {169659},
////                {164328},
////                {158993},
////                {182658},
////                {154328},
////                {170326},
////                {164995},
////                {159660}
////        };
////
////        Integer pt5[][] = {
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {194637},
////                {null},
////                {null},
////                {null},
////                {null},
////                {195993},
////                {null},
////                {null},
////                {null},
////                {null},
////                {195326},
////                {null},
////                {null},
////                {null},
////                {null},
////                {196660},
////                {null},
////                {null},
////                {null},
////                {null},
////                {197326},
////                {null},
////                {null},
////                {null},
////                {null},
////                {198660},
////                {null},
////                {null},
////                {null},
////                {null},
////                {197993},
////                {null},
////                {null},
////                {null},
////                {null},
////                {199324},
////                {null},
////                {null},
////                {null},
////                {null},
////                {199993},
////                {null},
////                {null},
////                {null},
////                {null},
////                {201340},
////                {null},
////                {null},
////                {null},
////                {null},
////                {200682},
////                {null},
////                {null},
////                {null},
////                {null},
////                {202128},
////                {null},
////                {null},
////                {null},
////                {null},
////                {203147},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null},
////                {null}
////        };
////
////        DeckTiming deckTiming[] = {
////                new DeckTiming(
////                        pt1, 1, 0
////                ),
////                new DeckTiming(
////                        pt2, 2, 52949
////                ),
////                new DeckTiming(
////                        pt3, 3, 96379
////                ),
////                new DeckTiming(
////                        pt4, 2, 149947
////                ),
////                new DeckTiming(
////                        pt5, 4, 193772
////                )
////        };
//
//        //TODO EDIT
//        //Log.d("Array", Arrays.deepToString(deckTiming[0].getDeckTiming()));
//
//        Music fadedMusic = new Music(
//                "preset_faded",
//                getDeckFromFileName(fileTag),
//                null
//        );
//
//        Preset fadedPreset = new Preset(2, fadedMusic, fadedAbout);
//
//        //String json = gson.toJson(preset, Preset.class);
//        largeLog("JSON", gson.toJson(fadedPreset));

        Bio tapadBio = new Bio(
                "info_tapad_bio_title",
                "about_bio_tapad",
                "info_tapad_bio_name",
                "info_tapad_bio_text",
                "info_tapad_bio_source"
        );

        Item tapadInfo[] = {
                new Item("info_tapad_info_check_update", "info_tapad_info_check_update_hint", "about_detail_google_play", new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_tapad_info_check_update"), R.color.colorAccent, a);
                        intent.intentLink(a, w.getStringFromId("info_tapad_info_check_update_link", a), 400);
                    }
                }, true),
                new Item("info_tapad_info_tester", "info_tapad_info_tester_hint", "about_detail_tester", new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_tapad_info_tester"), R.color.colorAccent, a);
                        intent.intentLink(a, w.getStringFromId("info_tapad_info_tester_link", a), 400);
                    }
                }, true),
                new Item("info_tapad_info_legal", null, "about_detail_info", new Runnable() {
                    @Override
                    public void run() {
                        // legal info dialog
                        new MaterialDialog.Builder(a)
                                .title(w.getStringId("info_tapad_info_legal"))
                                .content(w.getStringId("info_tapad_info_legal_text"))
                                .positiveText(R.string.dialog_close)
                                .positiveColorRes(R.color.colorAccent)
                                .show();
                    }
                }, false),
                new Item("info_tapad_info_version", "info_tapad_info_version_hint", ""),
                new Item("info_tapad_info_build_date", "info_tapad_info_build_date_hint", ""),
                new Item("info_tapad_info_changelog", null, "about_detail_changelog", new Runnable() {
                    @Override
                    public void run() {
                        // changelog info dialog
                        new MaterialDialog.Builder(a)
                                .title(w.getStringId("info_tapad_info_changelog"))
                                .content(w.getStringId("info_tapad_info_changelog_text"))
                                .positiveText(R.string.dialog_close)
                                .positiveColorRes(R.color.colorAccent)
                                .show();
                    }
                }, false),
                new Item("info_tapad_info_thanks", null, "about_detail_thanks", new Runnable() {
                    @Override
                    public void run() {
                        // thanks info dialog
                        new MaterialDialog.Builder(a)
                                .title(w.getStringId("info_tapad_info_thanks"))
                                .content(w.getStringId("info_tapad_info_thanks_text"))
                                .positiveText(R.string.dialog_close)
                                .positiveColorRes(R.color.colorAccent)
                                .show();
                    }
                }, false),
                new Item("info_tapad_info_dev", "info_tapad_info_dev_hint", "about_detail_dev", new Runnable() {
                    @Override
                    public void run() {
                        // TODO do this after the dev about constructed
                        //intent.intent();
                        Toast.makeText(a, "intent dev", Toast.LENGTH_SHORT).show();
                    }
                }, false)
                // TODO ADD ITEMS
        };

        Item tapadOthers[] = {
                new Item("info_tapad_others_song", "info_tapad_others_song_hint", "about_detail_others_song", new Runnable() {
                    @Override
                    public void run() {
                        intent.intentWithExtra(a, "activity.FeedbackActivity", "feedbackMode", "song", 400);
                    }
                }, true),
                new Item("info_tapad_others_feedback", "info_tapad_others_feedback_hint", "about_detail_others_feedback", new Runnable() {
                    @Override
                    public void run() {
                        intent.intentWithExtra(a, "activity.FeedbackActivity", "feedbackMode", "feedback", 400);
                    }
                }, true),
                new Item("info_tapad_others_report_bug", "info_tapad_others_report_bug_hint", "about_detail_others_report_bug", new Runnable() {
                    @Override
                    public void run() {
                        intent.intentWithExtra(a, "activity.FeedbackActivity", "feedbackMode", "report_bug", 400);
                    }
                }, true),
                new Item("info_tapad_others_rate", "info_tapad_others_rate_hint", "about_detail_others_rate", new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_tapad_others_rate"), R.color.colorAccent, a);
                        intent.intentMarket(a, "com.bedrock.padder", 400);
                    }
                }, true),
                new Item("info_tapad_others_translate", "info_tapad_others_translate_hint", "about_detail_web", new Runnable() {
                    @Override
                    public void run() {
                        // TODO make translation service available
                        Toast.makeText(a, w.getStringFromId("info_tapad_others_translate_error", a), Toast.LENGTH_SHORT).show();
                    }
                }, false),
                new Item("info_tapad_others_recommend", "info_tapad_others_recommend_hint", "about_detail_others_recommend", new Runnable() {
                    @Override
                    public void run() {
                        intent.intentShareText(a,
                                w.getStringFromId("info_tapad_others_recommend_share_subject", a),
                                w.getStringFromId("info_tapad_others_recommend_share_text", a),
                                w.getStringFromId("info_tapad_others_recommend_share_hint", a),
                                400);
                    }
                }, true)
        };

        Detail tapadDetails[] = {
                new Detail("info_tapad_info_title", tapadInfo),
                new Detail("info_tapad_others_title", tapadOthers)
        };

        About tapadAbout = new About(
                "info_tapad_title", "about_image_tapad",
                tapadBio, tapadDetails,
                "info_tapad_color_dark", "info_tapad_color"
        );

        largeLog("tapadAboutJSON", gson.toJson(tapadAbout));

        Bio berictBio = new Bio(
                "info_berict_bio_title",
                null,
                "info_berict_bio_name",
                "info_berict_bio_text",
                "info_berict_bio_source"
        );

        Item devItems[] = {
                new Item("facebook", "info_berict_detail_facebook"),
                new Item("twitter", "info_berict_detail_twitter"),
                new Item("google_plus", "info_berict_detail_google_plus"),
                new Item("youtube", "info_berict_detail_youtube"),
                new Item("web", "info_berict_detail_web")
        };

        Item devSupport[] = {
                new Item("info_berict_action_report_bug", "info_berict_action_report_bug_hint", "about_detail_others_report_bug", new Runnable() {
                    @Override
                    public void run() {
                        intent.intentWithExtra(a, "activity.FeedbackActivity", "feedbackMode", "report_bug", 400);
                    }
                }, true),
                new Item("info_berict_action_rate", "info_berict_action_rate_hint", "about_detail_others_rate", new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_berict_action_rate"), R.color.colorAccent, a);
                        intent.intentMarket(a, "com.bedrock.padder", 400);
                    }
                }, true),
                new Item("info_berict_action_translate", "info_berict_action_translate_hint", "about_detail_others_translate", new Runnable() {
                    @Override
                    public void run() {
                        // TODO make translation service available
                        Toast.makeText(a, w.getStringFromId("info_berict_action_translate_error", a), Toast.LENGTH_SHORT).show();
                    }
                }, false),
                new Item("info_berict_action_donate", "info_berict_action_donate_hint", "about_detail_others_donate", new Runnable() {
                    @Override
                    public void run() {
                        // TODO make translation service available
                        Toast.makeText(a, w.getStringFromId("info_berict_action_donate_error", a), Toast.LENGTH_SHORT).show();
                    }
                }, false)
        };

        Detail berictDetails[] = {
                new Detail("info_berict_detail_title", devItems),
                new Detail("info_berict_action_title", devSupport)
        };
        
        About berictAbout = new About(
                "info_berict_title", "about_image_berict",
                berictBio, berictDetails,
                "info_berict_color_dark", "info_berict_color"
        );

        largeLog("berictAboutJSON", gson.toJson(berictAbout));
    }

    String fileTag = "alan_walker_faded_";

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