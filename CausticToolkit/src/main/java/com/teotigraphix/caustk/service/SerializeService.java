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

package com.teotigraphix.caustk.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;

public class SerializeService implements ISerializeService {

    private ICaustkController controller;

    public SerializeService(ICaustkController controller) {
        this.controller = controller;
    }

    @Override
    public <T> T fromFile(File file, Class<T> classOfT) {
        return JsonUtils.fromGson(file, classOfT, controller);
    }

    @Override
    public <T> T fromString(String data, Class<T> classOfT) {
        return JsonUtils.fromGson(data, classOfT, controller);
    }

    @Override
    public String toUnString(Object serialized) {
        return JsonUtils.toGson(serialized, false);
    }

    @Override
    public String toString(Object serialized) {
        return JsonUtils.toGson(serialized, true);
    }

    @Override
    public void save(File target, Object serialized) throws IOException {
        String data = JsonUtils.toGson(serialized, true);
        FileUtils.writeStringToFile(target, data);
    }

}
