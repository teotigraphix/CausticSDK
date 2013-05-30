
package com.teotigraphix.caustk.controller.command;

public interface IOSCCommand extends ICommand {

    CommandContext getContext();

    void setContext(CommandContext value);
}
