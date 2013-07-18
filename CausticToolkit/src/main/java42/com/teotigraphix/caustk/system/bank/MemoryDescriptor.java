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

package com.teotigraphix.caustk.system.bank;

import java.util.List;

import com.teotigraphix.caustk.tone.ToneDescriptor;

public class MemoryDescriptor {

    public boolean fullLoad = false;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    private int index;

    public int getIndex() {
        return index;
    }

    /**
     * @param value
     * @see MemoryLoader#addDescriptor()
     */
    public void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // name
    //----------------------------------

    private String name;

    public String getName() {
        return name;
    }

    //----------------------------------
    // tones
    //----------------------------------

    private List<ToneDescriptor> toneDescriptors;

    public List<ToneDescriptor> getToneDescriptors() {
        return toneDescriptors;
    }

    public void setToneDescriptors(List<ToneDescriptor> value) {
        toneDescriptors = value;
    }

    //----------------------------------
    // patternItems
    //----------------------------------

    private List<PatternItem> patternItems;

    public List<PatternItem> getPatternItems() {
        return patternItems;
    }

    public void setPatternItems(List<PatternItem> value) {
        patternItems = value;
    }

    //----------------------------------
    // patchItems
    //----------------------------------

    private List<PhraseItem> phraseItems;

    public List<PhraseItem> getPhraseItems() {
        return phraseItems;
    }

    public void setPhraseItems(List<PhraseItem> value) {
        phraseItems = value;
    }

    //----------------------------------
    // patchItems
    //----------------------------------

    private List<PatchItem> patchItems;

    public List<PatchItem> getPatchItems() {
        return patchItems;
    }

    public void setPatchItems(List<PatchItem> value) {
        patchItems = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MemoryDescriptor(String name) {
        this.name = name;
    }

}
