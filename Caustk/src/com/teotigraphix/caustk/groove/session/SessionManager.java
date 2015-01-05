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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.TrackEntryNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private RackNode rackNode;

    @Tag(1)
    private SceneManager sceneManager;

    @Tag(2)
    private Map<Integer, MachineNode> machines = new HashMap<Integer, MachineNode>();

    @Tag(10)
    private int selectedSceneBankIndex;

    @Tag(11)
    private int selectedSceneMatrixIndex;

    private int measure;

    private int beat;

    private float floatBeat;

    private int sixteenth;

    private boolean locks = true;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public float getCurrentBeat() {
        return floatBeat;
    }

    public Scene getSelectedScene() {
        return sceneManager.getScene(selectedSceneBankIndex, selectedSceneMatrixIndex);
    }

    public int getSelectedSceneBankIndex() {
        return selectedSceneBankIndex;
    }

    public void setSelectedSceneBankIndex(int selectedIndex) {
        if (selectedIndex == this.selectedSceneBankIndex)
            return;
        this.selectedSceneBankIndex = selectedIndex;
        CaustkRuntime.getInstance().post(
                new SessionManagerEvent(SessionManagerEventKind.SelectedSceneChange, this,
                        getSelectedScene()));
    }

    public int getSelectedSceneMatrixIndex() {
        return selectedSceneMatrixIndex;
    }

    public void setSelectedSceneMatrixIndex(int selectedIndex) {
        if (selectedIndex == this.selectedSceneMatrixIndex)
            return;
        this.selectedSceneMatrixIndex = selectedIndex;
        CaustkRuntime.getInstance().post(
                new SessionManagerEvent(SessionManagerEventKind.SelectedSceneChange, this,
                        getSelectedScene()));
    }

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

    public boolean isLocks() {
        return locks;
    }

    public void setLocks(boolean locks) {
        this.locks = locks;
    }

    public boolean isRecording() {
        return rackNode.getSequencer().isRecording();
    }

    public void reset() {
        measure = 0;
        beat = 0;
        floatBeat = 0;
        getSceneManager().reset();
    }

    public void playPosition(int measure) {
        getSceneManager().reset();
        rackNode.getSequencer().playPosition(measure * 4);
        onBeatChange(measure, (measure * 4), measure == 0 ? 0 : 12, -1);
        //commitClips();
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

    /**
     * @param machineNode
     * @see com.teotigraphix.caustk.groove.session.SessionManager.SessionManagerEventKind#Connect
     */
    public void connect(MachineNode machineNode) {
        CaustkRuntime.getInstance().getLogger().log("SessionManager", "connect() " + machineNode);
        machines.put(machineNode.getIndex(), machineNode);
        // XXX Fix with Color id map        machineNode.setColor(StylesDefault.getMachineColor(machineNode.getType()));
        machineAdded(machineNode);
        CaustkRuntime.getInstance().post(
                new SessionManagerEvent(SessionManagerEventKind.Connect, this, machineNode));
    }

    /**
     * @param machineNode
     * @see com.teotigraphix.caustk.groove.session.SessionManager.SessionManagerEventKind#Disconnect
     */
    public MachineNode disconnect(MachineNode machineNode) {
        MachineNode removedMachine = machines.remove(machineNode.getIndex());
        machineRemoved(removedMachine);
        CaustkRuntime.getInstance().post(
                new SessionManagerEvent(SessionManagerEventKind.Disconnect, this, machineNode));
        return removedMachine;
    }

    public Clip touch(Clip clip) {
        if (isLockMeasure()) {
            CaustkRuntime.getInstance().getLogger().log("SessionManager", "touch(LOCKED) " + clip);
            return null;
        }
        CaustkRuntime.getInstance().getLogger().log("SessionManager", "touch() " + clip);

        Clip touch = getSceneManager().getScene(clip.getScene().getMatrixIndex()).touch(
                clip.getIndex());
        return touch;
    }

    boolean isStartMeasure() {
        return measure == 0 && sixteenth == 0;
    }

    boolean isLockMeasure() {
        if (!locks)
            return false;
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
        this.floatBeat = beat;
        this.beat = (int)Math.floor(beat);
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
     * Returns the current scene bank's matrix of scenes.
     * <p>
     * Will not return scenes that do not contain active clips.
     */
    public List<Scene> getViewScenes() {
        ArrayList<Scene> result = new ArrayList<Scene>();
        for (Scene scene : sceneManager.getScenes()) {
            if (scene.getBankIndex() == selectedSceneBankIndex && scene.hasClips())
                result.add(scene);
        }
        return result;
    }

    /**
     * Stops all clips playing in a scene at the next commit cycle.
     * 
     * @param scene the scene to stop.
     * @see com.teotigraphix.caustk.groove.session.SessionManager.SessionManagerSceneTransportEvent
     */
    public void stopScene(Scene scene) {
        sceneManager.stop(scene);
        CaustkRuntime.getInstance().post(new SessionManagerSceneTransportEvent(this, scene, false));
    }

    /**
     * Queues then plays all clips of the scene at the next commit cycle.
     * 
     * @param scene the scene to play.
     * @see com.teotigraphix.caustk.groove.session.SessionManager.SessionManagerSceneTransportEvent
     */
    public void playScene(Scene scene) {
        sceneManager.play(scene);
        CaustkRuntime.getInstance().post(new SessionManagerSceneTransportEvent(this, scene, true));
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

    /**
     * @param sceneIndex
     * @param trackIndex
     * @param name
     * @see com.teotigraphix.caustk.groove.session.SessionManager.SessionManagerEventKind#ClipAdd
     */
    public Clip addClip(int sceneIndex, int trackIndex, String name) {
        CaustkRuntime.getInstance().getLogger().log("SessionManager", "addClip() " + name);
        Clip clip = sceneManager.addClip(sceneIndex, trackIndex, name);
        if (clip != null) {
            CaustkRuntime.getInstance().post(
                    new SessionManagerEvent(SessionManagerEventKind.ClipAdd, this, clip));
        }
        return clip;
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

    //--------------------------------------------------------------------------
    // Used in TrackSequencer :: Methods
    //--------------------------------------------------------------------------

    public int getMeasureCount() {
        return 200;
    }

    public List<MachineNode> getChannels() {
        return new ArrayList<MachineNode>(machines.values());
    }

    public MachineNode getChannel(int machineIndex) {
        return machines.get(machineIndex);
    }

    public TrackEntryNode getEntry(int channelIndex, int startMeasure) {
        return getChannel(channelIndex).getTrack().getEntry(startMeasure);
    }

    public enum SessionManagerEventKind {
        Connect,

        Disconnect,

        SelectedSceneBankIndex,

        SelectedSceneMatrixIndex,

        SelectedSceneChange,

        ClipAdd,

        ClipRemove;
    }

    public static class SessionManagerSceneTransportEvent {
        private SessionManager manager;

        private Scene scene;

        private boolean playing;

        public SessionManager getManager() {
            return manager;
        }

        public Scene getScene() {
            return scene;
        }

        public boolean isPlaying() {
            return playing;
        }

        public SessionManagerSceneTransportEvent(SessionManager manager, Scene scene,
                boolean playing) {
            this.manager = manager;
            this.scene = scene;
            this.playing = playing;
        }
    }

    public static class SessionManagerEvent {

        private SessionManagerEventKind kind;

        private SessionManager manager;

        private Clip clip;

        private MachineNode machineNode;

        private Scene scene;

        public SessionManagerEventKind getKind() {
            return kind;
        }

        public SessionManager getManager() {
            return manager;
        }

        public Clip getClip() {
            return clip;
        }

        public MachineNode getMachineNode() {
            return machineNode;
        }

        public Scene getScene() {
            return scene;
        }

        public SessionManagerEvent(SessionManagerEventKind kind, SessionManager manager) {
            this.kind = kind;
            this.manager = manager;
        }

        public SessionManagerEvent(SessionManagerEventKind kind, SessionManager manager, Scene scene) {
            this.kind = kind;
            this.manager = manager;
            this.scene = scene;
        }

        public SessionManagerEvent(SessionManagerEventKind kind, SessionManager manager, Clip clip) {
            this.kind = kind;
            this.manager = manager;
            this.clip = clip;
        }

        public SessionManagerEvent(SessionManagerEventKind kind, SessionManager manager,
                MachineNode machineNode) {
            this.kind = kind;
            this.manager = manager;
            this.machineNode = machineNode;
        }
    }

}
