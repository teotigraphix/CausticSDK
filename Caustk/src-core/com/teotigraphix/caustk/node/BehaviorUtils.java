
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

        if (index == -1) {
            switch (control) {
                case Volume:
                    rack.getRackNode().getMaster().getVolume().setOut(value);
                    break;
                case High:
                    rack.getRackNode().getMaster().getEqualizer().setHigh(value);
                    break;
                case Mid:
                    rack.getRackNode().getMaster().getEqualizer().setMid(value);
                    break;
                case Bass:
                    rack.getRackNode().getMaster().getEqualizer().setBass(value);
                    break;
                default:
                    break;
            }
        } else {
            rack.getMachine(index).getMixer().setValue(control, value);
        }
    }
}
