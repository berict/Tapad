package com.bedrock.padder.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;

public class PresetStoreOnlineFragment extends Fragment implements Refreshable {

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private IntentHelper intent = new IntentHelper();
    private FileHelper fileHelper = new FileHelper();

    private String TAG = "PSOnline";

    AppCompatActivity a;
    View v;

    public PresetStoreAdapter presetStoreAdapter = null;

    public PresetStoreOnlineFragment() {
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
        return inflater.inflate(R.layout.fragment_preset_store_online, container, false);
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
    }

    void setUi() {
        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(a);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        window.getRecyclerView(R.id.layout_online_preset_store_recyclerview, v).setLayoutManager(layoutManager);
        window.getRecyclerView(R.id.layout_online_preset_store_recyclerview, v).setNestedScrollingEnabled(false);

        // firebase check
        setAdapter();
    }

    String tapadFolderPath = Environment.getExternalStorageDirectory().getPath() + "/Tapad";
    String metadataLocation = tapadFolderPath + "/presets/metadata";

    private void setLoadingFinished(boolean isFinished) {
        if (isFinished) {
            // finished, hide loading and show recyclerview
            Log.d(TAG, "Loading finished");
            anim.fadeOut(R.id.layout_online_preset_store_recyclerview_loading, 0, 200, v, a);
            anim.fadeIn(R.id.layout_online_preset_store_recyclerview, 200, 200, "rvIn", v, a);
        } else {
            // started, show loading
            anim.fadeOut(R.id.layout_online_preset_store_recyclerview, 0, 200, v, a);
            anim.fadeIn(R.id.layout_online_preset_store_recyclerview_loading, 200, 200, "rvLoadingIn", v, a);
        }
    }

    private void setLoadingFailed() {
        Log.d(TAG, "Loading failed");
        anim.fadeOut(R.id.layout_online_preset_store_recyclerview_loading, 0, 200, v, a);
        anim.fadeIn(R.id.layout_online_preset_store_recyclerview_failed, 200, 200, "rvIn", v, a);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(a)
                        .title(R.string.preset_store_download_no_connection_dialog_title)
                        .content(R.string.preset_store_download_no_connection_dialog_text)
                        .contentColorRes(R.color.dark_primary)
                        .neutralText(R.string.dialog_close)
                        .show();
            }
        }, 200);
    }

    private void onDownloadMetadataSuccess() {
        setLoadingFinished(true);
    }

    private void downloadMetadata() {
        // loading start
        setLoadingFinished(false);
        Log.d(TAG, "downloadMetaData");

        final File metadata = new File(tapadFolderPath + "/presets/metadata");

        StorageReference metadataReference =
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl("gs://tapad-4d342.appspot.com/presets")
                        .child("metadata");
        metadataReference.getFile(metadata).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Successful download at " + metadata.toString());
                setAdapter();
                onDownloadMetadataSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to download");
                setLoadingFinished(true);
            }
        });
    }

    private Handler connectionTimeout = new Handler();

    FirebaseMetadata firebaseMetadata;

    private void setAdapter() {
        connectionTimeout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (window.getView(R.id.layout_online_preset_store_recyclerview_loading, v).getVisibility() == View.VISIBLE) {
                    // loading for 10 seconds, prompt user to retry or not
                    new MaterialDialog.Builder(a)
                            .title(R.string.preset_store_connection_timeout_dialog_title)
                            .content(R.string.preset_store_connection_timeout_dialog_text)
                            .contentColorRes(R.color.dark_primary)
                            .positiveText(R.string.preset_store_connection_timeout_dialog_positive)
                            .negativeText(R.string.preset_store_connection_timeout_dialog_negative)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    setAdapter();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    setLoadingFailed();
                                }
                            })
                            .show();
                }
            }
        }, 10000);

        if (isConnected(a)) {
            Log.d(TAG, "setAdapter");
            // attach the adapter to the layout
            if (new File(metadataLocation).exists()) {
                // metadata file exists
                String metadata = fileHelper.getStringFromFile(metadataLocation);
                if (isFirebaseMetadataUpdated(a)) {
                    // updated, download new one
                    downloadMetadata();
                } else {
                    Log.d(TAG, "Attached adapter");
                    // offline or not updated, continue
                    Gson gson = new Gson();
                    firebaseMetadata = gson.fromJson(metadata, FirebaseMetadata.class);
                    if (firebaseMetadata == null ||
                            firebaseMetadata.getPresets() == null ||
                            firebaseMetadata.getVersionCode() == null) {
                        // corrupted metadata, download again
                        downloadMetadata();
                    } else {
                        // attach adapter while its not null
                        presetStoreAdapter = new PresetStoreAdapter(
                                firebaseMetadata,
                                R.layout.adapter_preset_store, a
                        );
                        window.getRecyclerView(R.id.layout_online_preset_store_recyclerview, v).setAdapter(presetStoreAdapter);
                    }
                }
            } else {
                downloadMetadata();
            }
        } else {
            setLoadingFailed();
        }
    }

    private boolean isConnected(Context context) {
        // returns whether the device is connected to the internet
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

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
                            .child("metadata");
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
                        if (window.getView(R.id.layout_online_preset_store_recyclerview_loading, v).getVisibility()
                                == View.VISIBLE) {
                            // loading
                            onDownloadMetadataSuccess();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Failed to get metadata");
                    isFMUpdated = false;
                }
            });
            return isFMUpdated;
        } else {
            Log.d(TAG, "Disconnected from the internet");
            return isFMUpdated;
        }
    }

    @Override
    public void refresh() {
        if (isFirebaseMetadataUpdated(a)) {
            // on updated
            setAdapter();
        } else {
            if (firebaseMetadata != null) {
                presetStoreAdapter = new PresetStoreAdapter(
                        firebaseMetadata,
                        R.layout.adapter_preset_store, a
                );
                window.getRecyclerView(R.id.layout_online_preset_store_recyclerview, v).setAdapter(presetStoreAdapter);
            }
        }
    }
}
