
package com.teotigraphix.caustk.controller.command;

public abstract class OSCCommandBase implements IOSCCommand {

    private CommandContext context;

    @Override
    public CommandContext getContext() {
        return context;
    }

    @Override
    public void setContext(CommandContext value) {
        context = value;
    }

    public OSCCommandBase() {
    }

    @Override
    public abstract void execute();

}
