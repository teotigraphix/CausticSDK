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

package com.teotigraphix.caustk.workstation;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;

/**
 * @author Michael Schmalle
 */
public class PartDescriptor {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

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
     * Note: In most cases this will not match the {@link #getMachineIndex()},
     * this is just the local index of the part within the specific machine.
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

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    PartDescriptor() {
    }

    PartDescriptor(int index, String patternTypeId, String machineName, MachineType machineType,
            byte[] presetData) {
        this.index = index;
        this.patternTypeId = patternTypeId;
        this.presetData = presetData;
        this.machineIndex = -1;
        this.machineName = machineName;
        this.machineType = machineType;
    }

    PartDescriptor(int index, String patternTypeId, int machineIndex, String machineName,
            MachineType machineType, byte[] presetData) {
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
