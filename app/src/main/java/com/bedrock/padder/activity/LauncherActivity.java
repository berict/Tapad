package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class LauncherActivity extends AppCompatActivity {

    private AnimateHelper anim = new AnimateHelper();
    private IntentHelper intent = new IntentHelper();
    private WindowHelper window = new WindowHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    Activity activity;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_launcher);

        activity = this;

        prefs = getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);

        // White screen set
        toolbar.setStatusBarTint(this);
        window.getNavigationBar(R.id.root, activity);
        window.getStatusBar(activity);

        // Logo fade out + intent
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                anim.fadeOut(R.id.root, 0, 400, activity);

                // edit this to enable normal intent
                checkVersionCode();

                // edit this to intent to welcome activity always
                //intent.intentFlagWithExtra(activity, "activity.MainActivity", "version", "new", 500);
                //intent.intentFlagWithExtra(activity, "activity.MainActivity", "version", "updated", 500);
            }
        }, 500);
    }

    void checkVersionCode() {
        int currentVersionCode;
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
        } else if (savedVersionCode == 0 || savedVersionCode == -1) {
            // New install / cleared cache, Welcome transition
            Log.d("FirstRun", "true, intent to MainActivity");

            intent.intentFlagWithExtra(activity, "activity.MainActivity", "version", "new", 500);
            // To show changelog
            prefs.edit().putInt("versionCode", currentVersionCode - 1).apply();
        } else if (currentVersionCode > savedVersionCode) {
            // Upgrade run
            Log.d("FirstRun", "false (Updated), intent to MainActivity");

            intent.intentFlagWithExtra(activity, "activity.MainActivity", "version", "updated", 500);
        }
    }
}