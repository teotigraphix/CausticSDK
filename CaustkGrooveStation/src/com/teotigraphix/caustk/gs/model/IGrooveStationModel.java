
package com.teotigraphix.caustk.gs.model;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveStation;

public interface IGrooveStationModel {

    GrooveBox getSelectedGrooveBox();

    void setGrooveStation(GrooveStation grooveStation);

    void write() throws CausticException;

}
