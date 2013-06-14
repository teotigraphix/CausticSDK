
package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustic.core.CausticEventListener;
import com.teotigraphix.caustic.core.Dispatcher;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustic.internal.output.OutputPanel;
import com.teotigraphix.caustic.internal.sequencer.Sequencer;
import com.teotigraphix.caustic.output.IOutputPanel;
import com.teotigraphix.caustic.output.IOutputPanel.Mode;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.command.CommandBase;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;

/*

Lol ... a keyframe in this context is the same as in an animation (if you
did any of that in your 3D work). Basically it's a time-value pair. Ex:
[Time=0, Value=0] [Time=0.5, Value=3]

For Caustic, the time is beats and the value is your OSC value for the
control you want to automate. The engine looks at the current time,
determines where it sits between the two keys, interpolates the value
accordingly and passes that onto the control in question.

So yeah, I could give you something like /caustic/sequencer addkey
[machine_index_or_name] [control_name] [time] [value]
and also a Query so that you could get all the keys, now this could get
pretty big, so it might need to be done in multiple stages (like query
which controls have keys, then query the keys for one control)

sequencer.addAutomationKey(String machineId, controlId, beat, value);

How would I know the exact beat?


*/

/**
 * Records controller operations as OSC messages.
 * <p>
 * Can playback OSC messages recorded.
 * <p>
 * OSC messages recorded can be transmitted externally to other devices.
 * <p>
 * Tracks, Song and Patters; A client gives the sequencer Song or Pattern. The
 * sequencer will load the pattern and its Parts
 */
public class SystemSequencer implements CausticEventListener {

    //--------------------------------------------------------------------------
    // Public OSC Command API
    //--------------------------------------------------------------------------

    /**
     * Plays the song sequencer in pattern or song mode.
     * 
     * @see OnSystemSequencerPlay
     */
    public static final String COMMAND_PLAY = "system_sequencer/play";

    /**
     * Stops the song sequencer.
     * 
     * @see OnSystemSequencerStop
     */
    public static final String COMMAND_STOP = "system_sequencer/stop";

    /**
     * Sets the bpm of the {@link IOutputPanel}.
     * 
     * @see UndoCommand
     */
    public static final String COMMAND_TEMPO = "system_sequencer/tempo";

    private ICaustkController controller;

    private OutputPanel outputPanel;

    private Sequencer sequencer;

    private StepEngine stepEngine;

    private boolean playing;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // dispatcher
    //----------------------------------

    private IDispatcher dispatcher;

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    //----------------------------------
    // tempo
    //----------------------------------

    float getTempo() {
        return outputPanel.getBPM();
    }

