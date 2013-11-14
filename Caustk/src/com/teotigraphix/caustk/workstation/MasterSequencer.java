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

package com.teotigraphix.caustk.workstation;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerTransportChange;
import com.teotigraphix.caustk.rack.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.rack.sequencer.SystemSequencer;

/**
 * @author Michael Schmalle
 */
public class MasterSequencer extends CaustkComponent {

    private transient IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private RackSet rackSet;

    @Tag(101)
    private SystemSequencer systemSequencer;

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return null;
    }

    //----------------------------------
    // rackSet
    //----------------------------------

    public RackSet getRackSet() {
        return rackSet;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    MasterSequencer() {
    }

    MasterSequencer(RackSet rackSet) {
        this.rackSet = rackSet;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Calculates the tempo based on a Timer calculation between calls to this
     * method.
     * <p>
     * If the calculated tempo is valid, the system sequencer bpm will be
     * updated.
     */
    public void tempoTap() {
        // TODO Impl tempoTap()
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                systemSequencer = new SystemSequencer();
                systemSequencer.create(context);
                break;

            case Load:
                rack = context.getRack();
                systemSequencer = new SystemSequencer();
                systemSequencer.phaseChange(context, ComponentPhase.Load);
                restore();
                break;

            case Update:
                rack = context.getRack();
                systemSequencer.phaseChange(context, ComponentPhase.Update);
                for (Machine caustkMachine : rackSet.getMachines()) {
                    updateMachine(caustkMachine);
                }
                break;

            case Restore:
                systemSequencer.phaseChange(context, ComponentPhase.Restore);
                String patterns = systemSequencer.getPatterns();
                if (patterns != null) {
                    loadPatterns(patterns);
                }
                break;

            case Connect:
                systemSequencer.phaseChange(context, ComponentPhase.Connect);
                break;

            case Disconnect:
                systemSequencer.phaseChange(context, ComponentPhase.Disconnect);
                break;
        }
    }

    private void loadPatterns(String patterns) {
        // [machin_index] [start_measure] [bank] [pattern] [end_measure]
        String[] split = patterns.split("\\|");
        for (String group : split) {
            String[] parts = group.split(" ");
            int index = Integer.valueOf(parts[0]);
            int start = Integer.valueOf(parts[1]);
            int bank = Integer.valueOf(parts[2]);
            int pattern = Integer.valueOf(parts[3]);
            int end = Integer.valueOf(parts[4]);

            Machine machine = rackSet.getMachine(index);
            machine.addPattern(bank, pattern, start, end);
        }
    }

    boolean updatePosition(int measure, float beat) {
        return systemSequencer.updatePosition(measure, beat);
    }

    void updateMachine(Machine caustkMachine) {
        for (SequencerPattern caustkSequencerPattern : caustkMachine.getPatterns().values()) {
            SequencerMessage.PATTERN_EVENT.send(rack, caustkMachine.getMachineIndex(),
                    caustkSequencerPattern.getStartBeat(), caustkSequencerPattern.getBankIndex(),
                    caustkSequencerPattern.getPatternIndex(), caustkSequencerPattern.getEndBeat());
        }
    }

    //--------------------------------------------------------------------------
    // Proxy API
    //--------------------------------------------------------------------------

    //----------------------------------
    // bpm
    //----------------------------------

    public void setBPM(float value) {
        systemSequencer.setBPM(value);
    }

    public float getBPM() {
        return systemSequencer.getBPM();
    }

    public boolean isPlaying() {
        return systemSequencer.isPlaying();
    }

    /**
     * Plays the {@link SystemSequencer}.
     * 
     * @see OnSystemSequencerTransportChange
     */
    public void play() {
        systemSequencer.play();
    }

    public void play(SequencerMode mode) {
        systemSequencer.play(mode);
    }

    /**
     * Stops the {@link SystemSequencer}.
     * 
     * @see OnSystemSequencerTransportChange
     */
    public void stop() {
        systemSequencer.stop();
    }

    public int getCurrentSixteenthStep() {
        return systemSequencer.getCurrentSixteenthStep();
    }

}
