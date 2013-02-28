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

import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.IPattern;
import com.teotigraphix.common.IDispose;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * The IPart interface is a musician that can play an instrument, the IPatch.
 * <p>
 * When creating an application, parts are usually non-mutable from startup to
 * shutdown in the application. This means that an application creates 8 parts,
 * these parts will remain intact throughout the duration of client interaction
 * of the IPattern selected.
 * </p>
 * <p>
 * It's also important to note that the IPart does not correspond to a part
 * selection button on the musical instrument. All a button selecting a part
 * would do is select the part within the current IPattern.
 * </p>
 * <p>
 * A part can be playing, muted or even empty. When the part is considered empty
 * is when no IPatch (a sound source) has been assigned to the part.
 * </p>
 * <p>
 * The part's IPattern is a structure of it's sibling parts. The pattern manages
 * what bank and pattern number the part's IPhrase belongs to.
 * </p>
 * <p>
 * For sequencing step not triggers in a part, the ISoundSource api is used to
 * abstract the actual implementation from the IMachine being used to play the
 * actual sound in it's IPatternSequencer.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPart extends IPersist, IDispose {

    //--------------------------------------------------------------------------
    // 
    //  Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  index
    //----------------------------------

    /**
     * The index of the part found in the {@link ISoundGenerator}.
     * 
     * @see ISoundGenerator#getPart(int)
     */
    int getIndex();

    //----------------------------------
    //  id
    //----------------------------------

    /**
     * The identifier of the part found in the {@link ISoundGenerator}.
     */
    String getId();

    //----------------------------------
    //  name
    //----------------------------------

    /**
     * Returns the human readable part name.
     * <p>
     * This name can be set independently from the {@link ISoundGenerator} id
     * found with {@link #getId()}.
     */
    String getName();

    void copyTone(IMemento memento);

    void pasteTone(IMemento memento);

    //----------------------------------
    //  initialized
    //----------------------------------

    /**
     * Whether the {@link IPart} is in an initialized state.
     * <p>
     * It's up to the client that creates the part from the sound generator to
     * set the initialized property based on the part's state. Sometimes parts
     * need to be restore before allowing access from other clients. So using
     * the initialized property allows the Part to block access to API until the
     * creator has determined the part is in an initialized state.
     */
    boolean isInitialized();

    /**
     * Sets the initialized state of the {@link IPart}.
     * 
     * @param value <code>true</code> if initialized.
     * @see #isInitialized()
     */
    void setInitialized(boolean value);

    //----------------------------------
    //  tone
    //----------------------------------

    /**
     * Returns the part's sound generating tone.
     */
    ITone getTone();

    //----------------------------------
    //  pattern
    //----------------------------------

    /**
     * The part's IPattern parent.
     */
    IPattern getPattern();

    //----------------------------------
    //  patch
    //----------------------------------

    /**
     * Return the IPart's single IPatch.
     */
    IPatch getPatch();

    //----------------------------------
    //  phrase
    //----------------------------------

    /**
     * Return the IPart's active IPhrase.
     */
    IPhrase getPhrase();

    //----------------------------------
    //  selected
    //----------------------------------

    /**
     * Whether the part is in a selected state.
     */
    boolean isSelected();

    /**
     * @see #isSelected()
     */
    void setSelected(boolean value);

    //----------------------------------
    //  mute
    //----------------------------------

    /**
     * Whether the part is in a muted state.
     */
    boolean isMute();

    /**
     * @see #isMute()
     */
    void setMute(boolean value);

    //----------------------------------
    //  solo
    //----------------------------------

    /**
     * Whether the part is in a soloed state.
     */
    boolean isSolo();

    /**
     * @see #isSolo()
     */
    void setSolo(boolean value);

    //----------------------------------
    //  data
    //----------------------------------

    /**
     * The data associated with the part.
     */
    PartData getData();

    void setData(PartData value);

    //--------------------------------------------------------------------------
    // 
    //  Listeners
    // 
    //--------------------------------------------------------------------------

    void addPartListener(IPartListener value);

    void removePartListener(IPartListener value);

    public interface IPartListener {

        void onMuteChange(IPart part, boolean isMute);

        void onSoloChange(IPart part, boolean isSolo);

        void onSelectedChange(IPart part, boolean isSelected);
    }

}
