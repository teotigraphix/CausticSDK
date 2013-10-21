
package com.teotigraphix.caustk.gs.view.screen;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;

public class MachineMediatorBase extends ScreenMediator {

    private GrooveMachine machine;

    public GrooveMachine getMachine() {
        return machine;
    }

    public MachineMediatorBase() {
    }

    @Override
    public final void onRegister() {
    }

    @Override
    public void onInitialize(IScreen screen) {
        super.onInitialize(screen);

        IMachineScreen machineScreen = (IMachineScreen)screen;
        machine = machineScreen.getMachine();
    }
}
