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

import com.esotericsoftware.kryo.Kryo;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.controller.core.CaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.Rack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.utils.KryoUtils;

/**
 * One {@link CaustkFactory} per {@link ICaustkApplication}.
 * <p>
 * The factory holds no state, only creates instances. The factory does hold the
 * single {@link ICaustkApplication} and {@link IRack} instance. These are
 * singleton top level application components that are needed during
 * construction.
 * 
 * @author Michael Schmalle
 */
public class CaustkFactory implements ICaustkFactory {

    //----------------------------------
    // Factories
    //----------------------------------

    private Kryo kryo;

    @Override
    public Kryo getKryo() {
        return kryo;
    }

    private ComponentInfoFactory infoFactory;

    private LibraryFactory libraryFactory;

    private TrackSetFactory trackSetFactory;

    private RackSetFactory rackSetFactory;

    private MachineFactory machineFactory;

    private MasterFactory masterFactory;

    private RackToneFactory rackToneFactory;

    private PatternSetFactory patternSetFactory;

    private SongSetFactory songSetFactory;

    //----------------------------------
    // application
    //----------------------------------

    private ICaustkApplication application;

    public ICaustkApplication getApplication() {
        return application;
    }

    //----------------------------------
    // rack
    //----------------------------------

    @Override
    public IRack getRack() {
        return application.getRack();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CaustkFactory(ICaustkApplication application) {
        this.application = application;
        this.kryo = KryoUtils.createKryo(this);
        createFactories();
    }

    private void createFactories() {
        infoFactory = new ComponentInfoFactory();
        infoFactory.setFactory(this);

        libraryFactory = new LibraryFactory();
        libraryFactory.setFactory(this);

        trackSetFactory = new TrackSetFactory();
        trackSetFactory.setFactory(this);

        rackSetFactory = new RackSetFactory();
        rackSetFactory.setFactory(this);
        rackToneFactory = new RackToneFactory();
        rackToneFactory.setFactory(this);

        machineFactory = new MachineFactory();
        machineFactory.setFactory(this);

        masterFactory = new MasterFactory();
        masterFactory.setFactory(this);

        patternSetFactory = new PatternSetFactory();
        patternSetFactory.setFactory(this);

        songSetFactory = new SongSetFactory();
        songSetFactory.setFactory(this);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public ICaustkApplicationContext createContext() {
        return new CaustkApplicationContext(application);
    }

    @Override
    public IRack createRack() {
        Rack rack = new Rack(application);
        return rack;
    }

    //----------------------------------
    // ComponentInfo
    //----------------------------------

    @Override
    public ComponentInfo createInfo(ComponentType type) {
        return infoFactory.createInfo(type);
    }

    @Override
    public ComponentInfo createInfo(ComponentType type, String name) {
        return infoFactory.createInfo(type, name);
    }

    @Override
    public ComponentInfo createInfo(ComponentType type, String relativePath, String name) {
        return infoFactory.createInfo(type, relativePath, name);
    }

    @Override
    public ComponentInfo createInfo(ComponentType type, File relativePath, String name) {
        return infoFactory.createInfo(type, relativePath, name);
    }

    //----------------------------------
    // Library
    //----------------------------------

    /**
     * Creates an empty {@link Library} with a name.
     * <p>
     * The name is used for the directory name held within the
     * <code>/storageRoot/AppName/libraries</code> directory.
     * 
     * @param name The name of the library, used as the directory name.
     */
    @Override
    public Library createLibrary(String name) {
        return libraryFactory.createLibrary(name);
    }

    @Override
    public Library loadLibrary(String name) throws IOException {
        return libraryFactory.loadLibrary(name);
    }

    //----------------------------------
    // TrackSet
    //----------------------------------

    @Override
    public TrackSet createTrackSet(ComponentInfo info, RackSet rackSet) {
        return trackSetFactory.createTrackSet(info, rackSet);
    }

    //----------------------------------
    // RackSet
    //----------------------------------

    /**
     * Creates an empty {@link RackSet} with name.
     * 
     * @param name The name of the scene.
     */
    @Override
    public RackSet createRackSet(ComponentInfo info) {
        return rackSetFactory.createRackSet(info);
    }

    /**
     * Creates a {@link RackSet} from a <code>.caustic</code> song file import.
     * 
     * @param absoluteCausticFile The absolute location of the
     *            <code>.caustic</code> song file.
     */
    @Override
    public RackSet createRackSet(ComponentInfo info, File absoluteCausticFile) {
        return rackSetFactory.createRackSet(info, absoluteCausticFile);
    }

    //----------------------------------
    // Machine
    //----------------------------------

    @Override
    public Machine createMachine(ComponentInfo info, int machineIndex, MachineType machineType,
            String machineName) {
        return machineFactory.createMachine(info, machineIndex, machineType, machineName);
    }

    @Override
    public Machine createMachine(RackSet rackSet, int machineIndex, MachineType machineType,
            String machineName) {
        return machineFactory.createMachine(rackSet, machineIndex, machineType, machineName);
    }

    /**
     * Creates a {@link Patch} with {@link UUID} and {@link MachineType}.
     * 
     * @param toneType The {@link MachineType} of the
     */
    @Override
    public Patch createPatch(ComponentInfo info, MachineType machineType) {
        return machineFactory.createPatch(info, machineType);
    }

    /**
     * Creates a new {@link Patch}, assigns the {@link Machine}.
     * 
     * @param machine A {@link Machine} that does not exist in the native rack.
     */
    @Override
    public Patch createPatch(Machine machine) {
        return machineFactory.createPatch(machine);
    }

    @Override
    public Phrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex,
            int patternIndex) {
        return machineFactory.createPhrase(info, machineType, bankIndex, patternIndex);
    }

    @Override
    public Phrase createPhrase(Machine caustkMachine, int bankIndex, int patternIndex) {
        return machineFactory.createPhrase(caustkMachine, bankIndex, patternIndex);
    }

    /**
     * Creates an non attached {@link Effect} with the internal {@link IEffect}
     * created.
     * <p>
     * Non attached means no {@link Patch} or {@link Machine} references.
     * 
     * @param info The {@link ComponentInfo}.
     * @param effectType The {@link EffectType}.
     */
    @Override
    public Effect createEffect(ComponentInfo info, EffectType effectType) {
        return machineFactory.createEffect(info, effectType);
    }

    @Override
    public Effect createEffect(int slot, EffectType effectType) {
        return machineFactory.createEffect(slot, effectType);
    }

    /**
     * @param slot
     * @param effectType
     * @param caustkPatch
     * @see Patch#load(LibraryFactory)
     */
    @Override
    public Effect createEffect(int slot, EffectType effectType, Patch caustkPatch) {
        return machineFactory.createEffect(slot, effectType, caustkPatch);
    }

    //----------------------------------
    // PatternSet
    //----------------------------------

    @Override
    public PatternSet createPatternSet(ComponentInfo info, RackSet rackSet) {
        return patternSetFactory.createPatternSet(info, rackSet);
    }

    @Override
    public Pattern createPattern(ComponentInfo info, PatternSet patternSet, int index) {
        return patternSetFactory.createPattern(info, patternSet, index);
    }

    @Override
    public Part createPart(ComponentInfo info, PatternSet patternSet, Machine machine) {
        return patternSetFactory.createPart(info, patternSet, machine);
    }

    //----------------------------------
    // SongSet
    //----------------------------------

    @Override
    public SongSet createSongSet(ComponentInfo info, UUID patternSetId) {
        return songSetFactory.createSongSet(info, patternSetId);
    }

    @Override
    public SongSet createSongSet(ComponentInfo info, PatternSet patternSet) {
        return songSetFactory.createSongSet(info, patternSet);
    }

    @Override
    public Song createSong(ComponentInfo info, SongSet songSet) {
        return songSetFactory.createSong(info, songSet);
    }

    //----------------------------------
    // Master
    //----------------------------------

    @Override
    public MasterSystem createMasterSystem(RackSet rackSet) {
        return masterFactory.createMasterSystem(rackSet);
    }

    @Override
    public MasterMixer createMasterMixer(RackSet caustkScene) {
        return masterFactory.createMasterMixer(caustkScene);
    }

    @Override
    public MasterSequencer createMasterSequencer(RackSet caustkScene) {
        return masterFactory.createMasterSequencer(caustkScene);
    }

    //----------------------------------
    // Tones
    //----------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RackTone> T createRackTone(String machineName, MachineType machineType,
            int machineIndex) throws CausticException {
        RackTone tone = rackToneFactory.createRackTone(machineName, machineType, machineIndex);
        tone.create(createContext());
        return (T)tone;
    }

