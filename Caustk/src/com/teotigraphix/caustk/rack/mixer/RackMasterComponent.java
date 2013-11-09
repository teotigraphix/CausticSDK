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

package com.teotigraphix.caustk.rack.mixer;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.utils.ExceptionUtils;
import com.teotigraphix.caustk.workstation.CaustkComponent;
import com.teotigraphix.caustk.workstation.RackSet;

/**
 * @author Michael Schmalle
 */
public class RackMasterComponent extends CaustkComponent {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private transient IRack rack;

    CausticMessage getBypassMessage() {
        return null;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private RackSet rackSet;

    @Tag(51)
    private boolean bypass = false;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return null;
    }

    //----------------------------------
    // rackSet
    //----------------------------------

    public RackSet getRackSet() {
        return rackSet;
    }

    public void setRackSet(RackSet value) {
        rackSet = value;
    }

    //----------------------------------
    // rack
    //----------------------------------

    protected final IRack getRack() {
        return rack;
    }

    //----------------------------------
    // bypass
    //----------------------------------

    public boolean isBypass() {
        return bypass;
    }

    boolean isBypass(boolean restore) {
        return getBypassMessage().query(getRack()) == 1 ? true : false;
    }

    public void setBypass(boolean value) {
        if (bypass == value)
            return;
        bypass = value;
        getBypassMessage().send(getRack(), value ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public RackMasterComponent() {
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                rack = context.getRack();
                break;

            case Load:
                rack = context.getRack();
                restore();
                break;

            case Update:
                rack = context.getRack();
                getBypassMessage().send(getRack(), bypass ? 1 : 0);
                break;

            case Restore:
                setBypass(isBypass(true));
                break;

            case Connect:
                rack = context.getRack();
                break;

            case Disconnect:
                break;
        }
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
