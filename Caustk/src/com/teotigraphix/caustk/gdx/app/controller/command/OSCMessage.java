////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The OSCMessage contains the data used to contact the Caustic Core with device
 * messaging.
 * <p>
 * [controller]/[device]/[control] [...params]
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class OSCMessage {

    private static final String SPACE = " ";

    //--------------------------------------------------------------------------
    // 
    //  Public :: Variables
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  exception
    //----------------------------------

    private Exception mException;

    public final Exception getException() {
        return mException;
    }

    public final void setException(Exception e) {
        mException = e;
    }

    public final boolean hasException() {
        return mException != null;
    }

    //----------------------------------
    //  instance
    //----------------------------------

    Object instance;

    /**
     * The actual IDevice|IDeviceComponent that is sending the message.
     * (Optional)
     */
    public final Object getInstance() {
        return instance;
    }

    public final void setInstance(Object value) {
        instance = value;
    }

    //----------------------------------
    //  controller
    //----------------------------------

    String mController;

    /**
     * The controller name.
     */
    public String getController() {
        return mController;
    }

    public void setController(String value) {
        mController = value;
    }

    //----------------------------------
    //  device
    //----------------------------------

    String device;

    /**
     * The String IDevice name.
     */
    public final String getDevice() {
        return device;
    }

    //----------------------------------
    //  control
    //----------------------------------

    String control;

    /**
     * The IDevice control name.
     */
    public final String getControl() {
        return control;
    }

    //----------------------------------
    //  data
    //----------------------------------

    Object data;

    /**
     * The arbitrary data based on the specific device, component and control
     * involved in the message.
     */
    public final Object getData() {
        return data;
    }

    //----------------------------------
    //  result
    //----------------------------------

    public float result;

    private Object mResult;

    /**
     * The result of the message call if any.
     * <p>
     * Once the event is dispatched, if the data variable of the event is not
     * undefined, the core will update the result after the message returns.
     * </p>
     */
    public final Object getResult() {
        return mResult;
    }

    public final void setResult(Object value) {
        mResult = value;
    }

    //----------------------------------
    //  parameters
    //----------------------------------

    List<String> parameters;

    /**
     * The parameter list if any.
     */
    public final List<String> getParameters() {
        // lazy init
        //		if (mParameters == null && data != null) {
        //			StrTokenizer st = new StrTokenizer(data.toString(), ' ', '"');
        //			mParameters = st.getTokenList();
        //		}
        return parameters;
    }

    /**
     * Returns a String parameter if exists, <code>null</code> if the index is
     * out of bounds.
     * 
     * @param index The parameter index.
     */
    public String getParameter(int index) {
        if (!hasParameter(index))
            return null;
        return parameters.get(index);
    }

    public boolean hasParameter(int index) {
        return index < parameters.size();
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    /**
     * Creates a new OSCMessage message event.
     * 
     * @param device A String IDevice name.
     * @param control An IDevice control name.
     * @param data The arbitrary data based on the specific device and control
     *            involved in the message.
     */
    public OSCMessage(String device, String control, Object data) {
        this.device = device;
        this.control = control;
        this.data = data;
    }

    //--------------------------------------------------------------------------
    // 
    //  Public :: Methods
    // 
    //--------------------------------------------------------------------------

    /**
     * Adds a parameter to the parameters collection.
     * 
     * @param value The value to add.
     * @return
     */
    public final OSCMessage add(Object value) {
        if (parameters == null) {
            parameters = new ArrayList<String>();
            data = null;
        }
        parameters.add(value.toString());
        if (data != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(data);
            sb.append(SPACE);
            sb.append(value.toString());
            data = sb.toString();
        } else {
            data = value.toString();
        }

        return this;
    }

    /**
     * Creates and returns the constructed OSC message for the core.
     */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("/");
        result.append(mController != null ? mController : "caustic");
        result.append("/");
        result.append(device);
        result.append("/");
        result.append(control);

        if (data != null) {
            result.append(" ");
            result.append(data);
        }

        return result.toString();
    }

    public String toCommandString() {
        StringBuffer result = new StringBuffer();
        result.append("/");
        result.append(getController());
        result.append("/");
        result.append(getDevice());
        result.append("/");
        result.append(getControl());
        // no data, just raw command
        return result.toString();
    }

    /**
     * Initializes a string message and returns a new {@link OSCMessage} with
     * populated values from the OSC string.
     * 
     * @param message The String OSC message to digest.
     * @return A new {@link OSCMessage} based on the digested String message.
     */
    public static OSCMessage initialize(String message) {
        StringTokenizer t = new StringTokenizer(message + "\n");
        String command = t.nextToken();
        String data = null;
        try {
            data = t.nextToken("\n").trim();
        } catch (Exception e) {
            // TODO: handle exception
        }

        t = new StringTokenizer(command);
        String base = t.nextToken("/");
        String device = t.nextToken("/");
        String control = t.nextToken("/");

        ArrayList<String> datas = new ArrayList<String>();
        if (data != null) {
            t = new StringTokenizer(data);
            while (t.hasMoreTokens()) {
                String token = t.nextToken();
                datas.add(token);
            }
        }

        OSCMessage result = OSCMessage.create(device, control, data);
        result.mController = base;
        result.parameters = datas;

        return result;
    }

    //--------------------------------------------------------------------------
    // 
    //  Public Class :: Methods
    // 
    //--------------------------------------------------------------------------

    /**
     * Creates a new OSCMessage message event.
     * 
     * @param device A String IDevice name.
     * @param control An IDevice control name.
     * @param data The arbitrary data based on the specific device and control
     *            involved in the message.
     * @return A new OSCMessage.
     */
    public final static OSCMessage create(String device, String control, Object data) {
        return new OSCMessage(device, control, data);
    }

    public final static OSCMessage create(String device, String control) {
        return new OSCMessage(device, control, null);
    }

}
