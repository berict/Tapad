package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.bedrock.padder.R;
import com.bedrock.padder.activity.MainActivity;
import com.bedrock.padder.model.preset.Preset;

public class SoundService {

    //TODO NEW PRESET : update sound count
    public static final int HELLO_SOUND_COUNT = 84;
    public static final int ROSES_SOUND_COUNT = 102;
    public static final int FADED_SOUND_COUNT = 105;

    public static final int PRESET_SOUND_COUNTS[] = {
            HELLO_SOUND_COUNT,
            ROSES_SOUND_COUNT,
            FADED_SOUND_COUNT
    };

    public SoundPool sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    public int toggle;

    int sp_id_1_00;
    int sp_id_1_00_1;
    int sp_id_1_00_2;
    int sp_id_1_00_3;
    int sp_id_1_00_4;
    int sp_id_2_00;
    int sp_id_2_00_1;
    int sp_id_2_00_2;
    int sp_id_2_00_3;
    int sp_id_2_00_4;
    int sp_id_3_00;
    int sp_id_3_00_1;
    int sp_id_3_00_2;
    int sp_id_3_00_3;
    int sp_id_3_00_4;
    int sp_id_4_00;
    int sp_id_4_00_1;
    int sp_id_4_00_2;
    int sp_id_4_00_3;
    int sp_id_4_00_4;
    int sp_id_1_01;
    int sp_id_1_01_1;
    int sp_id_1_01_2;
    int sp_id_1_01_3;
    int sp_id_1_01_4;
    int sp_id_2_01;
    int sp_id_2_01_1;
    int sp_id_2_01_2;
    int sp_id_2_01_3;
    int sp_id_2_01_4;
    int sp_id_3_01;
    int sp_id_3_01_1;
    int sp_id_3_01_2;
    int sp_id_3_01_3;
    int sp_id_3_01_4;
    int sp_id_4_01;
    int sp_id_4_01_1;
    int sp_id_4_01_2;
    int sp_id_4_01_3;
    int sp_id_4_01_4;
    int sp_id_1_02;
    int sp_id_1_02_1;
    int sp_id_1_02_2;
    int sp_id_1_02_3;
    int sp_id_1_02_4;
    int sp_id_2_02;
    int sp_id_2_02_1;
    int sp_id_2_02_2;
    int sp_id_2_02_3;
    int sp_id_2_02_4;
    int sp_id_3_02;
    int sp_id_3_02_1;
    int sp_id_3_02_2;
    int sp_id_3_02_3;
    int sp_id_3_02_4;
    int sp_id_4_02;
    int sp_id_4_02_1;
    int sp_id_4_02_2;
    int sp_id_4_02_3;
    int sp_id_4_02_4;
    int sp_id_1_03;
    int sp_id_1_03_1;
    int sp_id_1_03_2;
    int sp_id_1_03_3;
    int sp_id_1_03_4;
    int sp_id_2_03;
    int sp_id_2_03_1;
    int sp_id_2_03_2;
    int sp_id_2_03_3;
    int sp_id_2_03_4;
    int sp_id_3_03;
    int sp_id_3_03_1;
    int sp_id_3_03_2;
    int sp_id_3_03_3;
    int sp_id_3_03_4;
    int sp_id_4_03;
    int sp_id_4_03_1;
    int sp_id_4_03_2;
    int sp_id_4_03_3;
    int sp_id_4_03_4;
    int sp_id_1_04;
    int sp_id_1_04_1;
    int sp_id_1_04_2;
    int sp_id_1_04_3;
    int sp_id_1_04_4;
    int sp_id_2_04;
    int sp_id_2_04_1;
    int sp_id_2_04_2;
    int sp_id_2_04_3;
    int sp_id_2_04_4;
    int sp_id_3_04;
    int sp_id_3_04_1;
    int sp_id_3_04_2;
    int sp_id_3_04_3;
    int sp_id_3_04_4;
    int sp_id_4_04;
    int sp_id_4_04_1;
    int sp_id_4_04_2;
    int sp_id_4_04_3;
    int sp_id_4_04_4;
    int sp_id_1_11;
    int sp_id_1_11_1;
    int sp_id_1_11_2;
    int sp_id_1_11_3;
    int sp_id_1_11_4;
    int sp_id_2_11;
    int sp_id_2_11_1;
    int sp_id_2_11_2;
    int sp_id_2_11_3;
    int sp_id_2_11_4;
    int sp_id_3_11;
    int sp_id_3_11_1;
    int sp_id_3_11_2;
    int sp_id_3_11_3;
    int sp_id_3_11_4;
    int sp_id_4_11;
    int sp_id_4_11_1;
    int sp_id_4_11_2;
    int sp_id_4_11_3;
    int sp_id_4_11_4;
    int sp_id_1_12;
    int sp_id_1_12_1;
    int sp_id_1_12_2;
    int sp_id_1_12_3;
    int sp_id_1_12_4;
    int sp_id_2_12;
    int sp_id_2_12_1;
    int sp_id_2_12_2;
    int sp_id_2_12_3;
    int sp_id_2_12_4;
    int sp_id_3_12;
    int sp_id_3_12_1;
    int sp_id_3_12_2;
    int sp_id_3_12_3;
    int sp_id_3_12_4;
    int sp_id_4_12;
    int sp_id_4_12_1;
    int sp_id_4_12_2;
    int sp_id_4_12_3;
    int sp_id_4_12_4;
    int sp_id_1_13;
    int sp_id_1_13_1;
    int sp_id_1_13_2;
    int sp_id_1_13_3;
    int sp_id_1_13_4;
    int sp_id_2_13;
    int sp_id_2_13_1;
    int sp_id_2_13_2;
    int sp_id_2_13_3;
    int sp_id_2_13_4;
    int sp_id_3_13;
    int sp_id_3_13_1;
    int sp_id_3_13_2;
    int sp_id_3_13_3;
    int sp_id_3_13_4;
    int sp_id_4_13;
    int sp_id_4_13_1;
    int sp_id_4_13_2;
    int sp_id_4_13_3;
    int sp_id_4_13_4;
    int sp_id_1_14;
    int sp_id_1_14_1;
    int sp_id_1_14_2;
    int sp_id_1_14_3;
    int sp_id_1_14_4;
    int sp_id_2_14;
    int sp_id_2_14_1;
    int sp_id_2_14_2;
    int sp_id_2_14_3;
    int sp_id_2_14_4;
    int sp_id_3_14;
    int sp_id_3_14_1;
    int sp_id_3_14_2;
    int sp_id_3_14_3;
    int sp_id_3_14_4;
    int sp_id_4_14;
    int sp_id_4_14_1;
    int sp_id_4_14_2;
    int sp_id_4_14_3;
    int sp_id_4_14_4;
    int sp_id_1_21;
    int sp_id_1_21_1;
    int sp_id_1_21_2;
    int sp_id_1_21_3;
    int sp_id_1_21_4;
    int sp_id_2_21;
    int sp_id_2_21_1;
    int sp_id_2_21_2;
    int sp_id_2_21_3;
    int sp_id_2_21_4;
    int sp_id_3_21;
    int sp_id_3_21_1;
    int sp_id_3_21_2;
    int sp_id_3_21_3;
    int sp_id_3_21_4;
    int sp_id_4_21;
    int sp_id_4_21_1;
    int sp_id_4_21_2;
    int sp_id_4_21_3;
    int sp_id_4_21_4;
    int sp_id_1_22;
    int sp_id_1_22_1;
    int sp_id_1_22_2;
    int sp_id_1_22_3;
    int sp_id_1_22_4;
    int sp_id_2_22;
    int sp_id_2_22_1;
    int sp_id_2_22_2;
    int sp_id_2_22_3;
    int sp_id_2_22_4;
    int sp_id_3_22;
    int sp_id_3_22_1;
    int sp_id_3_22_2;
    int sp_id_3_22_3;
    int sp_id_3_22_4;
    int sp_id_4_22;
    int sp_id_4_22_1;
    int sp_id_4_22_2;
    int sp_id_4_22_3;
    int sp_id_4_22_4;
    int sp_id_1_23;
    int sp_id_1_23_1;
    int sp_id_1_23_2;
    int sp_id_1_23_3;
    int sp_id_1_23_4;
    int sp_id_2_23;
    int sp_id_2_23_1;
    int sp_id_2_23_2;
    int sp_id_2_23_3;
    int sp_id_2_23_4;
    int sp_id_3_23;
    int sp_id_3_23_1;
    int sp_id_3_23_2;
    int sp_id_3_23_3;
    int sp_id_3_23_4;
    int sp_id_4_23;
    int sp_id_4_23_1;
    int sp_id_4_23_2;
    int sp_id_4_23_3;
    int sp_id_4_23_4;
    int sp_id_1_24;
    int sp_id_1_24_1;
    int sp_id_1_24_2;
    int sp_id_1_24_3;
    int sp_id_1_24_4;
    int sp_id_2_24;
    int sp_id_2_24_1;
    int sp_id_2_24_2;
    int sp_id_2_24_3;
    int sp_id_2_24_4;
    int sp_id_3_24;
    int sp_id_3_24_1;
    int sp_id_3_24_2;
    int sp_id_3_24_3;
    int sp_id_3_24_4;
    int sp_id_4_24;
    int sp_id_4_24_1;
    int sp_id_4_24_2;
    int sp_id_4_24_3;
    int sp_id_4_24_4;
    int sp_id_1_31;
    int sp_id_1_31_1;
    int sp_id_1_31_2;
    int sp_id_1_31_3;
    int sp_id_1_31_4;
    int sp_id_2_31;
    int sp_id_2_31_1;
    int sp_id_2_31_2;
    int sp_id_2_31_3;
    int sp_id_2_31_4;
    int sp_id_3_31;
    int sp_id_3_31_1;
    int sp_id_3_31_2;
    int sp_id_3_31_3;
    int sp_id_3_31_4;
    int sp_id_4_31;
    int sp_id_4_31_1;
    int sp_id_4_31_2;
    int sp_id_4_31_3;
    int sp_id_4_31_4;
    int sp_id_1_32;
    int sp_id_1_32_1;
    int sp_id_1_32_2;
    int sp_id_1_32_3;
    int sp_id_1_32_4;
    int sp_id_2_32;
    int sp_id_2_32_1;
    int sp_id_2_32_2;
    int sp_id_2_32_3;
    int sp_id_2_32_4;
    int sp_id_3_32;
    int sp_id_3_32_1;
    int sp_id_3_32_2;
    int sp_id_3_32_3;
    int sp_id_3_32_4;
    int sp_id_4_32;
    int sp_id_4_32_1;
    int sp_id_4_32_2;
    int sp_id_4_32_3;
    int sp_id_4_32_4;
    int sp_id_1_33;
    int sp_id_1_33_1;
    int sp_id_1_33_2;
    int sp_id_1_33_3;
    int sp_id_1_33_4;
    int sp_id_2_33;
    int sp_id_2_33_1;
    int sp_id_2_33_2;
    int sp_id_2_33_3;
    int sp_id_2_33_4;
    int sp_id_3_33;
    int sp_id_3_33_1;
    int sp_id_3_33_2;
    int sp_id_3_33_3;
    int sp_id_3_33_4;
    int sp_id_4_33;
    int sp_id_4_33_1;
    int sp_id_4_33_2;
    int sp_id_4_33_3;
    int sp_id_4_33_4;
    int sp_id_1_34;
    int sp_id_1_34_1;
    int sp_id_1_34_2;
    int sp_id_1_34_3;
    int sp_id_1_34_4;
    int sp_id_2_34;
    int sp_id_2_34_1;
    int sp_id_2_34_2;
    int sp_id_2_34_3;
    int sp_id_2_34_4;
    int sp_id_3_34;
    int sp_id_3_34_1;
    int sp_id_3_34_2;
    int sp_id_3_34_3;
    int sp_id_3_34_4;
    int sp_id_4_34;
    int sp_id_4_34_1;
    int sp_id_4_34_2;
    int sp_id_4_34_3;
    int sp_id_4_34_4;
    int sp_id_1_41;
    int sp_id_1_41_1;
    int sp_id_1_41_2;
    int sp_id_1_41_3;
    int sp_id_1_41_4;
    int sp_id_2_41;
    int sp_id_2_41_1;
    int sp_id_2_41_2;
    int sp_id_2_41_3;
    int sp_id_2_41_4;
    int sp_id_3_41;
    int sp_id_3_41_1;
    int sp_id_3_41_2;
    int sp_id_3_41_3;
    int sp_id_3_41_4;
    int sp_id_4_41;
    int sp_id_4_41_1;
    int sp_id_4_41_2;
    int sp_id_4_41_3;
    int sp_id_4_41_4;
    int sp_id_1_42;
    int sp_id_1_42_1;
    int sp_id_1_42_2;
    int sp_id_1_42_3;
    int sp_id_1_42_4;
    int sp_id_2_42;
    int sp_id_2_42_1;
    int sp_id_2_42_2;
    int sp_id_2_42_3;
    int sp_id_2_42_4;
    int sp_id_3_42;
    int sp_id_3_42_1;
    int sp_id_3_42_2;
    int sp_id_3_42_3;
    int sp_id_3_42_4;
    int sp_id_4_42;
    int sp_id_4_42_1;
    int sp_id_4_42_2;
    int sp_id_4_42_3;
    int sp_id_4_42_4;
    int sp_id_1_43;
    int sp_id_1_43_1;
    int sp_id_1_43_2;
    int sp_id_1_43_3;
    int sp_id_1_43_4;
    int sp_id_2_43;
    int sp_id_2_43_1;
    int sp_id_2_43_2;
    int sp_id_2_43_3;
    int sp_id_2_43_4;
    int sp_id_3_43;
    int sp_id_3_43_1;
    int sp_id_3_43_2;
    int sp_id_3_43_3;
    int sp_id_3_43_4;
    int sp_id_4_43;
    int sp_id_4_43_1;
    int sp_id_4_43_2;
    int sp_id_4_43_3;
    int sp_id_4_43_4;
    int sp_id_1_44;
    int sp_id_1_44_1;
    int sp_id_1_44_2;
    int sp_id_1_44_3;
    int sp_id_1_44_4;
    int sp_id_2_44;
    int sp_id_2_44_1;
    int sp_id_2_44_2;
    int sp_id_2_44_3;
    int sp_id_2_44_4;
    int sp_id_3_44;
    int sp_id_3_44_1;
    int sp_id_3_44_2;
    int sp_id_3_44_3;
    int sp_id_3_44_4;
    int sp_id_4_44;
    int sp_id_4_44_1;
    int sp_id_4_44_2;
    int sp_id_4_44_3;
    int sp_id_4_44_4;

