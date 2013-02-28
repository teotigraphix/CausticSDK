////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.part;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.MachineException;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.part.ITone;
import com.teotigraphix.caustic.part.IToneLibrary;
import com.teotigraphix.caustic.song.IPreset;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.common.IMemento;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class ToneLibrary extends PresetLibrary implements IToneLibrary {

    private static final String TAG_TONE = "tone";

    private static final String TAG_TONES = "tones";

    public ToneLibrary(IWorkspace workspace) {
        super(workspace);
        tagChildren = TAG_TONES;
        tagChild = TAG_TONE;
    }

    @Override
    protected IPreset createPreset(IMemento memento) {
        //		RuntimeTone tone = new RuntimeTone();
        //		return tone;
        return null;
    }

    @Override
    public ITone createDefaultTone(int index, String toneId, String toneName, MachineType type)
            throws MachineException {
        ITone machine = (ITone)getWorkspace().getRack().getMachine(index);
        if (machine == null) {
            try {
                //				machine = (ITone) getProject().getRack().addMachineAt(index,
                //						toneId, type);
                machine = (ITone)getWorkspace().getRack().addMachine(toneId, type);
            } catch (CausticException e) {
                throw new MachineException("Error creating tone for machine " + index + ":"
                        + type.toString(), e);
            }
        }
        machine.setName(toneName);
        machine.setPresetBank(DEFAULT_BANK);
        return machine;
    }
}
