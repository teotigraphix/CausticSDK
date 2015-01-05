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

import com.teotigraphix.caustk.core.factory.LibraryFactory;
import com.teotigraphix.caustk.core.factory.NodeFactory;
import com.teotigraphix.caustk.node.ICaustkNode;
import com.teotigraphix.caustk.utils.RuntimeUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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

    private NodeFactory nodeFactory;

    private LibraryFactory libraryFactory;

    private File cacheDirectory;

    //--------------------------------------------------------------------------
    //  Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    //  runtime
    //----------------------------------

    @Override
    public final CaustkRuntime getRuntime() {
        return runtime;
    }

    //----------------------------------
    //  factory
    //----------------------------------

    @Override
    public final NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    @Override
    public final LibraryFactory getLibraryFactory() {
        return libraryFactory;
    }

    @Override
    public File getCacheDirectory(String relativePath) {
        return new File(cacheDirectory, relativePath);
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

        nodeFactory = new NodeFactory(this);
        libraryFactory = new LibraryFactory(this);

    }

    @Override
    public void initialize() throws CausticException {
        try {
            cacheDirectory = getTempDirectory("cache", true);
        } catch (IOException e) {
            throw new CausticException("Cache directory could not be created or cleaned");
        }
    }

    /**
     * Returns a sub directory of the <code>.temp</code> directory inside the
     * application.
     * 
     * @param reletivePath The path of the .temp sub directory.
     * @param clean Whether to clean the sub directory if exists.
     * @throws java.io.IOException the .temp or relative directory cannot be
     *             created.
     * @see com.teotigraphix.caustk.utils.RuntimeUtils#getApplicationTempDirectory()
     */
    private File getTempDirectory(String reletivePath, boolean clean) throws IOException {
        File tempDir = RuntimeUtils.getApplicationTempDirectory();
        if (!tempDir.exists())
            FileUtils.forceMkdir(tempDir);
        File directory = new File(tempDir, reletivePath);
        if (!directory.exists())
            FileUtils.forceMkdir(directory);
        if (clean)
            FileUtils.cleanDirectory(directory);
        return directory;
    }

    //--------------------------------------------------------------------------
    // Public Serialization API :: Methods
    //--------------------------------------------------------------------------
    //
    //    @Override
    //    @SuppressWarnings("unchecked")
    //    public <T> T deserialize(String json, Class<? extends Object> clazz) throws CausticException {
    //        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
    //        builder.setExclusionStrategies(new GsonExclusionStrategy(null));
    //        builder.registerTypeAdapter(MachineNode.class, new MachineNodeDeserializer());
    //        builder.registerTypeAdapter(EffectNode.class, new EffectNodeDeserializer());
    //        builder.registerTypeAdapter(NoteNode.class, new NoteNodeDeserializer());
    //        Gson gson = builder.create();
    //        return (T)gson.fromJson(json, clazz);
    //    }
    //
    //    @Override
    //    public String serialize(Object node, boolean prettyPrint) {
    //        GsonBuilder builder = new GsonBuilder();
    //        builder.setExclusionStrategies(new GsonExclusionStrategy(null));
    //        if (prettyPrint)
    //            builder.setPrettyPrinting();
    //        Gson serializer = builder.create();
    //        String json = serializer.toJson(node);
    //        return json;
    //    }
    //
    //    @Override
    //    public String serialize(Object node) {
    //        return serialize(node, false);
    //    }

    //    //--------------------------------------------------------------------------
    //    // Serializers
    //    //--------------------------------------------------------------------------
    //
    //    public class GsonExclusionStrategy implements ExclusionStrategy {
    //
    //        private final Class<?> typeToExclude;
    //
    //        public GsonExclusionStrategy(Class<?> clazz) {
    //            this.typeToExclude = clazz;
    //        }
    //
    //        @Override
    //        public boolean shouldSkipClass(Class<?> clazz) {
    //            return (this.typeToExclude != null && this.typeToExclude == clazz)
    //                    || clazz.getAnnotation(GsonExclude.class) != null;
    //        }
    //
    //        @Override
    //        public boolean shouldSkipField(FieldAttributes f) {
    //            return f.getAnnotation(GsonExclude.class) != null;
    //        }
    //
    //    }
    //
    //    class NoteNodeDeserializer implements JsonDeserializer<NoteNode> {
    //        @Override
    //        public NoteNode deserialize(JsonElement element, Type type,
    //                JsonDeserializationContext context) throws JsonParseException {
    //            JsonObject jsonObject = element.getAsJsonObject();
    //            String noteData = jsonObject.getAsJsonPrimitive("noteData").getAsString();
    //            NoteNode noteNode = new NoteNode(noteData);
    //            noteNode.setSelected(jsonObject.getAsJsonPrimitive("selected").getAsBoolean());
    //            return noteNode;
    //        }
    //    }
    //
    //    class MachineNodeDeserializer implements JsonDeserializer<MachineNode> {
    //        @Override
    //        public MachineNode deserialize(JsonElement element, Type type,
    //                JsonDeserializationContext context) throws JsonParseException {
    //            final JsonObject jsonObject = element.getAsJsonObject();
    //            JsonElement typeElement = jsonObject.get("type");
    //            Class<? extends ICaustkNode> clazz = null;
    //            switch (MachineType.valueOf(typeElement.getAsString())) {
    //                case SubSynth:
    //                    clazz = SubSynthMachine.class;
    //                    break;
    //                case Bassline:
    //                    clazz = BasslineMachine.class;
    //                    break;
    //                case BeatBox:
    //                    clazz = BeatBoxMachine.class;
    //                    break;
    //                case EightBitSynth:
    //                    clazz = EightBitSynthMachine.class;
    //                    break;
    //                case FMSynth:
    //                    clazz = FMSynthMachine.class;
    //                    break;
    //                case Modular:
    //                    clazz = ModularMachine.class;
    //                    break;
    //                case Organ:
    //                    clazz = OrganMachine.class;
    //                    break;
    //                case PCMSynth:
    //                    clazz = PCMSynthMachine.class;
    //                    break;
    //                case PadSynth:
    //                    clazz = PadSynthMachine.class;
    //                    break;
    //                case Vocoder:
    //                    clazz = VocoderMachine.class;
    //                    break;
    //                default:
    //                    break;
    //
    //            }
    //            MachineNode result = null;
    //            try {
    //                result = CaustkFactory.this.deserialize(jsonObject.toString(), clazz);
    //            } catch (CausticException e) {
    //                // TODO log err()
    //                e.printStackTrace();
    //            }
    //            return result;
    //        }
    //    }
    //
    //    class EffectNodeDeserializer implements JsonDeserializer<EffectNode> {
    //        @Override
    //        public EffectNode deserialize(JsonElement element, Type type,
    //                JsonDeserializationContext context) throws JsonParseException {
    //            final JsonObject jsonObject = element.getAsJsonObject();
    //            JsonElement typeElement = jsonObject.get("type");
    //            Class<? extends EffectNode> clazz = null;
    //            switch (EffectType.valueOf(typeElement.getAsString())) {
    //                case Autowah:
    //                    clazz = AutoWahEffect.class;
    //                    break;
    //                case Bitcrusher:
    //                    clazz = BitcrusherEffect.class;
    //                    break;
    //                case CabinetSimulator:
    //                    clazz = CabinetSimulatorEffect.class;
    //                    break;
    //                case Chorus:
    //                    clazz = ChorusEffect.class;
    //                    break;
    //                case CombFilter:
    //                    clazz = CombFilterEffect.class;
    //                    break;
    //                case Compressor:
    //                    clazz = CompressorEffect.class;
    //                    break;
    //                case Delay:
    //                    clazz = DelayEffect.class;
    //                    break;
    //                case Distortion:
    //                    clazz = DistortionEffect.class;
    //                    break;
    //                case Flanger:
    //                    clazz = FlangerEffect.class;
    //                    break;
    //                case Limiter:
    //                    clazz = LimiterEffect.class;
    //                    break;
    //                case MultiFilter:
    //                    clazz = MultiFilterEffect.class;
    //                    break;
    //                case ParametricEQ:
    //                    clazz = ParametricEQEffect.class;
    //                    break;
    //                case Phaser:
    //                    clazz = PhaserEffect.class;
    //                    break;
    //                case Reverb:
    //                    clazz = ReverbEffect.class;
    //                    break;
    //                case StaticFlanger:
    //                    clazz = StaticFlangerEffect.class;
    //                    break;
    //                case VinylSimulator:
    //                    clazz = VinylSimulatorEffect.class;
    //                    break;
    //
    //            }
    //            EffectNode result = null;
    //            try {
    //                result = CaustkFactory.this.deserialize(jsonObject.toString(), clazz);
    //            } catch (CausticException e) {
    //                // TODO log err()
    //                e.printStackTrace();
    //            }
    //            return result;
    //        }
    //    }
}
