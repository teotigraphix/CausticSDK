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

package com.teotigraphix.caustk.machine;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

/*
 * LiveSet
 *   - [i] AudioTrack
 *   
 *   - SessionSequencer
 *     - [i] SessionScene
 *       - [i] SessionClip
 *   
 *   - ArrangementSequencer
 * 
 * - When an AudioTrack is added to a LiveSet, the session and arrangement
 *   sequencers will have there add callbacks fired.
 * - When an AudioTrack is removed from a LiveSet, the session and arrangement
 *   sequencers will have there remove callbacks fired.
 *   
 * - The two sub sequencers will always be in sync with the LiveSet's 
 *   Real AudioTrack map
 */

/**
 * @author Michael Schmalle
 */
public class AudioTrack {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private AudioTrackInfo info;

    @Tag(1)
    private Machine machine;

    @Tag(2)
    private AudioTrackMixer mixer;

    @Tag(3)
    private boolean selected;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // info
    //----------------------------------

    /**
     * Returns the {@link AudioTrack}'s meta data information.
     */
    public AudioTrackInfo getInfo() {
        return info;
    }

    //----------------------------------
    // machine
    //----------------------------------

    /**
     * Returns the {@link Machine} that owns this audio track.
     */
    public Machine getMachine() {
        return machine;
    }

    //----------------------------------
    // machine
    //----------------------------------

    /**
     * Returns the track's {@link AudioTrackMixer}.
     */
    public AudioTrackMixer getMixer() {
        return mixer;
    }

    //----------------------------------
    // machine
    //----------------------------------

    /**
     * Whether the track is the selected track of the {@link LiveSet}.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected track within the {@link LiveSet}.
     * 
     * @param selected Whether selected or not.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the user defined, display name of the audio track.
     * <p>
     * This name is not the same name that is assigned to the {@link Machine}.
     */
    public String getName() {
        return info.getName();
    }

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the index of the audio track, this value is the
     * {@link Machine#getIndex()}.
     */
    public int getIndex() {
        return machine.getIndex();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    AudioTrack() {
    }

    public AudioTrack(AudioTrackInfo info, Machine machine) {
        this.info = info;
        this.machine = machine;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void create() {
        mixer = new AudioTrackMixer(this);
    }

    /**
     * Disposes the {@link AudioTrack}, once this method has been called, the
     * track cannot be reused.
     * <p>
     * Removes the {@link Machine} from the {@link RackSet}.
     */
    public void dispose() {
        mixer.dispose();
        machine.getRackSet().removeMachine(machine);
        machine = null;
    }

    /**
     * Information object for the {@link AudioTrack}.
     * 
     * @author Michael Schmalle
     */
    public static class AudioTrackInfo {

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
}
