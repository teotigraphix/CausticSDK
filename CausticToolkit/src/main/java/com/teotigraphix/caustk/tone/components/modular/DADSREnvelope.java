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

package com.teotigraphix.caustk.tone.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class DADSREnvelope extends ModularComponentBase {

    //----------------------------------
    // delay
    //----------------------------------

    private float delay;

    public float getDelay() {
        return delay;
    }

    float getDelay(boolean restore) {
        return getValue("delay");
    }

    /**
     * @param value (0..2)
     */
    public void setDelay(float value) {
        if (value == delay)
            return;
        delay = value;
        if (value < 0f || value > 2f)
            newRangeException("delay", "0..2", value);
        setValue("delay", value);
    }

    //----------------------------------
    // attack
    //----------------------------------

    private float attack;

    public float getAttack() {
        return attack;
    }

    float getAttack(boolean restore) {
        return getValue("attack");
    }

    /**
     * @param value (0..2)
     */
    public void setAttack(float value) {
        if (value == attack)
            return;
        attack = value;
        if (value < 0f || value > 2f)
            newRangeException("attack", "0..2", value);
        setValue("attack", value);
    }

    //----------------------------------
    // decay
    //----------------------------------

    private float decay;

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return getValue("decay");
    }

    /**
     * @param value (0..2)
     */
    public void setDecay(float value) {
        if (value == decay)
            return;
        decay = value;
        if (value < 0f || value > 2f)
            newRangeException("decay", "0..2", value);
        setValue("decay", value);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    private float sustain;

    public float getSustain() {
        return sustain;
    }

    float getSustain(boolean restore) {
        return getValue("sustain");
    }

    /**
     * @param value (0..1)
     */
    public void setSustain(float value) {
        if (value == sustain)
            return;
        sustain = value;
        if (value < 0f || value > 1f)
            newRangeException("sustain", "0..1", value);
        setValue("sustain", value);
    }

    //----------------------------------
    // release
    //----------------------------------

    private float release;

    public float getRelease() {
        return release;
    }

    float getRelease(boolean restore) {
        return getValue("release");
    }

    /**
     * @param value (0..1)
     */
    public void setRelease(float value) {
        if (value == release)
            return;
        release = value;
        if (value < 0f || value > 1f)
            newRangeException("release", "0..1", value);
        setValue("release", value);
    }

    //----------------------------------
    // legato
    //----------------------------------

    private int legato;

    public int getLagato() {
        return legato;
    }

    int getLagato(boolean restore) {
        return (int)getValue("legato");
    }

    /**
     * @param value (0,1)
     */
    public void setLagato(int value) {
        if (value == legato)
            return;
        legato = value;
        if (value < 0 || value > 1)
            newRangeException("legato", "0,1", value);
        setValue("legato", value);
    }

    //----------------------------------
    // attackSlope
    //----------------------------------

    private EnvelopeSlope attackSlope;

    public EnvelopeSlope getAttackSlope() {
        return attackSlope;
    }

    EnvelopeSlope getAttackSlope(boolean restore) {
        return EnvelopeSlope.fromFloat(getValue("attack_slope"));
    }

    /**
     * @param value (0,1,2)
     */
    public void setAttackSlope(EnvelopeSlope value) {
        if (value == attackSlope)
            return;
        attackSlope = value;
        setValue("attack_slope", value.getValue());
    }

    //----------------------------------
    // decaySlope
    //----------------------------------

    private EnvelopeSlope decaySlope;

    public EnvelopeSlope getDecaySlope() {
        return decaySlope;
    }

    EnvelopeSlope getDecaySlope(boolean restore) {
        return EnvelopeSlope.fromFloat(getValue("decay_slope"));
    }

    /**
     * @param value (0,1,2)
     */
    public void setDecaySlope(EnvelopeSlope value) {
        if (value == decaySlope)
            return;
        decaySlope = value;
        setValue("decay_slope", value.getValue());
    }

    //----------------------------------
    // releaseSlope
    //----------------------------------

    private EnvelopeSlope releaseSlope;

    public EnvelopeSlope getReleaseSlope() {
        return releaseSlope;
    }

    EnvelopeSlope getReleaseSlope(boolean restore) {
        return EnvelopeSlope.fromFloat(getValue("release_slope"));
    }

    /**
     * @param value (0,1,2)
     */
    public void setReleaseSlope(EnvelopeSlope value) {
        if (value == releaseSlope)
            return;
        releaseSlope = value;
        setValue("release_slope", value.getValue());
    }

    //----------------------------------
    // outGain
    //----------------------------------

    private float outGain;

    public float getOutGain() {
        return outGain;
    }

    float getOutGain(boolean restore) {
        return getValue("out_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setOutGain(float value) {
        if (value == outGain)
            return;
        outGain = value;
        if (value < 0f || value > 1f)
            newRangeException("out_gain", "0..1", value);
        setValue("out_gain", value);
    }

    //----------------------------------
    // invOutGain
    //----------------------------------

    private float invOutGain;

    public float getInvOutGain() {
        return invOutGain;
    }

    float getInvOutGain(boolean restore) {
        return getValue("invout_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setInvOutGain(float value) {
        if (value == invOutGain)
            return;
        invOutGain = value;
        if (value < 0f || value > 1f)
            newRangeException("invout_gain", "0..1", value);
        setValue("invout_gain", value);
    }

    public DADSREnvelope() {
    }

    public DADSREnvelope(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 2;
    }

    @Override
    public void restore() {
        super.restore();
        setAttack(getAttack(true));
        setAttackSlope(getAttackSlope(true));
        setDecay(getDecay(true));
        setDecaySlope(getDecaySlope(true));
        setDelay(getDelay(true));
        setInvOutGain(getInvOutGain(true));
        setLagato(getLagato(true));
        setOutGain(getOutGain(true));
        setRelease(getRelease(true));
        setReleaseSlope(getReleaseSlope(true));
        setSustain(getSustain(true));
    }

    public enum EnvelopeSlope {

        Slow(0),

        Linear(1),

        Fast(2);

        private int value;

        public final int getValue() {
            return value;
        }

        public static EnvelopeSlope fromFloat(float value) {
            for (EnvelopeSlope envelopeSlope : values()) {
                if (envelopeSlope.getValue() == (int)value)
                    return envelopeSlope;
            }
            return null;
        }

        EnvelopeSlope(int value) {
            this.value = value;
        }
    }

    public enum DADSREnvelopeJack implements IModularJack {

        InModulation(0),

        Out(0),

        OutInv(1);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        DADSREnvelopeJack(int value) {
            this.value = value;
        }
    }
}
