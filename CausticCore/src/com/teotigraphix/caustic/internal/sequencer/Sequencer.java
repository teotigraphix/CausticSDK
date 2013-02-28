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

package com.teotigraphix.caustic.internal.sequencer;

import com.singlecellsoftware.causticcore.CausticEventListener;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.internal.device.Device;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.SequencerMessage;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.sequencer.ISequencer;

/**
 * The default implementation of the {@link ISequencer} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Sequencer extends Device implements ISequencer, CausticEventListener {

    //--------------------------------------------------------------------------
    //
    // IRackAware API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    private IRack mRack;

    @Override
    public IRack getRack() {
        return mRack;
    }

    @Override
    public void setRack(IRack value) {
        if (mRack != null) {
            setEngine(null);
        }

        mRack = value;

        if (mRack != null) {
            setEngine(mRack.getEngine());
        }
    }

    //--------------------------------------------------------------------------
    //
    // ISequencer API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // currentBeat
    //----------------------------------

    private int mCurrentBeat;

    @Override
    public int getCurrentBeat() {
        return mCurrentBeat;
    }

    void setCurrentBeat(int value) {
        mCurrentBeat = value;
        if (mOnBeatChangeListener != null)
            mOnBeatChangeListener.onBeatChanged(mCurrentBeat);
    }

    //----------------------------------
    // currentMeasure
    //----------------------------------

    private int mCurrentMeasure;

    @Override
    public int getCurrentMeasure() {
        return mCurrentMeasure;
    }

    void setCurrentMeasure(int value) {
        mCurrentMeasure = value;
        if (mOnMeasureChangeListener != null)
            mOnMeasureChangeListener.onMeasureChanged(mCurrentMeasure);
    }

    //----------------------------------
    // currentBeatInMeasure
    //----------------------------------

    @Override
    public int getCurrentMeasureBeat() {
        return mCurrentBeat % 4;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public Sequencer() {
        super();
        setId(SequencerConstants.DEVICE_ID);
    }

    @Override
    protected void initializeEngine(ICausticEngine engine) {
        super.initializeEngine(engine);
        engine.addCoreEventListener(this);
    }

    //--------------------------------------------------------------------------
    //
    // ISequencer API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void clearAutomation() {
        SequencerMessage.CLEAR_AUTOMATION.send(getEngine());
    }

    @Override
    public void clearAutomation(IMachine machine) {
        SequencerMessage.CLEAR_MACHINE_AUTOMATION.send(getEngine(), machine.getIndex());
    }

    @Override
    public void clearPatterns() {
        SequencerMessage.CLEAR_PATTERNS.send(getEngine());
    }

    @Override
    public void exportSong(String exportPath, ExportType type, int quality) {
        String ftype = "";
        String fquality = "";
        if (type != null) {
            ftype = type.getValue();
            fquality = Integer.toString(quality);
        }
        SequencerMessage.EXPORT_SONG.send(getEngine(), exportPath, ftype, fquality);
        if (mOnSongExportListener != null)
            mOnSongExportListener.onComplete();
    }

    @Override
    public void exportSong(String exportPath, ExportType type) {
        SequencerMessage.EXPORT_SONG_DEFAULT.send(getEngine(), exportPath, type.getValue());
        if (mOnSongExportListener != null)
            mOnSongExportListener.onComplete();
    }

    @Override
    public float exportSongProgress() {
        // TODO (mschmalle) an AsyncHandler would work at about 100ms update to
        // send the progress event
        return SequencerMessage.EXPORT_PROGRESS.query(getEngine());
    }

    @Override
    public void playPosition(int beat) {
        SequencerMessage.PLAY_POSITION.send(getEngine(), beat);
    }

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
    @Override
    public void playPositionAt(int bar, int step) {
        // the number of beats in the bars
        int beats = (bar * 4);
        if (step > 0) {
            beats += step / 4;
        }
        playPosition(beats);
    }

    @Override
    public void addPattern(IMachine machine, int bank, int pattern, int start, int end) {
        SequencerMessage.PATTERN_EVENT.send(getEngine(), machine.getIndex(), start, bank, pattern,
                end);
    }

    @Override
    public void removePattern(IMachine machine, int start, int end) {
        SequencerMessage.PATTERN_EVENT.send(getEngine(), machine.getIndex(), start, -1, -1, end);
    }

    @Override
    public void setLoopPoints(int startBar, int endBar) {
        SequencerMessage.LOOP_POINTS.send(getEngine(), startBar, endBar);
    }

    //--------------------------------------------------------------------------
    //
    // Listeners
    //
    //--------------------------------------------------------------------------

    private OnBeatChangeListener mOnBeatChangeListener;

    private OnMeasureChangeListener mOnMeasureChangeListener;

    private OnSongExportListener mOnSongExportListener;

    @Override
    public void setOnBeatChangeListener(OnBeatChangeListener l) {
        mOnBeatChangeListener = l;
    }

    @Override
    public void setOnMeasureChangeListener(OnMeasureChangeListener l) {
        mOnMeasureChangeListener = l;
    }

    @Override
    public void setOnSongExportListener(OnSongExportListener l) {
        mOnSongExportListener = l;
    }

    @Override
    public void OnBeatChanged(int beat) {
        setCurrentBeat(beat);
    }

    @Override
    public void OnMeasureChanged(int measure) {
        setCurrentMeasure(measure);
    }

}
