package com.bedrock.padder.model.app.theme;

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

    public Integer getColorButtonFavorite(int index) {
        return colorButtonFavorite[index];
    }
}
