
package com.teotigraphix.caustk.library.vo;

import com.teotigraphix.caustic.core.IMemento;

/**
 * The {@link MixerPanelInfo} holds the state of a mixer scene that was saved
 * during a loaded caustic rack session.
 */
public class MixerPanelInfo extends MementoInfo {

    public MixerPanelInfo() {
    }

    public IMemento getMasterMemento() {
        return getMemento().getChild("master");
    }

}
