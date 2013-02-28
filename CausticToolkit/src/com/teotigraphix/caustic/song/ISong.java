////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

import com.teotigraphix.caustic.internal.song.SongData;
import com.teotigraphix.caustic.internal.song.SongPatternInfo;
import com.teotigraphix.caustic.sequencer.ISongSequencer;
import com.teotigraphix.common.IDispose;
import com.teotigraphix.common.IPersist;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISong extends ISongSequencer, IDispose, IPersist {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // project
    //----------------------------------

    /**
     * Return the ISong parent IProject.
     */
    IProject getProject();

    /**
     * @see #getProject()
     */
    void setProject(IProject value);

    //----------------------------------
    // data
    //----------------------------------

    /**
     * Return data associated with the song.
     */
    SongData getData();

    /**
     * @see #getData()
     */
    void setData(SongData value);

    //--------------------------------------------------------------------------
    //
    // Listeners
    //
    //--------------------------------------------------------------------------

    void addSongListener(ISongListener value);

    void removeSongListener(ISongListener value);

    public interface ISongListener {

        /**
         * @param beat The new beat Integer.
         * @param oldBeat The old beat Integer.
         */
        void onBeatChanged(int beat, int oldBeat);

        /**
         * @param bar The new measure Integer.
         * @param oldBar The old measure Integer.
         */
        void onMeasureChanged(int measure, int oldMeasure);

        /**
         * @param info {@link SongPatternInfo}
         */
        void onPatternAdded(SongPatternInfo info);

        /**
         * @param info {@link SongPatternInfo}
         */
        void onPatternRemoved(SongPatternInfo info);
    }

}
