package com.bedrock.padder.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.about.Item;

import static com.bedrock.padder.helper.WindowHelper.getStringFromId;
import static com.bedrock.padder.model.preferences.Preferences.APPLICATION_ID;

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
        holder.itemText.setText(item[position].getText(context));

        if(position == getItemCount() - 1) {
            // last item on list, hide divider
            holder.divider.setVisibility(View.GONE);
        }

        if (item[position].getHint(context) == null) {
            // hint null
            holder.itemHint.setVisibility(View.GONE);
        } else if (item[position].isHintVisible() == false) {
            // hint invisible
            holder.itemHint.setVisibility(View.GONE);

            if (item[position].getHint(context).startsWith("http")) {
                // link available check
                anim.circularRevealTouch(holder.itemLayout, R.id.layout_placeholder,
                        new AccelerateDecelerateInterpolator(), new Runnable() {
                            @Override
                            public void run() {
                                window.setRecentColor(item[holder.getAdapterPosition()].getText(context), R.color.colorAccent, activity);
                                intent.intentLink(activity, item[holder.getAdapterPosition()].getHint(context), 400);
                            }
                        }, 400, 0, activity);
            }
        } else {
            // hint visible
            holder.itemHint.setText(item[position].getHint(context));

            if (item[position].getHint(context).startsWith("http")) {
                // link available check
                anim.circularRevealTouch(holder.itemLayout, R.id.layout_placeholder,
                        new AccelerateDecelerateInterpolator(), new Runnable() {
                            @Override
                            public void run() {
                                window.setRecentColor(item[holder.getAdapterPosition()].getText(context), R.color.colorAccent, activity);
                                intent.intentLink(activity, item[holder.getAdapterPosition()].getHint(context), 400);
                            }
                        }, 400, 0, activity);
            }
        }

        if (getExceptionalRunnable(item[position].getText()) != null) {
            if (item[position].isRunnableWithAnim() == true) {
                // Runnable with reveal anim
                anim.circularRevealTouch(holder.itemLayout, R.id.layout_placeholder,
                        new AccelerateDecelerateInterpolator(),
                        getExceptionalRunnable(item[position].getText()),
                        400, 0, activity);
            } else {
                // Runnable with no anim
                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getExceptionalRunnable(item[holder.getAdapterPosition()].getText()).run();
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
                try {
                    // no image res fallback
                    Log.e("ItemAdapter", "Image res not found, trying with logo prefix");
                    holder.itemIcon.setImageResource(window.getDrawableId("ic_logo_" + item[position].getImage()));
                } catch (Exception e2) {
                    try {
                        // no image res fallback
                        Log.e("ItemAdapter", "Image res not found, trying with icon prefix");
                        holder.itemIcon.setImageResource(window.getDrawableId("ic_" + item[position].getImage() + "_black"));
                    } catch (Exception e3) {
                        Log.e("ItemAdapter", "Failed to find icon\n" + e3.getMessage());
                    }
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
                        intent.intentLink(a, getStringFromId("info_tapad_info_check_update_link", a), 400);
                    }
                };
                break;
            case "info_tapad_info_tester":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        w.setRecentColor(w.getStringId("info_tapad_info_tester"), R.color.colorAccent, a);
                        intent.intentLink(a, getStringFromId("info_tapad_info_tester_link", a), 400);
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
            case "info_tapad_info_dev":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // intent to developer details
                        intent.intentWithExtra(a, "activity.AboutActivity", "about", "dev", 0);
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
            case "info_tapad_others_create":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(activity)
                                .title(getStringFromId("info_tapad_others_create_dialog_title", a))
                                .content(getStringFromId("info_tapad_others_create_dialog_content", a))
                                .positiveText(getStringFromId("info_tapad_others_create_dialog_open", a))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        intent.intentLink(a, getStringFromId("info_tapad_others_create_link", a), 0);
                                    }
                                })
                                .neutralText(getStringFromId("dialog_cancel", a))
                                .negativeText(getStringFromId("info_tapad_others_create_dialog_clipboard", a))
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        try {
                                            // copy to clipboard
                                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(android.content.Context.CLIPBOARD_SERVICE);
                                            android.content.ClipData clip = android.content.ClipData.newPlainText(
                                                    "preset-store",
                                                    getStringFromId("info_tapad_others_create_link", a)
                                            );
                                            assert clipboard != null;
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(activity, R.string.info_tapad_others_create_dialog_clipboard_success, Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(activity, R.string.info_tapad_others_create_dialog_clipboard_failure, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .stackingBehavior(StackingBehavior.ALWAYS)
                                .show();
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
                        new MaterialDialog.Builder(activity)
                                .title(getStringFromId("info_berict_action_report_bug", a))
                                .content(getStringFromId("info_berict_action_report_bug_dialog_content", a))
                                .positiveText(getStringFromId("info_berict_action_report_bug_dialog_github", a))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        intent.intentLink(a, getStringFromId("info_berict_action_report_bug_dialog_github_url", a), 0);
                                    }
                                })
                                .neutralText(getStringFromId("dialog_cancel", a))
                                .negativeText(getStringFromId("info_berict_action_report_bug_dialog_email", a))
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        intent.intentWithExtra(a, "activity.FeedbackActivity", "feedbackMode", "report_bug", 400);
                                    }
                                })
                                .stackingBehavior(StackingBehavior.ALWAYS)
                                .show();
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
                        new MaterialDialog.Builder(activity)
                                .title(getStringFromId("info_berict_action_translate", a))
                                .content(getStringFromId("info_berict_action_translate_dialog_content", a))
                                .positiveText(getStringFromId("info_berict_action_translate_dialog_github", a))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        intent.intentLink(a, getStringFromId("info_berict_action_translate_dialog_github_url", a), 0);
                                    }
                                })
                                .negativeText(getStringFromId("dialog_cancel", a))
                                .stackingBehavior(StackingBehavior.ALWAYS)
                                .show();
                    }
                };
                break;
            case "info_tapad_others_recommend":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        intent.intentShareText(a,
                                getStringFromId("info_tapad_others_recommend_share_subject", a),
                                getStringFromId("info_tapad_others_recommend_share_text", a),
                                getStringFromId("info_tapad_others_recommend_share_hint", a),
                                400);
                    }
                };
                break;
            case "info_berict_action_report_bug":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(activity)
                                .title(getStringFromId("info_berict_action_report_bug", a))
                                .content(getStringFromId("info_berict_action_report_bug_dialog_content", a))
                                .positiveText(getStringFromId("info_berict_action_report_bug_dialog_github", a))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        intent.intentLink(a, getStringFromId("info_berict_action_report_bug_dialog_github_url", a), 0);
                                    }
                                })
                                .neutralText(getStringFromId("dialog_cancel", a))
                                .negativeText(getStringFromId("info_berict_action_report_bug_dialog_email", a))
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        intent.intentWithExtra(a, "activity.FeedbackActivity", "feedbackMode", "report_bug", 400);
                                    }
                                })
                                .stackingBehavior(StackingBehavior.ALWAYS)
                                .show();
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
            case "info_berict_action_contribute":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(activity)
                                .title(getStringFromId("info_berict_action_contribute", a))
                                .content(getStringFromId("info_berict_action_contribute_dialog_content", a))
                                .positiveText(getStringFromId("info_berict_action_contribute_dialog_github", a))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        intent.intentLink(a, getStringFromId("info_berict_action_contribute_dialog_github_url", a), 0);
                                    }
                                })
                                .negativeText(getStringFromId("dialog_cancel", a))
                                .stackingBehavior(StackingBehavior.ALWAYS)
                                .show();
                    }
                };
                break;
            case "info_berict_action_donate":
                exceptionalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(activity)
                                .title(getStringFromId("info_berict_action_donate", a))
                                .content(getStringFromId("info_berict_action_donate_dialog_content", a))
                                .positiveText(getStringFromId("info_berict_action_donate_dialog_positive", a))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        intent.intent(a, "activity.VideoAdActivity");
                                    }
                                })
                                .negativeText(getStringFromId("dialog_no_thanks", a))
                                .show();
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