////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.groove.session;

import java.util.ArrayList;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.NoteNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.machine.sequencer.TrackComponent;
import com.teotigraphix.caustk.node.machine.sequencer.TrackEntryNode;

public class Clip {

    public enum ClipState {

        /**
         * From: [Stop, Dequeued], To: [Queued, Dequeued].
         */
        Idle,

        /**
         * From: Idle, To: [Idle, Play].
         */
        Queued,

        /**
         * From: Play, To: [Stop, Idle]
         */
        Dequeued,

        /**
         * From: Queued, To: [Dequeued, Stop]
         */
        Play,

        /**
         * From: [Dequeued, Play] To: Idle
         */
        //Stop
    }

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private Scene scene;

    @Tag(1)
    private int machineIndex;

    @Tag(2)
    private ClipInfo info;

    @Tag(3)
    private ClipState state = ClipState.Idle;

    @Tag(4)
    private ClipState lastState;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the machine index of the clip.
     */
    public int getMachineIndex() {
        return machineIndex;
    }

    /**
     * Returns the scene's linear index (0..63).
     */
    public int getSceneLinearIndex() {
        return scene.getLinearIndex();
    }

    /**
     * Returns the scene's bank index (0..3).
     */
    public int getBankIndex() {
        return scene.getBankIndex();
    }

    /**
     * Returns the scene's index within it's bank. (0..16).
     */
    public int getSceneIndex() {
        return scene.getSceneIndex();
    }

    /**
     * Returns the clip's metadata info.
     */
    public ClipInfo getInfo() {
        return info;
    }

    //--------------------------------------------------------------------------
    // Internal :: Properties
    //--------------------------------------------------------------------------

    Scene getScene() {
        return scene;
    }

    MachineNode getMachineNode() {
        return scene.getMachine(machineIndex);
    }

    PatternNode getPattern() {
        return getMachineNode().getSequencer().getPattern(getBankIndex(), getSceneIndex());
    }

    TrackComponent getTrack() {
        return getMachineNode().getTrack();
    }

    //----------------------------------
    // state
    //----------------------------------

    ClipState getState() {
        return state;
    }

    boolean isIdle() {
        return state == ClipState.Idle;
    }

    boolean isPlaying() {
        return state == ClipState.Play;
    }

    boolean isQueded() {
        return state == ClipState.Queued;
    }

    boolean isDequeded() {
        return state == ClipState.Dequeued;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    Clip() {
    }

    public Clip(Scene scene, int machineIndex, ClipInfo info) {
        this.scene = scene;
        this.machineIndex = machineIndex;
        this.info = info;
    }

    void onMeasureStart(int measure, float beat, int sixteenth, int thirtysecond) {

    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    void idle() {
        lastState = state;
        state = ClipState.Idle;
    }

    void queue() {
        lastState = state;
        state = ClipState.Queued;
    }

    void dequeue() {
        lastState = state;
        state = ClipState.Dequeued;
    }

    /**
     * Called when a dequeued playing clip is played again,
     * "while still playing".
     */
    void softPlay() {
        lastState = state;
        state = ClipState.Play;
    }

    void commitPlay() {
        lastState = state;
        state = ClipState.Play;
        // /caustic/sequencer/pattern_event [machin_index] [start_measure] [bank] [pattern] [end_measure] 
        int startMeasure = getScene().getSessionManager().getMeasure();
        int length = getLoopLength();

        // add to sequencer
        TrackComponent track = getTrack();
        if (!track.isContained(startMeasure)) {
            try {
                track.addEntry(getPattern(), startMeasure, startMeasure + length);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    }

    void commitStop() {
        if (lastState == ClipState.Play) {
            System.out.println("Clean up a playing clip that is being stopped");
            TrackComponent track = getTrack();

            TrackEntryNode entry = track.getLastEntry();

            int endMeasure = getScene().getSessionManager().getMeasure(); // 6
            int startMeasure = entry.getStartMeasure(); // 4
            track.trimEntry(entry, startMeasure, endMeasure);
        }

        lastState = state;
        state = ClipState.Idle;
    }

    public void clearNoteData() {
        PatternNode patternNode = getPattern();
        patternNode.clear();
    }

    public void setNoteData(String data) {
        PatternNode patternNode = getPattern();
        patternNode.setNoteData(data);
    }

    public void setNoteData(ArrayList<NoteNode> notes) {
        PatternNode patternNode = getPattern();
        patternNode.setNoteData(notes);
    }

    public int getLoopLength() {
        return getPattern().getNumMeasures();
    }

    public void setLoopLength(int length) {
        PatternNode patternNode = getPattern();
        patternNode.setNumMeasures(length);
    }

    @Override
    public String toString() {
        return "Clip [machineIndex=" + machineIndex + ", name=" + info.getName() + ", state="
                + state + "]";
    }

}
