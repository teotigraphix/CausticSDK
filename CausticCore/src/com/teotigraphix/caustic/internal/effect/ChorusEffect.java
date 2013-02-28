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

import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.effect.IChorusEffect;
import com.teotigraphix.common.IMemento;

/**
 * Default implementation of the {@link IChorusEffect} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class ChorusEffect extends Effect implements IChorusEffect {

    //--------------------------------------------------------------------------
    //
    // IChorusEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    private float mDepth = 0.3f;

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
    // rate
    //----------------------------------

    private float mRate = 0.4f;

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
        if (value < 0f || value > 1.0f)
            throw newRangeException(CONTROL_RATE, "0.0..1.0", value);
        mRate = value;
        set(CONTROL_RATE, mRate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    private float mWet = 0.5f;

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

    public ChorusEffect(int index, IDevice device) {
        super(index, device);
        setType(EffectType.CHORUS);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);
        memento.putFloat(CONTROL_DEPTH, getDepth());
        memento.putFloat(CONTROL_RATE, getRate());
        memento.putFloat(CONTROL_WET, getWet());
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);
        setDepth(memento.getFloat(CONTROL_DEPTH));
        setRate(memento.getFloat(CONTROL_RATE));
        setWet(memento.getFloat(CONTROL_WET));
    }

    @Override
    public void restore() {
        super.restore();
        setDepth(getDepth(true));
        setRate(getRate(true));
        setWet(getWet(true));
    }
}
