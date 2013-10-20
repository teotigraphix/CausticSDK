////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.gs.machine.part;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.memory.MemoryBank;
import com.teotigraphix.caustk.gs.memory.TemporaryMemory;

/**
 * Abstract base class for all machine parts.
 */
public abstract class MachineComponentPart implements IDispatcher {

    protected final ICaustkController getController() {
        return machine.getController();
    }

    protected final MemoryBank getMemoryBank() {
        return machine.getMemory().getSelectedMemoryBank();
    }

    protected final TemporaryMemory getTemporaryMemory() {
        return machine.getMemory().getTemporaryMemory();
    }

    //----------------------------------
    // grooveMachine
    //----------------------------------

    private final GrooveMachine machine;

    /**
     * Returns the {@link GrooveMachine} owner of this part.
     */
    public final GrooveMachine getMachine() {
        return machine;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineComponentPart(GrooveMachine machine) {
        this.machine = machine;
    }

    //--------------------------------------------------------------------------
    // IDispatcher API
    //--------------------------------------------------------------------------

    @Override
    public <T> void register(Class<T> event, EventObserver<T> observer) {
        machine.register(event, observer);
    }

    @Override
    public void unregister(EventObserver<?> observer) {
        machine.unregister(observer);
    }

    @Override
    public void trigger(Object event) {
        machine.trigger(event);
    }

    @Override
    public void clear() {
        machine.clear();
    }
}
