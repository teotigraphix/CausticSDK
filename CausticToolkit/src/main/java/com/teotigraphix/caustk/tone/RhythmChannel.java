
package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.sampler.IBeatboxSamplerChannel;

public class RhythmChannel {

    private IBeatboxSamplerChannel channel;

    public IBeatboxSamplerChannel getChannel() {
        return channel;
    }

    public RhythmChannel(IBeatboxSamplerChannel channel) {
        this.channel = channel;

    }

}
