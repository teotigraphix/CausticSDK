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

package com.teotigraphix.caustic.router;

import com.teotigraphix.caustic.command.ICommand;
import com.teotigraphix.caustic.command.ICommandHistory;
import com.teotigraphix.caustic.controller.OSCMessage;
import com.teotigraphix.caustic.core.CausticException;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IRouter {

    /**
     * Returns the unique controller name used with controller routers to
     * specify message destinations.
     */
    String getName();

    /**
     * Returns the controller's command history stack.
     */
    ICommandHistory getHistory();

    /**
     * Adds an {@link ICommand} to the controller using the message of the
     * router as the execution key.
     * 
     * @param message The ControllerMessage router execution key.
     * @param command The {@link ICommand} class.
     */
    void put(String message, Class<?> command);

    /**
     * Removes the {@link ICommand} from the controller.
     * 
     * @param message The String router execution key.
     */
    void remove(String message);

    /**
     * @param message
     * @throws CausticException
     */
    void sendCommand(OSCMessage message) throws CausticException;

    /**
     * Sends an {@link OSCMessage} to the controller that will be dispatched to
     * the correct {@link ICommand}.
     * 
     * @param message The execution message.
     * @throws CausticException
     */
    void sendCommand(String message) throws CausticException;

    public static class OnRegisterControllerCommandsEvent {

        private IRouter controller;

        public final IRouter getController() {
            return controller;
        }

        public OnRegisterControllerCommandsEvent(IRouter controller) {
            this.controller = controller;
        }
    }

    /**
     * Clients of caustic applications such as an application controller are
     * responsible for calling this method.
     * <p>
     * When the method is called, the {@link IRouter} will fire the
     * {@link OnRegisterControllerCommandsEvent}, this is when sub controllers
     * will register their commands against the main {@link IRouter} instance.
     */
    void initialize();

}
