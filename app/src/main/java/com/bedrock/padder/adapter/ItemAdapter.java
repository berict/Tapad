package com.bedrock.padder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.about.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.DetailViewHolder> {
    private Item[] item;
    private int rowLayout;
    private Context context;
    
    WindowService window = new WindowService();

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemText;
        TextView itemHint;
        View divider;

        public DetailViewHolder(View view) {
            super(view);
            itemIcon = (ImageView) view.findViewById(R.id.layout_item_icon);
            itemText = (TextView) view.findViewById(R.id.layout_item_text);
            itemHint = (TextView) view.findViewById(R.id.layout_item_hint);
            divider = view.findViewById(R.id.divider);
        }
    }

    public ItemAdapter(Item[] item, int rowLayout, Context context) {
        this.item = item;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        holder.itemIcon.setImageResource(window.getDrawableId(item[position].getImageId()));
        holder.itemText.setText(String.valueOf(item[position].getText()));
        holder.itemHint.setText(String.valueOf(item[position].getHint()));

        if(position == getItemCount() - 1) {
            // last item on list
            holder.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return item.length;
    }

}