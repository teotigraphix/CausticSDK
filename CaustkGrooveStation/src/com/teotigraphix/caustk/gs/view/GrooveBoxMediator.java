
package com.teotigraphix.caustk.gs.view;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustk.gs.model.IGrooveStationModel;
import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveBox.OnGrooveBoxChange;
import com.teotigraphix.caustk.workstation.Part;
import com.teotigraphix.caustk.workstation.Pattern;
import com.teotigraphix.caustk.workstation.Pattern.OnPatternChange;
import com.teotigraphix.caustk.workstation.PatternBank.OnPatternBankChange;
import com.teotigraphix.caustk.workstation.Phrase;
import com.teotigraphix.caustk.workstation.Phrase.OnPhraseChange;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.controller.ScreenMediatorChild;
import com.teotigraphix.libgdx.screen.IScreen;

public class GrooveBoxMediator extends ScreenMediator {

    @Inject
    IGrooveStationModel grooveStationModel;

    private GrooveBox grooveBox;

    public final GrooveBox getGrooveBox() {
        return grooveBox;
    }

    public void setGrooveBox(GrooveBox grooveBox) {
        this.grooveBox = grooveBox;
    }

    public final Pattern getSelectedPattern() {
        return grooveBox.getTemporaryPattern();
    }

    public final Part getSelectedPart() {
        return grooveBox.getSelectedPart();
    }

    public final Phrase getSelectedPhrase() {
        return grooveBox.getSelectedPhrase();
    }

    public GrooveBoxMediator() {
    }

    @Override
    public void onInitialize(IScreen screen) {
        super.onInitialize(screen);
        grooveBox = grooveStationModel.getSelectedGrooveBox();
    }

    public void onGrooveBoxChange(OnGrooveBoxChange object) {
        for (ScreenMediatorChild subMediator : subMediators) {
            GrooveBoxMediatorChild child = (GrooveBoxMediatorChild)subMediator;
            child.onGrooveBoxChange(object);
        }
    }

    public void onPatternBankChange(OnPatternBankChange object) {
        for (ScreenMediatorChild subMediator : subMediators) {
            GrooveBoxMediatorChild child = (GrooveBoxMediatorChild)subMediator;
            child.onPatternBankChange(object);
        }
    }

    public void onPatternChange(OnPatternChange object) {
        for (ScreenMediatorChild subMediator : subMediators) {
            GrooveBoxMediatorChild child = (GrooveBoxMediatorChild)subMediator;
            child.onPatternChange(object);
        }
    }

    public void onPhraseChange(OnPhraseChange object) {
        for (ScreenMediatorChild subMediator : subMediators) {
            GrooveBoxMediatorChild child = (GrooveBoxMediatorChild)subMediator;
            child.onPhraseChange(object);
        }
    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);

        register(grooveBox.getDispatcher(), OnGrooveBoxChange.class,
                new EventObserver<OnGrooveBoxChange>() {
                    @Override
                    public void trigger(OnGrooveBoxChange object) {
                        onGrooveBoxChange(object);
                    }
                });

        register(grooveBox.getDispatcher(), OnPatternBankChange.class,
                new EventObserver<OnPatternBankChange>() {
                    @Override
                    public void trigger(OnPatternBankChange object) {
                        onPatternBankChange(object);
                    }
                });

        register(grooveBox.getDispatcher(), OnPatternChange.class,
                new EventObserver<OnPatternChange>() {
                    @Override
                    public void trigger(OnPatternChange object) {
                        onPatternChange(object);
                    }
                });

        // foreach part register OnPhraseChange
        for (Part part : grooveBox.getParts()) {
            register(part.getMachine().getDispatcher(), OnPhraseChange.class,
                    new EventObserver<OnPhraseChange>() {
                        @Override
                        public void trigger(OnPhraseChange object) {
                            onPhraseChange(object);
                        }
                    });
        }
    }

    @Override
    protected void addChild(ScreenMediatorChild child) {
        super.addChild(child);
        ((GrooveBoxMediatorChild)child).setGrooveBox(grooveBox);
    }
}
