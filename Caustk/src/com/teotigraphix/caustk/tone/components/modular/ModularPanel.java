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

package com.teotigraphix.caustk.tone.components.modular;

public class ModularPanel extends ModularComponentBase {

    private static final long serialVersionUID = 2758979796475610030L;

    public ModularPanel() {
        super(16);
    }

    @Override
    protected int getNumBays() {
        return -1;
    }

    public enum ModularPanelJack implements IModularJack {

        OutNoteCV(0),

        OutVelocity(1),

        OutModulation(2),

        InLeft(0),

        InRight(1),

        InVolumeModulation(2);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        ModularPanelJack(int value) {
            this.value = value;
        }

    }
}
