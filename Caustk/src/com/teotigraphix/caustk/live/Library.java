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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/*
 *  /MyApp/Libraries/Trance1
 *    - Libraries used to copy into a project library
 *  Or..
 *  
 *  /MyApp/Projects/MyProject/Library
 *    - The project library that is serialized at save and unserialized at startup
 *    - This directory won't have the exploded directories that a global library has
 *      since its all in memory??????

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

    private static final String MANIFEST = "manifest";

    private static final String LIBRARIES = "Libraries";

    private transient ICaustkFactory factory;

    private transient Map<ComponentType, ArrayList<? extends ICaustkComponent>> components = new HashMap<ComponentType, ArrayList<? extends ICaustkComponent>>();

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private Map<ComponentType, ComponentInfo> map = new HashMap<ComponentType, ComponentInfo>();

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

    @Override
    public String getDefaultName() {
        return info.getName();
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
     * Returns the
     * <code>/storageRoot/AppName/Libraries/[library_name]/manifest.clb</code>
     * file.
     */
    public final File getManifestFile() {
        return new File(getDirectory(), MANIFEST + "." + ComponentType.Library.getExtension());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Library() {
    }

    Library(ComponentInfo info, ICaustkFactory factory) {
        this.info = info;
        this.factory = factory;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns whether the library contains the component by reference in the
     * component collection.
     * 
     * @param component The library component.
     */
    public boolean contains(ICaustkComponent component) {
        Collection<? extends ICaustkComponent> collection = components.get(component.getInfo()
                .getType());
        if (collection == null)
            return false;
        return collection.contains(component);
    }

    public void add(ICaustkComponent component) throws IOException {
        ArrayList<ICaustkComponent> collection = getRawCollection(component);
        if (collection == null) {
            collection = new ArrayList<ICaustkComponent>();
            components.put(component.getInfo().getType(), collection);
        }
        ICaustkComponent copy = factory.copy(component);
        collection.add(copy);
        componentAdded(copy);
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

        File location = factory.save(component, getDirectory());
        return location;
    }

    public File resolveLocation(ICaustkComponent component) {
        return factory.resolveLocation(component, getDirectory());
    }

    /**
     * Save the library to disk using it's {@link #getDirectory()} location.
     * 
     * @throws IOException
     */
    public void save() throws IOException {
        if (!exists())
            getDirectory().mkdirs();
        factory.save(this, getLibrariesDirectory());
    }

    private ArrayList<? extends ICaustkComponent> getCollection(ICaustkComponent component) {
        return components.get(component.getInfo().getType());
    }

    @SuppressWarnings("unchecked")
    private ArrayList<ICaustkComponent> getRawCollection(ICaustkComponent component) {
        return (ArrayList<ICaustkComponent>)components.get(component.getInfo().getType());
    }

    public boolean exists() {
        return getDirectory().exists();
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onSave() {
    }

    @Override
    public void disconnect() {
    }

    private void componentAdded(ICaustkComponent component) throws FileNotFoundException {
        component.disconnect();
        save(component);
    }

    private void componentRemoved(ICaustkComponent component) {

    }
}
