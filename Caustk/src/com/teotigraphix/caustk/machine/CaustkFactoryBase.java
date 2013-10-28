
package com.teotigraphix.caustk.machine;

public abstract class CaustkFactoryBase {

    private CaustkLibraryFactory factory;

    public CaustkLibraryFactory getFactory() {
        return factory;
    }

    public void setFactory(CaustkLibraryFactory factory) {
        this.factory = factory;
    }

    public CaustkFactoryBase() {
    }

}
