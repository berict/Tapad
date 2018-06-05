package com.bedrock.padder.model.preset.store;

import android.animation.Animator;
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
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.activity.PresetStoreActivity;
import com.bedrock.padder.adapter.PresetStoreAdapter;
import com.bedrock.padder.helper.ApiHelper;
import com.bedrock.padder.helper.FileHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.SoundHelper;
import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.Preset;
import com.bedrock.padder.model.preset.PresetSchema;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bedrock.padder.activity.MainActivity.isPresetChanged;
import static com.bedrock.padder.activity.MainActivity.isPresetDownloading;
import static com.bedrock.padder.helper.FileHelper.PRESET_LOCATION;
import static com.bedrock.padder.helper.FileHelper.PROJECT_LOCATION_PRESETS;

/*
* Wrapper class for saving download status
* */

public class Item {

    private static final transient String TAG = "Store.Item";

    private transient PresetStore presetStore = null;
    private transient Activity activity = null;
    private transient Preferences preferences = null;
    private transient PresetStoreAdapter adapter = null;

    private transient FileHelper file = new FileHelper();

    private PresetSchema presetSchema;
    private PresetStoreAdapter.PresetViewHolder holder;
    private Download download = null;

    private long bytesTransferred = -1;
    private long totalByteCount = -1;

    private int index = -1;

    public Item(PresetSchema presetSchema, int index) {
        this.presetSchema = presetSchema;
        this.index = index;
    }

    public Item(PresetSchema presetSchema) {
        // for selected
        this.presetSchema = presetSchema;
    }

    public Preset getPreset() {
        return presetSchema.getPreset();
    }

    public PresetSchema getPresetSchema() {
        return presetSchema;
    }

    public boolean isDownloading() {
        return bytesTransferred != -1 && totalByteCount != -1;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object item) {
        try {
            return getPresetSchema().equals(((Item) item).getPresetSchema());
        } catch (Exception e) {
            Log.e(TAG, "equals(), cannot compare with another object");
            return false;
        }
    }

    public long getBytesTransferred() {
        return bytesTransferred;
    }

    public void setBytesTransferred(long bytesTransferred) {
        this.bytesTransferred = bytesTransferred;
    }

    public long getTotalByteCount() {
        return totalByteCount;
    }

    public void setTotalByteCount(long totalByteCount) {
        this.totalByteCount = totalByteCount;
    }

    public void clear() {
        this.totalByteCount = -1;
        this.bytesTransferred = -1;
    }

    public void load(Context context) {
        isPresetChanged = true;
        new Preferences(context).setLastPlayed(getPreset().getTag());
    }

    public void load(int color, int colorDef, Activity activity) {
        SoundHelper sound = new SoundHelper();
        sound.load(getPreset(), color, colorDef, activity);
    }

    public void largeLog(String log) {
        Log.i(TAG, log.substring(0, 4000));
        if (log.substring(4000).length() > 4000) {
            largeLog(log.substring(4000));
        } else {
            Log.i(TAG, log.substring(4000));
        }
    }

    public void download(final Runnable onFinish, final Activity activity) {
        // download the preset from presetStore
        final Item item = this;
        if (isConnected(activity)) {
            if (isWifiConnected(activity)) {
                download = new Download(item, onFinish, activity);
                download.execute();
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
                                download = new Download(item, onFinish, activity);
                                download.execute();
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
                                new IntentHelper().intentWiFiSettings(activity, 0);
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
    }

    public void remove(Runnable onFinish, Activity activity) {
        removeLocalPreset(getPreset().getTag(), onFinish, null);

        if (isSelected()) {
            // reset the savedPreset
            isPresetChanged = true;
            new Preferences(activity).setLastPlayed(null);
        }
    }

    public void repair(final Runnable onFinish, final Activity activity) {
        // remove and download the preset again
        removeLocalPreset(getPreset().getTag(), new Runnable() {
            @Override
            public void run() {
                download(onFinish, activity);
            }
        }, null);

        if (isSelected()) {
            // reset the savedPreset
            isPresetChanged = true;
            new Preferences(activity).setLastPlayed(null);
        }
    }

    public void removeLocalPreset(String presetName, Runnable onSuccess, Runnable onFailure) {
        if (new FileHelper().deleteRecursive(new File(PROJECT_LOCATION_PRESETS + "/" + presetName))) {
            Log.d("remove()", "Successfully removed preset folder");
            if (onSuccess != null) {
                onSuccess.run();
            }
        } else {
            Log.d("remove()", "Failed to remove preset folder");
            if (onFailure != null) {
                onFailure.run();
            }
        }
    }

    // UI elements

    public void init(PresetStoreAdapter.PresetViewHolder vh,
                     PresetStoreAdapter ad,
                     PresetStore ps,
                     Preferences prefs,
                     Activity a) {
        this.holder = vh;
        this.adapter = ad;
        this.presetStore = ps;
        this.preferences = prefs;
        this.activity = a;

        draw();
    }

    private void draw() {
        final PresetSchema presetSchema = getPresetSchema();
        final Preset preset = presetSchema.getPreset();

        // load preset image
        String imageUrl = PRESET_LOCATION + "/" + preset.getTag() + "/album_art.jpg";

        if (download != null && download.isCancelled()) {
            Log.i(TAG, "Updated cancelled item");

            // doesn't exist, download action
            holder.select.setVisibility(View.GONE);
            holder.select.setTextColor(preset.getAbout().getColor());
            holder.remove.setVisibility(View.GONE);
            holder.update.setVisibility(View.GONE);
            holder.download.setVisibility(View.VISIBLE);
            holder.download.setTextColor(preset.getAbout().getColor());
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download(new Runnable() {
                        @Override
                        public void run() {
                            if (isSelected()) {
                                presetStore.unselect();
                            }
                            adapter.notifyItemChanged(index);
                        }
                    }, activity);
                }
            });
            // load url image
            Picasso.with(activity)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_album_placeholder)
                    .error(R.drawable.ic_image_album_error)
                    .into(holder.image);

