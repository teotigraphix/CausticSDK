////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.tone.components;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.FilterMessage;

public class SynthFilterComponent extends FilterComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float attack = 0f;

    @Tag(101)
    private float decay = 0f;

    @Tag(102)
    private float release = 1.5f;

    @Tag(103)
    private float sustain = 1.0f;

    @Tag(104)
    private float track = 0f;

    @Tag(105)
    private FilterType type = FilterType.NONE;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    public float getAttack() {
        return attack;
    }

    float getAttack(boolean restore) {
        return FilterMessage.FILTER_ATTACK.query(getEngine(), getToneIndex());
    }

    public void setAttack(float value) {
        if (value == attack)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(FilterMessage.FILTER_ATTACK.toString(), "0..3.0625", value);
        attack = value;
        FilterMessage.FILTER_ATTACK.send(getEngine(), getToneIndex(), attack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return FilterMessage.FILTER_DECAY.query(getEngine(), getToneIndex());
    }

    public void setDecay(float value) {
        if (value == decay)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(FilterMessage.FILTER_DECAY.toString(), "0..3.0625", value);
        decay = value;
        FilterMessage.FILTER_DECAY.send(getEngine(), getToneIndex(), decay);
    }

    //----------------------------------
    // release
    //----------------------------------

    public float getRelease() {
        return release;
    }

    float getRelease(boolean restore) {
        return FilterMessage.FILTER_RELEASE.query(getEngine(), getToneIndex());
    }

    public void setRelease(float value) {
        if (value == release)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(FilterMessage.FILTER_RELEASE.toString(), "0..3.0625", value);
        release = value;
        FilterMessage.FILTER_RELEASE.send(getEngine(), getToneIndex(), release);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    public float getSustain() {
        return sustain;
    }

    float getSustain(boolean restore) {
        return FilterMessage.FILTER_SUSTAIN.query(getEngine(), getToneIndex());
    }

    public void setSustain(float value) {
        if (value == sustain)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(FilterMessage.FILTER_SUSTAIN.toString(), "0..1.0", value);
        sustain = value;
        FilterMessage.FILTER_SUSTAIN.send(getEngine(), getToneIndex(), sustain);
    }

    //----------------------------------
    // track
    //----------------------------------

    public float getTrack() {
        return track;
    }

    float getTrack(boolean restore) {
        return FilterMessage.FILTER_KBTRACK.query(getEngine(), getToneIndex());
    }

    public void setTrack(float value) {
        if (value == track)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(FilterMessage.FILTER_KBTRACK.toString(), "0..1.0", value);
        track = value;
        FilterMessage.FILTER_KBTRACK.send(getEngine(), getToneIndex(), track);
    }

    //----------------------------------
    // type
    //----------------------------------

    public FilterType getType() {
        return type;
    }

    FilterType getType(boolean restore) {
        return FilterType.toType(FilterMessage.FILTER_TYPE.query(getEngine(), getToneIndex()));
    }

    public void setType(FilterType value) {
        if (value == type)
            return;
        type = value;
        FilterMessage.FILTER_TYPE.send(getEngine(), getToneIndex(), type.getValue());
    }

    public SynthFilterComponent() {

    }

    @Override
    public void restore() {
        super.restore();

        setAttack(getAttack(true));
        setDecay(getDecay(true));
        setRelease(getRelease(true));
        setSustain(getSustain(true));
        try {
            setTrack(getTrack(true));
        } catch (IllegalArgumentException e) {
            // PCMSynth dosn't have tracking
        }

        setType(getType(true));
    }

    /**
     * The {@link IFilter#getType()} type enumeration.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum FilterType {

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

        private final int value;

        /**
         * The Integer value for the filter type.
         */
        public int getValue() {
            return value;
        }

        FilterType(int type) {
            value = type;
        }

        /**
         * Returns a {@link FilterType} for the integer passed, null if not
         * found.
         * 
         * @param type The filter type.
         */
        public static FilterType toType(Integer type) {
            for (FilterType result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static FilterType toType(Float type) {
            return toType(type.intValue());
        }
    }
}
