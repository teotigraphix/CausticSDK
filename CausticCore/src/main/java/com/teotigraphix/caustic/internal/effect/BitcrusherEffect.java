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
import com.teotigraphix.caustic.effect.IBitcrusherEffect;

/**
 * Default implementation of the {@link IBitcrusherEffect} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BitcrusherEffect extends Effect implements IBitcrusherEffect {

    //--------------------------------------------------------------------------
    //
    // IBitcrusherEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    private int mDepth = 3;

    @Override
    public int getDepth() {
        return mDepth;
    }

    public int getDepth(boolean restore) {
        return (int)get(CONTROL_DEPTH);
    }

    @Override
    public void setDepth(int value) {
        if (value == mDepth)
            return;
        if (value < 1 || value > 16)
            throw newRangeException(CONTROL_DEPTH, "1..16", value);
        mDepth = value;
        set(CONTROL_DEPTH, mDepth);
    }

    //----------------------------------
    // jitter
    //----------------------------------

    private float mJitter = 0f;

    @Override
    public float getJitter() {
        return mJitter;
    }

    public float getJitter(boolean restore) {
        return get(CONTROL_JITTER);
    }

    @Override
    public void setJitter(float value) {
        if (value == mJitter)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(CONTROL_JITTER, "0..1", value);
        mJitter = value;
        set(CONTROL_JITTER, mJitter);
    }

    //----------------------------------
    // rate
    //----------------------------------

    private float mRate = 0.1f;

    @Override
    public float getRate() {
        return mRate;
    }

    public float getRate(boolean restore) {
        return get(CONTROL_RATE);
    }

    @Override
    public void setRate(float value) {
        if (value == mRate)
            return;
        if (value < 0.01f || value > 0.5f)
            throw newRangeException(CONTROL_RATE, "0.01..0.5", value);
        mRate = value;
        set(CONTROL_RATE, mRate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    private float mWet = 1f;

    @Override
    public float getWet() {
        return mWet;
    }

    public float getWet(boolean restore) {
        return get(CONTROL_WET);
    }

    @Override
    public void setWet(float value) {
        if (value == mWet)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(CONTROL_WET, "0..1", value);
        mWet = value;
        set(CONTROL_WET, mWet);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public BitcrusherEffect(int index, IDevice device) {
        super(index, device);
        setType(EffectType.BITCRUSHER);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putInteger(CONTROL_DEPTH, getDepth());
        memento.putFloat(CONTROL_JITTER, getJitter());
        memento.putFloat(CONTROL_RATE, getRate());
        memento.putFloat(CONTROL_WET, getWet());
    }

    @Override
    public void paste(IMemento memento) {
        setDepth(memento.getInteger(CONTROL_DEPTH));
        setJitter(memento.getFloat(CONTROL_JITTER));
        setRate(memento.getFloat(CONTROL_RATE));
        setWet(memento.getFloat(CONTROL_WET));
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setJitter(getJitter(true));
        setRate(getRate(true));
        setWet(getWet(true));
    }
}
