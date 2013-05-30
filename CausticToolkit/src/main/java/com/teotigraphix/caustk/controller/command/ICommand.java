
package com.teotigraphix.caustk.controller.command;

public interface ICommand {

    /**
     * Perform an action
     * 
     * @throws CommandExecutionException An exception occurred during execution.
     */
    void execute();

}
