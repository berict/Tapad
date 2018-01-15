package com.bedrock.padder.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AdMobHelper;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.google.android.gms.ads.AdListener;
import com.squareup.picasso.Picasso;

import static com.bedrock.padder.activity.MainActivity.coordinate;
import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isPresetVisible;

public class AboutFragment extends Fragment {

    private WindowHelper w = new WindowHelper();
    private AdMobHelper ad = new AdMobHelper();
    private IntentHelper intent = new IntentHelper();
    private AnimateHelper anim = new AnimateHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    private int circularRevealDuration = 400;
    private int fadeAnimDuration = 200;

    public boolean isAdsShown = false;

    AppCompatActivity a;
    View v;

    private OnFragmentInteractionListener mListener;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        v = getView();
        ad.requestLoadAd(ad.getAdView(R.id.adView_about, a));
        setPresetInfo();
    }

    @Override
    public void onPause() {
        ad.pauseAdView(R.id.adView_about, a);

        super.onPause();
    }

    @Override
    public void onResume() {
        ad.resumeAdView(R.id.adView_about, a);

        super.onResume();
    }

    @Override
    public void onDestroy() {
        ad.destroyAdView(R.id.adView_about, a);

        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        a = (AppCompatActivity)getActivity();
    }

    Menu menu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        int i = 0;
        while (true) {
            try {
                menu.getItem(i++).setVisible(false);
            } catch (Exception e) {
                break;
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("itemSelected", item.toString());
        if (item != menu.findItem(R.id.action_about) &&
                item != menu.findItem(R.id.action_settings) &&
                item != menu.findItem(R.id.action_help)) {
            if (!item.toString().equals("About") &&
                    !item.toString().equals("Settings") &&
                    !item.toString().equals("Help")) {
                // only the back icon
                pressBack();
            }
        }
        return true;
    }

    private void pressBack() {
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        a.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        a.dispatchKeyEvent(kUp);
    }

    public void setPresetInfo() {
        toolbar.setActionBar(a, v);
        toolbar.setActionBarTitle(R.string.about);
        toolbar.setActionBarPadding(a, v);
        toolbar.setActionBarDisplayHomeAsUpIcon(R.drawable.ic_close_white);

        if (currentPreset != null) {
            // CardView
            w.getView(R.id.cardview_music_layout, v).setVisibility(View.VISIBLE);
            Picasso.with(a)
                    .load("file:" + currentPreset.getAbout().getImage(currentPreset))
                    .placeholder(R.drawable.ic_image_album_placeholder)
                    .error(R.drawable.ic_image_album_error)
                    .into(w.getImageView(R.id.cardview_music_image, v));
            w.getTextView(R.id.cardview_music_song, v).setText(currentPreset.getAbout().getTitle());
            w.getTextView(R.id.cardview_music_explore, v).setTextColor(currentPreset.getAbout().getColor());
            w.getTextView(R.id.cardview_music_change, v).setTextColor(currentPreset.getAbout().getColor());

            toolbar.setActionBarColor(currentPreset.getAbout().getColor(), a);
        } else {
            w.getView(R.id.cardview_music_layout, v).setVisibility(View.GONE);

            toolbar.setActionBarColor(R.color.colorPrimary, a);
        }

        // artist
        w.getView(R.id.cardview_music, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_music_image, "transition", "about", "now_playing", 0, v);
            }
        });

        w.getView(R.id.cardview_music_explore, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_music_image, "transition", "about", "now_playing", 0, v);
            }
        });

        w.getView(R.id.cardview_music_change, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPresetVisible == false) {
                    intent.intent(a, "activity.PresetStoreActivity");
                }
            }
        });

        // preset store
        w.getView(R.id.cardview_preset_store, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.intentSharedElementWithExtra(a, "activity.PresetStoreActivity",
                        R.id.cardview_preset_store_image, "transition", "about", "now_playing", 0, v);
            }
        });

        w.getView(R.id.cardview_preset_store_explore, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.intentSharedElementWithExtra(a, "activity.PresetStoreActivity",
                        R.id.cardview_preset_store_image, "transition", "about", "now_playing", 0, v);
            }
        });

        // tapad
        w.getView(R.id.cardview_about, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_about_image, "transition", "about", "tapad", 0, v);
            }
        });

        w.getView(R.id.cardview_about_explore, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_about_image, "transition", "about", "tapad", 0, v);
            }
        });

        w.getView(R.id.cardview_about_settings, v).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinate[2] = (int) event.getRawX();
                coordinate[3] = (int) event.getRawY();

                return false;
            }
        });

        w.getView(R.id.cardview_about_settings, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim.circularRevealInPx(R.id.placeholder,
                        coordinate[2], coordinate[3],
                        0, (int) Math.hypot(coordinate[2], coordinate[3]) + 200, new AccelerateDecelerateInterpolator(),
                        circularRevealDuration, 0, a);

                intent.intent(a, "activity.SettingsActivity", circularRevealDuration);
            }
        });

        // developer
        w.getView(R.id.cardview_dev, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_dev_image, "transition", "about", "dev", 0, v);
            }
        });

        w.getView(R.id.cardview_dev_explore, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_dev_image, "transition", "about", "dev", 0, v);
            }
        });

        // Blank ads
        if (ad.isConnected(a)) {
            // connected to internet, check ad is working
            ad.getAdView(R.id.adView_about, a).setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Ad loaded
                    Log.d("AdView", "Loaded");
                    anim.fadeOut(R.id.cardview_ad_loading, 0, 400, v, a);
                    w.getView(R.id.cardview_ad_failed, v).setVisibility(View.GONE);
                    w.getView(R.id.adView_about, v).setVisibility(View.VISIBLE);
                    isAdsShown = true;
                    Log.d("AdView", "adView visible = " + String.valueOf(w.getView(R.id.adView_about, v).getVisibility() == View.VISIBLE));
                    super.onAdLoaded();
                }
                @Override
                public void onAdFailedToLoad(int i) {
                    // Ad failed to load
                    Log.d("AdView", "Failed to load");
                    anim.fadeOut(R.id.cardview_ad_loading, 0, 400, v, a);
                    anim.fadeIn(R.id.cardview_ad_failed, 400, 400, "adFailFadeIn", v, a);
                    super.onAdFailedToLoad(i);
                }
            });
        } else {
            // not connected to internet
            Log.d("AdView", "Failed to connect to the internet");
            w.getView(R.id.cardview_ad, v).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener = null;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
