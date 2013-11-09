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

package com.teotigraphix.caustk.gs.machine;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ICaustkLogger;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gs.machine.GrooveStation.GrooveMachineDescriptor;
import com.teotigraphix.caustk.gs.machine.part.MachineControls;
import com.teotigraphix.caustk.gs.machine.part.MachineFooter;
import com.teotigraphix.caustk.gs.machine.part.MachineHeader;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer;
import com.teotigraphix.caustk.gs.machine.part.MachineSound;
import com.teotigraphix.caustk.gs.machine.part.MachineSystem;
import com.teotigraphix.caustk.gs.machine.part.MachineTransport;
import com.teotigraphix.caustk.gs.memory.Memory.Category;
import com.teotigraphix.caustk.gs.memory.Memory.Type;
import com.teotigraphix.caustk.gs.memory.MemoryBank;
import com.teotigraphix.caustk.gs.memory.MemoryManager;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.PartUtils;
import com.teotigraphix.caustk.gs.pattern.Pattern;
import com.teotigraphix.caustk.rack.IRack;

public abstract class GrooveMachine implements IDispatcher {

    private transient IDispatcher dispatcher;

    public final IRack getRack() {
        return controller.getRack();
    }

    public final ICaustkLogger getLogger() {
        return controller.getLogger();
    }

    //----------------------------------
    // nextPatternIndex
    //----------------------------------

    private int nextPatternIndex = -1;

    public int getNextPatternIndex() {
        return nextPatternIndex;
    }

    public void setNextPatternIndex(int value) {
        if (value == nextPatternIndex)
            return;
        nextPatternIndex = value;
        getSequencer().setNextPattern(nextPatternIndex);
    }

    //----------------------------------
    // memoryManager
    //----------------------------------

    private MemoryManager memoryManager;

    public MemoryManager getMemory() {
        return memoryManager;
    }

    //----------------------------------
    // machineSound
    //----------------------------------

    private MachineSound machineSound;

    public MachineSound getSound() {
        return machineSound;
    }

    protected void setSound(MachineSound value) {
        machineSound = value;
    }

    //----------------------------------
    // machineHeader
    //----------------------------------

    private MachineHeader machineHeader;

    public MachineHeader getHeader() {
        return machineHeader;
    }

    void setHeader(MachineHeader value) {
        machineHeader = value;
    }

    //----------------------------------
    // machineSystem
    //----------------------------------

    private MachineSystem machineSystem;

    public MachineSystem getSystem() {
        return machineSystem;
    }

    public void setSystem(MachineSystem value) {
        machineSystem = value;
    }

    //----------------------------------
    // machineTransport
    //----------------------------------

    private MachineTransport machineTransport;

    public MachineTransport getTransport() {
        return machineTransport;
    }

    public void setTransport(MachineTransport value) {
        machineTransport = value;
    }

    //----------------------------------
    // machineSequencer
    //----------------------------------

    private MachineSequencer machineSequencer;

    public MachineSequencer getSequencer() {
        return machineSequencer;
    }

    public void setSequencer(MachineSequencer value) {
        machineSequencer = value;
    }

    //----------------------------------
    // machineControls
    //----------------------------------

    private MachineControls machineControls;

    public MachineControls getControls() {
        return machineControls;
    }

    public void setControls(MachineControls value) {
        machineControls = value;
    }

    //----------------------------------
    // machineFooter
    //----------------------------------

    private MachineFooter machineFooter;

    public MachineFooter getFooter() {
        return machineFooter;
    }

    public void setFooter(MachineFooter value) {
        machineFooter = value;
    }

    //----------------------------------
    // machineFooter
    //----------------------------------

    private ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public void setController(ICaustkController controller) {
        this.controller = controller;
        createMainComponentParts();
        createComponentParts();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public GrooveMachine() {
        dispatcher = new Dispatcher();
    }

    protected void createMainComponentParts() {
        memoryManager = new MemoryManager(this);
        memoryManager.setSelectedType(Type.USER);
        memoryManager.setSelectedCategory(Category.PATTERN);
        machineSequencer = new MachineSequencer(this);
    }

    protected abstract void createComponentParts();

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    public void write() throws CausticException {
        machineSequencer.write();
    }

    /**
     * Mutes/unmutes all parts of the machine.
     * 
     * @param muted Whether the machine is muted.
     */
    public void setMute(boolean muted) {
        for (Part part : getSound().getParts()) {
            part.setMute(muted);
        }
    }

    public void setup(GrooveMachineDescriptor descriptor) throws CausticException {
        setupParts(descriptor);
        setupPatterns();
    }

    private void setupParts(GrooveMachineDescriptor descriptor) throws CausticException {
        getSound().setupParts(descriptor);
    }

    protected void setupPatterns() {
        MemoryBank memoryBank = getMemory().getSelectedMemoryBank();
        for (int i = 0; i < 64; i++) {
            Pattern pattern = memoryBank.getPattern(i);
            PatternMemoryItem patternItem = pattern.getMemoryItem();
            memoryBank.getPatternSlot().addItem(patternItem);

            // have to add parts first, set length, then set note data

            //            for (Part part : parts) {
            //                pattern.addPart(part);
            //                TrackPhrase phrase = memoryBank.getPhrase(part);
            //                PhraseMemoryItem phraseItem = phrase.getMemoryItem();
            //                memoryBank.getPhraseSlot().addItem(phraseItem);
            //
            //                int bankIndex = PatternUtils.getBank(i);
            //                int patternIndex = PatternUtils.getPattern(i);
            //                PartUtils.setBankPattern(part, bankIndex, patternIndex);
            //                phrase.configure();
            //
            //            }
        }

        // reset bank/pattern to 0
        for (Part part : getSound().getParts()) {
            PartUtils.setBankPattern(part, 0, 0);
        }
    }

    void beatChange(int measure, float beat) {
        // CausticCore > IGame > ISystemSequencer > GrooveStation > GrooveMachine
        machineSequencer.beatChange(measure, beat);
    }

    //--------------------------------------------------------------------------
    // IDispatcher API
    //--------------------------------------------------------------------------

    @Override
    public <T> void register(Class<T> event, EventObserver<T> observer) {
        dispatcher.register(event, observer);
    }

    @Override
    public void unregister(EventObserver<?> observer) {
        dispatcher.unregister(observer);
    }

    @Override
    public void trigger(Object event) {
        dispatcher.trigger(event);
    }

    @Override
    public void clear() {
        dispatcher.clear();
    }

}
