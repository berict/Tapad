package com.bedrock.padder.model.about;

public class Item {

    private String   textId;

    private String   hint;

    private Boolean  hintIsVisible;

    private String   imageId;

    private Boolean  runnableIsWithAnim;

    public Item (String textId, String hint) {
        this.textId = textId;
        this.hint = hint;
        this.hintIsVisible = true;
        this.imageId = textId;
        this.runnableIsWithAnim = false;
    }

    public Item (String textId, String hint, String imageId) {
        this.textId = textId;
        this.hint = hint;
        this.hintIsVisible = true;
        this.imageId = imageId;
        this.runnableIsWithAnim = false;
    }

    public Item (String textId, String hint, Boolean hintIsVisible) {
        this.textId = textId;
        this.hint = hint;
        this.hintIsVisible = hintIsVisible;
        this.imageId = textId;
        this.runnableIsWithAnim = false;
    }

    public Item (String textId, String hint, Boolean hintIsVisible, String imageId) {
        this.textId = textId;
        this.hint = hint;
        this.hintIsVisible = hintIsVisible;
        this.imageId = imageId;
        this.runnableIsWithAnim = false;
    }

    public Item (String textId, String hint, String imageId, Boolean runnableIsWithAnim) {
        this.textId = textId;
        this.hint = hint;
        this.hintIsVisible = true;
        this.imageId = imageId;
        this.runnableIsWithAnim = runnableIsWithAnim;
    }

    public Item (String textId, String hint, Boolean hintIsVisible, Boolean runnableIsWithAnim) {
        this.textId = textId;
        this.hint = hint;
        this.hintIsVisible = hintIsVisible;
        this.imageId = textId;
        this.runnableIsWithAnim = runnableIsWithAnim;
    }

    public String getTextId() {
        return textId;
    }

    public String getHintId() {
        return hint;
    }

    public String getImage() {
        return imageId;
    }

    public Boolean getHintIsVisible() {
        return hintIsVisible;
    }

    public Boolean getRunnableIsWithAnim() {
        return runnableIsWithAnim;
    }
}
