package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.model.preset.Preset;
import com.bedrock.padder.model.preset.PresetSchema;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.bedrock.padder.activity.MainActivity.isPresetChanged;
import static com.bedrock.padder.activity.PresetStoreActivity.isPresetDownloading;
import static com.bedrock.padder.helper.PresetStoreHelper.PROJECT_LOCATION_PRESETS;

public class FileHelper {

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

    public boolean deleteRecursive(File fileOrDirectory) {
        boolean recursive = true;
        if (fileOrDirectory.isDirectory())
            if (fileOrDirectory.listFiles() != null && fileOrDirectory.listFiles().length > 0)
            for (File child : fileOrDirectory.listFiles())
                recursive = deleteRecursive(child);

        Log.d(TAG, "Deleted : " + fileOrDirectory.toString());
        return fileOrDirectory.delete() && recursive;
    }

    public void unzip(String zipLocation, String targetLocation, String presetName, View parentView, Activity activity, Runnable onFinish) {
        decompress = new Decompress(zipLocation, targetLocation, presetName, parentView, activity, onFinish);
        decompress.execute();
    }

    public void unzip(String zipLocation, String targetLocation, String presetName, Activity activity, Runnable onFinish) {
        decompress = new Decompress(zipLocation, targetLocation, presetName, null, activity, onFinish);
        decompress.execute();
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
            String presetName = preset.getTag();
            // preset available
            File folderSound = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/sounds");
            File folderTiming = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/timing");
            File folderAbout = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about");
            File fileJson = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json");
            File fileAlbum = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/album_art");
            File fileIcon = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/artist_icon");
            File fileImage = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/artist_image");
            if (folderSound.listFiles() != null) {
                Log.d(TAG, "SoundCountPreset = " + preset.getSoundCount() + ", SoundCountFound = " + folderSound.listFiles().length);
            } else {
                Log.e(TAG, "Preset [" + preset.getTag() + "] is not available.");
                return false;
            }
            // TODO DEBUGGING
            boolean available = folderSound.isDirectory() && folderSound.exists() &&
                    preset.getSoundCount() == folderSound.listFiles().length &&
                    folderTiming.isDirectory() && folderTiming.exists() &&
                    folderAbout.isDirectory() && folderAbout.exists() &&
                    fileJson.exists() &&
                    fileAlbum.exists() &&
                    fileIcon.exists() &&
                    fileImage.exists();
            if (available) {
                Log.i(TAG, "Preset [" + preset.getTag() + "] is available.");
            }
            return available;
        } else {
            Log.e(TAG, "Preset is null.");
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

    public boolean isPresetMetadataAvailable(String presetName) {
        if (presetName != null) {
            File fileJson = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json");
            // should be 100%
            return fileJson.exists();
        } else {
            return false;
        }
    }

    public PresetSchema getPresetSchemaFromMetadata(String presetName, Gson gson) {
        if (presetName != null) {
            PresetSchema preset = gson.fromJson(
                    this.getStringFromFile(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json"),
                    PresetSchema.class
            );
            preset.getPreset().setTag(presetName);
            return preset;
        } else {
            return null;
        }
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
            if (new File(zipLocation).length() > getAvailableExternalMemorySize()) {
                // no storage
                new MaterialDialog.Builder(activity)
                        .title(R.string.preset_store_download_no_space_dialog_title)
                        .content(R.string.preset_store_download_no_space_dialog_text)
                        .contentColorRes(R.color.dark_primary)
                        .neutralText(R.string.dialog_close)
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                cancelDecompress();
                            }
                        })
                        .show();
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                FileInputStream fileInputStream = new FileInputStream(zipLocation);
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    String zipName = zipEntry.getName();
                    Log.d("PresetStore", "Unzipping " + zipName);
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
                Log.d("PresetStore", "Unzipping completed at " + targetLocation);
            } catch (Exception e) {
                Log.d("PresetStore", "Unzipping failed");
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
            if (parentView != null) {
                // only when downloaded
                if (new File(zipLocation).delete()) {
                    Log.d(TAG, "Successfully removed zip file");
                } else {
                    Log.d(TAG, "Failed to remove zip file");
                }
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

            if (parentView != null) {
                anim.fadeOut(R.id.layout_preset_store_download_installing, 100, 200, parentView, activity);
                anim.fadeIn(R.id.layout_preset_store_action_layout, 300, 200, "actionIn", parentView, activity);
            }
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

    public void copy(String sourceLocation, String targetLocation) {
        File sourceFile = new File(sourceLocation);
        File destFile = new File(targetLocation);
        // move file
        if (sourceFile.renameTo(destFile)) {
            Log.d(TAG, "File copied from " + sourceFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
        } else {
            Log.e(TAG, "Failed to copy from  " + sourceFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
        }
    }
}
