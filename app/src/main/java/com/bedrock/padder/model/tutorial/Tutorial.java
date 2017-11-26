package com.bedrock.padder.model.tutorial;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bedrock.padder.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Tutorial {

    private String tag;

    private int bpm;

    private Activity activity;

    private ArrayList<Sync> syncs = new ArrayList<>();

    private View tutorialPads[];

    private int tutorialViewIds[] = {
            R.id.btn00_tutorial,
            R.id.tgl1_tutorial,
            R.id.tgl2_tutorial,
            R.id.tgl3_tutorial,
            R.id.tgl4_tutorial,
            R.id.tgl5_tutorial,
            R.id.tgl6_tutorial,
            R.id.tgl7_tutorial,
            R.id.tgl8_tutorial,
            R.id.btn11_tutorial,
            R.id.btn12_tutorial,
            R.id.btn13_tutorial,
            R.id.btn14_tutorial,
            R.id.btn21_tutorial,
            R.id.btn22_tutorial,
            R.id.btn23_tutorial,
            R.id.btn24_tutorial,
            R.id.btn31_tutorial,
            R.id.btn32_tutorial,
            R.id.btn33_tutorial,
            R.id.btn34_tutorial,
            R.id.btn41_tutorial,
            R.id.btn42_tutorial,
            R.id.btn43_tutorial,
            R.id.btn44_tutorial
    };

    public Tutorial(String tag, Activity activity) {
        this.tag = tag;
        this.activity = activity;
        initViews();
    }

    public Tutorial(String tag, int bpm, Activity activity) {
        this.tag = tag;
        this.bpm = bpm;
        this.activity = activity;
        initViews();
    }

    public void parse() {
        TutorialXmlParser parser = new TutorialXmlParser(tag, activity);
        init(parser.parse());
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

    public void sort() {
        Collections.sort(syncs, new SyncComparator());
    }

    private void init(Tutorial tutorial) {
        if (tutorial != null) {
            this.bpm = tutorial.bpm;
            this.syncs = tutorial.syncs;
            Log.i(this.getClass().getSimpleName(), "tutorial.syncs.size = " + tutorial.syncs.size());
            Log.i(this.getClass().getSimpleName(), "syncs.size = " + syncs.size());
            initViews();
        } else {
            Log.e(this.getClass().getSimpleName(), ".init: tutorial is null");
        }
    }

    public int size() {
        return syncs.size();
    }

    private void initViews() {
        View views[] = new View[tutorialViewIds.length];

        for (int i = 0; i < tutorialViewIds.length; i++) {
            views[i] = activity.findViewById(tutorialViewIds[i]);
        }
    }

    @Override
    public String toString() {
        return "Tutorial{" +
                "tag='" + tag + '\'' +
                ", bpm=" + bpm +
                ", activity=" + activity +
                ", syncs.size()=" + syncs.size() +
                ", tutorialPads=" + Arrays.toString(tutorialPads) +
                ", tutorialViewIds=" + Arrays.toString(tutorialViewIds) +
                '}';
    }
}
