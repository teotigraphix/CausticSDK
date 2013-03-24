////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.sequencer;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.rack.IRackAware;
import com.teotigraphix.common.IPersist;

/**
 * The sequencer manages {@link ISong} and external media exporting.
 * <p>
 * The sequencer is used to add patterns to {@link IPatternSequencer} machines
 * and will play song positions.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISequencer extends IDevice, IPersist, IRackAware
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    /**
     * Returns the number of beats total that have played in the song.
     * <p>
     * Beats are 0 index based, which means beat 1 in a 4 beat measure is
     * actually beat 0.
     */
    int getCurrentBeat();

    /**
     * Returns the current measure within the song.
     * <p>
     * Note: This only applies to {@link OutputPanelMode#SONG}, when in pattern
     * mode, the method will return <code>0</code>.
     */
    int getCurrentMeasure();

    /**
     * Returns the current beat within the current measure.
     * <p>
     * Since the sequencer is currently 4/4, this is the modulo of the
     * {@link #getCurrentBeat()}.
     */
    int getCurrentMeasureBeat();

    // //----------------------------------
    // // currentSong
    // //----------------------------------
    //
    // /**
    // * Returns the current ISong loaded or playing in the sequencer.
    // */
    // ISong getCurrentSong();
    //
    // /**
    // * @see #getCurrentSong()
    // */
    // void setCurrentSong(ISong value);

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Adds {@link IMachine} bank patterns into the song sequencer.
     * <p>
     * Patterns span whole measures whether a patterns length is 1, 2, 4 or 8,
     * does not matter.
     * <p>
     * A pattern added that has a length of 2 measures and who's start was
     * placed at 0 and end at 1 would only get half the pattern played in the
     * song sequencer.
     * 
     * @param machine The {@link IMachine} to sequence.
     * @param bank The bank of the pattern.
     * @param pattern The pattern of the IMachine.
     * @param start The start measure of the insert
     * @param end The end measure of insert.
     * @throws CausticException Invalid values
     */
    void addPattern(IMachine machine, int bank, int pattern, int start, int end)
            throws CausticException;

    /**
     * Removes {@link IMachine} bank pattern from the song sequencer.
     * 
     * @param machine The {@link IMachine} sequenced.
     * @param start The start measure to remove.
     * @param end The measure bar to remove to.
     * @throws CausticException Invalid values
     */
    void removePattern(IMachine machine, int start, int end)
            throws CausticException;

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
     * [0..100], defaulting to 70 if none specified.
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
     * will play.
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
    void clearAutomation(IMachine machine);

    /**
     * The export type for external media from the {@link ISequencer}.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     * @see ISequencer#exportSong(String, ExportType)
     */
    public enum ExportType
    {

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

        ExportType(String value)
        {
            mValue = value;
        }

        /**
         * Returns the String value.
         */
        public String getValue()
        {
            return mValue;
        }
    }

    //--------------------------------------------------------------------------
    //
    // Listeners
    //
    //--------------------------------------------------------------------------

    /**
     * Register a callback to be invoked when the sequencer beat changes.
     * 
     * @param l The callback that will run
     */
    void setOnBeatChangeListener(OnBeatChangeListener l);

    /**
     * Register a callback to be invoked when the sequencer measure changes.
     * 
     * @param l The callback that will run
     */
    void setOnMeasureChangeListener(OnMeasureChangeListener l);

    /**
     * Register a callback to be invoked when the sequencer exports a song to
     * disk with progress and complete.
     * 
     * @param l The callback that will run
     */
    void setOnSongExportListener(OnSongExportListener l);

    /**
     * Interface used to notify listeners of the {@link ISequencer}'s beat
     * change during pattern and song mode.
     */
    public interface OnBeatChangeListener
    {
        /**
         * Dispatched when the sequencer beat changes during a notification from
         * the {@link ICausticEngine}.
         * 
         * @param beat The new current beat from the core.
         */
        void onBeatChanged(int beat);
    }

    /**
     * Interface used to notify listeners of the {@link ISequencer}'s measure
     * change during pattern and song mode.
     */
    public interface OnMeasureChangeListener
    {
        /**
         * Dispatched when the sequencer measure changes during a notification
         * from the {@link ICausticEngine}.
         * 
         * @param measure The new current measure from the core.
         */
        void onMeasureChanged(int measure);
    }

    /**
     * Interface used to notify listeners of the {@link ISequencer}'s export
     * progress and completion.
     * 
     * @see ISequencer#exportSong(String, ExportType)
     */
    public interface OnSongExportListener
    {

        /**
         * Dispatched during song export.
         * 
         * @param progress The amount of progress made exporting;
         * <code>(0..100)</code>, when 100 is reached the {@link #onComplete()}
         * callback is dispatched.
         */
        void onProgess(int progress);

        /**
         * Dispatched when the song is finished exporting and saved to disk.
         */
        void onComplete();
    }
}
