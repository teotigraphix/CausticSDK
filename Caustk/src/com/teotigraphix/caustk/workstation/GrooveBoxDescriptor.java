
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

    public void addPart(int machineIndex, String machineName, MachineType machineType,
            byte[] presetData) {
        int index = parts.size();
        PartDescriptor part = new PartDescriptor(index, patternTypeId, machineIndex, machineName,
                machineType, presetData);
        parts.add(part);
    }

    /**
     * Adds a part description without native machineIndex location requirement.
     * 
     * @param partName
     * @param machineType
     * @param presetData
     */
    public void addPart(String partName, MachineType machineType, byte[] presetData) {
        int index = parts.size();
        PartDescriptor part = new PartDescriptor(index, patternTypeId, partName, machineType,
                presetData);
        parts.add(part);
    }

    public static class PartDescriptor {

        @Tag(50)
        private int index;

        @Tag(51)
        private String patternTypeId;

        @Tag(52)
        private int machineIndex;

        @Tag(53)
        private String machineName;

        @Tag(54)
        private MachineType machineType;

        @Tag(55)
        private byte[] presetData;

        /**
         * Returns the local index of the groovebox part index.
         * <p>
         * Note: In most cases this will not match the
         * {@link #getMachineIndex()}, this is just the local index of the part
         * within the specific machine.
         */
        public int getIndex() {
            return index;
        }

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

        public byte[] getPresetData() {
            return presetData;
        }

        PartDescriptor() {
        }

        public PartDescriptor(int index, String patternTypeId, String machineName,
                MachineType machineType, byte[] presetData) {
            this.index = index;
            this.patternTypeId = patternTypeId;
            this.presetData = presetData;
            this.machineIndex = -1;
            this.machineName = machineName;
            this.machineType = machineType;
        }

        public PartDescriptor(int index, String patternTypeId, int machineIndex,
                String machineName, MachineType machineType, byte[] presetData) {
            this.index = index;
            this.patternTypeId = patternTypeId;
            this.machineIndex = machineIndex;
            this.machineName = machineName;
            this.machineType = machineType;
            this.presetData = presetData;
        }

        public PartDescriptor(PartDescriptor descriptor) {
            this.index = descriptor.getIndex();
            this.patternTypeId = descriptor.getPatternTypeId();
            this.machineIndex = descriptor.getMachineIndex();
            this.machineName = descriptor.getMachineName();
            this.machineType = descriptor.getMachineType();
            this.presetData = descriptor.getPresetData();
        }

        public ToneDescriptor createDescriptor() {
            return new ToneDescriptor(machineIndex, machineName, machineType);
        }

        @Override
        public String toString() {
            return "PartDescriptor[(" + index + ", " + machineIndex + ", " + machineType + ", "
                    + machineName + ")]";
        }
    }
}
