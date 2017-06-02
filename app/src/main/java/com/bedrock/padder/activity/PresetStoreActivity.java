package com.bedrock.padder.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.adapter.PresetStoreAdapter;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.FileService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.ToolbarService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.FirebaseMetadata;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;

import static com.bedrock.padder.activity.MainActivity.isDeckShouldCleared;
import static com.bedrock.padder.activity.MainActivity.isPresetVisible;

public class PresetStoreActivity extends AppCompatActivity {

    Activity activity = this;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private WindowService window = new WindowService();
    private AnimService anim = new AnimService();
    private ToolbarService toolbar = new ToolbarService();
    private IntentService intent = new IntentService();
    private FileService fileService = new FileService();

    private int themeColor;
    private String themeTitle;
    private String TAG = "PresetStore";

    public static boolean isPresetDownloading = false;
    private boolean shouldAdapterRefreshed = false;

    private static final int REQUEST_WRITE_STORAGE = 112;
    public PresetStoreAdapter presetStoreAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_store);

        isPresetVisible = true;

        themeColor = getResources().getColor(R.color.amber);
        themeTitle = getResources().getString(R.string.preset_store);

        // initialize firebase
        FirebaseApp.initializeApp(activity);

        toolbar.setActionBar(this);
        toolbar.setActionBarDisplayHomeAsUp(true);
        toolbar.setStatusBarTint(this);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        window.setNavigationBar(R.color.transparent, activity);

        View statusbar = findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT < 21) {
            statusbar.setVisibility(View.GONE);
        } else {
            try {
                statusbar.getLayoutParams().height = window.getStatusBarFromPrefs(activity);
            } catch (NullPointerException e) {
                Log.d("NullExp", e.getMessage());
                statusbar.setVisibility(View.GONE);
            }
        }

        window.setMarginRelativePX(R.id.layout_relative, 0, window.getStatusBarFromPrefs(activity), 0, 0, activity);
        window.getView(R.id.layout_margin, activity).getLayoutParams().height = window.getNavigationBarFromPrefs(activity) + window.convertDPtoPX(10, activity);

        themeColor = activity.getResources().getColor(R.color.amber);
        themeTitle = activity.getResources().getString(R.string.preset_store);

        enterAnim();
        setUi();

        isDeckShouldCleared = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // reload my activity with permission granted or use the features what required the permission
                    downloadMetadata();
                } else {
                    // show dialog to grant access
                    new MaterialDialog.Builder(activity)
                            .title(R.string.preset_store_permission_dialog_title)
                            .titleColorRes(R.color.dark_primary)
                            .content(R.string.preset_store_permission_dialog_text)
                            .contentColorRes(R.color.dark_action)
                            .positiveText(R.string.preset_store_permission_dialog_positive)
                            .positiveColorRes(R.color.dark_primary)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // go to settings
                                    intent.intentAppDetailSettings(activity, 0);
                                    shouldAdapterRefreshed = true;
                                }
                            })
                            .negativeText(R.string.preset_store_permission_dialog_negative)
                            .negativeColorRes(R.color.red_400)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // close current activity
                                    finish(100);
                                }
                            })
                            .show();
                    Log.e(TAG, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission");
                }
        }
    }

    private void finish(int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, delay);
    }

    String tapadFolderPath = Environment.getExternalStorageDirectory().getPath() + "/Tapad";
    String metadataLocation = tapadFolderPath + "/presets/metadata.txt";

    private void setLoadingFinished(boolean isFinished) {
        if (isFinished) {
            // finished, hide loading and show recyclerview
            Log.d(TAG, "Loading finished");
            anim.fadeOut(R.id.layout_preset_store_recyclerview_loading, 0, 200, activity);
            anim.fadeIn(R.id.layout_preset_store_recyclerview, 200, 200, "rvIn", activity);
        } else {
            // started, show loading
            anim.fadeOut(R.id.layout_preset_store_recyclerview, 0, 200, activity);
            anim.fadeIn(R.id.layout_preset_store_recyclerview_loading, 200, 200, "rvLoadingIn", activity);
        }
    }

    private void downloadMetadata() {
        // loading start
        setLoadingFinished(false);
        Log.d(TAG, "downloadMetaData");

        boolean hasPermission =
                ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        // Make sdcard/Tapad folder
        File folder = new File(tapadFolderPath);
        if (folder.mkdirs()) {
            Log.i(TAG, "folder successfully created");
        } else {
            // folder exists
            Log.e(TAG, "folder already exists");
        }

        // Make sdcard/Tapad/presets folder
        File presets = new File(tapadFolderPath + "/presets");
        if (presets.mkdirs()) {
            Log.i(TAG, "folder successfully created");
        } else {
            // folder exists
            Log.e(TAG, "folder already exists");
        }

        final File metadata = new File(tapadFolderPath + "/presets/metadata.txt");

        StorageReference metadataReference =
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl("gs://tapad-4d342.appspot.com/presets")
                        .child("metadata.txt");
        metadataReference.getFile(metadata).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Successful download at " + metadata.toString());
                setAdapter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to download");
            }
        });
    }

    private void setUi() {
        // status bar
        window.getView(R.id.statusbar, activity).setBackgroundColor(themeColor);

        // action bar
        collapsingToolbarLayout.setContentScrimColor(themeColor);
        collapsingToolbarLayout.setStatusBarScrimColor(themeColor);
        collapsingToolbarLayout.setTitle(themeTitle);

        // set taskDesc
        window.setRecentColor(themeTitle, themeColor, activity);

        // title image / text
        window.getImageView(R.id.layout_image, activity).setImageResource(R.drawable.about_image_preset_store);

        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        window.getRecyclerView(R.id.layout_preset_store_recyclerview, activity).setLayoutManager(layoutManager);
        window.getRecyclerView(R.id.layout_preset_store_recyclerview, activity).setNestedScrollingEnabled(false);
        // firebase check
        setAdapter();
    }

    private void setAdapter() {
        Log.d(TAG, "setAdapter");
        // attach the adapter to the layout
        if (new File(metadataLocation).exists()) {
            // metadata file exists
            String metadata = fileService.getStringFromFile(metadataLocation);
            if (isFirebaseMetadataUpdated(activity)) {
                // updated, download new one
                downloadMetadata();
            } else {
                Log.d(TAG, "Attached adapter");
                // offline or not updated, continue
                Gson gson = new Gson();
                FirebaseMetadata firebaseMetadata = gson.fromJson(metadata, FirebaseMetadata.class);
                if (firebaseMetadata == null ||
                        firebaseMetadata.getPresets() == null ||
                        firebaseMetadata.getVersionCode() == null) {
                    // corrupted metadata, download again
                    downloadMetadata();
                } else {
                    // attach adapter while its not null
                    presetStoreAdapter = new PresetStoreAdapter(
                            firebaseMetadata,
                            R.layout.adapter_preset_store, activity
                    );
                    window.getRecyclerView(R.id.layout_preset_store_recyclerview, activity).setAdapter(presetStoreAdapter);
                }
                setLoadingFinished(true);
            }
        } else {
            downloadMetadata();
        }
    }

    private boolean isConnected(Context context) {
        // returns whether the device is connected to the internet
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private boolean isFMUpdated = false;

    private boolean isFirebaseMetadataUpdated(Context context) {
        isFMUpdated = false;
        if (isConnected(context)) {
            Log.d(TAG, "Connected to the internet");
            StorageReference metadataReference =
                    FirebaseStorage
                            .getInstance()
                            .getReferenceFromUrl("gs://tapad-4d342.appspot.com/presets")
                            .child("metadata.txt");
            metadataReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    Log.d(TAG, "Successful getting metadata");
                    if (storageMetadata.getUpdatedTimeMillis() > new File(metadataLocation).lastModified()) {
                        // firebase metadata is updated since last download
                        // get the new updated metadata
                        Log.d(TAG, "File updated");
                        isFMUpdated = true;
                    } else {
                        Log.d(TAG, "File not updated");
                        isFMUpdated = false;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Failed to get metadata");
                    isFMUpdated =  false;
                }
            });
            return isFMUpdated;
        } else {
            Log.d(TAG, "Disconnected from the internet");
            return isFMUpdated;
        }
    }

    @Override
    public void onBackPressed() {
        anim.fadeOut(R.id.layout_preset_store, 0, 200, activity);
        anim.fadeOut(R.id.layout_text, 100, 200, activity);
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                PresetStoreActivity.super.onBackPressed();
            }
        }, 300);
        // TODO add notification
