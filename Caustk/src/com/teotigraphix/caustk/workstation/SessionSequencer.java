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

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

/**
 * @author Michael Schmalle
 */
public class SessionSequencer extends ClipSequencer {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    List<SessionScene> scenes = new ArrayList<SessionScene>();

    public List<SessionScene> getScenes() {
        return new ArrayList<SessionScene>(scenes);
    }

    SessionSequencer() {
    }

    public SessionSequencer(LiveSet liveSet) {
        super(liveSet);
    }

    @Override
    public void create() {
    }

    public SessionScene addScene(String name) {
        return addScene(scenes.size(), name);
    }

    public SessionScene addScene(int index, String name) {
        SessionScene scene = new SessionScene(name, this);
        scenes.add(index, scene);
        return scene;
    }

    @Override
    public void onTrackAdded(AudioTrack track, Machine machine) {
        // XXX In the context of the session sequencer I really
        // don't see this doing anything right now other than invalidating
        // graphics to redraw the pane

        for (SessionScene scene : scenes) {
            scene.onTrackAdded(track, machine);
        }
    }

    @Override
    public void onTrackRemoved(AudioTrack track) {
        // Will remove all SessionClip instances 

        for (SessionScene scene : scenes) {
            scene.onTrackRemoved(track);
        }
    }

}
