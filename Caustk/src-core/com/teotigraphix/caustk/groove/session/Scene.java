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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class Scene {

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private SceneManager sceneManager;

    @Tag(1)
    private int index;

    @Tag(2)
    private int bankIndex;

    @Tag(3)
    private int sceneIndex;

    @Tag(4)
    private Map<Integer, Clip> clips = new HashMap<Integer, Clip>(14);

    @Tag(5)
    private boolean playing;

    @Tag(6)
    private boolean queded;

    @Tag(10)
    private String name;

    private ArrayList<Clip> queueClips = new ArrayList<Clip>();

    private ArrayList<Clip> dequeueClips = new ArrayList<Clip>();

    private ArrayList<Clip> playClips = new ArrayList<Clip>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public int getIndex() {
        return index;
    }

    public int getBankIndex() {
        return bankIndex;
    }

    public int getSceneIndex() {
        return sceneIndex;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isQueded() {
        return queded;
    }

    public String getName() {
        return name;
    }

    Collection<Clip> getClips() {
        return clips.values();
    }

    MachineNode getMachine(int machineIndex) {
        return sceneManager.getMachine(machineIndex);
    }

    public boolean containsClip(int index) {
        return clips.containsKey(index);
    }

    public Clip getClip(int index) {
        return clips.get(index);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    Scene() {
    }

    public Scene(SceneManager sceneManager, String name, int index, int bankIndex, int sceneIndex) {
        this.sceneManager = sceneManager;
        this.name = name;
        this.index = index;
        this.bankIndex = bankIndex;
        this.sceneIndex = sceneIndex;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    void onMeasureStart(int measure, float beat, int sixteenth, int thirtysecond) {
        for (Clip clip : queueClips) {
            clip.play();
            playClips.add(clip);
        }
        queueClips.clear();
        for (Clip clip : clips.values()) {
            clip.onMeasureStart(measure, beat, sixteenth, thirtysecond);
        }
    }

    public Clip addClip(int machineIndex) {
        Clip clip = new Clip(this, machineIndex);
        clips.put(machineIndex, clip);
        return clip;
    }

    public Clip removeClip(int trackIndex) {
        Clip clip = clips.remove(trackIndex);
        return clip;
    }

    public void queue() {
        for (Clip clip : clips.values()) {
            clip.queue();
        }
    }

    public Clip touch(int index) {
        Clip clip = clips.get(index);
        if (!clip.isPlaying() && !clip.isQueded()) { // stopped AND notQueued
            //queue(clip);
            clip.queue();
            queueClips.add(clip);
        } else if (clip.isPlaying()) { // playing
            //dequeue(clip);
            clip.dequeue();
            dequeueClips.add(clip);
        } else if (clip.isQueded() && !clip.isPlaying()) { // queued AND notPlaying
            //stop(clip);
            clip.stop();
            queueClips.remove(clip);
        }
        return null;
    }

    public void play() {
        for (Clip clip : clips.values()) {
            clip.play();
        }
    }

    public void stop() {
        for (Clip clip : clips.values()) {
            clip.stop();
        }
    }

    @Override
    public String toString() {
        return "Scene [index=" + index + ", bankIndex=" + bankIndex + ", sceneIndex=" + sceneIndex
                + ", playing=" + playing + ", name=" + name + "]";
    }

}
