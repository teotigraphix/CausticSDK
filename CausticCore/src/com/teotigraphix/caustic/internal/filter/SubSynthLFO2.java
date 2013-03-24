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

import com.teotigraphix.caustic.filter.ISubSynthLFO2;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.SubSynthLFOMessage;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link ISubSynthLFO2} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SubSynthLFO2 extends LFOComponent implements ISubSynthLFO2
{

    //--------------------------------------------------------------------------
    //
    // ILFO2SubSynth API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // target
    //----------------------------------

    private LFOTarget mTarget = LFOTarget.NONE;

    @Override
    public LFOTarget getTarget()
    {
        return mTarget;
    }

    public LFOTarget getTarget(boolean restore)
    {
        return LFOTarget.toType(SubSynthLFOMessage.LFO2_TARGET.query(
                getEngine(), getMachineIndex()));
    }

    @Override
    public void setTarget(LFOTarget value)
    {
        if (value == mTarget)
            return;
        mTarget = value;
        SubSynthLFOMessage.LFO2_TARGET.send(getEngine(), getMachineIndex(),
                mTarget.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SubSynthLFO2(IMachine machine)
    {
        super(machine);
        mDepthMessage = SubSynthLFOMessage.LFO2_DEPTH;
        mRateMessage = SubSynthLFOMessage.LFO2_RATE;
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
        memento.putInteger(FilterConstants.ATT_TARGET, getTarget().getValue());
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        setTarget(LFOTarget.toType(memento
                .getInteger(FilterConstants.ATT_TARGET)));
    }

    @Override
    public void restore()
    {
        super.restore();
        setTarget(getTarget(true));
    }
}
