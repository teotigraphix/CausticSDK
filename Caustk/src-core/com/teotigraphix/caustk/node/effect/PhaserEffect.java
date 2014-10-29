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

package com.teotigraphix.caustk.node.effect;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.EffectControls;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link PhaserEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PhaserEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float depth = 0.8f;

    @Tag(201)
    private float feedback = 0.47f;

    @Tag(202)
    private float highFreq = 0.09f;

    @Tag(203)
    private float lowFreq = 0.01f;

    @Tag(204)
    private int rate = 10;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see EffectControls#Phaser_Depth
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return get(EffectControls.Phaser_Depth);
    }

    /**
     * @see EffectControls#Phaser_Depth
     */
    public void setDepth(float depth) {
        if (!EffectControls.Phaser_Depth.isValid(depth, this.depth))
            return;
        this.depth = depth;
        set(EffectControls.Phaser_Depth, depth);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see EffectControls#Phaser_Feedback
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return get(EffectControls.Phaser_Feedback);
    }

    /**
     * @see EffectControls#Phaser_Feedback
     */
    public void setFeedback(float feedback) {
        if (!EffectControls.Phaser_Feedback.isValid(feedback, this.feedback))
            return;
        this.feedback = feedback;
        set(EffectControls.Phaser_Feedback, feedback);
    }

    //----------------------------------
    // highFreq
    //----------------------------------

    /**
     * @see EffectControls#Phaser_HighFreq
     */
    public float getHighfreq() {
        return highFreq;
    }

    public float queryHighfreq() {
        return get(EffectControls.Phaser_HighFreq);
    }

    /**
     * @see EffectControls#Phaser_HighFreq
     */
    public void setHighfreq(float highFreq) {
        if (!EffectControls.Phaser_HighFreq.isValid(highFreq, this.highFreq))
            return;
        this.highFreq = highFreq;
        set(EffectControls.Phaser_HighFreq, highFreq);
    }

    //----------------------------------
    //lowFreq
    //----------------------------------

    /**
     * @see EffectControls#Phaser_LowFreq
     */
    public float getLowfreq() {
        return lowFreq;
    }

    public float queryLowfreq() {
        return get(EffectControls.Phaser_LowFreq);
    }

    /**
     * @see EffectControls#Phaser_LowFreq
     */
    public void setLowfreq(float lowFreq) {
        if (!EffectControls.Phaser_LowFreq.isValid(lowFreq, this.lowFreq))
            return;
        this.lowFreq = lowFreq;
        set(EffectControls.Phaser_LowFreq, lowFreq);
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see EffectControls#Phaser_Rate
     */
    public int getRate() {
        return rate;
    }

    public int queryRate() {
        return (int)get(EffectControls.Phaser_Rate);
    }

    public void setRate(float rate) {
        setRate((int)rate);
    }

    /**
     * @see EffectControls#Phaser_Rate
     */
    public void setRate(int rate) {
        if (!EffectControls.Phaser_Rate.isValid(rate, this.rate))
            return;
        this.rate = rate;
        set(EffectControls.Phaser_Rate, rate);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PhaserEffect() {
    }

    public PhaserEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Phaser);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.Phaser_Depth, depth);
        set(EffectControls.Phaser_Feedback, feedback);
        set(EffectControls.Phaser_HighFreq, highFreq);
        set(EffectControls.Phaser_LowFreq, lowFreq);
        set(EffectControls.Phaser_Rate, rate);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setDepth(getDepth());
        setFeedback(getFeedback());
        setHighfreq(getHighfreq());
        setLowfreq(getLowfreq());
        setRate(getRate());
    }
}
