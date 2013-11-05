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

package com.teotigraphix.caustk.rack.effect;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;

/**
 * @author Michael Schmalle
 */
public class BitcrusherEffect extends RackEffect {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int depth = 3;

    @Tag(101)
    private float jitter = 0f;

    @Tag(102)
    private float rate = 0.1f;

    @Tag(103)
    private float wet = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    public int getDepth() {
        return depth;
    }

    int getDepth(boolean restore) {
        return (int)get(BitcrusherControl.Depth);
    }

    public void setDepth(int value) {
        if (value == depth)
            return;
        if (value < 1 || value > 16)
            throw newRangeException(BitcrusherControl.Depth, "1..16", value);
        depth = value;
        set(BitcrusherControl.Depth, depth);
    }

    //----------------------------------
    // jitter
    //----------------------------------

    public float getJitter() {
        return jitter;
    }

    float getJitter(boolean restore) {
        return get(BitcrusherControl.Jitter);
    }

    public void setJitter(float value) {
        if (value == jitter)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BitcrusherControl.Jitter, "0..1", value);
        jitter = value;
        set(BitcrusherControl.Jitter, jitter);
    }

    //----------------------------------
    // rate
    //----------------------------------

    public float getRate() {
        return rate;
    }

    float getRate(boolean restore) {
        return get(BitcrusherControl.Rate);
    }

    public void setRate(float value) {
        if (value == rate)
            return;
        if (value < 0.01f || value > 0.5f)
            throw newRangeException(BitcrusherControl.Rate, "0.01..0.5", value);
        rate = value;
        set(BitcrusherControl.Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    public float getWet() {
        return wet;
    }

    float getWet(boolean restore) {
        return get(BitcrusherControl.Wet);
    }

    public void setWet(float value) {
        if (value == wet)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BitcrusherControl.Wet, "0..1", value);
        wet = value;
        set(BitcrusherControl.Wet, wet);
    }

    BitcrusherEffect() {
    }

    public BitcrusherEffect(int slot) {
        super(EffectType.Bitcrusher, slot);
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setJitter(getJitter(true));
        setRate(getRate(true));
        setWet(getWet(true));
    }

    @Override
    public void update(ICaustkApplicationContext context) {
        super.update(context);
        set(BitcrusherControl.Depth, depth);
        set(BitcrusherControl.Jitter, jitter);
        set(BitcrusherControl.Rate, rate);
        set(BitcrusherControl.Wet, wet);
    }

    public enum BitcrusherControl implements IEffectControl {

        /**
         * 1..16
         */
        Depth("depth"),

        /**
         * 0..1
         */
        Jitter("jitter"),

        /**
         * 0.01..0.5
         */
        Rate("rate"),

        /**
         * 0..1
         */
        Wet("wet");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private BitcrusherControl(String control) {
            this.control = control;
        }
    }

}
