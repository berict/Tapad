package com.bedrock.padder.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.FirebaseMetadata;
import com.bedrock.padder.model.preset.Preset;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.PRESET_KEY;
import static com.bedrock.padder.activity.MainActivity.isPresetChanged;
import static com.bedrock.padder.helper.FirebaseHelper.PROJECT_LOCATION_PRESETS;
import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class PresetStoreAdapter extends RecyclerView.Adapter<PresetStoreAdapter.PresetViewHolder> {

    private static final String TAG = "PresetAdapter";
    private FirebaseMetadata firebaseMetadata;
    private int rowLayout;
    private Activity activity;
    private View parentView;

    private WindowHelper window = new WindowHelper();

    private SharedPreferences prefs;

    public static class PresetViewHolder extends RecyclerView.ViewHolder {
        LinearLayout presetGesture;
        LinearLayout presetWarningLayout;
        ImageView presetImage;
        TextView presetCurrentPreset;
        TextView presetTitle;
        TextView presetArtist;
        TextView presetCreator;
        TextView presetDownload;
        TextView presetSelect;
        TextView presetUpdate;
        TextView presetRemove;
        TextView presetWarning;
        TextView presetInstalling;

        public PresetViewHolder(View view) {
            super(view);
            presetGesture = (LinearLayout) view.findViewById(R.id.layout_preset_store_gesture_layout);
            presetWarningLayout = (LinearLayout) view.findViewById(R.id.layout_preset_store_warning_layout);
            presetImage = (ImageView) view.findViewById(R.id.layout_preset_store_image);
            presetCurrentPreset = (TextView) view.findViewById(R.id.layout_preset_store_current_preset);
            presetTitle = (TextView) view.findViewById(R.id.layout_preset_store_title);
            presetArtist = (TextView) view.findViewById(R.id.layout_preset_store_artist);
            presetCreator = (TextView) view.findViewById(R.id.layout_preset_store_preset_creator);
            presetDownload = (TextView) view.findViewById(R.id.layout_preset_store_action_download);
            presetSelect = (TextView) view.findViewById(R.id.layout_preset_store_action_select);
            presetUpdate = (TextView) view.findViewById(R.id.layout_preset_store_action_update);
            presetRemove = (TextView) view.findViewById(R.id.layout_preset_store_action_remove);
            presetWarning = (TextView) view.findViewById(R.id.layout_preset_store_warning_text);
            presetInstalling = (TextView) view.findViewById(R.id.layout_preset_store_download_installing);
        }
    }

    public PresetStoreAdapter(FirebaseMetadata firebaseMetadata, int rowLayout, Activity activity) {
        this.firebaseMetadata = firebaseMetadata;
        this.rowLayout = rowLayout;
        this.activity = activity;

        prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
    }

    @Override
    public PresetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentView = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PresetViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final PresetViewHolder holder, int position) {
        final Preset preset = firebaseMetadata.getPreset(position);

        // set gesture
        if (preset.getMusic().getGesture()) {
            // preset is gesture
            holder.presetGesture.setVisibility(View.VISIBLE);
            holder.presetGesture.setOnClickListener(new View.OnClickListener() {
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
            holder.presetGesture.setVisibility(View.GONE);
        }

        // load preset image
        String imageUrl = window.getStringFromId(R.string.google_firebase_link_root, activity)
                + "/presets%2F"
                + preset.getFirebaseLocation()
                + "%2F" + "album_art.jpg"
                + "?alt=media";

        String presetName = preset.getAbout().getTitle();
        String presetString[] = presetName.split(" - ");

        // set title
        holder.presetTitle.setText(presetString[1]);

        // set artist name
        holder.presetArtist.setText(presetString[0]);

        // set preset creator
        holder.presetCreator.setText(
                window.getStringFromId(R.string.preset_store_preset_by, activity)
                        + " "
                        + preset.getAbout().getPresetCreator());

        holder.presetInstalling.setVisibility(View.INVISIBLE);

        // actions
        if (isPresetExists(preset.getFirebaseLocation())) {
            if (isPresetAvailable(preset.getFirebaseLocation())) {
                // exists, select | remove action
                holder.presetSelect.setVisibility(View.VISIBLE);
                holder.presetSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // select and load preset
                        preset.setLoadPreset(activity);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });
                holder.presetWarningLayout.setVisibility(View.GONE);
                // load local image
                Picasso.with(activity)
                        .load("file:" + PROJECT_LOCATION_PRESETS + "/" + preset.getFirebaseLocation() + "/about/album_art.jpg")
                        .placeholder(R.drawable.ic_image_album_placeholder)
                        .error(R.drawable.ic_image_album_error)
                        .into(holder.presetImage);
                onFirebasePresetUpdated(preset.getFirebaseLocation(), new Runnable() {
                    @Override
                    public void run() {
                        // preset updated
                        holder.presetUpdate.setVisibility(View.VISIBLE);
                        holder.presetUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // re-download the preset
                                preset.repairPreset(parentView, activity, new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyItemChanged(holder.getAdapterPosition());
                                        // reset the savedPreset
                                        isPresetChanged = true;
                                        SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
                                        prefs.edit().putString(PRESET_KEY, null).apply();
                                    }
                                });
                            }
                        });
                        // show warning
                        holder.presetWarningLayout.setVisibility(View.VISIBLE);
                        holder.presetWarning.setText(R.string.preset_store_action_update_available);
                    }
                });
            } else {
                // corrupted, show repair
                holder.presetSelect.setVisibility(View.VISIBLE);
                holder.presetSelect.setTextColor(activity.getResources().getColor(R.color.colorAccent));
                holder.presetSelect.setText(R.string.preset_store_action_repair);
                // show warning
                holder.presetWarningLayout.setVisibility(View.VISIBLE);
                holder.presetWarning.setText(R.string.preset_store_action_repair_needed);
                holder.presetSelect.setOnClickListener(new View.OnClickListener() {
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
                                        preset.repairPreset(parentView, activity, new Runnable() {
                                            @Override
                                            public void run() {
                                                notifyItemChanged(holder.getAdapterPosition());
                                                // reset the savedPreset
                                                isPresetChanged = true;
                                                SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
                                                prefs.edit().putString(PRESET_KEY, null).apply();
                                            }
                                        });
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
                        .into(holder.presetImage);
            }
            holder.presetRemove.setVisibility(View.VISIBLE);
            holder.presetRemove.setOnClickListener(new View.OnClickListener() {
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
                                    preset.removePreset(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyItemChanged(holder.getAdapterPosition());
                                        }
                                    }, activity);
                                }
                            })
                            .negativeText(R.string.dialog_cancel)
                            .show();
                }
            });
            holder.presetUpdate.setVisibility(View.GONE);
            holder.presetDownload.setVisibility(View.GONE);
        } else {
            // doesn't exist, download action
            holder.presetSelect.setVisibility(View.GONE);
            holder.presetRemove.setVisibility(View.GONE);
            holder.presetUpdate.setVisibility(View.GONE);
            holder.presetDownload.setVisibility(View.VISIBLE);
            holder.presetDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preset.downloadPreset(parentView, activity, new Runnable() {
                        @Override
                        public void run() {
                            makeCurrentPreset(firebaseMetadata.getPresets(), holder.getAdapterPosition());
                            // reset the savedPreset
                            isPresetChanged = true;
                            SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
                            prefs.edit().putString(PRESET_KEY, null).apply();
                            Log.d("run", "key = " + prefs.getString(PRESET_KEY, null));
                        }
                    });
                }
            });
            // load url image
            Picasso.with(activity)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_album_placeholder)
                    .error(R.drawable.ic_image_album_error)
                    .into(holder.presetImage);
        }

        if (getPresetKey() != null &&
                getPresetKey().equals(preset.getFirebaseLocation()) &&
                isPresetAvailable(preset.getFirebaseLocation())) {
            // current preset set, downloaded
            holder.presetCurrentPreset.setVisibility(View.VISIBLE);
            holder.presetSelect.setVisibility(View.GONE);
        } else {
            holder.presetCurrentPreset.setVisibility(View.GONE);
        }
    }

    private String getPresetKey() {
        return prefs.getString(PRESET_KEY, null);
    }

    // Swap itemA with itemB
    private void swapPresetItems(Preset presets[], int itemAIndex, int itemBIndex) {
        //make sure to check if dataset is null and if itemA and itemB are valid indexes.
        Preset itemA = presets[itemAIndex];
        Preset itemB = presets[itemBIndex];
        presets[itemAIndex] = itemB;
        presets[itemBIndex] = itemA;

        notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
    }

    private void makeCurrentPreset(Preset presets[], int adapterPosition) {
        swapPresetItems(presets, 0, adapterPosition);
    }

    private void onFirebasePresetUpdated(final String presetName, final Runnable onUpdated) {
        StorageReference metadataReference =
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl("gs://tapad-4d342.appspot.com/presets/" + presetName)
                        .child("preset.zip");
        metadataReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.d(TAG, "Successful getting metadata");
                if (storageMetadata.getUpdatedTimeMillis() >
                        new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json").lastModified()) {
                    // firebase preset is updated since last download
                    // get the new updated preset
                    Log.d(TAG, "Preset updated");
                    Log.d(TAG, "Firebase = " + storageMetadata.getUpdatedTimeMillis());
                    Log.d(TAG, "Local    = " + new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json").lastModified());
                    onUpdated.run();
                } else {
                    Log.d(TAG, "Preset not updated");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get preset metadata");
            }
        });
    }

    private boolean isPresetExists(String presetName) {
        // preset exist
        File folder = new File(PROJECT_LOCATION_PRESETS + "/" + presetName); // folder check
        return folder.isDirectory() && folder.exists();
    }

    private boolean isPresetAvailable(String presetName) {
        // preset available
        File folderSound = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/sounds");
        File folderTiming = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/timing");
        File folderAbout = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about");
        File fileJson = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json");
        // TODO add 100% available
        return folderSound.isDirectory() && folderSound.exists() &&
                folderTiming.isDirectory() && folderTiming.exists() &&
                folderAbout.isDirectory() && folderAbout.exists() &&
                fileJson.exists();
    }

    @Override
    public int getItemCount() {
        return firebaseMetadata.getPresets().length;
    }
}