    @Override
    public RackTone createRackTone(ToneDescriptor descriptor) throws CausticException {
        return rackToneFactory.createRackTone(descriptor);
    }

    @Override
    public RackTone createRackTone(Machine machine, ToneDescriptor descriptor)
            throws CausticException {
        return rackToneFactory.createRackTone(machine, descriptor);
    }

    @Override
    public final String resolvePath(ICaustkComponent component) {
        ComponentInfo info = component.getInfo();

        if (info.getType() == ComponentType.Library) {
            // Library library = (Library)component;
            return null;// library.getManifestFile();
        }

        String name = info.getType().name();
        final StringBuilder sb = new StringBuilder();

        // add the ComponentType sub directory
        sb.append(name);
        sb.append(File.separator);

        // add the specific sub directory after component type
        if (info.getType() == ComponentType.TrackSet) {
            // RackSet uses root
            // RackSet rackSet = (RackSet)component;
        } else if (info.getType() == ComponentType.RackSet) {
            // RackSet uses root
            // RackSet rackSet = (RackSet)component;
        } else if (info.getType() == ComponentType.Machine) {
            // Machine uses MachineType
            Machine machine = (Machine)component;
            sb.append(machine.getMachineType().name());
            sb.append(File.separator);
        } else if (info.getType() == ComponentType.Patch) {
            // Patch uses MachineType
            Patch patch = (Patch)component;
            sb.append(patch.getMachineType().name());
            sb.append(File.separator);
        } else if (info.getType() == ComponentType.Phrase) {
            // Phrase uses MachineType
            Phrase phrase = (Phrase)component;
            sb.append(phrase.getMachineType().name());
            sb.append(File.separator);
        } else if (info.getType() == ComponentType.Effect) {
            // Effect uses EffectType
            Effect effect = (Effect)component;
            sb.append(effect.getEffectType().name());
            sb.append(File.separator);
        }

        if (info.getFile() != null)
            sb.append(info.getFile().getPath());
        else
            sb.append(component.getDefaultName());

        return sb.toString();
    }

    @Override
    public final File resolveLocation(ICaustkComponent component, File rootDirectory) {
        if (component.getInfo().getType() == ComponentType.Library) {
            Library library = (Library)component;
            return library.getManifestFile();
        }
        return new File(rootDirectory, resolvePath(component));
    }

    @Override
    public <T> T copy(T instance) {
        return kryo.copy(instance);
    }

    @Override
    public <T extends ICaustkComponent> T load(File componentFile, Class<T> clazz)
            throws FileNotFoundException {
        if (!componentFile.exists())
            throw new FileNotFoundException("Component file note found: " + componentFile);
        T component = KryoUtils.readFileObject(kryo, componentFile, clazz);
        if (component instanceof IRackSerializer)
            ((IRackSerializer)component).onLoad(createContext());
        return component;
    }

    @Override
    public File save(ICaustkComponent component, File rootDirectory) throws FileNotFoundException {
        if (!rootDirectory.exists())
            throw new FileNotFoundException("rootDirectory does not exist: " + rootDirectory);
        if (component instanceof IRackSerializer)
            ((IRackSerializer)component).onSave(createContext());
        File location = resolveLocation(component, rootDirectory);
        KryoUtils.writeFileObject(kryo, location, component);
        return location;
    }
}
