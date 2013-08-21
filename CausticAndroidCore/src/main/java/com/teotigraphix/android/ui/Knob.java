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

package com.teotigraphix.android.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.teotigraphix.caustic.android.R;

@SuppressWarnings("unused")
public class Knob extends Slider {

    //--------------------------------------------------------------------------
    //
    // Variables
    //
    //--------------------------------------------------------------------------

    private Paint mBackgroundPaint;

    private Paint mForegroundPaint;

    private Bitmap mBackground;

    private Bitmap mForeground;

    private float currentDeg = 0f;

    private PointF clickOffset;

    private boolean mSkinChanged;

    private float currentAngle = 0f;

    private float originalAngle = 0f;

    private float lastAngle = 0f;

    private float minimumAngle = 40f;

    //--------------------------------------------------------------------------
    //
    // Constructors
    //
    //--------------------------------------------------------------------------

    public Knob(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Knob(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Knob(Context context) {
        super(context);
        init();
    }

    private void init() {
        //setIsDebug(true);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mForegroundPaint = new Paint();
        mForegroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Public :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void setSelectedValue(float selectedValue) {
        super.setSelectedValue(selectedValue);
        mSkinChanged = true;
        invalidateProperties();
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(60, 60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawBitmap(mBackground, 0, 0, mBackgroundPaint);

        //canvas.save(Canvas.MATRIX_SAVE_FLAG);
        //{
        // canvas.rotate(currentDeg, 30, 30);
        // canvas.drawBitmap(mForeground, 0, 0, mForegroundPaint);
        // canvas.restore();
        //}
    }

    @Override
    protected void drawThumb(Canvas canvas) {

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        {
            getThumbDrawable().setBounds(0, 0, getWidth(), getHeight());
            canvas.rotate(currentDeg, 30, 30);
            // canvas.drawBitmap(mForeground, 0, 0, mForegroundPaint);
            getThumbDrawable().draw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected void touchDownHandler(int x, int y, int index) {
        originalAngle = lastAngle;
        clickOffset = new PointF(x, y);
    }

    @Override
    protected void touchMoveHandler(int x, int y, int index) {
        ///*
        float oldValue = getSelectedValue();
        PointF p = new PointF(x, y);
        float newValue = pointToValue(p.x - clickOffset.x, p.y - clickOffset.y);
        setValue(nearestValidValue(newValue, getSnapInterval()));

        //Log.d("Dial", x + ":" + y);

        if (oldValue == getSelectedValue())
            return;

        if (mDialListener != null)
            mDialListener.OnDialChange(getSelectedValue());

        mSkinChanged = true;
        invalidateProperties();
        //*/
    }

    @Override
    protected void touchUpHandler(int x, int y, int index) {
        lastAngle = currentDeg;
    }

    @Override
    protected void commitProperties() {
        super.commitProperties();

        if (mSkinChanged) {
            updateSkin();
            mSkinChanged = false;
        }
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void updateSkin() {
        ///*
        try {
            currentDeg = getAngleFromValue(getSelectedValue()) - 140;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //*/
        invalidate();
    }

    @Override
    protected float pointToValue(float x, float y) {
        float delta = x;
        float result;

        currentAngle = originalAngle + delta;

        if (currentAngle < 0) {
            if (Math.abs(currentAngle) >= 180 - minimumAngle)
                currentAngle = -(180 - minimumAngle);
        } else {
            if (currentAngle >= 180 - minimumAngle)
                currentAngle = 180 - minimumAngle;
        }

        // 280 degrees to min/mx
        float spanAngle = 360 - (minimumAngle * 2);
        float spanValue = (currentAngle + spanAngle - 140);

        result = getMinimum() + (spanValue / spanAngle) * (getMaximum() - getMinimum());

        return result;
    }

    protected float getAngleFromValue(float value) throws Exception {
        if (value < getMinimum() || value > getMaximum()) {
            throw new Exception("Invalid value found when attempting to retrieve angle.");
        }

        float valuePercentage = (value - getMinimum()) / (getMaximum() - getMinimum());
        float maxRotation = 360 - (minimumAngle * 2);
        float angleForValue = valuePercentage * maxRotation;
        return angleForValue;
    }

    //--------------------------------------------------------------------------
    //
    // Dial API :: Properties
    //
    //--------------------------------------------------------------------------

    private DialListener mDialListener;

    public interface DialListener {
        void OnDialChange(float value);
    }

    public void addDialListener(DialListener listener) {
        mDialListener = listener;
    }
}
