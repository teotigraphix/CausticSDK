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

package com.teotigraphix.caustk.live;

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
import com.teotigraphix.caustk.rack.mixer.MasterDelay;
import com.teotigraphix.caustk.rack.mixer.MasterEqualizer;
import com.teotigraphix.caustk.rack.mixer.MasterLimiter;
import com.teotigraphix.caustk.rack.mixer.MasterReverb;
import com.teotigraphix.caustk.rack.tone.RackTone;

public class RackSet implements ICaustkComponent, IRackAware {

    private transient IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private File causticFile;

    @Tag(2)
    private boolean isInternal;

    @Tag(3)
    private Map<Integer, Machine> machines = new HashMap<Integer, Machine>(14);

    @Tag(4)
    private MasterMixer masterMixer;

    @Tag(5)
    private MasterSequencer masterSequencer;

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

    //----------------------------------
    // causticFile
    //----------------------------------

    /**
     * Sets the {@link RackSet} as an internal scene(not saved to disk), meaning
     * it is treated as a application state scene loaded when the application is
     * loaded with the application's state.
     */
    public final void setInternal() {
        isInternal = true;
    }

    /**
     * Returns whether the {@link RackSet} is an internal scene. (not saved to
     * disk)
     */
    public boolean isInternal() {
        return isInternal;
    }

    public final MasterDelay getDelay() {
        return masterMixer.getDelay();
    }

    public final MasterReverb getReverb() {
        return masterMixer.getReverb();
    }

    public final MasterEqualizer getEqualizer() {
        return masterMixer.getEqualizer();
    }

    public final MasterLimiter getLimiter() {
        return masterMixer.getLimiter();
    }

    public final float getVolume() {
        return masterMixer.getVolume().getVolume();
    }

    public final void setVolume(float value) {
        masterMixer.getVolume().setVolume(value);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    RackSet() {
    }

    RackSet(ComponentInfo info) {
        this.info = info;
    }

    RackSet(ComponentInfo info, File absoluteCausticFile) {
        this.info = info;
        this.causticFile = absoluteCausticFile;
        this.info.setName(absoluteCausticFile.getName().replace(".caustic", ""));
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void addMachine(int index, Machine caustkMachine) {
        // XXX This is going to be complex but just try adding to empty
        // if the index is right, should be able to call update()
        // and have the majic happen
        caustkMachine.setIndex(index);
        machines.put(index, caustkMachine);
        caustkMachine.update();
        masterSequencer.updateMachine(caustkMachine);
    }

    public boolean removeMachine(Machine caustkMachine) {
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

    public Collection<Machine> getMachines() {
        return Collections.unmodifiableCollection(machines.values());
    }

    public Collection<RackTone> getRackTones() {
        ArrayList<RackTone> result = new ArrayList<RackTone>();
        for (Machine machine : machines.values()) {
            result.add(machine.getRackTone());
        }
        return result;
    }

    /**
     * Returns the {@link Machine} at the specified index, <code>null</code> if
     * does not exist.
     * 
     * @param index The machine index.
     */
    public Machine getMachine(int index) {
        return machines.get(index);
    }

    public Machine getMachineByName(String value) {
        for (Machine caustkMachine : machines.values()) {
            if (caustkMachine.getMachineName().equals(value))
                return caustkMachine;
        }
        return null;
    }

    public List<Machine> findMachineStartsWith(String name) {
        List<Machine> result = new ArrayList<Machine>();
        for (Machine tone : machines.values()) {
            if (tone.getMachineName().startsWith(name))
                result.add(tone);
        }
        return result;
    }

    public void rackChanged(IRack rack) throws CausticException {
        // since the is a restoration of deserialized components, all sub
        // components are guaranteed to be created, setRack() recurses and sets
        // all components rack
        this.rack = rack;

        if (masterMixer == null && masterSequencer == null) {
            create();
        } else if (!isInternal) {
            // if this scene is internal, the rack state is already in the correct state
            // no need to update the native rack with the scene's serialized properties
            update();
        }
    }

    public void create() throws CausticException {
        masterMixer = rack.getFactory().createMasterMixer(this);
        masterSequencer = rack.getFactory().createMasterSequencer(this);
        masterMixer.create();
        masterSequencer.create();
    }

    public void update() {
        if (rack == null)
            throw new IllegalStateException("Rack cannot be null");

        RackMessage.BLANKRACK.send(rack);

        masterMixer.update();

        for (Machine machine : machines.values()) {
            machine.update();
        }

        masterSequencer.update();
    }

    /**
     * Loads the {@link RackSet} using the {@link #getCausticFile()} passed during
     * scene construction.
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
            Machine caustkMachine = getMachine(i);
            if (caustkMachine != null) {
                loadMachine(caustkMachine, context);
            }
        }
        masterSequencer.load(context);
    }

    public Machine createMachine(int index, String machineName, MachineType machineType)
            throws CausticException {
        Machine caustkMachine = getRack().getFactory().createMachine(this, index, machineType,
                machineName);
        machines.put(index, caustkMachine);
        caustkMachine.create();
        return caustkMachine;
    }

    private void createMachine(int index, IRackContext context) throws IOException,
            CausticException {
        String machineName = RackMessage.QUERY_MACHINE_NAME.queryString(rack, index);
        if (machineName == null)
            return;

        MachineType machineType = MachineType.fromString(RackMessage.QUERY_MACHINE_TYPE
                .queryString(rack, index));
        Machine caustkMachine = context.getFactory().createMachine(this, index, machineType,
                machineName);
        machines.put(index, caustkMachine);
    }

    private void loadMachine(Machine caustkMachine, IRackContext context) throws IOException,
            CausticException {
        // loads CaustkPatch (MachinePreset, MixerPreset, CaustkEffects), CaustkPhrases
        caustkMachine.load(context);
    }

}
