
package com.teotigraphix.caustk.controller.command;



public interface IUndoCommand extends ICommand {

    /**
     * Reverse the performed action
     * 
     * @throws CommandExecutionException An exception occurred during undo.
     */
    void undo();

    void cancel();

    public static class CommandEvent {

        public IUndoCommand command;

        public CommandEvent(IUndoCommand command) {
            this.command = command;
        }
    }

    public static class OnExecuteComplete extends CommandEvent {
        public OnExecuteComplete(IUndoCommand command) {
            super(command);
        }
    }

    public static class OnUndoExecuteComplete extends CommandEvent {
        public OnUndoExecuteComplete(IUndoCommand command) {
            super(command);
        }
    }

    public static class OnExecuteCanceled extends CommandEvent {
        public OnExecuteCanceled(IUndoCommand command) {
            super(command);
        }
    }

}
