////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.part;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.part.ILibraryManifest;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class LibraryManifest implements ILibraryManifest {

    private ArrayList<LibraryManifestEntry> mEntries;

    //----------------------------------
    //  name
    //----------------------------------

    private String mName;

    @Override
    public String getName() {
        return mName;
    }

    //----------------------------------
    //  data
    //----------------------------------

    private String mData;

    @Override
    public String getData() {
        return mData;
    }

    //----------------------------------
    //  manifest
    //----------------------------------

    private File mFile;

    @Override
    public File getFile() {
        return mFile;
    }

    /**
     * Creates a new library manifest.
     * 
     * @param name The name of the manifest.
     * @param file The XML manifest File describing the library contents.
     */
    public LibraryManifest(String name, File file) {
        mName = name;
        mFile = file;
        mEntries = new ArrayList<LibraryManifestEntry>();
        fillEntries();
    }

    private void fillEntries() {
        IMemento memento = null;
        try {
            memento = RuntimeUtils.loadMemento(mFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IMemento[] children = memento.getChildren("patch");
        for (IMemento child : children) {
            mEntries.add(new LibraryManifestEntry(child));
        }
    }

    @Override
    public Collection<LibraryManifestEntry> getEntries() {
        return Collections.unmodifiableCollection(mEntries);
    }

    public static class LibraryManifestEntry {

        private String mId;

        private String mName;

        private String mPresetBank;

        private MachineType mType;

        public final String getId() {
            return mId;
        }

        public final String getName() {
            return mName;
        }

        public final String getPresetBank() {
            return mPresetBank;
        }

        public final MachineType getType() {
            return mType;
        }

        public LibraryManifestEntry(IMemento memento) {
            super();
            // <patch id="A001" presetBank="SYSTEM" type="subsynth" name="8Bit"/>
            mId = memento.getString("id");
            mName = memento.getString("name");
            mPresetBank = memento.getString("presetBank");
            mType = MachineType.fromString(memento.getString("type"));
        }

        @Override
        public String toString() {
            return "{" + mId + ":" + mName + "}";
        }
    }
}
