
package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.ExportType;
import com.teotigraphix.caustk.tone.Tone;

/**
 * Controls the outputpanel (transport).
 */
@SuppressWarnings("unused")
public interface ISystemSequencer extends IControllerComponent, IRestore {

    //--------------------------------------------------------------------------
    // Native Song sequencer
    //--------------------------------------------------------------------------
    /**
     * Sets the mode for the song sequencers end.
     * 
     * @param mode The new mode.
     */
    void setSongEndMode(SongEndMode mode);

    /**
     * Returns the current value form the core's song sequencer's song end mode.
     */
    SongEndMode getSongEndMode();

    /**
     * Returns the raw token string of patterns in the song sequencer.
     * 
     * @see SequencerMessage#QUERY_PATTERN_EVENT
     */
    String getPatterns();

    /**
     * Adds {@link Tone} bank patterns into the song sequencer.
     * <p>
     * Patterns span whole measures whether a patterns length is 1, 2, 4 or 8,
     * does not matter.
     * <p>
     * A pattern added that has a length of 2 measures and who's start was
     * placed at 0 and end at 1 would only get half the pattern played in the
     * song sequencer.
     * 
     * @param tone The {@link Tone} to sequence.
     * @param bank The bank of the pattern.
     * @param pattern The pattern of the IMachine.
     * @param start The start measure of the insert
     * @param end The end measure of insert.
     * @throws CausticException Invalid values
     */
    void addPattern(Tone tone, int bank, int pattern, int start, int end) throws CausticException;

    /**
     * Removes {@link Tone} bank pattern from the song sequencer.
     * 
     * @param tone The {@link Tone} sequenced.
     * @param start The start measure to remove.
     * @param end The measure bar to remove to.
     * @throws CausticException Invalid values
     */
    void removePattern(Tone tone, int start, int end) throws CausticException;

    /**
     * Sets loop points for a song loop of patterns.
     * 
     * @param startBar The bar to start looping.
     * @param endBar The bar to end looping.
     */
    void setLoopPoints(int startBar, int endBar);

    /**
     * Positions the sequencer at a specific beat in the song.
     * <p>
     * Notice this one's in beats, this is because Caustic has no real notion of
     * time apart from BPM. All events are stored in beats, where 4 beats = 1
     * bar (at the fixed 4/4 signature). So you can input any floating point
     * number, but for example to skip halfway into to the second bar (We're
     * using 0-index bar numbers), you'd send (1 + 0.5) * 4 = 6 or bar = 2(2 *
     * 16 = 32), step = 8 [
     * </p>
     * 
     * @param positionInBeats The position in beats to play.
     */
    void playPosition(int positionInBeats);

    /**
     * Positions the sequencer at a specific bar and step in the song.
     * 
     * @param bar The bar to play at.
     * @param step The step to play at.
     * @see #playPosition()
     */
    void playPositionAt(int bar, int step);

    /**
     * Exports a song to wav, ogg or midi.
     * 
     * @param exportPath a full path for the exported file, without extension
     * @param type ExportType - WAV (default), OGG or MID
     * @param quality only needed if you've asked for OGG and goes from
     *            [0..100], defaulting to 70 if none specified.
     */
    void exportSong(String exportPath, ExportType type, int quality);

    /**
     * @see #exportSong(String, ExportType, int)
     * @param exportPath a full path for the exported file, without extension
     * @param type {@link ExportType} - WAV (default) or MID
     */
    void exportSong(String exportPath, ExportType type);

    /**
     * Reports the progress of export.
     * 
     * @return 0 to 100, When 100, the export is complete, until then no sound
     *         will play.
     */
    float exportSongProgress();

    /**
     * Clears all patterns from the sequencer.
     */
    void clearPatterns();

    /**
     * Clears all machine automation from the sequencer.
     */
    void clearAutomation();

    /**
     * Clears the specific machine automation from the sequencer.
     * 
     * @param machine The {@link IMachine} to clear automation from.
     */
    void clearAutomation(Tone machine);

    ShuffleMode getShuffleMode();

    void setShuffleMode(ShuffleMode value);

    float getShuffleAmount();

    void setShuffleAmount(float value);

