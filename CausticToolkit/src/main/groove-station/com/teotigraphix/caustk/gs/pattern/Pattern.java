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

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.utils.PatternUtils;

public class Pattern implements IDispatcher {

    private final IDispatcher dispatcher;

    private final GrooveMachine machine;

    public ICaustkController getController() {
        return machine.getController();
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

        for (Part part : machine.getSound().getParts()) {
            part.getPhrase().setLength(value);
        }
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Pattern(GrooveMachine machine, PatternMemoryItem PatternMemoryItem) {
        this.machine = machine;
        this.patternMemoryItem = PatternMemoryItem;
        dispatcher = new Dispatcher();
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
