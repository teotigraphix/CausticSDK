
package com.teotigraphix.caustic.screen;

import com.teotigraphix.caustic.mediator.ICaustkMediator;

public interface IScreenManager extends ICaustkMediator {

    void addScreen(Class<? extends IScreenView> type);

    <T> void create(T root);

    void showPopUp(Class<? extends IScreenView> type);

    void hidePopUp(Class<? extends IScreenView> type);

}
