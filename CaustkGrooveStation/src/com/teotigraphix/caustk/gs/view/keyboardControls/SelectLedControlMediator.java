
package com.teotigraphix.caustk.gs.view.keyboardControls;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.tablelayout.Cell;
import com.teotigraphix.caustk.gs.view.GrooveBoxMediatorChild;
import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveBox.OnGrooveBoxChange;
import com.teotigraphix.caustk.workstation.Part;
import com.teotigraphix.caustk.workstation.Pattern;
import com.teotigraphix.caustk.workstation.Pattern.OnPatternChange;
import com.teotigraphix.caustk.workstation.PatternBank.OnPatternBankChange;
import com.teotigraphix.caustk.workstation.Phrase;
import com.teotigraphix.caustk.workstation.Phrase.OnPhraseChange;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.caustk.SelectLedControl;
import com.teotigraphix.libgdx.ui.caustk.SelectLedControl.OnSelectLedControlListener;

public class SelectLedControlMediator extends GrooveBoxMediatorChild {

    private SelectLedControl view;

    public SelectLedControlMediator() {
    }

    @Override
    public void onGrooveBoxChange(OnGrooveBoxChange object) {
        switch (object.getKind()) {
            case KeyboardMode:
                refreshView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPatternBankChange(OnPatternBankChange object) {
        switch (object.getKind()) {
            case PatternChange:
                final GrooveBox grooveBox = getGrooveBox();
                Pattern pattern = grooveBox.getTemporaryPattern();

                int bankIndex = pattern.getBankIndex();
                int patternIndex = pattern.getPatternIndex();

                for (Part part : grooveBox.getParts()) {
                    part.getMachine().setCurrentBankPattern(bankIndex, patternIndex);
                }

                refreshView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPatternChange(OnPatternChange object) {
        switch (object.getKind()) {
            case Length:
                refreshLedControls(getGrooveBox().getSelectedPhrase());
                break;
            case Octave:
                break;
            case SelectedPartIndex:
                refreshView();
                break;
            case Tempo:
                break;
        }
    }

    @Override
    public void onPhraseChange(OnPhraseChange object) {
        switch (object.getKind()) {
            case Beat:
                view.setTopIndex(getSelectedPhrase().getLocalMeasure());
                break;
            case Position:
                refreshView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(IScreen screen, Cell<Actor> parent) {
        parent.left().expandX().pad(5f);

        view = new SelectLedControl(screen.getSkin());
        view.setOnSelectLedControlListener(new OnSelectLedControlListener() {

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
                GrooveBox machine = getGrooveBox();
                switch (machine.getKeyboardMode()) {
                    case Key:
                        machine.getSelectedPart().setOctaveIndex(index);
                        break;

                    case Shift:
                        break;

                    case Step:
                        machine.getSelectedPhrase().setPosition(index + 1);
                        break;
                    case Off:
                        break;
                }
            }
        });
        view.setTopIndex(0);
        view.setBottomIndex(0);
        //selectLedControl.setMaxBottomIndex(0);

        parent.setWidget(view);
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);
        view.setTopIndex(0);
        refreshLedControls(getGrooveBox().getSelectedPhrase());
    }

    private void refreshLedControls(Phrase phrase) {
        view.setBottomIndex(phrase.getPosition() - 1);
        view.setMaxBottomIndex(phrase.getLength() - 1);
    }

    protected void refreshView() {
        final GrooveBox grooveBox = getGrooveBox();
        switch (grooveBox.getKeyboardMode()) {
            case Key:
                // set top index to 0
                // set bottom to octave index
                view.setMaxBottomIndex(7);
                // only reset to zero if the sequencer is not playing
                if (!getController().getRackSet().getSequencer().isPlaying())
                    view.setTopIndex(0);
                view.setBottomIndex(grooveBox.getSelectedPart().getOctaveIndex());
                break;
            case Off:
                break;
            case Shift:
                break;
            case Step:
                // set the max measure length
                // set the current edit measure
                //selectLedControl.setTopIndex(0);
                refreshLedControls(grooveBox.getSelectedPhrase());
                break;
        }
    }
}
