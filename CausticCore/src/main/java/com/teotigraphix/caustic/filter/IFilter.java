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

import com.teotigraphix.caustic.osc.FilterMessage;

/**
 * The {@link IFilter} interface gives access to all filter features of a synth
 * tone generator.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IFilter extends IFilterComponent
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // type
    //----------------------------------

    /**
     * The type of filter applied to the machine.
     * <p>
     * The possible filter types are None(0), LowPass(1), HighPass(2),
     * BandPass(3), Inv. LP(4), Inv. HP(5) and Inv. BP(6)
     * <p>
     * The inverted version affects the envelope section. Instead of going from
     * 0 to 1, invert makes them go from 1 to 0.
     * <p>
     * Values; ({@link FilterType}), default; {@link FilterType#NONE}
     * 
     * @see FilterMessage#FILTER_TYPE
     */
    FilterType getType();

    /**
     * @see #getType()
     * @see FilterMessage#FILTER_TYPE
     */
    void setType(FilterType value);

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * The amount of time before cutoff reaches the value defined by the cutoff
     * slider, stating at 0.
     * 
     * @see FilterMessage#FILTER_ATTACK
     */
    float getAttack();

    /**
     * @see #getAttack()
     * @see FilterMessage#FILTER_ATTACK
     */
    void setAttack(float value);

    //----------------------------------
    // cutoff
    //----------------------------------

    /**
     * The frequency at which the filter starts to cut the signal.
     * 
     * @see FilterMessage#FILTER_CUTOFF
     */
    @Override
    float getCutoff();

    /**
     * @see #getCutoff()
     * @see FilterMessage#FILTER_CUTOFF
     */
    @Override
    void setCutoff(float value);

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * The amount of time, after the attack period, for the cutoff to go from
     * the decay value to the sustain level.
     * 
     * @see FilterMessage#FILTER_DECAY
     */
    float getDecay();

    /**
     * @see #getDecay()
     * @see FilterMessage#FILTER_DECAY
     */
    void setDecay(float value);

    //----------------------------------
    // release
    //----------------------------------

    /**
     * The amount of time for the cutoff to go from the sustain level down to 0.
     * 
     * @see FilterMessage#FILTER_RELEASE
     */
    float getRelease();

    /**
     * @see #getRelease()
     * @see FilterMessage#FILTER_RELEASE
     */
    void setRelease(float value);

    //----------------------------------
    // sustain
    //----------------------------------

    /**
     * The value for cutoff at which to settle, once the attack and decay period
     * have elapsed and the note is held, relative to the cutoff value.
     * 
     * @see FilterMessage#FILTER_SUSTAIN
     */
    float getSustain();

    /**
     * @see #getSustain()
     * @see FilterMessage#FILTER_SUSTAIN
     */
    void setSustain(float value);

    //----------------------------------
    // track
    //----------------------------------

    /**
     * The amount of filtering done based on the keyboard note played.
     * <p>
     * Full keyboard tracking cuts off more the lower the note played.
     * 
     * @see FilterMessage#FILTER_KBTRACK
     */
    float getTrack();

    /**
     * @see #getTrack()
     * @see FilterMessage#FILTER_KBTRACK
     */
    void setTrack(float value);

    /**
     * The {@link IFilter#getType()} type enumeration.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum FilterType
    {

        //--------------------------------------------------------------------------
        //
        // Public :: Values
        //
        //--------------------------------------------------------------------------

        /**
         * No filter applied.
         */
        NONE(0),

        /**
         * The low pass filter.
         */
        LOW_PASS(1),

        /**
         * The high pass filter.
         */
        HIGH_PASS(2),

        /**
         * The band pass filter.
         */
        BAND_PASS(3),

        /**
         * The inverted low pass filter.
         */
        INV_LP(4),

        /**
         * The inverted high pass filter.
         */
        INV_HP(5),

        /**
         * The inverted band pass filter.
         */
        INV_BP(6);

        //--------------------------------------------------------------------------
        //
        // Public :: Properties
        //
        //--------------------------------------------------------------------------

        //----------------------------------
        // value
        //----------------------------------

        private final int mValue;

        /**
         * The Integer value for the filter type.
         */
        public int getValue()
        {
            return mValue;
        }

        FilterType(int value)
        {
            mValue = value;
        }

        /**
         * Returns a {@link FilterType} for the integer passed, null if not
         * found.
         * 
         * @param type The filter type.
         */
        public static FilterType toType(Integer type)
        {
            for (FilterType result : values())
            {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static FilterType toType(Float type)
        {
            return toType(type.intValue());
        }
    }
}
