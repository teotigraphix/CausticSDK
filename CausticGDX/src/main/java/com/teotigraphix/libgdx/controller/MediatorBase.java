
package com.teotigraphix.libgdx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.libgdx.screen.IScreen;

// Mediators never dispatch events!, only listen and act with logic
// that could eventually be put in a Command
public abstract class MediatorBase implements ICaustkMediator {

    private ICaustkController controller;

    private IApplicationController applicationController;

    public final IApplicationController getApplicationController() {
        return applicationController;
    }

    public final ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MediatorBase() {
    }

    @Inject
    public void setApplicationController(IApplicationController applicationController) {
        this.applicationController = applicationController;
        controller = applicationController.getController();
        applicationController.registerMeditor(this);
    }

    /**
     * Register {@link ICaustkController#getDispatcher()} events.
     * <p>
     * Called once when the controller is set.
     */
    protected void registerObservers() {
        register(getController().getDispatcher(), OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {

                        switch (object.getKind()) {
                            case CREATE:
                                onProjectCreate();
                                break;
                            case CLOSE_COMPLETE:
                                onProjectCloseComplete();
                                break;
                            case EXIT:
                                onProjectExit();
                                break;
                            case LOAD:
                                onProjectLoad();
                                break;
                            case LOAD_COMPLETE:
                                onProjectLoadComplete();
                                break;
                            case SAVE:
                                onProjectSave();
                                break;
                            case SAVE_COMPLETE:
                                onProjectSaveComplete();
                                break;
                        }
                    }
                });
    }

    protected void onProjectSaveComplete() {
        // TODO Auto-generated method stub

    }

    protected void onProjectExit() {
        // TODO Auto-generated method stub

    }

    protected void onProjectCloseComplete() {
        // TODO Auto-generated method stub

    }

    protected void onProjectLoadComplete() {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("rawtypes")
    private Map<IDispatcher, List<EventObserver>> observers = new HashMap<IDispatcher, List<EventObserver>>();

    /**
     * Registers an event on the {@link ICaustkController#getDispatcher()}.
     * 
     * @param event
     * @param observer
     */
    protected <T> void register(Class<T> event, EventObserver<T> observer) {
        register(getController().getDispatcher(), event, observer);
    }

    /**
     * Registers an event on the passed {@link IDispatcher}.
     * 
     * @param dispatcher
     * @param event
     * @param observer
     */
    @SuppressWarnings("rawtypes")
    protected <T> void register(IDispatcher dispatcher, Class<T> event, EventObserver<T> observer) {
        List<EventObserver> list = observers.get(dispatcher);
        if (list == null) {
            list = new ArrayList<EventObserver>();
            observers.put(dispatcher, list);
        }
        dispatcher.register(event, observer);
    }

    @Override
    public void onRegisterObservers() {
        registerObservers();
    }

    /**
     * @see OnMediatorRegister
     */
    @Override
    public abstract void onRegister();

    @SuppressWarnings("rawtypes")
    @Override
    public void dispose() {
        for (Entry<IDispatcher, List<EventObserver>> entry : observers.entrySet()) {
            IDispatcher dispatcher = entry.getKey();
            for (EventObserver observer : entry.getValue()) {
                dispatcher.unregister(observer);
            }
        }
    }

    protected void onProjectSave() {
    }

    protected void onProjectLoad() {
    }

    protected void onProjectCreate() {
    }

    @Override
    public void create(IScreen screen) {
    }

    @Override
    public void onShow(IScreen screen) {
    }
}
