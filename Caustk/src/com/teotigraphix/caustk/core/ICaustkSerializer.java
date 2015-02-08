
package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.teotigraphix.caustk.groove.importer.CausticFileImporter;

public interface ICaustkSerializer {

    Kryo getKryo();

    CausticFileImporter getImporter();

    void serialize(File target, Object instance) throws IOException;

    <T> T deserialize(File file, Class<T> type) throws IOException;

    <T> T fromXMLManifest(File manifestFile, Class<T> clazz) throws FileNotFoundException;

    <T> T fromXMLManifest(String manifestData, Class<T> clazz) throws FileNotFoundException;
}
