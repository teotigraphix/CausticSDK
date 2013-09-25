
package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.IRestore;

public interface IRackComponent extends IRestore {

    ICaustkController getController();

    void setController(ICaustkController controller);

}
