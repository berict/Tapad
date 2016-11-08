package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.ThemeService;
import com.bedrock.padder.helper.WindowService;

public class LauncherActivity extends Activity {

    private AnimService anim = new AnimService();
    private IntentService intent = new IntentService();
    private ThemeService theme = new ThemeService();
    private WindowService window = new WindowService();

    Activity activity;

    SharedPreferences prefs = null;

    //@TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        activity = this;

        prefs = getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        //Tester - Set firstrun = true
        //prefs.edit().putBoolean("welcome", true).apply();
        //prefs.edit().putInt("scheme", 1).apply();

        // White screen set
        theme.color(this, "FFFFFF");

        window.getNavigationBar(R.id.root, activity);
        window.getStatusBar(R.id.root, activity);

        /** Logo fade out + intent */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                anim.fadeOut(R.id.root, 0, 400, activity);

                checkVersionCode();
            }
        }, 1000); //Default 1500
    }

    void checkVersionCode() {
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            currentVersionCode = -1; // Error
        }

        Log.i("VersionCode", "Version code = " + String.valueOf(currentVersionCode));

        int savedVersionCode = prefs.getInt("versionCode", 0);
        if (currentVersionCode == savedVersionCode) {
            // Normal run, Main transition
            Log.d("FirstRun", "false, intent to MainActivity");

            intent.intentFlag(activity, "activity.MainActivity", 500);
            //prefs.edit().putInt("quickstart", 0).apply();
        } else if (savedVersionCode == 0 || savedVersionCode == -1) {
            // New install / cleared sharedpref, Welcome transition
            Log.d("FirstRun", "true, intent to WelcomeActivity");

            intent.intentFlag(activity, "activity.WelcomeActivity", 500);
            prefs.edit().putInt("quickstart", 0).apply();
            prefs.edit().putInt("versionCode", currentVersionCode - 1).apply(); // To show changelog
        } else if (currentVersionCode > savedVersionCode) {
            // Upgrade run
            Log.d("FirstRun", "false (Updated), intent to MainActivity");

            intent.intentFlag(activity, "activity.MainActivity", 500);
        }
    }

//    @Override
//    public void onPause(){
//        super.onPause();
//        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//    }

//    @Override
//    public void finish() {
//        super.finish();
//        Log.d("Animation", "Fade animation");
//        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//    }
}