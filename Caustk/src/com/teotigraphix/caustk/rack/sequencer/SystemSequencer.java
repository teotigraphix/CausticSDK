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

package com.teotigraphix.caustk.rack.sequencer;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.command.CommandBase;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.ISystemSequencer;
import com.teotigraphix.caustk.rack.ISystemSequencer.ExportType;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerBPMChange;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerStepChange;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerTransportChange;
import com.teotigraphix.caustk.rack.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.rack.ISystemSequencer.ShuffleMode;
import com.teotigraphix.caustk.rack.ISystemSequencer.SongEndMode;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.workstation.CaustkComponent;

/**
 * The {@link SystemSequencer} manages the native pattern and song sequencer OSC
 * messages.
 * <p>
 * The only state this class holds is on per session basis that the client will
 * update such as current measure, beat, tempo, whether the sequencer is playing
 * etc.
 * 
 * @author Michael Schmalle
 */
public class SystemSequencer extends CaustkComponent {

    private transient IRack rack;

    private IRack getRack() {
        return rack;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private boolean isPlaying = false;

    @Tag(101)
    private SequencerMode sequencerMode = SequencerMode.Pattern;

    @Tag(102)
    private float bpm = 120f;

    @Tag(103)
    private int currentMeasure = -1;

    @Tag(104)
    private int currentBeat = -1;

    @Tag(105)
    private float currentFloatBeat = -1f;

    @Tag(106)
    private int currentSixteenthStep;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return null;
    }

    //----------------------------------
    // isPlaying
    //----------------------------------

