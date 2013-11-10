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
    private KeyboardMode keyboardMode = KeyboardMode.Off;

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
    // keyboardMode
    //----------------------------------

    public KeyboardMode getKeyboardMode() {
        return keyboardMode;
    }

    /**
     * @param value
     * @see OnMasterSystemChange
     * @see MasterSystemChangeKind#KeyboardMode
     */
    public void setKeyboardMode(KeyboardMode value) {
        if (value == keyboardMode)
            return;
        keyboardMode = value;
        trigger(new OnMasterSystemChange(this, MasterSystemChangeKind.KeyboardMode));
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

    public enum KeyboardMode {
        Off,

        Step,

        Key
    }

    public enum MasterSystemChangeKind {
        Record,

        ShiftMode,

        KeyboardMode
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

}
