
package com.teotigraphix.caustk.controller.command;

import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IControllerAPI;

public class CommandContext {

    private final ICaustkController controller;

    public <T extends IControllerAPI> T api(Class<T> clazz) {
        return controller.api(clazz);
    }

    private final OSCMessage message;

    public OSCMessage getMessage() {
        return message;
    }

    public IDispatcher getDispatcher() {
        return controller.getDispatcher();
    }

    public CommandContext(ICaustkController controller, OSCMessage message) {
        this.controller = controller;
        this.message = message;
    }

}
