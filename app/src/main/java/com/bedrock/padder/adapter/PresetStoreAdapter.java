package com.bedrock.padder.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.FirebaseService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.FirebaseMetadata;
import com.bedrock.padder.model.preset.Preset;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.bedrock.padder.helper.FirebaseService.PROJECT_LOCATION_PRESETS;

public class PresetStoreAdapter extends RecyclerView.Adapter<PresetStoreAdapter.PresetViewHolder> {

    private FirebaseMetadata firebaseMetadata;
    private int rowLayout;
    private Activity activity;
    private View parentView;

    private WindowService window = new WindowService();
    private FirebaseService firebase = new FirebaseService();

    public static class PresetViewHolder extends RecyclerView.ViewHolder {
        LinearLayout presetGesture;
        ImageView presetImage;
        TextView presetTitle;
        TextView presetArtist;
        TextView presetCreator;
        TextView presetDownload;
        TextView presetSelect;
        TextView presetRemove;

        public PresetViewHolder(View view) {
            super(view);
            presetGesture = (LinearLayout) view.findViewById(R.id.layout_preset_store_gesture_layout);
            presetImage = (ImageView) view.findViewById(R.id.layout_preset_store_image);
            presetTitle = (TextView) view.findViewById(R.id.layout_preset_store_title);
            presetArtist = (TextView) view.findViewById(R.id.layout_preset_store_artist);
            presetCreator = (TextView) view.findViewById(R.id.layout_preset_store_preset_creator);
            presetDownload = (TextView) view.findViewById(R.id.layout_preset_store_action_download);
            presetSelect = (TextView) view.findViewById(R.id.layout_preset_store_action_select);
            presetRemove = (TextView) view.findViewById(R.id.layout_preset_store_action_remove);
        }
    }

    public PresetStoreAdapter(FirebaseMetadata firebaseMetadata, int rowLayout, Activity activity) {
        this.firebaseMetadata = firebaseMetadata;
        this.rowLayout = rowLayout;
        this.activity = activity;
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
        Picasso.with(activity).load(imageUrl).into(holder.presetImage);

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

        // actions
        if (isPresetExists(preset.getFirebaseLocation())) {
            if (isPresetAvailable(preset.getFirebaseLocation())) {
                // exists, select | remove action
                holder.presetSelect.setVisibility(View.VISIBLE);
                holder.presetSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // select and load preset
                        // TODO need to revise the loading method
                        Toast.makeText(activity, "SELECTED", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // corrupted, disable select
                holder.presetSelect.setVisibility(View.VISIBLE);
                holder.presetSelect.setAlpha(0.5f);
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
                                            }
                                        });
                                    }
                                })
                                .negativeText(R.string.dialog_cancel)
                                .show();
                    }
                });
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
                                    });
                                }
                            })
                            .negativeText(R.string.dialog_cancel)
                            .show();
                }
            });
            holder.presetDownload.setVisibility(View.GONE);
        } else {
            // doesn't exist, download action
            holder.presetSelect.setVisibility(View.GONE);
            holder.presetRemove.setVisibility(View.GONE);
            holder.presetDownload.setVisibility(View.VISIBLE);
            holder.presetDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preset.downloadPreset(parentView, activity, new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(holder.getAdapterPosition());
                        }
                    });
                }
            });
        }
    }

    private boolean isPresetExists(String presetName) {
        // preset exist
        File folder = new File(PROJECT_LOCATION_PRESETS + "/" + presetName); // folder check
        Log.d("PSA", folder.toString());
        return folder.isDirectory() && folder.exists();
    }

    private boolean isPresetAvailable(String presetName) {
        // preset available
        File folderSound = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/sounds");
        Log.d("PSA", folderSound.toString());
        File folderTiming = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/timing");
        Log.d("PSA", folderTiming.toString());
        File folderAbout = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about");
        Log.d("PSA", folderAbout.toString());
        File fileJson = new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/about/json.txt");
        Log.d("PSA", fileJson.toString());
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