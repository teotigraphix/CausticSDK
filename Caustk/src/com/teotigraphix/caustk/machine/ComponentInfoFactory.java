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

package com.teotigraphix.caustk.machine;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * @author Michael Schmalle
 */
public class ComponentInfoFactory extends CaustkSubFactoryBase {

    public ComponentInfoFactory() {
    }

    public ComponentInfo createInfo(ComponentType type) {
        final ComponentInfo result = new ComponentInfo(UUID.randomUUID(), type);
        result.setCreated(new Date());
        result.setModified(new Date());
        return result;
    }

    public ComponentInfo createInfo(ComponentType type, String name) {
        return createInfo(type, new File("."), name);
    }

    public ComponentInfo createInfo(ComponentType type, String relativePath, String name) {
        return createInfo(type, new File(relativePath), name);
    }

    public ComponentInfo createInfo(ComponentType type, File relativePath, String name) {
        File file = new File(relativePath, name + "." + type.getExtension());
        // if we have no directory, just construct from name
        if (relativePath.getName().equals("."))
            file = new File(name + "." + type.getExtension());
        ComponentInfo result = new ComponentInfo(UUID.randomUUID(), type, file, name);
        result.setCreated(new Date());
        result.setModified(new Date());
        return result;
    }
}
