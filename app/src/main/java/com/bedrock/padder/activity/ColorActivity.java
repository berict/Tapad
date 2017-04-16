package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.FabService;
import com.bedrock.padder.helper.WindowService;

import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class ColorActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    private WindowService w = new WindowService();
    private FabService fab = new FabService();
    private AppbarService ab = new AppbarService();

    Activity activity = this;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);

        setFab();
        setAppBar();

        // Set transparent nav bar
        w.setStatusBar(R.color.transparent, activity);
        w.setNavigationBar(R.color.transparent, activity);

        ab.setStatusHeight(activity);
    }

    private void setFab() {
        fab.setFab(activity);
        fab.show();
        fab.setImage(R.drawable.icon_add);
        fab.onClick(new Runnable() {
            @Override
            public void run() {
                showColorChooserDialog();
            }
        });

        // set bottom margin
        w.setMarginRelativePX(R.id.fab, 0, 0,
                w.convertDPtoPX(20, activity),
                w.getNavigationBarFromPrefs(activity) + w.convertDPtoPX(20, activity),
                activity);
    }

    private void setAppBar() {
        ab.setNav(1, null, activity);
        ab.setTitle(R.string.task_settings_color, activity);
        ab.setColor(R.color.colorAccent, activity);
        w.setRecentColor(R.string.task_settings_color, 0, R.color.colorAccent, activity);
    }

    private void showColorChooserDialog() {
        new ColorChooserDialog.Builder(this, R.string.dialog_color)
                .accentMode(false)
                .titleSub(R.string.dialog_color)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .dynamicButtonColor(true)
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int colorInt) {
        getJson(colorInt);
    }

    private String getJson(int color) {

        return "";
    }
}