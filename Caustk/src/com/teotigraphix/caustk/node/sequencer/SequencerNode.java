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

package com.teotigraphix.caustk.node.sequencer;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.TrackComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link SequencerNode} API manages the song/pattern sequencer and output
 * panel (play/stop/bpm).
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class SequencerNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private RackNode rackNode;

    @Tag(51)
    private boolean playing = false;

    @Tag(52)
    private SequencerMode sequencerMode = SequencerMode.Pattern;

    @Tag(53)
    private SongEndMode songEndMode = SongEndMode.Stop;

    @Tag(54)
    private float bpm = 120f;

    @Tag(55)
    private ShuffleMode shuffleMode = ShuffleMode.Sixteenth;

    @Tag(56)
    private float shuffleAmount = 0f;

    @Tag(75)
    private boolean recording = false;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // isPlaying
    //----------------------------------

    /**
     * Returns whether the output panel is playing the pattern or song
     * sequencer.
     */
    public final boolean isPlaying() {
        return playing;
    }

    final void setPlaying(boolean playing) {
        if (playing == this.playing)
            return;
        this.playing = playing;
        OutputPanelMessage.PLAY.send(getRack(), playing ? 1 : 0);
        resetPostion();
        post(new SequencerNodeTransportChangeEvent(this, playing, recording));
    }

    //----------------------------------
    // sequencerMode
    //----------------------------------

    /**
     * Returns the current
     * {@link com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerMode}
     * .
     */
    public final SequencerMode getSequencerMode() {
        return sequencerMode;
    }

    public final SequencerMode querySequencerMode() {
        return SequencerMode.fromInt((int)OutputPanelMessage.MODE.query(getRack()));
    }

    /**
     * Sets the current
     * {@link com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerMode}
     * , if the sequencer is playing, it will continue to play when this value
     * is change.
     * 
     * @param sequencerMode The new sequencer mode.
     * @see com.teotigraphix.caustk.core.osc.OutputPanelMessage#MODE
     */
    public final void setSequencerMode(SequencerMode sequencerMode) {
        if (sequencerMode == this.sequencerMode)
            return;
        this.sequencerMode = sequencerMode;
        OutputPanelMessage.MODE.send(getRack(), sequencerMode.getValue());
    }

    //----------------------------------
    // songEndMode
    //----------------------------------

    /**
     * Returns the song end mode for the song sequencer.
     */
    public final SongEndMode getSongEndMode() {
        return songEndMode;
    }

    public final SongEndMode querySongEndMode() {
        return SongEndMode.fromInt((int)SequencerMessage.SONG_END_MODE.query(getRack()));
    }

    /**
     * Sets the song end mode (Play, Stop, Loop).
     * 
     * @param songEndMode The new
     *            {@link com.teotigraphix.caustk.node.sequencer.SequencerNode.SongEndMode}
     *            .
     * @see com.teotigraphix.caustk.core.osc.SequencerMessage#SONG_END_MODE
     */
    public final void setSongEndMode(SongEndMode songEndMode) {
        this.songEndMode = songEndMode;
        SequencerMessage.SONG_END_MODE.send(getRack(), songEndMode.getValue());
    }

    //----------------------------------
    // bpm
    //----------------------------------

    /**
     * Returns the beats per minute of the internal output panel.
     */
    public final float getBPM() {
        return bpm;
    }

    public final float queryBPM() {
        return OutputPanelMessage.BPM.query(getRack());
    }

    /**
     * Sets the beats per minute of the internal output panel.
     * 
     * @param bpm The beats per minute (60.0..250).
     * @see com.teotigraphix.caustk.core.osc.OutputPanelMessage#BPM
     * @see com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerNodeBPMChangeEvent
     */
    public final void setBPM(float bpm) {
        if (bpm == this.bpm)
            return;
        this.bpm = bpm;
        OutputPanelMessage.BPM.send(getRack(), bpm);
        post(new SequencerNodeBPMChangeEvent(this, bpm));
    }

    //----------------------------------
    // shuffleMode
    //----------------------------------

    /**
     * Returns the sequencer's shuffle mode.
     */
    public final ShuffleMode getShuffleMode() {
        return shuffleMode;
    }

    public final ShuffleMode queryShuffleMode() {
        return ShuffleMode.fromInt((int)OutputPanelMessage.SHUFFLE_MODE.query(getRack()));
    }

    /**
     * Sets the sequencer's shuffle mode.
     * 
     * @param shuffleMode The
     *            {@link com.teotigraphix.caustk.node.sequencer.SequencerNode.ShuffleMode}
     *            .
     * @see com.teotigraphix.caustk.core.osc.OutputPanelMessage#SHUFFLE_MODE
     */
    public final void setShuffleMode(ShuffleMode shuffleMode) {
        if (shuffleMode == this.shuffleMode)
            return;
        this.shuffleMode = shuffleMode;
        OutputPanelMessage.SHUFFLE_MODE.send(getRack(), shuffleMode.getValue());
    }

    //----------------------------------
    // shuffleAmount
    //----------------------------------

    /**
     * Returns the sequencer's shuffle amount.
     */
    public float getShuffleAmount() {
        return shuffleAmount;
    }

    public float queryShuffleAmount() {
        return OutputPanelMessage.SHUFFLE_AMOUNT.query(getRack());
    }

    /**
     * Sets the sequencer's shuffle amount.
     * 
     * @param shuffleAmount The shuffle amount(0..1).
     * @see com.teotigraphix.caustk.core.osc.OutputPanelMessage#SHUFFLE_AMOUNT
     */
    public void setShuffleAmount(float shuffleAmount) {
        if (shuffleAmount == this.shuffleAmount)
            return;
        this.shuffleAmount = shuffleAmount;
        OutputPanelMessage.SHUFFLE_AMOUNT.send(getRack(), shuffleAmount);
    }

    //----------------------------------
    // recording
    //----------------------------------

    /**
     * Returns whether the output panel is playing the pattern or song
     * sequencer.
     */
    public final boolean isRecording() {
        return recording;
    }

    /**
     * Sets recording state.
     * 
     * @param recording Whether recording.
     * @see com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerNodeTransportChangeEvent
     */
    public final void setRecording(boolean recording) {
        if (recording == this.recording)
            return;
        this.recording = recording;
        post(new SequencerNodeTransportChangeEvent(this, playing, recording));
    }

    //----------------------------------
    // track
    //----------------------------------

    /**
     * Returns the
     * {@link com.teotigraphix.caustk.node.machine.sequencer.TrackComponent} for
     * the machine index.
     * <p>
     * Convenience;
     * {@link com.teotigraphix.caustk.core.CaustkRack#getRackNode()} must not be
     * <code>null</code>.
     * 
     * @param machineIndex The machine index for the track(0..13).
     */
    public TrackComponent getTrack(int machineIndex) {
        return getRack().get(machineIndex).getTrack();
    }

    List<TrackComponent> getTracks() {
        ArrayList<TrackComponent> result = new ArrayList<TrackComponent>();
        for (int i = 0; i < 14; i++) {
            MachineNode machine = getRack().get(i);
            if (machine != null) {
                result.add(machine.getTrack());
            }
        }
        return result;
    }

    /**
     * Returns the full measure count taking into account all existing track
     * measure counts.
     * <p>
     * This method returns the greatest track measure count.
     */
    public int getMeasureCount() {
        int appendMeasure = 0;
        for (TrackComponent trackNode : getTracks()) {
            if (trackNode.getAppendMeasure() > appendMeasure)
                appendMeasure = trackNode.getAppendMeasure();
        }
        return appendMeasure;
    }

    //--------------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------------

    /**
     * Serialization.
     */
    public SequencerNode() {
    }

    public SequencerNode(RackNode rackNode) {
        this.rackNode = rackNode;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Plays the pattern sequencer using
     * {@link com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerMode#Pattern}
     * .
     * 
     * @see com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerNodeTransportChangeEvent
     */
    public void playLooped() {
        play(SequencerMode.Pattern);
    }

    /**
     * Plays the song sequencer using
     * {@link com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerMode#Song}
     * .
     * 
     * @see com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerNodeTransportChangeEvent
     */
    public void play() {
        play(SequencerMode.Song);
    }

    /**
     * Plays the internal sequencer, which sequencer is determined by the mode
     * parameter.
     * 
     * @param mode The
     *            {@link com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerMode}
     *            , Pattern or Song.
     * @see com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerNodeTransportChangeEvent
     */
    public void play(SequencerMode mode) {
        setSequencerMode(mode);
        setPlaying(true);
    }

    /**
     * Forces a stop() then play()
     */
    public void restart(SequencerMode mode) {
        setSequencerMode(mode);
        playing = false;
        resetPostion();
        playing = true;
        OutputPanelMessage.PLAY.send(getRack(), 1);
        post(new SequencerNodeTransportChangeEvent(this, playing, recording));
    }

    /**
     * Stops the current sequencer from playing.
     * 
     * @see com.teotigraphix.caustk.node.sequencer.SequencerNode.SequencerNodeTransportChangeEvent
     */
    public void stop() {
        setPlaying(false);
    }

    /**
     * Moves the song sequencer's play head to the specific beat.
     * 
     * @param beat The beat to start playing in the song sequencer.
     */
    public void playPosition(float beat) {
        SequencerMessage.PLAY_POSITION.send(getRack(), beat);
    }

    /**
     * Sets the loop point measures in the song sequencer.
     * <p>
     * Loop points are used with song mode play and exporting media.
     * 
     * @param startMeasure The start measure.
     * @param endMeasure The end measure.
     */
    public void setLoopPoints(int startMeasure, int endMeasure) {
        SequencerMessage.LOOP_POINTS.send(getRack(), startMeasure, endMeasure);
    }

    /**
     * Exports a song as an .wav, .ogg or .mid file depending on the export
     * options.
     * 
     * @param loopMode The type of export, loop or song.
     * @param exportPath The absolute path for the exported media.
     * @param type The
     *            {@link com.teotigraphix.caustk.node.sequencer.SequencerNode.ExportType}
     *            of media.
     * @param quality The quality of media as a percentage (10..100).
     */
    public void exportSong(ExportLoopMode loopMode, ExportType type, int quality, String exportPath) {
        // caustic/export [loop_mode] [format] [quality] [path] 
        SequencerMessage.EXPORT.send(getRack(), loopMode.getValue(), type.getValue(), quality,
                exportPath);
    }

    /**
     * Returns the media's export progress (0..100).
     */
    public float exportSongProgress() {
        return SequencerMessage.EXPORT_PROGRESS.query(getRack());
    }

    // XXX These are going to have to operate on each TackNode

    /**
     * Clears all patterns from the song sequencer.
     */
    public void clearPatterns() {
        SequencerMessage.CLEAR_PATTERNS.send(getRack());
    }

    /**
     * Clears all automation from the song sequencer.
     */
    public void clearAutomation() {
        SequencerMessage.CLEAR_AUTOMATION.send(getRack());
    }

    /**
     * Clears all automation for a machine in the song sequencer.
     * 
     * @param machineIndex The machine index (0..13).
     */
    public void clearAutomation(int machineIndex) {
        SequencerMessage.CLEAR_MACHINE_AUTOMATION.send(getRack(), machineIndex);
    }

    //--------------------------------------------------------------------------
    // Frame :: Methods
    //--------------------------------------------------------------------------

    private transient boolean beatChanged = false;

    private transient boolean sixteenthChanged = false;

    private transient boolean thirtysecondChanged = false;

    private transient int currentMeasure = -1;

    private transient int currentBeat = -1;

    private transient float currentFloatBeat = -1f;

    private transient int currentSixteenthStep;

    private transient int currentThritySecondStep;

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

    public int getCurrentBeat() {
        return currentBeat;
    }

    public float getCurrentFloatBeat() {
        return currentFloatBeat;
    }

    private boolean setCurrentBeat(int value) {
        if (value == currentBeat)
            return false;

        currentBeat = value;
        return true;
    }

    public int getStep() {
        int step = (currentBeat % 4) * 4;
        return step;
    }

    public int getCurrentSixteenthStep() {
        return currentSixteenthStep;
    }

    public int getCurrentThritySecondStep() {
        return currentThritySecondStep;
    }

    /**
     * True during the frame when a full beat has changed (0..31) in pattern
     * mode.
     * 
     * @see SystemSequencer#getCurrentBeat()
     */
    public boolean isBeatChanged() {
        return beatChanged;
    }

    /**
     * True during the frame when a sixteenth step has changed.
     * 
     * @see SystemSequencer#getCurrentSixteenthStep()
     */
    public boolean isSixteenthChanged() {
        return sixteenthChanged;
    }

    /**
     * True during the frame when a thirty second step has changed.
     * 
     * @see SystemSequencer#getCurrentThritySecondStep()
     */
    public boolean isThirtysecondChanged() {
        return thirtysecondChanged;
    }

    /**
     * Called during a game loop frame change, updates position of the sequencer
     * using native beat & measure values.
     * 
     * @param delta The millsecond delta from the last frame change.
     */
    public void frameChanged(float delta) {
        beatChanged = false;
        sixteenthChanged = false;
        thirtysecondChanged = false;

        final int measure = (int)getRack().getCurrentSongMeasure();
        final float beat = getRack().getCurrentBeat();

        // XXX Not sure if I want to introduce these events again
        // maybe its just better to pool the boolean 'is' changed
        // methods inside the applications ... uh yeah it all needs to be
        // event based because the only loop is the frame change with LibGDX

        beatChanged = updatePosition(measure, beat);
        if (beatChanged) {
        }

        sixteenthChanged = updateStep(measure, beat);
        if (sixteenthChanged) {
        }

        thirtysecondChanged = updateThritySecondStep(measure, beat);
        if (thirtysecondChanged) {
        }
    }

    private boolean updatePosition(int measure, float beat) {
        if (!isPlaying())
            return false;

        boolean changed = false;

        currentMeasure = measure;
        currentFloatBeat = beat;

        int round = (int)Math.floor(currentFloatBeat);
        if (round != currentBeat) {
            changed = setCurrentBeat(round);
        }

        return changed;
    }

    private boolean updateStep(int measure, float beat) {
        int step = (int)Math.floor((beat % 4) * 4);
        if (step != currentSixteenthStep) {
            currentSixteenthStep = step;
            return true;
        }
        return false;
    }

    private boolean updateThritySecondStep(int measure, float beat) {
        int step = (int)Math.floor((beat % 4) * 8);
        if (step != currentThritySecondStep) {
            currentThritySecondStep = step;
            return true;
        }
        return false;
    }

    private void resetPostion() {
        if (!playing) {
            currentBeat = -1;
            currentFloatBeat = -1f;
            currentMeasure = -1;
            currentSixteenthStep = -1;
        }
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        OutputPanelMessage.BPM.send(getRack(), bpm);
        OutputPanelMessage.MODE.send(getRack(), sequencerMode.getValue());
        OutputPanelMessage.SHUFFLE_AMOUNT.send(getRack(), shuffleAmount);
        OutputPanelMessage.SHUFFLE_MODE.send(getRack(), shuffleMode.getValue());
        SequencerMessage.SONG_END_MODE.send(getRack(), songEndMode.getValue());
    }

    @Override
    protected void restoreComponents() {
        setBPM(queryBPM());
        setSequencerMode(querySequencerMode());
        setShuffleAmount(queryShuffleAmount());
        setShuffleMode(queryShuffleMode());
        setSongEndMode(querySongEndMode());
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     */
    public enum SequencerMode {

        /**
         * Sequencer pattern mode.
         */
        Pattern(0),

        /**
         * Sequencer song mode.
         */
        Song(1);

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
                    return Song;
                default:
                    return Pattern;
            }
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     */
    public enum ShuffleMode {

        Eighth(1),

        Sixteenth(2);

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
     * The export type for external media from the song sequencer.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public enum ExportType {

        /**
         * Export as a <code>*.wav</code> file.
         */
        Wav("WAV"),

        /**
         * Export as a <code>*.ogg</code> file.
         */
        Ogg("OGG"),

        /**
         * Export as a <code>*.mid</code> file.
         */
        Mid("MID");

        private final String value;

        ExportType(String value) {
            this.value = value;
        }

        /**
         * Returns the String value.
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The export loop type for external media from the song sequencer.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public enum ExportLoopMode {

        /**
         * Export a loop using the song's loop points.
         */
        Loop("loop"),

        /**
         * Exports the whole song.
         */
        Song("song");

        private final String value;

        ExportLoopMode(String value) {
            this.value = value;
        }

        /**
         * Returns the String value.
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The mode(0,1,2) keep playing, stop, loop to start.
     */
    public enum SongEndMode {

        /**
         * Keep playing.
         */
        Play(0),

        /**
         * Stop at last measure.
         */
        Stop(1),

        /**
         * Loop to start measure from end.
         */
        Loop(2);

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

    public static class SequencerNodeTransportChangeEvent extends NodeEvent {

        private boolean playing;

        private boolean recording;

        public boolean isPlaying() {
            return playing;
        }

        public boolean isRecording() {
            return recording;
        }

        public SequencerNodeTransportChangeEvent(NodeBase target, boolean playing, boolean recording) {
            super(target);
            this.playing = playing;
            this.recording = recording;
        }
    }

    public static class SequencerNodeBPMChangeEvent extends NodeEvent {
        private float bpm;

        public float getBPM() {
            return bpm;
        }

        public SequencerNodeBPMChangeEvent(NodeBase target, float bpm) {
            super(target);
            this.bpm = bpm;
        }
    }

    @Override
    public String toString() {
        return "SequencerNode [isPlaying=" + playing + ", sequencerMode=" + sequencerMode
                + ", songEndMode=" + songEndMode + ", bpm=" + bpm + ", shuffleMode=" + shuffleMode
                + ", shuffleAmount=" + shuffleAmount + "]";
    }
}
