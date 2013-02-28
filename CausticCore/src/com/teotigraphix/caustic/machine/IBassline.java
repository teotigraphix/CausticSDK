////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

import com.teotigraphix.caustic.effect.IBasslineDistortionUnit;
import com.teotigraphix.caustic.filter.IBasslineFilter;
import com.teotigraphix.caustic.filter.IBasslineLFO1;
import com.teotigraphix.caustic.filter.IBasslineOSC1;
import com.teotigraphix.caustic.filter.IVolumeComponent;

/**
 * The IBassline interface creates a 303 bassline machine API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IBassline extends ISynth {

    int NONE = 0;

    int SLIDE = 1;

    int ACCENT = 2;

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // distortion
    //----------------------------------

    /**
     * The bassline's volume component.
     * <p>
     */
    IVolumeComponent getVolume();

    //----------------------------------
    // distortion
    //----------------------------------

    /**
     * The bassline's distortion unit.
     * <p>
     */
    IBasslineDistortionUnit getDistortion();

    //----------------------------------
    // lfo1
    //----------------------------------

    /**
     * The bassline's lfo component.
     * <p>
     */
    IBasslineLFO1 getLFO1();

    //----------------------------------
    // filter
    //----------------------------------

    /**
     * The bassline's filter component.
     * <p>
     */
    IBasslineFilter getFilter();

    //----------------------------------
    // osc1
    //----------------------------------

    /**
     * The bassline's oscillator component.
     * <p>
     */
    IBasslineOSC1 getOsc1();
}
