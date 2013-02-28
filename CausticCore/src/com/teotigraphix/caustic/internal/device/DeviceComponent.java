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

package com.teotigraphix.caustic.internal.device;

import com.teotigraphix.caustic.core.CausticMessage;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.device.IDeviceComponent;
import com.teotigraphix.caustic.internal.util.ExceptionUtils;
import com.teotigraphix.caustic.machine.IMachine;

/**
 * The default implementation of the {@link IDeviceComponent} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class DeviceComponent implements IDeviceComponent {

    //--------------------------------------------------------------------------
    //
    // IDeviceComponent API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // name
    //----------------------------------

    private String mName;

    @Override
    public String getName() {
        return mName;
    }

    protected void setName(String value) {
        mName = value;
    }

    //--------------------------------------------------------------------------
    //
    // IDeviceAware API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // device
    //----------------------------------

    private IDevice mDevice;

    @SuppressWarnings("unused")
    private int mDeviceIndex;

    private ICausticEngine mEngine;

    @Override
    public IDevice getDevice() {
        return mDevice;
    }

    public void setDevice(IDevice value) {
        mDevice = value;
        if (mDevice != null) {
            mEngine = mDevice.getEngine();
            if (mDevice instanceof IMachine)
                mDeviceIndex = ((IMachine)mDevice).getIndex();
        }
    }

    /**
     * Returns the {@link IMachine#getIndex()} for the current hosted device.
     * <p>
     * This is commonly used when sending OSC messages using the static
     * {@link CausticMessage} subclass constants.
     * <p>
     * Will be {@code null} if the {@link #getDevice()} is null.
     */
    protected int getDeviceIndex() {
        if (mDevice instanceof IMachine)
            return ((IMachine)mDevice).getIndex();
        return -1;
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // engine
    //----------------------------------

    protected ICausticEngine getEngine() {
        return mEngine;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public DeviceComponent(IDevice device) {
        setDevice(device);
    }

    //--------------------------------------------------------------------------
    //
    // IRestore API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void restore() {
    }

    @Override
    public void dispose() {
        mDevice = null;
        mEngine = null;
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Returns a new {@link IllegalArgumentException} for an error in OSC range.
     * 
     * @param control The OSC control involved.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }

}
