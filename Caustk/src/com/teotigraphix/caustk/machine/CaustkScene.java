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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.IRackAware;
import com.teotigraphix.caustk.controller.IRackContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;
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

    public boolean removeMachine(CaustkMachine caustkMachine) {
        if (machines.remove(caustkMachine.getIndex()) == null)
            return false;
        RackMessage.REMOVE.send(rack, caustkMachine.getIndex());
        return true;
    }

    public int getMachineCount() {
        return machines.size();
    }

    public boolean hasMachine(int index) {
        return machines.containsKey(index);
    }

    public Collection<CaustkMachine> getMachines() {
        return Collections.unmodifiableCollection(machines.values());
    }

    public Collection<Tone> getTones() {
        ArrayList<Tone> result = new ArrayList<Tone>();
        for (CaustkMachine machine : machines.values()) {
            result.add(machine.getTone());
        }
        return result;
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

    public CaustkMachine getMachineByName(String value) {
        for (CaustkMachine caustkMachine : machines.values()) {
            if (caustkMachine.getMachineName().equals(value))
                return caustkMachine;
        }
        return null;
    }

    public List<CaustkMachine> findMachineStartsWith(String name) {
        List<CaustkMachine> result = new ArrayList<CaustkMachine>();
        for (CaustkMachine tone : machines.values()) {
            if (tone.getMachineName().startsWith(name))
                result.add(tone);
        }
        return result;
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
     * <p>
     * So; any client calling this needs to do it in an initialize phase or save
     * the state of the rack into a temp <code>.caustic</code> file to reload
     * after this method returns.
     * 
     * @param context
     * @throws IOException
     * @throws CausticException
     */
    public void load(IRackContext context) throws CausticException {
        if (causticFile == null || !causticFile.exists())
            throw new IllegalStateException("Caustic song file null or not found on file system: "
                    + causticFile);

        if (rack == null)
            throw new IllegalStateException("Rack must not be null");

        // reset the rack and sound source to empty
        RackMessage.BLANKRACK.send(rack);

        // load the song raw, don not create tones
        RackMessage.LOAD_SONG.send(rack, causticFile.getAbsolutePath());

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
