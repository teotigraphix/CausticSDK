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
import com.teotigraphix.caustic.effect.IEffect;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.EffectRackMessage;

/**
 * Default implementation of the {@link IEffect} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class Effect extends EffectComponent implements IEffect
{

    //--------------------------------------------------------------------------
    //
    // IEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    private int mIndex;

    @Override
    public int getIndex()
    {
        return mIndex;
    }

    protected void setIndex(int value)
    {
        mIndex = value;
    }

    //----------------------------------
    // type
    //----------------------------------

    private EffectType mType;

    @Override
    public EffectType getType()
    {
        return mType;
    }

    /**
     * Subclasses set in constructor.
     * 
     * @param value The {@link EffectType} of the effect.
     */
    protected void setType(EffectType value)
    {
        mType = value;
    }

    private IMachine mMachine;

    @Override
    public IMachine getMachine()
    {
        return mMachine;
    }

    @Override
    public void setMachine(IMachine value)
    {
        setName(null);
        mMachine = value;
        if (mMachine != null)
        {
            setName(mMachine.getId());
        }
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public Effect(int index, IDevice device)
    {
        super(device); // device IEffectPanel
        setIndex(index);
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    /**
     * /caustic/effects_rack/[machine_index]/[slot]/[param]
     * 
     * @param control
     * @return
     */
    protected final float get(String control)
    {
        return EffectRackMessage.GET.query(getEngine(), getDeviceIndex(),
                getIndex(), control);
    }

    /**
     * /caustic/effects_rack/[machine_index]/[slot]/[param] [value]
     * 
     * @param control
     * @param value
     */
    protected final void set(String control, float value)
    {
        EffectRackMessage.SET.send(getEngine(), getDeviceIndex(), getIndex(),
                control, value);
    }

    protected final void set(String control, int value)
    {
        EffectRackMessage.SET.send(getEngine(), getDeviceIndex(), getIndex(),
                control, value);
    }

    @Override
    protected int getDeviceIndex()
    {
        if (mMachine != null)
            return mMachine.getIndex();
        return -1;
    }
}
