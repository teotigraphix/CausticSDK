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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryProductManifest;
import com.teotigraphix.caustk.groove.utils.LibraryGroupUtils;
import com.teotigraphix.caustk.groove.utils.LibraryPatternBankUtils;
import com.teotigraphix.caustk.groove.utils.LibraryProductExportUtils;
import com.teotigraphix.caustk.groove.utils.LibraryProductUtils;

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

    //----------------------------------
    // descriptors
    //----------------------------------

    /**
     * Returns a list of {@link LibraryItemManifest} instances that match the
     * {@link LibraryItemFormat}.
     * <p>
     * The list order is not relevant.
     * 
     * @param format The format to match manifest instances against.
     * @return A list of manifests matching the passed format.
     */
    public List<LibraryItemManifest> getDescriptors(LibraryItemFormat format) {
        ArrayList<LibraryItemManifest> result = new ArrayList<LibraryItemManifest>();
        for (LibraryItemManifest manifest : map.values()) {
            if (manifest.getFormat() == format)
                result.add(manifest);
        }
        return result;
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

    //----------------------------------
    // Load Methods
    //----------------------------------

    public LibraryGroup loadGroup(LibraryItemManifest manifest) throws CausticException,
            IOException {
        File groupArchive = new File(getDirectory(), manifest.getProductPath());
        File uncompressDirectory = null;
        LibraryGroup instance = LibraryGroupUtils.importGroup(groupArchive, uncompressDirectory);
        return instance;
    }

    public LibraryProductItem createFromManifest(LibraryItemManifest libraryItemManifest)
            throws CausticException, IOException {
        File archive = libraryItemManifest.getAbsoluteProductPath(this);
        if (!archive.exists())
            throw new IOException("Archive doe snot exist: " + archive);
        File uncompressDirectory = CaustkRuntime.getInstance().getFactory()
                .getCacheDirectory("imports/" + UUID.randomUUID());
        LibraryPatternBank instance = LibraryPatternBankUtils.importPatternBank(
                uncompressDirectory, archive);
        return instance;
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
     * Export product to the .gprod targetArchive
     * 
     * @param targetArchive The absolute location of the <code>.gprod</code>
     *            archive.
     * @throws java.io.IOException
     */
    public void exportProduct(File targetArchive) throws IOException {
        LibraryProductExportUtils.exportProduct(this, targetArchive);
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

    public LibraryGroup createGroup(String path, String displayName, File causticFile) {
        LibraryGroup libraryGroup = CaustkRuntime.getInstance().getFactory().getLibraryFactory()
                .createGroup(this, displayName, path);
        return libraryGroup;
    }

}
