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

package com.teotigraphix.caustic.internal.filter;

import com.teotigraphix.caustic.filter.IBasslineLFO1;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.BasslineLFOMessage;
import com.teotigraphix.common.IMemento;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BasslineLFO1 extends LFOComponent implements IBasslineLFO1
{

    //--------------------------------------------------------------------------
    //
    // IBasslineLFO1 API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // phase
    //----------------------------------

    private float mPhase = 0f;

    @Override
    public float getPhase()
    {
        return mPhase;
    }

    public float getPhase(boolean restore)
    {
        return BasslineLFOMessage.LFO_PHASE.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setPhase(float value)
    {
        if (value == mPhase)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BasslineLFOMessage.LFO_PHASE.toString(),
                    "0..1", value);
        mPhase = value;
        BasslineLFOMessage.LFO_PHASE.send(getEngine(), getMachineIndex(),
                mPhase);
    }

    //----------------------------------
    // target
    //----------------------------------

    private LFOTarget mTarget = LFOTarget.OFF;

    @Override
    public LFOTarget getTarget()
    {
        return mTarget;
    }

    public LFOTarget getTarget(boolean restore)
    {
        return LFOTarget.toType(BasslineLFOMessage.LFO_TARGET.query(
                getEngine(), getMachineIndex()));
    }

    @Override
    public void setTarget(LFOTarget value)
    {
        if (value == mTarget)
            return;
        mTarget = value;
        BasslineLFOMessage.LFO_TARGET.send(getEngine(), getMachineIndex(),
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
    public BasslineLFO1(IMachine machine)
    {
        super(machine);
        mDepthMessage = BasslineLFOMessage.LFO_DEPTH;
        mRateMessage = BasslineLFOMessage.LFO_RATE;
        mRate = 0;
    }

    @Override
    public void copy(IMemento memento)
    {
        super.copy(memento);
        memento.putInteger(FilterConstants.ATT_TARGET, getTarget().getValue());
        memento.putFloat(FilterConstants.ATT_PHASE, getPhase());
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        setTarget(LFOTarget.toType(memento
                .getInteger(FilterConstants.ATT_TARGET)));
        setPhase(memento.getFloat(FilterConstants.ATT_PHASE));
    }

    @Override
    public void restore()
    {
        super.restore();
        setPhase(getPhase(true));
        setTarget(getTarget(true));
    }
}
