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
    private LiveSet liveSet;

    @Tag(2)
    private Machine machine;

    @Tag(3)
    private AudioTrackMixer mixer;

    @Tag(10)
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
    // liveSet
    //----------------------------------

    public LiveSet getLiveSet() {
        return liveSet;
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
     * {@link Machine#getMachineIndex()}.
     */
    public int getIndex() {
        // XXX Master override
        if (machine == null)
            return -1;
        return machine.getMachineIndex();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    AudioTrack() {
    }

    public AudioTrack(AudioTrackInfo info, LiveSet liveSet) {
        this.info = info;
        this.liveSet = liveSet;
        // XXX is master
    }

    public AudioTrack(AudioTrackInfo info, LiveSet liveSet, Machine machine) {
        this.info = info;
        this.liveSet = liveSet;
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
        //        machine.getRackSet().removeMachine(machine);
        machine = null;
    }
}
