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

package com.teotigraphix.caustk.live;

import com.teotigraphix.caustk.rack.effect.EffectType;

public class EffectFactory extends CaustkSubFactoryBase {

    public EffectFactory() {
    }

    public Effect createEffect(ComponentInfo info, EffectType effectType) {
        Effect caustkEffect = new Effect(info, effectType);
        // create the internal IEffect instance with slot and toneIndex set to -1
        caustkEffect.create(null);
        return caustkEffect;
    }

    public Effect createEffect(int slot, EffectType effectType) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Effect);
        Effect caustkEffect = new Effect(info, slot, effectType);
        return caustkEffect;
    }

    public Effect createEffect(int slot, EffectType effectType, Patch caustkPatch) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Effect);
        Effect caustkEffect = new Effect(info, slot, effectType, caustkPatch);
        return caustkEffect;
    }

}
