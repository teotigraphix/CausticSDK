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

package com.teotigraphix.caustk.core;

import java.io.File;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryItemFactory;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.manifest.LibraryEffectManifest;
import com.teotigraphix.caustk.node.ICaustkNode;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.AutoWahEffect;
import com.teotigraphix.caustk.node.effect.BitcrusherEffect;
import com.teotigraphix.caustk.node.effect.CabinetSimulatorEffect;
import com.teotigraphix.caustk.node.effect.ChorusEffect;
import com.teotigraphix.caustk.node.effect.CombFilterEffect;
import com.teotigraphix.caustk.node.effect.CompressorEffect;
import com.teotigraphix.caustk.node.effect.DelayEffect;
import com.teotigraphix.caustk.node.effect.DistortionEffect;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectNodeFactory;
import com.teotigraphix.caustk.node.effect.EffectType;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.effect.FlangerEffect;
import com.teotigraphix.caustk.node.effect.LimiterEffect;
import com.teotigraphix.caustk.node.effect.MultiFilterEffect;
import com.teotigraphix.caustk.node.effect.ParametricEQEffect;
import com.teotigraphix.caustk.node.effect.PhaserEffect;
import com.teotigraphix.caustk.node.effect.ReverbEffect;
import com.teotigraphix.caustk.node.effect.StaticFlangerEffect;
import com.teotigraphix.caustk.node.effect.VinylSimulatorEffect;
import com.teotigraphix.caustk.node.machine.BasslineMachine;
import com.teotigraphix.caustk.node.machine.BeatBoxMachine;
import com.teotigraphix.caustk.node.machine.EightBitSynthMachine;
import com.teotigraphix.caustk.node.machine.FMSynthMachine;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.MachineNodeFactory;
import com.teotigraphix.caustk.node.machine.ModularMachine;
import com.teotigraphix.caustk.node.machine.OrganMachine;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;
import com.teotigraphix.caustk.node.machine.VocoderMachine;
import com.teotigraphix.caustk.node.machine.sequencer.NoteNode;

