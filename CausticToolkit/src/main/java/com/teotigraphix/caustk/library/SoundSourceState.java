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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;
import com.teotigraphix.caustk.tone.ToneType;

public class SoundSourceState {

    private Map<Integer, String> tones = new HashMap<Integer, String>();

    public SoundSourceState() {
    }

    public void addTone(int index, String data) {
        tones.put(index, data);
    }

    /**
     * Returns a lazy List of the {@link RackInfoItem}s found in the rack info.
     */
    public List<RackInfoItem> getItems() {
        List<RackInfoItem> result = new ArrayList<RackInfoItem>();

        return result;
    }

    public static class RackInfoItem {
        // <machine active="1" id="DRUMSIES" index="5" 
        // patchId="a146b131-d14d-4828-97b0-369c1accfa2d" type="beatbox"/>

        private UUID patchId;

        /**
         * Returns the patch {@link UUID} that the machine had when its library
         * was created.
         * <p>
         * This id may become invalid over time if libraries are merged.
         */
        public UUID getPatchId() {
            return patchId;
        }

        private String name;

        /**
         * Returns the String id of the machine in the rack.
         */
        public String getName() {
            return name;
        }

        private boolean active;

        public boolean isActive() {
            return active;
        }

        private int index;

        public int getIndex() {
            return index;
        }

        private ToneType toneType;

        public ToneType getToneType() {
            return toneType;
        }

        public RackInfoItem(Tone tone) {
            active = true;
            index = tone.getIndex();
            toneType = tone.getToneType();
            name = tone.getName();
            //            String pid = memento.getString("patchId");
            //            if (pid != null)
            //                patchId = UUID.fromString(pid);
        }

        public ToneDescriptor createDescriptor() {
            ToneDescriptor descriptor = new ToneDescriptor(index, name, toneType);
            return descriptor;
        }

    }
}
