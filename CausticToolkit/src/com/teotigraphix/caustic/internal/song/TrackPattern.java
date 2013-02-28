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

package com.teotigraphix.caustic.internal.song;

import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.ITrack;
import com.teotigraphix.caustic.song.ITrackPattern;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class TrackPattern extends Pattern implements ITrackPattern {

    @Override
    public int getBank() {
        return getPhrase().getBank();
    }

    @Override
    public int getIndex() {
        return getPhrase().getIndex();
    }

    @Override
    public int getLength() {
        // this allows for virtual lengths in the track
        // this means that a phrase may be 8 measures but was added as 4 measures
        // which the start and end would show with this calc
        return getEndMeasure() - getStartMeasure();
    }

    @Override
    public int getPosition() {
        return -1;//getPhrase().getPosition();
    }

    //--------------------------------------------------------------------------
    // 
    //  ITrackPattern API :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  startMeasure
    //----------------------------------

    private int mStartMeasure;

    @Override
    public int getStartMeasure() {
        return mStartMeasure;
    }

    @Override
    public void setStartMeasure(int value) {
        mStartMeasure = value;
    }

    //----------------------------------
    //  endMeasure
    //----------------------------------

    private int mEndMeasure;

    @Override
    public int getEndMeasure() {
        return mEndMeasure;
    }

    @Override
    public void setEndMeasure(int value) {
        mEndMeasure = value;
    }

    //----------------------------------
    //  phrase
    //----------------------------------

    private IPhrase mPhrase;

    @Override
    public IPhrase getPhrase() {
        return mPhrase;
    }

    @Override
    public void setPhrase(IPhrase value) {
        mPhrase = value;
    }

    //----------------------------------
    //  track
    //----------------------------------

    private ITrack mTrack;

    @Override
    public ITrack getTrack() {
        return mTrack;
    }

    @Override
    public void setTrack(ITrack value) {
        mTrack = value;
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public TrackPattern() {
        super();
    }

    @Override
    public String toString() {
        return "TrackPattern [start=" + mStartMeasure + ", end=" + mEndMeasure + ", phrase="
                + mPhrase + "]";
    }

}
