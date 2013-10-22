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

package com.teotigraphix.caustk.tone.components.beatbox;

import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.BeatboxMessage;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneComponent;

public class WavSamplerComponent extends ToneComponent {

    private static final int NUM_CHANNELS = 8;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private Map<Integer, WavSamplerChannel> map;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public void setTone(Tone value) {
        super.setTone(value);
        map = new TreeMap<Integer, WavSamplerChannel>();

        for (int i = 0; i < NUM_CHANNELS; i++) {
            WavSamplerChannel channel = new WavSamplerChannel(this);
            channel.setTone(value);
            channel.setIndex(i);
            map.put(i, channel);
        }
    }

    public WavSamplerComponent() {
    }

    //--------------------------------------------------------------------------
    // API :: Methods
    //--------------------------------------------------------------------------

    public String getSampleName(int channel) {
        return BeatboxMessage.QUERY_CHANNEL_SAMPLE_NAME.queryString(getEngine(), getToneIndex(),
                channel);
    }

    public WavSamplerChannel getChannel(int index) {
        return map.get(index);
    }

    public WavSamplerChannel loadChannel(int index, String path) {
        BeatboxMessage.CHANNEL_LOAD.send(getEngine(), getToneIndex(), index, path);
        return getChannel(index);
    }

    @SuppressWarnings("unused")
    @Override
    public void restore() {
        for (WavSamplerChannel channel : map.values()) {
            // XXX IMPLEMENT channel.restore();
        }
    }

}
