package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.bedrock.padder.R;
import com.bedrock.padder.activity.MainActivity;

public class SoundService {

    int sp_id_1_00;
    int sp_id_2_00;
    int sp_id_3_00;
    int sp_id_4_00;
    int sp_id_1_01;
    int sp_id_2_01;
    int sp_id_3_01;
    int sp_id_4_01;
    int sp_id_1_02;
    int sp_id_2_02;
    int sp_id_3_02;
    int sp_id_4_02;
    int sp_id_1_03;
    int sp_id_2_03;
    int sp_id_3_03;
    int sp_id_4_03;
    int sp_id_1_04;
    int sp_id_2_04;
    int sp_id_3_04;
    int sp_id_4_04;
    int sp_id_1_11;
    int sp_id_2_11;
    int sp_id_3_11;
    int sp_id_4_11;
    int sp_id_1_12;
    int sp_id_2_12;
    int sp_id_3_12;
    int sp_id_4_12;
    int sp_id_1_13;
    int sp_id_2_13;
    int sp_id_3_13;
    int sp_id_4_13;
    int sp_id_1_14;
    int sp_id_2_14;
    int sp_id_3_14;
    int sp_id_4_14;
    int sp_id_1_21;
    int sp_id_2_21;
    int sp_id_3_21;
    int sp_id_4_21;
    int sp_id_1_22;
    int sp_id_2_22;
    int sp_id_3_22;
    int sp_id_4_22;
    int sp_id_1_23;
    int sp_id_2_23;
    int sp_id_3_23;
    int sp_id_4_23;
    int sp_id_1_24;
    int sp_id_2_24;
    int sp_id_3_24;
    int sp_id_4_24;
    int sp_id_1_31;
    int sp_id_2_31;
    int sp_id_3_31;
    int sp_id_4_31;
    int sp_id_1_32;
    int sp_id_2_32;
    int sp_id_3_32;
    int sp_id_4_32;
    int sp_id_1_33;
    int sp_id_2_33;
    int sp_id_3_33;
    int sp_id_4_33;
    int sp_id_1_34;
    int sp_id_2_34;
    int sp_id_3_34;
    int sp_id_4_34;
    int sp_id_1_41;
    int sp_id_2_41;
    int sp_id_3_41;
    int sp_id_4_41;
    int sp_id_1_42;
    int sp_id_2_42;
    int sp_id_3_42;
    int sp_id_4_42;
    int sp_id_1_43;
    int sp_id_2_43;
    int sp_id_3_43;
    int sp_id_4_43;
    int sp_id_1_44;
    int sp_id_2_44;
    int sp_id_3_44;
    int sp_id_4_44;

    private ThemeService theme = new ThemeService();
    private AnimService anim = new AnimService();
    private WindowService window = new WindowService();

    public SoundPool sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

    //Play SoundPool
    public void soundPlay(SoundPool sp, int soundid) {
        sp.play(soundid, 1, 1, 1, 0, 1f);
    }

    //Stop SoundPool
    public void soundStop(SoundPool sp, int soundid) {
        sp.stop(soundid);
    }

    public void release() {
        try {
            sp.release();
        } catch (NullPointerException e) {
            Log.e("SoundService", "NPE, soundpool is null. Cannot release soundpool.");
        }
    }

    //Stop All SoundPool
    public void soundAllStop() {
        try {
            sp.autoPause();
        } catch (NullPointerException e) {
            Log.e("SoundService", "NPE, soundpool is null. Cannot autoPause soundpool.");
        }
    }

    boolean isPresetLoaded = false;

