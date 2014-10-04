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

import com.teotigraphix.caustk.node.effect.EffectType;

/*
<effect displayName="Foo Effect">
<type index="0" type="Autowah"/>
<type index="1" type="Delay"/>
</effect>
*/
public class CausticEffect extends CausticItem {

    private Map<Integer, CausticEffectType> types = new HashMap<Integer, CausticEffectType>();

    public EffectType get(int index) {
        if (!types.containsKey(index))
            return null;
        return types.get(index).getType();
    }

    public CausticEffect(String path, String displayName, EffectType effect1, EffectType effect2) {
        super(path, displayName);
        types.put(0, new CausticEffectType(0, effect1));
        types.put(1, new CausticEffectType(1, effect2));
    }

    @Override
    public String toString() {
        return "CausticEffect [\ndisplayName=" + getDisplayName() + "\n]";
    }
}
