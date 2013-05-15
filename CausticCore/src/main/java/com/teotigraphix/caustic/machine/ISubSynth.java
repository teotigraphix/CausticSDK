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

import com.teotigraphix.caustic.filter.IFilter;
import com.teotigraphix.caustic.filter.ISubSynthLFO1;
import com.teotigraphix.caustic.filter.ISubSynthLFO2;
import com.teotigraphix.caustic.filter.ISubSynthOsc1;
import com.teotigraphix.caustic.filter.ISubSynthOsc2;
import com.teotigraphix.caustic.filter.IVolumeEnvelope;

/**
 * The {@link ISubSynth} interface creates a subtractive synthesizer.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISubSynth extends ISynth {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // volume
    //----------------------------------

    /**
     * Returns the {@link ISubSynth}s implemented {@link IVolumeEnvelope}
     * component.
     */
    IVolumeEnvelope getVolume();

    //----------------------------------
    // filter
    //----------------------------------

    /**
     * Returns the {@link ISubSynth}s implemented {@link IFilter} component.
     */
    IFilter getFilter();

    //----------------------------------
    // osc1
    //----------------------------------

    /**
     * Returns the {@link ISubSynth}s implemented {@link ISubSynthOsc1}
     * component.
     */
    ISubSynthOsc1 getOsc1();

    //----------------------------------
    // osc2
    //----------------------------------

    /**
     * Returns the {@link ISubSynth}s implemented {@link ISubSynthOsc2}
     * component.
     */
    ISubSynthOsc2 getOsc2();

    //----------------------------------
    // lfo1
    //----------------------------------

    /**
     * Returns the {@link ISubSynth}s implemented {@link ISubSynthLFO1}
     * component.
     */
    ISubSynthLFO1 getLFO1();

    //----------------------------------
    // lof2
    //----------------------------------

    /**
     * Returns the {@link ISubSynth}s implemented {@link ISubSynthLFO2}
     * component.
     */
    ISubSynthLFO2 getLFO2();

}
