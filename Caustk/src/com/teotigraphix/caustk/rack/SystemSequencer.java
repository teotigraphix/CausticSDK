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

package com.teotigraphix.caustk.rack;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.command.CommandBase;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.rack.tone.Tone;

public class SystemSequencer extends RackComponent implements ISystemSequencer {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private boolean isPlaying = false;

    @Tag(101)
    private SequencerMode sequencerMode = SequencerMode.PATTERN;

    @Tag(102)
    private float tempo = 120f;

    @Tag(103)
    private int currentMeasure;

    @Tag(104)
    private int currentBeat;

    @Tag(105)
    private float floatBeat;

    @Tag(106)
    // XXX Should this be serialized?
    private int currentSixteenthStep;

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // isPlaying
    //----------------------------------

    @Override
    public void setIsPlaying(boolean value) {
        isPlaying = value;
        OutputPanelMessage.PLAY.send(getRack(), isPlaying ? 1 : 0);
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

    @Override
    public final SequencerMode getSequencerMode() {
        return sequencerMode;
    }

    @Override
    public void setSequencerMode(SequencerMode value) {
        sequencerMode = value;
        OutputPanelMessage.MODE.send(getRack(), sequencerMode.getValue());
    }

    //----------------------------------
    // tempo
    //----------------------------------

    @Override
    public void setTempo(float value) {
        if (value == tempo)
            return;
        tempo = value;
        OutputPanelMessage.BPM.send(getRack(), tempo);
        getRack().getGlobalDispatcher().trigger(new OnSystemSequencerTempoChange(value));
    }

    @Override
    public float getTempo() {
        return tempo;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SystemSequencer() {
    }

    public SystemSequencer(Rack rack) {
        super(rack);
    }

    @Override
    public void registerObservers() {
        super.registerObservers();

        getRack().put(COMMAND_PLAY, SystemSequencerPlayCommand.class);
        getRack().put(COMMAND_STOP, SystemSequencerStopCommand.class);
        getRack().put(COMMAND_U_TEMPO, SystemSequencerTempoCommand.class);
    }

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    @Override
    public void play(SequencerMode mode) {
        setSequencerMode(mode);
        setIsPlaying(true);
        getRack().getGlobalDispatcher().trigger(new OnSystemSequencerTransportChange());
    }

    @Override
    public void stop() {
        setIsPlaying(false);
        getRack().getGlobalDispatcher().trigger(new OnSystemSequencerTransportChange());
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

    @Override
    public int getCurrentMeasure() {
        return currentMeasure;
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

    @Override
    public int getCurrentSixteenthStep() {
        return currentSixteenthStep;
    }

    @Override
    public void beatChange(int measure, float beat) {
        if (!isPlaying())
            return;
        currentMeasure = measure;
        floatBeat = beat;

        int round = (int)Math.floor(floatBeat);
        if (round != currentBeat) {
            setCurrentBeat(round);
            //CtkDebug.log("Beat:" + currentBeat);
        }

        // sixteenth step calculation
        int step = (int)Math.floor((floatBeat % 4) * 4);
        if (step != currentSixteenthStep) {
            currentSixteenthStep = step;
            //CtkDebug.log("Step:" + currentSixteenthStep);
            getGlobalDispatcher().trigger(new OnSystemSequencerStepChange());
        }
    }

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

        currentBeat = value;

        getGlobalDispatcher().trigger(new OnSystemSequencerBeatChange(currentMeasure, currentBeat));
    }

    //--------------------------------------------------------------------------
    // Song

    @Override
    public void setSongEndMode(SongEndMode mode) {
        SequencerMessage.SONG_END_MODE.send(getRack(), mode.getValue());
    }

    @Override
    public SongEndMode getSongEndMode() {
        return SongEndMode.fromInt((int)SequencerMessage.SONG_END_MODE.send(getRack()));
    }

    @Override
    public String getPatterns() {
        return SequencerMessage.QUERY_PATTERN_EVENT.queryString(getRack());
    }

    @Override
    public void addPattern(Tone tone, int bank, int pattern, int start, int end)
            throws CausticException {
        SequencerMessage.PATTERN_EVENT.send(getRack(), tone.getIndex(), start, bank, pattern, end);
    }

    @Override
    public void removePattern(Tone tone, int start, int end) throws CausticException {
        SequencerMessage.PATTERN_EVENT.send(getRack(), tone.getIndex(), start, -1, -1, end);
    }

    @Override
    public void setLoopPoints(int startBar, int endBar) {
        SequencerMessage.LOOP_POINTS.send(getRack(), startBar, endBar);
    }

    @Override
    public void playPosition(int beat) {
        SequencerMessage.PLAY_POSITION.send(getRack(), beat);
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
        SequencerMessage.EXPORT_SONG.send(getRack(), exportPath, ftype, fquality);
    }

    @Override
    public void exportSong(String exportPath, ExportType type) {
        exportSong(exportPath, type, 70);
    }

    @Override
    public float exportSongProgress() {
        return SequencerMessage.EXPORT_PROGRESS.query(getRack());
    }

    @Override
    public void clearPatterns() {
        SequencerMessage.CLEAR_PATTERNS.send(getRack());
    }

    @Override
    public void clearAutomation() {
        SequencerMessage.CLEAR_AUTOMATION.send(getRack());
    }

    @Override
    public void clearAutomation(Tone tone) {
        SequencerMessage.CLEAR_MACHINE_AUTOMATION.send(getRack(), tone.getIndex());
    }

    @Override
    public ShuffleMode getShuffleMode() {
        return ShuffleMode.fromInt((int)OutputPanelMessage.SHUFFLE_MODE.query(getRack()));
    }

    @Override
    public void setShuffleMode(ShuffleMode value) {
        OutputPanelMessage.SHUFFLE_MODE.send(getRack(), value.getValue());
    }

    @Override
    public float getShuffleAmount() {
        return OutputPanelMessage.SHUFFLE_AMOUNT.query(getRack());
    }

    @Override
    public void setShuffleAmount(float value) {
        OutputPanelMessage.SHUFFLE_AMOUNT.send(getRack(), value);
    }
}
