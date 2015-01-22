
package com.teotigraphix.caustk.gdx.app.api;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gdx.app.AbstractProjectModelAPI;
import com.teotigraphix.caustk.gdx.app.ProjectModel;
import com.teotigraphix.caustk.gdx.app.ProjectState;
import com.teotigraphix.caustk.gdx.app.controller.command.CommandExecutionException;
import com.teotigraphix.caustk.gdx.app.controller.command.ICommand;

public class CommandAPI extends AbstractProjectModelAPI {

    public void execute(String message, Object... args) throws CommandExecutionException {
        getCommandManager().execute(message, args);
    }

    public int undo() throws CausticException {
        return getCommandManager().undo();
    }

    public int redo() throws CausticException {
        return getCommandManager().redo();
    }

    public void put(String message, Class<? extends ICommand> command) {
        getCommandManager().put(message, command);
    }

    public CommandAPI(ProjectModel projectModel) {
        super(projectModel);
    }

    @Override
    public void restore(ProjectState state) {
    }

}
