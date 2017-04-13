package com.bedrock.padder.model.app.theme;

import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

public class ColorData {

    private Integer colorButton;

    private Integer[] colorButtonFavorite;

    public ColorData(Integer colorButton, Integer[] colorButtonFavorite) {
        this.colorButton = colorButton;
        this.colorButtonFavorite = colorButtonFavorite;
    }

    public ColorData(Integer colorButton) {
        this.colorButton = colorButton;
        this.colorButtonFavorite = null;
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
            ArrayUtils.add(colorButtonFavorite, insertColor);
            Log.d("ColorData", "Successfully inserted");
        } else {
            // duplicate exists
            Log.e("ColorData", "Duplicate exists");
        }
    }

    public void removeColorButtonFavorite(Integer deleteColor) {
        if (ArrayUtils.contains(colorButtonFavorite, deleteColor)) {
            // validated, no duplicates
            ArrayUtils.removeElement(colorButtonFavorite, deleteColor);
            Log.d("ColorData", "Successfully deleted");
        } else {
            // duplicate exists
            Log.e("ColorData", "Value doesn't exists");
        }
    }
}