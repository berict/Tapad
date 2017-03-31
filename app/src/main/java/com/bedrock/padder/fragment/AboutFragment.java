package com.bedrock.padder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AdmobService;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.preset.Preset;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class AboutFragment extends Fragment {

    private AppbarService ab = new AppbarService();
    private WindowService w = new WindowService();
    private AdmobService ad = new AdmobService();

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
    }

    private Preset getCurrentPreset() {
        SharedPreferences prefs = a.getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);
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
