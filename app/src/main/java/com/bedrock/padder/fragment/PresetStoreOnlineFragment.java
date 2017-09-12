package com.bedrock.padder.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.activity.PresetStoreActivity;
import com.bedrock.padder.adapter.PresetStoreAdapter;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.ApiHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.Schema;

import rx.Subscriber;

public class PresetStoreOnlineFragment extends Fragment implements Refreshable {

    private WindowHelper window = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private ApiHelper api = new ApiHelper();

    private String TAG = "PSOnline";

    AppCompatActivity a;
    View v;

    public PresetStoreAdapter presetStoreAdapter = null;

    public PresetStoreOnlineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preset_store_online, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        v = getView();
        setUi();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        a = (AppCompatActivity) getActivity();
    }

    RecyclerView recyclerView = null;
    View recyclerViewLoading = null;
    View recyclerViewFailed = null;
    View recyclerViewFailedRetry = null;

    void setUi() {
        recyclerView = window.getRecyclerView(R.id.layout_online_preset_store_recycler_view, v);
        recyclerViewLoading = window.getView(R.id.layout_online_preset_store_recycler_view_loading, v);
        recyclerViewFailed = window.getView(R.id.layout_online_preset_store_recycler_view_failed, v);
        recyclerViewFailedRetry = window.getView(R.id.layout_online_preset_store_recycler_view_failed_retry, v);

        // adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(a);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        // firebase check
        setAdapter();
    }

    private void setLoadingFinished(boolean isFinished) {
        if (isFinished) {
            // finished, hide loading and show recyclerView
            Log.d(TAG, "Loading finished");
            anim.fadeOut(recyclerViewLoading, 0, 200, a);
            anim.fadeIn(recyclerView, 200, 200, "rvIn", a);
        } else {
            // started, show loading
            anim.fadeOut(recyclerView, 0, 200, a);
            anim.fadeIn(recyclerViewLoading, 200, 200, "rvLoadingIn", a);
        }
    }

    private void setLoadingFailed() {
        Log.d(TAG, "Loading failed");
        anim.fadeOut(recyclerView, 0, 200, a);
        anim.fadeOut(recyclerViewLoading, 0, 200, a);
        anim.fadeIn(recyclerViewFailed, 200, 200, "rvIn", a);
        recyclerViewFailedRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retry button
                setAdapter();
            }
        });
        Handler handler = new Handler();
        if (((TabLayout)window.getView(R.id.layout_tab_layout, a)).getSelectedTabPosition() == 1) {
            // only when the online tab is selected
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new MaterialDialog.Builder(a)
                            .title(R.string.preset_store_download_no_connection_dialog_title)
                            .content(R.string.preset_store_download_no_connection_dialog_text)
                            .contentColorRes(R.color.dark_primary)
                            .neutralText(R.string.dialog_close)
                            .show();
                }
            }, 200);
        }
    }

    private Handler connectionTimeout = new Handler();

    private void setAdapter() {
        connectionTimeout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (window.getView(R.id.layout_online_preset_store_recycler_view_loading, v).getVisibility() == View.VISIBLE) {
                    // loading for 10 seconds, prompt user to retry or not
                    if (((TabLayout)window.getView(R.id.layout_tab_layout, a)).getSelectedTabPosition() == 1) {
                        // only when the online page is visible
                        setLoadingFailed();
                        if (a != null) {
                            new MaterialDialog.Builder(a)
                                    .title(R.string.preset_store_connection_timeout_dialog_title)
                                    .content(R.string.preset_store_connection_timeout_dialog_text)
                                    .contentColorRes(R.color.dark_primary)
                                    .positiveText(R.string.preset_store_connection_timeout_dialog_positive)
                                    .negativeText(R.string.preset_store_connection_timeout_dialog_negative)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            setAdapter();
                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                }
            }
        }, 10000);

        if (isConnected(a)) {
            Log.d(TAG, "setAdapter");
            // attach the adapter to the layout
            api.getObservableSchema().subscribe(new Subscriber<Schema>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "Error parsing schema, " + e.getMessage());
                }

                @Override
                public void onNext(Schema schema) {
                    if (schema == null ||
                            schema.getPresets() == null ||
                            schema.getVersion() == null) {
                        // corrupted metadata, download again
                        Log.e(TAG, "Schema is null");
                        setLoadingFailed();
                    } else {
                        // attach adapter while its not null
                        presetStoreAdapter = new PresetStoreAdapter(
                                schema,
                                R.layout.adapter_preset_store, a
                        );
                        window.getRecyclerView(R.id.layout_online_preset_store_recycler_view, v).setAdapter(presetStoreAdapter);
                        setLoadingFinished(true);
                    }
                }
            });
        } else {
            setLoadingFailed();
        }
    }

    private boolean isConnected(Context context) {
        // returns whether the device is connected to the internet
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void refresh() {
        if (!PresetStoreActivity.isPresetDownloading) {
            // only update when the preset is not downloading
            setAdapter();
        }
    }

    @Override
    public void clear() {
        Log.i(TAG, "clear()");
        if (recyclerView != null && recyclerView.getVisibility() != View.GONE) {
            recyclerView.setVisibility(View.GONE);
        }

        if (recyclerViewLoading != null && recyclerViewLoading.getVisibility() != View.VISIBLE) {
            recyclerViewLoading.setVisibility(View.VISIBLE);
        }
    }
}
