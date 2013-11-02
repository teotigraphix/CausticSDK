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

package com.teotigraphix.caustk.live;

import com.teotigraphix.caustk.live.LiveSet.OnLiveSetListener;

/**
 * @author Michael Schmalle
 */
public abstract class ClipSequencer implements OnLiveSetListener {

    private LiveSet liveSet;

    public LiveSet getLiveSet() {
        return liveSet;
    }

    ClipSequencer() {
    }

    ClipSequencer(LiveSet liveSet) {
        this.liveSet = liveSet;
    }

    public abstract void create();

    @Override
    public abstract void onTrackAdded(AudioTrack track, Machine machine);

    @Override
    public abstract void onTrackRemoved(AudioTrack track);
}
