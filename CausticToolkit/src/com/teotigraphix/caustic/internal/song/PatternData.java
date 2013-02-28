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

package com.teotigraphix.caustic.internal.song;

import com.teotigraphix.caustic.song.IPattern;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PatternData implements IPersist {

    @SuppressWarnings("unused")
    private IPattern mPattern;

    // delay, reverb settings (master)

    // bass, mid, high eq (master)

    // part mute

    // We NEED the IRack

    public PatternData(IPattern parent) {
        mPattern = parent;
    }

    @Override
    public void copy(IMemento memento) {
        //        IOutputPanel output = mPattern.getProject().getWorkspace().getRack().getOutputPanel();
        //        IMixerPanel mixer = mPattern.getProject().getWorkspace().getRack().getMixerPanel();
        //
        //        // holds tempo
        //        output.copy(memento.createChild("output"));
        //        // holds channel delay, reverb settings & eg including master
        //        // full snapshot of the mixer
        //        mixer.copy(memento.createChild("mixer"));
    }

    @Override
    public void paste(IMemento memento) {
        //        if (mPattern.isInitialized()) {
        //            IOutputPanel output = mPattern.getProject().getWorkspace().getRack().getOutputPanel();
        //            IMixerPanel mixer = mPattern.getProject().getWorkspace().getRack().getMixerPanel();
        //
        //            output.paste(memento.getChild("output"));
        //            mixer.paste(memento.getChild("mixer"));
        //        }
    }

}
