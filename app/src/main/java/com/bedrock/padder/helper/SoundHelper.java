package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bedrock.padder.R;
import com.bedrock.padder.activity.MainActivity;
import com.bedrock.padder.model.preferences.Preferences;
import com.bedrock.padder.model.preset.Preset;
import com.bedrock.padder.model.sound.Deck;
import com.bedrock.padder.model.sound.GesturePad;
import com.bedrock.padder.model.sound.Pad;
import com.bedrock.padder.model.sound.Sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.bedrock.padder.activity.MainActivity.PAD_PATTERN;
import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isPresetLoading;
import static com.bedrock.padder.activity.MainActivity.setPadPattern;
import static com.bedrock.padder.helper.WindowHelper.getViewFromId;
import static com.bedrock.padder.model.tutorial.TimingListener.broadcast;

public class SoundHelper {

    private static Preset previousPreset = null;

    private SoundPool sp = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);

    private MediaMetadataRetriever mmr = new MediaMetadataRetriever();

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

    private int padPatternToggleId[] = {
            R.id.tgl5,
            R.id.tgl6,
            R.id.tgl7,
            R.id.tgl8,
    };

    private AdMobHelper ad = new AdMobHelper();
    private AnimateHelper anim = new AnimateHelper();
    private WindowHelper window = new WindowHelper();

    private AsyncTask unload = null;
    private AsyncTask load = null;

    private ProgressBar progress;

    private int progressCount;
    private int presetSoundCount;

    private Deck decks[];

    private int color = 0;
    private int colorDef = 0;

    private int intervalPixel;
    private int intervalCount;

    public SoundPool getSoundPool() {
        return sp;
    }

    public void setDecks(int color, int colorDef, Activity activity) {
        Preferences preferences = new Preferences(activity);
        this.color = preferences.getColor();
        this.colorDef = colorDef;
        decks = new Deck[]{
                new Deck(new Pad[17], null, activity.findViewById(buttonId[1]), color, colorDef, activity),
                new Deck(new Pad[17], null, activity.findViewById(buttonId[2]), color, colorDef, activity),
                new Deck(new Pad[17], null, activity.findViewById(buttonId[3]), color, colorDef, activity),
                new Deck(new Pad[17], null, activity.findViewById(buttonId[4]), color, colorDef, activity),
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

        for (int i = 0; i < 4; i++) {
            final int index = i + 1;
            window.setOnClick(padPatternToggleId[i], new Runnable() {
                @Override
                public void run() {
                    // set pad pattern ids
                    if (index == PAD_PATTERN) {
                        // was already selected
                        selectPattern(-1);
                    } else {
                        selectPattern(index);
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
                    broadcast(i + 1);
                    decks[i].setSelected(true);
                    for (Pad pad : decks[i].getPads()) {
                        // update looping pads
                        pad.update();
                    }
                } else {
                    decks[i].setSelected(false);
                }
            }
        }
    }

    private void selectPattern(int index) {
        Log.d("SH", "Index selected " + index);
        // index starts from 0
        if (index == -1) {
            // disable all
            setPadPattern(0);
            for (int i = 1; i <= 4; i++) {
                getViewFromId("tgl" + String.valueOf(i + 4), activity).setBackgroundColor(activity.getResources().getColor(R.color.grey));
            }
        } else {
            setPadPattern(index);
            for (int i = 1; i <= 4; i++) {
                if (i == index) {
                    // selected
                    getViewFromId("tgl" + String.valueOf(i + 4), activity).setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                } else {
                    getViewFromId("tgl" + String.valueOf(i + 4), activity).setBackgroundColor(activity.getResources().getColor(R.color.grey));
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

    public void loadColor(int color) {
        if (decks != null && currentPreset != null) {
            for (Deck deck : decks) {
                for (Pad pad : deck.getPads()) {
                    if (pad != null) {
                        pad.setColor(color);
                    }
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

        if (!(currentPreset.getInAppTutorialAvailable() ||
                currentPreset.getAbout().getTutorialAvailable())) {
            // no tutorial available
            activity.findViewById(R.id.toolbar_tutorial).setVisibility(View.GONE);
        } else {
            activity.findViewById(R.id.toolbar_tutorial).setVisibility(View.VISIBLE);
        }
    }

    public void cancelLoad() {
        try {
            unload.cancel(true);
            Log.i("TAG", "Unloading canceled");
        } catch (NullPointerException e) {
            Log.e("NPE", "Unload AsyncTask is null");
        }
        try {
            load.cancel(true);
            Log.i("TAG", "Loading canceled");
        } catch (NullPointerException e) {
            Log.e("NPE", "Loading AsyncTask is null");
        }
    }

    public void stop() {
        for (Deck deck : decks) {
            deck.stop();
        }
    }

    public void stopAll() {
        sp.autoPause();
        stop();
    }

    private GesturePad getGesturePadFromArray(String soundPaths[],
                                              SoundPool soundPool,
                                              int deck,
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
        return new GesturePad(sounds, deck, buttonView, color, colorDef, activity);
    }

    private void onLoadFinish() {
        // final sampleId
        Log.d("LoadSound", "Loading completed, SoundPool successfully loaded "
                + presetSoundCount
                + " sounds");

        // pause adViewMain after the loading
        ad.pauseAdView(R.id.adView_main, activity);

        ((ImageView) activity.findViewById(R.id.toolbar_tutorial_icon)).setImageResource(R.drawable.ic_tutorial_white);

        anim.fadeOut(R.id.progress_bar_layout, 0, 600, activity);
        anim.fadeOut(R.id.adView_main, 0, 600, activity);

        // Load finished, set AsyncTask objects to null
        load = null;
        unload = null;

        revealButtonWithAnimation();

        isPresetLoading = false;
    }

    public void revealButtonWithAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                window.setVisible(R.id.base, 0, activity);
                buttonRevealAnimation(new Random().nextInt(25));
            }
        }, 600);
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
                activity.findViewById(R.id.btn00),
                activity.findViewById(R.id.tgl1),
                activity.findViewById(R.id.tgl2),
                activity.findViewById(R.id.tgl3),
                activity.findViewById(R.id.tgl4),
                activity.findViewById(R.id.tgl5),
                activity.findViewById(R.id.tgl6),
                activity.findViewById(R.id.tgl7),
                activity.findViewById(R.id.tgl8),
                activity.findViewById(R.id.btn11),
                activity.findViewById(R.id.btn12),
                activity.findViewById(R.id.btn13),
                activity.findViewById(R.id.btn14),
                activity.findViewById(R.id.btn21),
                activity.findViewById(R.id.btn22),
                activity.findViewById(R.id.btn23),
                activity.findViewById(R.id.btn24),
                activity.findViewById(R.id.btn31),
                activity.findViewById(R.id.btn32),
                activity.findViewById(R.id.btn33),
                activity.findViewById(R.id.btn34),
                activity.findViewById(R.id.btn41),
                activity.findViewById(R.id.btn42),
                activity.findViewById(R.id.btn43),
                activity.findViewById(R.id.btn44)
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // started loading
            isPresetLoading = true;
            presetSoundCount = currentPreset.getSoundCount();
            // set progress
            progressCount = 0;
            progress = ((ProgressBar) activity.findViewById(R.id.progress_bar));
            ad.resumeAdView(R.id.adView_main, activity);
            if (activity.findViewById(R.id.progress_bar_layout).getVisibility() == View.GONE) {
                anim.fadeIn(R.id.progress_bar_layout, 0, 600, "progressIn", activity);
                // request ads
                anim.fadeIn(R.id.adView_main, 0, 600, "adViewIn", activity);
                ad.requestLoadAd(ad.getAdView(R.id.adView_main, activity));
                window.setInvisible(R.id.base, 600, activity);
                progress.setIndeterminate(true);
            }

            // initialize view
            View buttonViews[] = {
                    activity.findViewById(R.id.btn00),
                    activity.findViewById(R.id.tgl1),
                    activity.findViewById(R.id.tgl2),
                    activity.findViewById(R.id.tgl3),
                    activity.findViewById(R.id.tgl4),
                    activity.findViewById(R.id.tgl5),
                    activity.findViewById(R.id.tgl6),
                    activity.findViewById(R.id.tgl7),
                    activity.findViewById(R.id.tgl8),
                    activity.findViewById(R.id.btn11),
                    activity.findViewById(R.id.btn12),
                    activity.findViewById(R.id.btn13),
                    activity.findViewById(R.id.btn14),
                    activity.findViewById(R.id.btn21),
                    activity.findViewById(R.id.btn22),
                    activity.findViewById(R.id.btn23),
                    activity.findViewById(R.id.btn24),
                    activity.findViewById(R.id.btn31),
                    activity.findViewById(R.id.btn32),
                    activity.findViewById(R.id.btn33),
                    activity.findViewById(R.id.btn34),
                    activity.findViewById(R.id.btn41),
                    activity.findViewById(R.id.btn42),
                    activity.findViewById(R.id.btn43),
                    activity.findViewById(R.id.btn44)
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
            ((ImageView) activity.findViewById(R.id.toolbar_tutorial_icon)).setImageResource(R.drawable.ic_tutorial_disabled_white);

            // initialize view
            buttonViews = new View[]{
                    activity.findViewById(R.id.btn00),
                    activity.findViewById(R.id.btn11),
                    activity.findViewById(R.id.btn12),
                    activity.findViewById(R.id.btn13),
                    activity.findViewById(R.id.btn14),
                    activity.findViewById(R.id.btn21),
                    activity.findViewById(R.id.btn22),
                    activity.findViewById(R.id.btn23),
                    activity.findViewById(R.id.btn24),
                    activity.findViewById(R.id.btn31),
                    activity.findViewById(R.id.btn32),
                    activity.findViewById(R.id.btn33),
                    activity.findViewById(R.id.btn34),
                    activity.findViewById(R.id.btn41),
                    activity.findViewById(R.id.btn42),
                    activity.findViewById(R.id.btn43),
                    activity.findViewById(R.id.btn44)
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
                            decks[i].setPad(new Pad(new Sound(sp, sounds.get(0), mmr), i + 1, buttonViews[j],
                                    color, colorDef, activity), j);
                        } else if (sounds.size() > 1) {
                            // gesture pad
                            decks[i].setPad(getGesturePadFromArray(sounds.toArray(new String[5]), sp, i + 1, buttonViews[j],
                                    color, colorDef, activity), j);
                        } else {
                            // no sounds
                            decks[i].setPad(new Pad(new Sound(), i + 1, buttonViews[j],
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

                // Add loaded preset to shortcut
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
                    Intent main = new Intent(activity.getApplicationContext(), MainActivity.class);
                    main.setAction(Intent.ACTION_VIEW);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    main.putExtra("shortcut", currentPreset.getTag());

                    ShortcutManager shortcutManager = activity.getSystemService(ShortcutManager.class);

                    ShortcutInfo.Builder shortcut = new ShortcutInfo.Builder(activity, currentPreset.getTag())
                            .setShortLabel(currentPreset.getAbout().getSongName())
                            .setLongLabel(currentPreset.getAbout().getSongName() + " - " + currentPreset.getAbout().getSongArtist())
                            .setIntent(main);

                    Bitmap icon;
                    try {
                        icon = BitmapFactory.decodeStream(new FileInputStream(
                                new File(currentPreset.getAbout().getImage(currentPreset))
                        ));

                        shortcut = shortcut.setIcon(Icon.createWithBitmap(icon));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (shortcutManager != null) {
                        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut.build()));
                    }
                }

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
