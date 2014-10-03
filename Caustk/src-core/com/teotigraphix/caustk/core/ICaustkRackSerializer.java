
package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.teotigraphix.caustk.groove.library.LibraryEffect;

public interface ICaustkRackSerializer {

    Kryo getKryo();

    void serialize(File target, Object instance) throws IOException;

    <T> T deserialize(File file, Class<T> type) throws IOException;

    String toEffectXML(LibraryEffect item);

    <T> T fromXMLManifest(File manifestFile, Class<T> clazz) throws FileNotFoundException;
}
