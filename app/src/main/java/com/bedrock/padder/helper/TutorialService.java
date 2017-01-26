package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.VideoView;

import com.bedrock.padder.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TutorialService extends Activity{

    private AnimService anim = new AnimService();
    private WindowService window = new WindowService();

//    public double hello[][] = {
//                /* 00 */ {56.867, 184.875, -1},
//                /* 01 */ {0, 128, -1},
//                /* 02 */ {56.03, -1},
//                /* 03 */ {92.57, -1},
//                /* 04 */ {220.58, -1},
//                /* 05 */ {-1},
//                /* 06 */ {-1},
//                /* 07 */ {-1},
//                /* 08 */ {-1},
//                /* 11 */ {20.59, 21.73, 22.88, 24.03, 25.17, 26.31, 27.46, 28.60, 29.74, 30.88, 32.03, 33.18, 34.33, 35.46, 36.61, 37.75, 57.14, 61.72, 66.28, 84.58, 93.71, 98.29, 102.86, 107.43, 148.6, 149.74, 150.89, 152.03, 153.18, 154.32, 155.47, 156.61, 157.75, 158.89, 160.04, 161.18, 162.33, 163.47, 164.62, 165.76, 185.15, 189.72, 194.29, 212.58, 221.73, -1},
//                /* 12 */ {58.29, 62.87, 67.43, 85.73, 99.43, 108.58, 186.30, 190.87, 195.44, 213.73, -1},
//                /* 13 */ {70.85, 71.13, 89.14, 89.42, 99.72, 108.58, 130.00, 198.86, 199.14,  -1},
//                /* 14 */ {5.72, 14.88, 24.03, 33.18, 71.43, 89.72, 94.28, 98.86, 103.43, 108.00, 133.73, 142.88, 152.03, 161.18, 199.43, 217.72, 222.29, -1},
//                /* 21 */ {2.29, 6.87, 11.44, 16.02, 20.59, 25.17, 29.74, 34.32, 59.43, 94.86, 104.01, 130.30, 134.87, 129.45, 144.02, 148.60, 153.17, 157.75, 162.32, 187.44, -1},
//                /* 22 */ {3.43, 8.00, 12.58, 17.16, 21.73, 26.31, 30.88, 35.46, 60.58, 90.29, 100, 109.14, 131.44, 136.02, 140.59, 145.17, 149.74, 154.32, 158.89, 163.47, 188.58, 200.01, 218.30, -1},
//                /* 23 */ {4.58, 9.16, 13.73, 18.31, 22.88, 27.46, 32.30, 36.61, 72.00, 91.43, 100.28, 109.43, 132.59, 137.17, 141.74, 146.32, 150.89, 155.47, 160.04, 164.62, 201.15, 202.28, 219.44, -1},
//                /* 24 */ {73.14, 95.14, 104.29, 130.00, -1},
//                /* 31 */ {0, 64.01, 95.43, 104.58, 192.02, -1},
//                /* 32 */ {0.58, 65.14, 112.00, 121.14, 193.15, -1},
//                /* 33 */ {1.14, 75.43, 116.58, 125.73, 203.44, -1},
//                /* 34 */ {10.30, 19.45, 28.60, 37.75, 77.71, 95.72, 104.87, 138.31, 147.46, 156.61, 165.76, 205.720, -1},
//                /* 41 */ {38.88, 41.16, 43.43, 45.71, 68.58, 86.87, 96.01, 114.29, 166.89, 169.17, 171.44, 173.72, 196.58, 214.88, 222.86, -1},
//                /* 42 */ {47.99, 49.16, 50.29, 51.43, 69.71, 88.00, 100.57, 118.85, 176.00, 117.17, 178.30, 179.44, 197.72, 216.01, -1},
//                /* 43 */ {52.60, 53.73, 80.00, 81.15, 105.14, 123.43, 180.61, 181.73, 208.01, 209.16, -1},
//                /* 44 */ {54.86, 82.30, 83.43, 109.73, 182.87, 210.31, 211.44, 223.72, -1}
//    };

    public double hello[][] = {
                /* 00 */ {56.867, 184.875, -1},
                /* 01 */ {0, 128, -1},
                /* 02 */ {56.03, -1},
                /* 03 */ {92.57, -1},
                /* 04 */ {220.58, -1},
                /* 05 */ {-1},
                /* 06 */ {-1},
                /* 07 */ {-1},
                /* 08 */ {-1},
                /* 11 */ {20.592, 21.167, 21.733, 22.308, 22.883, 23.458, 24.025, 24.600, 25.167, 25.742, 26.308, 26.883, 27.458, 28.033, 28.600, 29.175, 29.742, 30.317, 30.883, 31.458, 32.033, 32.608, 33.175, 33.750, 34.317, 34.892, 35.458, 36.033, 36.608, 37.183, 37.750, 38.325, 57.142, 61.717, 66.283, 84.575, 93.708, 98.292, 102.858, 107.433, 148.600, 149.175, 149.742, 150.317, 150.892, 151.467, 152.033, 152.608, 153.175, 153.750, 154.317, 154.892, 155.467, 156.042, 156.608, 157.183, 157.750, 158.325, 158.892, 159.467, 160.042, 160.617, 161.183, 161.758, 162.325, 162.900, 163.467, 164.042, 164.617, 165.192, 165.758, 166.333, 185.150, 189.725, 194.292, 212.583, 221.725, -1},
                /* 12 */ {58.292, 62.867, 67.433, 85.725, 99.433, 108.575, 186.300, 190.875, 195.442, 213.733, -1},
                /* 13 */ {70.850, 71.133, 89.142, 89.425, 99.717, 108.858, 130.000, 198.858, 199.142, 217.150, -1},
                /* 14 */ {5.717, 14.875, 24.025, 33.175, 71.425, 89.717, 94.283, 98.858, 103.433, 108.000, 133.725, 142.883, 152.033, 161.183, 199.433, 217.725, 222.292, -1},
                /* 21 */ {2.292, 6.867, 11.442, 16.017, 20.592, 25.167, 29.742, 34.317, 59.433, 94.858, 104.008, 130.300, 130.300, 134.875, 139.450, 144.025, 148.600, 153.175, 157.750, 162.325, 187.442, -1},
                /* 22 */ {3.433, 8.008, 12.583, 17.158, 21.733, 26.308, 30.883, 35.458, 60.575, 90.292, 100.000, 109.142, 131.442, 136.017, 140.592, 145.167, 149.742, 154.317, 158.892, 163.467, 188.583, 200.008, 218.300, -1},
                /* 23 */ {4.583, 9.158, 13.733, 18.308, 22.883, 27.458, 32.033, 36.608, 72.000, 91.433, 100.283, 109.425, 132.592, 137.167, 141.742, 146.317, 150.892, 155.467, 160.042, 164.617, 201.150, 219.442, -1},
                /* 24 */ {73.142, 74.275, 95.142, 104.292, 130.000, -1},
                /* 31 */ {0.000, 64.008, 95.425, 104.575, 192.017, -1},
                /* 32 */ {0.575, 65.142, 112.000, 121.142, 193.150, -1},
                /* 33 */ {1.142, 75.433, 116.575, 125.725, 203.442, -1},
                /* 34 */ {10.300, 19.450, 28.600, 37.750, 77.708, 78.842, 95.717, 104.867, 138.308, 147.458, 156.608, 165.758, 205.717, -1},
                /* 41 */ {38.883, 41.158, 43.433, 45.717, 68.575, 86.867, 96.008, 114.292, 166.892, 169.167, 171.442, 173.725, 196.583, 214.875, 222.858, -1},
                /* 42 */ {47.992, 49.158, 50.292, 51.433, 69.708, 88.000, 100.567, 118.850, 176.000, 177.167, 178.300, 179.442, 197.717, 216.008, -1},
                /* 43 */ {52.600, 53.725, 80.000, 81.150, 105.142, 123.433, 180.608, 181.733, 208.008, -1},
                /* 44 */ {54.858, 82.300, 83.433, 109.725, 182.867, 210.308, 223.717,
            -1}
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
        mHandler = new Handler();
        a = activity;
    }

    private int count;
    private int i;

    int countToButtonNumber(int count) {
        int btnNum = -1;
        switch (count) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                btnNum = count;
                break;

            case 9:
            case 10:
            case 11:
            case 12:
                btnNum = count + 2;
                break;

            case 13:
            case 14:
            case 15:
            case 16:
                btnNum = count + 8;
                break;

            case 17:
            case 18:
            case 19:
            case 20:
                btnNum = count + 14;
                break;

            case 21:
            case 22:
            case 23:
            case 24:
                btnNum = count + 20;
                break;
        }
        return btnNum;
    }

    Handler mHandler;
    Activity a;

    // motionDelays
    private int motionDelay00 = 0;
    private int motionDelay01 = 0;
    private int motionDelay02 = 0;
    private int motionDelay03 = 0;
    private int motionDelay04 = 0;
    private int motionDelay05 = 0;
    private int motionDelay06 = 0;
    private int motionDelay07 = 0;
    private int motionDelay08 = 0;
    private int motionDelay11 = 0;
    private int motionDelay12 = 0;
    private int motionDelay13 = 0;
    private int motionDelay14 = 0;
    private int motionDelay21 = 0;
    private int motionDelay22 = 0;
    private int motionDelay23 = 0;
    private int motionDelay24 = 0;
    private int motionDelay31 = 0;
    private int motionDelay32 = 0;
    private int motionDelay33 = 0;
    private int motionDelay34 = 0;
    private int motionDelay41 = 0;
    private int motionDelay42 = 0;
    private int motionDelay43 = 0;
    private int motionDelay44 = 0;

    private int motionDelay00_1 = 0;
    private int motionDelay01_1 = 0;
    private int motionDelay02_1 = 0;
    private int motionDelay03_1 = 0;
    private int motionDelay04_1 = 0;
    private int motionDelay05_1 = 0;
    private int motionDelay06_1 = 0;
    private int motionDelay07_1 = 0;
    private int motionDelay08_1 = 0;
    private int motionDelay11_1 = 0;
    private int motionDelay12_1 = 0;
    private int motionDelay13_1 = 0;
    private int motionDelay14_1 = 0;
    private int motionDelay21_1 = 0;
    private int motionDelay22_1 = 0;
    private int motionDelay23_1 = 0;
    private int motionDelay24_1 = 0;
    private int motionDelay31_1 = 0;
    private int motionDelay32_1 = 0;
    private int motionDelay33_1 = 0;
    private int motionDelay34_1 = 0;
    private int motionDelay41_1 = 0;
    private int motionDelay42_1 = 0;
    private int motionDelay43_1 = 0;
    private int motionDelay44_1 = 0;

    private int motionDelay00_2 = 0;
    private int motionDelay01_2 = 0;
    private int motionDelay02_2 = 0;
    private int motionDelay03_2 = 0;
    private int motionDelay04_2 = 0;
    private int motionDelay05_2 = 0;
    private int motionDelay06_2 = 0;
    private int motionDelay07_2 = 0;
    private int motionDelay08_2 = 0;
    private int motionDelay11_2 = 0;
    private int motionDelay12_2 = 0;
    private int motionDelay13_2 = 0;
    private int motionDelay14_2 = 0;
    private int motionDelay21_2 = 0;
    private int motionDelay22_2 = 0;
    private int motionDelay23_2 = 0;
    private int motionDelay24_2 = 0;
    private int motionDelay31_2 = 0;
    private int motionDelay32_2 = 0;
    private int motionDelay33_2 = 0;
    private int motionDelay34_2 = 0;
    private int motionDelay41_2 = 0;
    private int motionDelay42_2 = 0;
    private int motionDelay43_2 = 0;
    private int motionDelay44_2 = 0;

    private int motionDelay00_3 = 0;
    private int motionDelay01_3 = 0;
    private int motionDelay02_3 = 0;
    private int motionDelay03_3 = 0;
    private int motionDelay04_3 = 0;
    private int motionDelay05_3 = 0;
    private int motionDelay06_3 = 0;
    private int motionDelay07_3 = 0;
    private int motionDelay08_3 = 0;
    private int motionDelay11_3 = 0;
    private int motionDelay12_3 = 0;
    private int motionDelay13_3 = 0;
    private int motionDelay14_3 = 0;
    private int motionDelay21_3 = 0;
    private int motionDelay22_3 = 0;
    private int motionDelay23_3 = 0;
    private int motionDelay24_3 = 0;
    private int motionDelay31_3 = 0;
    private int motionDelay32_3 = 0;
    private int motionDelay33_3 = 0;
    private int motionDelay34_3 = 0;
    private int motionDelay41_3 = 0;
    private int motionDelay42_3 = 0;
    private int motionDelay43_3 = 0;
    private int motionDelay44_3 = 0;

    private int motionDelay00_4 = 0;
    private int motionDelay01_4 = 0;
    private int motionDelay02_4 = 0;
    private int motionDelay03_4 = 0;
    private int motionDelay04_4 = 0;
    private int motionDelay05_4 = 0;
    private int motionDelay06_4 = 0;
    private int motionDelay07_4 = 0;
    private int motionDelay08_4 = 0;
    private int motionDelay11_4 = 0;
    private int motionDelay12_4 = 0;
    private int motionDelay13_4 = 0;
    private int motionDelay14_4 = 0;
    private int motionDelay21_4 = 0;
    private int motionDelay22_4 = 0;
    private int motionDelay23_4 = 0;
    private int motionDelay24_4 = 0;
    private int motionDelay31_4 = 0;
    private int motionDelay32_4 = 0;
    private int motionDelay33_4 = 0;
    private int motionDelay34_4 = 0;
    private int motionDelay41_4 = 0;
    private int motionDelay42_4 = 0;
    private int motionDelay43_4 = 0;
    private int motionDelay44_4 = 0;

    int motionDelays[][] = {
            {
                    motionDelay00  ,
                    motionDelay01  ,
                    motionDelay02  ,
                    motionDelay03  ,
                    motionDelay04  ,
                    motionDelay05  ,
                    motionDelay06  ,
                    motionDelay07  ,
                    motionDelay08  ,
                    motionDelay11  ,
                    motionDelay12  ,
                    motionDelay13  ,
                    motionDelay14  ,
                    motionDelay21  ,
                    motionDelay22  ,
                    motionDelay23  ,
                    motionDelay24  ,
                    motionDelay31  ,
                    motionDelay32  ,
                    motionDelay33  ,
                    motionDelay34  ,
                    motionDelay41  ,
                    motionDelay42  ,
                    motionDelay43  ,
                    motionDelay44  
            },
            {
                    motionDelay00_1,
                    motionDelay01_1,
                    motionDelay02_1,
                    motionDelay03_1,
                    motionDelay04_1,
                    motionDelay05_1,
                    motionDelay06_1,
                    motionDelay07_1,
                    motionDelay08_1,
                    motionDelay11_1,
                    motionDelay12_1,
                    motionDelay13_1,
                    motionDelay14_1,
                    motionDelay21_1,
                    motionDelay22_1,
                    motionDelay23_1,
                    motionDelay24_1,
                    motionDelay31_1,
                    motionDelay32_1,
                    motionDelay33_1,
                    motionDelay34_1,
                    motionDelay41_1,
                    motionDelay42_1,
                    motionDelay43_1,
                    motionDelay44_1
            },
            {
                    motionDelay00_2,
                    motionDelay01_2,
                    motionDelay02_2,
                    motionDelay03_2,
                    motionDelay04_2,
                    motionDelay05_2,
                    motionDelay06_2,
                    motionDelay07_2,
                    motionDelay08_2,
                    motionDelay11_2,
                    motionDelay12_2,
                    motionDelay13_2,
                    motionDelay14_2,
                    motionDelay21_2,
                    motionDelay22_2,
                    motionDelay23_2,
                    motionDelay24_2,
                    motionDelay31_2,
                    motionDelay32_2,
                    motionDelay33_2,
                    motionDelay34_2,
                    motionDelay41_2,
                    motionDelay42_2,
                    motionDelay43_2,
                    motionDelay44_2
            },
            {
                    motionDelay00_3,
                    motionDelay01_3,
                    motionDelay02_3,
                    motionDelay03_3,
                    motionDelay04_3,
                    motionDelay05_3,
                    motionDelay06_3,
                    motionDelay07_3,
                    motionDelay08_3,
                    motionDelay11_3,
                    motionDelay12_3,
                    motionDelay13_3,
                    motionDelay14_3,
                    motionDelay21_3,
                    motionDelay22_3,
                    motionDelay23_3,
                    motionDelay24_3,
                    motionDelay31_3,
                    motionDelay32_3,
                    motionDelay33_3,
                    motionDelay34_3,
                    motionDelay41_3,
                    motionDelay42_3,
                    motionDelay43_3,
                    motionDelay44_3
            },
            {
                    motionDelay00_4,
                    motionDelay01_4,
                    motionDelay02_4,
                    motionDelay03_4,
                    motionDelay04_4,
                    motionDelay05_4,
                    motionDelay06_4,
                    motionDelay07_4,
                    motionDelay08_4,
                    motionDelay11_4,
                    motionDelay12_4,
                    motionDelay13_4,
                    motionDelay14_4,
                    motionDelay21_4,
                    motionDelay22_4,
                    motionDelay23_4,
                    motionDelay24_4,
                    motionDelay31_4,
                    motionDelay32_4,
                    motionDelay33_4,
                    motionDelay34_4,
                    motionDelay41_4,
                    motionDelay42_4,
                    motionDelay43_4,
                    motionDelay44_4
            }
    };

    // motionDelayIndexes
    private short motionDelayIndex00 = 0;
    private short motionDelayIndex01 = 0;
    private short motionDelayIndex02 = 0;
    private short motionDelayIndex03 = 0;
    private short motionDelayIndex04 = 0;
    private short motionDelayIndex05 = 0;
    private short motionDelayIndex06 = 0;
    private short motionDelayIndex07 = 0;
    private short motionDelayIndex08 = 0;
    private short motionDelayIndex11 = 0;
    private short motionDelayIndex12 = 0;
    private short motionDelayIndex13 = 0;
    private short motionDelayIndex14 = 0;
    private short motionDelayIndex21 = 0;
    private short motionDelayIndex22 = 0;
    private short motionDelayIndex23 = 0;
    private short motionDelayIndex24 = 0;
    private short motionDelayIndex31 = 0;
    private short motionDelayIndex32 = 0;
    private short motionDelayIndex33 = 0;
    private short motionDelayIndex34 = 0;
    private short motionDelayIndex41 = 0;
    private short motionDelayIndex42 = 0;
    private short motionDelayIndex43 = 0;
    private short motionDelayIndex44 = 0;

    private short motionDelayIndex00_1 = 0;
    private short motionDelayIndex01_1 = 0;
    private short motionDelayIndex02_1 = 0;
    private short motionDelayIndex03_1 = 0;
    private short motionDelayIndex04_1 = 0;
    private short motionDelayIndex05_1 = 0;
    private short motionDelayIndex06_1 = 0;
    private short motionDelayIndex07_1 = 0;
    private short motionDelayIndex08_1 = 0;
    private short motionDelayIndex11_1 = 0;
    private short motionDelayIndex12_1 = 0;
    private short motionDelayIndex13_1 = 0;
    private short motionDelayIndex14_1 = 0;
    private short motionDelayIndex21_1 = 0;
    private short motionDelayIndex22_1 = 0;
    private short motionDelayIndex23_1 = 0;
    private short motionDelayIndex24_1 = 0;
    private short motionDelayIndex31_1 = 0;
    private short motionDelayIndex32_1 = 0;
    private short motionDelayIndex33_1 = 0;
    private short motionDelayIndex34_1 = 0;
    private short motionDelayIndex41_1 = 0;
    private short motionDelayIndex42_1 = 0;
    private short motionDelayIndex43_1 = 0;
    private short motionDelayIndex44_1 = 0;

    private short motionDelayIndex00_2 = 0;
    private short motionDelayIndex01_2 = 0;
    private short motionDelayIndex02_2 = 0;
    private short motionDelayIndex03_2 = 0;
    private short motionDelayIndex04_2 = 0;
    private short motionDelayIndex05_2 = 0;
    private short motionDelayIndex06_2 = 0;
    private short motionDelayIndex07_2 = 0;
    private short motionDelayIndex08_2 = 0;
    private short motionDelayIndex11_2 = 0;
    private short motionDelayIndex12_2 = 0;
    private short motionDelayIndex13_2 = 0;
    private short motionDelayIndex14_2 = 0;
    private short motionDelayIndex21_2 = 0;
    private short motionDelayIndex22_2 = 0;
    private short motionDelayIndex23_2 = 0;
    private short motionDelayIndex24_2 = 0;
    private short motionDelayIndex31_2 = 0;
    private short motionDelayIndex32_2 = 0;
    private short motionDelayIndex33_2 = 0;
    private short motionDelayIndex34_2 = 0;
    private short motionDelayIndex41_2 = 0;
    private short motionDelayIndex42_2 = 0;
    private short motionDelayIndex43_2 = 0;
    private short motionDelayIndex44_2 = 0;

    private short motionDelayIndex00_3 = 0;
    private short motionDelayIndex01_3 = 0;
    private short motionDelayIndex02_3 = 0;
    private short motionDelayIndex03_3 = 0;
    private short motionDelayIndex04_3 = 0;
    private short motionDelayIndex05_3 = 0;
    private short motionDelayIndex06_3 = 0;
    private short motionDelayIndex07_3 = 0;
    private short motionDelayIndex08_3 = 0;
    private short motionDelayIndex11_3 = 0;
    private short motionDelayIndex12_3 = 0;
    private short motionDelayIndex13_3 = 0;
    private short motionDelayIndex14_3 = 0;
    private short motionDelayIndex21_3 = 0;
    private short motionDelayIndex22_3 = 0;
    private short motionDelayIndex23_3 = 0;
    private short motionDelayIndex24_3 = 0;
    private short motionDelayIndex31_3 = 0;
    private short motionDelayIndex32_3 = 0;
    private short motionDelayIndex33_3 = 0;
    private short motionDelayIndex34_3 = 0;
    private short motionDelayIndex41_3 = 0;
    private short motionDelayIndex42_3 = 0;
    private short motionDelayIndex43_3 = 0;
    private short motionDelayIndex44_3 = 0;

    private short motionDelayIndex00_4 = 0;
    private short motionDelayIndex01_4 = 0;
    private short motionDelayIndex02_4 = 0;
    private short motionDelayIndex03_4 = 0;
    private short motionDelayIndex04_4 = 0;
    private short motionDelayIndex05_4 = 0;
    private short motionDelayIndex06_4 = 0;
    private short motionDelayIndex07_4 = 0;
    private short motionDelayIndex08_4 = 0;
    private short motionDelayIndex11_4 = 0;
    private short motionDelayIndex12_4 = 0;
    private short motionDelayIndex13_4 = 0;
    private short motionDelayIndex14_4 = 0;
    private short motionDelayIndex21_4 = 0;
    private short motionDelayIndex22_4 = 0;
    private short motionDelayIndex23_4 = 0;
    private short motionDelayIndex24_4 = 0;
    private short motionDelayIndex31_4 = 0;
    private short motionDelayIndex32_4 = 0;
    private short motionDelayIndex33_4 = 0;
    private short motionDelayIndex34_4 = 0;
    private short motionDelayIndex41_4 = 0;
    private short motionDelayIndex42_4 = 0;
    private short motionDelayIndex43_4 = 0;
    private short motionDelayIndex44_4 = 0;
    
    short motionDelayIndexes[][] = {
            {
                    motionDelayIndex00  ,
                    motionDelayIndex01  ,
                    motionDelayIndex02  ,
                    motionDelayIndex03  ,
                    motionDelayIndex04  ,
                    motionDelayIndex05  ,
                    motionDelayIndex06  ,
                    motionDelayIndex07  ,
                    motionDelayIndex08  ,
                    motionDelayIndex11  ,
                    motionDelayIndex12  ,
                    motionDelayIndex13  ,
                    motionDelayIndex14  ,
                    motionDelayIndex21  ,
                    motionDelayIndex22  ,
                    motionDelayIndex23  ,
                    motionDelayIndex24  ,
                    motionDelayIndex31  ,
                    motionDelayIndex32  ,
                    motionDelayIndex33  ,
                    motionDelayIndex34  ,
                    motionDelayIndex41  ,
                    motionDelayIndex42  ,
                    motionDelayIndex43  ,
                    motionDelayIndex44
            },
            {
                    motionDelayIndex00_1,
                    motionDelayIndex01_1,
                    motionDelayIndex02_1,
                    motionDelayIndex03_1,
                    motionDelayIndex04_1,
                    motionDelayIndex05_1,
                    motionDelayIndex06_1,
                    motionDelayIndex07_1,
                    motionDelayIndex08_1,
                    motionDelayIndex11_1,
                    motionDelayIndex12_1,
                    motionDelayIndex13_1,
                    motionDelayIndex14_1,
                    motionDelayIndex21_1,
                    motionDelayIndex22_1,
                    motionDelayIndex23_1,
                    motionDelayIndex24_1,
                    motionDelayIndex31_1,
                    motionDelayIndex32_1,
                    motionDelayIndex33_1,
                    motionDelayIndex34_1,
                    motionDelayIndex41_1,
                    motionDelayIndex42_1,
                    motionDelayIndex43_1,
                    motionDelayIndex44_1
            },
            {
                    motionDelayIndex00_2,
                    motionDelayIndex01_2,
                    motionDelayIndex02_2,
                    motionDelayIndex03_2,
                    motionDelayIndex04_2,
                    motionDelayIndex05_2,
                    motionDelayIndex06_2,
                    motionDelayIndex07_2,
                    motionDelayIndex08_2,
                    motionDelayIndex11_2,
                    motionDelayIndex12_2,
                    motionDelayIndex13_2,
                    motionDelayIndex14_2,
                    motionDelayIndex21_2,
                    motionDelayIndex22_2,
                    motionDelayIndex23_2,
                    motionDelayIndex24_2,
                    motionDelayIndex31_2,
                    motionDelayIndex32_2,
                    motionDelayIndex33_2,
                    motionDelayIndex34_2,
                    motionDelayIndex41_2,
                    motionDelayIndex42_2,
                    motionDelayIndex43_2,
                    motionDelayIndex44_2
            },
            {
                    motionDelayIndex00_3,
                    motionDelayIndex01_3,
                    motionDelayIndex02_3,
                    motionDelayIndex03_3,
                    motionDelayIndex04_3,
                    motionDelayIndex05_3,
                    motionDelayIndex06_3,
                    motionDelayIndex07_3,
                    motionDelayIndex08_3,
                    motionDelayIndex11_3,
                    motionDelayIndex12_3,
                    motionDelayIndex13_3,
                    motionDelayIndex14_3,
                    motionDelayIndex21_3,
                    motionDelayIndex22_3,
                    motionDelayIndex23_3,
                    motionDelayIndex24_3,
                    motionDelayIndex31_3,
                    motionDelayIndex32_3,
                    motionDelayIndex33_3,
                    motionDelayIndex34_3,
                    motionDelayIndex41_3,
                    motionDelayIndex42_3,
                    motionDelayIndex43_3,
                    motionDelayIndex44_3
            },
            {
                    motionDelayIndex00_4,
                    motionDelayIndex01_4,
                    motionDelayIndex02_4,
                    motionDelayIndex03_4,
                    motionDelayIndex04_4,
                    motionDelayIndex05_4,
                    motionDelayIndex06_4,
                    motionDelayIndex07_4,
                    motionDelayIndex08_4,
                    motionDelayIndex11_4,
                    motionDelayIndex12_4,
                    motionDelayIndex13_4,
                    motionDelayIndex14_4,
                    motionDelayIndex21_4,
                    motionDelayIndex22_4,
                    motionDelayIndex23_4,
                    motionDelayIndex24_4,
                    motionDelayIndex31_4,
                    motionDelayIndex32_4,
                    motionDelayIndex33_4,
                    motionDelayIndex34_4,
                    motionDelayIndex41_4,
                    motionDelayIndex42_4,
                    motionDelayIndex43_4,
                    motionDelayIndex44_4
            }
    };

    // motions
    // Normal
    Runnable motion00  ;// = new Runnable() {@Override public void run() {try {motionAnimation(0 , 0, a); if(i < timing.length - 1) { motionDelay00   = (timing[0 ][0][i + 1] - timing[0 ][0][i]); Log.d("mInterval", "Set to " + motionDelay00  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion00  );}} finally { if(motionDelay00   > 0) { mHandler.postDelayed(motion00  , motionDelay00  ); } else {mHandler.removeCallbacks(motion00  );}}}};
    Runnable motion01  ;// = new Runnable() {@Override public void run() {try {motionAnimation(1 , 0, a); if(i < timing.length - 1) { motionDelay01   = (timing[1 ][0][i + 1] - timing[1 ][0][i]); Log.d("mInterval", "Set to " + motionDelay01  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion01  );}} finally { if(motionDelay01   > 0) { mHandler.postDelayed(motion01  , motionDelay01  ); } else {mHandler.removeCallbacks(motion01  );}}}};
    Runnable motion02  ;// = new Runnable() {@Override public void run() {try {motionAnimation(2 , 0, a); if(i < timing.length - 1) { motionDelay02   = (timing[2 ][0][i + 1] - timing[2 ][0][i]); Log.d("mInterval", "Set to " + motionDelay02  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion02  );}} finally { if(motionDelay02   > 0) { mHandler.postDelayed(motion02  , motionDelay02  ); } else {mHandler.removeCallbacks(motion02  );}}}};
    Runnable motion03  ;// = new Runnable() {@Override public void run() {try {motionAnimation(3 , 0, a); if(i < timing.length - 1) { motionDelay03   = (timing[3 ][0][i + 1] - timing[3 ][0][i]); Log.d("mInterval", "Set to " + motionDelay03  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion03  );}} finally { if(motionDelay03   > 0) { mHandler.postDelayed(motion03  , motionDelay03  ); } else {mHandler.removeCallbacks(motion03  );}}}};
    Runnable motion04  ;// = new Runnable() {@Override public void run() {try {motionAnimation(4 , 0, a); if(i < timing.length - 1) { motionDelay04   = (timing[4 ][0][i + 1] - timing[4 ][0][i]); Log.d("mInterval", "Set to " + motionDelay04  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion04  );}} finally { if(motionDelay04   > 0) { mHandler.postDelayed(motion04  , motionDelay04  ); } else {mHandler.removeCallbacks(motion04  );}}}};
    Runnable motion05  ;// = new Runnable() {@Override public void run() {try {motionAnimation(5 , 0, a); if(i < timing.length - 1) { motionDelay05   = (timing[5 ][0][i + 1] - timing[5 ][0][i]); Log.d("mInterval", "Set to " + motionDelay05  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion05  );}} finally { if(motionDelay05   > 0) { mHandler.postDelayed(motion05  , motionDelay05  ); } else {mHandler.removeCallbacks(motion05  );}}}};
    Runnable motion06  ;// = new Runnable() {@Override public void run() {try {motionAnimation(6 , 0, a); if(i < timing.length - 1) { motionDelay06   = (timing[6 ][0][i + 1] - timing[6 ][0][i]); Log.d("mInterval", "Set to " + motionDelay06  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion06  );}} finally { if(motionDelay06   > 0) { mHandler.postDelayed(motion06  , motionDelay06  ); } else {mHandler.removeCallbacks(motion06  );}}}};
    Runnable motion07  ;// = new Runnable() {@Override public void run() {try {motionAnimation(7 , 0, a); if(i < timing.length - 1) { motionDelay07   = (timing[7 ][0][i + 1] - timing[7 ][0][i]); Log.d("mInterval", "Set to " + motionDelay07  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion07  );}} finally { if(motionDelay07   > 0) { mHandler.postDelayed(motion07  , motionDelay07  ); } else {mHandler.removeCallbacks(motion07  );}}}};
    Runnable motion08  ;// = new Runnable() {@Override public void run() {try {motionAnimation(8 , 0, a); if(i < timing.length - 1) { motionDelay08   = (timing[8 ][0][i + 1] - timing[8 ][0][i]); Log.d("mInterval", "Set to " + motionDelay08  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion08  );}} finally { if(motionDelay08   > 0) { mHandler.postDelayed(motion08  , motionDelay08  ); } else {mHandler.removeCallbacks(motion08  );}}}};
    Runnable motion11  ;// = new Runnable() {@Override public void run() {try {motionAnimation(9 , 0, a); if(i < timing.length - 1) { motionDelay11   = (timing[9 ][0][i + 1] - timing[9 ][0][i]); Log.d("mInterval", "Set to " + motionDelay11  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion11  );}} finally { if(motionDelay11   > 0) { mHandler.postDelayed(motion11  , motionDelay11  ); } else {mHandler.removeCallbacks(motion11  );}}}};
    Runnable motion12  ;// = new Runnable() {@Override public void run() {try {motionAnimation(10, 0, a); if(i < timing.length - 1) { motionDelay12   = (timing[10][0][i + 1] - timing[10][0][i]); Log.d("mInterval", "Set to " + motionDelay12  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion12  );}} finally { if(motionDelay12   > 0) { mHandler.postDelayed(motion12  , motionDelay12  ); } else {mHandler.removeCallbacks(motion12  );}}}};
    Runnable motion13  ;// = new Runnable() {@Override public void run() {try {motionAnimation(11, 0, a); if(i < timing.length - 1) { motionDelay13   = (timing[11][0][i + 1] - timing[11][0][i]); Log.d("mInterval", "Set to " + motionDelay13  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion13  );}} finally { if(motionDelay13   > 0) { mHandler.postDelayed(motion13  , motionDelay13  ); } else {mHandler.removeCallbacks(motion13  );}}}};
    Runnable motion14  ;// = new Runnable() {@Override public void run() {try {motionAnimation(12, 0, a); if(i < timing.length - 1) { motionDelay14   = (timing[12][0][i + 1] - timing[12][0][i]); Log.d("mInterval", "Set to " + motionDelay14  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion14  );}} finally { if(motionDelay14   > 0) { mHandler.postDelayed(motion14  , motionDelay14  ); } else {mHandler.removeCallbacks(motion14  );}}}};
    Runnable motion21  ;// = new Runnable() {@Override public void run() {try {motionAnimation(13, 0, a); if(i < timing.length - 1) { motionDelay21   = (timing[13][0][i + 1] - timing[13][0][i]); Log.d("mInterval", "Set to " + motionDelay21  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion21  );}} finally { if(motionDelay21   > 0) { mHandler.postDelayed(motion21  , motionDelay21  ); } else {mHandler.removeCallbacks(motion21  );}}}};
    Runnable motion22  ;// = new Runnable() {@Override public void run() {try {motionAnimation(14, 0, a); if(i < timing.length - 1) { motionDelay22   = (timing[14][0][i + 1] - timing[14][0][i]); Log.d("mInterval", "Set to " + motionDelay22  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion22  );}} finally { if(motionDelay22   > 0) { mHandler.postDelayed(motion22  , motionDelay22  ); } else {mHandler.removeCallbacks(motion22  );}}}};
    Runnable motion23  ;// = new Runnable() {@Override public void run() {try {motionAnimation(15, 0, a); if(i < timing.length - 1) { motionDelay23   = (timing[15][0][i + 1] - timing[15][0][i]); Log.d("mInterval", "Set to " + motionDelay23  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion23  );}} finally { if(motionDelay23   > 0) { mHandler.postDelayed(motion23  , motionDelay23  ); } else {mHandler.removeCallbacks(motion23  );}}}};
    Runnable motion24  ;// = new Runnable() {@Override public void run() {try {motionAnimation(16, 0, a); if(i < timing.length - 1) { motionDelay24   = (timing[16][0][i + 1] - timing[16][0][i]); Log.d("mInterval", "Set to " + motionDelay24  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion24  );}} finally { if(motionDelay24   > 0) { mHandler.postDelayed(motion24  , motionDelay24  ); } else {mHandler.removeCallbacks(motion24  );}}}};
    Runnable motion31  ;// = new Runnable() {@Override public void run() {try {motionAnimation(17, 0, a); if(i < timing.length - 1) { motionDelay31   = (timing[17][0][i + 1] - timing[17][0][i]); Log.d("mInterval", "Set to " + motionDelay31  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion31  );}} finally { if(motionDelay31   > 0) { mHandler.postDelayed(motion31  , motionDelay31  ); } else {mHandler.removeCallbacks(motion31  );}}}};
    Runnable motion32  ;// = new Runnable() {@Override public void run() {try {motionAnimation(18, 0, a); if(i < timing.length - 1) { motionDelay32   = (timing[18][0][i + 1] - timing[18][0][i]); Log.d("mInterval", "Set to " + motionDelay32  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion32  );}} finally { if(motionDelay32   > 0) { mHandler.postDelayed(motion32  , motionDelay32  ); } else {mHandler.removeCallbacks(motion32  );}}}};
    Runnable motion33  ;// = new Runnable() {@Override public void run() {try {motionAnimation(19, 0, a); if(i < timing.length - 1) { motionDelay33   = (timing[19][0][i + 1] - timing[19][0][i]); Log.d("mInterval", "Set to " + motionDelay33  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion33  );}} finally { if(motionDelay33   > 0) { mHandler.postDelayed(motion33  , motionDelay33  ); } else {mHandler.removeCallbacks(motion33  );}}}};
    Runnable motion34  ;// = new Runnable() {@Override public void run() {try {motionAnimation(20, 0, a); if(i < timing.length - 1) { motionDelay34   = (timing[20][0][i + 1] - timing[20][0][i]); Log.d("mInterval", "Set to " + motionDelay34  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion34  );}} finally { if(motionDelay34   > 0) { mHandler.postDelayed(motion34  , motionDelay34  ); } else {mHandler.removeCallbacks(motion34  );}}}};
    Runnable motion41  ;// = new Runnable() {@Override public void run() {try {motionAnimation(21, 0, a); if(i < timing.length - 1) { motionDelay41   = (timing[21][0][i + 1] - timing[21][0][i]); Log.d("mInterval", "Set to " + motionDelay41  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion41  );}} finally { if(motionDelay41   > 0) { mHandler.postDelayed(motion41  , motionDelay41  ); } else {mHandler.removeCallbacks(motion41  );}}}};
    Runnable motion42  ;// = new Runnable() {@Override public void run() {try {motionAnimation(22, 0, a); if(i < timing.length - 1) { motionDelay42   = (timing[22][0][i + 1] - timing[22][0][i]); Log.d("mInterval", "Set to " + motionDelay42  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion42  );}} finally { if(motionDelay42   > 0) { mHandler.postDelayed(motion42  , motionDelay42  ); } else {mHandler.removeCallbacks(motion42  );}}}};
    Runnable motion43  ;// = new Runnable() {@Override public void run() {try {motionAnimation(23, 0, a); if(i < timing.length - 1) { motionDelay43   = (timing[23][0][i + 1] - timing[23][0][i]); Log.d("mInterval", "Set to " + motionDelay43  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion43  );}} finally { if(motionDelay43   > 0) { mHandler.postDelayed(motion43  , motionDelay43  ); } else {mHandler.removeCallbacks(motion43  );}}}};
    Runnable motion44  ;// = new Runnable() {@Override public void run() {try {motionAnimation(24, 0, a); if(i < timing.length - 1) { motionDelay44   = (timing[24][0][i + 1] - timing[24][0][i]); Log.d("mInterval", "Set to " + motionDelay44  ); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion44  );}} finally { if(motionDelay44   > 0) { mHandler.postDelayed(motion44  , motionDelay44  ); } else {mHandler.removeCallbacks(motion44  );}}}};

    // Up
    Runnable motion00_1;// = new Runnable() {@Override public void run() {try {motionAnimation(0 , 1, a); if(i < timing.length - 1) { motionDelay00_1 = (timing[0 ][1][i + 1] - timing[0 ][1][i]); Log.d("mInterval", "Set to " + motionDelay00_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion00_1);}} finally { if(motionDelay00_1 > 0) { mHandler.postDelayed(motion00_1, motionDelay00_1); } else {mHandler.removeCallbacks(motion00_1);}}}};
    Runnable motion01_1;// = new Runnable() {@Override public void run() {try {motionAnimation(1 , 1, a); if(i < timing.length - 1) { motionDelay01_1 = (timing[1 ][1][i + 1] - timing[1 ][1][i]); Log.d("mInterval", "Set to " + motionDelay01_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion01_1);}} finally { if(motionDelay01_1 > 0) { mHandler.postDelayed(motion01_1, motionDelay01_1); } else {mHandler.removeCallbacks(motion01_1);}}}};
    Runnable motion02_1;// = new Runnable() {@Override public void run() {try {motionAnimation(2 , 1, a); if(i < timing.length - 1) { motionDelay02_1 = (timing[2 ][1][i + 1] - timing[2 ][1][i]); Log.d("mInterval", "Set to " + motionDelay02_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion02_1);}} finally { if(motionDelay02_1 > 0) { mHandler.postDelayed(motion02_1, motionDelay02_1); } else {mHandler.removeCallbacks(motion02_1);}}}};
    Runnable motion03_1;// = new Runnable() {@Override public void run() {try {motionAnimation(3 , 1, a); if(i < timing.length - 1) { motionDelay03_1 = (timing[3 ][1][i + 1] - timing[3 ][1][i]); Log.d("mInterval", "Set to " + motionDelay03_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion03_1);}} finally { if(motionDelay03_1 > 0) { mHandler.postDelayed(motion03_1, motionDelay03_1); } else {mHandler.removeCallbacks(motion03_1);}}}};
    Runnable motion04_1;// = new Runnable() {@Override public void run() {try {motionAnimation(4 , 1, a); if(i < timing.length - 1) { motionDelay04_1 = (timing[4 ][1][i + 1] - timing[4 ][1][i]); Log.d("mInterval", "Set to " + motionDelay04_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion04_1);}} finally { if(motionDelay04_1 > 0) { mHandler.postDelayed(motion04_1, motionDelay04_1); } else {mHandler.removeCallbacks(motion04_1);}}}};
    Runnable motion05_1;// = new Runnable() {@Override public void run() {try {motionAnimation(5 , 1, a); if(i < timing.length - 1) { motionDelay05_1 = (timing[5 ][1][i + 1] - timing[5 ][1][i]); Log.d("mInterval", "Set to " + motionDelay05_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion05_1);}} finally { if(motionDelay05_1 > 0) { mHandler.postDelayed(motion05_1, motionDelay05_1); } else {mHandler.removeCallbacks(motion05_1);}}}};
    Runnable motion06_1;// = new Runnable() {@Override public void run() {try {motionAnimation(6 , 1, a); if(i < timing.length - 1) { motionDelay06_1 = (timing[6 ][1][i + 1] - timing[6 ][1][i]); Log.d("mInterval", "Set to " + motionDelay06_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion06_1);}} finally { if(motionDelay06_1 > 0) { mHandler.postDelayed(motion06_1, motionDelay06_1); } else {mHandler.removeCallbacks(motion06_1);}}}};
    Runnable motion07_1;// = new Runnable() {@Override public void run() {try {motionAnimation(7 , 1, a); if(i < timing.length - 1) { motionDelay07_1 = (timing[7 ][1][i + 1] - timing[7 ][1][i]); Log.d("mInterval", "Set to " + motionDelay07_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion07_1);}} finally { if(motionDelay07_1 > 0) { mHandler.postDelayed(motion07_1, motionDelay07_1); } else {mHandler.removeCallbacks(motion07_1);}}}};
    Runnable motion08_1;// = new Runnable() {@Override public void run() {try {motionAnimation(8 , 1, a); if(i < timing.length - 1) { motionDelay08_1 = (timing[8 ][1][i + 1] - timing[8 ][1][i]); Log.d("mInterval", "Set to " + motionDelay08_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion08_1);}} finally { if(motionDelay08_1 > 0) { mHandler.postDelayed(motion08_1, motionDelay08_1); } else {mHandler.removeCallbacks(motion08_1);}}}};
    Runnable motion11_1;// = new Runnable() {@Override public void run() {try {motionAnimation(9 , 1, a); if(i < timing.length - 1) { motionDelay11_1 = (timing[9 ][1][i + 1] - timing[9 ][1][i]); Log.d("mInterval", "Set to " + motionDelay11_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion11_1);}} finally { if(motionDelay11_1 > 0) { mHandler.postDelayed(motion11_1, motionDelay11_1); } else {mHandler.removeCallbacks(motion11_1);}}}};
    Runnable motion12_1;// = new Runnable() {@Override public void run() {try {motionAnimation(10, 1, a); if(i < timing.length - 1) { motionDelay12_1 = (timing[10][1][i + 1] - timing[10][1][i]); Log.d("mInterval", "Set to " + motionDelay12_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion12_1);}} finally { if(motionDelay12_1 > 0) { mHandler.postDelayed(motion12_1, motionDelay12_1); } else {mHandler.removeCallbacks(motion12_1);}}}};
    Runnable motion13_1;// = new Runnable() {@Override public void run() {try {motionAnimation(11, 1, a); if(i < timing.length - 1) { motionDelay13_1 = (timing[11][1][i + 1] - timing[11][1][i]); Log.d("mInterval", "Set to " + motionDelay13_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion13_1);}} finally { if(motionDelay13_1 > 0) { mHandler.postDelayed(motion13_1, motionDelay13_1); } else {mHandler.removeCallbacks(motion13_1);}}}};
    Runnable motion14_1;// = new Runnable() {@Override public void run() {try {motionAnimation(12, 1, a); if(i < timing.length - 1) { motionDelay14_1 = (timing[12][1][i + 1] - timing[12][1][i]); Log.d("mInterval", "Set to " + motionDelay14_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion14_1);}} finally { if(motionDelay14_1 > 0) { mHandler.postDelayed(motion14_1, motionDelay14_1); } else {mHandler.removeCallbacks(motion14_1);}}}};
    Runnable motion21_1;// = new Runnable() {@Override public void run() {try {motionAnimation(13, 1, a); if(i < timing.length - 1) { motionDelay21_1 = (timing[13][1][i + 1] - timing[13][1][i]); Log.d("mInterval", "Set to " + motionDelay21_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion21_1);}} finally { if(motionDelay21_1 > 0) { mHandler.postDelayed(motion21_1, motionDelay21_1); } else {mHandler.removeCallbacks(motion21_1);}}}};
    Runnable motion22_1;// = new Runnable() {@Override public void run() {try {motionAnimation(14, 1, a); if(i < timing.length - 1) { motionDelay22_1 = (timing[14][1][i + 1] - timing[14][1][i]); Log.d("mInterval", "Set to " + motionDelay22_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion22_1);}} finally { if(motionDelay22_1 > 0) { mHandler.postDelayed(motion22_1, motionDelay22_1); } else {mHandler.removeCallbacks(motion22_1);}}}};
    Runnable motion23_1;// = new Runnable() {@Override public void run() {try {motionAnimation(15, 1, a); if(i < timing.length - 1) { motionDelay23_1 = (timing[15][1][i + 1] - timing[15][1][i]); Log.d("mInterval", "Set to " + motionDelay23_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion23_1);}} finally { if(motionDelay23_1 > 0) { mHandler.postDelayed(motion23_1, motionDelay23_1); } else {mHandler.removeCallbacks(motion23_1);}}}};
    Runnable motion24_1;// = new Runnable() {@Override public void run() {try {motionAnimation(16, 1, a); if(i < timing.length - 1) { motionDelay24_1 = (timing[16][1][i + 1] - timing[16][1][i]); Log.d("mInterval", "Set to " + motionDelay24_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion24_1);}} finally { if(motionDelay24_1 > 0) { mHandler.postDelayed(motion24_1, motionDelay24_1); } else {mHandler.removeCallbacks(motion24_1);}}}};
    Runnable motion31_1;// = new Runnable() {@Override public void run() {try {motionAnimation(17, 1, a); if(i < timing.length - 1) { motionDelay31_1 = (timing[17][1][i + 1] - timing[17][1][i]); Log.d("mInterval", "Set to " + motionDelay31_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion31_1);}} finally { if(motionDelay31_1 > 0) { mHandler.postDelayed(motion31_1, motionDelay31_1); } else {mHandler.removeCallbacks(motion31_1);}}}};
    Runnable motion32_1;// = new Runnable() {@Override public void run() {try {motionAnimation(18, 1, a); if(i < timing.length - 1) { motionDelay32_1 = (timing[18][1][i + 1] - timing[18][1][i]); Log.d("mInterval", "Set to " + motionDelay32_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion32_1);}} finally { if(motionDelay32_1 > 0) { mHandler.postDelayed(motion32_1, motionDelay32_1); } else {mHandler.removeCallbacks(motion32_1);}}}};
    Runnable motion33_1;// = new Runnable() {@Override public void run() {try {motionAnimation(19, 1, a); if(i < timing.length - 1) { motionDelay33_1 = (timing[19][1][i + 1] - timing[19][1][i]); Log.d("mInterval", "Set to " + motionDelay33_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion33_1);}} finally { if(motionDelay33_1 > 0) { mHandler.postDelayed(motion33_1, motionDelay33_1); } else {mHandler.removeCallbacks(motion33_1);}}}};
    Runnable motion34_1;// = new Runnable() {@Override public void run() {try {motionAnimation(20, 1, a); if(i < timing.length - 1) { motionDelay34_1 = (timing[20][1][i + 1] - timing[20][1][i]); Log.d("mInterval", "Set to " + motionDelay34_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion34_1);}} finally { if(motionDelay34_1 > 0) { mHandler.postDelayed(motion34_1, motionDelay34_1); } else {mHandler.removeCallbacks(motion34_1);}}}};
    Runnable motion41_1;// = new Runnable() {@Override public void run() {try {motionAnimation(21, 1, a); if(i < timing.length - 1) { motionDelay41_1 = (timing[21][1][i + 1] - timing[21][1][i]); Log.d("mInterval", "Set to " + motionDelay41_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion41_1);}} finally { if(motionDelay41_1 > 0) { mHandler.postDelayed(motion41_1, motionDelay41_1); } else {mHandler.removeCallbacks(motion41_1);}}}};
    Runnable motion42_1;// = new Runnable() {@Override public void run() {try {motionAnimation(22, 1, a); if(i < timing.length - 1) { motionDelay42_1 = (timing[22][1][i + 1] - timing[22][1][i]); Log.d("mInterval", "Set to " + motionDelay42_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion42_1);}} finally { if(motionDelay42_1 > 0) { mHandler.postDelayed(motion42_1, motionDelay42_1); } else {mHandler.removeCallbacks(motion42_1);}}}};
    Runnable motion43_1;// = new Runnable() {@Override public void run() {try {motionAnimation(23, 1, a); if(i < timing.length - 1) { motionDelay43_1 = (timing[23][1][i + 1] - timing[23][1][i]); Log.d("mInterval", "Set to " + motionDelay43_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion43_1);}} finally { if(motionDelay43_1 > 0) { mHandler.postDelayed(motion43_1, motionDelay43_1); } else {mHandler.removeCallbacks(motion43_1);}}}};
    Runnable motion44_1;// = new Runnable() {@Override public void run() {try {motionAnimation(24, 1, a); if(i < timing.length - 1) { motionDelay44_1 = (timing[24][1][i + 1] - timing[24][1][i]); Log.d("mInterval", "Set to " + motionDelay44_1); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion44_1);}} finally { if(motionDelay44_1 > 0) { mHandler.postDelayed(motion44_1, motionDelay44_1); } else {mHandler.removeCallbacks(motion44_1);}}}};

    // Right
    Runnable motion00_2;// = new Runnable() {@Override public void run() {try {motionAnimation(0 , 2, a); if(i < timing.length - 1) { motionDelay00_2 = (timing[0 ][2][i + 1] - timing[0 ][2][i]); Log.d("mInterval", "Set to " + motionDelay00_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion00_2);}} finally { if(motionDelay00_2 > 0) { mHandler.postDelayed(motion00_2, motionDelay00_2); } else {mHandler.removeCallbacks(motion00_2);}}}};
    Runnable motion01_2;// = new Runnable() {@Override public void run() {try {motionAnimation(1 , 2, a); if(i < timing.length - 1) { motionDelay01_2 = (timing[1 ][2][i + 1] - timing[1 ][2][i]); Log.d("mInterval", "Set to " + motionDelay01_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion01_2);}} finally { if(motionDelay01_2 > 0) { mHandler.postDelayed(motion01_2, motionDelay01_2); } else {mHandler.removeCallbacks(motion01_2);}}}};
    Runnable motion02_2;// = new Runnable() {@Override public void run() {try {motionAnimation(2 , 2, a); if(i < timing.length - 1) { motionDelay02_2 = (timing[2 ][2][i + 1] - timing[2 ][2][i]); Log.d("mInterval", "Set to " + motionDelay02_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion02_2);}} finally { if(motionDelay02_2 > 0) { mHandler.postDelayed(motion02_2, motionDelay02_2); } else {mHandler.removeCallbacks(motion02_2);}}}};
    Runnable motion03_2;// = new Runnable() {@Override public void run() {try {motionAnimation(3 , 2, a); if(i < timing.length - 1) { motionDelay03_2 = (timing[3 ][2][i + 1] - timing[3 ][2][i]); Log.d("mInterval", "Set to " + motionDelay03_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion03_2);}} finally { if(motionDelay03_2 > 0) { mHandler.postDelayed(motion03_2, motionDelay03_2); } else {mHandler.removeCallbacks(motion03_2);}}}};
    Runnable motion04_2;// = new Runnable() {@Override public void run() {try {motionAnimation(4 , 2, a); if(i < timing.length - 1) { motionDelay04_2 = (timing[4 ][2][i + 1] - timing[4 ][2][i]); Log.d("mInterval", "Set to " + motionDelay04_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion04_2);}} finally { if(motionDelay04_2 > 0) { mHandler.postDelayed(motion04_2, motionDelay04_2); } else {mHandler.removeCallbacks(motion04_2);}}}};
    Runnable motion05_2;// = new Runnable() {@Override public void run() {try {motionAnimation(5 , 2, a); if(i < timing.length - 1) { motionDelay05_2 = (timing[5 ][2][i + 1] - timing[5 ][2][i]); Log.d("mInterval", "Set to " + motionDelay05_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion05_2);}} finally { if(motionDelay05_2 > 0) { mHandler.postDelayed(motion05_2, motionDelay05_2); } else {mHandler.removeCallbacks(motion05_2);}}}};
    Runnable motion06_2;// = new Runnable() {@Override public void run() {try {motionAnimation(6 , 2, a); if(i < timing.length - 1) { motionDelay06_2 = (timing[6 ][2][i + 1] - timing[6 ][2][i]); Log.d("mInterval", "Set to " + motionDelay06_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion06_2);}} finally { if(motionDelay06_2 > 0) { mHandler.postDelayed(motion06_2, motionDelay06_2); } else {mHandler.removeCallbacks(motion06_2);}}}};
    Runnable motion07_2;// = new Runnable() {@Override public void run() {try {motionAnimation(7 , 2, a); if(i < timing.length - 1) { motionDelay07_2 = (timing[7 ][2][i + 1] - timing[7 ][2][i]); Log.d("mInterval", "Set to " + motionDelay07_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion07_2);}} finally { if(motionDelay07_2 > 0) { mHandler.postDelayed(motion07_2, motionDelay07_2); } else {mHandler.removeCallbacks(motion07_2);}}}};
    Runnable motion08_2;// = new Runnable() {@Override public void run() {try {motionAnimation(8 , 2, a); if(i < timing.length - 1) { motionDelay08_2 = (timing[8 ][2][i + 1] - timing[8 ][2][i]); Log.d("mInterval", "Set to " + motionDelay08_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion08_2);}} finally { if(motionDelay08_2 > 0) { mHandler.postDelayed(motion08_2, motionDelay08_2); } else {mHandler.removeCallbacks(motion08_2);}}}};
    Runnable motion11_2;// = new Runnable() {@Override public void run() {try {motionAnimation(9 , 2, a); if(i < timing.length - 1) { motionDelay11_2 = (timing[9 ][2][i + 1] - timing[9 ][2][i]); Log.d("mInterval", "Set to " + motionDelay11_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion11_2);}} finally { if(motionDelay11_2 > 0) { mHandler.postDelayed(motion11_2, motionDelay11_2); } else {mHandler.removeCallbacks(motion11_2);}}}};
    Runnable motion12_2;// = new Runnable() {@Override public void run() {try {motionAnimation(10, 2, a); if(i < timing.length - 1) { motionDelay12_2 = (timing[10][2][i + 1] - timing[10][2][i]); Log.d("mInterval", "Set to " + motionDelay12_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion12_2);}} finally { if(motionDelay12_2 > 0) { mHandler.postDelayed(motion12_2, motionDelay12_2); } else {mHandler.removeCallbacks(motion12_2);}}}};
    Runnable motion13_2;// = new Runnable() {@Override public void run() {try {motionAnimation(11, 2, a); if(i < timing.length - 1) { motionDelay13_2 = (timing[11][2][i + 1] - timing[11][2][i]); Log.d("mInterval", "Set to " + motionDelay13_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion13_2);}} finally { if(motionDelay13_2 > 0) { mHandler.postDelayed(motion13_2, motionDelay13_2); } else {mHandler.removeCallbacks(motion13_2);}}}};
    Runnable motion14_2;// = new Runnable() {@Override public void run() {try {motionAnimation(12, 2, a); if(i < timing.length - 1) { motionDelay14_2 = (timing[12][2][i + 1] - timing[12][2][i]); Log.d("mInterval", "Set to " + motionDelay14_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion14_2);}} finally { if(motionDelay14_2 > 0) { mHandler.postDelayed(motion14_2, motionDelay14_2); } else {mHandler.removeCallbacks(motion14_2);}}}};
    Runnable motion21_2;// = new Runnable() {@Override public void run() {try {motionAnimation(13, 2, a); if(i < timing.length - 1) { motionDelay21_2 = (timing[13][2][i + 1] - timing[13][2][i]); Log.d("mInterval", "Set to " + motionDelay21_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion21_2);}} finally { if(motionDelay21_2 > 0) { mHandler.postDelayed(motion21_2, motionDelay21_2); } else {mHandler.removeCallbacks(motion21_2);}}}};
    Runnable motion22_2;// = new Runnable() {@Override public void run() {try {motionAnimation(14, 2, a); if(i < timing.length - 1) { motionDelay22_2 = (timing[14][2][i + 1] - timing[14][2][i]); Log.d("mInterval", "Set to " + motionDelay22_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion22_2);}} finally { if(motionDelay22_2 > 0) { mHandler.postDelayed(motion22_2, motionDelay22_2); } else {mHandler.removeCallbacks(motion22_2);}}}};
    Runnable motion23_2;// = new Runnable() {@Override public void run() {try {motionAnimation(15, 2, a); if(i < timing.length - 1) { motionDelay23_2 = (timing[15][2][i + 1] - timing[15][2][i]); Log.d("mInterval", "Set to " + motionDelay23_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion23_2);}} finally { if(motionDelay23_2 > 0) { mHandler.postDelayed(motion23_2, motionDelay23_2); } else {mHandler.removeCallbacks(motion23_2);}}}};
    Runnable motion24_2;// = new Runnable() {@Override public void run() {try {motionAnimation(16, 2, a); if(i < timing.length - 1) { motionDelay24_2 = (timing[16][2][i + 1] - timing[16][2][i]); Log.d("mInterval", "Set to " + motionDelay24_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion24_2);}} finally { if(motionDelay24_2 > 0) { mHandler.postDelayed(motion24_2, motionDelay24_2); } else {mHandler.removeCallbacks(motion24_2);}}}};
    Runnable motion31_2;// = new Runnable() {@Override public void run() {try {motionAnimation(17, 2, a); if(i < timing.length - 1) { motionDelay31_2 = (timing[17][2][i + 1] - timing[17][2][i]); Log.d("mInterval", "Set to " + motionDelay31_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion31_2);}} finally { if(motionDelay31_2 > 0) { mHandler.postDelayed(motion31_2, motionDelay31_2); } else {mHandler.removeCallbacks(motion31_2);}}}};
    Runnable motion32_2;// = new Runnable() {@Override public void run() {try {motionAnimation(18, 2, a); if(i < timing.length - 1) { motionDelay32_2 = (timing[18][2][i + 1] - timing[18][2][i]); Log.d("mInterval", "Set to " + motionDelay32_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion32_2);}} finally { if(motionDelay32_2 > 0) { mHandler.postDelayed(motion32_2, motionDelay32_2); } else {mHandler.removeCallbacks(motion32_2);}}}};
    Runnable motion33_2;// = new Runnable() {@Override public void run() {try {motionAnimation(19, 2, a); if(i < timing.length - 1) { motionDelay33_2 = (timing[19][2][i + 1] - timing[19][2][i]); Log.d("mInterval", "Set to " + motionDelay33_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion33_2);}} finally { if(motionDelay33_2 > 0) { mHandler.postDelayed(motion33_2, motionDelay33_2); } else {mHandler.removeCallbacks(motion33_2);}}}};
    Runnable motion34_2;// = new Runnable() {@Override public void run() {try {motionAnimation(20, 2, a); if(i < timing.length - 1) { motionDelay34_2 = (timing[20][2][i + 1] - timing[20][2][i]); Log.d("mInterval", "Set to " + motionDelay34_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion34_2);}} finally { if(motionDelay34_2 > 0) { mHandler.postDelayed(motion34_2, motionDelay34_2); } else {mHandler.removeCallbacks(motion34_2);}}}};
    Runnable motion41_2;// = new Runnable() {@Override public void run() {try {motionAnimation(21, 2, a); if(i < timing.length - 1) { motionDelay41_2 = (timing[21][2][i + 1] - timing[21][2][i]); Log.d("mInterval", "Set to " + motionDelay41_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion41_2);}} finally { if(motionDelay41_2 > 0) { mHandler.postDelayed(motion41_2, motionDelay41_2); } else {mHandler.removeCallbacks(motion41_2);}}}};
    Runnable motion42_2;// = new Runnable() {@Override public void run() {try {motionAnimation(22, 2, a); if(i < timing.length - 1) { motionDelay42_2 = (timing[22][2][i + 1] - timing[22][2][i]); Log.d("mInterval", "Set to " + motionDelay42_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion42_2);}} finally { if(motionDelay42_2 > 0) { mHandler.postDelayed(motion42_2, motionDelay42_2); } else {mHandler.removeCallbacks(motion42_2);}}}};
    Runnable motion43_2;// = new Runnable() {@Override public void run() {try {motionAnimation(23, 2, a); if(i < timing.length - 1) { motionDelay43_2 = (timing[23][2][i + 1] - timing[23][2][i]); Log.d("mInterval", "Set to " + motionDelay43_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion43_2);}} finally { if(motionDelay43_2 > 0) { mHandler.postDelayed(motion43_2, motionDelay43_2); } else {mHandler.removeCallbacks(motion43_2);}}}};
    Runnable motion44_2;// = new Runnable() {@Override public void run() {try {motionAnimation(24, 2, a); if(i < timing.length - 1) { motionDelay44_2 = (timing[24][2][i + 1] - timing[24][2][i]); Log.d("mInterval", "Set to " + motionDelay44_2); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion44_2);}} finally { if(motionDelay44_2 > 0) { mHandler.postDelayed(motion44_2, motionDelay44_2); } else {mHandler.removeCallbacks(motion44_2);}}}};

    // Down
    Runnable motion00_3;// = new Runnable() {@Override public void run() {try {motionAnimation(0 , 3, a); if(i < timing.length - 1) { motionDelay00_3 = (timing[0 ][3][i + 1] - timing[0 ][3][i]); Log.d("mInterval", "Set to " + motionDelay00_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion00_3);}} finally { if(motionDelay00_3 > 0) { mHandler.postDelayed(motion00_3, motionDelay00_3); } else {mHandler.removeCallbacks(motion00_3);}}}};
    Runnable motion01_3;// = new Runnable() {@Override public void run() {try {motionAnimation(1 , 3, a); if(i < timing.length - 1) { motionDelay01_3 = (timing[1 ][3][i + 1] - timing[1 ][3][i]); Log.d("mInterval", "Set to " + motionDelay01_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion01_3);}} finally { if(motionDelay01_3 > 0) { mHandler.postDelayed(motion01_3, motionDelay01_3); } else {mHandler.removeCallbacks(motion01_3);}}}};
    Runnable motion02_3;// = new Runnable() {@Override public void run() {try {motionAnimation(2 , 3, a); if(i < timing.length - 1) { motionDelay02_3 = (timing[2 ][3][i + 1] - timing[2 ][3][i]); Log.d("mInterval", "Set to " + motionDelay02_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion02_3);}} finally { if(motionDelay02_3 > 0) { mHandler.postDelayed(motion02_3, motionDelay02_3); } else {mHandler.removeCallbacks(motion02_3);}}}};
    Runnable motion03_3;// = new Runnable() {@Override public void run() {try {motionAnimation(3 , 3, a); if(i < timing.length - 1) { motionDelay03_3 = (timing[3 ][3][i + 1] - timing[3 ][3][i]); Log.d("mInterval", "Set to " + motionDelay03_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion03_3);}} finally { if(motionDelay03_3 > 0) { mHandler.postDelayed(motion03_3, motionDelay03_3); } else {mHandler.removeCallbacks(motion03_3);}}}};
    Runnable motion04_3;// = new Runnable() {@Override public void run() {try {motionAnimation(4 , 3, a); if(i < timing.length - 1) { motionDelay04_3 = (timing[4 ][3][i + 1] - timing[4 ][3][i]); Log.d("mInterval", "Set to " + motionDelay04_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion04_3);}} finally { if(motionDelay04_3 > 0) { mHandler.postDelayed(motion04_3, motionDelay04_3); } else {mHandler.removeCallbacks(motion04_3);}}}};
    Runnable motion05_3;// = new Runnable() {@Override public void run() {try {motionAnimation(5 , 3, a); if(i < timing.length - 1) { motionDelay05_3 = (timing[5 ][3][i + 1] - timing[5 ][3][i]); Log.d("mInterval", "Set to " + motionDelay05_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion05_3);}} finally { if(motionDelay05_3 > 0) { mHandler.postDelayed(motion05_3, motionDelay05_3); } else {mHandler.removeCallbacks(motion05_3);}}}};
    Runnable motion06_3;// = new Runnable() {@Override public void run() {try {motionAnimation(6 , 3, a); if(i < timing.length - 1) { motionDelay06_3 = (timing[6 ][3][i + 1] - timing[6 ][3][i]); Log.d("mInterval", "Set to " + motionDelay06_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion06_3);}} finally { if(motionDelay06_3 > 0) { mHandler.postDelayed(motion06_3, motionDelay06_3); } else {mHandler.removeCallbacks(motion06_3);}}}};
    Runnable motion07_3;// = new Runnable() {@Override public void run() {try {motionAnimation(7 , 3, a); if(i < timing.length - 1) { motionDelay07_3 = (timing[7 ][3][i + 1] - timing[7 ][3][i]); Log.d("mInterval", "Set to " + motionDelay07_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion07_3);}} finally { if(motionDelay07_3 > 0) { mHandler.postDelayed(motion07_3, motionDelay07_3); } else {mHandler.removeCallbacks(motion07_3);}}}};
    Runnable motion08_3;// = new Runnable() {@Override public void run() {try {motionAnimation(8 , 3, a); if(i < timing.length - 1) { motionDelay08_3 = (timing[8 ][3][i + 1] - timing[8 ][3][i]); Log.d("mInterval", "Set to " + motionDelay08_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion08_3);}} finally { if(motionDelay08_3 > 0) { mHandler.postDelayed(motion08_3, motionDelay08_3); } else {mHandler.removeCallbacks(motion08_3);}}}};
    Runnable motion11_3;// = new Runnable() {@Override public void run() {try {motionAnimation(9 , 3, a); if(i < timing.length - 1) { motionDelay11_3 = (timing[9 ][3][i + 1] - timing[9 ][3][i]); Log.d("mInterval", "Set to " + motionDelay11_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion11_3);}} finally { if(motionDelay11_3 > 0) { mHandler.postDelayed(motion11_3, motionDelay11_3); } else {mHandler.removeCallbacks(motion11_3);}}}};
    Runnable motion12_3;// = new Runnable() {@Override public void run() {try {motionAnimation(10, 3, a); if(i < timing.length - 1) { motionDelay12_3 = (timing[10][3][i + 1] - timing[10][3][i]); Log.d("mInterval", "Set to " + motionDelay12_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion12_3);}} finally { if(motionDelay12_3 > 0) { mHandler.postDelayed(motion12_3, motionDelay12_3); } else {mHandler.removeCallbacks(motion12_3);}}}};
    Runnable motion13_3;// = new Runnable() {@Override public void run() {try {motionAnimation(11, 3, a); if(i < timing.length - 1) { motionDelay13_3 = (timing[11][3][i + 1] - timing[11][3][i]); Log.d("mInterval", "Set to " + motionDelay13_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion13_3);}} finally { if(motionDelay13_3 > 0) { mHandler.postDelayed(motion13_3, motionDelay13_3); } else {mHandler.removeCallbacks(motion13_3);}}}};
    Runnable motion14_3;// = new Runnable() {@Override public void run() {try {motionAnimation(12, 3, a); if(i < timing.length - 1) { motionDelay14_3 = (timing[12][3][i + 1] - timing[12][3][i]); Log.d("mInterval", "Set to " + motionDelay14_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion14_3);}} finally { if(motionDelay14_3 > 0) { mHandler.postDelayed(motion14_3, motionDelay14_3); } else {mHandler.removeCallbacks(motion14_3);}}}};
    Runnable motion21_3;// = new Runnable() {@Override public void run() {try {motionAnimation(13, 3, a); if(i < timing.length - 1) { motionDelay21_3 = (timing[13][3][i + 1] - timing[13][3][i]); Log.d("mInterval", "Set to " + motionDelay21_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion21_3);}} finally { if(motionDelay21_3 > 0) { mHandler.postDelayed(motion21_3, motionDelay21_3); } else {mHandler.removeCallbacks(motion21_3);}}}};
    Runnable motion22_3;// = new Runnable() {@Override public void run() {try {motionAnimation(14, 3, a); if(i < timing.length - 1) { motionDelay22_3 = (timing[14][3][i + 1] - timing[14][3][i]); Log.d("mInterval", "Set to " + motionDelay22_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion22_3);}} finally { if(motionDelay22_3 > 0) { mHandler.postDelayed(motion22_3, motionDelay22_3); } else {mHandler.removeCallbacks(motion22_3);}}}};
    Runnable motion23_3;// = new Runnable() {@Override public void run() {try {motionAnimation(15, 3, a); if(i < timing.length - 1) { motionDelay23_3 = (timing[15][3][i + 1] - timing[15][3][i]); Log.d("mInterval", "Set to " + motionDelay23_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion23_3);}} finally { if(motionDelay23_3 > 0) { mHandler.postDelayed(motion23_3, motionDelay23_3); } else {mHandler.removeCallbacks(motion23_3);}}}};
    Runnable motion24_3;// = new Runnable() {@Override public void run() {try {motionAnimation(16, 3, a); if(i < timing.length - 1) { motionDelay24_3 = (timing[16][3][i + 1] - timing[16][3][i]); Log.d("mInterval", "Set to " + motionDelay24_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion24_3);}} finally { if(motionDelay24_3 > 0) { mHandler.postDelayed(motion24_3, motionDelay24_3); } else {mHandler.removeCallbacks(motion24_3);}}}};
    Runnable motion31_3;// = new Runnable() {@Override public void run() {try {motionAnimation(17, 3, a); if(i < timing.length - 1) { motionDelay31_3 = (timing[17][3][i + 1] - timing[17][3][i]); Log.d("mInterval", "Set to " + motionDelay31_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion31_3);}} finally { if(motionDelay31_3 > 0) { mHandler.postDelayed(motion31_3, motionDelay31_3); } else {mHandler.removeCallbacks(motion31_3);}}}};
    Runnable motion32_3;// = new Runnable() {@Override public void run() {try {motionAnimation(18, 3, a); if(i < timing.length - 1) { motionDelay32_3 = (timing[18][3][i + 1] - timing[18][3][i]); Log.d("mInterval", "Set to " + motionDelay32_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion32_3);}} finally { if(motionDelay32_3 > 0) { mHandler.postDelayed(motion32_3, motionDelay32_3); } else {mHandler.removeCallbacks(motion32_3);}}}};
    Runnable motion33_3;// = new Runnable() {@Override public void run() {try {motionAnimation(19, 3, a); if(i < timing.length - 1) { motionDelay33_3 = (timing[19][3][i + 1] - timing[19][3][i]); Log.d("mInterval", "Set to " + motionDelay33_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion33_3);}} finally { if(motionDelay33_3 > 0) { mHandler.postDelayed(motion33_3, motionDelay33_3); } else {mHandler.removeCallbacks(motion33_3);}}}};
    Runnable motion34_3;// = new Runnable() {@Override public void run() {try {motionAnimation(20, 3, a); if(i < timing.length - 1) { motionDelay34_3 = (timing[20][3][i + 1] - timing[20][3][i]); Log.d("mInterval", "Set to " + motionDelay34_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion34_3);}} finally { if(motionDelay34_3 > 0) { mHandler.postDelayed(motion34_3, motionDelay34_3); } else {mHandler.removeCallbacks(motion34_3);}}}};
    Runnable motion41_3;// = new Runnable() {@Override public void run() {try {motionAnimation(21, 3, a); if(i < timing.length - 1) { motionDelay41_3 = (timing[21][3][i + 1] - timing[21][3][i]); Log.d("mInterval", "Set to " + motionDelay41_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion41_3);}} finally { if(motionDelay41_3 > 0) { mHandler.postDelayed(motion41_3, motionDelay41_3); } else {mHandler.removeCallbacks(motion41_3);}}}};
    Runnable motion42_3;// = new Runnable() {@Override public void run() {try {motionAnimation(22, 3, a); if(i < timing.length - 1) { motionDelay42_3 = (timing[22][3][i + 1] - timing[22][3][i]); Log.d("mInterval", "Set to " + motionDelay42_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion42_3);}} finally { if(motionDelay42_3 > 0) { mHandler.postDelayed(motion42_3, motionDelay42_3); } else {mHandler.removeCallbacks(motion42_3);}}}};
    Runnable motion43_3;// = new Runnable() {@Override public void run() {try {motionAnimation(23, 3, a); if(i < timing.length - 1) { motionDelay43_3 = (timing[23][3][i + 1] - timing[23][3][i]); Log.d("mInterval", "Set to " + motionDelay43_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion43_3);}} finally { if(motionDelay43_3 > 0) { mHandler.postDelayed(motion43_3, motionDelay43_3); } else {mHandler.removeCallbacks(motion43_3);}}}};
    Runnable motion44_3;// = new Runnable() {@Override public void run() {try {motionAnimation(24, 3, a); if(i < timing.length - 1) { motionDelay44_3 = (timing[24][3][i + 1] - timing[24][3][i]); Log.d("mInterval", "Set to " + motionDelay44_3); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion44_3);}} finally { if(motionDelay44_3 > 0) { mHandler.postDelayed(motion44_3, motionDelay44_3); } else {mHandler.removeCallbacks(motion44_3);}}}};

    // Left
    Runnable motion00_4;// = new Runnable() {@Override public void run() {try {motionAnimation(0 , 4, a); if(i < timing.length - 1) { motionDelay00_4 = (timing[0 ][4][i + 1] - timing[0 ][4][i]); Log.d("mInterval", "Set to " + motionDelay00_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion00_4);}} finally { if(motionDelay00_4 > 0) { mHandler.postDelayed(motion00_4, motionDelay00_4); } else {mHandler.removeCallbacks(motion00_4);}}}};
    Runnable motion01_4;// = new Runnable() {@Override public void run() {try {motionAnimation(1 , 4, a); if(i < timing.length - 1) { motionDelay01_4 = (timing[1 ][4][i + 1] - timing[1 ][4][i]); Log.d("mInterval", "Set to " + motionDelay01_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion01_4);}} finally { if(motionDelay01_4 > 0) { mHandler.postDelayed(motion01_4, motionDelay01_4); } else {mHandler.removeCallbacks(motion01_4);}}}};
    Runnable motion02_4;// = new Runnable() {@Override public void run() {try {motionAnimation(2 , 4, a); if(i < timing.length - 1) { motionDelay02_4 = (timing[2 ][4][i + 1] - timing[2 ][4][i]); Log.d("mInterval", "Set to " + motionDelay02_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion02_4);}} finally { if(motionDelay02_4 > 0) { mHandler.postDelayed(motion02_4, motionDelay02_4); } else {mHandler.removeCallbacks(motion02_4);}}}};
    Runnable motion03_4;// = new Runnable() {@Override public void run() {try {motionAnimation(3 , 4, a); if(i < timing.length - 1) { motionDelay03_4 = (timing[3 ][4][i + 1] - timing[3 ][4][i]); Log.d("mInterval", "Set to " + motionDelay03_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion03_4);}} finally { if(motionDelay03_4 > 0) { mHandler.postDelayed(motion03_4, motionDelay03_4); } else {mHandler.removeCallbacks(motion03_4);}}}};
    Runnable motion04_4;// = new Runnable() {@Override public void run() {try {motionAnimation(4 , 4, a); if(i < timing.length - 1) { motionDelay04_4 = (timing[4 ][4][i + 1] - timing[4 ][4][i]); Log.d("mInterval", "Set to " + motionDelay04_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion04_4);}} finally { if(motionDelay04_4 > 0) { mHandler.postDelayed(motion04_4, motionDelay04_4); } else {mHandler.removeCallbacks(motion04_4);}}}};
    Runnable motion05_4;// = new Runnable() {@Override public void run() {try {motionAnimation(5 , 4, a); if(i < timing.length - 1) { motionDelay05_4 = (timing[5 ][4][i + 1] - timing[5 ][4][i]); Log.d("mInterval", "Set to " + motionDelay05_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion05_4);}} finally { if(motionDelay05_4 > 0) { mHandler.postDelayed(motion05_4, motionDelay05_4); } else {mHandler.removeCallbacks(motion05_4);}}}};
    Runnable motion06_4;// = new Runnable() {@Override public void run() {try {motionAnimation(6 , 4, a); if(i < timing.length - 1) { motionDelay06_4 = (timing[6 ][4][i + 1] - timing[6 ][4][i]); Log.d("mInterval", "Set to " + motionDelay06_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion06_4);}} finally { if(motionDelay06_4 > 0) { mHandler.postDelayed(motion06_4, motionDelay06_4); } else {mHandler.removeCallbacks(motion06_4);}}}};
    Runnable motion07_4;// = new Runnable() {@Override public void run() {try {motionAnimation(7 , 4, a); if(i < timing.length - 1) { motionDelay07_4 = (timing[7 ][4][i + 1] - timing[7 ][4][i]); Log.d("mInterval", "Set to " + motionDelay07_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion07_4);}} finally { if(motionDelay07_4 > 0) { mHandler.postDelayed(motion07_4, motionDelay07_4); } else {mHandler.removeCallbacks(motion07_4);}}}};
    Runnable motion08_4;// = new Runnable() {@Override public void run() {try {motionAnimation(8 , 4, a); if(i < timing.length - 1) { motionDelay08_4 = (timing[8 ][4][i + 1] - timing[8 ][4][i]); Log.d("mInterval", "Set to " + motionDelay08_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion08_4);}} finally { if(motionDelay08_4 > 0) { mHandler.postDelayed(motion08_4, motionDelay08_4); } else {mHandler.removeCallbacks(motion08_4);}}}};
    Runnable motion11_4;// = new Runnable() {@Override public void run() {try {motionAnimation(9 , 4, a); if(i < timing.length - 1) { motionDelay11_4 = (timing[9 ][4][i + 1] - timing[9 ][4][i]); Log.d("mInterval", "Set to " + motionDelay11_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion11_4);}} finally { if(motionDelay11_4 > 0) { mHandler.postDelayed(motion11_4, motionDelay11_4); } else {mHandler.removeCallbacks(motion11_4);}}}};
    Runnable motion12_4;// = new Runnable() {@Override public void run() {try {motionAnimation(10, 4, a); if(i < timing.length - 1) { motionDelay12_4 = (timing[10][4][i + 1] - timing[10][4][i]); Log.d("mInterval", "Set to " + motionDelay12_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion12_4);}} finally { if(motionDelay12_4 > 0) { mHandler.postDelayed(motion12_4, motionDelay12_4); } else {mHandler.removeCallbacks(motion12_4);}}}};
    Runnable motion13_4;// = new Runnable() {@Override public void run() {try {motionAnimation(11, 4, a); if(i < timing.length - 1) { motionDelay13_4 = (timing[11][4][i + 1] - timing[11][4][i]); Log.d("mInterval", "Set to " + motionDelay13_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion13_4);}} finally { if(motionDelay13_4 > 0) { mHandler.postDelayed(motion13_4, motionDelay13_4); } else {mHandler.removeCallbacks(motion13_4);}}}};
    Runnable motion14_4;// = new Runnable() {@Override public void run() {try {motionAnimation(12, 4, a); if(i < timing.length - 1) { motionDelay14_4 = (timing[12][4][i + 1] - timing[12][4][i]); Log.d("mInterval", "Set to " + motionDelay14_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion14_4);}} finally { if(motionDelay14_4 > 0) { mHandler.postDelayed(motion14_4, motionDelay14_4); } else {mHandler.removeCallbacks(motion14_4);}}}};
    Runnable motion21_4;// = new Runnable() {@Override public void run() {try {motionAnimation(13, 4, a); if(i < timing.length - 1) { motionDelay21_4 = (timing[13][4][i + 1] - timing[13][4][i]); Log.d("mInterval", "Set to " + motionDelay21_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion21_4);}} finally { if(motionDelay21_4 > 0) { mHandler.postDelayed(motion21_4, motionDelay21_4); } else {mHandler.removeCallbacks(motion21_4);}}}};
    Runnable motion22_4;// = new Runnable() {@Override public void run() {try {motionAnimation(14, 4, a); if(i < timing.length - 1) { motionDelay22_4 = (timing[14][4][i + 1] - timing[14][4][i]); Log.d("mInterval", "Set to " + motionDelay22_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion22_4);}} finally { if(motionDelay22_4 > 0) { mHandler.postDelayed(motion22_4, motionDelay22_4); } else {mHandler.removeCallbacks(motion22_4);}}}};
    Runnable motion23_4;// = new Runnable() {@Override public void run() {try {motionAnimation(15, 4, a); if(i < timing.length - 1) { motionDelay23_4 = (timing[15][4][i + 1] - timing[15][4][i]); Log.d("mInterval", "Set to " + motionDelay23_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion23_4);}} finally { if(motionDelay23_4 > 0) { mHandler.postDelayed(motion23_4, motionDelay23_4); } else {mHandler.removeCallbacks(motion23_4);}}}};
    Runnable motion24_4;// = new Runnable() {@Override public void run() {try {motionAnimation(16, 4, a); if(i < timing.length - 1) { motionDelay24_4 = (timing[16][4][i + 1] - timing[16][4][i]); Log.d("mInterval", "Set to " + motionDelay24_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion24_4);}} finally { if(motionDelay24_4 > 0) { mHandler.postDelayed(motion24_4, motionDelay24_4); } else {mHandler.removeCallbacks(motion24_4);}}}};
    Runnable motion31_4;// = new Runnable() {@Override public void run() {try {motionAnimation(17, 4, a); if(i < timing.length - 1) { motionDelay31_4 = (timing[17][4][i + 1] - timing[17][4][i]); Log.d("mInterval", "Set to " + motionDelay31_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion31_4);}} finally { if(motionDelay31_4 > 0) { mHandler.postDelayed(motion31_4, motionDelay31_4); } else {mHandler.removeCallbacks(motion31_4);}}}};
    Runnable motion32_4;// = new Runnable() {@Override public void run() {try {motionAnimation(18, 4, a); if(i < timing.length - 1) { motionDelay32_4 = (timing[18][4][i + 1] - timing[18][4][i]); Log.d("mInterval", "Set to " + motionDelay32_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion32_4);}} finally { if(motionDelay32_4 > 0) { mHandler.postDelayed(motion32_4, motionDelay32_4); } else {mHandler.removeCallbacks(motion32_4);}}}};
    Runnable motion33_4;// = new Runnable() {@Override public void run() {try {motionAnimation(19, 4, a); if(i < timing.length - 1) { motionDelay33_4 = (timing[19][4][i + 1] - timing[19][4][i]); Log.d("mInterval", "Set to " + motionDelay33_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion33_4);}} finally { if(motionDelay33_4 > 0) { mHandler.postDelayed(motion33_4, motionDelay33_4); } else {mHandler.removeCallbacks(motion33_4);}}}};
    Runnable motion34_4;// = new Runnable() {@Override public void run() {try {motionAnimation(20, 4, a); if(i < timing.length - 1) { motionDelay34_4 = (timing[20][4][i + 1] - timing[20][4][i]); Log.d("mInterval", "Set to " + motionDelay34_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion34_4);}} finally { if(motionDelay34_4 > 0) { mHandler.postDelayed(motion34_4, motionDelay34_4); } else {mHandler.removeCallbacks(motion34_4);}}}};
    Runnable motion41_4;// = new Runnable() {@Override public void run() {try {motionAnimation(21, 4, a); if(i < timing.length - 1) { motionDelay41_4 = (timing[21][4][i + 1] - timing[21][4][i]); Log.d("mInterval", "Set to " + motionDelay41_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion41_4);}} finally { if(motionDelay41_4 > 0) { mHandler.postDelayed(motion41_4, motionDelay41_4); } else {mHandler.removeCallbacks(motion41_4);}}}};
    Runnable motion42_4;// = new Runnable() {@Override public void run() {try {motionAnimation(22, 4, a); if(i < timing.length - 1) { motionDelay42_4 = (timing[22][4][i + 1] - timing[22][4][i]); Log.d("mInterval", "Set to " + motionDelay42_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion42_4);}} finally { if(motionDelay42_4 > 0) { mHandler.postDelayed(motion42_4, motionDelay42_4); } else {mHandler.removeCallbacks(motion42_4);}}}};
    Runnable motion43_4;// = new Runnable() {@Override public void run() {try {motionAnimation(23, 4, a); if(i < timing.length - 1) { motionDelay43_4 = (timing[23][4][i + 1] - timing[23][4][i]); Log.d("mInterval", "Set to " + motionDelay43_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion43_4);}} finally { if(motionDelay43_4 > 0) { mHandler.postDelayed(motion43_4, motionDelay43_4); } else {mHandler.removeCallbacks(motion43_4);}}}};
    Runnable motion44_4;// = new Runnable() {@Override public void run() {try {motionAnimation(24, 4, a); if(i < timing.length - 1) { motionDelay44_4 = (timing[24][4][i + 1] - timing[24][4][i]); Log.d("mInterval", "Set to " + motionDelay44_4); i++; } else { Log.d("Array", "Finished"); mHandler.removeCallbacks(motion44_4);}} finally { if(motionDelay44_4 > 0) { mHandler.postDelayed(motion44_4, motionDelay44_4); } else {mHandler.removeCallbacks(motion44_4);}}}};

    Runnable motions[][] = {
            {
                    motion00  ,
                    motion01  ,
                    motion02  ,
                    motion03  ,
                    motion04  ,
                    motion05  ,
                    motion06  ,
                    motion07  ,
                    motion08  ,
                    motion11  ,
                    motion12  ,
                    motion13  ,
                    motion14  ,
                    motion21  ,
                    motion22  ,
                    motion23  ,
                    motion24  ,
                    motion31  ,
                    motion32  ,
                    motion33  ,
                    motion34  ,
                    motion41  ,
                    motion42  ,
                    motion43  ,
                    motion44
            },
            {
                    motion00_1,
                    motion01_1,
                    motion02_1,
                    motion03_1,
                    motion04_1,
                    motion05_1,
                    motion06_1,
                    motion07_1,
                    motion08_1,
                    motion11_1,
                    motion12_1,
                    motion13_1,
                    motion14_1,
                    motion21_1,
                    motion22_1,
                    motion23_1,
                    motion24_1,
                    motion31_1,
                    motion32_1,
                    motion33_1,
                    motion34_1,
                    motion41_1,
                    motion42_1,
                    motion43_1,
                    motion44_1
            },
            {
                    motion00_2,
                    motion01_2,
                    motion02_2,
                    motion03_2,
                    motion04_2,
                    motion05_2,
                    motion06_2,
                    motion07_2,
                    motion08_2,
                    motion11_2,
                    motion12_2,
                    motion13_2,
                    motion14_2,
                    motion21_2,
                    motion22_2,
                    motion23_2,
                    motion24_2,
                    motion31_2,
                    motion32_2,
                    motion33_2,
                    motion34_2,
                    motion41_2,
                    motion42_2,
                    motion43_2,
                    motion44_2
            },
            {
                    motion00_3,
                    motion01_3,
                    motion02_3,
                    motion03_3,
                    motion04_3,
                    motion05_3,
                    motion06_3,
                    motion07_3,
                    motion08_3,
                    motion11_3,
                    motion12_3,
                    motion13_3,
                    motion14_3,
                    motion21_3,
                    motion22_3,
                    motion23_3,
                    motion24_3,
                    motion31_3,
                    motion32_3,
                    motion33_3,
                    motion34_3,
                    motion41_3,
                    motion42_3,
                    motion43_3,
                    motion44_3
            },
            {
                    motion00_4,
                    motion01_4,
                    motion02_4,
                    motion03_4,
                    motion04_4,
                    motion05_4,
                    motion06_4,
                    motion07_4,
                    motion08_4,
                    motion11_4,
                    motion12_4,
                    motion13_4,
                    motion14_4,
                    motion21_4,
                    motion22_4,
                    motion23_4,
                    motion24_4,
                    motion31_4,
                    motion32_4,
                    motion33_4,
                    motion34_4,
                    motion41_4,
                    motion42_4,
                    motion43_4,
                    motion44_4
            },
    };

    int indexI;
    int indexJ;

    private int timing[][][];

    void setRunnables() {
        for(indexI = 0; indexI < 5; indexI++) {
            for(indexJ = 0; indexJ < 25; indexJ++) {
                motions[indexI][indexJ] = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            motionAnimation(indexJ, indexI, a);
                            if (motionDelayIndexes[indexI][indexJ] < timing[indexJ][indexI].length - 1) { //TODO CHECK ARRAY
                                motionDelays[indexI][indexJ] = timing[indexJ][indexI][motionDelayIndexes[indexI][indexJ] + 1] - timing[indexJ][indexI][motionDelayIndexes[indexI][indexJ]];
                                Log.d("setRunnables", "motionDelay at [" + indexI + "," + indexJ + "] set to " + motionDelays[indexI][indexJ] + "ms");
                                motionDelayIndexes[indexI][indexJ]++;
                            } else {
                                Log.d("setRunnables", "motionDelay array ended");
                                mHandler.removeCallbacks(motions[indexI][indexJ]);
                            }
                        } finally {
                            if (motionDelays[indexI][indexJ] > 0) {
                                mHandler.postDelayed(motions[indexI][indexJ], motionDelays[indexI][indexJ]);
                            } else {
                                mHandler.removeCallbacks(motions[indexI][indexJ]);
                            }
                        }
                    }
                };
            }
        }
    }

    void tutorialPlay(final double array[][], final Activity activity){
        Log.i("TutorialService", "tutorial started");
        window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset);
        for(count = 0; count < 24; count++){
            i = 0;

            final int btnNum[] = {-1};
            btnNum[0] = countToButtonNumber(count);

            //TODO LOGIC CHECK

            final int delay;

            if(count == 1 && i == 0) {
                delay = (int) (array[count][i]) * 1000;
            } else {
                delay = (int) (array[count][i]) * 1000 + 200;
            }

//            tutorial.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    motionAnimation(btnNum[0], activity);
//                    if(array[count][i] != -1 && array[count][i + 1] != -1 && i <= array[count].length) {
//                        Log.d("PostDelayed", "delayed btn" + count + " for " + (array[count][i + 1] - array[count][i]) + "ms");
//                        tutorial.postDelayed(this, (long) (array[count][i + 1] - array[count][i]));
//                        i++;
//                    }
//                }
//            }, (long) array[count][0]);
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

    int buttonIds[] = {
            R.id.btn00_tutorial,
            R.id.tgl1_tutorial ,
            R.id.tgl2_tutorial ,
            R.id.tgl3_tutorial ,
            R.id.tgl4_tutorial ,
            R.id.tgl5_tutorial ,
            R.id.tgl6_tutorial ,
            R.id.tgl7_tutorial ,
            R.id.tgl8_tutorial ,
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

    void motionAnimation(int buttonNumber, int toggleNumber, Activity activity){
//        switch (buttonNumber){
//            case 0:  playMotion(R.id.btn00_tutorial, activity); break;
//
//            //Toggle2
//            case 1:  playMotion(R.id.tgl1_tutorial , activity); break;
//            case 2:  playMotion(R.id.tgl2_tutorial , activity); break;
//            case 3:  playMotion(R.id.tgl3_tutorial , activity); break;
//            case 4:  playMotion(R.id.tgl4_tutorial , activity); break;
//            case 5:  playMotion(R.id.tgl5_tutorial , activity); break;
//            case 6:  playMotion(R.id.tgl6_tutorial , activity); break;
//            case 7:  playMotion(R.id.tgl7_tutorial , activity); break;
//            case 8:  playMotion(R.id.tgl8_tutorial , activity); break;
//
//            //Main
//            case 11: playMotion(R.id.btn11_tutorial, activity); break;
//            case 12: playMotion(R.id.btn12_tutorial, activity); break;
//            case 13: playMotion(R.id.btn13_tutorial, activity); break;
//            case 14: playMotion(R.id.btn14_tutorial, activity); break;
//            case 21: playMotion(R.id.btn21_tutorial, activity); break;
//            case 22: playMotion(R.id.btn22_tutorial, activity); break;
//            case 23: playMotion(R.id.btn23_tutorial, activity); break;
//            case 24: playMotion(R.id.btn24_tutorial, activity); break;
//            case 31: playMotion(R.id.btn31_tutorial, activity); break;
//            case 32: playMotion(R.id.btn32_tutorial, activity); break;
//            case 33: playMotion(R.id.btn33_tutorial, activity); break;
//            case 34: playMotion(R.id.btn34_tutorial, activity); break;
//            case 41: playMotion(R.id.btn41_tutorial, activity); break;
//            case 42: playMotion(R.id.btn42_tutorial, activity); break;
//            case 43: playMotion(R.id.btn43_tutorial, activity); break;
//            case 44: playMotion(R.id.btn44_tutorial, activity); break;
//
//            default: Log.i("Turorial Service", "Method called without any matching number"); break;
//        }
        playMotion(buttonIds[buttonNumber], toggleNumber, activity);
    }

    void playMotionAnimation(int btnId, int phId, int motionId, Activity activity) {
        VideoView videoView;
        View placeholder;

        videoView = (VideoView)activity.findViewById(btnId);
        placeholder = (View)activity.findViewById(phId);
        animator(videoView, placeholder, motionId, activity);
        Log.i("MotionAnimation", "Animation played on " + activity.getResources().getResourceEntryName(phId));
    }

    // ScaleAnimations
    // Normal
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

    // Up (1)
    ScaleAnimation btn00_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f); //TODO CHECK
    ScaleAnimation btn01_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn02_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn03_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn04_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn05_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn06_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn07_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn08_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn11_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn12_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn13_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn14_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn21_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn22_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn23_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn24_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn31_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn32_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn33_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn34_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn41_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn42_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn43_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    ScaleAnimation btn44_1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);

    // Right (2)
    ScaleAnimation btn00_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f); //TODO CHECK
    ScaleAnimation btn01_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn02_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn03_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn04_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn05_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn06_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn07_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn08_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn11_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn12_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn13_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn14_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn21_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn22_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn23_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn24_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn31_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn32_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn33_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn34_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn41_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn42_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn43_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn44_2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);

    // Down (3)
    ScaleAnimation btn00_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f); //TODO CHECK
    ScaleAnimation btn01_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn02_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn03_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn04_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn05_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn06_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn07_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn08_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn11_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn12_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn13_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn14_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn21_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn22_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn23_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn24_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn31_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn32_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn33_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn34_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn41_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn42_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn43_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
    ScaleAnimation btn44_3 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);

    // Left (4)
    ScaleAnimation btn00_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f); //TODO CHECK
    ScaleAnimation btn01_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn02_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn03_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn04_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn05_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn06_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn07_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn08_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn11_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn12_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn13_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn14_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn21_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn22_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn23_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn24_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn31_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn32_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn33_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn34_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn41_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn42_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn43_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation btn44_4 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);

    ScaleAnimation scaleAnimations[][] = {
            {btn00, btn01, btn02, btn03, btn04, btn05, btn06, btn07, btn08, btn11, btn12, btn13, btn14, btn21, btn22, btn23, btn24, btn31, btn32, btn33, btn34, btn41, btn42, btn43, btn44},
            {btn00_1, btn01_1, btn02_1, btn03_1, btn04_1, btn05_1, btn06_1, btn07_1, btn08_1, btn11_1, btn12_1, btn13_1, btn14_1, btn21_1, btn22_1, btn23_1, btn24_1, btn31_1, btn32_1, btn33_1, btn34_1, btn41_1, btn42_1, btn43_1, btn44_1},
            {btn00_2, btn01_2, btn02_2, btn03_2, btn04_2, btn05_2, btn06_2, btn07_2, btn08_2, btn11_2, btn12_2, btn13_2, btn14_2, btn21_2, btn22_2, btn23_2, btn24_2, btn31_2, btn32_2, btn33_2, btn34_2, btn41_2, btn42_2, btn43_2, btn44_2},
            {btn00_3, btn01_3, btn02_3, btn03_3, btn04_3, btn05_3, btn06_3, btn07_3, btn08_3, btn11_3, btn12_3, btn13_3, btn14_3, btn21_3, btn22_3, btn23_3, btn24_3, btn31_3, btn32_3, btn33_3, btn34_3, btn41_3, btn42_3, btn43_3, btn44_3},
            {btn00_4, btn01_4, btn02_4, btn03_4, btn04_4, btn05_4, btn06_4, btn07_4, btn08_4, btn11_4, btn12_4, btn13_4, btn14_4, btn21_4, btn22_4, btn23_4, btn24_4, btn31_4, btn32_4, btn33_4, btn34_4, btn41_4, btn42_4, btn43_4, btn44_4}
    };

    //TODO: add swipe tutorials

    public void playMotion(int viewId, int toggleNumber, Activity activity) {
        // Animator
        final View view = (View) activity.findViewById(viewId);

        switch (viewId){
            case R.id.btn00_tutorial: playAnimation(view, scaleAnimations[toggleNumber][0] ); break;
            case R.id.tgl1_tutorial : playAnimation(view, scaleAnimations[toggleNumber][1] ); break;
            case R.id.tgl2_tutorial : playAnimation(view, scaleAnimations[toggleNumber][2] ); break;
            case R.id.tgl3_tutorial : playAnimation(view, scaleAnimations[toggleNumber][3] ); break;
            case R.id.tgl4_tutorial : playAnimation(view, scaleAnimations[toggleNumber][4] ); break;
            case R.id.tgl5_tutorial : playAnimation(view, scaleAnimations[toggleNumber][5] ); break;
            case R.id.tgl6_tutorial : playAnimation(view, scaleAnimations[toggleNumber][6] ); break;
            case R.id.tgl7_tutorial : playAnimation(view, scaleAnimations[toggleNumber][7] ); break;
            case R.id.tgl8_tutorial : playAnimation(view, scaleAnimations[toggleNumber][8] ); break;
            case R.id.btn11_tutorial: playAnimation(view, scaleAnimations[toggleNumber][9] ); break;
            case R.id.btn12_tutorial: playAnimation(view, scaleAnimations[toggleNumber][10]); break;
            case R.id.btn13_tutorial: playAnimation(view, scaleAnimations[toggleNumber][11]); break;
            case R.id.btn14_tutorial: playAnimation(view, scaleAnimations[toggleNumber][12]); break;
            case R.id.btn21_tutorial: playAnimation(view, scaleAnimations[toggleNumber][13]); break;
            case R.id.btn22_tutorial: playAnimation(view, scaleAnimations[toggleNumber][14]); break;
            case R.id.btn23_tutorial: playAnimation(view, scaleAnimations[toggleNumber][15]); break;
            case R.id.btn24_tutorial: playAnimation(view, scaleAnimations[toggleNumber][16]); break;
            case R.id.btn31_tutorial: playAnimation(view, scaleAnimations[toggleNumber][17]); break;
            case R.id.btn32_tutorial: playAnimation(view, scaleAnimations[toggleNumber][18]); break;
            case R.id.btn33_tutorial: playAnimation(view, scaleAnimations[toggleNumber][19]); break;
            case R.id.btn34_tutorial: playAnimation(view, scaleAnimations[toggleNumber][20]); break;
            case R.id.btn41_tutorial: playAnimation(view, scaleAnimations[toggleNumber][21]); break;
            case R.id.btn42_tutorial: playAnimation(view, scaleAnimations[toggleNumber][22]); break;
            case R.id.btn43_tutorial: playAnimation(view, scaleAnimations[toggleNumber][23]); break;
            case R.id.btn44_tutorial: playAnimation(view, scaleAnimations[toggleNumber][24]); break;

            default: Log.i("Tutorial Service", "Method called without any matching number"); break;
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