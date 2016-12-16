package com.bedrock.padder.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bedrock.padder.R;

public class AboutActivity extends AppCompatActivity {

    int json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // get intent values
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            json = extras.getInt("json");
        }
    }
}
