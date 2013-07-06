
package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustic.output.IOutputPanel;
import com.teotigraphix.caustic.output.IOutputPanel.Mode;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IControllerAPI;
import com.teotigraphix.caustk.sequencer.SystemSequencer.OnSystemSequencerPlay;
import com.teotigraphix.caustk.sequencer.SystemSequencer.OnSystemSequencerStop;
import com.teotigraphix.caustk.sequencer.SystemSequencer.SystemSequencerPlayCommand;
import com.teotigraphix.caustk.sequencer.SystemSequencer.SystemSequencerStopCommand;
import com.teotigraphix.caustk.sequencer.SystemSequencer.SystemSequencerTempoCommand;

public class SequencerAPI implements IControllerAPI {

    private ICaustkController controller;

    public SequencerAPI(ICaustkController controller) {
        this.controller = controller;
        commitCommands();
    }

    private void commitCommands() {
        controller.getCommandManager().put(SystemSequencer.COMMAND_PLAY,
                SystemSequencerPlayCommand.class);
        controller.getCommandManager().put(SystemSequencer.COMMAND_STOP,
                SystemSequencerStopCommand.class);
        controller.getCommandManager().put(SystemSequencer.COMMAND_TEMPO,
                SystemSequencerTempoCommand.class);

    }

    public boolean isPlaying() {
        return controller.getSystemSequencer().isPlaying();
    }

    /**
     * Plays the system sequencer.
     * 
     * @param mode The play mode; pattern/song
     * @see OnSystemSequencerPlay
     * @see SystemSequencer#COMMAND_PLAY
     */
    public void play() {
        controller.getSystemSequencer().play();
    }

    /**
     * @see #play()
     */
    public void play(Mode mode) {
        controller.getSystemSequencer().play(mode);
    }

    /**
     * Stops the system sequencer.
     * 
     * @see OnSystemSequencerStop
     * @see SystemSequencer#COMMAND_PLAY
     */
    public void stop() {
        controller.getSystemSequencer().stop();
    }

    /**
     * Returns the {@link IOutputPanel#getBPM()}.
     * <p>
     * Most clients will need to use the {@link Pattern#getTempo()}.
     */
    public float getTempo() {
        return controller.getSystemSequencer().getTempo();
    }

    /**
     * Sets the bpm using the {@link IOutputPanel#setBPM(float)}.
     * <p>
     * Note; Use the {@link Pattern#setTempo(float)}, this method is for low
     * level implementations like {@link Pattern}.
     * 
     * @param value The new tempo bpm. TODO Think about a way to get this off
     *            the public API
     */
    public void setTempo(float value) {
        controller.getSystemSequencer().setTempo(value);
    }

}
