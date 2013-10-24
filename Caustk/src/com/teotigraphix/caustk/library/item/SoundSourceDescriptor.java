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

package com.teotigraphix.caustk.library.item;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;

import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.ToneType;

@SuppressLint("UseSparseArrays")
public class SoundSourceDescriptor {

    private Map<Integer, ToneDescriptor> descriptors = new HashMap<Integer, ToneDescriptor>();

    public Map<Integer, ToneDescriptor> getDescriptors() {
        return descriptors;
    }

    private Map<Integer, SoundMixerChannelDescriptor> channels = new HashMap<Integer, SoundMixerChannelDescriptor>();

    public Map<Integer, SoundMixerChannelDescriptor> getChannels() {
        return channels;
    }

    public SoundSourceDescriptor() {
    }

    public void addTone(IRack rack, int index, String name, ToneType toneType, UUID patchId) {
        // create the tone descriptor
        ToneDescriptor descriptor = new ToneDescriptor(index, name, toneType);
        descriptor.setPatchId(patchId);
        descriptors.put(index, descriptor);

        // create the mixer channel descriptor
        SoundMixerChannelDescriptor channelDescriptor = new SoundMixerChannelDescriptor(rack, index);
        channels.put(index, channelDescriptor);
    }

    public void addTone(Tone tone, UUID patchId) {
        // create the tone descriptor
        ToneDescriptor descriptor = new ToneDescriptor(tone.getIndex(), tone.getName(),
                tone.getToneType());
        descriptor.setPatchId(patchId);
        descriptors.put(tone.getIndex(), descriptor);

        // create the mixer channel descriptor
        SoundMixerChannelDescriptor channelDescriptor = new SoundMixerChannelDescriptor(tone);
        channels.put(tone.getIndex(), channelDescriptor);
    }
}
