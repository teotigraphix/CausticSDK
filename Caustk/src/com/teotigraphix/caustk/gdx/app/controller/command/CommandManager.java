////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.gdx.app.controller.command;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.gdx.app.ICaustkApplication;
import com.teotigraphix.caustk.gdx.app.controller.command.ICommandHistory.OnClearComplete;
import com.teotigraphix.caustk.gdx.app.controller.command.ICommandHistory.OnFastForwardComplete;
import com.teotigraphix.caustk.gdx.app.controller.command.ICommandHistory.OnRewindComplete;

public class CommandManager implements ICommandManager {

    private static final String SLASH = "/";

    @Inject
    Injector injector;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ICaustkApplication application;

    private EventBus eventBus;

    private CommandHistory commandHistory;

    private Map<String, Class<?>> commands = new HashMap<String, Class<?>>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cursor
    //----------------------------------

    @Override
    public int getCursor() {
        return commandHistory.getCursor();
    }

    //----------------------------------
    // commands
    //----------------------------------

    @Override
    public boolean canUndo() {
        return commandHistory.getCurrent() != null;
    }

    @Override
    public boolean canRedo() {
        return commandHistory.getCursor() < commandHistory.size();
    }

    @Override
    public Array<IUndoCommand> getCommands() {
        Array<IUndoCommand> result = new Array<IUndoCommand>();
        for (IUndoCommand command : commandHistory.getCommands()) {
            result.add(command);
        }
        return result;
    }

    //----------------------------------
    // eventBus
    //----------------------------------

    public EventBus getEventBus() {
        return eventBus;
    }

    @Inject
    public void setApplication(ICaustkApplication application) {
        this.application = application;
        eventBus = application.getEventBus();
        commandHistory = new CommandHistory(eventBus);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CommandManager() {
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Clears the
     * {@link com.teotigraphix.caustk.gdx.controller.command.CommandHistory}.
     * 
     * @see OnClearComplete
     */
    @Override
    public void clearHistory() {
        commandHistory.clear();
    }

    /**
     * An undo in the {@link ICommandHistory}.
     * 
     * @see OnRewindComplete
     */
    @Override
    public int undo() throws CausticException {
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
    @Override
    public int redo() throws CausticException {
        try {
            return commandHistory.forward(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adds a {@link com.teotigraphix.caustk.gdx.controller.command.ICommand} to
     * the manager.
     * <p>
     * The <code>message</code> does not contain the controller head, the
     * controller/applicationId will automatically be appended.
     * 
     * @param message The message key that links to the
     *            {@link com.teotigraphix.caustk.gdx.controller.command.ICommand}
     *            .
     * @param command The Class that will be instantiated.
     */
    @Override
    public void put(String message, Class<? extends ICommand> command) {
        final String controllerMessage = returnControllerMessage(message);
        if (commands.containsKey(controllerMessage))
            return;
        commands.put(controllerMessage, command);
    }

    @Override
    public void remove(String message) {
        final String controllerMessage = returnControllerMessage(message);
        if (commands.containsKey(controllerMessage)) {
            commands.remove(controllerMessage);
        }
    }

    public void execute(ICommand command) throws CommandExecutionException {
        executeInstance(command, null);
    }

    /**
     * Executes an
     * {@link com.teotigraphix.caustk.gdx.controller.command.ICommand} against a
     * registered message.
     * 
     * @param message The message without the controller/applicationId.
     * @param args Arguments to pass to the created
     *            {@link com.teotigraphix.caustk.gdx.controller.command.OSCMessage}
     *            that will be created.
     * @throws CommandExecutionException
     * @see #sendOSCCommand(com.teotigraphix.caustk.gdx.controller.command.OSCMessage)
     * @see com.teotigraphix.caustk.gdx.controller.command.CommandManager.CommandManagerRefreshEvent
     */
    @Override
    public void execute(String message, Object... args) throws CommandExecutionException {
        OSCMessage result = OSCMessage.initialize(returnControllerMessage(message));
        for (Object value : args) {
            result.add(value);
        }
        sendOSCCommand(result);
    }

    /**
     * Capable of sending inter-application messages.
     * <p>
     * Not for use within the same app API.
     * 
     * @throws CommandExecutionException
     * @see com.teotigraphix.caustk.gdx.controller.command.CommandManager.CommandManagerRefreshEvent
     */
    public void sendOSCCommand(String message) throws CommandExecutionException {
        OSCMessage result = OSCMessage.initialize(message);
        sendOSCCommand(result);
    }

    /**
     * Executes an
     * {@link com.teotigraphix.caustk.gdx.controller.command.ICommand} based on
     * the message.
     * <p>
     * The message is forced to have the same {@link #applicationId} as this
     * instance. This method is not for inter-application communications, see
     * {@link #sendOSCCommand(String)}.
     * 
     * @param message The command message without the controller/applicationId
     * @throws com.teotigraphix.caustk.core.CausticException
     * @throws CommandExecutionException
     * @see com.teotigraphix.caustk.gdx.controller.command.CommandManager.CommandManagerRefreshEvent
     */
    public void sendOSCCommand(OSCMessage message) throws CommandExecutionException {
        // This controller forces ALL messages to use it's root controller name
        // this will overwrite anything that is passed in
        //message.setController(applicationId);
        exectueCommand(message);
    }

    private void exectueCommand(OSCMessage message) throws CommandExecutionException {
        System.err.println("  OSC[" + message.toString() + "]");
        String commandString = message.toCommandString();
        Class<?> command = getCommand(commandString);

        if (command == null) {
            CaustkRuntime.getInstance().getLogger()
                    .warn("CommandManager", "Command not registered:" + commandString);
            return;
        }

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

        executeInstance(instance, message);
    }

    private void executeInstance(ICommand instance, OSCMessage message)
            throws CommandExecutionException {
        if (instance instanceof CommandBase)
            ((CommandBase)instance).setContext(new CommandContext(application, message));

        injector.injectMembers(instance);

        try {
            if (instance instanceof IUndoCommand) {
                // this calls execute and adds to history stack
                commandHistory.execute((IUndoCommand)instance);
            } else if (instance instanceof ICommand) {
                // normal execute()
                instance.execute();
            }
            eventBus.post(new CommandManagerRefreshEvent());
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    private Class<?> getCommand(String command) {
        return commands.get(command);
    }

    private String returnControllerMessage(String message) {
        final StringBuilder sb = new StringBuilder();
        sb.append(SLASH);
        sb.append(application.getApplicationId());
        sb.append(SLASH);
        sb.append(message);
        return sb.toString();
    }

    public static class CommandManagerRefreshEvent {
    }
}