    public int soundPoolId[][][] = {
            {
                    {sp_id_1_00, sp_id_1_00_1, sp_id_1_00_2, sp_id_1_00_3, sp_id_1_00_4}, {sp_id_1_01, sp_id_1_01_1, sp_id_1_01_2, sp_id_1_01_3, sp_id_1_01_4}, {sp_id_1_02, sp_id_1_02_1, sp_id_1_02_2, sp_id_1_02_3, sp_id_1_02_4}, {sp_id_1_03, sp_id_1_03_1, sp_id_1_03_2, sp_id_1_03_3, sp_id_1_03_4}, {sp_id_1_04, sp_id_1_04_1, sp_id_1_04_2, sp_id_1_04_3, sp_id_1_04_4},
                    {sp_id_1_11, sp_id_1_11_1, sp_id_1_11_2, sp_id_1_11_3, sp_id_1_11_4}, {sp_id_1_12, sp_id_1_12_1, sp_id_1_12_2, sp_id_1_12_3, sp_id_1_12_4}, {sp_id_1_13, sp_id_1_13_1, sp_id_1_13_2, sp_id_1_13_3, sp_id_1_13_4}, {sp_id_1_14, sp_id_1_14_1, sp_id_1_14_2, sp_id_1_14_3, sp_id_1_14_4},
                    {sp_id_1_21, sp_id_1_21_1, sp_id_1_21_2, sp_id_1_21_3, sp_id_1_21_4}, {sp_id_1_22, sp_id_1_22_1, sp_id_1_22_2, sp_id_1_22_3, sp_id_1_22_4}, {sp_id_1_23, sp_id_1_23_1, sp_id_1_23_2, sp_id_1_23_3, sp_id_1_23_4}, {sp_id_1_24, sp_id_1_24_1, sp_id_1_24_2, sp_id_1_24_3, sp_id_1_24_4},
                    {sp_id_1_31, sp_id_1_31_1, sp_id_1_31_2, sp_id_1_31_3, sp_id_1_31_4}, {sp_id_1_32, sp_id_1_32_1, sp_id_1_32_2, sp_id_1_32_3, sp_id_1_32_4}, {sp_id_1_33, sp_id_1_33_1, sp_id_1_33_2, sp_id_1_33_3, sp_id_1_33_4}, {sp_id_1_34, sp_id_1_34_1, sp_id_1_34_2, sp_id_1_34_3, sp_id_1_34_4},
                    {sp_id_1_41, sp_id_1_41_1, sp_id_1_41_2, sp_id_1_41_3, sp_id_1_41_4}, {sp_id_1_42, sp_id_1_42_1, sp_id_1_42_2, sp_id_1_42_3, sp_id_1_42_4}, {sp_id_1_43, sp_id_1_43_1, sp_id_1_43_2, sp_id_1_43_3, sp_id_1_43_4}, {sp_id_1_44, sp_id_1_44_1, sp_id_1_44_2, sp_id_1_44_3, sp_id_1_44_4},
            }, {
                    {sp_id_2_00, sp_id_2_00_1, sp_id_2_00_2, sp_id_2_00_3, sp_id_2_00_4}, {sp_id_2_01, sp_id_2_01_1, sp_id_2_01_2, sp_id_2_01_3, sp_id_2_01_4}, {sp_id_2_02, sp_id_2_02_1, sp_id_2_02_2, sp_id_2_02_3, sp_id_2_02_4}, {sp_id_2_03, sp_id_2_03_1, sp_id_2_03_2, sp_id_2_03_3, sp_id_2_03_4}, {sp_id_2_04, sp_id_2_04_1, sp_id_2_04_2, sp_id_2_04_3, sp_id_2_04_4},
                    {sp_id_2_11, sp_id_2_11_1, sp_id_2_11_2, sp_id_2_11_3, sp_id_2_11_4}, {sp_id_2_12, sp_id_2_12_1, sp_id_2_12_2, sp_id_2_12_3, sp_id_2_12_4}, {sp_id_2_13, sp_id_2_13_1, sp_id_2_13_2, sp_id_2_13_3, sp_id_2_13_4}, {sp_id_2_14, sp_id_2_14_1, sp_id_2_14_2, sp_id_2_14_3, sp_id_2_14_4},
                    {sp_id_2_21, sp_id_2_21_1, sp_id_2_21_2, sp_id_2_21_3, sp_id_2_21_4}, {sp_id_2_22, sp_id_2_22_1, sp_id_2_22_2, sp_id_2_22_3, sp_id_2_22_4}, {sp_id_2_23, sp_id_2_23_1, sp_id_2_23_2, sp_id_2_23_3, sp_id_2_23_4}, {sp_id_2_24, sp_id_2_24_1, sp_id_2_24_2, sp_id_2_24_3, sp_id_2_24_4},
                    {sp_id_2_31, sp_id_2_31_1, sp_id_2_31_2, sp_id_2_31_3, sp_id_2_31_4}, {sp_id_2_32, sp_id_2_32_1, sp_id_2_32_2, sp_id_2_32_3, sp_id_2_32_4}, {sp_id_2_33, sp_id_2_33_1, sp_id_2_33_2, sp_id_2_33_3, sp_id_2_33_4}, {sp_id_2_34, sp_id_2_34_1, sp_id_2_34_2, sp_id_2_34_3, sp_id_2_34_4},
                    {sp_id_2_41, sp_id_2_41_1, sp_id_2_41_2, sp_id_2_41_3, sp_id_2_41_4}, {sp_id_2_42, sp_id_2_42_1, sp_id_2_42_2, sp_id_2_42_3, sp_id_2_42_4}, {sp_id_2_43, sp_id_2_43_1, sp_id_2_43_2, sp_id_2_43_3, sp_id_2_43_4}, {sp_id_2_44, sp_id_2_44_1, sp_id_2_44_2, sp_id_2_44_3, sp_id_2_44_4},
            }, {
                    {sp_id_3_00, sp_id_3_00_1, sp_id_3_00_2, sp_id_3_00_3, sp_id_3_00_4}, {sp_id_3_01, sp_id_3_01_1, sp_id_3_01_2, sp_id_3_01_3, sp_id_3_01_4}, {sp_id_3_02, sp_id_3_02_1, sp_id_3_02_2, sp_id_3_02_3, sp_id_3_02_4}, {sp_id_3_03, sp_id_3_03_1, sp_id_3_03_2, sp_id_3_03_3, sp_id_3_03_4}, {sp_id_3_04, sp_id_3_04_1, sp_id_3_04_2, sp_id_3_04_3, sp_id_3_04_4},
                    {sp_id_3_11, sp_id_3_11_1, sp_id_3_11_2, sp_id_3_11_3, sp_id_3_11_4}, {sp_id_3_12, sp_id_3_12_1, sp_id_3_12_2, sp_id_3_12_3, sp_id_3_12_4}, {sp_id_3_13, sp_id_3_13_1, sp_id_3_13_2, sp_id_3_13_3, sp_id_3_13_4}, {sp_id_3_14, sp_id_3_14_1, sp_id_3_14_2, sp_id_3_14_3, sp_id_3_14_4},
                    {sp_id_3_21, sp_id_3_21_1, sp_id_3_21_2, sp_id_3_21_3, sp_id_3_21_4}, {sp_id_3_22, sp_id_3_22_1, sp_id_3_22_2, sp_id_3_22_3, sp_id_3_22_4}, {sp_id_3_23, sp_id_3_23_1, sp_id_3_23_2, sp_id_3_23_3, sp_id_3_23_4}, {sp_id_3_24, sp_id_3_24_1, sp_id_3_24_2, sp_id_3_24_3, sp_id_3_24_4},
                    {sp_id_3_31, sp_id_3_31_1, sp_id_3_31_2, sp_id_3_31_3, sp_id_3_31_4}, {sp_id_3_32, sp_id_3_32_1, sp_id_3_32_2, sp_id_3_32_3, sp_id_3_32_4}, {sp_id_3_33, sp_id_3_33_1, sp_id_3_33_2, sp_id_3_33_3, sp_id_3_33_4}, {sp_id_3_34, sp_id_3_34_1, sp_id_3_34_2, sp_id_3_34_3, sp_id_3_34_4},
                    {sp_id_3_41, sp_id_3_41_1, sp_id_3_41_2, sp_id_3_41_3, sp_id_3_41_4}, {sp_id_3_42, sp_id_3_42_1, sp_id_3_42_2, sp_id_3_42_3, sp_id_3_42_4}, {sp_id_3_43, sp_id_3_43_1, sp_id_3_43_2, sp_id_3_43_3, sp_id_3_43_4}, {sp_id_3_44, sp_id_3_44_1, sp_id_3_44_2, sp_id_3_44_3, sp_id_3_44_4},
            }, {
                    {sp_id_4_00, sp_id_4_00_1, sp_id_4_00_2, sp_id_4_00_3, sp_id_4_00_4}, {sp_id_4_01, sp_id_4_01_1, sp_id_4_01_2, sp_id_4_01_3, sp_id_4_01_4}, {sp_id_4_02, sp_id_4_02_1, sp_id_4_02_2, sp_id_4_02_3, sp_id_4_02_4}, {sp_id_4_03, sp_id_4_03_1, sp_id_4_03_2, sp_id_4_03_3, sp_id_4_03_4}, {sp_id_4_04, sp_id_4_04_1, sp_id_4_04_2, sp_id_4_04_3, sp_id_4_04_4},
                    {sp_id_4_11, sp_id_4_11_1, sp_id_4_11_2, sp_id_4_11_3, sp_id_4_11_4}, {sp_id_4_12, sp_id_4_12_1, sp_id_4_12_2, sp_id_4_12_3, sp_id_4_12_4}, {sp_id_4_13, sp_id_4_13_1, sp_id_4_13_2, sp_id_4_13_3, sp_id_4_13_4}, {sp_id_4_14, sp_id_4_14_1, sp_id_4_14_2, sp_id_4_14_3, sp_id_4_14_4},
                    {sp_id_4_21, sp_id_4_21_1, sp_id_4_21_2, sp_id_4_21_3, sp_id_4_21_4}, {sp_id_4_22, sp_id_4_22_1, sp_id_4_22_2, sp_id_4_22_3, sp_id_4_22_4}, {sp_id_4_23, sp_id_4_23_1, sp_id_4_23_2, sp_id_4_23_3, sp_id_4_23_4}, {sp_id_4_24, sp_id_4_24_1, sp_id_4_24_2, sp_id_4_24_3, sp_id_4_24_4},
                    {sp_id_4_31, sp_id_4_31_1, sp_id_4_31_2, sp_id_4_31_3, sp_id_4_31_4}, {sp_id_4_32, sp_id_4_32_1, sp_id_4_32_2, sp_id_4_32_3, sp_id_4_32_4}, {sp_id_4_33, sp_id_4_33_1, sp_id_4_33_2, sp_id_4_33_3, sp_id_4_33_4}, {sp_id_4_34, sp_id_4_34_1, sp_id_4_34_2, sp_id_4_34_3, sp_id_4_34_4},
                    {sp_id_4_41, sp_id_4_41_1, sp_id_4_41_2, sp_id_4_41_3, sp_id_4_41_4}, {sp_id_4_42, sp_id_4_42_1, sp_id_4_42_2, sp_id_4_42_3, sp_id_4_42_4}, {sp_id_4_43, sp_id_4_43_1, sp_id_4_43_2, sp_id_4_43_3, sp_id_4_43_4}, {sp_id_4_44, sp_id_4_44_1, sp_id_4_44_2, sp_id_4_44_3, sp_id_4_44_4}
            }
    };

