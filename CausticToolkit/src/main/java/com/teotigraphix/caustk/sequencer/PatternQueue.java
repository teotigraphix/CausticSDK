
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
        System.out.println("StateChange: " + pattern.toString() + " " + state);
    }

    public void addPattern(int bank, int index) {
        int len = patterns.size();
        PatternFSM pattern = new PatternFSM(getDispatcher(), bank, index);
        patterns.add(pattern);        
    }
}
