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

package com.teotigraphix.caustk.sequencer.track;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.Tone;

@SuppressLint("UseSparseArrays")
public class TrackSong {

    public TrackSequencer getTrackSequencer() {
        return trackSequencer;
    }

    final IDispatcher getDispatcher() {
        return trackSequencer.getRack();
    }

    final IRack getRack() {
        return getTrackSequencer().getRack();
    }

    //--------------------------------------------------------------------------
    // Private :: variables
    //--------------------------------------------------------------------------

    //    private TrackItem lastPatternInTracks;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private TrackSequencer trackSequencer;

    @Tag(1)
    private Map<Integer, Track> tracks = new HashMap<Integer, Track>();

    @Tag(2)
    private int currentTrack = -1;

    @Tag(3)
    private File file;

    @Tag(4)
    private int currentMeasure = 0;

    @Tag(5)
    private float currentBeat = -1;

    //----------------------------------
    // file
    //----------------------------------

    public boolean exists() {
        return file != null;
    }

    /**
     * The relative path within the project directory.
     */
    public final File getFile() {
        return file;
    }

    void setFile(File value) {
        file = value;
    }

    /**
     * Returns the absolute location of the song file on disk, within the
     * project's resource directory.
     */
    public File getAbsoluteFile() {
        // XXX This is breaking encapsulation
        final File absoluteFile = getTrackSequencer().getRack().getProject()
                .getAbsoluteResource(new File("songs", file.getPath()).getPath());
        return absoluteFile;
    }

    /**
     * The relative path of the containing directory within the project
     * directory.
     */
    public final File getDirectory() {
        if (file == null)
            return null;
        return file.getParentFile();
    }

    /**
     * The song's file name without the extension.
     */
    public String getFileName() {
        if (file == null)
            return null;
        return file.getName().replace(".ctks", "");
    }

    /**
     * The relative location of the sibling <code>.caustic</code> file.
     */
    public File getCausticFile() {
        if (file == null)
            return null;
        return new File(getDirectory(), getFileName() + ".caustic");
    }

    /**
     * The absolute location of the sibling <code>.caustic</code> file on disk.
     */
    public File getAbsoluteCausticFile() {
        if (file == null)
            return null;
        return new File(getAbsoluteFile().getParentFile(), getFileName() + ".caustic");
    }

    //----------------------------------
    // currentTrack
    //----------------------------------

    public int getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(int value) {
        if (value < 0 || value > 13)
            throw new IllegalArgumentException("Illigal track index " + value);
        if (tracks.containsKey(value))
            throw new IllegalArgumentException("Track index does not exist;" + value);
        if (value == currentTrack)
            return;
        currentTrack = value;
        //        getDispatcher().trigger(new OnTrackSequencerCurrentTrackChange(currentTrack));
    }

    //----------------------------------
    // currentBank
    //----------------------------------

    public int getCurrentBank() {
        return getCurrentBank(currentTrack);
    }

    public int getCurrentBank(int trackIndex) {
        return getTrack(trackIndex).getCurrentBank();
    }

    //----------------------------------
    // currentPattern
    //----------------------------------

    public int getCurrentPattern() {
        return getCurrentPattern(currentTrack);
    }

    public int getCurrentPattern(int trackIndex) {
        return getTrack(trackIndex).getCurrentPattern();
    }

    //----------------------------------
    // track
    //----------------------------------

    public boolean hasTracks() {
        return tracks.size() > 0;
    }

    public Collection<Track> getTracks() {
        return tracks.values();
    }

    public Track getSelectedTrack() {
        return getTrack(currentTrack);
    }

    public Track getTrack(int index) {
        Track track = tracks.get(index);
        if (track == null) {
            track = new Track(this, index);
            tracks.put(index, track);
        }
        return track;
    }

    public Phrase getPhrase(int toneIndex, int bankIndex, int patterIndex) {
        return getTrack(toneIndex).getPhrase(bankIndex, patterIndex);
    }

    public TrackSong() {
    }

    public TrackSong(TrackSequencer trackSequencer, File file) {
        this.trackSequencer = trackSequencer;
        this.file = file;
    }

    void toneAdd(Tone tone) {
        Track channel = getTrack(tone.getIndex());
        tracks.put(tone.getIndex(), channel);
        channel.onAdded();
    }

    void toneRemove(Tone tone) {
        Track channel = tracks.remove(tone.getIndex());
        channel.onRemoved();
    }

    //--------------------------------------------------------------------------
    // SongSequencer API :: Methods
    //--------------------------------------------------------------------------

    //----------------------------------
    //  currentMeasure
    //----------------------------------

    /**
     * Returns the current measure playing in Song mode.
     * <p>
     * Note: The current bar is divisible by 4, the current measure is the sum
     * of all steps played currently in a song.
     * </p>
     */
    public int getCurrentMeasure() {
        return currentMeasure;
    }

    void setCurrentMeasure(int value) {
        currentMeasure = value;
    }

    /**
     * Returns the actual beat in the current measure.
     * <p>
     * Example; measure 4, beat 14 would be beat 2 in the measure (0 index - 3rd
     * beat in measure).
     * </p>
     */
    public int getMeasureBeat() {
        return (int)(currentBeat % 4);
    }

    public void setPosition(int measure, float beat) {
        setCurrentMeasure(measure);
        setCurrentBeat(beat);
    }

    //----------------------------------
    //  currentBeat
    //----------------------------------

    /**
     * Return the ISong current beat.
     */
    public float getCurrentBeat() {
        return currentBeat;
    }

    void setCurrentBeat(float value) {
        currentBeat = value;
        for (Track track : tracks.values()) {
            track.setCurrentBeat(currentBeat);
        }
    }

    //    public int getNumBeats() {
    //        if (lastPatternInTracks == null)
    //            return 0;
    //        int measures = lastPatternInTracks.getEndMeasure();
    //        return measures * 4;
    //    }
    //
    //    public int getNumMeasures() {
    //        if (lastPatternInTracks == null)
    //            return 0;
    //        // 0 index, we need to use the end measure that is measures + 1
    //        int measures = lastPatternInTracks.getEndMeasure();
    //        return measures;
    //    }
    //
    //    public int getTotalTime() {
    //        float bpm = getRack().getSystemSequencer().getTempo();
    //        float timeInSec = 60 / bpm;
    //        float totalNumBeats = getNumBeats() + getMeasureBeat();
    //        float total = timeInSec * totalNumBeats;
    //        return (int)total;
    //    }

    public int getCurrentTime() {
        float bpm = getRack().getSystemSequencer().getTempo();
        float timeInSec = 60 / bpm;
        float totalNumBeats = (getCurrentMeasure() * 4) + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    public void dispose() {
    }

    public void clear() throws CausticException {
        for (Track track : tracks.values()) {
            track.clear();
        }
    }
}
