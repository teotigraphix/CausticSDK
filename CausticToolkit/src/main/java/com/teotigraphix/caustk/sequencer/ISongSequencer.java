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

package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.tone.Tone;

public interface ISongSequencer extends IControllerComponent, IRestore {

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
     * @param type ExportType - WAV (default) or MID
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

}
