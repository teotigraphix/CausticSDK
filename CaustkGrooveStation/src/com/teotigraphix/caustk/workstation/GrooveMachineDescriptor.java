
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

    public void addPart(int index, String partName, MachineType machineType) {
        PartDescriptor part = new PartDescriptor(index, partName, machineType);
        parts.add(part);
    }

    /**
     * Adds a part description without index location requirement.
     * 
     * @param partName
     * @param machineType
     */
    public void addPart(String partName, MachineType machineType) {
        PartDescriptor part = new PartDescriptor(partName, machineType);
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

        public PartDescriptor(String machineName, MachineType machineType) {
            this.machineIndex = -1;
            this.machineName = machineName;
            this.machineType = machineType;
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
