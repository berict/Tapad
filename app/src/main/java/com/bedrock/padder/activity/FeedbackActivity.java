package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bedrock.padder.BuildConfig;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.FabService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.WindowService;

public class FeedbackActivity extends AppCompatActivity {

    final String TAG = "FeedbackActivity";
    String MODE_TAG = "";

    private FabService fab = new FabService();
    private WindowService w = new WindowService();
    private AppbarService ab = new AppbarService();
    private IntentService intent = new IntentService();

    Activity a = this;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Log.d(TAG, "Sharedprefs initialized");
        prefs = this.getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        Intent intent = getIntent();
        MODE_TAG = intent.getExtras().getString("feedbackMode");

        setFab();
        initUi();

        w.setStatusBar(R.color.transparent, a);
        w.setNavigationBar(R.color.transparent, a);

        w.setMarginRelativePX(R.id.fab, 0, 0, w.convertDPtoPX(20, a), prefs.getInt("navBarPX", 0) + w.convertDPtoPX(20, a), a);
        ab.setStatusHeight(a);
        ab.setColor(R.color.colorFeedback, a);
        ab.setTitle(w.getStringId("task_feedback_" + MODE_TAG), a);
        ab.setNav(3, null, a);
        w.setRecentColor(w.getStringId("task_feedback_" + MODE_TAG), 0, R.color.colorFeedback, a);
    }
    
    private TextInputLayout songNameLayout, songArtistLayout, songLinkLayout, songMessageLayout;
    private EditText songName, songArtist, songLink, songMessage;
    private Spinner songGenre;

    private String sendMessage;
    private String systemInfo;

    private void initUi() {
        switch (MODE_TAG) {
            case "song":
                // layout
                songNameLayout    = (TextInputLayout) findViewById(R.id.feedback_song_name_input_layout);
                songArtistLayout  = (TextInputLayout) findViewById(R.id.feedback_song_artist_input_layout);
                songLinkLayout    = (TextInputLayout) findViewById(R.id.feedback_song_link_input_layout);
                songMessageLayout = (TextInputLayout) findViewById(R.id.feedback_song_message_input_layout);
                // edittext
                songName          = (EditText) findViewById(R.id.feedback_song_name_input);
                songArtist        = (EditText) findViewById(R.id.feedback_song_artist_input);
                songLink          = (EditText) findViewById(R.id.feedback_song_link_input);
                songMessage       = (EditText) findViewById(R.id.feedback_song_message_input);
                // spinner
                songGenre         = (Spinner) findViewById(R.id.feedback_song_genre_spinner);

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
                            songGenreString = "";
                        }
                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                break;
            case "feedback":

                break;
            case "report_bug":

                break;
        }
    }

    private String songNameString;
    private String songArtistString;
    private String songLinkString;
    private String songMessageString;
    private String songGenreString;

    private boolean validateEditText(EditText editText) {
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
                break;
            case "report_bug":
                break;
        }
        return false;
    }

    private boolean validateMusicUrl(String url) {
        if (url.contains("youtube.com/watch?v=") ||
                url.contains("youtu.be/") ||
                url.contains("soundcloud.com/")) {
            // legit url
            return true;
        }
        return false;
    }

    private boolean validateAll() {
        switch (MODE_TAG) {
            case "song":
                if (validateEditText(songName) &&
                        validateEditText(songArtist) &&
                        validateEditText(songLink) &&
                        validateEditText(songMessage) &&
                        songGenreString.isEmpty() == false) {
                    return true;
                }
                break;
            case "feedback":

                break;
            case "report_bug":

                break;
        }
        return false;
    }

    private String br = System.getProperty("line.separator");

    private void send() {
        if (validateAll() == true) {
            systemInfo = "Version code = " + BuildConfig.VERSION_CODE + br +
                    "Version name = \"" + BuildConfig.VERSION_NAME + "\"" + br +
                    "Android version = \"" + Build.VERSION.CODENAME + "\" (" + Build.VERSION.SDK_INT + ")" + br +
                    "Device model (Product) = " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")" + br +
                    "Device screen = " +
                        getWindow().getWindowManager().getDefaultDisplay().getWidth()  + " (W) X " +
                        getWindow().getWindowManager().getDefaultDisplay().getHeight() + " (H)" + br +
                    "Screen density = " + getResources().getDisplayMetrics().densityDpi + " dpi";
            switch (MODE_TAG) {
                case "song":
                    sendMessage =   "Song title   = \"" + songNameString    + "\"" + br +
                                    "Artist name  = \"" + songArtistString  + "\"" + br +
                                    "Song genre   = \"" + songGenreString   + "\"" + br +
                                    "Song link    = \"" + songLinkString    + "\"" + br + // TODO check auto br or not
                                    "User message = \"" + songMessageString + "\"" + br + br +
                                    systemInfo + br + br +
                                    "### Do not edit the subject and the message to get reply ###";
                    intent.intentEmail(a, R.string.feedback_email,
                            "Tapad Feedback - Song Request", sendMessage,
                            "Select a email client to send feedback", 0);
                    break;
                case "feedback":

                    break;
                case "report_bug":

                    break;
            }
        } else {
            Toast.makeText(a, w.getStringId("feedback_" + MODE_TAG + "_send_error"), Toast.LENGTH_SHORT).show();
        }
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
                send();
            }
        });
    }

    private class mTextWatcher implements TextWatcher {

        private View view;

        private mTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            validateEditText((EditText)view);
        }
    }
}
