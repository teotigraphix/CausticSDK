
package com.teotigraphix.caustk.gs.machine.parts;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;

/*
 * This class will implement and wrap the Tone API for each machine.
 * 
 * It will allow the Machine's control user interface to implement
 * whatever it wants and access this class's dispatching and OSC action API.
 * 
 * The class has no user interface.
 */

/**
 * Holds the model and OSC actions for the {@link MachineControls} part.
 */
public class MachineSound extends MachineComponentPart {

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineSound(GrooveMachine grooveMachine) {
        super(grooveMachine);
    }

}
