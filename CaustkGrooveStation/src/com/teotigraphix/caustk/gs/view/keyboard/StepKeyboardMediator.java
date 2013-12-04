
package com.teotigraphix.caustk.gs.view.keyboard;

import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.tablelayout.Cell;
import com.google.inject.Inject;
import com.teotigraphix.caustk.core.midi.MidiReference;
import com.teotigraphix.caustk.gs.controller.IFunctionController;
import com.teotigraphix.caustk.gs.model.ISystemStateModel;
import com.teotigraphix.caustk.gs.view.GrooveBoxMediatorChild;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.ui.StepKeyboard;
import com.teotigraphix.caustk.ui.StepKeyboard.OnStepKeyboardListener;
import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveBox.KeyboardMode;
import com.teotigraphix.caustk.workstation.GrooveBox.OnGrooveBoxChange;
import com.teotigraphix.caustk.workstation.Part;
import com.teotigraphix.caustk.workstation.Pattern.OnPatternChange;
import com.teotigraphix.caustk.workstation.PatternBank.OnPatternBankChange;
import com.teotigraphix.caustk.workstation.Phrase;
import com.teotigraphix.caustk.workstation.Phrase.OnPhraseChange;
import com.teotigraphix.caustk.workstation.PhraseUtils;
import com.teotigraphix.caustk.workstation.RhythmPart;
import com.teotigraphix.caustk.workstation.Trigger;
import com.teotigraphix.libgdx.screen.IScreen;

public class StepKeyboardMediator extends GrooveBoxMediatorChild {

    @Inject
    ISystemStateModel systemStateModel;

    @Inject
    IFunctionController functionController;

    private StepKeyboard stepKeyboard;

    @Override
    public void onPatternChange(OnPatternChange object) {
        switch (object.getKind()) {
            case SelectedPartIndex:
                refreshView();
                updateEditSelection();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPatternBankChange(OnPatternBankChange object) {
        switch (object.getKind()) {
            case PatternChange:
                refreshView();
                updateEditSelection();
                break;
            default:
                break;
        }
    }

    @Override
    public void onGrooveBoxChange(OnGrooveBoxChange object) {
        switch (object.getKind()) {
            case KeyboardMode:
                stepKeyboard.setMode(object.getGrooveBox().getKeyboardMode());
                break;
            case RhythmChannel:
                refreshView();
                updateEditSelection();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPhraseChange(OnPhraseChange object) {
        switch (object.getKind()) {
            case Step:
                final int sixteenthStep = getSelectedPhrase().getCurrentSixteenthStep();
                int position = getSelectedPhrase().getPosition();

                if (getGrooveBox().getKeyboardMode() == KeyboardMode.Step) {
                    if (getSelectedPhrase().getLocalMeasure() == position - 1)
                        stepKeyboard.setCurrentStepIndex(sixteenthStep);
                    else
                        stepKeyboard.setCurrentStepIndex(-1);
                }
                break;
            case Position:
                refreshView();
                updateEditSelection();
                break;
            case EditSelection:
                updateEditSelection();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(IScreen screen, Cell<Actor> parent) {
        parent.fill().expand().padLeft(5f).padRight(5f).padBottom(5f).padTop(5f);

        stepKeyboard = new StepKeyboard(screen.getSkin());
        stepKeyboard.setFunctionGroups(functionController.getFunctionGroups());
        stepKeyboard.setMode(KeyboardMode.Step);
        stepKeyboard.setOnStepKeyboardListener(new OnStepKeyboardListener() {

            @Override
            public boolean onStepDown(int index) {
                // we set the global step here, the callback will clear
                // the selection but if the keyboard is in the correct measure
                // the selected button will be activated
                Phrase phrase = getSelectedPhrase();
                float beat = PhraseUtils.getGlobalBeatFromLocalStep(index, phrase);
                phrase.setSelectedBeat(beat);
                //stepKeyboard.setSelectedStep(index);
                return false;
            }

            @Override
            public boolean onStepChange(int index, boolean selected) {
                // only allow trigger selection if in Pattern mode
                if (systemStateModel.getSelectedStateIndex() == 0
                        && systemStateModel.getSelectedStateItemIndex() == 0) {
                    trigger(index, selected);
                    return false;
                }
                return true;
            }

            @Override
            public void onModeStateChange(KeyboardMode mode) {
                // this will callback to the sequencer to update its ui
                getGrooveBox().setKeyboardMode(mode);
            }

            @Override
            public void onKeyUp(int index) {
                getGrooveBox().noteOff(index - 3);
                //                messageModel.clearStatus();
            }

            @Override
            public void onKeyDown(int index) {
                getGrooveBox().noteOn(index - 3, 1f);
                int pitch = getGrooveBox().getSelectedPart().toPitch(index - 3);
                @SuppressWarnings("unused")
                String noteName = MidiReference.getInstance().getNoteName(pitch, true);
                //                messageModel.setStatus(noteName);
            }

            @Override
            public void onFunctionDown(int index) {
                // FunctionGroupItem item = stepKeyboard.getItem(index);
                // getController().getLogger().view("StepSequencerMediator", "Func:" + item.getName());
                // functionController.execute(item.getFunction());
            }

        });

        getGrooveBox().setKeyboardMode(KeyboardMode.Step);

        parent.setWidget(stepKeyboard);
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);

        refreshView();
        updateEditSelection();
    }

    protected void updateEditSelection() {
        Phrase phrase = getSelectedPhrase();
        Trigger selected = phrase.getEditSelection();
        if (selected == null) {
            stepKeyboard.setSelectedStep(-1);
            return;
        }

        int measure = PhraseUtils.toLocalMeasure(selected.getBeat(), phrase.getLength());
        int position = phrase.getPosition();
        if (measure != position - 1) {// measure is 0 index
            stepKeyboard.setSelectedStep(-1);
            return;
        }

        float localBeat = PhraseUtils.toMeasureBeat(selected.getBeat());
        //System.out.println(localBeat);
        stepKeyboard.setSelectedStep(Resolution.toStep(localBeat, phrase.getResolution()));
    }

    protected void trigger(int index, boolean selected) {
        GrooveBox machine = getGrooveBox();
        Part selectedPart = machine.getSelectedPart();
        if (!selectedPart.isRhythm()) {
            triggerSynth(index, selected);
        } else {
            triggerRhythm(index, selected);
        }
    }

    private void triggerRhythm(int index, boolean selected) {
        GrooveBox machine = getGrooveBox();
        RhythmPart selectedPart = (RhythmPart)machine.getSelectedPart();
        int channel = selectedPart.getSelectedChannel();
        Phrase phrase = machine.getSelectedPhrase();
        int position = phrase.getPosition();
        int step = index + ((position - 1) * 16);
        if (selected) {
            selectedPart.triggerOn(channel, step, 1f);
        } else {
            selectedPart.triggerOff(channel, step);
        }
    }

    private void triggerSynth(int index, boolean selected) {
        GrooveBox machine = getGrooveBox();
        // stepButton trigger on/off ONLY from user gesture
        // XXX this mediator is going to end up being abstract
        // so the index here will point to the currently showing machine index
        Phrase phrase = machine.getSelectedPhrase();
        int position = phrase.getPosition();
        int step = index + ((position - 1) * 16);
        if (selected) {
            phrase.triggerOn(step);
        } else {
            phrase.triggerOff(step);
        }
    }

    private void refreshView() {
        Part selectedPart = getSelectedPart();
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
