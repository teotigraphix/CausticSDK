
package com.teotigraphix.caustk.service;

import com.teotigraphix.caustk.controller.IControllerComponent;

public interface IInjectorService extends IControllerComponent {
    void inject(Object instance);
}
