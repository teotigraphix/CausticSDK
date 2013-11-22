
package com.teotigraphix.caustk.gs.view;

import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveBox.OnGrooveBoxChange;
import com.teotigraphix.caustk.workstation.Part;
import com.teotigraphix.caustk.workstation.Pattern;
import com.teotigraphix.caustk.workstation.Pattern.OnPatternChange;
import com.teotigraphix.caustk.workstation.PatternBank.OnPatternBankChange;
import com.teotigraphix.caustk.workstation.Phrase;
import com.teotigraphix.caustk.workstation.Phrase.OnPhraseChange;
import com.teotigraphix.libgdx.controller.ScreenMediatorChild;

/**
 * Mediates one and only one groove box during a session.
 * <p>
 * Create a new instance of these mediators for each {@link GrooveBox} in an
 * application.
 * 
 * @author Michael Schmalle
 */
public class GrooveBoxMediatorChild extends ScreenMediatorChild {

    private GrooveBox grooveBox;

    public final GrooveBox getGrooveBox() {
        return grooveBox;
    }

    public void setGrooveBox(GrooveBox grooveBox) {
        this.grooveBox = grooveBox;
    }

    public final Pattern getSelectedPattern() {
        return getGrooveBox().getTemporaryPattern();
    }

    public final Part getSelectedPart() {
        return getGrooveBox().getSelectedPart();
    }

    public final Phrase getSelectedPhrase() {
        return getGrooveBox().getSelectedPhrase();
    }

    public GrooveBoxMediatorChild() {
    }

    public void onPhraseChange(OnPhraseChange object) {
        // TODO Auto-generated method stub

    }

    public void onPatternChange(OnPatternChange object) {
        // TODO Auto-generated method stub

    }

    public void onPatternBankChange(OnPatternBankChange object) {
        // TODO Auto-generated method stub

    }

    public void onGrooveBoxChange(OnGrooveBoxChange object) {
        // TODO Auto-generated method stub

    }

}
