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

package com.teotigraphix.caustk.sequencer;

import java.util.ArrayList;
import java.util.List;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustic.core.Dispatcher;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustk.sequencer.PatternFSM.OnStateChange;
import com.teotigraphix.caustk.sequencer.PatternFSM.PatternState;

/*

- user loads a .caustic song which acts as a pattern library
- the song holds a possible 6 machines with 4 banks and 16 patterns per bank

- 4 * 16 = 64
- 64 * 6 = 384

- bank D pattern 16 is reserved for empty pattern play
  - this makes 380 patterns possible

- for now think of loading a .caustic song as loading a Scene

*/

public class PatternQueue {

    private List<PatternFSM> patterns = new ArrayList<PatternFSM>();

    // Holds PatternFSM state machines.
    // Listens to each pattern for its events when changing state
    private final IDispatcher dispatcher;

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    public PatternQueue() {
        this.dispatcher = new Dispatcher();
        dispatcher.register(OnStateChange.class, new EventObserver<OnStateChange>() {
            @Override
            public void trigger(OnStateChange object) {
                onStateChanged(object.getPattern(), object.getState());
            }
        });
    }

    protected void onStateChanged(PatternFSM pattern, PatternState state) {
        //System.out.println("StateChange: " + pattern.toString() + " " + state);
    }

    void addPattern(int bank, int index) {
        //int len = patterns.size();
        PatternFSM pattern = new PatternFSM(getDispatcher(), bank, index);
        patterns.add(pattern);
    }

    public void initialize(int numBanks, int numPatterns) {
        for (int i = 0; i < numBanks; i++) {
            for (int j = 0; j < numPatterns; j++) {
                addPattern(i, j);
            }
        }
    }

    public int getNumPatterns() {
        return patterns.size();
    }

    public PatternFSM touch(int bank, int index) {
        PatternFSM pattern = getPattern(bank, index);
        pattern.touch();
        return pattern;
    }

    public void nextMeasure() {
        for (PatternFSM pattern : patterns) {
            pattern.nextMeasure();
        }
    }

    private PatternFSM getPattern(int bank, int index) {
        for (PatternFSM pattern : patterns) {
            if (pattern.getBank() == bank && pattern.getIndex() == index)
                return pattern;
        }
        return null;
    }
}
