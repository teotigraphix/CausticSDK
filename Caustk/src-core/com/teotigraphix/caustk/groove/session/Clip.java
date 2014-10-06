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
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.NoteNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;

public class Clip {

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private Scene scene;

    @Tag(1)
    private int machineIndex;

    @Tag(2)
    private String name;

    @Tag(3)
    private boolean playing;

    @Tag(4)
    private boolean queded;

    @Tag(4)
    private boolean dequeded;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    Scene getScene() {
        return scene;
    }

    public int getMachineIndex() {
        return machineIndex;
    }

    public int getIndex() {
        return scene.getIndex();
    }

    public int getBankIndex() {
        return scene.getBankIndex();
    }

    public int getSceneIndex() {
        return scene.getSceneIndex();
    }

    public String getName() {
        return name;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isQueded() {
        return queded;
    }

    public boolean isDequeded() {
        return dequeded;
    }

    MachineNode getMachineNode() {
        return scene.getMachine(machineIndex);
    }

    PatternNode getPattern() {
        return getMachineNode().getSequencer().getPattern(getBankIndex(), getSceneIndex());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    Clip() {
    }

    public Clip(Scene scene, int machineIndex) {
        this.scene = scene;
        this.machineIndex = machineIndex;
    }

    void onMeasureStart(int measure, float beat, int sixteenth, int thirtysecond) {

    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    void play() {
        playing = true;
        queded = false;
    }

    void queue() {
        playing = false;
        queded = true;
    }

    void dequeue() {
        queded = true;
        dequeded = true;
        // playing could be true/false
    }

    void stop() {
        playing = false;
        queded = false;
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

}
