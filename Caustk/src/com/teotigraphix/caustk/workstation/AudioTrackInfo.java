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
 * Information object for the {@link AudioTrack}.
 * 
 * @author Michael Schmalle
 */
public class AudioTrackInfo {

    private String name;

    private String infoText;

    private Object color;

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the user defined, display name of the audio track.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------
    // infoText
    //----------------------------------

    /**
     * Returns the information text attached to the {@link AudioTrack}.
     */
    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    //----------------------------------
    // color
    //----------------------------------

    /**
     * Returns the Color of the {@link AudioTrack}.
     */
    public Object getColor() {
        return color;
    }

    public void setColor(Object color) {
        this.color = color;
    }

    public AudioTrackInfo(String name) {
        this.name = name;
    }
}
