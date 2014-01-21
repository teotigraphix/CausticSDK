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

package com.teotigraphix.caustk.node;

import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectsChannelNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.PresetNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.master.MasterNode;

/**
 * The {@link NodeBase} node types.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public enum NodeType {

    Library("library"),

    /**
     * A serialized {@link RackNode} with full state save.
     * <p>
     * This archive will contain;
     * <ul>
     * <li>/Archive/manifest.json</li>
     * <li>/Archive/presets</li>
     * <li>/Archive/presets/Machine_[i..n].preset</li>
     * </ul>
     */
    Rack("rack"),

    /**
     * A serialized {@link MasterNode}.
     */
    Master("master"),

    /**
     * A serialized {@link PresetNode} with the {@link EffectsChannelNode} and
     * it's {@link EffectNode}s.
     */
    Patch("patch"),

    Effect("effect"),

    /**
     * A serialized {@link PatternNode}.
     */
    Pattern("pattern"),

    /**
     * A serialized PatternSetNode.
     */
    PatternSet("patternset"),

    /**
     * A serialized {@link MachineNode}.
     */
    Machine("machine");

    private final String extension;

    public final String getExtension() {
        return extension;
    }

    NodeType(String extension) {
        this.extension = extension;
    }
}
