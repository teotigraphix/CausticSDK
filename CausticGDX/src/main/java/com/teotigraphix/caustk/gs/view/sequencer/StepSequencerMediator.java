
package com.teotigraphix.caustk.gs.view.sequencer;

import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer.OnMachineSequencerListener;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer.StepKeyboardMode;
import com.teotigraphix.caustk.gs.machine.part.MachineSound.OnMachineSoundListener;
import com.teotigraphix.caustk.gs.model.IGrooveStationModel;
import com.teotigraphix.caustk.gs.model.IGrooveStationModel.OnGrooveStationModelChange;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.ui.UI;
import com.teotigraphix.caustk.gs.ui.UIUtils;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerStepChange;
import com.teotigraphix.caustk.sequencer.track.Phrase;
import com.teotigraphix.caustk.sequencer.track.Trigger;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.caustk.StepKeyboard;
import com.teotigraphix.libgdx.ui.caustk.StepKeyboard.FunctionGroup;
import com.teotigraphix.libgdx.ui.caustk.StepKeyboard.OnStepKeyboardListener;

public abstract class StepSequencerMediator extends CaustkMediator {

    @Inject
    IGrooveStationModel grooveStationModel;

    private int index = 0;

    /**
     * Since the sequencer is abstract and shared, it will need an index to work
     * on the correct machine.
     */
    public int getMachineIndex() {
        return index;
    }

    private StepKeyboard stepKeyboard;

    public StepSequencerMediator() {
    }

    @Override
    public void onAttach() {
        super.onAttach();

        GrooveMachine machine = grooveStationModel.getMachine(getMachineIndex());
        machine.getSequencer().addOnMachineSequencerListener(new OnMachineSequencerListener() {
            @Override
            public void onBeatChange(MachineSequencer machineSequencer) {
            }

            @Override
            public void onModeChange(StepKeyboardMode mode, StepKeyboardMode oldMode) {
                stepKeyboard.setMode(mode);
            }

            @Override
            public void onRefresh() {
                refreshView();
            }
        });

        machine.getSound().addOnMachineSequencerListener(new OnMachineSoundListener() {
            @Override
            public void onSelectedPartChange(Part part, Part oldPart) {
            }
        });

        register(grooveStationModel, OnGrooveStationModelChange.class,
                new EventObserver<OnGrooveStationModelChange>() {
                    @Override
                    public void trigger(OnGrooveStationModelChange object) {
                        switch (object.getKind()) {
                            case SelectedPart:
                                refreshView();
                                break;
                        }
                    }
                });

        register(getController().getSystemSequencer(), OnSystemSequencerStepChange.class,
                new EventObserver<OnSystemSequencerStepChange>() {
                    @Override
                    public void trigger(OnSystemSequencerStepChange object) {
                        GrooveMachine machine = grooveStationModel.getMachine(getMachineIndex());
                        final int sixteenthStep = getController().getSystemSequencer()
                                .getCurrentSixteenthStep();
                        int position = machine.getSequencer().getSelectedPhrase().getPosition();
                        if (machine.getSequencer().getLocalMeasure() == position - 1)
                            stepKeyboard.setCurrentIndex(sixteenthStep);
                        else
                            stepKeyboard.setCurrentIndex(-1);
                    }
                });
    }

    protected abstract Array<FunctionGroup> createFunctions();

    @Override
    public void create(IScreen screen) {

        Table table = new Table();
        table.debug();
        table.align(Align.top);

        //        table = new Table();
        //        //table.debug();
        table.add().width(50f).expand(false, false);

        //---------------------------------------------

        //table.row();//.padTop(10f);

        Array<FunctionGroup> groups = createFunctions();
        stepKeyboard = new StepKeyboard(groups, screen.getSkin());
        stepKeyboard.setMode(grooveStationModel.getMachine(getMachineIndex()).getSequencer()
                .getMode());
        stepKeyboard.setOnStepKeyboardListener(new OnStepKeyboardListener() {
            @Override
            public void onStepChange(int index, boolean selected) {
                GrooveMachine machine = grooveStationModel.getMachine(getMachineIndex());
                // stepButton trigger on/off ONLY from user gesture
                // XXX this mediator is going to end up being abstract
                // so the index here will point to the currently showing machine index
                Phrase phrase = machine.getSequencer().getSelectedPhrase();
                int step = index + ((phrase.getPosition() - 1) * 16);
                if (selected) {
                    phrase.triggerOn(step);
                } else {
                    phrase.triggerOff(step);
                }
            }

            @Override
            public void onKeyUp(int index) {
                grooveStationModel.getCurrentMachine().getSound().noteOff(index - 3);
            }

            @Override
            public void onKeyDown(int index) {
                grooveStationModel.getCurrentMachine().getSound().noteOn(index - 3, 1f);
            }

            @Override
            public void onModeStateChange(StepKeyboardMode mode) {
                // this will callback to the sequencer to update its ui
                grooveStationModel.getMachine(getMachineIndex()).getSequencer().setMode(mode);
            }

        });

        grooveStationModel.getMachine(getMachineIndex()).getSequencer()
                .setMode(StepKeyboardMode.Step);

        stepKeyboard.validate();
        stepKeyboard.size(stepKeyboard.getPrefWidth(), stepKeyboard.getPrefHeight());
        table.add(stepKeyboard).colspan(3).align(Align.right);

        UIUtils.setBounds(table, UI.boundsStepSequencer);
        screen.getStage().addActor(table);
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);

        refreshView();
    }

    private void refreshView() {
        Phrase phrase = grooveStationModel.getMachine(getMachineIndex()).getSequencer()
                .getSelectedPhrase();
        Map<Integer, Trigger> map = phrase.getViewTriggerMap();
        int position = phrase.getPosition();
        int start = 16 * (position - 1);
        int top = 16 * position;
        int index = 0;
        for (int i = start; i < top; i++) {
            if (map.containsKey(i)) {
                if (map.get(i).isSelected())
                    stepKeyboard.select(index, true);
                else
                    stepKeyboard.select(index, false);
            } else {
                stepKeyboard.select(index, false);
            }
            index++;
        }
    }
}
