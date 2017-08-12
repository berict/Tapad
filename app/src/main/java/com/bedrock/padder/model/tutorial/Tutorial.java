package com.bedrock.padder.model.tutorial;

import android.view.animation.ScaleAnimation;

public class Tutorial {

    private PadTiming padTimings[];

    private ScaleAnimation animations[];

    public Tutorial(PadTiming[] padTimings, ScaleAnimation[] animations) {
        this.padTimings = padTimings;
        this.animations = animations;
    }

    public void start() {
        // start all pads all threads
        for (PadTiming padTiming : padTimings) {
            if (padTiming != null) {
                padTiming.start();
            }
        }
    }

    public void stop() {
        // cancel all pads all threads
        for (PadTiming padTiming : padTimings) {
            if (padTiming != null) {
                padTiming.stop();
            }
        }
    }
}
