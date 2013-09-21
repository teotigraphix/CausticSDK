
package com.teotigraphix.caustk.gs.model;

import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gs.config.IGrooveStationConfiguration;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.GrooveStation;
import com.teotigraphix.caustk.gs.machine.GrooveStation.GrooveStationSetup;
import com.teotigraphix.libgdx.model.CaustkModel;

public class GrooveStationModel extends CaustkModel implements IGrooveStationModel {

    @Inject
    IGrooveStationConfiguration configuration;

    @Override
    public IGrooveStationConfiguration getConfiguration() {
        return configuration;
    }

    private GrooveStation grooveStation;

    @Override
    public GrooveMachine getMachine(int machineIndex) {
        return getMachines().get(machineIndex);
    }

    private int currentMachineIndex;

    public int getCurrentMachineIndex() {
        return currentMachineIndex;
    }

    public void setCurrentMachineIndex(int value) {
        currentMachineIndex = value;
    }

    @Override
    public GrooveMachine getCurrentMachine() {
        return getMachines().get(currentMachineIndex);
    }

    public List<GrooveMachine> getMachines() {
        return Collections.unmodifiableList(grooveStation.getMachines());
    }

    public GrooveStationModel() {
    }

    @Override
    protected void construct() {
        grooveStation = new GrooveStation(getController());

        GrooveStationSetup setup = new GrooveStationSetup();
        configureSetup(setup);

        try {
            grooveStation.setup(setup);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    private void configureSetup(GrooveStationSetup setup) {
        configuration.setup(setup);
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {
        construct(); // don't call in onRegister(), app mediators have not been attatched
        trigger(new OnGrooveStationStartMachines());
    }

    @Override
    public void selectPart(int machineIndex, int partIndex) {
        GrooveMachine machine = getMachines().get(machineIndex);
        machine.getSound().setSelectedPart(partIndex);
        trigger(new OnGrooveStationModelChange(GrooveStationModelChangeKind.SelectedPart));
    }
}
