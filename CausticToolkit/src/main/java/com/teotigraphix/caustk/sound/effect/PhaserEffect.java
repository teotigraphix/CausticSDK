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

public class PhaserEffect extends EffectBase {

    //----------------------------------
    // depth
    //----------------------------------

    private float mDepth = 0.8f;

    public float getDepth() {
        return mDepth;
    }

    float getDepth(boolean restore) {
        return get(ParametricEQControl.Depth);
    }

    public void setDepth(float value) {
        if (value == mDepth)
            return;
        if (value < 0.1f || value > 0.95f)
            throw newRangeException(ParametricEQControl.Depth, "0.1..0.95", value);
        mDepth = value;
        set(ParametricEQControl.Depth, mDepth);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    private float mFeedback = 0.9f;

    public float getFeedback() {
        return mFeedback;
    }

    float getFeedback(boolean restore) {
        return get(ParametricEQControl.Feedback);
    }

    public void setFeedback(float value) {
        if (value == mFeedback)
            return;
        if (value < 0.1f || value > 0.95f)
            throw newRangeException(ParametricEQControl.Feedback, "0.1..0.95", value);
        mFeedback = value;
        set(ParametricEQControl.Feedback, mFeedback);
    }

    //----------------------------------
    // rate
    //----------------------------------

    private int mRate = 10;

    public int getRate() {
        return mRate;
    }

    int getRate(boolean restore) {
        return (int)get(ParametricEQControl.Rate);
    }

    public void setRate(int value) {
        if (value == mRate)
            return;
        if (value < 2 || value > 50)
            throw newRangeException(ParametricEQControl.Rate, "2..50", value);
        mRate = value;
        set(ParametricEQControl.Rate, mRate);
    }

    public PhaserEffect(EffectType type, int slot, int toneIndex) {
        super(EffectType.PHASER, slot, toneIndex);
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setFeedback(getFeedback(true));
        setRate(getRate(true));
    }

    public enum ParametricEQControl implements IEffectControl {

        /**
         * 0.1..0.95
         */
        Depth("Depth"),

        /**
         * 0.1..0.95
         */
        Feedback("feedback"),

        /**
         * 2..50
         */
        Rate("rate");

        private String control;

        public String getControl() {
            return control;
        }

        private ParametricEQControl(String control) {
            this.control = control;
        }
    }
}
