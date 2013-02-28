
package com.teotigraphix.android.components.support;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.teotigraphix.caustic.ui.R;

public class RangeView extends UIComponent {

    @SuppressWarnings("unused")
    private static final String TAG = "RangeView";

    //--------------------------------------------------------------------------
    //
    //  Public API :: Properties
    //
    //--------------------------------------------------------------------------

    //---------------------------------
    //  maximum
    //---------------------------------

    private float mMaximum = 100f;

    public float getMaximum() {
        return mMaximum;
    }

    public void setMaximum(float value) {
        mMaximum = value;
        //Log.d(TAG, "setMaximum() " + mMaximum);
        propertiesChanged = true;
        invalidateProperties();
    }

    //---------------------------------
    //  minimum
    //---------------------------------

    private float mMinimum = 0f;

    public float getMinimum() {
        return mMinimum;
    }

    public void setMinimum(float value) {
        mMinimum = value;
        //Log.d(TAG, "setMinimum() " + mMinimum);
        propertiesChanged = true;
        invalidateProperties();
    }

    //---------------------------------
    //  stepSize
    //---------------------------------

    private float mStepSize = 0f;

    public float getStepSize() {
        return mStepSize;
    }

    public void setStepSize(float value) {
        mStepSize = value;
        propertiesChanged = true;
        invalidateProperties();
    }

    //---------------------------------
    //  stepSize
    //---------------------------------

    private float mSelectedValue = -1f;

    private float mChangedSelectedValue = 0f;

    private boolean valueChanged = false;

    public float getSelectedValue() {
        return (valueChanged) ? mChangedSelectedValue : mSelectedValue;
    }

    public void setSelectedValue(float value) {
        if (mSelectedValue == value)
            return;

        mChangedSelectedValue = value;
        //Log.d("RangeView", "setSelectedValue() " + mChangedSelectedValue);
        propertiesChanged = true;
        valueChanged = true;
        invalidateProperties();
    }

    //---------------------------------
    //  snapInterval
    //---------------------------------

    private float mSnapInterval = 1f;

    public float getSnapInterval() {
        return mSnapInterval;
    }

    public void setSnapInterval(float value) {
        mSnapInterval = value;
        //Log.d("RangeView", "setSnapInterval() " + mSnapInterval);
        propertiesChanged = true;
        invalidateProperties();
    }

    //--------------------------------------------------------------------------
    //
    //  Constructors
    //
    //--------------------------------------------------------------------------

    public RangeView(Context context) {
        super(context);
    }

    public RangeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeView);

        final int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {

                case R.styleable.RangeView_maximum:
                    setMaximum(a.getFloat(attr, getMaximum()));
                    break;
                case R.styleable.RangeView_minimum:
                    setMinimum(a.getFloat(attr, getMinimum()));
                    break;
                case R.styleable.RangeView_snap_interval:
                    setSnapInterval(a.getFloat(attr, getSnapInterval()));
                    break;
                case R.styleable.RangeView_selected_value:
                    setSelectedValue(a.getFloat(attr, getSelectedValue()));
                    break;

            }
        }
        a.recycle();
    }

    public RangeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //--------------------------------------------------------------------------
    //
    //  Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void commitProperties() {
        super.commitProperties();
        if (propertiesChanged) {
            float currentValue = (valueChanged) ? mChangedSelectedValue : mSelectedValue;
            setValue(nearestValidValue(currentValue, mSnapInterval));

            propertiesChanged = false;
            valueChanged = false;
        }
    }

    public void changeValueByStep(Boolean increase) {
        if (getStepSize() == 0)
            return;

        float newValue = (increase) ? getSelectedValue() + getStepSize() : getSelectedValue()
                - getStepSize();
        setValue(nearestValidValue(newValue, getSnapInterval()));
    }

    protected void setValue(float value) {
        if (mSelectedValue == value)
            return;

        if (!Float.isNaN(mMaximum) && !Float.isNaN(mMinimum) && (mMaximum > mMinimum))
            mSelectedValue = Math.min(mMaximum, Math.max(mMinimum, value));
        else
            mSelectedValue = value;

        dispatchValueComment();
    }

    protected float nearestValidValue(float value, float interval) {
        if (interval == 0)
            return Math.max(mMinimum, Math.min(mMaximum, value));

        float maxValue = mMaximum - mMinimum;
        float scale = 1;

        value -= mMinimum;

        // If interval isn't an integer, there's a possibility that the floating point 
        // approximation of value or value/interval will be slightly larger or smaller 
        // than the real value.  This can lead to errors in calculations like 
        // floor(value/interval)*interval, which one might expect to just equal value, 
        // when value is an exact multiple of interval.  Not so if value=0.58 and 
        // interval=0.01, in that case the calculation yields 0.57!  To avoid problems, 
        // we scale by the implicit precision of the interval and then round.  For 
        // example if interval=0.01, then we scale by 100.    

        if (interval != Math.round(interval)) {
            String v = (new String(1 + Float.toString(interval)));

            String parts[] = v.split("\\.");
            scale = (float)Math.pow(10, parts[1].length());
            maxValue *= scale;
            value = Math.round(value * scale);
            interval = Math.round(interval * scale);
        }

        float lower = (float)Math.max(0, Math.floor(value / interval) * interval);
        float upper = (float)Math.min(maxValue, Math.floor((value + interval) / interval)
                * interval);
        float validValue = ((value - lower) >= ((upper - lower) / 2)) ? upper : lower;

        return (validValue / scale) + mMinimum;
    }

    //--------------------------------------------------------------------------
    //
    //  Listener API :: Methods
    //
    //--------------------------------------------------------------------------

    private OnValueCommitListener mValueCommitListener;

    public void setOnValueCommitListener(OnValueCommitListener listener) {
        mValueCommitListener = listener;
    }

    private OnChangeListener mChangeListener;

    public void setOnChangeListener(OnChangeListener listener) {
        mChangeListener = listener;
    }

    protected void dispatchValueComment() {
        if (mValueCommitListener != null) {
            mValueCommitListener.onValueCommit(this);
        }
    }

    protected void dispatchChange() {
        if (mChangeListener != null) {
            mChangeListener.onChange(this);
        }
    }

    /**
     * ValueCommit is dispatched when the setValue() changes the selected value.
     */
    public interface OnValueCommitListener {
        void onValueCommit(RangeView view);
    }

    /**
     * Change is dispatched only when a user gesture changes the selected value.
     * ValueCommit will be dispatched right before the change event.
     */
    public interface OnChangeListener {
        void onChange(RangeView view);
    }
}
