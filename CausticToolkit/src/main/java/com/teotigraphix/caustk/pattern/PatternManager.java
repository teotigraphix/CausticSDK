
package com.teotigraphix.caustk.pattern;

import java.util.Collection;

import com.sun.jna.Memory;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.PatternUtils;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.sequencer.SystemSequencer;
import com.teotigraphix.caustk.system.TemporaryMemory;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;

public class PatternManager implements IControllerComponent, IPatternManager {

    //--------------------------------------------------------------------------
    // Public OSC Command API
    //--------------------------------------------------------------------------

    /**
     * Queues the next pattern in the current bank, does not commit.
     * <p>
     * This pattern will be in the {@link ControllerAPI#getPendingPattern()}.
     * 
     * @see OnPatternSequencerPatternChangePending
     * @see UndoCommand
     */
    public static final String COMMAND_PATTERN_NEXT = "pattern_sequencer/pattern_next";

    /**
     * Commits and plays the next queued pattern.
     * 
     * @see PatternSequencerPatternPlayCommand
     * @see UndoCommand
     */
    public static final String COMMAND_PATTERN_PLAY = "pattern_sequencer/pattern_play";

    /**
     * Queues, commits and plays the next pattern synchronously.
     * 
     * @see PatternSequencerPatternCommand
     * @see UndoCommand
     */
    public static final String COMMAND_PATTERN = "pattern_sequencer/pattern";

    public final IDispatcher getDispatcher() {
        return controller.getDispatcher();
    }

    private ICaustkController controller;

    //----------------------------------
    // bank & pattern
    //----------------------------------

    private int nextPattern = -1;

    @Override
    public void setNextPattern(int pattern) {
        if (pattern == nextPattern)
            return;

        // if the sequencer is locked, return
        // the sequencer is locked when we are in the last beat of the pattern
        if (getPattern() != null) {
            int beat = controller.getSystemSequencer().getCurrentBeat() + 1;
            int measure = controller.getSystemSequencer().getCurrentMeasure() + 1;
            int position = getPattern().getSelectedPart().getPhrase().getPosition();
            //            if (beat % 4 == 1 && position == measure - 1) // last beat, last measure
            //                throw new RuntimeException("Pattern change locked");
        }

        updateName(pattern);

        nextPattern = pattern;

        pendingPattern = controller.getMemoryManager().getTemporaryMemory().copyPattern(pattern);

        getDispatcher().trigger(new OnPatternSequencerPatternChangePending());

        // if the sequencer is not playing, advance automatically.
        if (!controller.getSystemSequencer().isPlaying()) {
            playNextPattern();
        }
    }

    private void updateName(int pattern) {
        int bank = pattern / 16;
        int index = pattern % 16;
        //@SuppressWarnings("unused")
        //String text = PatternUtils.toString(bank, index);
        //        systemController.setDisplay(text, "main");
    }

    @Override
    public void playNextPattern() {
        if (pendingPattern == null)
            return;

        // !!! There has to be a commitPattern() method on the temp memory
        // Because we want to pre setup the pattern but not actually change
        // it until the sequencer finishes the current pattern's length measures
        // TEMP for now we just do this right here until we sync into the
        // core's beat and measure callbacks
        controller.getMemoryManager().getTemporaryMemory().commit();
        setPattern(pendingPattern);
    }

    @Override
    public Pattern playPattern(int pattern) {
        setNextPattern(pattern);
        playNextPattern();
        return getPattern();
    }

    //----------------------------------
    // pattern
    //----------------------------------

    // We should not have this ref, we should proxy straight back to
    // the temp memory that actually owns the instance. This way there
    // is no mistake when saving happens or changing, there is nothing to clean
    // up, only in the temp memory area
    private Pattern pattern;

    private Pattern pendingPattern;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    private void setPattern(Pattern value) {
        pendingPattern = null;

        Pattern oldPattern = pattern;

        pattern = value;

        getDispatcher().trigger(new OnPatternSequencerPatternChange(pattern, oldPattern));
    }

