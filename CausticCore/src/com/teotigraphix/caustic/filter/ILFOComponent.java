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

/**
 * The ILFOComponent interface allows a machine to add LFO support.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ILFOComponent extends IMachineComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * The depth of oscillation.
     */
    float getDepth();

    /**
     * @see #getDepth()
     */
    void setDepth(float value);

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * The rate of oscillation.
     */
    int getRate();

    /**
     * @see #getRate()
     */
    void setRate(int value);

    /**
     * The LFO waveforms.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum WaveForm {

        /**
         * A sine wave (0).
         */
        SINE(0),

        /**
         * A saw wave (1).
         */
        SAW(1),

        /**
         * A triangle wave (2).
         */
        TRIANGLE(2),

        /**
         * A square wave (3).
         */
        SQUARE(3);

        private final int mValue;

        WaveForm(int value) {
            mValue = value;
        }

        /**
         * Returns the integer value of the {@link Waveform}.
         */
        public int getValue() {
            return mValue;
        }

        /**
         * Returns a {@link WaveForm} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static WaveForm toType(Integer type) {
            for (WaveForm result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static WaveForm toType(Float type) {
            return toType(type.intValue());
        }
    }
}
