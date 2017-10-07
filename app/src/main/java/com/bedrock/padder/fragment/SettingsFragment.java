package com.bedrock.padder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimateHelper;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;
import com.bedrock.padder.model.preferences.Preferences;

import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isAboutVisible;
import static com.bedrock.padder.activity.MainActivity.isDeckShouldCleared;
import static com.bedrock.padder.activity.MainActivity.isPresetVisible;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class SettingsFragment extends Fragment {

    private WindowHelper w = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private IntentHelper intent = new IntentHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    private AppCompatActivity a;
    private View v;

    private Preferences preferences = null;

    EditText deckMarginEditText = null;
    SeekBar deckMarginSeekbar = null;

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
        setUi();
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
        preferences = new Preferences(a);
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

    private void setIntents() {
        // presets
        w.getView(R.id.layout_settings_preset, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPresetVisible == false) {
                    intent.intent(a, "activity.PresetStoreActivity");
                }
            }
        });

        // colors
        w.getView(R.id.layout_settings_color, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intent(a, "activity.ColorActivity");
            }
        });

        // abouts
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

        // unused
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
    }

    public void setUi() {
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

        setStartPage();
        setDeckMargin();
        setIntents();
    }

    private void setStartPage() {
        updateStartPageText();

        final MaterialDialog startPageDialog = new MaterialDialog.Builder(a)
                .title(R.string.settings_start_page)
                .items(R.array.settings_start_page_items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        preferences.setStartPage(getStartPageValueFromIndex(which));
                        return true;
                    }
                })
                .build();

        w.getView(R.id.layout_settings_start_page, v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPageDialog.setSelectedIndex(getStartPageIndexFromValue(preferences.getStartPage()));
                startPageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        // update the text
                        updateStartPageText();
                    }
                });
                startPageDialog.show();
            }
        });
    }

    public void updateStartPageText() {
        w.getTextView(R.id.layout_settings_start_page_hint, v).setText(getStringFromId("settings_start_page_" + preferences.getStartPage(), a));
    }

    @Nullable
    public String getStartPageValueFromIndex(int index) {
        switch (index) {
            case 0:
                return "recent";
            case 1:
                return "about";
            case 2:
                return "preset_store";
            default:
                return null;
        }
    }

    public int getStartPageIndexFromValue(String value) {
        switch (value) {
            case "recent":
                return 0;
            case "about":
                return 1;
            case "preset_store":
                return 2;
            default:
                return -1;
        }
    }

    private void setDeckMargin() {
        // deck margin

        deckMarginEditText = w.getEditText(R.id.layout_settings_deck_margin_input, v);
        deckMarginSeekbar = w.getSeekBar(R.id.layout_settings_deck_margin_slider, v);

        setDeckMarginEditText(preferences.getDeckMargin());
        setDeckMarginSeekbar(preferences.getDeckMargin());

        final float originalDeckMargin = (float) deckMarginSeekbar.getProgress() / deckMarginSeekbar.getMax();

        deckMarginSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = (float) progress / seekBar.getMax();
                String valueString = String.format("%2.02f", value);
                deckMarginEditText.setText(valueString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float value = (float) seekBar.getProgress() / seekBar.getMax();
                if (originalDeckMargin != value) {
                    // changed
                    preferences.setDeckMargin(value);
                    isDeckShouldCleared = true;
                }
                if (value >= 0 && value < 0.3f) {
                    // warning
                    showDeckMarginEditTextError("warning");
                } else {
                    hideDeckMarginEditTextError();
                }
            }
        });

        deckMarginEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (textView.getText() != null && textView.getText().length() > 0) {
                    float value = Float.valueOf(textView.getText().toString());
                    Log.i("Settings", String.valueOf(value));
                    if (value >= 0 && value <= 1) {
                        // in the safezone
                        hideDeckMarginEditTextError();
                        setDeckMarginSeekbar(value);
                        if (originalDeckMargin != value) {
                            // changed
                            preferences.setDeckMargin(value);
                            isDeckShouldCleared = true;
                        }
                        if (value < 0.3f) {
                            // warning
                            showDeckMarginEditTextError("warning");
                        } else {
                            hideDeckMarginEditTextError();
                        }
                    } else {
                        // bound error
                        showDeckMarginEditTextError("bound");
                    }
                } else {
                    // empty
                    showDeckMarginEditTextError("empty");
                }
                deckMarginEditText.clearFocus();
                hideSoftKeyboard(a);
                return false;
            }
        });
    }

    private void hideSoftKeyboard(Activity activity) {
        if (activity == null) return;
        if (activity.getCurrentFocus() == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void setDeckMarginSeekbar(float value) {
        if (value >= 0 && value <= 1) {
            deckMarginSeekbar.setProgress((int) (100 * value));
            if (value < 0.3f) {
                // warning
                showDeckMarginEditTextError("warning");
            }
        }
    }

    private void setDeckMarginEditText(float value) {
        if (value >= 0 && value <= 1) {
            deckMarginEditText.setText(String.format("%2.02f", value));
        }
    }

    private void showDeckMarginEditTextError(String errorType) {
        Log.i("Settings", "showDeckMarginEditTextError(" + errorType + ")");
        View view;

        switch (errorType) {
            case "empty":
                view = w.getView(R.id.layout_settings_deck_margin_input_error_empty, v);
                break;
            case "bound":
                view = w.getView(R.id.layout_settings_deck_margin_input_error_bound, v);
                break;
            case "warning":
                view = w.getView(R.id.layout_settings_deck_margin_input_warning, v);
                break;
            default:
                view = null;
                break;
        }

        if (view != null) {
            anim.fadeIn(view, hideDeckMarginEditTextError() * 200, 200, "error", a);
        }
    }

    private int hideDeckMarginEditTextError() {
        View views[] = {
                w.getView(R.id.layout_settings_deck_margin_input_error_empty, v),
                w.getView(R.id.layout_settings_deck_margin_input_error_bound, v),
                w.getView(R.id.layout_settings_deck_margin_input_warning, v)
        };

        int isAnimated = 0;

        for (View view : views) {
            if (view.getVisibility() == View.VISIBLE) {
                anim.fadeOut(view, 0, 200, a);
                isAnimated = 1;
            }
        }

        return isAnimated;
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
