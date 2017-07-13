package com.bedrock.padder.activity;

import android.os.Bundle;

import com.bedrock.padder.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        setButtonCtaVisible(true);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        // start auto play
        autoplay(2500, 1);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_1_title)
                .description(R.string.intro_1_text)
                .image(R.drawable.intro_1_drawable)
                .background(R.color.intro_1_color)
                .backgroundDark(R.color.intro_1_color_dark)
                .layout(R.layout.fragment_simple_slide_large_image)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_2_title)
                .description(R.string.intro_2_text)
                .image(R.drawable.intro_2_drawable)
                .background(R.color.intro_2_color)
                .backgroundDark(R.color.intro_2_color_dark)
                .layout(R.layout.fragment_simple_slide_large_image)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_3_title)
                .description(R.string.intro_3_text)
                .image(R.drawable.intro_3_drawable)
                .background(R.color.intro_3_color)
                .backgroundDark(R.color.intro_3_color_dark)
                .layout(R.layout.fragment_simple_slide_large_image)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_4_title)
                .description(R.string.intro_4_text)
                .image(R.drawable.intro_4_drawable)
                .background(R.color.intro_4_color)
                .backgroundDark(R.color.intro_4_color_dark)
                .layout(R.layout.fragment_simple_slide_large_image)
                .build());
    }
}
