
package com.teotigraphix.gdx.app;

import com.esotericsoftware.kryo.Kryo;

public interface IApplicationConfigurator {
    void configure(Kryo kryo);
}
