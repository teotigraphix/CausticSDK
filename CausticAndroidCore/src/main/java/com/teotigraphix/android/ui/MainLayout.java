
package com.teotigraphix.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.teotigraphix.android.service.ITouchService;

public class MainLayout extends LinearLayout {

    ITouchService touchService;

    public final ITouchService getTouchService() {
        return touchService;
    }

    public final void setTouchService(ITouchService value) {
        touchService = value;
    }

    public MainLayout(Context context) {
        super(context);
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return touchService.onInterceptTouchEvent(this, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return touchService.onTouchEvent(this, event);
    }
}
