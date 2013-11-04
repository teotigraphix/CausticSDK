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
public class PhaserEffect extends RackEffect {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float depth = 0.8f;

    @Tag(101)
    private float feedback = 0.47f;

    @Tag(102)
    private int rate = 10;

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
        return get(PhaserControl.Depth);
    }

    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0.1f || value > 0.95f)
            throw newRangeException(PhaserControl.Depth, "0.1..0.95", value);
        depth = value;
        set(PhaserControl.Depth, depth);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    public float getFeedback() {
        return feedback;
    }

    float getFeedback(boolean restore) {
        return get(PhaserControl.Feedback);
    }

    public void setFeedback(float value) {
        if (value == feedback)
            return;
        if (value < 0.1f || value > 0.95f)
            throw newRangeException(PhaserControl.Feedback, "0.1..0.95", value);
        feedback = value;
        set(PhaserControl.Feedback, feedback);
    }

    //----------------------------------
    // rate
    //----------------------------------

    public int getRate() {
        return rate;
    }

    int getRate(boolean restore) {
        return (int)get(PhaserControl.Rate);
    }

    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 0/*2 XXX*/|| value > 50)
            throw newRangeException(PhaserControl.Rate, "2..50", value);
        rate = value;
        set(PhaserControl.Rate, rate);
    }

    PhaserEffect() {
    }

    public PhaserEffect(int slot, int toneIndex) {
        super(EffectType.Phaser, slot, toneIndex);
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setFeedback(getFeedback(true));
        setRate(getRate(true));
    }

    @Override
    public void update(ICaustkApplicationContext context) {
        super.update(context);
        set(PhaserControl.Depth, depth);
        set(PhaserControl.Feedback, feedback);
        set(PhaserControl.Rate, rate);
    }

    public enum PhaserControl implements IEffectControl {

        /**
         * 0.1..0.95
         */
        Depth("depth"),

        /**
         * 0.1..0.95
         */
        Feedback("feedback"),

        /**
         * 2..50
         */
        Rate("rate");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private PhaserControl(String control) {
            this.control = control;
        }
    }
}
