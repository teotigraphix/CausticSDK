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

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.filter.ISubSynthLFO1;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.SubSynthLFOMessage;

/**
 * The default implementation of the {@link ISubSynthLFO1} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SubSynthLFO1 extends LFOComponent implements ISubSynthLFO1 {

    //--------------------------------------------------------------------------
    //
    // ILFO1SubSynth API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // target
    //----------------------------------

    private LFOTarget mTarget = LFOTarget.NONE;

    @Override
    public LFOTarget getTarget() {
        return mTarget;
    }

    public LFOTarget getTarget(boolean restore) {
        return LFOTarget.toType(SubSynthLFOMessage.LFO1_TARGET
                .query(getEngine(), getMachineIndex()));
    }

    @Override
    public void setTarget(LFOTarget value) {
        if (value == mTarget)
            return;
        mTarget = value;
        SubSynthLFOMessage.LFO1_TARGET.send(getEngine(), getMachineIndex(), mTarget.getValue());
    }

    //----------------------------------
    // waveForm
    //----------------------------------

    private WaveForm mWaveForm = WaveForm.SINE;

    @Override
    public WaveForm getWaveform() {
        return mWaveForm;
    }

    public WaveForm getWaveform(boolean restore) {
        return WaveForm.toType(SubSynthLFOMessage.LFO1_WAVEFORM.query(getEngine(),
                getMachineIndex()));
    }

    @Override
    public void setWaveForm(WaveForm value) {
        if (value == mWaveForm)
            return;
        mWaveForm = value;
        SubSynthLFOMessage.LFO1_WAVEFORM.send(getEngine(), getMachineIndex(), mWaveForm.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SubSynthLFO1(IMachine machine) {
        super(machine);
        mDepthMessage = SubSynthLFOMessage.LFO1_DEPTH;
        mRateMessage = SubSynthLFOMessage.LFO1_RATE;
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);
        memento.putInteger(FilterConstants.ATT_TARGET, getTarget().getValue());
        memento.putInteger(FilterConstants.ATT_WAVEFORM, getWaveform().getValue());
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);
        setWaveForm(WaveForm.toType(memento.getInteger(FilterConstants.ATT_WAVEFORM)));
        setTarget(LFOTarget.toType(memento.getInteger(FilterConstants.ATT_TARGET)));
    }

    @Override
    public void restore() {
        super.restore();
        setTarget(getTarget(true));
        setWaveForm(getWaveform(true));
    }
}
