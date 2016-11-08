package com.bedrock.padder.helper;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;

public class TextService {
    public void setEditText(int editText, int editTextLayout, String type, int hint, int errorMessage, Activity activity) {
        AppCompatEditText text = (AppCompatEditText)activity.findViewById(editText);
        text.addTextChangedListener(new AdvancedTextWatcher(editText, editTextLayout, type, hint, errorMessage, activity));
    }

    public void setColor(int editText, int colorId, Activity activity){
        AppCompatEditText text = (AppCompatEditText)activity.findViewById(editText);
        text.getBackground().mutate().setColorFilter(activity.getResources().getColor(colorId), PorterDuff.Mode.SRC_ATOP);
    }

    public void clearEditText(int editText, Activity activity){
        AppCompatEditText text = (AppCompatEditText)activity.findViewById(editText);
        text.getText().clear();
        text.setText(null);
    }

    public void clearEditTextLayout(int editTextLayout, Activity activity){
        TextInputLayout layout = (TextInputLayout)activity.findViewById(editTextLayout);
        layout.setErrorEnabled(false);
    }

    public String getEditText(int editText, Activity activity) {
        AppCompatEditText text = (AppCompatEditText)activity.findViewById(editText);

        return text.getText().toString();
    }

    public boolean isvalidAll(int editText[], String type[], Activity activity){
        if((editText.length != type.length) || (editText.length == 0)){
            Log.d("TextService", "Error : editText object and type doesn't match or editText object has no elements");
            return false;
        } else {
            boolean vaild = true;
            for (int i = 0; i < editText.length; i++) {
                if(type[i].equals("text")){
                    if(!isTextValid(editText[i], activity)){
                        vaild = false;
                        break;
                    }
                } else if(type[i].equals("uri")){
                    if(!isUriValid(editText[i], activity)){
                        vaild = false;
                        break;
                    }
                }
            }
            return vaild;
        }
    }

    public boolean isvalidAll(int editText[], Activity activity){
        if(editText.length == 0){
            Log.d("TextService", "Error : editText object has no elements");
            return false;
        } else {
            boolean vaild = true;
            for (int i = 0; i < editText.length; i++) {
                if(!isTextValid(editText[i], activity)){
                    vaild = false;
                    break;
                }
            }
            return vaild;
        }
    }

    public boolean isTextValid(int editText, Activity activity) {
        AppCompatEditText text = (AppCompatEditText)activity.findViewById(editText);

        return text.getText().toString().trim().length() > 0;
    }

    public boolean isUriValid(int editText, Activity activity) {
        AppCompatEditText text = (AppCompatEditText)activity.findViewById(editText);

        return Patterns.WEB_URL.matcher(text.getText().toString()).matches() && isTextValid(editText, activity);
    }

    public void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class AdvancedTextWatcher implements TextWatcher {

        private int editText;
        private int editTextLayout;
        private String type;
        private String hintString;
        private String errorMessageString;
        private Activity activity;

        private AppCompatEditText text;
        private TextInputLayout layout;

        private AdvancedTextWatcher(int editText, int editTextLayout, String type, int hint, int errorMessage, Activity activity) {
            this.editText = editText;
            this.editTextLayout = editTextLayout;
            this.type = type;
            this.hintString = activity.getString(hint);
            this.errorMessageString = activity.getString(errorMessage);
            this.activity = activity;

            text = (AppCompatEditText)activity.findViewById(editText);
            layout = (TextInputLayout)activity.findViewById(editTextLayout);
            layout.setHint(hintString);
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            if(type.equals("text")) {
                if (isTextValid(editText, activity)) {
                    layout.setErrorEnabled(false);
                } else {
                    layout.setError(errorMessageString);
                    requestFocus(text, activity);
                }
            } else {
                if (isUriValid(editText, activity)) {
                    layout.setErrorEnabled(false);
                } else {
                    layout.setError(errorMessageString);
                    requestFocus(text, activity);
                }
            }
        }
    }
}
