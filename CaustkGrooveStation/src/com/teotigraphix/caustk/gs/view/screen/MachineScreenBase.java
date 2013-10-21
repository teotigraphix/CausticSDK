
package com.teotigraphix.caustk.gs.view.screen;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.libgdx.screen.ScreenBase;

public class MachineScreenBase extends ScreenBase implements IMachineScreen {

    private GrooveMachine machine;

    @Override
    public GrooveMachine getMachine() {
        return machine;
    }

    public void setMachine(GrooveMachine value) {
        machine = value;
    }

    public MachineScreenBase() {
    }

}
