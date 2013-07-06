
package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustic.internal.sequencer.PatternSequencer;

import au.com.ds.ef.EasyFlow;
import au.com.ds.ef.Event;
import au.com.ds.ef.FlowBuilder;
import au.com.ds.ef.State;
import au.com.ds.ef.StatefulContext;
import au.com.ds.ef.SyncExecutor;
import au.com.ds.ef.call.StateHandler;

public class PatternFSM {

    private EasyFlow<Context> flow;

    private Context context;

    //--------------------------------------------------------------------------
    // States
    //--------------------------------------------------------------------------

    // To   : QUEUED_STATE
    // From : STOPPED_STATE
    private final State<Context> IDLE = FlowBuilder.state("IDLE");

    // To   : PLAYING_STATE, UNQUEUED_STATE
    // From : IDLE
    private final State<Context> QUEUED_STATE = FlowBuilder.state("QUEUED_STATE");

    // To   : UNQUEUED_STATE
    // From : QUEUED_STATE
    private final State<Context> PLAYING_STATE = FlowBuilder.state("PLAYING_STATE");

    // To   : QUEUED_STATE, PLAYING_STATE
    // From : STOPPED_STATE
    private final State<Context> UNQUEUED_STATE = FlowBuilder.state("UNQUEUED_STATE");

    // To   : IDLE
    // From : STOPPED_STATE
    private final State<Context> STOPPED_STATE = FlowBuilder.state("STOPPED_STATE");

    //--------------------------------------------------------------------------
    // Transition Events
    //--------------------------------------------------------------------------

    private final Event<Context> onIdle = FlowBuilder.event("onIdle");

    private final Event<Context> onQueued = FlowBuilder.event("onQueued");

    private final Event<Context> onUnQueued = FlowBuilder.event("onUnQueued");

    private final Event<Context> onStop = FlowBuilder.event("onStop");

    private final Event<Context> onPlay = FlowBuilder.event("onPlay");

    private final IDispatcher dispatcher;

    private int index;

    private int bank;

    /**
     * The pattern bank in the {@link PatternSequencer}.
     */
    public int getBank() {
        return bank;
    }

    /**
     * The pattern index in the {@link PatternSequencer}.
     */
    public int getIndex() {
        return index;
    }

    public PatternFSM(IDispatcher dispatcher, int bank, int index) {
        this.dispatcher = dispatcher;
        this.bank = bank;
        this.index = index;

        initialize();
        bind();

        context = new Context();

        flow.executor(new SyncExecutor()).start(context);
    }

    private void initialize() {
        flow = FlowBuilder.from(IDLE).transit(
                onQueued.to(QUEUED_STATE).transit(
                        onPlay.to(PLAYING_STATE).transit(onUnQueued.to(UNQUEUED_STATE)),
                        onUnQueued.to(UNQUEUED_STATE).transit(
                                onStop.to(STOPPED_STATE).transit(onIdle.to(IDLE)))));
    }

    private void bind() {
        IDLE.whenEnter(new StateHandler<Context>() {
            @Override
            public void call(State<Context> arg0, final Context context) throws Exception {
                context.idle();
                dispatcher.trigger(new OnStateChange(PatternFSM.this, PatternState.IDLE));
            }
        });
        QUEUED_STATE.whenEnter(new StateHandler<Context>() {
            @Override
            public void call(State<Context> arg0, final Context context) throws Exception {
                context.isIdle = false;
                context.isQued = true;
                dispatcher.trigger(new OnStateChange(PatternFSM.this, PatternState.QUEUED));
            }
        });
        PLAYING_STATE.whenEnter(new StateHandler<Context>() {
            @Override
            public void call(State<Context> arg0, final Context context) throws Exception {
                context.isQued = false;
                context.isPlaying = true;
                dispatcher.trigger(new OnStateChange(PatternFSM.this, PatternState.PLAYING));
            }
        });
        UNQUEUED_STATE.whenEnter(new StateHandler<Context>() {
            @Override
            public void call(State<Context> arg0, final Context context) throws Exception {
                context.isUnQueued = true;
                context.isQued = false;
                dispatcher.trigger(new OnStateChange(PatternFSM.this, PatternState.UNQUEUED));
            }
        });
        STOPPED_STATE.whenEnter(new StateHandler<Context>() {
            @Override
            public void call(State<Context> arg0, final Context context) throws Exception {
                onIdle.trigger(context);
                context.measure = 1;
                dispatcher.trigger(new OnStateChange(PatternFSM.this, PatternState.STOPPED));
            }
        });
    }

    public void nextMeasure() {
        if (mode == PatternMode.LOOP) {
            if (context.isQueued()) {
                onPlay.trigger(context);
            } else if (context.isUnQueued()) {
                onStop.trigger(context);
            } else if (context.isPlaying()) {
                context.measure++;
            }

        } else {
            // one shot only can play when queued, if playing is stopped
            if (context.isQueued())
                onPlay.trigger(context);
            else if (context.isPlaying()) {
                onUnQueued.trigger(context);
                onStop.trigger(context);
            }
        }
    }

    public void touch() {

        if (mode == PatternMode.LOOP) {
            // if is playing enqueue so the pattern queue will remove
            // next measure
            if (context.isPlaying()) {
                onUnQueued.trigger(context);
                return;
            }

            if (context.isQueued())
                onUnQueued.trigger(context);
            else
                onQueued.trigger(context);

        } else if (mode == PatternMode.ONESHOT) {
            // if the onshot is playing, nothing happens since we are
            // using mesaures to switch on
            if (context.isPlaying()) {
                return;
            }

            if (context.isQueued()) {
                onUnQueued.trigger(context);
                onStop.trigger(context);
            } else {
                onQueued.trigger(context);
            }
        }

    }

    public static class Context extends StatefulContext {

        private static final long serialVersionUID = 1L;

        int length = 1;

        int measure = 0;

        boolean isIdle = true;

        boolean isPlaying = false;

        boolean isQued = false;

        boolean isUnQueued = false;

        boolean isQueued() {
            return isQued;
        }

        void idle() {
            isIdle = true;
            isPlaying = false;
            isQued = false;
            isUnQueued = false;
        }

        boolean isUnQueued() {
            return isUnQueued;
        }

        boolean isPlaying() {
            return isPlaying;
        }

        public boolean isIdle() {
            return isIdle;
        }
    }

    public boolean isQueued() {
        return context.isQueued();
    }

    public boolean isUnQueued() {
        return context.isUnQueued();
    }

    public boolean isPlaying() {
        return context.isPlaying();
    }

    public boolean isIdle() {
        return context.isIdle();
    }

    private boolean gate;

    public boolean isGate() {
        return gate;
    }

    public void setGate(boolean value) {
        gate = value;
    }

    private PatternMode mode = PatternMode.ONESHOT;

    public PatternMode getMode() {
        return mode;
    }

    public void setMode(PatternMode value) {
        mode = value;
    }

    public enum PatternMode {
        ONESHOT, LOOP;
    }

    public enum PatternState {
        IDLE, QUEUED, PLAYING, UNQUEUED, STOPPED;
    }

    public static class OnStateChange {

        private final PatternFSM pattern;

        public PatternFSM getPattern() {
            return pattern;
        }

        private final PatternState patternState;

        public PatternState getState() {
            return patternState;
        }

        public OnStateChange(PatternFSM pattern, PatternState patternState) {
            this.pattern = pattern;
            this.patternState = patternState;
        }
    }

    @Override
    public String toString() {
        return "PatternFSM[" + bank + ":" + index + "]";
    }

}
