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

package com.teotigraphix.caustic.effect;

import java.util.Map;

import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.effect.IEffect.EffectType;
import com.teotigraphix.caustic.internal.effect.EffectsRack.EffectData;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.rack.IRackAware;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IEffectsRack extends IDevice, IPersist, IRackAware
{

    /**
     * Adds an effec to the effects rack.
     * 
     * @param machine The machine to add an effect to.
     * @param slot The slot to add the effect at (0,1).
     * @param type The {@link EffectType} of the effect.
     * @return The new {@link IEffect} instance added.
     */
    IEffect putEffect(IMachine machine, int slot, EffectType type);

    /**
     * Removes an effect from the effects rack.
     * 
     * @param machine The machine to remove the effect from.
     * @param slot The slot to remove the effect from (0,1).
     * @return The removed {@link IEffect} or <code>null</code> if no effect
     * exists.
     */
    IEffect removeEffect(IMachine machine, int slot);

    /**
     * Returns the {@link IEffect} at the slot or <code>null</code> if no effect
     * exists.
     * 
     * @param machine The machine to find an effect for.
     * @param slot The slot on the effects rack (0,1).
     * @return The machine's {@link IEffect} or <code>null</code>.
     */
    IEffect getEffect(IMachine machine, int slot);

    /**
     * Returns whether the machine has an registered effects.
     * 
     * @param machine The machine to find an effect for.
     */
    boolean hasEffectsFor(IMachine machine);

    /**
     * Returns a map of existing effect on machines.
     * <p>
     * Only the machines that contain active effects are returned.
     */
    Map<Integer, EffectData> getEffects();

    /**
     * Returns a map of existing effects for the machine.
     * 
     * @param machine The machine to return effects for.
     */
    Map<Integer, IEffect> getEffectsFor(IMachine machine);

    /**
     * Copies a machine effect channel.
     * 
     * @param machine The machine to copy effects from.
     * @param memento The parent {@link IMemento}.
     */
    void copyChannel(IMachine machine, IMemento memento);

    /**
     * Pastes a machine effect channel.
     * 
     * @param machine The machine to paste effects to.
     * @param memento The parent {@link IMemento}.
     */
    void pasteChannel(IMachine machine, IMemento memento);

}
