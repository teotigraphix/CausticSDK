
package com.teotigraphix.caustk.core.components.beatbox;

import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.core.osc.BeatboxSamplerMessage;

public class WavSamplerComponent extends ToneComponent {

    private static final int NUM_CHANNELS = 8;

    private final Map<Integer, WavSamplerChannel> map;

    public WavSamplerComponent() {
        map = new TreeMap<Integer, WavSamplerChannel>();

        for (int i = 0; i < NUM_CHANNELS; i++) {
            WavSamplerChannel channel = new WavSamplerChannel(this);
            channel.setIndex(i);
            map.put(i, channel);
        }
    }

    //--------------------------------------------------------------------------
    // API :: Methods
    //--------------------------------------------------------------------------

    public String getSampleName(int channel) {
        return BeatboxSamplerMessage.QUERY_CHANNEL_SAMPLE_NAME.queryString(getEngine(),
                getToneIndex(), channel);
    }

    public WavSamplerChannel getChannel(int index) {
        return map.get(index);
    }

    public WavSamplerChannel loadChannel(int index, String path) {
        BeatboxSamplerMessage.CHANNEL_LOAD.send(getEngine(), getToneIndex(), index, path);
        return getChannel(index);
    }

    @Override
    public void restore() {
        for (WavSamplerChannel channel : map.values()) {
            channel.restore();
        }
    }

}