    void setIsPlaying(boolean value) {
        isPlaying = value;
        OutputPanelMessage.PLAY.send(getRack(), isPlaying ? 1 : 0);
        if (!isPlaying) {
            currentBeat = -1;
            currentFloatBeat = -1f;
            currentMeasure = -1;
            currentSixteenthStep = -1;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    //----------------------------------
    // sequencerMode
    //----------------------------------

    public final SequencerMode getSequencerMode() {
        return sequencerMode;
    }

    public void setSequencerMode(SequencerMode value) {
        sequencerMode = value;
        OutputPanelMessage.MODE.send(getRack(), sequencerMode.getValue());
    }

    //----------------------------------
    // bpm
    //----------------------------------

    public void setBPM(float value) {
        if (value == bpm)
            return;
        bpm = value;
        OutputPanelMessage.BPM.send(getRack(), bpm);
        getRack().getDispatcher().trigger(new OnSystemSequencerBPMChange(value));
    }

    public float getBPM() {
        return bpm;
    }

    //    public void setRack(IRack value) {
    //        super.setRack(value);
    //
    //        getController().put(COMMAND_PLAY, SystemSequencerPlayCommand.class);
    //        getController().put(COMMAND_STOP, SystemSequencerStopCommand.class);
    //        getController().put(COMMAND_U_TEMPO, SystemSequencerTempoCommand.class);
    //    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SystemSequencer() {
    }

    //----------------------------------
    // currentMeasure
    //----------------------------------

    public int getCurrentMeasure() {
        return currentMeasure;
    }

    public int getMeasureBeat() {
        return currentBeat % 4;
    }

    //----------------------------------
    // currentBeat
    //----------------------------------

    public int getStep() {
        int step = (currentBeat % 4) * 4;
        return step;
    }

    public int getCurrentSixteenthStep() {
        return currentSixteenthStep;
    }

    public boolean updatePosition(int measure, float beat) {
        if (!isPlaying())
            return false;

        boolean changed = false;

        currentMeasure = measure;
        currentFloatBeat = beat;

        int round = (int)Math.floor(currentFloatBeat);
        if (round != currentBeat) {
            changed = setCurrentBeat(round);
        }

        // sixteenth step calculation
        int step = (int)Math.floor((currentFloatBeat % 4) * 4);
        if (step != currentSixteenthStep) {
            currentSixteenthStep = step;
            getRack().getDispatcher().trigger(new OnSystemSequencerStepChange());
        }

        return changed;
    }

    public int getCurrentBeat() {
        return currentBeat;
    }

    boolean setCurrentBeat(int value) {
        if (value == currentBeat)
            return false;

        currentBeat = value;

        getRack().getDispatcher().trigger(
                new OnSystemSequencerBeatChange(currentMeasure, currentBeat));

        return true;
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        if (context != null)
            rack = context.getRack();

        switch (phase) {
            case Create:
                break;
            case Load:
                break;
            case Restore:
                sequencerMode = SequencerMode
                        .fromInt((int)OutputPanelMessage.MODE.query(getRack()));
                bpm = OutputPanelMessage.BPM.query(getRack());
                break;
            case Update:
                break;
            case Connect:
                break;
            case Disconnect:
                rack = null;
                break;
        }
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void play() {
        play(SequencerMode.Pattern);
    }

    public void play(SequencerMode mode) {
        setSequencerMode(mode);
        setIsPlaying(true);
        getRack().getDispatcher().trigger(new OnSystemSequencerTransportChange());
    }

    public void stop() {
        setIsPlaying(false);
        getRack().getDispatcher().trigger(new OnSystemSequencerTransportChange());
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
            last = getContext().getComponent(ISystemSequencer.class).getBPM();
            float bpm = CommandUtils.getFloat(getContext(), 0);
            getContext().getComponent(ISystemSequencer.class).setBPM(bpm);
        }

        @Override
        protected void undoExecute() {
            getContext().getComponent(ISystemSequencer.class).setBPM(last);
        }
    }

    //--------------------------------------------------------------------------
    // Song

    public void setSongEndMode(SongEndMode mode) {
        SequencerMessage.SONG_END_MODE.send(getRack(), mode.getValue());
    }

    public SongEndMode getSongEndMode() {
        return SongEndMode.fromInt((int)SequencerMessage.SONG_END_MODE.send(getRack()));
    }

    public String getPatterns() {
        return SequencerMessage.QUERY_PATTERN_EVENT.queryString(getRack());
    }

    public void addPattern(RackTone rackTone, int bank, int pattern, int start, int end)
            throws CausticException {
        SequencerMessage.PATTERN_EVENT.send(getRack(), rackTone.getMachineIndex(), start, bank,
                pattern, end);
    }

    public void removePattern(RackTone rackTone, int start, int end) throws CausticException {
        SequencerMessage.PATTERN_EVENT.send(getRack(), rackTone.getMachineIndex(), start, -1, -1,
                end);
    }

    public void setLoopPoints(int startBar, int endBar) {
        SequencerMessage.LOOP_POINTS.send(getRack(), startBar, endBar);
    }

    public void playPosition(int beat) {
        SequencerMessage.PLAY_POSITION.send(getRack(), beat);
    }

    public void playPositionAt(int bar, int step) {
        playPositionAt(bar, step);
    }

    public void exportSong(String exportPath, ExportType type, int quality) {
        String ftype = "";
        String fquality = "";
        if (type != null) {
            ftype = type.getValue();
            fquality = Integer.toString(quality);
        }
        SequencerMessage.EXPORT_SONG.send(getRack(), exportPath, ftype, fquality);
    }

    public void exportSong(String exportPath, ExportType type) {
        exportSong(exportPath, type, 70);
    }

    public float exportSongProgress() {
        return SequencerMessage.EXPORT_PROGRESS.query(getRack());
    }

    public void clearPatterns() {
        SequencerMessage.CLEAR_PATTERNS.send(getRack());
    }

    public void clearAutomation() {
        SequencerMessage.CLEAR_AUTOMATION.send(getRack());
    }

    public void clearAutomation(RackTone rackTone) {
        SequencerMessage.CLEAR_MACHINE_AUTOMATION.send(getRack(), rackTone.getMachineIndex());
    }

    public ShuffleMode getShuffleMode() {
        return ShuffleMode.fromInt((int)OutputPanelMessage.SHUFFLE_MODE.query(getRack()));
    }

    public void setShuffleMode(ShuffleMode value) {
        OutputPanelMessage.SHUFFLE_MODE.send(getRack(), value.getValue());
    }

    public float getShuffleAmount() {
        return OutputPanelMessage.SHUFFLE_AMOUNT.query(getRack());
    }

    public void setShuffleAmount(float value) {
        OutputPanelMessage.SHUFFLE_AMOUNT.send(getRack(), value);
    }
}
