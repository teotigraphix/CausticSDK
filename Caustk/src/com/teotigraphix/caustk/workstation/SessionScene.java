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

import com.teotigraphix.caustk.workstation.LiveSet.OnLiveSetListener;

/**
 * @author Michael Schmalle
 */
public class SessionScene implements OnLiveSetListener {

    private SessionSequencer sessionSequencer;

    public SessionSequencer getSessionSequencer() {
        return sessionSequencer;
    }

    private String name;

    private int index;

    private int bankIndex;

    private int patternIndex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBankIndex() {
        return bankIndex;
    }

    public int getPatternIndex() {
        return patternIndex;
    }

    public AudioTrack getTrack() {
        return sessionSequencer.getLiveSet().getTrack(index);
    }

    private Map<Integer, SessionClip> map = new TreeMap<Integer, SessionClip>();

    public SessionClip getClip(int index) {
        return map.get(index);
    }

    SessionScene() {
    }

    SessionScene(String name, SessionSequencer sessionSequencer) {
        this.name = name;
        this.sessionSequencer = sessionSequencer;
    }

    //--------------------------------------------------------------------------

    /*
     * A session scene holds SessionClips, when play() or stop() is called
     * the scene proxies to all clips it holds
     */

    public void play() {

    }

    public void stop() {

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
