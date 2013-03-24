////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.filter;

import com.teotigraphix.caustic.core.CausticMessage;
import com.teotigraphix.caustic.filter.ILFOComponent;
import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link ILFOComponent} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class LFOComponent extends MachineComponent implements
        ILFOComponent
{

    protected CausticMessage mDepthMessage;

    protected CausticMessage mRateMessage;

    //--------------------------------------------------------------------------
    //
    // ILFOComponent API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // rate
    //----------------------------------

    protected int mRate = 1;

    @Override
    public int getRate()
    {
        return mRate;
    }

    public int getRate(boolean restore)
    {
        return (int) mRateMessage.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setRate(int value)
    {
        if (value == mRate)
            return;
        if (value < 0 || value > 12)
            throw newRangeException("lfo1_rate", "0..12", value);
        mRate = value;
        mRateMessage.send(getEngine(), getMachineIndex(), mRate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    private float mDepth = 0.0f;

    @Override
    public float getDepth()
    {
        return mDepth;
    }

    public float getDepth(boolean restore)
    {
        return mDepthMessage.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setDepth(float value)
    {
        if (value == mDepth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("lfo1_depth", "0..1", value);
        mDepth = value;
        mDepthMessage.send(getEngine(), getMachineIndex(), mDepth);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public LFOComponent(IMachine machine)
    {
        super(machine);
    }

    @Override
    public void copy(IMemento memento)
    {
        memento.putInteger(FilterConstants.ATT_RATE, getRate());
        memento.putFloat(FilterConstants.ATT_DEPTH, getDepth());
    }

    @Override
    public void paste(IMemento memento)
    {
        setRate(memento.getInteger(FilterConstants.ATT_RATE));
        setDepth(memento.getFloat(FilterConstants.ATT_DEPTH));
    }

    @Override
    public void restore()
    {
        super.restore();
        setDepth(getDepth(true));
        setRate(getRate(true));
    }
}
