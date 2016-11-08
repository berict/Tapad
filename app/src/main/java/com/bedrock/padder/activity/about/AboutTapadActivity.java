package com.bedrock.padder.activity.about;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.TextService;
import com.bedrock.padder.helper.ThemeService;
import com.bedrock.padder.helper.WindowService;

public class AboutTapadActivity extends AppCompatActivity {

    private WindowService window = new WindowService();
    private AnimService anim = new AnimService();
    private IntentService intent = new IntentService();
    private ThemeService theme = new ThemeService();
    private AppbarService ab = new AppbarService();
    private TextService text = new TextService();

    Activity activity;

    SharedPreferences prefs;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_tapad);

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

        window.setMarginRelativePX(R.id.layout_tapad_relative, 0, prefs.getInt("statBarPX", 0), 0, 0, activity);
        window.getView(R.id.layout_tapad_margin, activity).getLayoutParams().height = prefs.getInt("navBarPX", 0) + window.convertDPtoPX(10, activity);
        window.getView(R.id.statusbar, activity).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        window.setRecentColor(R.string.task_about_tapad, 0, R.color.colorPrimary, activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.tapad));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));

        enterAnim();
        setLink();
    }

    boolean closeFeedback = false;
    @Override
    public void onBackPressed(){
        if(isFeedbackVisible == true){
            if(closeFeedback == false){
                if(!(!text.isTextValid(R.id.dialog_feedback_subject, activity) &&
                     !text.isTextValid(R.id.dialog_feedback_text,    activity) &&
                     !text.isTextValid(R.id.dialog_feedback_link,    activity))) {
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
            anim.fadeOut(R.id.layout_tapad_text, 0, 200, activity);
            anim.fadeOut(R.id.layout_tapad_detail, 0, 200, activity);
            Handler delay = new Handler();
            delay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AboutTapadActivity.super.onBackPressed();
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
                theme.setGone(R.id.layout_tapad_placeholder, 0, activity);
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
        anim.fadeIn(R.id.layout_tapad_text, 400, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_tapad_detail_about, 500, 200, "aboutIn", activity);
        anim.fadeIn(R.id.layout_tapad_detail_info, 600, 200, "infoIn", activity);
        anim.fadeIn(R.id.layout_tapad_detail_other, 600, 200, "otherIn", activity);
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

    int feedbackScheme = 0;

    void showFeedbackDialog(final int touch_id) {
        setFeedback();

        window.getView(touch_id, activity).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[0] = (int)event.getRawX();
                coord[1] = (int)event.getRawY();
                window.hideKeyboard(activity);

                return false;
            }
        });

        window.getView(touch_id, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear editText
                text.clearEditText(R.id.dialog_feedback_link,    activity);
                text.clearEditText(R.id.dialog_feedback_text,    activity);
                text.clearEditText(R.id.dialog_feedback_subject, activity);

                text.clearEditTextLayout(R.id.dialog_feedback_link_layout,    activity);
                text.clearEditTextLayout(R.id.dialog_feedback_text_layout,    activity);
                text.clearEditTextLayout(R.id.dialog_feedback_subject_layout, activity);

                // Gains focus, hide keyboard
                window.hideKeyboard(activity);

                if(touch_id == R.id.layout_tapad_request_song){
                    ab.setTitle(R.string.tapad_song, activity);
                    window.getView(R.id.dialog_feedback_link_layout, activity).setVisibility(View.VISIBLE);
                    feedbackScheme = 1;
                    text.setEditText(R.id.dialog_feedback_text,    R.id.dialog_feedback_text_layout, "text",    R.string.dialog_song_hint_text,    R.string.dialog_song_hint_wrong_text,    activity);
                    window.setRecentColor(R.string.task_about_tapad_song, 0, R.color.colorAccent, activity);
                } else if (touch_id == R.id.layout_tapad_feedback) {
                    ab.setTitle(R.string.tapad_feedback, activity);
                    window.getView(R.id.dialog_feedback_link_layout, activity).setVisibility(View.GONE);
                    feedbackScheme = 2;
                    text.setEditText(R.id.dialog_feedback_text,    R.id.dialog_feedback_text_layout, "text",    R.string.dialog_feedback_hint_text,    R.string.dialog_feedback_hint_wrong_text,    activity);
                    window.setRecentColor(R.string.task_about_tapad_feedback, 0, R.color.colorAccent, activity);
                } else if (touch_id == R.id.layout_tapad_report_bug) {
                    ab.setTitle(R.string.tapad_report_bug, activity);
                    window.getView(R.id.dialog_feedback_link_layout, activity).setVisibility(View.GONE);
                    feedbackScheme = 3;
                    text.setEditText(R.id.dialog_feedback_text,    R.id.dialog_feedback_text_layout, "text",    R.string.dialog_report_bug_hint_text,    R.string.dialog_report_bug_hint_wrong_text,    activity);
                    window.setRecentColor(R.string.task_about_tapad_report_bug, 0, R.color.colorAccent, activity);
                }
                
                anim.circularRevealinpx(R.id.dialog_feedback,
                        coord[0], coord[1],
                        0, window.getWindowHypot(activity), new AccelerateDecelerateInterpolator(),
                        400, 0, activity);
                Handler dialogDelay = new Handler();
                dialogDelay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim.fadeOut(R.id.dialog_feedback_placeholder, 0, 200, activity);
                    }
                }, 400);
                isFeedbackVisible = true;
            }
        });