    @Override
    public Pattern getPendingPattern() {
        return pendingPattern;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public PatternManager(ICaustkController controller) {
        this.controller = controller;
    }

    //-----------------------------------------------------------------------------
    // CONFIGURATION

    private static PatternLocation pattern1 = new PatternLocation(0, 1);

    private static PatternLocation pattern2 = new PatternLocation(0, 2);

    private static PatternLocation currentPatternLocation = pattern1;

    private static PatternLocation nextPatternLocation = pattern2;

    /**
     * @see SystemSequencer#setNextPattern(int)
     * @see TemporaryMemory#copyPattern(int)
     * @see Memory#copyPattern(int)
     */
    public void configure(Pattern pattern) {
        // copies serialized data from disk into the pattern model.
        // nothing happens anywhere except inside the  Pattern right now,
        // this the 'configure', when 'commit' is called, all settings
        // of the Pattern model that apply to global devices get instantly applied

        try {
            configureParts(pattern);
        } catch (CausticException e) {
            e.printStackTrace();
        }

        queueNext(pattern);

        configurePartData(pattern);
        configureProperteis(pattern);
    }

    protected void configureProperteis(Pattern pattern) {
    }

    protected void configureParts(Pattern pattern) throws CausticException {
        Collection<Tone> tones = controller.getSoundSource().getTones();
        if (tones.size() == 0) {
            // initialize the pattern set
            for (ToneDescriptor descriptor : pattern.getPatternItem().getToneSet().getDescriptors()) {
                Tone tone = controller.getSoundSource().createTone(descriptor);
                Part part = null;

                if (tone instanceof BeatboxTone) {
                    part = new RhythmPart(pattern, tone);
                } else {
                    part = new SynthPart(pattern, tone);
                }

                pattern.addPart(part);
            }
        } else {
            // Tones have already been created
            for (Tone tone : controller.getSoundSource().getTones()) {
                Part part = null;

                if (tone instanceof BeatboxTone) {
                    part = new RhythmPart(pattern, tone);
                } else {
                    part = new SynthPart(pattern, tone);
                }

                pattern.addPart(part);
            }
        }

        for (Part part : pattern.getParts()) {
            controller.getMemoryManager().getSelectedMemoryBank().copyPhrase(part, 0);
            controller.getMemoryManager().getSelectedMemoryBank().copyPatch(part, 0);
        }
    }

    protected void configurePartData(Pattern pattern) {
        for (Part part : pattern.getParts()) {
            configurePart(part);
        }
    }

    protected void configurePart(Part part) {
        if (part.isRhythm()) {
        } else {
        }
    }

    /**
     * @see SystemSequencer#playNextPattern()
     * @see TemporaryMemory#commit()
     */
    public void commit(Pattern pattern) {
        for (Part part : pattern.getParts()) {
            part.getPatch().commit();
            part.getPhrase().commit();
        }

        commitPropertySettings(pattern);
    }

    protected void commitPropertySettings(Pattern pattern) {
        // set the default tempo/bpm
        if (pattern.getPatternItem() != null) {
            pattern.setTempo(pattern.getPatternItem().getTempo());
        }

        for (Part part : pattern.getParts()) {
            PatternSequencerComponent component = part.getTone().getComponent(
                    PatternSequencerComponent.class);
            component.clearIndex(currentPatternLocation.pattern);

            changePosition(nextPatternLocation, part);
        }

        if (pattern.getPatternItem() != null) {
            pattern.setLength(pattern.getPatternItem().getLength());
        }

        PatternLocation lastPatternLocation = currentPatternLocation;
        currentPatternLocation = nextPatternLocation;
        nextPatternLocation = lastPatternLocation;

        pattern.setSelectedPart(pattern.getPart(0));
    }

    private void queueNext(Pattern pattern) {
        // add the phrase data
        for (Part part : pattern.getParts()) {
            // switch to edit
            changePosition(nextPatternLocation, part);

            part.getPhrase().configure();

            // switch back to current
            changePosition(currentPatternLocation, part);
        }
    }

    private void changePosition(PatternLocation location, Part part) {
        PatternSequencerComponent component = part.getTone().getComponent(
                PatternSequencerComponent.class);
        component.setSelectedPattern(location.bank, location.pattern);
    }

    static class PatternLocation {
        private int bank;

        private int pattern;

        public PatternLocation(int bank, int pattern) {
            this.bank = bank;
            this.pattern = pattern;
        }
    }

    public static class OnPatternSequencerPatternChangePending {
    }

    public static class OnPatternSequencerPatternChange {
        private Pattern pattern;

        public final Pattern getPattern() {
            return pattern;
        }

        private Pattern oldPattern;

        public final Pattern getOldPattern() {
            return oldPattern;
        }

        public OnPatternSequencerPatternChange(Pattern pattern, Pattern oldPattern) {
            this.pattern = pattern;
            this.oldPattern = oldPattern;
        }
    }

    public static class PatternSequencerPatternNextCommand extends UndoCommand {

        int last = -1;

        @Override
        protected void doExecute() {
            // we have to use the pending pattern since this command sets
            // the next pending pattern and has NOT committed yet
            Pattern pattern = getContext().getComponent(IPatternManager.class).getPendingPattern();
            if (pattern != null)
                last = pattern.getIndex();

            int index = CommandUtils.getInteger(getContext(), 0);
            getContext().getComponent(IPatternManager.class).setNextPattern(index);
        }

        @Override
        protected void undoExecute() {
            if (last != -1)
                getContext().getComponent(IPatternManager.class).setNextPattern(last);
        }
    }

    public static class PatternSequencerPatternPlayCommand extends UndoCommand {

        @Override
        protected void doExecute() {
            getContext().getComponent(IPatternManager.class).playNextPattern();
        }

        @Override
        protected void undoExecute() {
            // TODO What is the undo, save last pattern and instantly change back
            // no queing? its more like if the sequencer is playing, this
            // undo does not apply, will get to it later
        }
    }

    public static class PatternSequencerPatternCommand extends UndoCommand {
        int last = -1;

        @Override
        protected void doExecute() {
            Pattern pattern = getContext().getComponent(IPatternManager.class).getPattern();
            if (pattern != null)
                last = pattern.getIndex();

            int index = CommandUtils.getInteger(getContext(), 0);
            getContext().getComponent(IPatternManager.class).playPattern(index);
        }

        @Override
        protected void undoExecute() {
            if (last != -1)
                getContext().getComponent(IPatternManager.class).playPattern(last);
        }
    }
}
