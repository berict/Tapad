package com.bedrock.padder.model.sound;

import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class Sound {

    final String TAG = "Sound";

    boolean isLooping = false;
    boolean isPlaying = false;

    private int playingThreadCount = 0;

    boolean canLoop = true;

    private int soundPoolId = 0;

    private int streamId = 0;

    private int duration = -1;

    private long startTime = -1;

    private SoundPool soundPool;

    private SoundListener listener;

    private Sound sound = this;

    private Handler handler;

    public Sound(SoundPool soundPool, String path, MediaMetadataRetriever mmr) {
        this.soundPool = soundPool;
        this.duration = getDurationFromFile(path, mmr);
        this.load(path);
        handler = new Handler(Looper.getMainLooper());
    }

    public Sound() {
        // empty initializer for null pads
        this.soundPool = null;
        handler = new Handler(Looper.getMainLooper());
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
            if (isLooping && canLoop) {
                canLoop = false;
                streamId = soundPool.play(soundPoolId, 1, 1, 1, -1, 1);
            } else {
                soundPool.play(soundPoolId, 1, 1, 1, 0, 1);
            }

            playingThreadCount++;

            startTime = System.currentTimeMillis();

            if (listener != null) {
                isPlaying = true;
                listener.onSoundStart(sound, playingThreadCount);

                if (isLooping) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSoundLoop(sound);
                            handler.postDelayed(this, getDuration());
                        }
                    }, getDuration());
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isPlaying = false;
                            playingThreadCount--;
                            listener.onSoundEnd(sound, playingThreadCount);
                        }
                    }, getDuration());
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Sound was not initialized");
            e.getMessage();
        }
    }

    void stop() {
        canLoop = true;
        try {
            if (streamId != 0) {
                soundPool.stop(streamId);
            } else {
                throw new NullStreamException();
            }

            // remove loop callbacks
            handler.removeCallbacksAndMessages(null);
            if (listener != null) {
                isPlaying = false;
                playingThreadCount--;
                listener.onSoundStop(this, (int) getCurrentPosition(), getCurrentCompletion());
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

    void loop(boolean isLoop) {
        if (isLoop == true) {
            this.isLooping = true;
            play();
        } else {
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

    long getCurrentPosition() {
        return System.currentTimeMillis() - startTime;
    }

    float getCurrentCompletion() {
        return (int) getCurrentPosition() / (float) duration;
    }

    void setRate(float rate) {
        try {
            if (streamId != 0) {
                soundPool.setRate(streamId, rate);
                // change the duration to the corresponding rate
                duration = (int) (duration / rate);
            } else {
                throw new NullStreamException();
            }
        } catch (NullStreamException e) {
            e.getMessage();
        }
    }

    public void setSoundListener(SoundListener listener) {
        this.listener = listener;
    }

    private class NullStreamException extends Exception {
        NullStreamException() {
            super("streamID is not initialized");
        }
    }

    public interface SoundListener {

        void onSoundStart(Sound sound, int playingThreadCount);

        void onSoundEnd(Sound sound, int playingThreadCount);

        void onSoundStop(Sound sound, int position, float completion);

        void onSoundLoop(Sound sound);
    }
}
