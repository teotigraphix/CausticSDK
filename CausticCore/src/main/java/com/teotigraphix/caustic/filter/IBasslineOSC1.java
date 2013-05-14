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
import com.teotigraphix.caustic.machine.IMachineComponent;
import com.teotigraphix.caustic.osc.BasslineOscMessage;

/**
 * The {@link IBasslineOSC1} API for the {@link IBassline#getOsc1()}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IBasslineOSC1 extends IMachineComponent
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // accent
    //----------------------------------

    /**
     * The amount of accent applied to the bassline notes.
     * 
     * @see BasslineOscMessage#ACCENT
     */
    float getAccent();

    /**
     * @see #getAccent()
     * @see BasslineOscMessage#ACCENT
     */
    void setAccent(float value);

    //----------------------------------
    // pulseWidth
    //----------------------------------

    /**
     * The pulse width modulation applied when the {@link Waveform#SQUARE} is
     * used.
     * 
     * @see BasslineOscMessage#PULSE_WIDTH
     */
    float getPulseWidth();

    /**
     * @see #getPulseWidth()
     * @see BasslineOscMessage#PULSE_WIDTH
     */
    void setPulseWidth(float value);

    //----------------------------------
    // tune
    //----------------------------------

    /**
     * The detune of the oscillator relitive to the notes played.
     * 
     * @see BasslineOscMessage#TUNE
     */
    int getTune();

    /**
     * @see #getTune()
     * @see BasslineOscMessage#TUNE
     */
    void setTune(int value);

    //----------------------------------
    // waveForm
    //----------------------------------

    /**
     * The current waveform for the oscillator.
     * 
     * @see BasslineOscMessage#WAVEFORM
     */
    Waveform getWaveForm();

    /**
     * @see #getWaveForm()
     * @see BasslineOscMessage#WAVEFORM
     */
    void setWaveForm(Waveform value);

    /**
     * The {@link IBasslineOSC1} waveforms.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Waveform
    {

        /**
         * A saw wave (0).
         */
        SAW(0),

        /**
         * A square wave (1).
         */
        SQUARE(1);

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
