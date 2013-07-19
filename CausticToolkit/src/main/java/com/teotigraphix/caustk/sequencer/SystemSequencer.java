
package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.controller.command.CommandBase;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.core.CausticEventListener;

public class SystemSequencer extends SubControllerBase implements ISystemSequencer,
        CausticEventListener {
    
    //----------------------------------
    // modelType
    //----------------------------------

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return SystemSequencerModel.class;
    }

    //----------------------------------
    // model
    //----------------------------------

    SystemSequencerModel getModel() {
        return (SystemSequencerModel)getInternalModel();
    }

    @Override
    public void setIsPlaying(boolean value) {
        getModel().setIsPlaying(value);
    }

    @Override
    public boolean isPlaying() {
        return getModel().isPlaying();
    }

    @Override
    public final SequencerMode getSequencerMode() {
        return getModel().getSequencerMode();
    }

    @Override
    public void setTempo(float value) {
        getModel().setTempo(value);
    }

    @Override
    public float getTempo() {
        return getModel().getTempo();
    }

    public final void setSequencerMode(SequencerMode value) {
        getModel().setSequencerMode(value);
    }

    public SystemSequencer(ICaustkController controller) {
        super(controller);

        controller.addComponent(ISystemSequencer.class, this);

        controller.getSoundGenerator().addEventListener(this);
    }

    @Override
    public void play(SequencerMode mode) {
        setSequencerMode(mode);
        setIsPlaying(true);
    }

    @Override
    public void stop() {
        setIsPlaying(false);
    }

    public void executePlay() {
        getController().execute(COMMAND_PLAY);
    }

    public void executeStop() {
        getController().execute(COMMAND_STOP);
    }

    public void executeTempo(float tempo) {
        getController().execute(COMMAND_STOP, tempo);
    }

    @Override
    public void restore() {
    }

    //--------------------------------------------------------------------------
    // Public Command API
    //--------------------------------------------------------------------------

    public static class SystemSequencerPlayCommand extends CommandBase {
        @Override
        public void execute() {
            int smode = CommandUtils.getInteger(getContext(), 0);
            SequencerMode mode = SequencerMode.fromInt(smode);
            getContext().getComponent(ISystemSequencer.class).play(mode);
        }
    }

    public static class SystemSequencerStopCommand extends CommandBase {
        @Override
        public void execute() {
            getContext().getComponent(ISystemSequencer.class).stop();
        }
    }

    public static class SystemSequencerTempoCommand extends UndoCommand {

        float last;

        @Override
        protected void doExecute() {
            last = getContext().getComponent(ISystemSequencer.class).getTempo();
            float bpm = CommandUtils.getFloat(getContext(), 0);
            getContext().getComponent(ISystemSequencer.class).setTempo(bpm);
        }

        @Override
        protected void undoExecute() {
            getContext().getComponent(ISystemSequencer.class).setTempo(last);
        }
    }

    @Override
    public void OnBeatChanged(int beat) {
        System.out.println("   beat " + beat);
    }

    @Override
    public void OnMeasureChanged(int measure) {
        System.out.println("measure " +  measure);
    }

}
