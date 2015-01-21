
package com.teotigraphix.caustk.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.teotigraphix.caustk.core.midi.ChordReference;
import com.teotigraphix.caustk.core.midi.MidiReference;
import com.teotigraphix.caustk.core.midi.NoteReference;
import com.teotigraphix.caustk.core.midi.ScaleReference;
import com.teotigraphix.caustk.core.osc.BasslineMessage;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ChorusMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DelayMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DistortionProgram;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.FlangerMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.MultiFilterMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.StaticFlangerMode;
import com.teotigraphix.caustk.core.osc.FMSynthMessage;
import com.teotigraphix.caustk.core.osc.FMSynthMessage.FMOperatorControl;
import com.teotigraphix.caustk.core.osc.FilterMessage.FilterType;
import com.teotigraphix.caustk.core.osc.PCMSynthMessage.LFO1Waveform;
import com.teotigraphix.caustk.core.osc.PCMSynthMessage.PlayMode;
import com.teotigraphix.caustk.core.osc.PadSynthMessage.LFO1Target;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.CentsMode;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.LFO2Target;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.ModulationMode;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.Osc1Waveform;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.Osc2Waveform;
import com.teotigraphix.caustk.core.osc.VocoderMessage.CarrierOscWaveform;
import com.teotigraphix.caustk.gdx.app.Project;
import com.teotigraphix.caustk.gdx.app.ProjectProperties;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;
import com.teotigraphix.caustk.groove.library.LibraryPatternBank;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.manifest.LibraryEffectManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryGroupManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryInstrumentManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryPatternBankManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryProductManifest;
import com.teotigraphix.caustk.groove.manifest.LibrarySoundManifest;
import com.teotigraphix.caustk.groove.session.Clip;
import com.teotigraphix.caustk.groove.session.ClipInfo;
import com.teotigraphix.caustk.groove.session.Scene;
import com.teotigraphix.caustk.groove.session.SceneInfo;
import com.teotigraphix.caustk.groove.session.SceneManager;
import com.teotigraphix.caustk.groove.session.SessionManager;
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
import com.teotigraphix.caustk.node.effect.EffectChannel;
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
import com.teotigraphix.caustk.node.machine.KSSynthMachine;
import com.teotigraphix.caustk.node.machine.ModularMachine;
import com.teotigraphix.caustk.node.machine.OrganMachine;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;
import com.teotigraphix.caustk.node.machine.VocoderMachine;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.machine.patch.PresetComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthFilterComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeComponent;
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
import com.teotigraphix.caustk.node.machine.patch.organ.LeslieComponent;
import com.teotigraphix.caustk.node.machine.patch.padsynth.HarmonicsComponent;
import com.teotigraphix.caustk.node.machine.patch.padsynth.LFO1Component;
import com.teotigraphix.caustk.node.machine.patch.padsynth.LFO2Component;
import com.teotigraphix.caustk.node.machine.patch.padsynth.MorphComponent;
import com.teotigraphix.caustk.node.machine.patch.padsynth.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMSamplerChannel;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMSamplerComponent;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMTunerComponent;
import com.teotigraphix.caustk.node.machine.patch.subsynth.Osc1Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.Osc2Component;
import com.teotigraphix.caustk.node.machine.patch.vocoder.ModulatorControlsComponent;
import com.teotigraphix.caustk.node.machine.patch.vocoder.VocoderModulatorComponent;
import com.teotigraphix.caustk.node.machine.sequencer.ClipComponent;
import com.teotigraphix.caustk.node.machine.sequencer.NoteNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent;
import com.teotigraphix.caustk.node.machine.sequencer.TrackComponent;
import com.teotigraphix.caustk.node.machine.sequencer.TrackEntryNode;
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

public final class CaustkSerializerTags {

