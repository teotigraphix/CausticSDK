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

/**
 * @author Michael Schmalle
 */
public class SessionClip {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private SessionScene scene;

    @Tag(1)
    private SessionClipState state;

    @Tag(2)
    private String name;

    @Tag(3)
    private Object color;

    @Tag(4)
    private boolean selected;

    @Tag(5)
    private int bankIndex;

    @Tag(6)
    private int patternIndex;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // scene
    //----------------------------------

    public SessionScene getScene() {
        return scene;
    }

    //----------------------------------
    // state
    //----------------------------------

    /**
     * The clips current state.
     */
    public SessionClipState getState() {
        return state;
    }

    public void setState(SessionClipState value) {
        state = value;
    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * The display name of the clip.
     */
    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    //----------------------------------
    // color
    //----------------------------------

    /**
     * The color of the clip.
     */
    public Object getColor() {
        return color;
    }

    public void setColor(Object value) {
        color = value;
    }

    //----------------------------------
    // selected
    //----------------------------------

    /**
     * Whether the clip is selected in the user interface.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the clip selected or unselected.
     * 
     * @param value Whether the clip is selected.
     */
    public void setSelected(boolean value) {
        selected = value;
    }

    //----------------------------------
    // bankIndex
    //----------------------------------

    /**
     * Returns the native bank index assignment for this clip in a
     * pattern_sequencer (0..3).
     */
    public int getBankIndex() {
        return bankIndex;
    }

    //----------------------------------
    // patternIndex
    //----------------------------------

    /**
     * Returns the native pattern index assignment for this clip in a
     * pattern_sequencer (0..15).
     */
    public int getPatternIndex() {
        return patternIndex;
    }

    //----------------------------------
    // track
    //----------------------------------

    /**
     * The owner {@link AudioTrack}.
     */
    public AudioTrack getTrack() {
        return scene.getTrack();
    }

    /**
     * Returns the index of the {@link AudioTrack} that owns this clip.
     * <p>
     * In the {@link SessionSequencer}, this refers to the column index.
     */
    public int getIndex() {
        return scene.getTrack().getIndex();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    SessionClip() {
    }

    SessionClip(SessionScene scene) {
        this.scene = scene;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void play() {

    }

    public void stop() {

    }

    public void togglePlay() {

    }

}
