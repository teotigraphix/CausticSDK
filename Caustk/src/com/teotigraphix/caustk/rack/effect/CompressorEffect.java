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
public class CompressorEffect extends RackEffect {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float attack = 0.01f;

    @Tag(101)
    private float ratio = 1f;

    @Tag(102)
    private float release = 0.05f;

    @Tag(103)
    private int sidechain = -1;

    @Tag(104)
    private float threshold = 0.1f;

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
        return get(CompressorControl.Attack);
    }

    public void setAttack(float value) {
        if (value == attack)
            return;
        if (value < 0.00001f || value > 0.2f)
            throw newRangeException(CompressorControl.Attack, "0.00001..0.2", value);
        attack = value;
        set(CompressorControl.Attack, attack);
    }

    //----------------------------------
    // ratio
    //----------------------------------

    public float getRatio() {
        return ratio;
    }

    float getRatio(boolean restore) {
        return get(CompressorControl.Ratio);
    }

    public void setRatio(float value) {
        if (value == ratio)
            return;
        if (value < 0f || value > 1.0f)
            throw newRangeException(CompressorControl.Ratio, "0.0..1.0", value);
        ratio = value;
        set(CompressorControl.Ratio, ratio);
    }

    //----------------------------------
    // release
    //----------------------------------

    public float getRelease() {
        return release;
    }

    float getRelease(boolean restore) {
        return get(CompressorControl.Release);
    }

    public void setRelease(float value) {
        if (value == release)
            return;
        if (value < 0.001f || value > 0.2f)
            throw newRangeException(CompressorControl.Release, "0.001..0.2", value);
        release = value;
        set(CompressorControl.Release, release);
    }

    //----------------------------------
    // sidechain
    //----------------------------------

    public int getSidechain() {
        return sidechain;
    }

    int getSidechain(boolean restore) {
        return (int)get(CompressorControl.Sidechain);
    }

    public void setSidechain(int value) {
        if (value == sidechain)
            return;
        if (value < 0 || value > 6)
            throw newRangeException(CompressorControl.Sidechain, "0..6", value);
        sidechain = value;
        set(CompressorControl.Sidechain, sidechain);
    }

    //----------------------------------
    // threshold
    //----------------------------------

    public float getThreshold() {
        return threshold;
    }

    float getThreshold(boolean restore) {
        return get(CompressorControl.Threshold);
    }

    public void setThreshold(float value) {
        if (value == threshold)
            return;
        if (value < 0f || value > 1.0f)
            throw newRangeException(CompressorControl.Threshold, "0.0..1.0", value);
        threshold = value;
        set(CompressorControl.Threshold, threshold);
    }

    CompressorEffect() {
    }

    public CompressorEffect(int slot) {
        super(EffectType.Compressor, slot);
    }

    @Override
    public void restore() {
        setAttack(getAttack(true));
        setRatio(getRatio(true));
        setRelease(getRelease(true));
        setSidechain(getSidechain(true));
        setThreshold(getThreshold(true));
    }

    @Override
    public void update(ICaustkApplicationContext context) {
        super.update(context);
        set(CompressorControl.Attack, attack);
        set(CompressorControl.Ratio, ratio);
        set(CompressorControl.Release, release);
        set(CompressorControl.Sidechain, sidechain);
        set(CompressorControl.Threshold, threshold);
    }

    public enum CompressorControl implements IEffectControl {

        /**
         * 0.00001..0.2
         */
        Attack("attack"),

        /**
         * 0.0..1.0
         */
        Ratio("ratio"),

        /**
         * 0.001..0.2
         */
        Release("release"),

        /**
         * 0..6
         */
        Sidechain("sidechain"),

        /**
         * 0.0..1.0
         */
        Threshold("threshold");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private CompressorControl(String control) {
            this.control = control;
        }
    }
}
