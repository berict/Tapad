package com.bedrock.padder.helper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.model.preset.Preset;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bedrock.padder.activity.MainActivity.isPresetChanged;
import static com.bedrock.padder.activity.PresetStoreActivity.isPresetDownloading;
import static com.bedrock.padder.helper.FirebaseHelper.PROJECT_LOCATION_PRESETS;

public class FileHelper {

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();

    private String TAG = "FileHelper";
    private Decompress decompress;

    public String getStringFromFile(String fileLocation) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileLocation));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            inputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStringFromUrl(final String url) {
        Log.d(TAG, "Requested : " + url);
        final String string[] = {null};

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "Response : " + response.body().string());
                    string[0] = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    string[0] = null;
                }
            }
        }).start();

        return string[0];
    }

    public boolean deleteRecursive(File fileOrDirectory) {
        boolean recursive = true;
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                recursive = deleteRecursive(child);

        Log.d(TAG, "Deleted : " + fileOrDirectory.toString());
        return fileOrDirectory.delete() && recursive;
    }

    public void unzip(String zipLocation, String targetLocation, String presetName, View parentView, Activity activity, Runnable onFinish) {
        decompress = new Decompress(zipLocation, targetLocation, presetName, parentView, activity, onFinish);
        decompress.execute();
    }

    private class Decompress extends AsyncTask<Void, Void, Integer> {

        private String zipLocation;
        private String targetLocation;
        private String presetName;
        private View parentView;
        private Activity activity;
        private Runnable onFinish;

        public Decompress(String zipLocation,
                          String targetLocation,
                          String presetName,
                          View parentView,
                          Activity activity,
                          Runnable onFinish) {
            this.zipLocation = zipLocation;
            this.targetLocation = targetLocation + "/";
            this.presetName = presetName;
            this.activity = activity;
            this.parentView = parentView;
            this.onFinish = onFinish;
            dirChecker("");
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                FileInputStream fileInputStream = new FileInputStream(zipLocation);
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    String zipName = zipEntry.getName();
                    Log.d("Firebase", "Unzipping " + zipName);
                    if (zipEntry.isDirectory()) {
                        dirChecker(zipName);
                    } else {
                        FileOutputStream fileOutputStream = new FileOutputStream(targetLocation + zipName);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        byte[] buffer = new byte[1024];
                        int read = 0;
                        while ((read = zipInputStream.read(buffer)) != -1) {
                            bufferedOutputStream.write(buffer, 0, read);
                        }
                        bufferedOutputStream.close();
                        zipInputStream.closeEntry();
                        fileOutputStream.close();
                    }
                }
                zipInputStream.close();
                Log.d("Firebase", "Unzipping completed at " + targetLocation);
            } catch (Exception e) {
                Log.d("Firebase", "Unzipping failed");
                e.printStackTrace();
            }
            return 0;
            // From https://stackoverflow.com/questions/4504291/how-to-speed-up-unzipping-time-in-java-android/4504692#4504692
        }

        @Override
        protected void onProgressUpdate(Void... args) {
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d(TAG, "onPostExecute");
            super.onPostExecute(integer);
            // finished unzipping, delete the original zip file
            if (new File(zipLocation).delete()) {
                Log.d(TAG, "Successfully removed zip file");
            } else {
                Log.d(TAG, "Failed to remove zip file");
            }

            if (new File(targetLocation + "/" + presetName).delete()) {
                Log.d(TAG, "Successfully removed zip folder");
            } else {
                Log.d(TAG, "Failed to remove zip folder");
            }

            File newFolder = new File(targetLocation + "/" + presetName);
            File oldFolder = new File(targetLocation + "/preset");
            if (oldFolder.renameTo(newFolder)) {
                Log.d(TAG, "Successfully rename preset folder");
            } else {
                Log.d(TAG, "Failed to rename preset folder");
            }

            // finished downloading preset
            isPresetDownloading = false;
            isPresetChanged = true;

            // refresh the preset adapter actions
            Handler handler = new Handler();
            handler.postDelayed(onFinish, 200);

            anim.fadeOut(R.id.layout_preset_store_download_installing, 100, 200, parentView, activity);
            anim.fadeIn(R.id.layout_preset_store_action_layout, 300, 200, "actionIn", parentView, activity);
            window.getProgressBar(R.id.layout_preset_store_download_progressbar, activity).setIndeterminate(true);
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "DownloadPreset cancelled");
            if (new File(PROJECT_LOCATION_PRESETS + "/" + presetName).delete()) {
                Log.d(TAG, "Successfully removed preset folder");
            } else {
                Log.d(TAG, "Failed to remove preset folder");
            }
            isPresetDownloading = false;
            super.onCancelled();
        }

        private void dirChecker(String dir) {
            File file = new File(targetLocation + dir);

            if (!file.isDirectory()) {
                file.mkdirs();
            }
        }
    }

    private void refreshPresetAdapterActions(String presetName, View parentView, final Activity activity) {
        TextView presetDownload = (TextView) parentView.findViewById(R.id.layout_preset_store_action_download);
        TextView presetSelect = (TextView) parentView.findViewById(R.id.layout_preset_store_action_select);
        TextView presetRemove = (TextView) parentView.findViewById(R.id.layout_preset_store_action_remove);
        if (isPresetExists(presetName)) {
            if (isPresetAvailable(presetName)) {
                // exists, select | remove action
                presetSelect.setVisibility(View.VISIBLE);
                presetSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // select and load preset
                        // TODO need to revise the loading method
                    }
                });
            } else {
                // corrupted, disable select
                presetSelect.setVisibility(View.VISIBLE);
                presetSelect.setAlpha(0.5f);
                presetSelect.setOnClickListener(new View.OnClickListener() {
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
                                        // remove and download the preset

                                    }
                                })
                                .negativeText(R.string.dialog_cancel)
                                .show();
                    }
                });
            }
            presetRemove.setVisibility(View.VISIBLE);
