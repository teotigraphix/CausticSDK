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

package com.teotigraphix.caustk.gs.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.utils.PatternUtils;

public class Pattern implements IDispatcher {

    private final IDispatcher dispatcher;

    private final ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    //----------------------------------
    // inMemory
    //----------------------------------

    private boolean inMemory = false;

    public boolean isInMemory() {
        return inMemory;
    }

    public void setInMemory(boolean value) {
        inMemory = value;
    }

    //----------------------------------
    // libraryPattern
    //----------------------------------

    private PatternMemoryItem patternMemoryItem;

    public PatternMemoryItem getMemoryItem() {
        return patternMemoryItem;
    }

    //----------------------------------
    // parts
    //----------------------------------

    private ArrayList<Part> parts = new ArrayList<Part>();

    public int getPartCount() {
        return parts.size();
    }

    public List<Part> getParts() {
        return Collections.unmodifiableList(parts);
    }

    public Part getPart(int index) {
        return parts.get(index);
    }

    //----------------------------------
    // bank/pattern
    //----------------------------------

    public int getIndex() {
        return (getBankIndex() + 1) * getPatternIndex();
    }

    /**
     * Returns the bank index the parts of this pattern are assigned to.
     */
    public int getBankIndex() {
        return PatternUtils.getBank(patternMemoryItem.getIndex());
    }

    /**
     * Returns the pattern index the parts of this pattern are assigned to.
     */
    public int getPatternIndex() {
        return PatternUtils.getPattern(patternMemoryItem.getIndex());
    }

    //----------------------------------
    // tempo
    //----------------------------------

    private float tempo;

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float value) {
        tempo = value;
    }

    //----------------------------------
    // length
    //----------------------------------

    // start off with invalid length, must be set so Phrase is updated with num steps
    private int length = -1;

    public int getLength() {
        return length;
    }

    public void setLength(int value) {
        if (length == value)
            return;

        length = value;

        for (Part part : parts) {
            part.getPhrase().setLength(value);
        }
    }

    //----------------------------------
    // selectedPart
    //----------------------------------

    private Part selectedPart;

    /**
     * The selected part of the pattern.
     */
    @SuppressWarnings("unchecked")
    public <T extends Part> T getSelectedPart() {
        return (T)selectedPart;
    }

    /**
     * Sets the selected part of the pattern using the index.
     * 
     * @param index The part index to be selected.
     * @see OnPatternSelectedPartChange
     * @see Pattern#setSelectedPart(Part)
     */
    public void setSelectedPart(int index) {
        setSelectedPart(getPart(index));
    }

    /**
     * Sets the selected part of the pattern.
     * 
     * @param value The part to be selected.
     */
    public void setSelectedPart(Part value) {
        if (selectedPart == value)
            return;

        //GrooveMachinePart oldPart = selectedPart;
        selectedPart = value;

        //dispatch(new OnPatternSelectedPartChange(selectedPart, oldPart));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Pattern(ICaustkController controller, PatternMemoryItem PatternMemoryItem) {
        this.controller = controller;
        this.patternMemoryItem = PatternMemoryItem;
        dispatcher = new Dispatcher();
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    public void addPart(Part part) {
        if (parts.contains(part))
            return;
        parts.add(part);
        part.setPattern(this);
    }

    public void removePart(Part part) {
        parts.remove(part);
        part.setPattern(null);
    }

    //--------------------------------------------------------------------------
    // IDispatcher API
    //--------------------------------------------------------------------------

    @Override
    public void trigger(Object event) {
        dispatcher.trigger(event);
    }

    @Override
    public <T> void register(Class<T> event, EventObserver<T> observer) {
        dispatcher.register(event, observer);
    }

    @Override
    public void unregister(EventObserver<?> observer) {
        dispatcher.unregister(observer);
    }

    @Override
    public void clear() {
        dispatcher.clear();
    }
}
