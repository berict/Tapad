package com.bedrock.padder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bedrock.padder.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        String mode = intent.getExtras().getString("feedbackMode");

        TextView textView = (TextView)findViewById(R.id.test);
        textView.setText(mode);
    }
}
