
package com.teotigraphix.caustk.gs.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.tone.ToneType;

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
        ISystemSequencer systemSequencer = controller.getSystemSequencer();

        systemSequencer.getDispatcher().register(OnSystemSequencerBeatChange.class,
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

        private MachineType machineType;

        public MachineType getMachineType() {
            return machineType;
        }

        public GrooveMachineDescriptor(MachineType machineType) {
            this.machineType = machineType;
        }

        public GrooveMachine createMachine() {
            switch (machineType) {
                case Bassline:
                    return new BasslineMachine();

                default:
                    break;
            }
            return null;
        }

        public void addPart(String name, ToneType toneType) {
            GrooveMachinePart part = new GrooveMachinePart(name, toneType);
            parts.add(part);
        }
    }

    public static class GrooveMachinePart {

        private String name;

        public String getName() {
            return name;
        }

        private ToneType toneType;

        public ToneType getToneType() {
            return toneType;
        }

        public GrooveMachinePart(String name, ToneType toneType) {
            this.name = name;
            this.toneType = toneType;
        }
    }

}
