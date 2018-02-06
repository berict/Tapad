package com.bedrock.padder.model.tutorial;

import android.util.Log;

import java.util.ArrayList;

import static com.bedrock.padder.model.tutorial.Tutorial.TUTORIAL_ANIMATION_DURATION;

public class TimingListener {

    public static ArrayList<Timing> timings = new ArrayList<>();

    // attach on TutorialView
    public static void listen(Timing timing) {
        if (timings.indexOf(timing) >= 0) {
            timings.remove(timing);
            Log.e("broadcast", "duplicate: listener removed for " + timing.toString());
        }
        timings.add(timing);
        Log.i("broadcast", "listener added for " + timing.toString());
    }

    // attach on Pad
    public static boolean broadcast(Timing timing) {
        int index = timings.indexOf(timing);
        if (index >= 0) {
            Timing listening = timings.get(index);
            // timing configuration
            int delay = (int) (timing.listenTime - listening.listenTime
                    - (TUTORIAL_ANIMATION_DURATION + 100));

            if (listening.listener != null) {
                listening.listener.onBroadcast(timing, delay);
            }
            timings.remove(index);
            Log.e("broadcast", "listener removed for " + timing.toString());
            return true;
        } else {
            Log.e("broadcast", "no listener found for " + timing.toString());
            return false;
        }
    }

    public static boolean broadcast(int deck, int pad, int gesture) {
        return broadcast(new Timing(deck, pad, gesture));
    }

    public static boolean broadcast(int deck, int pad) {
        return broadcast(new Timing(deck, pad));
    }

    public static boolean broadcast(int deck) {
        return broadcast(new Timing(deck));
    }
}
