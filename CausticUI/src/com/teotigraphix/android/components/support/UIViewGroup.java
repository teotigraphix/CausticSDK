////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.teotigraphix.android.components.ITouchListener;
import com.teotigraphix.android.service.ITouchService;

/**
 * The {@link UIViewGroup} is the root class for groups of views that there
 * touch handling is delegated to the {@link ITouchService}.
 * 
 * @author Michael Schmalle
 */
public class UIViewGroup extends ViewGroup implements ITouchListener {

    @SuppressWarnings("unused")
    private static final String TAG = "UIViewGroup";

    protected boolean commitPropertiesFlag = false;

    //--------------------------------------------------------------------------
    // ITouchListener API
    //--------------------------------------------------------------------------

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
    // Constructors
    // 
    //--------------------------------------------------------------------------

    public UIViewGroup(Context context) {
        super(context);
        initialize();
    }

    public UIViewGroup(Context context, AttributeSet set) {
        super(context, set);
        initializeSet(set);
        initialize();
    }

    public UIViewGroup(Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);
        initializeSet(set);
        initialize();
    }

    //--------------------------------------------------------------------------
    // 
    //  Overridden Public :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
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
            commitPropertiesFlag = false;
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    /**
     * Initializes the view's attributes before children are created.
     * 
     * @param set The {@link AttributeSet}.
     */
    protected void initializeSet(AttributeSet set) {
    }

    /**
     * Initializes the view's setup property values and creates it's children.
     * 
     * @see #preinitialize()
     * @see UIViewGroup#createChildren()
     */
    protected void initialize() {
        preinitialize();
        createChildren();
    }

    /**
     * Called during the constructor just before {@link #createChildren()}.
     * <p>
     * Note; Set initial field values for use in {@link #createChildren()} in
     * this method, don't initialize them in the class body.
     */
    protected void preinitialize() {
    }

    /**
     * Creates the {@link ViewGroup}s child {@link View}s.
     */
    protected void createChildren() {
    }

    protected void invalidateProperties() {
        commitPropertiesFlag = true;
        invalidate();
    }

    protected void commitProperties() {
    }

    /**
     * Multi-touch, returns a view at the specified coords using hit testing.
     * 
     * @param x The parent local x coordinate.
     * @param y The parent local y coordinate.
     * @return a {@link View} if hit tested child is under the point.
     */
    protected final View getViewAt(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (hitTest(child, x, y)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Checks whether a child is under the specific x and y coordinates.
     * 
     * @param child The child {@link View} to test.
     * @param x The parent local x coordinate.
     * @param y The parent local y coordinate.
     * @return a {@link Boolean} whether this child is under the point.
     */
    protected final static boolean hitTest(View child, int x, int y) {
        final Rect rect = new Rect(child.getLeft(), child.getTop(), child.getRight(),
                child.getBottom());
        return rect.contains(x, y);
    }
}
