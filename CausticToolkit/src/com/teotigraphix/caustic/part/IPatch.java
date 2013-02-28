////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.part;

import com.teotigraphix.caustic.rack.IRackAware;
import com.teotigraphix.caustic.song.IPreset;
import com.teotigraphix.common.IMemento;

/**
 * The IPatch API contains tones that are played together as a whole sound
 * layered on top of each other.
 * <p>
 * A patch will uses it's IPart parent's IPhrase to synchronize the sequencers
 * of the backing sound generators of the ITone.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPatch extends IPreset, IRackAware {

    //--------------------------------------------------------------------------
    // 
    //  Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  part
    //----------------------------------

    /**
     * Returns the parent part for the patch.
     */
    IPart getPart();

    //----------------------------------
    //  data
    //----------------------------------

    /**
     * The extra data associated with the patch.
     */
    PatchData getData();

    /**
     * @see #getData()
     */
    void setData(PatchData value);

    //----------------------------------
    //  memento
    //----------------------------------

    IMemento getMemento();

    void setMemento(IMemento memento);

    /**
     * Reverts the patch back to it's original {@link IMemento} state.
     * <p>
     * If the patch contains an {@link IMemento} state, returns
     * <code>true</code> else returns <code>false</code>.
     */
    boolean revert();

    // load(File file);

    // save(File file);
}
