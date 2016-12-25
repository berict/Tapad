package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.VideoView;

import com.bedrock.padder.R;

public class TutorialService extends Activity{

    private AnimService anim = new AnimService();
    private WindowService window = new WindowService();

    public double hello[][] = {
                /* 00 */ {56.87, 184.87, -1},
                /* 01 */ {0, 128, -1},
                /* 02 */ {56.03, -1},
                /* 03 */ {92.57, -1},
                /* 04 */ {220.58, -1},
                /* 05 */ {-1},
                /* 06 */ {-1},
                /* 07 */ {-1},
                /* 08 */ {-1},
                /* 11 */ {20.59, 21.73, 22.88, 24.03, 25.17, 26.31, 27.46, 28.60, 29.74, 30.88, 32.03, 33.18, 34.33, 35.46, 36.61, 37.75, 57.14, 61.72, 66.28, 84.58, 93.71, 98.29, 102.86, 107.43, 148.6, 149.74, 150.89, 152.03, 153.18, 154.32, 155.47, 156.61, 157.75, 158.89, 160.04, 161.18, 162.33, 163.47, 164.62, 165.76, 185.15, 189.72, 194.29, 212.58, 221.73, -1},
                /* 12 */ {58.29, 62.87, 67.43, 85.73, 99.43, 108.58, 186.30, 190.87, 195.44, 213.73, -1},
                /* 13 */ {70.85, 71.13, 89.14, 89.42, 99.72, 108.58, 130.00, 198.86, 199.14,  -1},
                /* 14 */ {5.72, 14.88, 24.03, 33.18, 71.43, 89.72, 94.28, 98.86, 103.43, 108.00, 133.73, 142.88, 152.03, 161.18, 199.43, 217.72, 222.29, -1},
                /* 21 */ {2.29, 6.87, 11.44, 16.02, 20.59, 25.17, 29.74, 34.32, 59.43, 94.86, 104.01, 130.30, 134.87, 129.45, 144.02, 148.60, 153.17, 157.75, 162.32, 187.44, -1},
                /* 22 */ {3.43, 8.00, 12.58, 17.16, 21.73, 26.31, 30.88, 35.46, 60.58, 90.29, 100, 109.14, 131.44, 136.02, 140.59, 145.17, 149.74, 154.32, 158.89, 163.47, 188.58, 200.01, 218.30, -1},
                /* 23 */ {4.58, 9.16, 13.73, 18.31, 22.88, 27.46, 32.30, 36.61, 72.00, 91.43, 100.28, 109.43, 132.59, 137.17, 141.74, 146.32, 150.89, 155.47, 160.04, 164.62, 201.15, 202.28, 219.44, -1},
                /* 24 */ {73.14, 95.14, 104.29, 130.00, -1},
                /* 31 */ {0, 64.01, 95.43, 104.58, 192.02, -1},
                /* 32 */ {0.58, 65.14, 112.00, 121.14, 193.15, -1},
                /* 33 */ {1.14, 75.43, 116.58, 125.73, 203.44, -1},
                /* 34 */ {10.30, 19.45, 28.60, 37.75, 77.71, 95.72, 104.87, 138.31, 147.46, 156.61, 165.76, 205.720, -1},
                /* 41 */ {38.88, 41.16, 43.43, 45.71, 68.58, 86.87, 96.01, 114.29, 166.89, 169.17, 171.44, 173.72, 196.58, 214.88, 222.86, -1},
                /* 42 */ {47.99, 49.16, 50.29, 51.43, 69.71, 88.00, 100.57, 118.85, 176.00, 117.17, 178.30, 179.44, 197.72, 216.01, -1},
                /* 43 */ {52.60, 53.73, 80.00, 81.15, 105.14, 123.43, 180.61, 181.73, 208.01, 209.16, -1},
                /* 44 */ {54.86, 82.30, 83.43, 109.73, 182.87, 210.31, 211.44, 223.72, -1}
    };