    public int soundPoolId[] = {
            sp_id_1_00, sp_id_1_01, sp_id_1_02, sp_id_1_03, sp_id_1_04,
            sp_id_1_11, sp_id_1_12, sp_id_1_13, sp_id_1_14,
            sp_id_1_21, sp_id_1_22, sp_id_1_23, sp_id_1_24,
            sp_id_1_31, sp_id_1_32, sp_id_1_33, sp_id_1_34,
            sp_id_1_41, sp_id_1_42, sp_id_1_43, sp_id_1_44,

            sp_id_2_00, sp_id_2_01, sp_id_2_02, sp_id_2_03, sp_id_2_04,
            sp_id_2_11, sp_id_2_12, sp_id_2_13, sp_id_2_14,
            sp_id_2_21, sp_id_2_22, sp_id_2_23, sp_id_2_24,
            sp_id_2_31, sp_id_2_32, sp_id_2_33, sp_id_2_34,
            sp_id_2_41, sp_id_2_42, sp_id_2_43, sp_id_2_44,

            sp_id_3_00, sp_id_3_01, sp_id_3_02, sp_id_3_03, sp_id_3_04,
            sp_id_3_11, sp_id_3_12, sp_id_3_13, sp_id_3_14,
            sp_id_3_21, sp_id_3_22, sp_id_3_23, sp_id_3_24,
            sp_id_3_31, sp_id_3_32, sp_id_3_33, sp_id_3_34,
            sp_id_3_41, sp_id_3_42, sp_id_3_43, sp_id_3_44,

            sp_id_4_00, sp_id_4_01, sp_id_4_02, sp_id_4_03, sp_id_4_04,
            sp_id_4_11, sp_id_4_12, sp_id_4_13, sp_id_4_14,
            sp_id_4_21, sp_id_4_22, sp_id_4_23, sp_id_4_24,
            sp_id_4_31, sp_id_4_32, sp_id_4_33, sp_id_4_34,
            sp_id_4_41, sp_id_4_42, sp_id_4_43, sp_id_4_44};
    public int toggle;

    //  R.raw.h1_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
    //  R.raw.h1_11, R.raw.h1_12, R.raw.h1_13, R.raw.h1_14,
    //  R.raw.h1_21, R.raw.h1_22, R.raw.h1_23, R.raw.h1_24,
    //  R.raw.h1_31, R.raw.h1_32, R.raw.h1_33, R.raw.h1_34,
    //  R.raw.h1_41, R.raw.h1_42, R.raw.h1_43, R.raw.h1_44,
    //
    //  R.raw.h2_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
    //  R.raw.h2_11, R.raw.h2_12, R.raw.h2_13, R.raw.h2_14,
    //  R.raw.h2_21, R.raw.h2_22, R.raw.h2_23, R.raw.h2_24,
    //  R.raw.h2_31, R.raw.h2_32, R.raw.h2_33, R.raw.h2_34,
    //  R.raw.h2_41, R.raw.h2_42, R.raw.h2_43, R.raw.h2_44,
    //
    //  R.raw.h3_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
    //  R.raw.h3_11, R.raw.h3_12, R.raw.h3_13, R.raw.h3_14,
    //  R.raw.h3_21, R.raw.h3_22, R.raw.h3_23, R.raw.h3_24,
    //  R.raw.h3_31, R.raw.h3_32, R.raw.h3_33, R.raw.h3_34,
    //  R.raw.h3_41, R.raw.h3_42, R.raw.h3_43, R.raw.h3_44,
    //
    //  R.raw.h4_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
    //  R.raw.h4_11, R.raw.h4_12, R.raw.h4_13, R.raw.h4_14,
    //  R.raw.h4_21, R.raw.h4_22, R.raw.h4_23, R.raw.h4_24,
    //  R.raw.h4_31, R.raw.h4_32, R.raw.h4_33, R.raw.h4_34,
    //  R.raw.h4_41, R.raw.h4_42, R.raw.h4_43, R.raw.h4_44,

