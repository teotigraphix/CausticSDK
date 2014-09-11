////////////////////////////////////////////////////////////////////////////////
//Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, software 
//distributed under the License is distributed on an "AS IS" BASIS, 
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and 
//limitations under the License
//
//Author: Michael Schmalle, Principal Architect
//mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node.effect;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.AutowahControl;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link AutoWahEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class AutoWahEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float cutoff = 2.23f;

    @Tag(201)
    private float depth = 1f;

    @Tag(202)
    private float resonance = 0.5f;

    @Tag(203)
    private float speed = 0.4f;

    @Tag(204)
    private float wet = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    /**
     * @see AutowahControl#Cutoff
     */
    public float getCutoff() {
        return cutoff;
    }

    public float queryCutoff() {
        return get(AutowahControl.Cutoff);
    }

    /**
     * @see AutowahControl#Cutoff
     * @param cutoff (0.5..4.0)
     */
    public void setCutoff(float cutoff) {
        if (cutoff == this.cutoff)
            return;
        if (cutoff < 0.5f || cutoff > 4.0f)
            throw newRangeException(AutowahControl.Cutoff, "0.5..4.0", cutoff);
        this.cutoff = cutoff;
        set(AutowahControl.Cutoff, cutoff);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see AutowahControl#Depth
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return get(AutowahControl.Depth);
    }

    /**
     * @param depth (0..1)
     * @see AutowahControl#Depth
     */
    public void setDepth(float depth) {
        if (depth == this.depth)
            return;
        if (depth < 0f || depth > 1f)
            throw newRangeException(AutowahControl.Depth, "0..1", depth);
        this.depth = depth;
        set(AutowahControl.Depth, depth);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    /**
     * @see AutowahControl#Resonance
     */
    public float getResonance() {
        return resonance;
    }

    public float queryResonance() {
        return get(AutowahControl.Resonance);
    }

    /**
     * @param resonance (0..1)
     * @see AutowahControl#Resonance
     */
    public void setResonance(float resonance) {
        if (resonance == this.resonance)
            return;
        if (resonance < 0f || resonance > 1f)
            throw newRangeException(AutowahControl.Resonance, "0..1", resonance);
        this.resonance = resonance;
        set(AutowahControl.Resonance, resonance);
    }

    //----------------------------------
    // speed
    //----------------------------------

    /**
     * @see AutowahControl#Speed
     */
    public float getSpeed() {
        return speed;
    }

    public float querySpeed() {
        return get(AutowahControl.Speed);
    }

    /**
     * @param speed (0..0.5)
     * @see AutowahControl#Speed
     */
    public void setSpeed(float speed) {
        if (speed == this.speed)
            return;
        if (speed < 0f || speed > 0.5f)
            throw newRangeException(AutowahControl.Speed, "0..0.5", speed);
        this.speed = speed;
        set(AutowahControl.Speed, speed);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see AutowahControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(AutowahControl.Wet);
    }

    /**
     * @param wet (0..1)
     * @see AutowahControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 1f)
            throw newRangeException(AutowahControl.Wet, "0..1", wet);
        this.wet = wet;
        set(AutowahControl.Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public AutoWahEffect() {
    }

    public AutoWahEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Autowah);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(AutowahControl.Cutoff, cutoff);
        set(AutowahControl.Depth, depth);
        set(AutowahControl.Resonance, resonance);
        set(AutowahControl.Speed, speed);
        set(AutowahControl.Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setCutoff(queryCutoff());
        setDepth(queryDepth());
        setResonance(queryResonance());
        setSpeed(querySpeed());
        setWet(queryWet());
    }
}
