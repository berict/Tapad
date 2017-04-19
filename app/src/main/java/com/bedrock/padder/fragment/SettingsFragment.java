package com.bedrock.padder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.activity.MainActivity;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.AppbarService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.SoundService;
import com.bedrock.padder.helper.TutorialService;
import com.bedrock.padder.helper.WindowService;
import com.bedrock.padder.model.preset.Preset;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isAboutVisible;
import static com.bedrock.padder.activity.MainActivity.isDeckShouldCleared;
import static com.bedrock.padder.activity.MainActivity.isPresetVisible;
import static com.bedrock.padder.activity.MainActivity.presets;
import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class SettingsFragment extends Fragment {

    private AppbarService ab = new AppbarService();
    private WindowService w = new WindowService();
    private IntentService intent = new IntentService();
    private AnimService anim = new AnimService();
    private MainActivity main = new MainActivity();
    private TutorialService tut = new TutorialService();
    private SoundService sound = new SoundService();

    SharedPreferences prefs = null;

    private Activity a;
    View v;

    private OnFragmentInteractionListener mListener;
    private MaterialDialog PresetDialog;

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
                        } else {
                            // preset is not changed
                            setScheme(defaultPreset);
                        }
                        setSchemeInfo();
                        isPresetVisible = false;
                    }
                })
                .show();
    }

    public void setSchemeInfo() {
        Preset currentPreset = presets[getScheme()];
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
                if (isPresetVisible == false) {
                    showPresetDialog(a);
                    isPresetVisible = true;
                }
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
