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

package com.teotigraphix.caustic.effect;

import com.teotigraphix.caustic.machine.IBassline;

/**
 * The distortion insert for the {@link IBassline} machine.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IBasslineDistortionUnit extends IEffectComponent
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // amount
    //----------------------------------

    /**
     * The amount of distortion.
     * <p>
     * 
     * @see #CONTROL_DISTORTION_AMOUNT
     */
    float getAmount();

    /**
     * @see #getAmount()
     * @see #CONTROL_DISTORTION_AMOUNT
     */
    void setAmount(float value);

    //----------------------------------
    // postGain
    //----------------------------------

    /**
     * The amount of post-gain distortion.
     * <p>
     * 
     * @see #CONTROL_DISTORTION_POSTGAIN
     */
    float getPostGain();

    /**
     * @see #getPostGain()
     * @see #CONTROL_DISTORTION_POSTGAIN
     */
    void setPostGain(float value);

    //----------------------------------
    // preGain
    //----------------------------------

    /**
     * The amount of pre-gain distortion.
     * <p>
     * 
     * @see #CONTROL_DISTORTION_PREGAIN
     */
    float getPreGain();

    /**
     * @see #getPreGain()
     * @see #CONTROL_DISTORTION_PREGAIN
     */
    void setPreGain(float value);

    //----------------------------------
    // program
    //----------------------------------

    /**
     * @see #CONTROL_DISTORTION_PROGRAM
     */
    Program getProgram();

    /**
     * @see #getProgram()
     * @see #CONTROL_DISTORTION_PROGRAM
     */
    void setProgram(Program value);

    /**
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Program
    {

        /**
		 * 
		 */
        OFF(0),

        /**
		 * 
		 */
        OVERDRIVE(1),

        /**
		 * 
		 */
        SATURATE(2),

        /**
		 * 
		 */
        FOLDBACK(3),

        /**
		 * 
		 */
        FUZZ(4);

        private int mValue;

        Program(int value)
        {
            mValue = value;
        }

        public int getValue()
        {
            return mValue;
        }

        public static Program toType(Integer type)
        {
            for (Program p : values())
            {
                if (p.getValue() == type)
                    return p;
            }
            return null;
        }

        public static Program toType(Float type)
        {
            return toType(type.intValue());
        }
    }
}
