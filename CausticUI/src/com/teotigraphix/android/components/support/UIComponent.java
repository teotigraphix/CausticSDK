////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.android.components.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.teotigraphix.android.components.ITouchListener;

public class UIComponent extends View implements ITouchListener {

    @SuppressWarnings("unused")
    private static final String TAG = "UIComponent";

    protected boolean propertiesChanged = false;

    protected boolean commitPropertiesFlag = false;

    protected boolean mLongClickPerformed;

    //--------------------------------------------------------------------------
    // 
    //  Drawing :: Properties
    // 
    //--------------------------------------------------------------------------

    private Rect mDebugRect;

    private Paint mDebugRectPaint;

    private Bitmap mBackground;

    private Paint mBackgroundPaint;

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Properties
    // 
    //--------------------------------------------------------------------------

    private boolean mShowDebugRect = false;

    protected void setIsDebug(boolean value) {
        mShowDebugRect = value;
    }

    //----------------------------------
    //  preferredWidth
    //----------------------------------

    private int mPreferredWidth = 0;

    protected int getPreferredWidth() {
        return mPreferredWidth;
    }

    protected void setPreferredWidth(int value) {
        mPreferredWidth = value;
    }

    //----------------------------------
    //  preferredHeight
    //----------------------------------

    private int mPreferredHeight = 0;

    protected int getPreferredHeight() {
        return mPreferredHeight;
    }

    protected void setPreferredHeight(int value) {
        mPreferredHeight = value;
    }

    //--------------------------------------------------------------------------
    // 
    //  ITouchListener API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  fingerId
    //----------------------------------

    //    private int mFingerId;
    //
    //    @Override
    //    public int getFingerId() {
    //        return mFingerId;
    //    }
    //
    //    @Override
    //    public void setFingerId(int index) {
    //        mFingerId = index;
    //    }

    //----------------------------------
    //  multiTouch
    //----------------------------------

    private boolean mMultiTouch = false;

    @Override
    public void setMultiTouch(boolean value) {
        mMultiTouch = value;
    }

    @Override
    public boolean isMultiTouch() {
        return mMultiTouch;
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructors
    // 
    //--------------------------------------------------------------------------

    public UIComponent(Context context) {
        super(context);
        preinitialize();
        initialize();
    }

    public UIComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        preinitialize();
        initialize(attrs);
    }

    public UIComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        preinitialize();
        initialize(attrs, defStyle);
    }

    //--------------------------------------------------------------------------
    // 
    //  ITouchListener API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mMultiTouch) {
            //onTouch(event);
            //return true;
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performLongClick() {
        mLongClickPerformed = true;
        return super.performLongClick();

    }

    @Override
    public boolean onTouch(MotionEvent event) {

        int index = event.getActionIndex();
        //if (event.getPointerId(index) != getFingerId())
        //	return false;

        // these ARE local coordinates to THIS component as converted
        // by the TouchService globalToLocal method
        int calcX = (int)event.getX(index);
        int calcY = (int)event.getY(index);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLongClickPerformed = false;
                touchDownHandler(calcX, calcY, index);
                return true;

            case MotionEvent.ACTION_MOVE:
                touchMoveHandler(calcX, calcY, index);
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                touchUpHandler(calcX, calcY, index);
                return true;
            default:
                break;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    // 
    //  Overridden Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (commitPropertiesFlag) {
            commitProperties();
        }

        drawDebugRect(canvas);

        redrawBackground(canvas);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // regenerateBackground();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        regenerateBackground();
    }

    /**
     * Draw all static background bitmaps and graphics here.
     * <p>
     * The background will be updated during the onSizeChanged() call.
     * 
     * @param canvas The static background canvas that is drawn onto the
     *            components canvas during onDraw().
     */
    protected void drawBackground(Canvas canvas) {

    }

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    /**
     * Setup model and data specific properties and state.
     */
    protected void preinitialize() {
    }

    /**
     * Creates the children and is called after {@link #preinitialize()}.
     */
    protected void initialize() {
        createChildren();
    }

    protected void initialize(AttributeSet attrs) {
        createChildren();
    }

    protected void initialize(AttributeSet attrs, int defStyle) {
        createChildren();
    }

    /**
     * Create the components children and bitmap resources.
     */
    protected void createChildren() {
        // background
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFilterBitmap(true);
        // debugRect
        mDebugRectPaint = new Paint();
        mDebugRectPaint.setColor(0xFFFF0000);
        mDebugRectPaint.setStrokeWidth(1f);
        mDebugRectPaint.setStyle(Style.STROKE);
    }

    protected void invalidateProperties() {
        commitPropertiesFlag = true;
        invalidate();
    }

    protected void commitProperties() {
    }

    //--------------------------------------------------------------------------
    // 
    //  Protected Touch :: Handlers
    // 
    //--------------------------------------------------------------------------

    protected void touchDownHandler(int x, int y, int index) {

    }

    protected void touchMoveHandler(int x, int y, int index) {

    }

    protected void touchUpHandler(int x, int y, int index) {

    }

    private void drawDebugRect(Canvas canvas) {
        if (mShowDebugRect) {
            mDebugRect = new Rect(0, 0, getWidth() - 1, getHeight() - 1);

            canvas.drawRect(mDebugRect, mDebugRectPaint);
        }
    }

    private void redrawBackground(Canvas canvas) {
        if (mBackground == null) {
            //Log.w(TAG, "Background not created");
        } else {
            canvas.drawBitmap(mBackground, 0, 0, mBackgroundPaint);
        }
    }

    private void regenerateBackground() {
        // free the old bitmap
        if (mBackground != null) {
            mBackground.recycle();
        }

        if (getWidth() <= 0 || getHeight() <= 0)
            return;

        mBackground = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBackground);

        drawBackground(canvas);
    }

}
