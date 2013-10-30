
package com.teotigraphix.caustk.machine;

import com.teotigraphix.caustk.controller.core.CaustkFactory;

public abstract class CaustkFactoryBase {

    private CaustkFactory factory;

    public CaustkFactory getFactory() {
        return factory;
    }

    public void setFactory(CaustkFactory factory) {
        this.factory = factory;
    }

    public CaustkFactoryBase() {
    }

}
