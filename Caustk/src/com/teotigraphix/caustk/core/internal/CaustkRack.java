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

package com.teotigraphix.caustk.core.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.ICaustkSerializer;
import com.teotigraphix.caustk.gdx.app.ICaustkApplication;
import com.teotigraphix.caustk.gdx.app.Project;
import com.teotigraphix.caustk.groove.importer.CausticFileImporter;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryPatternBank;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.RackInstance;
import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.node.machine.MachineType;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.master.MasterDelayNode;
import com.teotigraphix.caustk.node.master.MasterEqualizerNode;
import com.teotigraphix.caustk.node.master.MasterLimiterNode;
import com.teotigraphix.caustk.node.master.MasterReverbNode;
import com.teotigraphix.caustk.node.master.MasterVolumeNode;
import com.teotigraphix.caustk.node.sequencer.MasterSequencerChannel;
import com.teotigraphix.caustk.utils.core.RuntimeUtils;

/**
 * The {@link CaustkRack} holds the current {@link RackInstance} session state.
 * <p>
 * All events dispatched through a {@link NodeBase} uses the rack's
 * {@link #getEventBus()}.
 * <p>
 * The rack is really just a Facade over the {@link RackInstance} public API for
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

    //private RackInstance rackInstance;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    @Override
    public final boolean isLoaded() {
        return getRackInstance() != null;
    }

    //----------------------------------
    // application
    //----------------------------------

    @Override
    public final ICaustkApplication getApplication() {
        return runtime.getApplication();
    }

    //----------------------------------
    // serializer
    //----------------------------------

    @Override
    public final ICaustkSerializer getSerializer() {
        return serializer;
    }

    //----------------------------------
    // rackNode
    //----------------------------------

    @Override
    public final RackInstance getRackInstance() {
        return project.getRackInstance();
        //return rackInstance;
    }

    //    protected void setRackNode(RackInstance rackNode) {
    //        if (rackNode == getRackInstance())
    //            return;
    //        RackInstance oldNode = getRackInstance();
    //        if (oldNode != null) {
    //            oldNode.destroy();
    //        }
    //        this.rackInstance = rackNode;
    //        RackMessage.BLANKRACK.send(this);
    //    }

    //--------------------------------------------------------------------------
    // Public RackNode Facade API
    //--------------------------------------------------------------------------

    //----------------------------------
    // RackNode
    //----------------------------------

    @Override
    public final String getName() {
        return getRackInstance().getName();
    }

    @Override
    public final String getPath() {
        return getRackInstance().getPath();
    }

    @Override
    public final File getFile() {
        return getRackInstance().getAbsoluteFile();
    }

    //----------------------------------
    // MasterNode
    //----------------------------------

    @Override
    public final MasterDelayNode getDelay() {
        return getRackInstance().getMaster().getDelay();
    }

    @Override
    public final MasterReverbNode getReverb() {
        return getRackInstance().getMaster().getReverb();
    }

    @Override
    public final MasterEqualizerNode getEqualizer() {
        return getRackInstance().getMaster().getEqualizer();
    }

    @Override
    public final MasterLimiterNode getLimiter() {
        return getRackInstance().getMaster().getLimiter();
    }

    @Override
    public final MasterVolumeNode getVolume() {
        return getRackInstance().getMaster().getVolume();
    }

    //----------------------------------
    // MachineNode
    //----------------------------------

    @Override
    public final boolean contains(int machineIndex) {
        return getRackInstance().containsMachine(machineIndex);
    }

    @Override
    public final Collection<? extends Machine> machines() {
        return getRackInstance().getMachines();
    }

    @Override
    public final Machine get(int machineIndex) {
        return getRackInstance().getMachine(machineIndex);
    }

    //----------------------------------
    // SequencerNode
    //----------------------------------

    @Override
    public final MasterSequencerChannel getSequencer() {
        return getRackInstance().getSequencer();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    CaustkRack(CaustkRuntime runtime) {
        super(runtime.getSoundGenerator());
        this.runtime = runtime;
        this.serializer = new CaustkSerializer();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            runtime.getFactory().initialize();
        } catch (CausticException e) {
            e.printStackTrace();
            runtime.getLogger().err("", "Cache directory could not be created or cleaned");
        }
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    @Override
    public RackInstance fill(File file) throws IOException {
        // XXX Needs to be fixed
        //        File directory = runtime.getFactory().getCacheDirectory("fills");
        //        File tempCausticSnapshot = new File(directory, UUID.randomUUID().toString()
        //                .substring(0, 10)
        //                + ".caustic");
        //        tempCausticSnapshot = getRackInstance().saveSongAs(tempCausticSnapshot);
        //        if (!tempCausticSnapshot.exists())
        //            throw new IOException("failed to save temp .caustic file in fill()");
        //
        //        RackInstance oldNode = getRackInstance();
        //        RackInstance rackInstanceTarget = RackNodeUtils.create(file);
        //        this.rackInstance = rackInstanceTarget;
        //
        //        RackMessage.BLANKRACK.send(this);
        //        restore(rackInstance); // fill the node
        //
        //        this.rackInstance = oldNode;
        //        this.rackInstance.loadSong(tempCausticSnapshot);
        //
        //        return rackInstanceTarget;
        return null;
    }

    @Override
    public void fill(LibraryGroup libraryGroup) throws CausticException, IOException {
        fill(libraryGroup, true, true, true, true);
    }

    @Override
    public RackInstance fill(LibraryGroup libraryGroup, boolean importPreset,
            boolean importEffects, boolean importPatterns, boolean importMixer)
            throws CausticException, IOException {
        //        RackNode rackNode = create();
        // remove all machines, clear rack
        //        setRackNode(rackNode);

        // create machines
        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            LibraryEffect libraryEffect = librarySound.getEffect();
            LibraryInstrument libraryInstrument = librarySound.getInstrument();
            LibraryPatternBank patternBank = librarySound.getPatternBank();

            int index = librarySound.getIndex();
            MachineType machineType = libraryInstrument.getMachine().getType();
            String machineName = libraryInstrument.getMachine().getName();
            Machine machineNode = getRackInstance().createMachine(index, machineType, machineName);

            //libraryInstrument.setMachineNode(machineNode);

            if (importPreset) {
                File presetFile = libraryInstrument.getPendingPresetFile();
                if (presetFile == null) {
                    presetFile = loadTempPresetFile(libraryInstrument, machineNode);
                    libraryInstrument.setPendingPresetFile(presetFile);
                }
                if (machineType != MachineType.Vocoder)
                    loadPrest(machineNode, libraryInstrument);
            }

            if (importEffects) {
                loadEffects(machineNode, libraryEffect);
            }

            if (importMixer) {
                loadMixerChannel(machineNode, libraryInstrument);
            }

            if (importPatterns) {
                loadPatternSequencer(machineNode, patternBank);
            }
        }

        return getRackInstance();
    }

    public void loadPrest(Machine machineNode, LibraryInstrument libraryInstrument)
            throws IOException {
        machineNode.getPreset().load(libraryInstrument.getPendingPresetFile());
    }

    public void loadEffects(Machine machineNode, LibraryEffect libraryEffect) {
        machineNode.getEffect().updateEffects(machineNode, libraryEffect.get(0),
                libraryEffect.get(1));
    }

    public void loadMixerChannel(Machine machineNode, LibraryInstrument libraryInstrument) {
        MixerChannel oldMixer = libraryInstrument.getMachine().getMixer();
        machineNode.updateMixer(oldMixer);
    }

    public void loadPatternSequencer(Machine machineNode, LibraryPatternBank patternBank) {
        //        PatternSequencerComponent oldSequencer = libraryInstrument.getMachineNode().getSequencer();
        //        machineNode.updateSequencer(oldSequencer);

        for (PatternNode patternNode : patternBank.getPatterns()) {
            //System.out.println("    " + patternNode.getName());
            machineNode.getSequencer().addPattern(patternNode);
        }
    }

    private Project project;

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public <T extends Project> T setProject(File file, Class<T> type) throws IOException {

        T project = getSerializer().deserialize(file, type);

        byte[] data = project.getRackBytes();

        File tempDirectory = RuntimeUtils.getApplicationTempDirectory();
        File racksDirectory = new File(tempDirectory, "racks");
        File causticFile = new File(racksDirectory, project.getId().toString() + ".caustic");
        FileUtils.writeByteArrayToFile(causticFile, data);

        if (!causticFile.exists())
            throw new IOException(".caustic file failed to write in .temp");

        setProjectInternal(project);

        project.getRackInstance().loadSong(causticFile);

        FileUtils.deleteQuietly(causticFile);

        if (causticFile.exists())
            throw new IOException(".caustic file failed to delete in .temp");

        return project;
    }

    private void setProjectInternal(Project project) {
        this.project = project;
        project.setRack(this);
        //setRackNode(project.getRackInstance());
    }

    @Override
    public void setProject(Project project) throws IOException {
        setProjectInternal(project);
    }

    @Override
    public void load(File causticFile) throws IOException {
        // this.rackInstance = RackNodeUtils.create();
        // RackMessage.BLANKRACK
        getProject().reset();
        // RackMessage.LOAD_SONG
        getRackInstance().loadSong(causticFile);
    }

    @Override
    public void loadAndRestore(File causticFile) throws IOException {
        // this.rackInstance = RackNodeUtils.create();
        // RackMessage.BLANKRACK
        getProject().reset();
        // RackMessage.LOAD_SONG
        getRackInstance().loadSong(causticFile);

        getRackInstance().restore();
    }

    @Override
    public void restore(RackInstance rackInstance) {
        // will save the state of the native rack into the node
        // this basically takes a snap shot of the native rack
        //setRackNode(rackNode);
        rackInstance.restore();
    }

    @Override
    public void update(RackInstance rackNode) {
        //setRackNode(rackNode);
        rackNode.update();
    }

    @Override
    public void frameChanged(float deltaTime) {
        getRackInstance().getSequencer().frameChanged(deltaTime);
    }

    private File loadTempPresetFile(LibraryInstrument libraryInstrument, Machine machineNode)
            throws IOException {
        File tempPreset = null;
        MachineType machineType = machineNode.getType();
        if (machineType != MachineType.Vocoder) {
            File cacheDirectory = CaustkRuntime.getInstance().getFactory()
                    .getCacheDirectory("temp_presets");
            tempPreset = new File(cacheDirectory, "preset." + machineType.getExtension());
            byte[] data = libraryInstrument.getMachine().getPreset().getRestoredData();
            if (data != null) {
                FileUtils.writeByteArrayToFile(tempPreset, data);
            } else {
                runtime.getLogger().err("CausticRack",
                        "Could not load preset data for: " + libraryInstrument.getMachine());
            }

        }
        return tempPreset;
    }

    class CaustkSerializer implements ICaustkSerializer {

        private CausticFileImporter importer;

        private Kryo kryo;

        @Override
        public CausticFileImporter getImporter() {
            return importer;
        }

        @Override
        public Kryo getKryo() {
            return kryo;
        }

        CaustkSerializer() {

            importer = new CausticFileImporter();

            kryo = new Kryo();

            kryo.setDefaultSerializer(TaggedFieldSerializer.class);
            kryo.setRegistrationRequired(true);

            CaustkSerializerTags.register(kryo);
        }

        @Override
        public void serialize(File target, Object node) throws IOException {
            Output output = new Output(new FileOutputStream(target.getAbsolutePath()));
            kryo.writeObject(output, node);
            output.close();
        }

        @Override
        public <T> T deserialize(File file, Class<T> type) throws IOException {
            Input input = new Input(new FileInputStream(file));
            T instance = kryo.readObject(input, type);
            return instance;
        }

        @Override
        public <T> T fromXMLManifest(File manifestFile, Class<T> clazz)
                throws FileNotFoundException {
            return importer.fromXMLManifest(manifestFile, clazz);
        }

        @Override
        public <T> T fromXMLManifest(String manifestData, Class<T> clazz)
                throws FileNotFoundException {
            return importer.fromXMLManifest(manifestData, clazz);
        }

    }
}
