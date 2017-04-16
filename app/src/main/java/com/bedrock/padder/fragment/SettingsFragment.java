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
import android.widget.Toast;

import com.bedrock.padder.R;
import com.bedrock.padder.activity.MainActivity;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.preset.Preset;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.isAboutVisible;
import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class SettingsFragment extends Fragment {

    private AppbarService ab = new AppbarService();
    private WindowService w = new WindowService();
    private IntentService intent = new IntentService();
    private AnimService anim = new AnimService();
    private MainActivity main = new MainActivity();

    private int circularRevealDuration = 400;
    private int fadeAnimDuration = 200;

    SharedPreferences prefs = null;

    Activity a;
    View v;
    Gson gson = new Gson();

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        v = getView();
        setSchemeInfo();
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
        prefs = a.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
    }

    private void setSchemeInfo() {
        Preset currentPreset = getCurrentPreset();

        if (isAboutVisible) {
            ab.setNav(1, null, a, v);
        } else {
            ab.setNav(3, null, a, v);
        }
        ab.setColor(R.color.colorAccent, a, v);
        ab.setTitle(R.string.settings, a, v);

        w.getTextView(R.id.layout_settings_preset_hint, v).setText(w.getStringId(currentPreset.getMusic().getNameId() + "_full"));

        w.getView(R.id.layout_settings_preset, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.showDialogPreset(a);
                w.setRecentColor(R.string.task_presets, R.color.colorAccent, a);
            }
        });

        w.getView(R.id.layout_settings_color, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intent(a, "activity.ColorActivity", 0);
            }
        });

        w.getView(R.id.layout_settings_custom_touch, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_custom_touch_error, Toast.LENGTH_SHORT).show();
            }
        });

        w.getView(R.id.layout_settings_custom_sound, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_custom_sound_error, Toast.LENGTH_SHORT).show();
            }
        });

        w.getView(R.id.layout_settings_layout, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_layout_error, Toast.LENGTH_SHORT).show();
            }
        });

        w.getView(R.id.layout_settings_tutorial, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.toggleTutorial();
            }
        });

        w.getSwitchCompat(R.id.layout_settings_tutorial_switch, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (w.getView(R.id.progress_bar_layout, v).getVisibility() == View.GONE) {
                    main.toggleTutorial();
                } else {
                    // still loading preset
                    Toast.makeText(a, R.string.tutorial_loading, Toast.LENGTH_LONG).show();
                    w.getSwitchCompat(R.id.layout_settings_tutorial_switch, v).toggle();
                }
            }
        });

        w.getView(R.id.layout_settings_about_tapad, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentWithExtra(a, "activity.AboutActivity", "about", "tapad", 0);
            }
        });

        w.getView(R.id.layout_settings_about_dev, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentWithExtra(a, "activity.AboutActivity", "about", "dev", 0);
            }
        });
    }

    private Preset getCurrentPreset() {
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
