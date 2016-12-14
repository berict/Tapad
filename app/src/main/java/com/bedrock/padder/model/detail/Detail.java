package com.bedrock.padder.model.detail;

import android.app.Activity;

public class Detail {

    private String bio[];

    public Detail (Integer bioId, Activity activity) {
        this.bio = activity.getResources().getStringArray(bioId);
    }
}
