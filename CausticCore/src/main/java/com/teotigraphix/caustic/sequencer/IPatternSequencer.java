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

import com.teotigraphix.caustic.core.IRestore;
import com.teotigraphix.caustic.device.IDeviceComponent;

/**
 * The IPatternSequencer interface controls the selected pattern and pattern
 * bank in the implementing IDevice.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPatternSequencer extends IDeviceComponent, IRestore {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    /**
     * Sets both bank and pattern, signals a phraseChanged() if the triggerMap
     * exists and has note data.
     * <p>
     * Calling this method on a bank and pattern that has no note data will not
     * automatically create an {@link IStepPhrase}. It does change the current
     * bank and pattern but the {@link IStepPhrase} will only be created when
     * {@link #addNote(int, float, float, float, int)} is called.
     * 
     * @param bank The new bank index.
     * @param pattern The new pattern index.
     * @see IPatternSequencerListener#onPhraseChange(IStepPhrase)
     */
    void setBankPattern(int bank, int pattern);

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
    int getSelectedPattern();

    int getSelectedPattern(boolean restore);

    /**
     * Returns a list of currently filled patterns that contain pattern data.
     * <p>
     * This method queries the core and returns a List of Strings such as [A1,
     * A2, B4, C4] etc.
     * </p>
     * <p>
     * This method should only be used as a reference or to initialize this
     * sequencers pattern on a {@link #restore()}. Never returns
     * <code>null</code>.
     * </p>
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
     * Clears the pattern in the {@link #getSelectedBank()}.
     * 
     * @param index The pattern index to clear <code>0..15</code>.
     */
    IStepPhrase clearStepPhrase(int index);

    /**
     * Removes an existing triggerMap from the sequencer at the bank and
     * pattern.
     * <p>
     * If the triggerMap does not exist, <code>null</code> will be returned else
     * the existing {@link IStepPhrase} will be returned.
     * 
     * @param bank The bank index.
     * @param pattern The pattern index.
     */
    IStepPhrase clearStepPhrase(int bank, int index);

    /**
     * Returns a boolean indicating if the sequencer contains triggerMap data at
     * the specified bank and pattern index.
     * 
     * @param bank The bank index.
     * @param pattern The pattern index.
     */
    boolean hasStepPhrase(int bank, int pattern);

    /**
     * Returns the active triggerMap based on the {@link #getSelectedBank()} and
     * {@link #getSelectedPattern()} values.
     * <p>
     * When a pattern sequencer is created, the bank and pattern are 0 and the
     * active triggerMap is A1.
     * </p>
     */
    @Deprecated
    IStepPhrase getTriggerMap();

    IStepPhrase getActiveStepPhrase();

    void setActiveStepPhrase(IStepPhrase phrase);

    /**
     * Returns the {@link IStepPhrase} located at the bank and pattern of the
     * sequencer, <code>null</code> if not found.
     * 
     * @param bank The bank index.
     * @param pattern The pattern index.
     */
    IStepPhrase getStepPhraseAt(int bank, int pattern);

    /**
     * Returns all (banks) current {@link IStepPhrase}s that exist within the
     * machine's sequencer.
     */
    List<IStepPhrase> getStepPhrases();

    /**
     * Returns a List of {@link IStepPhrase}s that exist within the machine's
     * sequencer.
     */
    List<IStepPhrase> getStepPhraseForBank(int bank);

    /**
     * Adds a note beat span in the sequencer for the specified MIDI note.
     * <p>
     * The value of end - start equals the note's gate time. All calls to this
     * method will need client calculation regarding the note's resolution.
     * <p>
     * Using this method <strong>will</strong> send a note_data message to the
     * core. If you do not want the core to add notes, use the
     * {@link #addPhrase(int, int, String)} method instead.
     * <p>
     * The note flags;
     * <ul>
     * <li>0 - none</li>
     * <li>1 - slide</li>
     * <li>2 - accent</li>
     * </ul>
     * 
     * @param note The MIDI note value to add.
     * @param start The start beat in the pattern.
     * @param end The end beat in the pattern when the note stops.
     * @param velocity The velocity of the note on (0-1).
     * @param flags Optional flags to set on the note data.
     * @throws CausticError parameter bounds error
     */
    //void addNote(int note, float start, float end, float velocity, int flags);

    /**
     * Removes a note beat span in the sequencer for the specified MIDI note.
     * <p>
     * Using this method <strong>will</strong> send a note_data message to the
     * core. If you do not want the core to add notes, use the
     * {@link #removePhrase(int, int)} method instead.
     * 
     * @param note The MIDI note value to remove.
     * @param start The start step in the pattern (0-15).
     */
    //void removeNote(int note, float start);

    //--------------------------------------------------------------------------
    //
    // Listeners
    //
    //--------------------------------------------------------------------------

    /**
     * Adds a pattern sequencer listener.
     * 
     * @param value The {@link IPatternSequencerListener} to add.
     */
    void addPatternSequencerListener(IPatternSequencerListener value);

    /**
     * Removes a pattern sequencer listener.
     * 
     * @param value The {@link IPatternSequencerListener} to add.
     */
    void removePatternSequencerListener(IPatternSequencerListener value);

    /**
     * The listener API for the {@link IPatternSequencer}.
     */
    public interface IPatternSequencerListener {

        /**
         * Dispatched when a bank, pattern select occurs in the sequencer
         * signaling a new map is being edited.
         * 
         * @param map The new {@link IStepPhrase}.
         */
        void onTriggerMapChange(IStepPhrase map, IStepPhrase oldMap);
    }

}
