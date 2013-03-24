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

import com.teotigraphix.caustic.osc.SubSynthOscMessage;

/**
 * The {@link ISubSynthOsc1} interface allows for primary oscillator control.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISubSynthOsc1 extends IOscillatorComponent
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // bend
    //----------------------------------

    /**
     * The frequency bend.
     * 
     * @see SubSynthOscMessage#OSC_BEND
     */
    float getBend();

    /**
     * @see #getBend()
     * @see SubSynthOscMessage#OSC_BEND
     */
    void setBend(float value);

    //----------------------------------
    // fm
    //----------------------------------

    /**
     * The amount of frequency that is applied to osc1 using osc2 as the
     * modulator.
     * 
     * @see SubSynthOscMessage#OSC1_FM
     */
    float getFM();

    /**
     * @see #getFM()
     * @see SubSynthOscMessage#OSC1_FM
     */
    void setFM(float value);

    //----------------------------------
    // mix
    //----------------------------------

    /**
     * The signal mix between osc1 and osc2.
     * 
     * @see SubSynthOscMessage#OSC_MIX
     */
    float getMix();

    /**
     * @see #getMix()
     * @see SubSynthOscMessage#OSC_MIX
     */
    void setMix(float value);

    //----------------------------------
    // waveform
    //----------------------------------

    /**
     * The current waveform for the oscillator.
     * 
     * @see SubSynthOscMessage#OSC1_WAVEFORM
     */
    Waveform getWaveform();

    /**
     * @see #getWaveform()
     * @see SubSynthOscMessage#OSC1_WAVEFORM
     */
    void setWaveform(Waveform value);

    //--------------------------------------------------------------------------
    //
    // Public :: Enums
    //
    //--------------------------------------------------------------------------

    /**
     * The {@link ISubSynthOsc1} waveforms.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Waveform
    {

        /**
         * A sine wave (0).
         */
        SINE(0),

        /**
         * A saw tooth wave (1).
         */
        SAW(1),

        /**
         * A triangle wave (2).
         */
        TRIANGLE(2),

        /**
         * A square wave (3).
         */
        SQUARE(3),

        /**
         * A noise wave (4).
         */
        NOISE(4),

        /**
         * N/A. (5)
         */
        CUSTOM(5);

        private final int mValue;

        /**
         * Returns the integer value of the {@link Waveform}.
         */
        public int getValue()
        {
            return mValue;
        }

        Waveform(int value)
        {
            mValue = value;
        }

        /**
         * Returns a {@link Waveform} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static Waveform toType(Integer type)
        {
            for (Waveform result : values())
            {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static Waveform toType(Float type)
        {
            return toType(type.intValue());
        }
    }
}
