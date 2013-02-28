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

package com.teotigraphix.caustic.filter;

import com.teotigraphix.caustic.machine.IMachineComponent;
import com.teotigraphix.caustic.osc.FilterMessage;

/**
 * The {@link IFilterComponent} interface is the base for all filters.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IFilterComponent extends IMachineComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    /**
     * The frequency at which the filter starts to cut the signal.
     * 
     * @see FilterMessage#FILTER_CUTOFF
     */
    float getCutoff();

    /**
     * @see #getCutoff()
     * @see FilterMessage#FILTER_CUTOFF
     */
    void setCutoff(float value);

    //----------------------------------
    // resonance
    //----------------------------------

    /**
     * The amount of resonance produced by the filter.
     * <p>
     * Resonance is defined by small peaks at harmonic frequencies to the cutoff
     * frequency.
     * 
     * @see FilterMessage#FILTER_RESONANCE
     */
    float getResonance();

    /**
     * @see #getResonance()
     * @see FilterMessage#FILTER_RESONANCE
     */
    void setResonance(float value);
}
