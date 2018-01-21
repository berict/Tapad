package com.bedrock.padder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;

import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class HelpActivity extends AppCompatActivity {

    private AppCompatActivity activity = this;

    private WindowHelper window = new WindowHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();
    private IntentHelper intent = new IntentHelper();
    private AnimateHelper anim = new AnimateHelper();

    private TextView detailTitle;
    private TextView detailText;

    private boolean isDetailVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        detailTitle = ((TextView) activity.findViewById(R.id.activity_help_detail_title));
        detailText = ((TextView) activity.findViewById(R.id.activity_help_detail_text));

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

        setActions();
    }

    private void setActions() {
        window.setOnClick(R.id.activity_help_contact_email, new Runnable() {
            @Override
            public void run() {
                intent.intentEmail(activity, R.string.feedback_email,
                        "Tapad Feedback - Feedback [Email]", null,
                        R.string.feedback_email_client_feedback, 0);
            }
        }, activity);
        window.setOnClick(R.id.activity_help_contact_send_feedback, new Runnable() {
            @Override
            public void run() {
                intent.intentWithExtra(activity, "activity.FeedbackActivity", "feedbackMode", "feedback", 0);
            }
        }, activity);

        String helpItems[] = {
                "about_tapad",
                "about_gesture_mode",
                "preset_store_download",
                "preset_store_manage",
                "tutorial"
        };

        for (final String helpItem : helpItems) {
            // set onclick items
            window.setOnClick(window.getId("activity_help_popular_" + helpItem), new Runnable() {
                @Override
                public void run() {
                    showDetailHelp(helpItem);
                }
            }, activity);
        }
    }

    private void showDetailHelp(String helpResId) {
        if (!isDetailVisible) {
            isDetailVisible = true;
            detailTitle.setText(getStringFromId("help_popular_" + helpResId, activity));
            detailText.setText(getStringFromId("help_popular_" + helpResId + "_detail", activity));
            anim.fadeIn(R.id.activity_help_detail, 0, 200, "detailIn", activity);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            // reset taskDesc
            window.setRecentColor(R.string.help_title, R.color.colorAccent, activity);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDetailVisible) {
            // visible, close detail
            anim.fadeOut(R.id.activity_help_detail, 0, 200, activity);
            isDetailVisible = false;
        } else {
            super.onBackPressed();
        }
    }
}
