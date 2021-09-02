package com.example.demo;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by zzf on 2017/10/27.
 */

public class CardTransformer implements ViewPager.PageTransformer {
    private static final float MAX_SCALE = 1.0f;
    private static final float MIN_SCALE = 0.85f;//0.85f

    @Override
    public void transformPage(View page, float position) {
        if (position <= 1) {
            //   1.2f + (1-1)*(1.2-1.0)
            float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);

            page.setScaleX(scaleFactor);  //缩放效果

            if (position > 0) {
                page.setTranslationX(-scaleFactor * 2);
            } else if (position < 0) {
                page.setTranslationX(scaleFactor * 2);
            }
            page.setScaleY(scaleFactor);
        } else {

            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }
    }
}
