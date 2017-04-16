package com.bedrock.padder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.WindowService;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.DetailViewHolder> {
    private Integer[] colorFavorite;
    //private ColorData colorData;
    private int rowLayout;
    private Context context;
    private Activity activity;

    private WindowService window = new WindowService();

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView colorTextId;
        View colorView1;
        View colorView2;
        View colorView3;
        View colorView4;
        TextView actionPrimary;
        TextView actionDelete;

        public DetailViewHolder(View view) {
            super(view);
            colorTextId = (TextView) view.findViewById(R.id.layout_color_title);
            colorView1 = view.findViewById(R.id.view_color_1);
            colorView2 = view.findViewById(R.id.view_color_2);
            colorView3 = view.findViewById(R.id.view_color_3);
            colorView4 = view.findViewById(R.id.view_color_4);
            actionDelete = (TextView) view.findViewById(R.id.layout_color_action_delete);
            actionPrimary = (TextView) view.findViewById(R.id.layout_color_action_primary);
        }
    }

    public ColorAdapter(Integer[] colorFavorite, int rowLayout, Context context, Activity activity) {
        this.colorFavorite = colorFavorite;
        //this.colorData = colorData;
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
        Log.d("Holder", String.valueOf(colorFavorite[position]));
        Log.d("Holder length", String.valueOf(colorFavorite.length));
        final int color = colorFavorite[position];

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
                //colorData.removeColorButtonFavorite(color);
                //colorData.setColorButton(color);
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
                                //colorData.removeColorButtonFavorite(color);
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
        return colorFavorite.length;
    }
}