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
 * The ISubSynthOsc2 interface allows for secondary oscillator control.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISubSynthOsc2 extends IOscillatorComponent
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    /**
     * The fine frequency tuning for osc2, in cents. Used to create de-tune
     * effects.
     * 
     * @see SubSynthOscMessage#OSC2_CENTS
     */
    int getCents();

    /**
     * @see #getCents()
     * @see SubSynthOscMessage#OSC2_CENTS
     */
    void setCents(int value);

    //----------------------------------
    // phase
    //----------------------------------

    /**
     * The phase shift applied to the signal before it is mixed in with osc1.
     * 
     * @see SubSynthOscMessage#OSC2_PHASE
     */
    float getPhase();

    /**
     * @see #getPhase()
     * @see SubSynthOscMessage#OSC2_PHASE
     */
    void setPhase(float value);

    //----------------------------------
    // octave
    //----------------------------------

    /**
     * The coarse frequency tuning for osc2, from -3 to +3 octaves.
     * 
     * @see SubSynthOscMessage#OSC2_OCTAVE
     */
    int getOctave();

    /**
     * @see #getOctave()
     * @see SubSynthOscMessage#OSC2_OCTAVE
     */
    void setOctave(int value);

    //----------------------------------
    // semis
    //----------------------------------

    /**
     * The frequency tuning for osc2, in semitones (regular notes on a
     * keyboard).
     * 
     * @see SubSynthOscMessage#OSC2_SEMIS
     */
    int getSemis();

    /**
     * @see #getSemis()
     * @see SubSynthOscMessage#OSC2_SEMIS
     */
    void setSemis(int value);

    //----------------------------------
    // waveForm
    //----------------------------------

    /**
     * The current waveform for the secondary oscillator.
     * 
     * @see SubSynthOscMessage#OSC2_WAVEFORM
     */
    WaveForm getWaveform();

    /**
     * @see #getWaveform()
     * @see SubSynthOscMessage#OSC2_WAVEFORM
     */
    void setWaveform(WaveForm value);

    /**
     * The {@link ISubSynthOsc2} waveforms.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum WaveForm
    {

        /**
         * Inactive oscillator.
         */
        NONE(0),

        /**
         * A sine wave (1).
         */
        SINE(1),

        /**
         * A saw wave (2).
         */
        SAW(2),

        /**
         * A triangle wave (3).
         */
        TRIANGLE(3),

        /**
         * A square wave (4).
         */
        SQUARE(4),

        /**
         * A noise wave (5).
         */
        NOISE(5),

        /**
         * A custom wave (6) <strong>N/A</strong>.
         */
        CUSTOM(6);

        private final int mValue;

        /**
         * Returns in integer value for the {@link WaveForm}.
         */
        public int getValue()
        {
            return mValue;
        }

        WaveForm(int value)
        {
            mValue = value;
        }

        /**
         * Returns a {@link Waveform} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static WaveForm toType(Integer type)
        {
            for (WaveForm result : values())
            {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static WaveForm toType(Float type)
        {
            return toType(type.intValue());
        }
    }
}
