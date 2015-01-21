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
import com.teotigraphix.caustk.node.machine.sequencer.TrackComponent;
import com.teotigraphix.caustk.node.machine.sequencer.TrackEntryNode;

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
    private int matrixIndex;

    @Tag(4)
    private SceneInfo info;

    @Tag(5)
    private Map<Integer, Clip> clips = new HashMap<Integer, Clip>(14);

    @Tag(10)
    private ArrayList<Clip> queueClips = new ArrayList<Clip>();

    @Tag(11)
    private ArrayList<Clip> dequeueClips = new ArrayList<Clip>();

    @Tag(12)
    private ArrayList<Clip> playClips = new ArrayList<Clip>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the scene's linear index (0..63).
     */
    public int getIndex() {
        return index;
    }

    //----------------------------------
    // bankIndex
    //----------------------------------

    /**
     * Returns the scene's bank index (0..3).
     */
    public int getBankIndex() {
        return bankIndex;
    }

    //----------------------------------
    // matrixIndex
    //----------------------------------

    /**
     * Returns the scene's index within it's bank matrix. (0..16).
     */
    public int getMatrixIndex() {
        return matrixIndex;
    }

    public String getName() {
        return info.getName();
    }

    public boolean isPlaying() {
        if (isEmpty())
            return false;

        for (Clip clip : clips.values()) {
            if (!clip.hasPattern())
                continue;
            if (!clip.isStatePlaying())
                return false;
        }
        return true;
    }

    private boolean isEmpty() {
        for (Clip clip : clips.values()) {
            if (clip.hasPattern())
                return false;
        }
        return true;
    }

    public boolean isQueded() {
        if (isEmpty() || isPlaying())
            return false;
        for (Clip clip : clips.values()) {
            if (!clip.hasPattern())
                continue;
            if (!clip.isStateQueded())
                return false;
        }
        return true;
    }

    public boolean isDequeded() {
        if (isEmpty())
            return false;
        for (Clip clip : clips.values()) {
            if (!clip.hasPattern())
                continue;
            if (!clip.isStateDequeded())
                return false;
        }
        return true;
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
        this.index = index;
        this.bankIndex = bankIndex;
        this.matrixIndex = sceneIndex;
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

    public boolean hasClips() {
        return clips.size() > 0;
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
        return removeClip(clip.getIndex());
    }

    Clip removeClip(int trackIndex) {
        Clip removed = clips.remove(trackIndex);
        return removed;
    }

    Clip touch(int index) {
        Clip clip = clips.get(index);

        if (clip.isStateIdle()) { // Idle
            if (!clip.isStatePlaying()) {
                queue(clip);
            } else {
                dequeue(clip);
            }
            return clip;
        } else if (clip.isStateQueded()) { // Queued
            if (!clip.isStatePlaying()) {
                idle(clip);
                return clip;
            }
        } else if (clip.isStateDequeded()) { // Dequeued
            if (clip.isStatePlaying()) { // Dequeued but playing
                // XXX Test this
                cancelDequeue(clip);
            }
            return clip;
        } else if (clip.isStatePlaying()) { // Play
            dequeue(clip);
            return clip;
        } else if (!clip.isStatePlaying()) { // not playing
            idle(clip);
            return clip;
        }

        return null;
    }

    private void cancelDequeue(Clip clip) {
        clip.softPlayState();
        dequeueClips.remove(clip);
        playClips.add(clip);
    }

    private void idle(Clip clip) {
        clip.idleState();
        playClips.remove(clip);
        queueClips.remove(clip);
        dequeueClips.remove(clip);
    }

    private void dequeue(Clip clip) {
        clip.dequeueState();
        playClips.remove(clip);
        queueClips.remove(clip);
        dequeueClips.add(clip);
    }

    private void queue(Clip clip) {
        Clip conflict = sceneManager.isConflict(clip);
        if (conflict != null) {
            // need to dequeue from the conflict's scene
            if (conflict.isStatePlaying()) {
                conflict.getScene().dequeue(conflict);
                System.err.println("Removing Play conflict to Dequeued:" + conflict);
            } else if (conflict.isStateQueded()) {
                conflict.getScene().idle(conflict);
                System.err.println("Removing Queued conflict to Idle:" + conflict);
            } else if (conflict.isStateDequeded()) {
                //conflict.getScene().idle(conflict);
                System.err.println("Removing Dequeded conflict to Idle:" + conflict);
            }
        }
        clip.queueState();
        queueClips.add(clip);
    }

    void commitClips() {
        if (clips.size() == 0)
            return;
        // 1 bar measure change on next beat
        //System.out.println("---------------------- commitClips() " + this);
        //System.out.println("");
        SessionManager sessionManager = sceneManager.getSessionManager();
        if (!sessionManager.isRecording()) {
            for (MachineNode machineNode : getSessionManager().getMachines().values()) {
                TrackComponent track = machineNode.getTrack();
                //if (track.isInArrangement()) {
                queueTrackPlayClip(track);
                //}
            }
        }

        // All dequeued clips stop
        ArrayList<Clip> tempDequeue = new ArrayList<Clip>(dequeueClips);
        for (Clip clip : tempDequeue) {
            stop(clip);
        }

        // All queued clips play
        ArrayList<Clip> tempQueue = new ArrayList<Clip>(queueClips);
        for (Clip clip : tempQueue) {
            play(clip);
        }

        for (Clip clip : playClips) {
            clip.commitPlay();
        }
    }

    private void queueTrackPlayClip(TrackComponent track) {

        SessionManager sessionManager = sceneManager.getSessionManager();

        int trackIndex = track.getMachineIndex();

        // does the track have a clip for the next measure
        int nextMeaure = sessionManager.getNextMeasure();

        TrackEntryNode entry = track.getEntryContaining(nextMeaure);
        Clip clip = getClip(trackIndex);

        if (clip != null && entry != null) {

            if (clip == entry.getClip()) {
                //System.out.println("queueTrackPlayClip() " + track.getMachineNode());
                //System.out.println(clip);
                if (clip.isStateIdle()) {
                    queue(clip);
                }
            }
        } else if (clip != null) {
            entry = track.getEntryContaining(nextMeaure - 1);
            if (entry != null) {
                dequeue(clip);
            }

        }
    }

    void refreshClips() {
        for (Clip clip : clips.values()) {
            clip.refresh();
        }
    }

    Clip isConflict(int machineIndex) {
        for (Clip clip : playClips) {
            if (clip.getIndex() == machineIndex)
                return clip;
        }
        for (Clip clip : queueClips) {
            if (clip.getIndex() == machineIndex)
                return clip;
        }
        for (Clip clip : dequeueClips) {
            if (clip.getIndex() == machineIndex)
                return clip;
        }
        return null;
    }

    private void reset(Clip clip) {
        playClips.remove(clip);
        queueClips.remove(clip);
        dequeueClips.remove(clip);
        clip.commitStop();
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
            if (clip.hasPattern()) {
                if (!clip.isPlaying()) {
                    queue(clip);
                }
            }
        }
    }

    void stop() {
        for (Clip clip : clips.values()) {
            if (clip.hasPattern()) {
                if (clip.isPlaying()) {
                    dequeue(clip);
                } else if (clip.isQueued()) {
                    stop(clip);
                } else if (clip.isDequeued()) {
                }
            }
        }
    }

    void reset() {
        for (Clip clip : clips.values()) {
            reset(clip);
        }
    }

    @Override
    public String toString() {
        return "Scene [index=" + index + ", bankIndex=" + bankIndex + ", sceneIndex=" + matrixIndex
                + ", playing=" + isPlaying() + ", name=" + getName() + "]";
    }

}
