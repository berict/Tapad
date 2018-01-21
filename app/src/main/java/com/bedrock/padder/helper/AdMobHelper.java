package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdMobHelper {
    private WindowHelper window = new WindowHelper();
    private String TAG = "AdMobHelper";
    
    public AdView getAdView(int id, Activity activity) {
        return ((AdView) activity.findViewById(id));
    }

    public void pauseAdView(int id, Activity activity) {
        AdView adView = ((AdView) activity.findViewById(id));
        if (adView != null) {
            adView.pause();
            Log.d(TAG, "ad paused");
        }
    }

    public void resumeAdView(int id, Activity activity) {
        AdView adView = ((AdView) activity.findViewById(id));
        if (adView != null) {
            adView.resume();
            Log.d(TAG, "ad resumed");
        }
    }

    public void destroyAdView(int id, Activity activity) {
        AdView adView = ((AdView) activity.findViewById(id));
        if (adView != null) {
            adView.destroy();
            Log.d(TAG, "ad destroyed");
        }
    }

    public AdRequest getAdRequest() {
        Log.d(TAG, "ad requested");
        return new AdRequest.Builder().build();
    }

    public void loadAd(AdView adView, AdRequest adRequest) {
        adView.loadAd(adRequest);
        Log.d(TAG, "ad loaded");
    }

    public void requestLoadAd(AdView adView) {
        Log.d(TAG, "ad requested and loaded");
        loadAd(adView, getAdRequest());
    }

    public boolean isConnected(Context context) {
        // returns whether the device is connected to the internet
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}