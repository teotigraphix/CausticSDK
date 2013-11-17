
package com.teotigraphix.caustk.gs.model;

import com.teotigraphix.caustk.workstation.system.MasterSystemState;
import com.teotigraphix.caustk.workstation.system.SystemState;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface ISystemStateModel extends ICaustkModel {

    void setSelectedStateItem(int stateIndex, int stateItemIndex);

    int getSelectedStateIndex();

    int getSelectedStateItemIndex();

    SystemState[] getStates();

    MasterSystemState getSystemState();

    void setSystemState(MasterSystemState value);

    void incrementValue();

    void decrementValue();

    void update();
}
