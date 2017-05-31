package com.bedrock.padder.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.FirebaseService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.FirebaseMetadata;
import com.bedrock.padder.model.preset.Preset;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.bedrock.padder.helper.FirebaseService.PROJECT_LOCATION_PRESETS;

public class PresetStoreAdapter extends RecyclerView.Adapter<PresetStoreAdapter.DetailViewHolder> {
    private FirebaseMetadata firebaseMetadata;
    private int rowLayout;
    private Activity activity;

    private WindowService window = new WindowService();
    private FirebaseService firebase = new FirebaseService();

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        LinearLayout presetGesture;
        ImageView presetImage;
        TextView presetTitle;
        TextView presetArtist;
        TextView presetCreator;
        TextView presetDownload;
        TextView presetSelect;
        TextView presetRemove;

        public DetailViewHolder(View view) {
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
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, int position) {
        Preset preset = firebaseMetadata.getPreset(position);

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
        holder.presetCreator.setText(preset.getAbout().getPresetCreator());

        // actions
        if (isPresetAvailable(preset.getMusic().getFileName())) {
            // exists, select | remove action
            holder.presetSelect.setVisibility(View.VISIBLE);
            holder.presetSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.presetRemove.setVisibility(View.VISIBLE);
            holder.presetRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

                }
            });
        }
    }

    private boolean isPresetAvailable(String presetName) {
        return new File(PROJECT_LOCATION_PRESETS + "/" + presetName + "/preset.zip").exists();
    }

    @Override
    public int getItemCount() {
        return firebaseMetadata.getPresets().length;
    }
}