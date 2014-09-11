
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
import com.teotigraphix.caustk.core.osc.FilterMessage;
import com.teotigraphix.caustk.core.osc.SubSynthMessage;
import com.teotigraphix.caustk.node.NodeMetaData;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.machine.patch.PresetComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthFilterComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeEnvelopeComponent;
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

        kryo.register(byte[].class);
        kryo.register(UUID.class, new UUIDSerializer());
        kryo.register(ArrayList.class);
        kryo.register(TreeMap.class);
        kryo.register(HashMap.class);

        kryo.register(CaustkProject.class);

        kryo.register(MachineType.class);
        kryo.register(NodeMetaData.class);

        // RackNode
        kryo.register(RackNode.class);

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
