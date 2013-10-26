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

package com.teotigraphix.caustk.machine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;

public class CaustkScene implements ICaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(1)
    private String name;

    @Tag(2)
    private File file;

    private File causticFile;

    @Tag(10)
    Map<Integer, CaustkMachine> machines = new HashMap<Integer, CaustkMachine>(14);

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    @Override
    public final UUID getId() {
        return id;
    }

    //----------------------------------
    // name
    //----------------------------------

    @Override
    public final String getName() {
        return name;
    }

    void setName(String value) {
        name = value;
    }

    //----------------------------------
    // file
    //----------------------------------

    @Override
    public File getFile() {
        return file;
    }

    void setFile(File value) {
        file = value;
    }

    //----------------------------------
    // causticFile
    //----------------------------------

    /**
     * Returns the absolute location of a <code>.caustic</code> song file if the
     * scene is meant to be initialized from the song file.
     */
    public File getCausticFile() {
        return causticFile;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkScene() {
    }

    CaustkScene(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    CaustkScene(UUID id, File absoluteCausticFile) {
        this.id = id;
        this.causticFile = absoluteCausticFile;
        this.name = absoluteCausticFile.getName().replace(".caustic", "");
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Loads the {@link CaustkScene} using the {@link #getCausticFile()} passed
     * during scene construction.
     * 
     * @throws IOException
     * @throws CausticException
     */
    public void load(CaustkLibraryFactory factory) throws IOException, CausticException {
        if (causticFile == null || !causticFile.exists())
            throw new IllegalStateException("Caustic song file null or not found on file system: "
                    + causticFile);

        // load CaustkMachines
        IRack rack = factory.getRack();

        RackMessage.BLANKRACK.send(rack);

        RackMessage.LOAD_SONG.send(rack, causticFile.getAbsolutePath());

        for (int i = 0; i < 14; i++) {
            createMachineForLoadOperation(i, factory);
        }
    }

    private void createMachineForLoadOperation(int index, CaustkLibraryFactory factory)
            throws IOException, CausticException {
        IRack rack = factory.getRack();
        String machineName = RackMessage.QUERY_MACHINE_NAME.queryString(rack, index);
        if (machineName == null)
            return;

        MachineType machineType = MachineType.fromString(RackMessage.QUERY_MACHINE_TYPE
                .queryString(rack, index));
        CaustkMachine caustkMachine = factory.createMachine(machineType, index, machineName);
        machines.put(index, caustkMachine);

        // loads CaustkPatch (MachinePreset, MixerPreset, CaustkEffects), CaustkPhrases
        caustkMachine.load(factory);
    }
}
