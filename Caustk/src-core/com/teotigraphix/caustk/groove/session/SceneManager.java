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
import java.util.List;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class SceneManager {

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private SessionManager sessionManager;

    @Tag(10)
    private List<Scene> scenes = new ArrayList<Scene>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    SessionManager getSessionManager() {
        return sessionManager;
    }

    List<Scene> getScenes() {
        return scenes;
    }

    Scene getScene(int index) {
        return scenes.get(index);
    }

    Scene getScene(int bankIndex, int patternIndex) {
        return scenes.get((bankIndex * 16) + patternIndex);
    }

    MachineNode getMachine(int machineIndex) {
        return sessionManager.getMachine(machineIndex);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    SceneManager() {
    }

    public SceneManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public Clip addClip(int sceneIndex, int trackIndex, String name) { // x, y
        Scene scene = getScene(sceneIndex);
        Clip clip = null;
        if (!scene.containsClip(trackIndex)) {
            clip = scene.addClip(trackIndex, name);
        }
        return clip;
    }

    public Clip removeClip(Clip clip) {
        return removeClip(clip.getSceneIndex(), clip.getMachineIndex());
    }

    public Clip removeClip(int sceneIndex, int trackIndex) { // x, y
        Scene scene = getScene(sceneIndex);
        Clip clip = scene.removeClip(trackIndex);
        return clip;
    }

    public void play(int sceneIndex) {
        play(getScene(sceneIndex));
    }

    public void play(Scene scene) {
        for (Scene otherScene : scenes) {
            if (scene != otherScene) {
                otherScene.stop();
            }
        }
        scene.play();
    }

    /**
     * @param index Linear index.
     */
    public void stop(int index) {
        stop(getScene(index));
    }

    public void stop(Scene scene) {
        scene.stop();
    }

    void commitClips() {
        for (Scene scene : scenes) {
            scene.commitClips();
        }
    }

    Clip isConflict(Clip queuedClip) {
        Clip conflict = null;
        int machineIndex = queuedClip.getMachineIndex();
        for (Scene scene : scenes) {
            // only work on scenes other than the queued clip
            if (scene != queuedClip.getScene()) {
                conflict = scene.isConflict(machineIndex);
                if (conflict != null)
                    return conflict;
            }

        }
        return conflict;
    }

    //--------------------------------------------------------------------------
    // Internal :: Methods
    //--------------------------------------------------------------------------

    void machineAdded(MachineNode machineNode) {

    }

    void machineRemoved(MachineNode machineNode) {

    }

    //--------------------------------------------------------------------------
    // Startup :: Methods
    //--------------------------------------------------------------------------

    void initialize() {

    }

    void create() {
        // create 4 banks of 16
        int index = 0;
        for (int bankIndex = 0; bankIndex < 4; bankIndex++) {
            for (int sceneIndex = 0; sceneIndex < 16; sceneIndex++) {
                SceneInfo info = new SceneInfo("Scene " + index);
                Scene scene = new Scene(this, info, index, bankIndex, sceneIndex);
                scenes.add(scene);
                index++;
            }
        }
    }

    void load() {

    }

}
