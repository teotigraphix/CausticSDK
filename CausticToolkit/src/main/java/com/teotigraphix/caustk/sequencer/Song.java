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

package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.application.Dispatcher;
import com.teotigraphix.caustk.application.IDispatcher;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Song {

    private transient IDispatcher dispatcher;

    public final IDispatcher getDispatcher() {
        return dispatcher;
    }

    //--------------------------------------------------------------------------
    // 
    //  ISong API :: Properties
    // 
    //--------------------------------------------------------------------------

    private int bpm = 120;

    public final int getBPM() {
        return bpm;
    }

    public final void setBPM(int value) {
        bpm = value;
    }

    //----------------------------------
    //  currentMeasure
    //----------------------------------

    private int currentMeasure = 0;

    /**
     * Returns the current measure playing in Song mode.
     * <p>
     * Note: The current bar is divisible by 4, the current measure is the sum
     * of all steps played currently in a song.
     * </p>
     * 
     * @return
     */
    public int getCurrentMeasure() {
        return currentMeasure;
    }

    void setCurrentMeasure(int value) {
        @SuppressWarnings("unused")
        int last = currentMeasure;
        currentMeasure = value;
        //        fireMeasureChange(mCurrentMeasure, last);
    }

    /**
     * Returns the actual beat in the current measure.
     * <p>
     * Example; measure 4, beat 14 would be beat 2 in the measure (0 index - 3rd
     * beat in measure).
     * </p>
     */
    public int getMeasureBeat() {
        return currentBeat % 4;
    }

    //----------------------------------
    //  currentBeat
    //----------------------------------

    private int currentBeat = 0;

    /**
     * Return the ISong current beat.
     */
    public int getCurrentBeat() {
        return currentBeat;
    }

    void setCurrentBeat(int value) {
        setCurrentBeat(value, false);
    }

    void setCurrentBeat(int value, boolean seeking) {
        int last = currentBeat;
        currentBeat = value;

        //        fireBeatChange(mCurrentBeat, last);

        if (last < value) {
            // forward
            if (currentBeat == 0) {
                setCurrentMeasure(0);
            } else {
                int remainder = currentBeat % 4;
                if (seeking) {
                    setCurrentMeasure(currentBeat / 4);
                } else if (remainder == 0) {
                    setCurrentMeasure(currentMeasure + 1);
                }
            }
        } else if (last > value) {
            // reverse
            // if the last beat was a measure change, decrement measure
            int remainder = last % 4;
            if (remainder == 0) {
                setCurrentMeasure(currentMeasure - 1);
            }
        }
    }

    // the song doesn't know this since it's abstract, the track song
    // would know it's measures and beats since it adds track patterns
    // to it's tracks
    public int getNumBeats() {
        return -1;
    }

    public int getNumMeasures() {
        return -1;
    }

    /**
     * Returns the total time in seconds.
     */
    public int getTotalTime() {
        return -1;
    }

    public int getCurrentTime() {
        return -1;
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public Song() {
        dispatcher = new Dispatcher();
    }

    //--------------------------------------------------------------------------
    // 
    //  ISong API :: Methods
    // 
    //--------------------------------------------------------------------------
    /**
     * Enables the playhead.
     * <p>
     * Calling play dispatches signals without advancing the playhead. This is
     * useful for starting a song at o beat and 0 measure.
     * </p>
     */

    public void play() {
        setCurrentBeat(currentBeat);
    }

    /**
     * Rewinds the playhead to the start of the song, beat 0.
     */
    @SuppressWarnings("unused")
    public void rewind() {
        int lastBeat = currentBeat;
        int lastMeasure = currentMeasure;

        currentBeat = 0;
        currentMeasure = 0;

        //        fireBeatChange(mCurrentBeat, lastBeat);
        //        fireMeasureChange(mCurrentMeasure, lastMeasure);
    }

    /**
     * Moves playhead to next beat based on song implementation.
     */
    public void nextBeat() {
        setCurrentBeat(currentBeat + 1);
    }

    /**
     * Moves playhead to previous beat based on song implementation.
     */
    public void previousBeat() {
        setCurrentBeat(currentBeat - 1);
    }

    /**
     * Moves playhead to next measure based on song implementation.
     */
    public void nextMeasure() {
        // TODO use seek() and calc it
        // think about this though, do you want beat signals or not???
        // if so, just loop this, if not use seek() and only he measure
        // signal will fire
        // start with a next beat since we might be on a 0 beat of the measure
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
        nextBeat();
        if (getMeasureBeat() == 0)
            return;
    }

    /**
     * Moves playhead to previous measure based on song implementation.
     */
    public void previousMeasure() {
        // start with a previous beat since we might be on a 0 beat of the measure
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
        previousBeat();
        if (getMeasureBeat() == 0)
            return;
    }

    /**
     * Moves the playhead a number of beats.
     * 
     * @param beats The beats to rewind.
     */
    public void seek(int beat) {
        setCurrentBeat(beat, true);
    }

    /**
     * Moves the playhead to the end of the song data.
     */
    //void forward();

}
