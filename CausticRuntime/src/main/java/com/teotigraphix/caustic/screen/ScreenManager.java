
package com.teotigraphix.caustic.screen;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.MediatorBase;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;

/*

The main appp is composed of a Screen view stack.

This manager is responsible for managing the layers creation, visibility, destruction.

*/

@Singleton
public class ScreenManager extends MediatorBase implements IScreenManager {

    Map<Class<? extends IScreenView>, IScreenView> stack = new HashMap<Class<? extends IScreenView>, IScreenView>();

    @Inject
    Injector injector;

    @Inject
    public ScreenManager(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public <T> void create(T root) {
        for (IScreenView view : stack.values()) {
            view.create(root);
        }
    }

    @Override
    public void preinitialize() {
        super.preinitialize();
        for (IScreenView view : stack.values()) {
            view.preinitialize();
        }
    }

    @Override
    public void addScreen(Class<? extends IScreenView> type) {
        IScreenView instance = injector.getInstance(type);
        stack.put(type, instance);
    }

    @Override
    public void showPopUp(Class<? extends IScreenView> type) {
        IScreenView view = stack.get(type);
        view.getScreenRoot().show();
    }

    @Override
    public void hidePopUp(Class<? extends IScreenView> type) {
        IScreenView view = stack.get(type);
        view.getScreenRoot().hide();
    }
}
