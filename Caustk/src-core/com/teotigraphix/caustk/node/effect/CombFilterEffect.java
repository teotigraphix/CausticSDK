////////////////////////////////////////////////////////////////////////////////
//Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.CombFilterControl;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link CombFilterEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CombFilterEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private int freq = 10; // Phaser.rate

    @Tag(201)
    private float reso = 0.47f; // Phaser.feedback

    @Tag(202)
    private float wet = 0.8f; // Phaser.depth

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // freq
    //----------------------------------

    /**
     * @see CombFilterControl#Freq
     */
    public int getFreq() {
        return freq;
    }

    public int queryFreq() {
        return (int)get(CombFilterControl.Freq);
    }

    /**
     * @param freq (2..50)
     * @see CombFilterControl#Freq
     */
    public void setFreq(int freq) {
        if (freq == this.freq)
            return;
        if (freq < 2 || freq > 50)
            throw newRangeException(CombFilterControl.Freq, "2..50", freq);
        this.freq = freq;
        set(CombFilterControl.Freq, freq);
    }

    //----------------------------------
    // reso
    //----------------------------------

    /**
     * @see CombFilterControl#Reso
     */
    public float getReso() {
        return reso;
    }

    public float queryReso() {
        return get(CombFilterControl.Reso);
    }

    /**
     * @param freq (0.1..0.95)
     * @see CombFilterControl#Reso
     */
    public void setReso(float reso) {
        if (reso == this.reso)
            return;
        if (reso < 0f || reso > 0.95f)
            throw newRangeException(CombFilterControl.Reso, "0..0.95", reso);
        this.reso = reso;
        set(CombFilterControl.Reso, reso);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see CombFilterControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(CombFilterControl.Wet);
    }

    /**
     * @param freq (0.1..0.95)
     * @see CombFilterControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 0.9f)
            throw newRangeException(CombFilterControl.Wet, "0..0.9", wet);
        this.wet = wet;
        set(CombFilterControl.Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public CombFilterEffect() {
    }

    public CombFilterEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.CombFilter);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(CombFilterControl.Freq, getFreq());
        set(CombFilterControl.Reso, getFreq());
        set(CombFilterControl.Wet, getWet());
    }

    @Override
    protected void restoreComponents() {
        setFreq(queryFreq());
        setReso(queryReso());
        setWet(queryWet());
    }
}
