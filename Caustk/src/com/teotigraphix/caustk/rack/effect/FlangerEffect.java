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

/**
 * @author Michael Schmalle
 */
public class FlangerEffect extends EffectBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float depth = 0.25f;

    @Tag(101)
    private float feedback = 0.4f;

    @Tag(102)
    private float rate = 0.4f;

    @Tag(103)
    private float wet = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    public float getDepth() {
        return depth;
    }

    float getDepth(boolean restore) {
        return get(FlangerControl.Depth);
    }

    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0.1f || value > 0.95f)
            throw newRangeException(FlangerControl.Depth, "0.1..0.95", value);
        depth = value;
        set(FlangerControl.Depth, depth);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    public float getFeedback() {
        return feedback;
    }

    float getFeedback(boolean restore) {
        return get(FlangerControl.Feedback);
    }

    public void setFeedback(float value) {
        if (value == feedback)
            return;
        if (value < 0.25f || value > 0.8f)
            throw newRangeException(FlangerControl.Feedback, "0.25..0.8", value);
        feedback = value;
        set(FlangerControl.Feedback, feedback);
    }

    //----------------------------------
    // rate
    //----------------------------------

    public float getRate() {
        return rate;
    }

    float getRate(boolean restore) {
        return get(FlangerControl.Rate);
    }

    public void setRate(float value) {
        if (value == rate)
            return;
        if (value < 0.04f || value > 2.0f)
            throw newRangeException(FlangerControl.Rate, "0.04..2.0", value);
        rate = value;
        set(FlangerControl.Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    public float getWet() {
        return wet;
    }

    float getWet(boolean restore) {
        return get(FlangerControl.Wet);
    }

    public void setWet(float value) {
        if (value == wet)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(FlangerControl.Wet, "0..1", value);
        wet = value;
        set(FlangerControl.Wet, wet);
    }

    FlangerEffect() {
    }

    public FlangerEffect(int slot, int toneIndex) {
        super(EffectType.Flanger, slot, toneIndex);
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setFeedback(getFeedback(true));
        setRate(getRate(true));
        setWet(getWet(true));
    }

    @Override
    public void update() {
        super.update();

        set(FlangerControl.Depth, depth);
        set(FlangerControl.Feedback, feedback);
        set(FlangerControl.Rate, rate);
        set(FlangerControl.Wet, wet);
    }

    public enum FlangerControl implements IEffectControl {

        /**
         * 0.1..0.95
         */
        Depth("depth"),

        /**
         * 0.25..0.8
         */
        Feedback("feedback"),

        /**
         * 0.04..2.0
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

        private FlangerControl(String control) {
            this.control = control;
        }
    }
}
