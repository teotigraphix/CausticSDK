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

package com.teotigraphix.caustk.workstation;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;

/**
 * @author Michael Schmalle
 */
public class PartReference {

    @Tag(0)
    private int patternIndex;

    @Tag(1)
    private Patch patch;

    @Tag(2)
    private Phrase phrase;

    public Patch getPatch() {
        return patch;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    PartReference() {
    }

    PartReference(Part part, int patternIndex) {
        this.patternIndex = patternIndex;
    }

    public void update(ICaustkApplicationContext context, Part part) {
        // copy the patch
        Patch sourcePatch = part.getPatch();
        Machine machine = sourcePatch.getMachine();
        sourcePatch.setMachine(null);
        patch = context.getFactory().getKryo().copy(sourcePatch);
        sourcePatch.setMachine(machine);

        // copy the phrase
        Phrase sourcePhrase = part.getPhrase();
        machine = sourcePhrase.getMachine();
        sourcePhrase.setMachine(null);
        phrase = context.getFactory().getKryo().copy(sourcePhrase);
        phrase.setIndex(patternIndex);
        sourcePhrase.setMachine(machine);
    }

}
