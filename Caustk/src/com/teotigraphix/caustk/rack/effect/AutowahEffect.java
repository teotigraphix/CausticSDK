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
import com.teotigraphix.caustk.controller.IRackContext;

/**
 * @author Michael Schmalle
 */
public class AutowahEffect extends RackEffect {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float cutoff = 2.23f;

    @Tag(101)
    private float depth = 1f;

    @Tag(102)
    private float resonance = 0.5f;

    @Tag(103)
    private float speed = 0.4f;

    @Tag(104)
    private float wet = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    public float getCutoff() {
        return cutoff;
    }

    float getCutoff(boolean restore) {
        return get(AutowahControl.Cutoff);
    }

    public void setCutoff(float value) {
        if (value == cutoff)
            return;
        if (value < 0.5f || value > 4.0f)
            throw newRangeException(AutowahControl.Cutoff, "0.5..4.0", value);
        cutoff = value;
        set(AutowahControl.Cutoff, cutoff);
    }

    //----------------------------------
    // depth
    //----------------------------------

    public float getDepth() {
        return depth;
    }

    float getDepth(boolean restore) {
        return get(AutowahControl.Depth);
    }

    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(AutowahControl.Depth, "0..1", value);
        depth = value;
        set(AutowahControl.Depth, depth);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    public float getResonance() {
        return resonance;
    }

    float getResonance(boolean restore) {
        return get(AutowahControl.Resonance);
    }

    public void setResonance(float value) {
        if (value == resonance)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(AutowahControl.Resonance, "0..1", value);
        resonance = value;
        set(AutowahControl.Resonance, resonance);
    }

    //----------------------------------
    // speed
    //----------------------------------

    public float getSpeed() {
        return speed;
    }

    float getSpeed(boolean restore) {
        return get(AutowahControl.Speed);
    }

    public void setSpeed(float value) {
        if (value == speed)
            return;
        if (value < 0f || value > 0.5f)
            throw newRangeException(AutowahControl.Speed, "0..0.5", value);
        speed = value;
        set(AutowahControl.Speed, speed);
    }

    //----------------------------------
    // wet
    //----------------------------------

    public float getWet() {
        return wet;
    }

    float getWet(boolean restore) {
        return get(AutowahControl.Wet);
    }

    public void setWet(float value) {
        if (value == wet)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(AutowahControl.Wet, "0..1", value);
        wet = value;
        set(AutowahControl.Wet, wet);
    }

    AutowahEffect() {
    }

    public AutowahEffect(int slot, int toneIndex) {
        super(EffectType.Autowah, slot, toneIndex);
    }

    @Override
    public void restore() {
        super.restore();
        setCutoff(getCutoff(true));
        setDepth(getDepth(true));
        setResonance(getResonance(true));
        setSpeed(getSpeed(true));
        setWet(getWet(true));
    }

    @Override
    public void update(IRackContext context) {
        super.update(context);
        set(AutowahControl.Cutoff, cutoff);
        set(AutowahControl.Depth, depth);
        set(AutowahControl.Resonance, resonance);
        set(AutowahControl.Speed, speed);
        set(AutowahControl.Wet, wet);
    }

    public enum AutowahControl implements IEffectControl {

        /**
         * 0.5..4.0
         */
        Cutoff("cutoff"),

        /**
         * 0..1
         */
        Depth("depth"),

        /**
         * 0..1
         */
        Resonance("resonance"),

        /**
         * 0.5
         */
        Speed("speed"),

        /**
         * 0..1
         */
        Wet("wet");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private AutowahControl(String control) {
            this.control = control;
        }
    }
}
