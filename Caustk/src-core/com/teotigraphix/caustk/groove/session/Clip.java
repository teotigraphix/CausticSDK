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

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private Scene scene;

    @Tag(1)
    private int index; // machineIndex

    @Tag(2)
    private ClipInfo info;

    @Tag(3)
    private ClipState state = ClipState.Idle;

    @Tag(4)
    private ClipState lastState;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the track index (row) of the clip within it's owner {@link Scene}
     * .
     */
    public int getIndex() {
        return index;
    }

    //----------------------------------
    // info
    //----------------------------------

    /**
     * Returns the clip's metadata info.
     */
    public ClipInfo getInfo() {
        return info;
    }

    /**
     * Returns the clip's scene, read-only interface.
     */
    public Scene getScene() {
        return scene;
    }

    public boolean isIdle() {
        return isStateIdle();
    }

    public boolean isPlaying() {
        return isStatePlaying();
    }

    public boolean isQueued() {
        return isStateQueded();
    }

    public boolean isDequeued() {
        return isStateDequeded();
    }

    //--------------------------------------------------------------------------
    // Internal :: Properties
    //--------------------------------------------------------------------------

    MachineNode getMachineNode() {
        return scene.getMachine(index);
    }

    public boolean hasPattern() {
        PatternNode pattern = findPattern();
        if (pattern == null)
            return false;
        return pattern.getNotes().size() > 0;
    }

    PatternNode findPattern() {
        return getMachineNode().getSequencer().findPattern(scene.getBankIndex(),
                scene.getMatrixIndex());
    }

    PatternNode getPattern() {
        return getMachineNode().getSequencer().getPattern(scene.getBankIndex(),
                scene.getMatrixIndex());
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

    boolean isStateIdle() {
        return state == ClipState.Idle;
    }

    boolean isStatePlaying() {
        return state == ClipState.Play;
    }

    boolean isStateQueded() {
        return state == ClipState.Queued;
    }

    boolean isStateDequeded() {
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
        this.index = machineIndex;
        this.info = info;
    }

    void onMeasureStart(int measure, float beat, int sixteenth, int thirtysecond) {

    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void clearNoteData() {
        if (!hasPattern())
            return;
        PatternNode patternNode = getPattern();
        patternNode.clear();
    }

    public void setNoteData(String data) {
        if (!hasPattern())
            return;
        PatternNode patternNode = getPattern();
        patternNode.setNoteData(data);
    }

    public void setNoteData(ArrayList<NoteNode> notes) {
        if (!hasPattern())
            return;
        PatternNode patternNode = getPattern();
        patternNode.setNoteData(notes);
    }

    public int getLoopLength() {
        if (!hasPattern())
            return -1;
        return getPattern().getNumMeasures();
    }

    public void setLoopLength(int length) {
        if (!hasPattern())
            return;
        PatternNode patternNode = getPattern();
        patternNode.setNumMeasures(length);
    }

    //--------------------------------------------------------------------------
    // Internal :: Methods
    //--------------------------------------------------------------------------

    void refresh() {
    }

    void idleState() {
        lastState = state;
        state = ClipState.Idle;
    }

    void queueState() {
        lastState = state;
        state = ClipState.Queued;
    }

    void dequeueState() {
        lastState = state;
        state = ClipState.Dequeued;
    }

    /**
     * Called when a dequeued playing clip is played again,
     * "while still playing".
     */
    void softPlayState() {
        lastState = state;
        state = ClipState.Play;
    }

    void commitPlay() {
        lastState = state;
        state = ClipState.Play;
        SessionManager sessionManager = getScene().getSessionManager();

        // /caustic/sequencer/pattern_event [machin_index] [start_measure] [bank] [pattern] [end_measure] 
        int nextMeasure = sessionManager.getNextMeasure();
        int length = getLoopLength();

        //System.out.println("Clip - current" + currentMeasure);
        // add to sequencer
        if (sessionManager.isRecording()) {
            TrackComponent track = getTrack();
            if (!track.isContained(nextMeasure)) {
                try {
                    TrackEntryNode entry = track.addEntry(getPattern(), nextMeasure, nextMeasure
                            + length);
                    entry.setClip(this);
                } catch (CausticException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    void commitStop() {
        if (lastState == ClipState.Play) {
            System.out.println("Clean up a playing clip that is being stopped");
            TrackComponent track = getTrack();

            TrackEntryNode entry = track.getLastEntry();
            SessionManager sessionManager = getScene().getSessionManager();
            int startMeasure = entry.getStartMeasure(); // 4
            int endMeasure = getScene().getSessionManager().getCurrentMeasure() + 1; // 6
            //System.out.println("Clip - startMeasure" + startMeasure + ", endMeasure" + endMeasure);

            if (sessionManager.isRecording()) {
                if (startMeasure == endMeasure) {
                    System.out.println("Error: start == end " + this);
                } else {
                    track.trimEntry(entry, startMeasure, endMeasure);
                }
            }

        }

        lastState = state;
        state = ClipState.Idle;
    }

    @Override
    public String toString() {
        return "Clip [machineIndex=" + index + ", name=" + info.getName() + ", state=" + state
                + "]";
    }

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
        Play;
    }

}
