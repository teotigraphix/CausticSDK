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
public class DistortionEffect extends RackEffect {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private Program program = Program.OVERDRIVE;

    @Tag(101)
    private float preGain;

    @Tag(102)
    private float amount = 16.3f;

    @Tag(103)
    private float postGain;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // program
    //----------------------------------

    public Program getProgram() {
        return program;
    }

    Program getProgram(boolean restore) {
        return Program.fromInt((int)get(DistortionControl.Program));
    }

    public void setProgram(Program value) {
        if (value == program)
            return;
        program = value;
        set(DistortionControl.Program, program.getValue());
    }

    //----------------------------------
    // pre
    //----------------------------------

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

    DistortionEffect() {
    }

    public DistortionEffect(int slot, int toneIndex) {
        super(EffectType.Distortion, slot, toneIndex);
    }

    @Override
    public void restore() {
        setAmount(getAmount(true));
        setPreGain(getPreGain(true));
        setProgram(getProgram(true));
        setPostGain(getPostGain(true));
    }

    @Override
    public void update(IRackContext context) {
        super.update(context);
        set(DistortionControl.Amount, amount);
        set(DistortionControl.Post, postGain);
        set(DistortionControl.Pre, preGain);
        set(DistortionControl.Program, program.getValue());
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
