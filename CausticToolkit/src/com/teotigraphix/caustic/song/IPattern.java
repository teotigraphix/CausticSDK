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

package com.teotigraphix.caustic.song;

import com.teotigraphix.caustic.internal.song.PatternData;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPattern extends IPreset {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    boolean isInitialized();

    void setInitialized(boolean value);

    //----------------------------------
    // project
    //----------------------------------

    /**
     * Return the IPattern parent IProject.
     * 
     * @return
     */
    IProject getProject();

    void setProject(IProject value);

    //----------------------------------
    // song
    //----------------------------------

    /**
     * Return the IPattern parent ISong if the pattern has been inserted into an
     * ISong.
     * <p>
     * This property will return null if the sequencer is playing in pattern
     * mode, otherwise the ISong will be returned that is currently playing in
     * the song mode of the sequencer.
     * </p>
     * 
     * @return
     */
    ISong getSong();

    void setSong(ISong value);

    //----------------------------------
    // bank
    //----------------------------------

    /**
     * Return the IPattern's bank index the project collection.
     * 
     * @return
     */
    int getBank();

    void setBank(int value);

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Return the IPattern's index in it's bank collection.
     * 
     * @return
     */
    @Override
    int getIndex();

    void setIndex(int value);

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Sets the length of the pattern which will then update the part's phrases
     * at the same time.
     * <p>
     * Note: In the part framework, patterns of machines are not required to
     * have the same length but, the pattern has a specific implementation that
     * require the part's phrase length to match the patterns length.
     * </p>
     */
    int getLength();

    void setLength(int value);

    //----------------------------------
    // position
    //----------------------------------

    int getPosition();

    void setPosition(int value);

    //----------------------------------
    // data
    //----------------------------------

    /**
     * Return data associated with the part.
     * 
     * @return
     */
    PatternData getData();

    void setData(PatternData value);

}
