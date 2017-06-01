package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bedrock.padder.R;
import com.bedrock.padder.activity.MainActivity;
import com.bedrock.padder.model.preset.Preset;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.bedrock.padder.activity.MainActivity.currentPreset;
import static com.bedrock.padder.activity.MainActivity.isPresetLoading;
import static com.bedrock.padder.helper.WindowService.APPLICATION_ID;

public class SoundService {
    private SoundPool sp = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
    private int toggle;

    private int sp_id_1_00 = 0;
    private int sp_id_1_00_1 = 0;
    private int sp_id_1_00_2 = 0;
    private int sp_id_1_00_3 = 0;
    private int sp_id_1_00_4 = 0;
    private int sp_id_2_00 = 0;
    private int sp_id_2_00_1 = 0;
    private int sp_id_2_00_2 = 0;
    private int sp_id_2_00_3 = 0;
    private int sp_id_2_00_4 = 0;
    private int sp_id_3_00 = 0;
    private int sp_id_3_00_1 = 0;
    private int sp_id_3_00_2 = 0;
    private int sp_id_3_00_3 = 0;
    private int sp_id_3_00_4 = 0;
    private int sp_id_4_00 = 0;
    private int sp_id_4_00_1 = 0;
    private int sp_id_4_00_2 = 0;
    private int sp_id_4_00_3 = 0;
    private int sp_id_4_00_4 = 0;
    private int sp_id_1_01 = 0;
    private int sp_id_1_01_1 = 0;
    private int sp_id_1_01_2 = 0;
    private int sp_id_1_01_3 = 0;
    private int sp_id_1_01_4 = 0;
    private int sp_id_2_01 = 0;
    private int sp_id_2_01_1 = 0;
    private int sp_id_2_01_2 = 0;
    private int sp_id_2_01_3 = 0;
    private int sp_id_2_01_4 = 0;
    private int sp_id_3_01 = 0;
    private int sp_id_3_01_1 = 0;
    private int sp_id_3_01_2 = 0;
    private int sp_id_3_01_3 = 0;
    private int sp_id_3_01_4 = 0;
    private int sp_id_4_01 = 0;
    private int sp_id_4_01_1 = 0;
    private int sp_id_4_01_2 = 0;
    private int sp_id_4_01_3 = 0;
    private int sp_id_4_01_4 = 0;
    private int sp_id_1_02 = 0;
    private int sp_id_1_02_1 = 0;
    private int sp_id_1_02_2 = 0;
    private int sp_id_1_02_3 = 0;
    private int sp_id_1_02_4 = 0;
    private int sp_id_2_02 = 0;
    private int sp_id_2_02_1 = 0;
    private int sp_id_2_02_2 = 0;
    private int sp_id_2_02_3 = 0;
    private int sp_id_2_02_4 = 0;
    private int sp_id_3_02 = 0;
    private int sp_id_3_02_1 = 0;
    private int sp_id_3_02_2 = 0;
    private int sp_id_3_02_3 = 0;
    private int sp_id_3_02_4 = 0;
    private int sp_id_4_02 = 0;
    private int sp_id_4_02_1 = 0;
    private int sp_id_4_02_2 = 0;
    private int sp_id_4_02_3 = 0;
    private int sp_id_4_02_4 = 0;
    private int sp_id_1_03 = 0;
    private int sp_id_1_03_1 = 0;
    private int sp_id_1_03_2 = 0;
    private int sp_id_1_03_3 = 0;
    private int sp_id_1_03_4 = 0;
    private int sp_id_2_03 = 0;
    private int sp_id_2_03_1 = 0;
    private int sp_id_2_03_2 = 0;
    private int sp_id_2_03_3 = 0;
    private int sp_id_2_03_4 = 0;
    private int sp_id_3_03 = 0;
    private int sp_id_3_03_1 = 0;
    private int sp_id_3_03_2 = 0;
    private int sp_id_3_03_3 = 0;
    private int sp_id_3_03_4 = 0;
    private int sp_id_4_03 = 0;
    private int sp_id_4_03_1 = 0;
    private int sp_id_4_03_2 = 0;
    private int sp_id_4_03_3 = 0;
    private int sp_id_4_03_4 = 0;
    private int sp_id_1_04 = 0;
    private int sp_id_1_04_1 = 0;
    private int sp_id_1_04_2 = 0;
    private int sp_id_1_04_3 = 0;
    private int sp_id_1_04_4 = 0;
    private int sp_id_2_04 = 0;
    private int sp_id_2_04_1 = 0;
    private int sp_id_2_04_2 = 0;
    private int sp_id_2_04_3 = 0;
    private int sp_id_2_04_4 = 0;
    private int sp_id_3_04 = 0;
    private int sp_id_3_04_1 = 0;
    private int sp_id_3_04_2 = 0;
    private int sp_id_3_04_3 = 0;
    private int sp_id_3_04_4 = 0;
    private int sp_id_4_04 = 0;
    private int sp_id_4_04_1 = 0;
    private int sp_id_4_04_2 = 0;
    private int sp_id_4_04_3 = 0;
    private int sp_id_4_04_4 = 0;
    private int sp_id_1_11 = 0;
    private int sp_id_1_11_1 = 0;
    private int sp_id_1_11_2 = 0;
    private int sp_id_1_11_3 = 0;
    private int sp_id_1_11_4 = 0;
    private int sp_id_2_11 = 0;
    private int sp_id_2_11_1 = 0;
    private int sp_id_2_11_2 = 0;
    private int sp_id_2_11_3 = 0;
    private int sp_id_2_11_4 = 0;
    private int sp_id_3_11 = 0;
    private int sp_id_3_11_1 = 0;
    private int sp_id_3_11_2 = 0;
    private int sp_id_3_11_3 = 0;
    private int sp_id_3_11_4 = 0;
    private int sp_id_4_11 = 0;
    private int sp_id_4_11_1 = 0;
    private int sp_id_4_11_2 = 0;
    private int sp_id_4_11_3 = 0;
    private int sp_id_4_11_4 = 0;
    private int sp_id_1_12 = 0;
    private int sp_id_1_12_1 = 0;
    private int sp_id_1_12_2 = 0;
    private int sp_id_1_12_3 = 0;
    private int sp_id_1_12_4 = 0;
    private int sp_id_2_12 = 0;
    private int sp_id_2_12_1 = 0;
    private int sp_id_2_12_2 = 0;
    private int sp_id_2_12_3 = 0;
    private int sp_id_2_12_4 = 0;
    private int sp_id_3_12 = 0;
    private int sp_id_3_12_1 = 0;
    private int sp_id_3_12_2 = 0;
    private int sp_id_3_12_3 = 0;
    private int sp_id_3_12_4 = 0;
    private int sp_id_4_12 = 0;
    private int sp_id_4_12_1 = 0;
    private int sp_id_4_12_2 = 0;
    private int sp_id_4_12_3 = 0;
    private int sp_id_4_12_4 = 0;
    private int sp_id_1_13 = 0;
    private int sp_id_1_13_1 = 0;
    private int sp_id_1_13_2 = 0;
    private int sp_id_1_13_3 = 0;
    private int sp_id_1_13_4 = 0;
    private int sp_id_2_13 = 0;
    private int sp_id_2_13_1 = 0;
    private int sp_id_2_13_2 = 0;
    private int sp_id_2_13_3 = 0;
    private int sp_id_2_13_4 = 0;
    private int sp_id_3_13 = 0;
    private int sp_id_3_13_1 = 0;
    private int sp_id_3_13_2 = 0;
    private int sp_id_3_13_3 = 0;
    private int sp_id_3_13_4 = 0;
    private int sp_id_4_13 = 0;
    private int sp_id_4_13_1 = 0;
    private int sp_id_4_13_2 = 0;
    private int sp_id_4_13_3 = 0;
    private int sp_id_4_13_4 = 0;
    private int sp_id_1_14 = 0;
    private int sp_id_1_14_1 = 0;
    private int sp_id_1_14_2 = 0;
    private int sp_id_1_14_3 = 0;
    private int sp_id_1_14_4 = 0;
    private int sp_id_2_14 = 0;
    private int sp_id_2_14_1 = 0;
    private int sp_id_2_14_2 = 0;
    private int sp_id_2_14_3 = 0;
    private int sp_id_2_14_4 = 0;
    private int sp_id_3_14 = 0;
    private int sp_id_3_14_1 = 0;
    private int sp_id_3_14_2 = 0;
    private int sp_id_3_14_3 = 0;
    private int sp_id_3_14_4 = 0;
    private int sp_id_4_14 = 0;
    private int sp_id_4_14_1 = 0;
    private int sp_id_4_14_2 = 0;
    private int sp_id_4_14_3 = 0;
    private int sp_id_4_14_4 = 0;
    private int sp_id_1_21 = 0;
    private int sp_id_1_21_1 = 0;
    private int sp_id_1_21_2 = 0;
    private int sp_id_1_21_3 = 0;
    private int sp_id_1_21_4 = 0;
    private int sp_id_2_21 = 0;
    private int sp_id_2_21_1 = 0;
    private int sp_id_2_21_2 = 0;
    private int sp_id_2_21_3 = 0;
    private int sp_id_2_21_4 = 0;
    private int sp_id_3_21 = 0;
    private int sp_id_3_21_1 = 0;
    private int sp_id_3_21_2 = 0;
    private int sp_id_3_21_3 = 0;
    private int sp_id_3_21_4 = 0;
    private int sp_id_4_21 = 0;
    private int sp_id_4_21_1 = 0;
    private int sp_id_4_21_2 = 0;
    private int sp_id_4_21_3 = 0;
    private int sp_id_4_21_4 = 0;
    private int sp_id_1_22 = 0;
    private int sp_id_1_22_1 = 0;
    private int sp_id_1_22_2 = 0;
    private int sp_id_1_22_3 = 0;
    private int sp_id_1_22_4 = 0;
    private int sp_id_2_22 = 0;
    private int sp_id_2_22_1 = 0;
    private int sp_id_2_22_2 = 0;
    private int sp_id_2_22_3 = 0;
    private int sp_id_2_22_4 = 0;
    private int sp_id_3_22 = 0;
    private int sp_id_3_22_1 = 0;
    private int sp_id_3_22_2 = 0;
    private int sp_id_3_22_3 = 0;
    private int sp_id_3_22_4 = 0;
    private int sp_id_4_22 = 0;
    private int sp_id_4_22_1 = 0;
    private int sp_id_4_22_2 = 0;
    private int sp_id_4_22_3 = 0;
    private int sp_id_4_22_4 = 0;
    private int sp_id_1_23 = 0;
    private int sp_id_1_23_1 = 0;
    private int sp_id_1_23_2 = 0;
    private int sp_id_1_23_3 = 0;
    private int sp_id_1_23_4 = 0;
    private int sp_id_2_23 = 0;
    private int sp_id_2_23_1 = 0;
    private int sp_id_2_23_2 = 0;
    private int sp_id_2_23_3 = 0;
    private int sp_id_2_23_4 = 0;
    private int sp_id_3_23 = 0;
    private int sp_id_3_23_1 = 0;
    private int sp_id_3_23_2 = 0;
    private int sp_id_3_23_3 = 0;
    private int sp_id_3_23_4 = 0;
    private int sp_id_4_23 = 0;
    private int sp_id_4_23_1 = 0;
    private int sp_id_4_23_2 = 0;
    private int sp_id_4_23_3 = 0;
    private int sp_id_4_23_4 = 0;
    private int sp_id_1_24 = 0;
    private int sp_id_1_24_1 = 0;
    private int sp_id_1_24_2 = 0;
    private int sp_id_1_24_3 = 0;
    private int sp_id_1_24_4 = 0;
    private int sp_id_2_24 = 0;
    private int sp_id_2_24_1 = 0;
    private int sp_id_2_24_2 = 0;
    private int sp_id_2_24_3 = 0;
    private int sp_id_2_24_4 = 0;
    private int sp_id_3_24 = 0;
    private int sp_id_3_24_1 = 0;
    private int sp_id_3_24_2 = 0;
    private int sp_id_3_24_3 = 0;
    private int sp_id_3_24_4 = 0;
    private int sp_id_4_24 = 0;
    private int sp_id_4_24_1 = 0;
    private int sp_id_4_24_2 = 0;
    private int sp_id_4_24_3 = 0;
    private int sp_id_4_24_4 = 0;
    private int sp_id_1_31 = 0;
    private int sp_id_1_31_1 = 0;
    private int sp_id_1_31_2 = 0;
    private int sp_id_1_31_3 = 0;
    private int sp_id_1_31_4 = 0;
    private int sp_id_2_31 = 0;
    private int sp_id_2_31_1 = 0;
    private int sp_id_2_31_2 = 0;
    private int sp_id_2_31_3 = 0;
    private int sp_id_2_31_4 = 0;
    private int sp_id_3_31 = 0;
    private int sp_id_3_31_1 = 0;
    private int sp_id_3_31_2 = 0;
    private int sp_id_3_31_3 = 0;
    private int sp_id_3_31_4 = 0;
    private int sp_id_4_31 = 0;
    private int sp_id_4_31_1 = 0;
    private int sp_id_4_31_2 = 0;
    private int sp_id_4_31_3 = 0;
    private int sp_id_4_31_4 = 0;
    private int sp_id_1_32 = 0;
    private int sp_id_1_32_1 = 0;
    private int sp_id_1_32_2 = 0;
    private int sp_id_1_32_3 = 0;
    private int sp_id_1_32_4 = 0;
    private int sp_id_2_32 = 0;
    private int sp_id_2_32_1 = 0;
    private int sp_id_2_32_2 = 0;
    private int sp_id_2_32_3 = 0;
    private int sp_id_2_32_4 = 0;
    private int sp_id_3_32 = 0;
    private int sp_id_3_32_1 = 0;
    private int sp_id_3_32_2 = 0;
    private int sp_id_3_32_3 = 0;
    private int sp_id_3_32_4 = 0;
    private int sp_id_4_32 = 0;
    private int sp_id_4_32_1 = 0;
    private int sp_id_4_32_2 = 0;
    private int sp_id_4_32_3 = 0;
    private int sp_id_4_32_4 = 0;
    private int sp_id_1_33 = 0;
    private int sp_id_1_33_1 = 0;
    private int sp_id_1_33_2 = 0;
    private int sp_id_1_33_3 = 0;
    private int sp_id_1_33_4 = 0;
    private int sp_id_2_33 = 0;
    private int sp_id_2_33_1 = 0;
    private int sp_id_2_33_2 = 0;
    private int sp_id_2_33_3 = 0;
    private int sp_id_2_33_4 = 0;
    private int sp_id_3_33 = 0;
    private int sp_id_3_33_1 = 0;
    private int sp_id_3_33_2 = 0;
    private int sp_id_3_33_3 = 0;
    private int sp_id_3_33_4 = 0;
    private int sp_id_4_33 = 0;
    private int sp_id_4_33_1 = 0;
    private int sp_id_4_33_2 = 0;
    private int sp_id_4_33_3 = 0;
    private int sp_id_4_33_4 = 0;
    private int sp_id_1_34 = 0;
    private int sp_id_1_34_1 = 0;
    private int sp_id_1_34_2 = 0;
    private int sp_id_1_34_3 = 0;
    private int sp_id_1_34_4 = 0;
    private int sp_id_2_34 = 0;
    private int sp_id_2_34_1 = 0;
    private int sp_id_2_34_2 = 0;
    private int sp_id_2_34_3 = 0;
    private int sp_id_2_34_4 = 0;
    private int sp_id_3_34 = 0;
    private int sp_id_3_34_1 = 0;
    private int sp_id_3_34_2 = 0;
    private int sp_id_3_34_3 = 0;
    private int sp_id_3_34_4 = 0;
    private int sp_id_4_34 = 0;
    private int sp_id_4_34_1 = 0;
    private int sp_id_4_34_2 = 0;
    private int sp_id_4_34_3 = 0;
    private int sp_id_4_34_4 = 0;
    private int sp_id_1_41 = 0;
    private int sp_id_1_41_1 = 0;
    private int sp_id_1_41_2 = 0;
    private int sp_id_1_41_3 = 0;
    private int sp_id_1_41_4 = 0;
    private int sp_id_2_41 = 0;
    private int sp_id_2_41_1 = 0;
    private int sp_id_2_41_2 = 0;
    private int sp_id_2_41_3 = 0;
    private int sp_id_2_41_4 = 0;
    private int sp_id_3_41 = 0;
    private int sp_id_3_41_1 = 0;
    private int sp_id_3_41_2 = 0;
    private int sp_id_3_41_3 = 0;
    private int sp_id_3_41_4 = 0;
    private int sp_id_4_41 = 0;
    private int sp_id_4_41_1 = 0;
    private int sp_id_4_41_2 = 0;
    private int sp_id_4_41_3 = 0;
    private int sp_id_4_41_4 = 0;
    private int sp_id_1_42 = 0;
    private int sp_id_1_42_1 = 0;
    private int sp_id_1_42_2 = 0;
    private int sp_id_1_42_3 = 0;
    private int sp_id_1_42_4 = 0;
    private int sp_id_2_42 = 0;
    private int sp_id_2_42_1 = 0;
    private int sp_id_2_42_2 = 0;
    private int sp_id_2_42_3 = 0;
    private int sp_id_2_42_4 = 0;
    private int sp_id_3_42 = 0;
    private int sp_id_3_42_1 = 0;
    private int sp_id_3_42_2 = 0;
    private int sp_id_3_42_3 = 0;
    private int sp_id_3_42_4 = 0;
    private int sp_id_4_42 = 0;
    private int sp_id_4_42_1 = 0;
    private int sp_id_4_42_2 = 0;
    private int sp_id_4_42_3 = 0;
    private int sp_id_4_42_4 = 0;
    private int sp_id_1_43 = 0;
    private int sp_id_1_43_1 = 0;
    private int sp_id_1_43_2 = 0;
    private int sp_id_1_43_3 = 0;
    private int sp_id_1_43_4 = 0;
    private int sp_id_2_43 = 0;
    private int sp_id_2_43_1 = 0;
    private int sp_id_2_43_2 = 0;
    private int sp_id_2_43_3 = 0;
    private int sp_id_2_43_4 = 0;
    private int sp_id_3_43 = 0;
    private int sp_id_3_43_1 = 0;
    private int sp_id_3_43_2 = 0;
    private int sp_id_3_43_3 = 0;
    private int sp_id_3_43_4 = 0;
    private int sp_id_4_43 = 0;
    private int sp_id_4_43_1 = 0;
    private int sp_id_4_43_2 = 0;
    private int sp_id_4_43_3 = 0;
    private int sp_id_4_43_4 = 0;
    private int sp_id_1_44 = 0;
    private int sp_id_1_44_1 = 0;
    private int sp_id_1_44_2 = 0;
    private int sp_id_1_44_3 = 0;
    private int sp_id_1_44_4 = 0;
    private int sp_id_2_44 = 0;
    private int sp_id_2_44_1 = 0;
    private int sp_id_2_44_2 = 0;
    private int sp_id_2_44_3 = 0;
    private int sp_id_2_44_4 = 0;
    private int sp_id_3_44 = 0;
    private int sp_id_3_44_1 = 0;
    private int sp_id_3_44_2 = 0;
    private int sp_id_3_44_3 = 0;
    private int sp_id_3_44_4 = 0;
    private int sp_id_4_44 = 0;
    private int sp_id_4_44_1 = 0;
    private int sp_id_4_44_2 = 0;
    private int sp_id_4_44_3 = 0;
    private int sp_id_4_44_4 = 0;

