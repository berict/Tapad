package com.bedrock.padder.model.about;

import android.content.Context;

import static com.bedrock.padder.helper.WindowHelper.getStringFromIdWithFallback;

public class Item {

    private String text;

    private String hint;

    private Boolean isHintVisible;

    private String imageId;

    private Boolean isRunnableWithAnim;

    public Item(String text, String hint) {
        this.text = text;
        this.hint = hint;
        this.isHintVisible = true;
        this.imageId = text;
        this.isRunnableWithAnim = false;
    }

    public Item(String text, String hint, Boolean isHintVisible) {
        this.text = text;
        this.hint = hint;
        this.isHintVisible = isHintVisible;
        this.imageId = text;
        this.isRunnableWithAnim = false;
    }

    public Item(String text, String hint, Boolean isHintVisible, String imageId) {
        this.text = text;
        this.hint = hint;
        this.isHintVisible = isHintVisible;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public String getText(Context context) {
        return getStringFromIdWithFallback(getText(), context);
    }

    public String getHint() {
        return hint;
    }

    public String getHint(Context context) {
        return getStringFromIdWithFallback(getHint(), context);
    }

    public String getImage() {
        return imageId;
    }

    public Boolean isHintVisible() {
        return isHintVisible;
    }

    public Boolean isRunnableWithAnim() {
        if (isRunnableWithAnim != null) {
            return isRunnableWithAnim;
        } else {
            return false;
        }
    }
}
