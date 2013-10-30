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

package com.teotigraphix.caustk.controller.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ICaustkFactory;
import com.teotigraphix.caustk.controller.command.CommandManager;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.machine.CastkMasterMixer;
import com.teotigraphix.caustk.machine.CaustkEffect;
import com.teotigraphix.caustk.machine.CaustkEffectFactory;
import com.teotigraphix.caustk.machine.CaustkInfoFactory;
import com.teotigraphix.caustk.machine.CaustkLibrary;
import com.teotigraphix.caustk.machine.CaustkLibraryFactory;
import com.teotigraphix.caustk.machine.CaustkMachine;
import com.teotigraphix.caustk.machine.CaustkMachineFactory;
import com.teotigraphix.caustk.machine.CaustkMasterMixerFactory;
import com.teotigraphix.caustk.machine.CaustkMasterSequencer;
import com.teotigraphix.caustk.machine.CaustkMasterSequencerFactory;
import com.teotigraphix.caustk.machine.CaustkPatch;
import com.teotigraphix.caustk.machine.CaustkPatchFactory;
import com.teotigraphix.caustk.machine.CaustkPhrase;
import com.teotigraphix.caustk.machine.CaustkPhraseFactory;
import com.teotigraphix.caustk.machine.CaustkScene;
import com.teotigraphix.caustk.machine.CaustkSceneFactory;
import com.teotigraphix.caustk.machine.ComponentInfo;
import com.teotigraphix.caustk.machine.ComponentType;
import com.teotigraphix.caustk.machine.ICaustkComponent;
import com.teotigraphix.caustk.machine.MachinePreset;
import com.teotigraphix.caustk.machine.MachineType;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.ProjectManager;
import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.Rack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.service.serialize.SerializeService;
import com.teotigraphix.caustk.utils.KryoUtils;

/**
 * One {@link CaustkFactory} per {@link ICaustkApplication}.
 * <p>
 * The factory holds no state, only creates instances. The factory does hold the
 * single {@link ICaustkApplication} and {@link IRack} instance. These are
 * singleton top level application components that are needed during
 * construction.
 * 
 * @author Michael Schmalle
 */
public class CaustkFactory implements ICaustkFactory {

    //----------------------------------
    // Factories
    //----------------------------------

    private CaustkInfoFactory infoFactory;

    private CaustkLibraryFactory libraryFactory;

    private CaustkSceneFactory sceneFactory;

    private CaustkMachineFactory machineFactory;

    private CaustkPatchFactory patchFactory;

    private CaustkEffectFactory effectFactory;

    private CaustkPhraseFactory phraseFactory;

    private CaustkMasterMixerFactory masterMixerFactory;

    private CaustkMasterSequencerFactory masterSequencerFactory;

    //----------------------------------
    // application
    //----------------------------------

    private ICaustkApplication application;

    public ICaustkApplication getApplication() {
        return application;
    }

    //----------------------------------
    // rack
    //----------------------------------

    public IRack getRack() {
        return application.getRack();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CaustkFactory(ICaustkApplication application) {
        this.application = application;

        infoFactory = new CaustkInfoFactory();
        infoFactory.setFactory(this);
        libraryFactory = new CaustkLibraryFactory();
        libraryFactory.setFactory(this);
        sceneFactory = new CaustkSceneFactory();
        sceneFactory.setFactory(this);
        machineFactory = new CaustkMachineFactory();
        machineFactory.setFactory(this);
        patchFactory = new CaustkPatchFactory();
        patchFactory.setFactory(this);
        effectFactory = new CaustkEffectFactory();
        effectFactory.setFactory(this);
        phraseFactory = new CaustkPhraseFactory();
        phraseFactory.setFactory(this);
        masterMixerFactory = new CaustkMasterMixerFactory();
        masterMixerFactory.setFactory(this);
        masterSequencerFactory = new CaustkMasterSequencerFactory();
        masterSequencerFactory.setFactory(this);
    }

    //--------------------------------------------------------------------------
    // Public Application Component Creation API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public ICaustkController createController() {
        return new CaustkController(application);
    }

    @Override
    public ISerializeService createSerializeService() {
        return new SerializeService();
    }

    @Override
    public ICommandManager createCommandManager() {
        return new CommandManager();
    }