/**
 * A factory that creates {@link ICaustkNode}s for the {@link CaustkRuntime}.
 * <p>
 * Holds the serialization contracts for the {@link ICaustkNode} framework.
 * <p>
 * There is a one to one relationship between the {@link CaustkFactory} and the
 * {@link CaustkRuntime}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CaustkFactory implements ICaustkFactory {

    private CaustkRuntime runtime;

    private EffectNodeFactory effectNodeFactory;

    private MachineNodeFactory machineNodeFactory;

    private LibraryItemFactory libraryItemFactory;

    //--------------------------------------------------------------------------
    //  Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    //  factory
    //----------------------------------

    /**
     * The {@link CaustkRuntime} that owns this factory.
     */
    public final CaustkRuntime getRuntime() {
        return runtime;
    }

    //--------------------------------------------------------------------------
    //  Groove Library
    //--------------------------------------------------------------------------

    public LibraryEffect createLibraryEffect(LibraryProduct product, String displayName,
            File archiveFile, String relativePath, EffectNode efffect0, EffectNode efffect1) {
        LibraryEffectManifest manifest = new LibraryEffectManifest(displayName, archiveFile,
                relativePath, efffect0, efffect1);
        LibraryEffect libraryEffect = new LibraryEffect(product.getId(), manifest);
        libraryEffect.add(0, efffect0);
        libraryEffect.add(1, efffect1);

        return libraryItemFactory.createLibraryEffect(product, displayName, archiveFile,
                relativePath, efffect0, efffect1);
    }

    //--------------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------------

    /**
     * Creates a {@link CaustkFactory} that is exclusively used with the
     * {@link CaustkRuntime}.
     * 
     * @param runtime The runtime that owns this factory.
     */
    public CaustkFactory(CaustkRuntime runtime) {
        this.runtime = runtime;

        effectNodeFactory = new EffectNodeFactory(this);
        machineNodeFactory = new MachineNodeFactory(this);

        libraryItemFactory = new LibraryItemFactory(this);
    }

    //----------------------------------
    // NodeInfo
    //----------------------------------

    //    public NodeInfo createInfo(NodeType nodeType) {
    //        return nodeInfoFactory.createInfo(nodeType);
    //    }
    //
    //    public NodeInfo createInfo(NodeType nodeType, String name) {
    //        return nodeInfoFactory.createInfo(nodeType, name);
    //    }
    //
    //    public NodeInfo createInfo(NodeType nodeType, String relativePath, String name) {
    //        return nodeInfoFactory.createInfo(nodeType, relativePath, name);
    //    }
    //
    //    public NodeInfo createInfo(NodeType nodeType, File relativePath, String name) {
    //        return nodeInfoFactory.createInfo(nodeType, relativePath, name);
    //    }

    //    //----------------------------------
    //    // Library
    //    //----------------------------------
    //
    //    /**
    //     * Creates an empty {@link Library} with a name.
    //     * <p>
    //     * The name is used for the directory name held within the
    //     * <code>/storageRoot/AppName/libraries</code> directory.
    //     * 
    //     * @param name The name of the library, used as the directory name.
    //     */
    //    public Library createLibrary(String name) {
    //        return libraryFactory.createLibrary(name);
    //    }
    //
    //    /**
    //     * @param reletiveOrAbsDirectory
    //     * @return
    //     */
    //    public Library createLibrary(File reletiveOrAbsDirectory) {
    //        return libraryFactory.createLibrary(reletiveOrAbsDirectory);
    //    }
    //
    //    public Library loadLibrary(File reletiveOrAbsDirectory) throws IOException {
    //        return libraryFactory.loadLibrary(reletiveOrAbsDirectory);
    //    }

    //----------------------------------
    // RackNode
    //----------------------------------

    /**
     * Creates and returns a new initialized {@link RackNode}.
     */
    public RackNode createRack() {
        RackNode rackNode = new RackNode();
        return rackNode;
    }

    /**
     * Creates and returns a {@link RackNode} that wraps a <code>.caustic</code>
     * file.
     * 
     * @param relativeOrAbsolutePath The relative or absolute
     *            <code>.caustic</code> file location.
     */
    public RackNode createRack(String relativeOrAbsolutePath) {
        RackNode rackNode = new RackNode(relativeOrAbsolutePath);
        return rackNode;
    }

    /**
     * Creates and returns a {@link RackNode} that wraps a <code>.caustic</code>
     * file.
     * 
     * @param file The <code>.caustic</code> file location.
     */
    public RackNode createRack(File file) {
        RackNode rackNode = new RackNode(file);
        return rackNode;
    }

    //----------------------------------
    // EffectNode
    //----------------------------------

    /**
     * Creates an {@link EffectNode} subclass using the {@link EffectType}.
     * 
     * @param machineIndex The machine index of the new effect.
     * @param slot The effect slot within the {@link EffectsChannel}.
     * @param effectType The {@link EffectType} of the effect to be created.
     * @return A new {@link EffectNode}, has not been added to an
     *         {@link EffectsChannel}.
     */
    public <T extends EffectNode> T createEffect(int machineIndex, int slot, EffectType effectType) {
        return effectNodeFactory.createEffect(machineIndex, slot, effectType);
    }

    //----------------------------------
    // MachineNode
    //----------------------------------

    /**
     * Creates a {@link MachineNode} subclass using the {@link MachineType}.
     * 
     * @param index The machine index lost in the native rack.
     * @param type The {@link MachineType} of machine to create.
     * @param name The machine name (10 character alpha numeric).
     * @return A new {@link MachineNode}, added to the native rack.
     */

    public <T extends MachineNode> T createMachine(int index, MachineType type, String name) {
        return machineNodeFactory.createMachine(index, type, name);
    }

    //--------------------------------------------------------------------------
    // Public Serialization API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Deserializes a JSON String into a {@link ICaustkNode}.
     * 
     * @param json The valid JSON formated serial String.
     * @param clazz The {@link ICaustkNode} implementation result.
     * @return A new instance of the {@link ICaustkNode} deserialized.
     * @throws CausticException Serialization exception.
     */
    @SuppressWarnings("unchecked")
    public <T extends ICaustkNode> T deserialize(String json, Class<? extends ICaustkNode> clazz)
            throws CausticException {
        GsonBuilder deserializer = new GsonBuilder().setPrettyPrinting();
        deserializer.registerTypeAdapter(MachineNode.class, new MachineNodeDeserializer());
        deserializer.registerTypeAdapter(EffectNode.class, new EffectNodeDeserializer());
        deserializer.registerTypeAdapter(NoteNode.class, new NoteNodeDeserializer());
        Gson gson = deserializer.create();
        return (T)gson.fromJson(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T _deserialize(String json, Class<? extends Object> clazz) throws CausticException {
        GsonBuilder deserializer = new GsonBuilder().setPrettyPrinting();
        deserializer.registerTypeAdapter(MachineNode.class, new MachineNodeDeserializer());
        deserializer.registerTypeAdapter(EffectNode.class, new EffectNodeDeserializer());
        deserializer.registerTypeAdapter(NoteNode.class, new NoteNodeDeserializer());
        Gson gson = deserializer.create();
        return (T)gson.fromJson(json, clazz);
    }

    /**
     * Serializes an {@link ICaustkNode} into a JSON String.
     * 
     * @param node The {@link ICaustkNode} to serialize.
     * @param prettyPrint Whether to pretty print for debugging.
     * @return A serialized JSON String of the {@link ICaustkNode}.
     */
    public String serialize(ICaustkNode node, boolean prettyPrint) {
        GsonBuilder builder = new GsonBuilder();
        if (prettyPrint)
            builder.setPrettyPrinting();
        Gson serializer = builder.create();
        String json = serializer.toJson(node);
        return json;
    }

    public String serialize(Object node, boolean prettyPrint) {
        GsonBuilder builder = new GsonBuilder();
        if (prettyPrint)
            builder.setPrettyPrinting();
        Gson serializer = builder.create();
        String json = serializer.toJson(node);
        return json;
    }

    /**
     * Serializes an {@link ICaustkNode} into a JSON String.
     * 
     * @param node The {@link ICaustkNode} to serialize.
     * @return A serialized JSON String of the {@link ICaustkNode}.
     */
    public String serialize(ICaustkNode node) {
        return serialize(node, false);
    }

    //--------------------------------------------------------------------------
    // Serializers
    //--------------------------------------------------------------------------

    class NoteNodeDeserializer implements JsonDeserializer<NoteNode> {
        @Override
        public NoteNode deserialize(JsonElement element, Type type,
                JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = element.getAsJsonObject();
            String noteData = jsonObject.getAsJsonPrimitive("noteData").getAsString();
            NoteNode noteNode = new NoteNode(noteData);
            noteNode.setSelected(jsonObject.getAsJsonPrimitive("selected").getAsBoolean());
            return noteNode;
        }
    }

    class MachineNodeDeserializer implements JsonDeserializer<MachineNode> {
        @Override
        public MachineNode deserialize(JsonElement element, Type type,
                JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = element.getAsJsonObject();
            JsonElement typeElement = jsonObject.get("type");
            Class<? extends ICaustkNode> clazz = null;
            switch (MachineType.valueOf(typeElement.getAsString())) {
                case SubSynth:
                    clazz = SubSynthMachine.class;
                    break;
                case Bassline:
                    clazz = BasslineMachine.class;
                    break;
                case BeatBox:
                    clazz = BeatBoxMachine.class;
                    break;
                case EightBitSynth:
                    clazz = EightBitSynthMachine.class;
                    break;
                case FMSynth:
                    clazz = FMSynthMachine.class;
                    break;
                case Modular:
                    clazz = ModularMachine.class;
                    break;
                case Organ:
                    clazz = OrganMachine.class;
                    break;
                case PCMSynth:
                    clazz = PCMSynthMachine.class;
                    break;
                case PadSynth:
                    clazz = PadSynthMachine.class;
                    break;
                case Vocoder:
                    clazz = VocoderMachine.class;
                    break;
                default:
                    break;

            }
            MachineNode result = null;
            try {
                result = CaustkFactory.this.deserialize(jsonObject.toString(), clazz);
            } catch (CausticException e) {
                // TODO log err()
                e.printStackTrace();
            }
            return result;
        }
    }

    class EffectNodeDeserializer implements JsonDeserializer<EffectNode> {
        @Override
        public EffectNode deserialize(JsonElement element, Type type,
                JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = element.getAsJsonObject();
            JsonElement typeElement = jsonObject.get("type");
            Class<? extends EffectNode> clazz = null;
            switch (EffectType.valueOf(typeElement.getAsString())) {
                case Autowah:
                    clazz = AutoWahEffect.class;
                    break;
                case Bitcrusher:
                    clazz = BitcrusherEffect.class;
                    break;
                case CabinetSimulator:
                    clazz = CabinetSimulatorEffect.class;
                    break;
                case Chorus:
                    clazz = ChorusEffect.class;
                    break;
                case CombFilter:
                    clazz = CombFilterEffect.class;
                    break;
                case Compressor:
                    clazz = CompressorEffect.class;
                    break;
                case Delay:
                    clazz = DelayEffect.class;
                    break;
                case Distortion:
                    clazz = DistortionEffect.class;
                    break;
                case Flanger:
                    clazz = FlangerEffect.class;
                    break;
                case Limiter:
                    clazz = LimiterEffect.class;
                    break;
                case MultiFilter:
                    clazz = MultiFilterEffect.class;
                    break;
                case ParametricEQ:
                    clazz = ParametricEQEffect.class;
                    break;
                case Phaser:
                    clazz = PhaserEffect.class;
                    break;
                case Reverb:
                    clazz = ReverbEffect.class;
                    break;
                case StaticFlanger:
                    clazz = StaticFlangerEffect.class;
                    break;
                case VinylSimulator:
                    clazz = VinylSimulatorEffect.class;
                    break;

            }
            EffectNode result = null;
            try {
                result = CaustkFactory.this.deserialize(jsonObject.toString(), clazz);
            } catch (CausticException e) {
                // TODO log err()
                e.printStackTrace();
            }
            return result;
        }
    }

}
