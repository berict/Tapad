package com.bedrock.padder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AdmobService;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.SoundService;
import com.bedrock.padder.helper.TutorialService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.preset.Preset;
import com.google.android.gms.ads.AdListener;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.coord;
import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isDeckShouldCleared;
import static com.bedrock.padder.activity.MainActivity.isPresetVisible;
import static com.bedrock.padder.activity.MainActivity.isSettingVisible;
import static com.bedrock.padder.activity.MainActivity.presets;
import static com.bedrock.padder.activity.MainActivity.setSettingVisible;
import static com.bedrock.padder.activity.MainActivity.showSettingsFragment;
import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class AboutFragment extends Fragment {

    private AppbarService ab = new AppbarService();
    private WindowService w = new WindowService();
    private AdmobService ad = new AdmobService();
    private IntentService intent = new IntentService();
    private AnimService anim = new AnimService();
    private TutorialService tut = new TutorialService();
    private SoundService sound = new SoundService();

    private int circularRevealDuration = 400;
    private int fadeAnimDuration = 200;

    int themeColor = R.color.hello;
    AppCompatActivity a;
    View v;

    private OnFragmentInteractionListener mListener;
    private MaterialDialog PresetDialog;
    private SharedPreferences prefs;

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
        ad.requestLoadNativeAd(ad.getNativeAdView(R.id.adView_about, a));
        setSchemeInfo();
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
        a = (AppCompatActivity)getActivity();
        prefs = a.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
    }

    private void showPresetDialog(Activity a) {
        tut.tutorialStop(a);
        sound.soundAllStop();

        final int defaultPreset = getScheme();
        int color = currentPreset.getAbout().getActionbarColor();

        PresetDialog = new MaterialDialog.Builder(a)
                .title(R.string.dialog_preset_title)
                .items(R.array.presets)
                .autoDismiss(false)
                .itemsCallbackSingleChoice(defaultPreset, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        setScheme(which);
                        int selectedPresetColor = presets[which].getAbout().getActionbarColor();
                        PresetDialog.getBuilder()
                                .widgetColorRes(selectedPresetColor)
                                .positiveColorRes(selectedPresetColor);
                        setSchemeInfo();

                        return true;
                    }
                })
                .alwaysCallSingleChoiceCallback()
                .widgetColorRes(color)
                .positiveText(R.string.dialog_preset_positive)
                .positiveColorRes(color)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PresetDialog.dismiss();
                    }
                })
                .negativeText(R.string.dialog_preset_negative)
                .negativeColorRes(R.color.dark_secondary)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setScheme(defaultPreset);
                        PresetDialog.dismiss();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (defaultPreset != getScheme()) {
                            // preset changed
                            loadPreset();
                            isDeckShouldCleared = true;
                        }
                        setSchemeInfo();
                        isPresetVisible = false;
                    }
                })
                .show();
    }

    public void setSchemeInfo() {
        Preset currentPreset = presets[getScheme()];
        Log.d("currentPreset", "NAME : " + currentPreset.getAbout().getTitle(a));
        themeColor = currentPreset.getAbout().getActionbarColor();

        ab.setNav(3, null, a, v);
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
                    showPresetDialog(a);
                    isPresetVisible = true;
                }
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
                coord[2] = (int) event.getRawX();
                coord[3] = (int) event.getRawY();

                return false;
            }
        });

        w.getView(R.id.cardview_about_settings, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSettingVisible == false) {
                    anim.circularRevealInPx(R.id.placeholder,
                            coord[2], coord[3],
                            0, (int) Math.hypot(coord[2], coord[3]) + 200, new AccelerateDecelerateInterpolator(),
                            circularRevealDuration, 0, a);

                    Handler about = new Handler();
                    about.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showSettingsFragment(a);
                        }
                    }, circularRevealDuration);

                    anim.fadeOut(R.id.placeholder, circularRevealDuration, fadeAnimDuration, a);

                    setSettingVisible(true);
                }
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
            ad.getNativeAdView(R.id.adView_about, a).setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Ad loaded
                    Log.d("AdView", "Loaded");
                    anim.fadeOut(R.id.cardview_ad_loading, 0, 400, v, a);
                    w.getView(R.id.cardview_ad_failed, v).setVisibility(View.GONE);
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

    private void setScheme(int scheme) {
        prefs.edit().putInt("scheme", scheme).apply();
    }

    int getScheme() {
        return prefs.getInt("scheme", 0);
    }

    private void loadPreset() {
        sound.loadSchemeSound(presets[getScheme()], a);
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
