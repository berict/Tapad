package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bedrock.padder.R;
import com.bedrock.padder.model.preset.Preset;
import com.bedrock.padder.model.sound.Deck;
import com.bedrock.padder.model.sound.GesturePad;
import com.bedrock.padder.model.sound.Pad;
import com.bedrock.padder.model.sound.Sound;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isPresetLoading;
import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class SoundHelper {
    private static Preset previousPreset = null;
    private SoundPool sp = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
    private MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private int toggle;
    private int soundPoolId[][][] = new int[4][17][5];
    private Deck decks[];
    private Activity activity;
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
    private AdmobHelper ad = new AdmobHelper();
    private AnimateHelper anim = new AnimateHelper();
    private WindowHelper window = new WindowHelper();
    private AsyncTask unload = null;
    private AsyncTask load = null;
    private ProgressBar progress;
    private int progressCount;
    private int presetSoundCount;
    private int color = 0;
    private int colorDef = 0;
    private int intervalPixel;
    private int intervalCount;
    private SharedPreferences prefs;

    public SoundPool getSoundPool() {
        return sp;
    }

    public void setDecks(int color, int colorDef, Activity activity) {
        prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        this.color = prefs.getInt("color", R.color.cyan_400);
        this.colorDef = colorDef;
        decks = new Deck[] {
                new Deck(new Pad[17], null, window.getView(buttonId[1], activity), color, colorDef, activity),
                new Deck(new Pad[17], null, window.getView(buttonId[2], activity), color, colorDef, activity),
                new Deck(new Pad[17], null, window.getView(buttonId[3], activity), color, colorDef, activity),
                new Deck(new Pad[17], null, window.getView(buttonId[4], activity), color, colorDef, activity),
        };

        for (int i = 1; i <= 4; i++) {
            final int index = i - 1;
            window.setOnClick(buttonId[i], new Runnable() {
                @Override
                public void run() {
                    // set onTouch events
                    if (decks[index].isSelected()) {
                        // was already selected
                        select(-1);
                    } else {
                        select(index);
                    }
                }
            }, activity);
        }
    }

    private void select(int index) {
        Log.d("SH", "Index selected " + index);
        // index starts from 0
        if (index == -1) {
            // disable all
            for (Deck deck : decks) {
                deck.setUnselected();
                deck.setSelected(false);
            }
        } else {
            for (int i = 0; i < decks.length; i++) {
                decks[i].setSound(new Sound(sp, currentPreset.getSound(index, i + 1), mmr));
                if (i == index) {
                    // selected
                    decks[i].setSelected(true);
                } else {
                    decks[i].setSelected(false);
                }
            }
        }
    }

    public void clear() {
        // clear buttons
        for (Deck deck : decks) {
            for (Pad pad : deck.getPads()) {
                if (pad != null) {
                    pad.stop();
                }
            }
        }
    }

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

    public void stop() {
        for (Deck deck : decks) {
            deck.stop();
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
                sounds[i] = new Sound(soundPool, soundPaths[i], mmr);
            } else {
                // no sound gesture
                sounds[i] = new Sound(soundPool, null, mmr);
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
        // gets view hypothesis
        return viewDistance < distance;
    }

    private class Unload extends AsyncTask<Void, Void, Void> {

        String TAG = "UnLoad";
        SharedPreferences prefs;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // started loading
            isPresetLoading = true;
            presetSoundCount = currentPreset.getSoundCount();
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
                Log.i(TAG, "Preset \"" + previousPreset.getTag() + "\" unloading");
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
            onLoadFinish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            onLoadFinish();
        }
    }

    private class Load extends AsyncTask<Void, Void, Void> {

        String TAG = "Load";
        View buttonViews[];
        private int savedSampleId = 0;
        private int savedSampleIdInRunnable = 1;

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
                Log.i(TAG, "Preset \"" + currentPreset.getTag() + "\" loading");
                for (int i = 0; i < 4; i++) {
                    Log.i(TAG, "  Deck " + (i + 1));
                    // pad loop
                    for (int j = 0; j < 17; j++) {
                        Log.i(TAG, "    Pad " + (j + 1));
                        // pad read from file
                        ArrayList<String> sounds = new ArrayList<>();
                        for (int k = 0; k < 5; k++) {
                            String sound = currentPreset.getSound(i, j, k);
                            if (sound != null) {
                                sounds.add(sound);
                                publishProgress();
                            }
                        }
                        if (sounds.size() == 1) {
                            // only one sound, use sound
                            decks[i].setPad(new Pad(new Sound(sp, sounds.get(0), mmr), buttonViews[j],
                                    color, colorDef, activity), j);
                        } else if (sounds.size() > 1) {
                            // gesture pad
                            decks[i].setPad(getGesturePadFromArray(sounds.toArray(new String[5]), sp, buttonViews[j],
                                    color, colorDef, activity), j);
                        } else {
                            // no sounds
                            decks[i].setPad(new Pad(null, buttonViews[j],
                                    color, colorDef, activity), j);
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
                            onLoadFinish();
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
}
