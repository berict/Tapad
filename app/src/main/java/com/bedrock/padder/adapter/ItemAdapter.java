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

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.about.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.DetailViewHolder> {
    private Item[] item;
    private int rowLayout;
    private Context context;
    private Activity activity;
    
    private WindowService window = new WindowService();

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemText;
        TextView itemHint;
        View divider;
        RelativeLayout itemLayout;

        public DetailViewHolder(View view) {
            super(view);
            itemIcon = (ImageView) view.findViewById(R.id.layout_item_icon);
            itemText = (TextView) view.findViewById(R.id.layout_item_text);
            itemHint = (TextView) view.findViewById(R.id.layout_item_hint);
            divider = view.findViewById(R.id.divider);
            itemLayout = (RelativeLayout) view.findViewById(R.id.layout_item);
        }
    }

    public ItemAdapter(Item[] item, int rowLayout, Context context, Activity activity) {
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
    private AnimService anim =  new AnimService();

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, int position) {
        holder.itemText.setText(window.getStringId(item[position].getTextId()));

        if(position == getItemCount() - 1) {
            // last item on list, hide divider
            holder.divider.setVisibility(View.GONE);
        }

        if(item[position].getHintId() == null) {
            // no hint provided
            holder.itemHint.setVisibility(View.GONE);
        } else {
            holder.itemHint.setText(window.getStringId(item[position].getHintId()));

            if(item[position].getRunnable() != null) {
                // run runnable
                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item[holder.getAdapterPosition()].getRunnable().run();
                    }
                });
            }

            if(window.getStringFromId(item[position].getHintId(), activity).startsWith("http")) {
                // link available check
                anim.circularRevealTouch(holder.itemLayout, R.id.layout_placeholder,
                        new AccelerateDecelerateInterpolator(), new Runnable() {
                            @Override
                            public void run() {
                                window.setRecentColor(window.getStringId(item[holder.getAdapterPosition()].getTextId()), R.color.colorAccent, activity);
                                intent.intentLink(activity, window.getStringFromId(item[holder.getAdapterPosition()].getHintId(), activity), 400);
                            }
                        }, 400, 0, activity);
            }
        }

        if(item[position].getImageId() == null) {
            // no icon provided
            holder.itemIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.itemIcon.setImageResource(window.getDrawableId(item[position].getImageId()));
        }
    }

    @Override
    public int getItemCount() {
        return item.length;
    }
}