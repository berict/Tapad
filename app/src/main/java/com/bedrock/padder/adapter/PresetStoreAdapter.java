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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.ApiHelper;
import com.bedrock.padder.helper.FileHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.Preset;
import com.bedrock.padder.model.preset.PresetSchema;
import com.bedrock.padder.model.preset.store.Item;
import com.bedrock.padder.model.preset.store.PresetStore;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bedrock.padder.activity.PresetStoreActivity.isPresetDownloading;
import static com.bedrock.padder.helper.FileHelper.PRESET_LOCATION;
import static com.bedrock.padder.helper.FileHelper.PROJECT_LOCATION_PRESETS;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class PresetStoreAdapter extends RecyclerView.Adapter<PresetStoreAdapter.PresetViewHolder> {

    private static final String TAG = "PresetAdapter";

    private PresetStore presetStore;
    private int rowLayout;
    private Activity activity;

    private View parentView;
    private Preferences preferences;

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private FileHelper file = new FileHelper();

    public PresetStoreAdapter(PresetStore presetStore, int rowLayout, Activity activity) {
        this.presetStore = presetStore;
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
    public void onBindViewHolder(final PresetViewHolder holder, final int position) {
        final boolean isSelected = presetStore.getSelected() != null && position == 0;

        final Item item;

        if (isSelected) {
            item = presetStore.getSelected();
        } else {
            item = presetStore.getItem(position);
        }

        final PresetSchema presetSchema = item.getPresetSchema();
        final Preset preset = presetSchema.getPreset();

        // set gesture
        if (preset.isGesture()) {
            // preset is gesture
            Log.i(TAG, "gesture");
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
            Log.i(TAG, "normal");
            holder.gesture.setVisibility(View.GONE);
        }

        // load preset image
        String imageUrl = PRESET_LOCATION + "/" + preset.getTag() + "/album_art.jpg";

        // set title
        holder.title.setText(preset.getAbout().getSongName());

        // set artist name
        holder.artist.setText(preset.getAbout().getSongArtist());

        // set preset creator
        holder.creator.setText(
                getStringFromId(R.string.preset_store_preset_by, activity)
                        + " "
                        + preset.getAbout().getPresetArtist());

        holder.installing.setVisibility(View.INVISIBLE);

        // actions
        if (isPresetExists(preset.getTag())) {
            if (file.isPresetAvailable(preset)) {
                // exists, select | remove action
                holder.select.setVisibility(View.VISIBLE);
                holder.select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // select and load preset
                        preset.loadPreset(activity);
                        presetStore.select(position);
                        notifyDataSetChanged();
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
                                item.repair(holder, new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO this needed?
                                        //notifyItemChanged(holder.getAdapterPosition());
                                        // reset the savedPreset
                                        //isPresetChanged = true;
                                        //preferences.setLastPlayed(null);
                                    }
                                }, isSelected, activity);
                            }
                        });
                        // show warning
                        holder.warningLayout.setVisibility(View.VISIBLE);
                        holder.warning.setText(R.string.preset_store_action_update_available);
                    }
                });
            } else {
                if (isPresetDownloading) {
                    holder.installing.setText(R.string.preset_store_download_size_downloading);
                    anim.fadeOut(R.id.layout_preset_store_download_layout, 0, 200, parentView, activity);
                    anim.fadeIn(R.id.layout_preset_store_download_installing, 200, 200, "installIn", parentView, activity);
                    window.getView(R.id.layout_preset_store_action_layout, parentView).setVisibility(View.INVISIBLE);
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
                                            item.repair(holder, new Runnable() {
                                                @Override
                                                public void run() {
                                                    // TODO this needed?
                                                    //notifyItemChanged(holder.getAdapterPosition());
                                                    // reset the savedPreset
                                                    //isPresetChanged = true;
                                                    //preferences.setLastPlayed(null);
                                                }
                                            }, isSelected, activity);
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
                                    item.remove(new Runnable() {
                                        @Override
                                        public void run() {
                                            presetStore.remove(holder.getAdapterPosition());
                                            // TODO check this working
                                            notifyItemChanged(holder.getAdapterPosition());
                                        }
                                    }, isSelected, activity);
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
            holder.remove.setVisibility(View.GONE);
            holder.update.setVisibility(View.GONE);
            holder.download.setVisibility(View.VISIBLE);
            holder.download.setTextColor(preset.getAbout().getColor());
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.download(holder, new Runnable() {
                        @Override
                        public void run() {
                            // TODO this needed?
                            //presetStore.select(holder.getAdapterPosition());
                            // reset the savedPreset
                            //isPresetChanged = true;
                            //preferences.setLastPlayed(null);
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

        if (preferences.getLastPlayed() != null &&
                preferences.getLastPlayed().equals(preset.getTag()) &&
                file.isPresetAvailable(preset)) {
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
        return presetStore.getLength();
    }

    public static class PresetViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public LinearLayout gesture;
        public LinearLayout warningLayout;
        public LinearLayout actionLayout;

        public ImageView image;

        public TextView currentPreset;
        public TextView title;
        public TextView artist;
        public TextView creator;
        public TextView download;
        public TextView select;
        public TextView update;
        public TextView remove;
        public TextView warning;
        public TextView installing;

        public RelativeLayout downloadLayout;
        public ProgressBar downloadProgressBar;
        public TextView downloadPercent;
        public TextView downloadSize;
        public ImageView downloadCancel;

        public PresetViewHolder(View view) {
            super(view);
            root = view;

            gesture = view.findViewById(R.id.layout_preset_store_gesture_layout);
            warningLayout = view.findViewById(R.id.layout_preset_store_warning_layout);
            actionLayout = view.findViewById(R.id.layout_preset_store_action_layout);
            downloadLayout = view.findViewById(R.id.layout_preset_store_action_layout);

            image = view.findViewById(R.id.layout_preset_store_image);

            currentPreset = view.findViewById(R.id.layout_preset_store_current_preset);
            title = view.findViewById(R.id.layout_preset_store_title);
            artist = view.findViewById(R.id.layout_preset_store_artist);
            creator = view.findViewById(R.id.layout_preset_store_preset_creator);
            download = view.findViewById(R.id.layout_preset_store_action_download);
            select = view.findViewById(R.id.layout_preset_store_action_select);
            update = view.findViewById(R.id.layout_preset_store_action_update);
            remove = view.findViewById(R.id.layout_preset_store_action_remove);
            warning = view.findViewById(R.id.layout_preset_store_warning_text);
            installing = view.findViewById(R.id.layout_preset_store_download_installing);

            downloadProgressBar = view.findViewById(R.id.layout_preset_store_download_progressbar);

            downloadSize = view.findViewById(R.id.layout_preset_store_download_size);
            downloadPercent = view.findViewById(R.id.layout_preset_store_download_percent);
            downloadCancel = view.findViewById(R.id.layout_preset_store_download_cancel);
        }
    }
}