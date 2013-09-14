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

package com.teotigraphix.caustk.gs.machine;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.gs.machine.part.MachineControls;
import com.teotigraphix.caustk.gs.machine.part.MachineFooter;
import com.teotigraphix.caustk.gs.machine.part.MachineHeader;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer;
import com.teotigraphix.caustk.gs.machine.part.MachineSound;
import com.teotigraphix.caustk.gs.machine.part.MachineSystem;
import com.teotigraphix.caustk.gs.machine.part.MachineTransport;

public abstract class GrooveMachine {

    private MachineSound machineSound;

    public MachineSound getMachineSound() {
        return machineSound;
    }

    void setMachineSound(MachineSound value) {
        machineSound = value;
    }

    //----------------------------------
    // machineHeader
    //----------------------------------

    private MachineHeader machineHeader;

    public MachineHeader getMachineHeader() {
        return machineHeader;
    }

    void setMachineHeader(MachineHeader value) {
        machineHeader = value;
    }

    //----------------------------------
    // machineSystem
    //----------------------------------

    private MachineSystem machineSystem;

    public MachineSystem getMachineSystem() {
        return machineSystem;
    }

    public void setMachineSystem(MachineSystem value) {
        machineSystem = value;
    }

    //----------------------------------
    // machineTransport
    //----------------------------------

    private MachineTransport machineTransport;

    public MachineTransport getMachineTransport() {
        return machineTransport;
    }

    public void setMachineTransport(MachineTransport value) {
        machineTransport = value;
    }

    //----------------------------------
    // machineSequencer
    //----------------------------------

    private MachineSequencer machineSequencer;

    public MachineSequencer getMachineSequencer() {
        return machineSequencer;
    }

    public void setMachineSequencer(MachineSequencer value) {
        machineSequencer = value;
    }

    //----------------------------------
    // machineControls
    //----------------------------------

    private MachineControls machineControls;

    public MachineControls getMachineControls() {
        return machineControls;
    }

    public void setMachineControls(MachineControls value) {
        machineControls = value;
    }

    //----------------------------------
    // machineFooter
    //----------------------------------

    private MachineFooter machineFooter;

    public MachineFooter getMachineFooter() {
        return machineFooter;
    }

    public void setMachineFooter(MachineFooter value) {
        machineFooter = value;
    }

    //----------------------------------
    // machineFooter
    //----------------------------------

    private ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public void setController(ICaustkController value) {
        controller = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public GrooveMachine() {
    }

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    public abstract void createParts();

}
