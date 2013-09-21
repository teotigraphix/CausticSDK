
package com.teotigraphix.caustk.gs.model;

import com.teotigraphix.caustk.gs.config.IGrooveStationConfiguration;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IGrooveStationModel extends ICaustkModel {

    IGrooveStationConfiguration getConfiguration();

    /**
     * The current machine in edit view.
     */
    GrooveMachine getCurrentMachine();

    GrooveMachine getMachine(int machineIndex);

    void selectPart(int machineIndex, int partIndex);

    public static class OnGrooveStationStartMachines {
    }

    public enum GrooveStationModelChangeKind {
        SelectedPart;
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
