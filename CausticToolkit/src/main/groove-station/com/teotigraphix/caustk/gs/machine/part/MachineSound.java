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

package com.teotigraphix.caustk.gs.machine.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.sound.Patch;
import com.teotigraphix.caustk.gs.memory.Memory.Category;
import com.teotigraphix.caustk.gs.memory.MemorySlotItem;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.gs.memory.item.PhraseMemoryItem;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.Pattern;

/*
 * This class will implement and wrap the Tone API for each machine.
 * 
 * It will allow the Machine's control user interface to implement
 * whatever it wants and access this class's dispatching and OSC action API.
 * 
 * The class has no user interface.
 */

/**
 * Holds the model and OSC actions for the {@link MachineControls} part.
 */
public class MachineSound extends MachineComponentPart {

    //--------------------------------------------------------------------------
    // Public API
    //--------------------------------------------------------------------------

    //----------------------------------
    // Parts
    //----------------------------------

    private List<Part> parts = new ArrayList<Part>();

    public Part getPart(int index) {
        return parts.get(index);
    }

    public List<Part> getParts() {
        return Collections.unmodifiableList(parts);
    }

    private Part selectedPart;

    /**
     * The selected part of the pattern.
     */
    @SuppressWarnings("unchecked")
    public <T extends Part> T getSelectedPart() {
        return (T)selectedPart;
    }

    public void setSelectedPart(int partIndex) {
        Part part = parts.get(partIndex);
        selectedPart = part;
        final Part oldPart = selectedPart;
        for (OnMachineSoundListener listener : onMachineSoundListener) {
            listener.onSelectedPartChange(selectedPart, oldPart);
        }
    }

    public void addPart(Part part) {
        parts.add(part);
    }

    public final Patch getSelectedPatch() {
        return getSelectedPart().getPatch();
    }

    //----------------------------------
    // octave
    //----------------------------------

    final int[] octaves = new int[] {
            -4, -3, -2, -1, 0, 2, 3, 4
    };

    private int octaveIndex = 4;

    public int getOctaveIndex() {
        return octaveIndex;
    }

    public void setOctaveIndex(int value) {
        octaveIndex = value;
        if (octaveIndex > 7)
            octaveIndex = 7;
        else if (octaveIndex < 0)
            octaveIndex = 0;
        setOctave(octaves[octaveIndex]);
        CtkDebug.log("O:" + octave);
    }

    public void incrementOctave() {
        octaveIndex++;
        if (octaveIndex > 7)
            octaveIndex = 7;
        setOctave(octaves[octaveIndex]);
    }

    public void deccrementOctave() {
        octaveIndex--;
        if (octaveIndex < 0)
            octaveIndex = 0;
        setOctave(octaves[octaveIndex]);
    }

    private int octave = -1;

    public int getOctave() {
        return octave;
    }

    public void setOctave(int value) {
        octave = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineSound(GrooveMachine grooveMachine) {
        super(grooveMachine);
    }

    public void configure(Pattern pattern) {
        // add parts the the pattern
        for (Part part : getParts()) {
            part.setPattern(pattern);
        }

        for (Part part : getParts()) {
            getMemoryBank().copyPatch(part, pattern.getIndex());
            getMemoryBank().copyPhrase(part, pattern.getIndex());
            //part.getPhrase().configure();
        }
    }

    public MemorySlotItem createInitData(Category category) {
        switch (category) {
            case PATCH:
                return null;
            case PATTERN:
                return createPatternInitData();
            case PATTERN_SET:
                return null;
            case PHRASE:
                return createPhraseInitData();
            case RPSSET:
                return null;
            case SONG:
                return null;
        }
        return null;
    }

    protected PatternMemoryItem createPatternInitData() {
        return new PatternMemoryItem();
    }

    protected MemorySlotItem createPhraseInitData() {
        return new PhraseMemoryItem();
    }

    /**
     * Sounds a note from the machine's sound engine.
     * 
     * @param pitch The relative pitch without the octave.
     * @param velocity The velocity of the note.
     */
    public void noteOn(int pitch, float velocity) {
        int root = 60 + (octaves[octaveIndex] * 12);
        getSelectedPart().getPatch().noteOn(root + pitch, velocity);
    }

    /**
     * Stops a note from the machine's sound engine.
     * 
     * @param pitch The relative pitch without the octave.
     */
    public void noteOff(int pitch) {
        int root = 60 + (octaves[octaveIndex] * 12);
        getSelectedPart().getPatch().noteOff(root + pitch);
    }

    //--------------------------------------------------------------------------
    // Listeners
    //--------------------------------------------------------------------------

    private List<OnMachineSoundListener> onMachineSoundListener = new ArrayList<OnMachineSoundListener>();

    public void addOnMachineSequencerListener(OnMachineSoundListener l) {
        onMachineSoundListener.add(l);
    }

    public interface OnMachineSoundListener {
        void onSelectedPartChange(Part part, Part oldPart);
    }

}
