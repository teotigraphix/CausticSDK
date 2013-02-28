////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.part;

import java.util.Collection;
import java.util.Iterator;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.part.LibraryManifest;
import com.teotigraphix.caustic.internal.part.LibraryManifest.LibraryManifestEntry;
import com.teotigraphix.caustic.song.IPreset;
import com.teotigraphix.common.IMemento;

/**
 * The ILibrary interface allows tone, patch, pattern and song libraries to
 * share their bas implementation.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPresetLibrary {

    //--------------------------------------------------------------------------
    // 
    //  Methods
    // 
    //--------------------------------------------------------------------------

    /**
     * Loads the item manifest and initializes the session items for use in
     * parent presets.
     * 
     * @throws CausticException
     */
    void addManifest(ILibraryManifest manifest) throws CausticException;

    /**
     * Whether the library contains the manifest by name.
     * 
     * @param name The name of the manifest.
     */
    boolean hasManifest(String name);

    Collection<LibraryManifestEntry> getEntries(String bankName);

    /**
     * Tries to find a preset by the presetId, if the preset is found, returns
     * an IPreset set at it's defaults, otherwise it returns null.
     * 
     * @param bankName The preset bank name to find the presetId from.
     * @param presetId The presets unique id.
     */
    IPreset findItemById(String bankName, String presetId);

    IPreset getItemById(String libraryName, String presetId);

    /**
     * Tries to find a preset by the index assigned at load.
     * 
     * @param systemBank
     * @param index
     * @return
     */
    IPreset findItemAt(String systemBank, int index);

    /**
     * Returns an {@link Iterator} that can iterate over the bank presets
     * located in the library.
     * 
     * @param bankName The bank name that has been loaded into the library.
     */
    Iterator<IPreset> iterator(String bankName);

    IMemento saveBankState(String bankName);

    void loadBankState(IMemento memento);

    LibraryManifest copyBank(LibraryManifest manifest, String name) throws CausticException;

    void addPresetLibraryListener(PresetLibraryListener listener);

    void removePresetLibraryListener(PresetLibraryListener listener);

    public interface PresetLibraryListener {
        void onPresetAdded(IPreset preset);

        void onPresetRemoved(IPreset preset);
    }
}
