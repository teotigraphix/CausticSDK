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

package com.teotigraphix.caustk.machine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.utils.PatternUtils;

public class CaustkMachine implements ICaustkComponent {

    /*
     * The tone is only set when the LiveMachine is actually assigned to a channel
     * in the LiveScene.
     */
    private transient Tone tone;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(1)
    private UUID id;

    @Tag(1)
    private String name;

    @Tag(1)
    private File file;

    @Tag(0)
    private int index;

    @Tag(1)
    private MachineType machineType;

    @Tag(2)
    private CaustkPatch patch;

    @Tag(3)
    private Map<Integer, CaustkPhrase> phrases = new HashMap<Integer, CaustkPhrase>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    @Override
    public UUID getId() {
        return id;
    }

    //----------------------------------
    // name
    //----------------------------------

    @Override
    public String getName() {
        return name;
    }

    //----------------------------------
    // file
    //----------------------------------

    @Override
    public File getFile() {
        return file;
    }

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return index;
    }

    //----------------------------------
    // machineType
    //----------------------------------

    public MachineType getMachineType() {
        return machineType;
    }

    //----------------------------------
    // patch
    //----------------------------------

    public CaustkPatch getPatch() {
        return patch;
    }

    //----------------------------------
    // phrases
    //----------------------------------

    public Map<Integer, CaustkPhrase> getPhrases() {
        return phrases;
    }

    public Tone getTone() {
        return tone;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkMachine() {
    }

    CaustkMachine(UUID id, MachineType machineType) {
        this.id = id;
        this.machineType = machineType;
    }

    CaustkMachine(UUID id, MachineType machineType, int index, String name) {
        this.id = id;
        this.machineType = machineType;
        this.index = index;
        this.name = name;
    }

    public void load(CaustkLibraryFactory factory) throws IOException, CausticException {
        final IRack rack = factory.getRack();

        // CaustkPatch
        patch = factory.createPatch(this);
        patch.load(factory);

        // load phrases
        String patterns = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(rack, index);
        if (patterns != null) {
            createPatternsFromLoadOperation(factory, patterns);
        }

    }

    private void createPatternsFromLoadOperation(CaustkLibraryFactory factory, String patterns)
            throws CausticException {
        final IRack rack = factory.getRack();

        for (String patternName : patterns.split(" ")) {
            int bankIndex = PatternUtils.toBank(patternName);
            int patternIndex = PatternUtils.toPattern(patternName);

            CaustkPhrase caustkPhrase = factory.createPhrase(this, bankIndex, patternIndex);
            phrases.put(caustkPhrase.getIndex(), caustkPhrase);
            caustkPhrase.load(factory);
        }
    }
}
