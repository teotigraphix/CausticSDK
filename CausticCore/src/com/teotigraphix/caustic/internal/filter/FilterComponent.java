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

import com.teotigraphix.caustic.filter.IFilterComponent;
import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.FilterMessage;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link IFilterComponent} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class FilterComponent extends MachineComponent implements
        IFilterComponent
{

    //--------------------------------------------------------------------------
    //
    // IFilterComponent API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    protected float mCutoff = 1.0f;

    @Override
    public float getCutoff()
    {
        return mCutoff;
    }

    public float getCutoff(boolean restore)
    {
        return FilterMessage.FILTER_CUTOFF
                .query(getEngine(), getMachineIndex());
    }

    @Override
    public void setCutoff(float value)
    {
        if (value == mCutoff)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(FilterMessage.FILTER_CUTOFF.toString(),
                    "0..1", value);
        mCutoff = value;
        FilterMessage.FILTER_CUTOFF.send(getEngine(), getMachineIndex(),
                mCutoff);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    protected float mResonance = 0f;

    @Override
    public float getResonance()
    {
        return mResonance;
    }

    public float getResonance(boolean restore)
    {
        return FilterMessage.FILTER_RESONANCE.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setResonance(float value)
    {
        if (value == mResonance)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(FilterMessage.FILTER_CUTOFF.toString(),
                    "0..1", value);
        mResonance = value;
        FilterMessage.FILTER_RESONANCE.send(getEngine(), getMachineIndex(),
                mResonance);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public FilterComponent(IMachine machine)
    {
        super(machine);
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
        memento.putFloat(FilterConstants.ATT_CUTOFF, getCutoff());
        memento.putFloat(FilterConstants.ATT_RESONANCE, getResonance());
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        setCutoff(memento.getFloat(FilterConstants.ATT_CUTOFF));
        setResonance(memento.getFloat(FilterConstants.ATT_RESONANCE));
    }

    @Override
    public void restore()
    {
        super.restore();
        setCutoff(getCutoff(true));
        setResonance(getResonance(true));
    }
}