    int raw[][] = {
        {
            // Hello
            R.raw.a0_00, R.raw.h3_01, R.raw.a0_00, R.raw.h2_03, R.raw.h2_04,
            R.raw.h1_11, R.raw.h1_12, R.raw.h1_13, R.raw.h1_14,
            R.raw.h1_21, R.raw.h1_22, R.raw.h1_23, R.raw.h1_24,
            R.raw.h1_31, R.raw.h1_32, R.raw.h1_33, R.raw.h1_34,
            R.raw.h1_41, R.raw.h1_42, R.raw.h1_43, R.raw.h1_44,

            R.raw.h2_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.h2_11, R.raw.h2_12, R.raw.h2_13, R.raw.h2_14,
            R.raw.h2_21, R.raw.h2_22, R.raw.h2_23, R.raw.h2_24,
            R.raw.h2_31, R.raw.h2_32, R.raw.h2_33, R.raw.h2_34,
            R.raw.h2_41, R.raw.h2_42, R.raw.h2_43, R.raw.h2_44,

            R.raw.h3_01, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.h3_11, R.raw.h3_12, R.raw.h3_13, R.raw.h3_14,
            R.raw.h3_21, R.raw.h3_22, R.raw.h3_23, R.raw.h3_24,
            R.raw.h3_31, R.raw.h3_32, R.raw.h3_33, R.raw.h3_34,
            R.raw.h3_41, R.raw.h3_42, R.raw.h3_43, R.raw.h3_44,

            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.h4_11, R.raw.a0_00, R.raw.a0_00, R.raw.h4_14,
            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.h4_41, R.raw.a0_00, R.raw.a0_00, R.raw.h4_44,
        }, {
            // Roses
            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.r1_11, R.raw.r1_12, R.raw.r1_13, R.raw.r1_14,
            R.raw.r1_21, R.raw.r1_22, R.raw.r1_23, R.raw.r1_24,
            R.raw.r1_31, R.raw.r1_32, R.raw.r1_33, R.raw.r1_34,
            R.raw.r1_41, R.raw.r1_42, R.raw.r1_43, R.raw.r1_44,

            R.raw.r2_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.r2_11, R.raw.r2_12, R.raw.r2_13, R.raw.r2_14,
            R.raw.r2_21, R.raw.r2_22, R.raw.r2_23, R.raw.r2_24,
            R.raw.r2_31, R.raw.r2_32, R.raw.r2_33, R.raw.r2_34,
            R.raw.r2_41, R.raw.r2_42, R.raw.r2_43, R.raw.r2_44,

            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.r2_11, R.raw.r3_12, R.raw.r3_13, R.raw.r3_14,
            R.raw.r3_21, R.raw.r3_22, R.raw.r3_23, R.raw.r3_24,
            R.raw.r3_31, R.raw.r3_32, R.raw.r3_33, R.raw.r3_34,
            R.raw.r2_41, R.raw.r3_42, R.raw.r2_43, R.raw.r3_44,

            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.r2_11, R.raw.r4_12, R.raw.r4_13, R.raw.r4_14,
            R.raw.r4_21, R.raw.r4_22, R.raw.r4_23, R.raw.r4_24,
            R.raw.r4_31, R.raw.r4_32, R.raw.r4_33, R.raw.r4_34,
            R.raw.r2_41, R.raw.r4_42, R.raw.r2_43, R.raw.r3_44,
        }, {
            // Faded
            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.r1_11, R.raw.r1_12, R.raw.r1_13, R.raw.r1_14,
            R.raw.r1_21, R.raw.r1_22, R.raw.r1_23, R.raw.r1_24,
            R.raw.r1_31, R.raw.r1_32, R.raw.r1_33, R.raw.r1_34,
            R.raw.r1_41, R.raw.r1_42, R.raw.r1_43, R.raw.r1_44,

            R.raw.r2_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.r2_11, R.raw.r2_12, R.raw.r2_13, R.raw.r2_14,
            R.raw.r2_21, R.raw.r2_22, R.raw.r2_23, R.raw.r2_24,
            R.raw.r2_31, R.raw.r2_32, R.raw.r2_33, R.raw.r2_34,
            R.raw.r2_41, R.raw.r2_42, R.raw.r2_43, R.raw.r2_44,

            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.r2_11, R.raw.r3_12, R.raw.r3_13, R.raw.r3_14,
            R.raw.r3_21, R.raw.r3_22, R.raw.r3_23, R.raw.r3_24,
            R.raw.r3_31, R.raw.r3_32, R.raw.r3_33, R.raw.r3_34,
            R.raw.r2_41, R.raw.r3_42, R.raw.r2_43, R.raw.r3_44,

            R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
            R.raw.r2_11, R.raw.r4_12, R.raw.r4_13, R.raw.r4_14,
            R.raw.r4_21, R.raw.r4_22, R.raw.r4_23, R.raw.r4_24,
            R.raw.r4_31, R.raw.r4_32, R.raw.r4_33, R.raw.r4_34,
            R.raw.r2_41, R.raw.r4_42, R.raw.r2_43, R.raw.r3_44,
        }
    };

    Activity activity;

    class LoadSound extends AsyncTask<Void, Integer, String> {
        String TAG = "LoadSound";
        SharedPreferences prefs;

