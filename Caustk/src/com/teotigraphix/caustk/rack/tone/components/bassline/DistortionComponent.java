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

package com.teotigraphix.caustk.rack.tone.components.bassline;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.BasslineMessage;
import com.teotigraphix.caustk.rack.tone.ToneComponent;

public class DistortionComponent extends ToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float amount = 15f;

    @Tag(101)
    private float postGain = 0.2f;

    @Tag(102)
    private float preGain = 4.05f;

    @Tag(103)
    private Program program = Program.OFF;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // amount
    //----------------------------------

    public float getAmount() {
        return amount;
    }

    public float getAmount(boolean restore) {
        return BasslineMessage.DISTORTION_AMOUNT.query(getEngine(), getToneIndex());
    }

    public void setAmount(float value) {
        if (value == amount)
            return;
        if (value < 0f || value > 20f)
            throw newRangeException(BasslineMessage.DISTORTION_AMOUNT.toString(), "0..20", value);
        amount = value;
        BasslineMessage.DISTORTION_AMOUNT.send(getEngine(), getToneIndex(), amount);
    }

    //----------------------------------
    // postGain
    //----------------------------------

    public float getPostGain() {
        return postGain;
    }

    public float getPostGain(boolean restore) {
        return BasslineMessage.DISTORTION_POSTGAIN.query(getEngine(), getToneIndex());
    }

    public void setPostGain(float value) {
        if (value == postGain)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BasslineMessage.DISTORTION_POSTGAIN.toString(), "0..1", value);
        postGain = value;
        BasslineMessage.DISTORTION_POSTGAIN.send(getEngine(), getToneIndex(), postGain);
    }

    //----------------------------------
    // preGain
    //----------------------------------

    public float getPreGain() {
        return preGain;
    }

    public float getPreGain(boolean restore) {
        return BasslineMessage.DISTORTION_PREGAIN.query(getEngine(), getToneIndex());
    }

    public void setPreGain(float value) {
        if (value == preGain)
            return;
        if (value < 0f || value > 5f)
            throw newRangeException(BasslineMessage.DISTORTION_PREGAIN.toString(), "0..5", value);
        preGain = value;
        BasslineMessage.DISTORTION_PREGAIN.send(getEngine(), getToneIndex(), preGain);
    }

    //----------------------------------
    // program
    //----------------------------------

    public Program getProgram() {
        return program;
    }

    public Program getProgram(boolean restore) {
        return Program
                .toType(BasslineMessage.DISTORTION_PROGRAM.query(getEngine(), getToneIndex()));
    }

    public void setProgram(Program value) {
        if (value == program)
            return;
        program = value;
        BasslineMessage.DISTORTION_PROGRAM.send(getEngine(), getToneIndex(), program.getValue());
    }

    public DistortionComponent() {
    }

    @Override
    public void restore() {
        setAmount(getAmount(true));
        setPostGain(getPostGain(true));
        setPreGain(getPreGain(true));
        setProgram(getProgram(true));
    }

    /**
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Program {

        /**
         * 
         */
        OFF(0),

        /**
         * 
         */
        OVERDRIVE(1),

        /**
         * 
         */
        SATURATE(2),

        /**
         * 
         */
        FOLDBACK(3),

        /**
         * 
         */
        FUZZ(4);

        private int mValue;

        Program(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        public static Program toType(Integer type) {
            for (Program p : values()) {
                if (p.getValue() == type)
                    return p;
            }
            return null;
        }

        public static Program toType(Float type) {
            return toType(type.intValue());
        }
    }
}
