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

import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.workstation.LiveSet.OnLiveSetListener;

/**
 * @author Michael Schmalle
 */
public class SessionScene implements OnLiveSetListener {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private SessionSequencer sessionSequencer;

    @Tag(1)
    private Map<Integer, SessionClip> clips = new TreeMap<Integer, SessionClip>();

    @Tag(2)
    private int index;

    @Tag(3)
    private String name;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // sessionSequencer
    //----------------------------------

    public SessionSequencer getSessionSequencer() {
        return sessionSequencer;
    }

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return index;
    }

    //----------------------------------
    // name
    //----------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------
    // track
    //----------------------------------

    public AudioTrack getTrack() {
        return sessionSequencer.getLiveSet().getTrack(index);
    }

    //----------------------------------
    // clip
    //----------------------------------

    public SessionClip getClip(int index) {
        return clips.get(index);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    SessionScene() {
    }

    SessionScene(int index, String name, SessionSequencer sessionSequencer) {
        this.index = index;
        this.name = name;
        this.sessionSequencer = sessionSequencer;
    }

    //--------------------------------------------------------------------------

    /*
     * A session scene holds SessionClips, when play() or stop() is called
     * the scene proxies to all clips it holds
     */

    public void play() {
        for (SessionClip sessionClip : clips.values()) {
            sessionClip.play();
        }
    }

    public void stop() {
        for (SessionClip sessionClip : clips.values()) {
            sessionClip.stop();
        }
    }

    public void togglePlay() {

    }

    @Override
    public void onTrackAdded(AudioTrack track, Machine machine) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTrackRemoved(AudioTrack track) {
        // TODO Auto-generated method stub

    }

}
