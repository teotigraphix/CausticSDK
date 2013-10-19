
package com.teotigraphix.caustk.gs.view.sequencer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer.OnMachineSequencerListener;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer.StepKeyboardMode;
import com.teotigraphix.caustk.gs.machine.part.MachineSound.OnMachineSoundListener;
import com.teotigraphix.caustk.gs.machine.part.MachineSound.PartSelectState;
import com.teotigraphix.caustk.gs.model.IGrooveStationModel;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.caustk.SelectLedControl;
import com.teotigraphix.libgdx.ui.caustk.SelectLedControl.OnSelectLedControlListener;

public abstract class StepSequencerControlsMediator extends ScreenMediator {

    @Inject
    IGrooveStationModel grooveStationModel;

    private SelectLedControl selectLedControl;

    private WidgetGroup keyboardToolsGroup;

    private int machineIndex = 0;

    private TextButton shiftToggle;

    private Button keyBoardToggle;

    /**
     * Since the sequencer is abstract and shared, it will need an index to work
     * on the correct machine.
     */
    public int getMachineIndex() {
        return machineIndex;
    }

    public StepSequencerControlsMediator() {
    }

    protected abstract Table createTable(IScreen screen);

    @Override
    public void onRegister() {
        GrooveMachine machine = grooveStationModel.getMachine(getMachineIndex());
        machine.getSequencer().addOnMachineSequencerListener(new OnMachineSequencerListener() {
            @Override
            public void onBeatChange(MachineSequencer machineSequencer) {
                selectLedControl.setTopIndex(machineSequencer.getLocalMeasure());
            }

            @Override
            public void onModeChange(StepKeyboardMode mode, StepKeyboardMode oldMode) {
                refreshView();
            }

            @Override
            public void onRefresh() {
            }
        });

        machine.getSound().addOnMachineSequencerListener(new OnMachineSoundListener() {
            @Override
            public void onSelectedPartChange(Part part, Part oldPart) {
                refreshView();
            }

            @Override
            public void onPartSelectStateChange(PartSelectState state, PartSelectState oldState) {

            }
        });
    }

    protected void refreshView() {
        GrooveMachine machine = grooveStationModel.getCurrentMachine();
        switch (machine.getSequencer().getMode()) {
            case Step:
                // set the max measure length
                // set the current edit measure
                selectLedControl.setTopIndex(0);
                selectLedControl.setBottomIndex(machine.getSequencer().getSelectedPhrase()
                        .getPosition() - 1);
                selectLedControl.setMaxBottomIndex(machine.getSequencer().getSelectedPhrase()
                        .getLength());
                break;

            case Key:
                // set top index to 0
                // set bottom to octave index
                selectLedControl.setMaxBottomIndex(7);
                // only reset to zero if the sequencer is not playing
                if (!getController().getRack().getSystemSequencer().isPlaying())
                    selectLedControl.setTopIndex(0);
                selectLedControl.setBottomIndex(machine.getSound().getOctaveIndex());
                break;

            case Shift:
                break;
        }
    }

    @Override
    public void onCreate(IScreen screen) {
        Table table = createTable(screen);
        //table.debug();
        table.align(Align.top);

        // table.add().width(50f).expand(false, false);

        shiftToggle = new TextButton("SHIFT", screen.getSkin());
        shiftToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button button = (Button)actor;
                if (button.isChecked()) {
                    fireModeChange(StepKeyboardMode.Shift);
                } else {
                    if (keyBoardToggle.isChecked()) {
                        fireModeChange(StepKeyboardMode.Key);
                    } else {
                        fireModeChange(StepKeyboardMode.Step);
                    }
                }
            }
        });
        table.add(shiftToggle).minSize(10f).pad(5f);

        //
        keyBoardToggle = new TextButton("KEYBOARD", screen.getSkin());
        keyBoardToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (shiftToggle.isChecked()) {
                    event.cancel();
                    return;
                }
                Button button = (Button)actor;
                if (button.isChecked()) {
                    fireModeChange(StepKeyboardMode.Key);
                } else {
                    if (shiftToggle.isChecked()) {
                        fireModeChange(StepKeyboardMode.Shift);
                    } else {
                        fireModeChange(StepKeyboardMode.Step);
                    }
                }
            }
        });

        table.add(keyBoardToggle).minSize(10f).pad(5f);

        //
        selectLedControl = new SelectLedControl(screen.getSkin());
        selectLedControl.setOnSelectLedControlListener(new OnSelectLedControlListener() {

            @Override
            public void onIncrement(int index) {
            }

            @Override
            public void onDecrement(int index) {
            }

            @Override
            public void onTopIndexChange(int index) {
            }

            @Override
            public void onBottomIndexChange(int index) {
                GrooveMachine machine = grooveStationModel.getCurrentMachine();
                switch (machine.getSequencer().getMode()) {
                    case Key:
                        machine.getSound().setOctaveIndex(index);
                        break;

                    case Shift:
                        break;

                    case Step:
                        grooveStationModel.getMachine(getMachineIndex()).getSequencer()
                                .getSelectedPhrase().setPosition(index + 1);
                        machine.getSequencer().refresh();
                        break;
                }
            }
        });
        selectLedControl.setTopIndex(0);
        selectLedControl.setBottomIndex(0);
        selectLedControl.setMaxBottomIndex(3);

        table.add(selectLedControl).left().expandX().pad(5f);

        //

        keyboardToolsGroup = createToolGroup(screen.getSkin());
        table.add(keyboardToolsGroup).right();
    }

    private void fireModeChange(StepKeyboardMode mode) {
        //onStepKeyboardListener.onModeStateChange(mode);
    }

    protected abstract WidgetGroup createToolGroup(Skin skin);

}
