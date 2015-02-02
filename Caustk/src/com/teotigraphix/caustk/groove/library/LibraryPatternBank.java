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

package com.teotigraphix.caustk.groove.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryPatternBankManifest;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.machine.sequencer.SequencerChannel;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryPatternBank extends LibraryProductItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private LibraryPatternBankManifest manifest;

    //private Map<Integer, Map<Integer, PatternNode>> patterns = new HashMap<Integer, Map<Integer, PatternNode>>();
    @Tag(51)
    private SequencerChannel sequencer;

    //--------------------------------------------------------------------------
    // Transient :: Variables
    //--------------------------------------------------------------------------

    private transient LibrarySound sound;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    //  manifest
    //----------------------------------

    @Override
    public LibraryPatternBankManifest getManifest() {
        return manifest;
    }

    //----------------------------------
    //  sound
    //----------------------------------

    public LibrarySound getSound() {
        return sound;
    }

    public void setSound(LibrarySound sound) {
        this.sound = sound;
    }

    //--------------------------------------------------------------------------
    //  Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization.
     */
    LibraryPatternBank() {
    }

    public LibraryPatternBank(LibraryPatternBankManifest manifest, MachineNode machineNode) {
        this.manifest = manifest;
        manifest.setMachineType(machineNode.getType());
        this.sequencer = machineNode.getSequencer();
        this.sequencer.setMachineNode(null);
    }

    /**
     * Returns a sorted copy of the sequencers
     * {@link com.teotigraphix.caustk.node.machine.sequencer.PatternNode}s.
     */
    public Collection<PatternNode> getPatterns() {
        Collection<PatternNode> patterns = sequencer.getPatterns();
        List<PatternNode> result = new ArrayList<PatternNode>(patterns);
        Collections.sort(result, new Comparator<PatternNode>() {
            @Override
            public int compare(PatternNode lhs, PatternNode rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        return result;
    }
}
