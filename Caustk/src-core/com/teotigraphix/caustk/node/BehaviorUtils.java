////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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
        if (!rack.contains(index))
            return;

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
            rack.get(index).getMixer().invoke(control, value);
        }
    }
}
