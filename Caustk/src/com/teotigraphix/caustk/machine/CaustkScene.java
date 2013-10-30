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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.IRackAware;
import com.teotigraphix.caustk.controller.IRackContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.ISoundSource;
import com.teotigraphix.caustk.rack.tone.Tone;

public class CaustkScene implements ICaustkComponent, IRackAware {

    private transient IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private File causticFile;

    @Tag(2)
    private Map<Integer, CaustkMachine> machines = new HashMap<Integer, CaustkMachine>(14);

    @Tag(3)
    private CastkMasterMixer masterMixer;

    @Tag(4)
    private CaustkMasterSequencer masterSequencer;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    @Override
    public IRack getRack() {
        return rack;
    }

    @Override
    public void setRack(IRack value) {
        rack = value;
        if (masterMixer != null)
            masterMixer.setRack(rack);
        for (CaustkMachine machine : machines.values()) {
            machine.setRack(rack);
        }
        if (masterSequencer != null)
            masterSequencer.setRack(rack);
    }

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public final ComponentInfo getInfo() {
        return info;
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

    public Collection<CaustkMachine> getMachines() {
        return machines.values();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkScene() {
    }

    CaustkScene(ComponentInfo info) {
        this.info = info;
    }

    CaustkScene(ComponentInfo info, File absoluteCausticFile) {
        this.info = info;
        this.causticFile = absoluteCausticFile;
        this.info.setName(absoluteCausticFile.getName().replace(".caustic", ""));
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void addMachine(int index, CaustkMachine caustkMachine) {
        // XXX This is going to be complex but just try adding to empty
        // if the index is right, should be able to call update()
        // and have the majic happen
        caustkMachine.setIndex(index);
        machines.put(index, caustkMachine);
        caustkMachine.update();
        masterSequencer.updateMachine(caustkMachine);
    }

    /**
     * Returns the {@link CaustkMachine} at the specified index,
     * <code>null</code> if does not exist.
     * 
     * @param index The machine index.
     */
    public CaustkMachine getMachine(int index) {
        return machines.get(index);
    }

    public void update() {
        if (rack == null)
            throw new IllegalStateException("Rack cannot be null");

        RackMessage.BLANKRACK.send(rack);

        masterMixer.update();

        for (CaustkMachine machine : machines.values()) {
            machine.update();
        }

        masterSequencer.update();
    }

    /**
     * Loads the {@link CaustkScene} using the {@link #getCausticFile()} passed
     * during scene construction.
     * <p>
     * Calling this method will issue a <code>BLANKRACK</code> command and
     * <code>LOAD_SONG</code>, all song state is reset to default before
     * loading.
     * 
     * @param factory The library factory.
     * @param populateTones Whether to create and populate the
     *            {@link ISoundSource} with {@link Tone} instances based of the
     *            restoration of the native rack state.
     * @throws IOException
     * @throws CausticException
     */
    public void load(IRackContext context) throws CausticException {
        if (causticFile == null || !causticFile.exists())
            throw new IllegalStateException("Caustic song file null or not found on file system: "
                    + causticFile);

        setRack(context.getRack());

        // reset the rack and sound source to empty
        getRack().clearAndReset();
        // load the song raw, don not create tones
        getRack().loadSongRaw(causticFile);

        try {
            // create the scene sub components
            createComponents(context);
            // load the current song rack state into the sub components
            loadComponents(context);
        } catch (IOException e) {
            throw new CausticException(e);
        }
    }

    private void createComponents(IRackContext context) throws IOException, CausticException {
        masterMixer = context.getFactory().createMasterMixer(this);
        for (int i = 0; i < 14; i++) {
            createMachine(i, context);
        }
        masterSequencer = context.getFactory().createMasterSequencer(this);
    }

    private void loadComponents(IRackContext context) throws IOException, CausticException {
        masterMixer.load(context);
        for (int i = 0; i < 14; i++) {
            CaustkMachine caustkMachine = getMachine(i);
            if (caustkMachine != null) {
                loadMachine(caustkMachine, context);
            }
        }
        masterSequencer.load(context);
    }

    private void createMachine(int index, IRackContext context) throws IOException,
            CausticException {
        String machineName = RackMessage.QUERY_MACHINE_NAME.queryString(rack, index);
        if (machineName == null)
            return;

        MachineType machineType = MachineType.fromString(RackMessage.QUERY_MACHINE_TYPE
                .queryString(rack, index));
        CaustkMachine caustkMachine = context.getFactory().createMachine(index, machineType,
                machineName);
        machines.put(index, caustkMachine);
    }

    private void loadMachine(CaustkMachine caustkMachine, IRackContext context) throws IOException,
            CausticException {
        // loads CaustkPatch (MachinePreset, MixerPreset, CaustkEffects), CaustkPhrases
        caustkMachine.load(context);
    }

}
