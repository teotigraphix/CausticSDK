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

package com.teotigraphix.caustk.rack.tone;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.utils.ExceptionUtils;

/**
 * The {@link RackToneComponent} is a sub component held on a {@link RackTone}.
 * <p>
 * Using the {@link RackTone#getComponent(Class)} will return the component that was
 * registered under the class type API passed, null if the component dosn't
 * exist.
 * 
 * @author Michael Schmalle
 */
public abstract class RackToneComponent implements IRestore {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private RackTone rackTone;

    //----------------------------------
    // tone
    //----------------------------------

    public RackTone getTone() {
        return rackTone;
    }

    public void setTone(RackTone value) {
        rackTone = value;
    }

    protected final int getToneIndex() {
        return rackTone.getMachineIndex();
    }

    protected final ICausticEngine getEngine() {
        return rackTone.getEngine();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public RackToneComponent() {
    }

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
