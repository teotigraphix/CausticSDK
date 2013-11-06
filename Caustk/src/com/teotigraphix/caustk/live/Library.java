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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

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

    /**
     * used with {@link ICaustkFactory#loadLibrary(String)}.
     * 
     * @param factory
     */
    void setFactory(ICaustkFactory factory) {
        this.factory = factory;
    }

    private transient Map<ComponentType, List<? extends ICaustkComponent>> components = new HashMap<ComponentType, List<? extends ICaustkComponent>>();

    private boolean invalidated = false;

    public boolean isInvalidated() {
        return invalidated;
    }

    public void validate() throws IOException {
        if (invalidated) {
            save();
        }
        invalidated = false;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private Map<ComponentType, List<ComponentInfo>> map = new HashMap<ComponentType, List<ComponentInfo>>();

    Map<ComponentType, List<ComponentInfo>> getMap() {
        return map;
    }

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

    /**
     * Adds a component to the library.
     * <p>
     * Will save the associated component serialized to disk.
     * <p>
     * The component passed will be deep copied and references to parents
     * unlinked using the {@link ICaustkComponent#disconnect()} method.
     * 
     * @param component The component to add and serialize to the library on
     *            disk.
     * @throws IOException
     */
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

    /**
     * Remove a component from the library by {@link UUID}.
     * <p>
     * The {@link Library} holds copies(templates) of components and at no time
     * is a component that is found in the app, contained in the library. When a
     * component is added to the library it's copied into it.
     * <p>
     * Will delete the associated serialized file on disk corresponding to the
     * component.
     * 
     * @param component The component whose {@link ComponentInfo#getId()} is
     *            used to remove the "Same" component ref from the library.
     * @return Whether the operation was successful
     * @throws IOException
     */
    public boolean remove(ICaustkComponent component) throws IOException {
        // have to remove by UUID
        ICaustkComponent found = findById(component);
        if (found == null)
            return false;

        if (!getCollection(component).remove(found))
            throw new IllegalStateException("Failed to remove component");

        componentRemoved(found);
        return true;
    }

    private ICaustkComponent findById(ICaustkComponent component) {
        List<? extends ICaustkComponent> collection = getCollection(component);
        if (collection == null)
            return null;
        for (ICaustkComponent search : collection) {
            if (search.getInfo().getId().equals(component.getInfo().getId()))
                return search;
        }
        return null;
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
    File writeToDisk(ICaustkComponent component) throws FileNotFoundException {
        // component must exist in Library
        if (!contains(component))
            throw new FileNotFoundException("Library does not contian component; " + component);

        File location = factory.save(component, getDirectory());
        return location;
    }

    File deleteFromDisk(ICaustkComponent component) throws FileNotFoundException {
        File file = resolveLocation(component);
        FileUtils.deleteQuietly(file);
        if (file.exists())
            throw new IllegalStateException("File not deleted:" + file);
        return file;
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

    private List<? extends ICaustkComponent> getCollection(ICaustkComponent component) {
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
    public void disconnect() {
    }

    private void componentAdded(ICaustkComponent component) throws FileNotFoundException {
        invalidated = true;
        ComponentInfo info = component.getInfo();
        String path = factory.resolvePath(component);
        info.setPath(path);

        ComponentType type = info.getType();
        List<ComponentInfo> list = map.get(type);
        if (list == null) {
            list = new ArrayList<ComponentInfo>();
            map.put(type, list);
        }
        list.add(info);
        component.disconnect();
        writeToDisk(component);
    }

    private void componentRemoved(ICaustkComponent component) throws FileNotFoundException {
        invalidated = true;
        deleteFromDisk(component);
    }

    /**
     * Saves and closes the Library, once the method is called, the Library
     * cannot be reused.
     * <p>
     * The instance still maintains its manifest entries.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        save();
        factory = null;
        components.clear();
    }

    public File resolveLocation(ComponentInfo info) {
        String path = info.getPath();
        if (path == null)
            throw new IllegalStateException("path must not be null");
        return new File(getDirectory(), path);
    }

    public List<ComponentInfo> findAll(ComponentType type) {
        List<ComponentInfo> result = new ArrayList<ComponentInfo>();
        List<ComponentInfo> list = map.get(type);
        for (ComponentInfo info : list) {
            if (!filter(info))
                result.add(info);
        }
        return result;
    }

    public <T extends ICaustkComponent> T newInstance(ComponentInfo info, Class<T> clazz)
            throws IOException {
        File location = resolveLocation(info);
        T component = factory.load(location, clazz);
        return component;
    }

    private boolean filter(ComponentInfo info) {
        return false;
    }

}
