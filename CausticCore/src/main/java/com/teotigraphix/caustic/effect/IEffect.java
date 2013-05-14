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

import com.teotigraphix.caustic.core.IPersist;
import com.teotigraphix.caustic.machine.IMachine;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IEffect extends IEffectComponent, IPersist
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * The slot that the effect holds within the {@link IEffectsRack}.
     */
    int getIndex();

    //----------------------------------
    // type
    //----------------------------------

    /**
     * The type of effect.
     */
    EffectType getType();

    //----------------------------------
    // machine
    //----------------------------------

    /**
     * The owner machine of the effect.
     */
    IMachine getMachine();

    void setMachine(IMachine machine);

    /**
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum EffectType
    {

        /**
         * Adds distortion to the machine's signal.
         */
        DISTORTION(2),

        /**
         * Compresses the machine's signal.
         */
        COMPRESSOR(3),

        BITCRUSHER(4),

        FLANGER(5),

        PHASER(6),

        CHORUS(7),

        AUTOWAH(8),

        PARAMETRICEQ(9);

        private int mValue;

        EffectType(int value)
        {
            mValue = value;
        }

        public int getValue()
        {
            return mValue;
        }

        public static EffectType toType(Integer type)
        {
            for (EffectType result : values())
            {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }
    }

}
