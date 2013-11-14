
package com.teotigraphix.caustk.workstation;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;

public class GrooveBoxDescriptor {

    @Tag(0)
    private GrooveBoxType grooveBoxType;

    @Tag(1)
    private String patternTypeId;

    @Tag(2)
    private List<PartDescriptor> parts = new ArrayList<PartDescriptor>();

    public GrooveBoxType getGrooveMachineType() {
        return grooveBoxType;
    }

    public String getPatternTypeId() {
        return patternTypeId;
    }

    public List<PartDescriptor> getParts() {
        return parts;
    }

    GrooveBoxDescriptor() {
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

        @Tag(50)
        private String patternTypeId;

        @Tag(51)
        private int machineIndex;

        @Tag(052)
        private String machineName;

        @Tag(53)
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

        PartDescriptor() {
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
