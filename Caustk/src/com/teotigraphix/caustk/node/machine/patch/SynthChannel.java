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

package com.teotigraphix.caustk.node.machine.patch;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.OSCUtils;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.node.machine.BeatBoxMachine;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.MachineType;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;

/**
 * The synth component, note on/off and polyphony.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see MachineNode#getSynth()
 */
public class SynthChannel extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int polyphony = 4;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // polyphony
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.SynthMessage#POLYPHONY
     */
    public int getPolyphony() {
        return polyphony;
    }

    public int queryPolyphony() {
        return (int)SynthMessage.POLYPHONY.query(getRack(), getMachineIndex());
    }

    /**
     * @param polyphony (0..14)
     * @see com.teotigraphix.caustk.core.osc.SynthMessage#POLYPHONY
     */
    public void setPolyphony(int polyphony) {
        MachineType machineType = OSCUtils.toMachineType(getRack(), getMachineIndex());
        if (machineType == MachineType.Bassline || machineType == MachineType.Modular
                || machineType == MachineType.EightBitSynth) {
            polyphony = 1;
            return;
        }
        // XXX OSC BUG!!!!!!
        if (machineType == MachineType.Vocoder) {
            polyphony = 4;
            return;
        }

        if (machineType == MachineType.BeatBox) {
            polyphony = 8;
            return;
        }
        if (polyphony == this.polyphony)
            return;

        // 0 is lagato in SubSynth
        if (polyphony < 0 || polyphony > 16)
            throw newRangeException(SynthMessage.POLYPHONY.toString(), "0..16", polyphony);

        this.polyphony = polyphony;
        SynthMessage.POLYPHONY.send(getRack(), getMachineIndex(), polyphony);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public SynthChannel() {
    }

    public SynthChannel(MachineNode machineNode) {
        super(machineNode);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Triggers a MIDI note on.
     * 
     * @param pitch The MIDI note.
     * @see #noteOn(int, float)
     */
    public void noteOn(int pitch) {
        noteOn(pitch, 1f);
    }

    /**
     * Triggers a MIDI note on with velocity.
     * 
     * @param pitch The MIDI note.
     * @param velocity The note velocity (0.0..1.0).
     * @see com.teotigraphix.caustk.core.osc.SynthMessage#NOTE
     */
    public void noteOn(int pitch, float velocity) {
        SynthMessage.NOTE.send(getRack(), getMachineIndex(), pitch, 1, velocity);
    }

    /**
     * Triggers a MIDI note off that was triggered by
     * {@link #noteOn(int, float)}.
     * 
     * @param pitch The MIDI note.
     * @see com.teotigraphix.caustk.core.osc.SynthMessage#NOTE
     */
    public void noteOff(int pitch) {
        SynthMessage.NOTE.send(getRack(), getMachineIndex(), pitch, 0);
    }

    /**
     * Previews a MIDI note.
     * 
     * @param pitch The MIDI note.
     * @param oneshot Play one shot, {@link BeatBoxMachine},
     *            {@link PCMSynthMachine} samples.
     * @see com.teotigraphix.caustk.core.osc.SynthMessage#NOTE_PREVIEW
     */
    public void notePreview(int pitch, boolean oneshot) {
        SynthMessage.NOTE_PREVIEW.send(getRack(), getMachineIndex(), pitch, oneshot ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        SynthMessage.POLYPHONY.send(getRack(), getMachineIndex(), polyphony);
    }

    @Override
    protected void restoreComponents() {
        setPolyphony(queryPolyphony());
    }
}
