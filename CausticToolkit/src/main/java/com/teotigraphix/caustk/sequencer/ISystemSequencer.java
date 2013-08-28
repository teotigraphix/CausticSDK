
package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.application.Dispatcher;
import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.core.IRestore;

/**
 * Controls the outputpanel (transport).
 */
public interface ISystemSequencer extends IControllerComponent, IRestore {

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
    void beatUpdate(float beat);

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

        private int beat;

        public int getBeat() {
            return beat;
        }

        public OnSystemSequencerBeatChange(int beat) {
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

    Dispatcher getDispatcher();

    int getStep();

}
