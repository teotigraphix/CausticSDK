////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.song;

import java.util.List;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPartSong extends ISong {

    //----------------------------------
    //  patterns
    //----------------------------------

    /**
     * Return the ISong's IPattern collection.
     * <p>
     * Note: This collection contains the references to the
     * {@link IProject#getPatterns()} patterns that have been inserted into the
     * song.
     * </p>
     * 
     * @return
     */
    List<IPattern> getPatterns();

    //void setPatterns(List<IPattern> value);

    //----------------------------------
    //  numPatterns
    //----------------------------------

    int getNumPatterns();

    //----------------------------------
    //  selectedPattern
    //----------------------------------

    /**
     * Return the {@link IPattern} which relates to the
     * {@link #getCurrentBeat()}.
     */
    IPattern getSelectedPattern();

    //--------------------------------------------------------------------------
    // 
    //  Methods
    // 
    //--------------------------------------------------------------------------

    /**
     * Adds an {@link IPartPattern} at the end of the current pattern list.
     * <p>
     * Note; The song calculates the startBar based off of the last pattern's
     * position plus it's length and the endBar off of the pattern's length plus
     * the startBar calculation.
     * </p>
     * 
     * @param pattern The pattern to add.
     */
    void addPattern(IPartPattern pattern);

    void addPatternAt(IPartPattern pattern, int startBar, int endBar);

    IPartPattern removePatternAt(int startBar, int endBar);

    List<IPartPattern> removeAllPatterns();

    IPartPattern getPatternAt(int bar);

    boolean hasPatternAt(int bar);
}
