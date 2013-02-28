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

package com.teotigraphix.caustic.song;

import java.util.List;

import com.teotigraphix.caustic.part.IPart;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPartPattern extends IPattern {

    //--------------------------------------------------------------------------
    // 
    //  Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  parts
    //----------------------------------

    /**
     * Return the IPattern's IPart collection.
     * 
     * @return
     */
    List<IPart> getParts();

    IPart getPartAt(int index);

    //----------------------------------
    //  selectedPart
    //----------------------------------

    /**
     * Return the IPattern's selected IPart.
     */
    IPart getSelectedPart();

    void setSelectedPart(IPart value);

    //--------------------------------------------------------------------------
    // 
    //  Methods
    // 
    //--------------------------------------------------------------------------

    IPart addPart(IPart part);

    IPart removePart(IPart part);

    void addAllParts(List<IPart> parts);

    void removeAllParts();

    void addPartPatternListener(IPartPatternListener value);

    void removePartPatternListener(IPartPatternListener value);

    public interface IPartPatternListener {

        /**
         * @param part The IPart added.
         */
        void onPartAdded(IPart part);

        /**
         * @param part The IPart removed.
         */
        void onPartRemoved(IPart part);

        /**
         * @param selectedPart The new selected IPart.
         * @param oldSlectedPart The old selected IPart.
         */
        void onSelectedPartChanged(IPart selectedPart, IPart oldSlectedPart);
    }
}
