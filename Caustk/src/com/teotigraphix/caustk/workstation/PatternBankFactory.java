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

public class PatternBankFactory extends CaustkSubFactoryBase {

    public PatternBankFactory() {
    }

    public PatternBank createPatternBank(RackSet rackSet) {
        ComponentInfo info = getFactory().createInfo(ComponentType.PatternBank);
        PatternBank patternBank = new PatternBank(info, rackSet);
        return patternBank;
    }

    public PatternBank createPatternBank(ComponentInfo info, RackSet rackSet) {
        PatternBank patternBank = new PatternBank(info, rackSet);
        return patternBank;
    }

    public Pattern createPattern(ComponentInfo info, PatternBank patternBank, int index) {
        Pattern pattern = new Pattern(info, patternBank, index);
        return pattern;
    }

    public Part createPart(ComponentInfo info, PatternBank patternBank, Machine machine) {
        Part part = new Part(info, patternBank, machine);
        return part;
    }
}
