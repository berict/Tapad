package com.bedrock.padder.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.about.Item;

import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.DetailViewHolder> {
    private Item[] item;
    private int rowLayout;
    private Context context;
    private Activity activity;
    
    private WindowHelper window = new WindowHelper();

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

    private IntentHelper intent = new IntentHelper();
    private AnimateHelper anim =  new AnimateHelper();

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, int position) {
        holder.itemText.setText(window.getStringFromId(item[position].getTextId(), activity));

        if(position == getItemCount() - 1) {
            // last item on list, hide divider
            holder.divider.setVisibility(View.GONE);
        }

        if(item[position].getHintId() == null) {
            // hint null
            holder.itemHint.setVisibility(View.GONE);
        } else if (item[position].getHintIsVisible() == false) {
            // hint invisible
            holder.itemHint.setVisibility(View.GONE);

            if(item[position].getHintId().startsWith("http")) {
                // link available check
                anim.circularRevealTouch(holder.itemLayout, R.id.layout_placeholder,
                        new AccelerateDecelerateInterpolator(), new Runnable() {
                            @Override
                            public void run() {
                                window.setRecentColor(item[holder.getAdapterPosition()].getTextId(), R.color.colorAccent, activity);
                                intent.intentLink(activity, item[holder.getAdapterPosition()].getHintId(), 400);
                            }
                        }, 400, 0, activity);
            }
        } else {
            // hint visible
            holder.itemHint.setText(item[position].getHintId());

            if(item[position].getHintId().startsWith("http")) {
                // link available check
                anim.circularRevealTouch(holder.itemLayout, R.id.layout_placeholder,
                        new AccelerateDecelerateInterpolator(), new Runnable() {
                            @Override
                            public void run() {
                                window.setRecentColor(item[holder.getAdapterPosition()].getTextId(), R.color.colorAccent, activity);
                                intent.intentLink(activity, item[holder.getAdapterPosition()].getHintId(), 400);
                            }
                        }, 400, 0, activity);
            }
        }

        if(getExceptionalRunnable(item[position].getTextId()) != null) {
            if (item[position].getRunnableIsWithAnim() == true) {
                // Runnable with reveal anim
                anim.circularRevealTouch(holder.itemLayout, R.id.layout_placeholder,
                        new AccelerateDecelerateInterpolator(),
                        getExceptionalRunnable(item[position].getTextId()),
                        400, 0, activity);
            } else {
                // Runnable with no anim
                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getExceptionalRunnable(item[holder.getAdapterPosition()].getTextId()).run();
                    }
                });
            }
        }

        if(item[position].getImage() == null || item[position].getImage().isEmpty()) {
            // no icon provided
            holder.itemIcon.setVisibility(View.INVISIBLE);
        } else {
            try {
                holder.itemIcon.setImageResource(window.getDrawableId(item[position].getImage()));
            } catch (Exception e1) {
                e1.getMessage();
                try {
                    // no image res fallback
                    Log.i("ItemAdapter", "Image res not found, trying with logo prefix");
                    holder.itemIcon.setImageResource(window.getDrawableId("ic_logo_" + item[position].getImage()));
                } catch (Exception e2) {
                    e2.getMessage();
                    // no image res fallback
                    Log.i("ItemAdapter", "Image res not found, trying with icon prefix");
                    holder.itemIcon.setImageResource(window.getDrawableId("ic_" + item[position].getImage() + "_black"));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return item.length;
    }

    private Runnable getExceptionalRunnable(String textId) {
        Runnable exceptionalRunnable;
        final Activity a = activity;
        final WindowHelper w = new WindowHelper();

        switch (textId) {
            case "info_tapad_info_check_update":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_tapad_info_check_update"), R.color.colorAccent, a);
                        intent.intentLink(a, w.getStringFromId("info_tapad_info_check_update_link", a), 400);
                    }
                };
                break;
            case "info_tapad_info_tester":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_tapad_info_tester"), R.color.colorAccent, a);
                        intent.intentLink(a, w.getStringFromId("info_tapad_info_tester_link", a), 400);
                    }
                };
                break;
            case "info_tapad_info_legal":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // legal info dialog
                        new MaterialDialog.Builder(a)
                                .title(w.getStringId("info_tapad_info_legal"))
                                .content(w.getStringId("info_tapad_info_legal_text"))
                                .contentColorRes(R.color.dark_primary)
                                .positiveText(R.string.dialog_close)
                                .positiveColorRes(R.color.colorAccent)
                                .show();
                    }
                };
                break;
            case "info_tapad_info_changelog":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // changelog info dialog
                        new MaterialDialog.Builder(a)
                                .title(w.getStringId("info_tapad_info_changelog"))
                                .content(w.getStringId("info_tapad_info_changelog_text"))
                                .contentColorRes(R.color.dark_primary)
                                .positiveText(R.string.dialog_close)
                                .positiveColorRes(R.color.colorAccent)
                                .show();
                    }
                };
                break;
            case "info_tapad_info_thanks":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // thanks info dialog
                        new MaterialDialog.Builder(a)
                                .title(w.getStringId("info_tapad_info_thanks"))
                                .content(w.getStringId("info_tapad_info_thanks_text"))
                                .contentColorRes(R.color.dark_primary)
                                .positiveText(R.string.dialog_close)
                                .positiveColorRes(R.color.colorAccent)
                                .show();
                    }
                };
                break;
            case "info_tapad_others_song":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        intent.intentWithExtra(activity, "activity.FeedbackActivity", "feedbackMode", "song", 400);
                    }
                };
                break;
            case "info_tapad_others_feedback":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        intent.intentWithExtra(activity, "activity.FeedbackActivity", "feedbackMode", "feedback", 400);
                    }
                };
                break;
            case "info_tapad_others_report_bug":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        intent.intentWithExtra(a, "activity.FeedbackActivity", "feedbackMode", "report_bug", 400);
                    }
                };
                break;
            case "info_tapad_others_rate":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_tapad_others_rate"), R.color.colorAccent, a);
                        intent.intentMarket(a, APPLICATION_ID, 400);
                    }
                };
                break;
            case "info_tapad_others_translate":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // TODO make translation service available
                        Toast.makeText(a, w.getStringFromId("info_tapad_others_translate_error", a), Toast.LENGTH_SHORT).show();
                    }
                };
                break;
            case "info_tapad_others_recommend":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        intent.intentShareText(a,
                                w.getStringFromId("info_tapad_others_recommend_share_subject", a),
                                w.getStringFromId("info_tapad_others_recommend_share_text", a),
                                w.getStringFromId("info_tapad_others_recommend_share_hint", a),
                                400);
                    }
                };
                break;
            case "info_berict_action_report_bug":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        intent.intentWithExtra(a, "activity.FeedbackActivity", "feedbackMode", "report_bug", 400);
                    }
                };
                break;
            case "info_berict_action_rate":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_berict_action_rate"), R.color.colorAccent, a);
                        intent.intentMarket(a, APPLICATION_ID, 400);
                    }
                };
                break;
            case "info_berict_action_translate":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // TODO make translation service available
                        Toast.makeText(a, w.getStringFromId("info_berict_action_translate_error", a), Toast.LENGTH_SHORT).show();
                    }
                };
                break;
            case "info_berict_action_donate":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // TODO make translation service available
                        Toast.makeText(a, w.getStringFromId("info_berict_action_donate_error", a), Toast.LENGTH_SHORT).show();
                    }
                };
                break;
            default:
                exceptionalRunnable = null;
                break;
        }

        return exceptionalRunnable;
    }
}