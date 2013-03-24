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

package com.teotigraphix.caustic.machine;

import com.teotigraphix.caustic.osc.SynthMessage;

/**
 * The {@link ISynthComponent} interface gives an {@link IMachine} the ability
 * to become a MIDI tone generator capable of playing multiple MIDI notes.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISynthComponent extends IMachineComponent
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // polyphony
    //----------------------------------

    /**
     * The number of simultaneous voices (1-16).
     * 
     * @see SynthMessage#POLYPHONY
     */
    int getPolyphony();

    /**
     * @see #getPolyphony()
     * @see SynthMessage#POLYPHONY
     */
    void setPolyphony(int value);

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Triggers a note on in the synthesizer.
     * 
     * @param pitch A int MIDI note to turn on.
     */
    void noteOn(int pitch);

    /**
     * Triggers a note on in the synthesizer with velocity.
     * 
     * @param pitch A int MIDI note to turn on.
     * @param velocity A float between (0..1).
     */
    void noteOn(int pitch, float velocity);

    /**
     * Triggers a note off in the synthesizer.
     * 
     * @param pitch A int MIDI note to turn off.
     */
    void noteOff(int pitch);

    /**
     * Previews a sample for the IPCMSynth and IBeatbox.
     * 
     * @param pitch A int MIDI note to turn off.
     * @param oneshot Whether to play once or loop the sample.
     */
    void notePreview(int pitch, boolean oneshot);
}