    void setTempo(float value) {
        stepEngine.setTempo(value);
        outputPanel.setBPM(value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SystemSequencer(ICaustkController controller) {
        this.controller = controller;

        dispatcher = new Dispatcher();
        stepEngine = new StepEngine(controller);

        controller.registerAPI(SequencerAPI.class, new SequencerAPI(controller));

        createOutputPanel();
        createSequencer();

        controller.getSoundGenerator().addEventListener(this);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    boolean isPlaying() {
        return playing;
    }

    public Mode getMode() {
        return outputPanel.getMode();
    }

    //--------------------------------------------------------------------------
    // Controller Method API
    //--------------------------------------------------------------------------

    void setMode(Mode mode) {
        outputPanel.setMode(mode);
    }

    /**
     * Plays the system sequencer in Pattern mode.
     * 
     * @see OnSystemSequencerPlay
     */
    void play() {
        play(Mode.PATTERN);
    }

    void play(Mode mode) {
        if (playing)
            return;
        setMode(mode);
        playing = true;
        outputPanel.play();
        stepEngine.start(0, getTempo());
        getDispatcher().trigger(new OnSystemSequencerPlay());
    }

    void stop() {
        if (!playing)
            return;
        playing = false;
        outputPanel.stop();
        stepEngine.stop();
        setCurrentMeasure(0);
        getDispatcher().trigger(new OnSystemSequencerStop());
    }

    //--------------------------------------------------------------------------
    // Protected Method API
    //--------------------------------------------------------------------------

    private void createOutputPanel() {
        outputPanel = (OutputPanel)controller.getFactory().createOutputPanel();
        outputPanel.setEngine(controller);
    }

    private void createSequencer() {
        sequencer = (Sequencer)controller.getFactory().createSequencer();
        sequencer.setEngine(controller);
    }

    //--------------------------------------------------------------------------
    // Observer Method API
    //--------------------------------------------------------------------------

    public static class OnSystemSequencerBeatChange {
        private int beat;

        public int getBeat() {
            return beat;
        }

        public OnSystemSequencerBeatChange(int beat) {
            this.beat = beat;
        }
    }

    public static class OnSystemSequencerStepChange {
        private int step;

        public int getStep() {
            return step;
        }

        public OnSystemSequencerStepChange(int step) {
            this.step = step;
        }
    }

    public static class OnSystemSequencerMeasureChange {
        private int measure;

        public int getMeasure() {
            return measure;
        }

        public OnSystemSequencerMeasureChange(int measure) {
            this.measure = measure;
        }
    }

    public static class OnSystemSequencerStop {
    }

    public static class OnSystemSequencerPlay {
    }

    @Override
    public void OnBeatChanged(int beat) {
        stepEngine.beatChanged(beat);

        updateMeasure(beat);

        getDispatcher().trigger(new OnSystemSequencerBeatChange(beat));
    }

    @Override
    public void OnMeasureChanged(int nNewMeasure) {
    }

    //----------------------------------
    // currentMeasure
    //----------------------------------

    private int mCurrentMeasure = 0;

    public int getCurrentMeasure() {
        return mCurrentMeasure;
    }

    void setCurrentMeasure(int value) {
        mCurrentMeasure = value;
        //        
        //        final int numMeasures = getPattern().getLength();
        //        int last = 1;
        //        if (numMeasures == 2)
        //            last = 
        if (mCurrentMeasure == 0) {
            //controller.getPatternSequencer().playNextPattern();
        }
        getDispatcher().trigger(new OnSystemSequencerMeasureChange(mCurrentMeasure));
    }

    public int getMeasureBeat() {
        return mCurrentBeat % 4;
    }

    //----------------------------------
    // currentBeat
    //----------------------------------

    private int mCurrentBeat = 0;

    public int getCurrentBeat() {
        return mCurrentBeat;
    }

    void setCurrentBeat(int value) {
        setCurrentBeat(value, false);
    }

    void setCurrentBeat(int value, boolean seeking) {
        int beatsInLength = 4;

        int last = mCurrentBeat;
        mCurrentBeat = value;

        //fireBeatChange(mCurrentBeat, last);

        if (last < value) {
            // forward
            if (mCurrentBeat == 0) {
                setCurrentMeasure(0);
            } else {
                int remainder = mCurrentBeat % beatsInLength;
                System.out.println("    remainder " + getMeasureBeat());
                if (seeking) {
                    setCurrentMeasure(mCurrentBeat / beatsInLength);
                } else if (remainder == 0) {
                    setCurrentMeasure(mCurrentMeasure + 1);
                }
            }
        } else if (last > value) {
            // reverse
            // if the last beat was a measure change, decrement measure
            int remainder = last % 4;
            if (remainder == 0) {
                setCurrentMeasure(mCurrentMeasure - 1);
            }
        }
    }

    int updateMeasure(int beat) {
        int len = 1; //controller.getPatternSequencer().getPattern().getLength();
        final int remainder = (beat + 1) % 4;
        if (len == 1) {
            if (remainder == 0)
                setCurrentMeasure(0);
        } else if (len == 2) {
            if (beat == 0 || beat == 8 || beat == 16 || beat == 24 || beat == 32)
                setCurrentMeasure(0);
            else if (beat == 4 || beat == 12 || beat == 20 || beat == 28)
                setCurrentMeasure(mCurrentMeasure + 1);
        } else if (len == 4) {
            if (beat == 0 || beat == 16 || beat == 32)
                setCurrentMeasure(0);
            else if (beat == 4 || beat == 8 || beat == 12 || beat == 20 || beat == 24 || beat == 28)
                setCurrentMeasure(mCurrentMeasure + 1);
        } else if (len == 8) {
            if (beat == 0)
                setCurrentMeasure(0);
            else if (remainder == 0)
                setCurrentMeasure(mCurrentMeasure + 1);
        }
        return 0;
    }

    // XXX how can I make this NOT public
    /**
     * XXX This needs to be removed
     * 
     * @param step
     */
    public void stepChanged(final int step) {
        /// XXX REMOVED controller.setDebugText(step + "");
        getDispatcher().trigger(new OnSystemSequencerStepChange(step));
    }

    // public void seek(int beat);

    //--------------------------------------------------------------------------
    // Public Command API
    //--------------------------------------------------------------------------

    public static class SystemSequencerPlayCommand extends CommandBase {
        @Override
        public void execute() {
            getContext().api(SequencerAPI.class).play();
        }
    }

    public static class SystemSequencerStopCommand extends CommandBase {
        @Override
        public void execute() {
            getContext().api(SequencerAPI.class).stop();
        }
    }

    public static class SystemSequencerTempoCommand extends UndoCommand {

        float last;

        @Override
        protected void doExecute() {
            last = getContext().api(SequencerAPI.class).getTempo();
            float bpm = CommandUtils.getFloat(getContext(), 0);
            getContext().api(SequencerAPI.class).setTempo(bpm);
        }

        @Override
        protected void undoExecute() {
            getContext().api(SequencerAPI.class).setTempo(last);
        }
    }
}
