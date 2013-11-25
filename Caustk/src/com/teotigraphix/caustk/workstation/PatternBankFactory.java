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

/**
 * @author Michael Schmalle
 */
public class PatternBankFactory extends CaustkSubFactoryBase {

    public PatternBankFactory() {
    }

    public PatternBank createPatternBank(String name, GrooveBox grooveBox) {
        ComponentInfo info = getFactory().createInfo(ComponentType.PatternBank, name);
        PatternBank patternBank = new PatternBank(info, grooveBox);
        return patternBank;
    }

    public PatternBank createPatternBank(ComponentInfo info, GrooveBox grooveBox) {
        PatternBank patternBank = new PatternBank(info, grooveBox);
        return patternBank;
    }

    public Pattern createPattern(ComponentInfo info, PatternBank patternBank, int index) {
        Pattern pattern = new Pattern(info, patternBank, index);
        return pattern;
    }

    public Part createPart(ComponentInfo info, GrooveBox grooveBox, Machine machine, int index) {
        Part part = new SynthPart(info, grooveBox, machine, index);
        if (machine.getMachineType() == MachineType.Beatbox) {
            part = new RhythmPart(info, grooveBox, machine, index);
        }
        return part;
    }
}
