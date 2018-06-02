package com.bedrock.padder.model.tutorial;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.bedrock.padder.helper.WindowHelper.getId;
import static com.bedrock.padder.model.tutorial.TimingListener.timings;

public class Tutorial {

    public static TimingListener TIMING_LISTENER;

    private String tag;

    private int bpm;

    private Activity activity;

    private ArrayList<Sync> syncs = new ArrayList<>();
    private int currentSyncIndex = 0;

    private TutorialListener listener;

    private Handler handler;

    private View decks[] = new View[4];
    private View pads[] = new View[45];

    private Animation animations[] = new Animation[5];
    public static Animation ANIMATION_FADE = new AlphaAnimation(1, 0);

    public static int TUTORIAL_ANIMATION_DURATION = 400; // 400 ~ 500 seems legit

    public Tutorial(String tag, Activity activity) {
        this.tag = tag;
        this.activity = activity;
        init();
    }

    public Tutorial(String tag, int bpm, Activity activity) {
        this.tag = tag;
        this.bpm = bpm;
        this.activity = activity;
        init();
    }

    public void parse() {
        listener.onLoadStart(this);
        new Parse().execute();
    }

    private class Parse extends AsyncTask<Void, Void, Tutorial> {

        TutorialXmlParser parser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            parser = new TutorialXmlParser(tag, activity);
        }

        @Override
        protected Tutorial doInBackground(Void... voids) {
            return parser.parse();
        }

        @Override
        protected void onPostExecute(Tutorial tutorial) {
            super.onPostExecute(tutorial);
            init(tutorial);
            Log.d("TUTORIAL", "onPostExecute");
            listener.onLoadFinish(getTutorial());
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getBpm() {
        return bpm;
    }

    public Tutorial getTutorial() {
        return this;
    }

    public void addSync(Sync sync) {
        syncs.add(sync);
    }

    public void addSyncs(ArrayList<Sync> sync) {
        syncs.addAll(sync);
    }

    public boolean isSorted() {
        boolean sorted = true;
        int start = -1;
        for (Sync sync : syncs) {
            if (sync.start > start) {
                // normal
                start = sync.start;
            } else {
                sorted = false;
                break;
            }
        }

        return sorted;
    }

    public void setTutorialListener(TutorialListener listener) {
        this.listener = listener;
    }

    public void sort() {
        Collections.sort(syncs, new SyncComparator());
    }

    private Runnable nextTutorial = new Runnable() {
        @Override
        public void run() {
            Sync sync = syncs.get(currentSyncIndex++);
            for (Sync.Item item : sync.items) {
                show(item.deck, item.pad, item.gesture);
            }

            if (currentSyncIndex < syncs.size()) {
                handler.postDelayed(this, syncs.get(currentSyncIndex).getStart() - sync.getStart());
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TUTORIAL", "ended");
                        listener.onFinish(getTutorial());
                        timings.clear();
                        currentSyncIndex = 0;
                    }
                }, 1000); // delay the finish
            }
        }
    };

    public void start() {
        handler.postDelayed(nextTutorial, syncs.get(0).getStart());
        listener.onStart(this);
    }

    private void show(int deck, int pad, int gesture) {
        if (deck > 0) {
            TutorialView view;
            if (pad != -1) {
                if (gesture != -1) {
                    // gesture pad
                    view = new TutorialView(pads[pad], new Timing(deck, pad, gesture));
                    view.setAnimation(animations[gesture]);
                } else {
                    view = new TutorialView(pads[pad], new Timing(deck, pad));
                    view.setAnimation(animations[0]);
                }
            } else {
                view = new TutorialView(decks[deck - 1], new Timing(deck));
                view.setAnimation(animations[0]);
            }
            view.start();
        }
        Log.d("TUTORIAL", "show [" + deck + ", " + pad + ", " + gesture + "]");
    }

    private void init(Tutorial tutorial) {
        if (tutorial != null) {
            this.bpm = tutorial.bpm;
            this.syncs = tutorial.syncs;
            Log.i(this.getClass().getSimpleName(), "tutorial.syncs.size = " + tutorial.syncs.size());
            Log.i(this.getClass().getSimpleName(), "syncs.size = " + syncs.size());
            init();
        } else {
            Log.e(this.getClass().getSimpleName(), ".init: tutorial is null");
        }
    }

    private void init() {
        handler = new Handler(Looper.getMainLooper());

        for (int i = 0; i < decks.length; i++) {
            decks[i] = activity.findViewById(getId("tgl" + String.valueOf(i + 1) + "_tutorial"));
        }

        pads[0] = activity.findViewById(getId("btn00_tutorial"));

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                pads[i * 10 + j] = activity.findViewById(getId("btn" + String.valueOf(i) + String.valueOf(j) + "_tutorial"));
            }
        }

        ANIMATION_FADE.setDuration(50);

        animations[0] = new ScaleAnimation(
                1, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        animations[1] = new ScaleAnimation(
                1, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1.0f);

        animations[2] = new ScaleAnimation(
                0, 1, 1, 1,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        animations[3] = new ScaleAnimation(
                1, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.0f);

        animations[4] = new ScaleAnimation(
                0, 1, 1, 1,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        for (Animation animation : animations) {
            animation.setInterpolator(new LinearOutSlowInInterpolator());
            animation.setDuration(TUTORIAL_ANIMATION_DURATION);
        }
    }

    public int size() {
        return syncs.size();
    }

    @Override
    public String toString() {
        return "Tutorial{" +
                "tag='" + tag + '\'' +
                ", bpm=" + bpm +
                ", activity=" + activity +
                ", syncs.size()=" + syncs.size() +
                '}';
    }

    public String syncToString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(syncs.toArray());
    }

    public interface TutorialListener {
        void onLoadStart(Tutorial tutorial);

        void onLoadFinish(Tutorial tutorial);

        void onStart(Tutorial tutorial);

        void onFinish(Tutorial tutorial);
    }

    public static class SyncComparator implements Comparator<Sync> {

        @Override
        public int compare(Sync t0, Sync t1) {
            // Sorting in ascending order of start offset
            return t0.start - t1.start;
        }
    }
}
