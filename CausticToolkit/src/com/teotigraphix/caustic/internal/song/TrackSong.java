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
import com.teotigraphix.caustic.internal.part.Part;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.ITone;
import com.teotigraphix.caustic.song.ITrack;
import com.teotigraphix.caustic.song.ITrack.ITrackListener;
import com.teotigraphix.caustic.song.ITrackPattern;
import com.teotigraphix.caustic.song.ITrackSong;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class TrackSong extends Song implements ITrackSong, ITrackListener {

    @Override
    public int getNumBeats() {
        if (mLastPatternInTracks == null)
            return 0;
        int measures = mLastPatternInTracks.getEndMeasure();
        return measures * 4;
    }

    @Override
    public int getNumMeasures() {
        if (mLastPatternInTracks == null)
            return 0;
        // 0 index, we need to use the end measure that is measures + 1
        int measures = mLastPatternInTracks.getEndMeasure();
        return measures;
    }

    @Override
    public int getTotalTime() {
        float bpm = getProject().getWorkspace().getRack().getOutputPanel().getBPM();
        float timeInSec = 60 / bpm;
        float totalNumBeats = getNumBeats() + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    @Override
    public int getCurrentTime() {
        float bpm = getProject().getWorkspace().getRack().getOutputPanel().getBPM();
        float timeInSec = 60 / bpm;
        float totalNumBeats = (getCurrentMeasure() * 4) + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    //--------------------------------------------------------------------------
    // 
    //  ITrackSong API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  tracks
    //----------------------------------

    private Map<Integer, ITrack> mTrackMap = new TreeMap<Integer, ITrack>();

    /*
     * The last pattern in the song in All tracks. This is used to easily
     * calculate the measure length of the song.
     */
    private ITrackPattern mLastPatternInTracks;

    @Override
    public Collection<ITrack> getTracks() {
        return mTrackMap.values();
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public TrackSong(String name) {
        super(name);
    }

    //--------------------------------------------------------------------------
    // 
    //  ITrackSong API :: Methods
    // 
    //--------------------------------------------------------------------------

    public void removeTrack(ITrack track) {
        mTrackMap.remove(track.getIndex());
        ((Track)track).setSong(null);
        track.removeTrackListener(this);
        fireTrackRemove(track);
    }

    @Override
    public ITrack getTrack(IPart part) {
        return getTrack(part.getIndex());
    }

    @Override
    public ITrack getTrack(int index) {
        return mTrackMap.get(index);
    }

    @Override
    public ITrack addTrack(IPart part) {
        Track track = new Track(part);
        track.addTrackListener(this);
        mTrackMap.put(part.getIndex(), track);
        track.setSong(this);
        fireTrackAdd(track);
        return track;
    }

    @Override
    public ITrack removeTrack(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeAllTracks() {
        List<ITrack> result = new ArrayList<ITrack>(mTrackMap.values());
        for (ITrack track : result) {
            removeTrack(track);
        }
    }

    //----------------------------------
    //  Song listeners
    //----------------------------------

    private final List<ITrackSongListener> mSongListeners = new ArrayList<ITrackSongListener>();

    @Override
    public final void addTrackSongListener(ITrackSongListener value) {
        if (mSongListeners.contains(value))
            return;
        mSongListeners.add(value);
    }

    @Override
    public final void removeTrackSongListener(ITrackSongListener value) {
        if (!mSongListeners.contains(value))
            return;
        mSongListeners.remove(value);
    }

    void fireTrackAdd(ITrack track) {
        for (ITrackSongListener listener : mSongListeners) {
            listener.onTrackAdded(track);
        }
    }

    void fireTrackRemove(ITrack track) {
        for (ITrackSongListener listener : mSongListeners) {
            listener.onTrackRemoved(track);
        }
    }

    @Override
    public void onPatternAdded(ITrackPattern pattern) {
        // add to sequencer
        if (mLastPatternInTracks != null) {
            if (pattern.getEndMeasure() > mLastPatternInTracks.getEndMeasure()) {
                mLastPatternInTracks = pattern;
            }
        } else {
            mLastPatternInTracks = pattern;
        }
        patternAdd(pattern);
    }

    @Override
    public void onPatternRemoved(ITrackPattern pattern) {
        patternRemove(pattern);
    }

    private void patternAdd(ITrackPattern pattern) {
        List<ITone> tones = ((Part)pattern.getTrack().getPart()).getTones();
        for (ITone tone : tones) {
            try {
                getProject()
                        .getWorkspace()
                        .getRack()
                        .getSequencer()
                        .addPattern(tone, pattern.getBank(), pattern.getIndex(),
                                pattern.getStartMeasure(), pattern.getEndMeasure());
            } catch (CausticException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void patternRemove(ITrackPattern pattern) {
        List<ITone> tones = ((Part)pattern.getTrack().getPart()).getTones();
        for (ITone tone : tones) {
            try {
                getProject()
                        .getWorkspace()
                        .getRack()
                        .getSequencer()
                        .addPattern(tone, -1, -1, pattern.getStartMeasure(),
                                pattern.getEndMeasure());
            } catch (CausticException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