//        window.getView(R.id.actionbar_layout_action, activity).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Send method
//                if(feedbackScheme == 1){
//                    if((!text.isTextValid(R.id.dialog_feedback_subject, activity) ||
//                        !text.isTextValid(R.id.dialog_feedback_text,    activity) ||
//                        !text.isUriValid(R.id.dialog_feedback_link,    activity))){
//                        // Form not filled
//                        Toast.makeText(activity, R.string.dialog_feedback_fill_out, Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Form filled, send
//                        anim.circularRevealTouch(R.id.actionbar_layout_action, R.id.dialog_feedback_placeholder,
//                                new AccelerateDecelerateInterpolator(), new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        intent.intentEmail(activity, getString(R.string.dev_email),
//                                                text.getEditText(R.id.dialog_feedback_subject, activity), "Tapad Song Request",
//                                                text.getEditText(R.id.dialog_feedback_text, activity), text.getEditText(R.id.dialog_feedback_link, activity),
//                                                activity.getString(R.string.dialog_feedback_select_email), 400);
//                                        window.hideKeyboard(activity);
//                                    }
//                                }, 400, 0, activity);
//                    }
//                } if(feedbackScheme == 2) {
//                    if((!text.isTextValid(R.id.dialog_feedback_subject, activity) ||
//                        !text.isTextValid(R.id.dialog_feedback_text,    activity))){
//                        // Form not filled
//                        Toast.makeText(activity, R.string.dialog_feedback_fill_out, Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Form filled, send
//                        anim.circularRevealTouch(R.id.actionbar_layout_action, R.id.dialog_feedback_placeholder,
//                                new AccelerateDecelerateInterpolator(), new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        intent.intentEmail(activity, getString(R.string.dev_email),
//                                                text.getEditText(R.id.dialog_feedback_subject, activity), "Tapad Feedback",
//                                                text.getEditText(R.id.dialog_feedback_text, activity), null,
//                                                activity.getString(R.string.dialog_feedback_select_email), 400);
//                                        window.hideKeyboard(activity);
//                                    }
//                                }, 400, 0, activity);
//                    }
//                } if(feedbackScheme == 3) {
//                    if((!text.isTextValid(R.id.dialog_feedback_subject, activity) ||
//                        !text.isTextValid(R.id.dialog_feedback_text,    activity))){
//                        // Form not filled
//                        Toast.makeText(activity, R.string.dialog_feedback_fill_out, Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Form filled, send
//                        anim.circularRevealTouch(R.id.actionbar_layout_action, R.id.dialog_feedback_placeholder,
//                                new AccelerateDecelerateInterpolator(), new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        intent.intentEmail(activity, getString(R.string.dev_email),
//                                                text.getEditText(R.id.dialog_feedback_subject, activity), "Tapad Bug Report",
//                                                text.getEditText(R.id.dialog_feedback_text, activity), null,
//                                                activity.getString(R.string.dialog_feedback_select_email), 400);
//                                        window.hideKeyboard(activity);
//                                    }
//                                }, 400, 0, activity);
//                    }
//                }
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
        text.setEditText(R.id.dialog_feedback_link,    R.id.dialog_feedback_link_layout, "uri",     R.string.dialog_song_hint_link,    R.string.dialog_song_hint_wrong_link,    activity);
    }

    void setLink() {
        Log.d("SetLink", "Activated");
        anim.circularRevealTouch(R.id.layout_tapad_update, R.id.layout_tapad_placeholder,
                new AccelerateDecelerateInterpolator(), new Runnable() {
                    @Override
                    public void run() {
                        intent.intentMarket(activity, "com.bedrock.padder", 400);
                    }
                }, 400, 0, activity);
        
        window.getView(R.id.layout_tapad_legal, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(activity)
                        .title(R.string.dialog_legal_title)
                        .content(R.string.dialog_legal_text)
                        .contentColorRes(R.color.dark_primary)
                        .positiveText(R.string.dialog_legal_positive)
                        .positiveColorRes(R.color.colorAccent)
                        .show();
            }
        });

        window.getView(R.id.layout_tapad_changelog, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(activity)
                        .title(R.string.dialog_changelog_title)
                        .content(R.string.dialog_changelog_text)
                        .contentColorRes(R.color.dark_primary)
                        .positiveText(R.string.dialog_changelog_positive)
                        .positiveColorRes(R.color.colorAccent)
                        .show();
            }
        });
        
        window.getView(R.id.layout_tapad_thanks, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(activity)
                        .title(R.string.dialog_thanks_title)
                        .content(R.string.dialog_thanks_text)
                        .contentColorRes(R.color.dark_primary)
                        .positiveText(R.string.dialog_thanks_positive)
                        .positiveColorRes(R.color.colorAccent)
                        .show();
            }
        });

        // anim.circularRevealTouch(R.id.layout_tapad_dev, R.id.layout_tapad_placeholder,
        //         new AccelerateDecelerateInterpolator(), new Runnable() {
        //             @Override
        //             public void run() {
        //                 // open dev fragment in dialog
        //             }
        //         }, 400, 0, activity);

        showFeedbackDialog(R.id.layout_tapad_request_song);

        showFeedbackDialog(R.id.layout_tapad_feedback);

        showFeedbackDialog(R.id.layout_tapad_report_bug);

        anim.circularRevealTouch(R.id.layout_tapad_dev, R.id.layout_tapad_placeholder,
                new AccelerateDecelerateInterpolator(), new Runnable() {
                    @Override
                    public void run() {
                        intent.intent(activity, "activity.about.AboutDevActivity", 400);
                    }
                }, 400, 0, activity);

        anim.circularRevealTouch(R.id.layout_tapad_rate, R.id.layout_tapad_placeholder,
                new AccelerateDecelerateInterpolator(), new Runnable() {
                    @Override
                    public void run() {
                        intent.intentMarket(activity, "com.bedrock.padder", 400);
                    }
                }, 400, 0, activity);

        //anim.circularRevealTouch(R.id.layout_tapad_translate, R.id.layout_tapad_placeholder,
        //        new AccelerateDecelerateInterpolator(), new Runnable() {
        //            @Override
        //            public void run() {
        //                // open translate in dialog
        //            }
        //        }, 400, 0, activity);

        window.getView(R.id.layout_tapad_translate, activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, R.string.tapad_translate_error, Toast.LENGTH_LONG).show();
            }
        });

        anim.circularRevealTouch(R.id.layout_tapad_recommend, R.id.layout_tapad_placeholder,
                new AccelerateDecelerateInterpolator(), new Runnable() {
                    @Override
                    public void run() {
                        intent.intentShareText(activity,
                                getResources().getString(R.string.tapad_recommend_share_subject),
                                getResources().getString(R.string.tapad_recommend_share_text),
                                getResources().getString(R.string.tapad_recommend_share_hint), 400);
                    }
                }, 400, 0, activity);
    }
}
