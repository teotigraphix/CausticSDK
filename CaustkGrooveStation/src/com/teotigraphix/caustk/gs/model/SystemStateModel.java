
package com.teotigraphix.caustk.gs.model;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.workstation.MasterSystem;
import com.teotigraphix.libgdx.model.CaustkModelBase;

/**
 * @author Michael Schmalle
 */
@Singleton
public class SystemStateModel extends CaustkModelBase implements ISystemStateModel {

    @Override
    public void setSelectedStateItem(int stateIndex, int stateItemIndex) {
        final MasterSystem masterSystem = getController().getRackSet().getSystem();
        masterSystem.setSelectedStateIndex(stateIndex, stateItemIndex);
    }

    @Override
    public int getSelectedStateIndex() {
        return getController().getRackSet().getSystem().getSelectedStateIndex();
    }

    @Override
    public int getSelectedStateItemIndex() {
        return getController().getRackSet().getSystem().getSelectedStateItemIndex();
    }

    public SystemStateModel() {
    }

}
