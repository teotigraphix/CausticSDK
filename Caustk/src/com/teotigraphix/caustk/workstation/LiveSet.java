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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.IRack;

/**
 * @author Michael Schmalle
 */
public class LiveSet implements ICaustkComponent, IRackSerializer {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private transient RackSet rackSet;

    private ComponentInfo info;

    private Map<Integer, AudioTrack> tracks = new HashMap<Integer, AudioTrack>();

    private AudioTrack masterTrack;

    public AudioTrack getMasterTrack() {
        return masterTrack;
    }

    private SessionSequencer sessionSequencer;

    public SessionSequencer getSessionSequencer() {
        return sessionSequencer;
    }

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

    LiveSet(ComponentInfo info, RackSet rackSet) {
        this.info = info;
        this.rackSet = rackSet;

        masterTrack = new AudioTrack(new AudioTrackInfo("Master"), this);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    //----------------------------------
    // tracks
    //----------------------------------

    /**
     * @param index
     * @param machineType
     * @param machineName
     * @param displayName
     * @see {@link OnLiveSetChange}
     * @return
     * @throws CausticException
     */
    public AudioTrack addTrack(int index, MachineType machineType, String machineName,
            String displayName) throws CausticException {
        Machine machine = rackSet.createMachine(index, machineName, machineType);

        AudioTrackInfo info = new AudioTrackInfo(displayName);
        AudioTrack track = new AudioTrack(info, this, machine);

        tracks.put(index, track);
        trackAdd(track, machine);

        rackSet.getGlobalDispatcher().trigger(
                new OnLiveSetChange(this, LiveSetChangeKind.TrackAdd, track));

        return track;
    }

    /**
     * @param index
     * @see {@link OnLiveSetChange}
     * @return
     * @throws CausticException
     */
    public AudioTrack removeTrack(int index) throws CausticException {
        return removeTrack(tracks.get(index));
    }

    public AudioTrack removeTrack(AudioTrack track) throws CausticException {
        AudioTrack removed = tracks.remove(track);
        trackRemove(track);

        rackSet.getGlobalDispatcher().trigger(
                new OnLiveSetChange(this, LiveSetChangeKind.TrackRemove, track));

        return removed;
    }

    //----------------------------------
    // scenes
    //----------------------------------

    public void addScene(String name) {
        SessionScene scene = sessionSequencer.addScene();
    }

    @Override
    public void disconnect() {
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

    @Override
    public void create(ICaustkApplicationContext context) throws CausticException {
        sessionSequencer = new SessionSequencer(this);
        arrangementSequencer = new ArrangementSequencer(this);

        listeners.add(sessionSequencer);
        listeners.add(arrangementSequencer);

        sessionSequencer.create();
        arrangementSequencer.create();
    }

    @Override
    public void load(ICaustkApplicationContext context) throws CausticException {
    }

    @Override
    public void update(ICaustkApplicationContext context) {
    }

    @Override
    public void restore() {
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onSave() {
    }

    public enum LiveSetChangeKind {
        TrackAdd,

        TrackRemove
    }

    /**
     * @author Michael Schmalle
     * @see IRack#getGlobalDispatcher()
     */
    public static class OnLiveSetChange {

        private LiveSet liveSet;

        private LiveSetChangeKind kind;

        private AudioTrack track;

        public LiveSet getLiveSet() {
            return liveSet;
        }

        public LiveSetChangeKind getKind() {
            return kind;
        }

        /**
         * The added or removed track.
         */
        public AudioTrack getTrack() {
            return track;
        }

        public OnLiveSetChange(LiveSet liveSet, LiveSetChangeKind kind) {
            this.liveSet = liveSet;
            this.kind = kind;
        }

        public OnLiveSetChange(LiveSet liveSet, LiveSetChangeKind kind, AudioTrack track) {
            this.liveSet = liveSet;
            this.kind = kind;
            this.track = track;
        }
    }
}
