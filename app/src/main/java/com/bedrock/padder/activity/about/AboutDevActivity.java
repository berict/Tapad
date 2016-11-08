package com.bedrock.padder.activity.about;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.bedrock.padder.R;
import com.bedrock.padder.service.AnimService;
import com.bedrock.padder.service.IntentService;
import com.bedrock.padder.service.TextService;
import com.bedrock.padder.service.ThemeService;
import com.bedrock.padder.service.WindowService;

public class AboutDevActivity extends AppCompatActivity {


    private WindowService window = new WindowService();
    private AnimService anim = new AnimService();
    private IntentService intent = new IntentService();
    private ThemeService theme = new ThemeService();
    private TextService text = new TextService();

    Activity activity;

    SharedPreferences prefs;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dev);

        activity = this;
        prefs = getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        window.setNavigationBar(R.color.transparent, activity);

        View statusbar = (View)findViewById(R.id.statusbar);
        if(Build.VERSION.SDK_INT < 21){
            statusbar.setVisibility(View.GONE);
        } else {
            try {
                statusbar.getLayoutParams().height = prefs.getInt("statBarPX", 0);
            } catch (NullPointerException e){
                Log.d("NullExp", e.getMessage());
                statusbar.setVisibility(View.GONE);
            }
        }

        window.setMarginRelativePX(R.id.layout_dev_relative, 0, prefs.getInt("statBarPX", 0), 0, 0, activity);
        window.getView(R.id.layout_dev_margin, activity).getLayoutParams().height = prefs.getInt("navBarPX", 0) + window.convertDPtoPX(10, activity);

        enterAnim();
        setSchemeInfo();
    }

    boolean closeFeedback = false;
    @Override
    public void onBackPressed(){
        if(isFeedbackVisible == true){
            if(closeFeedback == false){
                if(!(!text.isTextValid(R.id.dialog_feedback_subject, activity) &&
                     !text.isTextValid(R.id.dialog_feedback_text,    activity))) {
                    Toast.makeText(this, R.string.dialog_feedback_close, Toast.LENGTH_SHORT).show();
                    closeFeedback = true;
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            closeFeedback = false;
                        }
                    }, 2000);
                } else {
                    hideFeedbackDialog();
                }
            } else {
                hideFeedbackDialog();
            }
        } else {
            anim.fadeOut(R.id.layout_dev_text, 0, 200, activity);
            anim.fadeOut(R.id.layout_dev_detail, 0, 200, activity);
            Handler delay = new Handler();
            delay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AboutDevActivity.super.onBackPressed();
                }
            }, 200);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        pressBack();

        return true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Handler keyboard = new Handler();
        keyboard.postDelayed(new Runnable() {
            @Override
            public void run() {
                window.hideKeyboard(activity);
            }
        }, 10);

        if(hasFocus){
            if(isDialogVisible == false) {
                theme.setGone(R.id.layout_dev_placeholder, 0, activity);
            }
            if(isFeedbackVisible == true) {
                anim.circularRevealinpx(R.id.dialog_feedback,
                        coord[0], coord[1],
                        window.getWindowHypot(activity), 0, new AccelerateDecelerateInterpolator(),
                        400, 200, activity);
                window.hideKeyboard(activity);
                isFeedbackVisible = false;
            }
        }
    }

    void enterAnim() {
        anim.fadeIn(R.id.layout_dev_text, 400, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_dev_detail_bio, 500, 200, "bioIn", activity);
        anim.fadeIn(R.id.layout_dev_detail_about, 600, 200, "aboutIn", activity);
        anim.fadeIn(R.id.layout_dev_detail_support, 600, 200, "supportIn", activity);
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }

    boolean isDialogVisible = false;
    boolean isFeedbackVisible = false;
    int coord[] = {0, 0};

    void showFeedbackDialog(final int touch_id) {
//        setFeedback();
//
//        window.getView(touch_id, activity).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                coord[0] = (int)event.getRawX();
//                coord[1] = (int)event.getRawY();
//                window.hideKeyboard(activity);
//
//                return false;
//            }
//        });
//
//        window.getView(touch_id, activity).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Clear editText
//                text.clearEditText(R.id.dialog_feedback_text,    activity);
//                text.clearEditText(R.id.dialog_feedback_subject, activity);
//
//                text.clearEditTextLayout(R.id.dialog_feedback_text_layout,    activity);
//                text.clearEditTextLayout(R.id.dialog_feedback_subject_layout, activity);
//
//                window.getTextView(R.id.actionbar_layout_title, activity).setText(getResources().getString(R.string.dev_report_bug));
//                window.getView(R.id.dialog_feedback_link_layout, activity).setVisibility(View.GONE);
//                text.setEditText(R.id.dialog_feedback_text,    R.id.dialog_feedback_text_layout, "text",    R.string.dialog_report_bug_hint_text,    R.string.dialog_report_bug_hint_wrong_text,    activity);
//                window.setRecentColor(R.string.tapad_report_bug, 0, R.color.colorAccent, activity);
//
//                anim.circularRevealinpx(R.id.dialog_feedback,
//                        coord[0], coord[1],
//                        0, window.getWindowHypot(activity), new AccelerateDecelerateInterpolator(),
//                        400, 0, activity);
//                Handler dialogDelay = new Handler();
//                dialogDelay.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        anim.fadeOut(R.id.dialog_feedback_placeholder, 0, 200, activity);
//                    }
//                }, 400);
//                isFeedbackVisible = true;
//            }
//        });
//
//        window.getView(R.id.actionbar_layout_action, activity).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Send method
//                if((!text.isTextValid(R.id.dialog_feedback_subject, activity) ||
//                        !text.isTextValid(R.id.dialog_feedback_text,    activity))){
//                    // Form not filled
//                    Toast.makeText(activity, R.string.dialog_feedback_fill_out, Toast.LENGTH_SHORT).show();
//                } else {
//                    // Form filled, send
//                    anim.circularRevealTouch(R.id.actionbar_layout_action, R.id.dialog_feedback_placeholder,
//                            new AccelerateDecelerateInterpolator(), new Runnable() {
//                                @Override
//                                public void run() {
//                                    intent.intentEmail(activity, getString(R.string.dev_email),
//                                            text.getEditText(R.id.dialog_feedback_subject, activity), "Tapad Bug Report",
//                                            text.getEditText(R.id.dialog_feedback_text, activity), null,
//                                            activity.getString(R.string.dialog_feedback_select_email), 400);
//                                    window.hideKeyboard(activity);
//                                }
//                            }, 400, 0, activity);
//                }
//            }
//        });
//
//        window.getView(R.id.actionbar_close, activity).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pressBack();
//            }
//        });
    }

    void hideFeedbackDialog() {
        window.hideKeyboard(activity);
        anim.fadeIn(R.id.dialog_feedback_placeholder, 0, 200, "feedbackOUT", activity);
        anim.circularRevealinpx(R.id.dialog_feedback,
                coord[0], coord[1],
                window.getWindowHypot(activity), 0, new AccelerateDecelerateInterpolator(),
                400, 200, activity);
        isFeedbackVisible = false;
    }

    void setFeedback() {
        text.setEditText(R.id.dialog_feedback_subject, R.id.dialog_feedback_subject_layout, "text", R.string.dialog_song_hint_subject, R.string.dialog_song_hint_wrong_subject, activity);
        text.setEditText(R.id.dialog_feedback_text,    R.id.dialog_feedback_text_layout, "text",    R.string.dialog_song_hint_text,    R.string.dialog_song_hint_wrong_text,    activity);
    }

    void setSchemeInfo() {
        window.getView(R.id.statusbar, activity).setBackgroundColor(getResources().getColor(R.color.dev_dark));
        window.setRecentColor(R.string.task_about_dev, 0, R.color.dev, activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        collapsingToolbarLayout.setTitle(getResources().getString(R.string.berict_full));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.dev));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.dev));
        
        anim.circularRevealTouch(R.id.layout_dev_web, R.id.layout_dev_placeholder,
                new AccelerateDecelerateInterpolator(), new Runnable() {
                    @Override
                    public void run() {
                        intent.intentLink(activity, getResources().getString(R.string.berict_web), 400);
                    }
                }, 400, 0, activity);

        anim.circularRevealTouch(R.id.layout_dev_youtube, R.id.layout_dev_placeholder,
                new AccelerateDecelerateInterpolator(), new Runnable() {
                    @Override
                    public void run() {
                        intent.intentLink(activity, getResources().getString(R.string.berict_youtube), 400);
                    }
                }, 400, 0, activity);

        showFeedbackDialog(R.id.layout_dev_report_bug);

        anim.circularRevealTouch(R.id.layout_dev_rate, R.id.layout_dev_placeholder,
                new AccelerateDecelerateInterpolator(), new Runnable() {
                    @Override
                    public void run() {
                        intent.intentMarket(activity, "com.bedrock.padder", 400);
                    }
                }, 400, 0, activity);

        //anim.circularRevealTouch(R.id.layout_dev_translate, R.id.layout_dev_placeholder,
        //        new AccelerateDecelerateInterpolator(), new Runnable() {
        //            @Override
        //            public void run() {
        //                // open translate in dialog
        //            }
        //        }, 400, 0, activity);

        window.getView(R.id.layout_dev_translate, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, R.string.dev_translate_error, Toast.LENGTH_LONG).show();
            }
        });

        window.getView(R.id.layout_dev_donate, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, R.string.dev_donate_error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
