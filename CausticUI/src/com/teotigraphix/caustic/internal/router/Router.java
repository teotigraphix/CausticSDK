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

package com.teotigraphix.caustic.internal.router;

import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.inject.ContextSingleton;
import android.app.Activity;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.command.CommandExecutionException;
import com.teotigraphix.caustic.command.ICommand;
import com.teotigraphix.caustic.command.ICommandHistory;
import com.teotigraphix.caustic.command.IOSCCommand;
import com.teotigraphix.caustic.command.IUndoCommand;
import com.teotigraphix.caustic.controller.OSCMessage;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.router.IOSCAware;
import com.teotigraphix.caustic.router.IRouter;
import com.teotigraphix.caustic.song.IWorkspace;

/**
 * The default implementation of the {@link IRouter} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
@ContextSingleton
public class Router implements IRouter {

    private static final String TAG = "Controller";

    private Map<String, Class<?>> commands = new HashMap<String, Class<?>>();

    @Inject
    ICommandHistory history;

    @Inject
    IWorkspace workspace;

    @Inject
    Activity activity;

    @Override
    public ICommandHistory getHistory() {
        return history;
    }

    //--------------------------------------------------------------------------
    // 
    //  IController API :: Properties
    // 
    //--------------------------------------------------------------------------

    @Override
    public String getName() {
        return workspace.getApplicationName();
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public Router() {
    }

    //--------------------------------------------------------------------------
    // 
    //  IController API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        workspace.getEventManager().fire(new OnRegisterControllerCommandsEvent(this));
    }

    @Override
    public void sendCommand(String message) throws CausticException {
        sendCommand(OSCMessage.initialize(message));
    }

    @Override
    public void sendCommand(OSCMessage message) throws CausticException {
        // This controller forces ALL messages to use it's root controller name
        // this will overwrite anything that is passed in, this may already be set
        message.setController(getName());

        Log.d(TAG, message.toString());

        exectueCommand(message);
    }

    @Override
    public void put(String message, Class<?> command) {
        // TODO Log an error if the key already exists
        if (commands.containsKey(message))
            return;

        commands.put(message, command);
    }

    @Override
    public void remove(String message) {
        if (commands.containsKey(message)) {
            commands.remove(message);
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  Private :: Methods
    // 
    //--------------------------------------------------------------------------

    private void exectueCommand(OSCMessage message) {
        String commandString = message.toCommandString();
        Class<?> command = getCommand(commandString);

        if (command == null) {
            Log.e(TAG, "Command not found:[" + commandString + "]");
            return;
        }

        ICommand instance = null;

        try {
            instance = (ICommand)command.newInstance();
        } catch (IllegalAccessException e) {
            message.setException(e);
            Log.e(TAG, "Instance IllegalAccessException exception", e);
            return;
        } catch (InstantiationException e) {
            message.setException(e);
            Log.e(TAG, "Instance InstantiationException exception", e);
            return;
        }

        RoboGuice.injectMembers(activity, instance);

        if (instance instanceof IOSCAware) {
            ((IOSCAware)instance).setMessage(message);
        }

        try {
            if (instance instanceof IUndoCommand) {
                // this calls execute and adds to history stack
                history.execute((IUndoCommand)instance);
            } else if (instance instanceof IOSCCommand) {
                ((IOSCCommand)instance).execute(message);
            } else if (instance instanceof ICommand) {
                // normal execute()
                instance.execute();
            }
        } catch (CommandExecutionException e) {
            // if an undo command reaches here, it has NOT been added to history
            // TODO Figure out how controller will deal with CommandExecutionException
            Log.e("Router[" + getName() + "]",
                    e.getMessage() + " Cause:" + e.getCause().toString(), e);
            e.printStackTrace();
            message.setException(e);
        }
    }

    private Class<?> getCommand(String command) {
        return commands.get(command);
    }

}
