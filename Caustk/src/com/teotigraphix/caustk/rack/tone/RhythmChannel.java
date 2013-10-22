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

package com.teotigraphix.caustk.rack.tone;

import com.teotigraphix.caustk.rack.tone.components.beatbox.WavSamplerChannel;

/**
 * The channel impl for a Beatbox channel and wave sampler.
 * 
 * @author Michael Schmalle
 */
public class RhythmChannel {

    private WavSamplerChannel channel;

    public WavSamplerChannel getChannel() {
        return channel;
    }

    public RhythmChannel() {
    }

    public RhythmChannel(WavSamplerChannel channel) {
        this.channel = channel;

    }

}
