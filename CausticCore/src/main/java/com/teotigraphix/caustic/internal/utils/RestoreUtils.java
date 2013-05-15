
package com.teotigraphix.caustic.internal.utils;

import com.teotigraphix.caustic.internal.output.OutputPanel;

public class RestoreUtils {

    public static void restoreOutputPanel(OutputPanel component) {
        component.setBPM(component.getBPM(true));
        component.setMode(component.getMode(true));
    }

}
