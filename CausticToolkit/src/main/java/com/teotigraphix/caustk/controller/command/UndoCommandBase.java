
package com.teotigraphix.caustk.controller.command;

public abstract class UndoCommandBase extends OSCCommandBase implements IUndoCommand {

    /**
     * Keeps track of whether this command has been executed, to prevent undoing
     * commands that have not been yet been executed.
     */
    protected boolean mHasExectued = false;

    public UndoCommandBase() {
    }

    @Override
    public void execute() {
        if (!mHasExectued) {
            try {
                doExecute();
            } catch (Exception e) {
                throw new CommandExecutionException("Problem with execute()", e);
            }
            mHasExectued = true;
            getContext().getDispatcher().trigger(new OnExecuteComplete(this));
        }
    }

    /**
     * Subclasses must override this function.
     */
    abstract protected void doExecute();

    @Override
    public void undo() {
        if (mHasExectued) {
            try {
                undoExecute();
            } catch (Exception e) {
                throw new CommandExecutionException("Problem with undo()", e);
            }
            mHasExectued = false;
            getContext().getDispatcher().trigger(new OnUndoExecuteComplete(this));
        }
    }

    @Override
    public void cancel() {
    }

    /**
     * Subclasses must override this function.
     * <p>
     * This function should undo whatever the doExecute command did.
     * </p>
     */
    abstract protected void undoExecute();

}
