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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.workstation.GrooveMachineDescriptor.PartDescriptor;

/**
 * Holds all {@link GrooveMachine}s in an application, the main controller for
 * all machines.
 * 
 * @author Michael Schmalle
 */
public class GrooveSet extends CaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private RackSet rackSet;

    @Tag(101)
    private Map<Integer, GrooveMachine> machines = new TreeMap<Integer, GrooveMachine>();

    @Tag(102)
    private int selectedMachineIndex;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return null;
    }

    //----------------------------------
    // rackSet
    //----------------------------------

    public RackSet getRackSet() {
        return rackSet;
    }

    //----------------------------------
    // machines
    //----------------------------------

    public Collection<GrooveMachine> getMachines() {
        return machines.values();
    }

    //----------------------------------
    // selectedMachineIndex
    //----------------------------------

    public GrooveMachine getSelectedMachine() {
        return getMachine(selectedMachineIndex);
    }

    public int getSelectedMachineIndex() {
        return selectedMachineIndex;
    }

    /**
     * @param value
     * @see OnGrooveStationChange
     * @see GrooveStationChangeKind#SelectedMachineIndex
     */
    public void setSelectedMachineIndex(int value) {
        if (value == selectedMachineIndex)
            return;
        int oldIndex = selectedMachineIndex;
        selectedMachineIndex = value;
        trigger(new OnGrooveStationChange(this, GrooveStationChangeKind.SelectedMachineIndex,
                selectedMachineIndex, oldIndex));
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    GrooveSet() {
    }

    GrooveSet(ComponentInfo info, RackSet rackSet) {
        setInfo(info);
        this.rackSet = rackSet;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public GrooveMachine getMachine(int machineIndex) {
        if (machineIndex > machines.size() - 1)
            return null;
        return machines.get(selectedMachineIndex);
    }

    public void addMachine(GrooveMachineDescriptor descriptor) {
        // create the Parts from the descriptor
        List<PartDescriptor> parts = descriptor.getParts();
        for (PartDescriptor partDescriptor : parts) {
            createPart(partDescriptor);
        }
    }

    private void createPart(PartDescriptor partDescriptor) {
        ToneDescriptor toneDescriptor = partDescriptor.createDescriptor();

    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                break;
            case Load:
                break;
            case Update:
                break;
            case Restore:
                break;
            case Disconnect:
                break;
            case Connect:
                break;
        }
    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    private void trigger(Object event) {
        rackSet.getComponentDispatcher().trigger(event);
    }

    public enum GrooveStationChangeKind {
        SelectedMachineIndex
    }

    public static class OnGrooveStationChange {

        private GrooveSet grooveSet;

        private GrooveStationChangeKind kind;

        private int index;

        private int oldIndex;

        public GrooveSet getGrooveStation() {
            return grooveSet;
        }

        public GrooveStationChangeKind getKind() {
            return kind;
        }

        /**
         * @see GrooveStationChangeKind#SelectedMachineIndex
         */
        public int getIndex() {
            return index;
        }

        /**
         * @see GrooveStationChangeKind#SelectedMachineIndex
         */
        public int getOldIndex() {
            return oldIndex;
        }

        public OnGrooveStationChange(GrooveSet grooveSet, GrooveStationChangeKind kind) {
            this.grooveSet = grooveSet;
            this.kind = kind;
        }

        public OnGrooveStationChange(GrooveSet grooveSet, GrooveStationChangeKind kind, int index,
                int oldIndex) {
            this.grooveSet = grooveSet;
            this.kind = kind;
            this.index = index;
            this.oldIndex = oldIndex;
        }
    }

}
