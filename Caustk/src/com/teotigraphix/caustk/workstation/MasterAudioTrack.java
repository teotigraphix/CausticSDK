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

/**
 * @author Michael Schmalle
 */
public class MasterAudioTrack extends AudioTrack {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Override
    public final int getIndex() {
        return -1;
    }

    @Override
    public final String getName() {
        return "Master";
    }

    @Override
    public final Machine getMachine() {
        throw new IllegalStateException("Cannot call getMachine() on Master track");
    }

    @Override
    public final boolean isSelected() {
        return false;
    }

    @Override
    public final boolean isMaster() {
        return true;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    MasterAudioTrack() {
    }

    public MasterAudioTrack(AudioTrackInfo info, TrackSet trackSet) {
        super(info, trackSet, null);
    }
}
