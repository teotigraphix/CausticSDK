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

package com.teotigraphix.caustk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.teotigraphix.caustk.live.ICaustkComponent;

/**
 * @author Michael Schmalle
 */
public class KryoUtils {

    private static Kryo kryo;

    public static Kryo getKryo() {
        if (kryo == null)
            kryo = createKryo();
        return kryo;
    }

    public static <T> T copy(T instance) {
        return getKryo().copy(instance);
    }

    public static Kryo createKryo() {
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        //kryo.setRegistrationRequired(true);

        kryo.setAsmEnabled(true);
        //        kryo.register(stateType);
        kryo.register(UUID.class, new UUIDSerializer());
        kryo.register(File.class, new FileSerializer());
        //        kryo.register(Class.class);
        //        kryo.register(ISystemSequencer.SequencerMode.class);
        //
        //        kryo.register(Rack.class);
        //
        //        kryo.register(SubSynthTone.class);
        //
        //        kryo.register(SoundMixer.class);
        //        kryo.register(MasterMixer.class);
        //        kryo.register(HashMap.class);
        //        kryo.register(ArrayList.class);
        //        kryo.register(MasterDelay.class);
        //        kryo.register(MasterEqualizer.class);
        //        kryo.register(MasterLimiter.class);
        //        kryo.register(MasterReverb.class);
        //        kryo.register(SoundSource.class);
        //        kryo.register(SystemSequencer.class);
        //        kryo.register(TrackSequencer.class);
        //        kryo.register(TrackSong.class);
        //        kryo.register(CausticSongFile.class);
        //
        //        kryo.register(SoundMixerChannel.class);
        //        kryo.register(Track.class);
        //        kryo.register(TrackItem.class);
        //        kryo.register(TreeMap.class);
        //        kryo.register(Trigger.class);
        //        kryo.register(TriggerMap.class);
        //        kryo.register(Note.class);
        return kryo;
    }

    public static ICaustkComponent readFileObject(Kryo kryo, File file)
            throws FileNotFoundException {
        ICaustkComponent state = null;
        try {
            Input input = new Input(new FileInputStream(file));
            state = (ICaustkComponent)kryo.readClassAndObject(input);
            input.close();
        } catch (FileNotFoundException e) {
            throw e;
        }
        return state;
    }

    public static <T> T readFileObject(Kryo kryo, File file, Class<T> clazz)
            throws FileNotFoundException {
        T state = null;
        try {
            Input input = new Input(new FileInputStream(file));
            state = kryo.readObject(input, clazz);
            input.close();
        } catch (FileNotFoundException e) {
            throw e;
        }
        return state;
    }

    public static void writeFileObject(Kryo kryo, File file, Object state)
            throws FileNotFoundException {
        File parentFile = file.getParentFile();
        if (!parentFile.exists())
            parentFile.mkdirs();
        if (!parentFile.exists())
            throw new FileNotFoundException("Failed making parent directory");
        Output output = new Output(new FileOutputStream(file));
        kryo.writeObject(output, state);
        output.close();
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
        public File read(Kryo kryo, Input input, Class<File> type) {
            return new File(input.readString());
        }

        @Override
        public void write(Kryo kryo, Output output, File object) {
            output.writeString(object.toString());
        }

        @Override
        public File copy(Kryo kryo, File file) {
            return new File(file.getAbsolutePath());
        }
    }
}
