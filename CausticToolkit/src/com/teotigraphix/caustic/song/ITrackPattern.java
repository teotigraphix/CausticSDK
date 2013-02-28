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

package com.teotigraphix.caustic.song;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.IPattern;

/**
 * The ITrackPattern API defines an {@link IMachine} pattern that exists within
 * a loaded or created machine instance.
 * <p>
 * The {@link ITrackPattern} is used with the {@link ITrack} and the
 * {@link ITrackSong}.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ITrackPattern extends IPattern {

    //----------------------------------
    //  startMeasure
    //----------------------------------

    int getStartMeasure();

    void setStartMeasure(int value);

    //----------------------------------
    //  endMeasure
    //----------------------------------

    int getEndMeasure();

    void setEndMeasure(int value);

    //----------------------------------
    //  phrase
    //----------------------------------

    IPhrase getPhrase();

    void setPhrase(IPhrase phrase);

    //----------------------------------
    //  track
    //----------------------------------

    /**
     * The parent track of this pattern.
     */
    ITrack getTrack();

    /**
     * @see #getTrack()
     */
    void setTrack(ITrack value);

}
