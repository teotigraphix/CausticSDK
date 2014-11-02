
package com.teotigraphix.gdx.app;

import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.gdx.app.IProjectModel.ProjectModelEvent;
import com.teotigraphix.gdx.app.IProjectModel.ProjectModelEventKind;

public class ProjectModelMachineAPI extends AbstractProductModelAPI {

    public boolean hasMachine(int index) {
        return getProject().getRackNode().containsMachine(index);
    }

    public final int getSelectedMachineIndex() {
        return getSelectedMachine().getIndex();
    }

    public void setSelectedMachineIndex(int machineIndex) {
        if (getProject().getRackNode().getSelectedIndex() == machineIndex)
            return;
        getProject().getRackNode().setSelectedIndex(machineIndex);
        post(new ProjectModelEvent(ProjectModelEventKind.MachineSelectionChange, getProjectModel()));
    }

    public MachineNode getMachine(int channelIndex) {
        return getProject().getRackNode().getMachine(channelIndex);
    }

    public final MachineNode getSelectedMachine() {
        return getProject().getRackNode().getSelectedMachine();
    }

    public final PatternNode getSelectedMachinePattern() {
        MachineNode selectedMachine = getSelectedMachine();
        if (selectedMachine == null)
            return null;
        return selectedMachine.getSequencer().getSelectedPattern();
    }

    public ProjectModelMachineAPI(ProjectModel projectModel) {
        super(projectModel);
    }

}
