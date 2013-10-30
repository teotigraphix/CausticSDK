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
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICausticLogger;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.command.ICommand;
import com.teotigraphix.caustk.controller.core.CaustkFactory;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.caustk.machine.CaustkScene;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;

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

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public IDispatcher getGlobalDispatcher() {
        return controller;
    }

    private Map<Class<? extends IRackComponent>, IRackComponent> components;

    private SoundSource soundSource;

    private SystemSequencer systemSequencer;

    //----------------------------------
    // factory
    //----------------------------------

    private transient CaustkFactory factory;

    public CaustkFactory getFactory() {
        return factory;
    }

    public void setFactory(CaustkFactory value) {
        factory = value;
        setController(factory.getApplication().getController());
    }

    //----------------------------------
    // soundSource
    //----------------------------------

    public ICaustkController __getController() {
        return controller;
    }

    private void setController(ICaustkController controller) {
        this.controller = controller;
        this.dispatcher = new Dispatcher();

        controller.addComponent(IRack.class, this);

        soundGenerator = controller.getApplication().getConfiguration().getSoundGenerator();

        if (components == null) {
            components = new HashMap<Class<? extends IRackComponent>, IRackComponent>();

            soundSource = new SoundSource(this);
            systemSequencer = new SystemSequencer(this);
        }
    }

    //----------------------------------
    // soundSource
    //----------------------------------

    @Override
    public final ISoundSource getSoundSource() {
        return soundSource;
    }

    @Override
    public void clearAndReset() throws CausticException {
        ArrayList<Tone> remove = new ArrayList<Tone>(soundSource.getTones());
        for (Tone tone : remove) {
            destroyTone(tone);
        }
        RackMessage.BLANKRACK.send(this);
    }

    @Override
    public boolean isEmpty() {
        return soundSource.getToneCount() == 0;
    }

    @Override
    public <T extends Tone> T createTone(String name, Class<? extends Tone> toneClass)
            throws CausticException {
        T tone = soundSource.createTone(name, toneClass);
        toneAdd(tone);
        return tone;
    }

    @Override
    public <T extends Tone> T createTone(int index, String name, Class<? extends Tone> toneClass)
            throws CausticException {
        T tone = soundSource.createTone(index, name, toneClass);
        toneAdd(tone);
        return tone;
    }

    @Override
    public Tone createTone(ToneDescriptor descriptor) throws CausticException {
        Tone tone = soundSource.createTone(descriptor);
        toneAdd(tone);
        return tone;
    }

    private void toneAdd(Tone tone) {
    }

    private void toneRemove(Tone tone) {
    }

    @Override
    public void destroyTone(int index) {
        destroyTone(soundSource.getTone(index));
    }

    @Override
    public void destroyTone(Tone tone) {
        soundSource.destroyTone(tone);
        toneRemove(tone);
    }

    @Override
    public void loadSongRaw(File causticFile) throws CausticException {
        soundSource.loadSongRaw(causticFile);
    }

    @Override
    public void loadSong(File causticFile) throws CausticException {
        soundSource.loadSong(causticFile);
    }

    @Override
    public File saveSong(String name) {
        return soundSource.saveSong(name);
    }

    @Override
    public File saveSongAs(File file) throws IOException {
        return soundSource.saveSongAs(file);
    }

    @Override
    public void createScene(LibraryScene libraryScene) throws CausticException {
        soundSource.createScene(libraryScene);
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
        if (scene != null) {
            unload(scene);
        }
        scene = value;
        load(scene);
    }

    private void unload(CaustkScene scene) {
        scene.setRack(null);
    }

    private void load(CaustkScene scene) {
        // since the is a restoration of deserialized components, all sub
        // components a guaranteed to be created, setRack() recurses and sets
        // all components rack
        scene.setRack(this);
        // recursively updates all scene components based on their previous 
        // saved state
        scene.update();
    }

    //----------------------------------
    // systemSequencer
    //----------------------------------

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
    public void addComponent(Class<? extends IRackComponent> classType, IRackComponent component) {
        components.put(classType, component);
    }

    @Override
    public <T extends IRackComponent> T getComponent(Class<T> clazz) {
        return clazz.cast(components.get(clazz));
    }

    @Override
    public void registerObservers() {

    }

    @Override
    public void unregisterObservers() {

    }

    @Override
    public void update() {
        final int measure = (int)getCurrentSongMeasure();
        final float beat = getCurrentBeat();
        final boolean changed = systemSequencer.updatePosition(measure, beat);
        if (changed) {
            systemSequencer.beatChange(measure, beat);
            for (IRackComponent component : components.values()) {
                component.beatChange(measure, beat);
            }
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
        components.clear();

        controller = null;
        soundGenerator = null;
        soundSource = null;
        systemSequencer = null;
    }

    //--------------------------------------------------------------------------
    // IDispatcher API
    //--------------------------------------------------------------------------

    @Override
    public void put(String message, Class<? extends ICommand> command) {
        controller.put(message, command);
    }

    @Override
    public void remove(String message) {
        controller.remove(message);
    }

    @Override
    public void execute(String message, Object... args) throws CausticException {
        controller.execute(message, args);
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

    @Override
    public ICausticLogger getLogger() {
        return controller.getLogger();
    }

    @Override
    public Project getProject() {
        return controller.getProjectManager().getProject();
    }

    @Override
    public Library getLibrary() {
        return controller.getLibraryManager().getSelectedLibrary();
    }

    private transient boolean initalized = false;

    public void setInitialized(boolean value) {
        if (initalized)
            throw new IllegalStateException("Rack already initialized");

        initalized = true;
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

}
