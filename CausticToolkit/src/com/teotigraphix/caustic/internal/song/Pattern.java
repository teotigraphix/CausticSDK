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

import com.teotigraphix.caustic.sequencer.PatternMeasures;
import com.teotigraphix.caustic.song.IPattern;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.ISong;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the IPattern interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class Pattern implements IPattern {

    //--------------------------------------------------------------------------
    //
    // IPreset API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // initialized
    //----------------------------------

    private boolean mInitialized;

    @Override
    public boolean isInitialized() {
        return mInitialized;
    }

    @Override
    public void setInitialized(boolean value) {
        mInitialized = value;
    }

    //----------------------------------
    // id
    //----------------------------------

    private String mId;

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public void setId(String value) {
        mId = value;
    }

    //----------------------------------
    // name
    //----------------------------------

    private String mName;

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String value) {
        mName = value;
    }

    //----------------------------------
    // presetBank
    //----------------------------------

    private String mPresetBank;

    @Override
    public String getPresetBank() {
        return mPresetBank;
    }

    @Override
    public void setPresetBank(String value) {
        mPresetBank = value;
    }

    //--------------------------------------------------------------------------
    //
    // IPattern API :: Properties
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
    // song
    //----------------------------------

    private ISong mSong;

    @Override
    public ISong getSong() {
        return mSong;
    }

    @Override
    public void setSong(ISong value) {
        mSong = value;
    }

    //----------------------------------
    // index
    //----------------------------------

    private int mIndex = -1;

    @Override
    public int getIndex() {
        return mIndex;
    }

    @Override
    public void setIndex(int value) {
        mIndex = value;
    }

    //----------------------------------
    // bank
    //----------------------------------

    private int mBank = -1;

    @Override
    public int getBank() {
        return mBank;
    }

    @Override
    public void setBank(int value) {
        mBank = value;
    }

    //----------------------------------
    // length
    //----------------------------------

    private int mLength = 1;

    @Override
    public int getLength() {
        return mLength;
    }

    @Override
    public void setLength(int value) {
        if (value == mLength)
            return;
        if (!PatternMeasures.isValid(value))
            return;
        int old = mLength;
        mLength = value;
        if (mPosition >= mLength) {
            setPosition(mLength - 1);
        }
        fireLengthChanged(mLength, old);
    }

    //----------------------------------
    // position
    //----------------------------------

    private int mPosition = 0;

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Override
    public void setPosition(int value) {
        if (value == mPosition)
            return;
        int old = mPosition;
        mPosition = value;
        firePositionChanged(mPosition, old);
    }

    //----------------------------------
    // data
    //----------------------------------

    private PatternData mData;

    @Override
    public PatternData getData() {
        return mData;
    }

    @Override
    public void setData(PatternData value) {
        mData = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public Pattern() {
        setData(new PatternData(this));
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        // what does the pattern have to save in order to restore it's state?

        memento.putString("id", getId());
        memento.putString("name", getId());
        memento.putInteger("bank", getBank());
        memento.putInteger("position", getPosition());
        memento.putInteger("length", getLength());

        // - pattern data
        if (mData != null) {
            mData.copy(memento.createChild("data"));
        }

    }

    @Override
    public void paste(IMemento memento) {
        setId(memento.getString("id"));
        setName(memento.getString("name"));

        // Part specific that signal changes in Part
        setBank(memento.getInteger("bank"));
        setLength(memento.getInteger("length"));
        setPosition(memento.getInteger("position"));

        if (mData != null) {
            mData.paste(memento.getChild("data"));
        }

    }

    protected void fireLengthChanged(int length, int oldLength) {
    }

    protected void firePositionChanged(int position, int oldPosition) {
    }
}
