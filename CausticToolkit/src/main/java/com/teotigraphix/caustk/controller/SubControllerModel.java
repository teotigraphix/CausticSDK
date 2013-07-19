
package com.teotigraphix.caustk.controller;

import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.service.ISerializeService;

public class SubControllerModel implements ISerialize {

    transient ICaustkController controller;

    protected ICaustkController getController() {
        return controller;
    }

    /**
     * Called from {@link ISerializeService}, the controller gets set in
     * {@link #wakeup(ICaustkController)}.
     */
    public SubControllerModel() {
    }

    /**
     * Called when explicitly creating and instance in {@link SubControllerBase}
     * .
     * 
     * @param controller
     */
    public SubControllerModel(ICaustkController controller) {
        this.controller = controller;
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;

    }

}