            YoYo.with(Techniques.FadeOut)
                    .duration(200)
                    .onStart(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            holder.actionLayout.setVisibility(View.GONE);
                            holder.downloadProgressBar.setProgress(0);
                            holder.downloadPercent.setText("0%");
                            holder.downloadSize.setText(R.string.preset_store_download_size_downloading);
                        }
                    })
                    .playOn(holder.downloadLayout);

            YoYo.with(Techniques.FadeIn)
                    .delay(200)
                    .duration(200)
                    .onStart(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            holder.actionLayout.setVisibility(View.VISIBLE);
                        }
                    })
                    .playOn(holder.actionLayout);
        }

        // set gesture
        if (preset.isGesture()) {
            // preset is gesture
            holder.gesture.setVisibility(View.VISIBLE);
            holder.gesture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(activity)
                            .title(R.string.preset_store_gesture_help_dialog_title)
                            .content(R.string.preset_store_gesture_help_dialog_text)
                            .contentColorRes(R.color.dark_primary)
                            .positiveText(R.string.dialog_close)
                            .positiveColorRes(R.color.colorAccent)
                            .show();
                }
            });
        } else {
            holder.gesture.setVisibility(View.GONE);
        }

        // set title
        holder.title.setText(preset.getAbout().getSongName());

        // set artist name
        holder.artist.setText(preset.getAbout().getSongArtist());

        // set preset creator
        holder.creator.setText(
                getStringFromId(R.string.preset_store_preset_by, activity)
                        + " "
                        + preset.getAbout().getPresetArtist());

        // actions
        if (isPresetExists(preset.getTag())) {
            if (file.isPresetAvailable(preset)) {
                // exists, select | remove action
                holder.select.setVisibility(View.VISIBLE);
                holder.select.setTextColor(preset.getAbout().getColor());
                holder.select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // select and load preset
                        preset.loadPreset(activity);
                        presetStore.select(preset.getTag());
                        adapter.updatePresetStore(presetStore);
                    }
                });
                holder.warningLayout.setVisibility(View.GONE);
                // load local image
                Picasso.with(activity)
                        .load("file:" + PROJECT_LOCATION_PRESETS + "/" + preset.getTag() + "/about/album_art")
                        .placeholder(R.drawable.ic_image_album_placeholder)
                        .error(R.drawable.ic_image_album_error)
                        .into(holder.image);
                // check preset update
                onPresetUpdated(presetSchema.getLocalVersion(), preset.getTag(), new Runnable() {
                    @Override
                    public void run() {
                        // preset updated
                        holder.update.setVisibility(View.VISIBLE);
                        holder.update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // re-download the preset
                                repair(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyItemChanged(index);
                                    }
                                }, activity);
                            }
                        });
                        // show warning
                        holder.warningLayout.setVisibility(View.VISIBLE);
                        holder.warning.setText(R.string.preset_store_action_update_available);
                    }
                });
            } else {
                if (isPresetDownloading) {
                    int delay = 0;
                    if (holder.actionLayout.getVisibility() != View.GONE) {
                        YoYo.with(Techniques.FadeOut)
                                .duration(200)
                                .onEnd(new YoYo.AnimatorCallback() {
                                    @Override
                                    public void call(Animator animator) {
                                        holder.actionLayout.setVisibility(View.GONE);
                                    }
                                })
                                .playOn(holder.actionLayout);
                        delay = 200;
                    }
                    YoYo.with(Techniques.FadeIn)
                            .delay(delay)
                            .duration(200)
                            .onStart(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    holder.downloadLayout.setVisibility(View.VISIBLE);
                                }
                            })
                            .playOn(holder.downloadLayout);

                    // load url image
                    Picasso.with(activity)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_image_album_placeholder)
                            .error(R.drawable.ic_image_album_error)
                            .into(holder.image);
                } else {
                    // corrupted, show repair
                    holder.select.setVisibility(View.VISIBLE);
                    holder.select.setTextColor(activity.getResources().getColor(R.color.colorAccent));
                    holder.select.setText(R.string.preset_store_action_repair);
                    // show warning
                    holder.warningLayout.setVisibility(View.VISIBLE);
                    holder.warning.setText(R.string.preset_store_action_repair_needed);
                    holder.select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // show repair dialog
                            new MaterialDialog.Builder(activity)
                                    .title(R.string.preset_store_action_repair_dialog_title)
                                    .content(R.string.preset_store_action_repair_dialog_text)
                                    .contentColorRes(R.color.dark_primary)
                                    .positiveText(R.string.preset_store_action_repair)
                                    .positiveColorRes(R.color.colorAccent)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                            // repair the preset
                                            repair(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyItemChanged(index);
                                                    if (isSelected()) {
                                                        presetStore.unselect();
                                                    }
                                                }
                                            }, activity);
                                        }
                                    })
                                    .negativeText(R.string.dialog_cancel)
                                    .show();
                        }
                    });
                    // load url image
                    Picasso.with(activity)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_image_album_placeholder)
                            .error(R.drawable.ic_image_album_error)
                            .into(holder.image);
                }
            }
            // selected
            holder.remove.setVisibility(View.VISIBLE);
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // remove confirm dialog
                    new MaterialDialog.Builder(activity)
                            .content(R.string.preset_store_remove_warning_dialog_text)
                            .contentColorRes(R.color.dark_secondary)
                            .positiveText(R.string.remove_ac)
                            .positiveColorRes(R.color.red)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    // repair the preset
                                    remove(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (adapter.getCallingFragment().equals("installed")) {
                                                presetStore.remove(index);
                                                adapter.updatePresetStore(presetStore);
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }, activity);
                                }
                            })
                            .negativeText(R.string.dialog_cancel)
                            .show();
                }
            });
            holder.update.setVisibility(View.GONE);
            holder.download.setVisibility(View.GONE);
        } else {
            // doesn't exist, download action
            holder.select.setVisibility(View.GONE);
            holder.select.setTextColor(preset.getAbout().getColor());
            holder.remove.setVisibility(View.GONE);
            holder.update.setVisibility(View.GONE);
            holder.download.setVisibility(View.VISIBLE);
            holder.download.setTextColor(preset.getAbout().getColor());
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download(new Runnable() {
                        @Override
                        public void run() {
                            if (isSelected()) {
                                presetStore.unselect();
                            }
                            adapter.notifyItemChanged(index);
                        }
                    }, activity);
                }
            });
            // load url image
            Picasso.with(activity)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_album_placeholder)
                    .error(R.drawable.ic_image_album_error)
                    .into(holder.image);
        }

        if (isSelected()) {
            // selected: current preset set, downloaded
            holder.currentPreset.setVisibility(View.VISIBLE);
            holder.select.setVisibility(View.GONE);
        } else {
            holder.currentPreset.setVisibility(View.GONE);
        }
    }

    private void onPresetUpdated(final Integer version, final String tag, final Runnable onUpdated) {
        ApiHelper api = new ApiHelper();
        api.getPresetSchema(tag).enqueue(new Callback<PresetSchema>() {
            @Override
            public void onResponse(Call<PresetSchema> call, Response<PresetSchema> response) {
                if (response != null && response.body() != null) {
                    Log.i(TAG, "Current version : " + response.body().getVersion() + " / Local version : " + version);
                    if (response.body().getVersion() > version) {
                        // the version is updated
                        onUpdated.run();
                    }
                } else {
                    Log.i(TAG, "Cannot get online version");
                }
            }

            @Override
            public void onFailure(Call<PresetSchema> call, Throwable t) {
                Log.e(TAG, "Failure getting version of the preset [" + tag + "]");
            }
        });
    }

    private boolean isPresetExists(String presetName) {
        // preset exist
        File folder = new File(PROJECT_LOCATION_PRESETS + "/" + presetName); // folder check
        return folder.isDirectory() && folder.exists();
    }

    private boolean isSelected() {
        if (presetStore != null && presetStore.getSelected() != null) {
            return presetStore.getSelected().equals(this);
        } else {
            return false;
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

    class Download extends AsyncTask<Void, Boolean, Integer> {

        private static final String TAG = "Download";

        private Item item;
        private Runnable onFinish;
        private Activity activity;

        private String tag;
        private String fileLocation;
        private boolean isSpaceDialogVisible = false;

        private FileHelper fileHelper = new FileHelper();

        private NotificationManager notificationManager;
        private NotificationCompat.Builder mBuilder;
        private int mNotificationId = 1;
        private PendingIntent pendingIntent;

        private InputStream input;
        private OutputStream output;
        private HttpURLConnection connection;

        private int mProgress = 0;
        private String mProgressText;

        private int previousProgressCount = -1;
        private String previousProgressText = "";

        public Download(Item item, Runnable onFinish, Activity activity) {
            this.item = item;
            this.onFinish = onFinish;
            this.activity = activity;

            this.tag = item.getPreset().getTag();
            this.fileLocation = PROJECT_LOCATION_PRESETS + "/" + tag + "/preset.zip";

            notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

            // setup notifications
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
                mBuilder = new NotificationCompat.Builder(activity, "tapad-preset-store");
            } else {
                mBuilder = new NotificationCompat.Builder(activity).setDefaults(0);
            }
            Intent presetStoreIntent = new Intent(this.activity, PresetStoreActivity.class);
            presetStoreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(
                    this.activity, 0,
                    presetStoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        void cancel() {
            isPresetDownloading = false;
            if (item != null) {
                item.setBytesTransferred(-1);
                item.setTotalByteCount(-1);
                // cancelled / failed notification
                notificationManager.cancel(mNotificationId);
                if (isCancelled()) {
                    mBuilder.setProgress(0, 0, false)
                            .setContentText(getStringFromId(R.string.preset_store_download_notification_text_cancelled, activity))
                            .setSmallIcon(android.R.drawable.stat_sys_download_done)
                            .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download_done))
                            .setOngoing(false)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                } else {
                    mBuilder.setProgress(0, 0, false)
                            .setContentText(getStringFromId(R.string.preset_store_download_notification_text_failed, activity))
                            .setSmallIcon(android.R.drawable.stat_sys_download_done)
                            .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download_done))
                            .setOngoing(false)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                }
                notificationManager.notify(mNotificationId, mBuilder.build());
            } else {
                Log.e(TAG, "Download is not initialized");
            }

            // remove current preset
            remove(new Runnable() {
                @Override
                public void run() {
                    // re-init the view
                    draw();
                }
            }, activity);
        }

        @Override
        protected void onPreExecute() {
            if (isConnected(activity)) {
                // create preset project folder
                new File(PROJECT_LOCATION_PRESETS + "/" + tag).mkdirs();

                // storage available, start download
                isPresetDownloading = true;

                YoYo.with(Techniques.FadeOut)
                        .duration(200)
                        .playOn(holder.actionLayout);

                YoYo.with(Techniques.FadeIn)
                        .delay(200)
                        .duration(200)
                        .onStart(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                holder.downloadLayout.setVisibility(View.VISIBLE);
                            }
                        })
                        .playOn(holder.downloadLayout);

                // progressbar initialize
                holder.downloadProgressBar.setMax(100);
                holder.downloadCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancel(true);
                    }
                });

                mBuilder.setContentTitle(item.getPreset().getAbout().getTitle())
                        .setContentText(getStringFromId(R.string.preset_store_download_notification_text_downloading, activity))
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download))
                        .setOngoing(true)
                        .setContentIntent(pendingIntent)
                        .setOnlyAlertOnce(true);

                mBuilder.setDefaults(Notification.DEFAULT_ALL);
                notificationManager.notify(mNotificationId, mBuilder.build());
            } else {
                // no internet connection, cancel the task
                cancel(true);
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
        protected Integer doInBackground(Void... voids) {
            // new http file download

            input = null;
            output = null;
            connection = null;

            int count;

            try {
                URL url = new URL(PRESET_LOCATION + "/" + tag + "/preset.zip");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Log.i(TAG, "Connecting to " + url.getPath());
                // expect HTTP 200 OK, so we don't mistakenly save error report instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                    connection.disconnect();
                    // TODO add error dialog
                    return -1;
                } else {
                    Log.i(TAG, "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                }

                item.setTotalByteCount(connection.getContentLength());
                String totalByteSize = getReadableFileSize(item.getTotalByteCount());

                if (item.getTotalByteCount() > fileHelper.getAvailableExternalMemorySize()) {
                    // no space left dialog, cancel the task
                    if (isSpaceDialogVisible == false) {
                        isSpaceDialogVisible = true;
                        publishProgress(false);
                        cancel(true);
                    }
                }

                // InputStream with 8k buffer
                input = new BufferedInputStream(url.openStream(), 8192);
                output = new FileOutputStream(fileLocation);

                byte data[] = new byte[1024];

                item.setBytesTransferred(0);

                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        throw new Exception("cancel");
                    }

                    item.setBytesTransferred(item.getBytesTransferred() + count);

                    if (item.getTotalByteCount() > 0) {
                        // assign variables
                        mProgress = (int) (100 * item.getBytesTransferred() / item.getTotalByteCount());
                        if (item.getBytesTransferred() == 0) {
                            mProgressText = getStringFromId(R.string.preset_store_download_size_downloading, activity);
                        } else {
                            mProgressText = getReadableFileSize(item.getBytesTransferred())
                                    + "/" + totalByteSize;
                        }
                        publishProgress();
                    }

                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());

                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException e1) {
                    Log.e(TAG, e1.getMessage());
                }

                if (connection != null) {
                    connection.disconnect();
                    Log.i(TAG, "HttpURLConnection disconnected");
                }

                if (e.getMessage().equals("cancel")) {
                    cancel(true);
                }
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            return (int) (100 * (item.getBytesTransferred() / item.getTotalByteCount()));
        }

        @Override
        protected void onProgressUpdate(Boolean... args) {
            // update progress
            if (mProgress > previousProgressCount) {
                // less updates
                previousProgressCount = mProgress;
                holder.downloadPercent.setText(String.valueOf(mProgress) + "%");
                if (holder.downloadProgressBar.isIndeterminate()) {
                    holder.downloadProgressBar.setIndeterminate(false);
                }
                holder.downloadProgressBar.setProgress(mProgress);
                if (item.isDownloading()) {
                    // only notify when it is downloading
                    mBuilder.setProgress(1000, 10 * mProgress, false);
                }
                notificationManager.notify(mNotificationId, mBuilder.build());
            }

            if (!previousProgressText.equals(mProgressText)) {
                // less updates
                previousProgressText = mProgressText;
                holder.downloadSize.setText(mProgressText);
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
                                cancel(true);
                            }
                        })
                        .show();
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (!isCancelled()) {
                // Completed downloading
                item.clear();
                Log.d(TAG, "Successful download at " + fileLocation);
                notificationManager.cancel(mNotificationId);
                mBuilder.setContentTitle(item.getPreset().getAbout().getTitle())
                        .setContentText(getStringFromId(R.string.preset_store_download_notification_text_installing, activity))
                        .setProgress(0, 0, false)
                        .setOngoing(true)
                        .setContentIntent(pendingIntent)
                        .setOnlyAlertOnce(true);
                // downloaded
                YoYo.with(Techniques.FadeOut)
                        .duration(200)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                holder.downloadLayout.setVisibility(View.GONE);
                            }
                        })
                        .playOn(holder.downloadLayout);

                YoYo.with(Techniques.FadeIn)
                        .delay(200)
                        .duration(200)
                        .onStart(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                holder.installing.setVisibility(View.VISIBLE);
                            }
                        })
                        .playOn(holder.installing);

                fileHelper.unzip(PROJECT_LOCATION_PRESETS + "/" + item.getPreset().getTag() + "/preset.zip",
                        PROJECT_LOCATION_PRESETS, tag, holder.root, activity,
                        new Runnable() {
                            @Override
                            public void run() {
                                notificationManager.cancel(mNotificationId);
                                mBuilder.setContentTitle(item.getPreset().getAbout().getTitle())
                                        .setContentText(getStringFromId(R.string.preset_store_download_notification_text_complete, activity))
                                        .setSmallIcon(android.R.drawable.stat_sys_download_done)
                                        .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.stat_sys_download_done))
                                        .setOngoing(false)
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true);
                                notificationManager.notify(mNotificationId, mBuilder.build());
                                onFinish.run();
                            }
                        }
                );
            }
        }

        @Override
        protected void onCancelled(Integer integer) {
            cancel();
        }

        @Override
        protected void onCancelled() {
            cancel();
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
