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

package com.teotigraphix.caustk.sequencer.system;

import java.io.Serializable;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.command.CommandBase;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.controller.core.RackComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.tone.Tone;

public class SystemSequencer extends RackComponent implements ISystemSequencer, Serializable {

    private static final long serialVersionUID = 3013567748100411152L;

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // isPlaying
    //----------------------------------

    private boolean isPlaying = false;

    @Override
    public void setIsPlaying(boolean value) {
        isPlaying = value;
        OutputPanelMessage.PLAY.send(getController(), isPlaying ? 1 : 0);
        if (!value) {
            currentBeat = -1;
        }
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    //----------------------------------
    // sequencerMode
    //----------------------------------

    private SequencerMode sequencerMode = SequencerMode.PATTERN;

    @Override
    public final SequencerMode getSequencerMode() {
        return sequencerMode;
    }

    @Override
    public void setSequencerMode(SequencerMode value) {
        sequencerMode = value;
        OutputPanelMessage.MODE.send(getController(), sequencerMode.getValue());
    }

    //----------------------------------
    // tempo
    //----------------------------------

    private float tempo = 120f;

    @Override
    public void setTempo(float value) {
        if (value == tempo)
            return;
        tempo = value;
        OutputPanelMessage.BPM.send(getController(), tempo);
        getController().trigger(new OnSystemSequencerTempoChange(value));
    }

    @Override
    public float getTempo() {
        return tempo;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SystemSequencer(ICaustkController controller) {
        super(controller);
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        getController().addComponent(ISystemSequencer.class, this);

        getController().getCommandManager().put(COMMAND_PLAY, SystemSequencerPlayCommand.class);
        getController().getCommandManager().put(COMMAND_STOP, SystemSequencerStopCommand.class);
        getController().getCommandManager().put(COMMAND_U_TEMPO, SystemSequencerTempoCommand.class);
    }

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    @Override
    public void play(SequencerMode mode) {
        setSequencerMode(mode);
        setIsPlaying(true);
        getController().trigger(new OnSystemSequencerTransportChange());
    }

    @Override
    public void stop() {
        setIsPlaying(false);
        getController().trigger(new OnSystemSequencerTransportChange());
    }

    @Override
    public void restore() {
    }

    //--------------------------------------------------------------------------
    // Public Command API
    //--------------------------------------------------------------------------

    public static class SystemSequencerPlayCommand extends CommandBase {
        @Override
        public void execute() {
            int smode = CommandUtils.getInteger(getContext(), 0);
            SequencerMode mode = SequencerMode.fromInt(smode);
            getContext().getComponent(ISystemSequencer.class).play(mode);
        }
    }

    public static class SystemSequencerStopCommand extends CommandBase {
        @Override
        public void execute() {
            getContext().getComponent(ISystemSequencer.class).stop();
        }
    }

    public static class SystemSequencerTempoCommand extends UndoCommand {

        float last;

        @Override
        protected void doExecute() {
            last = getContext().getComponent(ISystemSequencer.class).getTempo();
            float bpm = CommandUtils.getFloat(getContext(), 0);
            getContext().getComponent(ISystemSequencer.class).setTempo(bpm);
        }

        @Override
        protected void undoExecute() {
            getContext().getComponent(ISystemSequencer.class).setTempo(last);
        }
    }

    //----------------------------------
    // currentMeasure
    //----------------------------------

    private int mCurrentMeasure = 0;

    private int currentMeasure;

    @Override
    public int getCurrentMeasure() {
        return mCurrentMeasure;
    }

    void setCurrentMeasure(int value) {
        mCurrentMeasure = value;
        //        
        //        final int numMeasures = getPattern().getLength();
        //        int last = 1;
        //        if (numMeasures == 2)
        //            last = 
        if (mCurrentMeasure == 0) {
            //controller.getPatternSequencer().playNextPattern();
        }
        //        getDispatcher().trigger(new OnSystemSequencerMeasureChange(mCurrentMeasure));
    }

    public int getMeasureBeat() {
        return currentBeat % 4;
    }

    //----------------------------------
    // currentBeat
    //----------------------------------

    @Override
    public int getStep() {
        int step = (currentBeat % 4) * 4;
        return step;
    }

    private int currentSixteenthStep;

    @Override
    public int getCurrentSixteenthStep() {
        return currentSixteenthStep;
    }

    @Override
    public void beatUpdate(int measure, float beat) {
        if (!isPlaying())
            return;
        currentMeasure = measure;
        floatBeat = beat;

        int round = (int)Math.floor(floatBeat);
        if (round != currentBeat) {
            setCurrentBeat(round);
        }

        // sixteenth step calculation
        int step = (int)Math.floor((floatBeat % 4) * 4);
        if (step != currentSixteenthStep) {
            currentSixteenthStep = step;
            getController().trigger(new OnSystemSequencerStepChange());
        }
    }

    private float floatBeat;

    private int currentBeat = -1;

    @Override
    public int getCurrentBeat() {
        return currentBeat;
    }

    void setCurrentBeat(int value) {
        setCurrentBeat(value, false);
    }

    void setCurrentBeat(int value, boolean seeking) {
        if (value == currentBeat)
            return;

        int beatsInLength = 4;

        int last = currentBeat;
        currentBeat = value;

        getController().trigger(new OnSystemSequencerBeatChange(currentMeasure, currentBeat));

        if (last < value) {
            // forward
            if (currentBeat == 0) {
                setCurrentMeasure(0);
            } else {
                int remainder = currentBeat % beatsInLength;
                //System.out.println("    remainder " + getMeasureBeat());
                if (seeking) {
                    setCurrentMeasure(currentBeat / beatsInLength);
                } else if (remainder == 0) {
                    setCurrentMeasure(mCurrentMeasure + 1);
                }
            }
        } else if (last > value) {
            // reverse
            // if the last beat was a measure change, decrement measure
            int remainder = last % 4;
            if (remainder == 0) {
                setCurrentMeasure(mCurrentMeasure - 1);
            }
        }

    }

    int updateMeasure(int beat) {
        int len = 1; //controller.getPatternSequencer().getPattern().getLength();
        final int remainder = (beat + 1) % 4;
        if (len == 1) {
            if (remainder == 0)
                setCurrentMeasure(0);
        } else if (len == 2) {
            if (beat == 0 || beat == 8 || beat == 16 || beat == 24 || beat == 32)
                setCurrentMeasure(0);
            else if (beat == 4 || beat == 12 || beat == 20 || beat == 28)
                setCurrentMeasure(mCurrentMeasure + 1);
        } else if (len == 4) {
            if (beat == 0 || beat == 16 || beat == 32)
                setCurrentMeasure(0);
            else if (beat == 4 || beat == 8 || beat == 12 || beat == 20 || beat == 24 || beat == 28)
                setCurrentMeasure(mCurrentMeasure + 1);
        } else if (len == 8) {
            if (beat == 0)
                setCurrentMeasure(0);
            else if (remainder == 0)
                setCurrentMeasure(mCurrentMeasure + 1);
        }
        return 0;
    }

    //--------------------------------------------------------------------------
    // Song

    @Override
    public void setSongEndMode(SongEndMode mode) {
        SequencerMessage.SONG_END_MODE.send(getController(), mode.getValue());
    }

    @Override
    public SongEndMode getSongEndMode() {
        return SongEndMode.fromInt((int)SequencerMessage.SONG_END_MODE.send(getController()));
    }

    @Override
    public String getPatterns() {
        return SequencerMessage.QUERY_PATTERN_EVENT.queryString(getController());
    }

    @Override
    public void addPattern(Tone tone, int bank, int pattern, int start, int end)
            throws CausticException {
        SequencerMessage.PATTERN_EVENT.send(getController(), tone.getIndex(), start, bank, pattern,
                end);
    }

    @Override
    public void removePattern(Tone tone, int start, int end) throws CausticException {
        SequencerMessage.PATTERN_EVENT.send(getController(), tone.getIndex(), start, -1, -1, end);
    }

    @Override
    public void setLoopPoints(int startBar, int endBar) {
        SequencerMessage.LOOP_POINTS.send(getController(), startBar, endBar);
    }

    @Override
    public void playPosition(int beat) {
        SequencerMessage.PLAY_POSITION.send(getController(), beat);
    }

    @Override
    public void playPositionAt(int bar, int step) {
        playPositionAt(bar, step);
    }

    @Override
    public void exportSong(String exportPath, ExportType type, int quality) {
        String ftype = "";
        String fquality = "";
        if (type != null) {
            ftype = type.getValue();
            fquality = Integer.toString(quality);
        }
        SequencerMessage.EXPORT_SONG.send(getController(), exportPath, ftype, fquality);
    }

    @Override
    public void exportSong(String exportPath, ExportType type) {
        exportSong(exportPath, type, 70);
    }

    @Override
    public float exportSongProgress() {
        return SequencerMessage.EXPORT_PROGRESS.query(getController());
    }

    @Override
    public void clearPatterns() {
        SequencerMessage.CLEAR_PATTERNS.send(getController());
    }

    @Override
    public void clearAutomation() {
        SequencerMessage.CLEAR_AUTOMATION.send(getController());
    }

    @Override
    public void clearAutomation(Tone tone) {
        SequencerMessage.CLEAR_MACHINE_AUTOMATION.send(getController(), tone.getIndex());
    }

    @Override
    public ShuffleMode getShuffleMode() {
        return ShuffleMode.fromInt((int)OutputPanelMessage.SHUFFLE_MODE.query(getController()));
    }

    @Override
    public void setShuffleMode(ShuffleMode value) {
        OutputPanelMessage.SHUFFLE_MODE.send(getController(), value.getValue());
    }

    @Override
    public float getShuffleAmount() {
        return OutputPanelMessage.SHUFFLE_AMOUNT.query(getController());
    }

    @Override
    public void setShuffleAmount(float value) {
        OutputPanelMessage.SHUFFLE_AMOUNT.send(getController(), value);
    }
}
