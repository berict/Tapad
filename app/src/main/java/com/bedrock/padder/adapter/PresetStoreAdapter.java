package com.bedrock.padder.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bedrock.padder.R;
import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.store.Item;
import com.bedrock.padder.model.preset.store.PresetStore;

import static com.bedrock.padder.activity.PresetStoreActivity.installedFragment;
import static com.bedrock.padder.activity.PresetStoreActivity.onlineFragment;

public class PresetStoreAdapter extends RecyclerView.Adapter<PresetStoreAdapter.PresetViewHolder> {

    private PresetStore presetStore;
    private Item items[];
    private int rowLayout;
    private Activity activity;

    private View parentView;
    private Preferences preferences;
    private String callingFragment = null;

    public PresetStoreAdapter(PresetStore presetStore, int rowLayout, Activity activity) {
        this.presetStore = presetStore;
        this.items = presetStore.get().toArray(new Item[presetStore.getLength()]);
        this.rowLayout = rowLayout;
        this.activity = activity;

        preferences = new Preferences(activity);
    }

    public String getCallingFragment() {
        return callingFragment;
    }

    public void setCallingFragment(String callingFragment) {
        this.callingFragment = callingFragment;
    }

    public void updatePresetStore(PresetStore presetStore) {
        this.presetStore = presetStore;
        this.items = presetStore.get().toArray(new Item[presetStore.getLength()]);
        if (callingFragment.equals("installed")) {
            installedFragment.refresh();
        }
        if (callingFragment.equals("online")) {
            onlineFragment.refresh();
        }
    }

    @Override
    public PresetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentView = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PresetViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final PresetViewHolder holder, final int position) {
        Item item = items[position];
        item.init(holder, this, presetStore, preferences, activity);
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
            downloadLayout = view.findViewById(R.id.layout_preset_store_download_layout);

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