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

package com.teotigraphix.caustk.machine;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.IRackAware;
import com.teotigraphix.caustk.core.IRackSerializer;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.rack.IRack;

public class CaustkMasterSequencer implements IRackSerializer, IRackAware {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private CaustkScene scene;

    public CaustkScene getScene() {
        return scene;
    }

    //--------------------------------------------------------------------------
    // IRackAware API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    @Override
    public IRack getRack() {
        return rack;
    }

    @Override
    public void setRack(IRack value) {
        rack = value;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkMasterSequencer() {
    }

    CaustkMasterSequencer(CaustkScene caustkScene) {
        this.scene = caustkScene;
    }

    @Override
    public void load(CaustkFactory factory) {
        setRack(factory.getRack());
        restore();
    }

    @Override
    public void restore() {
        String patterns = rack.getSystemSequencer().getPatterns();
        if (patterns != null) {
            loadPatterns(patterns);
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

            CaustkMachine caustkMachine = scene.getMachine(index);
            caustkMachine.addPattern(bank, pattern, start, end);
        }
    }

    public void updateMachine(CaustkMachine caustkMachine) {
        for (CaustkSequencerPattern caustkSequencerPattern : caustkMachine.getPatterns().values()) {
            SequencerMessage.PATTERN_EVENT.send(rack, caustkMachine.getIndex(),
                    caustkSequencerPattern.getStartBeat(), caustkSequencerPattern.getBankIndex(),
                    caustkSequencerPattern.getPatternIndex(), caustkSequencerPattern.getEndBeat());
        }
    }

    @Override
    public void update() {
        for (CaustkMachine caustkMachine : scene.getMachines()) {
            updateMachine(caustkMachine);
        }
    }
}