        protected void onPreExecute() {
            Log.d(TAG, "On preExceute, unloadSchemeSound");
            unloadSchemeSound(activity);

            window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.icon_tutorial_disabled);
            //window.getImageView(R.id.toolbar_tutorial_icon, activity).setClickable(true);
            //window.getImageView(R.id.toolbar_tutorial_icon, activity).setFocusable(true);
            window.getImageView(R.id.layout_settings_tutorial_icon, activity).setImageResource(R.drawable.settings_tutorial_disabled);
            //window.getImageView(R.id.layout_settings_tutorial_icon, activity).setClickable(true);
            //window.getImageView(R.id.layout_settings_tutorial_icon, activity).setFocusable(true);

            prefs = activity.getSharedPreferences("com.bedrock.padder", activity.MODE_PRIVATE);
        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground, start loading sounds");
            int scheme = prefs.getInt("scheme", 1);

            if (scheme >= 1 && scheme <= 3) {
                Log.i("SoundService", "Preset found");
                for (int i = 0; i < 84; i++) {
                    soundPoolId[i] = sp.load(activity, raw[scheme - 1][i], 1);
                    publishProgress(i + 1);
                }
            } else {
                Log.e("SoundService", "Error during initializing, scheme in SharedPrefs has a wrong value");
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            Log.d(TAG, "Sound loaded " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Finished loading sound");
            window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset_done);
            isPresetLoaded = true;

            window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.icon_tutorial);
            //window.getImageView(R.id.toolbar_tutorial_icon, activity).setClickable(false);
            //window.getImageView(R.id.toolbar_tutorial_icon, activity).setFocusable(false);
            window.getImageView(R.id.layout_settings_tutorial_icon, activity).setImageResource(R.drawable.settings_tutorial);
            //window.getImageView(R.id.layout_settings_tutorial_icon, activity).setClickable(false);
            //window.getImageView(R.id.layout_settings_tutorial_icon, activity).setFocusable(false);

            anim.fadeOut(R.id.progress_bar_layout, 400, 400, activity);
            MainActivity main = new MainActivity();
            main.setQuickstart(activity);

