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

import com.teotigraphix.caustic.machine.IPCMSynth;
import com.teotigraphix.caustic.osc.PCMSynthLFOMessage;

/**
 * The ILFO1PCMSynth interface allows a machine to add LFO support with a wave
 * form.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */

public interface IPCMSynthLFO1 extends ILFOComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // target
    //----------------------------------

    /**
     * The target of the LFO (Low Frequency Oscillator).
     * 
     * @see PCMSynthLFOMessage#LFO_TARGET
     */
    LFOTarget getTarget();

    /**
     * @see #getTarget()
     * @see PCMSynthLFOMessage#LFO_TARGET
     */
    void setTarget(LFOTarget value);

    //----------------------------------
    // waveForm
    //----------------------------------

    /**
     * The LFO oscillator waveform.
     * 
     * @see PCMSynthLFOMessage#LFO_WAVEFORM
     */
    WaveForm getWaveform();

    /**
     * @see #getWaveform()
     * @see PCMSynthLFOMessage#LFO_WAVEFORM
     */
    void setWaveForm(WaveForm value);

    /**
     * The LFO targets for the {@link IPCMSynth}.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum LFOTarget {

        /**
         * No LFO.
         */
        NONE(0),

        /**
         * Pitch oscillation.
         */
        PITCH(1),

        /**
         * Cutoff frequency oscillation.
         */
        CUTOFF(2),

        /**
         * Volume amplitude oscillation.
         */
        VOLUME(3);

        private final int mValue;

        LFOTarget(int value) {
            mValue = value;
        }

        /**
         * Returns int the value of the FLO.
         */
        public int getValue() {
            return mValue;
        }

        /**
         * Returns a {@link LFOTarget} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFOTarget toType(Integer type) {
            for (LFOTarget result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFOTarget#toType(Integer)
         */
        public static LFOTarget toType(Float type) {
            return toType(type.intValue());
        }
    }
}
