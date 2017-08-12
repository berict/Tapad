package com.bedrock.padder.model.sound;

import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.util.Log;

public class Sound {

    boolean isLooping = false;
    String TAG = "Sound";
    private int soundPoolId = 0;
    private int streamId = 0;
    private int duration = -1;
    private SoundPool soundPool;

    public Sound(SoundPool soundPool, String path, MediaMetadataRetriever mmr) {
        this.soundPool = soundPool;
        this.duration = getDurationFromFile(path, mmr);
        this.load(path);
    }

    private int getDurationFromFile(String path, MediaMetadataRetriever mmr) {
        if (path != null) {
            try {
                mmr.setDataSource(path);
                return Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "IAE");
                return -1;
            }
        } else {
            return -1;
        }
    }

    void play() {
        try {
            Log.i(TAG, "Attempted to play " + soundPoolId);
            if (isLooping) {
                streamId = soundPool.play(soundPoolId, 1, 1, 1, -1, 1);
            } else {
                streamId = soundPool.play(soundPoolId, 1, 1, 1, 0, 1);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Sound was not initialized");
            e.getMessage();
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
        } catch (NullPointerException e) {
            Log.e(TAG, "Sound was not initialized");
            e.getMessage();
        }
    }

    void loop() {
        if (this.isLooping == false) {
            // was not looping
            this.isLooping = true;
            play();
        } else {
            // stop looping
            this.isLooping = false;
            stop();
        }
    }

    void load(String path) {
        if (path != null) {
            this.soundPoolId = soundPool.load(path, 1);
            Log.d(TAG, "Sound [" + path + "] length is " + duration + "ms");
        }
    }

    void unload() {
        if (soundPoolId != 0 && soundPool != null) {
            this.soundPool.unload(soundPoolId);
            soundPoolId = 0;
            streamId = 0;
        }
    }

    int getDuration() {
        return duration;
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
