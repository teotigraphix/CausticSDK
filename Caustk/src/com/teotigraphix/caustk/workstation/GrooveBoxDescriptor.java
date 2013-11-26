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

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

/**
 * @author Michael Schmalle
 */
public class GrooveBoxDescriptor {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

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

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    GrooveBoxDescriptor() {
    }

    GrooveBoxDescriptor(GrooveBoxType grooveBoxType) {
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

}
