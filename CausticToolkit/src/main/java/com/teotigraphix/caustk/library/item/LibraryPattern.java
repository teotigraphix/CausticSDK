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

package com.teotigraphix.caustk.library.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustk.library.core.MetadataInfo;
import com.teotigraphix.caustk.tone.ToneDescriptor;
import com.teotigraphix.caustk.tone.ToneType;

/**
 * The {@link LibraryPattern} is a collection of library items linked together
 * by the name tag of the original machine.
 * <p>
 * In a 14 track caustic file, each track is labeled PART1A, PART1B etc. Each
 * part is then combined together to form the full part items for the pattern.
 */
public class LibraryPattern extends LibraryItem {

    public LibraryPattern createDefault() {
        LibraryPattern item = new LibraryPattern();
        item.setId(UUID.randomUUID());
        item.setIndex(-1);
        item.setLibrary(null);
        item.setMetadataInfo(new MetadataInfo());
        List<ToneDescriptor> descriptors = new ArrayList<ToneDescriptor>();
        descriptors.add(new ToneDescriptor(0, "part1", ToneType.Bassline));
        descriptors.add(new ToneDescriptor(1, "part2", ToneType.Bassline));
        item.setToneSet(toneSet);
        return item;
    }

    private ToneSet toneSet;

    public final ToneSet getToneSet() {
        return toneSet;
    }

    public final void setToneSet(ToneSet value) {
        toneSet = value;
    }

    public LibraryPattern() {

    }

    @Override
    public int getIndex() {
        // TODO Auto-generated method stub
        //XXX REMOVE since this shouldn't be useful with new LibraryPatterns
        return 0;
    }

    public float getTempo() {
        // TODO Auto-generated method stub
        return 120;
    }

    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ToneSet {
        private List<ToneDescriptor> descriptors = new ArrayList<ToneDescriptor>();

        public final List<ToneDescriptor> getDescriptors() {
            return descriptors;
        }

        public ToneSet(List<ToneDescriptor> descriptors) {
            this.descriptors = descriptors;
        }
    }

    private Map<Integer, UUID> partPhrases = new HashMap<Integer, UUID>();

    public void putPhrase(int partIndex, LibraryPhrase phrase) {
        if (phrase == null)
            return;
        partPhrases.put(partIndex, phrase.getId());
    }

    public UUID getPhrase(int index) {
        return partPhrases.get(index);
    }

}
