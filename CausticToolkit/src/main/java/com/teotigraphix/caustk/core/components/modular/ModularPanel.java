
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class ModularPanel extends ModularComponentBase {

    public ModularPanel() {
    }

    public ModularPanel(ICaustkController controller) {
        super(controller, 16);
    }

    @Override
    protected int getNumBays() {
        return -1;
    }

    public enum ModularPanelJack implements IModularJack {

        OutNoteCV(0),

        OutVelocity(1),

        OutModulation(2),

        InLeft(0),

        InRight(1),

        InVolumeModulation(2);

        private int value;

        public final int getValue() {
            return value;
        }

        ModularPanelJack(int value) {
            this.value = value;
        }

    }
}