    public enum ShuffleMode {

        EIGTH(1),

        SIXTEENTH(2);

        private final int value;

        ShuffleMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ShuffleMode fromInt(int value) {
            for (ShuffleMode mode : values()) {
                if (mode.getValue() == value)
                    return mode;
            }
            return null;
        }
    }

    /**
     * The export type for external media from the {@link ISequencer}.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     * @see ISequencer#exportSong(String, ExportType)
     */
    public enum ExportType {

        /**
         * Export as a <code>*.wav</code> file.
         */
        WAV("WAV"),

        /**
         * Export as a <code>*.ogg</code> file.
         */
        OGG("OGG"),

        /**
         * Export as a <code>*.mid</code> file.
         */
        MID("MID");

        private final String mValue;

        ExportType(String value) {
            mValue = value;
        }

        /**
         * Returns the String value.
         */
        public String getValue() {
            return mValue;
        }
    }

    /**
     * The mode(0,1,2) keep playing, stop, loop to start.
     */
    public enum SongEndMode {

        /**
         * Keep playing.
         */
        PLAY(0),

        /**
         * Stop at last measure.
         */
        STOP(1),

        /**
         * Loop to start measure from end.
         */
        LOOP(2);

        private int value;

        public int getValue() {
            return value;
        }

        SongEndMode(int value) {
            this.value = value;
        }

        public static SongEndMode fromInt(int value) {
            for (SongEndMode mode : values()) {
                if (mode.getValue() == value)
                    return mode;
            }
            return null;
        }
    }

    /**
     * Plays the song sequencer in pattern or song mode.
     * 
     * @see OnSystemSequencerPlay
     */
    public static final String COMMAND_PLAY = "system_sequencer/play";

    /**
     * Stops the song sequencer.
     * 
     * @see OnSystemSequencerStop
     */
    public static final String COMMAND_STOP = "system_sequencer/stop";

    /**
     * Sets the bpm of the {@link IOutputPanel}.
     * 
     * @see UndoCommand
     */
    public static final String COMMAND_TEMPO = "system_sequencer/tempo";

    void setTempo(float bpm);

    float getTempo();

    SequencerMode getSequencerMode();

    boolean isPlaying();

    void setIsPlaying(boolean value);

    void setSequencerMode(SequencerMode value);

    void play(SequencerMode mode);

    void stop();

    public enum SequencerMode {
        PATTERN(0), SONG(1);

        private int value;

        public final int getValue() {
            return value;
        }

        SequencerMode(int value) {
            this.value = value;
        }

        public static SequencerMode fromInt(int smode) {
            switch (smode) {
                case 1:
                    return SONG;
                default:
                    return PATTERN;
            }
        }
    }

    /**
     * The single entry point for a beat change from the caustic core audio.
     * 
     * @param beat The beat and decimal fraction in between the beat.
     */
    void beatUpdate(int measure, float beat);

    /**
     * Returns the current measure of the song sequencer, calculated from the
     * {@link #getCurrentBeat()}.
     */
    int getCurrentMeasure();

    /**
     * Returns the current beat of the song sequencer.
     */
    int getCurrentBeat();

    /**
     * @see ISystemSequencer#isPlaying()
     */
    public static class OnSongSequencerTransportChange {
    }

    /**
     * @see ISystemSequencer#setTempo(float)
     */
    public static class OnSongSequencerTempoChange {

        private float tempo;

        public float getTempo() {
            return tempo;
        }

        public OnSongSequencerTempoChange(float tempo) {
            this.tempo = tempo;
        }
    }

    public static class OnSystemSequencerBeatChange {

        private int measure;

        private float beat;

        public int getMeasure() {
            return measure;
        }

        public float getBeat() {
            return beat;
        }

        public OnSystemSequencerBeatChange(int measure, float beat) {
            this.measure = measure;
            this.beat = beat;
        }

    }

    /**
     * @see ISystemSequencer#getDispatcher()
     */
    public static class OnSongSequencerMeasureChange {

        private int measure;

        public int getMeasure() {
            return measure;
        }

        public OnSongSequencerMeasureChange(int measure) {
            this.measure = measure;
        }
    }

    int getStep();

}
