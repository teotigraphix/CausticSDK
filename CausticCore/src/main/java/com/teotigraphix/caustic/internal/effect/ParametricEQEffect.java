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
import com.teotigraphix.caustic.effect.IParametricEQEffect;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class ParametricEQEffect extends Effect implements IParametricEQEffect {

    //--------------------------------------------------------------------------
    //
    // IParametricEQEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    private float mFrequency = 0.54f;

    @Override
    public float getFrequency() {
        return mFrequency;
    }

    public float getFrequency(boolean restore) {
        return get(CONTROL_FREQUENCY);
    }

    @Override
    public void setFrequency(float value) {
        if (value == mFrequency)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(CONTROL_FREQUENCY, "0.0..1.0", value);
        mFrequency = value;
        set(CONTROL_FREQUENCY, mFrequency);
    }

    //----------------------------------
    // rate
    //----------------------------------

    private int mGain = 0;

    @Override
    public int getGain() {
        return mGain;
    }

    public int getGain(boolean restore) {
        return (int)get(CONTROL_GAIN);
    }

    @Override
    public void setGain(int value) {
        if (value == mGain)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(CONTROL_GAIN, "-12..12", value);
        mGain = value;
        set(CONTROL_GAIN, mGain);
    }

    //----------------------------------
    // wet
    //----------------------------------

    private float mWidth = 0.49999994f;

    @Override
    public float getWidth() {
        return mWidth;
    }

    public float getWidth(boolean restore) {
        return get(CONTROL_WIDTH);
    }

    @Override
    public void setWidth(float value) {
        if (value == mWidth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(CONTROL_WIDTH, "0.0..1.0", value);
        mWidth = value;
        set(CONTROL_WIDTH, mWidth);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public ParametricEQEffect(int index, IDevice device) {
        super(index, device);
        setType(EffectType.PARAMETRICEQ);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putFloat(CONTROL_FREQUENCY, getFrequency());
        memento.putInteger(CONTROL_GAIN, getGain());
        memento.putFloat(CONTROL_WIDTH, getWidth());
    }

    @Override
    public void paste(IMemento memento) {
        setFrequency(memento.getFloat(CONTROL_FREQUENCY));
        setGain(memento.getInteger(CONTROL_GAIN));
        setWidth(memento.getFloat(CONTROL_WIDTH));
    }

    @Override
    public void restore() {
        setFrequency(getFrequency(true));
        setGain(getGain(true));
        setWidth(getWidth(true));
    }
}
