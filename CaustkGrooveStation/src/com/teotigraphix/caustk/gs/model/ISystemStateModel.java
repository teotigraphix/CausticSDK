
package com.teotigraphix.caustk.gs.model;

import com.teotigraphix.libgdx.model.ICaustkModel;

public interface ISystemStateModel extends ICaustkModel {

    void setSelectedStateItem(int stateIndex, int stateItemIndex);

    int getSelectedStateIndex();

    int getSelectedStateItemIndex();

}
