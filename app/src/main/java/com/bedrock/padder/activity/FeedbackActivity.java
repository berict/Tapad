package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.FabService;
import com.bedrock.padder.helper.WindowService;

public class FeedbackActivity extends AppCompatActivity {

    final String TAG = "FeedbackActivity";

    private FabService fab = new FabService();
    private WindowService w = new WindowService();
    private AppbarService ab = new AppbarService();

    Activity a = this;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Log.d(TAG, "Sharedprefs initialized");
        prefs = this.getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        Intent intent = getIntent();
        String mode = intent.getExtras().getString("feedbackMode");

        setFab();

        w.setStatusBar(R.color.transparent, a);
        w.setNavigationBar(R.color.transparent, a);

        w.setMarginRelativePX(R.id.fab, 0, 0, w.convertDPtoPX(20, a), prefs.getInt("navBarPX", 0) + w.convertDPtoPX(20, a), a);
        ab.setStatusHeight(a);
        ab.setColor(R.color.colorFeedback, a);
        ab.setTitle(w.getStringId("task_feedback_" + mode), a);
        ab.setNav(3, null, a);
        w.setRecentColor(w.getStringId("task_feedback_" + mode), 0, R.color.colorFeedback, a);
    }

    @Override
    public void onBackPressed() {
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                FeedbackActivity.super.onBackPressed();
            }
        }, 200);
    }

    void setFab() {
        fab.setFab(a);
        fab.show();
        fab.onClick(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(a, "SEND", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