    private int soundPoolId[][][] = {
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

    private Activity activity;
    private static Preset previousPreset = null;

    private int buttonId[] = {
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
            R.id.btn44
    };

    public SoundPool getSoundPool() {
        return sp;
    }

    private AdmobService ad = new AdmobService();
    private AnimService anim = new AnimService();
    private WindowService window = new WindowService();

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

    private AsyncTask unLoadSound = null;
    private AsyncTask loadSound = null;

    @Deprecated
    public void loadSchemeSound(Preset preset, Activity a) {
        // set the previous preset
        previousPreset = currentPreset;
        currentPreset = preset;
        activity = a;
        unLoadSound = new UnloadSound().execute();
    }

    public void loadSound(Preset preset, Activity activity) {
        // set the previous preset
        previousPreset = currentPreset;
        currentPreset = preset;
        this.activity = activity;
        unLoadSound = new UnloadSound().execute();
    }

    public void cancelLoading() {
        try {
            unLoadSound.cancel(true);
            loadSound.cancel(true);
            Log.d("TAG", "AsyncTask canceled");
        } catch (NullPointerException e) {
            Log.d("NPE", "AsyncTask is null");
        }
    }

    public void playToggleButtonSound(int id) {
        sp.play(soundPoolId[id - 1][id][0], 1, 1, 1, 0, 1f);
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
        if (isPresetLoading == false) {
            for (int i = 0; i < 21; i++) {
                if (i == 0 || i > 4) {
                    if (id >= 1 && id <= 4) {
                        if (currentPreset.getMusic().getGesture()) {
                            window.setOnGestureSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], activity);
                        } else {
                            window.setOnTouchSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], activity);
                        }
                    }
                }
            }
            Log.i("SoundService", "ToggleButton sound set id " + String.valueOf(id));
        } else {
            setButton(colorId, activity);
        }
    }

    public void setButtonToggle(int id, int colorId, int patternId, Activity activity) {

        int pattern1[][][] = {
                {{}, {R.id.btn12, R.id.btn21}},
                {{}, {R.id.btn11, R.id.btn22, R.id.btn13}},
                {{}, {R.id.btn12, R.id.btn23, R.id.btn14}},
                {{}, {R.id.btn13, R.id.btn24}},
                {{}, {R.id.btn11, R.id.btn22, R.id.btn31}},
                {{}, {R.id.btn12, R.id.btn21, R.id.btn32, R.id.btn23}},
                {{}, {R.id.btn13, R.id.btn22, R.id.btn33, R.id.btn24}},
                {{}, {R.id.btn14, R.id.btn23, R.id.btn34}},
                {{}, {R.id.btn21, R.id.btn32, R.id.btn41}},
                {{}, {R.id.btn31, R.id.btn33, R.id.btn22, R.id.btn42}},
                {{}, {R.id.btn32, R.id.btn34, R.id.btn23, R.id.btn43}},
                {{}, {R.id.btn33, R.id.btn24, R.id.btn44}},
                {{}, {R.id.btn31, R.id.btn42}},
                {{}, {R.id.btn41, R.id.btn32, R.id.btn43}},
                {{}, {R.id.btn42, R.id.btn33, R.id.btn44}},
                {{}, {R.id.btn34, R.id.btn43}}
        };

        int pattern2[][][] = {
                {{R.id.btn12}, {R.id.btn13}, {R.id.btn14}},
                {{R.id.btn11, R.id.btn13}, {R.id.btn14}},
                {{R.id.btn12, R.id.btn14}, {R.id.btn11}},
                {{R.id.btn13}, {R.id.btn12}, {R.id.btn11}},
                {{R.id.btn22}, {R.id.btn23}, {R.id.btn24}},
                {{R.id.btn21, R.id.btn23}, {R.id.btn24}},
                {{R.id.btn24, R.id.btn22}, {R.id.btn21}},
                {{R.id.btn23}, {R.id.btn22}, {R.id.btn21}},
                {{R.id.btn32}, {R.id.btn33}, {R.id.btn34}},
                {{R.id.btn31, R.id.btn33}, {R.id.btn34}},
                {{R.id.btn34, R.id.btn32}, {R.id.btn31}},
                {{R.id.btn33}, {R.id.btn32}, {R.id.btn31}},
                {{R.id.btn42}, {R.id.btn43}, {R.id.btn44}},
                {{R.id.btn41, R.id.btn43}, {R.id.btn44}},
                {{R.id.btn44, R.id.btn42}, {R.id.btn41}},
                {{R.id.btn43}, {R.id.btn42}, {R.id.btn41}}
        };

        int pattern3[][][] = {
                {{R.id.btn21}, {R.id.btn31}, {R.id.btn41}},
                {{R.id.btn22}, {R.id.btn32}, {R.id.btn42}},
                {{R.id.btn23}, {R.id.btn33}, {R.id.btn43}},
                {{R.id.btn24}, {R.id.btn34}, {R.id.btn44}},
                {{R.id.btn11, R.id.btn31}, {R.id.btn41}},
                {{R.id.btn12, R.id.btn32}, {R.id.btn42}},
                {{R.id.btn13, R.id.btn33}, {R.id.btn43}},
                {{R.id.btn14, R.id.btn34}, {R.id.btn44}},
                {{R.id.btn41, R.id.btn21}, {R.id.btn11}},
                {{R.id.btn42, R.id.btn22}, {R.id.btn12}},
                {{R.id.btn43, R.id.btn23}, {R.id.btn13}},
                {{R.id.btn44, R.id.btn24}, {R.id.btn14}},
                {{R.id.btn31}, {R.id.btn21}, {R.id.btn11}},
                {{R.id.btn32}, {R.id.btn22}, {R.id.btn12}},
                {{R.id.btn33}, {R.id.btn23}, {R.id.btn13}},
                {{R.id.btn34}, {R.id.btn24}, {R.id.btn14}}
        };

        int pattern4[][][] = {
                {{R.id.btn12, R.id.btn21}, {R.id.btn13, R.id.btn31}, {R.id.btn14, R.id.btn41}},
                {{R.id.btn11, R.id.btn22, R.id.btn13}, {R.id.btn14, R.id.btn32}, {R.id.btn42}},
                {{R.id.btn12, R.id.btn14, R.id.btn23}, {R.id.btn11, R.id.btn33}, {R.id.btn43}},
                {{R.id.btn13, R.id.btn24}, {R.id.btn12, R.id.btn34}, {R.id.btn11, R.id.btn44}},
                {{R.id.btn11, R.id.btn22, R.id.btn31}, {R.id.btn23, R.id.btn41}, {R.id.btn24}},
                {{R.id.btn12, R.id.btn21, R.id.btn23, R.id.btn32}, {R.id.btn24, R.id.btn42}},
                {{R.id.btn13, R.id.btn22, R.id.btn24, R.id.btn33}, {R.id.btn21, R.id.btn43}},
                {{R.id.btn14, R.id.btn23, R.id.btn34}, {R.id.btn22, R.id.btn44}, {R.id.btn21}},
                {{R.id.btn21, R.id.btn32, R.id.btn41}, {R.id.btn11, R.id.btn33}, {R.id.btn34}},
                {{R.id.btn22, R.id.btn31, R.id.btn33, R.id.btn42}, {R.id.btn12, R.id.btn34}},
                {{R.id.btn23, R.id.btn32, R.id.btn34, R.id.btn43}, {R.id.btn13, R.id.btn31}},
                {{R.id.btn24, R.id.btn33, R.id.btn44}, {R.id.btn14, R.id.btn32}, {R.id.btn31}},
                {{R.id.btn31, R.id.btn42}, {R.id.btn21, R.id.btn43}, {R.id.btn11, R.id.btn44}},
                {{R.id.btn32, R.id.btn41, R.id.btn43}, {R.id.btn22, R.id.btn44}, {R.id.btn12}},
                {{R.id.btn33, R.id.btn42, R.id.btn44}, {R.id.btn23, R.id.btn41}, {R.id.btn13}},
                {{R.id.btn34, R.id.btn43}, {R.id.btn42, R.id.btn24}, {R.id.btn14, R.id.btn41}}
        };

        int pattern[][][] = {};

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

        if (isPresetLoading == false) {
            for (int i = 0; i < 21; i++) {
                if (i == 0 || i > 4) {
                    if (id >= 1 && id <= 4) {
                        if (currentPreset.getMusic().getGesture()) {
                            window.setOnGestureSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], pattern, activity);
                        } else {
                            window.setOnTouchSound(buttonId[i], colorId, R.color.grey, sp, soundPoolId[id - 1][i], pattern, activity);
                        }
                    }
                }
            }
            Log.i("SoundService", "ToggleButton pattern set id " + String.valueOf(patternId));
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

    private TextView progress;
    private TextView progressText;
    private int progressCount;
    private int presetSoundCount;

    private class UnloadSound extends AsyncTask<Void, Void, String> {
        String TAG = "UnloadSound";
        SharedPreferences prefs;

        protected void onPreExecute() {
            Log.d(TAG, "On preExecute, set prefs");
            isPresetLoading = true;
            progressCount = 0;
            presetSoundCount = currentPreset.getMusic().getSoundCount();
            ad.resumeNativeAdView(R.id.adView_main, activity);
            progress = window.getTextView(R.id.progress_bar_progress_text, activity);
            progressText = window.getTextView(R.id.progress_bar_text, activity);
            if (window.getView(R.id.progress_bar_layout, activity).getVisibility() == View.GONE) {
                Log.d(TAG, "ProgressBar fadeIn");
                //TODO EDIT
                anim.fadeIn(R.id.progress_bar_layout, 0, 400, "progressIn", activity);
                window.setInvisible(R.id.base, 400, activity);
                progress.setText(
                        activity.getResources().getString(R.string.progressbar_loading_preset_progress) + " 0 / " + presetSoundCount * 2);
            }
            prefs = activity.getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground, start unloading sounds");
            try {
                if (previousPreset != null) {
                    Log.i(TAG, "Preset \"" + window.getStringFromId(previousPreset.getMusic().getName(), activity));
                    // deck loop
                    for (int i = 0; i < 4; i++) {
                        Log.i(TAG, "  Deck " + (i + 1));
                        // pad loop
                        for (int j = 0; j < 21; j++) {
                            Log.i(TAG, "    Pad " + (j + 1));
                            // pad gesture
                            for (int k = 0; k < 5; k++) {
                                String sound = previousPreset.getSound(i, j, k);
                                if (soundPoolId[i][j][k] != 0 && sound != null) {
                                    if (sp.unload(soundPoolId[i][j][k])) {
                                        Log.i(TAG, "      Pad " + (j + 1) + " Gesture " + k + ", Sound unloaded");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "NPE, Can't find soundPool");
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Void... arg0) {
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Finished unloading sound");
            sp.release();
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            loadSound = new LoadSound().execute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("TAG", "UnLoadSound successfully canceled");
        }
    }

    private void onLoadFinished() {
        // final sampleId
        Log.d("LoadSound", "Loading completed, SoundPool successfully loaded "
                + presetSoundCount
                + " sounds");

        // pause adViewMain after the loading
        ad.pauseNativeAdView(R.id.adView_main, activity);

        progressText.setText(R.string.progressbar_loading_preset_done);
        anim.fadeOutInvisible(R.id.progress_bar_progress_text, 0, 400, activity);

        // Load finished, set AsyncTask objects to null
        loadSound = null;
        unLoadSound = null;

        window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.ic_tutorial_black);

        anim.fadeOut(R.id.progress_bar_layout, 400, 400, activity);
        window.setVisible(R.id.base, 400, activity);

        MainActivity main = new MainActivity();
        main.setQuickstart(activity);

        Handler setText = new Handler();
        setText.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressText.setText(R.string.progressbar_loading_preset);
                progress.setText(R.string.progressbar_loading_preset_progress_placeholder);
                progress.setVisibility(View.VISIBLE);
            }
        }, 800);

        isPresetLoading = false;
    }

    private void progressUpdate() {
        progress.setText(
                activity.getResources().getString(R.string.progressbar_loading_preset_progress) + " "
                        + progressCount++ + " / " + presetSoundCount);
    }

    private class LoadSound extends AsyncTask<Void, Void, String> {
        String TAG = "LoadSound";

        protected void onPreExecute() {
            Log.d(TAG, "On preExceute, unloadSchemeSound");

            window.getImageView(R.id.toolbar_tutorial_icon, activity).setImageResource(R.drawable.ic_tutorial_disabled_black);
            //window.getImageView(R.id.layout_settings_tutorial_icon, activity).setImageResource(R.drawable.settings_tutorial_disabled);
        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground, start loading sounds");
            
            if (currentPreset != null) {
                Log.i(TAG, "Preset \"" + window.getStringFromId(currentPreset.getMusic().getName(), activity));
                // deck loop
                for (int i = 0; i < 4; i++) {
                    Log.i(TAG, "  Deck " + (i + 1));
                    // pad loop
                    for (int j = 0; j < 21; j++) {
                        Log.i(TAG, "    Pad " + (j + 1));
                        // pad gesture
                        for (int k = 0; k < 5; k++) {
                            String sound = currentPreset.getSound(i, j, k);
                            if (sound != null) {
                                soundPoolId[i][j][k] = sp.load(sound, 1);
                                Log.i(TAG, sound + " loaded");
                                Log.i(TAG, "      Pad " + (j + 1) + " Gesture " + k + ", Sound loaded");
                                publishProgress();
                            }
                        }
                    }
                }
            }
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Void... arg0) {
            progressUpdate();
        }

        private int savedSampleId = 0;
        private int savedSampleIdInRunnable = 1;
        // needs to be different at first to make changes

        protected void onPostExecute(String result) {
            Log.d(TAG, "sampleId count : " + presetSoundCount);

            // gets random funnies
            progress.setText(getRandomStringFromStringArray(R.array.progressbar_loading_preset_progress_funnies));

            final Handler intervalTimer = new Handler();

            sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, final int sampleId, int status) {
                    Log.d(TAG, "Loading Finished, sampleId : " + sampleId);
                    savedSampleId = sampleId;
                }
            });

            intervalTimer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // loops while checking the last saved sample id and current one
                    // if same, break the loop
                    if (savedSampleId == savedSampleIdInRunnable) {
                        // finished
                        Log.d(TAG, "Finished loading all sounds");
                        onLoadFinished();
                    } else {
                        // updated
                        savedSampleIdInRunnable = savedSampleId;
                        intervalTimer.postDelayed(this, 100);
                    }
                }
            }, 100);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("TAG", "LoadSound successfully canceled");
        }
    }

    private String getRandomStringFromStringArray(int stringArrayId) {
        String stringArray[] = activity.getResources().getStringArray(stringArrayId);
        Random random = new Random();
        return stringArray[random.nextInt(stringArray.length)];
    }
}
