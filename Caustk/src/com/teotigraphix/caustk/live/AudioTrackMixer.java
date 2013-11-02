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


/*
 * - pan
 * - volume
 * - mute
 * - solo
 * - record
 */

/**
 * @author Michael Schmalle
 */
public class AudioTrackMixer {

    private AudioTrack track;

    public AudioTrack getTrack() {
        return track;
    }

    public Machine getMachine() {
        return track.getMachine();
    }

    protected MixerPreset getMixer() {
        return getMachine().getMixer();
    }

    //----------------------------------
    // pan
    //----------------------------------

    public float getPan() {
        return getMixer().getPan();
    }

    public void setPan(float pan) {
        getMixer().setPan(pan);
    }

    //----------------------------------
    // volume
    //----------------------------------

    public float getVolume() {
        return getMixer().getVolume();
    }

    public void setVolume(float volume) {
        getMixer().setVolume(volume);
    }

    //----------------------------------
    // mute
    //----------------------------------

    public boolean isMute() {
        return getMixer().isMute();
    }

    public void setMute(boolean mute) {
        getMixer().setMute(mute);
    }

    //----------------------------------
    // solo
    //----------------------------------

    public boolean isSolo() {
        return getMixer().isSolo();
    }

    public void setSolo(boolean solo) {
        getMixer().setSolo(solo);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    AudioTrackMixer() {
    }

    AudioTrackMixer(AudioTrack track) {
        this.track = track;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void dispose() {
    }

}
