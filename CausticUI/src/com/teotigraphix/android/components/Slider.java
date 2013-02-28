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

package com.teotigraphix.android.components;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.teotigraphix.android.components.support.SliderBaseView;
import com.teotigraphix.caustic.ui.R;

/**
 * @see custom:thumb
 * @see custom:track
 * @see custom:track_background
 * @see custom:text_color
 */
public class Slider extends SliderBaseView {

    @SuppressWarnings("unused")
    private static final String TAG = "Slider";

    //--------------------------------------------------------------------------
    //
    //  Config :: Variables
    //
    //--------------------------------------------------------------------------

    // whether the minimum is located on the top(true) or bottom(false)
    private boolean mMinimumTop;

    // XXX Figure out how to get a float into textSize from an int
    private int textSize = 9;

    //private float floatTextSize = 8.0f;

    private int gap = 5;

    //--------------------------------------------------------------------------
    //
    //  Private :: Variables
    //
    //--------------------------------------------------------------------------

    private boolean mSkinChanged;

    private Paint mTextPaint;

    //--------------------------------------------------------------------------
    //
    //  Public API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    //  text
    //----------------------------------

    private String mText;

    public String getText() {
        return mText;
    }

    public void setText(String value) {
        mText = value;
    }

    private boolean hasLabel() {
        return mText != null;
    }

    //----------------------------------
    //  textColor
    //----------------------------------

