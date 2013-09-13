
package com.teotigraphix.caustk.gs.machine.parts;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;

/**
 * Abstract base class for all machine parts.
 */
public abstract class MachineComponentPart {

    //----------------------------------
    // grooveMachine
    //----------------------------------

    private GrooveMachine grooveMachine;

    /**
     * Returns the {@link GrooveMachine} owner of this part.
     */
    public GrooveMachine getGrooveMachine() {
        return grooveMachine;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineComponentPart(GrooveMachine grooveMachine) {
        this.grooveMachine = grooveMachine;
    }

}
