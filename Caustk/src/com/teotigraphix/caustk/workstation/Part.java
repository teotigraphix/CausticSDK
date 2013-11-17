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
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @author Michael Schmalle
 */
public class Part extends CaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private GrooveBox grooveBox;

    @Tag(101)
    private Machine machine;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return machine.getDefaultName();
    }

    //----------------------------------
    // grooveBox
    //----------------------------------

    public GrooveBox getGrooveBox() {
        return grooveBox;
    }

    //----------------------------------
    // patternBank
    //----------------------------------

    /**
     * Returns the owning {@link PatternBank}.
     */
    public PatternBank getPatternBank() {
        return grooveBox.getPatternBank();
    }

    //----------------------------------
    // machine
    //----------------------------------

    /**
     * Returns the owning {@link Machine} in the {@link PatternBank}'s
     * {@link RackSet}.
     */
    public Machine getMachine() {
        return machine;
    }

    public int getMachineIndex() {
        return machine.getMachineIndex();
    }

    //----------------------------------
    // patch
    //----------------------------------

    /**
     * Returns the {@link Machine}s {@link Patch}.
     */
    public Patch getPatch() {
        return machine.getPatch();
    }

    //----------------------------------
    // phrase
    //----------------------------------

    /**
     * Returns the current {@link Pattern}'s part {@link Phrase} using the
     * selected index of the {@link PatternBank}.
     * <p>
     * If this part's index is 1 and the pattern set's selected index is 3, then
     * the {@link Phrase} for native machine 1 at A04 will be returned.
     */
    public final Phrase getPhrase() {
        return getPhrase(PatternUtils.getBank(getPatternBank().getSelectedIndex()),
                PatternUtils.getPattern(getPatternBank().getSelectedIndex()));
    }

    /**
     * Returns a {@link Phrase} for this part's {@link Machine} at the bank and
     * pattern index.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     */
    public final Phrase getPhrase(int bankIndex, int patternIndex) {
        return machine.getPhrase(bankIndex, patternIndex);
    }

    //----------------------------------
    // isRhythm
    //----------------------------------

    public boolean isRhythm() {
        // TODO Auto-generated method stub
        return false;
    }

    //----------------------------------
    // phrase
    //----------------------------------

    public boolean istMute() {
        return machine.isMute();
    }

    public void setMute(boolean muted) {
        machine.setMute(muted);
    }

    //----------------------------------
    // phrase
    //----------------------------------

    public int getLength() {
        return getPhrase().getLength();
    }

    public void setLength(int value) {
        getPhrase().setLength(value);
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
        //        getLogger().log("MachineSound", "O:" + octave);
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
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Part() {
    }

    Part(ComponentInfo info, GrooveBox grooveBox, Machine machine) {
        setInfo(info);
        this.grooveBox = grooveBox;
        this.machine = machine;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void transpose(int octave) {
        getPhrase().transpose(octave);
    }

    public int toPitch(int pitch) {
        int root = 60 + (octaves[octaveIndex] * 12);
        return root + pitch;
    }

    /**
     * Sounds a note from the machine's sound engine.
     * 
     * @param pitch The relative pitch without the octave.
     * @param velocity The velocity of the note.
     */
    public void noteOn(int pitch, float velocity) {
        int root = 60 + (octaves[octaveIndex] * 12);
        machine.getRackTone().getSynth().noteOn(root + pitch, velocity);
    }

    /**
     * Stops a note from the machine's sound engine.
     * 
     * @param pitch The relative pitch without the octave.
     */
    public void noteOff(int pitch) {
        int root = 60 + (octaves[octaveIndex] * 12);
        machine.getRackTone().getSynth().noteOff(root + pitch);
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

}
