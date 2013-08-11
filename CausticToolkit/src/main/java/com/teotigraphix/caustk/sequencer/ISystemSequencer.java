
package com.teotigraphix.caustk.sequencer;

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

    int getCurrentMeasure();

    int getCurrentBeat();

    public static class OnSongSequencerBeatChange {

        private int beat;

        public int getBeat() {
            return beat;
        }

        public OnSongSequencerBeatChange(int beat) {
            this.beat = beat;
        }
    }
    
    public static class OnSongSequencerMeasureChange {

        private int measure;

        public int getMeasure() {
            return measure;
        }

        public OnSongSequencerMeasureChange(int measure) {
            this.measure = measure;
        }
    }
}
