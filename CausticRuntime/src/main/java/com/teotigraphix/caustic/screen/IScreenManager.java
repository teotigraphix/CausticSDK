
package com.teotigraphix.caustic.screen;

public interface IScreenManager {

    void addScreen(Class<? extends IScreenView> type);

    <T> void create(T root);

    void showPopUp(Class<? extends IScreenView> type);

    void hidePopUp(Class<? extends IScreenView> type);

    void preinitialize();

}
