
package com.teotigraphix.caustk.node;

import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.osc.MixerControls;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;

public final class BehaviorUtils {

    /**
     * Sends mixer channel control messages to the machine's
     * {@link MixerChannel}.
     * 
     * @param rack The rack.
     * @param index The machine index.
     * @param control The {@link MixerControls}.
     * @param value The float value for the mixer control.
     */
    public static void send(ICaustkRack rack, int index, MixerControls control, float value) {
        if (control == MixerControls.Solo) {
            rack.getRackNode().setSolo(index, value == 0f ? false : true);
            return;
        } else if (control == MixerControls.Mute) {
            rack.getRackNode().setMute(index, value == 0f ? false : true);
            return;
        }

        if (index == -1) {
            switch (control) {
                case Volume:
                    rack.getRackNode().getMaster().getVolume().setOut(value);
                    break;
                case EqHigh:
                    rack.getRackNode().getMaster().getEqualizer().setHigh(value);
                    break;
                case EqMid:
                    rack.getRackNode().getMaster().getEqualizer().setMid(value);
                    break;
                case EqBass:
                    rack.getRackNode().getMaster().getEqualizer().setBass(value);
                    break;
                default:
                    break;
            }
        } else {
            rack.getMachine(index).getMixer().invoke(control, value);
        }
    }
}
