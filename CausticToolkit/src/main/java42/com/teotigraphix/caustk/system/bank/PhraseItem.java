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

package com.teotigraphix.caustk.system.bank;


public class PhraseItem extends MemorySlotItem {
    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultPatch
    //----------------------------------

    private final PatchItem defaultPatch;

    /**
     * The default patch serialization of the machine this {@link PhraseItem}
     * was unserialized from.
     * <p>
     * This is to allow the default sound of a {@link Phrase} to be that of the
     * sound it was created with.
     */
    public PatchItem getDefaultPatch() {
        return defaultPatch;
    }

    //----------------------------------
    // length
    //----------------------------------

    private int length;

    /**
     * The length of the {@link IMachine} pattern loaded.
     */
    public int getLength() {
        return length;
    }

    //----------------------------------
    // resolution
    //----------------------------------

    private Resolution resolution = Resolution.SIXTEENTH;

    /**
     * The resolution computed by the smallest loaded gate time in the
     * {@link IMachine} pattern.
     */
    public Resolution getResolution() {
        return resolution;
    }

    //----------------------------------
    // data
    //----------------------------------

    private String data;

    /**
     * The raw string data of all the note triggers in the pattern.
     * <p>
     * The String has not yet been split by the <code>|</code> separator.
     */
    public String getData() {
        return data;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public PhraseItem(String data, int length, PatchItem defaultPatch) {
        this.data = data;
        this.length = length;
        this.defaultPatch = defaultPatch;

        if (data != null && !data.isEmpty())
            this.resolution = calculateResolution(data);
    }

    //--------------------------------------------------------------------------
    // Private Method API
    //--------------------------------------------------------------------------

    private Resolution calculateResolution(String data) {
        // TODO This is totally inefficient, needs to be lazy loaded
        // push the notes into the machines sequencer
        float smallestGate = 1f;
        String[] notes = data.split("\\|");
        for (String noteData : notes) {
            String[] split = noteData.split(" ");

            float start = Float.parseFloat(split[0]);
            float end = Float.parseFloat(split[3]);
            float gate = end - start;
            smallestGate = Math.min(smallestGate, gate);
        }

        Resolution result = Resolution.SIXTEENTH;
        if (smallestGate <= Resolution.SIXTYFOURTH.getValue() * 4)
            result = Resolution.SIXTYFOURTH;
        else if (smallestGate <= Resolution.THIRTYSECOND.getValue() * 4)
            result = Resolution.THIRTYSECOND;
        else if (smallestGate <= Resolution.SIXTEENTH.getValue() * 4)
            result = Resolution.SIXTEENTH;

        return result;
    }
}
