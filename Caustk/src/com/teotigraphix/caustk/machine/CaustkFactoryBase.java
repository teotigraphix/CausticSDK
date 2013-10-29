
package com.teotigraphix.caustk.machine;

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
