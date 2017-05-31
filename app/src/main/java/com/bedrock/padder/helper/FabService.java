package com.bedrock.padder.helper;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.bedrock.padder.R;

public class FabService {

    public static int FAB = R.id.fab;

    private FloatingActionButton floatingActionButton;

    private AnimService anim = new AnimService();

    public void setFab(Activity activity) {
        floatingActionButton = (FloatingActionButton)activity.findViewById(FAB);
    }

    public void setFabColor(int color, Activity activity) {
        try {
            try {
                floatingActionButton.setBackgroundColor(ContextCompat.getColor(activity, color));
            } catch (Exception e) {
                floatingActionButton.setBackgroundColor(color);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("setFabColor", "Fab is not initialized");
        }
    }

    public void setFabIcon(int iconRes, Activity activity) {
        floatingActionButton.setImageResource(iconRes);
    }

    public void showFab() {
        try {
            anim.scaleIn(floatingActionButton, 0, 400, "fabIn");
        } catch (NullPointerException e) {
            Log.i("showFab", "Fab is not initialized");
        }
    }

    public void showFab(int delay) {
        try {
            anim.scaleIn(floatingActionButton, delay, 400, "fabIn");
        } catch (NullPointerException e) {
            Log.i("showFab", "Fab is not initialized");
        }
    }

    public void showFab(int delay, int duration) {
        try {
            anim.scaleIn(floatingActionButton, delay, duration, "fabIn");
        } catch (NullPointerException e) {
            Log.i("showFab", "Fab is not initialized");
        }
    }

    public void hideFab() {
        try {
            anim.scaleOut(floatingActionButton, 0, 400, "fabOut");
        } catch (NullPointerException e) {
            Log.i("hideFab", "Fab is not initialized");
        }
    }

    public void hideFab(int delay) {
        try {
            anim.scaleOut(floatingActionButton, delay, 400, "fabOut");
        } catch (NullPointerException e) {
            Log.i("hideFab", "Fab is not initialized");
        }
    }

    public void hideFab(int delay, int duration) {
        try {
            anim.scaleOut(floatingActionButton, delay, duration, "fabOut");
        } catch (NullPointerException e) {
            Log.i("hideFab", "Fab is not initialized");
        }
    }

    public void setFabOnClickListener(final Runnable runnable) {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run();
            }
        });
    }
}
