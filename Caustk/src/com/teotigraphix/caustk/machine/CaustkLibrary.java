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
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.IRack;
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
 * {@link CaustkLibraryFactory} or copied from and existing component. Any
 * component existing in the library has no reference to the {@link IRack} that
 * may have referenced it. This is to make sure the rack instance is not
 * serialized into the library.
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
public class CaustkLibrary {

    private static final String LIBRARIES = "libraries";

    private static final String EFFECTS = "libraries/effects";

    private static final String MACHINES = "libraries/machines";

    private static final String PATCHES = "libraries/patches";

    private static final String PHRASES = "libraries/phrases";

    private static final String SCENES = "libraries/scenes";

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(0)
    private String name;

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
    // id
    //----------------------------------

    /**
     * Returns the unique id for this {@link CaustkLibrary}.
     * <p>
     * <strong>Assigned only at construction.</strong>
     */
    public final UUID getId() {
        return id;
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
        return name;
    }

    /**
     * Returns the <code>/storageRoot/AppName/libraries</code> directory.
     */
    public final File getLibrariesDirectory() {
        return RuntimeUtils.getDirectory(LIBRARIES);
    }

    /**
     * Returns the <code>/storageRoot/AppName/libraries/[library_name]</code>
     * directory.
     */
    public final File getDirectory() {
        return new File(getLibrariesDirectory(), name);
    }

    /**
     * Returns the absolute location of the component in the library using the
     * {@link #getDirectory()} and relative file path of the component.
     * 
     * @param component The component to resolve absolute location.
     */
    public File getAbsoluteComponentLocation(ICaustkComponent component) {
        return new File(getDirectory(), component.getFile().getPath());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkLibrary() {
    }

    CaustkLibrary(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

}
