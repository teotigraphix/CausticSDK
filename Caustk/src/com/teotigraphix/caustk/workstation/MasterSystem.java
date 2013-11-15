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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;

/**
 * @author Michael Schmalle
 */
public class MasterSystem extends CaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private RackSet rackSet;

    @Tag(101)
    private boolean record;

    @Tag(102)
    private ShiftMode shiftMode = ShiftMode.Off;

    @Tag(103)
    private int selectedStateIndex = 0;

    @Tag(104)
    private Map<Integer, Integer> stateItemSelector = new HashMap<Integer, Integer>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return "System";
    }

    //----------------------------------
    // record
    //----------------------------------

    public boolean isRecord() {
        return record;
    }

    /**
     * @param value
     * @see OnMasterSystemChange
     * @see MasterSystemChangeKind#Record
     */
    public void setRecord(boolean value) {
        if (value == record)
            return;
        record = value;
        trigger(new OnMasterSystemChange(this, MasterSystemChangeKind.Record));
    }

    //----------------------------------
    // shiftMode
    //----------------------------------

    public ShiftMode getShiftMode() {
        return shiftMode;
    }

    /**
     * @param value
     * @see OnMasterSystemChange
     * @see MasterSystemChangeKind#ShiftMode
     */
    public void setShiftMode(ShiftMode value) {
        if (value == shiftMode)
            return;
        shiftMode = value;
        trigger(new OnMasterSystemChange(this, MasterSystemChangeKind.ShiftMode));
    }

    //----------------------------------
    // selectedStateIndex
    //----------------------------------

    /**
     * Returns the current state index.
     */
    public int getSelectedStateIndex() {
        return selectedStateIndex;
    }

    /**
     * Sets the current state index.
     * 
     * @param value An int state.
     */
    public void setSelectedStateIndex(int value) {
        selectedStateIndex = value;
    }

    /**
     * Sets the state item index of a particular state index.
     * 
     * @param stateIndex The state index.
     * @param stateItemIndex The state item index.
     */
    public void setSelectedStateIndex(int stateIndex, int stateItemIndex) {
        selectedStateIndex = stateIndex;
        stateItemSelector.put(stateIndex, stateItemIndex);
    }

    /**
     * Returns the selected state item index for the
     * {@link #getSelectedStateIndex()}.
     */
    public int getSelectedStateItemIndex() {
        return getStateItemIndex(selectedStateIndex);
    }

    /**
     * Returns the state item index for the state index.
     * 
     * @param stateIndex The state index.
     */
    public int getStateItemIndex(int stateIndex) {
        if (!stateItemSelector.containsKey(stateIndex))
            return 0;
        return stateItemSelector.get(stateIndex);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    MasterSystem() {
    }

    MasterSystem(RackSet rackSet) {
        this.rackSet = rackSet;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Writes the system state.
     * 
     * @throws CausticException
     * @throws IOException
     */
    public void write() throws CausticException, IOException {
        // TODO for now this should just call application.save()
        // for real, this is a two step proccess, where it asks;
        // do you really want to save, then press write again
        // XXX how to implement two step commands?
        // XXX This needs to fire a two step confirmation command for write
        rackSet._getRack().getController().getApplication().save();
    }

    /**
     * Global abstract value increment, depends solely on current state.
     * 
     * @see OnMasterSystemValueChange
     * @see MasterSystemValueChangeKind#Increment
     */
    public void incrementValue() {
        trigger(new OnMasterSystemValueChange(this, MasterSystemValueChangeKind.Increment));
    }

    /**
     * Global abstract value decrement, depends solely on current state.
     * 
     * @see OnMasterSystemValueChange
     * @see MasterSystemValueChangeKind#Decrement
     */
    public void decrementValue() {
        trigger(new OnMasterSystemValueChange(this, MasterSystemValueChangeKind.Decrement));
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

            case Connect:
                break;

            case Disconnect:
                break;
        }
    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    protected final void trigger(Object event) {
        rackSet.getComponentDispatcher().trigger(event);
    }

    public enum ShiftMode {
        Off,

        Shift
    }

    public enum MasterSystemChangeKind {
        Record,

        ShiftMode,

        Increment,

        Decrement
    }

    public enum MasterSystemValueChangeKind {

        Increment,

        Decrement
    }

    public static class OnMasterSystemChange {

        private MasterSystem masterSystem;

        private MasterSystemChangeKind kind;

        public MasterSystem getSystem() {
            return masterSystem;
        }

        public MasterSystemChangeKind getKind() {
            return kind;
        }

        public OnMasterSystemChange(MasterSystem masterSystem, MasterSystemChangeKind kind) {
            this.masterSystem = masterSystem;
            this.kind = kind;
        }
    }

    public static class OnMasterSystemValueChange {

        private MasterSystem masterSystem;

        private MasterSystemValueChangeKind kind;

        public MasterSystem getSystem() {
            return masterSystem;
        }

        public MasterSystemValueChangeKind getKind() {
            return kind;
        }

        public OnMasterSystemValueChange(MasterSystem masterSystem, MasterSystemValueChangeKind kind) {
            this.masterSystem = masterSystem;
            this.kind = kind;
        }
    }

}
