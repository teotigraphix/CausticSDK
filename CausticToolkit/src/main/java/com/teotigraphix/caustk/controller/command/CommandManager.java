
package com.teotigraphix.caustk.controller.command;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.Dispatcher;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.command.ICommandHistory.OnClearComplete;
import com.teotigraphix.caustk.controller.command.ICommandHistory.OnFastForwardComplete;
import com.teotigraphix.caustk.controller.command.ICommandHistory.OnRewindComplete;

public class CommandManager implements ICommandManager {

    private ICaustkController controller;

    private CommandHistory commandHistory;

    private Map<String, Class<?>> commands = new HashMap<String, Class<?>>();

    private String applicationId;

    //----------------------------------
    // dispatcher
    //----------------------------------

    private IDispatcher dispatcher;

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    public CommandManager(ICaustkController controller) {
        this.controller = controller;
        applicationId = controller.getConfiguration().getApplicationId();
        dispatcher = new Dispatcher();
        commandHistory = new CommandHistory(dispatcher);

    }

    /**
     * Clears the {@link CommandHistory}.
     * 
     * @see OnClearComplete
     */
    public void clearHistory() {
        commandHistory.clear();
    }

    /**
     * An undo in the {@link ICommandHistory}.
     * 
     * @see OnRewindComplete
     */
    public int undo() {
        try {
            return commandHistory.rewind(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * An redo in the {@link ICommandHistory}.
     * 
     * @see OnFastForwardComplete
     */
    public int redo() {
        try {
            return commandHistory.forward(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adds a {@link ICommand} to the manager.
     * <p>
     * The <code>message</code> does not contain the controller head, the
     * controller/applicationId will automatically be appended.
     * 
     * @param message The message key that links to the {@link ICommand}.
     * @param command The Class that will be instantiated.
     */
    public void put(String message, Class<?> command) {
        final String controllerMessage = returnControllerMessage(message);
        if (commands.containsKey(controllerMessage))
            return;
        commands.put(controllerMessage, command);
    }

    public void remove(String message) {
        final String controllerMessage = returnControllerMessage(message);
        if (commands.containsKey(controllerMessage)) {
            commands.remove(controllerMessage);
        }
    }

    /**
     * Executes an {@link ICommand} against a registered message.
     * 
     * @param message The message without the controller/applicationId.
     * @param args Arguments to pass to the created {@link OSCMessage} that will
     *            be created.
     * @see #sendOSCCommand(OSCMessage)
     */
    public void execute(String message, Object... args) {
        try {
            OSCMessage result = OSCMessage.initialize(returnControllerMessage(message));
            for (Object value : args) {
                result.add(value);
            }
            sendOSCCommand(result);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capable of sending inter-application messages.
     * <p>
     * Not for use within the same app API.
     */
    public void sendOSCCommand(String message) throws CausticException {
        OSCMessage result = OSCMessage.initialize(message);
        sendOSCCommand(result);
    }

    /**
     * Executes an {@link ICommand} based on the message.
     * <p>
     * The message is forced to have the same {@link #applicationId} as this
     * instance. This method is not for inter-application communications, see
     * {@link #sendOSCCommand(String)}.
     * 
     * @param message The command message without the controller/applicationId
     * @throws CausticException
     */
    public void sendOSCCommand(OSCMessage message) throws CausticException {
        // This controller forces ALL messages to use it's root controller name
        // this will overwrite anything that is passed in
        //message.setController(applicationId);
        exectueCommand(message);
    }

    private void exectueCommand(OSCMessage message) {
        System.err.println("  OSC[" + message.toString() + "]");
        String commandString = message.toCommandString();
        Class<?> command = getCommand(commandString);
        // TODO should log these commands that cannot be found
        if (command == null)
            return;

        ICommand instance = null;

        try {
            instance = (ICommand)command.newInstance();
        } catch (IllegalAccessException e) {
            message.setException(e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            message.setException(e);
            e.printStackTrace();
        }

        // Exception thrown during instantiation (constructor)
        if (instance == null)
            return;

        if (instance instanceof IOSCCommand) {
            ((IOSCCommand)instance).setContext(new CommandContext(controller, message));
        }

        try {
            if (instance instanceof IUndoCommand) {
                // this calls execute and adds to history stack
                commandHistory.execute((IUndoCommand)instance);
            } else if (instance instanceof ICommand) {
                // normal execute()
                instance.execute();
            }
        } catch (CommandExecutionException e) {
            // if an undo command reaches here, it has NOT been added to history
            // TODO Figure out how controller will deal with CommandExecutionException
            //            Log.e("Controller[" + getControllerName() + "]", e.getMessage() + " Cause:"
            //                    + e.getCause().toString());
            e.printStackTrace();
            message.setException(e);
        }
    }

    private Class<?> getCommand(String command) {
        return commands.get(command);
    }

    private String returnControllerMessage(String message) {
        return "/" + applicationId + "/" + message;
    }

}
