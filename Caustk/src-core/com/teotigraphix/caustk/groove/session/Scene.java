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
    private int linearIndex;

    @Tag(2)
    private int bankIndex;

    @Tag(3)
    private int sceneIndex;

    @Tag(4)
    private Map<Integer, Clip> clips = new HashMap<Integer, Clip>(14);

    @Tag(10)
    private SceneInfo info;

    private ArrayList<Clip> queueClips = new ArrayList<Clip>();

    private ArrayList<Clip> dequeueClips = new ArrayList<Clip>();

    private ArrayList<Clip> playClips = new ArrayList<Clip>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public int getLinearIndex() {
        return linearIndex;
    }

    public int getBankIndex() {
        return bankIndex;
    }

    public int getSceneIndex() {
        return sceneIndex;
    }

    public boolean isPlaying() {
        for (Clip clip : clips.values()) {
            if (clip.isPlaying())
                return true;
        }
        return false;
    }

    public boolean isQueded() {
        if (isPlaying())
            return false;
        for (Clip clip : clips.values()) {
            if (clip.isQueded())
                return true;
        }
        return false;
    }

    public String getName() {
        return info.getName();
    }

    //--------------------------------------------------------------------------
    // Internal :: Properties
    //--------------------------------------------------------------------------

    SceneManager getSceneManager() {
        return sceneManager;
    }

    SessionManager getSessionManager() {
        return sceneManager.getSessionManager();
    }

    Collection<Clip> getClips() {
        return clips.values();
    }

    MachineNode getMachine(int machineIndex) {
        return sceneManager.getMachine(machineIndex);
    }

    boolean containsClip(int index) {
        return clips.containsKey(index);
    }

    Clip getClip(int index) {
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

    public Scene(SceneManager sceneManager, SceneInfo info, int index, int bankIndex, int sceneIndex) {
        this.sceneManager = sceneManager;
        this.info = info;
        this.linearIndex = index;
        this.bankIndex = bankIndex;
        this.sceneIndex = sceneIndex;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    void onMeasureStart(int measure, float beat, int sixteenth, int thirtysecond) {
        //        for (Clip clip : queueClips) {
        //            clip.play();
        //            playClips.add(clip);
        //        }
        //        queueClips.clear();
        //        for (Clip clip : clips.values()) {
        //            clip.onMeasureStart(measure, beat, sixteenth, thirtysecond);
        //        }
    }

    Clip addClip(int machineIndex, ClipInfo info) {
        Clip clip = new Clip(this, machineIndex, info);
        clips.put(machineIndex, clip);
        return clip;
    }

    Clip addClip(int machineIndex, String name) {
        ClipInfo info = new ClipInfo(name);
        return addClip(machineIndex, info);
    }

    Clip removeClip(Clip clip) {
        return removeClip(clip.getMachineIndex());
    }

    Clip removeClip(int trackIndex) {
        Clip removed = clips.remove(trackIndex);
        return removed;
    }

    Clip touch(int index) {
        Clip clip = clips.get(index);

        if (clip.isIdle()) { // Idle
            if (!clip.isPlaying()) {
                queue(clip);
            } else {
                dequeue(clip);
            }
            return clip;
        } else if (clip.isQueded()) { // Queued
            if (!clip.isPlaying()) {
                idle(clip);
                return clip;
            }
        } else if (clip.isDequeded()) { // Dequeued
            if (clip.isPlaying()) { // Dequeued but playing
                // XXX Test this
                cancelDequeue(clip);
            }
            return clip;
        } else if (clip.isPlaying()) { // Play
            dequeue(clip);
            return clip;
        } else if (!clip.isPlaying()) { // not playing
            idle(clip);
            return clip;
        }

        return null;
    }

    private void cancelDequeue(Clip clip) {
        clip.softPlay();
        dequeueClips.remove(clip);
        playClips.add(clip);
    }

    private void idle(Clip clip) {
        clip.idle();
        playClips.remove(clip);
        queueClips.remove(clip);
        dequeueClips.remove(clip);
    }

    private void dequeue(Clip clip) {
        clip.dequeue();
        dequeueClips.add(clip);
    }

    private void queue(Clip clip) {
        Clip conflict = sceneManager.isConflict(clip);
        if (conflict != null) {
            // need to dequeue from the conflict's scene
            conflict.getScene().dequeue(conflict);
            System.err.println("Removing conflict:" + conflict);
        }
        clip.queue();
        queueClips.add(clip);
    }

    void commitClips() {
        if (clips.size() == 0)
            return;
        // 1 bar measure change on next beat

        // All queued clips play
        ArrayList<Clip> tempQueue = new ArrayList<Clip>(queueClips);
        for (Clip clip : tempQueue) {
            play(clip);
        }

        // All dequeued clips stop
        ArrayList<Clip> tempDequeue = new ArrayList<Clip>(dequeueClips);
        for (Clip clip : tempDequeue) {
            stop(clip);
        }

        for (Clip clip : playClips) {
            clip.commitPlay();
        }

    }

    Clip isConflict(int machineIndex) {
        for (Clip clip : playClips) {
            if (clip.getMachineIndex() == machineIndex)
                return clip;
        }
        return null;
    }

    private void stop(Clip clip) {
        playClips.remove(clip);
        queueClips.remove(clip);
        dequeueClips.remove(clip);
        clip.commitStop();
    }

    private void play(Clip clip) {
        playClips.add(clip);
        queueClips.remove(clip);
        //clip.commitPlay();
    }

    void play() {
        for (Clip clip : clips.values()) {
            if (!playClips.contains(clip) && !queueClips.contains(clip)) {
                queue(clip);
            }
        }
    }

    void stop() {
        for (Clip clip : clips.values()) {
            if (playClips.contains(clip) || queueClips.contains(clip)) {
                dequeue(clip);
            }
        }
    }

    @Override
    public String toString() {
        return "Scene [index=" + linearIndex + ", bankIndex=" + bankIndex + ", sceneIndex="
                + sceneIndex + ", playing=" + isPlaying() + ", name=" + getName() + "]";
    }

}
