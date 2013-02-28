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

package com.teotigraphix.caustic.internal.rack;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * The default implementation of the {@link IPersistable} state for the Rack
 * class.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class RackState implements IPersist {

    Rack mRack;

    public RackState(Rack rack) {
        mRack = rack;
    }

    @Override
    public void copy(IMemento memento) {
        IMemento state = null;

        // save the mixer panel
        state = memento.createChild(RackConsants.TAG_MIXER_PANEL);
        mRack.getMixerPanel().copy(state);

        // save the outputpanel
        state = memento.createChild(RackConsants.TAG_OUTPUT_PANEL);
        mRack.getOutputPanel().copy(state);

        // save the sequencer
        state = memento.createChild(RackConsants.TAG_SEQUENCER);
        mRack.getSequencer().copy(state);

        saveMachines(memento.createChild(RackConsants.TAG_MACHINES));
    }

    private void saveMachines(IMemento memento) {
        for (IMachine machine : mRack.getMachineMap().values()) {
            saveMachine(machine, memento.createChild(RackConsants.TAG_MACHINE));
        }
    }

    private void saveMachine(IMachine machine, IMemento memento) {
        machine.copy(memento);
    }

    @Override
    public void paste(IMemento memento) {
        try {
            loadMachines(memento.getChild(RackConsants.TAG_MACHINES));
        } catch (CausticException e) {
            e.printStackTrace();
        }

        mRack.getMixerPanel().paste(memento.getChild(RackConsants.TAG_MIXER_PANEL));
        mRack.getOutputPanel().paste(memento.getChild(RackConsants.TAG_OUTPUT_PANEL));
        mRack.getSequencer().paste(memento.getChild(RackConsants.TAG_SEQUENCER));
    }

    private void loadMachines(IMemento memento) throws CausticException {
        for (IMemento machine : memento.getChildren(RackConsants.TAG_MACHINE)) {
            loadMachine(machine);
        }
    }

    private void loadMachine(IMemento memento) throws CausticException {
        String name = memento.getString(RackConsants.ATT_ID);
        int index = memento.getInteger(RackConsants.ATT_INDEX);
        String type = memento.getString(RackConsants.ATT_TYPE);

        // fist try to see if it's already created
        IMachine machine = mRack.getMachine(index);

        if (machine == null) {
            machine = mRack.addMachineAt(index, name, type, false);
        }

        if (machine == null) {
            throw new CausticException("Could not create IMachine in IRack");
        }

        machine.paste(memento);
    }
}
