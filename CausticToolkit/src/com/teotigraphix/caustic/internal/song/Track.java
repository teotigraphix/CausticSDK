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

package com.teotigraphix.caustic.internal.song;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.ITrack;
import com.teotigraphix.caustic.song.ITrackPattern;
import com.teotigraphix.caustic.song.ITrackSong;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Track implements ITrack {

    //--------------------------------------------------------------------------
    // 
    //  ITrack API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  index
    //----------------------------------

    @Override
    public int getIndex() {
        return mPart.getIndex();
    }

    //----------------------------------
    //  name
    //----------------------------------

    @Override
    public String getName() {
        return mPart.getName();
    }

    //----------------------------------
    //  currentBeat
    //----------------------------------

    @Override
    public int getCurrentBeat() {
        return mSong.getCurrentBeat();
    }

    //----------------------------------
    //  currentMeasure
    //----------------------------------

    @Override
    public int getCurrentMeasure() {
        return mSong.getCurrentMeasure();
    }

    //----------------------------------
    //  song
    //----------------------------------

    private ITrackSong mSong;

    @Override
    public ITrackSong getSong() {
        return mSong;
    }

    void setSong(ITrackSong value) {
        mSong = value;
    }

    //----------------------------------
    //  patterns
    //----------------------------------

    private Map<Integer, ITrackPattern> mPatterns = new TreeMap<Integer, ITrackPattern>();

    @Override
    public Collection<ITrackPattern> getPatterns() {
        return mPatterns.values();
    }

    //----------------------------------
    //  part
    //----------------------------------

    private IPart mPart;

    @Override
    public IPart getPart() {
        return mPart;
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    /**
     * Creates a new track using the {@link IPart} as the sound generator and
     * phrase source.
     * 
     * @param part The part owner.
     */
    public Track(IPart part) {
        mPart = part;
    }

    //--------------------------------------------------------------------------
    // 
    //  ITrack API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public ITrackPattern addPattern(IPhrase phrase) throws CausticException {
        return addPatternAt(getCurrentMeasure(), phrase, phrase.getLength());
    }

    @Override
    public ITrackPattern addPattern(IPhrase phrase, int virtualLength) throws CausticException {
        return addPatternAt(getCurrentMeasure(), phrase, virtualLength);
    }

    @Override
    public ITrackPattern addPatternAt(int measure, IPhrase phrase) throws CausticException {
        return addPatternAt(measure, phrase, phrase.getLength());
    }

    @Override
    public ITrackPattern addPatternAt(int measure, IPhrase phrase, int virtualLegnth)
            throws CausticException {

        if (mPatterns.containsKey(measure))
            throw new CausticException("Patterns contain phrase at: " + measure);

        // no rolling update needed, just added to the end.
        ITrackPattern pattern = new TrackPattern();
        pattern.setTrack(this);
        pattern.setPhrase(phrase);

        pattern.setStartMeasure(measure);
        pattern.setEndMeasure(measure + virtualLegnth);

        mPatterns.put(measure, pattern);

        // ITrackSong listens to this and will send a pattern event
        firePatternAdded(pattern);

        return pattern;
    }

    //--------------------------------------------------------------------------
    // 
    //  IDispose API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public void dispose() {
        mTrackListeners.clear();
        mPart = null;
    }

    ITrackPattern getPattern(IPhrase phrase, int start) {
        //for (ITrackPattern pattern : mPatternPool) {
        //	if (phrase.getBank() == pattern.getBank()
        //			&& phrase.getIndex() == pattern.getIndex()) {
        //		return pattern;
        //	}
        //}
        return mPatterns.get(start);
    }

    public ITrackPattern removePattern(int index) {
        ITrackPattern pattern = null;
        //pattern.setTrack(null);
        //mPatterns.remove(pattern.getStartMeasure());
        firePatternRemoved(pattern);
        return pattern;
    }

    //----------------------------------
    //  Track listeners
    //----------------------------------

    private final List<ITrackListener> mTrackListeners = new ArrayList<ITrackListener>();

    @Override
    public final void addTrackListener(ITrackListener value) {
        if (mTrackListeners.contains(value))
            return;
        mTrackListeners.add(value);
    }

    @Override
    public final void removeTrackListener(ITrackListener value) {
        if (!mTrackListeners.contains(value))
            return;
        mTrackListeners.remove(value);
    }

    void firePatternAdded(ITrackPattern pattern) {
        for (ITrackListener listener : mTrackListeners) {
            listener.onPatternAdded(pattern);
        }
    }

    void firePatternRemoved(ITrackPattern pattern) {
        for (ITrackListener listener : mTrackListeners) {
            listener.onPatternRemoved(pattern);
        }
    }

}
