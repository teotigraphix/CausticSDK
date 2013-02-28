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

package com.teotigraphix.caustic.internal.song;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.ISong;
import com.teotigraphix.common.IMemento;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Song implements ISong {

    @SuppressWarnings("unused")
    private static final String TAG = "Song";

    protected int mNextInsertBar = 0;

    //--------------------------------------------------------------------------
    //
    // ISong API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // project
    //----------------------------------

    private IProject mProject;

    @Override
    public IProject getProject() {
        return mProject;
    }

    @Override
    public void setProject(IProject value) {
        mProject = value;
    }

    //----------------------------------
    // currentMeasure
    //----------------------------------

    private int mCurrentMeasure = 0;

    @Override
    public int getCurrentMeasure() {
        return mCurrentMeasure;
    }

    void setCurrentMeasure(int value) {
        int last = mCurrentMeasure;
        mCurrentMeasure = value;
        fireMeasureChange(mCurrentMeasure, last);
    }

    @Override
    public int getMeasureBeat() {
        return mCurrentBeat % 4;
    }

    //----------------------------------
    // currentBeat
    //----------------------------------

    private int mCurrentBeat = 0;

    @Override
    public int getCurrentBeat() {
        return mCurrentBeat;
    }

    void setCurrentBeat(int value) {
        setCurrentBeat(value, false);
    }

    void setCurrentBeat(int value, boolean seeking) {
        int last = mCurrentBeat;
        mCurrentBeat = value;

        fireBeatChange(mCurrentBeat, last);

        if (last < value) {
            // forward
            if (mCurrentBeat == 0) {
                setCurrentMeasure(0);
            } else {
                int remainder = mCurrentBeat % 4;
                if (seeking) {
                    setCurrentMeasure(mCurrentBeat / 4);
                } else if (remainder == 0) {
                    setCurrentMeasure(mCurrentMeasure + 1);
                }
            }
        } else if (last > value) {
            // reverse
            // if the last beat was a measure change, decrement measure
            int remainder = last % 4;
            if (remainder == 0) {
                setCurrentMeasure(mCurrentMeasure - 1);
            }
        }
    }

    // the song doesn't know this since it's abstract, the track song
    // would know it's measures and beats since it adds track patterns
    // to it's tracks
    @Override
    public int getNumBeats() {
        return -1;
    }

    @Override
    public int getNumMeasures() {
        return -1;
    }

    @Override
    public int getTotalTime() {
        return -1;
    }

    @Override
    public int getCurrentTime() {
        return -1;
    }

    //----------------------------------
    // data
    //----------------------------------

    private SongData mData;

    @Override
    public SongData getData() {
        return mData;
    }

    @Override
    public void setData(SongData value) {
        mData = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public Song(String name) {
        setData(new SongData(name));
    }

    //--------------------------------------------------------------------------
    //
    // IDispose API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void dispose() {
        mSongListeners.clear();
        mProject = null;
    }

    //--------------------------------------------------------------------------
    //
    // ISong API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void play() {
        setCurrentBeat(mCurrentBeat);
    }

    @Override
    public void rewind() {
        int lastBeat = mCurrentBeat;
        int lastMeasure = mCurrentMeasure;

        mCurrentBeat = 0;
        mCurrentMeasure = 0;

        fireBeatChange(mCurrentBeat, lastBeat);
        fireMeasureChange(mCurrentMeasure, lastMeasure);
    }

    @Override
    public void nextBeat() {
        setCurrentBeat(mCurrentBeat + 1);
    }

    @Override
    public void previousBeat() {
        setCurrentBeat(mCurrentBeat - 1);
    }

    @Override
    public void nextMeasure() {
        // (mschmalle)  use seek() and calc it
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

    @Override
    public void previousMeasure() {
        // start with a previous beat since we might be on a 0 beat of the
        // measure
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

    @Override
    public void seek(int beat) {
        setCurrentBeat(beat, true);
    }

    @Override
    public void copy(IMemento memento) {
    }

    @Override
    public void paste(IMemento memento) {
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // Song listeners
    //----------------------------------

    private final List<ISongListener> mSongListeners = new ArrayList<ISongListener>();

    @Override
    public final void addSongListener(ISongListener value) {
        if (mSongListeners.contains(value))
            return;
        mSongListeners.add(value);
    }

    @Override
    public final void removeSongListener(ISongListener value) {
        if (!mSongListeners.contains(value))
            return;
        mSongListeners.remove(value);
    }

    void fireBeatChange(int beat, int oldBeat) {
        //Log.d(TAG, "fireBeatChange [" + beat + "]");
        for (ISongListener listener : mSongListeners) {
            listener.onBeatChanged(beat, oldBeat);
        }
    }

    void fireMeasureChange(int measure, int oldMeasure) {
        // Log.d(TAG, "fireMeasureChange [" + measure + "]");
        for (ISongListener listener : mSongListeners) {
            listener.onMeasureChanged(measure, oldMeasure);
        }
    }

    protected void firePatternAdd(SongPatternInfo info) {
        for (ISongListener listener : mSongListeners) {
            listener.onPatternAdded(info);
        }
    }

    protected void firePatternRemove(SongPatternInfo info) {
        for (ISongListener listener : mSongListeners) {
            listener.onPatternRemoved(info);
        }
    }

}
