package com.appmatic.baseapp.gallery.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by grender on 15/04/17.
 */

public class ExceptionFreeViewPager extends ViewPager {
    public ExceptionFreeViewPager(Context context) {
        super(context);
    }

    public ExceptionFreeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // See https://github.com/chrisbanes/PhotoView#issues-with-viewgroups
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
