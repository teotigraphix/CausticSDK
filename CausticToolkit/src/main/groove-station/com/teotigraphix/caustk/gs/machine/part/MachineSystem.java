
package com.teotigraphix.caustk.gs.machine.part;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;

/*
 * - Master volume - correlates to the machine's mixer volume
 * - Digital display
 * - Value Dial
 * - SystemStateMatrix
 * - Write button
 */

/**
 * Holds all controls for system operations.
 */
public class MachineSystem extends MachineComponentPart {

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineSystem(GrooveMachine grooveMachine) {
        super(grooveMachine);
    }

}
