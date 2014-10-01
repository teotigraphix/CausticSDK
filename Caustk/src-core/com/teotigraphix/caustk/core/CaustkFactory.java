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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryItemFactory;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.manifest.LibraryGroupManifest;
import com.teotigraphix.caustk.groove.manifest.LibrarySoundManifest;
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
 * A factory that creates {@link ICaustkNode}s for the {@link ICaustkRuntime}.
 * <p>
 * Holds the serialization contracts for the {@link ICaustkNode} framework.
 * <p>
 * There is a one to one relationship between the {@link ICaustkFactory} and the
 * {@link ICaustkRuntime}.
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
    @Override
    public final CaustkRuntime getRuntime() {
        return runtime;
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

    //--------------------------------------------------------------------------
    // Public Rack Creation API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public RackNode createRack() {
        RackNode rackNode = new RackNode();
        return rackNode;
    }

    @Override
    public RackNode createRack(String relativeOrAbsolutePath) {
        RackNode rackNode = new RackNode(relativeOrAbsolutePath);
        return rackNode;
    }

    @Override
    public RackNode createRack(File file) {
        RackNode rackNode = new RackNode(file);
        return rackNode;
    }

    @Override
    public <T extends EffectNode> T createEffect(MachineNode machineNode, int slot,
            EffectType effectType) {
        return effectNodeFactory.createEffect(machineNode, slot, effectType);
    }

    @Override
    public <T extends MachineNode> T createMachine(RackNode rackNode, int index, MachineType type,
            String name) {
        return machineNodeFactory.createMachine(rackNode, index, type, name);
    }

    //--------------------------------------------------------------------------
    // Public Groove Library Creation API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public LibraryEffect createLibraryEffect(LibraryProduct product, String name,
            String relativePath, EffectNode efffect0, EffectNode efffect1) {
        return libraryItemFactory.createLibraryEffect(product, name, relativePath, efffect0,
                efffect1);
    }

    @Override
    public LibraryInstrument createLibraryInstrument(LibraryProduct product, String name,
            String relativePath, MachineNode machineNode) {
        return libraryItemFactory.createInstrument(product, name, relativePath, machineNode);
    }

    @Override
    public LibraryGroup createLibraryGroup(LibraryProduct product, String name, String relativePath) {

        LibraryGroupManifest manifest = new LibraryGroupManifest(name, relativePath);
        manifest.setProductId(product.getId());

        LibraryGroup libraryGroup = new LibraryGroup(manifest);

        return libraryGroup;
    }

    @Override
    public LibrarySound createLibrarySound(LibraryProduct product, String name, String relativePath) {
        LibrarySoundManifest manifest = new LibrarySoundManifest(name, relativePath);
        LibrarySound librarySound = new LibrarySound(manifest);
        return librarySound;
    }

    //--------------------------------------------------------------------------
    // Public Serialization API :: Methods
    //--------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(String json, Class<? extends Object> clazz) throws CausticException {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        builder.setExclusionStrategies(new GsonExclusionStrategy(null));
        builder.registerTypeAdapter(MachineNode.class, new MachineNodeDeserializer());
        builder.registerTypeAdapter(EffectNode.class, new EffectNodeDeserializer());
        builder.registerTypeAdapter(NoteNode.class, new NoteNodeDeserializer());
        Gson gson = builder.create();
        return (T)gson.fromJson(json, clazz);
    }

    @Override
    public String serialize(Object node, boolean prettyPrint) {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new GsonExclusionStrategy(null));
        if (prettyPrint)
            builder.setPrettyPrinting();
        Gson serializer = builder.create();
        String json = serializer.toJson(node);
        return json;
    }

    @Override
    public String serialize(Object node) {
        return serialize(node, false);
    }

    //--------------------------------------------------------------------------
    // Serializers
    //--------------------------------------------------------------------------

    public class GsonExclusionStrategy implements ExclusionStrategy {

        private final Class<?> typeToExclude;

        public GsonExclusionStrategy(Class<?> clazz) {
            this.typeToExclude = clazz;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return (this.typeToExclude != null && this.typeToExclude == clazz)
                    || clazz.getAnnotation(GsonExclude.class) != null;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(GsonExclude.class) != null;
        }

    }

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
