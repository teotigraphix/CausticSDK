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
import java.util.ArrayList;
import java.util.Collection;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.core.CaustkFactory;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.utils.KryoUtils;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/*
 * CaustkLibrary
 * 
 * - *CaustkScene     (collection) 
 * - *CaustkEffect    (collection)
 * - *CaustkMachine   (/toneType/collection)
 * - *CaustkPatch     (/toneType/collection)
 * - *CaustkPhrase    (/toneType/collection)
 */

/*
 *  /MyApp/Libraries/Trance1
 *    - Libraries used to copy into a project library
 *  Or..
 *  
 *  /MyApp/Projects/MyProject/Library
 *    - The project library that is serialized at save and unserialized at startup
 *    - This directory won't have the exploded directories that a global library has
 *      since its all in memory??????
 *  
 *    - Effect/
 *      - *LiveEffect.ctkeffect
 *    
 *    - Machine/  [toneType dependent]
 *      - *LiveMachine.ctkmachine
 *    
 *    - Mixer/
 *      - *CaustkMixer.ctkmixer
 *    
 *    - Patch/    [toneType dependent]
 *      - *CaustkPatch.ctkpatch
 *        
 *    - Phrase/   [toneType dependent (PCMSynth, Beatbox, Vocoder)]
 *      - *CaustkPhrase.ctkphrase
 *    
 *    - Scene/
 *      - *CaustkScene.ctkscene
 *    
 *    - Sequencer/
 *      - CaustkSequencer.ctksequencer
 */

/**
 * The {@link CaustkLibrary} is a collection container for all library
 * components that can be serialized.
 * <p>
 * When components are added to a library, they are either created using the
 * {@link CaustkFactory} or copied from and existing component. Any component
 * existing in the library has no reference to the {@link IRack} that may have
 * referenced it. This is to make sure the rack instance is not serialized into
 * the library.
 * <p>
 * <strong>Note;</strong> A component can have a reference to it's parent
 * composite depending on where in the chain a component is added. For example,
 * if a {@link CaustkMachine} is added to the library, its reference to the
 * {@link CaustkScene} will be disconnected before serialization. If a
 * {@link CaustkEffect} is added to the library, it's reference to the
 * {@link CaustkPatch} will be disconnected.
 * <p>
 * A library holds;
 * <ul>
 * <li>{@link CaustkScene}</li>
 * <li>{@link CaustkMachine}</li>
 * <li>{@link CaustkPatch}</li>
 * <li>{@link CaustkEffect}</li>
 * <li>{@link CaustkPhrase}</li>
 * <li>{@link CastkMasterMixer}</li>
 * <li>{@link CaustkMasterSequencer}</li>
 * </ul>
 * 
 * @author Michael Schmalle
 */
public class CaustkLibrary implements ICaustkComponent {

    private static final String LIBRARIES = "Libraries";

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private Collection<CaustkScene> scenes = new ArrayList<CaustkScene>();

    @Tag(2)
    private Collection<CaustkMachine> machines = new ArrayList<CaustkMachine>();

    @Tag(3)
    private Collection<CaustkPatch> patches = new ArrayList<CaustkPatch>();

    @Tag(4)
    private Collection<CaustkEffect> effects = new ArrayList<CaustkEffect>();

    @Tag(5)
    private Collection<CaustkPhrase> phrases = new ArrayList<CaustkPhrase>();

    @Tag(6)
    private Collection<CastkMasterMixer> mixers = new ArrayList<CastkMasterMixer>();

