package com.bedrock.padder.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bedrock.padder.R;
import com.bedrock.padder.helper.IntentHelper;
import com.bedrock.padder.helper.ToolbarHelper;
import com.bedrock.padder.helper.WindowHelper;

import static com.bedrock.padder.helper.WindowHelper.APPLICATION_ID;

public class UserBenefitsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip;
    private View reveal;

    private MaterialDialog PresetDialog;

    private Activity activity;
    private SharedPreferences prefs = null;

    private int position = 0;

    private ToolbarHelper toolbar = new ToolbarHelper();
    private WindowHelper window = new WindowHelper();
    private IntentHelper intent = new IntentHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_benefits);

        activity = this;
        prefs = getSharedPreferences(APPLICATION_ID, MODE_PRIVATE);
        toolbar.setStatusBarTint(this);

        initializeView();

        viewPager = (ViewPager) findViewById(R.id.user_benefits_view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.user_benefits_page_indicator);
        btnSkip = (Button) findViewById(R.id.user_benefits_get_started);

        // Layouts
        layouts = new int[]{
                R.layout.fragment_user_benefits_page1,
                R.layout.fragment_user_benefits_page2,
                R.layout.fragment_user_benefits_page3,
                R.layout.fragment_user_benefits_page4};

        // Set viewpager
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Setting windows default
        setBottomDots(0);

        reveal = findViewById(R.id.user_benefits_reveal_view);

        setIntentMain(btnSkip, reveal);

        prefs.edit().putInt("scheme", 0).apply();
        Log.d("SharedPrefs", "Preset : Hello");

        prefs.edit().putInt("color", R.color.cyan_400).apply();
    }

    private void initializeView() {
        // Set transparent nav bar
        window.setNavigationBar(R.color.transparent, activity);

        // Set window margins
        window.getView(R.id.user_benefits_view_pager, activity).setPadding(0, window.getStatusBarFromPrefs(activity), 0, 0);
        window.getView(R.id.user_benefits_layout, activity).setPadding(0, 0, 0, window.getStatusBarFromPrefs(activity));

        // Set taskDesc
        window.setRecentColor(R.string.app_name, 0, R.color.colorPrimary, activity);

        // Making status bar transparent
        window.setStatusBar(R.color.transparent, activity);
    }

    int x, y;

    private void setIntentMain(final View click_view, final View user_benefits_reveal_view) {
        if(Build.VERSION.SDK_INT >= 11) {
            final Animator[] revealAnim = {null};
            click_view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            Rect rect = new Rect();
                            click_view.getGlobalVisibleRect(rect);

                            x = (int) event.getRawX();
                            y = (int) event.getRawY();

                            Log.d("TouchListner", "X value : " + String.valueOf(x));
                            Log.d("TouchListner", "Y value : " + String.valueOf(y));

                            float finalRadius = (float) Math.hypot(x, y);

                            revealAnim[0] = ViewAnimationUtils.createCircularReveal(user_benefits_reveal_view, x, y, 0, finalRadius + 500);
                            revealAnim[0].setDuration(500);
                        } else {
                            intent.intentFlag(activity, "activity.MainActivity", 0);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            user_benefits_reveal_view.setVisibility(View.VISIBLE);
                            revealAnim[0].start();
                            showDialogPreset(400);
                        } else {
                            showDialogPreset(0);
                        }
                    }
                    return false;
                }
            });
        }

        prefs.edit().putBoolean("welcome", false).apply();
    }
    
    private void showDialogPreset(int delay) {
        Handler dialogDelay = new Handler();
        dialogDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                int color;

                switch (prefs.getInt("scheme", 0)) {
                    case 1:
                        color = R.color.hello;
                        break;
                    case 2:
                        color = R.color.roses;
                        break;
                    case 3:
                        color = R.color.faded;
                        break;
                    default:
                        color = R.color.hello;
                        break;
                }

                PresetDialog = new MaterialDialog.Builder(activity)
                        .title(R.string.dialog_preset_title)
                        .content(R.string.user_benefits_dialog_hint)
                        .items(R.array.presets)
                        .cancelable(false)
                        .autoDismiss(false)
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                setPreset(which);
                                switch (which) {
                                    case 0:
                                        PresetDialog.getBuilder()
                                                .widgetColorRes(R.color.hello)
                                                .positiveColorRes(R.color.hello);
                                        break;
                                    case 1:
                                        PresetDialog.getBuilder()
                                                .widgetColorRes(R.color.roses)
                                                .positiveColorRes(R.color.roses);
                                        break;
                                    case 2:
                                        PresetDialog.getBuilder()
                                                .widgetColorRes(R.color.faded)
                                                .positiveColorRes(R.color.faded);
                                        break;
                                    default:
                                        break;
                                }
                                return true;
                            }
                        })
                        .alwaysCallSingleChoiceCallback()
                        .widgetColorRes(color)
                        .positiveText(R.string.user_benefits_dialog_positive)
                        .positiveColorRes(R.color.colorAccent)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                PresetDialog.dismiss();
                                intent.intentFlag(activity, "activity.MainActivity", 0);
                                prefs.edit().putInt("quickstart", 0).apply();
                            }
                        })
                        .show();
            }
        }, delay);
    }

    void setPreset(int scheme) {
        prefs.edit().putInt("scheme", scheme).apply();
    }

    private void setBottomDots(int currentPage) {
        int dot[] = {R.id.dot_1, R.id.dot_2, R.id.dot_3, R.id.dot_4};

        for(int i = 0; i < dot.length; i++) {
            if(i == currentPage) {
                window.setViewBackgroundDrawable(dot[i], R.drawable.tutorial_dots_light, this);
            } else {
                window.setViewBackgroundDrawable(dot[i], R.drawable.tutorial_dots_dark, this);
            }
        }
    }

    // ViewPager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int pagePosition) {
            position = pagePosition;

            setBottomDots(pagePosition);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    // View pager adapter
    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(position == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
            }

            this.doubleBackToExitPressedOnce = true;

            Toast.makeText(this, R.string.confirm_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            // move to previous screen
            viewPager.setCurrentItem(position - 1);
        }
    }
}