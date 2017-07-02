package com.bedrock.padder.model;

import android.media.SoundPool;

public class Sound {

    int soundPoolId = 0;

    int streamId = 0;

    boolean isLooping = false;

    private SoundPool soundPool;

    String TAG = "Sound";

    public Sound(SoundPool soundPool, String path) {
        this.soundPool = soundPool;
        this.load(path);
    }

    void play() {
        if (isLooping) {
            streamId = soundPool.play(soundPoolId, 1, 1, 1, -1, 1);
        } else {
            streamId = soundPool.play(soundPoolId, 1, 1, 1, 0, 1);
        }
    }

    void stop() {
        try {
            if (streamId != 0) {
                soundPool.stop(streamId);
            } else {
                throw new NullStreamException();
            }
        } catch (NullStreamException e) {
            e.getMessage();
        }
    }

    void loop() {
        this.isLooping = true;
        play();
    }

    void load(String path) {
        this.soundPoolId = soundPool.load(path, 1);
    }

    void setRate(float rate) {
        try {
            if (streamId != 0) {
                soundPool.setRate(streamId, rate);
            } else {
                throw new NullStreamException();
            }
        } catch (NullStreamException e) {
            e.getMessage();
        }
    }

    protected class NullStreamException extends Exception {
        public NullStreamException() {
            super("streamID is not initialized");
        }
    }
}
