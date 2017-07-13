package com.bedrock.padder.model;

import android.media.SoundPool;

public class Sound {

    private int soundPoolId = 0;

    private int streamId = 0;

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

    void unload() {
        if (soundPoolId != 0 && soundPool != null) {
            this.soundPool.unload(soundPoolId);
            soundPoolId = 0;
            streamId = 0;
        }
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

    private class NullStreamException extends Exception {
        NullStreamException() {
            super("streamID is not initialized");
        }
    }
}
