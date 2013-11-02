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
 * The {@link Library} is a collection container for all library components that
 * can be serialized.
 * <p>
 * When components are added to a library, they are either created using the
 * {@link CaustkFactory} or copied from and existing component. Any component
 * existing in the library has no reference to the {@link IRack} that may have
 * referenced it. This is to make sure the rack instance is not serialized into
 * the library.
 * <p>
 * <strong>Note;</strong> A component can have a reference to it's parent
 * composite depending on where in the chain a component is added. For example,
 * if a {@link Machine} is added to the library, its reference to the
 * {@link RackSet} will be disconnected before serialization. If a
 * {@link Effect} is added to the library, it's reference to the {@link Patch}
 * will be disconnected.
 * <p>
 * A library holds;
 * <ul>
 * <li>{@link LiveSet}</li>
 * <li>{@link RackSet}</li>
 * <li>{@link Machine}</li>
 * <li>{@link Patch}</li>
 * <li>{@link Effect}</li>
 * <li>{@link Phrase}</li>
 * <li>{@link MasterMixer}</li>
 * <li>{@link MasterSequencer}</li>
 * </ul>
 * 
 * @author Michael Schmalle
 */
public class Library implements ICaustkComponent {

    private static final String LIBRARIES = "Libraries";

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private Collection<LiveSet> liveSets = new ArrayList<LiveSet>();

    @Tag(2)
    private Collection<RackSet> rackSets = new ArrayList<RackSet>();

    @Tag(20)
    private Collection<Machine> machines = new ArrayList<Machine>();

    @Tag(21)
    private Collection<Patch> patches = new ArrayList<Patch>();

    @Tag(22)
    private Collection<Effect> effects = new ArrayList<Effect>();

    @Tag(23)
    private Collection<Phrase> phrases = new ArrayList<Phrase>();

    @Tag(24)
    private Collection<MasterMixer> mixers = new ArrayList<MasterMixer>();

    @Tag(25)
    private Collection<MasterSequencer> sequencers = new ArrayList<MasterSequencer>();

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
        if (info.getType() == ComponentType.LiveSet) {
            // RackSet uses root
            // RackSet rackSet = (RackSet)component;
        } else if (info.getType() == ComponentType.RackSet) {
            // RackSet uses root
            // RackSet rackSet = (RackSet)component;
        } else if (info.getType() == ComponentType.Patch) {
            // Patch uses MachineType
            Patch patch = (Patch)component;
            sb.append(patch.getMachineType().name());
            sb.append(File.separator);
        } else if (info.getType() == ComponentType.Phrase) {
            // Phrase uses MachineType
            Phrase phrase = (Phrase)component;
            sb.append(phrase.getMachineType().name());
            sb.append(File.separator);
        } else if (info.getType() == ComponentType.Effect) {
            // Effect uses EffectType
            Effect effect = (Effect)component;
            sb.append(effect.getEffectType().name());
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
    Library() {
    }

    Library(ComponentInfo info) {
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

    public boolean add(LiveSet liveSet) throws IOException {
        if (liveSets.contains(liveSet))
            return false;
        liveSets.add(liveSet);
        save(liveSet);
        return true;
    }

    public boolean add(RackSet rackSet) throws IOException {
        if (rackSets.contains(rackSet))
            return false;
        rackSets.add(rackSet);
        save(rackSet);
        return true;
    }

    public boolean add(Machine machine) throws IOException {
        if (machines.contains(machine))
            return false;
        machines.add(machine);
        save(machine);
        return true;
    }

    /**
     * Adds a {@link Effect} to the library.
     * <p>
     * Will not add the instance if the library already contains the reference.
     * 
     * @param effect The {@link Effect} to add.
     * @throws IOException
     */
    public boolean add(Effect effect) throws IOException {
        if (effects.contains(effect))
            return false;
        effects.add(effect);
        save(effect);
        return true;
    }

    public boolean add(Patch patch) throws IOException {
        if (patches.contains(patch))
            return false;
        patches.add(patch);
        save(patch);
        return true;
    }

    public boolean add(Phrase phrase) throws IOException {
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
            case LiveSet:
                return liveSets;
            case RackSet:
                return rackSets;
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
        }
        return null;
    }

}
