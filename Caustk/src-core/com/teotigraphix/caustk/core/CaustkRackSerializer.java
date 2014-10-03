
package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.teotigraphix.caustk.core.midi.ChordReference;
import com.teotigraphix.caustk.core.midi.MidiReference;
import com.teotigraphix.caustk.core.midi.NoteReference;
import com.teotigraphix.caustk.core.midi.ScaleReference;
import com.teotigraphix.caustk.core.osc.BasslineMessage;
import com.teotigraphix.caustk.core.osc.BeatboxMessage;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ChorusMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DelayMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DistortionProgram;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.FlangerMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.MultiFilterMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.StaticFlangerMode;
import com.teotigraphix.caustk.core.osc.EightBitSynthMessage;
import com.teotigraphix.caustk.core.osc.FMSynthMessage;
import com.teotigraphix.caustk.core.osc.FMSynthMessage.FMAlgorithm;
import com.teotigraphix.caustk.core.osc.FMSynthMessage.FMOperatorControl;
import com.teotigraphix.caustk.core.osc.FilterMessage;
import com.teotigraphix.caustk.core.osc.ModularMessage;
import com.teotigraphix.caustk.core.osc.SubSynthMessage;
import com.teotigraphix.caustk.node.NodeMetaData;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.AutoWahEffect;
import com.teotigraphix.caustk.node.effect.BitcrusherEffect;
import com.teotigraphix.caustk.node.effect.CabinetSimulatorEffect;
import com.teotigraphix.caustk.node.effect.ChorusEffect;
import com.teotigraphix.caustk.node.effect.CombFilterEffect;
import com.teotigraphix.caustk.node.effect.CompressorEffect;
import com.teotigraphix.caustk.node.effect.DelayEffect;
import com.teotigraphix.caustk.node.effect.DistortionEffect;
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
import com.teotigraphix.caustk.node.machine.KSSynthMachine;
import com.teotigraphix.caustk.node.machine.ModularMachine;
import com.teotigraphix.caustk.node.machine.OrganMachine;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;
import com.teotigraphix.caustk.node.machine.VocoderMachine;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.machine.patch.PresetComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthFilterComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.node.machine.patch.bassline.DistortionComponent;
import com.teotigraphix.caustk.node.machine.patch.bassline.FilterComponent;
import com.teotigraphix.caustk.node.machine.patch.beatbox.WavSamplerChannel;
import com.teotigraphix.caustk.node.machine.patch.beatbox.WavSamplerComponent;
import com.teotigraphix.caustk.node.machine.patch.eightbitsynth.EightBitSynthControlsComponent;
import com.teotigraphix.caustk.node.machine.patch.eightbitsynth.ExpressionComponent;
import com.teotigraphix.caustk.node.machine.patch.fmsynth.FMControlsComponent;
import com.teotigraphix.caustk.node.machine.patch.fmsynth.FMOperatorComponent;
import com.teotigraphix.caustk.node.machine.patch.fmsynth.LFOComponent;
import com.teotigraphix.caustk.node.machine.patch.modular.AREnvelope;
import com.teotigraphix.caustk.node.machine.patch.modular.Arpeggiator;
import com.teotigraphix.caustk.node.machine.patch.modular.Crossfader;
import com.teotigraphix.caustk.node.machine.patch.modular.CrossoverModule;
import com.teotigraphix.caustk.node.machine.patch.modular.DADSREnvelope;
import com.teotigraphix.caustk.node.machine.patch.modular.DecayEnvelope;
import com.teotigraphix.caustk.node.machine.patch.modular.DelayModule;
import com.teotigraphix.caustk.node.machine.patch.modular.FMPair;
import com.teotigraphix.caustk.node.machine.patch.modular.FormantFilter;
import com.teotigraphix.caustk.node.machine.patch.modular.LagProcessor;
import com.teotigraphix.caustk.node.machine.patch.modular.MiniLFO;
import com.teotigraphix.caustk.node.machine.patch.modular.ModularBayComponent;
import com.teotigraphix.caustk.node.machine.patch.modular.NoiseGenerator;
import com.teotigraphix.caustk.node.machine.patch.modular.PanModule;
import com.teotigraphix.caustk.node.machine.patch.modular.PulseGenerator;
import com.teotigraphix.caustk.node.machine.patch.modular.ResonantLP;
import com.teotigraphix.caustk.node.machine.patch.modular.SVFilter;
import com.teotigraphix.caustk.node.machine.patch.modular.SampleAndHold;
import com.teotigraphix.caustk.node.machine.patch.modular.Saturator;
import com.teotigraphix.caustk.node.machine.patch.modular.SixInputMixer;
import com.teotigraphix.caustk.node.machine.patch.modular.SubOscillator;
import com.teotigraphix.caustk.node.machine.patch.modular.ThreeInputMixer;
import com.teotigraphix.caustk.node.machine.patch.modular.TwoInputMixer;
import com.teotigraphix.caustk.node.machine.patch.modular.WaveformGenerator;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMSamplerChannel;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMSamplerComponent;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMTunerComponent;
import com.teotigraphix.caustk.node.machine.patch.subsynth.LFO1Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.LFO2Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.Osc1Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.Osc2Component;
import com.teotigraphix.caustk.node.machine.sequencer.ClipComponent;
import com.teotigraphix.caustk.node.machine.sequencer.NoteNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent;
import com.teotigraphix.caustk.node.machine.sequencer.TrackComponent;
import com.teotigraphix.caustk.node.master.MasterDelayNode;
import com.teotigraphix.caustk.node.master.MasterEqualizerNode;
import com.teotigraphix.caustk.node.master.MasterLimiterNode;
import com.teotigraphix.caustk.node.master.MasterNode;
import com.teotigraphix.caustk.node.master.MasterReverbNode;
import com.teotigraphix.caustk.node.master.MasterVolumeNode;
import com.teotigraphix.caustk.node.sequencer.SequencerNode;

