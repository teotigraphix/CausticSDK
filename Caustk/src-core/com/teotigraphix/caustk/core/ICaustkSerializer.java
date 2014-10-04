
package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;

public interface ICaustkSerializer {

    Kryo getKryo();

    void serialize(File target, Object instance) throws IOException;

    <T> T deserialize(File file, Class<T> type) throws IOException;

    <T> T fromXMLManifest(File manifestFile, Class<T> clazz) throws FileNotFoundException;

    String toXML(Object instance);

}
