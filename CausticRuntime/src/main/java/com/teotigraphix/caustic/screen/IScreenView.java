
package com.teotigraphix.caustic.screen;

import com.teotigraphix.caustic.mediator.ICaustkMediator;

public interface IScreenView extends ICaustkMediator {
    
    IScreenRoot getScreenRoot();
    
    <T> void create(T root);

    /**
     * A screen root wraps the native ui component for an {@link IScreenView}.
     */
    public interface IScreenRoot {

        /**
         * Returns the root pane which in JavaFX is a Pane and Android is a
         * ViewParent.
         */
        <T> T getRoot();
        
        /**
         * Shows the pane.
         */
        void show();
        
        /**
         * Hides the pane.
         */
        void hide();

    }

}
