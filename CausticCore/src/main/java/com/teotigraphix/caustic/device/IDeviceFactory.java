////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.device;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.effect.IEffect;
import com.teotigraphix.caustic.effect.IEffect.EffectType;
import com.teotigraphix.caustic.effect.IEffectComponent;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.mixer.IMixerPanel;
import com.teotigraphix.caustic.mixer.MixerEffectType;
import com.teotigraphix.caustic.output.IOutputPanel;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.IStepPhrase;
import com.teotigraphix.caustic.sequencer.ISequencer;

/**
 * The IRackFactory interface is used to create top level devices and effects
 * within the IRack.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IDeviceFactory {

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Creates the single IMixerPanel for the IRack.
     */
    IMixerPanel createMixerPanel();

    /**
     * Creates the single IEffectPanel for the IRack.
     */
    IEffectsRack createEffectRack();

    /**
     * Creates the single IOutputPanel for the IRack.
     */
    IOutputPanel createOutputPanel();

    /**
     * Creates the single ISequencer for the IRack.
     */
    ISequencer createSequencer();

    IStepPhrase createStepPhrase(int bank, int index);

    /**
     * Creates a pattern sequencer for a machine.
     * 
     * @param machine The sequencer aware machine.
     */
    IPatternSequencer createPatternSequencer(IMachine machine);

    /**
     * Creates a unique {@link IMachine} instance for the IRack using the
     * machineId.
     * 
     * @param machineID The String id of the machine.
     * @param machineType The type of {@link IMachine} to create.
     * @return The new machine instance of type.
     * @throws CausticException
     */
    IMachine create(String machineId, MachineType machineType) throws CausticException;

    /**
     * Creates an IMixerPanel effect.
     * 
     * @param mixer The mixer.
     * @param type The MixerEffectType to create.
     */
    IEffectComponent createMixerEffect(IMixerPanel mixer, MixerEffectType type);

    IEffect createEffect(IEffectsRack panel, int index, EffectType type);

}