    public double roses[][] = {
                /* 00 */ {85.80, 181.78, -1},
                /* 01 */ {0, 143.91, -1},
                /* 02 */ {85.80, 181.78, -1},
                /* 03 */ {124.81, -1},
                /* 04 */ {201.57, -1},
                /* 05 */ {-1},
                /* 06 */ {-1},
                /* 07 */ {-1},
                /* 08 */ {-1},
                /* 11 */ {0, 4.80, 9.60, 14.41, 19.21, 24.01, 28.81, 33.64, 38.41, 43.18, 88.50, 93.30, 98.10, 184.50, 189.30, 194.10, -1},
                /* 12 */ {0.90, 5.70, 10.51, 15.31, 20.11, 24.91, 29.69, 34.54, 39.31, 44.08, 102.90, 112.50, 122.11, 141.30, 198.90, 208.50, -1},
                /* 13 */ {2.40, 7.20, 12.01, 16.81, 21.61, 26.41, 31.19, 36.04, 40.81, 45.58, 105.59, 124.81, 201.57, -1},
                /* 14 */ {3.30, 8.10, 12.91, 17.71, 22.51, 27.31, 32.09, 36.94, 41.71, 46.48, 115.21, 134.54, 211.22, -1},
                /* 21 */ {67.22, 87.30, 92.10, 96.90, 101.70, 130.50, 135.30, 140.14, 163.20, 183.30, 188.10, 192.90, 197.70, 201.57, -1},
                /* 22 */ {47.99, 87.01, 91.86, 96.66, 101.45, 130.21, 135.01, 139.89, 144.00, 183.01, 187.86, 192.66, 197.45, 204.01, -1},
                /* 23 */ {28.80, 96.41, 91.18, 100.77, 129.61, 134.41, 139.210, 182.41, 187.18, 196.77, 206.41, -1},
                /* 24 */ {9.61, 88.81, 89.40, 90.00, 94.20, 94.51, 94.81, 95.10, 98.38, 98.97, 99.61, 103.80, 104.11, 104.41, 104.70, 127.21, 127.80, 128.40, 132.60, 132.91, 133.21, 133.50, 136.80, 137.39, 138.03, 142.20, 142.51, 142.81, 143.10, 184.81, 185.40, 186.00, 190.20, 190.51, 190.81, 191.10, 194.38, 194.97, 195.61, 199.80, 200.11, 200.41, 200.70, 208.82, -1},
                /* 31 */ {88.50, 90.90, 93.29, 98.106, 100.497, 102.893, 129.30, 131.697, 136.527, 138.925, 141.30, 184.50, 186.90, 189.297, 194.106, 196.497, 198.893, 211.21, -1},
                /* 32 */ {79.99, 57.59, 67.218, 76.820, 87.57, 92.377, 97.176, 101.973, 130.777, 135.577, 140.415, 163.20, 172.80, 183.57, 188.377, 193.176, 197.973, 213.60, -1},
                /* 33 */ {50.387, 59.987, 69.615, 79.22, 90.30, 99.90, 128.70, 138.327, 165.597, 175.197, 186.30, 195.90, 216.00, -1},
                /* 34 */ {52.785, 55.19, 62.415, 64.82, 72.015, 74.42, 93.57, 103.17, 131.972, 141.575, 167.997, 170.40, 189.570, 199.17, 218.41, -1},
                /* 41 */ {9.60, 14.90, 19.21, 24.01, 28.81, 33.60, 38.45, 43.220, 86.40, 91.20, 96.00, 100.77, 144.00, 148.81, 153.65, 158.43, 182.40, 187.20, 192.00, 196.77, -1},
                /* 42 */ {28.81, 38.45, 105.59, 110.39, 115.21, 121.01, 124.81, 129.61, 134.41, 139.21, 201.57, 206.39, 211.21, 216.01, -1},
                /* 43 */ {31.205, 40.818, 95.38, 191.38, -1},
                /* 44 */ {33.605, 36.005, 43.218, 45.618, 96.30, 143.38, 192.30, -1}
    };

    Handler tutorial = new Handler();
    public Runnable tutDelay;

    public void tutorialStart(final Activity activity){
        tutorialLoad(activity);
    }

    void tutorialLoad(final Activity activity) {
        Log.i("TutorialService", "tutorial loaded");

        final double array[][];
        SharedPreferences prefs = activity.getSharedPreferences("com.bedrock.padder", activity.MODE_PRIVATE);
        int scheme = prefs.getInt("scheme", 0);

        switch (scheme) {
            case 1:
                array = hello;
                break;
            case 2:
                array = roses;
                break;
            default:
                array = hello;
                break;
        }

        /*
        window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_tutorial);
        anim.fadeIn(R.id.progress_bar_layout, 0, 400, "progressIn", activity);

        anim.fadeOut(R.id.progress_bar_layout, loadingTime + 400, 400, activity);
        */

        Handler startTutorial = new Handler();
        startTutorial.postDelayed(new Runnable() {
            @Override
            public void run() {
                tutorialPlay(array, activity);
            }
        }, 400 + 1000);
    }

