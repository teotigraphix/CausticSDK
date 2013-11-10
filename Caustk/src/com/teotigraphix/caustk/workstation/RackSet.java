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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.mixer.MasterDelay;
import com.teotigraphix.caustk.rack.mixer.MasterEqualizer;
import com.teotigraphix.caustk.rack.mixer.MasterLimiter;
import com.teotigraphix.caustk.rack.mixer.MasterReverb;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 */
public class RackSet extends CaustkComponent {

    private transient ICaustkFactory factory;

    private transient IRack rack;

    private transient boolean deleteCausticFile = true;

    /**
     * Returns the {@link ICaustkController#getDispatcher()}.
     * <P>
     * All events from {@link ICaustkComponent}s are dispatched through this
     * dispatcher.
     */
    public IDispatcher getComponentDispatcher() {
        return rack.getComponentDispatcher();
    }

    /**
     * Returns the {@link IRack#getDispatcher()}.
     * <p>
     * Only events <strong>within</strong> the {@link IRack} listen to these
     * events.
     */
    public IDispatcher getDispatcher() {
        return rack.getDispatcher();
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private File causticFile;

    @Tag(101)
    private byte[] data;

    @Tag(102)
    private Map<Integer, Machine> machines = new HashMap<Integer, Machine>(14);

    @Tag(103)
    private MasterSystem masterSystem;

    @Tag(104)
    private MasterMixer masterMixer;

    @Tag(105)
    private MasterSequencer masterSequencer;

    @Tag(106)
    private boolean isInternal;

    @Tag(107)
    private boolean isInitialized;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        String name = getInfo().getName();
        if (name == null && causticFile != null)
            name = causticFile.getName().replace(".caustic", "");
        else
            name = "TODO";
        return name;
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
    // machines
    //----------------------------------

    /**
     * Returns the bytes saved when saving a <code>.caustic</code> file state.
     */
    public byte[] getData() {
        return data;
    }

    //----------------------------------
    // machines
    //----------------------------------

    /**
     * Returns a collection of {@link Machine}s defined in the rack set.
     */
    public Collection<Machine> getMachines() {
        return Collections.unmodifiableCollection(machines.values());
    }

    //----------------------------------
    // MasterMixer
    //----------------------------------

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

    public final float getOut() {
        return masterMixer.getVolume().getOut();
    }

    public final void setOut(float value) {
        masterMixer.getVolume().setOut(value);
    }

    //----------------------------------
    // MasterSystem
    //----------------------------------

    public MasterSystem getSystem() {
        return masterSystem;
    }

    //----------------------------------
    // MasterSequencer
    //----------------------------------

    public MasterSequencer getSequencer() {
        return masterSequencer;
    }

    //----------------------------------
    // isInternal
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

    //----------------------------------
    // isInitialized
    //----------------------------------

    /**
     * Returns whether this rack set has had it's
     * {@link #create(ICaustkApplicationContext)} or
     * {@link #load(ICaustkApplicationContext)}method called.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    //----------------------------------
    // rack
    //----------------------------------

    /**
     * Returns the instance of the {@link IRack} that created this instance or
     * the session rack that exists when this RackSet was deserialized.
     */
    public IRack _getRack() {
        return rack;
    }

    //----------------------------------
    // factory
    //----------------------------------

    public ICaustkFactory getFactory() {
        return factory;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    RackSet() {
    }

    RackSet(ComponentInfo info, ICaustkFactory factory) {
        setInfo(info);
        this.factory = factory;
        this.rack = factory.getRack();
    }

    RackSet(ComponentInfo info, ICaustkFactory factory, File absoluteCausticFile) {
        setInfo(info);
        this.factory = factory;
        this.rack = factory.getRack();
        this.causticFile = absoluteCausticFile;
        info.setName(absoluteCausticFile.getName().replace(".caustic", ""));
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                masterSystem = factory.createMasterSystem(this);
                masterMixer = factory.createMasterMixer(this);
                masterSequencer = factory.createMasterSequencer(this);
                masterSystem.create(context);
                masterMixer.create(context);
                masterSequencer.create(context);

                isInitialized = true;

                break;

            case Load:
                if (causticFile == null || !causticFile.exists())
                    throw new IllegalStateException(
                            "Caustic song file null or not found on file system: " + causticFile);

                if (rack == null)
                    throw new IllegalStateException("Rack must not be null");

                // reset the rack and sound source to empty
                RackMessage.BLANKRACK.send(rack);

                // load the song raw, don not create tones
                RackMessage.LOAD_SONG.send(rack, causticFile.getAbsolutePath());

                //        create(context);

                try {
                    // create the scene sub components
                    // createComponents(context);
                    masterSystem = factory.createMasterSystem(this);
                    masterMixer = factory.createMasterMixer(this);
                    masterSequencer = factory.createMasterSequencer(this);
                    // load the current song rack state into the sub components
                    loadComponents(context);
                } catch (IOException e) {
                    throw new CausticException(e);
                }

                isInitialized = true;

                break;

            case Update:
                rack = context.getRack();

                RackMessage.BLANKRACK.send(rack);

                masterSystem.update(context);
                masterMixer.update(context);

                for (Machine machine : machines.values()) {
                    machine.update(context);
                }

                masterSequencer.update(context);
                break;

            case Restore:
                break;

            case Connect:
                rack = context.getRack();
                masterSystem.phaseChange(context, ComponentPhase.Connect);
                masterMixer.phaseChange(context, ComponentPhase.Connect);
                masterSequencer.phaseChange(context, ComponentPhase.Connect);
                for (Machine machine : machines.values()) {
                    machine.phaseChange(context, ComponentPhase.Connect);
                }
                break;

            case Disconnect:
                rack = null;
                break;
        }
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public boolean updatePosition(int measure, float beat) {
        final boolean changed = masterSequencer.updatePosition(measure, beat);
        return changed;
    }

    public void addMachine(int index, Machine machine) throws CausticException {
        // XXX This is going to be complex but just try adding to empty
        // if the index is right, should be able to call update()
        // and have the majic happen
        machine.setMachineIndex(index);
        machines.put(index, machine);
        machine.update(factory.createContext());
        masterSequencer.updateMachine(machine);
    }

    public boolean removeMachine(Machine caustkMachine) {
        if (machines.remove(caustkMachine.getMachineIndex()) == null)
            return false;
        RackMessage.REMOVE.send(rack, caustkMachine.getMachineIndex());
        return true;
    }

    public int getMachineCount() {
        return machines.size();
    }

    public boolean hasMachine(int index) {
        return machines.containsKey(index);
    }

    /**
     * Returns a collection of {@link RackTone}s defined in the rack set.
     */
    public Collection<RackTone> getRackTones() {
        ArrayList<RackTone> result = new ArrayList<RackTone>();
        for (Machine machine : machines.values()) {
            result.add(machine.getRackTone());
        }
        return result;
    }

    /**
     * Returns a collection of {@link Patch}s defined in the rack set using the
     * {@link Machine} parent.
     * <p>
     * This is a utility method to easily extract a collection of Patches in no
     * specific order.
     */
    public Collection<Patch> getPatches() {
        ArrayList<Patch> result = new ArrayList<Patch>();
        for (Machine machine : machines.values()) {
            result.add(machine.getPatch());
        }
        return result;
    }

    /**
     * Returns a collection of {@link Phrase}s defined in the rack set using the
     * {@link Machine} parent.
     * <p>
     * This is a utility method to easily extract a collection of Phrases in no
     * specific order.
     */
    public Collection<Phrase> getPhrases() {
        ArrayList<Phrase> result = new ArrayList<Phrase>();
        for (Machine machine : machines.values()) {
            Collection<Phrase> collection = machine.getPhrases().values();
            result.addAll(collection);
        }
        return result;
    }

    /**
     * Returns a collection of {@link Effect}s defined in the rack set using the
     * {@link Patch} parent.
     * <p>
     * This is a utility method to easily extract a collection of Phrases in no
     * specific order.
     */
    public Collection<Effect> getEffects() {
        ArrayList<Effect> result = new ArrayList<Effect>();
        for (Machine machine : machines.values()) {
            Patch patch = machine.getPatch();
            Effect effect1 = patch.getEffect(0);
            if (effect1 != null)
                result.add(effect1);
            Effect effect2 = patch.getEffect(1);
            if (effect2 != null)
                result.add(effect2);
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

    public void rackChanged(ICaustkFactory factory) throws CausticException, IOException {
        // since the is a restoration of deserialized components, all sub
        // components are guaranteed to be created, setRack() recurses and sets
        // all components rack
        this.factory = factory;
        this.rack = factory.getRack();

        final ICaustkApplicationContext context = factory.createContext();

        if (!isInternal) {

            if (!isInitialized)
                throw new IllegalStateException("RackSet has not been created");

            // if this set is internal, the rack state is already in the correct state
            // no need to update the native rack with the scene's serialized properties
            update(context);
        } else if (!isInitialized) {
            create(context);
        } else if (isInternal) {
            // hook Rack back up to machines and RackTones
            componentPhaseChange(context, ComponentPhase.Connect);
            // an internal RackSet saves it's .caustic binary data
            // and will reload it here so the native audio system is initialized
            ICaustkController controller = rack.getController();
            loadSongBytesIntoRack(controller, data);
        }
    }

    public void clearMachines() throws CausticException {
        ArrayList<Machine> list = new ArrayList<Machine>(getMachines());
        for (Machine machine : list) {
            removeMachine(machine);
        }
        rack.clearRack();
    }

    @Override
    public void onSave(ICaustkApplicationContext context) {
        masterSystem.onSave(context);
        masterMixer.onSave(context);
        masterSequencer.onSave(context);
        for (Machine machine : machines.values()) {
            machine.onSave(context);
        }
        if (isInternal) {
            ICaustkController controller = rack.getController();
            final File file = getTempCausticFile(controller);
            // save the .caustic file as a byte array
            try {
                File savedFile = rack.saveSongAs(file);
                data = FileUtils.readFileToByteArray(savedFile);
                if (deleteCausticFile)
                    FileUtils.deleteQuietly(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadComponents(ICaustkApplicationContext context) throws IOException,
            CausticException {
        masterMixer.load(context);
        for (int i = 0; i < 14; i++) {
            Machine machine = loadMachine(i, context);
            if (machine != null) {
                loadMachine(machine, context);
            }
        }
        masterSequencer.load(context);
    }

    public Machine createMachine(int machineIndex, String machineName, MachineType machineType)
            throws CausticException {
        Machine machine = factory.createMachine(this, machineIndex, machineType, machineName);
        machines.put(machineIndex, machine);
        machine.create(factory.createContext());
        return machine;
    }

    private Machine loadMachine(int index, ICaustkApplicationContext context) throws IOException,
            CausticException {
        String machineName = RackMessage.QUERY_MACHINE_NAME.queryString(rack, index);
        if (machineName == null)
            return null;

        MachineType machineType = MachineType.fromString(RackMessage.QUERY_MACHINE_TYPE
                .queryString(rack, index));
        Machine machine = context.getFactory().createMachine(this, index, machineType, machineName);
        // machine.create(context);
        machines.put(index, machine);
        return machine;
    }

    private void loadMachine(Machine machine, ICaustkApplicationContext context)
            throws IOException, CausticException {
        // loads CaustkPatch (MachinePreset, MixerPreset, CaustkEffects), CaustkPhrases
        machine.load(context);
    }

    public void dispose() throws CausticException {
        rack.clearRack();
        rack = null;
        factory = null;
    }

    //--------------------------------------------------------------------------
    // Loading song data
    //--------------------------------------------------------------------------

    static void loadSongBytesIntoRack(ICaustkController controller, byte[] data)
            throws IOException, CausticException {

        // save a temp .caustic file to load the binary data into the native core
        File absoluteCausticFile = getTempCausticFile(controller);
        FileUtils.writeByteArrayToFile(absoluteCausticFile, data);

        // only load the song into the core memory, we already have
        // the object graph mirrored in the Rack
        controller.getRack().loadSong(absoluteCausticFile);

        // remove the temp file
        FileUtils.deleteQuietly(absoluteCausticFile);
    }

    static File getTempCausticFile(ICaustkController controller) {
        String projectName = "TempAudio.caustic";
        File tempFile = new File(RuntimeUtils.getApplicationDirectory(".temp"), projectName);
        return tempFile;
    }

}
