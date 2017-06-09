package com.bedrock.padder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;

public class HelpActivity extends AppCompatActivity {

    private AppCompatActivity activity = this;

    private WindowHelper window = new WindowHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setUi();
    }

    private void setUi() {
        // set actionbar
        toolbar.setActionBar(activity);
        toolbar.setActionBarTitle(R.string.help_title);
        toolbar.setActionBarPadding(activity);
        toolbar.setActionBarColor(R.color.colorAccent, activity);
        toolbar.setStatusBarTint(activity);
        toolbar.setActionBarDisplayHomeAsUpIcon(R.drawable.ic_close_white);

        // transparent navigation bar
        window.setNavigationBar(R.color.transparent, activity);

        // set taskDesc
        window.setRecentColor(R.string.help_title, R.color.colorAccent, activity);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        pressBack();
        return true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            // reset taskDesc
            window.setRecentColor(window.getStringFromId("help_title", activity), R.color.colorAccent, activity);
        }
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }
}
