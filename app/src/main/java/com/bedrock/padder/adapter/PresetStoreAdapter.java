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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.ApiHelper;
import com.bedrock.padder.helper.FileHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.Schema;
import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.Preset;
import com.bedrock.padder.model.preset.PresetSchema;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bedrock.padder.activity.MainActivity.isPresetChanged;
import static com.bedrock.padder.activity.PresetStoreActivity.isPresetDownloading;
import static com.bedrock.padder.helper.PresetStoreHelper.PRESET_LOCATION;
import static com.bedrock.padder.helper.PresetStoreHelper.PROJECT_LOCATION_PRESETS;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class PresetStoreAdapter extends RecyclerView.Adapter<PresetStoreAdapter.PresetViewHolder> {

    private static final String TAG = "PresetAdapter";
    private Schema schema;
    private int rowLayout;
    private Activity activity;
    private View parentView;

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private FileHelper file = new FileHelper();

    private Preferences preferences;

    public PresetStoreAdapter(Schema schema, int rowLayout, Activity activity) {
        this.schema = schema;
        this.rowLayout = rowLayout;
        this.activity = activity;

        preferences = new Preferences(activity);
    }

    @Override
    public PresetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentView = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PresetViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final PresetViewHolder holder, int position) {
        final Preset preset = schema.getPreset(position).getPreset();
        final PresetSchema presetSchema = schema.getPreset(position);

        Log.i(TAG, preset.toString());

        // set gesture
        if (preset.isGesture()) {
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
        String imageUrl = PRESET_LOCATION + "/" + preset.getTag() + "/album_art.jpg";

        // set title
        holder.presetTitle.setText(preset.getAbout().getSongName());

        // set artist name
        holder.presetArtist.setText(preset.getAbout().getSongArtist());

        // set preset creator
        holder.presetCreator.setText(
                getStringFromId(R.string.preset_store_preset_by, activity)
                        + " "
                        + preset.getAbout().getPresetArtist());

        holder.presetInstalling.setVisibility(View.INVISIBLE);

        // actions
        if (isPresetExists(preset.getTag())) {
            if (file.isPresetAvailable(preset)) {
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
                        .load("file:" + PROJECT_LOCATION_PRESETS + "/" + preset.getTag() + "/about/album_art")
                        .placeholder(R.drawable.ic_image_album_placeholder)
                        .error(R.drawable.ic_image_album_error)
                        .into(holder.presetImage);
                // check preset update
                onPresetUpdated(presetSchema.getLocalVersion(), preset.getTag(), new Runnable() {
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
                                        preferences.setLastPlayed(null);
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
                if (isPresetDownloading) {
                    holder.presetInstalling.setText(R.string.preset_store_download_size_downloading);
                    anim.fadeOut(R.id.layout_preset_store_download_layout, 0, 200, parentView, activity);
                    anim.fadeIn(R.id.layout_preset_store_download_installing, 200, 200, "installIn", parentView, activity);
                    window.getView(R.id.layout_preset_store_action_layout, parentView).setVisibility(View.INVISIBLE);
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
                                                    preferences.setLastPlayed(null);
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
            }
            // selected
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
                            makeCurrentPreset(schema.getPresets(), holder.getAdapterPosition());
                            // reset the savedPreset
                            isPresetChanged = true;
                            preferences.setLastPlayed(null);
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

        if (preferences.getLastPlayed() != null &&
                preferences.getLastPlayed().equals(preset.getTag()) &&
                file.isPresetAvailable(preset)) {
            // selected: current preset set, downloaded
            holder.presetCurrentPreset.setVisibility(View.VISIBLE);
            holder.presetSelect.setVisibility(View.GONE);
        } else {
            holder.presetCurrentPreset.setVisibility(View.GONE);
        }
    }

    // Swap itemA with itemB
    private void swapPresetItems(PresetSchema presets[], int itemAIndex, int itemBIndex) {
        if (presets.length > 0 && itemAIndex >= 0 && itemBIndex >= 0) {
            //make sure to check if data set is null and if itemA and itemB are valid indexes.
            PresetSchema itemA = presets[itemAIndex];
            PresetSchema itemB = presets[itemBIndex];
            presets[itemAIndex] = itemB;
            presets[itemBIndex] = itemA;

            notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
        }
    }

    private void makeCurrentPreset(PresetSchema presets[], int adapterPosition) {
        swapPresetItems(presets, 0, adapterPosition);
    }

    private void onPresetUpdated(final Integer version, final String tag, final Runnable onUpdated) {
        ApiHelper api = new ApiHelper();
        api.getPresetSchema(tag).enqueue(new Callback<PresetSchema>() {
            @Override
            public void onResponse(Call<PresetSchema> call, Response<PresetSchema> response) {
                Log.i(TAG, "Current version : " + response.body().getVersion() + " / Local version : " + version);
                if (response.body().getVersion() > version) {
                    // the version is updated
                    onUpdated.run();
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

    @Override
    public int getItemCount() {
        return schema.getPresets().length;
    }

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
}