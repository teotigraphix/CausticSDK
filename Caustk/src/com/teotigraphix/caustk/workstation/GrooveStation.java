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

package com.teotigraphix.caustk.workstation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.core.CausticException;

/**
 * The toplevel model when using {@link GrooveSet}, {@link GrooveBox},
 * {@link PatternBank} and {@link SongBank}.
 * <p>
 * This class is not serialized, it loads and unloads {@link GrooveSet}s which
 * intern hold {@link GrooveBox} configurations.
 * <p>
 * All {@link GrooveBox} creation and loading occurs through this class's API.
 * 
 * @author Michael Schmalle
 */
public class GrooveStation {

    private ICaustkFactory factory;

    private GrooveSet grooveSet;

    private Library library;

    private List<ICaustkComponent> saveList = new ArrayList<ICaustkComponent>();

    private ICaustkApplication application;

    public GrooveSet getGrooveSet() {
        return grooveSet;
    }

    public void setGrooveSet(GrooveSet value) {
        grooveSet = value;
        application.getController().getProjectManager().getSessionPreferences()
                .put("lastGrooveSetId", grooveSet.getInfo().getId().toString());
        factory.getRack().setRackSet(grooveSet.getRackSet());
    }

    public GrooveStation(ICaustkApplication application, String libraryPath) throws IOException,
            CausticException {
        this.application = application;
        this.factory = application.getFactory();

        createOrLoadLibrary(libraryPath);
    }

    public GrooveSet createOrLoadGrooveSet() throws CausticException, FileNotFoundException {
        String grooveSetId = application.getController().getProjectManager()
                .getSessionPreferences().getString("lastGrooveSetId");
        GrooveSet grooveSet = null;
        if (grooveSetId != null) {
            ComponentInfo info = library.get(UUID.fromString(grooveSetId));
            File location = library.resolveLocation(info);
            grooveSet = factory.load(location, GrooveSet.class);
        } else {
            grooveSet = createGrooveSet("Untitled");
        }
        return grooveSet;
    }

    protected void add(ICaustkComponent component) {
        saveList.add(component);
        try {
            library.add(component);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public <T extends ICaustkComponent> T load(File componentFile, Class<T> clazz)
            throws FileNotFoundException {
        T component = factory.load(componentFile, clazz);
        return component;
    }

    private void createOrLoadLibrary(String libraryPath) throws IOException, CausticException {
        library = factory.createLibrary(libraryPath);
        if (!library.exists()) {
            // library.save();
            library.getDirectory().mkdirs();
            factory.save(library, library.getLibrariesDirectory());
        } else {
            library = factory.load(library.getManifestFile(), Library.class);
            library.load(factory.createContext());
        }
    }

    public GrooveSet createGrooveSet(String name) throws CausticException {
        // Create the  RackSet
        RackSet rackSet = factory.createRackSet();
        //rackSet.setInternal();

        // Create empty GrooveSet that will hold GrooveMachines
        ComponentInfo info = factory.createInfo(ComponentType.GrooveSet, name);
        GrooveSet grooveSet = factory.createGrooveSet(info, rackSet);
        grooveSet.create(factory.createContext());
        add(grooveSet);
        return grooveSet;
    }

    public GrooveBox createGrooveBox(GrooveBoxType grooveBoxType) throws CausticException {
        if (grooveSet == null)
            throw new IllegalStateException("grooveSet is null");

        // Create the GrooveBox
        GrooveBox grooveBox = factory.createGrooveBox(grooveSet, grooveBoxType);
        grooveSet.addGrooveBox(grooveBox);

        // Create the PatternBank for the grooveBox
        PatternBank patternBank = factory.createPatternBank(grooveBox);
        grooveBox.setPatternBank(patternBank);
        patternBank.create(grooveSet.getRackSet().getFactory().createContext());

        return grooveBox;
    }

    public void save() throws FileNotFoundException {
        for (ICaustkComponent component : saveList) {
            library.refresh(component);
        }
        factory.save(library, library.getLibrariesDirectory());
    }
}
