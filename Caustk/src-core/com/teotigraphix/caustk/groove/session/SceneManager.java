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
    // Private :: Variables
    //--------------------------------------------------------------------------

    private int numBanks = 4;

    private int numMatrixScenes = 16;

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

    SceneManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds a clip the the scene, returns the clip if successful, null if the
     * scene already contains a clip at the scene index.
     * 
     * @param sceneIndex The scene index.
     * @param trackIndex The track index.
     * @param name The name of the new clip.
     */
    Clip addClip(int sceneIndex, int trackIndex, String name) { // x, y
        Scene scene = getScene(sceneIndex);
        Clip clip = null;
        if (!scene.containsClip(trackIndex)) {
            clip = scene.addClip(trackIndex, name);
        }
        return clip;
    }

    /**
     * Removes a clip from the scene, returns the clip removed if successful,
     * null if the clip is not contained in the scene.
     * 
     * @param clip The clip to remove.
     */
    Clip removeClip(Clip clip) {
        return removeClip(clip.getScene().getMatrixIndex(), clip.getIndex());
    }

    /**
     * Removes a clip from the scene, returns the clip removed if successful,
     * null if the clip is not contained in the scene.
     * 
     * @param sceneIndex The scene index.
     * @param trackIndex The track index.
     */
    Clip removeClip(int sceneIndex, int trackIndex) { // x, y
        Scene scene = getScene(sceneIndex);
        Clip clip = scene.removeClip(trackIndex);
        return clip;
    }

    /**
     * Resets all clip states to idle.
     */
    void reset() {
        for (Scene scene : scenes) {
            scene.reset();
        }
    }

    /**
     * Plays a scene's clips.
     * 
     * @param index The scene index.
     */
    void play(int index) {
        play(getScene(index));
    }

    /**
     * Plays a scene's clips.
     * 
     * @param scene The scene to play.
     */
    void play(Scene scene) {
        for (Scene otherScene : scenes) {
            if (scene != otherScene) {
                otherScene.stop();
            }
        }
        scene.play();
    }

    /**
     * Stops a scene's clips.
     * 
     * @param index The scene index.
     */
    void stop(int index) {
        stop(getScene(index));
    }

    /**
     * Stops a scene's clips.
     * 
     * @param scene The scene to stop.
     */
    void stop(Scene scene) {
        scene.stop();
    }

    //--------------------------------------------------------------------------
    // Internal :: Methods
    //--------------------------------------------------------------------------

    void machineAdded(MachineNode machineNode) {

    }

    void machineRemoved(MachineNode machineNode) {

    }

    void commitClips() {
        for (Scene scene : scenes) {
            scene.commitClips();
        }
    }

    void refreshClips() {
        for (Scene scene : scenes) {
            scene.refreshClips();
        }
    }

    Clip isConflict(Clip queuedClip) {
        Clip conflict = null;
        int machineIndex = queuedClip.getIndex();
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
    // Startup :: Methods
    //--------------------------------------------------------------------------

    void initialize() {

    }

    void create() {
        // create 4 banks of 16
        int index = 0;
        for (int bankIndex = 0; bankIndex < numBanks; bankIndex++) {
            for (int sceneIndex = 0; sceneIndex < numMatrixScenes; sceneIndex++) {
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
