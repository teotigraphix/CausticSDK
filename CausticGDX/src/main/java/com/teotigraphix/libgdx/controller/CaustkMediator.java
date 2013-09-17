////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

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
public abstract class CaustkMediator implements ICaustkMediator {

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

    public CaustkMediator() {
    }

    @Inject
    public void setApplicationController(IApplicationController applicationController) {
        this.applicationController = applicationController;
        controller = applicationController.getController();
        applicationController.registerMeditor(this);
    }

    @Override
    public void onRegister(IScreen screen) {
        registerObservers();
        onAttach();
    }

    /**
     * Register {@link ICaustkController#getDispatcher()} events.
     * <p>
     * Called once when the controller is set.
     */
    private void registerObservers() {
        register(getController(), OnProjectManagerChange.class,
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
        register(getController(), event, observer);
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

    //
    //    @Override
    //    public void onRegisterObservers() {
    //        registerObservers();
    //    }

    public void onAttach() {
    }

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
