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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;

/**
 * @author Michael Schmalle
 */
public class Song extends CaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private SongBank songBank;

    @Tag(101)
    private int index;

    @Tag(102)
    private Map<Integer, UUID> patternIds = new HashMap<Integer, UUID>();

    @Tag(103)
    private float tempo = 120f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return null;
    }

    //----------------------------------
    // songBank
    //----------------------------------

    public SongBank getSongBank() {
        return songBank;
    }

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return index;
    }

    //----------------------------------
    // patternIds
    //----------------------------------

    /**
     * Returns a map of {@link Pattern} {@link UUID}s that can be used to load
     * the serialized {@link Pattern} instances against the
     * {@link SongBank#getPatternBankId()}.
     * <p>
     * Using the song set's pattern set id, to load the {@link PatternBank} and
     * then this map to gain access to the serialized {@link Pattern}, the
     * correct data can be loaded.
     */
    public Map<Integer, UUID> getPatternIds() {
        return patternIds;
    }

    //----------------------------------
    // tempo
    //----------------------------------

    public float getTempo() {
        return tempo;
    }

    /**
     * Sets the pattern tempo, TODO
     * 
     * @param value (60..250)
     * @see OnSongChange
     * @see SongChangeKind#Tempo
     */
    public void setTempo(float value) {
        if (value == tempo)
            return;
        tempo = value;
        // patternSet.getRackSet().getSequencer().setBPM(tempo);
        trigger(new OnSongChange(this, SongChangeKind.Tempo));
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Song() {
    }

    Song(ComponentInfo info, SongBank songBank) {
        setInfo(info);
        this.songBank = songBank;
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Connect:
                break;
            case Create:
                break;
            case Disconnect:
                break;
            case Load:
                break;
            case Restore:
                break;
            case Update:
                break;
        }
    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    private void trigger(Object event) {
        songBank.getPatternBank().getRackSet().getComponentDispatcher().trigger(event);
    }

    public enum SongChangeKind {

        Tempo
    }

    public static class OnSongChange {

        private Song song;

        private SongChangeKind kind;

        public Song getSong() {
            return song;
        }

        public SongChangeKind getKind() {
            return kind;
        }

        public OnSongChange(Song song, SongChangeKind kind) {
            this.song = song;
            this.kind = kind;
        }
    }
}
