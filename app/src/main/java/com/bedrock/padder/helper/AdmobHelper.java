package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;

public class AdmobHelper {
    private WindowHelper window = new WindowHelper();
    private String TAG = "AdmobHelper";
    
    public AdView getAdView(int id, Activity activity) {
        return window.getAdView(id, activity);
    }

    public void pauseAdView(int id, Activity activity) {
        AdView adView = window.getAdView(id, activity);
        if (adView != null) {
            adView.pause();
            Log.d(TAG, "ad paused");
        }
    }

    public void resumeAdView(int id, Activity activity) {
        AdView adView = window.getAdView(id, activity);
        if (adView != null) {
            adView.resume();
            Log.d(TAG, "ad resumed");
        }
    }

    public void destroyAdView(int id, Activity activity) {
        AdView adView = window.getAdView(id, activity);
        if (adView != null) {
            adView.destroy();
            Log.d(TAG, "ad destroyed");
        }
    }

    public void setAdViewSize(int id, AdSize adSize, Activity activity) {
        AdView adView = window.getAdView(id, activity);
        adView.setAdSize(adSize);
    }

    public void setAdViewSize(AdView adView, AdSize adSize, Activity activity) {
        adView.setAdSize(adSize);
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
        adView.loadAd(getAdRequest());
        Log.d(TAG, "ad requested and loaded");
    }

    public void requestLoadNativeAd(NativeExpressAdView adView) {
        adView.loadAd(getAdRequest());
        Log.d(TAG, "ad requested and loaded");
    }

    public NativeExpressAdView getNativeAdView(int id, Activity activity) {
        return window.getNativeAdView(id, activity);
    }

    public void pauseNativeAdView(int id, Activity activity) {
        NativeExpressAdView adView = window.getNativeAdView(id, activity);
        if (adView != null) {
            adView.pause();
            Log.d(TAG, "ad paused");
        }
    }

    public void resumeNativeAdView(int id, Activity activity) {
        NativeExpressAdView adView = window.getNativeAdView(id, activity);
        if (adView != null) {
            adView.resume();
            Log.d(TAG, "ad resumed");
        }
    }

    public void destroyNativeAdView(int id, Activity activity) {
        NativeExpressAdView adView = window.getNativeAdView(id, activity);
        if (adView != null) {
            adView.destroy();
            Log.d(TAG, "ad destroyed");
        }
    }

    public void setNativeAdViewSize(int id, AdSize adSize, Activity activity) {
        NativeExpressAdView adView = window.getNativeAdView(id, activity);
        adView.setAdSize(adSize);
    }

    public void setNativeAdViewSize(NativeExpressAdView adView, AdSize adSize, Activity activity) {
        adView.setAdSize(adSize);
    }

    public boolean isConnected(Context context) {
        // returns whether the device is connected to the internet
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}