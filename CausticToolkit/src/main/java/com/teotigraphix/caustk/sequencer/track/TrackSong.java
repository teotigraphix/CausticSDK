
package com.teotigraphix.caustk.sequencer.track;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;

public class TrackSong implements ISerialize {

    private transient ICaustkController controller;

    private Map<Integer, Track> tracks = new HashMap<Integer, Track>();

    IDispatcher getDispatcher() {
        return controller.getTrackSequencer();
    }

    //----------------------------------
    // file
    //----------------------------------

    public boolean exists() {
        return file != null;
    }

    private File file;

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
        final File absoluteFile = controller.getProjectManager().getProject()
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

    private int currentTrack = -1;

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
            track = new Track(controller, index);
            tracks.put(index, track);
        }
        return track;
    }

    public TrackPhrase getPhrase(int toneIndex, int bankIndex, int patterIndex) {
        return getTrack(toneIndex).getPhrase(bankIndex, patterIndex);
    }

    public TrackSong() {
    }

    public TrackSong(File file) {
        this();
        this.file = file;
    }

    //--------------------------------------------------------------------------
    // ISerialize API :: Methods
    //--------------------------------------------------------------------------

    /*
     * A song serializes;
     * - MasterDelay , MasterReverb, MasterEqualizer, MasterLimiter
     * - EffectChannel slot1, slot2
     */
    @Override
    public void sleep() {
        for (Track channel : tracks.values()) {
            channel.sleep();
        }
        // save the .caustic file
        try {
            File absoluteTargetSongFile = getAbsoluteCausticFile();
            controller.getSoundSource().saveSongAs(absoluteTargetSongFile);
            // masterMixer = controller.getSoundMixer().getMasterMixer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        if (!exists()) // dummy placeholder
            return;

        for (Track channel : tracks.values()) {
            channel.wakeup(controller);
        }
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

    private int currentMeasure = 0;

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

    private float currentBeat = -1;

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

    private TrackItem lastPatternInTracks;

    public int getNumBeats() {
        if (lastPatternInTracks == null)
            return 0;
        int measures = lastPatternInTracks.getEndMeasure();
        return measures * 4;
    }

    public int getNumMeasures() {
        if (lastPatternInTracks == null)
            return 0;
        // 0 index, we need to use the end measure that is measures + 1
        int measures = lastPatternInTracks.getEndMeasure();
        return measures;
    }

    public int getTotalTime() {
        float bpm = controller.getSystemSequencer().getTempo();
        float timeInSec = 60 / bpm;
        float totalNumBeats = getNumBeats() + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    public int getCurrentTime() {
        float bpm = controller.getSystemSequencer().getTempo();
        float timeInSec = 60 / bpm;
        float totalNumBeats = (getCurrentMeasure() * 4) + getMeasureBeat();
        float total = timeInSec * totalNumBeats;
        return (int)total;
    }

    public void dispose() {
    }

}