            Handler setText = new Handler();
            setText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset);
                }
            }, 800);
        }
    }

    class UnloadSound extends AsyncTask<Void, Integer, String> {
        String TAG = "UnloadSound";
        SharedPreferences prefs;

        protected void onPreExecute() {
            Log.d(TAG, "On preExceute, set prefs");
            prefs = activity.getSharedPreferences("com.bedrock.padder", activity.MODE_PRIVATE);
        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground, start unloading sounds");
            try {
                int scheme = prefs.getInt("scheme", 1);
                if (scheme >= 1 && scheme <= 3) {
                    Log.i("SoundService", "Preset found");
                    for (int i = 0; i < 84; i++) {
                        sp.unload(raw[scheme - 1][i]);
                        publishProgress(i + 1);
                    }
                } else {
                    Log.e("SoundService", "Error during initializing, scheme in SharedPrefs has a wrong value");
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "NPE, Can't find soundpool");
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            Log.d(TAG, "Sound unloaded " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Finished unloading sound");
            isPresetLoaded = false;
        }
    }

    public void loadSchemeSound(Activity a) {
        activity = a;
        new LoadSound().execute();
    }

    public void unloadSchemeSound(Activity a) {
        activity = a;
        new UnloadSound().execute();
    }

    //    boolean tgl1 = false;
    //    boolean tgl2 = false;
    //    boolean tgl3 = false;
    //    boolean tgl4 = false;
    //    boolean tgl5 = false;
    //    boolean tgl6 = false;
    //    boolean tgl7 = false;
    //    boolean tgl8 = false;
    //
    //    boolean toggleButton[] = {
    //            tgl1,
    //            tgl2,
    //            tgl3,
    //            tgl4,
    //            tgl5,
    //            tgl6,
    //            tgl7,
    //            tgl8 };
    //
    //    int toggleButtonId[] = {
    //            R.id.tgl1,
    //            R.id.tgl2,
    //            R.id.tgl3,
    //            R.id.tgl4,
    //            R.id.tgl5,
    //            R.id.tgl6,
    //            R.id.tgl7,
    //            R.id.tgl8 };


    //    public void setToggleButton(final int color_id, final Activity a) {
    //        for(final int[] i = {1}; i[0] < 5; i[0]++){
    //            w.setOnTouch(R.id.tgl1, new Runnable() {
    //                @Override
    //                public void run() {
    //                    setButtonToggle(i[0], R.color.green, a);
    //                    if (toggleButton[i[0] - 1] == false) {
    //                        for(int j = 0; j < 4; j++){
    //                            if(j == (i[0] - 1)){
    //                                w.setViewBackgroundColor(toggleButtonId[j], color_id, a);
    //                            } else {
    //                                w.setViewBackgroundColor(toggleButtonId[j], R.color.grey, a);
    //                            }
    //                        }
    //                    } else {
    //                        w.setViewBackgroundColor(toggleButtonId[i[0] - 1], R.color.grey, a);
    //                        setButton(R.color.grey_dark, a);
    //                        soundAllStop();
    //                    }
    //                }
    //            }, new Runnable() {
    //                @Override
    //                public void run() {
    //                    for(int i = 0; i < 4; i++){
    //                        if(toggleButton[i] == false){
    //                            for(int j = 0; j < 4; j++){
    //                                if(j == i){
    //                                    toggleButton[j] = false;
    //                                } else {
    //                                    toggleButton[j] = true;
    //                                }
    //                            }
    //                        } else {
    //                            toggleButton[i] = false;
    //                        }
    //                    }
    //                }
    //            }, a);
    //        }
    //    }

    public void playToggleButtonSound(int id) {
        sp.play(soundPoolId[id], 1, 1, 1, 0, 1f);
    }

    int buttonId[] = {
            R.id.btn00,
            R.id.tgl1,
            R.id.tgl2,
            R.id.tgl3,
            R.id.tgl4,
            R.id.btn11,
            R.id.btn12,
            R.id.btn13,
            R.id.btn14,
            R.id.btn21,
            R.id.btn22,
            R.id.btn23,
            R.id.btn24,
            R.id.btn31,
            R.id.btn32,
            R.id.btn33,
            R.id.btn34,
            R.id.btn41,
            R.id.btn42,
            R.id.btn43,
            R.id.btn44};

    public void setButtonToggle(int id, int colorId, Activity activity) {
        toggle = id;
        if (isPresetLoaded == true) {
            for (int i = 0; i < 21; i++) {
                if (i >= 1 && i <= 4) {
                    continue;
                } else {
                    //window.setOnTouchSound(buttonId[i], colorId, R.color.grey, soundPool[i + ((id - 1) * 21)], soundPoolId[i + ((id - 1) * 21)], activity);
                    if (id >= 1) {
                        window.setOnTouchSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[i + ((id - 1) * 21)], activity);
                    }
                }
            }
            Log.i("SoundService", "ToggleButton sound set id " + String.valueOf(id));
//            if (id == 1) {
//            } else if(id == 2) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_2_00, sp_id_2_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_2_11, sp_id_2_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_2_12, sp_id_2_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_2_13, sp_id_2_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_2_14, sp_id_2_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_2_21, sp_id_2_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_2_22, sp_id_2_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_2_23, sp_id_2_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_2_24, sp_id_2_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_2_31, sp_id_2_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_2_32, sp_id_2_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_2_33, sp_id_2_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_2_34, sp_id_2_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_2_41, sp_id_2_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_2_42, sp_id_2_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_2_43, sp_id_2_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_2_44, sp_id_2_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 2");
//            } else if(id == 3) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_3_00, sp_id_3_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_3_11, sp_id_3_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_3_12, sp_id_3_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_3_13, sp_id_3_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_3_14, sp_id_3_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_3_21, sp_id_3_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_3_22, sp_id_3_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_3_23, sp_id_3_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_3_24, sp_id_3_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_3_31, sp_id_3_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_3_32, sp_id_3_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_3_33, sp_id_3_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_3_34, sp_id_3_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_3_41, sp_id_3_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_3_42, sp_id_3_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_3_43, sp_id_3_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_3_44, sp_id_3_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 3");
//            } else if(id == 4) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_4_00, sp_id_4_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_4_11, sp_id_4_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_4_12, sp_id_4_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_4_13, sp_id_4_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_4_14, sp_id_4_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_4_21, sp_id_4_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_4_22, sp_id_4_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_4_23, sp_id_4_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_4_24, sp_id_4_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_4_31, sp_id_4_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_4_32, sp_id_4_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_4_33, sp_id_4_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_4_34, sp_id_4_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_4_41, sp_id_4_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_4_42, sp_id_4_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_4_43, sp_id_4_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_4_44, sp_id_4_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 4");
//            } else {
//                Log.i("SoundService", "ToggleButton undefined value");
//            }
        } else {
            setButton(colorId, activity);
        }
    }

    public void setButtonTogglePattern(int id, int colorId, int patternId, Activity activity) {

        int pattern1[][] = {
                {R.id.btn12, R.id.btn21},
                {R.id.btn11, R.id.btn22, R.id.btn13},
                {R.id.btn12, R.id.btn23, R.id.btn14},
                {R.id.btn13, R.id.btn24},
                {R.id.btn11, R.id.btn22, R.id.btn31},
                {R.id.btn12, R.id.btn21, R.id.btn32, R.id.btn23},
                {R.id.btn13, R.id.btn22, R.id.btn33, R.id.btn24},
                {R.id.btn14, R.id.btn23, R.id.btn34},
                {R.id.btn21, R.id.btn32, R.id.btn41},
                {R.id.btn31, R.id.btn33, R.id.btn22, R.id.btn42},
                {R.id.btn32, R.id.btn34, R.id.btn23, R.id.btn43},
                {R.id.btn33, R.id.btn24, R.id.btn44},
                {R.id.btn31, R.id.btn42},
                {R.id.btn41, R.id.btn32, R.id.btn43},
                {R.id.btn42, R.id.btn33, R.id.btn44},
                {R.id.btn34, R.id.btn43}
        };

        int pattern2[][] = {
                {R.id.btn12, R.id.btn13, R.id.btn14},
                {R.id.btn11, R.id.btn13, R.id.btn14},
                {R.id.btn11, R.id.btn12, R.id.btn14},
                {R.id.btn11, R.id.btn12, R.id.btn13},
                {R.id.btn22, R.id.btn23, R.id.btn24},
                {R.id.btn21, R.id.btn23, R.id.btn24},
                {R.id.btn21, R.id.btn22, R.id.btn24},
                {R.id.btn21, R.id.btn22, R.id.btn23},
                {R.id.btn32, R.id.btn33, R.id.btn34},
                {R.id.btn31, R.id.btn33, R.id.btn34},
                {R.id.btn31, R.id.btn32, R.id.btn34},
                {R.id.btn31, R.id.btn32, R.id.btn33},
                {R.id.btn42, R.id.btn43, R.id.btn44},
                {R.id.btn41, R.id.btn43, R.id.btn44},
                {R.id.btn41, R.id.btn42, R.id.btn44},
                {R.id.btn41, R.id.btn42, R.id.btn43}
        };

        int pattern3[][] = {
                {R.id.btn21, R.id.btn31, R.id.btn41},
                {R.id.btn22, R.id.btn32, R.id.btn42},
                {R.id.btn23, R.id.btn33, R.id.btn43},
                {R.id.btn24, R.id.btn34, R.id.btn44},
                {R.id.btn11, R.id.btn31, R.id.btn41},
                {R.id.btn12, R.id.btn32, R.id.btn42},
                {R.id.btn13, R.id.btn33, R.id.btn43},
                {R.id.btn14, R.id.btn34, R.id.btn44},
                {R.id.btn11, R.id.btn21, R.id.btn41},
                {R.id.btn12, R.id.btn22, R.id.btn42},
                {R.id.btn13, R.id.btn23, R.id.btn43},
                {R.id.btn14, R.id.btn24, R.id.btn44},
                {R.id.btn11, R.id.btn21, R.id.btn31},
                {R.id.btn12, R.id.btn22, R.id.btn32},
                {R.id.btn13, R.id.btn23, R.id.btn33},
                {R.id.btn14, R.id.btn24, R.id.btn34}
        };

        int pattern4[][] = {
                {R.id.btn12, R.id.btn13, R.id.btn14, R.id.btn21, R.id.btn31, R.id.btn41},
                {R.id.btn11, R.id.btn13, R.id.btn14, R.id.btn22, R.id.btn32, R.id.btn42},
                {R.id.btn11, R.id.btn12, R.id.btn14, R.id.btn23, R.id.btn33, R.id.btn43},
                {R.id.btn11, R.id.btn12, R.id.btn13, R.id.btn24, R.id.btn34, R.id.btn44},
                {R.id.btn22, R.id.btn23, R.id.btn24, R.id.btn11, R.id.btn31, R.id.btn41},
                {R.id.btn21, R.id.btn23, R.id.btn24, R.id.btn12, R.id.btn32, R.id.btn42},
                {R.id.btn21, R.id.btn22, R.id.btn24, R.id.btn13, R.id.btn33, R.id.btn43},
                {R.id.btn21, R.id.btn22, R.id.btn23, R.id.btn14, R.id.btn34, R.id.btn44},
                {R.id.btn32, R.id.btn33, R.id.btn34, R.id.btn11, R.id.btn21, R.id.btn41},
                {R.id.btn31, R.id.btn33, R.id.btn34, R.id.btn12, R.id.btn22, R.id.btn42},
                {R.id.btn31, R.id.btn32, R.id.btn34, R.id.btn13, R.id.btn23, R.id.btn43},
                {R.id.btn31, R.id.btn32, R.id.btn33, R.id.btn14, R.id.btn24, R.id.btn44},
                {R.id.btn42, R.id.btn43, R.id.btn44, R.id.btn11, R.id.btn21, R.id.btn31},
                {R.id.btn41, R.id.btn43, R.id.btn44, R.id.btn12, R.id.btn22, R.id.btn32},
                {R.id.btn41, R.id.btn42, R.id.btn44, R.id.btn13, R.id.btn23, R.id.btn33},
                {R.id.btn41, R.id.btn42, R.id.btn43, R.id.btn14, R.id.btn24, R.id.btn34}
        };

        int pattern[][] = {};

        switch (patternId) {
            case 1:
                pattern = pattern1;
                break;
            case 2:
                pattern = pattern2;
                break;
            case 3:
                pattern = pattern3;
                break;
            case 4:
                pattern = pattern4;
                break;
        }

        if (isPresetLoaded == true) {
            for (int i = 0; i < 21; i++) {
                if (i >= 1 && i <= 4) {
                    continue;
                } else {
                    if (id >= 1) {
                        window.setOnTouchSoundPattern(buttonId[i], pattern, colorId, R.color.grey, sp, soundPoolId[i + ((id - 1) * 21)], activity);
                    }
                }
            }
            Log.i("SoundService", "ToggleButton pattern set id " + String.valueOf(patternId));
//            if (id == 1) {
//            } else if(id == 2) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_2_00, sp_id_2_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_2_11, sp_id_2_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_2_12, sp_id_2_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_2_13, sp_id_2_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_2_14, sp_id_2_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_2_21, sp_id_2_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_2_22, sp_id_2_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_2_23, sp_id_2_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_2_24, sp_id_2_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_2_31, sp_id_2_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_2_32, sp_id_2_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_2_33, sp_id_2_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_2_34, sp_id_2_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_2_41, sp_id_2_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_2_42, sp_id_2_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_2_43, sp_id_2_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_2_44, sp_id_2_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 2");
//            } else if(id == 3) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_3_00, sp_id_3_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_3_11, sp_id_3_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_3_12, sp_id_3_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_3_13, sp_id_3_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_3_14, sp_id_3_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_3_21, sp_id_3_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_3_22, sp_id_3_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_3_23, sp_id_3_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_3_24, sp_id_3_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_3_31, sp_id_3_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_3_32, sp_id_3_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_3_33, sp_id_3_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_3_34, sp_id_3_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_3_41, sp_id_3_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_3_42, sp_id_3_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_3_43, sp_id_3_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_3_44, sp_id_3_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 3");
//            } else if(id == 4) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_4_00, sp_id_4_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_4_11, sp_id_4_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_4_12, sp_id_4_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_4_13, sp_id_4_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_4_14, sp_id_4_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_4_21, sp_id_4_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_4_22, sp_id_4_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_4_23, sp_id_4_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_4_24, sp_id_4_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_4_31, sp_id_4_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_4_32, sp_id_4_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_4_33, sp_id_4_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_4_34, sp_id_4_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_4_41, sp_id_4_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_4_42, sp_id_4_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_4_43, sp_id_4_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_4_44, sp_id_4_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 4");
//            } else {
//                Log.i("SoundService", "ToggleButton undefined value");
//            }
        } else {
            setButton(colorId, activity);
        }
    }

    public void setButton(final int colorId, final Activity activity) {
        for (int i = 0; i < buttonId.length - 4; i++) {
            if (i == 0) {
                window.setOnTouchColor(buttonId[i], colorId, R.color.grey, activity);
            } else {
                window.setOnTouchColor(buttonId[i + 4], colorId, R.color.grey, activity);
            }
        }
    }
}
