
package com.teotigraphix.caustic.mediator;

public interface ICaustkMediator {

    /**
     * Called before the application controller has it's start() invoked.
     */
    void onRegisterObservers();

    void onRegister();

}
