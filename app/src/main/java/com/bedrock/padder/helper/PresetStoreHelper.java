package com.bedrock.padder.helper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.activity.PresetStoreActivity;
import com.bedrock.padder.model.preset.PresetSchema;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static com.bedrock.padder.activity.PresetStoreActivity.isPresetDownloading;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class PresetStoreHelper {

    public static String PRESET_LOCATION = "http://file.berict.com/tapad/presets";
    public static String PROJECT_LOCATION_PRESETS = Environment.getExternalStorageDirectory().getPath() + "/Tapad/presets";

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private FileHelper fileHelper = new FileHelper();
    private IntentHelper intent = new IntentHelper();

    private String TAG = "PresetStoreHelper";
    private DownloadPreset downloadPreset = null;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;

    public String getPresetJson(String presetName) {
        FileHelper file = new FileHelper();
        return file.getStringFromFile(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json");
    }

    public PresetSchema getLocalPreset(String presetName) {
        Gson gson = new Gson();
        return gson.fromJson(getPresetJson(presetName), PresetSchema.class);
    }

    public DownloadPreset downloadPreset(final String presetName, final String presetTitle, final View parentView, final Activity activity, final Runnable onFinish) {
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

    public void initNotification(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // from https://stackoverflow.com/questions/44443690/notificationcompat-with-api-26
        if (Build.VERSION.SDK_INT >= 26) {
            // Android O support notification
            NotificationChannel channel = new NotificationChannel(
                    "tapad-preset-store",
                    "Preset download",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Show preset downloading progress");
            notificationManager.createNotificationChannel(channel);
            mBuilder = new NotificationCompat.Builder(context, "tapad-preset-store");
        } else {
            mBuilder = new NotificationCompat.Builder(context).setDefaults(0);
        }
    }

    public class DownloadPreset extends AsyncTask<Void, Boolean, Integer> {

        private Activity activity;
        private View parentView;
        private String presetName;
        private String presetTitle;
        private String fileLocation;
        private Runnable onFinish;

        private ProgressBar progressBar;
        private TextView progressTextPercent;
        private TextView progressTextSize;

        private long bytesTransferred = 0;
        private long totalByteCount = 0;

        private int id = 1;

        private boolean isDownloading = false;
        private boolean isSpaceDialogVisible = false;

        public DownloadPreset(String presetName, String presetTitle, View parentView, Activity activity, Runnable onFinish) {
            this.activity = activity;
            this.onFinish = onFinish;
            this.parentView = parentView;
            this.presetName = presetName;
            this.presetTitle = presetTitle;
            fileLocation = PROJECT_LOCATION_PRESETS + "/" + presetName + "/preset.zip";
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            if (isConnected(activity)) {
                // create preset project folder
                new File(PROJECT_LOCATION_PRESETS + "/" + presetName).mkdirs();

                // storage available, start download
                isPresetDownloading = true;

                anim.fadeOutInvisible(R.id.layout_preset_store_action_layout, 0, 200, parentView, activity);
                anim.fadeIn(R.id.layout_preset_store_download_layout, 200, 200, "progressIn", parentView, activity);
                // progressbar initialize
                progressBar = parentView.findViewById(R.id.layout_preset_store_download_progressbar);
                progressBar.setMax(100);

                progressTextSize = (TextView) parentView.findViewById(R.id.layout_preset_store_download_size);
                progressTextPercent = (TextView) parentView.findViewById(R.id.layout_preset_store_download_percent);
                window.getView(R.id.layout_preset_store_download_cancel, parentView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelDownloadPreset();
                    }
                });

                // TODO add download notification - synced with preset store
                // issue #189
                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, PresetStoreActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentTitle(presetTitle)
                        .setContentText(getStringFromId(R.string.preset_store_download_notification_text_downloading, activity))
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download))
                        .setOngoing(true)
                        .setOnlyAlertOnce(true);

                mBuilder.setDefaults(Notification.DEFAULT_ALL);
                notificationManager.notify(id, mBuilder.build());
            } else {
                // no internet connection, cancel the task
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

        @Override
        protected Integer doInBackground(Void... args) {
            isDownloading = true;

            // new http file download

            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            int count;

            try {
                URL url = new URL(PRESET_LOCATION + "/" + presetName + "/preset.zip");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Log.i(TAG, "Connecting to " + url.getPath());
                // expect HTTP 200 OK, so we don't mistakenly save error report instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                    return -1;
                } else {
                    Log.i(TAG, "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                }

                totalByteCount = connection.getContentLength();
                String totalByteSize = getReadableFileSize(totalByteCount);

                if (totalByteCount > fileHelper.getAvailableExternalMemorySize()) {
                    // no space left dialog, cancel the task
                    if (isSpaceDialogVisible == false) {
                        isSpaceDialogVisible = true;
                        publishProgress(false);
                    }
                }

                // InputStream with 8k buffer
                input = new BufferedInputStream(url.openStream(), 8192);
                output = new FileOutputStream(fileLocation);

                byte data[] = new byte[1024];

                bytesTransferred = 0;

                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return -1;
                    }

                    bytesTransferred += count;

                    if (totalByteCount > 0) {
                        // assign variables
                        mProgress = (int) (100 * bytesTransferred / totalByteCount);
                        if (bytesTransferred == 0) {
                            mProgressText = getStringFromId(R.string.preset_store_download_size_downloading, activity);
                        } else {
                            mProgressText = getReadableFileSize(bytesTransferred)
                                    + "/" + totalByteSize;
                        }
                        publishProgress();
                    }

                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, "Failed to download");
                cancel(true);
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage() + " error, but meh");
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }

            return (int) (100 * (bytesTransferred / totalByteCount));
        }

        int mProgress = 0;
        String mProgressText;

        int previousProgressCount = -1;
        String previousProgressText = "";

        @Override
        protected void onProgressUpdate(Boolean... args) {
            // update progress
            if (mProgress > previousProgressCount) {
                // less updates
                previousProgressCount = mProgress;
                progressTextPercent.setText(String.valueOf(mProgress) + "%");
                if (progressBar.isIndeterminate()) {
                    progressBar.setIndeterminate(false);
                }
                progressBar.setProgress(mProgress);
                if (isDownloading) {
                    // only notify when it is downloading
                    mBuilder.setProgress(1000, 10 * mProgress, false);
                }
                notificationManager.notify(id, mBuilder.build());
            }

            if (!previousProgressText.equals(mProgressText)) {
                // less updates
                previousProgressText = mProgressText;
                progressTextSize.setText(mProgressText);
            }

            if (isCancelled()) {
                cancelDownloadPreset();
            }

            if (args.length > 0 && args[0] == false) {
                // cancel task
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

        @Override
        protected void onPostExecute(Integer integer) {
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
