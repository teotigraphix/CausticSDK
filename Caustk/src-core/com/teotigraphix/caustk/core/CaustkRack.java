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

package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent;
import com.teotigraphix.caustk.node.master.MasterDelayNode;
import com.teotigraphix.caustk.node.master.MasterEqualizerNode;
import com.teotigraphix.caustk.node.master.MasterLimiterNode;
import com.teotigraphix.caustk.node.master.MasterReverbNode;
import com.teotigraphix.caustk.node.master.MasterVolumeNode;
import com.teotigraphix.caustk.node.sequencer.SequencerNode;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * The {@link CaustkRack} holds the current {@link RackNode} session state.
 * <p>
 * All events dispatched through a {@link NodeBase} uses the rack's
 * {@link #getEventBus()}.
 * <p>
 * The rack is really just a Facade over the {@link RackNode} public API for
 * ease of access.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CaustkRack extends CaustkEngine implements ICaustkRack {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private final CaustkRuntime runtime;

    private ICaustkSerializer serializer;

    private RackNode rackNode;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    @Override
    public final boolean isLoaded() {
        return rackNode != null;
    }

    //----------------------------------
    // serializer
    //----------------------------------

    @Override
    public ICaustkSerializer getSerializer() {
        return serializer;
    }

    //----------------------------------
    // rackNode
    //----------------------------------

    @Override
    public final RackNode getRackNode() {
        return rackNode;
    }

    private void setRackNode(RackNode rackNode) {
        if (rackNode == this.rackNode)
            return;
        RackNode oldNode = this.rackNode;
        if (oldNode != null) {
            oldNode.destroy();
        }
        this.rackNode = rackNode;
        RackMessage.BLANKRACK.send(this);
    }

    //--------------------------------------------------------------------------
    // Public RackNode Facade API
    //--------------------------------------------------------------------------

    //----------------------------------
    // RackNode
    //----------------------------------

    @Override
    public String getName() {
        return rackNode.getName();
    }

    @Override
    public String getPath() {
        return rackNode.getPath();
    }

    @Override
    public File getFile() {
        return rackNode.getAbsoluteFile();
    }

    //----------------------------------
    // MasterNode
    //----------------------------------

    @Override
    public MasterDelayNode getDelay() {
        return rackNode.getMaster().getDelay();
    }

    @Override
    public MasterReverbNode getReverb() {
        return rackNode.getMaster().getReverb();
    }

    @Override
    public MasterEqualizerNode getEqualizer() {
        return rackNode.getMaster().getEqualizer();
    }

    @Override
    public MasterLimiterNode getLimiter() {
        return rackNode.getMaster().getLimiter();
    }

    @Override
    public MasterVolumeNode getVolume() {
        return rackNode.getMaster().getVolume();
    }

    //----------------------------------
    // MachineNode
    //----------------------------------

    @Override
    public Collection<? extends MachineNode> getMachines() {
        return rackNode.getMachines();
    }

    @Override
    public MachineNode getMachine(int machineIndex) {
        return rackNode.getMachine(machineIndex);
    }

    //----------------------------------
    // SequencerNode
    //----------------------------------

    @Override
    public SequencerNode getSequencer() {
        return rackNode.getSequencer();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    CaustkRack(CaustkRuntime runtime) {
        super(runtime.getSoundGenerator());
        this.runtime = runtime;
        this.serializer = new CaustkSerializer();
    }

    //--------------------------------------------------------------------------
    // IRackEventBus API
    //--------------------------------------------------------------------------

    @Override
    public void register(Object subscriber) {
        getEventBus().register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        getEventBus().register(subscriber);
    }

    @Override
    public void post(Object event) {
        getEventBus().post(event);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    @Override
    public RackNode create() {
        RackNode rackNode = runtime.getFactory().createRack();
        return rackNode;
    }

    @Override
    public RackNode create(String relativeOrAbsolutePath) {
        RackNode rackNode = runtime.getFactory().createRack(relativeOrAbsolutePath);
        restore(rackNode);
        return rackNode;
    }

    @Override
    public RackNode create(File file) {
        RackNode rackNode = runtime.getFactory().createRack(file);
        restore(rackNode);
        return rackNode;
    }

    @Override
    public void create(RackNode rackNode) {
        // the native rack is created based on the state of the node graph
        // machines/patterns/effects in the node graph get created through OSC
        setRackNode(rackNode);
        rackNode.create();
    }

    @Override
    public RackNode create(LibraryGroup libraryGroup) throws CausticException, IOException {
        return create(libraryGroup, true, true, true, true);
    }

    @Override
    public RackNode create(LibraryGroup libraryGroup, boolean importPreset, boolean importEffects,
            boolean importPatterns, boolean importMixer) throws CausticException, IOException {
        RackNode rackNode = create();
        // remove all machines, clear rack
        setRackNode(rackNode);

        // create machines
        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            LibraryEffect libraryEffect = librarySound.getEffect();
            LibraryInstrument libraryInstrument = librarySound.getInstrument();

            int index = librarySound.getIndex();
            MachineType machineType = libraryInstrument.getMachineNode().getType();
            String machineName = libraryInstrument.getMachineNode().getName();
            MachineNode machineNode = rackNode.createMachine(index, machineType, machineName);

            libraryInstrument.setMachineNode(machineNode);

            if (importPreset) {
                loadPrest(machineNode, libraryInstrument);
            }

            if (importEffects) {
                loadEffects(machineNode, libraryEffect);
            }

            if (importMixer) {
                loadMixerChannel(machineNode, libraryInstrument);
            }

            if (importPatterns) {
                loadPatternSequencer(machineNode, libraryInstrument);
            }
        }

        return rackNode;
    }

    public void loadPrest(MachineNode machineNode, LibraryInstrument libraryInstrument)
            throws IOException {
        machineNode.getPreset().load(libraryInstrument.getPendingPresetFile());
    }

    public void loadEffects(MachineNode machineNode, LibraryEffect libraryEffect) {
        machineNode.getEffects().updateEffects(libraryEffect.get(0), libraryEffect.get(1));
    }

    public void loadMixerChannel(MachineNode machineNode, LibraryInstrument libraryInstrument) {
        MixerChannel oldMixer = libraryInstrument.getMachineNode().getMixer();
        machineNode.updateMixer(oldMixer);
    }

    public void loadPatternSequencer(MachineNode machineNode, LibraryInstrument libraryInstrument) {
        PatternSequencerComponent oldSequencer = machineNode.getSequencer();
        machineNode.updateSequencer(oldSequencer);
    }

    private CaustkProject project;

    @Override
    public CaustkProject getProject() {
        return project;
    }

    @Override
    public <T extends CaustkProject> T setProject(File file, Class<T> type) throws IOException {

        T project = getSerializer().deserialize(file, type);

        byte[] data = project.getRackBytes();

        File tempDirectory = RuntimeUtils.getApplicationTempDirectory();
        File racksDirectory = new File(tempDirectory, "racks");
        File causticFile = new File(racksDirectory, project.getId().toString() + ".caustic");
        FileUtils.writeByteArrayToFile(causticFile, data);

        if (!causticFile.exists())
            throw new IOException(".caustic file failed to write in .temp");

        setProjectInternal(project);

        project.getRackNode().loadSong(causticFile);

        FileUtils.deleteQuietly(causticFile);

        if (causticFile.exists())
            throw new IOException(".caustic file failed to delete in .temp");

        return project;
    }

    private void setProjectInternal(CaustkProject project) {
        this.project = project;
        project.setRack(this);
        setRackNode(project.getRackNode());
    }

    @Override
    public void setProject(CaustkProject project) throws IOException {
        setProjectInternal(project);
    }

    @Override
    public void setup(RackNode rackNode) {
        setRackNode(rackNode);
    }

    @Override
    public void restore(RackNode rackNode) {
        // will save the state of the native rack into the node
        // this basically takes a snap shot of the native rack
        setRackNode(rackNode);
        rackNode.restore();
    }

    @Override
    public void update(RackNode rackNode) {
        setRackNode(rackNode);
        rackNode.update();
    }

    @Override
    public void frameChanged(float deltaTime) {
        getRackNode().getSequencer().frameChanged(deltaTime);
    }
}
