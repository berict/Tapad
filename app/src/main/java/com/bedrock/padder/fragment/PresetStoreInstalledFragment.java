package com.bedrock.padder.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bedrock.padder.R;
import com.bedrock.padder.adapter.PresetStoreAdapter;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.FileHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.PresetSchema;
import com.bedrock.padder.model.preset.store.PresetStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;

import static com.bedrock.padder.activity.MainActivity.installed;
import static com.bedrock.padder.helper.FileHelper.PROJECT_LOCATION_PRESETS;

public class PresetStoreInstalledFragment extends Fragment implements Refreshable {

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private FileHelper fileHelper = new FileHelper();

    private String TAG = "PSInstalled";

    AppCompatActivity a;
    View v;
    Gson gson;

    public PresetStoreAdapter installedAdapter = null;

    private File[] presets;

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
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    void setUi() {
        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(a);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) v.findViewById(R.id.layout_installed_preset_store_recycler_view)).setLayoutManager(layoutManager);
        ((RecyclerView) v.findViewById(R.id.layout_installed_preset_store_recycler_view)).setNestedScrollingEnabled(false);

        setAdapter();
    }

    String tapadFolderPath = Environment.getExternalStorageDirectory().getPath() + "/Tapad";

    class ScanLocal extends AsyncTask<Void, Void, Void> {

        ArrayList<PresetSchema> presetArrayList;

        @Override
        protected void onPreExecute() {
            presets = getPresetFolderList();
            presetArrayList = new ArrayList<>();

            if (presets == null || presets.length <= 0) {
                cancel(true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (File presetFolder : presets) {
                if (fileHelper.isPresetMetadataAvailable(presetFolder.getName())) {
                    // check folder's presets
                    if (validateMetadata(presetFolder.getName())) {
                        // pass only JSON v2
                        presetArrayList.add(fileHelper.getPresetSchemaFromMetadata(presetFolder.getName(), gson));
                        Log.i(TAG, presetFolder.getName() + " exists");
                    } else {
                        Log.e(TAG, presetFolder.getName() + " Json is outdated or corrupted");
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // create metadata
            if (presetArrayList.size() > 0) {
                installed = new PresetStore(
                        presetArrayList.toArray(new PresetSchema[presetArrayList.size()]),
                        new Preferences(a)
                );
                setAdapter();
            } else {
                onCancelled();
            }
        }

        @Override
        protected void onCancelled() {
            // need to show no presets installed
            // TODO need to add additional dialog supports
            Log.e(TAG, "Cannot find installed presets");
            installed = new PresetStore();
            setAdapter();
        }
    }

    private void searchMetadata() {
        new ScanLocal().execute();
    }

    private boolean validateMetadata(String presetName) {
        String metadata = fileHelper.getStringFromFile(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json");
        if (metadata.contains("firebase_location")) {
            // JSON v1, need to be updated
            return false;
        } else if (metadata.contains("tag")) {
            // JSON v2
            return true;
        } else {
            // corrupted JSON
            return false;
        }
    }

    private File[] getPresetFolderList() {
        return new File(tapadFolderPath + "/presets").listFiles();
    }

    private void setAdapter() {
        Log.d(TAG, "setAdapter");
        // attach the adapter to the layout
        if (installed == null) {
            // not initialized
            searchMetadata();
        } else if (installed.getLength() <= 0) {
            // clear the recycler view
            attachAdapter();
            window.setGone(R.id.layout_installed_preset_store_recycler_view, 0, a);
            anim.fadeIn(R.id.layout_installed_preset_store_recycler_view_no_preset, 0, 200, "rvNoPresetIn", v, a);
        } else {
            if ((v.findViewById(R.id.layout_installed_preset_store_recycler_view_no_preset)).getVisibility()
                    == View.VISIBLE) {
                // no preset visible
                window.setGone(R.id.layout_installed_preset_store_recycler_view_no_preset, 0, a);
                anim.fadeIn(R.id.layout_installed_preset_store_recycler_view, 0, 200, "rvNoPresetIn", v, a);
            }
            attachAdapter();
        }
    }

    private void attachAdapter() {
        // attach adapter while its not null
        if (installed != null && installed.getLength() > 0) {
            Log.d(TAG, "Attached adapter");
            installedAdapter = new PresetStoreAdapter(
                    installed,
                    R.layout.adapter_preset_store, a
            );
            installedAdapter.setCallingFragment("installed");
            ((RecyclerView) v.findViewById(R.id.layout_installed_preset_store_recycler_view)).setAdapter(installedAdapter);
        } else {
            Log.d(TAG, "Metadata is null");
        }
    }

    @Override
    public void refresh() {
        searchMetadata();
    }

    @Override
    public void clear() {
        window.setGone(R.id.layout_installed_preset_store_recycler_view, 0, a);
        refresh();
    }
}