    @Override
    public IProjectManager createProjectManager() {
        return new ProjectManager();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public IRack createRack() {
        Rack rack = new Rack();
        rack.setFactory(this);
        return rack;
    }

    /**
     * Creates an empty {@link CaustkLibrary} with a name.
     * <p>
     * The name is used for the directory name held within the
     * <code>/storageRoot/AppName/libraries</code> directory.
     * 
     * @param name The name of the library, used as the directory name.
     */
    @Override
    public CaustkLibrary createLibrary(String name) {
        return libraryFactory.createLibrary(name);
    }

    /**
     * Creates an empty {@link CaustkScene} with name.
     * 
     * @param name The name of the scene.
     */
    @Override
    public CaustkScene createScene(ComponentInfo info) {
        return sceneFactory.createScene(info);
    }

    /**
     * Creates a {@link CaustkScene} from a <code>.caustic</code> song file
     * import.
     * 
     * @param absoluteCausticFile The absolute location of the
     *            <code>.caustic</code> song file.
     */
    @Override
    public CaustkScene createScene(ComponentInfo info, File absoluteCausticFile) {
        return sceneFactory.createScene(info, absoluteCausticFile);
    }

    @Override
    public CaustkMachine createMachine(ComponentInfo info, MachineType machineType,
            String machineName) {
        return machineFactory.createMachine(info, machineType, machineName);
    }

    @Override
    public CaustkMachine createMachine(int index, MachineType machineType, String machineName) {
        return machineFactory.createMachine(index, machineType, machineName);
    }

    //----------------------------------
    // CaustkPatch
    //----------------------------------

    /**
     * Creates a {@link CaustkPatch} with {@link UUID} and {@link MachineType}.
     * 
     * @param toneType The {@link MachineType} of the
     */
    @Override
    public CaustkPatch createPatch(ComponentInfo info, MachineType machineType) {
        return patchFactory.createPatch(info, machineType);
    }

    /**
     * Creates a new {@link CaustkPatch}, assigns the {@link CaustkMachine}.
     * 
     * @param machine A {@link CaustkMachine} that does not exist in the native
     *            rack.
     */
    @Override
    public CaustkPatch createPatch(CaustkMachine machine) {
        return patchFactory.createPatch(machine);
    }

    /**
     * Activates the patch, creating the {@link MachinePreset} and
     * <p>
     * - Creates and assigns the bytes for the {@link MachinePreset}.
     * <p>
     * - Creates and assigns the {@link CaustkPatch} which will then create 0-2
     * {@link CaustkEffect}s. When the {@link CaustkEffect} is created, only the
     * {@link EffectType} is saved and slot index. The {@link IEffect} instance
     * is not restored at this point.
     * 
     * @param livePatch
     * @throws IOException
     */
    public void _activatePatch(CaustkPatch caustkPatch) throws IOException {
        //        patchFactory.activatePatch(caustkPatch);
    }

    @Override
    public CaustkPhrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex,
            int patternIndex) {
        return phraseFactory.createPhrase(info, machineType, bankIndex, patternIndex);
    }

    @Override
    public CaustkPhrase createPhrase(CaustkMachine caustkMachine, int bankIndex, int patternIndex) {
        return phraseFactory.createPhrase(caustkMachine, bankIndex, patternIndex);
    }

    //----------------------------------
    // Effect
    //----------------------------------

    /**
     * Creates an non attached {@link CaustkEffect} with the internal
     * {@link IEffect} created.
     * <p>
     * Non attached means no {@link CaustkPatch} or {@link CaustkMachine}
     * references.
     * 
     * @param info The {@link ComponentInfo}.
     * @param effectType The {@link EffectType}.
     */
    @Override
    public CaustkEffect createEffect(ComponentInfo info, EffectType effectType) {
        return effectFactory.createEffect(info, effectType);
    }

    @Override
    public CaustkEffect createEffect(int slot, EffectType effectType) {
        return effectFactory.createEffect(slot, effectType);
    }

    /**
     * @param slot
     * @param effectType
     * @param caustkPatch
     * @see CaustkPatch#load(CaustkLibraryFactory)
     */
    @Override
    public CaustkEffect createEffect(int slot, EffectType effectType, CaustkPatch caustkPatch) {
        return effectFactory.createEffect(slot, effectType, caustkPatch);
    }

    @Override
    public CastkMasterMixer createMasterMixer(CaustkScene caustkScene) {
        return masterMixerFactory.createMasterMixer(caustkScene);
    }

    @Override
    public CaustkMasterSequencer createMasterSequencer(CaustkScene caustkScene) {
        return masterSequencerFactory.createMasterSequencer(caustkScene);
    }

    //----------------------------------
    // ComponentInfo
    //----------------------------------

    /**
     * Creates a new {@link ComponentInfo} instance empty.
     * <p>
     * The id, type, creation and modified date are populated during
     * construction.
     * 
     * @param type The {@link ComponentType} being created.
     */
    @Override
    public ComponentInfo createInfo(ComponentType type) {
        return infoFactory.createInfo(type);
    }

    @Override
    public ComponentInfo createInfo(ComponentType type, String name) {
        return infoFactory.createInfo(type, name);
    }

    /**
     * Creates a new info instance constructing the {@link File} instance from
     * the relativePath and name using the {@link ComponentType#getExtension()}
     * of the type.
     * 
     * @param type The type of component.
     * @param relativePath The relative path of the component.
     * @param name The display name of the component, is used as the file name
     *            also.
     */
    @Override
    public ComponentInfo createInfo(ComponentType type, String relativePath, String name) {
        return infoFactory.createInfo(type, relativePath, name);
    }

    /**
     * Creates a new info instance constructing the {@link File} instance from
     * the relativePath and name using the {@link ComponentType#getExtension()}
     * of the type.
     * 
     * @param type The type of component.
     * @param relativePath The relative path File of the component.
     * @param name The display name of the component, is used as the file name
     *            also.
     */
    @Override
    public ComponentInfo createInfo(ComponentType type, File relativePath, String name) {
        return infoFactory.createInfo(type, relativePath, name);
    }

    /**
     * Creates an {@link ICaustkComponent} from the component File passed.
     * 
     * @param componentFile The serialized Caustk component.
     * @param clazz The class type of the component to deserialize.
     * @throws FileNotFoundException
     */
    // XXX Make generic returning T 
    @Override
    public ICaustkComponent create(File componentFile, Class<? extends ICaustkComponent> clazz)
            throws FileNotFoundException {
        ICaustkComponent component = KryoUtils.readFileObject(KryoUtils.getKryo(), componentFile,
                clazz);
        return component;
    }

}
