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

package com.teotigraphix.caustic.internal.part;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustic.internal.song.Pattern;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.song.IPartPattern;
import com.teotigraphix.common.IMemento;

public class PartPattern extends Pattern implements IPartPattern {

    //----------------------------------
    //  initialized
    //----------------------------------

    @Override
    public void setInitialized(boolean value) {
        super.setInitialized(value);
        for (IPart part : getParts()) {
            part.setInitialized(value);
        }
    }

    //----------------------------------
    //  parts
    //----------------------------------

    private List<IPart> mParts = new ArrayList<IPart>();

    @Override
    public List<IPart> getParts() {
        return mParts;
    }

    @Override
    public IPart getPartAt(int index) {
        return mParts.get(index);
    }

    //----------------------------------
    //  selectedPart
    //----------------------------------

    private IPart mSelectedPart;

    @Override
    public IPart getSelectedPart() {
        return mSelectedPart;
    }

    @Override
    public void setSelectedPart(IPart value) {
        setSelectedPart(value.getIndex());
    }

    void setSelectedPart(int index) {
        IPart oldPart = mSelectedPart;
        mSelectedPart = getPartAt(index);
        fireSelectedPartChanged(mSelectedPart, oldPart);
    }

    public PartPattern() {
    }

    @Override
    public IPart addPart(IPart part) {
        if (containsPart(part))
            return null;

        if (mParts == null)
            mParts = new ArrayList<IPart>();

        part.getPhrase().setLength(getLength());
        mParts.add(part);
        ((Part)part).setPattern(this);

        firePartAdded(part);

        return null;
    }

    @Override
    public IPart removePart(IPart part) {
        if (!containsPart(part))
            return null;

        mParts.remove(part);
        ((Part)part).setPattern(null);

        if (mParts.size() == 0)
            mParts = null;

        firePartRemoved(part);

        return part;
    }

    @Override
    public void addAllParts(List<IPart> parts) {
        for (IPart part : parts) {
            addPart(part);
        }
    }

    @Override
    public void removeAllParts() {
        Object[] parts = mParts.toArray();
        for (Object part : parts) {
            removePart((IPart)part);
        }
    }

    private boolean containsPart(IPart part) {
        for (IPart childPart : mParts) {
            if (part.equals(childPart))
                return true;
        }
        return false;
    }

    @Override
    protected void fireLengthChanged(int length, int oldLength) {
        // for each Part, change the phrase length
        for (IPart part : mParts) {
            part.getPhrase().setLength(length);
        }
        super.fireLengthChanged(length, oldLength);
    }

    @Override
    protected void firePositionChanged(int position, int oldPosition) {
        // for each Part, change the phrase position
        for (IPart part : mParts) {
            part.getPhrase().setPosition(position);
        }
        super.firePositionChanged(position, oldPosition);
    }

    //--------------------------------------------------------------------------
    // 
    //  IPersistable API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);

        if (getSelectedPart() != null) {
            memento.putInteger("selectedPart", getSelectedPart().getIndex());
        }

        // - part id's
        for (IPart part : getParts()) {
            IMemento child = memento.createChild("part", part.getId());
            // the part saves the phrase and triggers
            part.copy(child);
        }
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);

        IMemento[] children = memento.getChildren("part");
        for (int i = 0; i < children.length; i++) {
            IMemento child = children[i];
            IPart part = getPartAt(i);
            part.paste(child);
        }

        if (isInitialized()) {
            Integer selIndex = memento.getInteger("selectedPart");
            if (selIndex != null) {
                setSelectedPart(selIndex);
            }
        }
    }

    //----------------------------------
    //  listeners
    //----------------------------------

    private final List<IPartPatternListener> mListeners = new ArrayList<IPartPatternListener>();

    @Override
    public final void addPartPatternListener(IPartPatternListener value) {
        if (mListeners.contains(value))
            return;
        mListeners.add(value);
    }

    @Override
    public final void removePartPatternListener(IPartPatternListener value) {
        if (!mListeners.contains(value))
            return;
        mListeners.remove(value);
    }

    void firePartAdded(IPart part) {
        for (IPartPatternListener listener : mListeners) {
            listener.onPartAdded(part);
        }
    }

    void firePartRemoved(IPart part) {
        for (IPartPatternListener listener : mListeners) {
            listener.onPartRemoved(part);
        }
    }

    void fireSelectedPartChanged(IPart selectedPart, IPart oldSlectedPart) {
        for (IPartPatternListener listener : mListeners) {
            listener.onSelectedPartChanged(selectedPart, oldSlectedPart);
        }
    }
}
