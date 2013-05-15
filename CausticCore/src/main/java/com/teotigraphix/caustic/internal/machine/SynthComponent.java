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

package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.ISynthComponent;
import com.teotigraphix.caustic.osc.SynthMessage;

/**
 * The default implementation of the {@link ISynthComponent} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SynthComponent extends MachineComponent implements ISynthComponent {

    //--------------------------------------------------------------------------
    //
    // ISynthComponent API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // polyphony
    //----------------------------------

    private int mPolyphony = 4;

    @Override
    public int getPolyphony() {
        return mPolyphony;
    }

    public int getPolyphony(boolean restore) {
        return (int)SynthMessage.POLYPHONY.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setPolyphony(int value) {
        if (value == mPolyphony)
            return;
        if (value < 1 || value > 16)
            throw newRangeException(SynthMessage.POLYPHONY.toString(), "1..16", value);
        mPolyphony = value;
        SynthMessage.POLYPHONY.send(getEngine(), getMachineIndex(), mPolyphony);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SynthComponent(IMachine machine) {
        super(machine);
    }

    //--------------------------------------------------------------------------
    //
    // ISynthComponent API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void noteOn(int pitch) {
        noteOn(pitch, 1f);
    }

    @Override
    public void noteOn(int pitch, float velocity) {
        SynthMessage.NOTE.send(getEngine(), getMachineIndex(), pitch, 1, velocity);
    }

    @Override
    public void noteOff(int pitch) {
        SynthMessage.NOTE.send(getEngine(), getMachineIndex(), pitch, 0);
    }

    @Override
    public void notePreview(int pitch, boolean oneshot) {
        SynthMessage.NOTE_PREVIEW.send(getEngine(), getMachineIndex(), pitch, oneshot);
    }

    @Override
    public void copy(IMemento memento) {
        memento.putInteger(SynthConstants.ATT_POLYPHONY, getPolyphony());
    }

    @Override
    public void paste(IMemento memento) {
        setPolyphony(memento.getInteger(SynthConstants.ATT_POLYPHONY));
    }

    @Override
    public void restore() {
        setPolyphony(getPolyphony(true));
    }

}
