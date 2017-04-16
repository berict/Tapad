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
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.app.theme.ColorData;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.DetailViewHolder> {
    private ColorData colorData;
    private int rowLayout;
    private Activity activity;

    private WindowService window = new WindowService();

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout colorLayout;
        TextView colorTextId;
        View colorView1;
        View colorView2;
        View colorView3;
        View colorView4;
        TextView actionPrimary;
        TextView actionDelete;

        public DetailViewHolder(View view) {
            super(view);
            colorLayout = (RelativeLayout) view.findViewById(R.id.layout_color);
            colorTextId = (TextView) view.findViewById(R.id.layout_color_title);
            colorView1 = view.findViewById(R.id.view_color_1);
            colorView2 = view.findViewById(R.id.view_color_2);
            colorView3 = view.findViewById(R.id.view_color_3);
            colorView4 = view.findViewById(R.id.view_color_4);
            actionDelete = (TextView) view.findViewById(R.id.layout_color_action_delete);
            actionPrimary = (TextView) view.findViewById(R.id.layout_color_action_primary);
        }
    }

    public ColorAdapter(ColorData colorData, int rowLayout, Activity activity) {
        this.colorData = colorData;
        this.rowLayout = rowLayout;
        this.activity = activity;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, final int position) {
        Log.d("Holder", String.valueOf(colorData.getColorButtonFavorite(position)));
        final int color = colorData.getColorButtonFavorite(position);

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
            try {
                colorView[i].setBackgroundColor(
                        window.getBlendColor(
                                activity.getResources().getColor(color),
                                activity.getResources().getColor(R.color.grey),
                                (0.8f - (0.3f * i))
                        )
                );
            } catch (Resources.NotFoundException e) {
                colorView[i].setBackgroundColor(
                        window.getBlendColor(
                                color,
                                activity.getResources().getColor(R.color.grey),
                                (0.8f - (0.3f * i))
                        )
                );
            }
        }

        // Set actions
        holder.actionPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove element
                colorData.removeColorButtonFavorite(color);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                // add primary color to list
                colorData.addColorButtonFavorite(colorData.getColorButton());
                notifyItemInserted(getItemCount());
                notifyItemRangeChanged(getItemCount() - 1, getItemCount());
                // set color
                colorData.setColorButton(color);
                setPrimaryColor();
            }
        });

        holder.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(activity)
                        .content(R.string.settings_color_dialog_delete)
                        .positiveText(R.string.delete_ac)
                        .positiveColorRes(R.color.red)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // remove element
                                colorData.removeColorButtonFavorite(color);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
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
        return colorData.getColorButtonFavorites().length;
    }

    private void setPrimaryColor() {
        int primaryColor = colorData.getColorButton();

        View colorView[] = {
                activity.findViewById(R.id.view_color_1),
                activity.findViewById(R.id.view_color_2),
                activity.findViewById(R.id.view_color_3),
                activity.findViewById(R.id.view_color_4)
        };

        for (int i = 0; i < 4; i++) {
            try {
                colorView[i].setBackgroundColor(
                        window.getBlendColor(
                                activity.getResources().getColor(primaryColor),
                                activity.getResources().getColor(R.color.grey),
                                (0.8f - (0.3f * i))
                        )
                );
            } catch (Resources.NotFoundException e) {
                colorView[i].setBackgroundColor(
                        window.getBlendColor(
                                primaryColor,
                                activity.getResources().getColor(R.color.grey),
                                (0.8f - (0.3f * i))
                        )
                );
            }
        }

        try {
            window.getTextView(R.id.layout_color_id, activity).setText(String.format("#%06X", (0xFFFFFF & activity.getResources().getColor(primaryColor))));
        } catch (Resources.NotFoundException e) {
            window.getTextView(R.id.layout_color_id, activity).setText(String.format("#%06X", (0xFFFFFF & primaryColor)));
        }
    }
}