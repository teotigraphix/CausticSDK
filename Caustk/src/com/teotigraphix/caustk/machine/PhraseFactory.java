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

import com.teotigraphix.caustk.utils.PatternUtils;

public class PhraseFactory extends CaustkSubFactoryBase {

    public PhraseFactory() {
    }

    public Phrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex,
            int patternIndex) {
        final int index = PatternUtils.getIndex(bankIndex, patternIndex);
        Phrase caustkPhrase = new Phrase(info, index, machineType);
        return caustkPhrase;
    }

    public Phrase createPhrase(Machine caustkMachine, int bankIndex, int patternIndex) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Phrase);
        final int index = PatternUtils.getIndex(bankIndex, patternIndex);
        Phrase caustkPhrase = new Phrase(info, index, caustkMachine);
        return caustkPhrase;
    }
}