    private int mTextColor = 0xffffffff;

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int value) {
        mTextColor = value;
    }

    //----------------------------------
    //  thumb
    //----------------------------------

    private Drawable mThumbDrawable;

    public Drawable getThumbDrawable() {
        return mThumbDrawable;
    }

    public void setThumbDrawable(Drawable value) {
        mThumbDrawable = value;
        invalidate();
    }

    //----------------------------------
    //  track
    //----------------------------------

    private Drawable mTrackDrawable;

    public Drawable getTrackDrawable() {
        return mTrackDrawable;
    }

    public void setTrackDrawable(Drawable value) {
        mTrackDrawable = value;
        invalidate();
    }

    //----------------------------------
    //  trackBackground
    //----------------------------------

    private Drawable mTrackBackgroundDrawable;

    public Drawable getTrackBackgroundDrawable() {
        return mTrackBackgroundDrawable;
    }

    public void setTrackBackgroundDrawable(Drawable value) {
        mTrackBackgroundDrawable = value;
        invalidate();
    }

    //--------------------------------------------------------------------------
    //
    //  Constructors
    //
    //--------------------------------------------------------------------------

    public Slider(Context context) {
        super(context);
    }

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Slider);

        final int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.Slider_text:
                    setText(a.getString(attr));
                    break;
                case R.styleable.Slider_thumb:
                    setThumbDrawable(a.getDrawable(attr));
                    break;
                case R.styleable.Slider_track:
                    setTrackDrawable(a.getDrawable(attr));
                    break;
                case R.styleable.Slider_track_background:
                    setTrackBackgroundDrawable(a.getDrawable(attr));
                    break;
                case R.styleable.Slider_text_color:
                    setTextColor(a.getColor(attr, getTextColor()));
                    break;
            }
        }
        a.recycle();
    }

    public Slider(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //--------------------------------------------------------------------------
    //
    //  Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void preinitialize() {
        super.preinitialize();

        setIsDebug(false);

        setPreferredWidth(50);
        setPreferredHeight(200);

        setThumbThickness(30);

        setDirection(SliderDirection.VERTICAL);
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        @SuppressWarnings("unused")
        Resources resources = getContext().getResources();
        //        mTrackDrawable = (NinePatchDrawable)resources.getDrawable(R.drawable.slider_track);
        //        mTrackBackgroundDrawable = (NinePatchDrawable)resources
        //                .getDrawable(R.drawable.slider_track_background);
        //        mThumbDrawable = (NinePatchDrawable)resources.getDrawable(R.drawable.slider_thumb);

        // Setup text Paint	
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);

        //	Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
        // "fonts/DIGITALDREAMFAT.ttf");

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(9f);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void commitProperties() {
        super.commitProperties();

        //Log.d(TAG, "commitProperties()");
        if (mSkinChanged) {
            updateSkin();
            mSkinChanged = false;
        }
    }

    protected void updateSkin() {
    }

    @Override
    protected void touchDownHandler(int x, int y, int index) {
        // getGlobalVisibleRect()
        commitValue(x, y);
    }

    @Override
    protected void touchMoveHandler(int x, int y, int index) {
        commitValue(x, y);
    }

    @Override
    protected void touchUpHandler(int x, int y, int index) {
        //Log.d(TAG, "up");
    }

    private void commitValue(int x, int y) {
        int bottom = getPaddingBottom();
        int textHeight = 0;

        if (hasLabel()) {
            textHeight += gap + textSize + bottom;
        }
        int trackHeight = getHeight() - textHeight;
        int calcY = y;
        if (!mMinimumTop) {
            calcY = trackHeight - y;
        }

        float oldValue = getSelectedValue();
        PointF p = new PointF(x, calcY);

        float newValue = pointToValue(p.x, p.y);
        setValue(nearestValidValue(newValue, getSnapInterval()));

        if (oldValue == getSelectedValue())
            return;

        mSkinChanged = true;
        invalidateProperties();

        dispatchChange();
    }

    @SuppressWarnings("unused")
    private void _commitValue(int x, int y) {
        float oldValue = getSelectedValue();
        PointF p = new PointF(x, y);

        float newValue = pointToValue(p.x, p.y);
        setValue(nearestValidValue(newValue, getSnapInterval()));

        if (oldValue == getSelectedValue())
            return;

        mSkinChanged = true;
        invalidateProperties();

        dispatchChange();
    }

    //--------------------------------------------------------------------------
    //
    //  Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int calcWidth = getPaddingLeft() + getPaddingRight();
        int calcHeight = getPaddingTop() + getPaddingBottom();

        int wMode = MeasureSpec.getMode(wSpec);
        int wSize = MeasureSpec.getSize(wSpec);

        int hMode = MeasureSpec.getMode(hSpec);
        int hSize = MeasureSpec.getSize(hSpec);

        int mWidth = chooseWidthDimension(wMode, wSize); //+ calcWidth;
        int mHeight = chooseHeightDimension(hMode, hSize); //+ calcHeight;

        setMeasuredDimension(mWidth, mHeight);
    }

    private int chooseWidthDimension(int mode, int size) {
        // AT_MOST == parent size
        // EXACTLY == set on the XML mark up
        // UNSPECIFIED == preferred component size

        // not using AT_MOST since we don' want the sliders to stretch
        if (size != 0 && mode == MeasureSpec.EXACTLY) {
            return size;
        } else { // (mode == MeasureSpec.UNSPECIFIED)
            return getPreferredWidth();
        }
    }

    private int chooseHeightDimension(int mode, int size) {
        // AT_MOST == parent size
        // EXACTLY == set on the XML mark up
        // UNSPECIFIED == preferred component size
        //if (hasLabel()) {
        //	if (getDirection() == SliderDirection.VERTICAL) {
        //		mHeight += gap + textSize;
        //	} else {
        //
        //	}
        //}

        if (mode == MeasureSpec.AT_MOST) {
            return getPreferredHeight();
        }

        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else { // (mode == MeasureSpec.UNSPECIFIED)
            return getPreferredHeight();
        }
    }

    @Override
    protected int getThumbRange() {
        int textThickness = 0;

        if (hasLabel()) {
            textThickness = textSize + gap;
        }

        if (getDirection() == SliderDirection.VERTICAL)
            return getHeight() - getThumbThickness() - textThickness;
        else
            return getWidth() - getThumbThickness() - textThickness;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d(TAG, "onDraw()");
        drawThumb(canvas);
    }

    @Override
    protected void drawBackground(Canvas canvas) {
        drawTrack(canvas);

        if (hasLabel()) {
            drawLabel(canvas);
        }
    }

    protected void drawTrack(Canvas canvas) {
        int bottom = getPaddingBottom();

        int trackWidth = 5;
        int width = getWidth();
        int trackX = (width - trackWidth) / 2;

        int textHeight = 0;

        if (hasLabel()) {
            textHeight += gap + textSize + bottom;
        }

        int trackHeight = getHeight() - textHeight;

        if (mTrackBackgroundDrawable != null) {
            mTrackBackgroundDrawable.setBounds(0, 0, width, trackHeight);
            mTrackBackgroundDrawable.draw(canvas);
        }

        if (mTrackDrawable != null) {
            mTrackDrawable.setBounds(trackX, 0, width - trackX, trackHeight);
            mTrackDrawable.draw(canvas);
        }
    }

    protected void drawThumb(Canvas canvas) {
        int left = getPaddingLeft();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        int thumbHeight = getThumbThickness();
        int width = getWidth();

        int textHeight = 0;

        if (hasLabel()) {
            textHeight += gap + textSize + bottom;
        }
        // 181
        int trackHeight = getHeight() - textHeight;
        // 161
        int thumbRange = trackHeight - thumbHeight;
        float range = getMaximum() - getMinimum();// 1.0
        // calculate new thumb position. 16
        float calc1 = getSelectedValue() - getMinimum();
        int thumbPosTrackY = (int)((range > 0) ? (calc1 / range) * thumbRange : 0);

        //Log.d(TAG, "drawThumb() " + thumbPosTrackY + ", " + thumbHeight);

        int ctop = trackHeight - thumbPosTrackY - thumbHeight;
        if (mMinimumTop) {
            ctop = thumbPosTrackY;
        }
        int cleft = left;
        int cright = width - right;
        int cbottom = ctop + getThumbThickness();

        // left:5, 16, 50 - 5, 16 + 20
        if (mThumbDrawable != null) {
            mThumbDrawable.setBounds(cleft, ctop, cright, cbottom);
            mThumbDrawable.draw(canvas);
        }
    }

    protected void _drawThumb(Canvas canvas) {
        int left = getPaddingLeft();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        int thumbHeight = getThumbThickness();
        int width = getWidth();

        int textHeight = 0;

        if (hasLabel()) {
            textHeight += gap + textSize + bottom;
        }

        int trackHeight = getHeight() - textHeight;

        int thumbRange = trackHeight - thumbHeight;
        float range = getMaximum() - getMinimum();
        // calculate new thumb position.
        int thumbPosTrackY = (int)((range > 0) ? ((getSelectedValue() - getMinimum()) / range)
                * thumbRange : 0);
        //Log.d(TAG, "drawThumb() " + thumbPosTrackY + ", " + thumbHeight);

        if (mThumbDrawable != null) {
            mThumbDrawable.setBounds(left, thumbPosTrackY, width - right, thumbPosTrackY
                    + getThumbThickness());
            mThumbDrawable.draw(canvas);
        }
    }

    protected void drawLabel(Canvas canvas) {
        int bottom = getPaddingBottom();
        mTextPaint.setColor(mTextColor);
        canvas.drawText(getText(), getWidth() / 2, getHeight() - bottom, mTextPaint);
    }
}
