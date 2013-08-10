
package com.teotigraphix.caustic.model;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustk.application.Dispatcher;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.Project;

public abstract class ModelBase implements ICaustkModel {

    private ICaustkController controller;

    private IDispatcher dispatcher;

    private IApplicationController applicationController;

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

    /**
     * The model's {@link IDispatcher} for local event dispatching.
     */
    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Triggers and event through this model's {@link #getDispatcher()}.
     * 
     * @param event The event to dispatch.
     */
    protected void trigger(Object event) {
        dispatcher.trigger(event);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    @Inject
    public void setApplicationController(IApplicationController applicationController) {
        this.applicationController = applicationController;
        this.controller = applicationController.getController();
        dispatcher = new Dispatcher();
        applicationController.registerModel(this);
    }

    public ModelBase() {
    }

    public final void setupState() {
        if (stateFactory == null)
            return;
        Project project = getController().getProjectManager().getProject();
        String data = project.getString(getStateKey());
        if (data != null) {
            state = getController().getSerializeService().fromString(data, stateFactory);
            initializeState(state);
        } else {
            try {
                state = stateFactory.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            configState(state);
        }
    }

    /**
     * Called after the {@link ICaustkModelState} has been deserialized.
     * 
     * @param state
     */
    protected void initializeState(ICaustkModelState state) {
    }

    /**
     * Called after the {@link ICaustkModelState} is created for the first time.
     * 
     * @param state
     */
    protected void configState(ICaustkModelState state) {
    }

    @Override
    public abstract void onShow();

    @Override
    public abstract void onRegister();

    @Override
    public final void save() {
        if (state == null)
            return;
        String data = getController().getSerializeService().toString(state);
        Project project = getController().getProjectManager().getProject();
        project.put(getStateKey(), data);
    }

}
