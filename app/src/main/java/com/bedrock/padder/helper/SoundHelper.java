package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bedrock.padder.R;
import com.bedrock.padder.model.Deck;
import com.bedrock.padder.model.GesturePad;
import com.bedrock.padder.model.Pad;
import com.bedrock.padder.model.Sound;
import com.bedrock.padder.model.preset.Preset;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isPresetLoading;
import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class SoundHelper {
    private SoundPool sp = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
    private int toggle;

    private int soundPoolId[][][] = new int[4][21][5];

    private Deck decks[] = {
            new Deck(new Pad[21]),
            new Deck(new Pad[21]),
            new Deck(new Pad[21]),
            new Deck(new Pad[21])
    };

    private Activity activity;
    private static Preset previousPreset = null;

    private int buttonId[] = {
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
            R.id.btn44
    };

    public SoundPool getSoundPool() {
        return sp;
    }

    private AdmobHelper ad = new AdmobHelper();
    private AnimateHelper anim = new AnimateHelper();
    private WindowHelper window = new WindowHelper();

    private AsyncTask unLoadSound = null;
    private AsyncTask loadSound = null;

    private AsyncTask unload = null;
    private AsyncTask load = null;

    public void load(Preset preset, int color, int colorDef, Activity activity) {
        // set the previous preset
        this.color = color;
        this.colorDef = colorDef;
        previousPreset = currentPreset;
        currentPreset = preset;
        this.activity = activity;
        unload = new Unload().execute();
    }

    public void cancelLoad() {
        try {
            unload.cancel(true);
            load.cancel(true);
            Log.d("TAG", "Loading canceled");
        } catch (NullPointerException e) {
            Log.d("NPE", "AsyncTask is null");
        }
    }

    public void pauseSounds() {

    }

    @Deprecated
    public void loadSound(Preset preset, Activity activity) {
        // set the previous preset
        previousPreset = currentPreset;
        currentPreset = preset;
        this.activity = activity;
        unLoadSound = new UnloadSound().execute();
    }

    @Deprecated
    public void cancelLoading() {
        try {
            unLoadSound.cancel(true);
            loadSound.cancel(true);
            Log.d("TAG", "AsyncTask canceled");
        } catch (NullPointerException e) {
            Log.d("NPE", "AsyncTask is null");
        }
    }

    public void playToggleButtonSound(int id) {
        sp.play(soundPoolId[id - 1][id][0], 1, 1, 1, 0, 1f);
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

    public void setButtonToggle(int id, int colorId, Activity activity) {
        toggle = id;
        if (isPresetLoading == false) {
            for (int i = 0; i < 21; i++) {
                if (i == 0 || i > 4) {
                    if (id >= 1 && id <= 4) {
                        if (currentPreset.getMusic().getGesture()) {
                            window.setOnGestureSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], activity);
                        } else {
                            window.setOnTouchSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], activity);
                        }
                    }
                }
            }
            Log.i("SoundHelper", "ToggleButton sound set id " + String.valueOf(id));
        } else {
            setButton(colorId, activity);
        }
    }

    public void setButtonToggle(int id, int colorId, int patternId, Activity activity) {

        int pattern1[][][] = {
                {{}, {R.id.btn12, R.id.btn21}},
                {{}, {R.id.btn11, R.id.btn22, R.id.btn13}},
                {{}, {R.id.btn12, R.id.btn23, R.id.btn14}},
                {{}, {R.id.btn13, R.id.btn24}},
                {{}, {R.id.btn11, R.id.btn22, R.id.btn31}},
                {{}, {R.id.btn12, R.id.btn21, R.id.btn32, R.id.btn23}},
                {{}, {R.id.btn13, R.id.btn22, R.id.btn33, R.id.btn24}},
                {{}, {R.id.btn14, R.id.btn23, R.id.btn34}},
                {{}, {R.id.btn21, R.id.btn32, R.id.btn41}},
                {{}, {R.id.btn31, R.id.btn33, R.id.btn22, R.id.btn42}},
                {{}, {R.id.btn32, R.id.btn34, R.id.btn23, R.id.btn43}},
                {{}, {R.id.btn33, R.id.btn24, R.id.btn44}},
                {{}, {R.id.btn31, R.id.btn42}},
                {{}, {R.id.btn41, R.id.btn32, R.id.btn43}},
                {{}, {R.id.btn42, R.id.btn33, R.id.btn44}},
                {{}, {R.id.btn34, R.id.btn43}}
        };

        int pattern2[][][] = {
                {{R.id.btn12}, {R.id.btn13}, {R.id.btn14}},
                {{R.id.btn11, R.id.btn13}, {R.id.btn14}},
                {{R.id.btn12, R.id.btn14}, {R.id.btn11}},
                {{R.id.btn13}, {R.id.btn12}, {R.id.btn11}},
                {{R.id.btn22}, {R.id.btn23}, {R.id.btn24}},
                {{R.id.btn21, R.id.btn23}, {R.id.btn24}},
                {{R.id.btn24, R.id.btn22}, {R.id.btn21}},
                {{R.id.btn23}, {R.id.btn22}, {R.id.btn21}},
                {{R.id.btn32}, {R.id.btn33}, {R.id.btn34}},
                {{R.id.btn31, R.id.btn33}, {R.id.btn34}},
                {{R.id.btn34, R.id.btn32}, {R.id.btn31}},
                {{R.id.btn33}, {R.id.btn32}, {R.id.btn31}},
                {{R.id.btn42}, {R.id.btn43}, {R.id.btn44}},
                {{R.id.btn41, R.id.btn43}, {R.id.btn44}},
                {{R.id.btn44, R.id.btn42}, {R.id.btn41}},
                {{R.id.btn43}, {R.id.btn42}, {R.id.btn41}}
        };

        int pattern3[][][] = {
                {{R.id.btn21}, {R.id.btn31}, {R.id.btn41}},
                {{R.id.btn22}, {R.id.btn32}, {R.id.btn42}},
                {{R.id.btn23}, {R.id.btn33}, {R.id.btn43}},
                {{R.id.btn24}, {R.id.btn34}, {R.id.btn44}},
                {{R.id.btn11, R.id.btn31}, {R.id.btn41}},
                {{R.id.btn12, R.id.btn32}, {R.id.btn42}},
                {{R.id.btn13, R.id.btn33}, {R.id.btn43}},
                {{R.id.btn14, R.id.btn34}, {R.id.btn44}},
                {{R.id.btn41, R.id.btn21}, {R.id.btn11}},
                {{R.id.btn42, R.id.btn22}, {R.id.btn12}},
                {{R.id.btn43, R.id.btn23}, {R.id.btn13}},
                {{R.id.btn44, R.id.btn24}, {R.id.btn14}},
                {{R.id.btn31}, {R.id.btn21}, {R.id.btn11}},
                {{R.id.btn32}, {R.id.btn22}, {R.id.btn12}},
                {{R.id.btn33}, {R.id.btn23}, {R.id.btn13}},
                {{R.id.btn34}, {R.id.btn24}, {R.id.btn14}}
        };

        int pattern4[][][] = {
                {{R.id.btn12, R.id.btn21}, {R.id.btn13, R.id.btn31}, {R.id.btn14, R.id.btn41}},
                {{R.id.btn11, R.id.btn22, R.id.btn13}, {R.id.btn14, R.id.btn32}, {R.id.btn42}},
                {{R.id.btn12, R.id.btn14, R.id.btn23}, {R.id.btn11, R.id.btn33}, {R.id.btn43}},
                {{R.id.btn13, R.id.btn24}, {R.id.btn12, R.id.btn34}, {R.id.btn11, R.id.btn44}},
                {{R.id.btn11, R.id.btn22, R.id.btn31}, {R.id.btn23, R.id.btn41}, {R.id.btn24}},
                {{R.id.btn12, R.id.btn21, R.id.btn23, R.id.btn32}, {R.id.btn24, R.id.btn42}},
                {{R.id.btn13, R.id.btn22, R.id.btn24, R.id.btn33}, {R.id.btn21, R.id.btn43}},
                {{R.id.btn14, R.id.btn23, R.id.btn34}, {R.id.btn22, R.id.btn44}, {R.id.btn21}},
                {{R.id.btn21, R.id.btn32, R.id.btn41}, {R.id.btn11, R.id.btn33}, {R.id.btn34}},
                {{R.id.btn22, R.id.btn31, R.id.btn33, R.id.btn42}, {R.id.btn12, R.id.btn34}},
                {{R.id.btn23, R.id.btn32, R.id.btn34, R.id.btn43}, {R.id.btn13, R.id.btn31}},
                {{R.id.btn24, R.id.btn33, R.id.btn44}, {R.id.btn14, R.id.btn32}, {R.id.btn31}},
                {{R.id.btn31, R.id.btn42}, {R.id.btn21, R.id.btn43}, {R.id.btn11, R.id.btn44}},
                {{R.id.btn32, R.id.btn41, R.id.btn43}, {R.id.btn22, R.id.btn44}, {R.id.btn12}},
                {{R.id.btn33, R.id.btn42, R.id.btn44}, {R.id.btn23, R.id.btn41}, {R.id.btn13}},
                {{R.id.btn34, R.id.btn43}, {R.id.btn42, R.id.btn24}, {R.id.btn14, R.id.btn41}}
        };

        int pattern[][][] = {};

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

        if (isPresetLoading == false) {
            for (int i = 0; i < 21; i++) {
                if (i == 0 || i > 4) {
                    if (id >= 1 && id <= 4) {
                        if (currentPreset.getMusic().getGesture()) {
                            window.setOnGestureSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], pattern, activity);
                        } else {
                            window.setOnTouchSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], pattern, activity);
                        }
                    }
                }
            }
            Log.i("SoundHelper", "ToggleButton pattern set id " + String.valueOf(patternId));
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

    private ProgressBar progress;
    private int progressCount;
    private int presetSoundCount;

    private int color = R.color.cyan_400;
    private int colorDef = R.color.grey;

    private class Unload extends AsyncTask<Void, Void, Void> {

        String TAG = "UnLoad";
        SharedPreferences prefs;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // started loading
            isPresetLoading = true;
            presetSoundCount = currentPreset.getMusic().getSoundCount();
            // set progress
            progressCount = 0;
            progress = window.getProgressBar(R.id.progress_bar, activity);
            ad.resumeNativeAdView(R.id.adView_main, activity);
            if (window.getView(R.id.progress_bar_layout, activity).getVisibility() == View.GONE) {
                anim.fadeIn(R.id.progress_bar_layout, 0, 600, "progressIn", activity);
                // request ads
                anim.fadeIn(R.id.adView_main, 0, 600, "adViewIn", activity);
                ad.requestLoadNativeAd(ad.getNativeAdView(R.id.adView_main, activity));
                window.setInvisible(R.id.base, 600, activity);
                progress.setIndeterminate(true);
            }
            prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);

            // initialize view
            View buttonViews[] = {
                    window.getView(R.id.btn00, activity),
                    window.getView(R.id.tgl1, activity),
                    window.getView(R.id.tgl2, activity),
                    window.getView(R.id.tgl3, activity),
                    window.getView(R.id.tgl4, activity),
                    window.getView(R.id.tgl5, activity),
                    window.getView(R.id.tgl6, activity),
                    window.getView(R.id.tgl7, activity),
                    window.getView(R.id.tgl8, activity),
                    window.getView(R.id.btn11, activity),
                    window.getView(R.id.btn12, activity),
                    window.getView(R.id.btn13, activity),
                    window.getView(R.id.btn14, activity),
                    window.getView(R.id.btn21, activity),
                    window.getView(R.id.btn22, activity),
                    window.getView(R.id.btn23, activity),
                    window.getView(R.id.btn24, activity),
                    window.getView(R.id.btn31, activity),
                    window.getView(R.id.btn32, activity),
                    window.getView(R.id.btn33, activity),
                    window.getView(R.id.btn34, activity),
                    window.getView(R.id.btn41, activity),
                    window.getView(R.id.btn42, activity),
                    window.getView(R.id.btn43, activity),
                    window.getView(R.id.btn44, activity)
            };

            for (View view : buttonViews) {
                view.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (previousPreset != null) {
                Log.i(TAG, "Preset \"" + previousPreset.getMusic().getName() + "\" unloading");
                for (int i = 0; i < 4; i++) {
                    Log.i(TAG, "Deck " + (i + 1));
                    decks[i].unload();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // start loading sound
            Log.d(TAG, "Finished unloading sounds");
            sp.release();
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            load = new Load().execute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            onLoadFinished();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            onLoadFinished();
        }
    }

    private class Load extends AsyncTask<Void, Void, Void> {

        String TAG = "Load";
        View buttonViews[];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // started loading sounds
            progress.setIndeterminate(false);
            progress.setMax(presetSoundCount);
            progress.setProgress(0);
            window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.ic_tutorial_disabled_white);

            // initialize view
            buttonViews = new View[]{
                    window.getView(R.id.btn00, activity),
                    window.getView(R.id.tgl1, activity),
                    window.getView(R.id.tgl2, activity),
                    window.getView(R.id.tgl3, activity),
                    window.getView(R.id.tgl4, activity),
                    window.getView(R.id.tgl5, activity),
                    window.getView(R.id.tgl6, activity),
                    window.getView(R.id.tgl7, activity),
                    window.getView(R.id.tgl8, activity),
                    window.getView(R.id.btn11, activity),
                    window.getView(R.id.btn12, activity),
                    window.getView(R.id.btn13, activity),
                    window.getView(R.id.btn14, activity),
                    window.getView(R.id.btn21, activity),
                    window.getView(R.id.btn22, activity),
                    window.getView(R.id.btn23, activity),
                    window.getView(R.id.btn24, activity),
                    window.getView(R.id.btn31, activity),
                    window.getView(R.id.btn32, activity),
                    window.getView(R.id.btn33, activity),
                    window.getView(R.id.btn34, activity),
                    window.getView(R.id.btn41, activity),
                    window.getView(R.id.btn42, activity),
                    window.getView(R.id.btn43, activity),
                    window.getView(R.id.btn44, activity)
            };
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (previousPreset != null) {
                Log.i(TAG, "Preset \"" + currentPreset.getMusic().getName() + "\" loading");
                for (int i = 0; i < 4; i++) {
                    Log.i(TAG, "  Deck " + (i + 1));
                    // pad loop
                    for (int j = 0; j < 21; j++) {
                        Log.i(TAG, "    Pad " + (j + 1));
                        // pad read from file
                        ArrayList<String> sounds = new ArrayList<>();
                        for (int k = 0; k < 5; k++) {
                            String sound = currentPreset.getSound(i, j, k);
                            if (sound != null) {
                                sounds.add(sound);
                            }
                        }
                        if (sounds.size() == 1) {
                            // only one sound, use sound
                            decks[i].setPad(new Pad(new Sound(sp, sounds.get(0)), buttonViews[j], color, colorDef, activity), j);
                        } else if (sounds.size() > 1) {
                            // gesture pad
                            decks[i].setPad(getGesturePadFromArray(sounds.toArray(new String[5]), sp, buttonViews[j], color, colorDef, activity), j);
                        } else {
                            // no sounds
                            decks[i].setPad(new Pad(null, buttonViews[j], color, colorDef, activity), j);
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progress.setProgress(progressCount++);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        private int savedSampleId = 0;
        private int savedSampleIdInRunnable = 1;
        // needs to be different at first to make changes

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isCancelled()) {
                onLoadFinish();
            } else {
                // loading finish listener
                Log.d(TAG, "sampleId count : " + presetSoundCount);
                progress.setIndeterminate(true);

                sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, final int sampleId, int status) {
                        Log.d(TAG, "Loading Finished, sampleId : " + sampleId);
                        savedSampleId = sampleId;
                    }
                });

                final Handler intervalTimer = new Handler();

                intervalTimer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // loops while checking the last saved sample id and current one
                        if (savedSampleId == savedSampleIdInRunnable) {
                            // if same, break the loop
                            Log.d(TAG, "Finished loading all sounds");
                            onLoadFinished();
                        } else {
                            // updated
                            savedSampleIdInRunnable = savedSampleId;
                            intervalTimer.postDelayed(this, 100);
                        }
                    }
                }, 100);
            }
        }
    }

    private GesturePad getGesturePadFromArray(String soundPaths[],
                                              SoundPool soundPool,
                                              View buttonView,
                                              int color, int colorDef,
                                              Activity activity) {
        Sound sounds[] = new Sound[5];
        for (int i = 0; i < 5; i++) {
            if (i < soundPaths.length) {
                // sounds exists
                sounds[i] = new Sound(soundPool, soundPaths[i]);
            } else {
                // no sound gesture
                sounds[i] = new Sound(soundPool, null);
            }
        }
        return new GesturePad(sounds, buttonView, color, colorDef, activity);
    }

    private void onLoadFinish() {
        // final sampleId
        Log.d("LoadSound", "Loading completed, SoundPool successfully loaded "
                + presetSoundCount
                + " sounds");

        // pause adViewMain after the loading
        ad.pauseNativeAdView(R.id.adView_main, activity);

        window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.ic_tutorial_white);

        anim.fadeOut(R.id.progress_bar_layout, 0, 600, activity);
        anim.fadeOut(R.id.adView_main, 0, 600, activity);

        // Load finished, set AsyncTask objects to null
        load = null;
        unload = null;

        final Random random = new Random();

        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                window.setVisible(R.id.base, 0, activity);
                buttonRevealAnimation(random.nextInt(25));
            }
        }, 600);

        isPresetLoading = false;
    }

    @Deprecated
    private class UnloadSound extends AsyncTask<Void, Void, Integer> {
        String TAG = "UnloadSound";
        SharedPreferences prefs;

        protected void onPreExecute() {
            Log.d(TAG, "On preExecute, set prefs");
            isPresetLoading = true;
            progressCount = 0;
            presetSoundCount = currentPreset.getMusic().getSoundCount();
            progress = window.getProgressBar(R.id.progress_bar, activity);
            ad.resumeNativeAdView(R.id.adView_main, activity);
            if (window.getView(R.id.progress_bar_layout, activity).getVisibility() == View.GONE) {
                anim.fadeIn(R.id.progress_bar_layout, 0, 600, "progressIn", activity);
                // Request ads
                anim.fadeIn(R.id.adView_main, 0, 600, "adViewIn", activity);
                ad.requestLoadNativeAd(ad.getNativeAdView(R.id.adView_main, activity));
                window.setInvisible(R.id.base, 600, activity);
                progress.setIndeterminate(true);
            }
            prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);

            // initialize view
            View buttonViews[] = {
                    window.getView(R.id.btn00, activity),
                    window.getView(R.id.tgl1, activity),
                    window.getView(R.id.tgl2, activity),
                    window.getView(R.id.tgl3, activity),
                    window.getView(R.id.tgl4, activity),
                    window.getView(R.id.tgl5, activity),
                    window.getView(R.id.tgl6, activity),
                    window.getView(R.id.tgl7, activity),
                    window.getView(R.id.tgl8, activity),
                    window.getView(R.id.btn11, activity),
                    window.getView(R.id.btn12, activity),
                    window.getView(R.id.btn13, activity),
                    window.getView(R.id.btn14, activity),
                    window.getView(R.id.btn21, activity),
                    window.getView(R.id.btn22, activity),
                    window.getView(R.id.btn23, activity),
                    window.getView(R.id.btn24, activity),
                    window.getView(R.id.btn31, activity),
                    window.getView(R.id.btn32, activity),
                    window.getView(R.id.btn33, activity),
                    window.getView(R.id.btn34, activity),
                    window.getView(R.id.btn41, activity),
                    window.getView(R.id.btn42, activity),
                    window.getView(R.id.btn43, activity),
                    window.getView(R.id.btn44, activity)
            };

            for (View view : buttonViews) {
                view.setVisibility(View.INVISIBLE);
            }
        }

        protected Integer doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground, start unloading sounds");
            try {
                if (previousPreset != null) {
                    Log.i(TAG, "Preset \"" + window.getStringFromId(previousPreset.getMusic().getName(), activity));
                    // deck loop
                    for (int i = 0; i < 4; i++) {
                        Log.i(TAG, "  Deck " + (i + 1));
                        // pad loop
                        for (int j = 0; j < 21; j++) {
                            Log.i(TAG, "    Pad " + (j + 1));
                            // pad gesture
                            for (int k = 0; k < 5; k++) {
                                String sound = previousPreset.getSound(i, j, k);
                                if (soundPoolId[i][j][k] != 0 && sound != null) {
                                    if (sp.unload(soundPoolId[i][j][k])) {
                                        Log.i(TAG, "      Pad " + (j + 1) + " Gesture " + k + ", Sound unloaded");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "NPE, Can't find soundPool");
            }
            return 0;
        }

        protected void onProgressUpdate(Void... arg0) {
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d(TAG, "Finished unloading sound");
            sp.release();
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            loadSound = new LoadSound().execute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("TAG", "UnLoadSound successfully canceled");
        }
    }

    @Deprecated
    private class LoadSound extends AsyncTask<Void, Void, String> {
        String TAG = "LoadSound";

        protected void onPreExecute() {
            Log.d(TAG, "On preExceute, unloadPresetSound");

            progress.setIndeterminate(false);
            progress.setMax(presetSoundCount);
            progress.setProgress(0);
            window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.ic_tutorial_disabled_white);
        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground, start loading sounds");

            if (currentPreset != null) {
                Log.i(TAG, "Preset \"" + window.getStringFromId(currentPreset.getMusic().getName(), activity));
                // deck loop
                for (int i = 0; i < 4; i++) {
                    Log.i(TAG, "  Deck " + (i + 1));
                    // pad loop
                    for (int j = 0; j < 21; j++) {
                        Log.i(TAG, "    Pad " + (j + 1));
                        // pad gesture
                        for (int k = 0; k < 5; k++) {
                            String sound = currentPreset.getSound(i, j, k);
                            if (sound != null) {
                                soundPoolId[i][j][k] = sp.load(sound, 1);
                                Log.i(TAG, sound + " loaded");
                                Log.i(TAG, "      Pad " + (j + 1) + " Gesture " + k + ", Sound loaded");
                                publishProgress();
                            }
                        }
                    }
                }
            }
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Void... arg0) {
            progress.setProgress(progressCount++);
        }

        private int savedSampleId = 0;
        private int savedSampleIdInRunnable = 1;
        // needs to be different at first to make changes

        protected void onPostExecute(String result) {
            Log.d(TAG, "sampleId count : " + presetSoundCount);
            progress.setIndeterminate(true);

            sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, final int sampleId, int status) {
                    Log.d(TAG, "Loading Finished, sampleId : " + sampleId);
                    savedSampleId = sampleId;
                }
            });

            final Handler intervalTimer = new Handler();

            intervalTimer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // loops while checking the last saved sample id and current one
                    if (savedSampleId == savedSampleIdInRunnable) {
                        // if same, break the loop
                        Log.d(TAG, "Finished loading all sounds");
                        onLoadFinished();
                    } else {
                        // updated
                        savedSampleIdInRunnable = savedSampleId;
                        intervalTimer.postDelayed(this, 100);
                    }
                }
            }, 100);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("TAG", "LoadSound successfully canceled");
        }
    }

    @Deprecated
    private void onLoadFinished() {
        // final sampleId
        Log.d("LoadSound", "Loading completed, SoundPool successfully loaded "
                + presetSoundCount
                + " sounds");

        // pause adViewMain after the loading
        ad.pauseNativeAdView(R.id.adView_main, activity);

        window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.ic_tutorial_white);

        anim.fadeOut(R.id.progress_bar_layout, 0, 600, activity);
        anim.fadeOut(R.id.adView_main, 0, 600, activity);

        // Load finished, set AsyncTask objects to null
        loadSound = null;
        unLoadSound = null;

        final Random random = new Random();

        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                window.setVisible(R.id.base, 0, activity);
                buttonRevealAnimation(random.nextInt(25));
            }
        }, 600);

        isPresetLoading = false;
    }

    private int intervalPixel;

    private int intervalCount;

    private void buttonRevealAnimation(final int buttonRectIndex) {
        final Rect buttonRects[] = {
                window.getRect(R.id.btn00, activity),
                window.getRect(R.id.tgl1, activity),
                window.getRect(R.id.tgl2, activity),
                window.getRect(R.id.tgl3, activity),
                window.getRect(R.id.tgl4, activity),
                window.getRect(R.id.tgl5, activity),
                window.getRect(R.id.tgl6, activity),
                window.getRect(R.id.tgl7, activity),
                window.getRect(R.id.tgl8, activity),
                window.getRect(R.id.btn11, activity),
                window.getRect(R.id.btn12, activity),
                window.getRect(R.id.btn13, activity),
                window.getRect(R.id.btn14, activity),
                window.getRect(R.id.btn21, activity),
                window.getRect(R.id.btn22, activity),
                window.getRect(R.id.btn23, activity),
                window.getRect(R.id.btn24, activity),
                window.getRect(R.id.btn31, activity),
                window.getRect(R.id.btn32, activity),
                window.getRect(R.id.btn33, activity),
                window.getRect(R.id.btn34, activity),
                window.getRect(R.id.btn41, activity),
                window.getRect(R.id.btn42, activity),
                window.getRect(R.id.btn43, activity),
                window.getRect(R.id.btn44, activity)
        };

        final View buttonViews[] = {
                window.getView(R.id.btn00, activity),
                window.getView(R.id.tgl1, activity),
                window.getView(R.id.tgl2, activity),
                window.getView(R.id.tgl3, activity),
                window.getView(R.id.tgl4, activity),
                window.getView(R.id.tgl5, activity),
                window.getView(R.id.tgl6, activity),
                window.getView(R.id.tgl7, activity),
                window.getView(R.id.tgl8, activity),
                window.getView(R.id.btn11, activity),
                window.getView(R.id.btn12, activity),
                window.getView(R.id.btn13, activity),
                window.getView(R.id.btn14, activity),
                window.getView(R.id.btn21, activity),
                window.getView(R.id.btn22, activity),
                window.getView(R.id.btn23, activity),
                window.getView(R.id.btn24, activity),
                window.getView(R.id.btn31, activity),
                window.getView(R.id.btn32, activity),
                window.getView(R.id.btn33, activity),
                window.getView(R.id.btn34, activity),
                window.getView(R.id.btn41, activity),
                window.getView(R.id.btn42, activity),
                window.getView(R.id.btn43, activity),
                window.getView(R.id.btn44, activity)
        };

        intervalPixel = (int) Math.hypot(window.getWindowWidthPx(activity), window.getWindowWidthPx(activity)) / 40;
        Log.i("intervalPixel", String.valueOf(intervalPixel));
        intervalCount = 0;
        // 40 intervals x 10ms = 400ms animation

        anim.fadeIn(buttonViews[buttonRectIndex], 0, 100, "btn" + String.valueOf(buttonRectIndex) + "In", activity);

        final Handler intervalTimer = new Handler();
        intervalTimer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (intervalCount <= 40) {
                    for (int i = 0; i < buttonRects.length; i++) {
                        if (buttonRectIndex != i) {
                            // not the view itself
                            if (isAnimationCollides(
                                    buttonRects[buttonRectIndex],
                                    buttonRects[i],
                                    intervalPixel * intervalCount) &&
                                    buttonViews[i].getVisibility() != View.VISIBLE) {
                                // collides, fadeIn
                                anim.fadeIn(buttonViews[i], 0, 50, "btn" + String.valueOf(i) + "In", activity);
                            }
                        }
                    }
                    intervalCount++;
                    intervalTimer.postDelayed(this, 10);
                }
            }
        }, 10);
    }

    private boolean isAnimationCollides(Rect startViewRect, Rect targetViewRect, int distance) {
        double viewDistance =
                Math.hypot(
                        Math.abs(startViewRect.centerX() - targetViewRect.centerX()),
                        Math.abs(startViewRect.centerY() - targetViewRect.centerY())
                );
        // gets view hypot
        return viewDistance < distance;
    }
}
