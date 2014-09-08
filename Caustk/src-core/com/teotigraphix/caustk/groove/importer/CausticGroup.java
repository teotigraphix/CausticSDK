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

package com.teotigraphix.caustk.groove.importer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkFactory;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryProduct;

/**
 * Imported from a .caustic file.
 */
public class CausticGroup extends CausticItem {

    private String name;

    private File sourceFile;

    private String displayName;

    Map<Integer, CausticSound> sounds = new HashMap<Integer, CausticSound>();

    public String getName() {
        return name;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<Integer, CausticSound> getSounds() {
        return sounds;
    }

    public CausticGroup(File sourceFile, String displayName) {
        this.sourceFile = sourceFile;
        this.displayName = displayName;

    }

    public CausticSound addSound(int index, String soundName, String effectName) {
        CausticSound machine = new CausticSound(index, soundName, effectName);
        sounds.put(index, machine);
        return machine;
    }

    public LibraryGroup create(LibraryProduct product) {
        LibraryGroup libraryGroup = getFactory().createLibraryGroup(product, getDisplayName(),
                getPath());
        libraryGroup.setCausticGroup(this);
        return libraryGroup;
    }

    private ICaustkFactory getFactory() {
        return CaustkRuntime.getInstance().getFactory();
    }

}
