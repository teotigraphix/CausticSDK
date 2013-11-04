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

package com.teotigraphix.caustk.live;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.core.CaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.Rack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.effect.RackEffect;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
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

    private ComponentInfoFactory infoFactory;

    private LibraryFactory libraryFactory;

    private LiveSetFactory liveSetFactory;

    private RackSetFactory rackSetFactory;

    private MachineFactory machineFactory;

    private PatchFactory patchFactory;

    private EffectFactory effectFactory;

    private PhraseFactory phraseFactory;

    private MasterMixerFactory masterMixerFactory;

    private MasterSequencerFactory masterSequencerFactory;

    private RackToneFactory rackToneFactory;

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

    @Override
    public IRack getRack() {
        return application.getRack();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CaustkFactory(ICaustkApplication application) {
        this.application = application;
        createFactories();
    }

    private void createFactories() {
        infoFactory = new ComponentInfoFactory();
        infoFactory.setFactory(this);
        libraryFactory = new LibraryFactory();
        libraryFactory.setFactory(this);
        liveSetFactory = new LiveSetFactory();
        liveSetFactory.setFactory(this);
        rackSetFactory = new RackSetFactory();
        rackSetFactory.setFactory(this);
        machineFactory = new MachineFactory();
        machineFactory.setFactory(this);
        patchFactory = new PatchFactory();
        patchFactory.setFactory(this);
        effectFactory = new EffectFactory();
        effectFactory.setFactory(this);
        phraseFactory = new PhraseFactory();
        phraseFactory.setFactory(this);
        masterMixerFactory = new MasterMixerFactory();
        masterMixerFactory.setFactory(this);
        masterSequencerFactory = new MasterSequencerFactory();
        masterSequencerFactory.setFactory(this);
        rackToneFactory = new RackToneFactory();
        rackToneFactory.setFactory(this);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public ICaustkApplicationContext createContext() {
        return new CaustkApplicationContext(application);
    }

    @Override
    public IRack createRack() {
        Rack rack = new Rack(application);
        return rack;
    }

    /**
     * Creates an empty {@link Library} with a name.
     * <p>
     * The name is used for the directory name held within the
     * <code>/storageRoot/AppName/libraries</code> directory.
     * 
     * @param name The name of the library, used as the directory name.
     */
    @Override
    public Library createLibrary(String name) {
        return libraryFactory.createLibrary(name);
    }

    @Override
    public LiveSet createLiveSet(ComponentInfo info) {
        return liveSetFactory.createLiveSet(info);
    }

    /**
     * Creates an empty {@link RackSet} with name.
     * 
     * @param name The name of the scene.
     */
    @Override
    public RackSet createRackSet(ComponentInfo info) {
        return rackSetFactory.createRackSet(info);
    }

    /**
     * Creates a {@link RackSet} from a <code>.caustic</code> song file import.
     * 
     * @param absoluteCausticFile The absolute location of the
     *            <code>.caustic</code> song file.
     */
    @Override
    public RackSet createRackSet(ComponentInfo info, File absoluteCausticFile) {
        return rackSetFactory.createRackSet(info, absoluteCausticFile);
    }

    @Override
    public Machine createMachine(ComponentInfo info, int machineIndex, MachineType machineType,
            String machineName) {
        return machineFactory.createMachine(info, machineIndex, machineType, machineName);
    }

    @Override
    public Machine createMachine(RackSet rackSet, int machineIndex, MachineType machineType,
            String machineName) {
        return machineFactory.createMachine(rackSet, machineIndex, machineType, machineName);
    }

    //----------------------------------
    // CaustkPatch
    //----------------------------------

    /**
     * Creates a {@link Patch} with {@link UUID} and {@link MachineType}.
     * 
     * @param toneType The {@link MachineType} of the
     */
    @Override
    public Patch createPatch(ComponentInfo info, MachineType machineType) {
        return patchFactory.createPatch(info, machineType);
    }

    /**
     * Creates a new {@link Patch}, assigns the {@link Machine}.
     * 
     * @param machine A {@link Machine} that does not exist in the native rack.
     */
    @Override
    public Patch createPatch(Machine machine) {
        return patchFactory.createPatch(machine);
    }

    /**
     * Activates the patch, creating the {@link MachinePreset} and
     * <p>
     * - Creates and assigns the bytes for the {@link MachinePreset}.
     * <p>
     * - Creates and assigns the {@link Patch} which will then create 0-2
     * {@link Effect}s. When the {@link Effect} is created, only the
     * {@link EffectType} is saved and slot index. The {@link RackEffect}
     * instance is not restored at this point.
     * 
     * @param livePatch
     * @throws IOException
     */
    public void _activatePatch(Patch caustkPatch) throws IOException {
        //        patchFactory.activatePatch(caustkPatch);
    }

    @Override
    public Phrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex,
            int patternIndex) {
        return phraseFactory.createPhrase(info, machineType, bankIndex, patternIndex);
    }

    @Override
    public Phrase createPhrase(Machine caustkMachine, int bankIndex, int patternIndex) {
        return phraseFactory.createPhrase(caustkMachine, bankIndex, patternIndex);
    }

    //----------------------------------
    // Effect
    //----------------------------------

    /**
     * Creates an non attached {@link Effect} with the internal {@link IEffect}
     * created.
     * <p>
     * Non attached means no {@link Patch} or {@link Machine} references.
     * 
     * @param info The {@link ComponentInfo}.
     * @param effectType The {@link EffectType}.
     */
    @Override
    public Effect createEffect(ComponentInfo info, EffectType effectType) {
        return effectFactory.createEffect(info, effectType);
    }

    @Override
    public Effect createEffect(int slot, EffectType effectType) {
        return effectFactory.createEffect(slot, effectType);
    }

    /**
     * @param slot
     * @param effectType
     * @param caustkPatch
     * @see Patch#load(LibraryFactory)
     */
    @Override
    public Effect createEffect(int slot, EffectType effectType, Patch caustkPatch) {
        return effectFactory.createEffect(slot, effectType, caustkPatch);
    }

    @Override
    public MasterMixer createMasterMixer(RackSet caustkScene) {
        return masterMixerFactory.createMasterMixer(caustkScene);
    }

    @Override
    public MasterSequencer createMasterSequencer(RackSet caustkScene) {
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

    //----------------------------------
    // Tones
    //----------------------------------

    @Override
    public RackTone createRackTone(String machineName, MachineType machineType, int machineIndex)
            throws CausticException {
        RackTone tone = rackToneFactory.createRackTone(machineName, machineType, machineIndex);
        tone.create(createContext());
        return tone;
    }

    @Override
    public RackTone createRackTone(ToneDescriptor descriptor) throws CausticException {
        return rackToneFactory.createRackTone(descriptor);
    }

    @Override
    public RackTone createRackTone(Machine machine, ToneDescriptor descriptor)
            throws CausticException {
        return rackToneFactory.createRackTone(machine, descriptor);
    }

    public static <T> T createInstance(File file, Class<T> clazz) throws FileNotFoundException {
        T instance = KryoUtils.readFileObject(KryoUtils.getKryo(), file, clazz);
        return instance;
    }

    public static boolean save(File file, Object instance) throws FileNotFoundException {
        KryoUtils.writeFileObject(KryoUtils.getKryo(), file, instance);
        return file.exists();
    }
}