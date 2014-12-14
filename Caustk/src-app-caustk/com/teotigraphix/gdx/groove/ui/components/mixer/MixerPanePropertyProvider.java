
package com.teotigraphix.gdx.groove.ui.components.mixer;

import com.badlogic.gdx.graphics.Color;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.gdx.controller.view.AbstractDisplay;

public interface MixerPanePropertyProvider {

    ICaustkRack getRack();

    boolean hasMaster();

    // StylesDefault.getMachineColor(machineNode.getType())
    Color getItemColor(int index);

    AbstractDisplay getDisplay();
}
