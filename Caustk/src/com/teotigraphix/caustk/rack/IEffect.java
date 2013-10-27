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

package com.teotigraphix.caustk.rack;

import com.teotigraphix.caustk.core.IRackAware;
import com.teotigraphix.caustk.core.IRackSerializer;
import com.teotigraphix.caustk.rack.effect.EffectType;

/**
 * @author Michael Schmalle
 */
public interface IEffect extends IRackAware, IRackSerializer {

    /**
     * Returns the {@link EffectType} of the effect, assigned when constructed.
     */
    EffectType getType();

    /**
     * Returns the tone index the effect is assigned to.
     */
    int getToneIndex();

    /**
     * Sets the new tone index for the effect.
     * <p>
     * It is up to the client to use correct OSC commands to change tones.
     * 
     * @param value The new tone index.
     */
    void setToneIndex(int value);

    /**
     * Returns the slot at which the effect exist in the machines mixer chain.
     */
    int getSlot();

    /**
     * Sets the new slot index on a tone for the effect.
     * <p>
     * It is up to the client to use correct OSC commands to change tone effect
     * slots.
     * 
     * @param value The new effect slot index.
     */
    void setSlot(int value);

}
