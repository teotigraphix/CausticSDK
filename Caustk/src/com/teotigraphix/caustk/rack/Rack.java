////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.rack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ICaustkFactory;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.CaustkFactory;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.machine.CaustkMachine;
import com.teotigraphix.caustk.machine.CaustkScene;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * The {@link Rack} is a fully serializable state instance.
 */
public class Rack implements IRack {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private transient ISoundGenerator soundGenerator;

    private transient ICaustkController controller;

    private transient IDispatcher dispatcher;

    // - The Rack is the dispatcher for all events coming from a Scene, 
    // so listeners in apps can safely listen to the Rack without getting coupled 
    // with a Scene reference, the Rack is ONLY CREATED ONCE

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public IDispatcher getGlobalDispatcher() {
        return controller;
    }

    //----------------------------------
    // factory
    //----------------------------------

    private CaustkFactory factory;

    @Override
    public ICaustkFactory getFactory() {
        return factory;
    }

    public void setFactory(ICaustkFactory value) {
        factory = (CaustkFactory)value;
        setController(factory.getApplication().getController());
    }

    //----------------------------------
    // controller
    //----------------------------------

    ICaustkController getController() {
        return controller;
    }

    private void setController(ICaustkController controller) {
        this.controller = controller;
        this.dispatcher = new Dispatcher();

        controller.addComponent(IRack.class, this);

        soundGenerator = controller.getApplication().getConfiguration().getSoundGenerator();

        systemSequencer = new SystemSequencer();
        systemSequencer.setRack(this);
        controller.addComponent(ISystemSequencer.class, systemSequencer);
    }

    @Override
    public void clearAndReset() throws CausticException {
        ArrayList<CaustkMachine> list = new ArrayList<CaustkMachine>(scene.getMachines());
        for (CaustkMachine machine : list) {
            scene.removeMachine(machine);
        }
        RackMessage.BLANKRACK.send(this);
    }

    @Override
    public boolean isEmpty() {
        return scene.getMachineCount() == 0;
    }

    @Override
    public void loadSongRaw(File causticFile) throws CausticException {
        RackMessage.LOAD_SONG.send(this, causticFile.getAbsolutePath());
    }

    @Override
    public void loadSong(File causticFile) throws CausticException {
        loadSongRaw(causticFile);
        //        loadMachines();
    }

    @Override
    public File saveSong(String name) {
        RackMessage.SAVE_SONG.send(this, name);
        return RuntimeUtils.getCausticSongFile(name);
    }

    @Override
    public File saveSongAs(File file) throws IOException {
        File song = saveSong(file.getName().replace(".caustic", ""));
        FileUtils.copyFileToDirectory(song, file.getParentFile(), true);
        song.delete();
        return file;
    }

    //----------------------------------
    // scene
    //----------------------------------

    private CaustkScene scene;

    @Override
    public final CaustkScene getScene() {
        return scene;
    }

    @Override
    public void setScene(CaustkScene value) {
        if (value == scene)
            return;

        CaustkScene oldScene = scene;
        scene = value;
        sceneChanged(scene, oldScene);
    }

    private void sceneChanged(CaustkScene newScene, CaustkScene oldScene) {
        if (oldScene != null) {
            removeScene(oldScene);
        }
        // when a scene is set, the Rack does not care or want to care
        // how the scene was load, unserialized etc., it will just call update()
        // and restore whatever is there.

        // since the is a restoration of deserialized components, all sub
        // components are guaranteed to be created, setRack() recurses and sets
        // all components rack
        newScene.setRack(this);

        // recursively updates all scene components based on their previous 
        // saved state
        newScene.update();
    }

    private void removeScene(CaustkScene scene) {
        scene.setRack(null);
    }

    //----------------------------------
    // systemSequencer
    //----------------------------------

    private SystemSequencer systemSequencer;

    @Override
    public final ISystemSequencer getSystemSequencer() {
        return systemSequencer;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public Rack() {
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void frameChanged(float delta) {
        final int measure = (int)getCurrentSongMeasure();
        final float beat = getCurrentBeat();
        final boolean changed = systemSequencer.updatePosition(measure, beat);
        if (changed) {
            systemSequencer.beatChange(measure, beat);
            //for (IRackComponent component : components.values()) {
            //    component.beatChange(measure, beat);
            //}
        }
    }

    //--------------------------------------------------------------------------
    // SoundGenerator
    //--------------------------------------------------------------------------

    @Override
    public final float getCurrentSongMeasure() {
        return soundGenerator.getCurrentSongMeasure();
    }

    @Override
    public final float getCurrentBeat() {
        return soundGenerator.getCurrentBeat();
    }

    //----------------------------------
    // IActivityCycle API
    //----------------------------------

    @Override
    public void onStart() {
        soundGenerator.onStart();
        soundGenerator.onResume();
    }

    @Override
    public void onResume() {
        soundGenerator.onResume();
    }

    @Override
    public void onPause() {
        soundGenerator.onPause();
    }

    @Override
    public void onStop() {
        soundGenerator.onStop();
    }

    @Override
    public void onDestroy() {
        soundGenerator.onDestroy();
    }

    @Override
    public void onRestart() {
        soundGenerator.onRestart();
    }

    @Override
    public void dispose() {
        RackMessage.BLANKRACK.send(this);

        controller.removeComponent(IRack.class);
        controller.removeComponent(ISystemSequencer.class);

        controller = null;
        soundGenerator = null;
        systemSequencer = null;
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    // we proxy the actual OSC impl so we can stop, or reroute
    @Override
    public final float sendMessage(String message) {
        return soundGenerator.sendMessage(message);
    }

    @Override
    public final String queryMessage(String message) {
        return soundGenerator.queryMessage(message);
    }
}
