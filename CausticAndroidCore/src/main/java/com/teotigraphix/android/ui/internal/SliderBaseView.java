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

package com.teotigraphix.android.ui.internal;

import android.content.Context;
import android.util.AttributeSet;

public class SliderBaseView extends RangeView {
    private int mThumbThickness;

    protected int getThumbThickness() {
        return mThumbThickness;
    }

    protected void setThumbThickness(int value) {
        mThumbThickness = value;
    }

    //--------------------------------------------------------------------------
    //
    // Public API :: Properties
    //
    //--------------------------------------------------------------------------

    //---------------------------------
    // maximum
    //---------------------------------

    private SliderDirection mDirection;

    public final SliderDirection getDirection() {
        return mDirection;
    }

    public final void setDirection(SliderDirection direction) {
        mDirection = direction;
    }

    //--------------------------------------------------------------------------
    //
    // Constructors
    //
    //--------------------------------------------------------------------------

    public SliderBaseView(Context context) {
        super(context);
    }

    public SliderBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SliderBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Public :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void changeValueByStep(Boolean increase) {
        @SuppressWarnings("unused")
        float prevValue = getSelectedValue();

        super.changeValueByStep(increase);

        // if (getSelectedValue() != prevValue)
        // dispatchEvent(new Event(Event.CHANGE));
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    protected float pointToValue(float x, float y) {
        float result;

        float range;
        float thumbRange;

        if (mDirection == SliderDirection.VERTICAL) {
            range = getMaximum() - getMinimum();
            thumbRange = getThumbRange();
            result = getMinimum() + ((thumbRange != 0) ? (y / thumbRange) * range : 0);
        } else {
            range = getMaximum() - getMinimum();
            thumbRange = getThumbRange();
            result = getMinimum() + ((thumbRange != 0) ? (x / thumbRange) * range : 0);
        }

        return result;
    }

    protected int getThumbRange() {
        if (getDirection() == SliderDirection.VERTICAL)
            return getHeight() - getThumbThickness();
        else
            return getWidth() - getThumbThickness();
    }

    public enum SliderDirection {
        VERTICAL, HORIZONTAL;
    }
}