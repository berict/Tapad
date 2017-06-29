package com.bedrock.padder.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.adapter.PresetStoreAdapter;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.FileHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.FirebaseMetadata;
import com.bedrock.padder.model.preset.Preset;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class PresetStoreInstalledFragment extends Fragment {

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private IntentHelper intent = new IntentHelper();
    private FileHelper fileHelper = new FileHelper();

    private String TAG = "PSInstalled";

    AppCompatActivity a;
    View v;
    Gson gson;

    public PresetStoreAdapter presetStoreAdapter = null;

    public PresetStoreInstalledFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preset_store_installed, container, false);
    }
    
    @Override
    public void onStart() {
        super.onStart();

        v = getView();
        setUi();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        a = (AppCompatActivity) getActivity();
        gson = new Gson();
    }

    void setUi() {
        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(a);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        window.getRecyclerView(R.id.layout_installed_preset_store_recyclerview, v).setLayoutManager(layoutManager);
        window.getRecyclerView(R.id.layout_installed_preset_store_recyclerview, v).setNestedScrollingEnabled(false);

        // firebase check
        setAdapter();
    }

    private boolean shouldAdapterRefreshed = false;

    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // reload my a with permission granted or use the features what required the permission
                    searchMetadata();
                } else {
                    // show dialog to grant access
                    new MaterialDialog.Builder(a)
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
                                    intent.intentAppDetailSettings(a, 0);
                                    shouldAdapterRefreshed = true;
                                }
                            })
                            .negativeText(R.string.preset_store_permission_dialog_negative)
                            .negativeColorRes(R.color.red_400)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // TODO show no permission error state
                                }
                            })
                            .show();
                    Log.e(TAG, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission");
                }
        }
    }

    String tapadFolderPath = Environment.getExternalStorageDirectory().getPath() + "/Tapad";

    private void searchMetadata() {
        // get metadata locally
        File[] presets = new File(tapadFolderPath + "/presets").listFiles();
        if (presets != null && presets.length > 1) {
            // length contains the metadata file
            Log.d(TAG, "Initialized arraylist, length is " + presets.length);
            ArrayList<Preset> presetArrayList = new ArrayList<>();

            for (File presetFolder : presets) {
                if (fileHelper.isPresetMetadataAvailable(presetFolder.getName())) {
                    // check folder's presets
                    Log.d(TAG, "Filename = " + presetFolder.getName());
                    presetArrayList.add(fileHelper.getPresetFromMetadata(presetFolder.getName(), gson));
                }
            }

            // create metadata
            firebaseMetadata = new FirebaseMetadata(presetArrayList.toArray(new Preset[presetArrayList.size()]), 0);
        } else {
            firebaseMetadata = new FirebaseMetadata(null, 0);
        }
        setAdapter();
    }

    private FirebaseMetadata firebaseMetadata = null;

    private void setAdapter() {
        Log.d(TAG, "setAdapter");
        // attach the adapter to the layout
        if (firebaseMetadata == null) {
            // not initialized
            searchMetadata();
        } else if (firebaseMetadata.getPresets() == null) {
            anim.fadeOut(R.id.layout_installed_preset_store_recyclerview, 0, 200, v, a);
            anim.fadeIn(R.id.layout_installed_preset_store_recyclerview_no_preset, 200, 200, "rvNoPresetIn", v, a);
        } else {
            Log.d(TAG, "Attached adapter");
            // attach adapter while its not null
            presetStoreAdapter = new PresetStoreAdapter(
                    firebaseMetadata,
                    R.layout.adapter_preset_store, a
            );
            window.getRecyclerView(R.id.layout_installed_preset_store_recyclerview, v).setAdapter(presetStoreAdapter);
        }
    }
}

