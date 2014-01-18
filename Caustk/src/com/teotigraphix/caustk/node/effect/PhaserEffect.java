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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.PhaserControl;

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

    private float depth = 0.8f;

    private float feedback = 0.47f;

    private float highFreq = 0.09f;

    private float lowFreq = 0.01f;

    private int rate = 10;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see PhaserControl#Depth
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return get(PhaserControl.Depth);
    }

    /**
     * @param depth (0.1..0.95)
     * @see PhaserControl#Depth
     */
    public void setDepth(float depth) {
        if (depth == this.depth)
            return;
        if (depth < 0.1f || depth > 0.95f)
            throw newRangeException(PhaserControl.Depth, "0.1..0.95", depth);
        this.depth = depth;
        set(PhaserControl.Depth, depth);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see PhaserControl#Feedback
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return get(PhaserControl.Feedback);
    }

    /**
     * @param feedback (0.1..0.95)
     * @see PhaserControl#Feedback
     */
    public void setFeedback(float feedback) {
        if (feedback == this.feedback)
            return;
        if (feedback < 0.1f || feedback > 0.95f)
            throw newRangeException(PhaserControl.Feedback, "0.1..0.95", feedback);
        this.feedback = feedback;
        set(PhaserControl.Feedback, feedback);
    }

    //----------------------------------
    // highFreq
    //----------------------------------

    /**
     * @see PhaserControl#HighFreq
     */
    public float getHighFreq() {
        return highFreq;
    }

    public float queryHighFreq() {
        return get(PhaserControl.HighFreq);
    }

    /**
     * @param highFreq (0.002..0.5)
     * @see PhaserControl#HighFreq
     */
    public void setHighFreq(float highFreq) {
        if (highFreq == this.highFreq)
            return;
        if (highFreq < 0.002f || highFreq > 0.5f)
            throw newRangeException(PhaserControl.HighFreq, "0.002..0.5", highFreq);
        this.highFreq = highFreq;
        set(PhaserControl.HighFreq, highFreq);
    }

    //----------------------------------
    //lowFreq
    //----------------------------------

    /**
     * @see PhaserControl#LowFreq
     */
    public float getLowFreq() {
        return lowFreq;
    }

    public float queryLowFreq() {
        return get(PhaserControl.LowFreq);
    }

    /**
     * @param lowFreq (0.002..0.5)
     * @see PhaserControl#LowFreq
     */
    public void setLowFreq(float lowFreq) {
        if (lowFreq == this.lowFreq)
            return;
        if (lowFreq < 0.002f || lowFreq > 0.5f)
            throw newRangeException(PhaserControl.LowFreq, "0.002..0.5", lowFreq);
        this.lowFreq = lowFreq;
        set(PhaserControl.LowFreq, lowFreq);
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see PhaserControl#Rate
     */
    public int getRate() {
        return rate;
    }

    public int queryRate() {
        return (int)get(PhaserControl.Rate);
    }

    /**
     * @param rate (2..50)
     * @see PhaserControl#Rate
     */
    public void setRate(int rate) {
        if (rate == this.rate)
            return;
        if (rate < 2 || rate > 50)
            throw newRangeException(PhaserControl.Rate, "2..50", rate);
        this.rate = rate;
        set(PhaserControl.Rate, rate);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PhaserEffect() {
        setType(EffectType.Phaser);
    }

    public PhaserEffect(int slot, int machineIndex) {
        super(slot, machineIndex);
        setType(EffectType.Phaser);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(PhaserControl.Depth, depth);
        set(PhaserControl.Feedback, feedback);
        set(PhaserControl.HighFreq, highFreq);
        set(PhaserControl.LowFreq, lowFreq);
        set(PhaserControl.Rate, rate);
    }

    @Override
    protected void restoreComponents() {
        setDepth(getDepth());
        setFeedback(getFeedback());
        setHighFreq(getHighFreq());
        setLowFreq(getLowFreq());
        setRate(getRate());
    }
}
