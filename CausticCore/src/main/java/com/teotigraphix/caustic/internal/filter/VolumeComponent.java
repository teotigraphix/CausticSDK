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

package com.teotigraphix.caustic.internal.filter;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.filter.IVolumeComponent;
import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.IPCMSynth;
import com.teotigraphix.caustic.osc.VolumeMessage;

/**
 * The default implementation of the {@link IVolumeComponent} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class VolumeComponent extends MachineComponent implements IVolumeComponent {

    //--------------------------------------------------------------------------
    //
    // IVolumeComponent API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // out
    //----------------------------------

    private float mOut = 1.0f;

    @Override
    public float getOut() {
        return mOut;
    }

    public float getOut(boolean restore) {
        return VolumeMessage.VOLUME_OUT.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setOut(float value) {
        if (value == mOut)
            return;
        if (getDevice() instanceof IPCMSynth) {
            if (value < 0 || value > 8.0f)
                throw newRangeException(VolumeMessage.VOLUME_OUT.toString(), "0..8.0", value);
        } else {
            if (value < 0 || value > 2.0f)
                throw newRangeException(VolumeMessage.VOLUME_OUT.toString(), "0..2.0", value);
        }
        mOut = value;
        VolumeMessage.VOLUME_OUT.send(getEngine(), getMachineIndex(), value);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public VolumeComponent(IMachine machine) {
        super(machine);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putFloat(FilterConstants.ATT_OUT, mOut);
    }

    @Override
    public void paste(IMemento memento) {
        setOut(memento.getFloat(FilterConstants.ATT_OUT));
    }

    @Override
    public void restore() {
        setOut(getOut(true));
    }
}
