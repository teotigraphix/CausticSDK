
package com.teotigraphix.caustk.gs.model;

import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gs.config.IGrooveStationConfiguration;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.GrooveStation;
import com.teotigraphix.caustk.gs.machine.GrooveStation.GrooveStationSetup;
import com.teotigraphix.libgdx.model.CaustkModelBase;

public class GrooveStationModel extends CaustkModelBase implements IGrooveStationModel {

    @Inject
    IGrooveStationConfiguration configuration;

    @Override
    public IGrooveStationConfiguration getConfiguration() {
        return configuration;
    }

    //----------------------------------
    // grooveStation
    //----------------------------------

    private GrooveStation grooveStation;

    @Override
    public GrooveMachine getMachine(int machineIndex) {
        return getMachines().get(machineIndex);
    }

    //----------------------------------
    // currentMachineIndex
    //----------------------------------

    private int currentMachineIndex;

    public int getCurrentMachineIndex() {
        return currentMachineIndex;
    }

    public void setCurrentMachineIndex(int value) {
        currentMachineIndex = value;
    }

    //----------------------------------
    // currentMachine
    //----------------------------------

    @Override
    public GrooveMachine getCurrentMachine() {
        return getMachines().get(currentMachineIndex);
    }

    //----------------------------------
    // machines
    //----------------------------------

    public List<GrooveMachine> getMachines() {
        return Collections.unmodifiableList(grooveStation.getMachines());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public GrooveStationModel() {
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void onRegister() {
        getController().addComponent(IGrooveStationModel.class, this);
    }

    //--------------------------------------------------------------------------
    // IGrooveStationModel API :: Methods
    //--------------------------------------------------------------------------
    //
    //    @Override
    //    public void selectPart(int machineIndex, int partIndex) {
    //        final GrooveMachine machine = getMachines().get(machineIndex);
    //        if (!machine.getSound().isSelectedPart(partIndex)) {
    //            machine.getSound().setSelectedPart(partIndex);
    //            trigger(new OnGrooveStationModelChange(GrooveStationModelChangeKind.SelectedPart));
    //        }
    //    }
    //
    //    @Override
    //    public void selectRhythmPart(int machineIndex, int partIndex, int channelIndex) {
    //        final GrooveMachine machine = getMachines().get(machineIndex);
    //
    //        selectPart(machineIndex, partIndex);
    //
    //        RhythmPart selectedPart = machine.getSound().getSelectedPart();
    //        selectedPart.setSelectedChannel(channelIndex);
    //        trigger(new OnGrooveStationModelChange(GrooveStationModelChangeKind.RhythmChannel));
    //    }

    @Override
    public void create() {
        grooveStation = new GrooveStation(getController());

        GrooveStationSetup setup = new GrooveStationSetup();
        configureSetup(setup);

        try {
            grooveStation.setup(setup);
        } catch (CausticException e) {
            e.printStackTrace();
        }

        trigger(new OnGrooveStationStartMachines());
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    private void configureSetup(GrooveStationSetup setup) {
        configuration.setup(setup);
    }

}
