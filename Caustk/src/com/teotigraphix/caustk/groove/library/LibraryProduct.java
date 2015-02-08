////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.groove.library;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryProductManifest;
import com.teotigraphix.caustk.utils.groove.LibraryProductExportUtils;
import com.teotigraphix.caustk.utils.groove.LibraryProductUtils;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryProduct extends LibraryItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(25)
    private LibraryProductManifest manifest;

    @Tag(26)
    private Map<UUID, LibraryItemManifest> map = new HashMap<UUID, LibraryItemManifest>();

    private LibraryProductAccess access;

    public LibraryProductAccess getAccess() {
        if (access == null)
            access = new LibraryProductAccess(this);
        return access;
    }

    protected Map<UUID, LibraryItemManifest> getMap() {
        return map;
    }

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // manifest
    //----------------------------------

    @Override
    public LibraryProductManifest getManifest() {
        return manifest;
    }

    //----------------------------------
    // directory
    //----------------------------------

    /**
     * Returns the absolute directory of the library product.
     */
    public final File getDirectory() {
        return manifest.getDirectory();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    LibraryProduct() {
    }

    public LibraryProduct(LibraryProductManifest manifest) {
        this.manifest = manifest;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns whether this product contains {@link LibraryProductItem}s.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns the total amount of product items.
     */
    public int size() {
        return map.size();
    }

    /**
     * Returns whether this library product exists on disk.
     */
    public final boolean exists() {
        return manifest.exists();
    }

    /**
     * Creates the library product's directory.
     * 
     * @throws java.io.IOException Directory exists
     */
    public final void create() throws IOException {
        if (exists())
            throw new IOException("Directory exists; " + getDirectory());

        FileUtils.forceMkdir(getDirectory());
    }

    /**
     * Destroys the library product's directory.
     * 
     * @throws java.io.IOException Directory does not exist
     */
    public final void destroy() throws IOException {
        if (!exists())
            throw new IOException("Directory does not exist; " + getDirectory());

        FileUtils.forceDelete(getDirectory());
        map.clear();
    }

    /**
     * Adds a {@link LibraryProductItem} to the library product.
     * 
     * @param item The product item, Project, Group, Sound, Instrument, Effect.
     * @throws com.teotigraphix.caustk.core.CausticException Product contains
     *             item.
     */
    public void addItem(LibraryProductItem item) throws CausticException {
        if (map.containsKey(item.getId()))
            throw new CausticException("Product contains item: " + item.getId());

        map.put(item.getId(), item.getManifest());
    }

    /**
     * Removes the item from the product and returns the
     * {@link LibraryItemManifest}, null if the item is not contained in the
     * product.
     * 
     * @param item The product item to remove.
     */
    public LibraryItemManifest removeItem(LibraryProductItem item) {
        return removeItem(item.getId());
    }

    /**
     * Removes the item by {@link java.util.UUID} from the product and returns
     * the {@link LibraryItemManifest}, null if the item is not contained in the
     * product.
     * 
     * @param id The item's {@link java.util.UUID}.
     */
    public LibraryItemManifest removeItem(UUID id) {
        if (!map.containsKey(id))
            return null;
        return map.remove(id);
    }

    /**
     * Saves the product item to disk within the product's directory.
     * 
     * @param item The product item.
     * @return
     * @throws java.io.IOException
     */
    public File saveItem(LibraryProductItem item) throws IOException {
        return LibraryProductUtils.addArchiveToProduct(item, this);
    }

    /**
     * Saves the manifest to disk in the root of the product directory.
     * 
     * @throws IOException
     */
    public void save() throws IOException {
        CaustkRuntime.getInstance().getRack().getSerializer()
                .serialize(LibraryProductUtils.toProductBinary(getDirectory()), this);
    }

    /**
     * Resolves the product item's internal product path.
     * <p>
     * Calculates the path based on the {@link #getDirectory()} and the item's
     * relative path, {@link LibraryItemManifest#getRelativePath()}.
     * 
     * @param item The product item.
     */
    public final File resolveInternalArchive(LibraryProductItem item) {
        File archive = new File(getDirectory(), item.getProductPath().getPath());
        return archive;
    }

    /**
     * Resolves the product manifest's internal product path.
     * 
     * @param item The product item's manifest.
     */
    public File resolveInternalArchive(LibraryItemManifest manifest) {
        File archive = new File(getDirectory(), LibraryProductUtils.getProductPath(manifest)
                .getPath());
        return archive;
    }

    /**
     * Export this product to the .gprod targetArchive
     * 
     * @param targetArchive The absolute location of the <code>.gprod</code>
     *            archive with extension.
     * @throws java.io.IOException
     */
    public void export(File targetArchive) throws IOException {
        LibraryProductExportUtils.exportProduct(this, targetArchive);
    }

}
