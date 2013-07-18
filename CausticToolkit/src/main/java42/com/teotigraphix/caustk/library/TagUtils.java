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

package com.teotigraphix.caustk.library;

import com.teotigraphix.caustk.library.vo.MetadataInfo;

public class TagUtils {

    public static void addDefaultTags(IMachine machine, LibraryPatch item) {
        MetadataInfo info = item.getMetadataInfo();
        info.getTags().add(item.getMachineType().getValue());
        info.getTags().add(machine.getId());
        String preset = machine.getPresetName();
        if (preset != null && !preset.isEmpty())
            info.getTags().add(preset);
    }

    public static void addDefaultTags(LibraryPhrase item) {
        MetadataInfo info = item.getMetadataInfo();
        info.getTags().add("length-" + item.getLength());
        info.getTags().add(item.getTempo() + "");
        info.getTags().add(item.getMachineType().getValue());
        info.getTags().add(item.getResolution().toString().toLowerCase());
    }

    public static void addDefaultTags(String name, IRack rack, LibraryScene item) {
        // add original song name, 
        MetadataInfo info = item.getMetadataInfo();
        info.getTags().add(name);
        info.getTags().add(rack.getOutputPanel().getBPM() + "");
    }

}
