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
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.rack.Rack.OnRackListener;

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

    private static final String LAST_GROOVE_SET_ID = "lastGrooveSetId";

    //--------------------------------------------------------------------------
    // Variables
    //--------------------------------------------------------------------------

    private ICaustkFactory factory;

    private GrooveSet grooveSet;

    private Library library;

    private ICaustkApplication application;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // grooveSet
    //----------------------------------

    GrooveSet getGrooveSet() {
        return grooveSet;
    }

    void setGrooveSet(GrooveSet value) {
        grooveSet = value;
        application.getController().getProjectManager().getSessionPreferences()
                .put(LAST_GROOVE_SET_ID, grooveSet.getInfo().getId().toString());
        factory.getRack().setRackSet(grooveSet.getRackSet());
    }

    public GrooveBox getGrooveBox(int index) {
        return grooveSet.getGrooveBox(index);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public GrooveStation(ICaustkApplication application) throws IOException, CausticException {
        this.application = application;
        this.factory = application.getFactory();

        factory.getRack().addListener(new OnRackListener() {
            @Override
            public void beatChange(int measure, float beat) {
                for (GrooveBox grooveBox : grooveSet.getMachines()) {
                    grooveBox.beatChange(measure, beat);
                }
            }

            @Override
            public void frameChange(float delta, int measure, float beat) {
                for (GrooveBox grooveBox : grooveSet.getMachines()) {
                    grooveBox.frameChange(delta, measure, beat);
                }
            }
        });

        createOrLoadLibrary();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Starts the groove station by either creating a new {@link GrooveSet} or
     * loading the last {@link GrooveSet} from the {@link Library}.
     * <p>
     * Needs to be called after the {@link Project} is created or loaded.
     * 
     * @throws CausticException
     * @throws FileNotFoundException
     */
    public void start() throws CausticException, FileNotFoundException {
        String grooveSetId = application.getController().getProjectManager()
                .getSessionPreferences().getString(LAST_GROOVE_SET_ID);

        GrooveSet grooveSet = null;
        if (grooveSetId != null) {
            ComponentInfo info = library.get(UUID.fromString(grooveSetId));
            File location = library.resolveLocation(info);
            grooveSet = factory.load(location, GrooveSet.class);
        } else {
            grooveSet = createGrooveSet("UntitledGrooveSet");
        }

        if (grooveSet == null)
            throw new IllegalStateException("GrooveSet was not created or loaded");

        setGrooveSet(grooveSet);
    }

    /**
     * Creates a {@link GrooveBox} machine and adds it to the {@link GrooveSet}.
     * <p>
     * When added to the {@link GrooveSet}, the create() method will be called
     * on the {@link GrooveBox} and a new {@link PatternBank} will be assigned,
     * create() is also called on the {@link PatternBank}.
     * 
     * @param grooveBoxType The type of {@link GrooveBox} to create using the
     *            {@link GrooveBoxType}.
     * @return A new {@link GrooveBox} instance with new {@link PatternBank}.
     * @throws CausticException
     */
    public GrooveBox createGrooveBox(GrooveBoxType grooveBoxType, String patternBankName)
            throws CausticException {
        if (grooveSet == null)
            throw new IllegalStateException("grooveSet is null");

        // Create the GrooveBox
        GrooveBox grooveBox = factory.createGrooveBox(grooveSet, grooveBoxType);
        grooveSet.addGrooveBox(grooveBox);

        // Create the PatternBank for the grooveBox
        String name = patternBankName + "-" + grooveBox.getInfo().getId().toString();
        PatternBank patternBank = createPatternBank(name, grooveBox);
        add(patternBank);

        return grooveBox;
    }

    private PatternBank createPatternBank(String name, GrooveBox grooveBox) throws CausticException {
        ComponentInfo info = factory.createInfo(ComponentType.PatternBank, name);
        PatternBank patternBank = factory.createPatternBank(info, grooveBox);
        grooveBox.setPatternBank(patternBank);
        patternBank.create(grooveSet.getRackSet().getFactory().createContext());
        return patternBank;
    }

    @SuppressWarnings("unused")
    private SongBank createSongBank(String name, PatternBank patternBank) throws CausticException {
        ComponentInfo info = factory.createInfo(ComponentType.SongBank, name);
        SongBank songBank = factory.createSongBank(info, patternBank);
        songBank.create(factory.createContext());
        return songBank;
    }

    public void save() throws FileNotFoundException {
        library.refresh(grooveSet);
        factory.save(library, library.getLibrariesDirectory());
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    protected void add(ICaustkComponent component) {
        try {
            library.add(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected GrooveSet createGrooveSet(String name) throws CausticException {
        // Create the  RackSet
        RackSet rackSet = factory.createRackSet();
        rackSet.setInternal();

        // Create empty GrooveSet that will hold GrooveMachines
        ComponentInfo info = factory.createInfo(ComponentType.GrooveSet, name);
        GrooveSet grooveSet = factory.createGrooveSet(info, rackSet);
        grooveSet.create(factory.createContext());
        add(grooveSet);
        return grooveSet;
    }

    private void createOrLoadLibrary() throws IOException, CausticException {
        String projectName = application.getController().getProjectManager().getProject().getName();

        library = factory.createLibrary(new File("Projects/" + projectName + "/Library"));
        if (!library.exists()) {
            // library.save();
            library.getDirectory().mkdirs();
            factory.save(library, library.getLibrariesDirectory());
        } else {
            library = factory.load(library.getManifestFile(), Library.class);
            library.load(factory.createContext());
        }
        factory.setLibrary(library);
    }
}