/*
TaggedFieldSerializer only serializes fields that have a @Tag annotation. 
This is less flexible than FieldSerializer, which can handle most classes 
without needing annotations, but allows TaggedFieldSerializer to support 
adding new fields without invalidating previously serialized bytes. If a 
field is removed it will invalidate previously serialized bytes, so fields 
should be annotated with @Deprecated instead of being removed.
*/

public class CaustkRackSerializer implements ICaustkRackSerializer {

    private Kryo kryo;

    @Override
    public Kryo getKryo() {
        return kryo;
    }

    CaustkRackSerializer() {

        kryo = new Kryo();

        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        kryo.setRegistrationRequired(true);

        // native 0-50
        kryo.register(byte[].class, 0);
        kryo.register(boolean[].class, 1);
        kryo.register(UUID.class, new UUIDSerializer(), 2);
        kryo.register(ArrayList.class, 3);
        kryo.register(TreeMap.class, 4);
        kryo.register(HashMap.class, 5);

        // core 51 - 100
        kryo.register(CausticFile.class, 51);
        kryo.register(CaustkProject.class, 52);
        kryo.register(MachineType.class, 53);

        kryo.register(ChordReference.class, 54);
        kryo.register(MidiReference.class, 55);
        kryo.register(NoteReference.class, 56);
        kryo.register(ScaleReference.class, 57);

        // 101-500 node
        kryo.register(NodeMetaData.class, 101);
        kryo.register(RackNode.class, 102);

        // node/effects 120-200
        kryo.register(EffectType.class, 120);
        kryo.register(ChorusMode.class, 121);
        kryo.register(DelayMode.class, 122);
        kryo.register(DistortionProgram.class, 123);
        kryo.register(FlangerMode.class, 124);
        kryo.register(MultiFilterMode.class, 125);
        kryo.register(StaticFlangerMode.class, 126);

        kryo.register(AutoWahEffect.class, 140);
        kryo.register(BitcrusherEffect.class, 141);
        kryo.register(CabinetSimulatorEffect.class, 142);
        kryo.register(ChorusEffect.class, 143);
        kryo.register(CombFilterEffect.class, 144);
        kryo.register(CompressorEffect.class, 145);
        kryo.register(DelayEffect.class, 156);
        kryo.register(DistortionEffect.class, 157);
        kryo.register(FlangerEffect.class, 148);
        kryo.register(LimiterEffect.class, 149);
        kryo.register(MultiFilterEffect.class, 150);
        kryo.register(ParametricEQEffect.class, 151);
        kryo.register(PhaserEffect.class, 152);
        kryo.register(ReverbEffect.class, 153);
        kryo.register(StaticFlangerEffect.class, 154);
        kryo.register(VinylSimulatorEffect.class, 155);

        // node/machine 200-220
        kryo.register(BasslineMachine.class, 200);
        kryo.register(BeatBoxMachine.class, 201);
        kryo.register(EightBitSynthMachine.class, 202);
        kryo.register(FMSynthMachine.class, 203);
        kryo.register(KSSynthMachine.class, 204);
        kryo.register(ModularMachine.class, 205);
        kryo.register(OrganMachine.class, 206);
        kryo.register(PCMSynthMachine.class, 207);
        kryo.register(SubSynthMachine.class, 208);
        kryo.register(VocoderMachine.class, 209);

        // node/machine 200
        // node/machine/patch/bassline 200-220
        kryo.register(BasslineMachine.class);
        kryo.register(DistortionComponent.class);
        kryo.register(BasslineMessage.DistorionProgram.class);
        kryo.register(FilterComponent.class);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.bassline.LFO1Component.class);
        kryo.register(BasslineMessage.LFOTarget.class);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.bassline.Osc1Component.class);
        kryo.register(BasslineMessage.Osc1Waveform.class);

        // node/machine/patch/beatbox 221-240
        kryo.register(WavSamplerComponent.class);
        kryo.register(WavSamplerChannel.class);
        kryo.register(BeatboxMessage.class);

        // node/machine/patch/eightbitsynth 241-260
        //kryo.register(EightBitSynthMachine.class);
        kryo.register(ExpressionComponent.class);
        kryo.register(EightBitSynthControlsComponent.class);
        kryo.register(EightBitSynthMessage.class);

        // node/machine/patch/fmsynth 261-280
        kryo.register(FMSynthMachine.class);
        kryo.register(FMControlsComponent.class);
        kryo.register(LFOComponent.class);
        kryo.register(FMSynthMessage.FMAlgorithm.class);
        kryo.register(FMSynthMessage.FMOperatorControl.class);

        // node/machine/patch/modular 280-320
        kryo.register(ModularMachine.class);
        kryo.register(ModularBayComponent.class);
        kryo.register(AREnvelope.class);
        kryo.register(Arpeggiator.class);
        kryo.register(Crossfader.class);
        kryo.register(CrossoverModule.class);
        kryo.register(DADSREnvelope.class);
        kryo.register(DecayEnvelope.class);
        kryo.register(DelayModule.class);
        kryo.register(FMPair.class);
        kryo.register(FormantFilter.class);
        kryo.register(LagProcessor.class);
        kryo.register(MiniLFO.class);
        kryo.register(NoiseGenerator.class);
        kryo.register(PanModule.class);
        kryo.register(PulseGenerator.class);
        kryo.register(ResonantLP.class);
        kryo.register(SampleAndHold.class);
        kryo.register(Saturator.class);
        kryo.register(SixInputMixer.class);
        kryo.register(SubOscillator.class);
        kryo.register(SVFilter.class);
        kryo.register(ThreeInputMixer.class);
        kryo.register(TwoInputMixer.class);
        kryo.register(WaveformGenerator.class);
        kryo.register(ModularMessage.class);
        kryo.register(ModularMessage.ModularComponentType.class);

        // node/machine/patch/modular 280-320

        kryo.register(MasterNode.class);
        kryo.register(MasterDelayNode.class);
        kryo.register(MasterReverbNode.class);
        kryo.register(MasterEqualizerNode.class);
        kryo.register(MasterLimiterNode.class);
        kryo.register(MasterVolumeNode.class);

        kryo.register(SequencerNode.class);
        kryo.register(SequencerNode.ExportLoopMode.class);
        kryo.register(SequencerNode.ExportType.class);
        kryo.register(SequencerNode.SequencerMode.class);
        kryo.register(SequencerNode.ShuffleMode.class);
        kryo.register(SequencerNode.SongEndMode.class);

        kryo.register(PatternNode.class);
        kryo.register(PatternNode.Resolution.class);
        kryo.register(PatternNode.ShuffleMode.class);
        kryo.register(NoteNode.class);

        kryo.register(VolumeComponent.class);
        kryo.register(PresetComponent.class);
        kryo.register(SynthComponent.class);
        kryo.register(PatternSequencerComponent.class);
        kryo.register(MixerChannel.class);
        kryo.register(EffectsChannel.class);
        kryo.register(TrackComponent.class);
        kryo.register(ClipComponent.class);

        // SubSynthMachine
        kryo.register(SubSynthMachine.class);

        kryo.register(SynthFilterComponent.class);
        kryo.register(LFO1Component.class);
        kryo.register(LFO2Component.class);
        kryo.register(Osc1Component.class);
        kryo.register(Osc2Component.class);
        kryo.register(VolumeEnvelopeComponent.class);

        kryo.register(FilterMessage.FilterType.class);

        kryo.register(SubSynthMessage.LFO1Target.class);
        kryo.register(SubSynthMessage.LFO1Waveform.class);
        kryo.register(SubSynthMessage.LFO2Target.class);

        kryo.register(SubSynthMessage.ModulationMode.class);
        kryo.register(SubSynthMessage.Osc1Waveform.class);
        kryo.register(SubSynthMessage.Osc2Waveform.class);
        kryo.register(SubSynthMessage.CentsMode.class);

        // FMSynthMachine
        kryo.register(FMSynthMachine.class);

        kryo.register(FMAlgorithm.class);
        kryo.register(FMOperatorControl.class);

        kryo.register(FMControlsComponent.class);
        kryo.register(LFOComponent.class);
        kryo.register(FMOperatorComponent.class);

        // PCMSynthMachine
        kryo.register(PCMSynthMachine.class);

        kryo.register(PCMSamplerChannel.class);

        kryo.register(VolumeEnvelopeComponent.class);
        kryo.register(SynthFilterComponent.class);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.pcmsynth.LFO1Component.class);
        kryo.register(PCMSamplerComponent.class);
        kryo.register(PCMTunerComponent.class);

        // PCMSynthMachine

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

    public class UUIDSerializer extends Serializer<UUID> {
        public UUIDSerializer() {
            setImmutable(true);
        }

        @Override
        public void write(final Kryo kryo, final Output output, final UUID uuid) {
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
        }

        @Override
        public UUID read(final Kryo kryo, final Input input, final Class<UUID> uuidClass) {
            return new UUID(input.readLong(), input.readLong());
        }
    }
}
