
package com.teotigraphix.caustk.workstation;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.rack.tone.ToneDescriptor;

public class GrooveMachineDescriptor {

    private List<PartDescriptor> parts = new ArrayList<PartDescriptor>();

    public List<PartDescriptor> getParts() {
        return parts;
    }

    public GrooveMachineDescriptor() {
    }

    public void addPart(int index, String name, MachineType machineType) {
        PartDescriptor part = new PartDescriptor(index, name, machineType);
        parts.add(part);
    }

    public static class PartDescriptor {

        private int machineIndex;

        public final int getMachineIndex() {
            return machineIndex;
        }

        private String machineName;

        public final String getMachineName() {
            return machineName;
        }

        private MachineType machineType;

        public final MachineType getMachineType() {
            return machineType;
        }

        public PartDescriptor(int machineIndex, String machineName, MachineType machineType) {
            this.machineIndex = machineIndex;
            this.machineName = machineName;
            this.machineType = machineType;
        }

        public ToneDescriptor createDescriptor() {
            return new ToneDescriptor(machineIndex, machineName, machineType);
        }
    }
}
