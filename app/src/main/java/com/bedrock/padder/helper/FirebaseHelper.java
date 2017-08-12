package com.bedrock.padder.helper;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.activity.PresetStoreActivity;
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
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class FirebaseHelper {

    private static final int REQUEST_WRITE_STORAGE = 112;
    public static String FIREBASE_LOCATION = "gs://tapad-4d342.appspot.com";
    public static String FIREBASE_LOCATION_PRESETS = "gs://tapad-4d342.appspot.com/presets";
    public static String FIREBASE_LOCATION_PRESETS_METADATA = "gs://tapad-4d342.appspot.com/presets/metadata";
    public static String PROJECT_LOCATION = Environment.getExternalStorageDirectory().getPath() + "/Tapad";
    public static String PROJECT_LOCATION_PRESETS = Environment.getExternalStorageDirectory().getPath() + "/Tapad/presets";
    public static String PROJECT_LOCATION_PRESET_METADATA = Environment.getExternalStorageDirectory().getPath() + "/Tapad/presets/metadata";
    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private FileHelper fileHelper = new FileHelper();
    private IntentHelper intent = new IntentHelper();
    private String TAG = "FirebaseHelper";
    private DownloadPreset downloadPreset = null;

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

    private void saveFromFirebase(StorageReference storageReference,
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

    public DownloadPreset downloadFirebasePreset(final String presetName, final String presetTitle, final View parentView, final Activity activity, final Runnable onFinish) {
        if (isConnected(activity)) {
            if (isWifiConnected(activity)) {
                downloadPreset = new DownloadPreset(presetName, presetTitle, parentView, activity, onFinish);
                downloadPreset.execute();
            } else {
                // not connected with wifi, show dialog
                new MaterialDialog.Builder(activity)
                        .title(R.string.preset_store_download_data_usage_title)
                        .content(R.string.preset_store_download_data_usage_text)
                        .contentColorRes(R.color.dark_secondary)
                        .positiveText(R.string.proceed_ac)
                        .positiveColorRes(R.color.colorAccent)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // download with cellular
                                downloadPreset = new DownloadPreset(presetName, presetTitle, parentView, activity, onFinish);
                                downloadPreset.execute();
                            }
                        })
                        .negativeText(R.string.dialog_close)
                        .negativeColorRes(R.color.dark_secondary)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .neutralText(R.string.preset_store_download_data_usage_neutral)
                        .neutralColorRes(R.color.dark_secondary)
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // intent wifi
                                intent.intentWiFiSettings(activity, 0);
                            }
                        })
                        .show();
            }
        } else {
            // no connection dialog
            new MaterialDialog.Builder(activity)
                    .title(R.string.preset_store_download_no_connection_dialog_title)
                    .content(R.string.preset_store_download_no_connection_dialog_text)
                    .contentColorRes(R.color.dark_primary)
                    .neutralText(R.string.dialog_close)
                    .show();
        }
        return downloadPreset;
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
            if (onSuccess != null) {
                onSuccess.run();
            }
        } else {
            Log.d(TAG, "Failed to remove preset folder");
            if (onFailure != null) {
                onFailure.run();
            }
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

    public boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    public class DownloadPreset extends AsyncTask<Void, Void, Integer> {

        private Activity activity;
        private View parentView;
        private String presetName;
        private String presetTitle;
        private String fileLocation = null;
        private Runnable onFinish;

        private ProgressBar progressBar;
        private TextView progressTextPercent;
        private TextView progressTextSize;

        private StorageReference storageReference;
        private long bytesTransferred = 0;
        private long totalByteCount = 0;

        private int id = 1;

        private NotificationManager notificationManager;
        private NotificationCompat.Builder mBuilder;
        private boolean isDownloading = false;
        private boolean isSpaceDialogVisible = false;
        private StorageTask downloadTask = null;

        public DownloadPreset(String presetName, String presetTitle, View parentView, Activity activity, Runnable onFinish) {
            this.activity = activity;
            this.onFinish = onFinish;
            this.parentView = parentView;
            this.presetName = presetName;
            this.presetTitle = presetTitle;
            fileLocation = PROJECT_LOCATION_PRESETS + "/" + presetName + "/preset.zip";

            notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(activity);
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

                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, PresetStoreActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentTitle(presetTitle)
                        .setContentText(getStringFromId(R.string.preset_store_download_notification_text_downloading, activity))
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download))
                        .setOngoing(true)
                        .setOnlyAlertOnce(true);
                notificationManager.notify(id, mBuilder.build());
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

        @SuppressWarnings("VisibleForTests")
        @Override
        protected Integer doInBackground(Void... params) {
            isDownloading = true;
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
                        // Completed downloading
                        isDownloading = false;
                        Log.d(TAG, "Successful download at " + fileLocation);
                        notificationManager.cancel(id);
                        mBuilder.setContentTitle(presetTitle)
                                .setContentText(getStringFromId(R.string.preset_store_download_notification_text_installing, activity))
                                .setProgress(0, 0, false)
                                .setOngoing(true)
                                .setOnlyAlertOnce(true);
                        // downloaded
                        anim.fadeOut(R.id.layout_preset_store_download_layout, 0, 200, parentView, activity);
                        anim.fadeIn(R.id.layout_preset_store_download_installing, 200, 200, "installIn", parentView, activity);
                        fileHelper.unzip(PROJECT_LOCATION_PRESETS + "/" + presetName + "/preset.zip",
                                PROJECT_LOCATION_PRESETS,
                                presetName,
                                parentView,
                                activity,
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        notificationManager.cancel(id);
                                        mBuilder.setContentTitle(presetTitle)
                                                .setContentText(getStringFromId(R.string.preset_store_download_notification_text_complete, activity))
                                                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                                                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download_done))
                                                .setOngoing(false)
                                                .setAutoCancel(true);
                                        notificationManager.notify(id, mBuilder.build());
                                        onFinish.run();
                                    }
                                }
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
            if (isDownloading) {
                // only notify when it is downloading
                mBuilder.setProgress(1000, (int) ((1000 * bytesTransferred) / totalByteCount), false);
            }
            notificationManager.notify(id, mBuilder.build());

            String progressText;
            if (bytesTransferred == 0) {
                progressText = getStringFromId(R.string.preset_store_download_size_downloading, activity);
            } else {
                progressText = getReadableFileSize(bytesTransferred)
                        + "/"
                        + getReadableFileSize(totalByteCount);
            }
            progressTextSize.setText(progressText);
            if (totalByteCount > fileHelper.getAvailableExternalMemorySize()) {
                // no space left dialog, cancel the task
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

        void cancelDownloadPreset() {
            if (downloadPreset != null) {
                downloadPreset.cancel(true);
                downloadPreset.downloadTask.cancel();
                downloadPreset.onCancelled();
                // cancelled / failed notification
                notificationManager.cancel(id);
                if (isCancelled()) {
                    mBuilder.setProgress(0, 0, false)
                            .setContentText(getStringFromId(R.string.preset_store_download_notification_text_cancelled, activity))
                            .setSmallIcon(android.R.drawable.stat_sys_download_done)
                            .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download_done))
                            .setOngoing(false)
                            .setAutoCancel(true);
                } else {
                    mBuilder.setProgress(0, 0, false)
                            .setContentText(getStringFromId(R.string.preset_store_download_notification_text_failed, activity))
                            .setSmallIcon(android.R.drawable.stat_sys_download_done)
                            .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download_done))
                            .setOngoing(false)
                            .setAutoCancel(true);
                }
                notificationManager.notify(id, mBuilder.build());
            } else {
                Log.e(TAG, "DownloadPreset is not initialized");
            }
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
}
