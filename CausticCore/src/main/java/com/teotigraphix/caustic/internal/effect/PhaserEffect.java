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

package com.teotigraphix.caustic.internal.effect;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.effect.IPhaserEffect;

/**
 * Default implementation of the {@link IPhaserEffect} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PhaserEffect extends Effect implements IPhaserEffect {

    //--------------------------------------------------------------------------
    //
    // IPhaserEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    private float mDepth = 0.8f;

    @Override
    public float getDepth() {
        return mDepth;
    }

    public float getDepth(boolean restore) {
        return get(CONTROL_DEPTH);
    }

    @Override
    public void setDepth(float value) {
        if (value == mDepth)
            return;
        if (value < 0.1f || value > 0.95f)
            throw newRangeException(CONTROL_DEPTH, "0.1..0.95", value);
        mDepth = value;
        set(CONTROL_DEPTH, mDepth);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    private float mFeedback = 0.9f;

    @Override
    public float getFeedback() {
        return mFeedback;
    }

    public float getFeedback(boolean restore) {
        return get(CONTROL_FEEDBACK);
    }

    @Override
    public void setFeedback(float value) {
        if (value == mFeedback)
            return;
        // XXX Phaser BUG if (value < 0.1f || value > 0.95f)
        //    throw newRangeException(CONTROL_DEPTH, "0.1..0.95", value);
        mFeedback = value;
        set(CONTROL_FEEDBACK, mFeedback);
    }

    //----------------------------------
    // rate
    //----------------------------------

    private int mRate = 10;

    @Override
    public int getRate() {
        return mRate;
    }

    public int getRate(boolean restore) {
        return (int)get(CONTROL_RATE);
    }

    @Override
    public void setRate(int value) {
        if (value == mRate)
            return;
        if (value < 2 || value > 50)
            throw newRangeException(CONTROL_RATE, "2..50", value);
        mRate = value;
        set(CONTROL_RATE, mRate);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public PhaserEffect(int index, IDevice device) {
        super(index, device);
        setType(EffectType.PHASER);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putFloat(CONTROL_DEPTH, getDepth());
        memento.putFloat(CONTROL_FEEDBACK, getFeedback());
        memento.putInteger(CONTROL_RATE, getRate());
    }

    @Override
    public void paste(IMemento memento) {
        setDepth(memento.getFloat(CONTROL_DEPTH));
        setFeedback(memento.getFloat(CONTROL_FEEDBACK));
        setRate(memento.getInteger(CONTROL_RATE));
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setFeedback(getFeedback(true));
        setRate(getRate(true));
    }
}
