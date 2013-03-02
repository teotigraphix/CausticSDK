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

import roboguice.event.Observes;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.command.ICommand;
import com.teotigraphix.caustic.controller.OSCMessage;
import com.teotigraphix.caustic.core.CausticError;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.router.IRouter;
import com.teotigraphix.caustic.router.IRouter.OnRegisterControllerCommandsEvent;
import com.teotigraphix.caustic.router.IRouterClient;

public abstract class BaseRouterClient implements IRouterClient {

    private static final String TAG = "BaseRouterClient";

    @Inject
    IRouter router;

    public BaseRouterClient() {
        // /Controller/Device/Control [Data, ...]
        // /Tones/application/foocontrol [42 foo bar]
    }

    /**
     * Executes an {@link ICommand} if registered witht he {@link IRouter}.
     * 
     * @param message The {@link OSCMessage} to execute within the command
     *            stack.
     */
    @Override
    public void execute(OSCMessage message) {
        message.setController(router.getName());

        try {
            router.sendCommand(message);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a controller specific command, where the Controller and Device is
     * already set.
     * 
     * @param control The command to control.
     * @param args The parameters for the control.
     */
    @Override
    public OSCMessage sendCommand(String control, Object... args) throws CausticError {
        OSCMessage message = OSCMessage.create(getName(), control, args);
        execute(message);
        return message;
    }

    /**
     * Adds an {@link ICommand} to the {@link IRouter}s command stack using this
     * controller's {@link #getName()} as the Device and the
     * {@link IRouter#getName()} as the Controller in the message.
     * 
     * @param control The control to map a command to.
     * @param command The {@link ICommand} {@link Class} that will be executed
     *            when the controller recieves the event key.
     */
    protected void addCommand(String control, Class<?> command) {
        String message = "/" + router.getName() + "/" + getName() + "/" + control;
        router.put(message, command);
    }

    void onRegisterControllerCommandsEvent(@Observes OnRegisterControllerCommandsEvent event) {
        Log.d(TAG, "onRegisterControllerCommandsEvent() -> registerCommands()");
        registerCommands();
    }

    protected void registerCommands() {
    }

    @Override
    public void initialize() throws CausticException {
        // TODO Auto-generated method stub

    }
}
