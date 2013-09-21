
package com.teotigraphix.caustk.gs.controller;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;

public class FunctionController implements IFunctionController {

    ICaustkController controller;

    @Inject
    public void setApplicationProvider(ICaustkApplicationProvider applicationProvider) {
        controller = applicationProvider.get().getController();
    }

    private Array<FunctionGroup> functionGroups = new Array<FunctionGroup>();

    @Override
    public Array<FunctionGroup> getFunctionGroups() {
        return functionGroups;
    }

    @Override
    public void setFunctionGroups(Array<FunctionGroup> value) {
        functionGroups = value;

        for (FunctionGroup functionGroup : functionGroups) {
            for (FunctionGroupItem item : functionGroup.getFunctions()) {
                String commandMessage = item.getFunction().getCommand();
                controller.getCommandManager().put(commandMessage, item.getCommand());
            }
        }
    }

    public FunctionController() {

    }

    @Override
    public void execute(IKeyFunction key) {
        String commandMessage = key.getCommand();
        controller.getCommandManager().execute(commandMessage);
    }
}
