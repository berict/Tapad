package com.bedrock.padder.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import static com.bedrock.padder.activity.MainActivity.isStopLoopOnSingle;
import static com.bedrock.padder.activity.MainActivity.setSettingVisible;
import static com.bedrock.padder.helper.WindowHelper.getStringFromId;

public class SettingsActivity extends AppCompatActivity {

    private WindowHelper w = new WindowHelper();
    private AnimateHelper anim = new AnimateHelper();
    private IntentHelper intent = new IntentHelper();
    private ToolbarHelper toolbar = new ToolbarHelper();

    private AppCompatActivity a = this;

    private Preferences preferences = null;

    EditText deckMarginEditText = null;
    SeekBar deckMarginSeekbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setSettingVisible(true);

        preferences = new Preferences(a);
        setUi();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        pressBack();
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
        (a.findViewById(R.id.layout_settings_preset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPresetVisible == false) {
                    intent.intent(a, "activity.PresetStoreActivity");
                }
            }
        });

        // colors
        (a.findViewById(R.id.layout_settings_color)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intent(a, "activity.ColorActivity");
            }
        });

        // abouts
        (a.findViewById(R.id.layout_settings_about_tapad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentWithExtra(a, "activity.AboutActivity", "about", "tapad", 0);
            }
        });

        (a.findViewById(R.id.layout_settings_about_dev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.intentWithExtra(a, "activity.AboutActivity", "about", "dev", 0);
            }
        });

        // unused
        (a.findViewById(R.id.layout_settings_custom_touch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_custom_touch_error, Toast.LENGTH_SHORT).show();
            }
        });

        (a.findViewById(R.id.layout_settings_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, R.string.settings_layout_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUi() {
        toolbar.setActionBar(this);
        toolbar.setActionBarDisplayHomeAsUpIcon(R.drawable.ic_close_white);
        toolbar.setStatusBarTint(this);
        toolbar.setActionBarTitle(R.string.settings);
        toolbar.setActionBarColor(R.color.colorAccent, a);
        if (isAboutVisible) {
            // back
            toolbar.setActionBarDisplayHomeAsUp(true);
        } else {
            // close
            toolbar.setActionBarDisplayHomeAsUpIcon(R.drawable.ic_close_white);
        }
        w.setStatusBar(R.color.colorAccentDark, a);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        w.setRecentColor(R.string.settings, R.color.colorAccent, a);

        if (currentPreset != null) {
            ((TextView) a.findViewById(R.id.layout_settings_preset_hint)).setText(currentPreset.getAbout().getTitle());
        } else {
            ((TextView) a.findViewById(R.id.layout_settings_preset_hint)).setText(R.string.settings_preset_hint_no_preset);
        }

        setFocusLoss();
        setStopLoop();
        setStartPage();
        setDeckMargin();
        setIntents();
    }

    private void setFocusLoss() {
        final boolean focusLoss = preferences.getStopOnFocusLoss();
        final SwitchCompat focusLossSwitch = ((SwitchCompat) a.findViewById(R.id.layout_settings_focus_loss_switch));
        RelativeLayout focusLossLayout = ((RelativeLayout) a.findViewById(R.id.layout_settings_focus_loss));

        focusLossSwitch.setChecked(focusLoss);
        focusLossSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b != focusLoss) {
                    // changed
                    isDeckShouldCleared = true;
                }
                preferences.setStopOnFocusLoss(b);
            }
        });

        focusLossLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusLossSwitch.toggle();
            }
        });
    }

    private void setStopLoop() {
        final boolean stopLoop = preferences.getStopLoopOnSingle();
        final SwitchCompat stopLoopSwitch = ((SwitchCompat) a.findViewById(R.id.layout_settings_stop_loop_switch));
        RelativeLayout stopLoopLayout = ((RelativeLayout) a.findViewById(R.id.layout_settings_stop_loop));

        stopLoopSwitch.setChecked(stopLoop);
        stopLoopSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b != stopLoop) {
                    // changed
                    isDeckShouldCleared = true;
                }
                preferences.setStopLoopOnSingle(b);
                isStopLoopOnSingle = b;
            }
        });

        stopLoopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLoopSwitch.toggle();
            }
        });
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

        (a.findViewById(R.id.layout_settings_start_page)).setOnClickListener(new View.OnClickListener() {
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
        ((TextView) a.findViewById(R.id.layout_settings_start_page_hint)).setText(getStringFromId("settings_start_page_" + preferences.getStartPage(), a));
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

        deckMarginEditText = ((EditText) a.findViewById(R.id.layout_settings_deck_margin_input));
        deckMarginSeekbar = ((SeekBar) a.findViewById(R.id.layout_settings_deck_margin_slider));

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
                    try {
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
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // init values
                        setDeckMarginEditText(preferences.getDeckMargin());
                        setDeckMarginSeekbar(preferences.getDeckMargin());
                        // Make error message
                        Toast.makeText(a, getStringFromId(R.string.settings_deck_margin_error_format, a), Toast.LENGTH_LONG).show();
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
                view = (a.findViewById(R.id.layout_settings_deck_margin_input_error_empty));
                break;
            case "bound":
                view = (a.findViewById(R.id.layout_settings_deck_margin_input_error_bound));
                break;
            case "warning":
                view = (a.findViewById(R.id.layout_settings_deck_margin_input_warning));
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
                (a.findViewById(R.id.layout_settings_deck_margin_input_error_empty)),
                (a.findViewById(R.id.layout_settings_deck_margin_input_error_bound)),
                (a.findViewById(R.id.layout_settings_deck_margin_input_warning))
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
}
