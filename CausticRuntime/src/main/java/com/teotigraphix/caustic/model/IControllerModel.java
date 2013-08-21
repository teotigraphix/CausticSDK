
package com.teotigraphix.caustic.model;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.IControllerComponent;

public interface IControllerModel extends IControllerComponent {
    IDispatcher getDispatcher();
}
