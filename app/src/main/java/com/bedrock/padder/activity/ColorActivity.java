package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.adapter.ColorAdapter;
import com.bedrock.padder.helper.FabHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.preferences.ItemColor;
import com.bedrock.padder.model.preferences.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.ArrayUtils;

public class ColorActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    private WindowHelper w = new WindowHelper();
    private FabHelper fab = new FabHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    Activity activity = this;
    Preferences preferences = null;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    int color;

    private ItemColor itemColor;
    private ColorAdapter colorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        preferences = new Preferences(this);
        color = preferences.getColor();

        // Set transparent nav bar
        w.setStatusBar(R.color.transparent, activity);
        w.setNavigationBar(R.color.transparent, activity);

        setUi();
    }

    private void setUi() {
        // toolbar
        toolbar.setActionBar(this);
        toolbar.setStatusBarTint(this);
        toolbar.setActionBarPadding(this);
        toolbar.setActionBarDisplayHomeAsUp(true);
        toolbar.setActionBarTitle(R.string.settings_color);
        toolbar.setActionBarColor(R.color.colorAccent, this);
        w.setRecentColor(R.string.task_settings_color, R.color.colorAccent, activity);

        // fab
        fab.set(activity);
        fab.show();
        fab.setIcon(R.drawable.ic_add_white, activity);
        fab.setOnClickListener(new Runnable() {
            @Override
            public void run() {
                showColorChooserDialog();
            }
        });

        // get item color values from Preferences
        itemColor = preferences.getRecentColor();

        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        colorAdapter = new ColorAdapter(
                itemColor,
                R.layout.adapter_color,
                preferences,
                activity
        );
        w.getRecyclerView(R.id.layout_color_recycler_view, activity).setLayoutManager(layoutManager);
        w.getRecyclerView(R.id.layout_color_recycler_view, activity).setNestedScrollingEnabled(false);
        w.getRecyclerView(R.id.layout_color_recycler_view, activity).setAdapter(colorAdapter);

        // adapter margin
        w.setMarginLinearPX(R.id.color_bottom_margin, 0, 0, 0, w.getNavigationBarFromPrefs(activity), activity);

        // set primary color
        setPrimaryColor();
    }

    private void showColorChooserDialog() {
        new ColorChooserDialog.Builder(this, R.string.settings_color_dialog)
                .accentMode(false)
                .titleSub(R.string.settings_color_dialog)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .dynamicButtonColor(true)
                .show();
    }

    private void setPrimaryColor() {
        View colorView[] = {
                findViewById(R.id.view_color_1),
                findViewById(R.id.view_color_2),
                findViewById(R.id.view_color_3),
                findViewById(R.id.view_color_4)
        };

        for (int i = 0; i < 4; i++) {
            try {
                colorView[i].setBackgroundColor(
                        w.getBlendColor(
                                activity.getResources().getColor(color),
                                activity.getResources().getColor(R.color.grey),
                                (0.8f - (0.3f * i))
                        )
                );
            } catch (Resources.NotFoundException e) {
                colorView[i].setBackgroundColor(
                        w.getBlendColor(
                                color,
                                activity.getResources().getColor(R.color.grey),
                                (0.8f - (0.3f * i))
                        )
                );
            }
        }

        try {
            w.getTextView(R.id.layout_color_id, activity).setText(String.format("#%06X", (0xFFFFFF & activity.getResources().getColor(color))));
        } catch (Resources.NotFoundException e) {
            w.getTextView(R.id.layout_color_id, activity).setText(String.format("#%06X", (0xFFFFFF & color)));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (itemColor.getColorButtonRecentLength() <= 0) {
            w.setVisible(R.id.layout_color_placeholder, 0, activity);
        } else {
            w.setGone(R.id.layout_color_placeholder, 0, activity);
        }
        super.onWindowFocusChanged(hasFocus);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        pressBack();
        return true;
    }

    private void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int colorInt) {
        if (ArrayUtils.indexOf(itemColor.getColorButtonRecents(), colorInt) >= 0) {
            // the value exists, show toast
            Toast.makeText(activity, R.string.settings_color_dialog_duplicate, Toast.LENGTH_SHORT).show();
        } else if (itemColor.getColorButton() == colorInt) {
            // the value is same to current color
            Toast.makeText(activity, R.string.settings_color_dialog_duplicate_current, Toast.LENGTH_SHORT).show();
        } else {
            insertNewColor(colorInt);
        }
    }

    private void insertNewColor(int color) {
        itemColor.addColorButtonRecent(color);
        colorAdapter.notifyDataSetChanged();
        // save again to json prefs
        preferences.setRecentColor(itemColor);
    }
}