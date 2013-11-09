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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.IRack;

/**
 * @author Michael Schmalle
 */
public class TrackSet extends CaustkComponent {

    private transient RackSet rackSet;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private AudioTrack masterTrack;

    @Tag(101)
    private Map<Integer, AudioTrack> tracks = new HashMap<Integer, AudioTrack>();

    @Tag(102)
    private SessionSequencer sessionSequencer;

    @Tag(103)
    private ArrangementSequencer arrangementSequencer;

    @Tag(104)
    private int selectedTrackIndex = -1;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return getInfo().getName();
    }

    //----------------------------------
    // track
    //----------------------------------

    public AudioTrack getMasterTrack() {
        return masterTrack;
    }

    //----------------------------------
    // track
    //----------------------------------

    public AudioTrack getTrack(int index) {
        return tracks.get(index);
    }

    public Collection<AudioTrack> getTracks() {
        return tracks.values();
    }

    //----------------------------------
    // sessionSequencer
    //----------------------------------

    public SessionSequencer getSessionSequencer() {
        return sessionSequencer;
    }

    //----------------------------------
    // arrangementSequencer
    //----------------------------------

    public ArrangementSequencer getArrangementSequencer() {
        return arrangementSequencer;
    }

    //----------------------------------
    // selectedTrackIndex
    //----------------------------------

    public int getSelectedTrackIndex() {
        return selectedTrackIndex;
    }

    /**
     * @param value
     * @see OnLiveSetChange
     * @see LiveSetChangeKind#SelectedTrackIndex
     */
    public void setSelectedTrackIndex(int value) {
        if (value == selectedTrackIndex)
            return;
        int oldValue = selectedTrackIndex;
        selectedTrackIndex = value;

        AudioTrack oldAudioTrack = getTrack(oldValue);
        if (oldAudioTrack != null)
            oldAudioTrack.setSelected(false);

        getTrack(selectedTrackIndex).setSelected(true);

        rackSet.getGlobalDispatcher().trigger(
                new OnTrackSetChange(this, TrackSetChangeKind.SelectedTrackIndex,
                        selectedTrackIndex, oldValue));
    }

    //----------------------------------
    // rackSet
    //----------------------------------

    RackSet getRackSet() {
        return rackSet;
    }

    public void setRackSet(RackSet value) {
        rackSet = value;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    TrackSet() {
    }

    TrackSet(ComponentInfo info, RackSet rackSet) {
        setInfo(info);
        this.rackSet = rackSet;

        masterTrack = new MasterAudioTrack(new AudioTrackInfo("Master"), this);
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
                new OnTrackSetChange(this, TrackSetChangeKind.TrackAdd, track));

        setSelectedTrackIndex(index);

        return track;
    }

    public void addPatchUnAssigned(AudioTrack audioTrack, Patch patch) {
        Machine machine = audioTrack.getMachine();
        if (machine.getMachineType() != null)
            throw new IllegalStateException("MachineType not null");

        // 
        try {
            machine.initializeMachine(patch);
        } catch (CausticException e) {
            e.printStackTrace();
        }

        rackSet.getGlobalDispatcher().trigger(
                new OnTrackSetChange(this, TrackSetChangeKind.TrackAssign, audioTrack));
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
                new OnTrackSetChange(this, TrackSetChangeKind.TrackRemove, track));

        return removed;
    }

    //----------------------------------
    // scenes
    //----------------------------------

    public SessionScene addScene() {
        int index = sessionSequencer.getScenes().size();
        return addScene(index, "" + (index + 1));
    }

    public SessionScene addScene(int index, String name) {
        SessionScene scene = sessionSequencer.addScene(index, name);
        rackSet.getGlobalDispatcher().trigger(
                new OnTrackSetChange(this, TrackSetChangeKind.SceneAdd, scene));
        return scene;
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void trackAdd(AudioTrack track, Machine machine) {
        for (OnTrackSetListener l : listeners) {
            l.onTrackAdded(track, machine);
        }
    }

    private void trackRemove(AudioTrack track) {
        track.dispose();
        for (OnTrackSetListener l : listeners) {
            l.onTrackRemoved(track);
        }
    }

    private List<OnTrackSetListener> listeners = new ArrayList<OnTrackSetListener>();

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                sessionSequencer = new SessionSequencer(this);
                arrangementSequencer = new ArrangementSequencer(this);

                listeners.add(sessionSequencer);
                listeners.add(arrangementSequencer);

                sessionSequencer.create();
                arrangementSequencer.create();
                break;

            case Load:
                break;

            case Update:
                // called from ApplicationState.update() deserializng
                for (AudioTrack audioTrack : tracks.values()) {
                    Machine machine = audioTrack.getMachine();
                    machine.update(context);
                }
                break;

            case Restore:
                break;

            case Connect:
                break;

            case Disconnect:
                break;
        }
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onSave() {
    }

    public enum TrackSetChangeKind {
        TrackAdd,

        TrackAssign,

        TrackRemove,

        SelectedTrackIndex,

        SceneAdd,

        SceneRemove
    }

    /**
     * @author Michael Schmalle
     * @see IRack#getGlobalDispatcher()
     */
    public static class OnTrackSetChange {

        private TrackSet trackSet;

        private TrackSetChangeKind kind;

        private AudioTrack track;

        private int trackIndex;

        private int oldTrackIndex;

        private SessionScene scene;

        public TrackSet getTrackSet() {
            return trackSet;
        }

        public TrackSetChangeKind getKind() {
            return kind;
        }

        /**
         * The added or removed track.
         * 
         * @see TrackSetChangeKind#TrackAdd
         * @see TrackSetChangeKind#TrackRemove
         */
        public AudioTrack getTrack() {
            return track;
        }

        /**
         * @see TrackSetChangeKind#SelectedTrackIndex
         */
        public int getTrackIndex() {
            return trackIndex;
        }

        /**
         * @see TrackSetChangeKind#SelectedTrackIndex
         */
        public int getOldTrackIndex() {
            return oldTrackIndex;
        }

        /**
         * @see TrackSetChangeKind#SceneAdd
         * @see TrackSetChangeKind#SceneRemove
         */
        public SessionScene getScene() {
            return scene;
        }

        public OnTrackSetChange(TrackSet trackSet, TrackSetChangeKind kind) {
            this.trackSet = trackSet;
            this.kind = kind;
        }

        public OnTrackSetChange(TrackSet trackSet, TrackSetChangeKind kind, AudioTrack track) {
            this.trackSet = trackSet;
            this.kind = kind;
            this.track = track;
        }

        public OnTrackSetChange(TrackSet trackSet, TrackSetChangeKind kind, int trackIndex,
                int oldTrackIndex) {
            this.trackSet = trackSet;
            this.kind = kind;
            this.trackIndex = trackIndex;
            this.oldTrackIndex = oldTrackIndex;
        }

        public OnTrackSetChange(TrackSet trackSet, TrackSetChangeKind kind, SessionScene scene) {
            this.trackSet = trackSet;
            this.kind = kind;
            this.scene = scene;
        }
    }

    public interface OnTrackSetListener {
        void onTrackAdded(AudioTrack track, Machine machine);

        void onTrackRemoved(AudioTrack track);
    }

}
