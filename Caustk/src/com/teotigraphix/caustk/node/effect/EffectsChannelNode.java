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

package com.teotigraphix.caustk.node.effect;

import java.util.HashMap;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The effect channel node, currently holds 2 slots.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see MachineNode#getEffects()
 */
public class EffectsChannelNode extends NodeBase {

    // If machineIndex is -1, this is the master effect channel

    private static final int NUM_SLOTS = 2;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private HashMap<Integer, EffectNode> slots = new HashMap<Integer, EffectNode>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // slots
    //----------------------------------

    /**
     * Returns whether the effects channel contains en {@link EffectNode} at the
     * specified slot.
     * 
     * @param slot The slot index (0,1).
     */
    public boolean containsEffect(int slot) {
        return slots.containsKey(slot);
    }

    /**
     * Returns an {@link EffectNode} at the specified slot or <code>null</code>.
     * 
     * @param slot The slot index (0,1).
     */
    public EffectNode getEfffect(int slot) {
        return slots.get(slot);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public EffectsChannelNode() {
    }

    public EffectsChannelNode(int machineIndex) {
        this.index = machineIndex;
    }

    public EffectsChannelNode(MachineNode machineNode) {
        this(machineNode.getIndex());
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates a {@link EffectNode} in the {@link #getEffects()} channel and
     * returns the new instance.
     * 
     * @param slot The effect slot (0,1).
     * @param effectType The {@link EffectType}.
     * @return The new {@link EffectNode}.
     * @throws CausticException Effect channel contains effect at slot
     */
    @SuppressWarnings("unchecked")
    public <T extends EffectNode> T createEffect(int slot, EffectType effectType)
            throws CausticException {
        if (containsEffect(slot))
            throw new CausticException("Effect channel contains effect at slot: " + slot);

        EffectNode effect = getFactory().createEffect(index, slot, effectType);
        EffectsRackMessage.CREATE.send(getRack(), effect.getMachineIndex(), effect.getSlot(),
                effect.getType().getValue());
        set(effect);

        return (T)effect;
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (containsEffect(i)) {
                EffectNode effectNode = getEfffect(i);
                EffectsRackMessage.CREATE.send(getRack(), effectNode.getMachineIndex(),
                        effectNode.getSlot(), effectNode.getType().getValue());
                effectNode.update();
            }
        }
    }

    @Override
    protected void restoreComponents() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            EffectType type = EffectType.fromInt((int)EffectsRackMessage.TYPE.send(getRack(),
                    index, i));
            if (type != null) {
                EffectNode effect;
                try {
                    effect = createEffect(i, type);
                    effect.restore();
                } catch (CausticException e) {
                    getLogger().err("EffectsChannelNode", e.getMessage());
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void set(EffectNode effectNode) {
        slots.put(effectNode.getSlot(), effectNode);
    }
}
