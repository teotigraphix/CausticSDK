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

import java.util.Collection;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.sequencer.ISequencer;
import com.teotigraphix.caustic.song.ISong.ISongListener;
import com.teotigraphix.common.IDispose;

/**
 * An {@link ITrack} is a linear sequence that maps one {@link IMachine} within
 * the {@link ISequencer}.
 * <p>
 * The {@link ITrack} uses the {@link ITrackPattern} to mark a pattern that has
 * been added to the track.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ITrack extends IDispose {

    //--------------------------------------------------------------------------
    // 
    //  Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  index
    //----------------------------------

    /**
     * The track index within the {@link ITrackSong}.
     * <p>
     * The index relates to the part relationship and will always be the same as
     * the {@link IPart#getIndex()} value.
     */
    int getIndex();

    //----------------------------------
    //  name
    //----------------------------------

    /**
     * The track name within the {@link ITrackSong}.
     * <p>
     * The track name will always be the same as the {@link IPart#getName()}
     * value.
     */
    String getName();

    //----------------------------------
    //  currentBeat
    //----------------------------------

    /**
     * The track gets it's current beat from the
     * {@link ITrackSong#getCurrentBeat()}.
     */
    int getCurrentBeat();

    //----------------------------------
    //  currentMeasure
    //----------------------------------

    /**
     * The track gets it's current measure from the
     * {@link ITrackSong#getCurrentMeasure()}.
     */
    int getCurrentMeasure();

    //----------------------------------
    //  song
    //----------------------------------

    /**
     * Returns the {@link ITrackSong} that owns this track.
     * <p>
     * The track listens to the {@link ISongListener} API and when removed from
     * the song, removes itself from that notification.
     */
    ITrackSong getSong();

    //----------------------------------
    //  patterns
    //----------------------------------

    /**
     * Returns the current {@link ITrackPattern}s registered within the track.
     */
    Collection<ITrackPattern> getPatterns();

    //----------------------------------
    //  part
    //----------------------------------

    /**
     * The current {@link IPart} that is playing the {@link ITrackPattern} song
     * data.
     */
    IPart getPart();

    /**
     * Adds a pattern at the current measure of the track.
     * 
     * @param phrase
     * @return
     * @throws CausticException
     */
    ITrackPattern addPattern(IPhrase phrase) throws CausticException;

    ITrackPattern addPatternAt(int measure, IPhrase phrase) throws CausticException;

    ITrackPattern addPattern(IPhrase phrase, int virtualLength) throws CausticException;

    ITrackPattern addPatternAt(int measure, IPhrase phrase, int virtualLegnth)
            throws CausticException;

    ITrackPattern removePattern(int index);

    void addTrackListener(ITrackListener value);

    void removeTrackListener(ITrackListener value);

    public interface ITrackListener {
        void onPatternAdded(ITrackPattern pattern);

        void onPatternRemoved(ITrackPattern pattern);
    }

}
