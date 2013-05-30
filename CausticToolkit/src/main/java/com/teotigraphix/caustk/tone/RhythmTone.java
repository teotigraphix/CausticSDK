
package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.machine.IMachine;

public class RhythmTone extends Tone {
    @Override
    public IMachine getMachine() {
        // XXX Unit test RhythmTone.getMachine() to make sure its return a IMachine
        return (IMachine)channel.getChannel().getDevice();
    }

    private RhythmChannel channel;

    public RhythmChannel getChannel() {
        return channel;
    }

    public RhythmTone(RhythmChannel channel) {
        this.channel = channel;
    }
}
