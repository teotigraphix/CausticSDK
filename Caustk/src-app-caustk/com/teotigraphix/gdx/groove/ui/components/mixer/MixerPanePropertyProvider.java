
package com.teotigraphix.gdx.groove.ui.components.mixer;

import com.badlogic.gdx.graphics.Color;
import com.teotigraphix.caustk.controller.core.AbstractDisplay;
import com.teotigraphix.caustk.core.ICaustkRack;

public interface MixerPanePropertyProvider {

    ICaustkRack getRack();

    boolean hasMaster();

    // StylesDefault.getMachineColor(machineNode.getType())
    Color getItemColor(int index);

    AbstractDisplay getDisplay();
}
