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

package com.teotigraphix.libgdx.model;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.service.IInjectorService;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.libgdx.controller.IApplicationController;

public abstract class CaustkModel implements ICaustkModel {

    private ICaustkController controller;

    private IDispatcher dispatcher;

    private IApplicationController applicationController;

    @Override
    public boolean isInitialized() {
        return state != null && stateFactory != null;
    }

    private ICaustkModelState state;

    protected ICaustkModelState getState() {
        return state;
    }

    protected String getStateKey() {
        return getClass().getName() + "/state";
    }

    private Class<? extends ICaustkModelState> stateFactory;

    protected Class<? extends ICaustkModelState> getStateFactoryType() {
        return stateFactory;
    }

    public void setStateFactory(Class<? extends ICaustkModelState> value) {
        stateFactory = value;
    }

    protected final IApplicationController getApplicationController() {
        return applicationController;
    }

    protected final ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    @Inject
    public void setApplicationController(IApplicationController applicationController) {
        this.applicationController = applicationController;
        controller = applicationController.getController();
        dispatcher = new Dispatcher();
        applicationController.registerModel(this);
    }

    public CaustkModel() {
    }

    @Override
    public <T> void register(Class<T> event, EventObserver<T> observer) {
        dispatcher.register(event, observer);
    }

    @Override
    public void unregister(EventObserver<?> observer) {
        dispatcher.unregister(observer);
    }

    @Override
    public void trigger(Object event) {
        dispatcher.trigger(event);
    }

    @Override
    public void clear() {
        dispatcher.clear();
    }

    public final void setupState() {
        if (stateFactory == null)
            return;
        Project project = getController().getProjectManager().getProject();
        String data = project.getString(getStateKey());
        if (data != null) {
            state = getController().getSerializeService().fromStateString(data, stateFactory);
            inject(state);
            restoreState(state);
        } else {
            try {
                state = stateFactory.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            preconfigState(state);
            inject(state);
            configState(state);
        }
        initalizeState();
    }

    /**
     * Called before the state is injected.
     * 
     * @param state
     */
    protected void preconfigState(ICaustkModelState state) {
    }

    /**
     * Injects the state with the current inejctor.
     * 
     * @param state
     */
    private void inject(ICaustkModelState state) {
        IInjectorService injectorService = controller.getComponent(IInjectorService.class);
        injectorService.inject(state);
        if (state instanceof ISerialize)
            ((ISerialize)state).wakeup(controller);
    }

    protected void initalizeState() {
    }

    /**
     * Called after the {@link ICaustkModelState} has been deserialized.
     * 
     * @param state
     */
    protected void restoreState(ICaustkModelState state) {
    }

    /**
     * Called after the {@link ICaustkModelState} is created for the first time.
     * 
     * @param state
     */
    protected void configState(ICaustkModelState state) {
    }

    @Override
    public abstract void onRegister();

    @Override
    public abstract void onShow();

    @Override
    public final void save() {
        if (state == null)
            return;
        String data = getController().getSerializeService().toString(state);
        Project project = getController().getProjectManager().getProject();
        project.put(getStateKey(), data);
    }

}