//        if (isPresetDownloading) {
//            new MaterialDialog.Builder(activity)
//                    .title(R.string.preset_store_exit_warning_dialog_title)
//                    .content(R.string.preset_store_exit_warning_dialog_text)
//                    .contentColorRes(R.color.dark_primary)
//                    .positiveText(R.string.preset_store_exit_warning_dialog_positive)
//                    .positiveColorRes(R.color.colorAccent)
//                    .negativeText(R.string.preset_store_exit_warning_dialog_negative)
//                    .onNegative(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            // cancel tasks and exit
//                            currentFirebaseService.cancelDownloadPreset();
//                            currentFileService.cancelDecompress();
//                        }
//                    })
//                    .show();
//        } else {
//        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        pressBack();
        return true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (shouldAdapterRefreshed) {
            // reload adapter and download metadata
            setAdapter();
            shouldAdapterRefreshed = false;
        }

        if (hasFocus) {
            window.setGone(R.id.layout_placeholder, 0, activity);
            // reset taskDesc
            window.setRecentColor(themeTitle, themeColor, activity);
        }
    }

    void enterAnim() {
        anim.fadeIn(R.id.layout_text, 100, 200, "titleIn", activity);
        anim.fadeIn(R.id.layout_preset_store, 200, 200, "presetIn", activity);
    }

    void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        activity.dispatchKeyEvent(kUp);
    }
}
