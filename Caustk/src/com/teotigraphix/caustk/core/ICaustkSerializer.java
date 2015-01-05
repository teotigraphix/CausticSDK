
package com.teotigraphix.caustk.core;

import com.esotericsoftware.kryo.Kryo;
import com.teotigraphix.caustk.groove.importer.CausticFileImporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ICaustkSerializer {

    Kryo getKryo();

    CausticFileImporter getImporter();

    void serialize(File target, Object instance) throws IOException;

    <T> T deserialize(File file, Class<T> type) throws IOException;

    <T> T fromXMLManifest(File manifestFile, Class<T> clazz) throws FileNotFoundException;

}
