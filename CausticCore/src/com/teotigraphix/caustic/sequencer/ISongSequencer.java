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

package com.teotigraphix.caustic.sequencer;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISongSequencer {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // currentMeasure
    //----------------------------------

    /**
     * Returns the current measure playing in Song mode.
     * <p>
     * Note: The current bar is divisible by 4, the current measure is the sum
     * of all steps played currently in a song.
     * </p>
     * 
     * @return
     */
    int getCurrentMeasure();

    /**
     * Returns the actual beat in the current measure.
     * <p>
     * Example; measure 4, beat 14 would be beat 2 in the measure (0 index - 3rd
     * beat in measure).
     * </p>
     */
    int getMeasureBeat();

    //----------------------------------
    // currentBar
    //----------------------------------

    /**
     * Return the ISong current beat.
     */
    int getCurrentBeat();

    int getNumBeats();

    int getNumMeasures();

    /**
     * Returns the total time in seconds.
     */
    int getTotalTime();

    int getCurrentTime();

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Enables the playhead.
     * <p>
     * Calling play dispatches signals without advancing the playhead. This is
     * useful for starting a song at o beat and 0 measure.
     * </p>
     */
    void play();

    /**
     * Rewinds the playhead to the start of the song, beat 0.
     */
    void rewind();

    /**
     * Moves the playhead a number of beats.
     * 
     * @param beats The beats to rewind.
     */
    void seek(int beat);

    /**
     * Moves playhead to next beat based on song implementation.
     */
    void nextBeat();

    /**
     * Moves playhead to previous beat based on song implementation.
     */
    void previousBeat();

    /**
     * Moves playhead to next measure based on song implementation.
     */
    void nextMeasure();

    /**
     * Moves playhead to previous measure based on song implementation.
     */
    void previousMeasure();

    /**
     * Moves the playhead to the end of the song data.
     */
    // void forward();

}
