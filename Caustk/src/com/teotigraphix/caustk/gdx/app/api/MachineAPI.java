
package com.teotigraphix.caustk.gdx.app.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.teotigraphix.caustk.gdx.app.AbstractProjectModelAPI;
import com.teotigraphix.caustk.gdx.app.IProjectModel.ProjectModelEvent;
import com.teotigraphix.caustk.gdx.app.ProjectModel;
import com.teotigraphix.caustk.gdx.app.ProjectState;
import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;

public class MachineAPI extends AbstractProjectModelAPI {

    private List<Integer> triggereddMachines = new ArrayList<Integer>();

    //----------------------------------
    // triggereddMachines
    //----------------------------------

    public List<Integer> getTriggeredMachines() {
        return triggereddMachines;
    }

    public void setTriggeredMachines(List<Integer> triggereddMachines) {
        this.triggereddMachines = triggereddMachines;
    }

    //----------------------------------
    // machine
    //----------------------------------

    public boolean hasMachine(int index) {
        return getRackInstance().containsMachine(index);
    }

    public Machine getMachine(int index) {
        return getRackInstance().getMachine(index);
    }

    //----------------------------------
    // selectedMachine
    //----------------------------------

    public final int getSelectedMachineIndex() {
        return getSelectedMachine().getIndex();
    }

    public void setSelectedMachineIndex(int machineIndex) {
        if (getRackInstance().getSelectedIndex() == machineIndex)
            return;
        getRackInstance().setSelectedIndex(machineIndex);
        post(new ProjectModelEvent(ProjectModelEvent.Kind.MachineSelectionChange, getProjectModel()));
    }

    public final Machine getSelectedMachine() {
        return getRackInstance().getSelectedMachine();
    }

    public final PatternNode getSelectedMachinePattern() {
        Machine selectedMachine = getSelectedMachine();
        if (selectedMachine == null)
            return null;
        return selectedMachine.getSequencer().getSelectedPattern();
    }

    public MachineAPI(ProjectModel projectModel) {
        super(projectModel);
    }

    @Override
    public void restore(ProjectState state) {
        getProjectModel().getEventBus().post(
                new ProjectModelEvent(ProjectModelEvent.Kind.MachineSelectionChange,
                        getProjectModel()));
    }

    public Collection<? extends Machine> machines() {
        return getRackInstance().getMachines();
    }
}
