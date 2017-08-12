package com.bedrock.padder.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.SettingsHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isAboutVisible;
import static com.bedrock.padder.activity.MainActivity.isPresetVisible;
import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class SettingsFragment extends Fragment {

    private WindowHelper w = new WindowHelper();
    private IntentHelper intent = new IntentHelper();
    private SettingsHelper settings = new SettingsHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    private SharedPreferences prefs = null;

    private AppCompatActivity a;
    private View v;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        v = getView();
        setPresetInfo();
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
        Log.d("SettingsFragment", "pressBack");
        KeyEvent kDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        a.dispatchKeyEvent(kDown);
        KeyEvent kUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
        a.dispatchKeyEvent(kUp);
    }

    EditText deckMarginValueEditText = null;

    public void setPresetInfo() {
        toolbar.setActionBar(a, v);
        if (isAboutVisible) {
            // back
            toolbar.setActionBarDisplayHomeAsUp(true);
        } else {
            // close
            toolbar.setActionBarDisplayHomeAsUpIcon(R.drawable.ic_close_white);
        }
        toolbar.setActionBarTitle(R.string.settings);
        toolbar.setActionBarPadding(a, v);
        toolbar.setActionBarColor(R.color.colorAccent, a);

        if (currentPreset != null) {
            w.getTextView(R.id.layout_settings_preset_hint, v).setText(currentPreset.getAbout().getTitle());
        } else {
            w.getTextView(R.id.layout_settings_preset_hint, v).setText(R.string.settings_preset_hint_no_preset);
        }

        w.getView(R.id.layout_settings_preset, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPresetVisible == false) {
                    intent.intent(a, "activity.PresetStoreActivity");
                }
            }
        });

        w.getView(R.id.layout_settings_color, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intent(a, "activity.ColorActivity");
            }
        });

        deckMarginValueEditText = w.getEditText(R.id.layout_settings_deck_margin_input, v);

        w.getSeekBar(R.id.layout_settings_deck_margin_slider, v).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String value = String.valueOf(progress / seekBar.getMax());
                Log.d("SettingsFrag", progress + " / " + seekBar.getMax());
                deckMarginValueEditText.setText(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        w.getView(R.id.layout_settings_custom_touch, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_custom_touch_error, Toast.LENGTH_SHORT).show();
            }
        });

        w.getView(R.id.layout_settings_layout, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_layout_error, Toast.LENGTH_SHORT).show();
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

    int getPreset() {
        return prefs.getInt("scheme", 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
