package com.bedrock.padder.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.model.preferences.ItemColor;
import com.bedrock.padder.model.preferences.Preferences;

import static com.bedrock.padder.activity.MainActivity.isDeckShouldCleared;
import static com.bedrock.padder.helper.WindowHelper.getBlendColor;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.DetailViewHolder> {
    private ItemColor itemColor;
    private int rowLayout;
    private Preferences preferences;
    private Activity activity;

    static class DetailViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout colorLayout;
        TextView colorTextId;
        View colorView1;
        View colorView2;
        View colorView3;
        View colorView4;
        TextView actionPrimary;
        TextView actionRemove;

        DetailViewHolder(View view) {
            super(view);
            colorLayout = view.findViewById(R.id.layout_color);
            colorTextId = view.findViewById(R.id.layout_color_title);
            colorView1 = view.findViewById(R.id.view_color_1);
            colorView2 = view.findViewById(R.id.view_color_2);
            colorView3 = view.findViewById(R.id.view_color_3);
            colorView4 = view.findViewById(R.id.view_color_4);
            actionRemove = view.findViewById(R.id.layout_color_action_remove);
            actionPrimary = view.findViewById(R.id.layout_color_action_primary);
        }
    }

    public ColorAdapter(ItemColor itemColor, int rowLayout, Preferences preferences, Activity activity) {
        this.itemColor = itemColor;
        this.rowLayout = rowLayout;
        this.preferences = preferences;
        this.activity = activity;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, int position) {
        Log.d("Holder", String.valueOf(itemColor.getColorButtonRecent(position)));
        final int color = itemColor.getColorButtonRecent(position);

        // Set the color id title
        try {
            holder.colorTextId.setText(String.format("#%06X", (0xFFFFFF & activity.getResources().getColor(color))));
        } catch (Resources.NotFoundException e) {
            holder.colorTextId.setText(String.format("#%06X", (0xFFFFFF & color)));
        }

        // Set the view colors
        View colorView[] = {
                holder.colorView1,
                holder.colorView2,
                holder.colorView3,
                holder.colorView4
        };

        for (int i = 0; i < 4; i++) {
            colorView[i].setBackgroundColor(
                    getBlendColor(
                            color,
                            R.color.grey,
                            (0.8f - (0.3f * i)),
                            activity
                    )
            );
        }

        // Set actions
        holder.actionPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove element
                itemColor.removeColorButtonRecent(color);
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                // add primary color to list
                itemColor.addColorButtonRecent(itemColor.getColorButton());
                notifyItemInserted(getItemCount());
                notifyItemRangeChanged(getItemCount() - 1, getItemCount());
                // set color
                itemColor.setColorButton(color);
                setPrimaryColor();
                // save again to json prefs
                preferences.setRecentColor(itemColor);
            }
        });

        holder.actionRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(activity)
                        .content(R.string.settings_color_dialog_remove)
                        .positiveText(R.string.remove_ac)
                        .positiveColorRes(R.color.red)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // remove element
                                itemColor.removeColorButtonRecent(color);
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                                // save again to json prefs
                                preferences.setRecentColor(itemColor);
                            }
                        })
                        .negativeText(R.string.dialog_cancel)
                        .negativeColorRes(R.color.dark_secondary)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemColor.getColorButtonRecents().length;
    }

    private void setPrimaryColor() {
        int primaryColor = itemColor.getColorButton();
        isDeckShouldCleared = true;

        View colorView[] = {
                activity.findViewById(R.id.view_color_1),
                activity.findViewById(R.id.view_color_2),
                activity.findViewById(R.id.view_color_3),
                activity.findViewById(R.id.view_color_4)
        };

        for (int i = 0; i < 4; i++) {
            colorView[i].setBackgroundColor(
                    getBlendColor(
                            primaryColor,
                            R.color.grey,
                            (0.8f - (0.3f * i)),
                            activity
                    )
            );
        }

        try {
            ((TextView) activity.findViewById(R.id.layout_color_id)).setText(String.format("#%06X", (0xFFFFFF & activity.getResources().getColor(primaryColor))));
        } catch (Resources.NotFoundException e) {
            ((TextView) activity.findViewById(R.id.layout_color_id)).setText(String.format("#%06X", (0xFFFFFF & primaryColor)));
        }

        // set to prefs color
        preferences.setColor(primaryColor);
    }
}