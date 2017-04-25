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
import com.bedrock.padder.model.preset.DeckTiming;
import com.bedrock.padder.model.preset.Preset;

import java.util.Arrays;

import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class TutorialService extends Activity {

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
                /* 44 */ {54.858, 82.300, 83.433, 109.725, 182.867, 210.308, 223.717, -1}
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
    Handler mHandler = new Handler();
    Activity a;
    // motions
    // Normal
    Runnable motion00;
    Runnable motion01;
    Runnable motion02;
    Runnable motion03;
    Runnable motion04;
    Runnable motion05;
    Runnable motion06;
    Runnable motion07;
    Runnable motion08;
    Runnable motion11;
    Runnable motion12;
    Runnable motion13;
    Runnable motion14;
    Runnable motion21;
    Runnable motion22;
    Runnable motion23;
    Runnable motion24;
    Runnable motion31;
    Runnable motion32;
    Runnable motion33;
    Runnable motion34;
    Runnable motion41;
    Runnable motion42;
    Runnable motion43;
    Runnable motion44;
    // Up
    Runnable motion00_1;
    Runnable motion01_1;
    Runnable motion02_1;
    Runnable motion03_1;
    Runnable motion04_1;
    Runnable motion05_1;
    Runnable motion06_1;
    Runnable motion07_1;
    Runnable motion08_1;
    Runnable motion11_1;
    Runnable motion12_1;
    Runnable motion13_1;
    Runnable motion14_1;
    Runnable motion21_1;
    Runnable motion22_1;
    Runnable motion23_1;
    Runnable motion24_1;
    Runnable motion31_1;
    Runnable motion32_1;
    Runnable motion33_1;
    Runnable motion34_1;
    Runnable motion41_1;
    Runnable motion42_1;
    Runnable motion43_1;
    Runnable motion44_1;
    // Right
    Runnable motion00_2;
    Runnable motion01_2;
    Runnable motion02_2;
    Runnable motion03_2;
    Runnable motion04_2;
    Runnable motion05_2;
    Runnable motion06_2;
    Runnable motion07_2;
    Runnable motion08_2;
    Runnable motion11_2;
    Runnable motion12_2;
    Runnable motion13_2;
    Runnable motion14_2;
    Runnable motion21_2;
    Runnable motion22_2;
    Runnable motion23_2;
    Runnable motion24_2;
    Runnable motion31_2;
    Runnable motion32_2;
    Runnable motion33_2;
    Runnable motion34_2;
    Runnable motion41_2;
    Runnable motion42_2;
    Runnable motion43_2;
    Runnable motion44_2;
    // Down
    Runnable motion00_3;
    Runnable motion01_3;
    Runnable motion02_3;
    Runnable motion03_3;
    Runnable motion04_3;
    Runnable motion05_3;
    Runnable motion06_3;
    Runnable motion07_3;
    Runnable motion08_3;
    Runnable motion11_3;
    Runnable motion12_3;
    Runnable motion13_3;
    Runnable motion14_3;
    Runnable motion21_3;
    Runnable motion22_3;
    Runnable motion23_3;
    Runnable motion24_3;
    Runnable motion31_3;
    Runnable motion32_3;
    Runnable motion33_3;
    Runnable motion34_3;
    Runnable motion41_3;
    Runnable motion42_3;
    Runnable motion43_3;
    Runnable motion44_3;
    // Left
    Runnable motion00_4;
    Runnable motion01_4;
    Runnable motion02_4;
    Runnable motion03_4;
    Runnable motion04_4;
    Runnable motion05_4;
    Runnable motion06_4;
    Runnable motion07_4;
    Runnable motion08_4;
    Runnable motion11_4;
    Runnable motion12_4;
    Runnable motion13_4;
    Runnable motion14_4;
    Runnable motion21_4;
    Runnable motion22_4;
    Runnable motion23_4;
    Runnable motion24_4;
    Runnable motion31_4;
    Runnable motion32_4;
    Runnable motion33_4;
    Runnable motion34_4;
    Runnable motion41_4;
    Runnable motion42_4;
    Runnable motion43_4;
    Runnable motion44_4;
    Runnable motions[] = {
            motion00, // 00
            motion00_1,
            motion00_2,
            motion00_3,
            motion00_4,
            motion01, // Deck
            motion02,
            motion03,
            motion04,
            motion11, // Others
            motion11_1,
            motion11_2,
            motion11_3,
            motion11_4,
            motion12,
            motion12_1,
            motion12_2,
            motion12_3,
            motion12_4,
            motion13,
            motion13_1,
            motion13_2,
            motion13_3,
            motion13_4,
            motion14,
            motion00_1,
            motion00_2,
            motion00_3,
            motion00_4,
            motion21,
            motion21_1,
            motion21_2,
            motion21_3,
            motion21_4,
            motion22,
            motion22_1,
            motion22_2,
            motion22_3,
            motion22_4,
            motion23,
            motion23_1,
            motion23_2,
            motion23_3,
            motion23_4,
            motion24,
            motion24_1,
            motion24_2,
            motion24_3,
            motion24_4,
            motion31,
            motion31_1,
            motion31_2,
            motion31_3,
            motion31_4,
            motion32,
            motion32_1,
            motion32_2,
            motion32_3,
            motion32_4,
            motion33,
            motion33_1,
            motion33_2,
            motion33_3,
            motion33_4,
            motion34,
            motion34_1,
            motion34_2,
            motion34_3,
            motion34_4,
            motion41,
            motion41_1,
            motion41_2,
            motion41_3,
            motion41_4,
            motion42,
            motion42_1,
            motion42_2,
            motion42_3,
            motion42_4,
            motion43,
            motion43_1,
            motion43_2,
            motion43_3,
            motion43_4,
            motion44,
            motion44_1,
            motion44_2,
            motion44_3,
            motion44_4
    };
    int indexI;
    int indexJ;
    int tutorialCurrentIndex = 0;
    int buttonIds[] = {
            R.id.btn00_tutorial, // 00
            R.id.btn00_tutorial,
            R.id.btn00_tutorial,
            R.id.btn00_tutorial,
            R.id.btn00_tutorial,
            R.id.tgl1_tutorial, // Deck
            R.id.tgl2_tutorial,
            R.id.tgl3_tutorial,
            R.id.tgl4_tutorial,
            R.id.btn11_tutorial, // Others
            R.id.btn11_tutorial,
            R.id.btn11_tutorial,
            R.id.btn11_tutorial,
            R.id.btn11_tutorial,
            R.id.btn12_tutorial,
            R.id.btn12_tutorial,
            R.id.btn12_tutorial,
            R.id.btn12_tutorial,
            R.id.btn12_tutorial,
            R.id.btn13_tutorial,
            R.id.btn13_tutorial,
            R.id.btn13_tutorial,
            R.id.btn13_tutorial,
            R.id.btn13_tutorial,
            R.id.btn14_tutorial,
            R.id.btn00_tutorial,
            R.id.btn00_tutorial,
            R.id.btn00_tutorial,
            R.id.btn00_tutorial,
            R.id.btn21_tutorial,
            R.id.btn21_tutorial,
            R.id.btn21_tutorial,
            R.id.btn21_tutorial,
            R.id.btn21_tutorial,
            R.id.btn22_tutorial,
            R.id.btn22_tutorial,
            R.id.btn22_tutorial,
            R.id.btn22_tutorial,
            R.id.btn22_tutorial,
            R.id.btn23_tutorial,
            R.id.btn23_tutorial,
            R.id.btn23_tutorial,
            R.id.btn23_tutorial,
            R.id.btn23_tutorial,
            R.id.btn24_tutorial,
            R.id.btn24_tutorial,
            R.id.btn24_tutorial,
            R.id.btn24_tutorial,
            R.id.btn24_tutorial,
            R.id.btn31_tutorial,
            R.id.btn31_tutorial,
            R.id.btn31_tutorial,
            R.id.btn31_tutorial,
            R.id.btn31_tutorial,
            R.id.btn32_tutorial,
            R.id.btn32_tutorial,
            R.id.btn32_tutorial,
            R.id.btn32_tutorial,
            R.id.btn32_tutorial,
            R.id.btn33_tutorial,
            R.id.btn33_tutorial,
            R.id.btn33_tutorial,
            R.id.btn33_tutorial,
            R.id.btn33_tutorial,
            R.id.btn34_tutorial,
            R.id.btn34_tutorial,
            R.id.btn34_tutorial,
            R.id.btn34_tutorial,
            R.id.btn34_tutorial,
            R.id.btn41_tutorial,
            R.id.btn41_tutorial,
            R.id.btn41_tutorial,
            R.id.btn41_tutorial,
            R.id.btn41_tutorial,
            R.id.btn42_tutorial,
            R.id.btn42_tutorial,
            R.id.btn42_tutorial,
            R.id.btn42_tutorial,
            R.id.btn42_tutorial,
            R.id.btn43_tutorial,
            R.id.btn43_tutorial,
            R.id.btn43_tutorial,
            R.id.btn43_tutorial,
            R.id.btn43_tutorial,
            R.id.btn44_tutorial,
            R.id.btn44_tutorial,
            R.id.btn44_tutorial,
            R.id.btn44_tutorial,
            R.id.btn44_tutorial
    };
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
    ScaleAnimation scaleAnimations[] = {
            btn00, // 00
            btn00_1,
            btn00_2,
            btn00_3,
            btn00_4,
            btn01, // Deck
            btn02,
            btn03,
            btn04,
            btn11, // Others
            btn11_1,
            btn11_2,
            btn11_3,
            btn11_4,
            btn12,
            btn12_1,
            btn12_2,
            btn12_3,
            btn12_4,
            btn13,
            btn13_1,
            btn13_2,
            btn13_3,
            btn13_4,
            btn14,
            btn00_1,
            btn00_2,
            btn00_3,
            btn00_4,
            btn21,
            btn21_1,
            btn21_2,
            btn21_3,
            btn21_4,
            btn22,
            btn22_1,
            btn22_2,
            btn22_3,
            btn22_4,
            btn23,
            btn23_1,
            btn23_2,
            btn23_3,
            btn23_4,
            btn24,
            btn24_1,
            btn24_2,
            btn24_3,
            btn24_4,
            btn31,
            btn31_1,
            btn31_2,
            btn31_3,
            btn31_4,
            btn32,
            btn32_1,
            btn32_2,
            btn32_3,
            btn32_4,
            btn33,
            btn33_1,
            btn33_2,
            btn33_3,
            btn33_4,
            btn34,
            btn34_1,
            btn34_2,
            btn34_3,
            btn34_4,
            btn41,
            btn41_1,
            btn41_2,
            btn41_3,
            btn41_4,
            btn42,
            btn42_1,
            btn42_2,
            btn42_3,
            btn42_4,
            btn43,
            btn43_1,
            btn43_2,
            btn43_3,
            btn43_4,
            btn44,
            btn44_1,
            btn44_2,
            btn44_3,
            btn44_4
    };
    private AnimService anim = new AnimService();
    private WindowService window = new WindowService();
    private int count;
    private int i;
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
    int motionDelays[] = {
            motionDelay00, // 00
            motionDelay00_1,
            motionDelay00_2,
            motionDelay00_3,
            motionDelay00_4,
            motionDelay01, // Deck
            motionDelay02,
            motionDelay03,
            motionDelay04,
            motionDelay11, // Others
            motionDelay11_1,
            motionDelay11_2,
            motionDelay11_3,
            motionDelay11_4,
            motionDelay12,
            motionDelay12_1,
            motionDelay12_2,
            motionDelay12_3,
            motionDelay12_4,
            motionDelay13,
            motionDelay13_1,
            motionDelay13_2,
            motionDelay13_3,
            motionDelay13_4,
            motionDelay14,
            motionDelay00_1,
            motionDelay00_2,
            motionDelay00_3,
            motionDelay00_4,
            motionDelay21,
            motionDelay21_1,
            motionDelay21_2,
            motionDelay21_3,
            motionDelay21_4,
            motionDelay22,
            motionDelay22_1,
            motionDelay22_2,
            motionDelay22_3,
            motionDelay22_4,
            motionDelay23,
            motionDelay23_1,
            motionDelay23_2,
            motionDelay23_3,
            motionDelay23_4,
            motionDelay24,
            motionDelay24_1,
            motionDelay24_2,
            motionDelay24_3,
            motionDelay24_4,
            motionDelay31,
            motionDelay31_1,
            motionDelay31_2,
            motionDelay31_3,
            motionDelay31_4,
            motionDelay32,
            motionDelay32_1,
            motionDelay32_2,
            motionDelay32_3,
            motionDelay32_4,
            motionDelay33,
            motionDelay33_1,
            motionDelay33_2,
            motionDelay33_3,
            motionDelay33_4,
            motionDelay34,
            motionDelay34_1,
            motionDelay34_2,
            motionDelay34_3,
            motionDelay34_4,
            motionDelay41,
            motionDelay41_1,
            motionDelay41_2,
            motionDelay41_3,
            motionDelay41_4,
            motionDelay42,
            motionDelay42_1,
            motionDelay42_2,
            motionDelay42_3,
            motionDelay42_4,
            motionDelay43,
            motionDelay43_1,
            motionDelay43_2,
            motionDelay43_3,
            motionDelay43_4,
            motionDelay44,
            motionDelay44_1,
            motionDelay44_2,
            motionDelay44_3,
            motionDelay44_4
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

    //Asynctask
//    private TextView progress;
//    private int progressCount;
//    private int presetTutorialCount = 85;
//
//    private void progressUpdate() {
//        progress.setText(
//                activity.getResources().getString(R.string.progressbar_loading_preset_progress) + " "
//                        + progressCount++ + " / " + presetTutorialCount);
//    }
//
//    private AsyncTask loadTutorial = null;
//
//    private class LoadTutorial extends AsyncTask<Void, Integer, String> {
//        String TAG = "LoadTutorial";
//
//        protected void onPreExecute() {
//            Log.d(TAG, "On preExceute, set UI");
//
//            window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.icon_tutorial_disabled);
//            window.getImageView(R.id.layout_settings_tutorial_icon, activity).setImageResource(R.drawable.settings_tutorial_disabled);
//        }
//
//        protected String doInBackground(Void... arg0) {
//            Log.d(TAG, "On doInBackground, start loading tutorials");
//
//            if (currentPreset != null) {
//                Log.i(TAG, "Preset \"" + window.getStringFromId(currentPreset.getMusic().getNameId(), activity) + "\", id " + currentPreset.getId());
//
//            }
//
//            return "You are at PostExecute";
//        }
//
//        protected void onProgressUpdate(Integer... a) {
//            progressUpdate();
//        }
//
//        protected void onPostExecute(String result) {
//            Log.d(TAG, "sampleId count : " + presetTutorialCount);
//
//            progress = window.getTextView(R.id.progress_bar_progress_text, activity);
//
//            sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                @Override
//                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                    Log.d(TAG, "Loading Finished, sampleId : " + sampleId);
//                    progressUpdate();
//                    if(sampleId == presetTutorialCount) {
//                        // final sampleId
//                        Log.d(TAG, "Loading completed, SoundPool successfully loaded "
//                                + presetTutorialCount
//                                + " tutorials");
//
//                        window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset_done);
//                        progress.setText(
//                                activity.getResources().getString(R.string.progressbar_loading_preset_progress) + " "
//                                        + presetTutorialCount * 2 + " / " + presetTutorialCount * 2);
//                        isPresetLoaded = true;
//
//                        // Load finished, set AsyncTask objects to null
//                        LoadTutorial = null;
//                        unLoadTutorial = null;
//
//                        window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.icon_tutorial);
//                        window.getImageView(R.id.layout_settings_tutorial_icon, activity).setImageResource(R.drawable.settings_tutorial);
//
//                        anim.fadeOut(R.id.progress_bar_layout, 400, 400, activity);
//                        MainActivity main = new MainActivity();
//                        main.setQuickstart(activity);
//
//                        Handler setText = new Handler();
//                        setText.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset);
//                                window.getTextView(R.id.progress_bar_progress_text, activity).setText(R.string.progressbar_loading_preset_progress_placeholder);
//                            }
//                        }, 800);
//
//                        main.isPresetLoading = false;
//                    }
//                }
//            });
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            Log.d("TAG", "LoadTutorial successfully canceled");
//        }
//    }
//
//    void startTutorial(Activity activity) {
//        this.activity = activity;
//    }

    void resetMotionDelayIndexes() {
        for (int i = 0; i < motionDelayIndexes.length; i++) {
            motionDelayIndexes[i] = 0;
            Log.d("MDIS", "Cleared at [" + i + "]");
        }
    }

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
    short motionDelayIndexes[] = {
            motionDelayIndex00, // 00
            motionDelayIndex00_1,
            motionDelayIndex00_2,
            motionDelayIndex00_3,
            motionDelayIndex00_4,
            motionDelayIndex01, // Deck
            motionDelayIndex02,
            motionDelayIndex03,
            motionDelayIndex04,
            motionDelayIndex11, // Others
            motionDelayIndex11_1,
            motionDelayIndex11_2,
            motionDelayIndex11_3,
            motionDelayIndex11_4,
            motionDelayIndex12,
            motionDelayIndex12_1,
            motionDelayIndex12_2,
            motionDelayIndex12_3,
            motionDelayIndex12_4,
            motionDelayIndex13,
            motionDelayIndex13_1,
            motionDelayIndex13_2,
            motionDelayIndex13_3,
            motionDelayIndex13_4,
            motionDelayIndex14,
            motionDelayIndex00_1,
            motionDelayIndex00_2,
            motionDelayIndex00_3,
            motionDelayIndex00_4,
            motionDelayIndex21,
            motionDelayIndex21_1,
            motionDelayIndex21_2,
            motionDelayIndex21_3,
            motionDelayIndex21_4,
            motionDelayIndex22,
            motionDelayIndex22_1,
            motionDelayIndex22_2,
            motionDelayIndex22_3,
            motionDelayIndex22_4,
            motionDelayIndex23,
            motionDelayIndex23_1,
            motionDelayIndex23_2,
            motionDelayIndex23_3,
            motionDelayIndex23_4,
            motionDelayIndex24,
            motionDelayIndex24_1,
            motionDelayIndex24_2,
            motionDelayIndex24_3,
            motionDelayIndex24_4,
            motionDelayIndex31,
            motionDelayIndex31_1,
            motionDelayIndex31_2,
            motionDelayIndex31_3,
            motionDelayIndex31_4,
            motionDelayIndex32,
            motionDelayIndex32_1,
            motionDelayIndex32_2,
            motionDelayIndex32_3,
            motionDelayIndex32_4,
            motionDelayIndex33,
            motionDelayIndex33_1,
            motionDelayIndex33_2,
            motionDelayIndex33_3,
            motionDelayIndex33_4,
            motionDelayIndex34,
            motionDelayIndex34_1,
            motionDelayIndex34_2,
            motionDelayIndex34_3,
            motionDelayIndex34_4,
            motionDelayIndex41,
            motionDelayIndex41_1,
            motionDelayIndex41_2,
            motionDelayIndex41_3,
            motionDelayIndex41_4,
            motionDelayIndex42,
            motionDelayIndex42_1,
            motionDelayIndex42_2,
            motionDelayIndex42_3,
            motionDelayIndex42_4,
            motionDelayIndex43,
            motionDelayIndex43_1,
            motionDelayIndex43_2,
            motionDelayIndex43_3,
            motionDelayIndex43_4,
            motionDelayIndex44,
            motionDelayIndex44_1,
            motionDelayIndex44_2,
            motionDelayIndex44_3,
            motionDelayIndex44_4
    };
    private int timing[][];
    private int deckButtonIds[] = {
            R.id.tgl1,
            R.id.tgl2,
            R.id.tgl3,
            R.id.tgl4
    };

    // old methods

    public void tutorialStart(final Activity activity) {
        tutorialLoad(activity);
    }

    void tutorialLoad(final Activity activity) {
        Log.i("TutorialService", "tutorial loaded");

        final double array[][];
        SharedPreferences prefs = activity.getSharedPreferences(APPLICATION_ID, activity.MODE_PRIVATE);
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

    void tutorialPlay(final double array[][], final Activity activity) {
        Log.i("TutorialService", "tutorial started");
        window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset);
        for (count = 0; count < 24; count++) {
            i = 0;

            final int btnNum[] = {-1};
            btnNum[0] = countToButtonNumber(count);

            //TODO LOGIC CHECK

            final int delay;

            if (count == 1 && i == 0) {
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
            mHandler.removeCallbacksAndMessages(null);
            window.getImageView(R.id.toolbar_tutorial_icon, a).setImageResource(R.drawable.icon_tutorial);
            window.getImageView(R.id.layout_settings_tutorial_icon, a).setImageResource(R.drawable.settings_tutorial);
            Log.i("TutorialService", "tutorial finished");
        } catch (NullPointerException e) {
            Log.e("tutorialService", "NPE, failed to remove callback from handler");
        }
    }

    // new methods

    public void startTutorial(final int index, Activity activity) {
        DeckTiming deckTiming = getCurrentPreset().getMusic().getDeckTimings()[index];
        Integer currentTiming[][] = deckTiming.getDeckTiming();

        this.a = activity;

        int deckTimingIds[] = new int[4];
        for (int j = 0; j < 4; j++) {
            if (j == deckTiming.getDeckStartingId()) {
                deckTimingIds[j] = deckTiming.getDeckStartTiming();
                Log.d("deckTiming", "selected deck is " + deckTiming.getDeckStartingId() + " at " + deckTiming.getDeckStartTiming());
            } else {
                deckTimingIds[j] = -1;
            }
        }

        // initialize the array with actual values
        int timingInit[][] = {
                getIntArray(currentTiming[0]),
                getIntArray(currentTiming[1]),
                getIntArray(currentTiming[2]),
                getIntArray(currentTiming[3]),
                getIntArray(currentTiming[4]),
                {deckTimingIds[0]},
                {deckTimingIds[0]},
                {deckTimingIds[0]},
                {deckTimingIds[0]},
                getIntArray(currentTiming[5]),
                getIntArray(currentTiming[6]),
                getIntArray(currentTiming[7]),
                getIntArray(currentTiming[8]),
                getIntArray(currentTiming[9]),
                getIntArray(currentTiming[10]),
                getIntArray(currentTiming[11]),
                getIntArray(currentTiming[12]),
                getIntArray(currentTiming[13]),
                getIntArray(currentTiming[14]),
                getIntArray(currentTiming[15]),
                getIntArray(currentTiming[16]),
                getIntArray(currentTiming[17]),
                getIntArray(currentTiming[18]),
                getIntArray(currentTiming[19]),
                getIntArray(currentTiming[20]),
                getIntArray(currentTiming[21]),
                getIntArray(currentTiming[22]),
                getIntArray(currentTiming[23]),
                getIntArray(currentTiming[24]),
                getIntArray(currentTiming[25]),
                getIntArray(currentTiming[26]),
                getIntArray(currentTiming[27]),
                getIntArray(currentTiming[28]),
                getIntArray(currentTiming[29]),
                getIntArray(currentTiming[30]),
                getIntArray(currentTiming[31]),
                getIntArray(currentTiming[32]),
                getIntArray(currentTiming[33]),
                getIntArray(currentTiming[34]),
                getIntArray(currentTiming[35]),
                getIntArray(currentTiming[36]),
                getIntArray(currentTiming[37]),
                getIntArray(currentTiming[38]),
                getIntArray(currentTiming[39]),
                getIntArray(currentTiming[40]),
                getIntArray(currentTiming[41]),
                getIntArray(currentTiming[42]),
                getIntArray(currentTiming[43]),
                getIntArray(currentTiming[44]),
                getIntArray(currentTiming[45]),
                getIntArray(currentTiming[46]),
                getIntArray(currentTiming[47]),
                getIntArray(currentTiming[48]),
                getIntArray(currentTiming[49]),
                getIntArray(currentTiming[50]),
                getIntArray(currentTiming[51]),
                getIntArray(currentTiming[52]),
                getIntArray(currentTiming[53]),
                getIntArray(currentTiming[54]),
                getIntArray(currentTiming[55]),
                getIntArray(currentTiming[56]),
                getIntArray(currentTiming[57]),
                getIntArray(currentTiming[58]),
                getIntArray(currentTiming[59]),
                getIntArray(currentTiming[60]),
                getIntArray(currentTiming[61]),
                getIntArray(currentTiming[62]),
                getIntArray(currentTiming[63]),
                getIntArray(currentTiming[64]),
                getIntArray(currentTiming[65]),
                getIntArray(currentTiming[66]),
                getIntArray(currentTiming[67]),
                getIntArray(currentTiming[68]),
                getIntArray(currentTiming[69]),
                getIntArray(currentTiming[70]),
                getIntArray(currentTiming[71]),
                getIntArray(currentTiming[72]),
                getIntArray(currentTiming[73]),
                getIntArray(currentTiming[74]),
                getIntArray(currentTiming[75]),
                getIntArray(currentTiming[76]),
                getIntArray(currentTiming[77]),
                getIntArray(currentTiming[78]),
                getIntArray(currentTiming[79]),
                getIntArray(currentTiming[80]),
                getIntArray(currentTiming[81]),
                getIntArray(currentTiming[82]),
                getIntArray(currentTiming[83]),
                getIntArray(currentTiming[84])
        };
        // Overwrite the timing with new values
        timing = timingInit;
        Log.d("timing", Arrays.deepToString(timing));
        //setRunnables(timing, activity);

        //if(tutorialCurrentIndex >= 1 && tutorialCurrentIndex <= 4) {
        //    // if tutorial is ongoing, set next deck button id
        //    window.getView(deckButtonIds[tutorialCurrentIndex - 1], a).setOnClickListener(new View.OnClickListener() {
        //        public void onClick(View v) {
        //            startTutorial(index + 1);
        //            Log.d("tutorialService", "tutorial deck " + index + " finished");
        //        }
        //    });
        //}

        setRunnable();
    }

    void setRunnable() {
        // initialize motion runnables
        motions = new Runnable[]{
                getRunnable(0),
                getRunnable(1),
                getRunnable(2),
                getRunnable(3),
                getRunnable(4),
                getRunnable(5),
                getRunnable(6),
                getRunnable(7),
                getRunnable(8),
                getRunnable(9),
                getRunnable(10),
                getRunnable(11),
                getRunnable(12),
                getRunnable(13),
                getRunnable(14),
                getRunnable(15),
                getRunnable(16),
                getRunnable(17),
                getRunnable(18),
                getRunnable(19),
                getRunnable(20),
                getRunnable(21),
                getRunnable(22),
                getRunnable(23),
                getRunnable(24),
                getRunnable(25),
                getRunnable(26),
                getRunnable(27),
                getRunnable(28),
                getRunnable(29),
                getRunnable(30),
                getRunnable(31),
                getRunnable(32),
                getRunnable(33),
                getRunnable(34),
                getRunnable(35),
                getRunnable(36),
                getRunnable(37),
                getRunnable(38),
                getRunnable(39),
                getRunnable(40),
                getRunnable(41),
                getRunnable(42),
                getRunnable(43),
                getRunnable(44),
                getRunnable(45),
                getRunnable(46),
                getRunnable(47),
                getRunnable(48),
                getRunnable(49),
                getRunnable(50),
                getRunnable(51),
                getRunnable(52),
                getRunnable(53),
                getRunnable(54),
                getRunnable(55),
                getRunnable(56),
                getRunnable(57),
                getRunnable(58),
                getRunnable(59),
                getRunnable(60),
                getRunnable(61),
                getRunnable(62),
                getRunnable(63),
                getRunnable(64),
                getRunnable(65),
                getRunnable(66),
                getRunnable(67),
                getRunnable(68),
                getRunnable(69),
                getRunnable(70),
                getRunnable(71),
                getRunnable(72),
                getRunnable(73),
                getRunnable(74),
                getRunnable(75),
                getRunnable(76),
                getRunnable(77),
                getRunnable(78),
                getRunnable(79),
                getRunnable(80),
                getRunnable(81),
                getRunnable(82),
                getRunnable(83),
                getRunnable(84),
                getRunnable(85),
                getRunnable(86),
                getRunnable(87),
                getRunnable(88)
        };
        startRunnable();
    }

    void startRunnable() {
        for (int i = 0; i < 89; i++) {
            Log.d(TAG, "Runnable started by index " + i);
            motions[i].run();
        }
    }

    Runnable getRunnable(final int index) {
        Log.d(TAG, "getRunnable(" + index + ")");
        return new Runnable() {
            @Override
            public void run() {
                // TODO timing not right
                // check timing exists
                if (timing[index].length <= 1) {
                    removeCallbacksWithDelay(this, index);
                    motionDelays[index] = -1;
                    Log.d("motionDelay", "null timing, Set to " + motionDelays[index]);
                    motionDelayIndexes[index] = 0;
                } else {
                    if (motionDelays[index] == -1) {
                        // first run
                        motionDelays[index] = timing[index][0];
                        mHandler.postDelayed(motions[index], motionDelays[index]);
                        Log.d("motionDelay", "Set to " + motionDelays[index]);
                    } else {
                        try {
                            // animate
                            motionAnimation(index, a);
                            if (motionDelayIndexes[index] < timing[index].length - 1) {
                                motionDelays[index] = (timing[index][motionDelayIndexes[index] + 1] - timing[index][motionDelayIndexes[index]]);
                                Log.d("motionDelay", "Set to " + motionDelays[index] + " at " + index);
                                motionDelayIndexes[index]++;
                                Log.d("motionDelayIndex", "Set to " + motionDelayIndexes[index] + " at " + index);
                            } else {
                                Log.d("Array", "Finished");
                                removeCallbacksWithDelay(this, index);
                            }
                        } finally {
                            if (motionDelays[index] > 0) {
                                mHandler.postDelayed(motions[index], motionDelays[index]);
                            } else {
                                removeCallbacksWithDelay(this, index);
                                motionDelays[index] = -1;
                                Log.d("motionDelay", "Set to " + motionDelays[index]);
                                motionDelayIndexes[index] = 0;
                            }
                        }
                    }
                }
            }
        };
    }

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

    public int getCurrentTutorialDeckId() {
        if (tutorialCurrentIndex < getMaxTutorialCount()) {
            // legit
            return tutorialCurrentIndex++;
        } else {
            // tutorial ended
            return -1;
        }
    }

    public void initCurrentTiming() {
        Log.d("init", "tutorialCurrentIndex set to 0");
        tutorialCurrentIndex = 0;
    }

    public int getMaxTutorialCount() {
        return currentPreset.getMusic().getDeckTimings().length;
    }

    int[][] setFinalItemInArray(int timing[][]) {
        int arr[][] = new int[85][];
        //TODO try
        for (int i = 0; i < timing.length; i++) {
            if (timing[i][timing[i].length - 1] != -1) {
                // if last item != -1 then change to then -1
                timing[i][timing[i].length - 1] = -1;
            }
        }
        return arr;
    }

    int[] getIntArray(Integer integerArray[]) {
        int array[] = new int[integerArray.length];
        for (int i = 0; i < integerArray.length; i++) {
            if (integerArray[i] == null) {
                array[i] = -1;
            } else {
                array[i] = integerArray[i];
            }
        }
        return array;
    }

    void removeCallbacksWithDelay(Runnable runnable, int index) {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //mHandler.removeCallbacks(motions[index]);
//                //Log.d("removeCallbacks", "Callback removed from " + index);
//                Log.d("removeCallbacks", "need to remove callback from " + index);
//            }
//        }, 0); //TODO MAYBE CHANGE
        mHandler.removeCallbacks(runnable);
        Log.d("removeCallbacks", "Callback removed from " + index);
    }

    private String TAG = "TutorialService";

    void setRunnables(final int timing[][], final Activity activity) {
        for (indexI = 0; indexI < 89; indexI++) {
            motions[indexI] = new Runnable() {
                @Override
                public void run() {
                    // check timing exists
                    if (timing[indexI].length <= 1) {
                        //removeCallbacksWithDelay(indexI);
                        motionDelays[indexI] = -1;
                        Log.d("motionDelay", "null timing, Set to " + motionDelays[indexI]);
                        motionDelayIndexes[indexI] = 0;
                    } else {
                        if (motionDelays[indexI] == -1) {
                            // first run
                            motionDelays[indexI] = timing[indexI][0];
                            mHandler.postDelayed(motions[indexI], motionDelays[indexI]);
                            Log.d("motionDelay", "Set to " + motionDelays[indexI]);
                        } else {
                            try {
                                // animate
                                motionAnimation(indexI, activity);
                                if (motionDelayIndexes[indexI] < timing[indexI].length) {
                                    motionDelays[indexI] = (timing[indexI][motionDelayIndexes[indexI] + 1] - timing[indexI][motionDelayIndexes[indexI]]);
                                    Log.d("motionDelay", "Set to " + motionDelays[indexI]);
                                    motionDelayIndexes[indexI]++;
                                } else {
                                    Log.d("Array", "Finished");
                                    //removeCallbacksWithDelay(indexI);
                                }
                            } finally {
                                if (motionDelays[indexI] > 0) {
                                    mHandler.postDelayed(motions[indexI], motionDelays[indexI]);
                                } else {
                                    //removeCallbacksWithDelay(indexI);
                                    motionDelays[indexI] = -1;
                                    Log.d("motionDelay", "Set to " + motionDelays[indexI]);
                                    motionDelayIndexes[indexI] = 0;
                                }
                            }
                        }
                    }
                }
            };
            Log.d(TAG, "Runnable " + (indexI + 1) + " initialized");
            //motions[indexI].run();
        }
    }

    Preset getCurrentPreset() {
        return currentPreset;
    }

    void motionAnimationDelay(final int buttonNumber, int delay, final Activity activity) {
        Handler motionDelay = new Handler();
        motionDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                //motionAnimation(buttonNumber, activity);
            }
        }, delay);
    }

    void motionAnimation(int id, Activity activity) {
        Log.d("motionAnimation", "at " + id);
        playAnimation(buttonIds[id], scaleAnimations[id], activity);
    }

    void playMotionAnimation(int btnId, int phId, int motionId, Activity activity) {
        VideoView videoView;
        View placeholder;

        videoView = (VideoView) activity.findViewById(btnId);
        placeholder = (View) activity.findViewById(phId);
        animator(videoView, placeholder, motionId, activity);
        Log.i("MotionAnimation", "Animation played on " + activity.getResources().getResourceEntryName(phId));
    }

    //TODO: add swipe tutorials

    public void playMotion(int viewId, Activity activity) {
        // Animator

        switch (viewId) {
            case R.id.btn00_tutorial:
                playAnimation(viewId, scaleAnimations[0], activity);
                break;
            case R.id.tgl1_tutorial:
                playAnimation(viewId, scaleAnimations[1], activity);
                break;
            case R.id.tgl2_tutorial:
                playAnimation(viewId, scaleAnimations[2], activity);
                break;
            case R.id.tgl3_tutorial:
                playAnimation(viewId, scaleAnimations[3], activity);
                break;
            case R.id.tgl4_tutorial:
                playAnimation(viewId, scaleAnimations[4], activity);
                break;
            case R.id.tgl5_tutorial:
                playAnimation(viewId, scaleAnimations[5], activity);
                break;
            case R.id.tgl6_tutorial:
                playAnimation(viewId, scaleAnimations[6], activity);
                break;
            case R.id.tgl7_tutorial:
                playAnimation(viewId, scaleAnimations[7], activity);
                break;
            case R.id.tgl8_tutorial:
                playAnimation(viewId, scaleAnimations[8], activity);
                break;
            case R.id.btn11_tutorial:
                playAnimation(viewId, scaleAnimations[9], activity);
                break;
            case R.id.btn12_tutorial:
                playAnimation(viewId, scaleAnimations[10], activity);
                break;
            case R.id.btn13_tutorial:
                playAnimation(viewId, scaleAnimations[11], activity);
                break;
            case R.id.btn14_tutorial:
                playAnimation(viewId, scaleAnimations[12], activity);
                break;
            case R.id.btn21_tutorial:
                playAnimation(viewId, scaleAnimations[13], activity);
                break;
            case R.id.btn22_tutorial:
                playAnimation(viewId, scaleAnimations[14], activity);
                break;
            case R.id.btn23_tutorial:
                playAnimation(viewId, scaleAnimations[15], activity);
                break;
            case R.id.btn24_tutorial:
                playAnimation(viewId, scaleAnimations[16], activity);
                break;
            case R.id.btn31_tutorial:
                playAnimation(viewId, scaleAnimations[17], activity);
                break;
            case R.id.btn32_tutorial:
                playAnimation(viewId, scaleAnimations[18], activity);
                break;
            case R.id.btn33_tutorial:
                playAnimation(viewId, scaleAnimations[19], activity);
                break;
            case R.id.btn34_tutorial:
                playAnimation(viewId, scaleAnimations[20], activity);
                break;
            case R.id.btn41_tutorial:
                playAnimation(viewId, scaleAnimations[21], activity);
                break;
            case R.id.btn42_tutorial:
                playAnimation(viewId, scaleAnimations[22], activity);
                break;
            case R.id.btn43_tutorial:
                playAnimation(viewId, scaleAnimations[23], activity);
                break;
            case R.id.btn44_tutorial:
                playAnimation(viewId, scaleAnimations[24], activity);
                break;

            default:
                Log.i("Tutorial Service", "Method called without any matching number");
                break;
        }
    }

    void playAnimation(final int viewId, ScaleAnimation animation, final Activity activity) {
        View view = window.getView(R.id.base_tutorial, activity).findViewById(viewId);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(300);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                anim.fadeOut(viewId, 0, 200, activity);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    void animator(final VideoView videoView, final View placeholder, int rawId, Activity activity) {
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
                if (state[0] == 1) {
                    placeholder.setVisibility(View.GONE);
                } else {
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