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

package com.teotigraphix.caustic.filter;

import com.teotigraphix.caustic.machine.IBassline;
import com.teotigraphix.caustic.osc.FilterMessage;

/**
 * The IBasslineFilter interface allows an {@link IBassline} to recieve envelope
 * modulation and decay.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IBasslineFilter extends IFilterComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // envMod
    //----------------------------------

    /**
     * @see FilterMessage#FILTER_ENVMOD
     */
    float getEnvMod();

    /**
     * @see getEnvMod()
     * @see FilterMessage#FILTER_ENVMOD
     */
    void setEnvMod(float value);

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * @see FilterMessage#FILTER_DECAY
     */
    float getDecay();

    /**
     * @see getEnvMod()
     * @see FilterMessage#FILTER_DECAY
     */
    void setDecay(float value);
}
