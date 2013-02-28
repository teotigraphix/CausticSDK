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

package com.teotigraphix.caustic.sequencer;

import com.teotigraphix.caustic.sequencer.data.TriggerData;
import com.teotigraphix.common.IPersist;

/**
 * The ITrigger interface represents a single trigger within an IPhrase.
 * <p>
 * A trigger is a selectable element that has a index, pitch, gate time and
 * velocity.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ITrigger extends IPersist {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // phrase
    //----------------------------------

    /**
     * Return the IPhrase parent.
     */
    IPhrase getPhrase();

    // void setPhrase(IPhrase value);

    //----------------------------------
    // index
    //----------------------------------

    /**
     * The index of the trigger in the IPhrase parent.
     */
    int getIndex();

    //----------------------------------
    // pitch
    //----------------------------------

    /**
     * The MIDI note pitch of the trigger.
     */
    int getPitch();

    //----------------------------------
    // velocity
    //----------------------------------

    /**
     * The velocity time of the trigger.
     */
    float getVelocity();

    //----------------------------------
    // velocity
    //----------------------------------

    /**
     * The flags for the trigger.
     */
    int getFlags();

    //----------------------------------
    // gate
    //----------------------------------

    /**
     * The gate time of the trigger.
     */
    float getGate();

    //----------------------------------
    // selected
    //----------------------------------

    /**
     * Whether the trigger is selected (true) or unselected (false).
     */
    boolean getSelected();

    //----------------------------------
    // data
    //----------------------------------

    /**
     * The data associated with the trigger.
     */
    TriggerData getData();
}
