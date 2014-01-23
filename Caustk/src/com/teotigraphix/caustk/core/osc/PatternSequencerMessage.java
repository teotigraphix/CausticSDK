////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.core.osc;

/**
 * The {@link PatternSequencerMessage} holds all OSC messages associated with
 * the {@link IPatternSequencer} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PatternSequencerMessage extends CausticMessage {

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/bank [value]</code>
     * <p>
     * Sets the current bank on a machine.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The index of the machine.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IPatternSequencer#getSelectedBank()
     * @see IPatternSequencer#setBankPattern(int, int)
     */
    public static final PatternSequencerMessage BANK = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/bank ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/pattern [value]</code>
     * <p>
     * Sets the current pattern on a machine.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The name of the machine.</li>
     * <li><strong>value</strong>: (0..15)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IPatternSequencer#getNextPatternIndex()
     * @see IPatternSequencer#setBankPattern(int, int)
     */
    public static final PatternSequencerMessage PATTERN = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/pattern ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/note_data [start] [pitch] [velocity] [end] [flags]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The index of the machine.</li>
     * <li><strong>start</strong>: The start beat value within the pattern.</li>
     * <li><strong>pitch</strong>: (0..255) The MIDI note value.</li>
     * <li><strong>velocity</strong>: The force to play.</li>
     * <li><strong>end</strong>: The end beat value within the pattern.</li>
     * <li><strong>flags</strong>: (<code>0</code> none, <code>1</code> slide,
     * <code>2</code> accent) bit or'd.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code>
     * 
     * @see IPatternSequencer#addNote(int, float, float, float, int)
     */
    public static final PatternSequencerMessage NOTE_DATA = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/note_data ${1} ${2} ${3} ${4} ${5}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/note_data [start] [pitch]</code>
     * <p>
     * Removes a note from the pattern sequencer.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The name of the machine.</li>
     * <li><strong>start</strong>: The start beat value.</li>
     * <li><strong>pitch</strong>: (0..255) The MIDI note value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IPatternSequencer#removeNote(int, float)
     */
    public static final PatternSequencerMessage NOTE_DATA_REMOVE = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/note_data ${1} ${2} -1 -1");

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/clear_bank [value]</code>
     * <p>
     * Clears a full bank of patterns in a machine.
     * </p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The index of the machine.</li>
     * <li><strong>value</strong>: (0..15) The bank to clear.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IPatternSequencer#clearBank(int)
     */
    public static final PatternSequencerMessage CLEAR_BANK = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/clear_bank ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/clear_pattern [value]</code>
     * <p>
     * Clears a pattern in a bank from a machine.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The index of the machine.</li>
     * <li><strong>value</strong>: (0..15) The pattern to clear.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IPatternSequencer#clearStepPhrase(int)
     */
    public static final PatternSequencerMessage CLEAR_PATTERN = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/clear_pattern ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/shuffle_mode [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The index of the machine.</li>
     * <li><strong>value</strong>: (0,1,2) 0 default inherit, 1
     * <code>8th notes</code>, 2 <code>16th notes</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PatternSequencerMessage SHUFFLE_MODE = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/shuffle_mode ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/shuffle_amount [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The index of the machine.</li>
     * <li><strong>value</strong>: (0..1)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PatternSequencerMessage SHUFFLE_AMOUNT = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/shuffle_amount ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/pattern_sequencer/num_measures [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The name of the machine.</li>
     * <li><strong>value</strong>: (1,2,4,8)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see com.teotigraphix.caustic.sequencer.IPatternSequencer#getLength()
     * @see com.teotigraphix.caustic.sequencer.IPatternSequencer#setNumMeasures()
     */
    public static final PatternSequencerMessage NUM_MEASURES = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/num_measures ${1}");

    /**
     * Query:
     * <code>/caustic/[machine_index]/pattern_sequencer/patterns_with_data</code>
     * <p>
     * Returns a serialized String of patterns that exist within the machine.
     * Where the String return value would look like
     * <code>"A1 B3 C14 C15 D16"</code> with spaces separating the pattern
     * names.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The name of the machine.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code>
     * 
     * @see IPatternSequencer#getPatternListing()
     */
    public static final PatternSequencerMessage QUERY_PATTERNS_WITH_DATA = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/patterns_with_data");

    /**
     * Query: <code>/caustic/[machine_index]/pattern_sequencer/note_data</code>
     * <p>
     * Returns a serialized String of notes that exist within the machine
     * pattern. Where the String return value would look like
     * <code>"0.0 60 0.5 1.0 0|1.0 60 1.0 2.0 0"</code> with spaces separating
     * the note parameters and pipes <code>|</code> separating the note. The
     * order of note parameters within a pipe is <strong>[start]</strong>
     * <strong>[note]</strong> <strong>[velocity]</strong>
     * <strong>[end]</strong> <strong>[flags]</strong>.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The name of the machine.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code> serialized.
     * 
     * @see IPatternSequencer#addNote(int, float, float, float, int)
     */
    public static final PatternSequencerMessage QUERY_NOTE_DATA = new PatternSequencerMessage(
            "/caustic/${0}/pattern_sequencer/note_data");

    PatternSequencerMessage(String message) {
        super(message);
    }

    /**
     * Controls for the {@link PatternSequencerMessage}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public enum PatternSequencerControl implements IOSCControl {

        Bank,

        ClearBank,

        ClearPattern,

        NoteDataAdd,

        NoteDataRemove,

        NumMeausures,

        Pattern,

        ShuffleAmount,

        ShuffleMode
    }
}
