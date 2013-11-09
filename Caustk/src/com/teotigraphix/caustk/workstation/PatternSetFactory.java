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

public class PatternSetFactory extends CaustkSubFactoryBase {

    public PatternSetFactory() {
    }

    public PatternSet createPatternSet(ComponentInfo info, RackSet rackSet) {
        PatternSet patternSet = new PatternSet(info, rackSet);
        return patternSet;
    }

    public Pattern createPattern(ComponentInfo info, PatternSet patternSet, int index) {
        Pattern pattern = new Pattern(info, patternSet, index);
        return pattern;
    }

    public Part createPart(ComponentInfo info, PatternSet patternSet, Machine machine) {
        Part part = new Part(info, patternSet, machine);
        return part;
    }
}
