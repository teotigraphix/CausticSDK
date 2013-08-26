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

package com.teotigraphix.caustk.sequencer;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.EffectMixerState;
import com.teotigraphix.caustk.library.SoundMixerState;
import com.teotigraphix.caustk.library.SoundSourceState;
import com.teotigraphix.caustk.sequencer.Track.OnTrackPhraseAdd;
import com.teotigraphix.caustk.sequencer.Track.OnTrackPhraseRemove;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class TrackSong extends Song implements ISerialize {

    private transient ICaustkController controller;

    public final ICaustkController getController() {
        return controller;
    }

    @Override
    void setCurrentBeat(float currentBeat) {
        super.setCurrentBeat(currentBeat);
        for (Track track : tracks.values()) {
            track.setCurrentBeat(currentBeat);
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  ITrackSong API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  soundSourceState
    //----------------------------------

    private SoundSourceState soundSourceState;

    public SoundSourceState getSoundSourceState() {
        return soundSourceState;
    }

    public void setSoundSourceState(SoundSourceState value) {
        soundSourceState = value;
    }

    //----------------------------------
    //  soundMixerState
    //----------------------------------

    private SoundMixerState soundMixerState;

    public SoundMixerState getSoundMixerState() {
        return soundMixerState;
    }

    public void setSoundMixerState(SoundMixerState value) {
        soundMixerState = value;
    }

    //----------------------------------
    //  effectRackInfo
    //----------------------------------

    private EffectMixerState effectMixerState;

    public EffectMixerState getEffectMixerState() {
        return effectMixerState;
    }

    public void setEffectMixerState(EffectMixerState value) {
        effectMixerState = value;
    }

    //----------------------------------
    //  file
    //----------------------------------

    private File file;

    /**
     * Returns the relative song file path from the base <code>songs</code>
     * directory.
     * <p>
     * Use the {@link ISongManager} to create an absolute file path for this
     * song.
     */
    public final File getFile() {
        return file;
    }

    public final void setFile(File value) {
        file = value;
    }

    //----------------------------------
    //  tracks
    //----------------------------------

    private int numTracks = -1;

    public int getNumTracks() {
        return numTracks;
    }

    /**
     * Resets the number of {@link Track}s int he song.
     * <p>
     * Setting this property will reset all data and tracks.
     * 
     * @param value The number of tracks in the song.
     */
    public void setNumTracks(int value) {
        numTracks = value;
        createTracks();
        initializeTracks();
    }

    private void createTracks() {
        tracks.clear();
        for (int i = 0; i < numTracks; i++) {
            Track track = new Track();
            track.setIndex(i);
            tracks.put(i, track);
        }
    }

    /**
     * Initializes the {@link Track} instance.
     * <p>
     * Must be called after a {@link TrackSong} deserialization.
     */
    public void initializeTracks() {
        for (Track track : tracks.values()) {
            track.setDispatcher(getDispatcher());
            getDispatcher().register(OnTrackPhraseAdd.class, onTrackPhraseHandler);
            getDispatcher().register(OnTrackPhraseRemove.class, onTrackPhraseRemoveHandler);
        }
    }

    private transient EventObserver<OnTrackPhraseAdd> onTrackPhraseHandler = new EventObserver<OnTrackPhraseAdd>() {
        @Override
        public void trigger(OnTrackPhraseAdd object) {
            Track track = object.getTrack();
            TrackItem trackItem = object.getItem();
            Tone tone = controller.getSoundSource().getTone(track.getIndex());
            int bank = trackItem.getBankIndex();
            int pattern = trackItem.getPatternIndex();
            int start = trackItem.getStartMeasure();
            int end = trackItem.getEndMeasure();
            // add the track to the song sequencer
            try {
                controller.getSongSequencer().addPattern(tone, bank, pattern, start, end);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    };

    private transient EventObserver<OnTrackPhraseRemove> onTrackPhraseRemoveHandler = new EventObserver<OnTrackPhraseRemove>() {
        @Override
        public void trigger(OnTrackPhraseRemove object) {
            Track track = object.getTrack();
            TrackItem trackItem = object.getItem();
            Tone tone = controller.getSoundSource().getTone(track.getIndex());
            int start = trackItem.getStartMeasure();
            int end = trackItem.getEndMeasure();
            // remove the track to the song sequencer
            try {
                controller.getSongSequencer().removePattern(tone, start, end);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    };

    //----------------------------------
    //  tracks
    //----------------------------------

    private Map<Integer, Track> tracks = new HashMap<Integer, Track>();

    /*
     * The last pattern in the song in All tracks. This is used to easily
     * calculate the measure length of the song.
     */
    private TrackItem lastPatternInTracks;

    public Collection<Track> getTracks() {
        return Collections.unmodifiableCollection(tracks.values());
    }

    @Override
    public int getNumBeats() {
        if (lastPatternInTracks == null)
            return 0;
        int measures = lastPatternInTracks.getEndMeasure();
        return measures * 4;
    }

    @Override
    public int getNumMeasures() {
        if (lastPatternInTracks == null)
            return 0;
        // 0 index, we need to use the end measure that is measures + 1
        int measures = lastPatternInTracks.getEndMeasure();
        return measures;
    }

    @Override
    public int getTotalTime() {
        float bpm = getBPM();
        float timeInSec = 60 / bpm;
        float totalNumBeats = getNumBeats() + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    @Override
    public int getCurrentTime() {
        float bpm = getBPM();
        float timeInSec = 60 / bpm;
        float totalNumBeats = (getCurrentMeasure() * 4) + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    public TrackSong() {
        super();
    }

    /**
     * Returns the {@link Track} at the specified index.
     * 
     * @param index The tack index.
     */
    public Track getTrack(int index) {
        return tracks.get(index);
    }

    /**
     * Clears all the tracks {@link TrackPhrase}s.
     * 
     * @throws CausticException
     */
    public void clearTracks() throws CausticException {
        for (Track track : tracks.values()) {
            track.clearPhrases();
        }
    }

    @Override
    public void sleep() {
        //        TrackUtils.refreshTrackSongInfos(controller, this);
        for (Track track : tracks.values()) {
            track.sleep();
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        initializeTracks();
        for (Track track : tracks.values()) {
            track.wakeup(controller);
        }
    }

    void setController(ICaustkController value) {
        controller = value;
    }

}