    void tutorialPlay(final double array[][], final Activity activity){
        Log.i("TutorialService", "tutorial started");
        window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset);
        for(int count = 0; count < 25; count++){
            int i = 0;
            while(array[count][i] != -1){
                final int btnNum[] = {-1};

                switch (count){
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8: btnNum[0] = count; break;

                    case 9:
                    case 10:
                    case 11:
                    case 12: btnNum[0] = count + 2; break;

                    case 13:
                    case 14:
                    case 15:
                    case 16: btnNum[0] = count + 8; break;

                    case 17:
                    case 18:
                    case 19:
                    case 20: btnNum[0] = count + 14; break;

                    case 21:
                    case 22:
                    case 23:
                    case 24: btnNum[0] = count + 20; break;
                }

                int delay;

                if(count == 1 && i == 0) {
                    delay = (int) (array[count][i]) * 1000;
                } else {
                    delay = (int) (array[count][i]) * 1000 + 200;
                }

                tutorial.postDelayed(tutDelay = new Runnable() {
                    @Override
                    public void run() {
                        motionAnimation(btnNum[0], activity);
                    }
                }, delay);

                i++;
            }
        }
    }

    public void tutorialStop(Activity a) {
        try {
            tutorial.removeCallbacksAndMessages(null);
            window.getImageView(R.id.toolbar_tutorial_icon, a).setImageResource(R.drawable.icon_tutorial);
            window.getImageView(R.id.layout_settings_tutorial_icon, a).setImageResource(R.drawable.settings_tutorial);
            Log.i("TutorialService", "tutorial finished");
        } catch (NullPointerException e){
            Log.e("tutorialService", "NPE, failed to remove callback from handler");
        }
    }

    void motionAnimationDelay(final int buttonNumber, int delay, final Activity activity){
        Handler motionDelay = new Handler();
        motionDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                //motionAnimation(buttonNumber, activity);
            }
        }, delay);
    }

    void motionAnimation(int buttonNumber, Activity activity){
        switch (buttonNumber){
            case 0:  playMotion(R.id.btn00_tutorial, activity); break;

            //Toggle2
            case 1:  playMotion(R.id.tgl1_tutorial , activity); break;
            case 2:  playMotion(R.id.tgl2_tutorial , activity); break;
            case 3:  playMotion(R.id.tgl3_tutorial , activity); break;
            case 4:  playMotion(R.id.tgl4_tutorial , activity); break;
            case 5:  playMotion(R.id.tgl5_tutorial , activity); break;
            case 6:  playMotion(R.id.tgl6_tutorial , activity); break;
            case 7:  playMotion(R.id.tgl7_tutorial , activity); break;
            case 8:  playMotion(R.id.tgl8_tutorial , activity); break;

            //Main
            case 11: playMotion(R.id.btn11_tutorial, activity); break;
            case 12: playMotion(R.id.btn12_tutorial, activity); break;
            case 13: playMotion(R.id.btn13_tutorial, activity); break;
            case 14: playMotion(R.id.btn14_tutorial, activity); break;
            case 21: playMotion(R.id.btn21_tutorial, activity); break;
            case 22: playMotion(R.id.btn22_tutorial, activity); break;
            case 23: playMotion(R.id.btn23_tutorial, activity); break;
            case 24: playMotion(R.id.btn24_tutorial, activity); break;
            case 31: playMotion(R.id.btn31_tutorial, activity); break;
            case 32: playMotion(R.id.btn32_tutorial, activity); break;
            case 33: playMotion(R.id.btn33_tutorial, activity); break;
            case 34: playMotion(R.id.btn34_tutorial, activity); break;
            case 41: playMotion(R.id.btn41_tutorial, activity); break;
            case 42: playMotion(R.id.btn42_tutorial, activity); break;
            case 43: playMotion(R.id.btn43_tutorial, activity); break;
            case 44: playMotion(R.id.btn44_tutorial, activity); break;

            default: Log.i("Turorial Service", "Method called without any matching number"); break;
        }
    }

    void playMotionAnimation(int btnId, int phId, int motionId, Activity activity) {
        VideoView videoView;
        View placeholder;

        videoView = (VideoView)activity.findViewById(btnId);
        placeholder = (View)activity.findViewById(phId);
        animator(videoView, placeholder, motionId, activity);
        Log.i("MotionAnimation", "Animation played on " + activity.getResources().getResourceEntryName(phId));
    }

    ScaleAnimation btn00 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn01 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn02 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn03 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn04 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn05 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn06 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn07 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn08 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn11 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn12 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn13 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn14 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn21 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn22 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn23 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn24 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn31 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn32 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn33 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn34 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn41 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn42 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn43 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn44 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    ScaleAnimation scaleAnimations[] = {
            btn00,
            btn01,
            btn02,
            btn03,
            btn04,
            btn05,
            btn06,
            btn07,
            btn08,
            btn11,
            btn12,
            btn13,
            btn14,
            btn21,
            btn22,
            btn23,
            btn24,
            btn31,
            btn32,
            btn33,
            btn34,
            btn41,
            btn42,
            btn43,
            btn44
    };

    //TODO: add swipe tutorials

    public void playMotion(int viewId, Activity activity) {
        // Animator
        final View view = (View) activity.findViewById(viewId);

        switch (viewId){
            case R.id.btn00_tutorial: playAnimation(view, scaleAnimations[0] ); break;
            case R.id.tgl1_tutorial : playAnimation(view, scaleAnimations[1] ); break;
            case R.id.tgl2_tutorial : playAnimation(view, scaleAnimations[2] ); break;
            case R.id.tgl3_tutorial : playAnimation(view, scaleAnimations[3] ); break;
            case R.id.tgl4_tutorial : playAnimation(view, scaleAnimations[4] ); break;
            case R.id.tgl5_tutorial : playAnimation(view, scaleAnimations[5] ); break;
            case R.id.tgl6_tutorial : playAnimation(view, scaleAnimations[6] ); break;
            case R.id.tgl7_tutorial : playAnimation(view, scaleAnimations[7] ); break;
            case R.id.tgl8_tutorial : playAnimation(view, scaleAnimations[8] ); break;
            case R.id.btn11_tutorial: playAnimation(view, scaleAnimations[9] ); break;
            case R.id.btn12_tutorial: playAnimation(view, scaleAnimations[10]); break;
            case R.id.btn13_tutorial: playAnimation(view, scaleAnimations[11]); break;
            case R.id.btn14_tutorial: playAnimation(view, scaleAnimations[12]); break;
            case R.id.btn21_tutorial: playAnimation(view, scaleAnimations[13]); break;
            case R.id.btn22_tutorial: playAnimation(view, scaleAnimations[14]); break;
            case R.id.btn23_tutorial: playAnimation(view, scaleAnimations[15]); break;
            case R.id.btn24_tutorial: playAnimation(view, scaleAnimations[16]); break;
            case R.id.btn31_tutorial: playAnimation(view, scaleAnimations[17]); break;
            case R.id.btn32_tutorial: playAnimation(view, scaleAnimations[18]); break;
            case R.id.btn33_tutorial: playAnimation(view, scaleAnimations[19]); break;
            case R.id.btn34_tutorial: playAnimation(view, scaleAnimations[20]); break;
            case R.id.btn41_tutorial: playAnimation(view, scaleAnimations[21]); break;
            case R.id.btn42_tutorial: playAnimation(view, scaleAnimations[22]); break;
            case R.id.btn43_tutorial: playAnimation(view, scaleAnimations[23]); break;
            case R.id.btn44_tutorial: playAnimation(view, scaleAnimations[24]); break;

            default: Log.i("Turorial Service", "Method called without any matching number"); break;
        }
    }

    void playAnimation(final View view, ScaleAnimation animation) {
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(300);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                anim.fadeOut(view, 0, 200);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    void animator(final VideoView videoView, final View placeholder, int rawId, Activity activity){
         final int[] state = {0};

        /**
         * 0 ; Ready
         * 1 : Playing
         * 0 : Finished */

        String uri = "android.resource://" + "com.bedrock.padder/";

        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(Uri.parse(uri + rawId));
        Log.i("Animator", activity.getResources().getResourceEntryName(rawId) + " loaded");

        videoView.seekTo(0);
        videoView.start();
        state[0] = 1;
        Log.i("Animator", "Animation started - Animator");

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("Animator", "Animation started - OnPreparedListner");
                if(state[0] == 1){
                    placeholder.setVisibility(View.GONE);
                } else{
                    placeholder.setVisibility(View.VISIBLE);
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                placeholder.setVisibility(View.VISIBLE);
                Log.i("Animator", "Animation finished - OnCompletionListner");
                state[0] = 0;
            }
        });
    }
}