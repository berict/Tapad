package com.bedrock.padder.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.model.FirebaseMetadata;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.gson.Gson;

import java.io.File;
import java.text.DecimalFormat;

import static com.bedrock.padder.activity.PresetStoreActivity.isPresetDownloading;

public class FirebaseHelper {

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private FileHelper fileHelper = new FileHelper();

    private String TAG = "FirebaseHelper";
    private static final int REQUEST_WRITE_STORAGE = 112;

    public static String FIREBASE_LOCATION = "gs://tapad-4d342.appspot.com";
    public static String FIREBASE_LOCATION_PRESETS = "gs://tapad-4d342.appspot.com/presets";
    public static String FIREBASE_LOCATION_PRESETS_METADATA = "gs://tapad-4d342.appspot.com/presets/metadata";

    public static String PROJECT_LOCATION = Environment.getExternalStorageDirectory().getPath() + "/Tapad";
    public static String PROJECT_LOCATION_PRESETS = Environment.getExternalStorageDirectory().getPath() + "/Tapad/presets";
    public static String PROJECT_LOCATION_PRESET_METADATA = Environment.getExternalStorageDirectory().getPath() + "/Tapad/presets/metadata";

    public FileDownloadTask saveFromFirebase(StorageReference storageReference,
                                             final String fileLocation,
                                             Activity activity) {
        // permission check
        boolean hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            // no permission
            Log.e(TAG, "No permission acquired");
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE
            );
            return null;
        } else {
            if (isConnected(activity)) {
                File file = new File(fileLocation);

                return storageReference.getFile(file);
            } else {
                Log.e(TAG, "Not connected to the internet");
                return null;
            }
        }
    }

    public void saveFromFirebase(StorageReference storageReference,
                                 final String fileLocation,
                                 final Runnable onSuccess,
                                 final Runnable onFailure,
                                 Activity activity) {
        // permission check
        boolean hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            // no permission
            Log.e(TAG, "No permission acquired");
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE
            );
        } else {
            if (isConnected(activity)) {
                File file = new File(fileLocation);

                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Successful download at " + fileLocation);
                        onSuccess.run();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to download");
                        onFailure.run();
                    }
                });
            } else {
                Log.e(TAG, "Not connected to the internet");
            }
        }
    }

    public FirebaseMetadata saveFirebaseMetadata(StorageReference storageReference,
                                                 final String fileLocation,
                                                 Activity activity) {
        // permission check
        boolean hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            // no permission
            Log.e(TAG, "No permission acquired");
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE
            );
        } else {
            if (isConnected(activity)) {
                File file = new File(fileLocation);

                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Successful download at " + fileLocation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to download");
                    }
                });
            } else {
                Log.e(TAG, "Not connected to the internet");
            }
        }
        return getFirebaseMetadata(activity);
    }

    public String getPresetJson(String presetName) {
        FileHelper file = new FileHelper();
        return file.getStringFromFile(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json");
    }

    public void downloadFirebaseMetadata(Activity activity) {
        this.downloadFirebase(
                "presets/metadata",
                "presets/metadata",
                activity
        );
    }

    private DownloadPreset downloadPreset = null;

    public DownloadPreset downloadFirebasePreset(String presetName, View parentView, Activity activity, Runnable onFinish) {
        downloadPreset = new DownloadPreset(presetName, parentView, activity, onFinish);
        downloadPreset.execute();
        return downloadPreset;
    }

    public void cancelDownloadPreset() {
        if (downloadPreset != null) {
            downloadPreset.cancel(true);
            downloadPreset.downloadTask.cancel();
            downloadPreset.onCancelled();
        } else {
            Log.e(TAG, "DownloadPreset is not initialized");
        }
    }

    public class DownloadPreset extends AsyncTask<Void, Void, Integer> {

        private Activity activity;
        private View parentView;
        private String presetName;
        private String fileLocation = null;
        private Runnable onFinish;

        private ProgressBar progressBar;
        private TextView progressTextPercent;
        private TextView progressTextSize;

        private StorageReference storageReference;
        private long bytesTransferred = 0;
        private long totalByteCount = 0;

        public DownloadPreset(String presetName, View parentView, Activity activity, Runnable onFinish) {
            this.activity = activity;
            this.onFinish = onFinish;
            this.parentView = parentView;
            this.presetName = presetName;
            fileLocation = PROJECT_LOCATION_PRESETS + "/" + presetName + "/preset.zip";
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            if (isConnected(activity)) {
                // reference initialize
                FirebaseApp.initializeApp(activity);
                storageReference =
                        FirebaseStorage
                                .getInstance()
                                .getReferenceFromUrl(FIREBASE_LOCATION_PRESETS + "/" + presetName + "/preset.zip");

                // create preset project folder
                new File(PROJECT_LOCATION_PRESETS + "/" + presetName).mkdirs();

                // storage available
                // start download
                isPresetDownloading = true;

                anim.fadeOutInvisible(R.id.layout_preset_store_action_layout, 0, 200, parentView, activity);
                anim.fadeIn(R.id.layout_preset_store_download_layout, 200, 200, "progressIn", parentView, activity);
                // progressbar initialize
                progressBar = (ProgressBar) parentView.findViewById(R.id.layout_preset_store_download_progressbar);
                progressBar.setMax(100);

                progressTextSize = (TextView) parentView.findViewById(R.id.layout_preset_store_download_size);
                progressTextPercent = (TextView) parentView.findViewById(R.id.layout_preset_store_download_percent);
                window.getView(R.id.layout_preset_store_download_cancel, parentView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelDownloadPreset();
                    }
                });
            } else {
                // no internet connection
                // cancel the task
                cancelDownloadPreset();
                // no connection dialog
                new MaterialDialog.Builder(activity)
                        .title(R.string.preset_store_download_no_connection_dialog_title)
                        .content(R.string.preset_store_download_no_connection_dialog_text)
                        .contentColorRes(R.color.dark_primary)
                        .neutralText(R.string.dialog_close)
                        .show();
            }
        }

        private boolean isSpaceDialogVisible = false;

        private StorageTask downloadTask = null;

        @SuppressWarnings("VisibleForTests")
        @Override
        protected Integer doInBackground(Void... params) {
            downloadTask = saveFromFirebase(storageReference, fileLocation, activity).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bytesTransferred = taskSnapshot.getBytesTransferred();
                    totalByteCount = taskSnapshot.getTotalByteCount();
                    publishProgress();
                }
            }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if (!isCancelled()) {
                        Log.d(TAG, "Successful download at " + fileLocation);
                        // downloaded
                        anim.fadeOut(R.id.layout_preset_store_download_layout, 0, 200, parentView, activity);
                        anim.fadeIn(R.id.layout_preset_store_download_installing, 200, 200, "installIn", parentView, activity);
                        fileHelper.unzip(PROJECT_LOCATION_PRESETS + "/" + presetName + "/preset.zip",
                                PROJECT_LOCATION_PRESETS,
                                presetName,
                                parentView,
                                activity,
                                onFinish
                        );
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed to download");
                    cancelDownloadPreset();
                }
            });
            return (int) (100 * (bytesTransferred / totalByteCount));
        }

        @Override
        protected void onProgressUpdate(Void... args) {
            // update progress
            int mProgress = (int) ((100 * bytesTransferred) / totalByteCount);
            progressTextPercent.setText(String.valueOf(mProgress) + "%");
            if (progressBar.isIndeterminate()) {
                progressBar.setIndeterminate(false);
            }
            progressBar.setProgress(mProgress);

            String progressText;
            if (bytesTransferred == 0) {
                progressText = window.getStringFromId(R.string.preset_store_download_size_downloading, activity);
            } else {
                progressText = getReadableFileSize(bytesTransferred)
                        + "/"
                        + getReadableFileSize(totalByteCount);
            }
            progressTextSize.setText(progressText);
            if (totalByteCount > fileHelper.getAvailableExternalMemorySize()) {
                // no space left
                // cancel the task
                // no space dialog
                if (isSpaceDialogVisible == false) {
                    isSpaceDialogVisible = true;
                    new MaterialDialog.Builder(activity)
                            .title(R.string.preset_store_download_no_space_dialog_title)
                            .content(R.string.preset_store_download_no_space_dialog_text)
                            .contentColorRes(R.color.dark_primary)
                            .neutralText(R.string.dialog_close)
                            .dismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    isSpaceDialogVisible = false;
                                    cancelDownloadPreset();
                                }
                            })
                            .show();
                }
            }
            if (isCancelled()) {
                cancelDownloadPreset();
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d(TAG, "onPostExecute");
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "DownloadPreset cancelled");
            removeLocalPreset(presetName);
            isPresetDownloading = false;
            anim.fadeOut(R.id.layout_preset_store_download_layout, 0, 200, parentView, activity);
            anim.fadeIn(R.id.layout_preset_store_action_layout, 200, 200, "actionIn", parentView, activity);
            super.onCancelled();
        }

        private String getReadableFileSize(long size) {
            if (size <= 0) {
                return "0 B";
            }
            final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
            return new DecimalFormat("#,##0.0").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        }
    }

    public void removeLocalPreset(String presetName) {
        if (fileHelper.deleteRecursive(new File(PROJECT_LOCATION_PRESETS + "/" + presetName))) {
            Log.d(TAG, "Successfully removed preset folder");
        } else {
            Log.d(TAG, "Failed to remove preset folder");
        }
    }

    public void removeLocalPreset(String presetName, Runnable onSuccess, Runnable onFailure) {
        if (fileHelper.deleteRecursive(new File(PROJECT_LOCATION_PRESETS + "/" + presetName))) {
            Log.d(TAG, "Successfully removed preset folder");
            onSuccess.run();
        } else {
            Log.d(TAG, "Failed to remove preset folder");
            onFailure.run();
        }
    }

    public void downloadFirebase(String firebaseLocation,
                                 String fileLocation,
                                 Activity activity) {
        FirebaseApp.initializeApp(activity);

        StorageReference storageReference =
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl(FIREBASE_LOCATION + "/" + firebaseLocation);
        this.saveFromFirebase(storageReference, PROJECT_LOCATION + "/" + fileLocation, activity);
    }

    public void downloadFirebase(String firebaseLocation,
                                 String fileLocation,
                                 Runnable onSuccess,
                                 Runnable onFailure,
                                 Activity activity) {
        FirebaseApp.initializeApp(activity);

        StorageReference storageReference =
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl(FIREBASE_LOCATION + "/" + firebaseLocation);
        this.saveFromFirebase(storageReference, PROJECT_LOCATION + "/" + fileLocation, onSuccess, onFailure, activity);
    }

    public FirebaseMetadata getFirebaseMetadata(Activity activity) {
        FileHelper fileHelper = new FileHelper();

        FirebaseApp.initializeApp(activity);
        String metadataLocation = PROJECT_LOCATION_PRESET_METADATA;
        StorageReference metadataReference =
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl(FIREBASE_LOCATION_PRESETS_METADATA);
        if (new File(metadataLocation).exists()) {
            // metadata file exists
            String metadata = fileHelper.getStringFromFile(metadataLocation);
            if (getStorageMetadata(metadataReference, activity).getUpdatedTimeMillis()
                    > new File(metadataLocation).lastModified()) {
                // updated, download new one
                return saveFirebaseMetadata(
                        metadataReference,
                        metadataLocation,
                        activity
                );
            } else {
                // offline or not updated, continue
                Gson gson = new Gson();
                FirebaseMetadata firebaseMetadata = gson.fromJson(metadata, FirebaseMetadata.class);
                if (firebaseMetadata.getPresets() == null ||
                        firebaseMetadata.getVersionCode() == null) {
                    // corrupted metadata, download again
                    return saveFirebaseMetadata(
                            metadataReference,
                            metadataLocation,
                            activity
                    );
                } else {
                    return firebaseMetadata;
                }
            }
        } else {
            return saveFirebaseMetadata(
                    metadataReference,
                    metadataLocation,
                    activity
            );
        }
    }

    public StorageMetadata getStorageMetadata(StorageReference storageReference,
                                              Activity activity) {
        FirebaseApp.initializeApp(activity);
        final StorageMetadata[] mStorageMetadata = {null};
        storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.d(TAG, "Successful getting metadata");
                mStorageMetadata[0] = storageMetadata;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get metadata");
            }
        });
        return mStorageMetadata[0];
    }

    private boolean isConnected(Context context) {
        // returns whether the device is connected to the internet
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
