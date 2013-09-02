
package com.teotigraphix.caustk.sequencer.track;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackSequencerCurrentTrackChange;
import com.teotigraphix.caustk.sequencer.track.TrackChannel.OnTrackChannelPhraseAdd;
import com.teotigraphix.caustk.sequencer.track.TrackChannel.OnTrackChannelPhraseRemove;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.sound.mixer.MasterMixer;
import com.teotigraphix.caustk.tone.Tone;

public class TrackSong implements ISerialize {

    private transient ICaustkController controller;

    private Map<Integer, TrackChannel> tracks = new HashMap<Integer, TrackChannel>();

    IDispatcher getDispatcher() {
        return controller.getTrackSequencer().getDispatcher();
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
                .getAbsoluteResource(file.getPath());
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
        getDispatcher().trigger(new OnTrackSequencerCurrentTrackChange(currentTrack));
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

    public Collection<TrackChannel> getTracks() {
        return tracks.values();
    }

    public TrackChannel getSelectedTrack() {
        return getTrack(currentTrack);
    }

    public TrackChannel getTrack(int index) {
        TrackChannel track = tracks.get(index);
        if (track == null) {
            track = new TrackChannel(controller, index);
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

    private MasterMixer masterMixer = null;

    /*
     * A song serializes;
     * - MasterDelay , MasterReverb, MasterEqualizer, MasterLimiter
     * - EffectChannel slot1, slot2
     */
    @Override
    public void sleep() {
        // save the .caustic file
        try {
            File absoluteTargetSongFile = getAbsoluteCausticFile();
            controller.getSoundSource().saveSongAs(absoluteTargetSongFile);
            masterMixer = controller.getSoundMixer().getMasterMixer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        if (!exists()) // dummy placeholder
            return;

        getDispatcher().register(OnTrackChannelPhraseAdd.class, onTrackChannelPhraseHandler);
        getDispatcher().register(OnTrackChannelPhraseRemove.class,
                onTrackChannelPhraseRemoveHandler);

        if (masterMixer != null)
            controller.getSoundMixer().setMasterMixer(masterMixer);

        for (TrackChannel item : tracks.values()) {
            item.wakeup(controller);
        }
    }

    void toneAdd(Tone tone) {
        TrackChannel channel = getTrack(tone.getIndex());
        tracks.put(tone.getIndex(), channel);
        channel.onAdded();
    }

    void toneRemove(Tone tone) {
        TrackChannel channel = tracks.remove(tone.getIndex());
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
        return currentBeat % 4;
    }

    //----------------------------------
    //  currentBeat
    //----------------------------------

    private int currentBeat = -1;

    /**
     * Return the ISong current beat.
     */
    public int getCurrentBeat() {
        return currentBeat;
    }

    @SuppressWarnings("unused")
    private float beat = -1;

    void setCurrentBeat(float value) {
        beat = value;
        for (TrackChannel track : tracks.values()) {
            track.setCurrentBeat(currentBeat);
        }
    }

    void setCurrentBeat(int value) {
        setCurrentBeat(value, false);
    }

    void setCurrentBeat(int value, boolean seeking) {
        int last = currentBeat;
        currentBeat = value;

        //        fireBeatChange(mCurrentBeat, last);

        if (last < value) {
            // forward
            if (currentBeat == 0) {
                setCurrentMeasure(0);
            } else {
                int remainder = currentBeat % 4;
                if (seeking) {
                    setCurrentMeasure(currentBeat / 4);
                } else if (remainder == 0) {
                    setCurrentMeasure(currentMeasure + 1);
                }
            }
        } else if (last > value) {
            // reverse
            // if the last beat was a measure change, decrement measure
            int remainder = last % 4;
            if (remainder == 0) {
                setCurrentMeasure(currentMeasure - 1);
            }
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

    /**
     * Enables the playhead.
     * <p>
     * Calling play dispatches signals without advancing the playhead. This is
     * useful for starting a song at o beat and 0 measure.
     * </p>
     */
    public void play() {
        setCurrentBeat(currentBeat);
    }

    /**
     * Rewinds the playhead to the start of the song, beat 0.
     */
    @SuppressWarnings("unused")
    public void rewind() {
        int lastBeat = currentBeat;
        int lastMeasure = currentMeasure;

        currentBeat = -1;
        currentMeasure = -1;

        //        fireBeatChange(mCurrentBeat, lastBeat);
        //        fireMeasureChange(mCurrentMeasure, lastMeasure);
    }

    /**
     * Moves playhead to next beat based on song implementation.
     */
    public void nextBeat() {
        setCurrentBeat(currentBeat + 1);
    }

    /**
     * Moves playhead to previous beat based on song implementation.
     */
    public void previousBeat() {
        setCurrentBeat(currentBeat - 1);
    }

    /**
     * Moves playhead to next measure based on song implementation.
     */
    public void nextMeasure() {
        // TODO use seek() and calc it
        // think about this though, do you want beat signals or not???
        // if so, just loop this, if not use seek() and only he measure
        // signal will fire
        // start with a next beat since we might be on a 0 beat of the measure
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
    }

    /**
     * Moves playhead to previous measure based on song implementation.
     */
    public void previousMeasure() {
        // start with a previous beat since we might be on a 0 beat of the measure
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
    }

    /**
     * Moves the playhead a number of beats.
     * 
     * @param beats The beats to rewind.
     */
    public void seek(int beat) {
        setCurrentBeat(beat, true);
    }

    private transient EventObserver<OnTrackChannelPhraseAdd> onTrackChannelPhraseHandler = new EventObserver<OnTrackChannelPhraseAdd>() {
        @Override
        public void trigger(OnTrackChannelPhraseAdd object) {
            TrackChannel track = object.getTrack();
            TrackItem trackItem = object.getItem();
            Tone tone = controller.getSoundSource().getTone(track.getIndex());
            int bank = trackItem.getBankIndex();
            int pattern = trackItem.getPatternIndex();
            int start = trackItem.getStartMeasure();
            int end = trackItem.getEndMeasure();
            // add the track to the song sequencer
            try {
                controller.getSystemSequencer().addPattern(tone, bank, pattern, start, end);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    };

    private transient EventObserver<OnTrackChannelPhraseRemove> onTrackChannelPhraseRemoveHandler = new EventObserver<OnTrackChannelPhraseRemove>() {
        @Override
        public void trigger(OnTrackChannelPhraseRemove object) {
            TrackChannel track = object.getTrack();
            TrackItem trackItem = object.getItem();
            Tone tone = controller.getSoundSource().getTone(track.getIndex());
            int start = trackItem.getStartMeasure();
            int end = trackItem.getEndMeasure();
            // remove the track to the song sequencer
            try {
                controller.getSystemSequencer().removePattern(tone, start, end);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    };
}
