package com.bedrock.padder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {
    private Result[] results;
    private int rowLayout;
    private Context context;

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        LinearLayout treeDetails;
        TextView treeName;
        TextView treeId;
        TextView treeStandardFields;

        public DetailViewHolder(View view) {
            super(view);
            treeDetails = (LinearLayout) view.findViewById(R.id.adapter_tree_details);
            treeName = (TextView) view.findViewById(R.id.adapter_tree_details_name);
            treeId = (TextView) view.findViewById(R.id.adapter_tree_details_id);
            treeStandardFields = (TextView) view.findViewById(R.id.adapter_tree_details_standard_fields);
        }

        //TODO: create layout file for first recycler view (second too)
    }

    public DetailAdapter(Result[] results, int rowLayout, Context context) {
        this.results = results;
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
        holder.treeName.setText(String.valueOf(results[position].getName()));
        holder.treeId.setText(String.valueOf(results[position].getId()));

        String standardFields;
        if(results[position].getStandardFields().length == 1) {
            standardFields = results[position].getStandardFields()[0];
        } else if (results[position].getStandardFields().length >= 2) {
            standardFields = results[position].getStandardFields()[0];
            for (int i = 1; i < results[position].getStandardFields().length; i++) {
                standardFields += ", " + results[position].getStandardFields()[i];
            }
        } else {
            standardFields = "Error";
        }
        holder.treeStandardFields.setText(standardFields);
    }

    @Override
    public int getItemCount() {
        return results.length;
    }
}

