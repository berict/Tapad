package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.adapter.ColorAdapter;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.FabService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.app.theme.ColorData;
import com.google.gson.Gson;

import org.apache.commons.lang3.ArrayUtils;

import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class ColorActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    private WindowService w = new WindowService();
    private FabService fab = new FabService();
    private AppbarService ab = new AppbarService();

    Activity activity = this;
    SharedPreferences prefs = null;
    Gson gson = new Gson();
    int color;

    private ColorData colorData;
    private ColorAdapter colorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);

        // Set transparent nav bar
        w.setStatusBar(R.color.transparent, activity);
        w.setNavigationBar(R.color.transparent, activity);

        ab.setStatusHeight(activity);
        color = prefs.getInt("color", R.color.cyan_400);

        setUi();
    }

    private void setUi() {
        // appbar
        ab.setNav(1, null, activity);
        ab.setTitle(R.string.task_settings_color, activity);
        ab.setColor(R.color.colorAccent, activity);
        w.setRecentColor(R.string.task_settings_color, 0, R.color.colorAccent, activity);

        // fab
        fab.setFab(activity);
        fab.show();
        fab.setImage(R.drawable.icon_add);
        fab.onClick(new Runnable() {
            @Override
            public void run() {
                showColorChooserDialog();
            }
        });

        String colorDataJson = prefs.getString("colorData", null);
        Log.i("ColorData", colorDataJson);

        colorData = gson.fromJson(colorDataJson, ColorData.class);
        Log.i("ColorData", colorDataJson);

        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        colorAdapter = new ColorAdapter(
                colorData,
                R.layout.adapter_color,
                prefs,
                activity
        );
        w.getRecyclerView(R.id.layout_color_recyclerview, activity).setLayoutManager(layoutManager);
        w.getRecyclerView(R.id.layout_color_recyclerview, activity).setNestedScrollingEnabled(false);
        w.getRecyclerView(R.id.layout_color_recyclerview, activity).setAdapter(colorAdapter);

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
        if (colorData.getColorButtonFavoriteLength() <= 0) {
            w.setVisible(R.id.layout_color_placeholder, 0, activity);
        } else {
            w.setGone(R.id.layout_color_placeholder, 0, activity);
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int colorInt) {
        if (ArrayUtils.indexOf(colorData.getColorButtonFavorites(), colorInt) >= 0) {
            // the value exists, show toast
            Toast.makeText(activity, R.string.settings_color_dialog_duplicate, Toast.LENGTH_SHORT).show();
        } else if (colorData.getColorButton() == colorInt) {
            // the value is same to current color
            Toast.makeText(activity, R.string.settings_color_dialog_duplicate_current, Toast.LENGTH_SHORT).show();
        } else {
            insertNewColor(colorInt);
        }
    }

    private void insertNewColor(int color) {
        colorData.addColorButtonFavorite(color);
        colorAdapter.notifyDataSetChanged();
        // save again to json prefs
        prefs.edit().putString("colorData", gson.toJson(colorData)).apply();
        Log.d("Prefs", "colorData : " + prefs.getString("colorData", null));
    }
}