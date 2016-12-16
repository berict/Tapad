package com.bedrock.padder.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.WindowService;

public class AboutActivity extends AppCompatActivity {

    Activity activity = this;
    int json;
    WindowService window = new WindowService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // get intent values
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            json = extras.getInt("json");
        }

        window.getTextView(R.id.about_textview, activity).setText(json);
    }
}
