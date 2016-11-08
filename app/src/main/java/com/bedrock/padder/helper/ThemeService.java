package com.bedrock.padder.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.bedrock.padder.R;

@TargetApi(22)
public class ThemeService extends Activity {

    public void setBackground (int view_id, int drawable_id, Activity activity){
        View view = (View)activity.findViewById(view_id);

        Drawable drawable;

        if (Build.VERSION.SDK_INT >= 21) {
            drawable = activity.getResources().getDrawable(drawable_id, activity.getTheme());
        } else {
            drawable = activity.getResources().getDrawable(drawable_id);
        }

        if (Build.VERSION.SDK_INT >= 16){
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }

        /** Dammit google, stop deprecating methods! */
    }

    public void setBackgroundRev(int drawable_id, int view_id, Activity activity){
        View view = (View)activity.findViewById(view_id);

        Drawable drawable;

        if (Build.VERSION.SDK_INT >= 21) {
            drawable = activity.getResources().getDrawable(drawable_id, activity.getTheme());
        } else {
            drawable = activity.getResources().getDrawable(drawable_id);
        }

        if (Build.VERSION.SDK_INT >= 16){
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }

        /** Dammit google, stop deprecating methods! */
    }

    public void setButtonBackground(int drawable_id, Activity activity){

        int button_id[] = {R.id.btn11,
                           R.id.btn12,
                           R.id.btn13,
                           R.id.btn14,
                           R.id.btn21,
                           R.id.btn22,
                           R.id.btn23,
                           R.id.btn24,
                           R.id.btn31,
                           R.id.btn32,
                           R.id.btn33,
                           R.id.btn34,
                           R.id.btn41,
                           R.id.btn42,
                           R.id.btn43,
                           R.id.btn44,
                           R.id.btn00};

        Log.i("Array size", String.valueOf(button_id.length));

        for(int i = 0; i < button_id.length; i++){
            setBackground(button_id[i], drawable_id, activity);
        }
    }

    public void color(Activity activity, String color){
        final int api = android.os.Build.VERSION.SDK_INT;
        String appliedColor = "#" + color;

        activity.getWindow().getDecorView().setBackgroundColor(Color.parseColor(appliedColor));
        if (api >= 21) {
            activity.getWindow().setNavigationBarColor(Color.parseColor(appliedColor));
            activity.getWindow().setStatusBarColor(Color.parseColor(appliedColor));
        }
    }

    public int getHeight(int view_id, Activity activity){
        final View view = (View) activity.findViewById(view_id);
        int height = view.getMeasuredHeight();

        Log.i("ThemeService", "getHeight : " + String.valueOf(height));
        return height;
    }

    public void setVisible(final int view_id, final int delay, final Activity activity){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final View view = (View) activity.findViewById(view_id);

                view.setVisibility(View.VISIBLE);
            }
        }, delay);
    }

    public void setInvisible(final int view_id, final int delay, final Activity activity){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final View view = (View) activity.findViewById(view_id);

                view.setVisibility(View.INVISIBLE);
            }
        }, delay);
    }

    public void setGone(final int view_id, final int delay, final Activity activity){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final View view = (View) activity.findViewById(view_id);

                view.setVisibility(View.INVISIBLE);
            }
        }, delay);
    }

    public void setAlpha(final int view_id, final float alpha, final int delay, final Activity activity){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final View view = (View) activity.findViewById(view_id);

                view.setAlpha(alpha);
            }
        }, delay);
    }

    boolean toggled = false;

    public void setMargin(final int view_id, int delay, final int margin_left, final int margin_top, final int margin_right, final int margin_bottom, Activity activity){
        final View view = (View) activity.findViewById(view_id);

        Handler margin = new Handler();
        margin.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(toggled == false){
                    if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                        Log.i("ThemeService_setMargin", "Margin detected from view id (" + String.valueOf(view_id) + ") = left " + String.valueOf(layoutParams.leftMargin) + ", top " + String.valueOf(layoutParams.topMargin) + ", right " + String.valueOf(layoutParams.rightMargin) + ", bottom " + String.valueOf(layoutParams.bottomMargin));
                        layoutParams.setMargins(margin_left + layoutParams.leftMargin, margin_top + layoutParams.topMargin, margin_right + layoutParams.rightMargin, margin_bottom + layoutParams.bottomMargin);
                        Log.i("ThemeService_setMargin", "Margin applied to view id (" + String.valueOf(view_id) + ") = left " + String.valueOf(layoutParams.leftMargin) + ", top " + String.valueOf(layoutParams.topMargin) + ", right " + String.valueOf(layoutParams.rightMargin) + ", bottom " + String.valueOf(layoutParams.bottomMargin));
                        view.requestLayout();
                        toggled = true;
                    } else Log.i("ThemeService", "Layout margin not supported");
                } else {
                    if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                        Log.i("ThemeService_setMargin", "Margin detected from view id (" + String.valueOf(view_id) + ") = left " + String.valueOf(layoutParams.leftMargin) + ", top " + String.valueOf(layoutParams.topMargin) + ", right " + String.valueOf(layoutParams.rightMargin) + ", bottom " + String.valueOf(layoutParams.bottomMargin));
                        layoutParams.setMargins(layoutParams.leftMargin - margin_left, layoutParams.topMargin - margin_top, layoutParams.rightMargin - margin_right, layoutParams.bottomMargin - margin_bottom);
                        Log.i("ThemeService_setMargin", "Margin applied to view id (" + String.valueOf(view_id) + ") = left " + String.valueOf(layoutParams.leftMargin) + ", top " + String.valueOf(layoutParams.topMargin) + ", right " + String.valueOf(layoutParams.rightMargin) + ", bottom " + String.valueOf(layoutParams.bottomMargin));
                        view.requestLayout();
                        toggled = false;
                    } else Log.i("ThemeService", "Layout margin not supported");
                }
            }
        }, delay);
    }

    TutorialService tutorial = new TutorialService();

    public void setVideoview(int videoview_id, final int video_id, final boolean repeat, final int background_color_id, int delay, final Activity activity){

        final VideoView videoView;
        String uri = "android.resource://" + "com.bedrock.padder/";

        videoView = (VideoView)activity.findViewById(videoview_id);

        videoView.setVideoURI(Uri.parse(uri + video_id));
        Log.i("setVideoview", String.valueOf(video_id) + " loaded");

        videoView.seekTo(0);

        Handler videoDelay = new Handler();
        videoDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.setBackgroundColor(activity.getResources().getColor(background_color_id));
                videoView.start();
            }
        }, delay);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
                mp.setLooping(repeat);
            }
        });
    }
}
