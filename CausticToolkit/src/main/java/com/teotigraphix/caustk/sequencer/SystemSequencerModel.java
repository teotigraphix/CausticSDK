
package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;

public class SystemSequencerModel extends SubControllerModel {

    private boolean isPlaying;

    public void setIsPlaying(boolean value) {
        isPlaying = value;
        OutputPanelMessage.PLAY.send(getController(), isPlaying ? 1 : 0);
    }

    public final boolean isPlaying() {
        return isPlaying;
    }

    private SequencerMode sequencerMode;

    public final SequencerMode getSequencerMode() {
        return sequencerMode;
    }

    public final void setSequencerMode(SequencerMode value) {
        sequencerMode = value;
        OutputPanelMessage.MODE.send(getController(), sequencerMode.getValue());
    }

    private float tempo;

    public void setTempo(float value) {
        tempo = value;
        OutputPanelMessage.BPM.send(getController(), tempo);
    }

    public float getTempo() {
        return tempo;
    }

    public SystemSequencerModel() {
    }

    public SystemSequencerModel(ICaustkController controller) {
        super(controller);
    }

}
