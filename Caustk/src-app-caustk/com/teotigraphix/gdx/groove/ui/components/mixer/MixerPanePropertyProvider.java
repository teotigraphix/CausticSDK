
package com.teotigraphix.gdx.groove.ui.components.mixer;

import com.badlogic.gdx.graphics.Color;
import com.teotigraphix.caustk.controller.core.AbstractDisplay;

public interface MixerPanePropertyProvider {

    // StylesDefault.getMachineColor(machineNode.getType())
    Color getItemColor(int index);

    AbstractDisplay getDisplay();
}
