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
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.groove.importer.CausticGroup;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryProductManifest;
import com.teotigraphix.caustk.groove.utils.LibraryGroupUtils;
import com.teotigraphix.caustk.groove.utils.LibraryProductUtils;
import com.teotigraphix.caustk.utils.RuntimeUtils;

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
    private List<LibraryItemManifest> list = new ArrayList<LibraryItemManifest>();

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

    /**
     * Returns a sub directory of the <code>.temp</code> directory inside the
     * application.
     * <p>
     * Does NOT clean the relative directory.
     * 
     * @param reletivePath The path of the .temp sub directory.
     * @throws IOException the .temp or relative directory cannot be created.
     * @see RuntimeUtils#getApplicationTempDirectory()
     */
    public File getTempDirectory(String reletivePath) throws IOException {
        return getTempDirectory(reletivePath, false);
    }

    /**
     * Returns a sub directory of the <code>.temp</code> directory inside the
     * application.
     * 
     * @param reletivePath The path of the .temp sub directory.
     * @param clean Whether to clean the sub directory if exists.
     * @throws IOException the .temp or relative directory cannot be created.
     * @see RuntimeUtils#getApplicationTempDirectory()
     */
    public File getTempDirectory(String reletivePath, boolean clean) throws IOException {
        File tempDir = RuntimeUtils.getApplicationTempDirectory();
        if (!tempDir.exists())
            FileUtils.forceMkdir(tempDir);
        File directory = new File(tempDir, reletivePath);
        if (!directory.exists())
            FileUtils.forceMkdir(directory);
        if (clean)
            FileUtils.cleanDirectory(directory);
        return directory;
    }

    //----------------------------------
    // descriptors
    //----------------------------------

    /**
     * Returns a collection of {@link LibraryItemManifest} instances that match
     * the {@link LibraryItemFormat}.
     * 
     * @param format The format to match manifest instances against.
     * @return A collection of manifests matching the passed format.
     */
    public Collection<LibraryItemManifest> getDescriptors(LibraryItemFormat format) {
        ArrayList<LibraryItemManifest> result = new ArrayList<LibraryItemManifest>();
        for (LibraryItemManifest manifest : list) {
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
        super();
        this.manifest = manifest;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns whether this library product exists on disk.
     */
    public final boolean exists() {
        return manifest.exists();
    }

    public LibraryGroup loadGroup(LibraryItemManifest manifest) throws CausticException,
            IOException {
        File archive = new File(getDirectory(), manifest.getProductPath().getPath());
        //String json = ZipUtils.readZipString(archive, new File("manifest.json"));
        //LibraryGroup instance = SerializeUtils.unpack(json, LibraryGroup.class);
        // need to then load the sounds which will load the instrument and effects

        LibraryGroup instance = LibraryGroupUtils.importGroup(archive);

        return instance;
    }

    /**
     * Creates the library product's directory.
     * 
     * @throws IOException Directory exists
     */
    public final void create() throws IOException {
        if (exists())
            throw new IOException("Directory exists; " + getDirectory());

        FileUtils.forceMkdir(getDirectory());
    }

    /**
     * Destroys the library product's directory.
     * 
     * @throws IOException Directory does not exist
     */
    public final void destroy() throws IOException {
        if (!exists())
            throw new IOException("Directory does not exist; " + getDirectory());

        FileUtils.forceDelete(getDirectory());
    }

    /**
     * Adds a {@link LibraryProductItem} to the library product.
     * 
     * @param item The product item, Project, Group, Sound, IInstrument, Effect.
     * @throws CausticException
     */
    public void addItem(LibraryProductItem item) throws CausticException {
        if (list.contains(item))
            throw new CausticException("Product contains item.");

        LibraryItemManifest manifest = item.getManifest();
        list.add(manifest);
    }

    /**
     * Resolves the product item's internal product url.
     * 
     * @param item The product item.
     */
    public final File resolveInternalArchive(LibraryProductItem item) {
        File archive = new File(getDirectory(), item.getProductPath().getPath());
        return archive;
    }

    /**
     * Saves the product item to disk within the product's directory.
     * 
     * @param item The product item.
     * @return
     * @throws IOException
     */
    public File saveItem(LibraryProductItem item) throws IOException {
        return LibraryProductUtils.addArchiveToProduct(item, this);
    }

    /**
     * Fills the {@link LibraryGroup} with its elements parsed from a native
     * <code>.caustic</code> source file.
     * <p>
     * the library group must had a {@link CausticGroup} created and loaded
     * before this method is called.
     * 
     * @param libraryGroup The library group to fill and populate with
     *            {@link LibraryItem}s parsed from a .caustic file.
     * @throws CausticException
     */
    public void fillGroup(LibraryGroup libraryGroup) throws CausticException {
        LibraryGroupUtils.fillGroup(this, libraryGroup);
    }

}
