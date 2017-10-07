package com.bedrock.padder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class LauncherActivity extends AppCompatActivity {

    private AnimateHelper anim = new AnimateHelper();
    private IntentHelper intent = new IntentHelper();
    private WindowHelper window = new WindowHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(
                new Fabric.Builder(this)
                        .kits(new Crashlytics())
                        // TODO RELEASE
                        .debuggable(true)
                        .build()
        );

        setContentView(R.layout.activity_launcher);

        activity = this;

        // White screen set
        toolbar.setStatusBarTint(this);
        window.getNavigationBar(R.id.root, activity);
        window.getStatusBar(R.id.root, activity);

        // Logo fade out + intent
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                anim.fadeOut(R.id.root, 0, 400, activity);

                intent.intentFlag(activity, "activity.MainActivity", 500);
            }
        }, 500);
    }
}