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

import com.teotigraphix.caustic.machine.IMachineComponent;
import com.teotigraphix.caustic.osc.FilterMessage;

/**
 * The {@link IPitchTuner} interface allows a machine to adjust pitch settings
 * such as octave, semitones and cents of a sample.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPitchTuner extends IMachineComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    /**
     * The amount of cents shift applied to the sample.
     * 
     * @see FilterMessage#PITCH_CENTS
     */
    int getCents();

    /**
     * @see #getCents()
     * @see FilterMessage#PITCH_CENTS
     */
    void setCents(int value);

    //----------------------------------
    // octave
    //----------------------------------

    /**
     * The amount of octave shift applied to the sample.
     * 
     * @see FilterMessage#PITCH_OCTAVE
     */
    int getOctave();

    /**
     * @see #getOctave()
     * @see FilterMessage#PITCH_OCTAVE
     */
    void setOctave(int value);

    //----------------------------------
    // semis
    //----------------------------------

    /**
     * The amount of semitone shift applied to the sample.
     * 
     * @see FilterMessage#PITCH_SEMIS
     */
    int getSemis();

    /**
     * @see #getSemis()
     * @see FilterMessage#PITCH_SEMIS
     */
    void setSemis(int value);
}
