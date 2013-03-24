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

import com.teotigraphix.caustic.osc.SubSynthLFOMessage;

/**
 * The ILFOPrimary interface allows a machine to add LFO support with a wave
 * form.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISubSynthLFO1 extends ILFOComponent
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // waveForm
    //----------------------------------

    /**
     * The LFO oscillator waveform.
     * 
     * @see SubSynthLFOMessage#LFO1_WAVEFORM
     */
    WaveForm getWaveform();

    /**
     * @see #getWaveform()
     * @see SubSynthLFOMessage#LFO1_WAVEFORM
     */
    void setWaveForm(WaveForm value);

    //----------------------------------
    // target
    //----------------------------------

    /**
     * The target of the LFO (Low Frequency Oscillator).
     * 
     * @see SubSynthLFOMessage#LFO1_TARGET
     */
    LFOTarget getTarget();

    /**
     * @see #getTarget()
     * @see SubSynthLFOMessage#LFO1_TARGET
     */
    void setTarget(LFOTarget value);

    /**
     * The LFO targets for the SubSynth.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum LFOTarget
    {

        /**
         * No LFO.
         */
        NONE(0),

        /**
         * Primary oscillation.
         */
        OSC_PRIMARY(1),

        /**
         * Secondary oscillation.
         */
        OSC_SECONDARY(2),

        /**
         * Primary and secondary oscillation.
         */
        OSC_PRIMARY_SECONDARY(3),

        /**
         * Phase oscillation.
         */
        PHASE(4),

        /**
         * Cutoff oscillation.
         */
        CUTOFF(5),

        /**
         * Volume oscillation.
         */
        VOLUME(6),

        /**
         * Octave oscillation.
         */
        OCTAVE(7),

        /**
         * Semitone oscillation.
         */
        SEMIS(8);

        private final int mValue;

        LFOTarget(int value)
        {
            mValue = value;
        }

        /**
         * Returns the int value of the lfo.
         */
        public int getValue()
        {
            return mValue;
        }

        /**
         * Returns a {@link LFOTarget} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFOTarget toType(Integer type)
        {
            for (LFOTarget result : values())
            {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFOTarget#toType(Integer)
         */
        public static LFOTarget toType(Float type)
        {
            return toType(type.intValue());
        }
    }
}