//            presetRemove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // remove confirm dialog
//                }
//            });
            presetDownload.setVisibility(View.GONE);
        } else {
            // doesn't exist, download action
            presetSelect.setVisibility(View.GONE);
            presetRemove.setVisibility(View.GONE);
            presetDownload.setVisibility(View.VISIBLE);
//            presetDownload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }

    public void cancelDecompress() {
        if (decompress != null) {
            decompress.cancel(true);
        } else {
            Log.e(TAG, "Decompress is not initialized");
        }
    }

    private boolean isPresetExists(String presetName) {
        // preset exist
        return new File(PROJECT_LOCATION_PRESETS + "/" + presetName).exists(); // folder check
    }

    long getAvailableExternalMemorySize() {
        return new File(PROJECT_LOCATION_PRESETS).getFreeSpace();
    }

    public boolean isPresetAvailable(Preset preset) {
        // with sound count check
        if (preset != null) {
            String presetName = preset.getFirebaseLocation();
            // preset available
            File folderSound = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/sounds");
            File folderTiming = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/timing");
            File folderAbout = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about");
            File fileJson = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json");
            File fileAlbum = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/album_art");
            File fileIcon = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/artist_icon");
            File fileImage = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/artist_image");
            if (folderSound.listFiles() != null) {
                Log.d(TAG, "SoundCountPreset = " + preset.getMusic().getSoundCount() + ", SoundCountFound = " + folderSound.listFiles().length);
            } else {
                return false;
            }
            // should be 100%
            return folderSound.isDirectory() && folderSound.exists() &&
                    preset.getMusic().getSoundCount() == folderSound.listFiles().length &&
                    folderTiming.isDirectory() && folderTiming.exists() &&
                    folderAbout.isDirectory() && folderAbout.exists() &&
                    fileJson.exists() &&
                    fileAlbum.exists() &&
                    fileIcon.exists() &&
                    fileImage.exists();
        } else {
            return false;
        }
    }

    public boolean isPresetAvailable(String presetName) {
        if (presetName != null) {
            // preset available
            File folderSound = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/sounds");
            File folderTiming = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/timing");
            File folderAbout = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about");
            File fileJson = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json");
            File fileAlbum = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/album_art");
            File fileIcon = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/artist_icon");
            File fileImage = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/artist_image");
            // should be 100%
            return folderSound.isDirectory() && folderSound.exists() &&
                    folderTiming.isDirectory() && folderTiming.exists() &&
                    folderAbout.isDirectory() && folderAbout.exists() &&
                    fileJson.exists() &&
                    fileAlbum.exists() &&
                    fileIcon.exists() &&
                    fileImage.exists();
        } else {
            return false;
        }
    }
}
