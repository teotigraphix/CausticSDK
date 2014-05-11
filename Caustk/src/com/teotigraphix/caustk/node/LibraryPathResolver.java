////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

import org.apache.commons.io.FilenameUtils;

import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.PresetComponent;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;

/**
 * Resolves {@link Library} archives based on {@link NodeInfo} and
 * {@link NodeType}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryPathResolver {

    private Library library;

    public LibraryPathResolver(Library library) {
        this.library = library;
    }

    //    File resolveLocation(ICaustkNode node, File rootDirectory) {
    //        if (node.getInfo().getType() == NodeType.Library) {
    //            Library library = (Library)node;
    //            return library.getManifestFile();
    //        }
    //        return new File(rootDirectory, resolvePath(node));
    //    }

    String resolvePath(ICaustkNode node) {
        NodeType type = node.getInfo().getType();
        if (type == NodeType.Library) {
            Library library = (Library)node;
            library.getManifestFile();
        }

        String subDirectory = null;

        if (type == NodeType.Patch) {
            // /Preset/SubSynth/
            PresetComponent presetDefinition = (PresetComponent)node;
            subDirectory = presetDefinition.getType().name();
        } else if (type == NodeType.Machine) {
            // /Machine/SubSynth/
            MachineNode machineDefinition = (MachineNode)node;
            subDirectory = machineDefinition.getType().name();
        } else if (type == NodeType.Effect) {
            // /Effect/Phaser/
            EffectNode effectDefinition = (EffectNode)node;
            subDirectory = effectDefinition.getType().name();
        } else if (type == NodeType.Pattern) {
            // /Effect/Phaser/
            PatternNode patternNode = (PatternNode)node;
            subDirectory = patternNode.getMachineType().name();
        }

        return resolvePath(node.getInfo(), subDirectory);
    }

    String resolvePath(NodeInfo info, String subDirectory) {

        String name = info.getType().name();
        final StringBuilder sb = new StringBuilder();

        // add the NodeType sub directory
        sb.append(name);
        sb.append(File.separator);

        // add sub directory within node type, mostly MachineType, EffectType etc.
        sb.append(subDirectory);
        sb.append(File.separator);

        if (info.getFile() != null) {
            sb.append(info.getFile().getPath());
        } else {
            // if there has been no explicit path created, add the extension
            // to the default name
            sb.append(info.getName());
            sb.append(".");
            sb.append(info.getType().getExtension());
        }

        return FilenameUtils.normalize(sb.toString(), true);
    }

    File resolveLocation(NodeInfo info) {
        File file = info.getFile();
        if (file == null)
            throw new IllegalStateException("path must not be null");
        return new File(library.getDirectory(), file.getPath());
    }
}
