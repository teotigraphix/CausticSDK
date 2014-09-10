
package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;

public interface ICaustkRackSerializer {

    Kryo getKryo();

    void serialize(File target, Object node) throws IOException;

    <T> T deserialize(File file, Class<T> type) throws IOException;
}
