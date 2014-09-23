
package com.teotigraphix.caustk.controller.daw;

import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;

public class MachineBankProxy {

    private Model model;

    public MachineBankProxy(Model model) {
        this.model = model;
    }

    public PatternNode getSelectedPattern() {
        return getSelectedMachine().getSequencer().getSelectedPattern();
    }

    protected MachineNode getSelectedMachine() {
        return model.getRackNode().getSelectedMachine();
    }
}
