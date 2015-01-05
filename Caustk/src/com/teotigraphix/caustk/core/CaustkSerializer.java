
package com.teotigraphix.caustk.core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.teotigraphix.caustk.groove.importer.CausticFileImporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CaustkSerializer implements ICaustkSerializer {

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
    public <T> T fromXMLManifest(File manifestFile, Class<T> clazz) throws FileNotFoundException {
        return importer.fromXMLManifest(manifestFile, clazz);
    }

}
