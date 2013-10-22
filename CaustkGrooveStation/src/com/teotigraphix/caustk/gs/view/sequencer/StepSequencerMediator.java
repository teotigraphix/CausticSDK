
package com.teotigraphix.caustk.gs.view.sequencer;

import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.teotigraphix.caustk.gs.controller.IFunctionController;
import com.teotigraphix.caustk.gs.controller.IFunctionController.FunctionGroup;
import com.teotigraphix.caustk.gs.controller.IFunctionController.FunctionGroupItem;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer.OnMachineSequencerListener;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer.StepKeyboardMode;
import com.teotigraphix.caustk.gs.machine.part.MachineSound.OnMachineSoundListener;
import com.teotigraphix.caustk.gs.model.IGrooveStationModel;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.RhythmPart;
import com.teotigraphix.caustk.gs.view.screen.MachineMediatorBase;
import com.teotigraphix.caustk.rack.ISystemSequencer.OnSystemSequencerStepChange;
import com.teotigraphix.caustk.rack.track.Phrase;
import com.teotigraphix.caustk.rack.track.Trigger;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.caustk.StepKeyboard;
import com.teotigraphix.libgdx.ui.caustk.StepKeyboard.OnStepKeyboardListener;

public abstract class StepSequencerMediator extends MachineMediatorBase {

    @Inject
    IFunctionController functionController;

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
    public void onAttach(IScreen screen) {
        super.onAttach(screen);

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
        //
        //        machine.getSound().addOnMachineSequencerListener(new OnMachineSoundListener() {
        //            @Override
        //            public void onSelectedPartChange(Part part, Part oldPart) {
        //            }
        //
        //            @Override
        //            public void onPartSelectStateChange(PartSelectState state, PartSelectState oldState) {
        //            }
        //        });

        register(getMachine(), OnMachineSoundListener.class,
                new EventObserver<OnMachineSoundListener>() {
                    @Override
                    public void trigger(OnMachineSoundListener object) {
                        switch (object.getKind()) {
                            case SelectedPart:
                                refreshView();
                                break;
                            case RhythmChannel:
                                refreshView();
                                break;
                            case PartSelectState:
                                break;
                        }
                    }
                });

        register(getController(), OnSystemSequencerStepChange.class,
                new EventObserver<OnSystemSequencerStepChange>() {
                    @Override
                    public void trigger(OnSystemSequencerStepChange object) {
                        GrooveMachine machine = grooveStationModel.getMachine(getMachineIndex());
                        final int sixteenthStep = getController().getRack().getSystemSequencer()
                                .getCurrentSixteenthStep();
                        int position = machine.getSequencer().getSelectedPhrase().getPosition();

                        if (machine.getSequencer().getMode() == StepKeyboardMode.Step) {
                            if (machine.getSequencer().getLocalMeasure() == position - 1)
                                stepKeyboard.setCurrentIndex(sixteenthStep);
                            else
                                stepKeyboard.setCurrentIndex(-1);
                        }
                    }
                });
    }

    protected abstract Array<FunctionGroup> createFunctions();

    protected abstract Table createTable(IScreen screen);

    @Override
    public void onCreate(IScreen screen) {

        Table table = createTable(screen);
        table.align(Align.top);

        //        table = new Table();
        //        //table.debug();
        //        table.add().width(50f).expand(false, false);
        //
        //        //---------------------------------------------
        //
        //        //table.row();//.padTop(10f);
        //
        Array<FunctionGroup> functionGroups = createFunctions();
        functionController.setFunctionGroups(functionGroups);

        stepKeyboard = new StepKeyboard(functionGroups, screen.getSkin());
        stepKeyboard.setMode(grooveStationModel.getMachine(getMachineIndex()).getSequencer()
                .getMode());
        stepKeyboard.setOnStepKeyboardListener(new OnStepKeyboardListener() {
            @Override
            public void onStepChange(int index, boolean selected) {
                trigger(index, selected);
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

            @Override
            public void onFunctionDown(int index) {
                FunctionGroupItem item = stepKeyboard.getItem(index);
                getController().getLogger().view("StepSequencerMediator", "Func:" + item.getName());
                functionController.execute(item.getFunction());
            }

        });

        grooveStationModel.getMachine(getMachineIndex()).getSequencer()
                .setMode(StepKeyboardMode.Step);

        stepKeyboard.validate();
        stepKeyboard.size(stepKeyboard.getPrefWidth(), stepKeyboard.getPrefHeight());
        table.add(stepKeyboard).colspan(3).align(Align.right);

        table.add(stepKeyboard).fill().expand().padLeft(5f).padRight(5f).padBottom(5f);
    }

    protected void trigger(int index, boolean selected) {
        GrooveMachine machine = grooveStationModel.getMachine(getMachineIndex());
        Part selectedPart = machine.getSound().getSelectedPart();
        if (!selectedPart.isRhythm()) {
            triggerSynth(index, selected);
        } else {
            triggerRhythm(index, selected);
        }
    }

    private void triggerRhythm(int index, boolean selected) {
        GrooveMachine machine = grooveStationModel.getMachine(getMachineIndex());
        RhythmPart selectedPart = machine.getSound().getSelectedPart();
        int channel = selectedPart.getSelectedChannel();
        if (selected) {
            selectedPart.triggerOn(channel, index, 1f);
        } else {
            selectedPart.triggerOff(channel, index);
        }
    }

    private void triggerSynth(int index, boolean selected) {
        GrooveMachine machine = grooveStationModel.getMachine(getMachineIndex());
        // stepButton trigger on/off ONLY from user gesture
        // XXX this mediator is going to end up being abstract
        // so the index here will point to the currently showing machine index
        Phrase phrase = machine.getSequencer().getSelectedPhrase();
        int position = phrase.getPosition();
        int step = index + ((position - 1) * 16);
        if (selected) {
            phrase.triggerOn(step);
        } else {
            phrase.triggerOff(step);
        }
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);

        refreshView();
    }

    private void refreshView() {
        Part selectedPart = grooveStationModel.getCurrentMachine().getSound().getSelectedPart();
        if (selectedPart.isRhythm()) {
            refreshRhythmPart((RhythmPart)selectedPart);
        } else {
            refreshSynthPart(selectedPart);
        }
    }

    private void refreshSynthPart(Part selectedPart) {
        Phrase phrase = selectedPart.getPhrase();
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

    private void refreshRhythmPart(RhythmPart selectedPart) {
        Phrase phrase = selectedPart.getPhrase();
        Map<Integer, Trigger> map = phrase.getViewTriggerMap();

        int selectedChannel = selectedPart.getSelectedChannel();
        int pitch = selectedPart.toPitch(selectedChannel);

        int position = phrase.getPosition();
        int start = 16 * (position - 1);
        int top = 16 * position;
        int index = 0;
        for (int localStep = start; localStep < top; localStep++) {
            if (map.containsKey(localStep)) {
                Trigger trigger = map.get(localStep);
                if (trigger.hasNote(pitch) && trigger.isSelected(pitch))
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
