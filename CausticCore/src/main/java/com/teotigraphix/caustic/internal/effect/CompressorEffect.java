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
import com.teotigraphix.caustic.effect.ICompressorEffect;

/**
 * Default implementation of the {@link ICompressorEffect} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class CompressorEffect extends Effect implements ICompressorEffect {

    //--------------------------------------------------------------------------
    //
    // ICompressorEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    private float mAttack = 0.01f;

    @Override
    public float getAttack() {
        return mAttack;
    }

    public float getAttack(boolean restore) {
        return get(CONTROL_ATTACK);
    }

    @Override
    public void setAttack(float value) {
        if (value == mAttack)
            return;
        if (value < 0.00001f || value > 0.2f)
            throw newRangeException(CONTROL_ATTACK, "0.00001..0.2", value);
        mAttack = value;
        set(CONTROL_ATTACK, mAttack);
    }

    //----------------------------------
    // ratio
    //----------------------------------

    private float mRatio = 1f;

    @Override
    public float getRatio() {
        return mRatio;
    }

    public float getRatio(boolean restore) {
        return get(CONTROL_RATIO);
    }

    @Override
    public void setRatio(float value) {
        if (value == mRatio)
            return;
        if (value < 0f || value > 1.0f)
            throw newRangeException(CONTROL_RATIO, "0.0..1.0", value);
        mRatio = value;
        set(CONTROL_RATIO, mRatio);
    }

    //----------------------------------
    // release
    //----------------------------------

    private float mRelease = 0.05f;

    @Override
    public float getRelease() {
        return mRelease;
    }

    public float getRelease(boolean restore) {
        return get(CONTROL_RELEASE);
    }

    @Override
    public void setRelease(float value) {
        if (value == mRelease)
            return;
        if (value < 0.001f || value > 0.2f)
            throw newRangeException(CONTROL_RELEASE, "0.001..0.2", value);
        mRelease = value;
        set(CONTROL_RELEASE, mRelease);
    }

    //----------------------------------
    // sidechain
    //----------------------------------

    private int mSidechain = -1;

    @Override
    public int getSidechain() {
        return mSidechain;
    }

    public int getSidechain(boolean restore) {
        return (int)get(CONTROL_SIDECHAIN);
    }

    @Override
    public void setSidechain(int value) {
        if (value == mSidechain)
            return;
        if (value < 0 || value > 6)
            throw newRangeException(CONTROL_SIDECHAIN, "0..6", value);
        mSidechain = value;
        set(CONTROL_SIDECHAIN, mSidechain);
    }

    //----------------------------------
    // threshold
    //----------------------------------

    private float mThreshold = 0.1f;

    @Override
    public float getThreshold() {
        return mThreshold;
    }

    public float getThreshold(boolean restore) {
        return get(CONTROL_THRESHOLD);
    }

    @Override
    public void setThreshold(float value) {
        if (value == mThreshold)
            return;
        if (value < 0f || value > 1.0f)
            throw newRangeException(CONTROL_THRESHOLD, "0.0..1.0", value);
        mThreshold = value;
        set(CONTROL_THRESHOLD, mThreshold);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public CompressorEffect(int index, IDevice device) {
        super(index, device);
        setType(EffectType.COMPRESSOR);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putFloat(CONTROL_ATTACK, getAttack());
        memento.putFloat(CONTROL_RATIO, getRatio());
        memento.putFloat(CONTROL_RELEASE, getRelease());
        memento.putInteger(CONTROL_SIDECHAIN, getSidechain());
        memento.putFloat(CONTROL_THRESHOLD, getThreshold());
    }

    @Override
    public void paste(IMemento memento) {
        setAttack(memento.getFloat(CONTROL_ATTACK));
        setRatio(memento.getFloat(CONTROL_RATIO));
        setRelease(memento.getFloat(CONTROL_RELEASE));
        setSidechain(memento.getInteger(CONTROL_SIDECHAIN));
        setThreshold(memento.getFloat(CONTROL_THRESHOLD));
    }

    @Override
    public void restore() {
        setAttack(getAttack(true));
        setRatio(getRatio(true));
        setRelease(getRelease(true));
        setSidechain(getSidechain(true));
        setThreshold(getThreshold(true));
    }
}
