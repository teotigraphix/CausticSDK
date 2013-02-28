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

/**
 * The {@link ISynth} interface is the base interface for all Caustic tone
 * generator machines that can play MIDI notes.
 * 
 * @see ISubSynth
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISynth extends IMachine {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // synth
    //----------------------------------

    /**
     * Returns the playable synthesizer of the machine.
     * <p>
     * Using this component note on and off commands can be sent to the tone
     * generator of the synth.
     * 
     * @see ISynthComponent#getPolyphony()
     * @see ISynthComponent#noteOn(int)
     * @see ISynthComponent#noteOff(int)
     * @see ISynthComponent#notePreview(int, boolean)
     */
    ISynthComponent getSynth();
}
