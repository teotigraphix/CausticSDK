
package com.teotigraphix.caustk.controller.core;

import java.io.Serializable;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.IRestore;

public interface IRackComponent extends Serializable, IRestore {

    ICaustkController getController();

    void registerObservers();

    void unregisterObservers();

}
