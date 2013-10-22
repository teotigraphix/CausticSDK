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

package com.teotigraphix.caustk.gs.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.ToneType;

/**
 * Holds all {@link GrooveMachine}s in an application, tha main controller for
 * all machines.
 */
public class GrooveStation {

    private ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    private List<GrooveMachine> machines = new ArrayList<GrooveMachine>();

    public List<GrooveMachine> getMachines() {
        return Collections.unmodifiableList(machines);
    }

    public GrooveStation(ICaustkController controller) {
        this.controller = controller;
    }

    public void setup(GrooveStationSetup setup) throws CausticException {
        // set up the sequencer handler
        setupSystemSequencer();

        // create the full setup for all machine descriptors
        for (GrooveMachineDescriptor descriptor : setup.getDescriptors()) {

            GrooveMachine machine = descriptor.createMachine();
            machines.add(machine);

            machine.setController(controller);
            machine.setup(descriptor);

        }

        // setup mixer

        // setup effects

        // setup initial memory (patterns, patches)

    }

    private void setupSystemSequencer() {
        controller.register(OnSystemSequencerBeatChange.class,
                new EventObserver<OnSystemSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSystemSequencerBeatChange object) {
                        beatChange(object.getMeasure(), object.getBeat());
                    }
                });
    }

    protected void beatChange(int measure, float beat) {
        for (GrooveMachine machine : machines) {
            machine.beatChange(measure, beat);
        }
    }

    public static class GrooveStationSetup {

        private List<GrooveMachineDescriptor> descriptors = new ArrayList<GrooveMachineDescriptor>();

        public List<GrooveMachineDescriptor> getDescriptors() {
            return descriptors;
        }

        public void addDescriptor(GrooveMachineDescriptor descriptor) {
            descriptors.add(descriptor);
        }
    }

    public static class GrooveMachineDescriptor {

        private List<GrooveMachinePart> parts = new ArrayList<GrooveMachinePart>();

        public List<GrooveMachinePart> getParts() {
            return parts;
        }

        private GrooveMachine machine;

        private MachineType machineType;

        public MachineType getMachineType() {
            return machineType;
        }

        public GrooveMachineDescriptor(MachineType machineType) {
            this.machineType = machineType;
        }

        public GrooveMachineDescriptor(MachineType machineType, GrooveMachine machine) {
            this.machineType = machineType;
            this.machine = machine;
        }

        public GrooveMachine createMachine() {
            // for an app like MS1, it is only using one machine type
            // and sets the instance in the descriptor
            if (machine != null)
                return machine;

            switch (machineType) {
                case Bassline:
                    return new BasslineMachine();
                case Drum:
                    return new DrumMachine();
                default:
                    break;
            }
            return null;
        }

        public void addPart(int index, String name, ToneType toneType) {
            GrooveMachinePart part = new GrooveMachinePart(index, name, toneType);
            parts.add(part);
        }
    }

    public static class GrooveMachinePart {

        private int index;

        public final int getIndex() {
            return index;
        }

        private String name;

        public final String getName() {
            return name;
        }

        private ToneType toneType;

        public final ToneType getToneType() {
            return toneType;
        }

        public GrooveMachinePart(int index, String name, ToneType toneType) {
            this.index = index;
            this.name = name;
            this.toneType = toneType;
        }

        public ToneDescriptor createDescriptor() {
            return new ToneDescriptor(index, name, toneType);
        }
    }

}
