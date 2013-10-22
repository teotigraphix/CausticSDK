
package com.teotigraphix.caustk.gs.model;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.gs.config.IGrooveStationConfiguration;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.rack.tone.BeatboxTone;
import com.teotigraphix.caustk.rack.tone.PCMSynthTone;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IGrooveStationModel extends ICaustkModel, IControllerComponent {

    IGrooveStationConfiguration getConfiguration();

    /**
     * The current machine in edit view.
     */
    GrooveMachine getCurrentMachine();

    GrooveMachine getMachine(int machineIndex);

    /**
     * Selects the current part held in the {@link MachineSound#getParts()}.
     * 
     * @param machineIndex The groove station machine index.
     * @param partIndex The part index within the machine index.
     * @see OnGrooveStationModelChange
     * @see GrooveStationModelChangeKind#SelectedPart
     */
    //    void selectPart(int machineIndex, int partIndex);

    /**
     * Selects a current channel held in a rhythm part such as the
     * {@link BeatboxTone} or the {@link PCMSynthTone}.
     * 
     * @param machineIndex The groove station machine index.
     * @param partIndex The part index within the machine index.
     * @param channelIndex The rhythm channel index.
     * @see OnGrooveStationModelChange
     * @see GrooveStationModelChangeKind#SelectedPart
     */
    //    void selectRhythmPart(int machineIndex, int partIndex, int channelIndex);

    public static class OnGrooveStationStartMachines {
    }

    public enum GrooveStationModelChangeKind {
    }

    public static class OnGrooveStationModelChange {

        private GrooveStationModelChangeKind kind;

        public GrooveStationModelChangeKind getKind() {
            return kind;
        }

        public OnGrooveStationModelChange(GrooveStationModelChangeKind kind) {
            this.kind = kind;
        }
    }

}
