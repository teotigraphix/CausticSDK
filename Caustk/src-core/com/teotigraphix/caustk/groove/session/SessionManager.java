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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class SessionManager {

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private SceneManager sceneManager;

    @Tag(1)
    private Map<Integer, MachineNode> machines = new HashMap<Integer, MachineNode>();

    private int measure;

    private double beat;

    private int sixteenth;

    private RackNode rackNode;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public RackNode getRackNode() {
        return rackNode;
    }

    SceneManager getSceneManager() {
        return sceneManager;
    }

    Map<Integer, MachineNode> getMachines() {
        return machines;
    }

    MachineNode getMachine(int machineIndex) {
        return machines.get(machineIndex);
    }

    public int getCurrentMeasure() {
        return measure;
    }

    public int getNextMeasure() {
        return measure + 1;
    }

    public double getBeat() {
        return beat;
    }

    public int getSixteenth() {
        return sixteenth;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    SessionManager() {
    }

    public SessionManager(RackNode rackNode) {
        this.rackNode = rackNode;
        sceneManager = new SceneManager(this);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void connect(MachineNode machineNode) {
        CaustkRuntime.getInstance().getLogger().log("SessionManager", "connect() " + machineNode);
        machines.put(machineNode.getIndex(), machineNode);
        machineAdded(machineNode);
    }

    public MachineNode disconnect(MachineNode machineNode) {
        MachineNode removedMachine = machines.remove(machineNode.getIndex());
        machineRemoved(removedMachine);
        return removedMachine;
    }

    public Clip touch(Clip clip) {
        if (isLockMeasure()) {
            CaustkRuntime.getInstance().getLogger().log("SessionManager", "touch(LOCKED) " + clip);
            return null;
        }
        CaustkRuntime.getInstance().getLogger().log("SessionManager", "touch() " + clip);
        return getSceneManager().getScene(clip.getScene().getMatrixIndex()).touch(clip.getIndex());
    }

    boolean isStartMeasure() {
        return measure == 0 && sixteenth == 0;
    }

    boolean isLockMeasure() {
        return sixteenth >= 12;
    }

    void onSixteenthChange(int measure, int sixteenth) {
        this.measure = measure;
        this.sixteenth = sixteenth;

        System.out.println("m:" + measure + ", s:" + sixteenth); // 0, 4, 8, 12, 0, ...
        if (measure == 0 && sixteenth == 0) {
            this.measure = -1;
            start();
            this.measure = 0;
        } else if (sixteenth == 12) {
            // 1 bar measure change on next beat
            commitClips();
        } else if (sixteenth == 15) {
            refreshClips();
        }
    }

    private void commitClips() {
        System.out.println("SessionManager.commitClips()");
        getSceneManager().commitClips();
    }

    private void refreshClips() {
        System.out.println("SessionManager.refreshClips()");
        getSceneManager().refreshClips();
    }

    public void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond) {
        this.measure = measure;
        this.beat = Math.floor(beat);
        this.sixteenth = sixteenth;

        onSixteenthChange(measure, sixteenth);
    }

    private void start() {
        commitClips();
    }

    @SuppressWarnings("unused")
    private void onMeasureStart(int measure, float beat, int sixteenth, int thirtysecond) {
        for (Scene scene : sceneManager.getScenes()) {
            scene.onMeasureStart(measure, beat, sixteenth, thirtysecond);
        }
    }

    //--------------------------------------------------------------------------
    // Internal :: Methods
    //--------------------------------------------------------------------------

    private void machineAdded(MachineNode machineNode) {
        sceneManager.machineAdded(machineNode);
    }

    private void machineRemoved(MachineNode machineNode) {
        sceneManager.machineRemoved(machineNode);
    }

    //--------------------------------------------------------------------------
    // Startup :: Methods
    //--------------------------------------------------------------------------

    public void initialize() {
        // called each time BEFORE a project is created or loaded
        sceneManager.initialize();
    }

    public void create() {
        // create patterns, this only gets called when a project is created for the first time
        sceneManager.create();
    }

    public void load() {
        // only gets called when a project is loaded AFTER initialize
        sceneManager.load();
    }

    //--------------------------------------------------------------------------
    // Used in App :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns a {@link Scene} based on the linear index of the scene.
     * 
     * @param index The linear index.
     */
    public Scene getScene(int index) {
        return sceneManager.getScene(index);
    }

    /**
     * Stops all clips playing in a scene at the next commit cycle.
     * 
     * @param scene the scene to stop.
     */
    public void stopScene(Scene scene) {
        sceneManager.stop(scene);
    }

    /**
     * Queues then plays all clips of the scene at the next commit cycle.
     * 
     * @param scene the scene to play.
     */
    public void playScene(Scene scene) {
        sceneManager.play(scene);
    }

    /**
     * Returns a clip at the scene and track index, <code>null</code> if a clip
     * does not exist.
     * 
     * @param sceneIndex The x column grid.
     * @param trackIndex the y column grid.
     */
    public Clip getClip(int sceneIndex, int trackIndex) {
        Clip clip = sceneManager.getScene(sceneIndex).getClip(trackIndex);
        return clip;
    }

    public Clip addClip(int sceneIndex, int trackIndex, String name) {
        CaustkRuntime.getInstance().getLogger().log("SessionManager", "addClip() " + name);
        return sceneManager.addClip(sceneIndex, trackIndex, name);
    }

    public Collection<Clip> getClips(int sceneIndex) {
        return getScene(sceneIndex).getClips();
    }

    public void play(int sceneIndex) {
        sceneManager.play(sceneIndex);
    }

    public void stop(int sceneIndex) {
        sceneManager.stop(sceneIndex);
    }

}
