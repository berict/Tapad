package com.bedrock.padder.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bedrock.padder.R;
import com.bedrock.padder.helper.AnimService;
import com.bedrock.padder.helper.IntentService;
import com.bedrock.padder.helper.ThemeService;
import com.bedrock.padder.helper.WindowService;

public class WelcomeActivity extends Activity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private View reveal;

    Activity activity;

    SharedPreferences prefs = null;
    
    int position = 0;

    ThemeService theme = new ThemeService();
    AnimService anim = new AnimService();
    WindowService window = new WindowService();
    IntentService intent = new IntentService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        activity = this;
        prefs = getSharedPreferences("com.bedrock.padder", MODE_PRIVATE);

        //Set transparent nav bar
        window.setNavigationBar(R.color.transparent, activity);

        window.setMarginRelativePX(R.id.welcome_indicator, 0, 0, 0, prefs.getInt("navBarPX", 0), activity);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        /** Layouts */
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        // Setting windows default
        setBottomDots(0);
        setWindow(0);
        window.setNavigationBar(R.color.transparent, activity);

        // making status bar transparent
        window.setStatusBar(R.color.transparent, activity);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        reveal = (View)findViewById(R.id.reveal_view);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                }
            }
        });

        setIntentMain(btnSkip, reveal);

        anim.scaleInOverShoot(R.id.welcome_logo, 200, 500, "welcomeIn", this);

        prefs.edit().putInt("scheme", 1).apply();
        Log.d("SharedPrefs", "Scheme : Hello");

        prefs.edit().putInt("color", R.color.red).apply();
    }

    @Override
    public void onPause(){
        super.onPause();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    int x, y;

    private void setIntentMain(final View click_view, final View reveal_view){
        if(Build.VERSION.SDK_INT >= 11) {
            final Animator[] revealAnim = {null};
            click_view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        Rect rect = new Rect();
                        click_view.getGlobalVisibleRect(rect);

                        x = (int) event.getRawX();
                        y = (int) event.getRawY();

                        Log.d("TouchListner", "X value : " + String.valueOf(x));
                        Log.d("TouchListner", "Y value : " + String.valueOf(y));

                        float finalRadius = (float) Math.hypot(x, y);

                        revealAnim[0] = ViewAnimationUtils.createCircularReveal(reveal_view, x, y, 0, finalRadius + 500);
                        revealAnim[0].setDuration(500);
                    } else {
                        intent.intentFlag(activity, "activity.MainActivity", 0);
                    }
                    return false;
                }
            });

            click_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        reveal_view.setVisibility(View.VISIBLE);
                        revealAnim[0].start();
                        intent.intentFlag(activity, "activity.MainActivity", 400);
                    } else {
                        intent.intentFlag(activity, "activity.MainActivity", 0);
                    }
                }
            });
        }

        prefs.edit().putBoolean("welcome", false).apply();
    }

    private void setWindow(int position){
        int[] colors = new int[]{
                R.color.bg_screen1,
                R.color.bg_screen2,
                R.color.bg_screen3,
                R.color.bg_screen4 };

        window.setRecentColor(R.string.task_welcome, 0, colors[position], activity);
    }

    private void setBottomDots(int currentPage){
        int dot[] = {R.id.dot_1, R.id.dot_2, R.id.dot_3, R.id.dot_4,};

        for(int i = 0; i < dot.length; i++){
            if(i == currentPage){
                theme.setBackground(dot[i], R.drawable.tutorial_dots_light, this);
            } else {
                theme.setBackground(dot[i], R.drawable.tutorial_dots_dark, this);
            }
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    // ViewPager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int page_position) {
            position = page_position;

            setBottomDots(page_position);
            setWindow(page_position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (page_position == layouts.length - 1) {
                // last page. make button text to GOT IT
                try {
                    btnNext.setText(getString(R.string.hello)); //TODO edit
                    btnSkip.setVisibility(View.GONE);

                    Log.d("ViewPager", "Page is finishing page");

                    setIntentMain(btnNext, reveal);
                } catch (NullPointerException e){
                    Log.d("Error", e.getMessage());
                }
            } else {
                // still pages are left
                try {
                    btnNext.setText(getString(R.string.hello)); //TODO edit
                    btnSkip.setVisibility(View.VISIBLE);

                    Log.d("ViewPager", "Page is not finishing page");

                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(position + 1);
                        }
                    });
                } catch (NullPointerException e){
                    Log.d("Error", e.getMessage());
                }
            }

            if(page_position == 0){
                try {
                    theme.setVisible(R.id.welcome_logo, 200, activity);
                } catch (NullPointerException e){
                    Log.d("Error", e.getMessage());
                }
            }

            if(page_position == 2){
                try {
                    RadioButton hello = (RadioButton) findViewById(R.id.hello_radio);
                    RadioButton roses = (RadioButton) findViewById(R.id.roses_radio);
                    RadioButton faded = (RadioButton) findViewById(R.id.faded_radio);

                    switch (prefs.getInt("scheme", 0)){
                        case 1: hello.setChecked(true); break;
                        case 2: roses.setChecked(true); break;
                        case 3: faded.setChecked(true); break;
                        default: hello.setChecked(true); break;
                    }

                    hello.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prefs.edit().putInt("scheme", 1).apply();
                            Log.d("SharedPrefs", "Scheme : Hello");
                        }
                    });

                    roses.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prefs.edit().putInt("scheme", 2).apply();
                            Log.d("SharedPrefs", "Scheme : Roses");
                        }
                    });

                    faded.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prefs.edit().putInt("scheme", 3).apply();
                            Log.d("SharedPrefs", "Scheme : Roses");
                        }
                    });
                }  catch (NullPointerException e){
                    Log.d("Error", e.getMessage());
                }
            }

            if(page_position == 3){
                try {
                    window.setMarginRelativeDP(R.id.welcome_android, 0, 0, 5, 48 + (window.convertPXtoDP(prefs.getInt("navBarPX", 0), activity)), activity);
                }  catch (NullPointerException e){
                    Log.d("Error", e.getMessage());
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    // View pager adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
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