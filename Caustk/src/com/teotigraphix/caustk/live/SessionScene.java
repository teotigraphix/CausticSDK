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

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Michael Schmalle
 */
public class SessionScene {

    private String name;

    private int bankIndex;

    private int patternIndex;

    private AudioTrack track;

    public String getName() {
        return name;
    }

    public int getBankIndex() {
        return bankIndex;
    }

    public int getPatternIndex() {
        return patternIndex;
    }

    public AudioTrack getTrack() {
        return track;
    }

    private Map<Integer, SessionClip> map = new TreeMap<Integer, SessionClip>();

    public SessionClip getClip(int index) {
        return map.get(index);
    }

    public SessionScene() {
    }

}
