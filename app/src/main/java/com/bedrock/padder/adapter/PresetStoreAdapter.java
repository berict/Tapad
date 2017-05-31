package com.bedrock.padder.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.about.Item;

import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class PresetStoreAdapter extends RecyclerView.Adapter<PresetStoreAdapter.DetailViewHolder> {
    private Item[] item;
    private int rowLayout;
    private Context context;
    private Activity activity;

    private WindowService window = new WindowService();

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        ImageView presetImage;
        TextView presetTitle;
        TextView presetArtist;
        TextView presetCreator;
        View divider;
        RelativeLayout itemLayout;

        public DetailViewHolder(View view) {
            super(view);
//            itemIcon = (ImageView) view.findViewById(R.id.layout_item_icon);
//            itemText = (TextView) view.findViewById(R.id.layout_item_text);
//            itemHint = (TextView) view.findViewById(R.id.layout_item_hint);
            divider = view.findViewById(R.id.divider);
            itemLayout = (RelativeLayout) view.findViewById(R.id.layout_item);
        }
    }

    public PresetStoreAdapter(Item[] item, int rowLayout, Context context, Activity activity) {
        this.item = item;
        this.rowLayout = rowLayout;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new DetailViewHolder(view);
    }

    private IntentService intent = new IntentService();
    private AnimService anim = new AnimService();

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return item.length;
    }
}