
package com.teotigraphix.caustk.service;

import java.io.File;

import com.teotigraphix.caustk.controller.ICaustkController;

public class SerializeService implements ISerializeService {

    @SuppressWarnings("unused")
    private ICaustkController controller;

    public SerializeService(ICaustkController controller) {
        this.controller = controller;
    }

    @Override
    public <T> T fromFile(File file, Class<T> classOfT) {
        return JsonUtils.fromGson(file, classOfT);
    }

    @Override
    public <T> T fromString(String data, Class<T> classOfT) {
        return JsonUtils.fromGson(data, classOfT);
    }

    @Override
    public String toString(Object serialized) {
        return JsonUtils.toGson(serialized, true);
    }

}
