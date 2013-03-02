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

import com.teotigraphix.caustic.controller.OSCMessage;
import com.teotigraphix.caustic.core.CausticError;
import com.teotigraphix.caustic.core.CausticException;

/**
 * The router API allows classes to register commands to the main
 * {@link IRouter} using {@link OSCMessage}s.
 * <p>
 * When the controller receives a message it will fin a corresponding command
 * create, inject and execute it.
 * </p>
 */
public interface IRouterClient {

    /**
     * Initializes the router, typically called before any user interface has
     * been created.
     * 
     * @throws CausticException Initialization problem in the
     *             {@link IRuntimeService}.
     */
    void initialize() throws CausticException;

    /**
     * The String unique router id that is tagged after the controller id in and
     * OSC message.
     * 
     * @see OSCMessage#getDevice()
     */
    String getName();

    /**
     * Sends an {@link OSCMessage} through the router.
     * 
     * @param control The router control name.
     * @param params Any parameters for the message.
     * @throws CausticException
     */
    //OSCMessage sendOSCCommand(String control, Object... params) throws CausticException;

    void execute(OSCMessage message);

    OSCMessage sendCommand(String control, Object... args) throws CausticError;
}
