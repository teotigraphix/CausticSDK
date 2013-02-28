////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

import com.teotigraphix.caustic.internal.part.PartPattern;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.song.IMultiTonePattern;
import com.teotigraphix.common.IMemento;

public class MultiTonePattern extends PartPattern implements IMultiTonePattern {

    private int mCurrentBank;

    private int mCurrentPattern;

    @Override
    public int getCurrentBank() {
        return mCurrentBank;
    }

    @Override
    public int getCurrentPattern() {
        return mCurrentPattern;
    }

    public MultiTonePattern() {
        super();
    }

    @Override
    public void removeNote(int note, float start) {
        for (IPart part : getParts()) {
            part.getTone().getSequencer().removeNote(note, start);
        }
    }

    @Override
    public void addNote(int note, float start, float end, float velocity, int flags) {
        for (IPart part : getParts()) {
            part.getTone().getSequencer().addNote(note, start, end, velocity, flags);
        }
    }

    @Override
    public void setBankPattern(int bank, int pattern) {
        mCurrentBank = bank;
        mCurrentPattern = pattern;
        for (IPart part : getParts()) {
            part.getTone().getSequencer().setBankPattern(mCurrentBank, mCurrentPattern);
        }
    }

    @Override
    public void setLength(int value) {
        super.setLength(value);

        for (IPart part : getParts()) {
            part.getTone().getSequencer().getPhraseAt(0, 0).setLength(value);
        }
    }

    @Override
    public void copy(IMemento memento) {
        // what does the pattern have to save in order to restore it's state?

        //        memento.putString("id", getId());
        //        memento.putString("name", getId());
        //        memento.putInteger("bank", getBank());
        //        memento.putInteger("position", getPosition());
        //        memento.putInteger("length", getLength());
        //
        // - pattern data
        if (getData() != null) {
            getData().copy(memento.createChild("data"));
        }

        for (IPart part : getParts()) {
            part.getTone().getSequencer().copy(memento.createChild("sequencer"));
        }
    }

    @Override
    public void paste(IMemento memento) {
        //        setId(memento.getString("id"));
        //        setName(memento.getString("name"));
        //
        //        // Part specific that signal changes in Part
        //        setBank(memento.getInteger("bank"));
        //        setLength(memento.getInteger("length"));
        //        setPosition(memento.getInteger("position"));
        //
        if (getData() != null) {
            getData().paste(memento.getChild("data"));
        }

        for (IMemento child : memento.getChildren("sequencer")) {
            String id = child.getId();
            IPart part = getPartById(id);
            part.getTone().getSequencer().paste(child);
        }
    }

    private IPart getPartById(String id) {
        for (IPart part : getParts()) {
            if (part.getId().equals(id))
                return part;
        }
        return null;
    }
}
