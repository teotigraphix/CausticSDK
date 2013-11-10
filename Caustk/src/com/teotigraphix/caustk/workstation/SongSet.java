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
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @author Michael Schmalle
 */
public class SongSet extends CaustkComponent {

    private transient PatternSet patternSet;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private UUID patternSetId;

    @Tag(101)
    private Map<Integer, Song> songs = new TreeMap<Integer, Song>();

    @Tag(102)
    private int selectedIndex = 0;

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
    // patternSetId
    //----------------------------------

    /**
     * Returns the {@link PatternSet} id that holds the {@link Pattern}s this
     * song references.
     */
    public UUID getPatternSetId() {
        return patternSetId;
    }

    //----------------------------------
    // selectedIndex
    //----------------------------------

    public Song getSelectedSong() {
        return getSong(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @param value
     * @see OnSongSetChange
     * @see SongSetChangeKind#SelectedIndex
     */
    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        int oldIndex = selectedIndex;
        selectedIndex = value;
        trigger(new OnSongSetChange(this, SongSetChangeKind.SelectedIndex, selectedIndex, oldIndex));
    }

    //----------------------------------
    // patternSet
    //----------------------------------

    /**
     * Returns the active {@link PatternSet} instance for this song set.
     * <p>
     * The {@link PatternSet}'s id is the same as the {@link #getPatternSetId()}.
     */
    public PatternSet getPatternSet() {
        return patternSet;
    }

    /**
     * Returns the {@link Song} at the linear index (0..62).
     * 
     * @param index The linear index.
     */
    public Song getSong(int index) {
        return songs.get(index);
    }

    /**
     * Increments and returns the next song (0..63), when 64 is reached, the
     * index wraps around to 0.
     * 
     * @return The new selected {@link Song}.
     * @see #setSelectedIndex(int)
     */
    public Song incrementIndex() {
        int index = selectedIndex + 1;
        if (index > 63)
            index = 0;
        setSelectedIndex(index);
        return getSelectedSong();
    }

    /**
     * Decrements and returns the next song (0..63), when 0 is reached, the
     * index wraps around to 63.
     * 
     * @return The new selected {@link Song}.
     * @see #setSelectedIndex(int)
     */
    public Song decrementIndex() {
        int index = selectedIndex - 1;
        if (index < 0)
            index = 63;
        setSelectedIndex(index);
        return getSelectedSong();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    SongSet() {
    }

    SongSet(ComponentInfo info, UUID patternSetId) {
        setInfo(info);
        this.patternSetId = patternSetId;
    }

    SongSet(ComponentInfo info, PatternSet patternSet) {
        setInfo(info);
        this.patternSet = patternSet;
        this.patternSetId = patternSet.getInfo().getId();
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Connect:
                break;
            case Create:
                for (int i = 0; i < 64; i++) {
                    final ICaustkFactory factory = context.getFactory();
                    String name = PatternUtils.toString(i);
                    ComponentInfo info = factory.createInfo(ComponentType.Song, name);
                    Song song = factory.createSong(info, this);
                    addSong(song);
                }
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

    void addSong(Song song) {
        songs.put(song.getIndex(), song);
        songAdd(song);
    }

    private void songAdd(Song song) {
        // TODO Auto-generated method stub

    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    private void trigger(Object event) {
        patternSet.getRackSet().getComponentDispatcher().trigger(event);
    }

    public enum SongSetChangeKind {
        SongAdd,

        SongRemove,

        SongReplace,

        SelectedIndex,
    }

    /**
     * @author Michael Schmalle
     * @see RackSet#getComponentDispatcher()
     */
    public static class OnSongSetChange {

        private SongSet songSet;

        private SongSetChangeKind kind;

        private int index;

        private int oldIndex;

        public SongSet getSongSet() {
            return songSet;
        }

        public SongSetChangeKind getKind() {
            return kind;
        }

        /**
         * @see SongSetChangeKind#SelectedIndex
         */
        public int getIndex() {
            return index;
        }

        /**
         * @see SongSetChangeKind#SelectedIndex
         */
        public int getOldIndex() {
            return oldIndex;
        }

        public OnSongSetChange(SongSet songSet, SongSetChangeKind kind) {
            this.songSet = songSet;
            this.kind = kind;
        }

        public OnSongSetChange(SongSet songSet, SongSetChangeKind kind, int index, int oldIndex) {
            this.songSet = songSet;
            this.kind = kind;
            this.index = index;
            this.oldIndex = oldIndex;
        }
    }
}
