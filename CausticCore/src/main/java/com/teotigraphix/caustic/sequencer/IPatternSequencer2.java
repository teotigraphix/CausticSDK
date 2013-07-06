////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.sequencer;

import java.util.List;

import com.teotigraphix.caustic.device.IDeviceComponent;
import com.teotigraphix.caustic.sequencer.IStepPhrase.Resolution;

/**
 * The IPatternSequencer interface controls the selected pattern and pattern
 * bank in the implementing IDevice.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPatternSequencer2 extends IDeviceComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // length
    //----------------------------------

    /**
     * Returns the length of the <em>current</em> bank and pattern in measures,
     * currently with 4 beats per measure.
     */
    int getLength();

    /**
     * Sets the number of measures for the <em>current</em> bank and pattern.
     * 
     * @param value The new number of measures for the pattern.
     */
    void setLength(int value);

    /**
     * Returns the length of the bank and pattern in measures, currently with 4
     * beats per measure.
     * 
     * @param bankIndex The bank index.
     * @param patternIndex The pattern index.
     */
    int getLength(int bankIndex, int patternIndex);

    /**
     * Sets the number of measures for the bank and pattern passed.
     * <p>
     * This method will temporarily set the bank and pattern index to the
     * arguments, the switch back to the current bank and pattern of the
     * sequencer.
     * 
     * @param bankIndex The bank index.
     * @param patternIndex The pattern index.
     * @param value The new number of measures for the pattern.
     */
    void setLength(int bankIndex, int patternIndex, int value);

    //----------------------------------
    // bank/pattern
    //----------------------------------

    /**
     * Sets both bank and pattern, signals a phraseChanged() if the triggerMap
     * exists and has note data.
     * 
     * @param bank The new bank index.
     * @param index The new pattern index.
     */
    void setSelectedPattern(int bank, int index);

    //----------------------------------
    // selectedBank
    //----------------------------------

    /**
     * The currently selected pattern bank of the IDevice.
     * <p>
     * Valid ranges are from [0 to 3], also know as A, B, C and D.
     * </p>
     */
    int getSelectedBank();

    int getSelectedBank(boolean restore);

    //----------------------------------
    // selectedPattern
    //----------------------------------

    /**
     * The currently selected pattern of the selectedBank.
     * <p>
     * Valid ranges are from [0 to 15].
     * </p>
     */
    int getSelectedIndex();

    int getSelectedIndex(boolean restore);

    /**
     * Returns a list of currently filled patterns that contain pattern data.
     * <p>
     * This method queries the core and returns a List of Strings such as [A1,
     * A2, B4, C4] etc.
     * <p>
     * This method should only be used as a reference or to initialize this
     * sequencers pattern on a {@link #restore()}. Never returns
     * <code>null</code>.
     */
    List<String> getPatternListing();

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Clears the pattern sequencer of all banks and pattern data.
     */
    void clear();

    /**
     * Clears all the patterns in the {@link #getSelectedBank()}.
     * 
     * @param index The bank index to clear <code>0..3</code>.
     */
    void clearBank(int index);

    /**
     * Clears the pattern in the {@link #getSelectedBank()} current bank.
     * 
     * @param index The pattern index to clear <code>0..15</code>.
     */
    void clearIndex(int index);

    /**
     * Adds a note beat span in the sequencer for the specified MIDI note.
     * <p>
     * The value of end - start equals the note's gate time. All calls to this
     * method will need client calculation regarding the note's resolution.
     * <p>
     * Using this method <strong>will</strong> send a note_data message to the
     * core.
     * <p>
     * The note flags;
     * <ul>
     * <li>0 - none</li>
     * <li>1 - slide</li>
     * <li>2 - accent</li>
     * </ul>
     * 
     * @param pitch The MIDI note value to add.
     * @param start The start beat in the pattern.
     * @param end The end beat in the pattern when the note stops.
     * @param velocity The velocity of the note on (0-1).
     * @param flags Optional flags to set on the note data.
     * @throws CausticError parameter bounds error
     */
    void addNote(int pitch, float start, float end, float velocity, int flags);

    /**
     * @see #addNote(int, float, float, float, int)
     */
    void addNote(int bankIndex, int patternIndex, int pitch, float start, float end,
            float velocity, int flags);

    /**
     * Removes a note beat span in the sequencer for the specified MIDI note.
     * <p>
     * Using this method <strong>will</strong> send a note_data message to the
     * core.
     * 
     * @param note The MIDI note value to remove.
     * @param start The start beat in the pattern.
     */
    void removeNote(int pitch, float start);

    /**
     * @see #removeNote(int, float)
     */
    void removeNote(int bankIndex, int patternIndex, int pitch, float start);

    /**
     * Will trigger a step within the phrase to selected and update that
     * trigger's values based on the new values passed.
     * 
     * @param resolution
     * @param step The step trigger to turn on.
     * @param pitch The pitch of the step.
     * @param gate The gate time (how long the step plays).
     * @param velocity The velocity (how hard the step is played).
     * @param flags The bit flag holding trigger flags.
     */
    void triggerOn(Resolution resolution, int step, int pitch, float gate, float velocity, int flags);

    /**
     * @param bankIndex
     * @param patternIndex
     * @param resolution
     * @param step
     * @param pitch
     * @param gate
     * @param velocity
     * @param flags
     */
    void triggerOn(int bankIndex, int patternIndex, Resolution resolution, int step, int pitch,
            float gate, float velocity, int flags);

    /**
     * Triggers off the note in the current bank and pattern using a step
     * calculation.
     * 
     * @param resolution
     * @param step
     * @param pitch
     */
    void triggerOff(Resolution resolution, int step, int pitch);

    /**
     * @see #triggerOff(Resolution, int, int)
     */
    void triggerOff(int bankIndex, int patternIndex, Resolution resolution, int step, int pitch);
}
