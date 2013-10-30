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

package com.teotigraphix.caustk.rack;

import com.teotigraphix.caustk.controller.IRackAware;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.rack.ISystemSequencer.SequencerMode;

/**
 * The {@link IRackComponent} API specifies a component that can be added to the
 * {@link IRack}.
 * 
 * @author Michael Schmalle
 */
public interface IRackComponent extends IRackAware, IRestore {

    /**
     * Callback for a beat change from the {@link IRack#update()}.
     * <p>
     * Gives rack component's chance to operate on a beat change event.
     * 
     * @param measure The current measure.
     * @param beat The current float beat If the {@link SequencerMode} is
     *            Pattern this will be (0..31), the the mode is Song, the beat
     *            value will be the current beat in the entire song, the play
     *            head of the song sequencer.
     */
    void beatChange(int measure, float beat);
}
