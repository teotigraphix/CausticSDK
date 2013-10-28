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

package com.teotigraphix.caustk.machine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.utils.KryoUtils;

public class CaustkLibraryFactory {

    private CaustkSceneFactory sceneFactory;

    private CaustkMachineFactory machineFactory;

    private CaustkPatchFactory patchFactory;

    private CaustkEffectFactory effectFactory;

    private CaustkPhraseFactory phraseFactory;

    private CaustkMasterMixerFactory masterMixerFactory;

    @SuppressWarnings("unused")
    private CaustkMasterSequencerFactory masterSequencerFactory;

    private IRack rack;

    public IRack getRack() {
        return rack;
    }

    public void setRack(IRack value) {
        rack = value;

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
        masterSequencerFactory = new CaustkMasterSequencerFactory();
    }

    public CaustkLibraryFactory(IRack rack) {
        setRack(rack);
    }

    /**
     * Creates an empty {@link CaustkLibrary} with a name.
     * <p>
     * The name is used for the directory name held within the
     * <code>/storageRoot/AppName/libraries</code> directory.
     * 
     * @param name The name of the library, used as the directory name.
     */
    public CaustkLibrary createLibrary(String name) {
        CaustkLibrary caustkLibrary = new CaustkLibrary(UUID.randomUUID(), name);
        return caustkLibrary;
    }

    /**
     * Creates an empty {@link CaustkScene} with name.
     * 
     * @param name The name of the scene.
     */
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
    public CaustkScene createScene(ComponentInfo info, File absoluteCausticFile) {
        return sceneFactory.createScene(info, absoluteCausticFile);
    }

    public CaustkMachine createMachine(ComponentInfo info, MachineType machineType,
            String machineName) {
        return machineFactory.createMachine(info, machineType, machineName);
    }

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
    public CaustkPatch createPatch(ComponentInfo info, MachineType machineType) {
        return patchFactory.createPatch(info, machineType);
    }

    /**
     * Creates a new {@link CaustkPatch}, assigns the {@link CaustkMachine}.
     * 
     * @param machine A {@link CaustkMachine} that does not exist in the native
     *            rack.
     */
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

    public CaustkPhrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex,
            int patternIndex) {
        return phraseFactory.createPhrase(info, machineType, bankIndex, patternIndex);
    }

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
    public CaustkEffect createEffect(ComponentInfo info, EffectType effectType) {
        return effectFactory.createEffect(info, effectType);
    }

    public CaustkEffect createEffect(int slot, EffectType effectType) {
        return effectFactory.createEffect(slot, effectType);
    }

    /**
     * @param slot
     * @param effectType
     * @param caustkPatch
     * @see CaustkPatch#load(CaustkLibraryFactory)
     */
    public CaustkEffect createEffect(int slot, EffectType effectType, CaustkPatch caustkPatch) {
        return effectFactory.createEffect(slot, effectType, caustkPatch);
    }

    public CastkMasterMixer createMasterMixer(CaustkScene caustkScene) {
        return masterMixerFactory.createMasterMixer(caustkScene);
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
    public ComponentInfo createInfo(ComponentType type) {
        final ComponentInfo result = new ComponentInfo(UUID.randomUUID(), type);
        result.setCreated(new Date());
        result.setModified(new Date());
        return result;
    }

    public ComponentInfo createInfo(ComponentType type, String name) {
        return createInfo(type, new File("."), name);
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
    public ComponentInfo createInfo(ComponentType type, String relativePath, String name) {
        return createInfo(type, new File(relativePath), name);
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
    public ComponentInfo createInfo(ComponentType type, File relativePath, String name) {
        File file = new File(relativePath, name + "." + type.getExtension());
        // if we have no directory, just construct from name
        if (relativePath.getName().equals("."))
            file = new File(name + "." + type.getExtension());
        ComponentInfo result = new ComponentInfo(UUID.randomUUID(), type, file, name);
        result.setCreated(new Date());
        result.setModified(new Date());
        return result;
    }

    /**
     * Creates an {@link ICaustkComponent} from the component File passed.
     * 
     * @param componentFile The serialized Caustk component.
     * @param clazz The class type of the component to deserialize.
     * @throws FileNotFoundException
     */
    // XXX Make generic returning T
    public ICaustkComponent create(File componentFile, Class<? extends ICaustkComponent> clazz)
            throws FileNotFoundException {
        ICaustkComponent component = KryoUtils.readFileObject(KryoUtils.getKryo(), componentFile,
                clazz);
        return component;
    }

}
