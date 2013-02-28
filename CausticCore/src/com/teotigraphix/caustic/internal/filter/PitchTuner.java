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

import com.teotigraphix.caustic.filter.IPitchTuner;
import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.PitchMessage;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link IPitchTuner} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PitchTuner extends MachineComponent implements IPitchTuner {

    //--------------------------------------------------------------------------
    //
    // IPitchTuner API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    private int mCents = 0;

    @Override
    public int getCents() {
        return mCents;
    }

    public int getCents(boolean restore) {
        return (int)PitchMessage.PITCH_CENTS.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setCents(int value) {
        if (value == mCents)
            return;
        if (value < -50 || value > 50)
            throw newRangeException("pitch_cents", "-50..50", value);
        mCents = value;
        PitchMessage.PITCH_CENTS.send(getEngine(), getMachineIndex(), mCents);
    }

    //----------------------------------
    // octave
    //----------------------------------

    private int mOctave = 0;

    @Override
    public int getOctave() {
        return mOctave;
    }

    public int getOctave(boolean restore) {
        return (int)PitchMessage.PITCH_OCTAVE.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setOctave(int value) {
        if (value == mOctave)
            return;
        if (value < -4 || value > 4)
            throw newRangeException("pitch_octave", "-4..4", value);
        mOctave = value;
        PitchMessage.PITCH_OCTAVE.send(getEngine(), getMachineIndex(), mOctave);
    }

    //----------------------------------
    // semis
    //----------------------------------

    private int mSemis = 0;

    @Override
    public int getSemis() {
        return mSemis;
    }

    public int getSemis(boolean restore) {
        return (int)PitchMessage.PITCH_SEMIS.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setSemis(int value) {
        if (value == mSemis)
            return;
        if (value < -12 || value > 12)
            throw newRangeException("pitch_semis", "-12..12", value);
        mSemis = value;
        PitchMessage.PITCH_SEMIS.send(getEngine(), getMachineIndex(), mSemis);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public PitchTuner(IMachine machine) {
        super(machine);
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Public :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);
        memento.putInteger(FilterConstants.ATT_CENTS, getCents());
        memento.putInteger(FilterConstants.ATT_OCTAVE, getOctave());
        memento.putInteger(FilterConstants.ATT_SEMIS, getSemis());
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);
        setCents(memento.getInteger(FilterConstants.ATT_CENTS));
        setOctave(memento.getInteger(FilterConstants.ATT_OCTAVE));
        setSemis(memento.getInteger(FilterConstants.ATT_SEMIS));
    }

    @Override
    public void restore() {
        super.restore();
        setCents(getCents(true));
        setOctave(getOctave(true));
        setSemis(getSemis(true));
    }
}
