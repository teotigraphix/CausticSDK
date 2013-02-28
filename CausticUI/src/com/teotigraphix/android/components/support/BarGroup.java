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
import android.util.AttributeSet;
import android.view.View;

public abstract class BarGroup extends UIViewGroup {

    int chosenHeight = -1;

    //--------------------------------------------------------------------------
    // 
    //  Public API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  numItems
    //----------------------------------

    private int mNumItems;

    private boolean mNumItemsChanged;

    public int getNumItems() {
        return mNumItems;
    }

    public void setNumItems(int value) {
        mNumItems = value;
        mNumItemsChanged = true;
        invalidateProperties();
    }

    //----------------------------------
    //  selectedIndex
    //----------------------------------

    private int mSelectedIndex;

    private boolean mSelectedIndexChanged;

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int value) {
        mSelectedIndex = value;
        mSelectedIndexChanged = true;
        invalidateProperties();
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructors
    // 
    //--------------------------------------------------------------------------

    public BarGroup(Context context) {
        super(context);
    }

    public BarGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //--------------------------------------------------------------------------
    // 
    //  Overridden Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        for (int i = 0; i < getNumItems(); i++) {
            View child = createItemRenderer(i);
            addView(child);
        }
    }

    abstract protected View createItemRenderer(int index);

    @Override
    protected void commitProperties() {
        super.commitProperties();

        if (mNumItemsChanged) {
            commitNumItemsChanged();
            mNumItemsChanged = false;
        }

        if (mSelectedIndexChanged) {
            commitSelectedIndex();
            mSelectedIndexChanged = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
        //Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measuredChosenHeight = chosenHeight;
        if (chosenHeight == -1)
            //measuredChosenHeight = widthSize / mNumItems; // square
            measuredChosenHeight = heightSize;

        setMeasuredDimension(widthSize, measuredChosenHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int right = getPaddingRight();
        int left = getPaddingLeft();
        int bottom = getPaddingBottom();
        int top = getPaddingTop();

        int calcX = left;
        int calcWidth = (getWidth() - right - left) / mNumItems;
        int calcHeight = getHeight() - top - bottom;

        for (int i = 0; i < mNumItems; i++) {
            View view = getChildAt(i);
            view.layout(calcX, top, calcX + calcWidth, calcHeight);
            calcX += calcWidth;
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    protected void commitNumItemsChanged() {
    }

    protected void commitSelectedIndex() {
    }

    //--------------------------------------------------------------------------
    //  SelectionListener
    //--------------------------------------------------------------------------

    protected OnSelectionListener mSelectListener;

    public void setOnSelectionListener(OnSelectionListener listener) {
        mSelectListener = listener;
    }

    public interface OnSelectionListener {
        void onSelection(View view, boolean selected);

        void onLongClick(View view);
    }
}
