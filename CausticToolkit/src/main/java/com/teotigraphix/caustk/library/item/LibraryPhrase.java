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

import com.teotigraphix.caustk.pattern.PatternUtils;
import com.teotigraphix.caustk.tone.ToneType;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

public class LibraryPhrase extends LibraryItem {

    //----------------------------------
    //  name
    //----------------------------------

    String machineName;

    /**
     * The machine's name that this phrase was copied from in the .caustic file.
     */
    public final String getMachineName() {
        return machineName;
    }

    public final void setMachineName(String value) {
        machineName = value;
    }

    //----------------------------------
    //  bankIndex
    //----------------------------------

    private int bankIndex;

    public final int getBankIndex() {
        return bankIndex;
    }

    public final void setBankIndex(int value) {
        bankIndex = value;
    }

    //----------------------------------
    //  patternIndex
    //----------------------------------

    private int patternIndex;

    public final int getPatternIndex() {
        return patternIndex;
    }

    public final void setPatternIndex(int value) {
        patternIndex = value;
    }

    private Resolution resolution;

    public final Resolution getResolution() {
        return resolution;
    }

    public final void setResolution(Resolution value) {
        resolution = value;
    }

    //----------------------------------
    //  length
    //----------------------------------

    private int length;

    public final int getLength() {
        return length;
    }

    public final void setLength(int value) {
        length = value;
    }

    //----------------------------------
    //  swing
    //----------------------------------

    private String swing;

    public final String getSwing() {
        return swing;
    }

    public final void setSwing(String value) {
        swing = value;
    }

    //----------------------------------
    //  noteData
    //----------------------------------

    private String noteData = "";

    public final String getNoteData() {
        return noteData;
    }

    public final void setNoteData(String value) {
        noteData = value;
    }

    //----------------------------------
    //  toneType
    //----------------------------------

    private ToneType toneType;

    public final ToneType getToneType() {
        return toneType;
    }

    public final void setToneType(ToneType value) {
        toneType = value;
    }

    //----------------------------------
    //  tempo
    //----------------------------------

    private float tempo;

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float value) {
        tempo = value;
    }

    //--------------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------------

    public LibraryPhrase() {
    }

    @Override
    public String toString() {
        String name = getMetadataInfo().getName();
        if (name.equals("Untitled"))
            name = "";
        String pat = PatternUtils.toString(getBankIndex(), getPatternIndex());
        return name + "[" + pat + "] " + getMetadataInfo().getTags().toString();
    }

}