    boolean isPresetLoaded = false;
    int raw[][][] = {
            {
                    // Hello
                    {R.raw.a0_00}, {R.raw.h3_01}, {R.raw.a0_00}, {R.raw.h2_03}, {R.raw.h2_04},
                    {R.raw.h1_11}, {R.raw.h1_12}, {R.raw.h1_13}, {R.raw.h1_14},
                    {R.raw.h1_21}, {R.raw.h1_22}, {R.raw.h1_23}, {R.raw.h1_24},
                    {R.raw.h1_31}, {R.raw.h1_32}, {R.raw.h1_33}, {R.raw.h1_34},
                    {R.raw.h1_41}, {R.raw.h1_42}, {R.raw.h1_43}, {R.raw.h1_44},

                    {R.raw.h2_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
                    {R.raw.h2_11}, {R.raw.h2_12}, {R.raw.h2_13}, {R.raw.h2_14},
                    {R.raw.h2_21}, {R.raw.h2_22}, {R.raw.h2_23}, {R.raw.h2_24},
                    {R.raw.h2_31}, {R.raw.h2_32}, {R.raw.h2_33}, {R.raw.h2_34},
                    {R.raw.h2_41}, {R.raw.h2_42}, {R.raw.h2_43}, {R.raw.h2_44},

                    {R.raw.h3_01}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
                    {R.raw.h3_11}, {R.raw.h3_12}, {R.raw.h3_13}, {R.raw.h3_14},
                    {R.raw.h3_21}, {R.raw.h3_22}, {R.raw.h3_23}, {R.raw.h3_24},
                    {R.raw.h3_31}, {R.raw.h3_32}, {R.raw.h3_33}, {R.raw.h3_34},
                    {R.raw.h3_41}, {R.raw.h3_42}, {R.raw.h3_43}, {R.raw.h3_44},

                    {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
                    {R.raw.h4_11}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.h4_14},
                    {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
                    {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
                    {R.raw.h4_41}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.h4_44}
            }, {
            // Roses
            {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
            {R.raw.r1_11}, {R.raw.r1_12}, {R.raw.r1_13}, {R.raw.r1_14},
            {R.raw.r1_21_1, R.raw.r1_21_2, R.raw.r1_21_3, R.raw.r1_21_4}, {R.raw.r1_22_1, R.raw.r1_22_2, R.raw.r1_22_3, R.raw.r1_22_4}, {R.raw.r1_23_1, R.raw.r1_23_2, R.raw.r1_23_3, R.raw.r1_23_4}, {R.raw.r1_24_1, R.raw.r1_24_2, R.raw.r1_24_3, R.raw.r1_24_4},
            {R.raw.r1_31_1}, {R.raw.r1_32}, {R.raw.r1_33_1}, {R.raw.r1_34},
            {R.raw.r1_41_1}, {R.raw.r1_42}, {R.raw.r1_43}, {R.raw.r1_44},

            {R.raw.r2_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
            {R.raw.r2_11_1}, {R.raw.r2_12_1}, {R.raw.r2_13_1, R.raw.r2_13_2}, {R.raw.r2_14_1, R.raw.r2_14_2},
            {R.raw.r2_21}, {R.raw.r2_22}, {R.raw.r2_23}, {R.raw.r2_24},
            {R.raw.r2_31}, {R.raw.r2_32}, {R.raw.r2_33}, {R.raw.r2_34},
            {R.raw.r2_41}, {R.raw.r2_42_1}, {R.raw.r2_43}, {R.raw.r2_44},

            {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
            {R.raw.r2_11_1}, {R.raw.r3_12_1}, {R.raw.r3_13_1, R.raw.r3_13_2}, {R.raw.r3_14_1, R.raw.r3_14_2},
            {R.raw.r3_21}, {R.raw.r3_22}, {R.raw.r3_23}, {R.raw.r3_24},
            {R.raw.r3_31}, {R.raw.r3_32}, {R.raw.r3_33}, {R.raw.r3_34},
            {R.raw.r2_41}, {R.raw.r3_42_1}, {R.raw.r2_43}, {R.raw.r3_44},

            {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
            {R.raw.r2_11_1}, {R.raw.r4_12_1}, {R.raw.r4_13_1, R.raw.r4_13_2}, {R.raw.r4_14_1, R.raw.r4_14_2},
            {R.raw.r4_21_1}, {R.raw.r4_22_1}, {R.raw.r4_23_1}, {R.raw.r4_24_1},
            {R.raw.r4_31_1}, {R.raw.r4_32_1}, {R.raw.r4_33_1}, {R.raw.r4_34_1},
            {R.raw.r2_41}, {R.raw.r4_42_1}, {R.raw.r2_43}, {R.raw.r3_44}
    }, {
            // Faded
            {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
            {R.raw.f1_11}, {R.raw.f1_12}, {R.raw.f1_13}, {R.raw.f1_14},
            {R.raw.f1_21}, {R.raw.f1_22}, {R.raw.f1_23}, {R.raw.f1_24},
            {R.raw.f1_31}, {R.raw.f1_32}, {R.raw.f1_33}, {R.raw.f1_34},
            {R.raw.f1_41}, {R.raw.f1_42}, {R.raw.f1_43_1, R.raw.f1_43_2}, {R.raw.a0_00},

            {R.raw.f2_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
            {R.raw.f2_11}, {R.raw.f2_12}, {R.raw.f2_13}, {R.raw.f2_14_1, R.raw.f2_14_2, R.raw.f2_14_3},
            {R.raw.f2_21}, {R.raw.f2_22}, {R.raw.f2_23}, {R.raw.f2_24_1, R.raw.f2_24_2, R.raw.f2_24_3},
            {R.raw.f2_31}, {R.raw.f2_32}, {R.raw.f2_33}, {R.raw.f2_34_1, R.raw.f2_34_2, R.raw.f2_34_3},
            {R.raw.f2_41}, {R.raw.f2_42}, {R.raw.f2_43}, {R.raw.f2_44_1, R.raw.f2_44_2, R.raw.f2_44_3},

            {R.raw.f3_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
            {R.raw.f3_11}, {R.raw.f3_12}, {R.raw.f3_13}, {R.raw.f3_14_1, R.raw.f3_14_2, R.raw.f3_14_3},
            {R.raw.f3_21}, {R.raw.f3_22}, {R.raw.f3_23}, {R.raw.f3_24_1, R.raw.f3_24_2, R.raw.f3_24_3},
            {R.raw.f3_31}, {R.raw.f3_32}, {R.raw.f3_33}, {R.raw.f3_34_1, R.raw.f3_34_2, R.raw.f3_34_3},
            {R.raw.f3_41}, {R.raw.f3_42}, {R.raw.f3_43_1, R.raw.f3_43_2}, {R.raw.f3_44_1, R.raw.f3_44_2, R.raw.f3_44_3},

            {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00}, {R.raw.a0_00},
            {R.raw.f4_11}, {R.raw.f4_12}, {R.raw.f4_13}, {R.raw.f4_14},
            {R.raw.f4_21}, {R.raw.f4_22}, {R.raw.f4_23}, {R.raw.f4_24},
            {R.raw.f4_31}, {R.raw.f4_32}, {R.raw.f4_33}, {R.raw.f4_34},
            {R.raw.f4_41}, {R.raw.f3_42}, {R.raw.f3_43_1, R.raw.f3_43_2}, {R.raw.f3_44_1, R.raw.f3_44_2, R.raw.f3_44_3}
    }
    };
    Activity activity;
    Preset currentPreset = null;
    Preset previousPreset = null;

    int buttonId[] = {
            R.id.btn00,
            R.id.tgl1,
            R.id.tgl2,
            R.id.tgl3,
            R.id.tgl4,
            R.id.btn11,
            R.id.btn12,
            R.id.btn13,
            R.id.btn14,
            R.id.btn21,
            R.id.btn22,
            R.id.btn23,
            R.id.btn24,
            R.id.btn31,
            R.id.btn32,
            R.id.btn33,
            R.id.btn34,
            R.id.btn41,
            R.id.btn42,
            R.id.btn43,
            R.id.btn44};
    private ThemeService theme = new ThemeService();
    private AnimService anim = new AnimService();
    private WindowService window = new WindowService();

    //  R.raw.h1_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
    //  R.raw.h1_11, R.raw.h1_12, R.raw.h1_13, R.raw.h1_14,
    //  R.raw.h1_21, R.raw.h1_22, R.raw.h1_23, R.raw.h1_24,
    //  R.raw.h1_31, R.raw.h1_32, R.raw.h1_33, R.raw.h1_34,
    //  R.raw.h1_41, R.raw.h1_42, R.raw.h1_43, R.raw.h1_44,
    //
    //  R.raw.h2_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
    //  R.raw.h2_11, R.raw.h2_12, R.raw.h2_13, R.raw.h2_14,
    //  R.raw.h2_21, R.raw.h2_22, R.raw.h2_23, R.raw.h2_24,
    //  R.raw.h2_31, R.raw.h2_32, R.raw.h2_33, R.raw.h2_34,
    //  R.raw.h2_41, R.raw.h2_42, R.raw.h2_43, R.raw.h2_44,
    //
    //  R.raw.h3_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
    //  R.raw.h3_11, R.raw.h3_12, R.raw.h3_13, R.raw.h3_14,
    //  R.raw.h3_21, R.raw.h3_22, R.raw.h3_23, R.raw.h3_24,
    //  R.raw.h3_31, R.raw.h3_32, R.raw.h3_33, R.raw.h3_34,
    //  R.raw.h3_41, R.raw.h3_42, R.raw.h3_43, R.raw.h3_44,
    //
    //  R.raw.h4_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00, R.raw.a0_00,
    //  R.raw.h4_11, R.raw.h4_12, R.raw.h4_13, R.raw.h4_14,
    //  R.raw.h4_21, R.raw.h4_22, R.raw.h4_23, R.raw.h4_24,
    //  R.raw.h4_31, R.raw.h4_32, R.raw.h4_33, R.raw.h4_34,
    //  R.raw.h4_41, R.raw.h4_42, R.raw.h4_43, R.raw.h4_44,

    //Play SoundPool
    public void soundPlay(SoundPool sp, int soundid) {
        sp.play(soundid, 1, 1, 1, 0, 1f);
    }

    //Stop SoundPool
    public void soundStop(SoundPool sp, int soundid) {
        sp.stop(soundid);
    }

    public void release() {
        try {
            sp.release();
        } catch (NullPointerException e) {
            Log.e("SoundService", "NPE, soundPool is null. Cannot release soundPool.");
        }
    }

    //Stop All SoundPool
    public void soundAllStop() {
        try {
            sp.autoPause();
        } catch (NullPointerException e) {
            Log.e("SoundService", "NPE, soundPool is null. Cannot autoPause soundPool.");
        }
    }

    public void loadSchemeSound(Preset preset, Activity a) {
        previousPreset = currentPreset;
        currentPreset = preset;
        activity = a;
        new UnloadSound().execute();
    }

    public void unloadSchemeSound(Activity a) {
        activity = a;
        new UnloadSound().execute();
    }

    public void playToggleButtonSound(int id) {
        sp.play(soundPoolId[id - 1][0][0], 1, 1, 1, 0, 1f);
    }

    //    boolean tgl1 = false;
    //    boolean tgl2 = false;
    //    boolean tgl3 = false;
    //    boolean tgl4 = false;
    //    boolean tgl5 = false;
    //    boolean tgl6 = false;
    //    boolean tgl7 = false;
    //    boolean tgl8 = false;
    //
    //    boolean toggleButton[] = {
    //            tgl1,
    //            tgl2,
    //            tgl3,
    //            tgl4,
    //            tgl5,
    //            tgl6,
    //            tgl7,
    //            tgl8 };
    //
    //    int toggleButtonId[] = {
    //            R.id.tgl1,
    //            R.id.tgl2,
    //            R.id.tgl3,
    //            R.id.tgl4,
    //            R.id.tgl5,
    //            R.id.tgl6,
    //            R.id.tgl7,
    //            R.id.tgl8 };


    //    public void setToggleButton(final int color_id, final Activity a) {
    //        for(final int[] i = {1}; i[0] < 5; i[0]++){
    //            w.setOnTouch(R.id.tgl1, new Runnable() {
    //                @Override
    //                public void run() {
    //                    setButtonToggle(i[0], R.color.green, a);
    //                    if (toggleButton[i[0] - 1] == false) {
    //                        for(int j = 0; j < 4; j++){
    //                            if(j == (i[0] - 1)){
    //                                w.setViewBackgroundColor(toggleButtonId[j], color_id, a);
    //                            } else {
    //                                w.setViewBackgroundColor(toggleButtonId[j], R.color.grey, a);
    //                            }
    //                        }
    //                    } else {
    //                        w.setViewBackgroundColor(toggleButtonId[i[0] - 1], R.color.grey, a);
    //                        setButton(R.color.grey_dark, a);
    //                        soundAllStop();
    //                    }
    //                }
    //            }, new Runnable() {
    //                @Override
    //                public void run() {
    //                    for(int i = 0; i < 4; i++){
    //                        if(toggleButton[i] == false){
    //                            for(int j = 0; j < 4; j++){
    //                                if(j == i){
    //                                    toggleButton[j] = false;
    //                                } else {
    //                                    toggleButton[j] = true;
    //                                }
    //                            }
    //                        } else {
    //                            toggleButton[i] = false;
    //                        }
    //                    }
    //                }
    //            }, a);
    //        }
    //    }

    public void setButtonToggle(int id, int colorId, Activity activity) {
        toggle = id;
        if (isPresetLoaded == true) {
            for (int i = 0; i < 21; i++) {
                if (id >= 1) {
                    if (i < 1 || i > 4) {
                        window.setOnGestureSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], activity);
                    }
                } else {
                    Log.d("Error", "Incorrect id value found");
                }
            }
            Log.i("SoundService", "ToggleButton sound set id " + String.valueOf(id));
        } else {
            setButton(colorId, activity);
        }
    }

    public void setButtonTogglePattern(int id, int colorId, int patternId, Activity activity) {

        int pattern1[][] = {
                {R.id.btn12, R.id.btn21},
                {R.id.btn11, R.id.btn22, R.id.btn13},
                {R.id.btn12, R.id.btn23, R.id.btn14},
                {R.id.btn13, R.id.btn24},
                {R.id.btn11, R.id.btn22, R.id.btn31},
                {R.id.btn12, R.id.btn21, R.id.btn32, R.id.btn23},
                {R.id.btn13, R.id.btn22, R.id.btn33, R.id.btn24},
                {R.id.btn14, R.id.btn23, R.id.btn34},
                {R.id.btn21, R.id.btn32, R.id.btn41},
                {R.id.btn31, R.id.btn33, R.id.btn22, R.id.btn42},
                {R.id.btn32, R.id.btn34, R.id.btn23, R.id.btn43},
                {R.id.btn33, R.id.btn24, R.id.btn44},
                {R.id.btn31, R.id.btn42},
                {R.id.btn41, R.id.btn32, R.id.btn43},
                {R.id.btn42, R.id.btn33, R.id.btn44},
                {R.id.btn34, R.id.btn43}
        };

        int pattern2[][] = {
                {R.id.btn12, R.id.btn13, R.id.btn14},
                {R.id.btn11, R.id.btn13, R.id.btn14},
                {R.id.btn11, R.id.btn12, R.id.btn14},
                {R.id.btn11, R.id.btn12, R.id.btn13},
                {R.id.btn22, R.id.btn23, R.id.btn24},
                {R.id.btn21, R.id.btn23, R.id.btn24},
                {R.id.btn21, R.id.btn22, R.id.btn24},
                {R.id.btn21, R.id.btn22, R.id.btn23},
                {R.id.btn32, R.id.btn33, R.id.btn34},
                {R.id.btn31, R.id.btn33, R.id.btn34},
                {R.id.btn31, R.id.btn32, R.id.btn34},
                {R.id.btn31, R.id.btn32, R.id.btn33},
                {R.id.btn42, R.id.btn43, R.id.btn44},
                {R.id.btn41, R.id.btn43, R.id.btn44},
                {R.id.btn41, R.id.btn42, R.id.btn44},
                {R.id.btn41, R.id.btn42, R.id.btn43}
        };

        int pattern3[][] = {
                {R.id.btn21, R.id.btn31, R.id.btn41},
                {R.id.btn22, R.id.btn32, R.id.btn42},
                {R.id.btn23, R.id.btn33, R.id.btn43},
                {R.id.btn24, R.id.btn34, R.id.btn44},
                {R.id.btn11, R.id.btn31, R.id.btn41},
                {R.id.btn12, R.id.btn32, R.id.btn42},
                {R.id.btn13, R.id.btn33, R.id.btn43},
                {R.id.btn14, R.id.btn34, R.id.btn44},
                {R.id.btn11, R.id.btn21, R.id.btn41},
                {R.id.btn12, R.id.btn22, R.id.btn42},
                {R.id.btn13, R.id.btn23, R.id.btn43},
                {R.id.btn14, R.id.btn24, R.id.btn44},
                {R.id.btn11, R.id.btn21, R.id.btn31},
                {R.id.btn12, R.id.btn22, R.id.btn32},
                {R.id.btn13, R.id.btn23, R.id.btn33},
                {R.id.btn14, R.id.btn24, R.id.btn34}
        };

        int pattern4[][] = {
                {R.id.btn12, R.id.btn13, R.id.btn14, R.id.btn21, R.id.btn31, R.id.btn41},
                {R.id.btn11, R.id.btn13, R.id.btn14, R.id.btn22, R.id.btn32, R.id.btn42},
                {R.id.btn11, R.id.btn12, R.id.btn14, R.id.btn23, R.id.btn33, R.id.btn43},
                {R.id.btn11, R.id.btn12, R.id.btn13, R.id.btn24, R.id.btn34, R.id.btn44},
                {R.id.btn22, R.id.btn23, R.id.btn24, R.id.btn11, R.id.btn31, R.id.btn41},
                {R.id.btn21, R.id.btn23, R.id.btn24, R.id.btn12, R.id.btn32, R.id.btn42},
                {R.id.btn21, R.id.btn22, R.id.btn24, R.id.btn13, R.id.btn33, R.id.btn43},
                {R.id.btn21, R.id.btn22, R.id.btn23, R.id.btn14, R.id.btn34, R.id.btn44},
                {R.id.btn32, R.id.btn33, R.id.btn34, R.id.btn11, R.id.btn21, R.id.btn41},
                {R.id.btn31, R.id.btn33, R.id.btn34, R.id.btn12, R.id.btn22, R.id.btn42},
                {R.id.btn31, R.id.btn32, R.id.btn34, R.id.btn13, R.id.btn23, R.id.btn43},
                {R.id.btn31, R.id.btn32, R.id.btn33, R.id.btn14, R.id.btn24, R.id.btn44},
                {R.id.btn42, R.id.btn43, R.id.btn44, R.id.btn11, R.id.btn21, R.id.btn31},
                {R.id.btn41, R.id.btn43, R.id.btn44, R.id.btn12, R.id.btn22, R.id.btn32},
                {R.id.btn41, R.id.btn42, R.id.btn44, R.id.btn13, R.id.btn23, R.id.btn33},
                {R.id.btn41, R.id.btn42, R.id.btn43, R.id.btn14, R.id.btn24, R.id.btn34}
        };

        int pattern[][] = {};

        switch (patternId) {
            case 1:
                pattern = pattern1;
                break;
            case 2:
                pattern = pattern2;
                break;
            case 3:
                pattern = pattern3;
                break;
            case 4:
                pattern = pattern4;
                break;
        }

        if (isPresetLoaded == true) {
            for (int i = 0; i < 21; i++) {
                if (i >= 1 && i <= 4) {
                    continue;
                } else {
                    if (id >= 1) {
                        SharedPreferences prefs = activity.getSharedPreferences("com.bedrock.padder", activity.MODE_PRIVATE);
                        int scheme = prefs.getInt("scheme", 1);
                        window.setOnTouchSoundPattern(buttonId[i], pattern, colorId, R.color.grey, sp, soundPoolId[id - 1][i], raw[scheme - 1][i].length, activity);
                    }
                }
            }
            Log.i("SoundService", "ToggleButton pattern set id " + String.valueOf(patternId));
//            if (id == 1) {
//            } else if(id == 2) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_2_00, sp_id_2_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_2_11, sp_id_2_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_2_12, sp_id_2_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_2_13, sp_id_2_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_2_14, sp_id_2_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_2_21, sp_id_2_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_2_22, sp_id_2_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_2_23, sp_id_2_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_2_24, sp_id_2_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_2_31, sp_id_2_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_2_32, sp_id_2_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_2_33, sp_id_2_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_2_34, sp_id_2_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_2_41, sp_id_2_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_2_42, sp_id_2_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_2_43, sp_id_2_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_2_44, sp_id_2_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 2");
//            } else if(id == 3) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_3_00, sp_id_3_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_3_11, sp_id_3_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_3_12, sp_id_3_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_3_13, sp_id_3_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_3_14, sp_id_3_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_3_21, sp_id_3_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_3_22, sp_id_3_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_3_23, sp_id_3_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_3_24, sp_id_3_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_3_31, sp_id_3_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_3_32, sp_id_3_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_3_33, sp_id_3_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_3_34, sp_id_3_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_3_41, sp_id_3_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_3_42, sp_id_3_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_3_43, sp_id_3_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_3_44, sp_id_3_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 3");
//            } else if(id == 4) {
//                window.setOnTouchSound(R.id.btn00, colorId, R.color.grey, sp_4_00, sp_id_4_00, activity);
//                window.setOnTouchSound(R.id.btn11, colorId, R.color.grey, sp_4_11, sp_id_4_11, activity);
//                window.setOnTouchSound(R.id.btn12, colorId, R.color.grey, sp_4_12, sp_id_4_12, activity);
//                window.setOnTouchSound(R.id.btn13, colorId, R.color.grey, sp_4_13, sp_id_4_13, activity);
//                window.setOnTouchSound(R.id.btn14, colorId, R.color.grey, sp_4_14, sp_id_4_14, activity);
//                window.setOnTouchSound(R.id.btn21, colorId, R.color.grey, sp_4_21, sp_id_4_21, activity);
//                window.setOnTouchSound(R.id.btn22, colorId, R.color.grey, sp_4_22, sp_id_4_22, activity);
//                window.setOnTouchSound(R.id.btn23, colorId, R.color.grey, sp_4_23, sp_id_4_23, activity);
//                window.setOnTouchSound(R.id.btn24, colorId, R.color.grey, sp_4_24, sp_id_4_24, activity);
//                window.setOnTouchSound(R.id.btn31, colorId, R.color.grey, sp_4_31, sp_id_4_31, activity);
//                window.setOnTouchSound(R.id.btn32, colorId, R.color.grey, sp_4_32, sp_id_4_32, activity);
//                window.setOnTouchSound(R.id.btn33, colorId, R.color.grey, sp_4_33, sp_id_4_33, activity);
//                window.setOnTouchSound(R.id.btn34, colorId, R.color.grey, sp_4_34, sp_id_4_34, activity);
//                window.setOnTouchSound(R.id.btn41, colorId, R.color.grey, sp_4_41, sp_id_4_41, activity);
//                window.setOnTouchSound(R.id.btn42, colorId, R.color.grey, sp_4_42, sp_id_4_42, activity);
//                window.setOnTouchSound(R.id.btn43, colorId, R.color.grey, sp_4_43, sp_id_4_43, activity);
//                window.setOnTouchSound(R.id.btn44, colorId, R.color.grey, sp_4_44, sp_id_4_44, activity);
//
//                Log.i("SoundService", "ToggleButton set id 4");
//            } else {
//                Log.i("SoundService", "ToggleButton undefined value");
//            }
        } else {
            setButton(colorId, activity);
        }
    }

    public void setButton(final int colorId, final Activity activity) {
        for (int i = 0; i < buttonId.length - 4; i++) {
            if (i == 0) {
                window.setOnTouchColor(buttonId[i], colorId, R.color.grey, activity);
            } else {
                window.setOnTouchColor(buttonId[i + 4], colorId, R.color.grey, activity);
            }
        }
    }

    class LoadSound extends AsyncTask<Void, Integer, String> {
        String TAG = "LoadSound";

        protected void onPreExecute() {
            Log.d(TAG, "On preExceute, unloadSchemeSound");

            window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.icon_tutorial_disabled);
            window.getImageView(R.id.layout_settings_tutorial_icon, activity).setImageResource(R.drawable.settings_tutorial_disabled);
        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground, start loading sounds");
            
            if (currentPreset != null) {
                Log.i(TAG, "Preset \"" + currentPreset.getMusic().getName() + "\", id " + currentPreset.getMusic().getId());
                // deck loop
                for (int i = 0; i < 4; i++) {
                    Log.i(TAG, "  Deck " + (i + 1));
                    // pad loop
                    for (int j = 0; j < 21; j++) {
                        Log.i(TAG, "    Pad " + (j + 1));
                        // pad gesture
                        if (currentPreset.getMusic().getDecks()[i].getPad(j).getRaw() != null) {
                            soundPoolId[i][j][0] = sp.load(activity, window.getRawId(currentPreset.getMusic().getDecks()[i].getPad(j).getRaw(), activity), 1);
                            Log.i(TAG, "      Pad " + (j + 1) + "-Normal" + ", Sound loaded");
                        }
                        if (currentPreset.getMusic().getDecks()[i].getPad(j).getUp() != null) {
                            soundPoolId[i][j][1] = sp.load(activity, window.getRawId(currentPreset.getMusic().getDecks()[i].getPad(j).getUp(), activity), 1);
                            Log.i(TAG, "      Pad " + (j + 1) + "-Up" + ", Sound loaded");
                        }
                        if (currentPreset.getMusic().getDecks()[i].getPad(j).getRight() != null) {
                            soundPoolId[i][j][2] = sp.load(activity, window.getRawId(currentPreset.getMusic().getDecks()[i].getPad(j).getRight(), activity), 1);
                            Log.i(TAG, "      Pad " + (j + 1) + "-Right" + ", Sound loaded");
                        }
                        if (currentPreset.getMusic().getDecks()[i].getPad(j).getDown() != null) {
                            soundPoolId[i][j][3] = sp.load(activity, window.getRawId(currentPreset.getMusic().getDecks()[i].getPad(j).getDown(), activity), 1);
                            Log.i(TAG, "      Pad " + (j + 1) + "-Down" + ", Sound loaded");
                        }
                        if (currentPreset.getMusic().getDecks()[i].getPad(j).getLeft() != null) {
                            soundPoolId[i][j][4] = sp.load(activity, window.getRawId(currentPreset.getMusic().getDecks()[i].getPad(j).getLeft(), activity), 1);
                            Log.i(TAG, "      Pad " + (j + 1) + "-Left" + ", Sound loaded");
                        }
                        publishProgress(i + 1);
                    }
                }
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            //Log.d(TAG, "Sound loaded " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "sampleId count : " + PRESET_SOUND_COUNTS[currentPreset.getMusic().getId() - 1]);
            sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    Log.d(TAG, "Loading Finished, sampleId : " + sampleId);
                    int presetSoundCount = PRESET_SOUND_COUNTS[currentPreset.getMusic().getId() - 1];
                    if(sampleId == presetSoundCount) {
                        // final sampleId
                        Log.d(TAG, "Loading completed, SoundPool successfully loaded "
                                + presetSoundCount
                                + " sounds");

                        window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset_done);
                        isPresetLoaded = true;

                        window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.icon_tutorial);
                        window.getImageView(R.id.layout_settings_tutorial_icon, activity).setImageResource(R.drawable.settings_tutorial);

                        anim.fadeOut(R.id.progress_bar_layout, 400, 400, activity);
                        MainActivity main = new MainActivity();
                        main.setQuickstart(activity);

                        Handler setText = new Handler();
                        setText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                window.getTextView(R.id.progress_bar_text, activity).setText(R.string.progressbar_loading_preset);
                            }
                        }, 800);

                        main.isPresetLoading = false;
                    }
                }
            });
        }
    }

    class UnloadSound extends AsyncTask<Void, Integer, String> {
        String TAG = "UnloadSound";
        SharedPreferences prefs;

        protected void onPreExecute() {
            Log.d(TAG, "On preExecute, set prefs");
            isPresetLoaded = false;
            if (window.getView(R.id.progress_bar_layout, activity).getVisibility() == View.GONE) {
                Log.d(TAG, "ProgressBar fadeIn");
                anim.fadeIn(R.id.progress_bar_layout, 0, 400, "progressIn", activity);
            }
            prefs = activity.getSharedPreferences("com.bedrock.padder", activity.MODE_PRIVATE);
        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground, start unloading sounds");
            try {
                if (previousPreset != null) {
                    Log.i(TAG, "Preset \"" + previousPreset.getMusic().getName() + "\", id " + previousPreset.getMusic().getId());
                    // deck loop
                    for (int i = 0; i < 4; i++) {
                        Log.i(TAG, "  Deck " + (i + 1));
                        // pad loop
                        for (int j = 0; j < 21; j++) {
                            Log.i(TAG, "    Pad " + (j + 1));
                            // pad gesture
                            if (previousPreset.getMusic().getDecks()[i].getPad(j).getRaw() != null) {
                                sp.unload(window.getRawId(previousPreset.getMusic().getDecks()[i].getPad(j).getRaw(), activity));
                                Log.i(TAG, "      Pad " + (j + 1) + "-Normal" + ", Sound unloaded");
                            }
                            if (previousPreset.getMusic().getDecks()[i].getPad(j).getUp() != null) {
                                sp.unload(window.getRawId(previousPreset.getMusic().getDecks()[i].getPad(j).getUp(), activity));
                                Log.i(TAG, "      Pad " + (j + 1) + "-Up" + ", Sound unloaded");
                            }
                            if (previousPreset.getMusic().getDecks()[i].getPad(j).getRight() != null) {
                                sp.unload(window.getRawId(previousPreset.getMusic().getDecks()[i].getPad(j).getRight(), activity));
                                Log.i(TAG, "      Pad " + (j + 1) + "-Right" + ", Sound unloaded");
                            }
                            if (previousPreset.getMusic().getDecks()[i].getPad(j).getDown() != null) {
                                sp.unload(window.getRawId(previousPreset.getMusic().getDecks()[i].getPad(j).getDown(), activity));
                                Log.i(TAG, "      Pad " + (j + 1) + "-Down" + ", Sound unloaded");
                            }
                            if (previousPreset.getMusic().getDecks()[i].getPad(j).getLeft() != null) {
                                sp.unload(window.getRawId(previousPreset.getMusic().getDecks()[i].getPad(j).getLeft(), activity));
                                Log.i(TAG, "      Pad " + (j + 1) + "-Left" + ", Sound unloaded");
                            }
                            publishProgress(i + 1);
                        }
                    }
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "NPE, Can't find soundPool");
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            //Log.d(TAG, "Sound unloaded " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Finished unloading sound");
            sp.release();
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            new LoadSound().execute();
        }
    }
}
