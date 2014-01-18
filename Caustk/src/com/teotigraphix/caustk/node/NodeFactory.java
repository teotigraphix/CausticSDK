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

import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.machine.BasslineMachine;
import com.teotigraphix.caustk.node.machine.BeatBoxMachine;
import com.teotigraphix.caustk.node.machine.EightBitSynthMachine;
import com.teotigraphix.caustk.node.machine.FMSynthMachine;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.ModularMachine;
import com.teotigraphix.caustk.node.machine.OrganMachine;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;
import com.teotigraphix.caustk.node.machine.VocoderMachine;

/**
 * The {@link NodeFactory} creates caustk {@link NodeBase} instances.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class NodeFactory {

    public MachineNode createMachine(int index, MachineType type, String name) {
        MachineNode machineNode = null;
        switch (type) {
            case SubSynth:
                machineNode = new SubSynthMachine(index, name);
                break;
            case Bassline:
                machineNode = new BasslineMachine(index, name);
                break;
            case BeatBox:
                machineNode = new BeatBoxMachine(index, name);
                break;
            case EightBitSynth:
                machineNode = new EightBitSynthMachine(index, name);
                break;
            case FMSynth:
                machineNode = new FMSynthMachine(index, name);
                break;
            case Modular:
                machineNode = new ModularMachine(index, name);
                break;
            case Organ:
                machineNode = new OrganMachine(index, name);
                break;
            case PCMSynth:
                machineNode = new PCMSynthMachine(index, name);
                break;
            case PadSynth:
                machineNode = new PadSynthMachine(index, name);
                break;
            case Vocoder:
                machineNode = new VocoderMachine(index, name);
                break;
        }
        return machineNode;
    }
}
