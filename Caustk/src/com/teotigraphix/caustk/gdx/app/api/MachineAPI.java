
package com.teotigraphix.caustk.gdx.app.api;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.gdx.app.AbstractProjectModelAPI;
import com.teotigraphix.caustk.gdx.app.IProjectModel.ProjectModelEvent;
import com.teotigraphix.caustk.gdx.app.ProjectModel;
import com.teotigraphix.caustk.gdx.app.ProjectState;
import com.teotigraphix.caustk.node.machine.MachineNode;
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
        return getRackNode().containsMachine(index);
    }

    public MachineNode getMachine(int index) {
        return getRackNode().getMachine(index);
    }

    //----------------------------------
    // selectedMachine
    //----------------------------------

    public final int getSelectedMachineIndex() {
        return getSelectedMachine().getIndex();
    }

    public void setSelectedMachineIndex(int machineIndex) {
        if (getRackNode().getSelectedIndex() == machineIndex)
            return;
        getRackNode().setSelectedIndex(machineIndex);
        post(new ProjectModelEvent(ProjectModelEvent.Kind.MachineSelectionChange, getProjectModel()));
    }

    public final MachineNode getSelectedMachine() {
        return getRackNode().getSelectedMachine();
    }

    public final PatternNode getSelectedMachinePattern() {
        MachineNode selectedMachine = getSelectedMachine();
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
}
