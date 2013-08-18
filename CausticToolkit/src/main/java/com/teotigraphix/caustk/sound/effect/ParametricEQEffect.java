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

package com.teotigraphix.caustk.sound.effect;

public class ParametricEQEffect extends EffectBase {

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // frequency
    //----------------------------------

    private float frequency = 0.54f;

    public float getFrequency() {
        return frequency;
    }

    float getFrequency(boolean restore) {
        return get(ParametricEQControl.Frequency);
    }

    public void setFrequency(float value) {
        if (value == frequency)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(ParametricEQControl.Frequency, "0.0..1.0", value);
        frequency = value;
        set(ParametricEQControl.Frequency, frequency);
    }

    //----------------------------------
    // gain
    //----------------------------------

    private int gain = 0;

    public int getGain() {
        return gain;
    }

    int getGain(boolean restore) {
        return (int)get(ParametricEQControl.Gain);
    }

    public void setGain(int value) {
        if (value == gain)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(ParametricEQControl.Gain, "-12..12", value);
        gain = value;
        set(ParametricEQControl.Gain, gain);
    }

    //----------------------------------
    // wet
    //----------------------------------

    private float width = 0.49999994f;

    public float getWidth() {
        return width;
    }

    float getWidth(boolean restore) {
        return get(ParametricEQControl.Width);
    }

    public void setWidth(float value) {
        if (value == width)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(ParametricEQControl.Width, "0.0..1.0", value);
        width = value;
        set(ParametricEQControl.Width, width);
    }

    public ParametricEQEffect(int slot, int toneIndex) {
        super(EffectType.PARAMETRICEQ, slot, toneIndex);
    }

    @Override
    public void restore() {
        setFrequency(getFrequency(true));
        setGain(getGain(true));
        setWidth(getWidth(true));
    }

    public enum ParametricEQControl implements IEffectControl {

        /**
         * 0.0..1.0
         */
        Frequency("frequency"),

        /**
         * -12..12
         */
        Gain("gain"),

        /**
         * 0.0..20.0
         */
        Width("width");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private ParametricEQControl(String control) {
            this.control = control;
        }
    }
}
