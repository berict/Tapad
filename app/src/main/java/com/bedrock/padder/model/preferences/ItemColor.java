package com.bedrock.padder.model.preferences;

import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class ItemColor {

    private Integer colorButton;

    private Integer[] colorButtonRecent = new Integer[0];

    public ItemColor(Integer colorButton, Integer[] colorButtonRecent) {
        this.colorButton = colorButton;
        this.colorButtonRecent = colorButtonRecent;
    }

    public ItemColor(Integer colorButton) {
        this.colorButton = colorButton;
        this.colorButtonRecent = new Integer[0];
    }

    public void setColorButton(Integer colorButton) {
        this.colorButton = colorButton;
        Log.d("ColorData", "Successfully changed");
        Log.d("ColorData", "Color : " + colorButton);
    }

    public void setColorButtonRecent(Integer[] colorButtonRecent) {
        this.colorButtonRecent = colorButtonRecent;
    }

    public Integer getColorButton() {
        return colorButton;
    }

    public Integer[] getColorButtonRecents() {
        return colorButtonRecent;
    }

    public Integer getColorButtonRecentLength() {
        return colorButtonRecent.length;
    }

    public Integer getColorButtonRecent(int index) {
        return colorButtonRecent[index];
    }

    public void addColorButtonRecent(Integer insertColor) {
        if (!ArrayUtils.contains(colorButtonRecent, insertColor)) {
            // validated, no duplicates
            colorButtonRecent = ArrayUtils.add(colorButtonRecent, insertColor);
            Log.d("ColorData", "Successfully inserted");
            Log.d("ColorData", "Recent : " + Arrays.deepToString(colorButtonRecent));
        } else {
            // duplicate exists
            Log.e("ColorData", "Duplicate exists");
        }
    }

    public void removeColorButtonRecent(Integer deleteColor) {
        if (ArrayUtils.contains(colorButtonRecent, deleteColor)) {
            // validated, value exists
            colorButtonRecent = ArrayUtils.removeElement(colorButtonRecent, deleteColor);
            Log.d("ColorData", "Successfully deleted");
            Log.d("ColorData", "Recent : " + Arrays.deepToString(colorButtonRecent));
        } else {
            // value missing
            Log.e("ColorData", "Value doesn't exists");
        }
    }
}