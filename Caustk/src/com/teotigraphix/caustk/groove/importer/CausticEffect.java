////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.groove.importer;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectType;

/*
<effect displayName="Foo Effect">
<type index="0" type="Autowah"/>
<type index="1" type="Delay"/>
</effect>
*/
public class CausticEffect extends CausticItem {

    private Map<Integer, CausticEffectType> types = new HashMap<Integer, CausticEffectType>();

    private transient Map<Integer, EffectNode> effects = new HashMap<Integer, EffectNode>();

    public EffectType get(int index) {
        if (!types.containsKey(index))
            return null;
        return types.get(index).getType();
    }

    public EffectNode getEffect(int slot) {
        return effects.get(slot);
    }

    public void putEffect(int slot, EffectNode effect) {
        if (effects == null)
            effects = new HashMap<Integer, EffectNode>();
        effects.put(slot, effect);
    }

    public CausticEffect(LibraryEffect item) {
        super(item);

        EffectType type1 = null;
        EffectType type2 = null;
        if (item.get(0) != null)
            type1 = item.get(0).getType();
        if (item.get(1) != null)
            type2 = item.get(1).getType();

        types.put(0, new CausticEffectType(0, type1));
        types.put(1, new CausticEffectType(1, type2));
    }

    @Override
    public String toString() {
        return "CausticEffect [\ndisplayName=" + getDisplayName() + "\n]";
    }
}
