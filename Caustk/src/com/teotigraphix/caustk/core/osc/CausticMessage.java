////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.core.osc;

import com.teotigraphix.caustk.core.ICausticEngine;

/**
 * The base class for CausticCore messages.
 * <p>
 * The sublcasses of the {@link CausticMessage} define the OSC API while
 * allowing a type API for sending and querying the CausticCore.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class CausticMessage {

    private static final String MESSAGE_POSTFIX = "}";

    private static final String MESSAGE_PREFIX = "${";

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // tokenMessage
    //----------------------------------

    private String tokenMessage;

    /**
     * Returns the message with tokens.
     * <p>
     * Example: <code>/caustic/outputpanel/bpm ${0}</code>.
     */
    public String getTokenMessage() {
        return tokenMessage;
    }

    //----------------------------------
    // message
    //----------------------------------

    private String message;

    /**
     * Returns the trimmed message without tokens.
     * <p>
     * Example: <code>/caustic/outputpanel/bpm</code>.
     */
    public String getMessage() {
        return message;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CausticMessage(String tokenMessage) {
        this.tokenMessage = tokenMessage;
        this.message = tokenMessage.split(" ")[0];
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Sends a message to the CausticCore using the args passed to be replaced
     * in the message.
     * 
     * @param engine The core engine.
     * @param args The arguments to be replaced with in the message.
     * @return A float value if the CausticCore returned
     * @see ICausticEngine#sendMessage(String)
     */
    public final float send(ICausticEngine engine, Object... args) {
        final StringBuffer sb = new StringBuffer();
        String result = this.tokenMessage;
        int i = 0;
        for (Object arg : args) {
            sb.append(MESSAGE_PREFIX);
            sb.append(i++);
            sb.append(MESSAGE_POSTFIX);
            result = result.replace(sb.toString(), arg.toString());
            sb.setLength(0);
        }
        return engine.sendMessage(result);
    }

    /**
     * Queries a message to the CausticCore using the args passed to be replaced
     * in the message.
     * 
     * @param engine The core engine.
     * @param args The arguments to be replaced with in the message.
     * @return A String value of the query.
     * @see ICausticEngine#sendMessage(String)
     */
    public final float query(ICausticEngine engine, Object... args) {
        final StringBuffer sb = new StringBuffer();
        String result = message;
        int i = 0;
        for (Object arg : args) {
            sb.append(MESSAGE_PREFIX);
            sb.append(i++);
            sb.append(MESSAGE_POSTFIX);
            result = result.replace(sb.toString(), arg.toString());
            sb.setLength(0);
        }
        return engine.sendMessage(result);
    }

    /**
     * Queries a message to the CausticCore using the args passed to be replaced
     * in the message.
     * 
     * @param engine The core engine.
     * @param args The arguments to be replaced with in the message.
     * @return A String value of the query.
     * @see ICausticEngine#queryMessage(String)
     */
    public final String queryString(ICausticEngine engine, Object... args) {
        final StringBuffer sb = new StringBuffer();
        String result = this.tokenMessage;
        int i = 0;
        for (Object arg : args) {
            sb.append(MESSAGE_PREFIX);
            sb.append(i++);
            sb.append(MESSAGE_POSTFIX);
            result = result.replace(sb.toString(), arg.toString());
            sb.setLength(0);
        }
        return engine.queryMessage(result);
    }

    @Override
    public String toString() {
        return tokenMessage;
    }
}