    public static void register(Kryo kryo) {

        //------------------------------
        // native 0-100
        //------------------------------

        kryo.register(byte[].class, 0);
        kryo.register(boolean[].class, 1);
        kryo.register(int[].class, 2);
        kryo.register(double[].class, 3);
        kryo.register(float[].class, 4);
        kryo.register(float[][].class, 5);
        kryo.register(String[].class, 6);
        kryo.register(Integer[].class, 7);
        kryo.register(Float[].class, 8);
        kryo.register(UUID.class, new UUIDSerializer(), 9);
        kryo.register(ArrayList.class, 10);
        kryo.register(TreeMap.class, 11);
        kryo.register(HashMap.class, 12);
        kryo.register(Date.class, 13);
        kryo.register(File.class, new FileSerializer(), 14);

        //------------------------------
        // core 101 - 300
        //------------------------------

        kryo.register(CausticFile.class, 101);
        kryo.register(Project.class, 102);
        kryo.register(MachineType.class, 103);

        kryo.register(ChordReference.class, 104);
        kryo.register(MidiReference.class, 105);
        kryo.register(NoteReference.class, 106);
        kryo.register(ScaleReference.class, 107);

        kryo.register(NodeMetaData.class, 108);
        kryo.register(RackNode.class, 109);

        // effects
        kryo.register(EffectType.class, 200);
        kryo.register(ChorusMode.class, 201);
        kryo.register(DelayMode.class, 202);
        kryo.register(DistortionProgram.class, 203);
        kryo.register(FlangerMode.class, 204);
        kryo.register(MultiFilterMode.class, 205);
        kryo.register(StaticFlangerMode.class, 206);

        kryo.register(AutoWahEffect.class, 207);
        kryo.register(BitcrusherEffect.class, 208);
        kryo.register(CabinetSimulatorEffect.class, 209);
        kryo.register(ChorusEffect.class, 210);
        kryo.register(CombFilterEffect.class, 211);
        kryo.register(CompressorEffect.class, 212);
        kryo.register(DelayEffect.class, 213);
        kryo.register(DistortionEffect.class, 214);
        kryo.register(FlangerEffect.class, 215);
        kryo.register(LimiterEffect.class, 216);
        kryo.register(MultiFilterEffect.class, 217);
        kryo.register(ParametricEQEffect.class, 218);
        kryo.register(PhaserEffect.class, 219);
        kryo.register(ReverbEffect.class, 220);
        kryo.register(StaticFlangerEffect.class, 221);
        kryo.register(VinylSimulatorEffect.class, 222);

        // Main
        kryo.register(MasterNode.class, 250);
        kryo.register(MasterDelayNode.class, 251);
        kryo.register(MasterReverbNode.class, 252);
        kryo.register(MasterEqualizerNode.class, 253);
        kryo.register(MasterLimiterNode.class, 254);
        kryo.register(MasterVolumeNode.class, 255);

        kryo.register(SequencerNode.class, 256);
        kryo.register(SequencerNode.SequencerMode.class, 257);
        kryo.register(SequencerNode.ShuffleMode.class, 258);
        kryo.register(SequencerNode.SongEndMode.class, 259);

        kryo.register(PatternNode.class, 260);
        kryo.register(PatternNode.Resolution.class, 261);
        kryo.register(PatternNode.ShuffleMode.class, 262);
        kryo.register(NoteNode.class, 263);

        kryo.register(VolumeComponent.class, 264);
        kryo.register(PresetComponent.class, 265);
        kryo.register(SynthComponent.class, 266);
        kryo.register(PatternSequencerComponent.class, 267);
        kryo.register(MixerChannel.class, 268);
        kryo.register(EffectChannel.class, 269);
        kryo.register(TrackComponent.class, 270);
        kryo.register(ClipComponent.class, 271);
        kryo.register(TrackEntryNode.class, 272);

        //------------------------------
        // machines 301-350
        //------------------------------

        kryo.register(BasslineMachine.class, 301);
        kryo.register(BeatBoxMachine.class, 302);
        kryo.register(EightBitSynthMachine.class, 303);
        kryo.register(FMSynthMachine.class, 304);
        kryo.register(KSSynthMachine.class, 305);
        kryo.register(ModularMachine.class, 306);
        kryo.register(OrganMachine.class, 307);
        kryo.register(PadSynthMachine.class, 308);
        kryo.register(PCMSynthMachine.class, 309);
        kryo.register(SubSynthMachine.class, 310);
        kryo.register(VocoderMachine.class, 311);

        //------------------------------
        // Bassline 351-400
        //------------------------------

        kryo.register(DistortionComponent.class, 351);
        kryo.register(BasslineMessage.DistorionProgram.class, 352);
        kryo.register(FilterComponent.class, 353);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.bassline.LFO1Component.class, 354);
        kryo.register(BasslineMessage.LFOTarget.class, 355);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.bassline.Osc1Component.class, 356);
        kryo.register(BasslineMessage.Osc1Waveform.class, 357);

        //------------------------------
        // BeatBox 401-450
        //------------------------------

        kryo.register(WavSamplerComponent.class, 401);
        kryo.register(WavSamplerChannel.class, 402);

        //------------------------------
        // EightBitSynth 451-500
        //------------------------------

        kryo.register(EightBitSynthControlsComponent.class, 451);
        kryo.register(ExpressionComponent.class, 452);

        //------------------------------
        // FMSynth 501-550
        //------------------------------

        kryo.register(FMControlsComponent.class, 501);
        kryo.register(FMSynthMessage.FMAlgorithm.class, 502);
        kryo.register(LFOComponent.class, 503);
        kryo.register(FMOperatorComponent.class, 504);
        kryo.register(FMOperatorControl.class, 505);

        //------------------------------
        // Modular 551-600
        //------------------------------

        kryo.register(ModularBayComponent.class, 551);
        kryo.register(AREnvelope.class, 552);
        kryo.register(AREnvelope.EnvelopeSlope.class, 553);
        kryo.register(Arpeggiator.class, 554);
        kryo.register(Arpeggiator.Sequence.class, 555);
        kryo.register(Crossfader.class, 556);
        kryo.register(CrossoverModule.class, 557);
        kryo.register(DADSREnvelope.class, 558);
        kryo.register(DADSREnvelope.EnvelopeSlope.class, 559);
        kryo.register(DecayEnvelope.class, 560);
        kryo.register(DelayModule.class, 561);
        kryo.register(FMPair.class, 562);
        kryo.register(FormantFilter.class, 563);
        kryo.register(LagProcessor.class, 564);
        kryo.register(MiniLFO.class, 565);
        kryo.register(MiniLFO.WaveForm.class, 566);
        kryo.register(NoiseGenerator.class, 567);
        kryo.register(PanModule.class, 568);
        kryo.register(PulseGenerator.class, 569);
        kryo.register(ResonantLP.class, 570);
        kryo.register(SampleAndHold.class, 571);
        kryo.register(Saturator.class, 572);
        kryo.register(SixInputMixer.class, 573);
        kryo.register(SubOscillator.class, 574);
        kryo.register(SVFilter.class, 575);
        kryo.register(ThreeInputMixer.class, 576);
        kryo.register(TwoInputMixer.class, 577);
        kryo.register(WaveformGenerator.class, 578);
        //kryo.register(ModularVolumeBug.class);

        //------------------------------
        // Organ 601-650
        //------------------------------

        kryo.register(LeslieComponent.class, 601);

        //------------------------------
        // PadSynth 651-700
        //------------------------------

        kryo.register(HarmonicsComponent.class, 651);
        kryo.register(LFO1Component.class, 652);
        kryo.register(LFO1Target.class, 653);
        kryo.register(LFO2Component.class, 654);
        kryo.register(MorphComponent.class, 655);
        kryo.register(VolumeEnvelopeComponent.class, 656);

        //------------------------------
        // PCMSynth 701-750
        //------------------------------

        kryo.register(SynthFilterComponent.class, 701);
        kryo.register(FilterType.class, 702);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.pcmsynth.LFO1Component.class, 703);
        kryo.register(com.teotigraphix.caustk.core.osc.PCMSynthMessage.LFO1Target.class, 704);
        kryo.register(LFO1Waveform.class, 705);
        kryo.register(PCMSamplerComponent.class, 706);
        kryo.register(PCMTunerComponent.class, 707);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.VolumeEnvelopeComponent.class, 708);
        kryo.register(PCMSamplerChannel.class, 709);
        kryo.register(PlayMode.class, 710);

        //------------------------------
        // SubSynth 751-800
        //------------------------------

        kryo.register(com.teotigraphix.caustk.node.machine.patch.subsynth.LFO1Component.class, 751);
        kryo.register(com.teotigraphix.caustk.core.osc.SubSynthMessage.LFO1Target.class, 752);
        kryo.register(com.teotigraphix.caustk.core.osc.SubSynthMessage.LFO1Waveform.class, 753);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.subsynth.LFO2Component.class, 754);
        kryo.register(LFO2Target.class, 755);
        kryo.register(Osc1Component.class, 756);
        kryo.register(ModulationMode.class, 757);
        kryo.register(Osc1Waveform.class, 758);
        kryo.register(Osc2Component.class, 759);
        kryo.register(CentsMode.class, 760);
        kryo.register(Osc2Waveform.class, 761);

        //------------------------------
        // Vocoder 801-850
        //------------------------------

        kryo.register(ModulatorControlsComponent.class, 801);
        kryo.register(CarrierOscWaveform.class, 802);
        kryo.register(VocoderModulatorComponent.class, 803);

        //------------------------------
        // Groove framework 901-1000
        //------------------------------

        kryo.register(LibraryItemFormat.class, 901);

        kryo.register(LibraryEffect.class, 925);
        kryo.register(LibraryEffectManifest.class, 926);
        kryo.register(LibraryInstrument.class, 927);
        kryo.register(LibraryInstrumentManifest.class, 928);
        kryo.register(LibrarySound.class, 929);
        kryo.register(LibrarySoundManifest.class, 930);
        kryo.register(LibraryGroup.class, 931);
        kryo.register(LibraryGroupManifest.class, 932);
        kryo.register(LibraryProduct.class, 933);
        kryo.register(LibraryProductManifest.class, 934);
        kryo.register(LibraryPatternBank.class, 935);
        kryo.register(LibraryPatternBankManifest.class, 936);

        kryo.register(SessionManager.class, 950);
        kryo.register(SceneManager.class, 951);
        kryo.register(Scene.class, 952);
        kryo.register(SceneInfo.class, 953);
        kryo.register(Clip.class, 954);
        kryo.register(Clip.ClipState.class, 955);
        kryo.register(ClipInfo.class, 956);

        kryo.register(ProjectProperties.class, 960);
        //kryo.register(Project.class, 1001);
    }

    public static class UUIDSerializer extends Serializer<UUID> {
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

    public static class FileSerializer extends Serializer<File> {

        @Override
        public File read(Kryo kryo, Input input, Class<File> clazz) {
            return new File(input.readString());
        }

        @Override
        public void write(Kryo kryo, Output output, File file) {
            output.writeString(file.getAbsolutePath());
        }
    }
}
