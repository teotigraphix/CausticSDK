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
import com.teotigraphix.caustic.effect.IAutowahEffect;
import com.teotigraphix.common.IMemento;

/**
 * Default implementation of the {@link IAutowahEffect} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class AutowahEffect extends Effect implements IAutowahEffect {

    //--------------------------------------------------------------------------
    //
    // IAutowahEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    private float mCutoff = 2.23f;

    @Override
    public float getCutoff() {
        return mCutoff;
    }

    public float getCutoff(boolean restore) {
        return get(CONTROL_CUTOFF);
    }

    @Override
    public void setCutoff(float value) {
        if (value == mCutoff)
            return;
        if (value < 0.5f || value > 4.0f)
            throw newRangeException(CONTROL_CUTOFF, "0.5..4.0", value);
        mCutoff = value;
        set(CONTROL_CUTOFF, mCutoff);
    }

    //----------------------------------
    // depth
    //----------------------------------

    private float mDepth = 1f;

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
        if (value < 0f || value > 1f)
            throw newRangeException(CONTROL_DEPTH, "0..1", value);
        mDepth = value;
        set(CONTROL_DEPTH, mDepth);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    private float mResonance = 0.5f;

    @Override
    public float getResonance() {
        return mResonance;
    }

    public float getResonance(boolean restore) {
        return get(CONTROL_RESONANCE);
    }

    @Override
    public void setResonance(float value) {
        if (value == mResonance)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(CONTROL_RESONANCE, "0..1", value);
        mResonance = value;
        set(CONTROL_RESONANCE, mResonance);
    }

    //----------------------------------
    // speed
    //----------------------------------

    private float mSpeed = 0.4f;

    @Override
    public float getSpeed() {
        return mSpeed;
    }

    public float getSpeed(boolean restore) {
        return get(CONTROL_SPEED);
    }

    @Override
    public void setSpeed(float value) {
        if (value == mSpeed)
            return;
        if (value < 0f || value > 0.5f)
            throw newRangeException(CONTROL_SPEED, "0..0.5", value);
        mSpeed = value;
        set(CONTROL_SPEED, mSpeed);
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

    public AutowahEffect(int index, IDevice device) {
        super(index, device);
        setType(EffectType.AUTOWAH);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);
        memento.putFloat(EffectConstants.ATT_CUTOFF, getCutoff());
        memento.putFloat(EffectConstants.ATT_DEPTH, getDepth());
        memento.putFloat(EffectConstants.ATT_RESONANCE, getResonance());
        memento.putFloat(EffectConstants.ATT_SPEED, getSpeed());
        memento.putFloat(EffectConstants.ATT_WET, getWet());
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);
        setCutoff(memento.getFloat(EffectConstants.ATT_CUTOFF));
        setDepth(memento.getFloat(EffectConstants.ATT_DEPTH));
        setResonance(memento.getFloat(EffectConstants.ATT_RESONANCE));
        setSpeed(memento.getFloat(EffectConstants.ATT_SPEED));
        setWet(memento.getFloat(EffectConstants.ATT_WET));
    }

    @Override
    public void restore() {
        setCutoff(getCutoff(true));
        setDepth(getDepth(true));
        setResonance(getResonance(true));
        setSpeed(getSpeed(true));
        setWet(getWet(true));
    }
}
