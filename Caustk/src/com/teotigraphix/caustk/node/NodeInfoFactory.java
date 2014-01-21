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

package com.teotigraphix.caustk.node;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

/**
 * Factory to create {@link NodeInfo}s.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class NodeInfoFactory extends NodeFactoryBase {

    NodeInfoFactory(CaustkFactory factory) {
        super(factory);
    }

    public NodeInfo createInfo(NodeType type) {
        final NodeInfo result = new NodeInfo(UUID.randomUUID(), type);
        result.setCreated(new Date());
        result.setModified(new Date());
        return result;
    }

    public NodeInfo createInfo(NodeType type, String name) {
        return createInfo(type, new File("."), name);
    }

    public NodeInfo createInfo(NodeType type, String relativePath, String name) {
        relativePath = FilenameUtils.normalize(relativePath);
        return createInfo(type, new File(relativePath), name);
    }

    public NodeInfo createInfo(NodeType type, File relativePath, String name) {
        File file = new File(relativePath, name + "." + type.getExtension());
        // if we have no directory, just construct from name
        if (relativePath.getName().equals("."))
            file = new File(name + "." + type.getExtension());
        NodeInfo result = new NodeInfo(UUID.randomUUID(), type, file, name);
        result.setCreated(new Date());
        result.setModified(new Date());
        return result;
    }
}
