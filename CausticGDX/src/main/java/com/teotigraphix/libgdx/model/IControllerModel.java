
package com.teotigraphix.libgdx.model;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.controller.IDispatcher;

public interface IControllerModel extends IControllerComponent {
    IDispatcher getDispatcher();
}
