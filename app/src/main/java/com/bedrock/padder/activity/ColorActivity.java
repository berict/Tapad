package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.WindowService;

import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class ColorActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    private WindowService w = new WindowService();

    Activity activity = this;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int colorInt) {
        // TODO edit
    }
}
