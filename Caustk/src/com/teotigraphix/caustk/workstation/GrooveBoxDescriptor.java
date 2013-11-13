
package com.teotigraphix.caustk.workstation;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.rack.tone.ToneDescriptor;

public class GrooveBoxDescriptor {

    private GrooveBoxType grooveBoxType;

    private String patternTypeId;

    public GrooveBoxType getGrooveMachineType() {
        return grooveBoxType;
    }

    private List<PartDescriptor> parts = new ArrayList<PartDescriptor>();

    public String getPatternTypeId() {
        return patternTypeId;
    }

    public List<PartDescriptor> getParts() {
        return parts;
    }

    public GrooveBoxDescriptor(GrooveBoxType grooveBoxType) {
        this.grooveBoxType = grooveBoxType;
        this.patternTypeId = grooveBoxType.getType();
    }

    public void addPart(int machineIndex, String machineName, MachineType machineType) {
        PartDescriptor part = new PartDescriptor(patternTypeId, machineIndex, machineName,
                machineType);
        parts.add(part);
    }

    /**
     * Adds a part description without index location requirement.
     * 
     * @param partName
     * @param machineType
     */
    public void addPart(String partName, MachineType machineType) {
        PartDescriptor part = new PartDescriptor(patternTypeId, partName, machineType);
        parts.add(part);
    }

    public static class PartDescriptor {

        private String patternTypeId;

        private int machineIndex;

        private String machineName;

        private MachineType machineType;

        public String getPatternTypeId() {
            return patternTypeId;
        }

        public final int getMachineIndex() {
            return machineIndex;
        }

        public final String getMachineName() {
            return machineName;
        }

        public final MachineType getMachineType() {
            return machineType;
        }

        public PartDescriptor(String patternTypeId, String machineName, MachineType machineType) {
            this.patternTypeId = patternTypeId;
            this.machineIndex = -1;
            this.machineName = machineName;
            this.machineType = machineType;
        }

        public PartDescriptor(String patternTypeId, int machineIndex, String machineName,
                MachineType machineType) {
            this.patternTypeId = patternTypeId;
            this.machineIndex = machineIndex;
            this.machineName = machineName;
            this.machineType = machineType;
        }

        public ToneDescriptor createDescriptor() {
            return new ToneDescriptor(machineIndex, machineName, machineType);
        }
    }
}
