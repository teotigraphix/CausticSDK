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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teotigraphix.caustk.core.CausticException;

/**
 * @author Michael Schmalle
 */
public class LiveSet implements ICaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private transient RackSet rackSet;

    private ComponentInfo info;

    private Map<Integer, AudioTrack> tracks = new HashMap<Integer, AudioTrack>();

    private SessionSequencer sessionSequencer;

    private ArrangementSequencer arrangementSequencer;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public final ComponentInfo getInfo() {
        return info;
    }

    @Override
    public String getDefaultName() {
        return info.getName();
    }

    //----------------------------------
    // track
    //----------------------------------

    public AudioTrack getTrack(int index) {
        return tracks.get(index);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    LiveSet() {
    }

    LiveSet(ComponentInfo info) {
        this.info = info;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onSave() {
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void create() {
        sessionSequencer = new SessionSequencer(this);
        arrangementSequencer = new ArrangementSequencer(this);

        listeners.add(sessionSequencer);
        listeners.add(arrangementSequencer);

        sessionSequencer.create();
        arrangementSequencer.create();
    }

    public AudioTrack addTrack(int index, MachineType machineType, String machineName,
            String displayName) throws CausticException {
        Machine machine = rackSet.createMachine(index, machineName, machineType);

        AudioTrackInfo info = new AudioTrackInfo(displayName);
        AudioTrack track = new AudioTrack(info, machine);

        tracks.put(index, track);
        trackAdd(track, machine);

        return track;
    }

    public AudioTrack removeTrack(int index) throws CausticException {
        return removeTrack(tracks.get(index));
    }

    public AudioTrack removeTrack(AudioTrack track) throws CausticException {
        AudioTrack removed = tracks.remove(track);
        trackRemove(track);
        return removed;
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void trackAdd(AudioTrack track, Machine machine) {
        track.create();
        for (OnLiveSetListener l : listeners) {
            l.onTrackAdded(track, machine);
        }
    }

    private void trackRemove(AudioTrack track) {
        track.dispose();
        for (OnLiveSetListener l : listeners) {
            l.onTrackRemoved(track);
        }
    }

    private List<OnLiveSetListener> listeners = new ArrayList<OnLiveSetListener>();

    public interface OnLiveSetListener {
        void onTrackAdded(AudioTrack track, Machine machine);

        void onTrackRemoved(AudioTrack track);
    }

}
