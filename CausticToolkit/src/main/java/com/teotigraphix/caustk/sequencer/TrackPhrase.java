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

package com.teotigraphix.caustk.sequencer;

import java.util.UUID;

/*

What is a track pattern;
- a placeholder for pattern slots within a Track
- has assigned bank/pattern index


*/

public class TrackPhrase {
    //----------------------------------
    //  numMeasures
    //----------------------------------

    private int numMeasures;

    public final int getNumMeasures() {
        return numMeasures;
    }

    public final void setNumMeasures(int value) {
        numMeasures = value;
    }

    //----------------------------------
    //  id
    //----------------------------------

    private UUID id;

    public final UUID getId() {
        return id;
    }

    public final void setId(UUID value) {
        id = value;
    }

    //----------------------------------
    //  bankIndex
    //----------------------------------

    private int bankIndex;

    public final int getBankIndex() {
        return bankIndex;
    }

    public final void setBankIndex(int bankIndex) {
        this.bankIndex = bankIndex;
    }

    //----------------------------------
    //  patternIndex
    //----------------------------------

    private int patternIndex;

    public final int getPatternIndex() {
        return patternIndex;
    }

    public final void setPatternIndex(int patternIndex) {
        this.patternIndex = patternIndex;
    }

    //----------------------------------
    //  noteData
    //----------------------------------

    private String noteData;

    public final String getNoteData() {
        return noteData;
    }

    public final void setNoteData(String value) {
        noteData = value;
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public TrackPhrase() {
    }

}
