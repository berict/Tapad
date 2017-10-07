package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.BuildConfig;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.FabHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;

public class FeedbackActivity extends AppCompatActivity {

    final String TAG = "FeedbackActivity";
    String MODE_TAG = "";

    private FabHelper fab = new FabHelper();
    private WindowHelper w = new WindowHelper();
    private IntentHelper intent = new IntentHelper();
    private AnimateHelper anim = new AnimateHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    Activity a = this;

    private String br = System.getProperty("line.separator");
    private String sendMessage;
    private String systemInfo;
    private int circularRevealDuration = 400;
    private boolean feedbackSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        MODE_TAG = intent.getExtras().getString("feedbackMode");

        setFab();
        initUi();

        toolbar.setActionBar(this);
        toolbar.setActionBarDisplayHomeAsUpIcon(R.drawable.ic_close_white);
        toolbar.setStatusBarTint(this);
        toolbar.setActionBarTitle(w.getStringId("task_feedback_" + MODE_TAG));
        toolbar.setActionBarColor(R.color.colorFeedback, a);
        w.setStatusBar(R.color.colorFeedbackDark, a);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        w.setRecentColor(w.getStringId("task_feedback_" + MODE_TAG), R.color.colorFeedback, a);

        systemInfo = "Version code = " + BuildConfig.VERSION_CODE + br +
                "Version name = \"" + BuildConfig.VERSION_NAME + "\"" + br +
                "Android version = " + Build.VERSION.RELEASE + " (" + Build.VERSION.SDK_INT + ")" + br +
                "Device model (Product) = " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")" + br +
                "Device screen = " +
                getWindow().getWindowManager().getDefaultDisplay().getWidth()  + " (W) X " +
                getWindow().getWindowManager().getDefaultDisplay().getHeight() + " (H)" + br +
                "Screen density = " + getResources().getDisplayMetrics().densityDpi + " dpi";
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        pressBack();
        return true;
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        this.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        this.dispatchKeyEvent(kUp);
    }

    private TextInputLayout songNameLayout, songArtistLayout, songLinkLayout, songMessageLayout;
    private EditText songName, songArtist, songLink, songMessage;
    private Spinner songGenre;
    private boolean songGenreValidateFirstRun = true;

    private String songNameString;
    private String songArtistString;
    private String songLinkString;
    private String songMessageString;
    private String songGenreString;

    private TextInputLayout feedbackMessageLayout;
    private EditText feedbackMessage;
    private Spinner feedbackType;
    private boolean feedbackTypeValidateFirstRun = true;

    private String feedbackMessageString;
    private String feedbackTypeString;

    private TextInputLayout reportBugMessageLayout;
    private EditText reportBugMessage;
    private Spinner reportBugType;
    private boolean reportBugTypeValidateFirstRun = true;

    private String reportBugMessageString;
    private String reportBugTypeString;

    private void initUi() {
        switch (MODE_TAG) {
            case "song":
                // layout
                RelativeLayout songLayout = (RelativeLayout) findViewById(R.id.feedback_song);
                // input layout
                songNameLayout    = (TextInputLayout) findViewById(R.id.feedback_song_name_input_layout);
                songArtistLayout  = (TextInputLayout) findViewById(R.id.feedback_song_artist_input_layout);
                songLinkLayout    = (TextInputLayout) findViewById(R.id.feedback_song_link_input_layout);
                songMessageLayout = (TextInputLayout) findViewById(R.id.feedback_song_message_input_layout);
                // editText
                songName          = (EditText) findViewById(R.id.feedback_song_name_input);
                songArtist        = (EditText) findViewById(R.id.feedback_song_artist_input);
                songLink          = (EditText) findViewById(R.id.feedback_song_link_input);
                songMessage       = (EditText) findViewById(R.id.feedback_song_message_input);
                // spinner
                songGenre         = (Spinner) findViewById(R.id.feedback_song_genre_spinner);

                songLayout.setVisibility(View.VISIBLE);
                songName   .addTextChangedListener(new mTextWatcher(songName   ));
                songArtist .addTextChangedListener(new mTextWatcher(songArtist ));
                songLink   .addTextChangedListener(new mTextWatcher(songLink   ));
                songMessage.addTextChangedListener(new mTextWatcher(songMessage));
                songGenre  .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            // not the first item (placeholder)
                            songGenreString = getResources().getStringArray(R.array.feedback_song_genre_array)[position];
                            Log.d(TAG, songGenreString);
                        } else {
                            // first item
                            songGenreString = "";
                        }
                        validateSpinner(songGenreValidateFirstRun, R.id.feedback_song_genre_spinner_error, songGenreString);
                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                break;
            case "feedback":
                // layout
                RelativeLayout feedbackLayout = (RelativeLayout) findViewById(R.id.feedback_feedback);
                // input layout
                feedbackMessageLayout = (TextInputLayout) findViewById(R.id.feedback_feedback_message_input_layout);
                // editText
                feedbackMessage       = (EditText) findViewById(R.id.feedback_feedback_message_input);
                // spinner
                feedbackType          = (Spinner) findViewById(R.id.feedback_feedback_type_spinner);

                feedbackLayout.setVisibility(View.VISIBLE);
                feedbackMessage.addTextChangedListener(new mTextWatcher(feedbackMessage));
                feedbackType   .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            // not the first item (placeholder)
                            feedbackTypeString = getResources().getStringArray(R.array.feedback_feedback_type_array)[position];
                            Log.d(TAG, feedbackTypeString);
                        } else {
                            // first item
                            feedbackTypeString = "";
                            Log.d(TAG, "null");
                        }
                        validateSpinner(feedbackTypeValidateFirstRun, R.id.feedback_feedback_type_spinner_error, feedbackTypeString);
                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                break;
            case "report_bug":
                // layout
                RelativeLayout reportBugLayout = (RelativeLayout) findViewById(R.id.feedback_report_bug);
                // input layout
                reportBugMessageLayout = (TextInputLayout) findViewById(R.id.feedback_report_bug_message_input_layout);
                // editText
                reportBugMessage       = (EditText) findViewById(R.id.feedback_report_bug_message_input);
                // spinner
                reportBugType          = (Spinner) findViewById(R.id.feedback_report_bug_type_spinner);

                reportBugLayout.setVisibility(View.VISIBLE);
                reportBugMessage.addTextChangedListener(new mTextWatcher(reportBugMessage));
                reportBugType   .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        View quickFix  = findViewById(R.id.feedback_report_bug_type_quickfix);
                        if (position == 2) {
                            // custom quickfix
                            reportBugTypeString = getResources().getStringArray(R.array.feedback_report_bug_type_array)[position];
                            if (quickFix.getVisibility() == View.INVISIBLE) {
                                anim.fadeIn(R.id.feedback_report_bug_type_quickfix, 0, 100, "errorQuickFix", a);
                            }
                        } else if (position != 0) {
                            // not the first item (placeholder)
                            reportBugTypeString = getResources().getStringArray(R.array.feedback_report_bug_type_array)[position];
                            Log.d(TAG, reportBugTypeString);
                            if (quickFix.getVisibility() == View.VISIBLE) {
                                anim.fadeOutInvisible(R.id.feedback_report_bug_type_quickfix, 0, 100, a);
                            }
                        } else {
                            // first item
                            reportBugTypeString = "";
                            Log.d(TAG, "null");
                            if (quickFix.getVisibility() == View.VISIBLE) {
                                anim.fadeOutInvisible(R.id.feedback_report_bug_type_quickfix, 0, 100, a);
                            }
                        }
                        validateSpinner(reportBugTypeValidateFirstRun, R.id.feedback_report_bug_type_spinner_error, reportBugTypeString);
                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                w.setOnClick(R.id.feedback_report_bug_type_quickfix_action, new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(a)
                                .content(R.string.feedback_report_bug_type_quickfix_content)
                                .contentColorRes(R.color.dark_secondary)
                                .positiveText(R.string.feedback_report_bug_type_quickfix_neutral)
                                .positiveColorRes(R.color.colorAccent)
                                .show();
                    }
                }, a);
                break;
        }

        View learnMore = findViewById(R.id.feedback_disclaimer_action);
        learnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(a)
                        .title(R.string.feedback_disclaimer_dialog_title)
                        .content(systemInfo)
                        .contentColorRes(R.color.dark_primary)
                        .positiveText(R.string.feedback_disclaimer_dialog_action)
                        .positiveColorRes(R.color.colorAccent)
                        .show();
            }
        });
    }

    private boolean validateSpinner(boolean firstRun, int errorViewId, String spinnerCurrentItemString) {
        if (firstRun) {
            switch (MODE_TAG) {
                case "song":
                    songGenreValidateFirstRun = false;
                    break;
                case "feedback":
                    feedbackTypeValidateFirstRun = false;
                    break;
                case "report_bug":
                    reportBugTypeValidateFirstRun = false;
                    break;
            }
            return false;
        } else {
            View errorView = findViewById(errorViewId);
            if (spinnerCurrentItemString.isEmpty()) {
                // empty, first item
                anim.fadeIn(errorViewId, 0, 100, "errorFadeIn", a);
                return false;
            } else {
                if (errorView.getVisibility() == View.VISIBLE) {
                    anim.fadeOutInvisible(errorViewId, 0, 100, a);
                }
                return true;
            }
        }
    }

    private boolean validateEditText(EditText editText) {
        Log.d(TAG, "validateEditText");
        switch (MODE_TAG) {
            case "song":
                if (editText == songName) {
                    if (songName.getText().toString().trim().isEmpty()) {
                        // string empty
                        songNameLayout.setError(getResources().getString(R.string.feedback_song_name_input_error));
                        songName.requestFocus();
                        return false;
                    } else {
                        songNameLayout.setErrorEnabled(false);
                        songNameString = songName.getText().toString();
                    }
                    return true;
                }
                if (editText == songArtist) {
                    if (songArtist.getText().toString().trim().isEmpty()) {
                        // string empty
                        songArtistLayout.setError(getResources().getString(R.string.feedback_song_artist_input_error));
                        songArtist.requestFocus();
                        return false;
                    } else {
                        songArtistLayout.setErrorEnabled(false);
                        songArtistString = songArtist.getText().toString();
                    }
                    return true;
                }
                if (editText == songLink) {
                    if (songLink.getText().toString().trim().isEmpty()) {
                        // string empty
                        songLinkLayout.setError(getResources().getString(R.string.feedback_song_link_input_error));
                        songLink.requestFocus();
                        return false;
                    } else if (validateMusicUrl(songLink.getText().toString()) == false) {
                        songLinkLayout.setError(getResources().getString(R.string.feedback_song_link_input_url_error));
                        songLink.requestFocus();
                        return false;
                    } else {
                        songLinkLayout.setErrorEnabled(false);
                        songLinkString = songLink.getText().toString();
                    }
                    return true;
                }
                if (editText == songMessage) {
                    songMessageLayout.setErrorEnabled(false);
                    songMessageString = songMessage.getText().toString();
                    return true;
                }
                break;
            case "feedback":
                if (editText == feedbackMessage) {
                    if (feedbackMessage.getText().toString().trim().isEmpty()) {
                        // string empty
                        feedbackMessageLayout.setErrorTextAppearance(R.style.editTextErrorDefault);
                        feedbackMessageLayout.setError(getResources().getString(R.string.feedback_feedback_message_input_error_empty));
                        feedbackMessage.requestFocus();
                        return false;
                    } else if (feedbackMessage.getText().toString().length() < 15) {
                        // too short
                        feedbackMessageLayout.setErrorTextAppearance(R.style.editTextErrorGrey);
                        feedbackMessageLayout.setError(
                                (15 - feedbackMessage.getText().toString().length()) + " " +
                                getResources().getString(R.string.feedback_feedback_message_input_error_too_short)
                        );
                        feedbackMessage.requestFocus();
                        return false;
                    } else {
                        feedbackMessageLayout.setErrorEnabled(false);
                        feedbackMessageString = feedbackMessage.getText().toString();
                    }
                    return true;
                }
                break;
            case "report_bug":
                if (editText == reportBugMessage) {
                    if (reportBugMessage.getText().toString().trim().isEmpty()) {
                        // string empty
                        reportBugMessageLayout.setErrorTextAppearance(R.style.editTextErrorDefault);
                        reportBugMessageLayout.setError(getResources().getString(R.string.feedback_report_bug_message_input_error_empty));
                        reportBugMessage.requestFocus();
                        return false;
                    } else if (reportBugMessage.getText().toString().length() < 15) {
                        // too short
                        reportBugMessageLayout.setErrorTextAppearance(R.style.editTextErrorGrey);
                        reportBugMessageLayout.setError(
                                (15 - reportBugMessage.getText().toString().length()) + " " +
                                        getResources().getString(R.string.feedback_report_bug_message_input_error_too_short)
                        );
                        reportBugMessage.requestFocus();
                        return false;
                    } else {
                        reportBugMessageLayout.setErrorEnabled(false);
                        reportBugMessageString = reportBugMessage.getText().toString();
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean validateMusicUrl(String url) {
        return (validateUrlWithLength(url, "youtube.com/watch?v=", 31) ||
                validateUrlWithLength(url, "youtu.be/", 20) ||
                validateUrlWithLength(url, "soundcloud.com/", 16));
    }

    private boolean validateUrlWithLength(String url, String validateUrl, int validateUrlLength) {
        if (url.contains(validateUrl) && url.length() >= validateUrlLength) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateAll() {
        switch (MODE_TAG) {
            case "song":
                if (validateEditText(songName) &
                        validateEditText(songArtist) &
                        validateEditText(songLink) &
                        validateEditText(songMessage) &
                        validateSpinner(songGenreValidateFirstRun, R.id.feedback_song_genre_spinner_error, songGenreString)) {
                    return true;
                }
                break;
            case "feedback":
                if (validateEditText(feedbackMessage) &
                        validateSpinner(feedbackTypeValidateFirstRun, R.id.feedback_feedback_type_spinner_error, feedbackTypeString)) {
                    return true;
                }
                break;
            case "report_bug":
                if (validateEditText(reportBugMessage) &
                        validateSpinner(reportBugTypeValidateFirstRun, R.id.feedback_report_bug_type_spinner_error, reportBugTypeString)) {
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean send() {
        if (validateAll() == true) {
            switch (MODE_TAG) {
                case "song":
                    sendMessage =   "Song title   = \"" + songNameString    + "\"" + br +
                                    "Artist name  = \"" + songArtistString  + "\"" + br +
                                    "Song genre   = \"" + songGenreString   + "\"" + br +
                                    "Song link    = \"" + songLinkString    + "\"" + br +
                                    "User message = \"" + songMessageString + "\"" + br + br +
                                    systemInfo + br + "FeedbackId0" + br + br +
                                    "### Do not edit the subject and the message to receive a reply ###";
                    intent.intentEmail(a, R.string.feedback_email,
                            "Tapad Feedback - Song Request", sendMessage,
                            R.string.feedback_email_client_feedback, circularRevealDuration);
                    break;
                case "feedback":
                    sendMessage =   "Feedback type    = \"" + feedbackTypeString    + "\"" + br +
                                    "Feedback message = \"" + feedbackMessageString + "\"" + br + br +
                                    systemInfo + br + "FeedbackId1" + br + br +
                            "### Do not edit the subject and the message to receive a reply ###";
                    intent.intentEmail(a, R.string.feedback_email,
                            "Tapad Feedback - Feedback [" + feedbackTypeString + "]", sendMessage,
                            R.string.feedback_email_client_feedback, circularRevealDuration);
                    break;
                case "report_bug":
                    sendMessage =
                            "Bug type   = \"" + reportBugTypeString    + "\"" + br +
                            "Bug detail = \"" + reportBugMessageString + "\"" + br + br +
                            systemInfo + br + "FeedbackId2" + br + br +
                            "### Do not edit the subject and the message to receive a reply ###";
                    intent.intentEmail(a, R.string.feedback_email,
                            "Tapad Feedback - Bug Report [" + reportBugTypeString + "]", sendMessage,
                            R.string.feedback_email_client_bug_report, circularRevealDuration);
                    break;
            }
            feedbackSent = true;
            return true;
        } else {
            Toast.makeText(a, w.getStringId("feedback_" + MODE_TAG + "_send_error"), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private MaterialDialog onBackPressedDialog;

    @Override
    public void onBackPressed() {
        if (feedbackSent == false) {
            String currentContentTitle = w.getStringFromId("feedback_" + MODE_TAG + "_discard", a);

            onBackPressedDialog = new MaterialDialog.Builder(a)
                    .content(currentContentTitle)
                    .positiveText(R.string.feedback_quit_dialog_negative)
                    .positiveColorRes(R.color.red)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            FeedbackActivity.super.onBackPressed();
                        }
                    })
                    .negativeText(R.string.feedback_quit_dialog_positive)
                    .negativeColorRes(R.color.colorAccent)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            onBackPressedDialog.dismiss();
                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private int focusCount = 0;
    // count 2 for sent / canceled email

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        focusCount++;
        Log.d("FocusCount", String.valueOf(focusCount));
        if (focusCount == 2) {
            if (feedbackSent == true) {
                w.getView(R.id.layout_placeholder, a).setVisibility(View.GONE);
                w.setRecentColor(w.getStringId("task_feedback_" + MODE_TAG), R.color.colorFeedback, a);
            }
            focusCount = 0;
        }
        super.onWindowFocusChanged(hasFocus);
    }

    void setFab() {
        fab.set(a);
        fab.show();
        fab.setIcon(R.drawable.ic_send_white, a);
        fab.setOnClickListener(new Runnable() {
            @Override
            public void run() {
                focusCount = 0;
                if (send() == true) {
                    w.setRecentColor(w.getStringId("task_feedback_" + MODE_TAG), 0, R.color.colorAccent, a);
                }
            }
        });
    }

    private class mTextWatcher implements TextWatcher {

        private View view;

        private mTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            validateEditText((EditText)view);
        }
    }
}
