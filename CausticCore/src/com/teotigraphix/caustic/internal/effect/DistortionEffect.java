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
import com.teotigraphix.caustic.effect.IDistortionEffect;
import com.teotigraphix.common.IMemento;

/**
 * Default implementation of the {@link IDistortionEffect} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class DistortionEffect extends Effect implements IDistortionEffect
{

    //--------------------------------------------------------------------------
    //
    // IDistortionEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // program
    //----------------------------------

    private Program mProgram = Program.OVERDRIVE;

    @Override
    public Program getProgram()
    {
        return mProgram;
    }

    public Program getProgram(boolean restore)
    {
        return Program.toType((int) get(CONTROL_PROGRAM));
    }

    @Override
    public void setProgram(Program value)
    {
        if (value == mProgram)
            return;
        mProgram = value;
        set(CONTROL_PROGRAM, mProgram.getValue());
    }

    //----------------------------------
    // threshold
    //----------------------------------

    private float mPreGain;

    @Override
    public float getPreGain()
    {
        return mPreGain;
    }

    public float getPreGain(boolean restore)
    {
        return get(CONTROL_PRE);
    }

    @Override
    public void setPreGain(float value)
    {
        if (value == mPreGain)
            return;
        if (value < 0f || value > 5f)
            newRangeException(CONTROL_PRE, "0.0..5.0", value);
        mPreGain = value;
        set(CONTROL_PRE, mPreGain);
    }

    //----------------------------------
    // amount
    //----------------------------------

    private float mAmount = 16.3f;

    @Override
    public float getAmount()
    {
        return mAmount;
    }

    public float getAmount(boolean restore)
    {
        return get(CONTROL_AMOUNT);
    }

    @Override
    public void setAmount(float value)
    {
        if (value == mAmount)
            return;
        if (value < 0f || value > 5f)
            newRangeException(CONTROL_AMOUNT, "0.0..20.0", value);
        mAmount = value;
        set(CONTROL_AMOUNT, mAmount);
    }

    //----------------------------------
    // post
    //----------------------------------

    private float mPostGain;

    @Override
    public float getPostGain()
    {
        return mPostGain;
    }

    public float getPostGain(boolean restore)
    {
        return get(CONTROL_POST);
    }

    @Override
    public void setPostGain(float value)
    {
        if (value == mPostGain)
            return;
        if (value < 0f || value > 5f)
            newRangeException(CONTROL_POST, "0.0..1.0", value);
        mPostGain = value;
        set(CONTROL_POST, mPostGain);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public DistortionEffect(int index, IDevice device)
    {
        super(index, device);
        setType(EffectType.DISTORTION);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
        super.copy(memento);
        memento.putFloat(CONTROL_AMOUNT, getAmount());
        memento.putFloat(CONTROL_PRE, getPreGain());
        memento.putInteger(CONTROL_PROGRAM, getProgram().getValue());
        memento.putFloat(CONTROL_POST, getPostGain());
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        setAmount(memento.getFloat(CONTROL_AMOUNT));
        setPreGain(memento.getFloat(CONTROL_PRE));
        setProgram(Program.toType(memento.getInteger(CONTROL_PROGRAM)));
        setPostGain(memento.getFloat(CONTROL_POST));
    }

    @Override
    public void restore()
    {
        setAmount(getAmount(true));
        setPreGain(getPreGain(true));
        setProgram(getProgram(true));
        setPostGain(getPostGain(true));
    }
}
