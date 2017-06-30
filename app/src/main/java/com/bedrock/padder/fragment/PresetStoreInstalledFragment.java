package com.bedrock.padder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class PresetStoreInstalledFragment extends Fragment implements Refreshable {

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

        setAdapter();
    }

    String tapadFolderPath = Environment.getExternalStorageDirectory().getPath() + "/Tapad";

    private void searchMetadata() {
        Log.d(TAG, "searchMetadata");
        // get metadata locally
        File[] presets = getPresetFolderList();
        if (presets != null && presets.length > 1) {
            // length contains the metadata file
            Log.d(TAG, "Initialized arraylist, length is " + (presets.length - 1));
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
            Log.d(TAG, "null arraylist");
            firebaseMetadata = new FirebaseMetadata(null, 0);
        }
        setAdapter();
    }

    private File[] getPresetFolderList() {
        return new File(tapadFolderPath + "/presets").listFiles();
    }

    private FirebaseMetadata firebaseMetadata = null;

    private void setAdapter() {
        Log.d(TAG, "setAdapter");
        // attach the adapter to the layout
        if (firebaseMetadata == null) {
            // not initialized
            searchMetadata();
        } else if (firebaseMetadata.getPresets() == null) {
            // clear the recycler view
            attachAdapter();
            anim.fadeOut(R.id.layout_installed_preset_store_recyclerview, 0, 200, v, a);
            anim.fadeIn(R.id.layout_installed_preset_store_recyclerview_no_preset, 200, 200, "rvNoPresetIn", v, a);
        } else {
            if (window.getView(R.id.layout_installed_preset_store_recyclerview_no_preset, v).getVisibility()
                    == View.VISIBLE) {
                // no preset visible
                anim.fadeOut(R.id.layout_installed_preset_store_recyclerview_no_preset, 0, 200, v, a);
                anim.fadeIn(R.id.layout_installed_preset_store_recyclerview, 200, 200, "rvNoPresetIn", v, a);
            }
            attachAdapter();
        }
    }

    private void attachAdapter() {
        // attach adapter while its not null
        if (firebaseMetadata != null && firebaseMetadata.getPresets() != null) {
            Log.d(TAG, "Attached adapter");
            presetStoreAdapter = new PresetStoreAdapter(
                    firebaseMetadata,
                    R.layout.adapter_preset_store, a
            );
            window.getRecyclerView(R.id.layout_installed_preset_store_recyclerview, v).setAdapter(presetStoreAdapter);
        } else {
            Log.d(TAG, "Metadata is null");
        }
    }

    @Override
    public void refresh() {
        if (firebaseMetadata == null) {
            setAdapter();
        } else {
            if (firebaseMetadata.getPresets() != null) {
                // new download or remove
                if ((getPresetFolderList().length - 1) != firebaseMetadata.getPresets().length) {
                    // updated
                    Log.d(TAG, "Updated, preset not null");
                    searchMetadata();
                } else {
                    Log.d(TAG, "Not updated");
                }
            } else {
                // preset download from none
                if (getPresetFolderList().length > 1) {
                    // updated
                    Log.d(TAG, "Updated, preset null");
                    searchMetadata();
                } else {
                    Log.d(TAG, "Not updated");
                }
            }
        }
    }
}

