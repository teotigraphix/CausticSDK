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
import com.teotigraphix.caustic.osc.BasslineLFOMessage;

/**
 * The IBasslineLFO1 interface allows an {@link IBassline} to add LFO support
 * with a target.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IBasslineLFO1 extends ILFOComponent
{

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
     * @see BasslineLFOMessage#LFO_TARGET
     */
    LFOTarget getTarget();

    /**
     * @see #getTarget()
     * @see BasslineLFOMessage#LFO_TARGET
     */
    void setTarget(LFOTarget value);

    //----------------------------------
    // phase
    //----------------------------------

    /**
     * The phase of oscillation.
     * 
     * @see BasslineLFOMessage#LFO_PHASE
     */
    float getPhase();

    /**
     * @see #getPhase()
     * @see BasslineLFOMessage#LFO_PHASE
     */
    void setPhase(float value);

    /**
     * The {@link IBasslineLFO1#getTarget()} LFO value.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum LFOTarget
    {

        /**
         * No lfo on the bassline oscillator.
         */
        OFF(0),

        /**
         * Pulse Width Modulation.
         */
        PWM(1),

        /**
         * Voltage Control Frequency.
         */
        VCF(2),

        /**
         * Volume Controled Frequency.
         */
        VCA(3);

        private final int mValue;

        LFOTarget(int value)
        {
            mValue = value;
        }

        /**
         * Returns the int value for the lfo.
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
