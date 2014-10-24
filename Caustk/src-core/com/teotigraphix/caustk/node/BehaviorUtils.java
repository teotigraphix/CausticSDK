
package com.teotigraphix.caustk.node;

import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage.MixerChannelControl;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;

public final class BehaviorUtils {

    /**
     * Sends mixer channel control messages to the machine's
     * {@link MixerChannel}.
     * 
     * @param rack The rack.
     * @param index The machine index.
     * @param control The {@link MixerChannelControl}.
     * @param value The float value for the mixer control.
     */
    public static void send(ICaustkRack rack, int index, MixerChannelControl control, float value) {
        if (control == MixerChannelControl.Solo) {
            rack.getRackNode().setSolo(index, value == 0f ? false : true);
            return;
        } else if (control == MixerChannelControl.Mute) {
            rack.getRackNode().setMute(index, value == 0f ? false : true);
            return;
        }
        rack.getMachine(index).getMixer().setValue(control, value);
    }
}