    @Tag(7)
    private Collection<CaustkMasterSequencer> sequencers = new ArrayList<CaustkMasterSequencer>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public ComponentInfo getInfo() {
        return info;
    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the name of the library and is also used as the directory name of
     * the library held within the <code>/storageRoot/AppName/libraries</code>
     * directory.
     */
    public final String getName() {
        return info.getName();
    }

    /**
     * Returns the <code>/storageRoot/AppName/Libraries</code> directory.
     */
    final File getLibrariesDirectory() {
        return RuntimeUtils.getApplicationDirectory(LIBRARIES);
    }

    /**
     * Returns the <code>/storageRoot/AppName/Libraries/[library_name]</code>
     * directory.
     */
    public final File getDirectory() {
        return new File(getLibrariesDirectory(), getName());
    }

    /**
     * Returns the absolute location of the component in the library using the
     * {@link #getDirectory()} and relative file path of the component.
     * 
     * @param component The component to resolve absolute location.
     */
    public final File resolveLocation(ICaustkComponent component) {
        ComponentInfo info = component.getInfo();
        String name = info.getType().name();

        final StringBuilder sb = new StringBuilder();

        // add the ComponentType sub directory
        sb.append(name);
        sb.append(File.separator);

        // add the specific sub directory after component type
        if (info.getType() == ComponentType.Scene) {
            // Scene uses root
            // CaustkScene caustkScene = (CaustkScene)component;
        } else if (info.getType() == ComponentType.Patch) {
            // Patch uses MachineType
            CaustkPatch caustkPatch = (CaustkPatch)component;
            sb.append(caustkPatch.getMachineType().name());
            sb.append(File.separator);
        } else if (info.getType() == ComponentType.Phrase) {
            // Phrase uses MachineType
            CaustkPhrase caustkPhrase = (CaustkPhrase)component;
            sb.append(caustkPhrase.getMachineType().name());
            sb.append(File.separator);
        } else if (info.getType() == ComponentType.Effect) {
            // Effect uses EffectType
            CaustkEffect caustkEffect = (CaustkEffect)component;
            sb.append(caustkEffect.getEffectType().name());
            sb.append(File.separator);
        }

        sb.append(info.getFile().getPath());

        return new File(getDirectory(), sb.toString());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkLibrary() {
    }

    CaustkLibrary(ComponentInfo info) {
        this.info = info;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns whether the library contains the component by reference in the
     * component collection.
     * 
     * @param component The library component.
     * @return
     */
    public boolean contains(ICaustkComponent component) {
        return getCollection(component).contains(component);
    }

    public boolean add(CaustkScene scene) throws IOException {
        if (scenes.contains(scene))
            return false;
        scenes.add(scene);
        save(scene);
        return true;
    }

    public boolean add(CaustkMachine machine) throws IOException {
        if (machines.contains(machine))
            return false;
        machines.add(machine);
        save(machine);
        return true;
    }

    /**
     * Adds a {@link CaustkEffect} to the library.
     * <p>
     * Will not add the instance if the library already contains the reference.
     * 
     * @param effect The {@link CaustkEffect} to add.
     * @throws IOException
     */
    public boolean add(CaustkEffect effect) throws IOException {
        if (effects.contains(effect))
            return false;
        effects.add(effect);
        save(effect);
        return true;
    }

    public boolean add(CaustkPatch patch) throws IOException {
        if (patches.contains(patch))
            return false;
        patches.add(patch);
        save(patch);
        return true;
    }

    public boolean add(CaustkPhrase phrase) throws IOException {
        if (phrases.contains(phrase))
            return false;
        phrases.add(phrase);
        save(phrase);
        return true;
    }

    public boolean remove(ICaustkComponent component) {
        return getCollection(component).remove(component);
    }

    /**
     * Saves the library component to it's specific File location on disk.
     * <p>
     * Uses {@link #resolveLocation(ICaustkComponent)} to calculate where the
     * component is saved. The component is serialized into it's binary format.
     * 
     * @param component The library component.
     * @return A File pointing to the serialized component if serialization was
     *         successful.
     * @throws FileNotFoundException
     */
    public File save(ICaustkComponent component) throws FileNotFoundException {
        // component must exist in Library
        if (!contains(component))
            throw new FileNotFoundException("Library does not contian component; " + component);

        File location = resolveLocation(component);
        KryoUtils.writeFileObject(KryoUtils.getKryo(), location, component);
        return location;
    }

    public void save() throws IOException {

    }

    private Collection<? extends ICaustkComponent> getCollection(ICaustkComponent component) {
        switch (component.getInfo().getType()) {
            case Effect:
                return effects;
            case Library:
                break;
            case Machine:
                return machines;
            case MasterMixer:
                break;
            case MasterSequencer:
                break;
            case Patch:
                return patches;
            case Phrase:
                return phrases;
            case Scene:
                return scenes;
        }
        return null;
    }

}
