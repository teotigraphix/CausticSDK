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

package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.util.ExceptionUtils;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.IMachineComponent;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the IMachineComponent interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MachineComponent implements IMachineComponent
{

    //--------------------------------------------------------------------------
    //
    // IDeviceAware API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // device
    //----------------------------------

    private IDevice mDevice;

    private String mDeviceName;

    private ICausticEngine mEngine;

    @Override
    public IDevice getDevice()
    {
        return mDevice;
    }

    public void setDevice(IDevice value)
    {
        mDeviceName = null;
        mDevice = value;
        if (mDevice != null)
        {
            mDeviceName = mDevice.getId();
            mEngine = mDevice.getEngine();
        }
    }

    protected IMachine getMachine()
    {
        return (IMachine) mDevice;
    }

    protected int getMachineIndex()
    {
        return getMachine().getIndex();
    }

    protected String _getDeviceName()
    {
        return mDeviceName;
    }

    protected ICausticEngine getEngine()
    {
        return mEngine;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     * 
     * @param machine The component's parent IMachine.
     */
    public MachineComponent(IMachine machine)
    {
        setDevice(machine);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
    }

    @Override
    public void paste(IMemento memento)
    {
    }

    //--------------------------------------------------------------------------
    //
    // IRestore API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void restore()
    {
    }

    @Override
    public void dispose()
    {
        setDevice(null);
    }

    /**
     * Returns a new {@link IllegalArgumentException} for an error in OSC range.
     * 
     * @param control The OSC control involved.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected final RuntimeException newRangeException(String control,
            String range, Object value)
    {
        return ExceptionUtils.newRangeException(control, range, value);
    }
}
