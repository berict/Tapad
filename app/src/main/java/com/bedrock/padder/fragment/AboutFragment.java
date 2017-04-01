package com.bedrock.padder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bedrock.padder.R;
import com.bedrock.padder.activity.MainActivity;
import com.bedrock.padder.helper.AdmobService;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.preset.Preset;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class AboutFragment extends Fragment {

    private AppbarService ab = new AppbarService();
    private WindowService w = new WindowService();
    private AdmobService ad = new AdmobService();
    private IntentService intent = new IntentService();
    private AnimService anim = new AnimService();
    private MainActivity main = new MainActivity();

    private int circularRevealDuration = 400;
    private int fadeAnimDuration = 200;

    int themeColor = R.color.hello;
    Activity a;
    View v;
    Gson gson = new Gson();

    private OnFragmentInteractionListener mListener;

    public AboutFragment() {
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
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        v = getView();
        setSchemeInfo();
        ad.requestLoadNativeAd(ad.getNativeAdView(R.id.adView_about, a));
    }

    @Override
    public void onPause() {
        ad.pauseNativeAdView(R.id.adView_about, a);

        super.onPause();
    }

    @Override
    public void onResume() {
        ad.resumeNativeAdView(R.id.adView_about, a);

        super.onResume();
    }

    @Override
    public void onDestroy() {
        ad.destroyNativeAdView(R.id.adView_about, a);

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
        a = getActivity();
    }

    private int coord[] = {0, 0};

    private void setSchemeInfo() {
        Preset currentPreset = getCurrentPreset();
        Log.d("currentPreset", "NAME : " + currentPreset.getAbout().getTitle(a));
        themeColor = currentPreset.getAbout().getActionbarColor();
        w.setRecentColor(R.string.about, 0, themeColor, a);

        ab.setNav(1, null, a, v);
        ab.setColor(themeColor, a, v);
        ab.setTitle(R.string.about, a, v);

        // Cardview
        w.getImageView(R.id.cardview_music_image, v).setImageResource(w.getDrawableId("about_album_" + currentPreset.getMusic().getNameId().replace("preset_", "")));
        w.getTextView(R.id.cardview_music_song, v).setText(w.getStringId(currentPreset.getMusic().getNameId() + "_full"));
        w.getTextView(R.id.cardview_music_explore, v).setTextColor(getResources().getColor(themeColor));
        w.getTextView(R.id.cardview_music_change, v).setTextColor(getResources().getColor(themeColor));

        // artist
        w.getView(R.id.cardview_artist, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_music_image, "transition", "about", "now_playing", 0, v);
            }
        });

        w.getView(R.id.cardview_music_explore, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_music_image, "transition", "about", "now_playing", 0, v);
            }
        });

        w.getView(R.id.cardview_music_change, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.showDialogPreset(a);
            }
        });

        // tapad
        w.getView(R.id.cardview_about, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_about_image, "transition", "about", "tapad", 0, v);
            }
        });

        w.getView(R.id.cardview_about_explore, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_about_image, "transition", "about", "tapad", 0, v);
            }
        });

        w.getView(R.id.cardview_about_settings, v).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coord[0] = (int) event.getRawX();
                coord[1] = (int) event.getRawY();

                return false;
            }
        });

        w.getView(R.id.cardview_about_settings, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                w.setRecentColor(R.string.settings, 0, R.color.colorAccent, a);
                anim.circularRevealinpx(R.id.placeholder,
                        coord[0], coord[1],
                        0, (int) Math.hypot(coord[0], coord[1]) + 200, new AccelerateDecelerateInterpolator(),
                        circularRevealDuration, 0, a);

                Handler about = new Handler();
                about.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        w.getView(R.id.fragment_settings_container, a).setVisibility(View.VISIBLE);
                    }
                }, circularRevealDuration);

                anim.fadeOut(R.id.placeholder, circularRevealDuration, fadeAnimDuration, a);

                main.setSettingVisible(true);
            }
        });

        // developer
        w.getView(R.id.cardview_dev, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_dev_image, "transition", "about", "dev", 0, v);
            }
        });

        w.getView(R.id.cardview_dev_explore, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentSharedElementWithExtra(a, "activity.AboutActivity",
                        R.id.cardview_dev_image, "transition", "about", "dev", 0, v);
            }
        });
    }

    private Preset getCurrentPreset() {
        SharedPreferences prefs = a.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        Preset presets[] = new Preset[] {
                gson.fromJson(getResources().getString(R.string.json_hello), Preset.class),
                gson.fromJson(getResources().getString(R.string.json_roses), Preset.class),
                gson.fromJson(getResources().getString(R.string.json_faded), Preset.class)
        };
        Log.d("currentPreset", "returned preset id " + prefs.getInt("scheme", 0));
        return presets[prefs.getInt("scheme", 0)];
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
