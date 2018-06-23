package com.bedrock.padder.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bedrock.padder.R;
import com.bedrock.padder.model.about.About;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {
    private About about;
    private int rowLayout;
    private Context context;
    private Activity activity;

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView detailTitle;
        RecyclerView itemRecyclerView;

        public DetailViewHolder(View view) {
            super(view);
            detailTitle = view.findViewById(R.id.layout_detail_title);
            itemRecyclerView = view.findViewById(R.id.layout_item_recycler_view);
        }
    }

    public DetailAdapter(About about, int rowLayout, Context context, Activity activity) {
        this.about = about;
        this.rowLayout = rowLayout;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, int position) {
        if (about != null && about.getDetail(position) != null && about.getDetail(position).hasContent()) {
            holder.detailTitle.setText(about.getDetail(position).getTitle(context));
            holder.detailTitle.setTextColor(about.getColor());

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.itemRecyclerView.setLayoutManager(layoutManager);
            holder.itemRecyclerView.setAdapter(new ItemAdapter(about.getDetail(position).getItems(), R.layout.adapter_item, context, activity));
        } else {
            ((View) holder.itemRecyclerView.getParent().getParent().getParent()).setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return about.getDetails().length;
    }
}