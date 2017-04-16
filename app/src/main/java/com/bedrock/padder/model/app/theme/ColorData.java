package com.bedrock.padder.model.app.theme;

import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class ColorData {

    private Integer colorButton;

    private Integer[] colorButtonFavorite;

    public ColorData(Integer colorButton, Integer[] colorButtonFavorite) {
        this.colorButton = colorButton;
        this.colorButtonFavorite = colorButtonFavorite;
    }

    public ColorData(Integer colorButton) {
        this.colorButton = colorButton;
        this.colorButtonFavorite = new Integer[0];
    }

    public void setColorButton(Integer colorButton) {
        this.colorButton = colorButton;
        Log.d("ColorData", "Successfully changed");
        Log.d("ColorData", "Color : " + colorButton);
    }

    public void setColorButtonFavorite(Integer[] colorButtonFavorite) {
        this.colorButtonFavorite = colorButtonFavorite;
    }

    public Integer getColorButton() {
        return colorButton;
    }

    public Integer[] getColorButtonFavorites() {
        return colorButtonFavorite;
    }

    public Integer getColorButtonFavoriteLength() {
        return colorButtonFavorite.length;
    }

    public Integer getColorButtonFavorite(int index) {
        return colorButtonFavorite[index];
    }

    public void addColorButtonFavorite(Integer insertColor) {
        if (!ArrayUtils.contains(colorButtonFavorite, insertColor)) {
            // validated, no duplicates
            colorButtonFavorite = ArrayUtils.add(colorButtonFavorite, insertColor);
            Log.d("ColorData", "Successfully inserted");
            Log.d("ColorData", "Favorite : " + Arrays.deepToString(colorButtonFavorite));
        } else {
            // duplicate exists
            Log.e("ColorData", "Duplicate exists");
        }
    }

    public void removeColorButtonFavorite(Integer deleteColor) {
        if (ArrayUtils.contains(colorButtonFavorite, deleteColor)) {
            // validated, value exists
            colorButtonFavorite = ArrayUtils.removeElement(colorButtonFavorite, deleteColor);
            Log.d("ColorData", "Successfully deleted");
            Log.d("ColorData", "Favorite : " + Arrays.deepToString(colorButtonFavorite));
        } else {
            // value missing
            Log.e("ColorData", "Value doesn't exists");
        }
    }
}