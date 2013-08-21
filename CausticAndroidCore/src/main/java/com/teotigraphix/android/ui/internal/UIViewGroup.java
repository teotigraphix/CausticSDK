
package com.teotigraphix.android.ui.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.teotigraphix.android.service.ITouchListener;

public class UIViewGroup extends ViewGroup implements ITouchListener {
    @SuppressWarnings("unused")
    private static final String TAG = "UIViewGroup";

    protected boolean commitPropertiesFlag = false;

    //--------------------------------------------------------------------------
    // 
    //  Constructors
    // 
    //--------------------------------------------------------------------------

    public UIViewGroup(Context context) {
        super(context);
        initialize();
    }

    public UIViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UIViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    //--------------------------------------------------------------------------
    // 
    //  Overridden Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (commitPropertiesFlag) {
            commitProperties();
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    protected void initialize() {
        preinitialize();
        createChildren();
    }

    protected void preinitialize() {
    }

    protected void createChildren() {
    }

    protected void invalidateProperties() {
        commitPropertiesFlag = true;
        invalidate();
    }

    protected void commitProperties() {
    }

    @Override
    public int getFingerId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFingerId(int index) {
        // TODO Auto-generated method stub

    }

    private boolean mMultiTouch = false;

    @Override
    public void setMultiTouch(boolean value) {
        mMultiTouch = value;
    }

    @Override
    public boolean isMultiTouch() {
        return mMultiTouch;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        //Log.d(TAG, event.toString());
        return false;
    }
}
