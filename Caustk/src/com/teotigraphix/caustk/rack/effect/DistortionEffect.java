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

public class DistortionEffect extends EffectBase {

    private static final long serialVersionUID = 7727552593282987957L;

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // program
    //----------------------------------

    private Program mProgram = Program.OVERDRIVE;

    public Program getProgram() {
        return mProgram;
    }

    Program getProgram(boolean restore) {
        return Program.fromInt((int)get(DistortionControl.Program));
    }

    public void setProgram(Program value) {
        if (value == mProgram)
            return;
        mProgram = value;
        set(DistortionControl.Program, mProgram.getValue());
    }

    //----------------------------------
    // pre
    //----------------------------------

    private float preGain;

    public float getPreGain() {
        return preGain;
    }

    float getPreGain(boolean restore) {
        return get(DistortionControl.Pre);
    }

    public void setPreGain(float value) {
        if (value == preGain)
            return;
        if (value < 0f || value > 5f)
            newRangeException(DistortionControl.Pre, "0.0..5.0", value);
        preGain = value;
        set(DistortionControl.Pre, preGain);
    }

    //----------------------------------
    // amount
    //----------------------------------

    private float amount = 16.3f;

    public float getAmount() {
        return amount;
    }

    float getAmount(boolean restore) {
        return get(DistortionControl.Amount);
    }

    public void setAmount(float value) {
        if (value == amount)
            return;
        if (value < 0f || value > 5f)
            newRangeException(DistortionControl.Amount, "0.0..20.0", value);
        amount = value;
        set(DistortionControl.Amount, amount);
    }

    //----------------------------------
    // post
    //----------------------------------

    private float postGain;

    public float getPostGain() {
        return postGain;
    }

    float getPostGain(boolean restore) {
        return get(DistortionControl.Post);
    }

    public void setPostGain(float value) {
        if (value == postGain)
            return;
        if (value < 0f || value > 5f)
            newRangeException(DistortionControl.Post, "0.0..1.0", value);
        postGain = value;
        set(DistortionControl.Post, postGain);
    }

    public DistortionEffect(int slot, int toneIndex) {
        super(EffectType.DISTORTION, slot, toneIndex);
    }

    @Override
    public void restore() {
        setAmount(getAmount(true));
        setPreGain(getPreGain(true));
        setProgram(getProgram(true));
        setPostGain(getPostGain(true));
    }

    public enum Program {

        /**
         * Tube amp simulation.
         */
        OVERDRIVE(0),

        /**
         * Soft-knee limiter.
         */
        SATURATE(1),

        /**
         * Hard-knee limiter.
         */
        FOLDBACK(2),

        /**
         * folds the signal onto itself.
         */
        FUZZ(3);

        private int mValue;

        Program(int value) {
            mValue = value;
        }

        /**
         * Returns the int value for the {@link Program}.
         */
        public int getValue() {
            return mValue;
        }

        /**
         * Returns the {@link Program} from an int value.
         * 
         * @param value The int program value.
         */
        public static Program fromInt(Integer value) {
            for (Program p : values()) {
                if (p.getValue() == value)
                    return p;
            }
            return null;
        }
    }

    public enum DistortionControl implements IEffectControl {

        /**
         * 0..3
         */
        Program("program"),

        /**
         * 0.0..5.0
         */
        Pre("pre"),

        /**
         * 0.0..20.0
         */
        Amount("amount"),

        /**
         * 0.0..1.0
         */
        Post("post");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private DistortionControl(String control) {
            this.control = control;
        }
    }
}
